// 自动生成的 TableInfoView 组件
ood.Class("ooder.TableInfoView", "ood.Module", {
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
        "caption": "库表信息",
        "currComponentAlias": "TableInfoView",
        "dock": "fill",
        "title": "库表信息"
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

            // TableInfoView
            var TableInfoView = ood.create("ood.UI.Block")
                .setHost(host, "TableInfoView")
                .setName("TableInfoView")
                .setDock("fill")
                .setPanelBgClr("transparent")
            ;
            append(TableInfoView);

            // PAGECTX
            var PAGECTX = ood.create("ood.UI.Block")
                .setHost(host, "PAGECTX")
                .setName("PAGECTX")
                .setBorderType("none")
                .setVisibility("hidden")
            ;
            append(PAGECTX);

            // TableInfoViewMain
            var TableInfoViewMain = ood.create("ood.UI.Block")
                .setHost(host, "TableInfoViewMain")
                .setName("TableInfoViewMain")
                .setBorderType("none")
                .setDock("fill")
            ;
            append(TableInfoViewMain);

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
                .setMethodName("updateTable")
                .setProxyType("auto")
                .setQueryAsync(true)
                .setQueryMethod("POST")
                .setQueryURL("/db/table/updateTable")
                .setRequestDataSource([{"name":"tableInfo","path":"","type":"FORM"},{"name":"tableInfo","path":"","type":"FORM"},{"name":"TableInfoView","path":"","type":"FORM"},{"name":"PAGECTX","path":"","type":"FORM"}])
                .setRequestType("JSON")
                .setResponseCallback([])
                .setResponseDataTarget([])
                .setResponseType("JSON")
                .setSourceClassName("net.ooder.admin.db.table.TableService")
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
                .setDesc("库表信息")
                .setImageClass("ri-code-box-line")
                .setIsAllform(false)
                .setMethodName("getTableInfoView")
                .setProxyType("auto")
                .setQueryAsync(true)
                .setQueryMethod("POST")
                .setQueryURL("/db/table/TableInfoView")
                .setRequestDataSource([{"name":"TableInfoView","path":"","type":"FORM"},{"name":"PAGECTX","path":"","type":"FORM"}])
                .setRequestType("FORM")
                .setResponseCallback([])
                .setResponseDataTarget([{"name":"TableInfoView","path":"data","type":"FORM"},{"name":"PAGECTX","path":"data","type":"FORM"}])
                .setResponseType("JSON")
                .setSourceClassName("net.ooder.admin.db.table.TableNav")
                .setTips("库表信息")
            ;
            append(RELOAD);

            // configKey
            var configKey = ood.create("ood.UI.HiddenInput")
                .setHost(PAGECTX, "configKey")
                .setName("configKey")
                .setFormField(true)
                .setIsPid(false)
                .setPid(false)
            ;
            PAGECTX.append(configKey);

            // tablename
            var tablename = ood.create("ood.UI.HiddenInput")
                .setHost(PAGECTX, "tablename")
                .setName("tablename")
                .setFormField(true)
                .setIsPid(true)
                .setPid(true)
            ;
            PAGECTX.append(tablename);

            // projectName
            var projectName = ood.create("ood.UI.HiddenInput")
                .setHost(PAGECTX, "projectName")
                .setName("projectName")
                .setFormField(true)
                .setIsPid(false)
                .setPid(false)
                .setValue("sysVVVERSION0")
            ;
            PAGECTX.append(projectName);

            // projectVersionName
            var projectVersionName = ood.create("ood.UI.HiddenInput")
                .setHost(PAGECTX, "projectVersionName")
                .setName("projectVersionName")
                .setFormField(true)
                .setIsPid(false)
                .setPid(false)
                .setValue("sysVVVERSION0")
            ;
            PAGECTX.append(projectVersionName);

            // TableInfoView
            var TableInfoView = ood.create("ood.UI.FormLayout")
                .setHost(TableInfoViewMain, "TableInfoView")
                .setName("TableInfoView")
                .setCaption("库表信息")
                .setDefaultColWidth(150)
                .setDefaultColumnSize(-1)
                .setDefaultRowHeight(35)
                .setDefaultRowSize(-1)
                .setDock("fill")
                .setFloatHandler(false)
                .setImageClass("ri-file-text-line")
                .setLayoutData({"cells":{"A1":{"cellName":"A1","style":{"textAlign":"center"},"value":"表名"},"A2":{"cellName":"A2","style":{"textAlign":"center"},"value":"主键"},"A3":{"cellName":"A3","style":{"textAlign":"center"},"value":"连接串"},"C1":{"cellName":"C1","style":{"textAlign":"center"},"value":"注解"},"C2":{"cellName":"C2","style":{"textAlign":"center"},"value":"数据库标识"}},"colSetting":{"A":{"manualWidth":150},"B":{"manualWidth":150},"C":{"manualWidth":150},"D":{"manualWidth":150}},"cols":4,"merged":[{"col":1,"colspan":3,"removed":false,"row":2,"rowspan":1}],"rowSetting":{"1":{"manualHeight":35},"2":{"manualHeight":35},"3":{"manualHeight":35}},"rows":3})
                .setLineSpacing(10)
                .setMode("write")
                .setSolidGridlines(true)
                .setStretchH("all")
                .setStretchHeight("last")
                .setTagVar({})
            ;
            TableInfoViewMain.append(TableInfoView);

            // name
            var name = ood.create("ood.UI.Input")
                .setHost(TableInfoView, "name")
                .setName("name")
                .setCaption("表名")
                .setDesc("表名")
                .setDisabled(false)
                .setEvents({})
                .setImageClass("")
                .setItems([])
                .setLabelCaption("表名")
                .setLabelPos("left")
                .setLabelSize("6.0em")
                .setReadonly(false)
                .setRequired(true)
                .setTagVar({})
            ;
            TableInfoView.append(name);

            // cnname
            var cnname = ood.create("ood.UI.Input")
                .setHost(TableInfoView, "cnname")
                .setName("cnname")
                .setCaption("注解")
                .setDesc("注解")
                .setDisabled(false)
                .setEvents({})
                .setImageClass("")
                .setItems([])
                .setLabelCaption("注解")
                .setLabelPos("left")
                .setLabelSize("6.0em")
                .setReadonly(false)
                .setRequired(true)
                .setTagVar({})
            ;
            TableInfoView.append(cnname);

            // pkName
            var pkName = ood.create("ood.UI.Input")
                .setHost(TableInfoView, "pkName")
                .setName("pkName")
                .setCaption("主键")
                .setDesc("主键")
                .setDisabled(false)
                .setEvents({})
                .setImageClass("")
                .setItems([])
                .setLabelCaption("主键")
                .setLabelPos("left")
                .setLabelSize("6.0em")
                .setReadonly(true)
                .setTagVar({})
            ;
            TableInfoView.append(pkName);

            // configKey
            var configKey = ood.create("ood.UI.Input")
                .setHost(TableInfoView, "configKey")
                .setName("configKey")
                .setCaption("数据库标识")
                .setDesc("数据库标识")
                .setDisabled(false)
                .setEvents({})
                .setImageClass("")
                .setItems([])
                .setLabelCaption("数据库标识")
                .setLabelPos("left")
                .setLabelSize("6.0em")
                .setReadonly(true)
                .setTagVar({})
            ;
            TableInfoView.append(configKey);

            // url
            var url = ood.create("ood.UI.Input")
                .setHost(TableInfoView, "url")
                .setName("url")
                .setCaption("连接串")
                .setDesc("连接串")
                .setDisabled(false)
                .setEvents({})
                .setImageClass("")
                .setItems([])
                .setLabelCaption("连接串")
                .setLabelPos("left")
                .setLabelSize("6.0em")
                .setReadonly(true)
                .setTagVar({})
            ;
            TableInfoView.append(url);

            return children;
        }
    }
});
