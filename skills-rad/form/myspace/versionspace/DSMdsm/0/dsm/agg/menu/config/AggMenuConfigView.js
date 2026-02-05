// 自动生成的 AggMenuConfigView 组件
ood.Class("ooder.AggMenuConfigView", "ood.Module", {
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
        "caption": "菜单配置",
        "currComponentAlias": "AggMenuConfigView",
        "dock": "fill",
        "title": "菜单配置"
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

            // AggMenuConfigViewMain
            var AggMenuConfigViewMain = ood.create("ood.UI.Block")
                .setHost(host, "AggMenuConfigViewMain")
                .setName("AggMenuConfigViewMain")
                .setDock("fill")
                .setPanelBgClr("transparent")
                .setTabindex(1)
            ;
            append(AggMenuConfigViewMain);

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
                .setQueryURL("/dsm/agg/menu/config/method/group/saveWebMenu")
                .setRequestDataSource([{"name":"menuBarBean","path":"","type":"FORM"},{"name":"currentClassName","path":"","type":"FORM"},{"name":"xpath","path":"","type":"FORM"},{"name":"menuBarBean","path":"","type":"FORM"},{"name":"currentClassName","path":"","type":"FORM"},{"name":"xpath","path":"","type":"FORM"},{"name":"AggMenuConfigViewMain","path":"","type":"FORM"},{"name":"PAGECTX","path":"","type":"FORM"}])
                .setRequestType("JSON")
                .setResponseCallback([])
                .setResponseDataTarget([])
                .setResponseType("JSON")
                .setSourceClassName("net.ooder.dsm.aggregation.config.menu.tree.AggMenuConfigNavService")
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
                .setDesc("菜单配置")
                .setImageClass("ri-code-box-line")
                .setIsAllform(false)
                .setMethodName("getAggMenuConfigView")
                .setProxyType("auto")
                .setQueryAsync(true)
                .setQueryMethod("POST")
                .setQueryURL("/dsm/agg/menu/config/AggMenuConfigView")
                .setRequestDataSource([{"name":"AggMenuConfigViewMain","path":"","type":"FORM"},{"name":"PAGECTX","path":"","type":"FORM"}])
                .setRequestType("FORM")
                .setResponseCallback([])
                .setResponseDataTarget([{"name":"AggMenuConfigViewMain","path":"data","type":"FORM"},{"name":"PAGECTX","path":"data","type":"FORM"}])
                .setResponseType("JSON")
                .setSourceClassName("net.ooder.dsm.aggregation.config.menu.AggMenuNav")
                .setTabindex(3)
                .setTips("菜单配置")
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

            // parentId
            var parentId = ood.create("ood.UI.HiddenInput")
                .setHost(PAGECTX, "parentId")
                .setName("parentId")
                .setFormField(true)
                .setIsPid(true)
                .setPid(true)
                .setTabindex(6)
            ;
            PAGECTX.append(parentId);

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

            // index
            var index = ood.create("ood.UI.HiddenInput")
                .setHost(PAGECTX, "index")
                .setName("index")
                .setFormField(true)
                .setIsPid(false)
                .setPid(false)
                .setTabindex(8)
            ;
            PAGECTX.append(index);

            // AggMenuConfigView
            var AggMenuConfigView = ood.create("ood.UI.FormLayout")
                .setHost(AggMenuConfigViewMain, "AggMenuConfigView")
                .setName("AggMenuConfigView")
                .setCaption("菜单配置")
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
                .setLayoutData({"cells":{"A1":{"cellName":"A1","style":{"textAlign":"center"},"value":"菜单类型"},"A2":{"cellName":"A2","style":{"textAlign":"center"},"value":"显示名称"},"C4":{"cellName":"C4","style":{"textAlign":"center"},"value":"行头手柄"},"A3":{"cellName":"A3","style":{"textAlign":"center"},"value":"垂直对齐"},"A4":{"cellName":"A4","style":{"textAlign":"center"},"value":"显示标题"},"A5":{"cellName":"A5","style":{"textAlign":"center"},"value":"实现类"},"E2":{"cellName":"E2","style":{"textAlign":"center"},"value":"图标"},"G4":{"cellName":"G4","style":{"textAlign":"center"},"value":"延迟加载"},"E3":{"cellName":"E3","style":{"textAlign":"center"},"value":"横向对齐"},"E4":{"cellName":"E4","style":{"textAlign":"center"},"value":"动态加载"}},"colSetting":{"A":{"manualWidth":150},"B":{"manualWidth":150},"C":{"manualWidth":150},"D":{"manualWidth":150},"E":{"manualWidth":150},"F":{"manualWidth":150},"G":{"manualWidth":150},"H":{"manualWidth":150}},"cols":8,"merged":[{"col":1,"colspan":7,"removed":false,"row":0,"rowspan":1},{"col":1,"colspan":3,"removed":false,"row":1,"rowspan":1},{"col":5,"colspan":3,"removed":false,"row":1,"rowspan":1},{"col":1,"colspan":3,"removed":false,"row":2,"rowspan":1},{"col":5,"colspan":3,"removed":false,"row":2,"rowspan":1},{"col":1,"colspan":7,"removed":false,"row":4,"rowspan":1}],"rowSetting":{"1":{"manualHeight":100},"2":{"manualHeight":35},"3":{"manualHeight":35},"4":{"manualHeight":35},"5":{"manualHeight":35}},"rows":5})
                .setLineSpacing(10)
                .setMode("write")
                .setSolidGridlines(true)
                .setStretchH("all")
                .setStretchHeight("none")
                .setTabindex(0)
                .setTagVar({})
                .setVisibility("visible")
            ;
            AggMenuConfigViewMain.append(AggMenuConfigView);

            // menuType
            var menuType = ood.create("ood.UI.List")
                .setHost(AggMenuConfigView, "menuType")
                .setName("menuType")
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
                .setItems([{"bindClass":[],"caption":"工具栏","id":"TOOLBAR","imageClass":"ri-tools-line","name":"工具栏","tabindex":0,"tagVar":{"name":"TOOLBAR","clazz":"net.ooder.esd.annotation.menu.CustomMenuType"},"title":"工具栏"},{"bindClass":[],"caption":"弹出菜单","id":"LISTMENU","imageClass":"ri-tools-line","name":"弹出菜单","tabindex":1,"tagVar":{"name":"LISTMENU","clazz":"net.ooder.esd.annotation.menu.CustomMenuType"},"title":"弹出菜单"},{"bindClass":[],"caption":"下标栏","id":"BOTTOMBAR","imageClass":"ri-checkbox-blank-line","name":"下标栏","tabindex":2,"tagVar":{"name":"BOTTOMBAR","clazz":"net.ooder.esd.annotation.menu.CustomMenuType"},"title":"下标栏"},{"bindClass":[],"caption":"菜单栏","id":"MENUBAR","imageClass":"ri-menu-line","name":"菜单栏","tabindex":3,"tagVar":{"name":"MENUBAR","clazz":"net.ooder.esd.annotation.menu.CustomMenuType"},"title":"菜单栏"},{"bindClass":[],"caption":"行控制按钮","id":"ROWCMD","imageClass":"ri-checkbox-blank-line","name":"行控制按钮","tabindex":4,"tagVar":{"name":"ROWCMD","clazz":"net.ooder.esd.annotation.menu.CustomMenuType"},"title":"行控制按钮"},{"bindClass":[],"caption":"右键菜单","id":"CONTEXTMENU","imageClass":"ri-menu-line","name":"右键菜单","tabindex":5,"tagVar":{"name":"CONTEXTMENU","clazz":"net.ooder.esd.annotation.menu.CustomMenuType"},"title":"右键菜单"},{"bindClass":[],"caption":"子菜单","id":"SUB","imageClass":"ri-menu-line","name":"子菜单","tabindex":6,"tagVar":{"name":"SUB","clazz":"net.ooder.esd.annotation.menu.CustomMenuType"},"title":"子菜单"},{"bindClass":[],"caption":"流程控制栏","id":"BPM","imageClass":"ri-node-tree","name":"流程控制栏","tabindex":7,"tagVar":{"name":"BPM","clazz":"net.ooder.esd.annotation.menu.CustomMenuType"},"title":"流程控制栏"},{"bindClass":[],"caption":"流程控制栏","id":"BPMBOTTOM","imageClass":"ri-node-tree","name":"流程控制栏","tabindex":8,"tagVar":{"name":"BPMBOTTOM","clazz":"net.ooder.esd.annotation.menu.CustomMenuType"},"title":"流程控制栏"},{"bindClass":[],"caption":"RAD通用组件","id":"COMPONENT","imageClass":"ri-menu-line","name":"RAD通用组件","tabindex":9,"tagVar":{"name":"COMPONENT","clazz":"net.ooder.esd.annotation.menu.CustomMenuType"},"title":"RAD通用组件"},{"bindClass":[],"caption":"RAD顶部插件","id":"PLUGINS","imageClass":"ri-menu-line","name":"RAD顶部插件","tabindex":10,"tagVar":{"name":"PLUGINS","clazz":"net.ooder.esd.annotation.menu.CustomMenuType"},"title":"RAD顶部插件"},{"bindClass":[],"caption":"RAD工具栏菜单","id":"TOP","imageClass":"ri-menu-line","name":"RAD工具栏菜单","tabindex":11,"tagVar":{"name":"TOP","clazz":"net.ooder.esd.annotation.menu.CustomMenuType"},"title":"RAD工具栏菜单"}])
                .setSelMode("single")
                .setTabindex(0)
                .setVisibility("visible")
                .setWidth("auto")
            ;
            AggMenuConfigView.append(menuType);

            // caption
            var caption = ood.create("ood.UI.Input")
                .setHost(AggMenuConfigView, "caption")
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
                .setTabindex(1)
                .setTagVar({})
                .setVisibility("visible")
            ;
            AggMenuConfigView.append(caption);

            // imageClass
            var imageClass = ood.create("ood.UI.ComboInput")
                .setHost(AggMenuConfigView, "imageClass")
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
                .setTabindex(2)
                .setType("input")
                .setVisibility("visible")
            ;
            AggMenuConfigView.append(imageClass);

            // hAlign
            var hAlign = ood.create("ood.UI.ComboInput")
                .setHost(AggMenuConfigView, "hAlign")
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
                .setTabindex(3)
                .setType("combobox")
                .setVisibility("visible")
            ;
            AggMenuConfigView.append(hAlign);

            // vAlign
            var vAlign = ood.create("ood.UI.ComboInput")
                .setHost(AggMenuConfigView, "vAlign")
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
                .setTabindex(4)
                .setType("combobox")
                .setVisibility("visible")
            ;
            AggMenuConfigView.append(vAlign);

            // showCaption
            var showCaption = ood.create("ood.UI.CheckBox")
                .setHost(AggMenuConfigView, "showCaption")
                .setName("showCaption")
                .setCaption("")
                .setDefaultFocus(false)
                .setDesc("显示标题")
                .setDisableClickEffect(false)
                .setDisableHoverEffect(false)
                .setDisableTips(false)
                .setDock("none")
                .setEvents({})
                .setTabindex(5)
                .setVisibility("visible")
            ;
            AggMenuConfigView.append(showCaption);

            // handler
            var handler = ood.create("ood.UI.CheckBox")
                .setHost(AggMenuConfigView, "handler")
                .setName("handler")
                .setCaption("")
                .setDefaultFocus(false)
                .setDesc("行头手柄")
                .setDisableClickEffect(false)
                .setDisableHoverEffect(false)
                .setDisableTips(false)
                .setDock("none")
                .setEvents({})
                .setTabindex(6)
                .setVisibility("visible")
            ;
            AggMenuConfigView.append(handler);

            // dynLoad
            var dynLoad = ood.create("ood.UI.CheckBox")
                .setHost(AggMenuConfigView, "dynLoad")
                .setName("dynLoad")
                .setCaption("")
                .setDefaultFocus(false)
                .setDesc("动态加载")
                .setDisableClickEffect(false)
                .setDisableHoverEffect(false)
                .setDisableTips(false)
                .setDock("none")
                .setEvents({})
                .setTabindex(7)
                .setVisibility("visible")
            ;
            AggMenuConfigView.append(dynLoad);

            // lazy
            var lazy = ood.create("ood.UI.CheckBox")
                .setHost(AggMenuConfigView, "lazy")
                .setName("lazy")
                .setCaption("")
                .setDefaultFocus(false)
                .setDesc("延迟加载")
                .setDisableClickEffect(false)
                .setDisableHoverEffect(false)
                .setDisableTips(false)
                .setDock("none")
                .setEvents({})
                .setTabindex(8)
                .setVisibility("visible")
            ;
            AggMenuConfigView.append(lazy);

            // serviceClass
            var serviceClass = ood.create("ood.UI.Input")
                .setHost(AggMenuConfigView, "serviceClass")
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
                .setTabindex(9)
                .setTagVar({})
                .setVisibility("visible")
            ;
            AggMenuConfigView.append(serviceClass);

            // SAVEBottomBlock
            var SAVEBottomBlock = ood.create("ood.UI.Block")
                .setHost(AggMenuConfigViewMain, "SAVEBottomBlock")
                .setName("SAVEBottomBlock")
                .setBorderType("none")
                .setComboType("STATUSBUTTONS")
                .setDock("bottom")
                .setHeight("3.0em")
                .setOverflow("hidden")
                .setTabindex(1)
            ;
            AggMenuConfigViewMain.append(SAVEBottomBlock);

            // SAVEBottom
            var SAVEBottom = ood.create("ood.UI.StatusButtons")
                .setHost(SAVEBottomBlock, "SAVEBottom")
                .setName("SAVEBottom")
                .setBorderType("none")
                .setDock("fill")
                .setItemType("button")
                .setItemWidth("auto")
                .setItems([{"caption":"重置","iconColor":"#E6945C","id":"RESET_button","imageClass":"ri-refresh-line","index":0,"itemType":"button","tabindex":0,"tagVar":{"name":"RESET","clazz":"net.ooder.esd.annotation.menu.CustomFormMenu"},"tips":"重置","title":"重置"},{"caption":"保存","iconColor":"#195ead","id":"SAVE_button","imageClass":"ri-save-line","index":0,"itemType":"button","tabindex":1,"tagVar":{"name":"SAVE","clazz":"net.ooder.esd.annotation.menu.CustomFormMenu"},"tips":"保存","title":"保存"}])
                .setMenuType("BOTTOMBAR")
                .setPosition("static")
                .setShowCaption(true)
                .setTabindex(0)
                .setWidth("auto")
            ;
            SAVEBottomBlock.append(SAVEBottom);

            return children;
        }
    }
});
