// 自动生成的 CodeTempsNav 组件
ood.Class("ooder.CodeTempsNav", "ood.Module", {
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
        "caption": "JAVA模板",
        "dock": "fill",
        "title": "JAVA模板"
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

            // CodeTempsNavMain
            var CodeTempsNavMain = ood.create("ood.UI.Block")
                .setHost(host, "CodeTempsNavMain")
                .setName("CodeTempsNavMain")
                .setBorderType("none")
                .setDock("fill")
                .setPanelBgClr("transparent")
                .setTabindex(1)
            ;
            append(CodeTempsNavMain);

            // RELOAD
            var RELOAD = ood.create("ood.APICaller")
                .setHost(host, "RELOAD")
                .setName("RELOAD")
                .setAllform(false)
                .setAutoRun(true)
                .setCheckRequired(false)
                .setCheckValid(false)
                .setDesc("JAVA模板")
                .setImageClass("ri-tools-line")
                .setIsAllform(false)
                .setMethodName("getCodeTempsNav")
                .setProxyType("auto")
                .setQueryAsync(true)
                .setQueryMethod("POST")
                .setQueryURL("/dsm/temp/website/CodeTempsNav")
                .setRequestDataSource([{"name":"PAGECTX","path":"","type":"FORM"},{"name":"WebSiteTreeViewPanel","path":"","type":"FORM"}])
                .setRequestType("FORM")
                .setResponseCallback([])
                .setResponseDataTarget([{"name":"WebSiteTreeView","path":"data","type":"TREEVIEW"},{"name":"PAGECTX","path":"data","type":"FORM"}])
                .setResponseType("JSON")
                .setSourceClassName("net.ooder.dsm.website.manager.WebSiteNav")
                .setTabindex(2)
                .setTips("JAVA模板")
            ;
            append(RELOAD);

            // projectName
            var projectName = ood.create("ood.UI.HiddenInput")
                .setHost(PAGECTX, "projectName")
                .setName("projectName")
                .setFormField(true)
                .setIsPid(false)
                .setPid(false)
                .setTabindex(0)
                .setValue("DSMdsmVVVERSION0")
            ;
            PAGECTX.append(projectName);

            // dsmTempId
            var dsmTempId = ood.create("ood.UI.HiddenInput")
                .setHost(PAGECTX, "dsmTempId")
                .setName("dsmTempId")
                .setFormField(true)
                .setIsPid(true)
                .setPid(true)
                .setTabindex(1)
            ;
            PAGECTX.append(dsmTempId);

            // projectVersionName
            var projectVersionName = ood.create("ood.UI.HiddenInput")
                .setHost(PAGECTX, "projectVersionName")
                .setName("projectVersionName")
                .setFormField(true)
                .setIsPid(false)
                .setPid(false)
                .setTabindex(2)
                .setValue("DSMdsmVVVERSION0")
            ;
            PAGECTX.append(projectVersionName);

            // dsmTempType
            var dsmTempType = ood.create("ood.UI.HiddenInput")
                .setHost(PAGECTX, "dsmTempType")
                .setName("dsmTempType")
                .setFormField(true)
                .setIsPid(true)
                .setPid(true)
                .setTabindex(3)
            ;
            PAGECTX.append(dsmTempType);

            // customDomainType
            var customDomainType = ood.create("ood.UI.HiddenInput")
                .setHost(PAGECTX, "customDomainType")
                .setName("customDomainType")
                .setFormField(true)
                .setIsPid(true)
                .setPid(true)
                .setTabindex(4)
            ;
            PAGECTX.append(customDomainType);

            // dsmType
            var dsmType = ood.create("ood.UI.HiddenInput")
                .setHost(PAGECTX, "dsmType")
                .setName("dsmType")
                .setFormField(true)
                .setIsPid(true)
                .setPid(true)
                .setTabindex(5)
            ;
            PAGECTX.append(dsmType);

            // repositoryType
            var repositoryType = ood.create("ood.UI.HiddenInput")
                .setHost(PAGECTX, "repositoryType")
                .setName("repositoryType")
                .setFormField(true)
                .setIsPid(true)
                .setPid(true)
                .setTabindex(6)
            ;
            PAGECTX.append(repositoryType);

            // userSpace
            var userSpace = ood.create("ood.UI.HiddenInput")
                .setHost(PAGECTX, "userSpace")
                .setName("userSpace")
                .setFormField(true)
                .setIsPid(true)
                .setPid(true)
                .setTabindex(7)
            ;
            PAGECTX.append(userSpace);

            // itemDomainType
            var itemDomainType = ood.create("ood.UI.HiddenInput")
                .setHost(PAGECTX, "itemDomainType")
                .setName("itemDomainType")
                .setFormField(true)
                .setIsPid(true)
                .setPid(true)
                .setTabindex(8)
            ;
            PAGECTX.append(itemDomainType);

            // viewType
            var viewType = ood.create("ood.UI.HiddenInput")
                .setHost(PAGECTX, "viewType")
                .setName("viewType")
                .setFormField(true)
                .setIsPid(true)
                .setPid(true)
                .setTabindex(9)
            ;
            PAGECTX.append(viewType);

            // aggregationType
            var aggregationType = ood.create("ood.UI.HiddenInput")
                .setHost(PAGECTX, "aggregationType")
                .setName("aggregationType")
                .setFormField(true)
                .setIsPid(true)
                .setPid(true)
                .setTabindex(10)
            ;
            PAGECTX.append(aggregationType);

            // javaTempId
            var javaTempId = ood.create("ood.UI.HiddenInput")
                .setHost(PAGECTX, "javaTempId")
                .setName("javaTempId")
                .setFormField(true)
                .setIsPid(true)
                .setPid(true)
                .setTabindex(11)
            ;
            PAGECTX.append(javaTempId);

            // CodeTempsNavLAYOUT
            var CodeTempsNavLAYOUT = ood.create("ood.UI.Layout")
                .setHost(CodeTempsNavMain, "CodeTempsNavLAYOUT")
                .setName("CodeTempsNavLAYOUT")
                .setBorderType("none")
                .setItems([{"bindClass":[],"flexSize":false,"folded":false,"id":"before","max":1000,"min":100,"overflow":"auto","pos":"before","size":260,"tabindex":0,"transparent":true},{"bindClass":[],"flexSize":true,"id":"main","max":300,"overflow":"auto","pos":"main","tabindex":1,"transparent":true}])
                .setTabindex(0)
                .setTransparent(true)
                .setType("horizontal")
            ;
            CodeTempsNavMain.append(CodeTempsNavLAYOUT);

            // WebSiteTreeViewPanel
            var WebSiteTreeViewPanel = ood.create("ood.UI.Block")
                .setHost(CodeTempsNavLAYOUT, "WebSiteTreeViewPanel")
                .setName("WebSiteTreeViewPanel")
                .setBorderType("none")
                .setDock("fill")
                .setTabindex(0)
            ;
            CodeTempsNavLAYOUT.append(WebSiteTreeViewPanel);

            // WebSiteTreeView
            var WebSiteTreeView = ood.create("ood.UI.TreeView")
                .setHost(WebSiteTreeViewPanel, "WebSiteTreeView")
                .setName("WebSiteTreeView")
                .setCaption("WebSiteTreeView")
                .setDefaultFocus(false)
                .setDisableClickEffect(false)
                .setDisableHoverEffect(false)
                .setDisableTips(false)
                .setDisabled(false)
                .setDock("none")
                .setFormField(true)
                .setInitFold(false)
                .setItems([{"bindClass":["java.lang.Void"],"caption":"WebSiteTreeView","id":"codeTempsNav","imageClass":"ri-tools-line","initFold":false,"pattern":"","sub":[{"bindClass":["net.ooder.dsm.website.temp.service.WebSiteDomainTreeService"],"bindClassName":"net.ooder.dsm.website.temp.service.WebSiteDomainTreeService","caption":"JAVA模板（null)","closeBtn":false,"dynDestory":true,"dynLoad":true,"entityClass":"net.ooder.esd.dsm.domain.enums.NavDomainType","groupName":"dsm_temp_website_AllCustomDomainTempGrid","id":"dsm_temp_website_AllCustomDomainTempGrid","imageClass":"ri-node-tree","pattern":"","tagVar":{"name":"allCustomDomainTempGrid"},"title":"JAVA模板（null)"},{"bindClass":["net.ooder.dsm.website.temp.service.WebSiteViewTreeService"],"bindClassName":"net.ooder.dsm.website.temp.service.WebSiteViewTreeService","caption":"JAVA模板（null)","closeBtn":false,"dynDestory":false,"dynLoad":false,"entityClass":"net.ooder.esd.annotation.ViewType","groupName":"dsm_temp_website_AllViewTempGrid","id":"dsm_temp_website_AllViewTempGrid","imageClass":"ri-node-tree","pattern":"","tagVar":{"name":"allViewTempGrid"},"title":"JAVA模板（null)"},{"bindClass":["net.ooder.dsm.website.temp.service.WebSiteAggregationService"],"bindClassName":"net.ooder.dsm.website.temp.service.WebSiteAggregationService","caption":"JAVA模板（null)","closeBtn":false,"dynDestory":false,"dynLoad":false,"entityClass":"net.ooder.annotation.AggregationType","groupName":"dsm_temp_website_WebSiteAggregation","id":"dsm_temp_website_WebSiteAggregation","imageClass":"ri-node-tree","pattern":"","tagVar":{"name":"webSiteAggregation"},"title":"JAVA模板（null)"},{"bindClass":["net.ooder.dsm.website.temp.service.WebSiteRepositoryAdminService"],"bindClassName":"net.ooder.dsm.website.temp.service.WebSiteRepositoryAdminService","caption":"JAVA模板（null)","closeBtn":false,"dynDestory":false,"dynLoad":false,"entityClass":"net.ooder.esd.dsm.enums.RepositoryType","groupName":"dsm_temp_website_WebSiteRepositoryAdmin","id":"dsm_temp_website_WebSiteRepositoryAdmin","imageClass":"ri-node-tree","pattern":"","tagVar":{"name":"webSiteRepositoryAdmin"},"title":"JAVA模板（null)"},{"bindClass":["net.ooder.dsm.website.temp.service.WebSiteDomainService"],"bindClassName":"net.ooder.dsm.website.temp.service.WebSiteDomainService","caption":"JAVA模板（null)","closeBtn":false,"dynDestory":false,"dynLoad":false,"entityClass":"net.ooder.esd.dsm.domain.enums.CustomDomainType","groupName":"dsm_temp_website_LoadChildCustomDomain","id":"dsm_temp_website_LoadChildCustomDomain","imageClass":"ri-node-tree","pattern":"","tagVar":{"name":"loadChildCustomDomain"},"title":"JAVA模板（null)"},{"bindClass":["net.ooder.dsm.website.temp.service.WebSiteAdminTempService"],"bindClassName":"net.ooder.dsm.website.temp.service.WebSiteAdminTempService","caption":"模板分类","closeBtn":false,"dynDestory":false,"dynLoad":false,"entityClass":"net.ooder.esd.dsm.enums.DSMType","groupName":"dsm_temp_website_LoadChildDSM","id":"dsm_temp_website_LoadChildDSM","imageClass":"ri-tools-line","pattern":"","tagVar":{"name":"loadChildDSM"},"title":"模板分类"},{"bindClass":["net.ooder.dsm.website.temp.service.WebSiteUserSpaceService"],"bindClassName":"net.ooder.dsm.website.temp.service.WebSiteUserSpaceService","caption":"JAVA模板（null)","closeBtn":false,"dynDestory":false,"dynLoad":false,"entityClass":"net.ooder.annotation.UserSpace","groupName":"dsm_temp_website_LoadUserSpaceType","id":"dsm_temp_website_LoadUserSpaceType","imageClass":"ri-node-tree","pattern":"","tagVar":{"name":"loadUserSpaceType"},"title":"JAVA模板（null)"},{"bindClass":["net.ooder.dsm.website.temp.tree.WebSiteTreeView"],"bindClassName":"net.ooder.dsm.website.temp.tree.WebSiteTreeView","caption":"WebSiteTreeView","closeBtn":false,"dynDestory":false,"dynLoad":false,"entityClass":"net.ooder.dsm.website.temp.tree.WebSiteTreeView","id":"null","imageClass":"ri-tools-line","pattern":"","tagVar":{"name":"codeTempsNav"},"title":"WebSiteTreeView"},{"bindClass":["net.ooder.dsm.website.temp.WebSiteJavaTempService"],"bindClassName":"net.ooder.dsm.website.temp.WebSiteJavaTempService","caption":"loadUserSpaceType","id":"loadUserSpaceType","imageClass":"ri-tree-line","initFold":false,"pattern":"","sub":[{"bindClass":["net.ooder.dsm.website.temp.service.WebSiteDomainTreeService"],"bindClassName":"net.ooder.dsm.website.temp.service.WebSiteDomainTreeService","caption":"loadUserSpaceType（null)","closeBtn":false,"dynDestory":true,"dynLoad":true,"entityClass":"net.ooder.esd.dsm.domain.enums.NavDomainType","groupName":"dsm_temp_website_AllCustomDomainTempGrid","id":"loadUserSpaceType_dsm_temp_website_AllCustomDomainTempGrid","imageClass":"ri-node-tree","pattern":"","tagVar":{"name":"allCustomDomainTempGrid"},"title":"loadUserSpaceType（null)"},{"bindClass":["net.ooder.esd.dsm.temp.JavaTemp"],"bindClassName":"net.ooder.esd.dsm.temp.JavaTemp","caption":"模版节点","closeBtn":false,"dynDestory":false,"dynLoad":false,"entityClass":"net.ooder.esd.dsm.temp.JavaTemp","id":"loadUserSpaceType_null","imageClass":"ri-file-text-line","pattern":"","tagVar":{"name":"null"},"title":"模版节点"},{"bindClass":["net.ooder.dsm.website.temp.service.WebSiteAdminTempService"],"bindClassName":"net.ooder.dsm.website.temp.service.WebSiteAdminTempService","caption":"模板分类","closeBtn":false,"dynDestory":false,"dynLoad":false,"entityClass":"net.ooder.esd.dsm.enums.DSMType","groupName":"dsm_temp_website_LoadChildDSM","id":"loadUserSpaceType_dsm_temp_website_LoadChildDSM","imageClass":"ri-tools-line","pattern":"","tagVar":{"name":"loadChildDSM"},"title":"模板分类"},{"bindClass":["net.ooder.dsm.website.temp.service.WebSiteDomainService"],"bindClassName":"net.ooder.dsm.website.temp.service.WebSiteDomainService","caption":"loadUserSpaceType（null)","closeBtn":false,"dynDestory":false,"dynLoad":false,"entityClass":"net.ooder.esd.dsm.domain.enums.CustomDomainType","groupName":"dsm_temp_website_LoadChildCustomDomain","id":"loadUserSpaceType_dsm_temp_website_LoadChildCustomDomain","imageClass":"ri-node-tree","pattern":"","tagVar":{"name":"loadChildCustomDomain"},"title":"loadUserSpaceType（null)"},{"bindClass":["net.ooder.dsm.website.temp.service.WebSiteAggTempService"],"bindClassName":"net.ooder.dsm.website.temp.service.WebSiteAggTempService","caption":"聚合类型","closeBtn":false,"dynDestory":false,"dynLoad":false,"entityClass":"net.ooder.annotation.AggregationType","groupName":"dsm_temp_website_LoadAggTemps","id":"loadUserSpaceType_dsm_temp_website_LoadAggTemps","imageClass":"ri-node-tree","pattern":"","tagVar":{"name":"loadAggTemps"},"title":"聚合类型"},{"bindClass":["net.ooder.dsm.website.temp.service.WebSiteUserSpaceService"],"bindClassName":"net.ooder.dsm.website.temp.service.WebSiteUserSpaceService","caption":"仓储分类","closeBtn":false,"dynDestory":true,"dynLoad":false,"entityClass":"net.ooder.annotation.UserSpace","groupName":"dsm_temp_website_LoadUserSpaceType","id":"loadUserSpaceType_dsm_temp_website_LoadUserSpaceType","imageClass":"ri-node-tree","pattern":"","tagVar":{"name":"loadUserSpaceType"},"title":"仓储分类"},{"bindClass":["net.ooder.dsm.website.temp.service.WebSiteRepositoryTempService"],"bindClassName":"net.ooder.dsm.website.temp.service.WebSiteRepositoryTempService","caption":"仓储类型","closeBtn":false,"dynDestory":true,"dynLoad":false,"entityClass":"net.ooder.esd.dsm.enums.RepositoryType","groupName":"dsm_temp_website_LoadRepositoryTemps","id":"loadUserSpaceType_dsm_temp_website_LoadRepositoryTemps","imageClass":"ri-node-tree","pattern":"","tagVar":{"name":"loadRepositoryTemps"},"title":"仓储类型"},{"bindClass":["net.ooder.dsm.website.temp.service.WebSiteViewTempService"],"bindClassName":"net.ooder.dsm.website.temp.service.WebSiteViewTempService","caption":"loadUserSpaceType（null)","closeBtn":false,"dynDestory":false,"dynLoad":false,"entityClass":"net.ooder.esd.annotation.ViewType","groupName":"dsm_temp_website_LoadViewTemps","id":"loadUserSpaceType_dsm_temp_website_LoadViewTemps","imageClass":"ri-node-tree","pattern":"","tagVar":{"name":"loadViewTemps"},"title":"loadUserSpaceType（null)"},{"bindClass":["java.lang.Void"],"caption":"loadChildDSM","id":"loadUserSpaceType_loadChildDSM","imageClass":"ri-tree-line","initFold":false,"pattern":"","sub":[{"bindClass":["net.ooder.dsm.website.temp.service.WebSiteDomainTreeService"],"bindClassName":"net.ooder.dsm.website.temp.service.WebSiteDomainTreeService","caption":"JAVA模板（null)","closeBtn":false,"dynDestory":true,"dynLoad":true,"entityClass":"net.ooder.esd.dsm.domain.enums.OrgDomainType","groupName":"dsm_temp_website_AllCustomDomainTempGrid","id":"loadUserSpaceType_loadChildDSM_dsm_temp_website_AllCustomDomainTempGrid","imageClass":"ri-node-tree","pattern":"","tagVar":{"name":"allCustomDomainTempGrid"},"title":"JAVA模板（null)"},{"bindClass":["net.ooder.dsm.website.temp.service.WebSiteViewTreeService"],"bindClassName":"net.ooder.dsm.website.temp.service.WebSiteViewTreeService","caption":"JAVA模板（null)","closeBtn":false,"dynDestory":false,"dynLoad":false,"entityClass":"net.ooder.esd.annotation.ViewType","groupName":"dsm_temp_website_AllViewTempGrid","id":"loadUserSpaceType_loadChildDSM_dsm_temp_website_AllViewTempGrid","imageClass":"ri-node-tree","pattern":"","tagVar":{"name":"allViewTempGrid"},"title":"JAVA模板（null)"},{"bindClass":["net.ooder.dsm.website.temp.service.WebSiteAggregationService"],"bindClassName":"net.ooder.dsm.website.temp.service.WebSiteAggregationService","caption":"JAVA模板（null)","closeBtn":false,"dynDestory":false,"dynLoad":false,"entityClass":"net.ooder.annotation.AggregationType","groupName":"dsm_temp_website_WebSiteAggregation","id":"loadUserSpaceType_loadChildDSM_dsm_temp_website_WebSiteAggregation","imageClass":"ri-node-tree","pattern":"","tagVar":{"name":"webSiteAggregation"},"title":"JAVA模板（null)"},{"bindClass":["net.ooder.dsm.website.temp.service.WebSiteRepositoryAdminService"],"bindClassName":"net.ooder.dsm.website.temp.service.WebSiteRepositoryAdminService","caption":"JAVA模板（null)","closeBtn":false,"dynDestory":false,"dynLoad":false,"entityClass":"net.ooder.esd.dsm.enums.RepositoryType","groupName":"dsm_temp_website_WebSiteRepositoryAdmin","id":"loadUserSpaceType_loadChildDSM_dsm_temp_website_WebSiteRepositoryAdmin","imageClass":"ri-node-tree","pattern":"","tagVar":{"name":"webSiteRepositoryAdmin"},"title":"JAVA模板（null)"},{"bindClass":["net.ooder.dsm.website.temp.service.WebSiteAdminTempService"],"bindClassName":"net.ooder.dsm.website.temp.service.WebSiteAdminTempService","caption":"模板分类","closeBtn":false,"dynDestory":false,"dynLoad":false,"entityClass":"net.ooder.esd.dsm.enums.DSMType","groupName":"dsm_temp_website_LoadChildDSM","id":"loadUserSpaceType_loadChildDSM_dsm_temp_website_LoadChildDSM","imageClass":"ri-tools-line","pattern":"","tagVar":{"name":"loadChildDSM"},"title":"模板分类"},{"bindClass":["net.ooder.dsm.website.temp.service.WebSiteDomainService"],"bindClassName":"net.ooder.dsm.website.temp.service.WebSiteDomainService","caption":"JAVA模板（null)","closeBtn":false,"dynDestory":false,"dynLoad":false,"entityClass":"net.ooder.esd.dsm.domain.enums.CustomDomainType","groupName":"dsm_temp_website_LoadChildCustomDomain","id":"loadUserSpaceType_loadChildDSM_dsm_temp_website_LoadChildCustomDomain","imageClass":"ri-node-tree","pattern":"","tagVar":{"name":"loadChildCustomDomain"},"title":"JAVA模板（null)"},{"bindClass":["net.ooder.dsm.website.temp.service.WebSiteUserSpaceService"],"bindClassName":"net.ooder.dsm.website.temp.service.WebSiteUserSpaceService","caption":"JAVA模板（null)","closeBtn":false,"dynDestory":false,"dynLoad":false,"entityClass":"net.ooder.annotation.UserSpace","groupName":"dsm_temp_website_LoadUserSpaceType","id":"loadUserSpaceType_loadChildDSM_dsm_temp_website_LoadUserSpaceType","imageClass":"ri-node-tree","pattern":"","tagVar":{"name":"loadUserSpaceType"},"title":"JAVA模板（null)"}],"title":"loadChildDSM"}],"title":"loadUserSpaceType"}],"tabindex":0,"title":"WebSiteTreeView"}])
                .setSelMode("single")
                .setSingleOpen(false)
                .setTabindex(0)
                .setVisibility("visible")
            ;
            WebSiteTreeViewPanel.append(WebSiteTreeView);

            // getCodeTempsNav
            var getCodeTempsNav = ood.create("ood.UI.Tabs")
                .setHost(CodeTempsNavLAYOUT, "getCodeTempsNav")
                .setName("getCodeTempsNav")
                .setActiveLast(true)
                .setAutoReload(true)
                .setAutoSave(false)
                .setBarLocation("top")
                .setBarSize("2.5em")
                .setBorderType("none")
                .setCaption("JAVA模板")
                .setImageClass("ri-tools-line")
                .setInitFold(false)
                .setItems([])
                .setNoHandler(true)
                .setSelMode("single")
                .setSingleOpen(true)
                .setTabindex(1)
                .setTagCmdsAlign("floatright")
                .setTagVar({})
            ;
            CodeTempsNavLAYOUT.append(getCodeTempsNav);

            return children;
        }
    }
});
