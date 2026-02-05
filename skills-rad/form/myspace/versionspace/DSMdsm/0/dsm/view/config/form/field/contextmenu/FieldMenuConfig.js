// 自动生成的 FieldMenuConfig 组件
ood.Class("ooder.FieldMenuConfig", "ood.Module", {
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
        "caption": "右键菜单信息",
        "currComponentAlias": "FieldMenuConfig",
        "dock": "fill",
        "title": "右键菜单信息"
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

            // FieldMenuConfigMain
            var FieldMenuConfigMain = ood.create("ood.UI.Block")
                .setHost(host, "FieldMenuConfigMain")
                .setName("FieldMenuConfigMain")
                .setDock("fill")
                .setPanelBgClr("transparent")
                .setTabindex(1)
            ;
            append(FieldMenuConfigMain);

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
                .setMethodName("addFieldContextMenu")
                .setProxyType("auto")
                .setQueryAsync(true)
                .setQueryMethod("POST")
                .setQueryURL("/dsm/view/config/form/field/contextmenu/addFieldContextMenu")
                .setRequestDataSource([{"name":"FieldMenuConfigMain","path":"","type":"FORM"},{"name":"PAGECTX","path":"","type":"FORM"}])
                .setRequestType("FORM")
                .setResponseCallback([])
                .setResponseDataTarget([])
                .setResponseType("JSON")
                .setSourceClassName("net.ooder.dsm.view.config.form.field.contextmenu.menuclass.ContextFieldMenuService")
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
                .setDesc("右键菜单信息")
                .setImageClass("ri-code-box-line")
                .setIsAllform(false)
                .setMethodName("getFieldMenuConfig")
                .setProxyType("auto")
                .setQueryAsync(true)
                .setQueryMethod("POST")
                .setQueryURL("/dsm/view/config/form/field/contextmenu/FieldMenuConfig")
                .setRequestDataSource([{"name":"FieldMenuConfigMain","path":"","type":"FORM"},{"name":"PAGECTX","path":"","type":"FORM"}])
                .setRequestType("FORM")
                .setResponseCallback([])
                .setResponseDataTarget([{"name":"FieldMenuConfigMain","path":"data","type":"FORM"},{"name":"PAGECTX","path":"data","type":"FORM"}])
                .setResponseType("JSON")
                .setSourceClassName("net.ooder.dsm.view.config.form.field.contextmenu.ContextFieldMenuNav")
                .setTabindex(3)
                .setTips("右键菜单信息")
            ;
            append(RELOAD);

            // fieldname
            var fieldname = ood.create("ood.UI.HiddenInput")
                .setHost(PAGECTX, "fieldname")
                .setName("fieldname")
                .setFormField(true)
                .setIsPid(true)
                .setPid(true)
                .setTabindex(0)
            ;
            PAGECTX.append(fieldname);

            // currentClassName
            var currentClassName = ood.create("ood.UI.HiddenInput")
                .setHost(PAGECTX, "currentClassName")
                .setName("currentClassName")
                .setFormField(true)
                .setIsPid(true)
                .setPid(true)
                .setTabindex(1)
            ;
            PAGECTX.append(currentClassName);

            // xpath
            var xpath = ood.create("ood.UI.HiddenInput")
                .setHost(PAGECTX, "xpath")
                .setName("xpath")
                .setFormField(true)
                .setIsPid(false)
                .setPid(false)
                .setTabindex(2)
            ;
            PAGECTX.append(xpath);

            // sourceMethodName
            var sourceMethodName = ood.create("ood.UI.HiddenInput")
                .setHost(PAGECTX, "sourceMethodName")
                .setName("sourceMethodName")
                .setFormField(true)
                .setIsPid(true)
                .setPid(true)
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
                .setIsPid(false)
                .setPid(false)
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

            // path
            var path = ood.create("ood.UI.HiddenInput")
                .setHost(PAGECTX, "path")
                .setName("path")
                .setFormField(true)
                .setIsPid(true)
                .setPid(true)
                .setTabindex(9)
            ;
            PAGECTX.append(path);

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

            // parentID
            var parentID = ood.create("ood.UI.HiddenInput")
                .setHost(PAGECTX, "parentID")
                .setName("parentID")
                .setFormField(true)
                .setIsPid(false)
                .setPid(false)
                .setTabindex(11)
            ;
            PAGECTX.append(parentID);

            // FieldMenuConfig
            var FieldMenuConfig = ood.create("ood.UI.FormLayout")
                .setHost(FieldMenuConfigMain, "FieldMenuConfig")
                .setName("FieldMenuConfig")
                .setCaption("右键菜单信息")
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
                .setLayoutData({"cells":{"A1":{"cellName":"A1","style":{"textAlign":"center"},"value":"行头手柄"},"C3":{"cellName":"C3","style":{"textAlign":"center"},"value":"是否数据域"},"A2":{"cellName":"A2","style":{"textAlign":"center"},"value":"延迟加载"},"A3":{"cellName":"A3","style":{"textAlign":"center"},"value":"自动隐藏"},"C5":{"cellName":"C5","style":{"textAlign":"center"},"value":"字体大小"},"A4":{"cellName":"A4","style":{"textAlign":"center"},"value":"实现类"},"A5":{"cellName":"A5","style":{"textAlign":"center"},"value":"拖动支持KEY"},"A6":{"cellName":"A6","style":{"textAlign":"center"},"value":"菜单项Class"},"A7":{"cellName":"A7","style":{"textAlign":"center"},"value":"菜单项Style"},"A8":{"cellName":"A8","style":{"textAlign":"center"},"value":"绑定服务"},"C1":{"cellName":"C1","style":{"textAlign":"center"},"value":"动态加载"},"C2":{"cellName":"C2","style":{"textAlign":"center"},"value":"点击隐藏"}},"colSetting":{"A":{"manualWidth":150},"B":{"manualWidth":150},"C":{"manualWidth":150},"D":{"manualWidth":150}},"cols":4,"merged":[{"col":1,"colspan":3,"removed":false,"row":3,"rowspan":1},{"col":1,"colspan":3,"removed":false,"row":5,"rowspan":1},{"col":1,"colspan":3,"removed":false,"row":6,"rowspan":1},{"col":1,"colspan":3,"removed":false,"row":7,"rowspan":1}],"rowSetting":{"1":{"manualHeight":35},"2":{"manualHeight":35},"3":{"manualHeight":35},"4":{"manualHeight":35},"5":{"manualHeight":35},"6":{"manualHeight":35},"7":{"manualHeight":50},"8":{"manualHeight":35}},"rows":8})
                .setLineSpacing(10)
                .setMode("write")
                .setSolidGridlines(true)
                .setStretchH("all")
                .setStretchHeight("none")
                .setTabindex(0)
                .setTagVar({})
                .setVisibility("visible")
            ;
            FieldMenuConfigMain.append(FieldMenuConfig);

            // handler
            var handler = ood.create("ood.UI.CheckBox")
                .setHost(FieldMenuConfig, "handler")
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
            FieldMenuConfig.append(handler);

            // dynLoad
            var dynLoad = ood.create("ood.UI.CheckBox")
                .setHost(FieldMenuConfig, "dynLoad")
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
            FieldMenuConfig.append(dynLoad);

            // lazy
            var lazy = ood.create("ood.UI.CheckBox")
                .setHost(FieldMenuConfig, "lazy")
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
            FieldMenuConfig.append(lazy);

            // hideAfterClick
            var hideAfterClick = ood.create("ood.UI.CheckBox")
                .setHost(FieldMenuConfig, "hideAfterClick")
                .setName("hideAfterClick")
                .setCaption("")
                .setDefaultFocus(false)
                .setDesc("点击隐藏")
                .setDisableClickEffect(false)
                .setDisableHoverEffect(false)
                .setDisableTips(false)
                .setDock("none")
                .setEvents({})
                .setTabindex(3)
                .setVisibility("visible")
            ;
            FieldMenuConfig.append(hideAfterClick);

            // autoHide
            var autoHide = ood.create("ood.UI.CheckBox")
                .setHost(FieldMenuConfig, "autoHide")
                .setName("autoHide")
                .setCaption("")
                .setDefaultFocus(false)
                .setDesc("自动隐藏")
                .setDisableClickEffect(false)
                .setDisableHoverEffect(false)
                .setDisableTips(false)
                .setDock("none")
                .setEvents({})
                .setTabindex(4)
                .setVisibility("visible")
            ;
            FieldMenuConfig.append(autoHide);

            // formField
            var formField = ood.create("ood.UI.CheckBox")
                .setHost(FieldMenuConfig, "formField")
                .setName("formField")
                .setCaption("")
                .setDefaultFocus(false)
                .setDesc("是否数据域")
                .setDisableClickEffect(false)
                .setDisableHoverEffect(false)
                .setDisableTips(false)
                .setDock("none")
                .setEvents({})
                .setTabindex(5)
                .setVisibility("visible")
            ;
            FieldMenuConfig.append(formField);

            // serviceClass
            var serviceClass = ood.create("ood.UI.Input")
                .setHost(FieldMenuConfig, "serviceClass")
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
                .setTabindex(6)
                .setTagVar({})
                .setVisibility("visible")
            ;
            FieldMenuConfig.append(serviceClass);

            // listKey
            var listKey = ood.create("ood.UI.Input")
                .setHost(FieldMenuConfig, "listKey")
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
                .setTabindex(7)
                .setTagVar({})
                .setVisibility("visible")
            ;
            FieldMenuConfig.append(listKey);

            // iconFontSize
            var iconFontSize = ood.create("ood.UI.Input")
                .setHost(FieldMenuConfig, "iconFontSize")
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
                .setTabindex(8)
                .setTagVar({})
                .setVisibility("visible")
            ;
            FieldMenuConfig.append(iconFontSize);

            // itemClass
            var itemClass = ood.create("ood.UI.Input")
                .setHost(FieldMenuConfig, "itemClass")
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
                .setTabindex(9)
                .setTagVar({})
                .setVisibility("visible")
            ;
            FieldMenuConfig.append(itemClass);

            // itemStyle
            var itemStyle = ood.create("ood.UI.Input")
                .setHost(FieldMenuConfig, "itemStyle")
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
                .setTabindex(10)
                .setTagVar({})
                .setVisibility("visible")
            ;
            FieldMenuConfig.append(itemStyle);

            // bindService
            var bindService = ood.create("ood.UI.Input")
                .setHost(FieldMenuConfig, "bindService")
                .setName("bindService")
                .setCaption("绑定服务")
                .setDefaultFocus(false)
                .setDesc("绑定服务")
                .setDisableClickEffect(false)
                .setDisableHoverEffect(false)
                .setDisableTips(false)
                .setDisabled(false)
                .setDock("none")
                .setEvents({})
                .setImageClass("")
                .setItems([])
                .setLabelCaption("绑定服务")
                .setLabelPos("left")
                .setLabelSize("6.0em")
                .setReadonly(false)
                .setTabindex(11)
                .setTagVar({})
                .setVisibility("visible")
            ;
            FieldMenuConfig.append(bindService);

            return children;
        }
    }
});
