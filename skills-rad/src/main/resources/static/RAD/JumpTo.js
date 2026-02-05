ood.Class('RAD.JumpTo', 'ood.Module', {
    Instance: {
        initialize: function () {
            this.autoDestroy = true;
            this.properties = {};
        },
        iniComponents: function () {
            // [[Code created by ESDUI RAD Studio
            var host = this, children = [], append = function (child) {
                children.push(child.get(0));
            };

            append(
                ood.create("ood.UI.Dialog")
                    .setHost(host, "ctl_dialog")
                    .setLeft("4.375em")
                    .setTop("3.25em")
                    .setWidth("18em")
                    .setHeight("12em")
                    .setResizer(false)
                    .setCaption("$RAD.jumpto")
                    .setMinBtn(false)
                    .setMaxBtn(false)
                    .setOverflow("hidden")
                    .onHotKeydown("_ctl_dialog_onhotkeydown")
                    .beforeClose("_ctl_dialog_beforeclose")
            );

            host.ctl_dialog.append(
                ood.create("ood.UI.Label")
                    .setHost(host, "ctl_slabel1")
                    .setLeft("0.625em")
                    .setTop("0.5833333333333334em")
                    .setWidth("4.625em")
                    .setCaption("$RAD.jumpto")
            );

            host.ctl_dialog.append(
                ood.create("ood.UI.ComboInput")
                    .setHost(host, "ctl_comboinput3")
                    .setDirtyMark(false)
                    .setLeft("6.875em")
                    .setTop("0.3333333333333333em")
                    .setWidth("5.583333333333333em")
                    .setHeight("1.9166666666666667em")
                    .setType("spin")
                    .setPrecision(0)
                    .setIncrement(1)
                    .setMin(1)
                    .setMax(100000)
                    .setValue(1)
            );

            host.ctl_dialog.append(
                ood.create("ood.UI.Button")
                    .setHost(host, "ctl_sbutton5")
                    .setLeft("2.5em")
                    .setTop("4.166666666666667em")
                    .setWidth("4.666666666666667em")
                    .setHeight("1.9166666666666667em")
                    .setCaption("$RAD.cancel")
                    .onClick("_ctl_sbutton5_onclick")
            );

            host.ctl_dialog.append(
                ood.create("ood.UI.Button")
                    .setHost(host, "ctl_btnok")
                    .setLeft("9.166666666666666em")
                    .setTop("4.166666666666667em")
                    .setWidth("4.666666666666667em")
                    .setHeight("1.9166666666666667em")
                    .setCaption("$RAD.ok")
                    .onClick("_ctl_btnok_onclick")
            );


            host.ctl_dialog.append(
                ood.create("ood.UI.Block")
                    .setHost(host, "ood_ui_block112")
                    .setLeft("0.8333333333333334em")
                    .setTop("3.3333333333333335em")
                    .setWidth("14.166666666666666em")
                    .setHeight("0em")
                    .setBorderType("inset")
            );

            return children;
            // ]]Code created by ESDUI RAD Studio
        },
        customAppend: function (parent, subId, left, top) {
            this.ctl_comboinput3.setUIValue(1, true)
            this.ctl_dialog.showModal(parent, left, top);
        },
        events: {},
        _ctl_sbutton5_onclick: function (profile, e, src, value) {
            this.ctl_dialog.close();
        },
        _ctl_dialog_beforeclose: function (profile) {
            this.ctl_dialog.hide();
            return false;
        },
        _ctl_btnok_onclick: function (profile, e, src, value) {
            this.fireEvent("onOK", [this.ctl_comboinput3.getUIValue()]);
            this.ctl_dialog.close();
        },
        _ctl_dialog_onhotkeydown: function (profile, keyboard) {
            if (keyboard.key == 'esc') {
                this.ctl_dialog.close();
            }
        }
    }
});
