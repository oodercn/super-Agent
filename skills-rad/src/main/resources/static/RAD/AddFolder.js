ood.Class('RAD.AddFolder', 'ood.Module', {
    Instance: {
        _serial: 1,
        ajax: {},
        customAppend: function (parent) {
            var ns = this,
                dlg = ns.dialog,
                prop = ns.properties,
                pp = prop.parent;

            if (!dlg.get(0).renderId)
                dlg.render();

            var fileName = prop.fileName || "newFileName";
            var parentPath = prop.parentPath;

            if (SPA.curProjectPath) {
                var curProjectPath = SPA.curProjectPath;
                if (curProjectPath.indexOf("VVVERSION") > -1) {
                    curProjectPath = curProjectPath.replace("VVVERSION", "(V") + ")";
                }
                if (parentPath == curProjectPath) {
                    parentPath = "";
                } else if (parentPath + '/' == curProjectPath) {
                    parentPath = "";
                } else {
                    parentPath = ood.replace(prop.parentPath, curProjectPath, "");
                }
            }

            var v = parentPath;
            fileName = fileName + ((new Date).getMinutes() + (ns._serial++));
            ns.input.setType("input");
            ns.input.resetValue(fileName);
            title = "$RAD.addfile.newfolder";
            if (ood.str.endWith(parentPath, "/")) {
                v = parentPath + fileName;
            } else {
                v = parentPath + '/' + fileName;
            }

            ns.dialog.setCaption(title);
            ns._pPath = parentPath;

            ns.inputTarget.resetValue(v).setTips(v);
            ns.blockTypeCap.setHtml("<div style='padding:2px 0 0 2px'>" + prop.typeCaption + "</div>");
            //  ns._refresh();
            dlg.showModal(parent);
            return true;
        },
        reset: function () {
            var ns = this;
            ns.input.setValue("", true).setType("none");
            ns.inputTarget.setValue("", true).setTips("");
        },
        iniComponents: function () {
            // [[Code created by EUSUI RAD Studio
            var host = this, children = [], append = function (child) {
                children.push(child.get(0));
            };

            append(
                ood.create("ood.UI.Dialog")
                    .setHost(host, "dialog")
                    .setLeft("8.625em")
                    .setTop("3.625em")
                    .setWidth("40em")
                    .setHeight("13.5em")
                    .setResizer(false)
                    .setImageClass("cb-icon-file-plus")
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
                    .setHeight("5.916666666666667em")
                    .setBorderType("inset")
                    .setOverflow("hidden")
            );

            host.ctl_block8.append(
                ood.create("ood.UI.Label")
                    .setHost(host, "label3")
                    .setLeft("24.083333333333332em")
                    .setTop("3.5833333333333335em")
                    .setWidth("4.375em")
                    .setCaption("$RAD.addfile.filetype")
            );

            host.ctl_block8.append(
                ood.create("ood.UI.ComboInput")
                    .setHost(host, "input")
                    .setDefaultFocus(true)
                    .setLeft("1.25em")
                    .setTop("3.25em")
                    .setWidth("22em")
                    .setDynCheck(true)
                    .setLabelSize("8.333333333333334em")
                    .setLabelCaption("$RAD.addfile.filename")
                    .setType("none")
                    .beforeUIValueSet("_beforeInpuSet")
                    .afterUIValueSet("_refresh")
            );

            host.ctl_block8.append(
                ood.create("ood.UI.ComboInput")
                    .setHost(host, "inputTarget")
                    .setReadonly(true)
                    .setLeft("1.25em")
                    .setTop("0.75em")
                    .setWidth("35.5em")
                    .setLabelSize("8.333333333333334em")
                    .setLabelCaption("$RAD.addfile.target")
                    .setType("none")
            );

            host.ctl_block8.append(
                ood.create("ood.UI.Block")
                    .setHost(host, "blockTypeCap")
                    .setLeft("28.833333333333332em")
                    .setTop("3.3333333333333335em")
                    .setWidth("7.916666666666667em")
                    .setHeight("1.75em")
                    .setBorderType("inset")
            );

            host.dialog.append(
                ood.create("ood.UI.Button")
                    .setHost(host, "btnCancel")
                    .setLeft("10.833333333333334em")
                    .setTop("7.166666666666667em")
                    .setWidth("7.833333333333333em")
                    .setTabindex(3)
                    .setImageClass("cb-icon-close")
                    .setCaption("$RAD.cancel")
                    .onClick("_btncancel_onclick")
            );

            host.dialog.append(
                ood.create("ood.UI.Button")
                    .setHost(host, "btnOK")
                    .setLeft("21.416666666666668em")
                    .setTop("7.166666666666667em")
                    .setWidth("7.833333333333333em")
                    .setTabindex(4)
                    .setImageClass("cb-icon-check")
                    .setCaption("$RAD.ok")
                    .onClick("_btnok_onclick")
            );

            return children;
            // ]]Code created by EUSUI RAD Studio
        },
        _dialog_beforeclose: function (profile) {
            this.properties = {};
            this.dialog.hide();
            return false;
        },
        _btncancel_onclick: function (profile, e, value) {
            this.dialog.close();
        },
        _beforeInpuSet: function (profile, ov, nv) {
            var ns = this;

        },
        _refresh: function (profile, oldValue, newValue) {
            var ns = this,
                path = ns._pPath,
                prop = ns.properties;
            newValue = ns._adjuestFileName(newValue);
            if (path) {
                newValue = path + newValue;
            }

            ns.blockTypeCap.setHtml("<div style='padding:2px 0 0 2px'>" + newValue.substring(newValue.indexOf(".")) + "</div>");
            ns.inputTarget.resetValue(newValue);
        },
        _adjuestFileName: function (fileName) {
            return fileName.replace(/^ +/, "").replace(/[\. ]*$/, "").replace(/\"/g, "");
        },
        _btnok_onclick: function (profile, e, value) {
            this._apply();
        },
        _apply: function () {
            var ns = this,
            fileName = ns.input.getUIValue();
            fileName = ns._adjuestFileName(fileName);
            if (!fileName) {
                ood.message(ood.getRes('RAD.addfile.notarget'));
                return;
            } else {
                ns.dialog.busy(ood.adjustRes("$(RAD.tpl.Building the template)..."));
                ns.ajax.setQueryData({
                    projectName: SPA.curProjectName,
                    path: fileName,
                    fileName: fileName
                });
                ns.ajax.invoke(
                    function (txt) {
                        var obj = txt;
                        ns.dialog.free();
                        if (obj && obj.requestStatus != -1) {
                            ns.dialog.close();
                            ns.fireEvent('onOK', [obj.data, "/"]);
                        } else if (obj && !obj.error) {
                            ood.message(ood.get(obj, ['error', 'errdes']) || obj || 'no response!')

                        } else ood.message(ood.get(obj, ['error', 'errdes']) || obj || 'no response!');
                    }
                );
            }

        },
        _dialog_onhotkey: function (profile, key) {
            if (key.key == 'esc')
                profile.boxing().close();
        }
    }
});
