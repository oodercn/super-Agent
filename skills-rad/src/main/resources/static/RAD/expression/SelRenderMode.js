ood.Class('RAD.expression.SelRenderMode', 'ood.Module', {
    Instance: {
        iniComponents: function () {
            // [[Code created by ESDUI RAD Studio
            var host = this, children = [], append = function (child) {
                children.push(child.get(0));
            };

            append(
                ood.create("ood.UI.Dialog")
                    .setHost(host, "mainDlg")
                    .setLeft("13.125em")
                    .setTop("0.625em")
                    .setHeight("20.166666666666668em")
                    .setResizer(false)
                    .setCaption("$RAD.pageEditor.modeDlgCap")
                    .setMinBtn(false)
                    .setMaxBtn(false)
                    .setOverflow("hidden")
            );

            host.mainDlg.append(
                ood.create("ood.UI.List")
                    .setHost(host, "modeList")
                    .setDirtyMark(false)
                    .setLeft("0.1875em")
                    .setTop("0.3125em")
                    .setWidth("23.916666666666668em")
                    .setHeight("13.75em")
                    .setBorderType("inset")
                    .setValue("a")
                    .onDblclick("_modelist_ondblclick")
                    .setCustomStyle({
                            "EXTRA": "float:right;display:inline;"
                        }
                    )
            );

            host.mainDlg.append(
                ood.create("ood.UI.Button")
                    .setHost(host, "ctl_sbutton5")
                    .setLeft("13.25em")
                    .setTop("14.5em")
                    .setWidth("5.166666666666667em")
                    .setHeight("2.1666666666666665em")
                    .setCaption("$RAD.ok")
                    .onClick("_ctl_sbutton5_onclick")
            );

            host.mainDlg.append(
                ood.create("ood.UI.Button")
                    .setHost(host, "ctl_sbutton26")
                    .setLeft("5.75em")
                    .setTop("14.5em")
                    .setWidth("5.166666666666667em")
                    .setHeight("2.1666666666666665em")
                    .setCaption("$RAD.cancel")
                    .onClick("_ctl_sbutton26_onclick")
            );

            return children;
            // ]]Code created by ESDUI RAD Studio
        },
        events: {"onReady": "_com_onready"},
        _com_onready: function (com, threadid) {
            var arr = [];


            var modeInfo = [

                {name: "FCL", mime: "text/x-fcl", mode: "fcl", desc: "FCL存储命令"},
                {name: "JDS-Right", mimes: ["application/xml", "text/xml"], mode: "xml", ext: ["right"], desc: "通用权限表达式"},
                {name: "EDRScript", mime: "text/x-edrscript", mode: "edrscript", ext: ["edrscript"], desc: "EDR医疗样本数据"},
                {name: "JDS-El",  mimes: ["application/xml", "text/xml"], mode: "xml", ext: ["el"], desc: "通用脚本控制"},
                {name: "xpdl", mime: "text/x-xpdl", mode: "xpdl", ext: ["xpdl"], desc: "流程定义描述语言"},
                {name: "IOT-Command", mime: "text/x-iotcommand", mode: "iotcommand", ext: ["iotcommand"], desc: "设备通讯指令"},
                {name: "XML", mimes: ["application/xml", "text/xml"], mode: "xml", ext: ["xml", "xsl", "xsd", "svg"], alias: ["rss", "wsdl", "xsd"]},
                {name: "CSS", mime: "text/css", mode: "css", ext: ["css"], desc: "样式风格"},
                {
                    name: "Groovy",
                    mime: "text/x-groovy",
                    mode: "groovy",
                    ext: ["groovy", "gradle"],
                    file: /^Jenkinsfile$/
                },
                {name: "Java", mime: "text/x-java", mode: "clike", ext: ["java"]},
                {
                    name: "Java Server Pages",
                    mime: "application/x-jsp",
                    mode: "htmlembedded",
                    ext: ["jsp"],
                    alias: ["jsp"]
                },
                {
                    name: "JavaScript",
                    mimes: ["text/javascript", "text/javascript", "application/javascript", "application/x-javascript", "application/ecmascript"],
                    mode: "javascript",
                    ext: ["js"],
                    alias: ["ecmascript", "js", "node"]
                },
                {
                    name: "JSON",
                    mimes: ["application/json", "application/x-json"],
                    mode: "javascript",
                    ext: ["json", "map"],
                    alias: ["json5"]
                },
                {name: "Nginx", mime: "text/x-nginx-conf", mode: "nginx", file: /nginx.*\.conf$/i},
                {name: "SQL", mime: "text/x-sql", mode: "sql", ext: ["sql"]},
                {name: "Vue.js Component", mimes: ["script/x-vue", "text/x-vue"], mode: "vue", ext: ["vue"]}

            ];


            ood.arr.each(modeInfo, function (o, i) {
                arr.push({
                    id: o.mime + ":" + o.mode,
                    mode: o.mode,
                    mime: o.mime,
                    name: o.name,
                    caption: o.name,
                    ext: "<i style='color:#0000ff;'>" + (o.desc||o.mime) + "&nbsp;&nbsp;&nbsp;</i>"
                });
            });

            this.modeList.setItems(arr).setValue(null, true);
        },
        _ctl_sbutton26_onclick: function (profile, e, src, value) {
            var ns = this;
            ns.fireEvent("onSel", [null]);
            ns.mainDlg.close();
        },
        _ctl_sbutton5_onclick: function (profile, e, src, value) {
            var ns = this,
                v = ns.modeList.getUIValue(),
                item = ns.modeList.getItemByItemId(v);
            ns.fireEvent("onSel", [item]);
            ns.mainDlg.close();
        },
        _modelist_ondblclick: function (profile, item, e, src) {
            this._ctl_sbutton5_onclick();
        }
    }
});
