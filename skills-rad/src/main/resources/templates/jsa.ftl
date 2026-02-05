<!DOCTYPE html>
<html>
<head>

    <meta http-equiv="content-type" content="text/html; charset=utf-8"/>
    <meta http-equiv="Content-Style-Type" content="text/css"/>
    <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1"/>
    <meta http-equiv="imagetoolbar" content="no"/>
    <meta name="viewport"
          content="user-scalable=no, initial-scale=1.2, minimum-scale=0.5, maximum-scale=2.0,width=device-width, height=device-height"/>
    <link rel="stylesheet" type="text/css" href="/css/default.css"/>
    <link href="https://cdn.bootcdn.net/ajax/libs/font-awesome/6.4.0/css/all.min.css" rel="stylesheet">
    <!-- 引入Remix Icon -->
    <link rel="stylesheet" type="text/css" href="/css/remixicon/remixicon.css"/>    <#--<link rel="stylesheet" type="text/css" href="/css/remixicon/remixicon.css"/>-->
    <link rel="stylesheet" type="text/css" href="/ood/iconfont/iconfont.css"/>
    <!-- 默认加载dark主题 -->
    <link rel="stylesheet" type="text/css" href="/ood/appearance/dark/theme.css" id="theme-style"/>
    <link rel="stylesheet" type="text/css" href="/ood/js/mobile/mobile.css" id="theme-style"/>
    <link rel="stylesheet" type="text/css" href="/plugins/formlayout/handsontable.full.min.css"/>
    <!-- 移除这些冲突的引用 -->
    <script type="text/javascript" src="/ood/js/ThirdParty/hammer.js"></script>
    <script type="text/javascript" src="/ood/js/ood.js"></script>

    <script type="text/javascript" src="/ood/js/APICaller.js"></script>
    <script type="text/javascript" src="/ood/js/MQTT.js"></script>
    <script type="text/javascript" src="/ood/js/DataBinder.js"></script>
    <script type="text/javascript" src="/ood/js/Event.js"></script>
    <script type="text/javascript" src="/ood/js/CSS.js"></script>
    <script type="text/javascript" src="/ood/js/Dom.js"></script>
    <script type="text/javascript" src="/ood/js/DragDrop.js"></script>
    <script type="text/javascript" src="/ood/js/Template.js"></script>
    <script type="text/javascript" src="/ood/js/Cookies.js"></script>
    <script type="text/javascript" src="/ood/js/History.js"></script>
    <script type="text/javascript" src="/ood/js/Tips.js"></script>
    <script type="text/javascript" src="/ood/js/Module.js"></script>
    <script type="text/javascript" src="/ood/js/XML.js"></script>
    <script type="text/javascript" src="/ood/js/XMLRPC.js"></script>
    <script type="text/javascript" src="/ood/js/SOAP.js"></script>
    <script type="text/javascript" src="/ood/js/ModuleFactory.js"></script>
    <script type="text/javascript" src="/ood/js/Debugger.js"></script>
    <script type="text/javascript" src="/ood/js/Date.js"></script>
    <script type="text/javascript" src="/ood/Locale/en.js"></script>
    <script type="text/javascript" src="/ood/js/UI.js"></script>
    <script type="text/javascript" src="/ood/js/IconMapping.js"></script>
    <script type="text/javascript" src="/ood/js/UI/Image.js"></script>
    <script type="text/javascript" src="/ood/js/UI/Flash.js"></script>
    <script type="text/javascript" src="/ood/js/UI/Audio.js"></script>
    <script type="text/javascript" src="/ood/js/UI/FileUpload.js"></script>
    <script type="text/javascript" src="/ood/js/UI/Video.js"></script>
    <script type="text/javascript" src="/ood/js/UI/Tensor.js"></script>
    <script type="text/javascript" src="/ood/js/UI/Camera.js"></script>
    <script type="text/javascript" src="/ood/js/UI/Resizer.js"></script>
    <script type="text/javascript" src="/ood/js/UI/Block.js"></script>
    <script type="text/javascript" src="/ood/js/UI/Label.js"></script>
    <script type="text/javascript" src="/ood/js/UI/ProgressBar.js"></script>
    <script type="text/javascript" src="/ood/js/UI/Slider.js"></script>
    <script type="text/javascript" src="/ood/js/UI/Input.js"></script>
    <script type="text/javascript" src="/ood/js/UI/CheckBox.js"></script>
    <script type="text/javascript" src="/ood/js/UI/HiddenInput.js"></script>
    <script type="text/javascript" src="/ood/js/UI/RichEditor.js"></script>
    <script type="text/javascript" src="/ood/js/UI/ComboInput.js"></script>
    <script type="text/javascript" src="/ood/js/UI/ColorPicker.js"></script>
    <script type="text/javascript" src="/ood/js/UI/DatePicker.js"></script>
    <script type="text/javascript" src="/ood/js/UI/TimePicker.js"></script>
    <script type="text/javascript" src="/ood/js/UI/List.js"></script>
    <script type="text/javascript" src="/ood/js/UI/Gallery.js"></script>
    <script type="text/javascript" src="/ood/js/UI/ButtonLayout.js"></script>
    <script type="text/javascript" src="/ood/js/UI/TitleBlock.js"></script>
    <script type="text/javascript" src="/ood/js/UI/ContentBlock.js"></script>
    <script type="text/javascript" src="/ood/js/UI/Panel.js"></script>
    <script type="text/javascript" src="/ood/js/UI/Group.js"></script>
    <script type="text/javascript" src="/ood/js/UI/PageBar.js"></script>
    <script type="text/javascript" src="/ood/js/UI/Tabs.js"></script>
    <script type="text/javascript" src="/ood/js/UI/Stacks.js"></script>
    <script type="text/javascript" src="/ood/js/UI/ButtonViews.js"></script>
    <script type="text/javascript" src="/ood/js/UI/MTabs.js"></script>
    <script type="text/javascript" src="/ood/js/UI/RadioBox.js"></script>
    <script type="text/javascript" src="/ood/js/UI/StatusButtons.js"></script>
    <script type="text/javascript" src="/ood/js/UI/TreeBar.js"></script>
    <script type="text/javascript" src="/ood/js/UI/TreeView.js"></script>
    <script type="text/javascript" src="/ood/js/UI/MTreeView.js"></script>
    <script type="text/javascript" src="/ood/js/UI/PopMenu.js"></script>
    <script type="text/javascript" src="/ood/js/UI/MenuBar.js"></script>
    <script type="text/javascript" src="/ood/js/UI/ToolBar.js"></script>
    <script type="text/javascript" src="/ood/js/UI/Layout.js"></script>
    <script type="text/javascript" src="/ood/js/UI/TreeGrid.js"></script>
    <script type="text/javascript" src="/ood/js/UI/MTreeGrid.js"></script>
    <script type="text/javascript" src="/ood/js/UI/Dialog.js"></script>
