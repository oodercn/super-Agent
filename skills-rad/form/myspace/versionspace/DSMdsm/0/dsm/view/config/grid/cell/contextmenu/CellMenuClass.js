// 自动生成的 CellMenuClass 组件
ood.Class("ooder.CellMenuClass", "ood.Module", {
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
        "caption": "单元格菜单选择",
        "currComponentAlias": "CellMenuClass",
        "dock": "fill",
        "title": "单元格菜单选择"
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

            // CellMenuClassMain
            var CellMenuClassMain = ood.create("ood.UI.Block")
                .setHost(host, "CellMenuClassMain")
                .setName("CellMenuClassMain")
                .setBorderType("none")
                .setDock("fill")
                .setPanelBgClr("transparent")
                .setTabindex(1)
            ;
            append(CellMenuClassMain);

            // DELETE
            var DELETE = ood.create("ood.APICaller")
                .setHost(host, "DELETE")
                .setName("DELETE")
                .setAllform(false)
                .setAutoRun(false)
                .setCheckRequired(false)
                .setCheckValid(false)
                .setDesc("删除")
                .setImageClass("ri-code-box-line")
                .setIsAllform(false)
                .setMethodName("delContextMenu")
                .setProxyType("auto")
                .setQueryAsync(true)
                .setQueryMethod("POST")
                .setQueryURL("/dsm/view/config/grid/cell/contextmenu/delContextMenu")
                .setRequestDataSource([{"name":"CellMenuClass","path":"","type":"TREEGRID"},{"name":"PAGECTX","path":"","type":"FORM"}])
                .setRequestType("FORM")
                .setResponseCallback([])
                .setResponseDataTarget([])
                .setResponseType("JSON")
                .setSourceClassName("net.ooder.dsm.view.config.grid.cell.contextmenu.menuclass.ContextCellMenuService")
                .setTabindex(2)
                .setTips("删除")
            ;
            append(DELETE);

            // RELOAD
            var RELOAD = ood.create("ood.APICaller")
                .setHost(host, "RELOAD")
                .setName("RELOAD")
                .setAutoRun(true)
                .setDesc("单元格菜单选择")
                .setImageClass("ri-git-branch-line")
                .setMethodName("getCellMenuClass")
                .setProxyType("auto")
                .setQueryMethod("POST")
                .setQueryURL("/dsm/view/config/grid/cell/contextmenu/CellMenuClass")
                .setRequestDataSource([{"name":"PAGECTX","path":"","type":"FORM"}])
                .setRequestType("FORM")
                .setResponseCallback([])
                .setResponseDataTarget([{"name":"CellMenuClass","path":"data","type":"TREEGRID"},{"name":"PAGECTX","path":"data","type":"FORM"}])
                .setResponseType("JSON")
                .setSourceClassName("net.ooder.dsm.view.config.grid.cell.contextmenu.ContextCellMenuNav")
                .setTabindex(3)
                .setTips("单元格菜单选择")
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

            // menuClass
            var menuClass = ood.create("ood.UI.HiddenInput")
                .setHost(PAGECTX, "menuClass")
                .setName("menuClass")
                .setFormField(true)
                .setIsPid(true)
                .setPid(true)
                .setTabindex(8)
            ;
            PAGECTX.append(menuClass);

            // desc
            var desc = ood.create("ood.UI.HiddenInput")
                .setHost(PAGECTX, "desc")
                .setName("desc")
                .setFormField(true)
                .setIsPid(false)
                .setPid(false)
                .setTabindex(9)
            ;
            PAGECTX.append(desc);

            // CellMenuClass
            var CellMenuClass = ood.create("ood.UI.TreeGrid")
                .setHost(CellMenuClassMain, "CellMenuClass")
                .setName("CellMenuClass")
                .setDefaultFocus(false)
                .setDesc("getCellMenuClass")
                .setDisableClickEffect(false)
                .setDisableHoverEffect(false)
                .setDisableTips(false)
                .setDisabled(false)
                .setHeader([{"caption":"fieldname","hidden":true,"id":"fieldname","tips":"fieldname","title":"fieldname","type":"input"},{"caption":"currentClassName","hidden":true,"id":"currentClassName","tips":"currentClassName","title":"currentClassName","type":"input"},{"caption":"xpath","hidden":true,"id":"xpath","tips":"xpath","title":"xpath","type":"input"},{"caption":"sourceMethodName","hidden":true,"id":"sourceMethodName","tips":"sourceMethodName","title":"sourceMethodName","type":"input"},{"caption":"sourceClassName","hidden":true,"id":"sourceClassName","tips":"sourceClassName","title":"sourceClassName","type":"input"},{"caption":"domainId","hidden":true,"id":"domainId","tips":"domainId","title":"domainId","type":"input"},{"caption":"menuClass","hidden":true,"id":"menuClass","tips":"menuClass","title":"menuClass","type":"input"},{"caption":"名称","hidden":false,"id":"name","tips":"名称","title":"名称","type":"input","width":"150.0"},{"caption":"包名","flexSize":true,"hidden":false,"id":"packageName","tips":"包名","title":"包名","type":"input","width":"150.0"},{"caption":"描述","hidden":true,"id":"desc","tips":"描述","title":"描述","type":"input"}])
                .setHeaderHeight("2.0em")
                .setItems([])
                .setRowHandler(true)
                .setRowHandlerWidth("5.0em")
                .setRowHeight("3.0em")
                .setRowNumbered(true)
                .setSelMode("multibycheckbox")
                .setShowHeader(true)
                .setTabindex(0)
                .setUidColumn("menuClass")
                .setValueSeparator(";")
                .setVisibility("visible")
            ;
            CellMenuClassMain.append(CellMenuClass);

            // CellMenuClass_PAGEBARDiv
            var CellMenuClass_PAGEBARDiv = ood.create("ood.UI.Div")
                .setHost(CellMenuClassMain, "CellMenuClass_PAGEBARDiv")
                .setName("CellMenuClass_PAGEBARDiv")
                .setDock("bottom")
                .setHeight("2.5em")
                .setTabindex(1)
            ;
            CellMenuClassMain.append(CellMenuClass_PAGEBARDiv);

            // CellMenuClass_PAGEBAR
            var CellMenuClass_PAGEBAR = ood.create("ood.UI.PageBar")
                .setHost(CellMenuClass_PAGEBARDiv, "CellMenuClass_PAGEBAR")
                .setName("CellMenuClass_PAGEBAR")
                .setCaption("分页")
                .setPageCount(20)
                .setShowMoreBtns(true)
                .setTabindex(0)
                .setTips("分页")
                .setTitle("分页")
            ;
            CellMenuClass_PAGEBARDiv.append(CellMenuClass_PAGEBAR);

            // MENUBARMenu
            var MENUBARMenu = ood.create("ood.UI.MenuBar")
                .setHost(CellMenuClassMain, "MENUBARMenu")
                .setName("MENUBARMenu")
                .setAutoFontColor(false)
                .setAutoIconColor(true)
                .setAutoItemColor(false)
                .setDock("top")
                .setHAlign("left")
                .setIndex(100)
                .setItems([{"bindClass":[],"caption":"删除","expression":"true","iconColor":"#e04d7f","id":"DELETE_button","imageClass":"ri-close-line","tabindex":0,"tagVar":{},"tips":"删除","title":"删除","type":"button"},{"bindClass":[],"caption":"添加","expression":"true","iconColor":"#46C37B","id":"ADD_button","imageClass":"ri-add-line","tabindex":1,"tagVar":{},"tips":"添加","title":"添加","type":"button"},{"bindClass":[],"caption":"刷新","expression":"true","iconColor":"#195ead","id":"RELOAD_button","imageClass":"ri-refresh-line","tabindex":2,"tagVar":{},"tips":"刷新","title":"刷新","type":"button"}])
                .setLazy(false)
                .setShowCaption(true)
                .setTabindex(2)
            ;
            CellMenuClassMain.append(MENUBARMenu);

            return children;
        }
    }
});
