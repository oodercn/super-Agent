
ood.Class('RAD.About', 'ood.Module',{
    Instance:{
        initialize : function(){
        },
        Dependencies:[],
        Required:[],
        properties : {

        },

        events:{},
        functions:{},
        iniComponents : function(){
            // [[Code created by JDSEasy RAD Studio
            var host=this, children=[], properties={}, append=function(child){children.push(child.get(0));};
            ood.merge(properties, this.properties);

            // 使用命令方式组合视图并设置组件属性
            var dialog = ood.create("ood.UI.Dialog");
            dialog.setHost(host,"ood_ui_dialog17");
            dialog.setLeft("9.166666666666666em");
            dialog.setTop("5.833333333333333em");
            dialog.setWidth("32.833333333333336em");
            dialog.setHeight("17.833333333333332em");
            dialog.setResizer(false);
            dialog.setCaption("$RAD.menu.about");
            dialog.setMinBtn(false);
            dialog.setMaxBtn(false);
            dialog.setOverflow("hidden");
            append(dialog);

            host.ood_ui_dialog17.append(
                ood.create("ood.UI.Div")
                    .setHost(host,"ood_ui_div43")
                    .setLeft("1.6666666666666667em")
                    .setTop("0.625em")
                    .setWidth("27.916666666666668em")
                    .setHeight("3.75em")
                    .setHtml("<b><i>onecode<br>" + ood.getRes('RADAbout.description') + "</i></b>")
            );

            host.ood_ui_dialog17.append(
                ood.create("ood.UI.Button")
                    .setHost(host,"ood_ui_button23")
                    .setLeft("11.666666666666666em")
                    .setTop("11.583333333333334em")
                    .setWidth("9.166666666666666em")
                    .setHeight("2.5em")
                    .setZIndex("10")
                    .setCaption("$inline.ok")
                    .onClick([
                        {
                            "desc": ood.getRes('RADAbout.action1'),
                            "type":"page",
                            "target":"RAD.About",
                            "args":[ ],
                            "method":"destroy"
                        }
                    ])
            );

            host.ood_ui_dialog17.append(
                ood.create("ood.UI.Image")
                    .setHost(host,"ood_ui_image13")
                    .setLeft("15.833333333333334em")
                    .setTop("-2.0833333333333335em")
                    .setWidth("3.8333333333333335em")
                    .setHeight("3.4166666666666665em")
                    .setActiveItem("/RAD/img/logo.png")
                    .onClick([
                        {
                            "desc":"url",
                            "type":"other",
                            "target":"url",
                            "params":[
                                "http://www.itjds.com:81"
                            ],
                            "method":"open--_blank"
                        }
                    ])
            );

            host.ood_ui_dialog17.append(
                ood.create("ood.UI.Label")
                    .setHost(host,"ood_ui_label29")
                    .setLeft("5.833333333333333em")
                    .setTop("6.666666666666667em")
                    .setWidth("17.25em")
                    .setCaption(ood.getRes('RADAbout.contact'))
            );

            host.ood_ui_dialog17.append(
                ood.create("ood.UI.Label")
                    .setHost(host,"ood_ui_label30")
                    .setLeft("10.416666666666666em")
                    .setTop("7.916666666666667em")
                    .setWidth("12.666666666666666em")
                    .setHeight("2.4166666666666665em")
                    .setCaption(ood.getRes('RADAbout.wechat'))
            );

            return children;
            // ]]Code created by JDSEasy RAD Studio
        }
    } ,
    Static:{}
});
