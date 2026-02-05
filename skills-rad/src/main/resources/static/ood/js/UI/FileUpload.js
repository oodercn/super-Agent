ood.Class("ood.UI.FileUpload", "ood.UI", {
    iniProp: {dock: 'fill'},
    Instance: {
        setQueryData: function (data, path) {
            this.each(function (prf) {
                if (path) ood.set(prf.properties.params, (path || "").split("."), data);
                else prf.properties.params = data || {};
            });

        },

        reload: function (profile) {
            var src = this.getSrc(),
                uploadUrl = this.getUploadUrl(),
                host = profile.host,
                prepareFormData = this.getPrepareFormData(),
                params = this.getParams(), hash = {};
            if (!profile.$inDesign) {
                if (src && uploadUrl) {
                    if (host && prepareFormData) {
                        if (host.PAGECTX) {
                            ood.merge(hash, host.PAGECTX.getFormValues(), 'all');
                        } else {
                            ood.merge(hash, host.getData());
                        }
                    }
                    hash.uploadUrl = params.uploadUrl || uploadUrl;
                    for (key in params) {
                        if (key != 'uploadUrl') {
                            try {
                                var value = ood.adjustRes(params[key], true, 1, 1, null, {page: profile.module}, profile.module);
                                if (value) {
                                    hash[key] = ood.adjustRes(params[key], true, 1, 1, null, {page: profile.module}, profile.module)
                                }
                            } catch (e) {

                            }

                        }
                    }
                    var rulParams = ood.urlEncode(hash);
                    if (src.indexOf('?') > -1) {
                        src = src + "&" + rulParams;
                    } else {
                        src = src + "?" + rulParams;
                    }
                    profile.getSubNode("H5").attr("src", src);
                }
            }
            return src;
        },
        
        // 设置主题
        setTheme: function(theme) {
            return this.each(function(profile) {
                profile.properties.theme = theme;
                var root = profile.getRoot(),
                    h5 = profile.getSubNode('H5'),
                    cover = profile.getSubNode('COVER');

                // 添加基础类名
                root.addClass('fileupload-themed');
                h5.addClass('fileupload-h5');
                cover.addClass('fileupload-cover');

                // 移除所有主题类
                root.removeClass('fileupload-dark fileupload-hc');
                
                // 应用当前主题类
                if (theme === 'dark') {
                    root.addClass('fileupload-dark');
                } else if (theme === 'high-contrast') {
                    root.addClass('fileupload-hc');
                }
                
                // 保存主题设置
                localStorage.setItem('fileupload-theme', theme);
            });
        },
        
        // 获取当前主题
        getTheme: function() {
            var profile = this.get(0);
            return profile.properties.theme || localStorage.getItem('fileupload-theme') || 'light';
        },

        FileUploadTrigger: function() {
            var profile = this.get(0);
            var prop = profile.properties;
            var boxing = this;

            // 初始化主题
            if (prop.theme) {
                boxing.setTheme(prop.theme);
            } else {
                // 从本地存储恢复主题
                var savedTheme = localStorage.getItem('fileupload-theme');
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


        // 切换主题
        toggleTheme: function() {
            const themes = ['light', 'dark', 'high-contrast'];
            const currentTheme = this.getTheme();
            const nextIndex = (themes.indexOf(currentTheme) + 1) % themes.length;
            this.setTheme(themes[nextIndex]);
            return this;
        },
        
        // 响应式布局调整
        adjustLayout: function() {
            return this.each(function(profile) {
                var root = profile.getRoot(),
                    width = ood(document.body).cssSize().width,
                    h5 = profile.getSubNode('H5'),
                    prop = profile.properties;

                // 对于小屏幕，调整布局
                if (width < 768) {
                    root.addClass('fileupload-mobile');
                    
                    // 移动端调整按钮大小和间距
                    h5.css({
                        'border-radius': '6px',
                        'min-height': '2.5em'
                    });
                } else {
                    root.removeClass('fileupload-mobile');
                    
                    // 恢复桌面样式
                    h5.css({
                        'border-radius': '',
                        'min-height': ''
                    });
                }

                // 超小屏幕特殊处理
                if (width < 480) {
                    root.addClass('fileupload-tiny');
                    
                    // 调整最小高度
                    h5.css({
                        'min-height': '3em'
                    });
                } else {
                    root.removeClass('fileupload-tiny');
                }
            });
        },
        
        // 增强可访问性支持
        enhanceAccessibility: function() {
            return this.each(function(profile) {
                var root = profile.getRoot(),
                    h5 = profile.getSubNode('H5'),
                    properties = profile.properties;

                // 为容器添加ARIA属性
                root.attr({
                    'role': 'application',
                    'aria-label': '文件上传组件'
                });
                
                // 为iframe添加ARIA属性
                if (h5 && !h5.isEmpty()) {
                    h5.attr({
                        'title': '文件上传界面',
                        'aria-label': '文件上传操作区域',
                        'role': 'document'
                    });
                }
            });
        }
    },


    Static: {
        Appearances: {
            KEY: {
                overflow: 'hidden'
            },
            H5: {
                position: 'absolute',
                left: '-1px',
                top: '-1px',
                'z-index': 1,
                'border': '1px solid var(--fileupload-border)'
            },
            COVER: {
                position: 'absolute',
                left: '-1px',
                top: '-1px',
                width: 0,
                height: 0,
                'z-index': 4,
                'opacity': '0.7'
            }
        },
        Templates: {
            tagName: 'div',
            className: '{_className}',
            style: '{_style}',
            H5: {
                tagName: 'iframe',
                src: '{src}',
                text: 'Your browser does not support the audio element.'
            },
            COVER: {
                tagName: 'div',
                style: "background-image:url(" + ood.ini.img_bg + ");"
            }
        },
        Behaviors: {
            HotKeyAllowed: false
        },
        DataModel: {
            // 现代化属性
            theme: {
                ini: 'light',
                listbox: ['light', 'dark', 'high-contrast'],
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
            
            selectable: true,
            width: {
                $spaceunit: 1,
                ini: '40em'
            },
            height: {
                $spaceunit: 1,
                ini: '30em'
            },

            src: {
                ini: '/plugins/fileupload/uploadgrid.html',
                action: function (v) {
                    this.getSubNode("H5").attr("src", ood.adjustRes(v));
                }
            },

            prepareFormData: {
                ini: true
            },
            uploadUrl: {
                ini: 'upload/',
                action: function (v) {
                    //  this.getSubNode("H5").attr("uploadUrl", ood.adjustRes(v));
                }
            },
            params: {
                ini: {},
                action: function (v) {
                    this.getSubNode("H5").attr("params", v);
                }
            }

        },
        RenderTrigger: function () {
            var prf = this,
                H5 = prf.getSubNode('H5'),
                prop = prf.properties;
            if (window['postMessage']) {
                self._msgcb = function (data) {
                    var e = ood.unserialize(data);
                    if (e.data) {
                        e = e.data
                    }
                    if (prf[e.eventType]) {
                        switch (e.eventType) {
                            case    'uploadfile':
                                prf.boxing().uploadfile(prf, e.eventType, e.item, e.response)
                                break;
                            case    'uploadfail':
                                prf.boxing().uploadfail(prf, e.eventType, e.item, e.response)
                                break;
                            case    'uploadcomplete':
                                prf.boxing().uploadcomplete(prf, e.eventType, e.item, e.response)
                                break;
                            case    'uploadprogress':
                                prf.boxing().uploadprogress(prf, e.eventType, e.item, e.response)
                                break;
                        }
                    }

                };
                if (window.addEventListener) window.addEventListener('message', self._msgcb, false);
                else window.attachEvent('onmessage', self._msgcb);
            }
            
            // 现代化功能初始化
            ood.asyRun(function(){
                prf.boxing().FileUploadTrigger();
            });
        },
        

        _prepareData: function (profile) {
            var data = arguments.callee.upper.call(this, profile), hash = {}, host = profile.host, ns = this, src;
            if (!profile.$inDesign) {
                if (data.prepareFormData) {
                    var event = function (module) {
                        profile.boxing().reload(profile);
                    };
                    if (profile.getModule()) {
                        profile.getModule().setEvents("afterShow", event, false);
                    } else {
                        src = profile.boxing().reload(profile);
                    }
                }
            }
            if (src) {
                data.src = src;
            } else if (data.src && data.uploadUrl) {
                hash.uploadUrl = data.params.uploadUrl || data.uploadUrl;
                var rulParams = ood.urlEncode(hash);
                data.src = data.src + "?" + rulParams;
            }
            return data;
        },


        EventHandlers: {

            uploadfile: function (profile, eventType, item, response) {
            }
            ,
            uploadfail: function (profile, eventType, item, response) {
            }
            ,
            uploadcomplete: function (profile, eventType, item, response) {
            }
            ,
            uploadprogress: function (profile, eventType, item, response) {

            }
        }
        ,
        _onresize: function (profile, width, height) {
            var H5 = profile.getSubNode('H5'),
                size = H5.cssSize(),
                prop = profile.properties,
                us = ood.$us(profile),
                adjustunit = function (v, emRate) {
                    return profile.$forceu(v, us > 0 ? 'em' : 'px', emRate)
                },

                // caculate by px
                ww = width ? profile.$px(width) : width,
                hh = height ? profile.$px(height) : height;

            if ((width && !ood.compareNumber(size.width, ww, 6)) || (height && !ood.compareNumber(size.height, hh, 6))) {
                // reset here
                if (width) {
                    H5.attr("width", ww).width(prop.width = adjustunit(ww));
                }
                if (height) {
                    H5.attr("height", hh).height(prop.height = adjustunit(hh));
                }
                if (profile.$inDesign || prop.cover) {
                    profile.getSubNode('COVER').cssSize({
                        width: width ? prop.width : null,
                        height: height ? prop.height : null
                    }, true);
                }
            }
        }
    }

})
;