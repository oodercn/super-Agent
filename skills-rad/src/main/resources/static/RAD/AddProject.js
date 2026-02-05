ood.Class('RAD.AddProject', 'ood.Module', {
    Instance: {
        _serial: 1,
        customAppend: function (parent) {
            var ns = this,
                dlg = ns.dialog,
                prop = ns.properties,
                item=prop.item,
                pp = prop.parent;
            if (!dlg.get(0).renderId)
                dlg.render();
            var fileName = prop.fileName || "newProject";

            var title = ood.getRes('RAD.addproject.newProject') || "新建工程";
            ns.input.setType("input");
            ns.input.resetValue(fileName+"1");
            ns.url.resetValue(item.url);
            ns.desc.resetValue(item.desc+"1");
            ns.dialog.setCaption(title);
            ns.inputTarget.resetValue(fileName).setTips(fileName);

            dlg.showModal(parent);
            return true;
        },
        reset: function () {
            var ns = this;
            ns.input.setValue("", true).setType("none");
            ns.inputTarget.setValue("", true).setTips("");
        },
        iniComponents: function () {
            // [[Code created by JDSEasy RAD Studio
            var host = this, children = [], properties = {}, append = function (child) {
                children.push(child.get(0));
            };
            ood.merge(properties, this.properties);
            append(
                ood.create("ood.UI.Dialog")
                    .setHost(host, "dialog")
                    .setLeft("8.625em")
                    .setTop("3.625em")
                    .setWidth("40em")
                    .setHeight("22.333333333333332em")
                    .setResizer(false)
                    .setCaption("dialog")
                    .setImageClass("cb-icon-folder-plus")
                    .setMinBtn(false)
                    .setMaxBtn(false)
                    .setOverflow("hidden")
                    .onHotKeydown("_dialog_onhotkey")
                    .beforeClose("_dialog_beforeclose")
            );

            host.dialog.append(
                ood.create("ood.UI.Block")
                    .setHost(host, "ctl_block8")
                    .setLeft("0.375em")
                    .setTop("0.3125em")
                    .setWidth("37.916666666666664em")
                    .setHeight("13.25em")
                    .setBorderType("inset")
                    .setOverflow("hidden")
            );

            host.ctl_block8.append(
                ood.create("ood.UI.ComboInput")
                    .setHost(host, "input")
                    .setDefaultFocus(true)
                    .setLeft("3em")
                    .setTop("3.3333333333333335em")
                    .setWidth("17.25em")
                    .setDynCheck(true)
                    .setLabelSize("6em")
                    .setLabelCaption(ood.getRes('RAD.addproject.projectName') || "工程名称：")
                    .setType("none")
            );

            host.ctl_block8.append(
                ood.create("ood.UI.ComboInput")
                    .setHost(host, "desc")
                    .setLeft("21.333333333333332em")
                    .setTop("3.3333333333333335em")
                    .setWidth("14.5em")
                    .setLabelSize("5em")
                    .setLabelCaption(ood.getRes('RAD.addproject.chineseName') || "中文名称")
                    .setType("none")
            );




            host.ctl_block8.append(
                ood.create("ood.UI.ComboInput")
                    .setHost(host, "url")
                    .setDefaultFocus(true)
                    .setLeft("0.5em")
                    .setTop("6.416666666666667em")
                    .setWidth("34.5em")
                    .setHeight("1.8333333333333333em")
                    .setDynCheck(true)
                    .setLabelSize("8.333333333333334em")
                    .setLabelCaption(ood.getRes('RAD.addproject.publishAddress') || "发布地址：")
                    .setType("none")

            );

            host.ctl_block8.append(
                ood.create("ood.UI.ComboInput")
                    .setHost(host, "inputTarget")
                    .setReadonly(true)
                    .setLeft("0.75em")
                    .setTop("0.75em")
                    .setWidth("35.5em")
                    .setLabelSize("8.333333333333334em")
                    .setLabelCaption(ood.getRes('RAD.addproject.templateName') || "模板名称：")
                    .setType("none")
            );


            host.ctl_block8.append(
                ood.create("ood.UI.ComboInput")
                    .setHost(host, "projectType")
                    .setLeft("0.5em")
                    .setTop("9.5333333333333335em")
                    .setWidth("34.5em")
                    .setLabelSize("5em")
                    .setLabelCaption(ood.getRes('RAD.addproject.projectType') || "工程类型:")
                    .setType("none")
            );


            host.dialog.append(
                ood.create("ood.UI.Button")
                    .setHost(host, "btnCancel")
                    .setLeft("8.5em")
                    .setTop("14.066666666666666em")
                    .setWidth("7.833333333333333em")
                    .setTabindex(3)
                    .setCaption("$RAD.cancel")
                    .setImageClass("cb-icon-close")
                    .onClick("_btncancel_onclick")
            );

            host.dialog.append(
                ood.create("ood.UI.Button")
                    .setHost(host, "btnOK")
                    .setLeft("22.666666666666668em")
                    .setTop("14.066666666666666em")
                    .setWidth("7.833333333333333em")
                    .setTabindex(4)
                    .setCaption("$RAD.ok")
                    .setImageClass("cb-icon-check")
                    .onClick("_btnok_onclick")
            );

            return children;
            // ]]Code created by JDSEasy RAD Studio
        },
        _dialog_beforeclose: function (profile) {
            this.properties = {};
            this.dialog.hide();
            return false;
        },
        _btncancel_onclick: function (profile, e, value) {
            this.dialog.close();
        },

        _btnok_onclick: function (profile, e, value) {
            this._apply();
        },
        _apply: function () {
            var ns = this,
                prop = ns.properties,
                type = prop.type,
                fileName = ns.input.getUIValue(),
                url = ns.url.getUIValue(),
                projectType = ns.projectType.getUIValue(),
                desc = ns.desc.getUIValue(),
                pPath = ns._pPath,
                cls;
            if (!fileName) {
                ood.message(ood.getRes('RAD.addfile.notarget'));
                return;
            } else {
                ns.dialog.busy(ood.getRes('RAD.addproject.creating') || "正在创建...");
                ood.Ajax(CONF.newProject, {
                    tempName: prop.tempName,
                    path: pPath,
                    url: url,
                    projectType:projectType,
                    desc: desc,
                    newProjectName: fileName
                }, function (txt) {
                    var obj = txt;
                    ns.dialog.free();
                    if (obj && obj.requestStatus != -1) {
                        ns.dialog.close();
                        $ESD.openProject(fileName)
                        // ns.fireEvent('onOK', [pPath, fileName, fileName, type]);
                    } else if (obj && !obj.error) {
                        ood.message(ood.get(obj, ['error', 'errdes']) || obj || 'no response!')

                    } else ood.message(ood.get(obj, ['error', 'errdes']) || obj || 'no response!');
                }, null, null, {method: 'POST'}).start();
            }
        },
        _dialog_onhotkey: function (profile, key) {
            if (key.key == 'esc')
                profile.boxing().close();
        }
    },
    Static: {
        viewStyles: {}
    }
});
