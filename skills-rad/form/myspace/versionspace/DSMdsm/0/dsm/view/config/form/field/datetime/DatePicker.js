// 自动生成的 DatePicker 组件
ood.Class("ooder.DatePicker", "ood.Module", {
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
        "caption": "日期选择框",
        "currComponentAlias": "DatePicker",
        "dock": "fill",
        "title": "日期选择框"
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

            // DatePickerMain
            var DatePickerMain = ood.create("ood.UI.Block")
                .setHost(host, "DatePickerMain")
                .setName("DatePickerMain")
                .setDock("fill")
                .setPanelBgClr("transparent")
                .setTabindex(1)
            ;
            append(DatePickerMain);

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
                .setMethodName("updateDatePicker")
                .setProxyType("auto")
                .setQueryAsync(true)
                .setQueryMethod("POST")
                .setQueryURL("/dsm/view/config/form/field/combo/date/updateDatePicker")
                .setRequestDataSource([{"name":"comboView","path":"","type":"FORM"},{"name":"projectName","path":"","type":"FORM"},{"name":"domainId","path":"","type":"FORM"},{"name":"comboView","path":"","type":"FORM"},{"name":"projectName","path":"","type":"FORM"},{"name":"domainId","path":"","type":"FORM"},{"name":"DatePickerMain","path":"","type":"FORM"},{"name":"PAGECTX","path":"","type":"FORM"}])
                .setRequestType("JSON")
                .setResponseCallback([])
                .setResponseDataTarget([])
                .setResponseType("JSON")
                .setSourceClassName("net.ooder.dsm.view.config.form.field.combo.service.ComboDateService")
                .setTabindex(2)
                .setTips("保存")
            ;
            append(SAVE);

            // RESET
            var RESET = ood.create("ood.APICaller")
                .setHost(host, "RESET")
                .setName("RESET")
                .setAllform(false)
                .setAutoRun(false)
                .setCheckRequired(false)
                .setCheckValid(false)
                .setDesc("reSetDatePicker")
                .setImageClass("ri-code-box-line")
                .setIsAllform(false)
                .setMethodName("reSetDatePicker")
                .setProxyType("auto")
                .setQueryAsync(true)
                .setQueryMethod("POST")
                .setQueryURL("/dsm/view/config/form/field/combo/date/reSetDatePicker")
                .setRequestDataSource([{"name":"comboView","path":"","type":"FORM"},{"name":"projectName","path":"","type":"FORM"},{"name":"domainId","path":"","type":"FORM"},{"name":"comboView","path":"","type":"FORM"},{"name":"projectName","path":"","type":"FORM"},{"name":"domainId","path":"","type":"FORM"},{"name":"DatePickerMain","path":"","type":"FORM"},{"name":"PAGECTX","path":"","type":"FORM"}])
                .setRequestType("JSON")
                .setResponseCallback([])
                .setResponseDataTarget([])
                .setResponseType("JSON")
                .setSourceClassName("net.ooder.dsm.view.config.form.field.combo.service.ComboDateService")
                .setTabindex(3)
                .setTips("reSetDatePicker")
            ;
            append(RESET);

            // RELOAD
            var RELOAD = ood.create("ood.APICaller")
                .setHost(host, "RELOAD")
                .setName("RELOAD")
                .setAllform(false)
                .setAutoRun(true)
                .setCheckRequired(false)
                .setCheckValid(false)
                .setDesc("日期选择框")
                .setImageClass("ri-calendar-line")
                .setIsAllform(false)
                .setMethodName("getDatePicker")
                .setProxyType("auto")
                .setQueryAsync(true)
                .setQueryMethod("POST")
                .setQueryURL("/dsm/view/config/form/field/datetime/DatePicker")
                .setRequestDataSource([{"name":"DatePickerMain","path":"","type":"FORM"},{"name":"PAGECTX","path":"","type":"FORM"}])
                .setRequestType("FORM")
                .setResponseCallback([])
                .setResponseDataTarget([{"name":"DatePickerMain","path":"data","type":"FORM"},{"name":"PAGECTX","path":"data","type":"FORM"}])
                .setResponseType("JSON")
                .setSourceClassName("net.ooder.dsm.view.config.form.field.combo.ComboDateTimeGroup")
                .setTabindex(4)
                .setTips("日期选择框")
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

            // viewClassName
            var viewClassName = ood.create("ood.UI.HiddenInput")
                .setHost(PAGECTX, "viewClassName")
                .setName("viewClassName")
                .setFormField(true)
                .setIsPid(true)
                .setPid(true)
                .setTabindex(9)
            ;
            PAGECTX.append(viewClassName);

            // DatePicker
            var DatePicker = ood.create("ood.UI.FormLayout")
                .setHost(DatePickerMain, "DatePicker")
                .setName("DatePicker")
                .setCaption("日期选择框")
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
                .setImageClass("ri-calendar-line")
                .setLayoutData({"cells":{"A1":{"cellName":"A1","style":{"textAlign":"center"},"value":"字段名"},"C3":{"cellName":"C3","style":{"textAlign":"center"},"value":"显示时间"},"A2":{"cellName":"A2","style":{"textAlign":"center"},"value":"宽度"},"C4":{"cellName":"C4","style":{"textAlign":"center"},"value":"下一天"},"A3":{"cellName":"A3","style":{"textAlign":"center"},"value":"关闭按钮"},"C5":{"cellName":"C5","style":{"textAlign":"center"},"value":"日期格式"},"A4":{"cellName":"A4","style":{"textAlign":"center"},"value":"第一天"},"A5":{"cellName":"A5","style":{"textAlign":"center"},"value":"隐藏周"},"C1":{"cellName":"C1","style":{"textAlign":"center"},"value":"高度"},"C2":{"cellName":"C2","style":{"textAlign":"center"},"value":"默认值"}},"colSetting":{"A":{"manualWidth":150},"B":{"manualWidth":150},"C":{"manualWidth":150},"D":{"manualWidth":150}},"cols":4,"rowSetting":{"1":{"manualHeight":35},"2":{"manualHeight":35},"3":{"manualHeight":35},"4":{"manualHeight":35},"5":{"manualHeight":35}},"rows":5})
                .setLineSpacing(10)
                .setMode("write")
                .setSolidGridlines(true)
                .setStretchH("all")
                .setStretchHeight("none")
                .setTabindex(0)
                .setTagVar({})
                .setVisibility("visible")
            ;
            DatePickerMain.append(DatePicker);

            // fieldname
            var fieldname = ood.create("ood.UI.Input")
                .setHost(DatePicker, "fieldname")
                .setName("fieldname")
                .setCaption("字段名")
                .setDefaultFocus(false)
                .setDesc("字段名")
                .setDisableClickEffect(false)
                .setDisableHoverEffect(false)
                .setDisableTips(false)
                .setDisabled(false)
                .setDock("none")
                .setEvents({})
                .setImageClass("")
                .setItems([])
                .setLabelCaption("字段名")
                .setLabelPos("left")
                .setLabelSize("6.0em")
                .setReadonly(true)
                .setTabindex(0)
                .setTagVar({})
                .setVisibility("visible")
            ;
            DatePicker.append(fieldname);

            // height
            var height = ood.create("ood.UI.Input")
                .setHost(DatePicker, "height")
                .setName("height")
                .setCaption("高度")
                .setDefaultFocus(false)
                .setDesc("高度")
                .setDisableClickEffect(false)
                .setDisableHoverEffect(false)
                .setDisableTips(false)
                .setDisabled(false)
                .setDock("none")
                .setEvents({})
                .setImageClass("")
                .setItems([])
                .setLabelCaption("高度")
                .setLabelPos("left")
                .setLabelSize("6.0em")
                .setReadonly(false)
                .setTabindex(1)
                .setTagVar({})
                .setVisibility("visible")
            ;
            DatePicker.append(height);

            // width
            var width = ood.create("ood.UI.Input")
                .setHost(DatePicker, "width")
                .setName("width")
                .setCaption("宽度")
                .setDefaultFocus(false)
                .setDesc("宽度")
                .setDisableClickEffect(false)
                .setDisableHoverEffect(false)
                .setDisableTips(false)
                .setDisabled(false)
                .setDock("none")
                .setEvents({})
                .setImageClass("")
                .setItems([])
                .setLabelCaption("宽度")
                .setLabelPos("left")
                .setLabelSize("6.0em")
                .setReadonly(false)
                .setTabindex(2)
                .setTagVar({})
                .setVisibility("visible")
            ;
            DatePicker.append(width);

            // value
            var value = ood.create("ood.UI.Input")
                .setHost(DatePicker, "value")
                .setName("value")
                .setCaption("默认值")
                .setDefaultFocus(false)
                .setDesc("默认值")
                .setDisableClickEffect(false)
                .setDisableHoverEffect(false)
                .setDisableTips(false)
                .setDisabled(false)
                .setDock("none")
                .setEvents({})
                .setImageClass("")
                .setItems([])
                .setLabelCaption("默认值")
                .setLabelPos("left")
                .setLabelSize("6.0em")
                .setReadonly(false)
                .setTabindex(3)
                .setTagVar({})
                .setVisibility("visible")
            ;
            DatePicker.append(value);

            // closeBtn
            var closeBtn = ood.create("ood.UI.CheckBox")
                .setHost(DatePicker, "closeBtn")
                .setName("closeBtn")
                .setCaption("")
                .setDefaultFocus(false)
                .setDesc("关闭按钮")
                .setDisableClickEffect(false)
                .setDisableHoverEffect(false)
                .setDisableTips(false)
                .setDock("none")
                .setEvents({})
                .setTabindex(4)
                .setVisibility("visible")
            ;
            DatePicker.append(closeBtn);

            // timeInput
            var timeInput = ood.create("ood.UI.CheckBox")
                .setHost(DatePicker, "timeInput")
                .setName("timeInput")
                .setCaption("")
                .setDefaultFocus(false)
                .setDesc("显示时间")
                .setDisableClickEffect(false)
                .setDisableHoverEffect(false)
                .setDisableTips(false)
                .setDock("none")
                .setEvents({})
                .setTabindex(5)
                .setVisibility("visible")
            ;
            DatePicker.append(timeInput);

            // firstDayOfWeek
            var firstDayOfWeek = ood.create("ood.UI.ComboInput")
                .setHost(DatePicker, "firstDayOfWeek")
                .setName("firstDayOfWeek")
                .setCaption("第一天")
                .setCurrencyTpl("$ *")
                .setDecimalSeparator(".")
                .setDefaultFocus(false)
                .setDesc("第一天")
                .setDisableClickEffect(false)
                .setDisableHoverEffect(false)
                .setDisableTips(false)
                .setDisabled(false)
                .setDock("none")
                .setForceFillZero(true)
                .setIncrement(1)
                .setItems([])
                .setLabelCaption("第一天")
                .setPrecision(0)
                .setReadonly(false)
                .setTabindex(6)
                .setType("number")
                .setVisibility("visible")
            ;
            DatePicker.append(firstDayOfWeek);

            // offDays
            var offDays = ood.create("ood.UI.Input")
                .setHost(DatePicker, "offDays")
                .setName("offDays")
                .setCaption("下一天")
                .setDefaultFocus(false)
                .setDesc("下一天")
                .setDisableClickEffect(false)
                .setDisableHoverEffect(false)
                .setDisableTips(false)
                .setDisabled(false)
                .setDock("none")
                .setEvents({})
                .setImageClass("")
                .setItems([])
                .setLabelCaption("下一天")
                .setLabelPos("left")
                .setLabelSize("6.0em")
                .setReadonly(false)
                .setTabindex(7)
                .setTagVar({})
                .setVisibility("visible")
            ;
            DatePicker.append(offDays);

            // hideWeekLabels
            var hideWeekLabels = ood.create("ood.UI.CheckBox")
                .setHost(DatePicker, "hideWeekLabels")
                .setName("hideWeekLabels")
                .setCaption("")
                .setDefaultFocus(false)
                .setDesc("隐藏周")
                .setDisableClickEffect(false)
                .setDisableHoverEffect(false)
                .setDisableTips(false)
                .setDock("none")
                .setEvents({})
                .setTabindex(8)
                .setVisibility("visible")
            ;
            DatePicker.append(hideWeekLabels);

            // dateInputFormat
            var dateInputFormat = ood.create("ood.UI.Input")
                .setHost(DatePicker, "dateInputFormat")
                .setName("dateInputFormat")
                .setCaption("日期格式")
                .setDefaultFocus(false)
                .setDesc("日期格式")
                .setDisableClickEffect(false)
                .setDisableHoverEffect(false)
                .setDisableTips(false)
                .setDisabled(false)
                .setDock("none")
                .setEvents({})
                .setImageClass("")
                .setItems([])
                .setLabelCaption("日期格式")
                .setLabelPos("left")
                .setLabelSize("6.0em")
                .setReadonly(false)
                .setTabindex(9)
                .setTagVar({})
                .setVisibility("visible")
            ;
            DatePicker.append(dateInputFormat);

            // SAVEBottomBlock
            var SAVEBottomBlock = ood.create("ood.UI.Block")
                .setHost(DatePickerMain, "SAVEBottomBlock")
                .setName("SAVEBottomBlock")
                .setBorderType("none")
                .setComboType("STATUSBUTTONS")
                .setDock("bottom")
                .setHeight("3.0em")
                .setOverflow("hidden")
                .setTabindex(1)
            ;
            DatePickerMain.append(SAVEBottomBlock);

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
