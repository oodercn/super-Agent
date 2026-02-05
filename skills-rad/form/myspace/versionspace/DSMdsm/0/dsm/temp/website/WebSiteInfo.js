// 自动生成的 WebSiteInfo 组件
ood.Class("ooder.WebSiteInfo", "ood.Module", {
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
        "caption": "站点模版信息",
        "currComponentAlias": "WebSiteInfo",
        "dock": "fill",
        "title": "站点模版信息"
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

            // WebSiteInfoMain
            var WebSiteInfoMain = ood.create("ood.UI.Block")
                .setHost(host, "WebSiteInfoMain")
                .setName("WebSiteInfoMain")
                .setDock("fill")
                .setPanelBgClr("transparent")
                .setTabindex(1)
            ;
            append(WebSiteInfoMain);

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
                .setMethodName("updateWebSite")
                .setProxyType("auto")
                .setQueryAsync(true)
                .setQueryMethod("POST")
                .setQueryURL("/dsm/temp/website/updateWebSite")
                .setRequestDataSource([{"name":"tempInfo","path":"","type":"FORM"},{"name":"tempInfo","path":"","type":"FORM"},{"name":"WebSiteInfoMain","path":"","type":"FORM"},{"name":"PAGECTX","path":"","type":"FORM"}])
                .setRequestType("JSON")
                .setResponseCallback([])
                .setResponseDataTarget([])
                .setResponseType("JSON")
                .setSourceClassName("net.ooder.dsm.website.manager.WebSiteService")
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
                .setDesc("站点模版信息")
                .setImageClass("ri-code-box-line")
                .setIsAllform(false)
                .setMethodName("getWebSiteInfo")
                .setProxyType("auto")
                .setQueryAsync(true)
                .setQueryMethod("POST")
                .setQueryURL("/dsm/temp/website/WebSiteInfo")
                .setRequestDataSource([{"name":"WebSiteInfoMain","path":"","type":"FORM"},{"name":"PAGECTX","path":"","type":"FORM"}])
                .setRequestType("FORM")
                .setResponseCallback([])
                .setResponseDataTarget([{"name":"WebSiteInfoMain","path":"data","type":"FORM"},{"name":"PAGECTX","path":"data","type":"FORM"}])
                .setResponseType("JSON")
                .setSourceClassName("net.ooder.dsm.website.manager.WebSiteNav")
                .setTabindex(3)
                .setTips("站点模版信息")
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

            // thumbnailType
            var thumbnailType = ood.create("ood.UI.HiddenInput")
                .setHost(PAGECTX, "thumbnailType")
                .setName("thumbnailType")
                .setFormField(true)
                .setIsPid(true)
                .setPid(true)
                .setTabindex(3)
            ;
            PAGECTX.append(thumbnailType);

            // WebSiteInfo
            var WebSiteInfo = ood.create("ood.UI.FormLayout")
                .setHost(WebSiteInfoMain, "WebSiteInfo")
                .setName("WebSiteInfo")
                .setCaption("站点模版信息")
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
                .setLayoutData({"cells":{"A1":{"cellName":"A1","style":{"textAlign":"center"},"value":"模板名称"},"A2":{"cellName":"A2","style":{"textAlign":"center"},"value":"业务类型"},"C4":{"cellName":"C4","style":{"textAlign":"center"},"value":"上传略缩图"},"A3":{"cellName":"A3","style":{"textAlign":"center"},"value":"描述"},"A4":{"cellName":"A4","style":{"textAlign":"center"},"value":"图片"},"C1":{"cellName":"C1","style":{"textAlign":"center"},"value":"命名空间"}},"colSetting":{"A":{"manualWidth":150},"B":{"manualWidth":150},"C":{"manualWidth":150},"D":{"manualWidth":150}},"cols":4,"merged":[{"col":1,"colspan":3,"removed":false,"row":1,"rowspan":1},{"col":1,"colspan":3,"removed":false,"row":2,"rowspan":1}],"rowSetting":{"1":{"manualHeight":35},"2":{"manualHeight":35},"3":{"manualHeight":50},"4":{"manualHeight":35}},"rows":4})
                .setLineSpacing(10)
                .setMode("write")
                .setSolidGridlines(true)
                .setStretchH("all")
                .setStretchHeight("last")
                .setTabindex(0)
                .setTagVar({})
                .setVisibility("visible")
            ;
            WebSiteInfoMain.append(WebSiteInfo);

            // name
            var name = ood.create("ood.UI.Input")
                .setHost(WebSiteInfo, "name")
                .setName("name")
                .setCaption("模板名称")
                .setDefaultFocus(false)
                .setDesc("模板名称")
                .setDisableClickEffect(false)
                .setDisableHoverEffect(false)
                .setDisableTips(false)
                .setDisabled(false)
                .setDock("none")
                .setEvents({})
                .setImageClass("")
                .setItems([])
                .setLabelCaption("模板名称")
                .setLabelPos("left")
                .setLabelSize("6.0em")
                .setReadonly(false)
                .setRequired(true)
                .setTabindex(0)
                .setTagVar({})
                .setVisibility("visible")
            ;
            WebSiteInfo.append(name);

            // space
            var space = ood.create("ood.UI.Input")
                .setHost(WebSiteInfo, "space")
                .setName("space")
                .setCaption("命名空间")
                .setDefaultFocus(false)
                .setDesc("命名空间")
                .setDisableClickEffect(false)
                .setDisableHoverEffect(false)
                .setDisableTips(false)
                .setDisabled(false)
                .setDock("none")
                .setEvents({})
                .setImageClass("")
                .setItems([])
                .setLabelCaption("命名空间")
                .setLabelPos("left")
                .setLabelSize("6.0em")
                .setReadonly(false)
                .setRequired(true)
                .setTabindex(1)
                .setTagVar({})
                .setVisibility("visible")
            ;
            WebSiteInfo.append(space);

            // type
            var type = ood.create("ood.UI.ComboInput")
                .setHost(WebSiteInfo, "type")
                .setName("type")
                .setDefaultFocus(false)
                .setDesc("业务类型")
                .setDisableClickEffect(false)
                .setDisableHoverEffect(false)
                .setDisableTips(false)
                .setDisabled(false)
                .setDock("none")
                .setDynLoad(true)
                .setEnumClass("net.ooder.esd.dsm.enums.DSMTempType")
                .setItems([{"bindClass":[],"caption":"通用数据库模板","id":"dao","imageClass":"ri-database-line","name":"通用数据库模板","pattern":"","tabindex":0,"tagVar":{"name":"dao","clazz":"net.ooder.esd.dsm.enums.DSMTempType"},"title":"通用数据库模板"},{"bindClass":[],"caption":"通用视图模板","id":"custom","imageClass":"ri-stack-line","name":"通用视图模板","pattern":"","tabindex":1,"tagVar":{"name":"custom","clazz":"net.ooder.esd.dsm.enums.DSMTempType"},"title":"通用视图模板"},{"bindClass":[],"caption":"业务流程模板","id":"process","imageClass":"ri-node-tree","name":"业务流程模板","pattern":"","tabindex":2,"tagVar":{"name":"process","clazz":"net.ooder.esd.dsm.enums.DSMTempType"},"title":"业务流程模板"},{"bindClass":[],"caption":"统计分析模板","id":"statistics","imageClass":"ri-line-chart-line","name":"统计分析模板","pattern":"","tabindex":3,"tagVar":{"name":"statistics","clazz":"net.ooder.esd.dsm.enums.DSMTempType"},"title":"统计分析模板"},{"bindClass":[],"caption":"门户网站模板","id":"cms","imageClass":"ri-earth-line","name":"门户网站模板","pattern":"","tabindex":4,"tagVar":{"name":"cms","clazz":"net.ooder.esd.dsm.enums.DSMTempType"},"title":"门户网站模板"},{"bindClass":[],"caption":"移动应用模板","id":"mobile","imageClass":"ri-smartphone-line","name":"移动应用模板","pattern":"","tabindex":5,"tagVar":{"name":"mobile","clazz":"net.ooder.esd.dsm.enums.DSMTempType"},"title":"移动应用模板"}])
                .setLabelCaption("业务类型")
                .setReadonly(false)
                .setTabindex(2)
                .setType("listbox")
                .setVisibility("visible")
            ;
            WebSiteInfo.append(type);

            // desc
            var desc = ood.create("ood.UI.RichEditor")
                .setHost(WebSiteInfo, "desc")
                .setName("desc")
                .setCmdList("none")
                .setDefaultFocus(false)
                .setDesc("描述")
                .setDisableClickEffect(false)
                .setDisableHoverEffect(false)
                .setDisableTips(false)
                .setDock("none")
                .setEvents({})
                .setTabindex(3)
                .setTextType("text")
                .setVisibility("visible")
            ;
            WebSiteInfo.append(desc);

            // image
            var image = ood.create("ood.UI.Image")
                .setHost(WebSiteInfo, "image")
                .setName("image")
                .setDefaultFocus(false)
                .setDisableClickEffect(false)
                .setDisableHoverEffect(false)
                .setDisableTips(false)
                .setDisabled(false)
                .setDock("none")
                .setEvents({})
                .setTabindex(4)
                .setVisibility("visible")
            ;
            WebSiteInfo.append(image);

            // thumbnailFile
            var thumbnailFile = ood.create("ood.UI.FileUpload")
                .setHost(WebSiteInfo, "thumbnailFile")
                .setName("thumbnailFile")
                .setDefaultFocus(false)
                .setDesc("上传略缩图")
                .setDisableClickEffect(false)
                .setDisableHoverEffect(false)
                .setDisableTips(false)
                .setDisabled(false)
                .setDock("none")
                .setEvents({})
                .setHeight("30.0em")
                .setSrc("/plugins/fileupload/uploadgrid.html")
                .setTabindex(5)
                .setUploadUrl("/custom/dsm/AttachUPLoad?thumbnailType=dsmTemp")
                .setVisibility("visible")
                .setWidth("30.0em")
            ;
            WebSiteInfo.append(thumbnailFile);

            return children;
        }
    }
});
