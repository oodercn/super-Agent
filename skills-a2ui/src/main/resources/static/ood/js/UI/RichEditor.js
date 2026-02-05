ood.Class("ood.UI.RichEditor", ["ood.UI", "ood.absValue"], {
    Initialize: function () {
        this.addTemplateKeys(['TOOLBARBTN']);
    },
    Instance: {
        iniProp: {
            labelSize: '2em', height: '26em', labelPos: 'top', caption: '$RAD.widgets.richText', labelHAlign: 'left'
        },
        activate: function () {
            this.getEditorWin().focus();
            return this;
        },
        getEditorWin: function () {
            return this.get(0).$win;
        },
        getEditorDoc: function () {
            return this.get(0).$doc;
        },
        getEditorBody: function () {
            var doc = this.get(0).$doc;
            return doc && (doc.body || doc.documentElement);
        },
        _setCtrlValue: function (value) {
            if (!ood.isSet(value)) value = '';
            return this.each(function (profile) {
                var sp = window['/'];
                if (sp && sp.indexOf(':/') != -1)
                    value = value.replace(/{\/}/g, sp);
                var html = profile.$useOriginalText ? value : ood.adjustRes(value, 0, 1);
                if (!profile.$inDesign) {
                    var doc = profile.$doc, body = doc && (doc.body || doc.documentElement);
                    if (body) {
                        body.innerHTML = html;
                        return;
                    }
                }
                profile.getSubNode("EDITOR").html(html);
            });
        },
        _getCtrlValue: function () {
            var profile = this.get(0);
            if (!profile.$inDesign) {
                var doc = profile.$doc,
                    body = doc && (doc.body || doc.documentElement);
                if (body) {
                    var v = body.innerHTML;
                    if (this.getTextType() == 'text') {
                        v = body.innerText;
                    }
                    ;
                    sp = window['/'];
                    if (sp && sp.indexOf(':/') != -1)
                        v = v.replace(new RegExp(sp, 'g'), '{/}');
                    return v;
                } else
                    return '';
            } else {
                return profile.getSubNode("EDITOR").text();
            }
        },
        
        // 设置主题
        setTheme: function(theme) {
            return this.each(function(profile) {
                profile.properties.theme = theme;
                var root = profile.getRoot(),
                    toolbar = profile.getSubNode('TOOLBAR'),
                    editor = profile.getSubNode('EDITOR'),
                    toolbarBtns = profile.getSubNode('TOOLBARBTN', true);

                if (theme === 'dark') {
                    // 暗黑模式样式
                    root.addClass('richeditor-dark');
                    
                    // 工具栏暗黑样式
                    if (toolbar && !toolbar.isEmpty()) {
                        toolbar.css({
                            'background': 'var(--ood-dark-gradient)',
                            'border-color': 'var(--ood-dark-border)',
                            'color': 'var(--ood-dark-text)'
                        });
                    }
                    
                    // 编辑器区域暗黑样式
                    editor.css({
                        'background-color': 'var(--ood-dark-bg)',
                        'border-color': 'var(--ood-dark-border)',
                        'color': 'var(--ood-dark-text)'
                    });
                    
                    // 工具栏按钮暗黑样式
                    if (toolbarBtns && toolbarBtns.length > 0) {
                        toolbarBtns.css({
                            'background-color': 'var(--ood-dark-bg-secondary)',
                            'border-color': 'var(--ood-dark-border)',
                            'color': 'var(--ood-dark-text)'
                        });
                    }
                    
                    // 如果有iframe编辑器，为其内容设置暗黑模式
                    var editorDoc = profile.$doc;
                    if (editorDoc && editorDoc.body) {
                        ood(editorDoc.body).css({
                            'background-color': 'var(--ood-dark-bg)',
                            'color': 'var(--ood-dark-text)',
                            'font-family': '"Segoe UI", Tahoma, Arial, sans-serif'
                        });
                    }
                } else {
                    // 浅色模式样式
                    root.removeClass('richeditor-dark');
                    
                    // 恢复默认样式
                    if (toolbar && !toolbar.isEmpty()) {
                        toolbar.css({
                            'background': '',
                            'border-color': '',
                            'color': ''
                        });
                    }
                    
                    editor.css({
                        'background-color': '',
                        'border-color': '',
                        'color': ''
                    });
                    
                    if (toolbarBtns && toolbarBtns.length > 0) {
                        toolbarBtns.css({
                            'background-color': '',
                            'border-color': '',
                            'color': ''
                        });
                    }
                    
                    // 恢复iframe编辑器内容样式
                    var editorDoc = profile.$doc;
                    if (editorDoc && editorDoc.body) {
                        ood(editorDoc.body).css({
                            'background-color': '',
                            'color': '',
                            'font-family': ''
                        });
                    }
                }
                
                // 保存主题设置
                localStorage.setItem('richeditor-theme', theme);
            });
        },


        RichEditorTrigger: function() {
            var profile = this.get(0);
            var prop = profile.properties;
            var boxing = this;

            // 初始化主题
            if (prop.theme) {
                boxing.setTheme(prop.theme);
            } else {
                // 从本地存储恢复主题
                var savedTheme = localStorage.getItem('richeditor-theme');
                if (savedTheme) {
                    boxing.setTheme(savedTheme);
                }
            }

            // 初始化响应式设计
            if (prop.responsive !== false) {
                boxing.adjustLayout();
            }

            // 初始化可访问性
            boxing.enhanceAccessibility();
        },

        // 获取当前主题
        getTheme: function() {
            var profile = this.get(0);
            return profile.properties.theme || localStorage.getItem('richeditor-theme') || 'light';
        },
        
        // 切换暗黑模式
        toggleDarkMode: function() {
            var currentTheme = this.getTheme();
            this.setTheme(currentTheme === 'light' ? 'dark' : 'light');
            return this;
        },
        
        // 响应式布局调整
        adjustLayout: function() {
            return this.each(function(profile) {
                var root = profile.getRoot(),
                    width = ood(document.body).cssSize().width,
                    toolbar = profile.getSubNode('TOOLBAR'),
                    editor = profile.getSubNode('EDITOR'),
                    toolbarBtns = profile.getSubNode('TOOLBARBTN', true),
                    prop = profile.properties;

                // 对于小屏幕，调整布局
                if (width < 768) {
                    root.addClass('richeditor-mobile');
                    
                    // 移动端工具栏按钮调整
                    if (toolbarBtns && toolbarBtns.length > 0) {
                        toolbarBtns.css({
                            'min-width': '2.2em',
                            'min-height': '2.2em',
                            'font-size': '0.9em'
                        });
                    }
                    
                    // 移动端编辑器调整
                    editor.css({
                        'min-height': '8em',
                        'font-size': '1em'
                    });
                } else {
                    root.removeClass('richeditor-mobile');
                    
                    // 恢复桌面样式
                    if (toolbarBtns && toolbarBtns.length > 0) {
                        toolbarBtns.css({
                            'min-width': '',
                            'min-height': '',
                            'font-size': ''
                        });
                    }
                    
                    editor.css({
                        'min-height': '',
                        'font-size': ''
                    });
                }

                // 超小屏幕特殊处理
                if (width < 480) {
                    root.addClass('richeditor-tiny');
                    
                    // 隐藏部分不常用的工具栏按钮
                    if (toolbarBtns && toolbarBtns.length > 0) {
                        toolbarBtns.each(function(btn) {
                            var btnId = ood(btn).id();
                            if (btnId && (btnId.indexOf('fontname') > -1 || btnId.indexOf('fontsize') > -1)) {
                                ood(btn).css('display', 'none');
                            }
                        });
                    }
                } else {
                    root.removeClass('richeditor-tiny');
                    
                    // 恢复所有按钮显示
                    if (toolbarBtns && toolbarBtns.length > 0) {
                        toolbarBtns.css('display', '');
                    }
                }
            });
        },
        
        // 增强可访问性支持
        enhanceAccessibility: function() {
            return this.each(function(profile) {
                var root = profile.getRoot(),
                    toolbar = profile.getSubNode('TOOLBAR'),
                    editor = profile.getSubNode('EDITOR'),
                    toolbarBtns = profile.getSubNode('TOOLBARBTN', true),
                    properties = profile.properties;

                // 为容器添加ARIA属性
                root.attr({
                    'role': 'application',
                    'aria-label': '富文本编辑器'
                });
                
                // 为工具栏添加ARIA属性
                if (toolbar && !toolbar.isEmpty()) {
                    toolbar.attr({
                        'role': 'toolbar',
                        'aria-label': '编辑工具栏'
                    });
                }
                
                // 为编辑器区域添加ARIA属性
                editor.attr({
                    'role': 'textbox',
                    'aria-multiline': 'true',
                    'aria-label': '文本编辑区域'
                });
                
                // 为工具栏按钮添加ARIA属性
                if (toolbarBtns && toolbarBtns.length > 0) {
                    toolbarBtns.each(function(btn) {
                        var btnElement = ood(btn),
                            btnId = btnElement.id(),
                            btnTitle = btnElement.attr('title') || '编辑器按钮';
                        
                        btnElement.attr({
                            'role': 'button',
                            'aria-label': btnTitle,
                            'tabindex': properties.disabled ? '-1' : '0'
                        });
                    });
                }
                
                // 为iframe编辑器内容添加可访问性支持
                var editorDoc = profile.$doc;
                if (editorDoc && editorDoc.body) {
                    ood(editorDoc.body).attr({
                        'role': 'textbox',
                        'aria-multiline': 'true',
                        'aria-label': '富文本编辑内容'
                    });
                }
            });
        }
    },
    Static: {
        DIRYMARKICON: "DIRTYMARK",
        Templates: {
            tagName: 'div',
            style: '{_style}',
            className: '{_className} ood-ui-selectable',
            LABEL: {
                className: '{_required} ood-ui-ellipsis',
                style: '{labelShow};width:{_labelSize};{_labelHAlign};{_labelVAlign}',
                text: '{labelCaption}'
            },
            BOX: {
                EDITOR: {
                    tagName: 'div',

                    className: 'ood-uiborder-flat ood-uiborder-radius ood-uibase'
                },
                DIRTYMARK: {},
                POOL: {}
            }
        },
        DataModel: {
            // 现代化属性
            theme: {
                ini: 'light',
                listbox: ['light', 'dark'],
                action: function(value) {
                    this.boxing().setTheme(value);
                }
            },
            responsive: {
                ini: true,
                action: function(value) {
                    if (value) {
                        this.boxing().adjustLayout();
                    }
                }
            },
            
            expression: {
                ini: '',
                action: function () {
                }
            },
            selectable: true,

            value: {
                ini: '',
                html: 1
            },
            width: {
                $spaceunit: 1,
                ini: '32em'
            },
            height: {
                $spaceunit: 1,
                ini: '25em'
            },
            frameTemplate: {
                ini: '<html style="-webkit-overflow-scrolling: touch;padding:0;margin:0.25em;">' +
                '<head>' +
                '<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />' +
                '<meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0, maximum-scale=1.0, user-scalable=0\">' +
                '<style type="text/css">' +
                'body{height: 100%;-webkit-overflow-scrolling: touch;border:0;padding:0;margin:.0;cursor:text;color:var(--ood-text-primary);font-family:arial,helvetica,clean,sans-serif;font-style:normal;font-weight:normal;font-size:12px;line-height:1.22}' +
                'div, p{margin:0;padding:0;} ' +
                'body, p, div{word-wrap: break-word;} ' +
                'img, input, textarea{cursor:default;}' +
                '</style>' +
                '</head>' +
                '<body scroll="auto" spellcheck="false"></body>' +
                '</html>',
                action: function () {
                    this.boxing().refresh();
                }
            },
            frameStyle: "",
            cmdList: {
                ini: 'font1;font2;align;list;font4;font3;insert;clear;html',
                action: function (v) {
                    var ns = this;
                    if (!ns.properties.disabled && !ns.properties.readonly)
                        ns.box._iniToolBar(ns);
                }
            },
            cmdFilter: {
                ini: ''
            },
            enableBar: {
                ini: false,
                action: function (disabled) {
                    var ns = this;
                    this.cmdList = "";
                    this.labelPos = 'none';
                    if (!ns.properties.disabled && !ns.properties.readonly) {
                        ns.box._iniToolBar(ns);
                    }
                    ood.UI.$doResize(this, this.properties.width, this.properties.height, true);
                }
            },
            disabled: {
                ini: false,
                action: function (disabled) {
                    var disabled = this.properties.disabled || this.properties.readonly,
                        doc = this.$doc;
                    if (doc) {
                        if (doc.body.contentEditable != undefined && ood.browser.ie)
                            doc.body.contentEditable = disabled ? "false" : "true";
                        else
                            doc.designMode = disabled ? "off" : "on";
                        this.box._iniToolBar(this, !disabled);
                    }
                }
            },
            readonly: {
                ini: false,
                action: function (v) {
                    this.boxing().setDisabled(v);
                }
            },
            textType: {
                ini: "html",
                listbox: ['text', 'html'],
                action: function (v) {

                }
            },
            // label
            labelSize: {
                $spaceunit: 2,
                ini: 0,
                action: function (v) {
                    this.getSubNode('LABEL').css({display: v && v != 'auto' ? '' : 'none'});
                    ood.UI.$doResize(this, this.properties.width, this.properties.height, true);
                }
            },
            labelPos: {
                ini: "left",
                listbox: ['none', 'left', 'top', 'right', 'bottom'],
                action: function (v) {
                    ood.UI.$doResize(this, this.properties.width, this.properties.height, true);
                }
            },
            labelGap: {
                $spaceunit: 2,
                ini: 4,
                action: function (v) {
                    ood.UI.$doResize(this, this.properties.width, this.properties.height, true);
                }
            },
            labelCaption: {
                ini: "",
                action: function (v) {
                    v = (ood.isSet(v) ? v : "") + "";
                    this.getSubNode('LABEL').html(ood.adjustRes(v, true));
                }
            },
            labelHAlign: {
                ini: 'right',
                listbox: ['', 'left', 'center', 'right'],
                action: function (v) {
                    this.getSubNode('LABEL').css({
                        'textAlign': v || '',
                        'justifyContent': v == 'right' ? 'flex-end' : v == 'center' ? 'center' : v == 'left' ? 'flex-start' : ''
                    });
                }
            },
            labelVAlign: {
                ini: 'top',
                listbox: ['', 'top', 'middle', 'bottom'],
                action: function (v) {
                    this.getSubNode('LABEL').css('align-items', v == 'bottom' ? 'flex-end' : v == 'middle' ? 'center' : v == 'top' ? 'flex-start' : '');
                }
            }
        },
        Appearances: {
            KEY: {
                overflow: "hidden"
            },
            BOX: {
                left: 0,
                top: 0,
                width: '100%',
                position: 'absolute',
                overflow: 'hidden'
            },
            POOL: {
                position: 'absolute',
                display: 'none'
            },
            DIRTYMARK: {
                position: 'absolute',
                width: "1em",
                height: "1em"
            },
            TOOLBARBTN: {},
            LABEL: {
                'z-index': 1,
                top: 0,
                left: 0,
                display: ood.browser.isWebKit ? '-webkit-flex' : 'flex',
                position: 'absolute',
                'padding-top': '.5em'
            },
            EDITOR: {
                'background-color': 'var(--bg-input) !important',
                'color': 'var(--text-input)  !important',
                position: 'absolute',
                display: 'block',
                left: 0,
                top: 0,
                width: '100%',
                height: '100%',
                padding: 0,
                margin: 0,
                'z-index': '10'
            }
        },
        Behaviors: {
            LABEL: {
                onClick: function (profile, e, src) {
                    if (profile.properties.disabled) return false;
                    if (profile.onLabelClick)
                        profile.boxing().onLabelClick(profile, e, src);
                },
                onDblClick: function (profile, e, src) {
                    if (profile.properties.disabled) return false;
                    if (profile.onLabelDblClick)
                        profile.boxing().onLabelDblClick(profile, e, src);
                },
                onMousedown: function (profile, e, src) {
                    if (ood.Event.getBtn(e) != 'left') return;
                    if (profile.properties.disabled) return false;
                    if (profile.onLabelActive)
                        profile.boxing().onLabelActive(profile, e, src);
                }
            }
        },
        EventHandlers: {
            onInnerEvent: function (profile, type, node, e) {
            },
            onUpdateToolbar: function (profile, etype, doc) {
            },
            onReady: function (profile) {
            },

            onLabelClick: function (profile, e, src) {
            },
            onLabelDblClick: function (profile, e, src) {
            },
            onLabelActive: function (profile, e, src) {
            }
        },
        $cmds: {
            //font style
            font1: [
                {
                    id: 'bold',
                    command: 'Bold',
                    statusButton: true,
                    imageClass: 'oodfont ood-icon-bold',
                    _fi_: 'ood-icon-bold'
                },
                {
                    id: 'italic',
                    command: 'Italic',
                    statusButton: true,
                    imageClass: 'oodfont ood-icon-italic',
                    _fi_: 'ood-icon-italic'
                },
                {
                    id: 'underline',
                    command: 'Underline',
                    statusButton: true,
                    imageClass: 'oodfont ood-icon-underline',
                    _fi_: 'ood-icon-underline'
                },
                {
                    id: 'strikethrough',
                    command: 'strikeThrough',
                    statusButton: true,
                    imageClass: 'oodfont ood-icon-strikethrough',
                    _fi_: 'ood-icon-strikethrough'
                }
            ],
            font2: [
                {
                    id: 'subscript',
                    command: 'subscript',
                    statusButton: true,
                    imageClass: "oodfont ood-icon-sub",
                    _fi_: 'ood-icon-sub'
                },
                {
                    id: 'superscript',
                    command: 'superscript',
                    statusButton: true,
                    imageClass: "oodfont ood-icon-super",
                    _fi_: 'ood-icon-super'
                }
            ],
            font3: [
                {
                    id: 'forecolor',
                    command: 'custom',
                    imageClass: "oodfont ood-icon-forecolor",
                    _fi_: 'ood-icon-forecolor'
                },
                {id: 'bgcolor', command: 'custom', imageClass: "oodfont ood-icon-bgcolor", _fi_: 'ood-icon-bgcolor'}
            ],
            font4: [
                {id: 'fontsize', command: 'custom', caption: '$editor.fontsize', dropButton: true},
                {id: 'fontname', command: 'custom', caption: '$editor.fontname', dropButton: true},
                {id: 'formatblock', command: 'custom', caption: '$editor.formatblock', dropButton: true}
            ],
            align: [
                {
                    id: 'left',
                    command: 'justifyleft',
                    imageClass: "oodfont ood-icon-alignleft",
                    _fi_: 'ood-icon-alignleft'
                },
                {
                    id: 'center',
                    command: 'justifycenter',
                    imageClass: "oodfont ood-icon-aligncenter",
                    _fi_: 'ood-icon-aligncenter'
                },
                {
                    id: 'right',
                    command: 'justifyright',
                    imageClass: "oodfont ood-icon-alignright",
                    _fi_: 'ood-icon-alignright'
                },
                {
                    id: 'justify',
                    command: 'justifyfull',
                    imageClass: "oodfont ood-icon-alignjustify",
                    _fi_: 'ood-icon-alignjustify'
                }
            ],
            list: [
                {id: 'indent', command: 'indent', imageClass: "oodfont ood-icon-indent", _fi_: 'ood-icon-indent'},
                {id: 'outdent', command: 'outdent', imageClass: "oodfont ood-icon-outdent", _fi_: 'ood-icon-outdent'},
                {
                    id: 'ol',
                    command: 'insertorderedlist',
                    imageClass: "oodfont ood-icon-number",
                    _fi_: 'ood-icon-number'
                },
                {
                    id: 'ul',
                    command: 'insertunorderedlist',
                    imageClass: "oodfont ood-icon-bullet",
                    _fi_: 'ood-icon-bullet'
                }
            ],
            insert: [
                {
                    id: 'hr',
                    command: 'insertHorizontalRule',
                    imageClass: "oodfont ood-icon-inserthr",
                    _fi_: 'ood-icon-inserthr'
                },
                {
                    id: 'insertimage',
                    command: 'custom',
                    imageClass: "oodfont ood-icon-picture",
                    _fi_: 'ood-icon-picture'
                },
                {id: 'createlink', command: 'custom', imageClass: "oodfont ood-icon-link", _fi_: 'ood-icon-link'},
                {id: 'unlink', command: 'unlink', imageClass: "oodfont ood-icon-breaklink", _fi_: 'ood-icon-breaklink'}
            ],
            clear: [
                {
                    id: 'removeformat',
                    command: 'removeformat',
                    imageClass: "oodfont ood-icon-formatclear",
                    _fi_: 'ood-icon-formatclear'
                }
            ],
            html: [
                {id: 'html', command: 'custom', imageClass: "oodfont ood-icon-html", _fi_: 'ood-icon-html'}
            ]
        },
        _prepareData: function (profile) {
            var d = arguments.callee.upper.call(this, profile), t, v;
            d._labelHAlign = 'text-align:' + (v = d.labelHAlign || '') + ';justify-content:' + (v == 'right' ? 'flex-end' : v == 'center' ? 'center' : v == 'left' ? 'flex-start' : '');
            d._labelVAlign = 'align-items:' + ((v = d.labelVAlign) == 'bottom' ? 'flex-end' : v == 'middle' ? 'center' : v == 'top' ? 'flex-start' : '');
            d.labelShow = d.labelPos != 'none' && d.labelSize && d.labelSize != 'auto' ? "" : "display:none";
            d._labelSize = d.labelSize ? '' : 0 + profile.$picku();
            // adjustRes for labelCaption
            if (!d.enableBar) {
                this.cmdList = "";
                this.labelPos = 'none';
            }
            if (d.textType && d.textType=='text') {
                this.cmdList = "";
                this.labelPos = 'none';
            }



            if (d.labelCaption)
                d.labelCaption = ood.adjustRes(d.labelCaption, true);
            return d;
        },
        _updateToolbar: function (domId, clear, etype) {
            var profile = ood.$cache.profileMap[domId], toolbar;
            if (!profile) return;

            if (profile.properties.disabled || profile.properties.readonly) return;

            if (profile && (toolbar = profile.$toolbar)) {
                var doc = profile.$doc,
                    bold = clear ? false : doc.queryCommandState('bold'),
                    italic = clear ? false : doc.queryCommandState('italic'),
                    underline = clear ? false : doc.queryCommandState('underline'),
                    strikethrough = clear ? false : doc.queryCommandState('strikethrough'),
                    subscript = clear ? false : doc.queryCommandState('subscript'),
                    superscript = clear ? false : doc.queryCommandState('superscript'),

                    tb = toolbar.boxing();

                tb.updateItem('bold', {value: bold})
                tb.updateItem('italic', {value: italic})
                tb.updateItem('underline', {value: underline})
                tb.updateItem('strikethrough', {value: strikethrough})
                tb.updateItem('subscript', {value: subscript})
                tb.updateItem('superscript', {value: superscript})

                if (profile.onUpdateToolbar) {
                    profile.boxing().onUpdateToolbar(profile, etype, doc);
                }
                doc = null;
            }
        },
        RenderTrigger: function () {
            var self = this;

            if (!self.properties.disabled && !self.properties.readonly)
                self.box._iniToolBar(self);

            if (!self.$inDesign) {
                var div = self.getSubNode('EDITOR').get(0),
                    domId = self.$domId,
                    htmlTpl = self.properties.frameTemplate,
                    style = self.properties.frameStyle,
                    id = div.id;
                if (style) {
                    htmlTpl = htmlTpl.replace(/<\s*\/\s*head\s*>/, "<style>" + (style || "") + "</style></head>");
                }
                // rendered already
                if (!self.$once) {
                    self.$once = true;
                    try {
                        var iframe = self.$ifr = document.createElement("IFRAME");
                    } catch (e) {
                        var iframe = self.$ifr = document.createElement("<iframe name='" + id + "' id='" + id + "'></iframe>");
                    }
                    //_updateToolbar event
                    var kprf = this,
                        eventOutput = self._eventOutput = function (e) {
                            if (kprf && (kprf.properties.disabled || kprf.properties.readonly)) return;
                            if (kprf.onInnerEvent)
                                return kprf.boxing().onInnerEvent(kprf, e.type, ood.Event.getSrc(e), e);
                        },
                        event = self._event = function (e) {
                            if (kprf && (kprf.properties.disabled || kprf.properties.readonly)) return;
                            var etype = e.type;
                            if (etype !== "mouseover" && etype !== "mouseout") {
                                ood.resetRun('RichEditor:' + domId, function () {
                                    // destroyed
                                    if (!kprf.box) return;
                                    ood.UI.RichEditor._updateToolbar(domId, false, etype)
                                }, 100);
                                if (etype == 'mousedown') {
                                    if (ood.browser.applewebkit && e.target.tagName == "IMG") {
                                        var sel = self.$win.getSelection(), range = self.$doc.createRange();
                                        range.selectNode(e.target);
                                        sel.removeAllRanges();
                                        sel.addRange(range);
                                    }

                                    //for BlurTrigger
                                    ood.doc.onMousedown(true);
                                }
                            }
                            if (kprf.onInnerEvent)
                                return kprf.boxing().onInnerEvent(kprf, etype, ood.Event.getSrc(e), e);
                        },
                        event2 = self._event2 = function (e) {
                            if (kprf && (kprf.properties.disabled || kprf.properties.readonly)) return;
                            if (kprf.onInnerEvent) {
                                var etype = e.type;
                                ood.resetRun(kprf.$xid + ":frmInnerAsyEvent", function () {
                                    if (kprf && !kprf.destroyed)
                                        kprf.boxing().onInnerEvent(kprf, etype, ood.Event.getSrc(e), e);
                                });
                            }
                        },
                        _focus = function (e) {
                            if (!kprf) return;
                            if (kprf.properties.disabled || kprf.properties.readonly) return;
                            kprf.box._onchange(kprf);
                        },
                        _blur = function (e) {
                            if (!kprf) return;
                            if (kprf.properties.disabled || kprf.properties.readonly) return;

                            ood.resetRun('RichEditor:' + domId, function () {
                                // destroyed
                                if (!kprf.box) return;
                                ood.UI.RichEditor._updateToolbar(domId, true, 'blur')
                            }, 100);

                            if (kprf._onchangethread) {
                                clearInterval(kprf._onchangethread);
                                kprf._onchangethread = null;
                                // check again
                                if (kprf && kprf.box)
                                    kprf.box._checkc(kprf);
                            }

                            var v = kprf.boxing()._getCtrlValue();
                            // here: dont trigger setCtrlValue
                            kprf.boxing().setUIValue(v, null, true, 'blur');
                        },
                        gekfix = function (e) {
                            // to fix firefox appendChid's bug: refresh iframe's document
                            if (kprf) {
                                var ins = kprf.boxing();
                                ood.asyRun(function () {
                                    // destroyed
                                    if (!kprf.box) return;
                                    ins.refresh();
                                });
                            }
                        },
                        doc, win,
                        checkF = function () {
                            ood.setTimeout(function () {
                                // removed from DOM already
                                if (!frames[id]) return;
                                // not ready
                                if (!frames[id].document) return;

                                if (self.$win != frames[id].window) {
                                    win = self.$win = frames[id].window;

                                    doc = self.$doc = win.document;

                                    doc.open();
                                    doc.write(htmlTpl);
                                    doc.close();

                                    //if(ood.browser.isTouch && (ood.browser.isAndroid||||ood.browser.isBB)){
                                    //    ood(doc.body).$touchscroll('xy');
                                    //}

                                    try {
                                        doc.execCommand("styleWithCSS", 0, false)
                                    } catch (e) {
                                        try {
                                            doc.execCommand("useCSS", 0, true)
                                        } catch (e) {
                                        }
                                    }

                                    var disabled = self.properties.disabled || self.properties.readonly;

                                    if (doc.body.contentEditable != undefined && ood.browser.ie)
                                        doc.body.contentEditable = disabled ? "false" : "true";
                                    else
                                        doc.designMode = disabled ? "off" : "on";

                                    // ensure toolbar disable
                                    if (disabled) {
                                        self.box._iniToolBar(self, false);
                                    }

                                    win._gekfix = gekfix;

                                    if (ood.browser.ie && doc.attachEvent) {
                                    //    doc.attachEvent("unload", gekfix);

                                        if (!disabled) {
                                            doc.attachEvent("onmousedown", event);
                                            doc.attachEvent("ondblclick", event);
                                            doc.attachEvent("onclick", event);
                                            doc.attachEvent("oncontextmenu", eventOutput);
                                            doc.attachEvent("onkeyup", event);
                                            doc.attachEvent("onkeydown", event);

                                            doc.attachEvent("onmouseover", event);
                                            doc.attachEvent("onmouseout", event);
                                            doc.attachEvent("onmousemove", event2);

                                            win.attachEvent("onfocus", _focus);
                                            win.attachEvent("onblur", _blur);
                                            (self.$beforeDestroy = (self.$beforeDestroy || {}))["ifmClearMem"] = function () {
                                                var win = this.$win,
                                                    doc = this.$doc,
                                                    event = this._event,
                                                    event2 = this._event2;
                                                if (this._onchangethread) {
                                                    clearInterval(this._onchangethread);
                                                    this._onchangethread = null;
                                                }

                                                // crack for ie7/8 eat focus
                                                // error raise in ie6
                                                try {
                                                    var status = doc.designMode;
                                                    doc.designMode = "off";
                                                    doc.designMode = "on";
                                                    doc.designMode = status;
                                                } catch (e) {
                                                }

                                                win._gekfix = undefined;


                                                if (!this.properties.disabled && !this.properties.readonly) {
                                                    doc.detachEvent("onmousedown", event);
                                                    doc.detachEvent("ondblclick", event);
                                                    doc.detachEvent("onclick", event);
                                                    doc.detachEvent("oncontextmenu", eventOutput);
                                                    doc.detachEvent("onkeyup", event);
                                                    doc.detachEvent("onkeydown", event);

                                                    doc.detachEvent("onmouseover", event);
                                                    doc.detachEvent("onmouseout", event);
                                                    doc.detachEvent("onmousemove", event2);

                                                    win.detachEvent("onfocus", _focus);
                                                    win.detachEvent("onblur", _blur);
                                                }
                                                win = doc = event = event2 = null;
                                            };
                                        }
                                    } else {
                                        var prf = self;
                                        // for opera
                                        if (ood.browser.opr || !win.addEventListener) {
                                            prf.$repeatT = ood.Thread.repeat(function () {
                                                if (!frames[id])
                                                    return false;
                                                else {
                                                    if (!prf.$win.document || !prf.$win.document.defaultView)
                                                        prf.boxing().refresh();
                                                }
                                            }, 99);
                                        }
                                        //else
                                          //  win.addEventListener("unload", gekfix, false);

                                        if (!disabled) {
                                            doc.addEventListener("mousedown", event, false);
                                            doc.addEventListener("dblclick", event, false);
                                            doc.addEventListener("click", event, false);
                                            doc.addEventListener("contextmenu", eventOutput, false);
                                            doc.addEventListener("keyup", event, false);

                                            doc.addEventListener("mouseover", event, false);
                                            doc.addEventListener("mouseout", event, false);
                                            doc.addEventListener("mousemove", event2, false);

                                            if (ood.browser.gek || !win.addEventListener) {
                                                doc.addEventListener("focus", _focus, false);
                                                doc.addEventListener("blur", _blur, false);
                                                doc.addEventListener("keypress", event, false);
                                            } else {
                                                win.addEventListener("focus", _focus, false);
                                                win.addEventListener("blur", _blur, false);
                                                doc.addEventListener("keydown", event, false);
                                            }
                                        }

                                        //don't ues $ondestory, opera will set doc to null
                                        (self.$beforeDestroy = (self.$beforeDestroy || {}))["ifmClearMem"] = function () {
                                            var win = this.$win,
                                                doc = this.$doc,
                                                ifr = this.$ifr,
                                                event = this._event,
                                                event2 = this._event2;
                                            // for opera
                                            if (ood.browser.opr)
                                                if (prf.$repeatT) prf.$repeatT.abort();

                                            if (ifr.detachEvent) {
                                                ifr.detachEvent('onload', checkF);
                                            } else {
                                                ifr.onload = null;
                                            }

                                            win._gekfix = undefined;

                                            //for firefox
                                            delete frames[this.$frameId];

                                            if (!this.properties.disabled && !this.properties.readonly && doc.removeEventListener) {
                                                doc.removeEventListener("mousedown", event, false);
                                                doc.removeEventListener("dblclick", event, false);
                                                doc.removeEventListener("click", event, false);
                                                doc.removeEventListener("contextmenu", eventOutput, false);
                                                doc.removeEventListener("keyup", event, false);

                                                doc.removeEventListener("mouseover", event, false);
                                                doc.removeEventListener("mouseout", event, false);
                                                doc.removeEventListener("mousemove", event2, false);

                                                if (ood.browser.gek || !win.removeEventListener) {
                                                    doc.removeEventListener("focus", _focus, false);
                                                    doc.removeEventListener("blur", _blur, false);
                                                    doc.removeEventListener("keypress", event, false);
                                                } else {
                                                    win.removeEventListener("focus", _focus, false);
                                                    win.removeEventListener("blur", _blur, false);
                                                    doc.removeEventListener("keydown", event, false);
                                                }
                                            }
                                            prf = gekfix = event = event2 = win = doc = null;
                                        };
                                    }

                                    self.boxing()._setCtrlValue(self.properties.$UIvalue || "");

                                    iframe.style.visibility = '';
                                    iframe.style.overflow = 'auto';

                                    if (self.onReady) self.boxing().onReady(self);
                                }
                            });
                        };
                    self.$frameId = id;
                    iframe.id = iframe.name = id;
                    iframe.className = div.className;
                    iframe.src = "about:blank";
                    iframe.frameBorder = 0;
                    iframe.border = 0;
                    iframe.scrolling = 'yes';
                    iframe.marginWidth = 0;
                    iframe.marginHeight = 0;
                    iframe.tabIndex = -1;
                    iframe.allowTransparency = "allowtransparency";
                    iframe.style.visibility = 'hidden';

                    //replace the original one
                    ood.$cache.domPurgeData[iframe.$xid = div.$xid].element = iframe;
                    div.parentNode.replaceChild(iframe, div);

                    if (iframe.attachEvent) {
                        iframe.attachEvent('onload', checkF);
                    } else {
                        iframe.onload = checkF;
                    }
                }
            } else {
                self.boxing()._setCtrlValue(self.properties.value || "");
            }
            
            // 现代化功能初始化
            ood.asyRun(function(){
                self.boxing().RichEditorTrigger();
            });
        },
        

        _checkc: function (profile) {
            if (profile && profile.$doc) {
                var doc = profile.$doc, body = doc && (doc.body || doc.documentElement);
                if (!profile.__oldv)
                    profile.__oldv = body.innerHTML;
                if (body.innerHTML != profile.__oldv) {
                    var ov = profile.__oldv;
                    profile.__oldv = body.innerHTML;
                    profile.boxing().onChange(profile, ov, body.innerHTML);
                }
            }
        },
        _onchange: function (profile) {
            if (profile.onChange) {
                if (profile._onchangethread) {
                    clearInterval(profile._onchangethread);
                    profile._onchangethread = null;
                }
                profile._onchangethread = setInterval(function () {
                    if (profile && profile.box)
                        profile.box._checkc(profile);
                }, 500);
            }
        },
        _clearPool: function (profile) {
            profile.getSubNode('POOL').empty();
            profile.$colorPicker = profile.$fontsizeList = profile.$fontnameList = profile.$formatblockList = profile.$htmlEditor = null;
        },
        _iniToolBar: function (profile, flag) {
            var self = profile,
                pro = self.properties,
                cmdFilter = (pro.cmdFilter || '').split(/[\s,;]+/),
                tbH;
            if (self.$toolbar) {
                self.$toolbar.boxing().destroy(true);
                delete self._$tb;
                delete self.$toolbar;
            }

            if (flag !== false) {
                var t, v, o, items = [],
                    imageClass = self.getClass('TOOLBARBTN'),
                    arr = pro.cmdList.split(/[\s,;]+/),
                    h = {};
                ood.arr.each(arr, function (i) {
                    //filter
                    if ((o = self.box.$cmds[i]) && !h[i]) {
                        h[i] = 1;
                        ood.filter(o, function (v) {
                            if (ood.arr.indexOf(cmdFilter, v.id) !== -1) return false;
                            if (v.imagePos)
                                v.imageClass = imageClass;
                            v.tips = ood.wrapRes('editor.' + v.id);
                        });
                        items.push({id: i, sub: o});
                    }
                });

                //compose
                t = new ood.UI.ToolBar({
                    selectable: false,
                    handler: false,
                    items: items,
                    disabled: pro.disabled || pro.readonly
                });
                t.setCustomStyle('ITEMS', 'border:none');
                self.getSubNode('BOX').prepend(t);
                t.render(true);
                // keep toolbar's height number here
                profile.$_tbH = tbH = t.getRoot().offsetHeight();
                if (ood.browser.ie)
                    t.getSubNode('BOX').query('*').attr('unselectable', 'on');

                t = self._$tb = t.get(0);

                t.onClick = self.box._toolbarclick;
                v = self._$composed = {};
                v[t.$xid] = t;
                self.$toolbar = t;
                t.$hostage = self;
            }
            profile.adjustSize(true);
            return tbH;
        },
        _toolbarclick: function (profile, item, group, e, src) {
            var editor = profile.$hostage;
            if (!editor.$doc) return;

            var pro = editor.properties, first;
            editor.$win.focus();

            if (item.command == 'custom') {
                var cmd = item.id,
                    o, _clear, node,
                    items, items2;

                //get the pop control
                switch (cmd) {
                    case 'forecolor':
                    case 'bgcolor':
                        if (!editor.$colorPicker) {
                            first = true;
                            editor.$colorPicker = (new ood.UI.ColorPicker({
                                selectable: false,
                                barDisplay: false
                            })).render(true);
                        }
                        o = editor.$colorPicker;
                        break;
                    case 'fontsize':
                    case 'fontname':
                    case 'formatblock':
                        //if lang was changed, clear the pool first
                        if (editor.$lang != ood.getLang())
                            editor.box._clearPool(editor);
                        editor.$lang = ood.getLang();

                        //font size
                        if (cmd == 'fontsize') {
                            if (!editor.$fontsizeList) {
                                items = ood.getRes('editor.fontsizeList');
                                items = items.split(';');
                                items2 = [];
                                var t;
                                ood.arr.each(items, function (o) {
                                    o = o.split(',');
                                    t = o[0] == '...' ? '1' : o[0];
                                    items2.push({
                                        id: o[0],
                                        caption: '<font size="' + o[0] + '" ' + ood.$IEUNSELECTABLE() + '>' + o[1] + '</font>'
                                    });
                                });
                                first = true;
                                editor.$fontsizeList = (new ood.UI.List({
                                    selectable: false,
                                    height: 'auto',
                                    items: items2,
                                    width: 150
                                })).render(true);
                            }
                            o = editor.$fontsizeList;
                            //font family
                        } else if (cmd == 'fontname') {
                            if (!editor.$fontnameList) {
                                items = ood.getRes('editor.fontnameList');
                                items = items.split(';');
                                items2 = [];
                                var t;
                                ood.arr.each(items, function (o) {
                                    t = o == '...' ? '' : o;
                                    items2.push({
                                        id: o,
                                        caption: '<span style="font-family:' + o + '" ' + ood.$IEUNSELECTABLE() + '>' + o + '</span>'
                                    });
                                });
                                first = true;
                                editor.$fontnameList = (new ood.UI.List({
                                    selectable: false,
                                    height: 'auto',
                                    items: items2
                                })).render(true);
                            }
                            o = editor.$fontnameList;
                            //font format
                        } else if (cmd == 'formatblock') {
                            if (!editor.$formatblockList) {
                                items = ood.getRes('editor.formatblockList');
                                items = items.split(';');
                                items2 = [];
                                var t;
                                ood.arr.each(items, function (o) {
                                    o = o.split(',');
                                    t = o[0] == '...' ? 'span' : o[0];
                                    items2.push({
                                        id: o[0],
                                        caption: '<' + t + ' style="display:inline;padding:0;margin:0" ' + ood.$IEUNSELECTABLE() + '>' + o[1] + '</' + t + '>'
                                    });
                                });
                                first = true;
                                editor.$formatblockList = (new ood.UI.List({
                                    selectable: false,
                                    height: 'auto',
                                    items: items2
                                })).render(true);
                            }
                            o = editor.$formatblockList;
                        }
                        break;
                    case 'html':
                        if (!editor.$htmlEditor) {
                            first = true;
                            editor.$htmlEditor = new ood.UI.Input({
                                multiLines: true,
                                width: 400,
                                height: 300,
                                resizer: true
                            });
                        }
                        o = editor.$htmlEditor;
                        break;
                }
                //pop the control and set clear funciton
                if (o) {
                    var sid = profile.key + ":" + editor.$xid;
                    _clear = function () {
                        o.beforeUIValueSet(null);
                        editor.getSubNode('POOL').append(o.getRoot());
                        node.setBlurTrigger(sid);
                        ood.Event.keyboardHook('esc', 0, 0, 0, sid);
                        ood.asyRun(function () {
                            // destroyed
                            if (!editor || !editor.$win) return;
                            editor.$win.focus()
                        });
                    };

                    o.setValue('', true, 'clear');
                    node = o.reBoxing();

                    if (editor.$htmlEditor == o) {
                        var root = editor.getRoot(), ifr = editor.getSubNode("EDITOR");
                        o.setLeft(ifr.left()).setTop(ifr.top()).setWidth(ifr.offsetWidth()).setHeight(ifr.offsetHeight());
                        o.setZIndex(10);
                        editor.getSubNode('BOX').append(node);
                    } else {
                        node.popToTop(src);
                    }

                    if (first && ood.browser.ie)
                        o.getSubNode('BOX').query('*').attr('unselectable', 'on');

                    ood.tryF(o.activate, [], o);

                    //for on blur disappear
                    node.setBlurTrigger(sid, function () {
                        //force to trigger beforeUIValueSet event
                        if (o == editor.$htmlEditor)
                            var v = o._getCtrlValue();
                        // here: dont trigger setCtrlValue
                        o.setUIValue(v, null, true, 'blur');
                        _clear();
                    });
                    //for esc
                    ood.Event.keyboardHook('esc', 0, 0, 0, function () {
                        _clear();
                    }, sid, null, null, profile.domId);
                }
                //set beforeUIValueSet function
                switch (cmd) {
                    case 'forecolor':
                    case 'bgcolor':
                        o.beforeUIValueSet(function (p, o, v) {
                            _clear();
                            var doc = editor.$doc;
                            if (cmd == 'bgcolor' && ood.browser.gek) {
                                doc.execCommand('useCSS', 0, false);
                                doc.execCommand('hilitecolor', false, '#' + v);
                                doc.execCommand('useCSS', 0, true);
                            } else {
                                if (cmd == 'bgcolor')
                                    cmd = ood.browser.opr ? 'hilitecolor' : 'backcolor';
                                doc.execCommand(cmd, false, ood.browser.kde ? ('#' + v) : v);
                            }
                            doc = null;
                            return false;
                        });
                        break;
                    case 'fontsize':
                    case 'fontname':
                    case 'formatblock':
                        o.beforeUIValueSet(function (p, o, v) {
                            _clear();
                            //store range for IE
                            if (ood.browser.ie && (v == '...' || cmd == 'formatblock')) {
                                var selection = editor.$doc.selection,
                                    range = selection ? selection.createRange() : null;
                                if (range && range.parentElement().ownerDocument != editor.$doc)
                                    range = selection = null;
                            }
                            var f = function (cmd, v) {
                                var doc = editor.$doc;

                                //for formatblock in IE
                                //reset range for IE
                                if (range) {
                                    editor.$win.focus();
                                    if (cmd == 'formatblock' && v) {
                                        var p = range.parentElement(), html;
                                        if (p.ownerDocument == doc) {
                                            if (/^\s*</.test(range.htmlText)) {
                                                //affect the first block only
                                                range.collapse(true);
                                                p = range.parentElement();
                                                if (p.tagName == 'BODY') {
                                                    html = p.innerHTML;
                                                    p.innerHTML = "<" + v + ">" + html + "</" + v + ">"
                                                } else {
                                                    html = p.outerHTML;
                                                    html = html.replace(/\<[\w]+/, '<' + v).replace(/[\w]+\>$/, v + '>');
                                                    p.outerHTML = html;
                                                }
                                            } else {
                                                range.pasteHTML("<" + v + ">" + range.htmlText + "</" + v + ">")
                                            }
                                        }
                                        p = null;
                                    }
                                    range.select();
                                    selection = range = null;
                                }

                                doc.execCommand(cmd, false, v);
                                doc = null;
                            };
                            if (v == '...') {
                                var str = ood.getRes('editor.' + cmd);
                                ood.UI.Dialog.prompt(str, str, "", function (v) {
                                    if (v) {
                                        f(cmd, v);
                                    }
                                }, function () {
                                    //reset range for IE
                                    if (ood.browser.ie) {
                                        if (range) {
                                            editor.$win.focus();
                                            range.select();
                                        }
                                        selection = range = null
                                    }
                                });
                            } else
                                f(cmd, v);
                        });
                        break;
                    case 'insertimage':
                    case 'createlink':
                        var str = ood.getRes('editor.' + cmd),
                            str2 = ood.getRes('editor.' + cmd + '2');
                        //store range for IE
                        if (ood.browser.ie) {
                            var selection = editor.$doc.selection,
                                range = selection ? selection.createRange() : null;
                            if (range && range.parentElement().ownerDocument != editor.$doc)
                                range = selection = null;
                        }
                        ood.UI.Dialog.prompt(str, str2, "http:/" + '/', function (v) {
                            //reset range for IE
                            if (ood.browser.ie) {
                                if (range) {
                                    editor.$win.focus();
                                    range.select();
                                }
                                selection = range = null
                            }
                            if (v) {
                                var doc = editor.$doc;
                                doc.execCommand(cmd, false, ood.adjustRes(v, 0, 1));
                                doc = null;
                            }
                        }, function () {
                            //reset range for IE
                            if (ood.browser.ie) {
                                if (range) {
                                    editor.$win.focus();
                                    range.select();
                                }
                                selection = range = null
                            }
                        });
                        break;
                    case 'html':
                        var v = editor.boxing().getUIValue();
                        if (ood.Coder) v = ood.Coder.formatText(v, 'html');
                        o.setValue(v, true, 'editor');
                        o.beforeUIValueSet(function (p, o, v) {
                            _clear();
                            // here: trigger setCtrlValue
                            editor.boxing().setUIValue(v, null, null, 'html');
                        });
                        break;
                }
            } else {
                editor.$doc.execCommand(item.command, false, item.commandArgs || null);

                if (item.id == 'removeformat')
                    ood.UI.RichEditor._updateToolbar(editor.$domId, true, 'none')
                editor.$win.focus();
            }
        },
        _ensureValue: function (profile, value) {
            var p = ood.$getGhostDiv();
            p.innerHTML = (ood.isSet(value) ? value : '') + "";
            value = p.innerHTML;
            p.innerHTML = "";
            return value;
        },
        _onresize: function (profile, width, height) {
            if (width || height) {
                if (!height)
                    height = profile.properties.height;

                var prop = profile.properties,
                    root = profile.getRoot(),
                    box = profile.getSubNode('BOX'),
                    label = profile.getSubNode('LABEL'),

                    us = ood.$us(profile),
                    adjustunit = function (v, emRate) {
                        return profile.$forceu(v, us > 0 ? 'em' : 'px', emRate)
                    },

                    fzrate = profile.getEmSize() / root._getEmSize(),
                    labelfz = label._getEmSize(fzrate),

                    labelPos = prop.labelPos,
                    labelSize = (labelPos == 'none' || !labelPos) ? 0 : profile.$px(prop.labelSize, labelfz) || 0,
                    labelGap = (labelPos == 'none' || !labelPos) ? 0 : profile.$px(prop.labelGap) || 0,
                    ll, tt, ww, hh;

                // caculate by px
                if (width && width != 'auto') width = profile.$px(width);
                if (height && height != 'auto') height = profile.$px(height);

                box.cssRegion({
                    left: adjustunit(ll = labelPos == 'left' ? labelSize : 0),
                    top: adjustunit(tt = labelPos == 'top' ? labelSize : 0),
                    width: adjustunit(ww = width === null ? null : Math.max(0, (width - ((labelPos == 'left' || labelPos == 'right') ? labelSize : 0)))),
                    height: adjustunit(hh = height === null ? null : Math.max(0, (height - ((labelPos == 'top' || labelPos == 'bottom') ? labelSize : 0))))
                });
                if (labelSize) {
                    label.cssRegion({
                        left: adjustunit(width === null ? null : Math.max(0, labelPos == 'right' ? (width - labelSize + labelGap) : 0), labelfz),
                        top: adjustunit(height === null ? null : Math.max(0, labelPos == 'bottom' ? (height - labelSize + labelGap) : 0), labelfz),
                        width: adjustunit(width === null ? null : Math.max(0, ((labelPos == 'left' || labelPos == 'right') ? (labelSize - labelGap) : width)), labelfz),
                        height: adjustunit(height === null ? null : Math.max(0, ((labelPos == 'top' || labelPos == 'bottom') ? (labelSize - labelGap) : height)), labelfz)
                    });
                }
                ood.asyRun(function () {
                    if (!profile.renderId) return;

                    // calculate toolbar's height
                    var itb = profile._$tb,
                        size = {},
                        _top = 0,
                        tbh;
                    if (itb) {
                        // here, do resize first
                        itb.getRoot().width(adjustunit(ww, itb.getRoot()));
                        tbh = itb.getRoot().offsetHeight(true);
                        if (tbh)
                            profile.$_tbH = tbh;
                        else
                            tbh = profile.$_tbH;
                    }
                    _top = (itb ? (tbh - 1) : 0);

                    size.height = hh - _top - 2;
                    if (ww) size.width = ww - 2;

                    if (size.width < 0) size.width = 0;
                    if (size.height < 0) size.height = 0;

                    if (ww || hh) {
                        if (profile && profile.renderId) {
                            // non-sence for setting fontSize for EDITOR or MARK
                            size.width = adjustunit(size.width);
                            size.height = adjustunit(size.height);

                            profile.getSubNode('EDITOR').top(adjustunit(_top)).cssSize(size, true);
                            profile.getSubNode('DIRTYMARK').left(0 + profile.$picku()).top(adjustunit(_top + 1));
                        }
                    }
                }, 100/*greater than 16*/);
            }
        }
    }
});

