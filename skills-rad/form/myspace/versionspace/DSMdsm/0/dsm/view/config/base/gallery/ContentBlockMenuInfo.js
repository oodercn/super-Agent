// 自动生成的 ContentBlockMenuInfo 组件
ood.Class("ooder.ContentBlockMenuInfo", "ood.Module", {
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
        "caption": "菜单信息",
        "currComponentAlias": "ContentBlockMenuInfo",
        "dock": "fill",
        "title": "菜单信息"
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

            // ContentBlockMenuInfoMain
            var ContentBlockMenuInfoMain = ood.create("ood.UI.Block")
                .setHost(host, "ContentBlockMenuInfoMain")
                .setName("ContentBlockMenuInfoMain")
                .setDock("fill")
                .setPanelBgClr("transparent")
                .setTabindex(1)
            ;
            append(ContentBlockMenuInfoMain);

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
                .setMethodName("saveGalleryMenuClass")
                .setProxyType("auto")
                .setQueryAsync(true)
                .setQueryMethod("POST")
                .setQueryURL("/dsm/view/config/base/gallery/item/saveGalleryMenuClass")
                .setRequestDataSource([{"name":"ContentBlockMenuInfoMain","path":"","type":"FORM"},{"name":"PAGECTX","path":"","type":"FORM"}])
                .setRequestType("FORM")
                .setResponseCallback([])
                .setResponseDataTarget([])
                .setResponseType("JSON")
                .setSourceClassName("net.ooder.dsm.view.config.base.gallery.item.contextmenu.menuclass.ContextBaseGalleryMenuService")
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
                .setDesc("菜单信息")
                .setImageClass("ri-code-box-line")
                .setIsAllform(false)
                .setMethodName("getBaseGalleryMenuInfo")
                .setProxyType("auto")
                .setQueryAsync(true)
                .setQueryMethod("POST")
                .setQueryURL("/dsm/view/config/base/gallery/ContentBlockMenuInfo")
                .setRequestDataSource([{"name":"ContentBlockMenuInfoMain","path":"","type":"FORM"},{"name":"PAGECTX","path":"","type":"FORM"}])
                .setRequestType("FORM")
                .setResponseCallback([])
                .setResponseDataTarget([{"name":"ContentBlockMenuInfoMain","path":"data","type":"FORM"},{"name":"PAGECTX","path":"data","type":"FORM"}])
                .setResponseType("JSON")
                .setSourceClassName("net.ooder.dsm.view.config.base.gallery.item.contextmenu.ContextBaseGalleryMenuNav")
                .setTabindex(3)
                .setTips("菜单信息")
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

            // ContentBlockMenuInfo
            var ContentBlockMenuInfo = ood.create("ood.UI.FormLayout")
                .setHost(ContentBlockMenuInfoMain, "ContentBlockMenuInfo")
                .setName("ContentBlockMenuInfo")
                .setCaption("菜单信息")
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
                .setLayoutData({"cells":{"C1":{"cellName":"C1","style":{"textAlign":"center"},"value":"动态加载"},"A1":{"cellName":"A1","style":{"textAlign":"center"},"value":"行头手柄"},"A2":{"cellName":"A2","style":{"textAlign":"center"},"value":"延迟加载"},"C4":{"cellName":"C4","style":{"textAlign":"center"},"value":"拖动支持KEY"},"A3":{"cellName":"A3","style":{"textAlign":"center"},"value":"实现类"},"C5":{"cellName":"C5","style":{"textAlign":"center"},"value":"父级ID"},"A4":{"cellName":"A4","style":{"textAlign":"center"},"value":"点击隐藏"},"C6":{"cellName":"C6","style":{"textAlign":"center"},"value":"字体大小"},"A5":{"cellName":"A5","style":{"textAlign":"center"},"value":"自动隐藏"},"A6":{"cellName":"A6","style":{"textAlign":"center"},"value":"是否数据域"},"A7":{"cellName":"A7","style":{"textAlign":"center"},"value":"菜单项Class"},"A8":{"cellName":"A8","style":{"textAlign":"center"},"value":"菜单项Style"},"A9":{"cellName":"A9","style":{"textAlign":"center"},"value":"绑定服务"}},"colSetting":{"A":{"manualWidth":150},"B":{"manualWidth":150},"C":{"manualWidth":150},"D":{"manualWidth":150}},"cols":4,"merged":[{"col":1,"colspan":3,"removed":false,"row":2,"rowspan":1},{"col":1,"colspan":3,"removed":false,"row":7,"rowspan":1}],"rowSetting":{"1":{"manualHeight":35},"2":{"manualHeight":35},"3":{"manualHeight":35},"4":{"manualHeight":35},"5":{"manualHeight":35},"6":{"manualHeight":35},"7":{"manualHeight":35},"8":{"manualHeight":50},"9":{"manualHeight":35}},"rows":9})
                .setLineSpacing(10)
                .setMode("write")
                .setSolidGridlines(true)
                .setStretchH("all")
                .setStretchHeight("none")
                .setTabindex(0)
                .setTagVar({})
                .setVisibility("visible")
            ;
            ContentBlockMenuInfoMain.append(ContentBlockMenuInfo);

            // handler
            var handler = ood.create("ood.UI.CheckBox")
                .setHost(ContentBlockMenuInfo, "handler")
                .setName("handler")
                .setCaption("")
                .setDefaultFocus(false)
                .setDesc("行头手柄")
                .setDisableClickEffect(false)
                .setDisableHoverEffect(false)
                .setDisableTips(false)
                .setDock("none")
                .setEvents({})
                .setTabindex(0)
                .setVisibility("visible")
            ;
            ContentBlockMenuInfo.append(handler);

            // dynLoad
            var dynLoad = ood.create("ood.UI.CheckBox")
                .setHost(ContentBlockMenuInfo, "dynLoad")
                .setName("dynLoad")
                .setCaption("")
                .setDefaultFocus(false)
                .setDesc("动态加载")
                .setDisableClickEffect(false)
                .setDisableHoverEffect(false)
                .setDisableTips(false)
                .setDock("none")
                .setEvents({})
                .setTabindex(1)
                .setVisibility("visible")
            ;
            ContentBlockMenuInfo.append(dynLoad);

            // lazy
            var lazy = ood.create("ood.UI.CheckBox")
                .setHost(ContentBlockMenuInfo, "lazy")
                .setName("lazy")
                .setCaption("")
                .setDefaultFocus(false)
                .setDesc("延迟加载")
                .setDisableClickEffect(false)
                .setDisableHoverEffect(false)
                .setDisableTips(false)
                .setDock("none")
                .setEvents({})
                .setTabindex(2)
                .setVisibility("visible")
            ;
            ContentBlockMenuInfo.append(lazy);

            // serviceClass
            var serviceClass = ood.create("ood.UI.Input")
                .setHost(ContentBlockMenuInfo, "serviceClass")
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
                .setTabindex(3)
                .setTagVar({})
                .setVisibility("visible")
            ;
            ContentBlockMenuInfo.append(serviceClass);

            // hideAfterClick
            var hideAfterClick = ood.create("ood.UI.CheckBox")
                .setHost(ContentBlockMenuInfo, "hideAfterClick")
                .setName("hideAfterClick")
                .setCaption("")
                .setDefaultFocus(false)
                .setDesc("点击隐藏")
                .setDisableClickEffect(false)
                .setDisableHoverEffect(false)
                .setDisableTips(false)
                .setDock("none")
                .setEvents({})
                .setTabindex(4)
                .setVisibility("visible")
            ;
            ContentBlockMenuInfo.append(hideAfterClick);

            // listKey
            var listKey = ood.create("ood.UI.Input")
                .setHost(ContentBlockMenuInfo, "listKey")
                .setName("listKey")
                .setCaption("拖动支持KEY")
                .setDefaultFocus(false)
                .setDesc("拖动支持KEY")
                .setDisableClickEffect(false)
                .setDisableHoverEffect(false)
                .setDisableTips(false)
                .setDisabled(false)
                .setDock("none")
                .setEvents({})
                .setImageClass("")
                .setItems([])
                .setLabelCaption("拖动支持KEY")
                .setLabelPos("left")
                .setLabelSize("6.0em")
                .setReadonly(false)
                .setTabindex(5)
                .setTagVar({})
                .setVisibility("visible")
            ;
            ContentBlockMenuInfo.append(listKey);

            // autoHide
            var autoHide = ood.create("ood.UI.CheckBox")
                .setHost(ContentBlockMenuInfo, "autoHide")
                .setName("autoHide")
                .setCaption("")
                .setDefaultFocus(false)
                .setDesc("自动隐藏")
                .setDisableClickEffect(false)
                .setDisableHoverEffect(false)
                .setDisableTips(false)
                .setDock("none")
                .setEvents({})
                .setTabindex(6)
                .setVisibility("visible")
            ;
            ContentBlockMenuInfo.append(autoHide);

            // parentID
            var parentID = ood.create("ood.UI.Input")
                .setHost(ContentBlockMenuInfo, "parentID")
                .setName("parentID")
                .setCaption("父级ID")
                .setDefaultFocus(false)
                .setDesc("父级ID")
                .setDisableClickEffect(false)
                .setDisableHoverEffect(false)
                .setDisableTips(false)
                .setDisabled(false)
                .setDock("none")
                .setEvents({})
                .setImageClass("")
                .setItems([])
                .setLabelCaption("父级ID")
                .setLabelPos("left")
                .setLabelSize("6.0em")
                .setReadonly(false)
                .setTabindex(7)
                .setTagVar({})
                .setVisibility("visible")
            ;
            ContentBlockMenuInfo.append(parentID);

            // formField
            var formField = ood.create("ood.UI.CheckBox")
                .setHost(ContentBlockMenuInfo, "formField")
                .setName("formField")
                .setCaption("")
                .setDefaultFocus(false)
                .setDesc("是否数据域")
                .setDisableClickEffect(false)
                .setDisableHoverEffect(false)
                .setDisableTips(false)
                .setDock("none")
                .setEvents({})
                .setTabindex(8)
                .setVisibility("visible")
            ;
            ContentBlockMenuInfo.append(formField);

            // iconFontSize
            var iconFontSize = ood.create("ood.UI.Input")
                .setHost(ContentBlockMenuInfo, "iconFontSize")
                .setName("iconFontSize")
                .setCaption("字体大小")
                .setDefaultFocus(false)
                .setDesc("字体大小")
                .setDisableClickEffect(false)
                .setDisableHoverEffect(false)
                .setDisableTips(false)
                .setDisabled(false)
                .setDock("none")
                .setEvents({})
                .setImageClass("")
                .setItems([])
                .setLabelCaption("字体大小")
                .setLabelPos("left")
                .setLabelSize("6.0em")
                .setReadonly(false)
                .setTabindex(9)
                .setTagVar({})
                .setVisibility("visible")
            ;
            ContentBlockMenuInfo.append(iconFontSize);

            // itemClass
            var itemClass = ood.create("ood.UI.Input")
                .setHost(ContentBlockMenuInfo, "itemClass")
                .setName("itemClass")
                .setCaption("菜单项Class")
                .setDefaultFocus(false)
                .setDesc("菜单项Class")
                .setDisableClickEffect(false)
                .setDisableHoverEffect(false)
                .setDisableTips(false)
                .setDisabled(false)
                .setDock("none")
                .setEvents({})
                .setImageClass("")
                .setItems([])
                .setLabelCaption("菜单项Class")
                .setLabelPos("left")
                .setLabelSize("6.0em")
                .setReadonly(false)
                .setTabindex(10)
                .setTagVar({})
                .setVisibility("visible")
            ;
            ContentBlockMenuInfo.append(itemClass);

            // itemStyle
            var itemStyle = ood.create("ood.UI.Input")
                .setHost(ContentBlockMenuInfo, "itemStyle")
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
                .setTabindex(11)
                .setTagVar({})
                .setVisibility("visible")
            ;
            ContentBlockMenuInfo.append(itemStyle);

            // bindService
            var bindService = ood.create("ood.UI.ComboInput")
                .setHost(ContentBlockMenuInfo, "bindService")
                .setName("bindService")
                .setCaption("绑定服务")
                .setDefaultFocus(false)
                .setDesc("绑定服务")
                .setDisableClickEffect(false)
                .setDisableHoverEffect(false)
                .setDisableTips(false)
                .setDisabled(false)
                .setDock("none")
                .setItems([])
                .setLabelCaption("绑定服务")
                .setReadonly(false)
                .setTabindex(12)
                .setType("input")
                .setVisibility("visible")
            ;
            ContentBlockMenuInfo.append(bindService);

            return children;
        }
    }
});