<#--<script type="text/javascript" src="/ood/js/UI/MDialog.js"></script>-->
    <script type="text/javascript" src="/ood/js/UI/FormLayout.js"></script>
    <script type="text/javascript" src="/ood/js/UI/MFormLayout.js"></script>
    <script type="text/javascript" src="/ood/js/UI/FoldingTabs.js"></script>
    <script type="text/javascript" src="/ood/js/UI/FoldingList.js"></script>
    <script type="text/javascript" src="/ood/js/UI/Opinion.js"></script>
    <script type="text/javascript" src="/ood/js/ThirdParty/raphael.js"></script>
    <script type="text/javascript" src="/ood/js/svg.js"></script>
    <script type="text/javascript" src="/ood/js/UI/SVGPaper.js"></script>
    <script type="text/javascript" src="/ood/js/UI/FusionChartsXT.js"></script>
    <script type="text/javascript" src="/ood/js/UI/ECharts.js"></script>
    <script type="text/javascript" src="/ood/js/Coder.js"></script>
    <script type="text/javascript" src="/ood/js/Module/JSONEditor.js"></script>

    <script type="text/javascript" src="/RAD/custom/AdvResizer.js"></script>

    <script type="text/javascript" src="/ood/Locale/cn.js"></script>
    <script type="text/javascript" src="/plugins/codemirror5/lib/codemirror.js"></script>
    <script type="text/javascript" src="/plugins/codemirror5/mode/meta.js"></script>
    <script type="text/javascript" src="/plugins/codemirror5/keymap/sublime.js"></script>
    <script type="text/javascript" src="/plugins/codemirror5/addon/mode/loadmode.js"></script>
    <script type="text/javascript" src="/plugins/codemirror5/addon/lint/jshint.js"></script>
    <script type="text/javascript" src="/plugins/codemirror5/addon/lint/jsonlint.js"></script>
    <script type="text/javascript" src="/plugins/codemirror5/addon/lint/csslint.js"></script>
    <script type="text/javascript" src="/plugins/codemirror5/addon/lint/htmlhint.js"></script>
    <script type="text/javascript" src="/plugins/codemirror5/addon/lint/lint.js"></script>
    <script type="text/javascript" src="/plugins/codemirror5/addon/lint/javascript-lint.js"></script>
    <script type="text/javascript" src="/plugins/codemirror5/addon/lint/json-lint.js"></script>
    <script type="text/javascript" src="/plugins/codemirror5/addon/lint/css-lint.js"></script>
    <script type="text/javascript" src="/plugins/codemirror5/addon/lint/html-lint.js"></script>
    <script type="text/javascript" src="/plugins/codemirror5/addon/fold/foldcode.js"></script>
    <script type="text/javascript" src="/plugins/codemirror5/addon/fold/foldgutter.js"></script>
    <script type="text/javascript" src="/plugins/codemirror5/addon/fold/brace-fold.js"></script>
    <script type="text/javascript" src="/plugins/codemirror5/addon/fold/xml-fold.js"></script>
    <script type="text/javascript" src="/plugins/codemirror5/addon/fold/comment-fold.js"></script>
    <script type="text/javascript" src="/plugins/codemirror5/addon/selection/active-line.js"></script>
    <script type="text/javascript" src="/plugins/codemirror5/addon/search/searchcursor.js"></script>
    <script type="text/javascript" src="/plugins/codemirror5/addon/search/match-highlighter.js"></script>
    <script type="text/javascript" src="/plugins/codemirror5/addon/edit/matchbrackets.js"></script>
    <script type="text/javascript" src="/plugins/codemirror5/addon/edit/closebrackets.js"></script>
    <script type="text/javascript" src="/plugins/codemirror5/addon/comment/comment.js"></script>
    <script type="text/javascript" src="/plugins/codemirror5/addon/comment/continuecomment.js"></script>
    <script type="text/javascript" src="/plugins/codemirror5/mode/javascript/javascript.js"></script>
    <script type="text/javascript" src="/plugins/codemirror5/mode/xml/xml.js"></script>
    <script type="text/javascript" src="/plugins/codemirror5/mode/css/css.js"></script>
    <script type="text/javascript" src="/plugins/codemirror5/mode/htmlmixed/htmlmixed.js"></script>


    <script type="text/javascript" src="/RAD/conf.js"></script>'
    <script type="text/javascript" src="/RAD/InteractionMap.js"></script>
    <script type="text/javascript" src="/RAD/conf_widgets.js"></script>
    <script type="text/javascript" src="/RAD/SPAConf.js"></script>
    <script type="text/javascript" src="/RAD/fcconf.js"></script>
    <script type="text/javascript" src="/RAD/CodeEditor.js"></script>
    <script type="text/javascript" src="/RAD/ClassTool.js"></script>
    <script type="text/javascript" src="/RAD/JSEditor.js"></script>
    <script type="text/javascript" src="/RAD/expression/JavaEditor.js"></script>
    <script type="text/javascript" src="/RAD/PageEditor.js"></script>
    <script type="text/javascript" src="/RAD/FunEditor.js"></script>
    <script type="text/javascript" src="/RAD/JSONEditor.js"></script>
    <script type="text/javascript" src="/RAD/ObjectEditor2.js"></script>
    <script type="text/javascript" src="/RAD/FileSelector.js"></script>
    <script type="text/javascript" src="/RAD/MobileInstruction.js"></script>
    <script type="text/javascript" src="/RAD/HTMLEditor.js"></script>
    <script type="text/javascript" src="/RAD/Designer.js"></script>
    <script type="text/javascript" src="/RAD/ClassTree.js"></script>
    <script type="text/javascript" src="/RAD/ServiceTree.js"></script>
    <script type="text/javascript" src="/RAD/ProjectTree.js"></script>

    <script type="text/javascript" src="/RAD/JumpTo.js"></script>
    <script type="text/javascript" src="/RAD/FAndR.js"></script>
    <script type="text/javascript" src="/RAD/EditorTool.js"></script>
    <script type="text/javascript" src="/RAD/EditorTheme.js"></script>
    <script type="text/javascript" src="/RAD/ServiceTester.js"></script>
    <script type="text/javascript" src="/RAD/SelRenderMode.js"></script>
    <script type="text/javascript" src="/RAD/About.js"></script>
    <script type="text/javascript" src="/RAD/GridBorder.js"></script>


    <script type="text/javascript" src="/RAD/CustomDecoration.js"></script>
    <script type="text/javascript" src="/RAD/CustomDecoration2.js"></script>
    <script type="text/javascript" src="/RAD/CustomBorder.js"></script>
    <script type="text/javascript" src="/RAD/CustomBoxShadow.js"></script>
    <script type="text/javascript" src="/RAD/CustomTextShadow.js"></script>
    <script type="text/javascript" src="/RAD/CustomTransform.js"></script>
    <script type="text/javascript" src="/RAD/CustomGradients.js"></script>
    <script type="text/javascript" src="/RAD/CustomEffects.js"></script>
    <script type="text/javascript" src="/RAD/ActionsEditor.js"></script>
    <script type="text/javascript" src="/RAD/InteractionMap.js"></script>
    <script type="text/javascript" src="/RAD/AnimationConf.js"></script>
    <script type="text/javascript" src="/RAD/EventPicker.js"></script>

    <script type="text/javascript" src="/RAD/SVGCustomDecoration.js"></script>
    <script type="text/javascript" src="/RAD/SVGCustomGradients.js"></script>
    <script type="text/javascript" src="/RAD/ChartCustomSetting.js"></script>
    <script type="text/javascript" src="/RAD/CustomArrows.js"></script>
    <script type="text/javascript" src="/RAD/PropEditor.js"></script>
    <script type="text/javascript" src="/RAD/MixPropEditor.js"></script>

    <script type="text/javascript" src="/RAD/CustomPackage.js"></script>
    <script type="text/javascript" src="/RAD/APIConfig.js"></script>

    <script type="text/javascript" src="/RAD/SwitchWorkspace.js"></script>

    <script type="text/javascript" src="/RAD/UIDesigner.js"></script>
    <script type="text/javascript" src="/RAD/SelFontAwesome.js"></script>

    <script type="text/javascript" src="/RAD/OODBuilder.js"></script>
    <script type="text/javascript" src="/RAD/AddFile.js"></script>
    <script type="text/javascript" src="/RAD/ProjectPro.js"></script>
    <script type="text/javascript" src="/RAD/TemplatePreview.js"></script>
    <script type="text/javascript" src="/RAD/TemplateSelector.js"></script>
    <script type="text/javascript" src="/RAD/ImageSelector.js"></script>
    <script type="text/javascript" src="/RAD/expression/JavaEditor.js"></script>
    <script type="text/javascript" src="/RAD/expression/CodeEditor.js"></script>
    <script type="text/javascript" src="/plugins/formlayout/handsontable.full.min.js"></script>
    <script type="text/javascript" src="/RAD/expression/ExpressionTemp.js"></script>
    <script type="text/javascript" src="/RAD/expression/ExpressionEditor.js"></script>
    <script type="text/javascript" src="/RAD/expression/PageEditor.js"></script>


