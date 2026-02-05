ood.Class("ood.UI.Tensor", "ood.UI", {

    Instance: {
        // 添加 iniProp 对象来存储默认值
        iniProp: {
            patterned: false,
            dock: 'fill'
        },

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
        }
    },


    Static: {
        Appearances: {
            KEY: {
                overflow: 'hidden',
                'border': '1px solid var(--ood-border)',
                'background-color': 'var(--ood-bg)'
            },
            H5: {
                position: 'absolute',
                left: '-1px',
                top: '-1px',
                'z-index': 1,
                'border': 'none'
            },
            COVER: {
                position: 'absolute',
                left: '-1px',
                top: '-1px',
                width: 0,
                height: 0,
                'z-index': 4,
                'background-color': 'var(--ood-overlay)'
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
                ini: '/plugins/dist/index.html',
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