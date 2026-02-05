// 自动生成的 FieldContextMenuTree 组件
ood.Class("ooder.FieldContextMenuTree", "ood.Module", {
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
        "caption": "导入菜单动作",
        "currComponentAlias": "FieldContextMenuTree",
        "dock": "fill",
        "title": "导入菜单动作"
},

        // 事件定义
        events: {
        "onDestroy": {
                "actions": [
                        {
                                "args": [
                                        "{page.getParentModule().setData()}",
                                        null,
                                        null,
                                        "{page.FieldContextMenuTree.getCallBackValue()}"
                                ],
                                "conditions": [
                                        {
                                                "symbol": "!=",
                                                "left": "{page.FieldContextMenuTreegetCaptionValue()}",
                                                "right": "{}"
                                        }
                                ],
                                "desc": "填充返回值",
                                "eventClass": "net.ooder.esd.annotation.event.ModuleEventEnum",
                                "eventValue": "onDestroy",
                                "method": "call",
                                "target": "callback",
                                "type": "other"
                        }
                ]
        }
},

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

            // FieldContextMenuTreeMain
            var FieldContextMenuTreeMain = ood.create("ood.UI.Dialog")
                .setHost(host, "FieldContextMenuTreeMain")
                .setName("FieldContextMenuTreeMain")
                .setCaption("导入菜单动作")
                .setDefaultFocus(false)
                .setDisableClickEffect(false)
                .setDisableHoverEffect(false)
                .setDisableTips(false)
                .setDisabled(false)
                .setDock("none")
                .setHeight("36.0em")
                .setLeft("220.0")
                .setShowEffects("Classic")
                .setTabindex(1)
                .setTop("100.0")
                .setVisibility("visible")
                .setWidth("400.0")
            ;
            append(FieldContextMenuTreeMain);

            // RELOAD
            var RELOAD = ood.create("ood.APICaller")
                .setHost(host, "RELOAD")
                .setName("RELOAD")
                .setAllform(true)
                .setAutoRun(true)
                .setCheckRequired(false)
                .setCheckValid(false)
                .setDesc("导入菜单动作")
                .setImageClass("ri-code-box-line")
                .setIsAllform(true)
                .setMethodName("getFieldContextMenuTree")
                .setProxyType("auto")
                .setQueryAsync(true)
                .setQueryMethod("POST")
                .setQueryURL("/dsm/view/config/form/field/contextmenu/FieldContextMenuTree")
                .setRequestDataSource([{"name":"PAGECTX","path":"","type":"FORM"},{"name":"FieldContextMenuTreeMain","path":"","type":"FORM"}])
                .setRequestType("FORM")
                .setResponseCallback([])
                .setResponseDataTarget([{"name":"FieldContextMenuTree","path":"data","type":"TREEVIEW"},{"name":"PAGECTX","path":"data","type":"FORM"}])
                .setResponseType("JSON")
                .setSourceClassName("net.ooder.dsm.view.config.form.field.contextmenu.menuclass.ContextFieldMenuService")
                .setTabindex(2)
                .setTips("导入菜单动作")
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
                .setMethodName("addFieldContextMenu")
                .setProxyType("auto")
                .setQueryAsync(true)
                .setQueryMethod("POST")
                .setQueryURL("/dsm/view/config/form/field/contextmenu/addFieldContextMenu")
                .setRequestDataSource([{"name":"FieldContextMenuTree","path":"","type":"FORM"},{"name":"FieldContextMenuTreeMain","path":"","type":"FORM"},{"name":"PAGECTX","path":"","type":"FORM"}])
                .setRequestType("FORM")
                .setResponseCallback([])
                .setResponseDataTarget([])
                .setResponseType("JSON")
                .setSourceClassName("net.ooder.dsm.view.config.form.field.contextmenu.menuclass.ContextFieldMenuService")
                .setTabindex(3)
                .setTips("保存")
            ;
            append(SAVE);

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
                .setIsPid(false)
                .setPid(false)
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

            // projectName
            var projectName = ood.create("ood.UI.HiddenInput")
                .setHost(PAGECTX, "projectName")
                .setName("projectName")
                .setFormField(true)
                .setIsPid(false)
                .setPid(false)
                .setTabindex(5)
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
                .setTabindex(6)
            ;
            PAGECTX.append(domainId);

            // projectVersionName
            var projectVersionName = ood.create("ood.UI.HiddenInput")
                .setHost(PAGECTX, "projectVersionName")
                .setName("projectVersionName")
                .setFormField(true)
                .setIsPid(false)
                .setPid(false)
                .setTabindex(7)
                .setValue("DSMdsmVVVERSION0")
            ;
            PAGECTX.append(projectVersionName);

            // menuType
            var menuType = ood.create("ood.UI.HiddenInput")
                .setHost(PAGECTX, "menuType")
                .setName("menuType")
                .setFormField(true)
                .setIsPid(true)
                .setPid(true)
                .setTabindex(8)
            ;
            PAGECTX.append(menuType);

            // methodName
            var methodName = ood.create("ood.UI.HiddenInput")
                .setHost(PAGECTX, "methodName")
                .setName("methodName")
                .setFormField(true)
                .setIsPid(true)
                .setPid(true)
                .setTabindex(9)
            ;
            PAGECTX.append(methodName);

            // euClassName
            var euClassName = ood.create("ood.UI.HiddenInput")
                .setHost(PAGECTX, "euClassName")
                .setName("euClassName")
                .setFormField(true)
                .setIsPid(true)
                .setPid(true)
                .setTabindex(10)
            ;
            PAGECTX.append(euClassName);

            // fieldName
            var fieldName = ood.create("ood.UI.HiddenInput")
                .setHost(PAGECTX, "fieldName")
                .setName("fieldName")
                .setFormField(true)
                .setIsPid(false)
                .setPid(false)
                .setTabindex(11)
            ;
            PAGECTX.append(fieldName);

            // fieldCaption
            var fieldCaption = ood.create("ood.UI.HiddenInput")
                .setHost(PAGECTX, "fieldCaption")
                .setName("fieldCaption")
                .setFormField(true)
                .setIsPid(false)
                .setPid(false)
                .setTabindex(12)
            ;
            PAGECTX.append(fieldCaption);

            // FieldContextMenuTree
            var FieldContextMenuTree = ood.create("ood.UI.TreeView")
                .setHost(FieldContextMenuTreeMain, "FieldContextMenuTree")
                .setName("FieldContextMenuTree")
                .setCaption("AllFieldContextMenuTree")
                .setDefaultFocus(false)
                .setDisableClickEffect(false)
                .setDisableHoverEffect(false)
                .setDisableTips(false)
                .setDisabled(false)
                .setDock("none")
                .setFormField(true)
                .setInitFold(false)
                .setItems([{"bindClass":["java.lang.Void"],"caption":"AllFieldContextMenuTree","id":"fieldContextMenuTree","imageClass":"ri-tree-line","initFold":false,"sub":[{"bindClass":["net.ooder.esd.custom.ESDClass"],"bindClassName":"net.ooder.esd.custom.ESDClass","caption":"导入菜单动作（ESDClass)","closeBtn":false,"dynDestory":false,"dynLoad":false,"entityClass":"net.ooder.esd.custom.ESDClass","id":"null","imageClass":"ri-grid-line","tagVar":{"name":"null"},"title":"导入菜单动作（ESDClass)"},{"bindClass":["net.ooder.dsm.aggregation.config.menu.pop.AggMenuTreeService"],"bindClassName":"net.ooder.dsm.aggregation.config.menu.pop.AggMenuTreeService","caption":"导入菜单动作（null)","closeBtn":false,"dynDestory":true,"dynLoad":false,"entityClass":"net.ooder.esd.annotation.menu.CustomMenuType","groupName":"dsm_agg_menu_config_LoadChildMenu","id":"dsm_agg_menu_config_LoadChildMenu","imageClass":"ri-node-tree","tagVar":{"name":"loadChildMenu"},"title":"导入菜单动作（null)"},{"bindClass":["java.lang.Void"],"caption":"loadChildMenu","groupName":"dsm_agg_menu_config_LoadChildMenu","id":"loadChildMenu","imageClass":"ri-tree-line","initFold":false,"sub":[{"bindClass":["net.ooder.esd.custom.ESDClass"],"bindClassName":"net.ooder.esd.custom.ESDClass","caption":"loadChildMenu（ESDClass)","closeBtn":false,"dynDestory":false,"dynLoad":false,"entityClass":"net.ooder.esd.custom.ESDClass","id":"loadChildMenu_null","imageClass":"ri-table-line","tagVar":{"name":"null"},"title":"loadChildMenu（ESDClass)"},{"bindClass":["net.ooder.dsm.aggregation.config.menu.pop.AggMenuTreeService"],"bindClassName":"net.ooder.dsm.aggregation.config.menu.pop.AggMenuTreeService","caption":"loadChildMenu（null)","closeBtn":false,"dynDestory":true,"dynLoad":false,"entityClass":"net.ooder.esd.annotation.menu.CustomMenuType","groupName":"dsm_agg_menu_config_LoadChildMenu","id":"loadChildMenu_dsm_agg_menu_config_LoadChildMenu","imageClass":"ri-node-tree","tagVar":{"name":"loadChildMenu"},"title":"loadChildMenu（null)"}],"title":"loadChildMenu"}],"tabindex":0,"title":"AllFieldContextMenuTree"}])
                .setSelMode("multibycheckbox")
                .setSingleOpen(false)
                .setTabindex(0)
                .setVisibility("visible")
            ;
            FieldContextMenuTreeMain.append(FieldContextMenuTree);

            // StatusBottomBlock
            var StatusBottomBlock = ood.create("ood.UI.Block")
                .setHost(FieldContextMenuTreeMain, "StatusBottomBlock")
                .setName("StatusBottomBlock")
                .setBorderType("none")
                .setComboType("STATUSBUTTONS")
                .setDock("bottom")
                .setHeight("3.0em")
                .setOverflow("hidden")
                .setTabindex(1)
            ;
            FieldContextMenuTreeMain.append(StatusBottomBlock);

            // StatusBottom
            var StatusBottom = ood.create("ood.UI.StatusButtons")
                .setHost(StatusBottomBlock, "StatusBottom")
                .setName("StatusBottom")
                .setBorderType("none")
                .setDock("fill")
                .setItemType("button")
                .setItemWidth("auto")
                .setItems([{"caption":"确定","expression":"true","iconColor":"#195ead","id":"SAVE_button","imageClass":"ri-save-line","index":0,"itemType":"button","tabindex":0,"tagVar":{"name":"SAVE","clazz":"net.ooder.esd.annotation.menu.TreeMenu"},"tips":"确定","title":"确定"},{"caption":"关闭","expression":"true","iconColor":"#1f8d9b","id":"CLOSE_button","imageClass":"ri-close-line","index":0,"itemType":"button","tabindex":1,"tagVar":{"name":"CLOSE","clazz":"net.ooder.esd.annotation.menu.TreeMenu"},"tips":"关闭","title":"关闭"}])
                .setMenuType("BOTTOMBAR")
                .setPosition("static")
                .setShowCaption(true)
                .setTabindex(0)
                .setWidth("auto")
            ;
            StatusBottomBlock.append(StatusBottom);

            return children;
        }
    }
});