<#--<script type="text/javascript" src="/RAD/conf.js"></script>-->
<#--<script type="text/javascript" src="/RAD/CodeEditor.js"></script>-->
<#--<script type="text/javascript" src="/RAD/ClassTool.js"></script>-->
<#--<script type="text/javascript" src="/RAD/JSEditor.js"></script>-->
<#--<script type="text/javascript" src="/RAD/expression/CodeEditor.js"></script>-->
<#--<script type="text/javascript" src="/RAD/expression/JavaEditor.js"></script>-->

    <title>${projectName}预览界面</title>
</head>


<script type="text/javascript">
    currProjectName = "${projectName}";
    domainId = "${domainId}";
    var args = ood.getUrlParams(),
            onEnd = function () {
                ood('loading').remove();
                SPA = this;
                SPA.curProjectName = currProjectName;
                this.initData();
            };
    ood.launch('${className}', onEnd, 'cn', args && args.theme || 'default');


    CONF.widgets_itemsProp.items_conf =${itemsProp},

            CONF.widgets_itemsProp.items =${items},

            $E = ood.execExpression;
    $BPD = {
        open: function () {
            $E('$BPD.open()')
        },


        close: function () {
            $E('$BPD.close()')
        }
    }

    $ESD = {
        open: function (url, params) {
            var paramArr = params | {};
            if (url) {
                paramArr.url = url;
            }
            $E('$ESD.open()', paramArr)
        },
        screen: function () {
            var paramArr = {projectName: "${projectName}"};
            paramArr.url = window.location.href;
            $E('$ESD.screen()', paramArr)
        },
        reload: function (url, params) {
            var paramArr = params || {projectName: "${projectName}"};
            paramArr.handleId = window.handleId;
            if (url) {
                paramArr.url = url;

            }
            $E('$ESD.reload()', paramArr)
        },

        quit: function () {
            $E('$BPD.quit()')
        },
        logout: function () {
            $E('$BPD.logout()')
        }
    }

</script>
