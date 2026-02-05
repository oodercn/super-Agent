// 自动生成的 ColListView 组件
ood.Class("ooder.ColListView", "ood.Module", {
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
        "caption": "字段信息",
        "currComponentAlias": "Cols",
        "dock": "fill",
        "title": "字段信息"
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

            // ColListView
            var ColListView = ood.create("ood.UI.Block")
                .setHost(host, "ColListView")
                .setName("ColListView")
                .setBorderType("none")
                .setDock("fill")
                .setPanelBgClr("transparent")
            ;
            append(ColListView);

            // PAGECTX
            var PAGECTX = ood.create("ood.UI.Block")
                .setHost(host, "PAGECTX")
                .setName("PAGECTX")
                .setBorderType("none")
                .setVisibility("hidden")
            ;
            append(PAGECTX);

            // ColListViewMain
            var ColListViewMain = ood.create("ood.UI.Block")
                .setHost(host, "ColListViewMain")
                .setName("ColListViewMain")
                .setBorderType("none")
                .setDock("fill")
            ;
            append(ColListViewMain);

            // RELOAD
            var RELOAD = ood.create("ood.APICaller")
                .setHost(host, "RELOAD")
                .setName("RELOAD")
                .setAllform(false)
                .setAutoRun(true)
                .setCheckRequired(false)
                .setCheckValid(false)
                .setDesc("字段信息")
                .setImageClass("spafont spa-icon-c-comboinput")
                .setIsAllform(false)
                .setMethodName("getCols")
                .setProxyType("auto")
                .setQueryAsync(true)
                .setQueryMethod("POST")
                .setQueryURL("/db/table/ColListView")
                .setRequestDataSource([{"name":"PAGECTX","path":"","type":"FORM"}])
                .setRequestType("FORM")
                .setResponseCallback([])
                .setResponseDataTarget([{"name":"Cols","path":"data","type":"TREEGRID"},{"name":"PAGECTX","path":"data","type":"FORM"}])
                .setResponseType("JSON")
                .setSourceClassName("net.ooder.admin.db.table.TableNav")
                .setTips("字段信息")
            ;
            append(RELOAD);

            // del
            var del = ood.create("ood.APICaller")
                .setHost(host, "del")
                .setName("del")
                .setAllform(false)
                .setAutoRun(false)
                .setCheckRequired(false)
                .setCheckValid(false)
                .setDesc("删除")
                .setImageClass("ri-delete-bin-fill")
                .setIsAllform(false)
                .setMethodName("del")
                .setProxyType("auto")
                .setQueryAsync(true)
                .setQueryMethod("POST")
                .setQueryURL("/db/action/del")
                .setRequestDataSource([{"name":"Cols","path":"","type":"TREEGRIDROW"},{"name":"PAGECTX","path":"","type":"FORM"}])
                .setRequestType("FORM")
                .setResponseCallback([])
                .setResponseDataTarget([])
                .setResponseType("JSON")
                .setSourceClassName("net.ooder.admin.db.action.DBColAction")
                .setTips("删除")
            ;
            append(del);

            // moveDown
            var moveDown = ood.create("ood.APICaller")
                .setHost(host, "moveDown")
                .setName("moveDown")
                .setAllform(false)
                .setAutoRun(false)
                .setCheckRequired(false)
                .setCheckValid(false)
                .setDesc("向下")
                .setImageClass("ri-arrow-down-s-line")
                .setIsAllform(false)
                .setMethodName("moveDown")
                .setProxyType("auto")
                .setQueryAsync(true)
                .setQueryMethod("POST")
                .setQueryURL("/db/action/moveDown")
                .setRequestDataSource([{"name":"Cols","path":"","type":"TREEGRIDROW"},{"name":"PAGECTX","path":"","type":"FORM"}])
                .setRequestType("FORM")
                .setResponseCallback([])
                .setResponseDataTarget([])
                .setResponseType("JSON")
                .setSourceClassName("net.ooder.admin.db.action.DBColAction")
                .setTips("向下")
            ;
            append(moveDown);

            // moveUP
            var moveUP = ood.create("ood.APICaller")
                .setHost(host, "moveUP")
                .setName("moveUP")
                .setAllform(false)
                .setAutoRun(false)
                .setCheckRequired(false)
                .setCheckValid(false)
                .setDesc("向上")
                .setImageClass("ri-arrow-up-s-line")
                .setIsAllform(false)
                .setMethodName("moveUP")
                .setProxyType("auto")
                .setQueryAsync(true)
                .setQueryMethod("POST")
                .setQueryURL("/db/action/moveUP")
                .setRequestDataSource([{"name":"Cols","path":"","type":"TREEGRIDROW"},{"name":"PAGECTX","path":"","type":"FORM"}])
                .setRequestType("FORM")
                .setResponseCallback([])
                .setResponseDataTarget([])
                .setResponseType("JSON")
                .setSourceClassName("net.ooder.admin.db.action.DBColAction")
                .setTips("向上")
            ;
            append(moveUP);

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

            // name
            var name = ood.create("ood.UI.HiddenInput")
                .setHost(PAGECTX, "name")
                .setName("name")
                .setFormField(true)
                .setIsPid(true)
                .setPid(true)
            ;
            PAGECTX.append(name);

            // projectId
            var projectId = ood.create("ood.UI.HiddenInput")
                .setHost(PAGECTX, "projectId")
                .setName("projectId")
                .setFormField(true)
                .setIsPid(true)
                .setPid(true)
            ;
            PAGECTX.append(projectId);

            // dataType
            var dataType = ood.create("ood.UI.HiddenInput")
                .setHost(PAGECTX, "dataType")
                .setName("dataType")
                .setFormField(true)
                .setIsPid(false)
                .setPid(false)
            ;
            PAGECTX.append(dataType);

            // url
            var url = ood.create("ood.UI.HiddenInput")
                .setHost(PAGECTX, "url")
                .setName("url")
                .setFormField(true)
                .setIsPid(false)
                .setPid(false)
            ;
            PAGECTX.append(url);

            // configKey
            var configKey = ood.create("ood.UI.HiddenInput")
                .setHost(PAGECTX, "configKey")
                .setName("configKey")
                .setFormField(true)
                .setIsPid(true)
                .setPid(true)
            ;
            PAGECTX.append(configKey);

            // Cols
            var Cols = ood.create("ood.UI.TreeGrid")
                .setHost(ColListViewMain, "Cols")
                .setName("Cols")
                .setDesc("getCols")
                .setEditMode("inline")
                .setHeader([{"caption":"字段名","hidden":false,"id":"name","tips":"字段名","title":"字段名","type":"input"},{"caption":"类型","editorListItems":[{"bindClass":[],"caption":"字符串","id":"VARCHAR","name":"字符串","pattern":"","tagVar":{"name":"VARCHAR","clazz":"net.ooder.annotation.ColType"},"title":"字符串"},{"bindClass":[],"caption":"日期","id":"DATETIME","name":"日期","pattern":"","tagVar":{"name":"DATETIME","clazz":"net.ooder.annotation.ColType"},"title":"日期"},{"bindClass":[],"caption":"大文本","id":"TEXT","name":"大文本","pattern":"","tagVar":{"name":"TEXT","clazz":"net.ooder.annotation.ColType"},"title":"大文本"},{"bindClass":[],"caption":"整数","id":"INTEGER","name":"整数","pattern":"","tagVar":{"name":"INTEGER","clazz":"net.ooder.annotation.ColType"},"title":"整数"},{"bindClass":[],"caption":"数字","id":"FLOAT","name":"数字","pattern":"","tagVar":{"name":"FLOAT","clazz":"net.ooder.annotation.ColType"},"title":"数字"},{"bindClass":[],"caption":"BOOLEAN","id":"BOOLEAN","name":"BOOLEAN","pattern":"","tagVar":{"name":"BOOLEAN","clazz":"net.ooder.annotation.ColType"},"title":"BOOLEAN"},{"bindClass":[],"caption":"字符串","id":"VARCHAR2","name":"字符串","pattern":"","tagVar":{"name":"VARCHAR2","clazz":"net.ooder.annotation.ColType"},"title":"字符串"},{"bindClass":[],"caption":"二进制","id":"BLOB","name":"二进制","pattern":"","tagVar":{"name":"BLOB","clazz":"net.ooder.annotation.ColType"},"title":"二进制"},{"bindClass":[],"caption":"CHAR","id":"CHAR","name":"CHAR","pattern":"","tagVar":{"name":"CHAR","clazz":"net.ooder.annotation.ColType"},"title":"CHAR"},{"bindClass":[],"caption":"TINYINT","id":"TINYINT","name":"TINYINT","pattern":"","tagVar":{"name":"TINYINT","clazz":"net.ooder.annotation.ColType"},"title":"TINYINT"},{"bindClass":[],"caption":"SMALLINT","id":"SMALLINT","name":"SMALLINT","pattern":"","tagVar":{"name":"SMALLINT","clazz":"net.ooder.annotation.ColType"},"title":"SMALLINT"},{"bindClass":[],"caption":"枚举","id":"ENUM","name":"枚举","pattern":"","tagVar":{"name":"ENUM","clazz":"net.ooder.annotation.ColType"},"title":"枚举"},{"bindClass":[],"caption":"MEDIUMINT","id":"MEDIUMINT","name":"MEDIUMINT","pattern":"","tagVar":{"name":"MEDIUMINT","clazz":"net.ooder.annotation.ColType"},"title":"MEDIUMINT"},{"bindClass":[],"caption":"BIT","id":"BIT","name":"BIT","pattern":"","tagVar":{"name":"BIT","clazz":"net.ooder.annotation.ColType"},"title":"BIT"},{"bindClass":[],"caption":"BIGINT","id":"BIGINT","name":"BIGINT","pattern":"","tagVar":{"name":"BIGINT","clazz":"net.ooder.annotation.ColType"},"title":"BIGINT"},{"bindClass":[],"caption":"DOUBLE","id":"DOUBLE","name":"DOUBLE","pattern":"","tagVar":{"name":"DOUBLE","clazz":"net.ooder.annotation.ColType"},"title":"DOUBLE"},{"bindClass":[],"caption":"可变长度","id":"DECIMAL","name":"可变长度","pattern":"","tagVar":{"name":"DECIMAL","clazz":"net.ooder.annotation.ColType"},"title":"可变长度"},{"bindClass":[],"caption":"集合","id":"SET","name":"集合","pattern":"","tagVar":{"name":"SET","clazz":"net.ooder.annotation.ColType"},"title":"集合"},{"bindClass":[],"caption":"JSON","id":"JSON","name":"JSON","pattern":"","tagVar":{"name":"JSON","clazz":"net.ooder.annotation.ColType"},"title":"JSON"},{"bindClass":[],"caption":"几何类型","id":"Geometry","name":"几何类型","pattern":"","tagVar":{"name":"Geometry","clazz":"net.ooder.annotation.ColType"},"title":"几何类型"},{"bindClass":[],"caption":"ID","id":"ID","name":"ID","pattern":"","tagVar":{"name":"ID","clazz":"net.ooder.annotation.ColType"},"title":"ID"},{"bindClass":[],"caption":"整形","id":"INT","name":"整形","pattern":"","tagVar":{"name":"INT","clazz":"net.ooder.annotation.ColType"},"title":"整形"},{"bindClass":[],"caption":"DATE","id":"DATE","name":"DATE","pattern":"","tagVar":{"name":"DATE","clazz":"net.ooder.annotation.ColType"},"title":"DATE"},{"bindClass":[],"caption":"TIME","id":"TIME","name":"TIME","pattern":"","tagVar":{"name":"TIME","clazz":"net.ooder.annotation.ColType"},"title":"TIME"},{"bindClass":[],"caption":"TIMESTAMP","id":"TIMESTAMP","name":"TIMESTAMP","pattern":"","tagVar":{"name":"TIMESTAMP","clazz":"net.ooder.annotation.ColType"},"title":"TIMESTAMP"},{"bindClass":[],"caption":"YEAR","id":"YEAR","name":"YEAR","pattern":"","tagVar":{"name":"YEAR","clazz":"net.ooder.annotation.ColType"},"title":"YEAR"}],"enumClass":"net.ooder.annotation.ColType","hidden":false,"id":"colType","tips":"类型","title":"类型","type":"listbox"},{"caption":"长度","hidden":false,"id":"length","tips":"长度","title":"长度","type":"number","width":"6.0em"},{"caption":"数字精度","hidden":false,"id":"fractions","tips":"数字精度","title":"数字精度","type":"number","width":"6.0em"},{"caption":"是否主键","hidden":false,"id":"pk","tips":"是否主键","title":"是否主键","type":"checkbox"},{"caption":"是否可为空","hidden":false,"id":"canNull","tips":"是否可为空","title":"是否可为空","type":"checkbox"},{"caption":"注解","flexSize":true,"hidden":false,"id":"cnname","tips":"注解","title":"注解","type":"input","width":"150.0"},{"caption":"工程名","hidden":true,"id":"projectId","tips":"工程名","title":"工程名","type":"input"},{"caption":"表名","hidden":true,"id":"tablename","tips":"表名","title":"表名","type":"input"},{"caption":"数据类型","hidden":true,"id":"dataType","tips":"数据类型","title":"数据类型","type":"number","width":"6.0em"},{"caption":"连接串","hidden":true,"id":"url","tips":"连接串","title":"连接串","type":"input"},{"caption":"数据库标识","hidden":true,"id":"configKey","tips":"数据库标识","title":"数据库标识","type":"input"}])
                .setHeaderHeight("2.0em")
                .setItems([])
                .setRowHandler(true)
                .setRowHandlerWidth("5.0em")
                .setRowHeight("3.0em")
                .setRowNumbered(true)
                .setSelMode("multibycheckbox")
                .setShowHeader(true)
                .setTagCmds([{"buttonType":"text","caption":"","id":"delbutton","imageClass":"ri-delete-bin-fill","index":0,"pos":"row","tagCmdsAlign":"right","tips":"删除"},{"buttonType":"text","caption":"","id":"moveDownbutton","imageClass":"ri-arrow-down-s-line","index":0,"pos":"row","tagCmdsAlign":"right","tips":"向下"},{"buttonType":"text","caption":"","id":"moveUPbutton","imageClass":"ri-arrow-up-s-line","index":0,"pos":"row","tagCmdsAlign":"right","tips":"向上"}])
                .setUidColumn("name")
                .setValueSeparator(";")
            ;
            ColListViewMain.append(Cols);

            return children;
        }
    }
});
