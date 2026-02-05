itemConfig= {

    widgets_xprops: {
        'ood.Module': ["onReady", "onRender", "onModulePropChange", "onMessage", "onFragmentChanged"],
        'ood.APICaller': ["name", "queryURL", "requestType", "responseType", "queryMethod", "queryOptions", "queryArgs", "responseCallback", "beforeData", "onError", "onData", "proxyInvoker"],
        'ood.MQTT': ["server", "useSSL", "port", "path", "clientId", "userName", "password", "subscribers", "onMsgArrived"],
        'ood.Timer': ['interval', 'autoStart', 'onTime'],
        'ood.AnimBinder': ['type', 'frames'],
        'ood.MessageService': ['msgType', 'asynReceive', 'onMessageReceived'],
        'ood.UI.CSSBox': ['html', 'className', 'customCss', 'sandbox', 'normalStatus', 'hoverStatus', 'activeStatus'],
        'ood.UI.Element': ['html', 'nodeName', 'position', 'className', 'attributes'],
        'ood.UI.Icon': ['imageClass', 'dock', 'iconFontSize', 'iconColor', 'onClick'],
        'ood.UI.Span': ['html', 'position', 'className', 'dock'],
        'ood.UI.Label': ['caption', 'clock', 'fontSize', 'fontWeight', 'fontColor', 'fontFamily', 'imageClass', 'iconFontCode', 'hAlign', 'className', 'dock', 'onClick'],
        'ood.UI.Link': ['caption', 'dock', 'target', 'onClick'],
        'ood.UI.HTMLButton': ['caption', 'fontSize', 'visibility', 'fontWeight', 'fontColor', 'fontFamily', 'position', 'className', 'attributes', 'onClick'],
        'ood.UI.Button': ['type', 'value', 'caption', 'visibility', 'fontSize', 'fontWeight', 'fontColor', 'fontFamily', 'imageClass', 'iconFontCode', 'dirtyMark', 'onClick', 'onChange'],
        'ood.UI.CheckBox': ['value', 'caption', 'readonly', 'disabled', 'imageClass', 'iconFontCode', 'dock', 'dirtyMark', 'onChange'],
        'ood.UI.Input': ['value', 'readonly', 'disabled', 'type', 'valueFormat', 'labelPos', 'labelSize', 'labelCaption', 'dock', 'dirtyMark', 'onChange'],
        'ood.UI.HiddenInput': ['value'],
        'ood.UI.RichEditor': ['value', 'dock', 'readonly', 'disabled', 'dirtyMark', 'onChange'],
        'ood.UI.List': ['items', 'value', 'readonly', 'disabled', 'dock', 'dirtyMark', 'onItemSelected', 'onChange'],
        'ood.UI.ComboInput': ['type', 'imageClass', 'readonly', 'disabled', 'iconFontCode', 'commandBtn', 'value', 'labelSize', 'labelCaption', 'dock', 'dirtyMark', 'items', 'onClick', 'onCommand', 'onChange', 'beforeComboPop', 'beforePopShow'],
        'ood.UI.ProgressBar': ['value', 'captionTpl', 'type', 'fillBG', 'dock', 'dirtyMark', 'onChange'],
        'ood.UI.Slider': ['value', 'dock', 'dirtyMark', 'onChange'],
        'ood.UI.RadioBox': ['items', 'value', 'readonly', 'disabled', 'dock', 'dirtyMark', 'onItemSelected', 'onChange'],
        'ood.UI.DatePicker': ['value', 'dock', 'readonly', 'disabled', 'dirtyMark', 'onChange'],
        'ood.UI.TimePicker': ['value', 'dock', 'dirtyMark', 'onChange'],
        'ood.UI.ColorPicker': ['value', 'dock', 'dirtyMark', 'onChange'],
        'ood.UI.StatusButtons': ['items', 'value', 'onItemSelected'],
        'ood.UI.FoldingList': ['items', 'activeLast', 'onItemSelected'],
        'ood.UI.Gallery': ['items', 'value', 'iconList', 'dock', 'onItemSelected'],
        'ood.UI.ButtonLayout': ['items', 'value', 'iconList', 'dock', 'onItemSelected'],
        'ood.UI.Opinion': ['items', 'value', 'iconGalleryList', 'dock', 'onItemSelected'],
        'ood.UI.TitleBlock': ['items', 'value', 'iconTitleBlockList', 'dock', 'onItemSelected','onClickMore','onClickTitle'],
        'ood.UI.ContentBlock': ['items', 'value',  'dock', 'onItemSelected','onClickTitle'],

        'ood.UI.PageBar': ['caption', 'value', 'textTpl', 'prevMark', 'nextMark', 'onPageSet'],
        'ood.UI.PopMenu': ['items', 'onMenuSelected', 'onClick'],
        'ood.UI.MenuBar': ['items', 'dock', 'valueSeparator', 'onMenuSelected', 'onMenuBtnClick', 'onClick'],

        'ood.UI.SystemMenu': ['items', 'value', 'dock', 'onItemSelected', 'onChange'],
        'ood.UI.TreeBar': ['items', 'value', 'dock', 'onItemSelected', 'onChange'],

        'ood.UI.ToolBar': ['items', 'dock', 'onClick'],

        'ood.UI.TreeView': ['items', 'value', 'dock', 'onItemSelected', 'onChange'],
        'ood.UI.MTreeView': ['items', 'value', 'dock', 'onItemSelected', 'onChange'],
        'ood.UI.TreeGrid': ['header', 'uidColumn', 'rows', 'value', 'expression', 'dock', 'altRowsBg', 'headerHeight', 'selMode', 'rowNumbered', 'onClickRow', 'onDblclickRow', 'onRowSelected', 'afterRowActive'],
        'ood.UI.MTreeGrid': ['header', 'uidColumn', 'rows', 'value', 'expression', 'dock', 'altRowsBg', 'headerHeight', 'selMode', 'rowNumbered', 'onClickRow', 'onDblclickRow', 'onRowSelected', 'afterRowActive'],

        'ood.UI.Image': ['src', 'items', 'activeItem', 'cursor', 'dock', 'onClick'],
        'ood.UI.Flash': ['src', 'parameters', 'flashvars', 'dock'],
        'ood.UI.FormLayout': ['floatHandler', 'dock', 'solidGridlines', 'stretchH', 'stretchHeight', 'layoutData'],
        'ood.UI.MFormLayout': ['floatHandler', 'dock', 'solidGridlines', 'stretchH', 'stretchHeight', 'layoutData'],
        'ood.UI.Audio': ['src', 'onMediaEvent'],


        'ood.UI.FileUpload': ['params', 'prepareFormData', 'dock', 'uploadUrl', 'uploadfail', 'uploadcomplete'],
        'ood.UI.Video': ['src', 'onMediaEvent'],
        'ood.UI.ECharts': ['chartOption', 'dataset', 'chartRenderer', 'expression', 'chartTheme', 'onMouseEvent', 'onChartEvent'],
        'ood.UI.FusionChartsXT': ['chartType', 'JSONData', 'expression', 'configure', 'onDataClick', 'onLabelClick', 'onAnnotationClick'],
// container
        'ood.UI.Div': ['html', 'dropKeys', 'conLayoutColumns', 'dock', 'overflow', 'dataBinder', 'sandboxTheme'],
        'ood.UI.Block': ['html', 'dropKeys', 'borderType', 'background', 'conLayoutColumns', 'dock', 'overflow', 'dataBinder', 'sandboxTheme'],
        'ood.UI.Group': ['caption', 'html', 'dropKeys', 'conLayoutColumns', 'dock', 'overflow', 'dataBinder', 'sandboxTheme', 'beforeExpand', 'afterFold', 'onIniPanelView'],
        'ood.UI.Panel': ['caption', 'html', 'imageClass', 'dropKeys', 'conLayoutColumns', 'dock', 'overflow', 'dataBinder', 'sandboxTheme', 'beforeExpand', 'afterFold', 'onIniPanelView'],
        'ood.UI.Tabs': ['items', 'value', 'conLayoutColumns', 'dock', 'overflow', 'dataBinder', 'sandboxTheme', 'onItemSelected', 'onIniPanelView'],
        'ood.UI.MTabs': ['items', 'value', 'conLayoutColumns', 'dock', 'overflow', 'dataBinder', 'sandboxTheme', 'onItemSelected', 'onIniPanelView'],
        'ood.UI.Stacks': ['items', 'value', 'conLayoutColumns', 'dock', 'overflow', 'dataBinder', 'sandboxTheme', 'onItemSelected', 'onIniPanelView'],
        'ood.UI.ButtonViews': ['items', 'value', 'conLayoutColumns', 'dock', 'overflow', 'dataBinder', 'sandboxTheme', 'barLocation', 'barHAlign', 'barVAlign', 'barSize', 'sideBarStatus', 'onItemSelected', 'onIniPanelView'],
        'ood.UI.FoldingTabs': ['items', 'value', 'conLayoutColumns', 'dock', 'overflow', 'dataBinder', 'sandboxTheme', 'onItemSelected', 'onIniPanelView'],
        'ood.UI.Layout': ['type', 'items', 'conLayoutColumns', 'dock', 'overflow', 'dataBinder', 'sandboxTheme', 'onGetContent'],

        'ood.UI.Dialog': ['caption', 'modal', 'minBtn', 'imageClass', 'maxBtn', 'dock', 'html', 'overflow', 'sandboxTheme', 'beforeClose'],
        'ood.UI.SVGPaper': ['conLayoutColumns', 'dock', 'dropKeys', 'overflow', 'dataBinder', 'sandboxTheme'],
//svg
        'ood.svg.circle': ['attr', 'animDraw', 'offsetFlow', 'onClick'],
        'ood.svg.ellipse': ['attr', 'animDraw', 'offsetFlow', 'onClick'],
        'ood.svg.rect': ['attr', 'animDraw', 'offsetFlow', 'onClick'],
        'ood.svg.image': ['attr', 'onClick'],
        'ood.svg.text': ['attr', 'onClick'],
        'ood.svg.path': ['attr', 'animDraw', 'offsetFlow', 'onClick'],
        'ood.svg.rectComb': ['attr', 'animDraw', 'offsetFlow', 'hAlign', 'vAlign', 'onClick'],
        'ood.svg.circleComb': ['attr', 'animDraw', 'offsetFlow', 'hAlign', 'vAlign', 'onClick'],
        'ood.svg.ellipseComb': ['attr', 'animDraw', 'offsetFlow', 'hAlign', 'vAlign', 'onClick'],
        'ood.svg.pathComb': ['attr', 'animDraw', 'offsetFlow', 'hAlign', 'vAlign', 'onClick'],
        'ood.svg.imageComb': ['attr', 'hAlign', 'vAlign', 'onClick'],
        'ood.svg.connector': ['attr', 'animDraw', 'offsetFlow', 'hAlign', 'vAlign', 'onClick']
    },
    widgets_prop_cat: [
        {
            id: "Data & Form",
            items: ['value', 'attributes', 'readonly', 'disabled', 'tag', 'tagVar', 'formMethod', 'formTarget', 'formAction', 'formEnctype', 'formDataPath']
        },
        {
            id: "Visibility, Size and Dimensions",
            items: ['visibility', 'position', 'zIndex', 'width', 'height', 'align', 'hAlign', 'vAlign']
        },

        {
            id: 'Caption & Tips',
            items: ['caption', 'fontSize', 'fontWeight', 'fontColor', 'fontFamily', 'imageClass', 'labelCaption', 'labelHAlign']
        },

        {
            id: "Container",
            items: ['sandboxTheme', 'overflow', 'panelBgClr', 'panelBgImg', 'iframeAutoLoad']
        },
        {
            id: "Items",
            items: ['listKey', 'items', 'header', 'rows', 'grpCols', 'rawData', 'tagCmds', 'tagCmdsAlign']
        },
        {
            id: "Other Prop",
            items: ['className', 'selectable', 'defaultFocus']
        }
    ],
    widgets_events_cat: [
        {
            id: "Layout",
            items: ['beforeRender', 'onRender']
        },

        {
            id: "Keyboard",
            items: ['beforeKeypress']
        },

        {
            id: "swipe",
            items: [
                'swipe', 'swipeleft', 'swiperight', 'swipeup', 'swipedown'
            ]
        },
        {
            id: "pan",
            items: [
                'pan', 'panstart','panmove','panend','pancancel','panleft','panright','panup','pandown'
            ]
        },
        {
            id: "pinch",
            items: [
                'pinch', 'pinchstart','pinchmove', 'pinchend', 'pinchcancel', 'pinchin', 'pinchout'
            ]
        },
        {
            id: "press",
            items: [
                'press', 'pressup'
            ]
        },
        {
            id: "rotate",
            items: [
                'rotate','rotatestart', 'rotatemove','rotateend', 'rotatecancel',
            ]
        },


        {
            id: "Mouse & Touch",
            items: ['onClick', 'onDblclick', 'onCmd', 'onContextmenu','touchstart','touchmove','touchend','touchcancel',
                'pan', 'panstart','panmove','panend','pancancel','panleft','panright','panup','pandown',
                'pinch', 'pinchstart','pinchmove', 'pinchend', 'pinchcancel', 'pinchin', 'pinchout',
                'press', 'pressup',
                'rotate','rotatestart', 'rotatemove','rotateend', 'rotatecancel',
                'swipe', 'swipeleft', 'swiperight', 'swipeup', 'swipedown'
            ]
            //

        },
        {

            id: 'Editor',
            items: ['onEditorClick', 'onEndEdit']
        },
        {
            id: "Items",
            items: ['onIniPanelView', 'onGetContent', 'beforeFold', 'afterFold', 'beforeExpand', 'afterExpand']
        },
        {
            id: "OOD Module Events",
            items: ['onReady', 'onFragmentChanged', 'onMessage', 'afterIniComponents', 'afterShow']
        },
        {
            id: "Other Events",
            items: ['onBlur', 'onFocus', 'afterPropertyChanged', 'onShow']
        }
    ],
    lock_hidden_prop: {
        zIndex: 1,
        tabindex: 1,
        position: 1,
        left: 1,
        top: 1,
        bottom: 1,
        right: 1,
        width: 1,
        height: 1,
        rotate: 1,
        CC: 1,
        CS: 1,
        dock: 1,
        dockMargin: 1,
        dockOrder: 1,
        dockMinW: 1,
        dockMinH: 1,
        dockMaxW: 1,
        dockMaxH: 1,
        dockFloat: 1,
        dockIgnore: 1,
        dockIgnoreFlexFill: 1,
        dockStretch: 1,
        conDockPadding: 1,
        conDockSpacing: 1,
        conDockFlexFill: 1,
        conDockStretch: 1,
        conDockRelative: 1,
        conLayoutColumns: 1,
        visibility: 1,
        display: 1,
        overflow: 1
    },

    widgets_itemsProp: {
        items_conf: {
            id: {type: "input"},
            path: {type: "input"},
            target: {type: "input"},
            childname: {type: "childname"},
            caption: {type: "input"},
            title: {type: "input"},
            text: {type: "textarea"},
            url: {type: "input"},
            height: {type: "number"},
            creatorName: {type: "input"},
            createDateStr: {type: "input"},
            capDisplay: {type: "checkbox"},
            add: {type: "input"},
            comment: {type: "input"},
            type: {type: "listbox", editorListItems: ["", "split", "button", "checkbox", "radiobox"]},
            value: {type: "checkbox"},
            handler: {type: "checkbox"},
            size: {type: "spin", precision: 0, increment: 1, min: 0, max: 10000},
            maxlength: {id: "maxlength", type: "spin", precision: 0, increment: 1, min: 0, max: 10000},
            min: {type: "spin", precision: 0, increment: 1, min: 0, max: 10000},
            max: {type: "spin", precision: 0, increment: 1, min: 0, max: 10000},
            locked: {type: "checkbox"},
            folded: {type: "checkbox"},
            cmd: {type: "checkbox"},
            renderer: {type: "input"},
            imageClass: {
                type: "combobox",
                editorCommandBtn: 'pop',
                editorDropListWidth: '18em',
                editorListItems: CONF.designer_list_imageclass,
                tagVar: {
                    prop: 'imageClass'
                }
            },
            flagClass: {
                type: "combobox",
                editorCommandBtn: 'pop',
                editorDropListWidth: '18em',
                editorListItems: CONF.designer_list_imageclass,
                tagVar: {
                    prop: 'imageClass'
                }
            },

            iconFontCode: {type: 'input'},
            fontColor: {type: 'color'},
            iconColor: {type: 'color'},
            itemColor: {type: 'color'},
            iconFontSize: {type: "combobox", editorListItems: CONF.designer_data_fontsize},
            iconStyle: {type: 'input'},
            itemClass: {type: "input"},
            itemStyle: {type: "input"},
            image: {
                type: CONF.getClientMode() == 'singlepage' ? "input" : "popbox",
                event: function (profile, cell, editorprf) {
                    var node = profile.getSubNode('CELL', cell._serialId),
                        _cb = function (path) {
                            editorprf.boxing().setUIValue(CONF.adjustRelativePath(path));
                            node.focus();
                        };
                    var t = profile._targetProfile,
                        k = profile._targetPropKey;
                    if (window.SPA && false === SPA.fe("beforeURISelectorPop", [
                        k + ':item:image', cell.value, _cb,
                        'conf', t && t['ood.absProfile'] ? t.boxing() : t, node.get(0), cell, profile.boxing(), editorprf && editorprf.boxing()
                    ])) return;

                    if (CONF.getClientMode() != 'singlepage') {
                        ood.ModuleFactory.getCom('RAD.ImageSelector', function () {
                            this.setProperties({
                                fromRegion: node.cssRegion(true),
                                onOK: function (obj, path) {
                                    _cb(path);
                                }
                            });
                            this.show();
                        });
                    }
                }
            },
            imagePos: {type: "combobox", editorListItems: CONF.designer_background_repeat},
            imageBgSize: {type: "combobox", editorListItems: CONF.designer_background_size},
            imageRepeat: {type: "combobox", editorListItems: CONF.designer_background_repeat},
            itemMargin: {type: "input"},
            itemWidth: {
                type: "input",
                editorHAlign: 'right',
                cellStyle: "text-align:right",
                editorFormat: CONF.sizeFormat
            },
            itemHeight: {
                type: "input",
                editorHAlign: 'right',
                cellStyle: "text-align:right",
                editorFormat: CONF.sizeFormat
            },
            itemAlign: {type: "listbox", editorListItems: ["", "left", "center", "right"]},
            group: {type: "checkbox"},
            closeBtn: {type: "checkbox"},
            optBtn: {type: "checkbox"},
            popBtn: {type: "checkbox"},
            panelBgClr: {type: "color"},
            panelBgImg: {
                type: CONF.getClientMode() == 'singlepage' ? "input" : "popbox",
                event: function (profile, cell, editorprf) {
                    var node = profile.getSubNode('CELL', cell._serialId),
                        _cb = function (path) {
                            editorprf.boxing().setUIValue(CONF.adjustRelativePath(path));
                            node.focus();
                        };
                    var t = profile._targetProfile,
                        k = profile._targetPropKey;
                    if (window.SPA && false === SPA.fe("beforeURISelectorPop", [
                        k + ':item:panelBgImg', cell.value, _cb,
                        'conf', t && t['ood.absProfile'] ? t.boxing() : t, node.get(0), cell, profile.boxing(), editorprf && editorprf.boxing()
                    ])) return;

                    if (CONF.getClientMode() != 'singlepage') {
                        ood.ModuleFactory.getCom('RAD.ImageSelector', function () {
                            this.setProperties({
                                fromRegion: node.cssRegion(true),
                                onOK: function (obj, path) {
                                    _cb(path);
                                }
                            });
                            this.show();
                        });
                    }
                }
            },
            panelBgImgPos: {type: "combobox", editorListItems: CONF.designer_background_position},
            panelBgImgRepeat: {type: "combobox", editorListItems: CONF.designer_background_repeat},
            panelBgImgAttachment: {type: "combobox", editorListItems: CONF.designer_background_attachment},
            overflow: {type: "combobox", editorListItems: CONF.designer_data_overflow},
            iconFontSize: {type: "combobox", editorListItems: CONF.designer_data_fontsize},
            fontSize: {type: "combobox", editorListItems: CONF.designer_data_fontsize},
            fontWeight: {type: "combobox", editorListItems: CONF.designer_data_fontweight},
            html: {type: "textarea"},
            iframeAutoLoad: {type: "input"},//{url:/*String*/, query:/*String or Oject*/, method/*"get" or "set"*/, enctype:/*String*/},
            ajaxAutoLoad: {type: "input"},//{url:/*String*/, query:/*String or Oject*/,options:/*Obejct*/, refer to ood.Ajax function arguments},
            disabled: {type: "checkbox"},
            readonly: {type: "checkbox"},
            hidden: {type: "checkbox"},
            initFold: {type: "checkbox", dftValue: true},
            showMark: {type: "checkbox"},
            noIcon: {type: "checkbox"},
            tag: {type: "input"},
            tagCmdsAlign: {id: "tagCmdsAlign", type: "combobox", editorListItems: ["left", "right", "floatright"]},
            buttonType: {id: "buttonType", type: "combobox", editorListItems: ["button", "image", "text", "split"]},
            tagVar: tagVar,
            tagCmds: tagCmds
        },

        items: {
            "ood.UI.List": {
                header: ["id", {
                    id: 'type',
                    type: "listbox",
                    editorListItems: ["", "split"]
                }, "caption", "imageClass", "disabled", "hidden", "tagCmds", "tips", "tag", "tagVar"],
                uniqueKey: "id"
            },
            "ood.UI.RadioBox": {
                header: ["id", "caption", "itemClass", "iconStyle", "disabled", "hidden", "tips", "tag", "tagVar"],
                uniqueKey: "id"
            },
            "ood.UI.FoldingList": {
                header: ["id", "caption", 'className', "iconColor","fontColor","itemColor", "title", "text", "imageClass", "disabled", "hidden", "tag", "tagVar", "tagCmds", "tips"],
                uniqueKey: "id"
            },
            "ood.UI.ComboInput": {
                header: ["id", "caption", "imageClass", "iconFontSize", "disabled", "hidden", "tagCmds", "tips", "tag", "tagVar"],
                uniqueKey: "id"
            },
            "ood.UI.StatusButtons": {
                header: ["id", "caption", "iconColor","fontColor","itemColor", "renderer", {
                    id: 'itemType',
                    type: "listbox",
                    editorListItems: ["text", "button", "dropButton"]
                }, "imageClass", "disabled", "hidden", "tag", "tagVar", "tips"],
                uniqueKey: "id"
            },
            "ood.UI.Tabs": {
                header: ["id", "caption",  'euClassName',"iconColor","fontColor","itemColor", "url",'className', "imageClass", "closeBtn", "optBtn", "popBtn", "panelBgClr", "panelBgImg", "overflow", "html", "iframeAutoLoad", "ajaxAutoLoad", "disabled", "hidden", "tag", "tagVar", "tagCmds", "tips"],
                uniqueKey: "id"
            },
            "ood.UI.Stacks": {
                header: ["id", "caption", 'euClassName', "imageClass", "iconFontCode", "closeBtn", "optBtn", "popBtn", "panelBgClr", "panelBgImg", "overflow", "html", 'className',"iframeAutoLoad", "ajaxAutoLoad", "disabled", "hidden", "tag", "tagVar", "tagCmds", "tips"],
                uniqueKey: "id"
            },
            "ood.UI.ButtonViews": {
                header: ["id", "caption", 'euClassName', "iconColor","fontColor","itemColor", "imageClass", 'className',"iconFontCode", "closeBtn", "optBtn", "popBtn", "panelBgClr", "panelBgImg", "overflow", "html", "iframeAutoLoad", "ajaxAutoLoad", "disabled", "hidden", "tag", "tagVar", "tagCmds", "tips"],
                uniqueKey: "id"
            },
            "ood.UI.FoldingTabs": {
                header: ["id", "caption",'euClassName', 'className', "iconColor","fontColor","itemColor", "height", "imageBgSize", "imageRepeat", "imageClass", "iconFontCode", "iconFontSize", "iconStyle", "closeBtn", "optBtn", "popBtn", "panelBgClr", "panelBgImg", "overflow", "html", "iframeAutoLoad", "ajaxAutoLoad", "disabled", "hidden", "tag", "tagVar", "tagCmds", "tips"],
                uniqueKey: "id"
            },
            "ood.UI.Gallery": {
                header: ["id", "caption", "className","iconColor","fontColor","itemColor",  "capDisplay", "renderer", "comment", "itemClass", "itemStyle", "image", "imageClass", "iconFontCode", "iconFontSize", "iconStyle", "disabled", "hidden", "itemWidth", "itemHeight", "itemPadding", "itemMargin", "flagText", "flagClass", "flagStyle", "tag", "tagVar", "tips"],
                uniqueKey: "id"
            },
            "ood.UI.Opinion": {
                header: ["id", "caption", "creatorName", "createDateStr", "capDisplay", "renderer", "comment", "itemClass", "itemStyle", "image", "imageClass", "iconFontCode", "iconFontSize", "iconStyle", "disabled", "hidden", "itemWidth", "itemHeight", "itemPadding", "itemMargin", "flagText", "flagClass", "flagStyle", "tag", "tagVar", "tips"],
                uniqueKey: "id"
            },
            "ood.UI.TitleBlock": {
                header: ["id", "title",  "more", "msgnum","iconColor","fontColor","itemColor",  "moreBgColor", "bgColor", "itemClass", "itemStyle","disabled", "hidden", "itemWidth", "itemHeight", "itemPadding", "itemMargin", "flagText", "flagClass", "flagStyle", "tag", "tagVar", "tips"],
                uniqueKey: "id"
            },
            "ood.UI.ContentBlock": {
                header: ["id", "title",  "more", "datetime","iconColor","fontColor","itemColor",  "moreBgColor", "bgColor", "itemClass", "itemStyle","disabled", "hidden", "itemWidth", "itemHeight", "itemPadding", "itemMargin", "flagText", "flagClass", "flagStyle", "tag", "tagVar", "tips"],
                uniqueKey: "id"
            },
            "ood.UI.ButtonLayout": {
                header: ["id", "caption", "className","iconColor","fontColor","itemColor",  "capDisplay", "renderer", "comment", "itemClass", "itemStyle", "image", "imageClass", "iconFontCode", "iconFontSize", "iconStyle", "disabled", "hidden", "itemWidth", "itemHeight", "itemPadding", "itemMargin", "flagText", "flagClass", "flagStyle", "tag", "tagVar", "tips"],
                uniqueKey: "id"
            },
            "ood.UI.Image": {
                header: ["id", "image", "alt", "tips"],
                uniqueKey: "id"
            },
            "ood.UI.TreeBar": {
                header: ["id", {
                    id: 'type',
                    type: "listbox",
                    editorListItems: ["", "split"]
                }, "caption", 'entityClass',"bindClass","imageClass", "iconColor","fontColor","itemColor",'className',  "group", "disabled", "hidden", "tag", "tagVar", "initFold", "tips"],
                uniqueKey: "id",
                hasSub: true
            },

            "ood.UI.TreeView": {
                header: ["id", {
                    id: 'type',
                    type: "listbox",
                    editorListItems: ["", "split"]
                }, "caption", 'entityClass',"bindClass","imageClass", "group", "iconColor","fontColor","itemColor", "className", "disabled", "hidden", "tag", "tagVar", "tagCmds", "initFold", "tips"],
                uniqueKey: "id",
                hasSub: true
            },
            "ood.UI.MTreeView": {
                header: ["id", {
                    id: 'type',
                    type: "listbox",
                    editorListItems: ["", "split"]
                }, "caption", "className", "imageClass", "group", "iconColor","fontColor","itemColor", "disabled", "hidden", "tag", "tagVar", "tagCmds", "initFold", "tips"],
                uniqueKey: "id",
                hasSub: true
            },
            "ood.UI.PopMenu": {
                header: ["id", "caption", 'className', "group", "type", "value", "iconColor","fontColor","itemColor", "add", "imageClass", "disabled", "hidden", "tag", "tagVar", "tips"],
                uniqueKey: "id",
                hasSub: 1
            },
            "ood.UI.MenuBar": {
                header: ["id", "caption", 'className', "group", "type","iconColor","fontColor","itemColor", "value", "add", "imageClass", "iconFontCode", "disabled", "hidden", "tag", "tagVar", "tips"],
                uniqueKey: "id",
                hasSub: 1
            },
            "ood.UI.ToolBar": {
                header: ["id", "hidden","iconColor","fontColor","itemColor",  "handler"],
                subheader: ["id", "caption", "label", {
                    id: 'type',
                    type: "listbox",
                    editorListItems: ["", "button", "dropButton", "statusButton", "profile", "split"]
                }, "value", "renderer", "imageClass", "iconFontCode", "disabled", "hidden", "tag", "tagVar", "tips"],
                uniqueKey: "id",
                subFixedRows: false,
                innerKey: 'sub'
            },
            "ood.UI.Layout": {
                header: ["id", "size", "min", "max", {id:"pos",  type: "listbox", editorListItems: ["before", "main", "after"]},"locked", "folded", "hidden", "cmd", "panelBgClr", "panelBgImg", "panelBgImgPos", "overflow", "html", "itemClass", "itemStyle", "tag"],
                rows: {
//                        'before':[{type:'label'},{type:'label'}],
                    'main': {
                        tag: 'candel',
                        cells: [{type: 'label'}, {}, {}, {type: 'label', value: "-"}, {
                            type: 'label',
                            value: "-"
                        }, {type: 'label', value: "-"}, {type: 'label', value: "-"}, {type: 'label', value: "-"}]
                    }
//                        'after':[{type:'label'},{type:'label'}]
                }
            }
        },
        header: {
            "ood.UI.TreeGrid": {
                header: [
                    {id: "id", type: 'input'},
                    {id: "caption", type: 'input'},
                    {
                        id: 'type',
                        type: "listbox",
                        editorListItems: ',label,input,textarea,combobox,listbox,file,getter,helpinput,button,dropbutton,cmdbox,popbox,date,time,datetime,color,spin,counter,currency,number,checkbox,progress,cmd'.split(',')
                    },
                    {
                        id: "formula", type: 'input', editorEvents: {
                            _onChange: function (p, ov, v) {
                                if (false === ood.ExcelFormula.validate(v)) {
                                    ood.alert("It's not a valid formula");
                                }
                            }
                        }
                    },
                    {id: "defaultValue", type: "input"},
                    {id: "flexSize", type: "checkbox"},
                    {
                        id: "width",
                        type: "input",
                        editorHAlign: 'right',
                        cellStyle: "text-align:right",
                        editorFormat: CONF.sizeFormat
                    },
                    {
                        id: "minWidth",
                        type: "input",
                        editorHAlign: 'right',
                        cellStyle: "text-align:right",
                        editorFormat: CONF.sizeFormat
                    },
                    {
                        id: "maxWidth",
                        type: "input",
                        editorHAlign: 'right',
                        cellStyle: "text-align:right",
                        editorFormat: CONF.sizeFormat0
                    },
                    {id: "colResizer", type: "checkbox"},
                    {id: "headerStyle", type: 'input'},
                    {id: "headerClass", type: 'input'},
                    {id: "colRenderer", type: "input"},
                    {id: "hidden", type: 'checkbox'},
//sortby : Function(x,y){return -1:0:1;}, custom sortby function
                    {id: "cellRenderer", type: "input"},
                    {id: "cellCapTpl", type: 'input'},
                    {id: 'cellClass', type: 'input'},
                    {id: 'cellStyle', type: 'input'},
                    {id: "disabled", type: "checkbox"},
                    {id: "readonly", type: "checkbox"},
                    {id: "editable", type: "checkbox"},
                    {id: "editMode", type: "listbox", editorListItems: ["focus", "sharp", "hover", "inline"]},
                    {
                        id: "editorListItems",
                        cellRenderer: function () {
                            return ood.adjustRes("[ $RAD.designer.conf.Object ]")
                        },
                        type: "button",
                        event: function (profile, cell, editorprf) {
                            var ns = profile.host,
                                node = profile.getSubNode('CELL', cell._serialId),
                                obj = cell.obj || [];
                            RAD.Designer.prototype._showMixedEditor(profile._targetProfile, "ood.UI.List", "header." + cell._col.id, "items", obj, node, function (items) {
                                cell.obj = ood.clone(items, true);
                                cell.needCollect = 1;
                                ns._dirty = 1;
                                node.focus();
                            }, true);
                        }
                    },
                    {
                        id: "editorDropListWidth",
                        type: "input",
                        editorHAlign: 'right',
                        cellStyle: "text-align:right",
                        editorFormat: CONF.sizeFormat
                    },
                    {
                        id: "editorDropListHeight",
                        type: "input",
                        editorHAlign: 'right',
                        cellStyle: "text-align:right",
                        editorFormat: CONF.sizeFormat
                    },
                    {id: "precision", type: "spin", precision: 0, increment: 1, min: 0, max: 100},
                    {id: "increment", type: "number", precision: 2, increment: 0.01},
                    {id: "min", type: "number", precision: 2, increment: 0.01},
                    {id: "max", type: "number", precision: 2, increment: 0.01},
                    {id: "maxlength", type: "spin", precision: 0, increment: 1, min: 0, max: 10000},
                    {id: "groupingSeparator", type: "input", maxlength: 1},
                    {id: "decimalSeparator", type: "input", maxlength: 1},
                    {id: "forceFillZero", type: "checkbox"},
                    {id: "numberTpl", type: "input"},
                    {id: "currencyTpl", type: "input"},
                    {id: "dateEditorTpl", type: "input"},
                    {id: "tips", type: "input"},
                    {id: "tag", type: "input"},
                    tagVar
                ],
                uniqueKey: "id"
            }
        },
        grpCols: {
            "ood.UI.TreeGrid": {
                header: [
                    {id: "id", type: 'input'},
                    {id: "caption", type: 'input'},
                    {id: "from", type: "spin", precision: 0, increment: 1, min: 0},
                    {id: "to", type: "spin", precision: 0, increment: 1, min: 0},
                    {id: "colResizer", type: "checkbox"},
                    {id: "headerStyle", type: 'input'},
                    {id: "headerClass", type: 'input'},
                    {id: "tips", type: "input"},
                    {id: "tag", type: "input"},
                    tagVar
                ],
                uniqueKey: "id"
            }
        },
        rows: {
            "ood.UI.TreeGrid": {
                header: [

                    //cells : Array, row's cells data
                    //sub : Array or [true], sub rows data

                    {id: "id", type: 'input'},
                    {id: "caption", type: 'input'},
                    {
                        id: 'type',
                        type: "listbox",
                        editorListItems: ',label,input,textarea,combobox,listbox,file,getter,helpinput,button,dropbutton,cmdbox,popbox,date,time,datetime,color,spin,counter,currency,number,checkbox,progress,cmd'.split(',')
                    },
                    {
                        id: "formula", type: 'input', editorEvents: {
                            _onChange: function (p, ov, v) {
                                if (false === ood.ExcelFormula.validate(v)) {
                                    ood.alert("It's not a valid formula");
                                }
                            }
                        }
                    },
                    {
                        id: "height",
                        type: "input",
                        editorHAlign: 'right',
                        cellStyle: "text-align:right",
                        editorFormat: CONF.sizeFormat
                    },
                    {id: "rowStyle", type: 'input'},
                    {id: "rowClass", type: 'input'},
                    {id: "firstCellStyle", type: 'input'},
                    {id: "firstCellClass", type: 'input'},
                    {id: "group", type: "checkbox"},
                    {id: "hidden", type: 'checkbox'},
                    {id: "rowResizer", type: "checkbox"},
                    {id: "rowRenderer", type: "input"},
//sortby : Function(x,y){return -1:0:1;}, custom sortby function
                    {id: "cellRenderer", type: "input"},
                    {id: "cellCapTpl", type: 'input'},
                    {id: 'cellClass', type: 'input'},
                    {id: 'cellStyle', type: 'input'},
                    {id: "disabled", type: "checkbox"},
                    {id: "readonly", type: "checkbox"},
                    {id: "editable", type: "checkbox"},

                    {id: "editMode", type: "listbox", editorListItems: ["focus", "sharp", "hover", "inline"]},
                    {
                        id: "editorListItems",
                        cellRenderer: function () {
                            return ood.adjustRes("[ $RAD.designer.conf.Object ]")
                        },
                        type: "button",
                        event: function (profile, cell, editorprf) {
                            var ns = profile.host,
                                node = profile.getSubNode('CELL', cell._serialId),
                                obj = cell.obj || [];
                            RAD.Designer.prototype._showMixedEditor(profile._targetProfile, "ood.UI.List", "rows." + cell._col.id, "items", obj, node, function (items) {
                                cell.obj = ood.clone(items, true);
                                cell.needCollect = 1;
                                ns._dirty = 1;
                                node.focus();
                            }, true);
                        }
                    },
                    {
                        id: "editorDropListWidth",
                        type: "input",
                        editorHAlign: 'right',
                        cellStyle: "text-align:right",
                        editorFormat: CONF.sizeFormat
                    },
                    {
                        id: "editorDropListHeight",
                        type: "input",
                        editorHAlign: 'right',
                        cellStyle: "text-align:right",
                        editorFormat: CONF.sizeFormat
                    },
                    {id: "precision", type: "spin", precision: 0, increment: 1, min: 0, max: 100},
                    {id: "increment", type: "number"},
                    {id: "min", type: "number"},
                    {id: "max", type: "number"},
                    {id: "maxlength", type: "spin", precision: 0, increment: 1, min: 0, max: 10000},
                    {id: "groupingSeparator", type: "input", maxlength: 1},
                    {id: "decimalSeparator", type: "input", maxlength: 1},
                    {id: "forceFillZero", type: "checkbox"},
                    {id: "numberTpl", type: "input"},
                    {id: "currencyTpl", type: "input"},
                    {id: "dateEditorTpl", type: "input"},
                    {id: "initFold", type: "checkbox"},
                    {id: "tips", type: "input"},
                    {id: "tag", type: "input"},
                    tagVar,
                    tagCmds
                ],
                subheader: [
//                        {id:"id",type:'input'},
                    {id: "value", type: 'input'},
                    {id: "caption", type: 'input'},
                    {id: "cellRenderer", type: "input"},
                    {id: "cellCapTpl", type: 'input'},
                    {
                        id: 'type',
                        type: "listbox",
                        editorListItems: ',label,input,textarea,combobox,listbox,file,getter,helpinput,button,dropbutton,cmdbox,popbox,date,time,datetime,color,spin,counter,currency,number,checkbox,progress,cmd'.split(',')
                    },
                    {
                        id: "formula", type: 'input', editorEvents: {
                            _onChange: function (p, ov, v) {
                                if (false === ood.ExcelFormula.validate(v)) {
                                    ood.alert("It's not a valid formula");
                                }
                            }
                        }
                    },
                    {id: "tag", type: 'input'},
                    tagVar,
                    {id: 'cellClass', type: 'input'},
                    {id: 'cellStyle', type: 'input'},
                    {id: "disabled", type: "checkbox"},
                    {id: "readonly", type: "checkbox"},
                    {id: "editable", type: "checkbox"},
                    {id: "editMode", type: "listbox", editorListItems: ["focus", "sharp", "hover", "inline"]},
                    {
                        id: "editorListItems",
                        cellRenderer: function () {
                            return ood.adjustRes("[ $RAD.designer.conf.Object ]")
                        },
                        type: "button",
                        event: function (profile, cell, editorprf) {
                            var ns = profile.host,
                                node = profile.getSubNode('CELL', cell._serialId),
                                obj = cell.obj || [];
                            RAD.Designer.prototype._showMixedEditor(profile._targetProfile, "ood.UI.List", "rows.cell." + cell._col.id, "items", obj, node, function (items) {
                                cell.obj = ood.clone(items, true);
                                cell.needCollect = 1;
                                ns._dirty = 1;
                                node.focus();
                            }, true);
                        }
                    },
                    {
                        id: "editorDropListWidth",
                        type: "input",
                        editorHAlign: 'right',
                        cellStyle: "text-align:right",
                        editorFormat: CONF.sizeFormat
                    },
                    {
                        id: "editorDropListHeight",
                        type: "input",
                        editorHAlign: 'right',
                        cellStyle: "text-align:right",
                        editorFormat: CONF.sizeFormat
                    },
                    {id: "precision", type: "spin", precision: 0, increment: 1, min: 0, max: 100},
                    {id: "increment", type: "number"},
                    {id: "min", type: "number"},
                    {id: "max", type: "number"},
                    {id: "maxlength", type: "spin", precision: 0, increment: 1, min: 0, max: 10000},
                    {id: "groupingSeparator", type: "input", maxlength: 1},
                    {id: "decimalSeparator", type: "input", maxlength: 1},
                    {id: "forceFillZero", type: "checkbox"},
                    {id: "numberTpl", type: "input"},
                    {id: "currencyTpl", type: "input"},
                    {id: "dateEditorTpl", type: "input"},
                    {id: "tips", type: "input"},
                    tagCmds
                ],
                // dont set it , id will be auto set
                uniqueKey: null,
                hasSub: 1,
                innerKey: 'cells',
                subrowfixed: 1
            }
        },
        tagCmds: {
            "all": {
                header: [
                    {id: "id", type: "input"},
                    {id: "tagCmdsAlign", type: "combobox", editorListItems: ["left", "right", "floatright"]},
                    {id: "buttonType", type: "combobox", editorListItems: ["button", "image", "text", "split"]},
                    {id: "pos", type: "combobox", editorListItems: ["", "row", "header"]},
                    {id: "caption", type: "input"},
                    {
                        id: "itemClass",
                        type: "combobox",
                        editorCommandBtn: 'pop',
                        editorDropListWidth: '18em',
                        editorListItems: CONF.designer_list_imageclass2,
                        tagVar: {
                            prop: 'imageClass'
                        }
                    },
                    {id: "itemStyle", type: "input"},
                    {
                        id: "image",
                        type: CONF.getClientMode() == 'singlepage' ? "input" : "popbox",
                        event: function (profile, cell, editorprf) {
                            var node = profile.getSubNode('CELL', cell._serialId),
                                _cb = function (path) {
                                    editorprf.boxing().setUIValue(CONF.adjustRelativePath(path));
                                    node.focus();
                                };
                            var t = profile._targetProfile,
                                k = profile._targetPropKey;
                            if (window.SPA && false === SPA.fe("beforeURISelectorPop", [
                                k + ':item:image', cell.value, _cb,
                                'conf', t && t['ood.absProfile'] ? t.boxing() : t, node.get(0), cell, profile.boxing(), editorprf && editorprf.boxing()
                            ])) return;

                            if (CONF.getClientMode() != 'singlepage') {
                                ood.ModuleFactory.getCom('RAD.ImageSelector', function () {
                                    this.setProperties({
                                        fromRegion: node.cssRegion(true),
                                        onOK: function (obj, path) {
                                            _cb();
                                        }
                                    });
                                    this.show();
                                });
                            }
                        }
                    },
                    {id: "tips", type: "input"},
                    tagVar
                ]
            }
        }
    },
    widgets_mapProp: {
        dockMargin: {
            all: {
                cells: [
                    {id: "left", type: "spin", precision: 0, increment: 1},
                    {id: "top", type: "spin", precision: 0, increment: 1},
                    {id: "right", type: "spin", precision: 0, increment: 1},
                    {id: "bottom", type: "spin", precision: 0, increment: 1}
                ]
            }
        },
        conDockPadding: {
            all: {
                cells: [
                    {id: "left", type: "spin", precision: 0, increment: 1},
                    {id: "top", type: "spin", precision: 0, increment: 1},
                    {id: "right", type: "spin", precision: 0, increment: 1},
                    {id: "bottom", type: "spin", precision: 0, increment: 1}
                ]
            }
        },
        conDockSpacing: {
            all: {
                cells: [
                    {id: "width", type: "spin", precision: 0, increment: 1},
                    {id: "height", type: "spin", precision: 0, increment: 1}
                ]
            }
        },
        rowOptions: {
            "ood.UI.TreeGrid": {
                cells: [
                    {
                        id: 'type',
                        type: "listbox",
                        editorListItems: ',label,input,textarea,combobox,listbox,file,getter,helpinput,button,dropbutton,cmdbox,popbox,date,time,datetime,color,spin,counter,currency,number,checkbox,progress,cmd'.split(',')
                    },
                    {
                        id: "formula", type: 'input', editorEvents: {
                            _onChange: function (p, ov, v) {
                                if (false === ood.ExcelFormula.validate(v)) {
                                    ood.alert("It's not a valid formula");
                                }
                            }
                        }
                    },
                    {id: "rowRenderer", type: "input"},
                    {id: "cellRenderer", type: "input"},
                    {id: 'cellClass', type: 'input'},
                    {id: 'cellStyle', type: 'input'},
                    {id: "disabled", type: "checkbox"},
                    {id: "readonly", type: "checkbox"},
                    {id: "editable", type: "checkbox"},
                    {id: "editMode", type: "listbox", editorListItems: ["focus", "sharp", "hover", "inline"]},
                    {id: "precision", type: "spin", precision: 0, increment: 1, min: 0, max: 100},
                    {id: "increment", type: "number"},
                    {id: "min", type: "number"},
                    {id: "max", type: "number"},
                    {id: "maxlength", type: "spin", precision: 0, increment: 1, min: 0, max: 10000},
                    {id: "groupingSeparator", type: "input", maxlength: 1},
                    {id: "decimalSeparator", type: "input", maxlength: 1},
                    {id: "forceFillZero", type: "checkbox"},
                    {id: "numberTpl", type: "input"},
                    {id: "currencyTpl", type: "input"},
                    {id: "dateEditorTpl", type: "input"}
                ]
            }
        },
        colOptions: {
            "ood.UI.TreeGrid": {
                cells: [
                    {
                        id: 'type',
                        type: "listbox",
                        editorListItems: ',label,input,textarea,combobox,listbox,file,getter,helpinput,button,dropbutton,cmdbox,popbox,date,time,datetime,color,spin,counter,currency,number,checkbox,progress,cmd'.split(',')
                    },
                    {
                        id: "formula", type: 'input', editorEvents: {
                            _onChange: function (p, ov, v) {
                                if (false === ood.ExcelFormula.validate(v)) {
                                    ood.alert("It's not a valid formula");
                                }
                            }
                        }
                    },
                    {id: "colRenderer", type: "input"},
                    {id: "cellRenderer", type: "input"},
                    {id: 'cellClass', type: 'input'},
                    {id: 'cellStyle', type: 'input'},
                    {id: "disabled", type: "checkbox"},
                    {id: "readonly", type: "checkbox"},
                    {id: "editable", type: "checkbox"},
                    {id: "editMode", type: "listbox", editorListItems: ["focus", "sharp", "hover", "inline"]},
                    {id: "precision", type: "spin", precision: 0, increment: 1, min: 0, max: 100},
                    {id: "increment", type: "number"},
                    {id: "min", type: "number"},
                    {id: "max", type: "number"},
                    {id: "maxlength", type: "spin", precision: 0, increment: 1, min: 0, max: 10000},
                    {id: "groupingSeparator", type: "input", maxlength: 1},
                    {id: "decimalSeparator", type: "input", maxlength: 1},
                    {id: "forceFillZero", type: "checkbox"},
                    {id: "numberTpl", type: "input"},
                    {id: "currencyTpl", type: "input"},
                    {id: "dateEditorTpl", type: "input"}
                ]
            }
        }
    },
    widgets_attrProp: {
        dockMargin: {
            all: {
                header: ["key", "value"],
                rows: [
                    [{value: 'left'}],
                    [{value: 'top'}],
                    [{value: 'right'}],
                    [{value: 'bottom'}]
                ],
                fix: 1
            }
        }
    },
    widgets_customCellOptions: {
        'ood.UI.Input': {
            maxlength: {
                precision: 0
            }
        },
        'ood.UI.ComboInput': {
            maxlength: {
                precision: 0
            },
            min: {
                precision: 2
            },
            max: {
                precision: 2
            },
            increment: {
                precision: 2
            }
        },
        'ood.UI.ColorPicker': {
            value: {
                type: 'color'
            }
        },
        'ood.UI.Audio': {
            volume: {type: "spin", precision: 2, min: 0, max: 1}
        },
        'ood.UI.Video': {
            volume: {type: "spin", precision: 2, min: 0, max: 1}
        }
    },
    ModuleFactoryProfile: {
        about: {
            cls: 'RAD.About'
        },
        delFile: {
            cls: 'RAD.DelFile'
        },
        prjPro: {
            cls: 'RAD.ProjectPro'
        },
        prjSel: {
            cls: 'RAD.ProjectSelector'
        },
        prjVersionSel: {
            cls: 'RAD.ProjectVersionSelector'
        },
        funEditor: {
            cls: 'RAD.FunEditor'
        },
        jsonEditor: {
            cls: 'RAD.JSONEditor'
        },
        HTMLEditor: {
            cls: 'RAD.HTMLEditor'
        },
        FileSelector: {
            cls: 'RAD.FileSelector'
        },
        MobileInstruction: {
            cls: 'RAD.MobileInstruction'
        },
        RegisterForm: {
            cls: 'RAD.RegisterForm'
        }
    },
    inplaceedit: {
        "ood.UI.Link": {
            KEY: [0, 'getCaption', 'setCaption']
        },
        "ood.UI.Label": {
            KEY: [0, 'getCaption', 'setCaption']
        },
        "ood.UI.CheckBox": {
            CAPTION: [0, 'getCaption', 'setCaption']
        },
        "ood.UI.Span": {
            KEY: [0, 'getHtml', 'setHtml']
        },
        /*"ood.UI.Div":{
            KEY:[0,'getHtml','setHtml']
        },*/
        "ood.UI.HTMLButton": {
            KEY: [0, 'getCaption', 'setCaption']
        },
        "ood.UI.Button": {
            KEY: [0, 'getCaption', 'setCaption']
        },
        "ood.UI.Group": {
            CAPTION: [0, 'getCaption', 'setCaption']
        },
        "ood.UI.Input": {
            INPUT: [0, 'getValue', 'setValue'],
            LABEL: [0, 'getLabelCaption', 'setLabelCaption']
        },
        "ood.UI.ComboInput": {
            INPUT: [0, 'getValue', 'setValue'],
            LABEL: [0, 'getLabelCaption', 'setLabelCaption']
        },
        "ood.UI.RichEditor": {
            LABEL: [0, 'getLabelCaption', 'setLabelCaption']
        },
        "ood.UI.Slider": {
            LABEL: [0, 'getLabelCaption', 'setLabelCaption']
        },
        "ood.UI.PageBar": {
            LABEL: [0, 'getCaption', 'setCaption']
        },
        "ood.UI.Dialog": {
            CAPTION: [0, 'getCaption', 'setCaption']
        },
        "ood.UI.Panel": {
            CAPTION: [0, 'getCaption', 'setCaption']
        },
        "ood.UI.Slider": {
            LABEL: [0, 'getLabelCaption', 'setLabelCaption']
        },

        "ood.UI.List": {
            ITEM: [1, 'getItemByDom', 'updateItem'],
            LABEL: [0, 'getLabelCaption', 'setLabelCaption']
        },
        "ood.UI.RadioBox": {
            CAPTION: [1, 'getItemByDom', 'updateItem'],
            LABEL: [0, 'getLabelCaption', 'setLabelCaption']
        },
        "ood.UI.StatusButtons": {
            CAPTION: [1, 'getItemByDom', 'updateItem'],
            LABEL: [0, 'getLabelCaption', 'setLabelCaption']
        },
        "ood.UI.Gallery": {
            CAPTION: [1, 'getItemByDom', 'updateItem'],
            COMMENT: [1, 'getItemByDom', 'updateItem', 'comment']
        },
        "ood.UI.ButtonLayout": {
            CAPTION: [1, 'getItemByDom', 'updateItem'],
            COMMENT: [1, 'getItemByDom', 'updateItem', 'comment']
        },
        "ood.UI.Tabs": {
            CAPTION: [1, 'getItemByDom', 'updateItem']
        },
        "ood.UI.Stacks": {
            CAPTION: [1, 'getItemByDom', function (prf, item, itemKey, nv) {
                prf.boxing().updateItem(item.id, {caption: nv});
                prf.boxing().reLayout(true);
            }]
        },
        "ood.UI.ButtonViews": {
            CAPTION: [1, 'getItemByDom', 'updateItem']
        },
        "ood.UI.MenuBar": {
            ITEM: [1, 'getItemByDom', 'updateItem']
        },
        "ood.UI.TreeBar": {
            ITEMCAPTION: [1, 'getItemByDom', 'updateItem']
        },
        "ood.UI.TreeView": {
            ITEMCAPTION: [1, 'getItemByDom', 'updateItem']
        },
        "ood.UI.MTreeView": {
            ITEMCAPTION: [1, 'getItemByDom', 'updateItem']
        },
        "ood.UI.ToolBar": {
            LABEL: [1, function (prf, node) {
                return prf.SubSerialIdMapItem[prf.getSubId(node.id)];
            }, 'updateItem', 'label'],
            BTN: [1, function (prf, node) {
                return prf.SubSerialIdMapItem[prf.getSubId(node.id)];
            }, 'updateItem']
        },
        "ood.UI.TreeGrid": {
            CELLA: [1, function (prf, node) {
                var subId = prf.getSubId(node.id), o, item = {};
                if (subId.charAt(0) == '-' && subId.charAt(1) == 'r') {
                    o = prf.rowMap[subId];
                    item.id = o.id;
                    item.caption = o.caption;
                    item.isRow = true;
                } else {
                    o = prf.cellMap[subId];
                    item.id = o.id;
                    item.caption = o.value;
                }
                return item;
            }, function (prf, item, itemKey, nv) {
                if (item.isRow)
                    prf.boxing().updateRow(item.id, {caption: nv});
                else
                    prf.boxing().updateCell(item.id, {value: nv});
            }],
            HCELLA: [1, function (prf, node) {
                var subId = prf.getSubId(node.id);
                return subId ? prf.colMap[subId] : {
                    id: ' X ',
                    caption: prf.boxing().getGridHandlerCaption()
                };
            }, function (prf, item, itemKey, nv) {
                if (item.id != ' X ')
                    prf.boxing().updateHeader(item.id, {caption: nv});
                else
                    prf.boxing().setGridHandlerCaption(nv);
            }]
        },
        "ood.UI.FormLayout": {
            ITEM: [1, null, null, null, null, null, function (page, prf, node) {
                if (prf.$htable) {
                    var cellMeta = prf.getItemByDom(node.id());
                    prf.$htable.selectCell(cellMeta.row, cellMeta.col);
                }
            }]
        },
        "ood.UI.FoldingTabs": {
            CAPTION: [1, 'getItemByDom', 'updateItem'],
            MESSAGE: [1, 'getItemByDom', 'updateItem', 'message']
        },
        "ood.svg.text": {
            KEY: [0, function (prf) {
                return prf.properties.attr.text;
            }, function (prf, nv) {
                prf.boxing().setAttr({text: nv}, null, false);
            }, 'KEY', true]
        },
        "ood.svg.rectComb": {
            TEXT: [0, function (prf) {
                return ood.get(prf.properties.attr, ["TEXT", "text"]);
            }, function (prf, nv) {
                prf.boxing().setAttr("TEXT", {text: nv}, false);
            }, 'KEY', true]
        },
        "ood.svg.circleComb": {
            TEXT: [0, function (prf) {
                return ood.get(prf.properties.attr, ["TEXT", "text"]);
            }, function (prf, nv) {
                prf.boxing().setAttr("TEXT", {text: nv}, false);
            }, 'KEY', true]
        },
        "ood.svg.ellipseComb": {
            TEXT: [0, function (prf) {
                return ood.get(prf.properties.attr, ["TEXT", "text"]);
            }, function (prf, nv) {
                prf.boxing().setAttr("TEXT", {text: nv}, false);
            }, 'KEY', true]
        },
        "ood.svg.pathComb": {
            TEXT: [0, function (prf) {
                return ood.get(prf.properties.attr, ["TEXT", "text"]);
            }, function (prf, nv) {
                prf.boxing().setAttr("TEXT", {text: nv}, false);
            }, 'KEY', true]
        },
        "ood.svg.imageComb": {
            TEXT: [0, function (prf) {
                return ood.get(prf.properties.attr, ["TEXT", "text"]);
            }, function (prf, nv) {
                prf.boxing().setAttr("TEXT", {text: nv}, false);
            }, 'KEY', true]
        },
        "ood.svg.connector": {
            TEXT: [0, function (prf) {
                return ood.get(prf.properties.attr, ["TEXT", "text"]);
            }, function (prf, nv) {
                prf.boxing().setAttr("TEXT", {text: nv}, false);
            }, 'KEY', true]
        }
    }
};
