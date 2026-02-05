// 自动生成的 MenuBarConfig 组件
ood.Class("ooder.MenuBarConfig", "ood.Module", {
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
        "currComponentAlias": "MenuBarConfig",
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

            // MenuBarConfigMain
            var MenuBarConfigMain = ood.create("ood.UI.Block")
                .setHost(host, "MenuBarConfigMain")
                .setName("MenuBarConfigMain")
                .setDock("fill")
                .setPanelBgClr("transparent")
                .setTabindex(1)
            ;
            append(MenuBarConfigMain);

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
                .setMethodName("saveMenuBarInfo")
                .setProxyType("auto")
                .setQueryAsync(true)
                .setQueryMethod("POST")
                .setQueryURL("/dsm/view/config/menu/menubar/saveMenuBarInfo")
                .setRequestDataSource([{"name":"menuConfigView","path":"","type":"FORM"},{"name":"projectName","path":"","type":"FORM"},{"name":"FieldMenuClass","path":"","type":"FORM"},{"name":"PAGECTX","path":"","type":"FORM"},{"name":"menuConfigView","path":"","type":"FORM"},{"name":"projectName","path":"","type":"FORM"},{"name":"MenuBarConfigMain","path":"","type":"FORM"},{"name":"PAGECTX","path":"","type":"FORM"}])
                .setRequestType("JSON")
                .setResponseCallback([])
                .setResponseDataTarget([])
                .setResponseType("JSON")
                .setSourceClassName("net.ooder.dsm.view.config.menu.menubar.MenuBarConfigService")
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
                .setMethodName("getMenuBarConfig")
                .setProxyType("auto")
                .setQueryAsync(true)
                .setQueryMethod("POST")
                .setQueryURL("/dsm/view/config/menu/menubar/MenuBarConfig")
                .setRequestDataSource([{"name":"MenuBarConfigMain","path":"","type":"FORM"},{"name":"PAGECTX","path":"","type":"FORM"}])
                .setRequestType("FORM")
                .setResponseCallback([])
                .setResponseDataTarget([{"name":"MenuBarConfigMain","path":"data","type":"FORM"},{"name":"PAGECTX","path":"data","type":"FORM"}])
                .setResponseType("JSON")
                .setSourceClassName("net.ooder.dsm.view.config.menu.menubar.MenuBarNav")
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

            // parentId
            var parentId = ood.create("ood.UI.HiddenInput")
                .setHost(PAGECTX, "parentId")
                .setName("parentId")
                .setFormField(true)
                .setIsPid(true)
                .setPid(true)
                .setTabindex(7)
            ;
            PAGECTX.append(parentId);

            // id
            var id = ood.create("ood.UI.HiddenInput")
                .setHost(PAGECTX, "id")
                .setName("id")
                .setFormField(true)
                .setIsPid(true)
                .setPid(true)
                .setTabindex(8)
            ;
            PAGECTX.append(id);

            // index
            var index = ood.create("ood.UI.HiddenInput")
                .setHost(PAGECTX, "index")
                .setName("index")
                .setFormField(true)
                .setIsPid(false)
                .setPid(false)
                .setTabindex(9)
            ;
            PAGECTX.append(index);

            // iconColors
            var iconColors = ood.create("ood.UI.HiddenInput")
                .setHost(PAGECTX, "iconColors")
                .setName("iconColors")
                .setFormField(true)
                .setIsPid(false)
                .setPid(false)
                .setTabindex(10)
            ;
            PAGECTX.append(iconColors);

            // itemColors
            var itemColors = ood.create("ood.UI.HiddenInput")
                .setHost(PAGECTX, "itemColors")
                .setName("itemColors")
                .setFormField(true)
                .setIsPid(false)
                .setPid(false)
                .setTabindex(11)
            ;
            PAGECTX.append(itemColors);

            // fontColors
            var fontColors = ood.create("ood.UI.HiddenInput")
                .setHost(PAGECTX, "fontColors")
                .setName("fontColors")
                .setFormField(true)
                .setIsPid(false)
                .setPid(false)
                .setTabindex(12)
            ;
            PAGECTX.append(fontColors);

            // MenuBarConfig
            var MenuBarConfig = ood.create("ood.UI.FormLayout")
                .setHost(MenuBarConfigMain, "MenuBarConfig")
                .setName("MenuBarConfig")
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
                .setLayoutData({"cells":{"C2":{"cellName":"C2","style":{"textAlign":"center"},"value":"菜单位置"},"A1":{"cellName":"A1","style":{"textAlign":"center"},"value":"显示名称"},"C3":{"cellName":"C3","style":{"textAlign":"center"},"value":"图标"},"A2":{"cellName":"A2","style":{"textAlign":"center"},"value":"菜单类型"},"C4":{"cellName":"C4","style":{"textAlign":"center"},"value":"垂直对齐"},"A3":{"cellName":"A3","style":{"textAlign":"center"},"value":"工具栏布局"},"C5":{"cellName":"C5","style":{"textAlign":"center"},"value":"行头手柄"},"A4":{"cellName":"A4","style":{"textAlign":"center"},"value":"横向对齐"},"C6":{"cellName":"C6","style":{"textAlign":"center"},"value":"延迟加载"},"A5":{"cellName":"A5","style":{"textAlign":"center"},"value":"显示标题"},"C7":{"cellName":"C7","style":{"textAlign":"center"},"value":"菜单项边距"},"A6":{"cellName":"A6","style":{"textAlign":"center"},"value":"动态加载"},"C8":{"cellName":"C8","style":{"textAlign":"center"},"value":"菜单项宽度"},"A7":{"cellName":"A7","style":{"textAlign":"center"},"value":"上边距"},"C9":{"cellName":"C9","style":{"textAlign":"center"},"value":"菜单项类型"},"A8":{"cellName":"A8","style":{"textAlign":"center"},"value":"菜单项间隔"},"A9":{"cellName":"A9","style":{"textAlign":"center"},"value":"菜单项位置"},"A10":{"cellName":"A10","style":{"textAlign":"center"},"value":"彩色图标"},"A11":{"cellName":"A11","style":{"textAlign":"center"},"value":"彩色字体"},"C11":{"cellName":"C11","style":{"textAlign":"center"},"value":"自动连接"},"A12":{"cellName":"A12","style":{"textAlign":"center"},"value":"工具栏高"},"C10":{"cellName":"C10","style":{"textAlign":"center"},"value":"彩色选项"},"A13":{"cellName":"A13","style":{"textAlign":"center"},"value":"高度"},"C13":{"cellName":"C13","style":{"textAlign":"center"},"value":"工具栏高"},"A14":{"cellName":"A14","style":{"textAlign":"center"},"value":"边框"},"C12":{"cellName":"C12","style":{"textAlign":"center"},"value":"宽度"},"A15":{"cellName":"A15","style":{"textAlign":"center"},"value":"按钮位置"},"C15":{"cellName":"C15","style":{"textAlign":"center"},"value":"menus"},"A16":{"cellName":"A16","style":{"textAlign":"center"},"value":"menuClasses"},"C14":{"cellName":"C14","style":{"textAlign":"center"},"value":"工具栏边框"},"A17":{"cellName":"A17","style":{"textAlign":"center"},"value":"实现类"}},"colSetting":{"A":{"manualWidth":150},"B":{"manualWidth":150},"C":{"manualWidth":150},"D":{"manualWidth":150}},"cols":4,"merged":[{"col":1,"colspan":3,"removed":false,"row":0,"rowspan":1},{"col":1,"colspan":3,"removed":false,"row":16,"rowspan":1}],"rowSetting":{"1":{"manualHeight":35},"2":{"manualHeight":35},"3":{"manualHeight":35},"4":{"manualHeight":35},"5":{"manualHeight":35},"6":{"manualHeight":35},"7":{"manualHeight":35},"8":{"manualHeight":35},"9":{"manualHeight":35},"10":{"manualHeight":35},"11":{"manualHeight":35},"12":{"manualHeight":35},"13":{"manualHeight":35},"14":{"manualHeight":35},"15":{"manualHeight":35},"16":{"manualHeight":35},"17":{"manualHeight":35}},"rows":17})
                .setLineSpacing(10)
                .setMode("write")
                .setSolidGridlines(true)
                .setStretchH("all")
                .setStretchHeight("none")
                .setTabindex(0)
                .setTagVar({})
                .setVisibility("visible")
            ;
            MenuBarConfigMain.append(MenuBarConfig);

            // caption
            var caption = ood.create("ood.UI.Input")
                .setHost(MenuBarConfig, "caption")
                .setName("caption")
                .setCaption("显示名称")
                .setDefaultFocus(false)
                .setDesc("显示名称")
                .setDisableClickEffect(false)
                .setDisableHoverEffect(false)
                .setDisableTips(false)
                .setDisabled(false)
                .setDock("none")
                .setEvents({})
                .setImageClass("")
                .setItems([])
                .setLabelCaption("显示名称")
                .setLabelPos("left")
                .setLabelSize("6.0em")
                .setReadonly(false)
                .setTabindex(0)
                .setTagVar({})
                .setVisibility("visible")
            ;
            MenuBarConfig.append(caption);

            // menuType
            var menuType = ood.create("ood.UI.ComboInput")
                .setHost(MenuBarConfig, "menuType")
                .setName("menuType")
                .setDefaultFocus(false)
                .setDesc("菜单类型")
                .setDisableClickEffect(false)
                .setDisableHoverEffect(false)
                .setDisableTips(false)
                .setDisabled(false)
                .setDock("none")
                .setDynLoad(true)
                .setEnumClass("net.ooder.esd.annotation.menu.CustomMenuType")
                .setItems([{"bindClass":[],"caption":"工具栏","id":"TOOLBAR","imageClass":"ri-tools-line","name":"工具栏","tabindex":0,"tagVar":{"name":"TOOLBAR","clazz":"net.ooder.esd.annotation.menu.CustomMenuType"},"title":"工具栏"},{"bindClass":[],"caption":"弹出菜单","id":"LISTMENU","imageClass":"ri-tools-line","name":"弹出菜单","tabindex":1,"tagVar":{"name":"LISTMENU","clazz":"net.ooder.esd.annotation.menu.CustomMenuType"},"title":"弹出菜单"},{"bindClass":[],"caption":"下标栏","id":"BOTTOMBAR","imageClass":"ri-checkbox-blank-line","name":"下标栏","tabindex":2,"tagVar":{"name":"BOTTOMBAR","clazz":"net.ooder.esd.annotation.menu.CustomMenuType"},"title":"下标栏"},{"bindClass":[],"caption":"菜单栏","id":"MENUBAR","imageClass":"ri-menu-line","name":"菜单栏","tabindex":3,"tagVar":{"name":"MENUBAR","clazz":"net.ooder.esd.annotation.menu.CustomMenuType"},"title":"菜单栏"},{"bindClass":[],"caption":"行控制按钮","id":"ROWCMD","imageClass":"ri-checkbox-blank-line","name":"行控制按钮","tabindex":4,"tagVar":{"name":"ROWCMD","clazz":"net.ooder.esd.annotation.menu.CustomMenuType"},"title":"行控制按钮"},{"bindClass":[],"caption":"右键菜单","id":"CONTEXTMENU","imageClass":"ri-menu-line","name":"右键菜单","tabindex":5,"tagVar":{"name":"CONTEXTMENU","clazz":"net.ooder.esd.annotation.menu.CustomMenuType"},"title":"右键菜单"},{"bindClass":[],"caption":"子菜单","id":"SUB","imageClass":"ri-menu-line","name":"子菜单","tabindex":6,"tagVar":{"name":"SUB","clazz":"net.ooder.esd.annotation.menu.CustomMenuType"},"title":"子菜单"},{"bindClass":[],"caption":"流程控制栏","id":"BPM","imageClass":"ri-node-tree","name":"流程控制栏","tabindex":7,"tagVar":{"name":"BPM","clazz":"net.ooder.esd.annotation.menu.CustomMenuType"},"title":"流程控制栏"},{"bindClass":[],"caption":"流程控制栏","id":"BPMBOTTOM","imageClass":"ri-node-tree","name":"流程控制栏","tabindex":8,"tagVar":{"name":"BPMBOTTOM","clazz":"net.ooder.esd.annotation.menu.CustomMenuType"},"title":"流程控制栏"},{"bindClass":[],"caption":"RAD通用组件","id":"COMPONENT","imageClass":"ri-menu-line","name":"RAD通用组件","tabindex":9,"tagVar":{"name":"COMPONENT","clazz":"net.ooder.esd.annotation.menu.CustomMenuType"},"title":"RAD通用组件"},{"bindClass":[],"caption":"RAD顶部插件","id":"PLUGINS","imageClass":"ri-menu-line","name":"RAD顶部插件","tabindex":10,"tagVar":{"name":"PLUGINS","clazz":"net.ooder.esd.annotation.menu.CustomMenuType"},"title":"RAD顶部插件"},{"bindClass":[],"caption":"RAD工具栏菜单","id":"TOP","imageClass":"ri-menu-line","name":"RAD工具栏菜单","tabindex":11,"tagVar":{"name":"TOP","clazz":"net.ooder.esd.annotation.menu.CustomMenuType"},"title":"RAD工具栏菜单"}])
                .setLabelCaption("菜单类型")
                .setReadonly(false)
                .setTabindex(1)
                .setType("listbox")
                .setVisibility("visible")
            ;
            MenuBarConfig.append(menuType);

            // dock
            var dock = ood.create("ood.UI.ComboInput")
                .setHost(MenuBarConfig, "dock")
                .setName("dock")
                .setDefaultFocus(false)
                .setDesc("菜单位置")
                .setDisableClickEffect(false)
                .setDisableHoverEffect(false)
                .setDisableTips(false)
                .setDisabled(false)
                .setDock("none")
                .setDynLoad(true)
                .setEnumClass("net.ooder.esd.annotation.ui.Dock")
                .setItems([{"bindClass":[],"caption":"默认","id":"none","name":"默认","tabindex":0,"tagVar":{"name":"none","clazz":"net.ooder.esd.annotation.ui.Dock"},"title":"默认","type":"none"},{"bindClass":[],"caption":"顶部对齐","id":"top","name":"顶部对齐","tabindex":1,"tagVar":{"name":"top","clazz":"net.ooder.esd.annotation.ui.Dock"},"title":"顶部对齐"},{"bindClass":[],"caption":"底部对齐","id":"bottom","name":"底部对齐","tabindex":2,"tagVar":{"name":"bottom","clazz":"net.ooder.esd.annotation.ui.Dock"},"title":"底部对齐"},{"bindClass":[],"caption":"左对齐","id":"left","name":"左对齐","tabindex":3,"tagVar":{"name":"left","clazz":"net.ooder.esd.annotation.ui.Dock"},"title":"左对齐"},{"bindClass":[],"caption":"右对齐","id":"right","name":"右对齐","tabindex":4,"tagVar":{"name":"right","clazz":"net.ooder.esd.annotation.ui.Dock"},"title":"右对齐"},{"bindClass":[],"caption":"居中对齐","id":"center","name":"居中对齐","tabindex":5,"tagVar":{"name":"center","clazz":"net.ooder.esd.annotation.ui.Dock"},"title":"居中对齐"},{"bindClass":[],"caption":"垂直居中","id":"middle","name":"垂直居中","tabindex":6,"tagVar":{"name":"middle","clazz":"net.ooder.esd.annotation.ui.Dock"},"title":"垂直居中"},{"bindClass":[],"caption":"自适应排列","id":"origin","name":"自适应排列","tabindex":7,"tagVar":{"name":"origin","clazz":"net.ooder.esd.annotation.ui.Dock"},"title":"自适应排列"},{"bindClass":[],"caption":"宽度自适应","id":"width","name":"宽度自适应","tabindex":8,"tagVar":{"name":"width","clazz":"net.ooder.esd.annotation.ui.Dock"},"title":"宽度自适应"},{"bindClass":[],"caption":"高度自适应","id":"height","name":"高度自适应","tabindex":9,"tagVar":{"name":"height","clazz":"net.ooder.esd.annotation.ui.Dock"},"title":"高度自适应"},{"bindClass":[],"caption":"自动填充","id":"fill","name":"自动填充","tabindex":10,"tagVar":{"name":"fill","clazz":"net.ooder.esd.annotation.ui.Dock"},"title":"自动填充"},{"bindClass":[],"caption":"覆盖","id":"cover","name":"覆盖","tabindex":11,"tagVar":{"name":"cover","clazz":"net.ooder.esd.annotation.ui.Dock"},"title":"覆盖"}])
                .setLabelCaption("菜单位置")
                .setReadonly(false)
                .setTabindex(2)
                .setType("listbox")
                .setVisibility("visible")
            ;
            MenuBarConfig.append(dock);

            // barDock
            var barDock = ood.create("ood.UI.ComboInput")
                .setHost(MenuBarConfig, "barDock")
                .setName("barDock")
                .setDefaultFocus(false)
                .setDesc("工具栏布局")
                .setDisableClickEffect(false)
                .setDisableHoverEffect(false)
                .setDisableTips(false)
                .setDisabled(false)
                .setDock("none")
                .setDynLoad(true)
                .setEnumClass("net.ooder.esd.annotation.ui.Dock")
                .setItems([{"bindClass":[],"caption":"默认","id":"none","name":"默认","tabindex":0,"tagVar":{"name":"none","clazz":"net.ooder.esd.annotation.ui.Dock"},"title":"默认","type":"none"},{"bindClass":[],"caption":"顶部对齐","id":"top","name":"顶部对齐","tabindex":1,"tagVar":{"name":"top","clazz":"net.ooder.esd.annotation.ui.Dock"},"title":"顶部对齐"},{"bindClass":[],"caption":"底部对齐","id":"bottom","name":"底部对齐","tabindex":2,"tagVar":{"name":"bottom","clazz":"net.ooder.esd.annotation.ui.Dock"},"title":"底部对齐"},{"bindClass":[],"caption":"左对齐","id":"left","name":"左对齐","tabindex":3,"tagVar":{"name":"left","clazz":"net.ooder.esd.annotation.ui.Dock"},"title":"左对齐"},{"bindClass":[],"caption":"右对齐","id":"right","name":"右对齐","tabindex":4,"tagVar":{"name":"right","clazz":"net.ooder.esd.annotation.ui.Dock"},"title":"右对齐"},{"bindClass":[],"caption":"居中对齐","id":"center","name":"居中对齐","tabindex":5,"tagVar":{"name":"center","clazz":"net.ooder.esd.annotation.ui.Dock"},"title":"居中对齐"},{"bindClass":[],"caption":"垂直居中","id":"middle","name":"垂直居中","tabindex":6,"tagVar":{"name":"middle","clazz":"net.ooder.esd.annotation.ui.Dock"},"title":"垂直居中"},{"bindClass":[],"caption":"自适应排列","id":"origin","name":"自适应排列","tabindex":7,"tagVar":{"name":"origin","clazz":"net.ooder.esd.annotation.ui.Dock"},"title":"自适应排列"},{"bindClass":[],"caption":"宽度自适应","id":"width","name":"宽度自适应","tabindex":8,"tagVar":{"name":"width","clazz":"net.ooder.esd.annotation.ui.Dock"},"title":"宽度自适应"},{"bindClass":[],"caption":"高度自适应","id":"height","name":"高度自适应","tabindex":9,"tagVar":{"name":"height","clazz":"net.ooder.esd.annotation.ui.Dock"},"title":"高度自适应"},{"bindClass":[],"caption":"自动填充","id":"fill","name":"自动填充","tabindex":10,"tagVar":{"name":"fill","clazz":"net.ooder.esd.annotation.ui.Dock"},"title":"自动填充"},{"bindClass":[],"caption":"覆盖","id":"cover","name":"覆盖","tabindex":11,"tagVar":{"name":"cover","clazz":"net.ooder.esd.annotation.ui.Dock"},"title":"覆盖"}])
                .setLabelCaption("工具栏布局")
                .setReadonly(false)
                .setTabindex(3)
                .setType("listbox")
                .setVisibility("visible")
            ;
            MenuBarConfig.append(barDock);

            // imageClass
            var imageClass = ood.create("ood.UI.ComboInput")
                .setHost(MenuBarConfig, "imageClass")
                .setName("imageClass")
                .setCaption("图标")
                .setDefaultFocus(false)
                .setDesc("图标")
                .setDisableClickEffect(false)
                .setDisableHoverEffect(false)
                .setDisableTips(false)
                .setDisabled(false)
                .setDock("none")
                .setItems([])
                .setLabelCaption("图标")
                .setReadonly(false)
                .setTabindex(4)
                .setType("input")
                .setVisibility("visible")
            ;
            MenuBarConfig.append(imageClass);

            // hAlign
            var hAlign = ood.create("ood.UI.ComboInput")
                .setHost(MenuBarConfig, "hAlign")
                .setName("hAlign")
                .setCaption("横向对齐")
                .setDefaultFocus(false)
                .setDesc("横向对齐")
                .setDisableClickEffect(false)
                .setDisableHoverEffect(false)
                .setDisableTips(false)
                .setDisabled(false)
                .setDock("none")
                .setDynLoad(true)
                .setEnumClass("net.ooder.esd.annotation.ui.HAlignType")
                .setItems([{"bindClass":[],"caption":"center","id":"center","tabindex":0,"tagVar":{"name":"center","clazz":"net.ooder.esd.annotation.ui.HAlignType"},"title":"center"},{"bindClass":[],"caption":"left","id":"left","tabindex":1,"tagVar":{"name":"left","clazz":"net.ooder.esd.annotation.ui.HAlignType"},"title":"left"},{"bindClass":[],"caption":"right","id":"right","tabindex":2,"tagVar":{"name":"right","clazz":"net.ooder.esd.annotation.ui.HAlignType"},"title":"right"}])
                .setLabelCaption("横向对齐")
                .setReadonly(false)
                .setTabindex(5)
                .setType("combobox")
                .setVisibility("visible")
            ;
            MenuBarConfig.append(hAlign);

            // vAlign
            var vAlign = ood.create("ood.UI.ComboInput")
                .setHost(MenuBarConfig, "vAlign")
                .setName("vAlign")
                .setCaption("垂直对齐")
                .setDefaultFocus(false)
                .setDesc("垂直对齐")
                .setDisableClickEffect(false)
                .setDisableHoverEffect(false)
                .setDisableTips(false)
                .setDisabled(false)
                .setDock("none")
                .setDynLoad(true)
                .setEnumClass("net.ooder.esd.annotation.ui.VAlignType")
                .setItems([{"bindClass":[],"caption":"bottom","id":"bottom","tabindex":0,"tagVar":{"name":"bottom","clazz":"net.ooder.esd.annotation.ui.VAlignType"},"title":"bottom"},{"bindClass":[],"caption":"middle","id":"middle","tabindex":1,"tagVar":{"name":"middle","clazz":"net.ooder.esd.annotation.ui.VAlignType"},"title":"middle"},{"bindClass":[],"caption":"top","id":"top","tabindex":2,"tagVar":{"name":"top","clazz":"net.ooder.esd.annotation.ui.VAlignType"},"title":"top"}])
                .setLabelCaption("垂直对齐")
                .setReadonly(false)
                .setTabindex(6)
                .setType("combobox")
                .setVisibility("visible")
            ;
            MenuBarConfig.append(vAlign);

            // showCaption
            var showCaption = ood.create("ood.UI.CheckBox")
                .setHost(MenuBarConfig, "showCaption")
                .setName("showCaption")
                .setCaption("")
                .setDefaultFocus(false)
                .setDesc("显示标题")
                .setDisableClickEffect(false)
                .setDisableHoverEffect(false)
                .setDisableTips(false)
                .setDock("none")
                .setEvents({})
                .setTabindex(7)
                .setVisibility("visible")
            ;
            MenuBarConfig.append(showCaption);

            // handler
            var handler = ood.create("ood.UI.CheckBox")
                .setHost(MenuBarConfig, "handler")
                .setName("handler")
                .setCaption("")
                .setDefaultFocus(false)
                .setDesc("行头手柄")
                .setDisableClickEffect(false)
                .setDisableHoverEffect(false)
                .setDisableTips(false)
                .setDock("none")
                .setEvents({})
                .setTabindex(8)
                .setVisibility("visible")
            ;
            MenuBarConfig.append(handler);

            // dynLoad
            var dynLoad = ood.create("ood.UI.CheckBox")
                .setHost(MenuBarConfig, "dynLoad")
                .setName("dynLoad")
                .setCaption("")
                .setDefaultFocus(false)
                .setDesc("动态加载")
                .setDisableClickEffect(false)
                .setDisableHoverEffect(false)
                .setDisableTips(false)
                .setDock("none")
                .setEvents({})
                .setTabindex(9)
                .setVisibility("visible")
            ;
            MenuBarConfig.append(dynLoad);

            // lazy
            var lazy = ood.create("ood.UI.CheckBox")
                .setHost(MenuBarConfig, "lazy")
                .setName("lazy")
                .setCaption("")
                .setDefaultFocus(false)
                .setDesc("延迟加载")
                .setDisableClickEffect(false)
                .setDisableHoverEffect(false)
                .setDisableTips(false)
                .setDock("none")
                .setEvents({})
                .setTabindex(10)
                .setVisibility("visible")
            ;
            MenuBarConfig.append(lazy);

            // top
            var top = ood.create("ood.UI.Input")
                .setHost(MenuBarConfig, "top")
                .setName("top")
                .setCaption("上边距")
                .setDefaultFocus(false)
                .setDesc("上边距")
                .setDisableClickEffect(false)
                .setDisableHoverEffect(false)
                .setDisableTips(false)
                .setDisabled(false)
                .setDock("none")
                .setEvents({})
                .setImageClass("")
                .setItems([])
                .setLabelCaption("上边距")
                .setLabelPos("left")
                .setLabelSize("6.0em")
                .setReadonly(false)
                .setTabindex(11)
                .setTagVar({})
                .setVisibility("visible")
            ;
            MenuBarConfig.append(top);

            // itemPadding
            var itemPadding = ood.create("ood.UI.Input")
                .setHost(MenuBarConfig, "itemPadding")
                .setName("itemPadding")
                .setCaption("菜单项边距")
                .setDefaultFocus(false)
                .setDesc("菜单项边距")
                .setDisableClickEffect(false)
                .setDisableHoverEffect(false)
                .setDisableTips(false)
                .setDisabled(false)
                .setDock("none")
                .setEvents({})
                .setImageClass("")
                .setItems([])
                .setLabelCaption("菜单项边距")
                .setLabelPos("left")
                .setLabelSize("6.0em")
                .setReadonly(false)
                .setTabindex(12)
                .setTagVar({})
                .setVisibility("visible")
            ;
            MenuBarConfig.append(itemPadding);

            // itemMargin
            var itemMargin = ood.create("ood.UI.Input")
                .setHost(MenuBarConfig, "itemMargin")
                .setName("itemMargin")
                .setCaption("菜单项间隔")
                .setDefaultFocus(false)
                .setDesc("菜单项间隔")
                .setDisableClickEffect(false)
                .setDisableHoverEffect(false)
                .setDisableTips(false)
                .setDisabled(false)
                .setDock("none")
                .setEvents({})
                .setImageClass("")
                .setItems([])
                .setLabelCaption("菜单项间隔")
                .setLabelPos("left")
                .setLabelSize("6.0em")
                .setReadonly(false)
                .setTabindex(13)
                .setTagVar({})
                .setVisibility("visible")
            ;
            MenuBarConfig.append(itemMargin);

            // itemWidth
            var itemWidth = ood.create("ood.UI.Input")
                .setHost(MenuBarConfig, "itemWidth")
                .setName("itemWidth")
                .setCaption("菜单项宽度")
                .setDefaultFocus(false)
                .setDesc("菜单项宽度")
                .setDisableClickEffect(false)
                .setDisableHoverEffect(false)
                .setDisableTips(false)
                .setDisabled(false)
                .setDock("none")
                .setEvents({})
                .setImageClass("")
                .setItems([])
                .setLabelCaption("菜单项宽度")
                .setLabelPos("left")
                .setLabelSize("6.0em")
                .setReadonly(false)
                .setTabindex(14)
                .setTagVar({})
                .setVisibility("visible")
            ;
            MenuBarConfig.append(itemWidth);

            // itemAlign
            var itemAlign = ood.create("ood.UI.ComboInput")
                .setHost(MenuBarConfig, "itemAlign")
                .setName("itemAlign")
                .setCaption("菜单项位置")
                .setDefaultFocus(false)
                .setDesc("菜单项位置")
                .setDisableClickEffect(false)
                .setDisableHoverEffect(false)
                .setDisableTips(false)
                .setDisabled(false)
                .setDock("none")
                .setDynLoad(true)
                .setEnumClass("net.ooder.esd.annotation.ui.AlignType")
                .setItems([{"bindClass":[],"caption":"center","id":"center","tabindex":0,"tagVar":{"name":"center","clazz":"net.ooder.esd.annotation.ui.AlignType"},"title":"center"},{"bindClass":[],"caption":"left","id":"left","tabindex":1,"tagVar":{"name":"left","clazz":"net.ooder.esd.annotation.ui.AlignType"},"title":"left"},{"bindClass":[],"caption":"right","id":"right","tabindex":2,"tagVar":{"name":"right","clazz":"net.ooder.esd.annotation.ui.AlignType"},"title":"right"}])
                .setLabelCaption("菜单项位置")
                .setReadonly(false)
                .setTabindex(15)
                .setType("combobox")
                .setVisibility("visible")
            ;
            MenuBarConfig.append(itemAlign);

            // itemType
            var itemType = ood.create("ood.UI.ComboInput")
                .setHost(MenuBarConfig, "itemType")
                .setName("itemType")
                .setCaption("菜单项类型")
                .setDefaultFocus(false)
                .setDesc("菜单项类型")
                .setDisableClickEffect(false)
                .setDisableHoverEffect(false)
                .setDisableTips(false)
                .setDisabled(false)
                .setDock("none")
                .setDynLoad(true)
                .setEnumClass("net.ooder.esd.annotation.ui.StatusItemType")
                .setItems([{"bindClass":[],"caption":"text","id":"text","tabindex":0,"tagVar":{"name":"text","clazz":"net.ooder.esd.annotation.ui.StatusItemType"},"title":"text"},{"bindClass":[],"caption":"button","id":"button","tabindex":1,"tagVar":{"name":"button","clazz":"net.ooder.esd.annotation.ui.StatusItemType"},"title":"button"},{"bindClass":[],"caption":"dropButton","id":"dropButton","tabindex":2,"tagVar":{"name":"dropButton","clazz":"net.ooder.esd.annotation.ui.StatusItemType"},"title":"dropButton"}])
                .setLabelCaption("菜单项类型")
                .setReadonly(false)
                .setTabindex(16)
                .setType("combobox")
                .setVisibility("visible")
            ;
            MenuBarConfig.append(itemType);

            // autoIconColor
            var autoIconColor = ood.create("ood.UI.CheckBox")
                .setHost(MenuBarConfig, "autoIconColor")
                .setName("autoIconColor")
                .setCaption("")
                .setDefaultFocus(false)
                .setDesc("彩色图标")
                .setDisableClickEffect(false)
                .setDisableHoverEffect(false)
                .setDisableTips(false)
                .setDock("none")
                .setEvents({})
                .setTabindex(17)
                .setVisibility("visible")
            ;
            MenuBarConfig.append(autoIconColor);

            // autoItemColor
            var autoItemColor = ood.create("ood.UI.CheckBox")
                .setHost(MenuBarConfig, "autoItemColor")
                .setName("autoItemColor")
                .setCaption("")
                .setDefaultFocus(false)
                .setDesc("彩色选项")
                .setDisableClickEffect(false)
                .setDisableHoverEffect(false)
                .setDisableTips(false)
                .setDock("none")
                .setEvents({})
                .setTabindex(18)
                .setVisibility("visible")
            ;
            MenuBarConfig.append(autoItemColor);

            // autoFontColor
            var autoFontColor = ood.create("ood.UI.CheckBox")
                .setHost(MenuBarConfig, "autoFontColor")
                .setName("autoFontColor")
                .setCaption("")
                .setDefaultFocus(false)
                .setDesc("彩色字体")
                .setDisableClickEffect(false)
                .setDisableHoverEffect(false)
                .setDisableTips(false)
                .setDock("none")
                .setEvents({})
                .setTabindex(19)
                .setVisibility("visible")
            ;
            MenuBarConfig.append(autoFontColor);

            // connected
            var connected = ood.create("ood.UI.CheckBox")
                .setHost(MenuBarConfig, "connected")
                .setName("connected")
                .setCaption("")
                .setDefaultFocus(false)
                .setDesc("自动连接")
                .setDisableClickEffect(false)
                .setDisableHoverEffect(false)
                .setDisableTips(false)
                .setDock("none")
                .setEvents({})
                .setTabindex(20)
                .setVisibility("visible")
            ;
            MenuBarConfig.append(connected);

            // itemHeight
            var itemHeight = ood.create("ood.UI.Input")
                .setHost(MenuBarConfig, "itemHeight")
                .setName("itemHeight")
                .setCaption("工具栏高")
                .setDefaultFocus(false)
                .setDesc("工具栏高")
                .setDisableClickEffect(false)
                .setDisableHoverEffect(false)
                .setDisableTips(false)
                .setDisabled(false)
                .setDock("none")
                .setEvents({})
                .setImageClass("")
                .setItems([])
                .setLabelCaption("工具栏高")
                .setLabelPos("left")
                .setLabelSize("6.0em")
                .setReadonly(false)
                .setTabindex(21)
                .setTagVar({})
                .setVisibility("visible")
            ;
            MenuBarConfig.append(itemHeight);

            // width
            var width = ood.create("ood.UI.Input")
                .setHost(MenuBarConfig, "width")
                .setName("width")
                .setCaption("宽度")
                .setDefaultFocus(false)
                .setDesc("宽度")
                .setDisableClickEffect(false)
                .setDisableHoverEffect(false)
                .setDisableTips(false)
                .setDisabled(false)
                .setDock("none")
                .setEvents({})
                .setImageClass("")
                .setItems([])
                .setLabelCaption("宽度")
                .setLabelPos("left")
                .setLabelSize("6.0em")
                .setReadonly(false)
                .setTabindex(22)
                .setTagVar({})
                .setVisibility("visible")
            ;
            MenuBarConfig.append(width);

            // height
            var height = ood.create("ood.UI.Input")
                .setHost(MenuBarConfig, "height")
                .setName("height")
                .setCaption("高度")
                .setDefaultFocus(false)
                .setDesc("高度")
                .setDisableClickEffect(false)
                .setDisableHoverEffect(false)
                .setDisableTips(false)
                .setDisabled(false)
                .setDock("none")
                .setEvents({})
                .setImageClass("")
                .setItems([])
                .setLabelCaption("高度")
                .setLabelPos("left")
                .setLabelSize("6.0em")
                .setReadonly(false)
                .setTabindex(23)
                .setTagVar({})
                .setVisibility("visible")
            ;
            MenuBarConfig.append(height);

            // barheight
            var barheight = ood.create("ood.UI.Input")
                .setHost(MenuBarConfig, "barheight")
                .setName("barheight")
                .setCaption("工具栏高")
                .setDefaultFocus(false)
                .setDesc("工具栏高")
                .setDisableClickEffect(false)
                .setDisableHoverEffect(false)
                .setDisableTips(false)
                .setDisabled(false)
                .setDock("none")
                .setEvents({})
                .setImageClass("")
                .setItems([])
                .setLabelCaption("工具栏高")
                .setLabelPos("left")
                .setLabelSize("6.0em")
                .setReadonly(false)
                .setTabindex(24)
                .setTagVar({})
                .setVisibility("visible")
            ;
            MenuBarConfig.append(barheight);

            // borderType
            var borderType = ood.create("ood.UI.ComboInput")
                .setHost(MenuBarConfig, "borderType")
                .setName("borderType")
                .setCaption("边框")
                .setDefaultFocus(false)
                .setDesc("边框")
                .setDisableClickEffect(false)
                .setDisableHoverEffect(false)
                .setDisableTips(false)
                .setDisabled(false)
                .setDock("none")
                .setDynLoad(true)
                .setEnumClass("net.ooder.esd.annotation.ui.BorderType")
                .setItems([{"bindClass":[],"caption":"none","id":"none","tabindex":0,"tagVar":{"name":"none","clazz":"net.ooder.esd.annotation.ui.BorderType"},"title":"none"},{"bindClass":[],"caption":"flat","id":"flat","tabindex":1,"tagVar":{"name":"flat","clazz":"net.ooder.esd.annotation.ui.BorderType"},"title":"flat"},{"bindClass":[],"caption":"inset","id":"inset","tabindex":2,"tagVar":{"name":"inset","clazz":"net.ooder.esd.annotation.ui.BorderType"},"title":"inset"},{"bindClass":[],"caption":"outset","id":"outset","tabindex":3,"tagVar":{"name":"outset","clazz":"net.ooder.esd.annotation.ui.BorderType"},"title":"outset"}])
                .setLabelCaption("边框")
                .setReadonly(false)
                .setTabindex(25)
                .setType("combobox")
                .setVisibility("visible")
            ;
            MenuBarConfig.append(borderType);

            // barBorderType
            var barBorderType = ood.create("ood.UI.ComboInput")
                .setHost(MenuBarConfig, "barBorderType")
                .setName("barBorderType")
                .setCaption("工具栏边框")
                .setDefaultFocus(false)
                .setDesc("工具栏边框")
                .setDisableClickEffect(false)
                .setDisableHoverEffect(false)
                .setDisableTips(false)
                .setDisabled(false)
                .setDock("none")
                .setDynLoad(true)
                .setEnumClass("net.ooder.esd.annotation.ui.BorderType")
                .setItems([{"bindClass":[],"caption":"none","id":"none","tabindex":0,"tagVar":{"name":"none","clazz":"net.ooder.esd.annotation.ui.BorderType"},"title":"none"},{"bindClass":[],"caption":"flat","id":"flat","tabindex":1,"tagVar":{"name":"flat","clazz":"net.ooder.esd.annotation.ui.BorderType"},"title":"flat"},{"bindClass":[],"caption":"inset","id":"inset","tabindex":2,"tagVar":{"name":"inset","clazz":"net.ooder.esd.annotation.ui.BorderType"},"title":"inset"},{"bindClass":[],"caption":"outset","id":"outset","tabindex":3,"tagVar":{"name":"outset","clazz":"net.ooder.esd.annotation.ui.BorderType"},"title":"outset"}])
                .setLabelCaption("工具栏边框")
                .setReadonly(false)
                .setTabindex(26)
                .setType("combobox")
                .setVisibility("visible")
            ;
            MenuBarConfig.append(barBorderType);

            // tagCmdsAlign
            var tagCmdsAlign = ood.create("ood.UI.ComboInput")
                .setHost(MenuBarConfig, "tagCmdsAlign")
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
                .setTabindex(27)
                .setType("combobox")
                .setVisibility("visible")
            ;
            MenuBarConfig.append(tagCmdsAlign);

            // menus
            var menus = ood.create("ood.UI.Input")
                .setHost(MenuBarConfig, "menus")
                .setName("menus")
                .setCaption("menus")
                .setDefaultFocus(false)
                .setDesc("menus")
                .setDisableClickEffect(false)
                .setDisableHoverEffect(false)
                .setDisableTips(false)
                .setDisabled(false)
                .setDock("none")
                .setEvents({})
                .setItems([])
                .setLabelCaption("menus")
                .setLabelPos("left")
                .setLabelSize("6.0em")
                .setReadonly(false)
                .setTabindex(28)
                .setTagVar({})
                .setVisibility("visible")
            ;
            MenuBarConfig.append(menus);

            // menuClasses
            var menuClasses = ood.create("ood.UI.Input")
                .setHost(MenuBarConfig, "menuClasses")
                .setName("menuClasses")
                .setCaption("menuClasses")
                .setDefaultFocus(false)
                .setDesc("menuClasses")
                .setDisableClickEffect(false)
                .setDisableHoverEffect(false)
                .setDisableTips(false)
                .setDisabled(false)
                .setDock("none")
                .setEvents({})
                .setItems([])
                .setLabelCaption("menuClasses")
                .setLabelPos("left")
                .setLabelSize("6.0em")
                .setReadonly(false)
                .setTabindex(29)
                .setTagVar({})
                .setVisibility("visible")
            ;
            MenuBarConfig.append(menuClasses);

            // serviceClass
            var serviceClass = ood.create("ood.UI.Input")
                .setHost(MenuBarConfig, "serviceClass")
                .setName("serviceClass")
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
                .setTabindex(30)
                .setTagVar({})
                .setVisibility("visible")
            ;
            MenuBarConfig.append(serviceClass);

            return children;
        }
    }
});
