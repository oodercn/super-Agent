// 自动生成的 AggConfigTree 组件
ood.Class("ooder.AggConfigTree", "ood.Module", {
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

            // AggConfigTreeMain
            var AggConfigTreeMain = ood.create("ood.UI.Dialog")
                .setHost(host, "AggConfigTreeMain")
                .setName("AggConfigTreeMain")
                .setCaption("菜单配置")
                .setDefaultFocus(false)
                .setDisableClickEffect(false)
                .setDisableHoverEffect(false)
                .setDisableTips(false)
                .setDisabled(false)
                .setDock("none")
                .setHeight("680.0")
                .setImageClass("ri-settings-3-line")
                .setLeft("220.0")
                .setShowEffects("Classic")
                .setTabindex(1)
                .setTop("100.0")
                .setVisibility("visible")
                .setWidth("900.0")
            ;
            append(AggConfigTreeMain);

            // RELOAD
            var RELOAD = ood.create("ood.APICaller")
                .setHost(host, "RELOAD")
                .setName("RELOAD")
                .setAllform(false)
                .setAutoRun(true)
                .setCheckRequired(false)
                .setCheckValid(false)
                .setDesc("菜单配置")
                .setImageClass("ri-settings-3-line")
                .setIsAllform(false)
                .setMethodName("getAggConfigTree")
                .setProxyType("auto")
                .setQueryAsync(true)
                .setQueryMethod("POST")
                .setQueryURL("/dsm/view/config/base/gallery/item/AggConfigTree")
                .setRequestDataSource([{"name":"PAGECTX","path":"","type":"FORM"},{"name":"AggMenuConfigTreePanel","path":"","type":"FORM"}])
                .setRequestType("FORM")
                .setResponseCallback([])
                .setResponseDataTarget([{"name":"AggMenuConfigTree","path":"data","type":"TREEVIEW"},{"name":"PAGECTX","path":"data","type":"FORM"}])
                .setResponseType("JSON")
                .setSourceClassName("net.ooder.dsm.view.config.base.gallery.item.contextmenu.menuclass.ContextBaseGalleryMenuService")
                .setTabindex(2)
                .setTips("菜单配置")
            ;
            append(RELOAD);

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
                .setRequestDataSource([{"name":"AggMenuConfigTree","path":"","type":"FORM"},{"name":"PAGECTX","path":"","type":"FORM"}])
                .setRequestType("FORM")
                .setResponseCallback([])
                .setResponseDataTarget([])
                .setResponseType("JSON")
                .setSourceClassName("net.ooder.dsm.view.config.base.gallery.item.contextmenu.menuclass.ContextBaseGalleryMenuService")
                .setTabindex(3)
                .setTips("保存")
            ;
            append(SAVE);

            // AggMenuMainNavItem_MethodConfig_dsm_agg_menu_config_LoadAllMethod
            var AggMenuMainNavItem_MethodConfig_dsm_agg_menu_config_LoadAllMethod = ood.create("ood.APICaller")
                .setHost(host, "AggMenuMainNavItem_MethodConfig_dsm_agg_menu_config_LoadAllMethod")
                .setName("AggMenuMainNavItem_MethodConfig_dsm_agg_menu_config_LoadAllMethod")
                .setAllform(false)
                .setAutoRun(false)
                .setCheckRequired(false)
                .setCheckValid(false)
                .setDesc("loadAllMethod")
                .setImageClass("ri-code-box-line")
                .setIsAllform(false)
                .setMethodName("loadAllMethod")
                .setProxyType("auto")
                .setQueryAsync(true)
                .setQueryMethod("POST")
                .setQueryURL("/dsm/agg/menu/config/loadAllMethod")
                .setRequestDataSource([{"name":"PAGECTX","path":"","type":"FORM"},{"name":"AggMenuConfigTree","path":"","type":"FORM"}])
                .setRequestType("FORM")
                .setResponseCallback([])
                .setResponseDataTarget([])
                .setResponseType("JSON")
                .setSourceClassName("net.ooder.dsm.aggregation.config.menu.tree.AggMenuMethodsNavService")
                .setTabindex(4)
                .setTips("loadAllMethod")
            ;
            append(AggMenuMainNavItem_MethodConfig_dsm_agg_menu_config_LoadAllMethod);

            // dsm_agg_entity_config_LoadNav
            var dsm_agg_entity_config_LoadNav = ood.create("ood.APICaller")
                .setHost(host, "dsm_agg_entity_config_LoadNav")
                .setName("dsm_agg_entity_config_LoadNav")
                .setAllform(false)
                .setAutoRun(false)
                .setCheckRequired(false)
                .setCheckValid(false)
                .setDesc("loadNav")
                .setImageClass("ri-code-box-line")
                .setIsAllform(false)
                .setMethodName("loadNav")
                .setProxyType("auto")
                .setQueryAsync(true)
                .setQueryMethod("POST")
                .setQueryURL("/dsm/agg/entity/config/loadNav")
                .setRequestDataSource([{"name":"PAGECTX","path":"","type":"FORM"},{"name":"AggMenuConfigTree","path":"","type":"FORM"}])
                .setRequestType("FORM")
                .setResponseCallback([])
                .setResponseDataTarget([])
                .setResponseType("JSON")
                .setSourceClassName("net.ooder.dsm.aggregation.config.entity.tree.EntityMethodService")
                .setTabindex(5)
                .setTips("loadNav")
            ;
            append(dsm_agg_entity_config_LoadNav);

            // AggMenuNavItem_CustomMethodConfig_dsm_agg_menu_config_method_LoadMenuMethod
            var AggMenuNavItem_CustomMethodConfig_dsm_agg_menu_config_method_LoadMenuMethod = ood.create("ood.APICaller")
                .setHost(host, "AggMenuNavItem_CustomMethodConfig_dsm_agg_menu_config_method_LoadMenuMethod")
                .setName("AggMenuNavItem_CustomMethodConfig_dsm_agg_menu_config_method_LoadMenuMethod")
                .setAllform(false)
                .setAutoRun(false)
                .setCheckRequired(false)
                .setCheckValid(false)
                .setDesc("loadMenuMethod")
                .setImageClass("ri-code-box-line")
                .setIsAllform(false)
                .setMethodName("loadMenuMethod")
                .setProxyType("auto")
                .setQueryAsync(true)
                .setQueryMethod("POST")
                .setQueryURL("/dsm/agg/menu/config/method/loadMenuMethod")
                .setRequestDataSource([{"name":"PAGECTX","path":"","type":"FORM"},{"name":"AggMenuConfigTree","path":"","type":"FORM"}])
                .setRequestType("FORM")
                .setResponseCallback([])
                .setResponseDataTarget([])
                .setResponseType("JSON")
                .setSourceClassName("net.ooder.dsm.aggregation.config.menu.tree.AggMenuMethodNavService")
                .setTabindex(7)
                .setTips("loadMenuMethod")
            ;
            append(AggMenuNavItem_CustomMethodConfig_dsm_agg_menu_config_method_LoadMenuMethod);

            // AggEntityNavItem_ViewMethodConfig_dsm_agg_entity_config_view_LoadView
            var AggEntityNavItem_ViewMethodConfig_dsm_agg_entity_config_view_LoadView = ood.create("ood.APICaller")
                .setHost(host, "AggEntityNavItem_ViewMethodConfig_dsm_agg_entity_config_view_LoadView")
                .setName("AggEntityNavItem_ViewMethodConfig_dsm_agg_entity_config_view_LoadView")
                .setAllform(false)
                .setAutoRun(false)
                .setCheckRequired(false)
                .setCheckValid(false)
                .setDesc("loadView")
                .setImageClass("ri-code-box-line")
                .setIsAllform(false)
                .setMethodName("loadView")
                .setProxyType("auto")
                .setQueryAsync(true)
                .setQueryMethod("POST")
                .setQueryURL("/dsm/agg/entity/config/view/loadView")
                .setRequestDataSource([{"name":"PAGECTX","path":"","type":"FORM"},{"name":"AggMenuConfigTree","path":"","type":"FORM"}])
                .setRequestType("FORM")
                .setResponseCallback([])
                .setResponseDataTarget([])
                .setResponseType("JSON")
                .setSourceClassName("net.ooder.dsm.aggregation.config.entity.tree.AggEntityViewNavService")
                .setTabindex(8)
                .setTips("loadView")
            ;
            append(AggEntityNavItem_ViewMethodConfig_dsm_agg_entity_config_view_LoadView);

            // AggEntityMethodNavItem_ViewConfig_dsm_agg_entity_config_LoadNav
            var AggEntityMethodNavItem_ViewConfig_dsm_agg_entity_config_LoadNav = ood.create("ood.APICaller")
                .setHost(host, "AggEntityMethodNavItem_ViewConfig_dsm_agg_entity_config_LoadNav")
                .setName("AggEntityMethodNavItem_ViewConfig_dsm_agg_entity_config_LoadNav")
                .setAllform(false)
                .setAutoRun(false)
                .setCheckRequired(false)
                .setCheckValid(false)
                .setDesc("loadNav")
                .setImageClass("ri-code-box-line")
                .setIsAllform(false)
                .setMethodName("loadNav")
                .setProxyType("auto")
                .setQueryAsync(true)
                .setQueryMethod("POST")
                .setQueryURL("/dsm/agg/entity/config/loadNav")
                .setRequestDataSource([{"name":"PAGECTX","path":"","type":"FORM"},{"name":"AggMenuConfigTree","path":"","type":"FORM"}])
                .setRequestType("FORM")
                .setResponseCallback([])
                .setResponseDataTarget([])
                .setResponseType("JSON")
                .setSourceClassName("net.ooder.dsm.aggregation.config.entity.tree.EntityMethodService")
                .setTabindex(9)
                .setTips("loadNav")
            ;
            append(AggEntityMethodNavItem_ViewConfig_dsm_agg_entity_config_LoadNav);

            // AggEntityNavItem_CustomMethodConfig_dsm_agg_entity_config_method_LoadMethod
            var AggEntityNavItem_CustomMethodConfig_dsm_agg_entity_config_method_LoadMethod = ood.create("ood.APICaller")
                .setHost(host, "AggEntityNavItem_CustomMethodConfig_dsm_agg_entity_config_method_LoadMethod")
                .setName("AggEntityNavItem_CustomMethodConfig_dsm_agg_entity_config_method_LoadMethod")
                .setAllform(false)
                .setAutoRun(false)
                .setCheckRequired(false)
                .setCheckValid(false)
                .setDesc("loadMethod")
                .setImageClass("ri-code-box-line")
                .setIsAllform(false)
                .setMethodName("loadMethod")
                .setProxyType("auto")
                .setQueryAsync(true)
                .setQueryMethod("POST")
                .setQueryURL("/dsm/agg/entity/config/method/loadMethod")
                .setRequestDataSource([{"name":"PAGECTX","path":"","type":"FORM"},{"name":"AggMenuConfigTree","path":"","type":"FORM"}])
                .setRequestType("FORM")
                .setResponseCallback([])
                .setResponseDataTarget([])
                .setResponseType("JSON")
                .setSourceClassName("net.ooder.dsm.aggregation.config.entity.tree.AggEntityMethodNavService")
                .setTabindex(10)
                .setTips("loadMethod")
            ;
            append(AggEntityNavItem_CustomMethodConfig_dsm_agg_entity_config_method_LoadMethod);

            // AggEntityMethodNavItem_ModuleConfig_dsm_agg_module_config_LoadModuleNav
            var AggEntityMethodNavItem_ModuleConfig_dsm_agg_module_config_LoadModuleNav = ood.create("ood.APICaller")
                .setHost(host, "AggEntityMethodNavItem_ModuleConfig_dsm_agg_module_config_LoadModuleNav")
                .setName("AggEntityMethodNavItem_ModuleConfig_dsm_agg_module_config_LoadModuleNav")
                .setAllform(false)
                .setAutoRun(false)
                .setCheckRequired(false)
                .setCheckValid(false)
                .setDesc("loadWinNav")
                .setImageClass("ri-code-box-line")
                .setIsAllform(false)
                .setMethodName("loadWinNav")
                .setProxyType("auto")
                .setQueryAsync(true)
                .setQueryMethod("POST")
                .setQueryURL("/dsm/agg/module/config/loadModuleNav")
                .setRequestDataSource([{"name":"PAGECTX","path":"","type":"FORM"},{"name":"AggMenuConfigTree","path":"","type":"FORM"}])
                .setRequestType("FORM")
                .setResponseCallback([])
                .setResponseDataTarget([])
                .setResponseType("JSON")
                .setSourceClassName("net.ooder.dsm.aggregation.module.ModuleConfigService")
                .setTabindex(11)
                .setTips("loadWinNav")
            ;
            append(AggEntityMethodNavItem_ModuleConfig_dsm_agg_module_config_LoadModuleNav);

            // AggEntityNavItem_FieldsConfig_dsm_agg_field_LoadFieldList
            var AggEntityNavItem_FieldsConfig_dsm_agg_field_LoadFieldList = ood.create("ood.APICaller")
                .setHost(host, "AggEntityNavItem_FieldsConfig_dsm_agg_field_LoadFieldList")
                .setName("AggEntityNavItem_FieldsConfig_dsm_agg_field_LoadFieldList")
                .setAllform(false)
                .setAutoRun(false)
                .setCheckRequired(false)
                .setCheckValid(false)
                .setDesc("loadFieldList")
                .setImageClass("ri-code-box-line")
                .setIsAllform(false)
                .setMethodName("loadFieldList")
                .setProxyType("auto")
                .setQueryAsync(true)
                .setQueryMethod("POST")
                .setQueryURL("/dsm/agg/field/loadFieldList")
                .setRequestDataSource([{"name":"PAGECTX","path":"","type":"FORM"},{"name":"AggMenuConfigTree","path":"","type":"FORM"}])
                .setRequestType("FORM")
                .setResponseCallback([])
                .setResponseDataTarget([])
                .setResponseType("JSON")
                .setSourceClassName("net.ooder.dsm.aggregation.config.entity.tree.field.AggEntityFieldsService")
                .setTabindex(12)
                .setTips("loadFieldList")
            ;
            append(AggEntityNavItem_FieldsConfig_dsm_agg_field_LoadFieldList);

            // dsm_agg_entity_LoadChildView
            var dsm_agg_entity_LoadChildView = ood.create("ood.APICaller")
                .setHost(host, "dsm_agg_entity_LoadChildView")
                .setName("dsm_agg_entity_LoadChildView")
                .setAllform(false)
                .setAutoRun(false)
                .setCheckRequired(false)
                .setCheckValid(false)
                .setDesc("loadChildView")
                .setImageClass("ri-code-box-line")
                .setIsAllform(false)
                .setMethodName("loadChildView")
                .setProxyType("auto")
                .setQueryAsync(true)
                .setQueryMethod("POST")
                .setQueryURL("/dsm/agg/entity/loadChildView")
                .setRequestDataSource([{"name":"PAGECTX","path":"","type":"FORM"},{"name":"AggMenuConfigTree","path":"","type":"FORM"}])
                .setRequestType("FORM")
                .setResponseCallback([])
                .setResponseDataTarget([])
                .setResponseType("JSON")
                .setSourceClassName("net.ooder.dsm.aggregation.config.entity.tree.AggEntityViewService")
                .setTabindex(12)
                .setTips("loadChildView")
            ;
            append(dsm_agg_entity_LoadChildView);

            // AggEntityMethodNavItem_DesignerConfig_dsm_agg_module_config_LoadWinItem
            var AggEntityMethodNavItem_DesignerConfig_dsm_agg_module_config_LoadWinItem = ood.create("ood.APICaller")
                .setHost(host, "AggEntityMethodNavItem_DesignerConfig_dsm_agg_module_config_LoadWinItem")
                .setName("AggEntityMethodNavItem_DesignerConfig_dsm_agg_module_config_LoadWinItem")
                .setAllform(false)
                .setAutoRun(false)
                .setCheckRequired(false)
                .setCheckValid(false)
                .setDesc("loadWinItem")
                .setImageClass("ri-code-box-line")
                .setIsAllform(false)
                .setMethodName("loadWinItem")
                .setProxyType("auto")
                .setQueryAsync(true)
                .setQueryMethod("POST")
                .setQueryURL("/dsm/agg/module/config/loadWinItem")
                .setRequestDataSource([{"name":"PAGECTX","path":"","type":"FORM"},{"name":"AggMenuConfigTree","path":"","type":"FORM"}])
                .setRequestType("FORM")
                .setResponseCallback([])
                .setResponseDataTarget([])
                .setResponseType("JSON")
                .setSourceClassName("net.ooder.dsm.aggregation.module.panel.ModuleWinService")
                .setTabindex(13)
                .setTips("loadWinItem")
            ;
            append(AggEntityMethodNavItem_DesignerConfig_dsm_agg_module_config_LoadWinItem);

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

            // viewClassName
            var viewClassName = ood.create("ood.UI.HiddenInput")
                .setHost(PAGECTX, "viewClassName")
                .setName("viewClassName")
                .setFormField(true)
                .setIsPid(true)
                .setPid(true)
                .setTabindex(7)
            ;
            PAGECTX.append(viewClassName);

            // fieldname
            var fieldname = ood.create("ood.UI.HiddenInput")
                .setHost(PAGECTX, "fieldname")
                .setName("fieldname")
                .setFormField(true)
                .setIsPid(true)
                .setPid(true)
                .setTabindex(8)
            ;
            PAGECTX.append(fieldname);

            // menuType
            var menuType = ood.create("ood.UI.HiddenInput")
                .setHost(PAGECTX, "menuType")
                .setName("menuType")
                .setFormField(true)
                .setIsPid(true)
                .setPid(true)
                .setTabindex(9)
            ;
            PAGECTX.append(menuType);

            // AggConfigTreeLAYOUT
            var AggConfigTreeLAYOUT = ood.create("ood.UI.Layout")
                .setHost(AggConfigTreeMain, "AggConfigTreeLAYOUT")
                .setName("AggConfigTreeLAYOUT")
                .setBorderType("none")
                .setItems([{"bindClass":[],"flexSize":false,"folded":false,"id":"before","max":1000,"min":100,"overflow":"auto","pos":"before","size":260,"tabindex":0,"transparent":true},{"bindClass":[],"flexSize":true,"id":"main","max":300,"overflow":"auto","pos":"main","tabindex":1,"transparent":true}])
                .setTabindex(0)
                .setTransparent(true)
                .setType("horizontal")
            ;
            AggConfigTreeMain.append(AggConfigTreeLAYOUT);

            // AggMenuConfigTreePanel
            var AggMenuConfigTreePanel = ood.create("ood.UI.Block")
                .setHost(AggConfigTreeLAYOUT, "AggMenuConfigTreePanel")
                .setName("AggMenuConfigTreePanel")
                .setBorderType("none")
                .setDock("fill")
                .setTabindex(0)
            ;
            AggConfigTreeLAYOUT.append(AggMenuConfigTreePanel);

            // AggMenuConfigTree
            var AggMenuConfigTree = ood.create("ood.UI.TreeView")
                .setHost(AggMenuConfigTreePanel, "AggMenuConfigTree")
                .setName("AggMenuConfigTree")
                .setCaption("AggMenuConfigTree")
                .setDefaultFocus(false)
                .setDisableClickEffect(false)
                .setDisableHoverEffect(false)
                .setDisableTips(false)
                .setDisabled(false)
                .setDock("none")
                .setDynDestory(true)
                .setFormField(true)
                .setInitFold(false)
                .setItems([{"bindClass":["java.lang.Void"],"caption":"AggMenuConfigTree","id":"aggConfigTree","imageClass":"ri-settings-3-line","initFold":false,"sub":[{"bindClass":["net.ooder.dsm.aggregation.config.entity.AggEntityInfoService"],"bindClassName":"net.ooder.dsm.aggregation.config.entity.AggEntityInfoService","caption":"菜单配置（null)","closeBtn":false,"dynDestory":true,"dynLoad":true,"entityClass":"net.ooder.esd.bean.CustomViewBean","groupName":"dsm_view_entity_AggEntityInfo","id":"dsm_view_entity_AggEntityInfo","imageClass":"ri-node-tree","initFold":true,"tagVar":{"name":"aggEntityInfo"},"title":"菜单配置（null)"},{"bindClass":["net.ooder.dsm.aggregation.config.menu.tree.AggMenuMethodsNavService"],"bindClassName":"net.ooder.dsm.aggregation.config.menu.tree.AggMenuMethodsNavService","caption":"菜单项配置","closeBtn":false,"dynDestory":true,"dynLoad":true,"entityClass":"net.ooder.dsm.aggregation.config.menu.tree.AggMenuMainNavItem","groupName":"AggMenuMainNavItem_MethodConfig_dsm_agg_menu_config_LoadAllMethod","id":"MethodConfig","imageClass":"ri-settings-line","initFold":false,"tagVar":{"name":"MethodConfig"},"title":"菜单项配置"},{"bindClass":["net.ooder.dsm.aggregation.config.entity.tree.EntityMethodService"],"bindClassName":"net.ooder.dsm.aggregation.config.entity.tree.EntityMethodService","caption":"菜单配置（null)","closeBtn":false,"dynDestory":true,"dynLoad":true,"entityClass":"net.ooder.dsm.aggregation.config.menu.tree.MenuItemViewBean","groupName":"dsm_agg_entity_config_LoadNav","id":"dsm_agg_entity_config_LoadNav","imageClass":"ri-node-tree","initFold":true,"tagVar":{"name":"loadNav"},"title":"菜单配置（null)"},{"bindClass":["net.ooder.dsm.aggregation.config.menu.tree.AggMenuWebService"],"bindClassName":"net.ooder.dsm.aggregation.config.menu.tree.AggMenuWebService","caption":"Web配置","closeBtn":false,"dynDestory":true,"dynLoad":true,"entityClass":"net.ooder.dsm.aggregation.config.menu.tree.AggMenuNavItem","groupName":"AggMenuNavItem_WebMethodConfig_dsm_agg_menu_config_method_ConfigBaseInfo","id":"WebMethodConfig","imageClass":"spafont spa-icon-c-webapi","initFold":false,"tagVar":{"name":"WebMethodConfig"},"title":"Web配置"},{"bindClass":["net.ooder.dsm.aggregation.config.entity.tree.AggEntityViewService"],"bindClassName":"net.ooder.dsm.aggregation.config.entity.tree.AggEntityViewService","caption":"菜单配置（null)","closeBtn":false,"dynDestory":true,"dynLoad":true,"entityClass":"net.ooder.dsm.aggregation.config.menu.tree.RouteToTypeViewBean","groupName":"dsm_agg_entity_LoadChildView","id":"dsm_agg_entity_LoadChildView","imageClass":"ri-node-tree","initFold":true,"tagVar":{"name":"loadChildView"},"title":"菜单配置（null)"},{"bindClass":["net.ooder.dsm.aggregation.config.entity.tree.AggEntityTreeNavService"],"bindClassName":"net.ooder.dsm.aggregation.config.entity.tree.AggEntityTreeNavService","caption":"菜单配置（null)","closeBtn":false,"dynDestory":false,"dynLoad":false,"entityClass":"net.ooder.esd.bean.CustomAPICallBean","groupName":"dsm_agg_entity_config_nav_LoadEntity","id":"dsm_agg_entity_config_nav_LoadEntity","imageClass":"ri-node-tree","tagVar":{"name":"loadEntity"},"title":"菜单配置（null)"},{"bindClass":["net.ooder.dsm.aggregation.config.menu.tree.AggMenuConfigNavService"],"bindClassName":"net.ooder.dsm.aggregation.config.menu.tree.AggMenuConfigNavService","caption":"菜单配置","closeBtn":false,"dynDestory":true,"dynLoad":true,"entityClass":"net.ooder.dsm.aggregation.config.menu.tree.AggMenuNavItem","groupName":"AggMenuNavItem_MenuMethodConfig_dsm_agg_menu_config_method_group_AggMenuConfigView","id":"MenuMethodConfig","imageClass":"spafont spa-icon-project","initFold":false,"tagVar":{"name":"MenuMethodConfig"},"title":"菜单配置"},{"bindClass":["net.ooder.dsm.aggregation.config.menu.tree.AggMenuMethodNavService"],"bindClassName":"net.ooder.dsm.aggregation.config.menu.tree.AggMenuMethodNavService","caption":"方法调用","closeBtn":false,"dynDestory":true,"dynLoad":true,"entityClass":"net.ooder.dsm.aggregation.config.menu.tree.AggMenuNavItem","groupName":"AggMenuNavItem_CustomMethodConfig_dsm_agg_menu_config_method_LoadMenuMethod","id":"CustomMethodConfig","imageClass":"ri-settings-line","initFold":true,"tagVar":{"name":"CustomMethodConfig"},"title":"方法调用"},{"bindClass":["net.ooder.dsm.aggregation.config.menu.tree.AggMenuConfigTree"],"bindClassName":"net.ooder.dsm.aggregation.config.menu.tree.AggMenuConfigTree","caption":"AggMenuConfigTree","closeBtn":false,"dynDestory":false,"dynLoad":false,"entityClass":"net.ooder.dsm.aggregation.config.menu.tree.AggMenuConfigTree","id":"null","imageClass":"ri-settings-3-line","tagVar":{"name":"aggConfigTree"},"title":"AggMenuConfigTree"},{"bindClass":["java.lang.Void"],"caption":"loadNav","groupName":"dsm_agg_entity_config_LoadNav","id":"loadNav","imageClass":"ri-tree-line","initFold":false,"sub":[{"bindClass":["net.ooder.dsm.aggregation.config.entity.ref.AggRefService"],"bindClassName":"net.ooder.dsm.aggregation.config.entity.ref.AggRefService","caption":"loadNav（null)","closeBtn":false,"dynDestory":true,"dynLoad":false,"entityClass":"net.ooder.esd.dsm.aggregation.ref.AggEntityRef","groupName":"dsm_agg_entity_config_ref_RefInfo","id":"loadNav_dsm_agg_entity_config_ref_RefInfo","imageClass":"ri-node-tree","initFold":true,"tagVar":{"name":"refInfo"},"title":"loadNav（null)"},{"bindClass":["net.ooder.dsm.aggregation.config.entity.tree.field.AggEntityFieldService"],"bindClassName":"net.ooder.dsm.aggregation.config.entity.tree.field.AggEntityFieldService","caption":"loadNav（null)","closeBtn":false,"dynDestory":false,"dynLoad":false,"entityClass":"net.ooder.esd.dsm.aggregation.FieldAggConfig","groupName":"dsm_agg_field_FieldInfo","id":"loadNav_dsm_agg_field_FieldInfo","imageClass":"ri-node-tree","tagVar":{"name":"fieldInfo"},"title":"loadNav（null)"},{"bindClass":["net.ooder.dsm.aggregation.config.entity.AggEntityInfoService","net.ooder.dsm.aggregation.config.entity.tree.AggEntityViewNavService"],"bindClassName":"net.ooder.dsm.aggregation.config.entity.AggEntityInfoService","caption":"方法调用","closeBtn":false,"dynDestory":true,"dynLoad":true,"entityClass":"net.ooder.dsm.aggregation.config.entity.tree.AggEntityNavItem","groupName":"AggEntityNavItem_ViewMethodConfig_dsm_agg_entity_config_view_LoadView","id":"loadNav_ViewMethodConfig","imageClass":"spafont spa-icon-project","initFold":false,"tagVar":{"name":"ViewMethodConfig"},"title":"方法调用"},{"bindClass":["net.ooder.dsm.aggregation.config.entity.AggEntityInfoService"],"bindClassName":"net.ooder.dsm.aggregation.config.entity.AggEntityInfoService","caption":"loadNav（null)","closeBtn":false,"dynDestory":true,"dynLoad":true,"entityClass":"net.ooder.esd.bean.CustomViewBean","groupName":"dsm_view_entity_AggEntityInfo","id":"loadNav_dsm_view_entity_AggEntityInfo","imageClass":"ri-node-tree","initFold":true,"tagVar":{"name":"aggEntityInfo"},"title":"loadNav（null)"},{"bindClass":["net.ooder.dsm.aggregation.config.entity.tree.EntityMethodService"],"bindClassName":"net.ooder.dsm.aggregation.config.entity.tree.EntityMethodService","caption":"控制调用","closeBtn":false,"dynDestory":false,"dynLoad":true,"entityClass":"net.ooder.dsm.aggregation.config.entity.tree.AggEntityMethodNavItem","groupName":"AggEntityMethodNavItem_ViewConfig_dsm_agg_entity_config_LoadNav","id":"loadNav_ViewConfig","imageClass":"spafont spa-icon-c-webapi","initFold":true,"tagVar":{"name":"ViewConfig"},"title":"控制调用"},{"bindClass":["net.ooder.dsm.aggregation.config.entity.tree.EntityMethodService"],"bindClassName":"net.ooder.dsm.aggregation.config.entity.tree.EntityMethodService","caption":"loadNav（null)","closeBtn":false,"dynDestory":false,"dynLoad":false,"entityClass":"net.ooder.esd.bean.MethodConfig","groupName":"dsm_agg_entity_config_LoadNav","id":"loadNav_dsm_agg_entity_config_LoadNav","imageClass":"ri-node-tree","initFold":true,"tagVar":{"name":"loadNav"},"title":"loadNav（null)"},{"bindClass":["net.ooder.dsm.aggregation.config.entity.AggEntityInfoService","net.ooder.dsm.aggregation.config.entity.tree.AggEntityMethodNavService"],"bindClassName":"net.ooder.dsm.aggregation.config.entity.AggEntityInfoService","caption":"领域事件","closeBtn":false,"dynDestory":true,"dynLoad":true,"entityClass":"net.ooder.dsm.aggregation.config.entity.tree.AggEntityNavItem","groupName":"AggEntityNavItem_CustomMethodConfig_dsm_agg_entity_config_method_LoadMethod","id":"loadNav_CustomMethodConfig","imageClass":"ri-settings-line","initFold":true,"tagVar":{"name":"CustomMethodConfig"},"title":"领域事件"},{"bindClass":["net.ooder.dsm.aggregation.module.event.ModuleEventService"],"bindClassName":"net.ooder.dsm.aggregation.module.event.ModuleEventService","caption":"模块事件","closeBtn":false,"dynDestory":false,"dynLoad":false,"entityClass":"net.ooder.dsm.aggregation.module.ModuleEventNavItem","groupName":"ModuleEventNavItem_ModuleEventView_dsm_agg_entity_config_module_ModuleEvents","id":"loadNav_ModuleEventView","imageClass":"spafont spa-icon-event","initFold":false,"tagVar":{"name":"ModuleEventView"},"title":"模块事件"},{"bindClass":["net.ooder.dsm.aggregation.module.ModuleConfigService"],"bindClassName":"net.ooder.dsm.aggregation.module.ModuleConfigService","caption":"动作事件","closeBtn":false,"dynDestory":false,"dynLoad":true,"entityClass":"net.ooder.dsm.aggregation.config.entity.tree.AggEntityMethodNavItem","groupName":"AggEntityMethodNavItem_ModuleConfig_dsm_agg_module_config_LoadModuleNav","id":"loadNav_ModuleConfig","imageClass":"ri-play-circle-line","initFold":true,"tagVar":{"name":"ModuleConfig"},"title":"动作事件"},{"bindClass":["net.ooder.dsm.aggregation.api.method.service.MethodBaseService"],"bindClassName":"net.ooder.dsm.aggregation.api.method.service.MethodBaseService","caption":"接口配置","closeBtn":false,"dynDestory":false,"dynLoad":false,"entityClass":"net.ooder.dsm.aggregation.api.method.AggAPINavItem","groupName":"AggAPINavItem_APIConfig_dsm_agg_api_config_APIBaseMethodInfo","id":"loadNav_APIConfig","imageClass":"spafont spa-icon-c-webapi","initFold":true,"tagVar":{"name":"APIConfig"},"title":"接口配置"},{"bindClass":["net.ooder.dsm.aggregation.config.entity.tree.field.AggEntityFieldsService"],"bindClassName":"net.ooder.dsm.aggregation.config.entity.tree.field.AggEntityFieldsService","caption":"条目信息","closeBtn":false,"dynDestory":true,"dynLoad":true,"entityClass":"net.ooder.dsm.aggregation.config.entity.tree.AggEntityNavItem","groupName":"AggEntityNavItem_FieldsConfig_dsm_agg_field_LoadFieldList","id":"loadNav_FieldsConfig","imageClass":"spafont spa-icon-c-comboinput","initFold":true,"tagVar":{"name":"FieldsConfig"},"title":"条目信息"},{"bindClass":["net.ooder.dsm.aggregation.config.entity.tree.AggEntityViewService"],"bindClassName":"net.ooder.dsm.aggregation.config.entity.tree.AggEntityViewService","caption":"loadNav（null)","closeBtn":false,"dynDestory":true,"dynLoad":true,"entityClass":"net.ooder.esd.bean.CustomViewBean","groupName":"dsm_agg_entity_LoadChildView","id":"loadNav_dsm_agg_entity_LoadChildView","imageClass":"ri-node-tree","initFold":true,"tagVar":{"name":"loadChildView"},"title":"loadNav（null)"},{"bindClass":["net.ooder.dsm.aggregation.module.panel.ModuleWinService","net.ooder.dsm.aggregation.module.ModuleDeisignerService"],"bindClassName":"net.ooder.dsm.aggregation.module.panel.ModuleWinService","caption":"窗体画布","closeBtn":false,"dynDestory":false,"dynLoad":true,"entityClass":"net.ooder.dsm.aggregation.config.entity.tree.AggEntityMethodNavItem","groupName":"AggEntityMethodNavItem_DesignerConfig_dsm_agg_module_config_LoadWinItem","id":"loadNav_DesignerConfig","imageClass":"ri-paint-brush-line","initFold":true,"tagVar":{"name":"DesignerConfig"},"title":"窗体画布"},{"bindClass":["net.ooder.dsm.aggregation.module.action.ModuleActionService"],"bindClassName":"net.ooder.dsm.aggregation.module.action.ModuleActionService","caption":"常用动作","closeBtn":false,"dynDestory":false,"dynLoad":false,"entityClass":"net.ooder.dsm.aggregation.module.ModuleEventNavItem","groupName":"ModuleEventNavItem_ModuleActionView_dsm_agg_module_config_actions_ModuleActions","id":"loadNav_ModuleActionView","imageClass":"spafont spa-icon-action","initFold":false,"tagVar":{"name":"ModuleActionView"},"title":"常用动作"},{"bindClass":["net.ooder.dsm.aggregation.api.method.service.MethodEventService"],"bindClassName":"net.ooder.dsm.aggregation.api.method.service.MethodEventService","caption":"监听事件","closeBtn":false,"dynDestory":false,"dynLoad":false,"entityClass":"net.ooder.dsm.aggregation.api.method.AggAPINavItem","groupName":"AggAPINavItem_EventConfig_dsm_agg_api_config_events_CustomEvents","id":"loadNav_EventConfig","imageClass":"spafont spa-icon-event","initFold":false,"tagVar":{"name":"EventConfig"},"title":"监听事件"},{"bindClass":["net.ooder.dsm.aggregation.api.method.service.ViewEventService"],"bindClassName":"net.ooder.dsm.aggregation.api.method.service.ViewEventService","caption":"视图绑定事件","closeBtn":false,"dynDestory":false,"dynLoad":false,"entityClass":"net.ooder.dsm.aggregation.api.method.AggAPINavItem","groupName":"AggAPINavItem_BindEventConfig_dsm_agg_api_config_events_view_ViewEvents","id":"loadNav_BindEventConfig","imageClass":"spafont spa-icon-c-databinder","initFold":false,"tagVar":{"name":"BindEventConfig"},"title":"视图绑定事件"},{"bindClass":["net.ooder.dsm.aggregation.api.method.service.APIBindMenuService"],"bindClassName":"net.ooder.dsm.aggregation.api.method.service.APIBindMenuService","caption":"关联按钮","closeBtn":false,"dynDestory":false,"dynLoad":false,"entityClass":"net.ooder.dsm.aggregation.api.method.AggAPINavItem","groupName":"AggAPINavItem_BindConfig_dsm_agg_api_config_bindmenu_APIBindMenus","id":"loadNav_BindConfig","imageClass":"spafont spa-icon-values","initFold":false,"tagVar":{"name":"BindConfig"},"title":"关联按钮"},{"bindClass":["net.ooder.dsm.aggregation.config.entity.info.AggregationInfoService"],"bindClassName":"net.ooder.dsm.aggregation.config.entity.info.AggregationInfoService","caption":"聚合配置","closeBtn":false,"dynDestory":false,"dynLoad":true,"entityClass":"net.ooder.dsm.aggregation.config.entity.tree.AggEntityMethodNavItem","groupName":"AggEntityMethodNavItem_AggInfo_dsm_manager_agg_entity_AggInfo","id":"loadNav_AggInfo","imageClass":"spafont spa-icon-rendermode","initFold":true,"tagVar":{"name":"AggInfo"},"title":"聚合配置"},{"bindClass":["net.ooder.dsm.aggregation.api.method.service.APIBindActionService"],"bindClassName":"net.ooder.dsm.aggregation.api.method.service.APIBindActionService","caption":"绑定动作","closeBtn":false,"dynDestory":false,"dynLoad":false,"entityClass":"net.ooder.dsm.aggregation.api.method.AggAPINavItem","groupName":"AggAPINavItem_BindActionConfig_dsm_agg_api_config_bindactions_BindActions","id":"loadNav_BindActionConfig","imageClass":"spafont spa-icon-values","initFold":false,"tagVar":{"name":"BindActionConfig"},"title":"绑定动作"},{"bindClass":["net.ooder.dsm.aggregation.api.method.service.CustomActionService"],"bindClassName":"net.ooder.dsm.aggregation.api.method.service.CustomActionService","caption":"回调动作","closeBtn":false,"dynDestory":false,"dynLoad":false,"entityClass":"net.ooder.dsm.aggregation.api.method.AggAPINavItem","groupName":"AggAPINavItem_CallBackActionConfig_dsm_agg_api_config_customactions_CustomActions","id":"loadNav_CallBackActionConfig","imageClass":"spafont spa-icon-action","initFold":false,"tagVar":{"name":"CallBackActionConfig"},"title":"回调动作"}],"title":"loadNav"}],"tabindex":0,"title":"AggMenuConfigTree"}])
                .setSelMode("single")
                .setSingleOpen(false)
                .setTabindex(0)
                .setVisibility("visible")
            ;
            AggMenuConfigTreePanel.append(AggMenuConfigTree);

            // getAggConfigTree
            var getAggConfigTree = ood.create("ood.UI.Tabs")
                .setHost(AggConfigTreeLAYOUT, "getAggConfigTree")
                .setName("getAggConfigTree")
                .setActiveLast(true)
                .setAutoReload(true)
                .setAutoSave(false)
                .setBarLocation("top")
                .setBarSize("2.5em")
                .setBorderType("none")
                .setCaption("菜单配置")
                .setImageClass("ri-settings-3-line")
                .setInitFold(false)
                .setItems([])
                .setNoHandler(true)
                .setSelMode("single")
                .setSingleOpen(true)
                .setTabindex(1)
                .setTagCmdsAlign("floatright")
                .setTagVar({})
            ;
            AggConfigTreeLAYOUT.append(getAggConfigTree);

            return children;
        }
    }
});
