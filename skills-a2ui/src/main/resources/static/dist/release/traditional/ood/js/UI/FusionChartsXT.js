/*
ood.UI.FusionChartsXT is a EUSUI wrap for FusionChartsXT(www.FusionCharts.com), it is NOT part of EUSUI products
If you use this widget in commercial projects, please purchase it separately
*/
ood.Class("ood.UI.FusionChartsXT", "ood.UI", {
    Initialize: function () {

        // for fusioncharts in IE<=7
        if (!window.JSON) window.JSON = {
            parse: function (a) {
                return ood.unserialize(a)
            },
            stringify: function (a) {
                return ood.stringify(a)
            }
        };
    },
    Instance: {

        initialize: function () {
        },
        refreshChart: function (dataFormat) {
            return this.each(function (prf) {
                if (!prf || !prf.box) return;
                prf.boxing().busy(false, '');
                if (prf.renderId) {
                    var fun = function () {
                        var prop = prf.properties, t;
                        if (prf._chartId && (t = FusionCharts(prf._chartId))) {
                            // dispose
                            t.dispose();
                            // clear node
                            prf.getSubNode('BOX').html("", false);
                        }

                        // new one
                        var fc = new FusionCharts(
                            prop.chartType,
                            prf._chartId,
                            prf.$isEm(prop.width) ? prf.$em2px(prop.width) : prop.width,
                            prf.$isEm(prop.height) ? prf.$em2px(prop.height) : prop.height
                            ),
                            flag;

                        switch (dataFormat) {
                            case 'XMLUrl':
                                var xml = ood.getFileSync(prop.XMLUrl);
                                if (xml) fc.setXMLData(xml);
                                break;
                            case 'JSONUrl':
                                var json = ood.getFileSync(prop.JSONUrl);
                                if (json) fc.setJSONData(json);
                                break;
                            case 'XMLData':
                                fc.setXMLData(prop.XMLData);
                                break;
                            default:
                                if (prop.XMLUrl) {
                                    var xml = ood.getFileSync(prop.XMLUrl);
                                    if (xml) fc.setXMLData(xml);
                                } else if (prop.JSONUrl) {
                                    var json = ood.getFileSync(prop.JSONUrl);
                                    if (json) fc.setJSONData(json);
                                } else if (prop.XMLData) {
                                    fc.setXMLData(prop.XMLData);
                                } else if (!ood.isEmpty(prop.JSONData)) {
                                    flag = 1;
                                    fc.setJSONData(prf.box._prepareFCData(prf, prop.JSONData));
                                }
                        }
                        // ensure cursor pointer
                        if (!flag) {
                            fc.setJSONData(prf.box._prepareFCData(prf, fc.getJSONData()));
                        }
                        fc.setTransparent(true);
                        fc.render(prf.getSubNode('BOX').id());
                        // attachEvents
                        var t = FusionCharts(prf._chartId),
                            f1 = function (a, argsMap) {
                                if (prf.onDataClick) {
                                    var sourceData, datas = a.sender.options.dataSource.data;
                                    if (datas) {
                                        ood.arr.each(datas, function (data) {
                                            if (data.id = a.data.id) {
                                                sourceData = data;
                                            }
                                        })
                                    }
                                    prf.boxing().onDataClick(prf, argsMap, sourceData)
                                }
                            }, f2 = function (a, argsMap) {
                                if (prf.onLabelClick) {
                                    prf.boxing().onLabelClick(prf, argsMap);
                                }
                            }, f3 = function (a, argsMap) {
                                if (prf.onAnnotationClick) prf.boxing().onAnnotationClick(prf, argsMap);
                            };

                        if (prf._f1) t.removeEventListener("dataplotClick", prf._f1);
                        if (prf._f2) t.removeEventListener("dataLabelClick", prf._f2);
                        if (prf._f3) t.removeEventListener("annotationClick", prf._f3);

                        t.addEventListener("dataplotClick", prf._f1 = f1);
                        t.addEventListener("dataLabelClick", prf._f2 = f1);
                        t.addEventListener("annotationClick", prf._f3 = f1);

                        prf.boxing().free();
                    };
                    ood.resetRun('ood.UI.FusionChartsXT:' + prf.$xid, fun, 200);
                }
            });
        },
        setTransparent: function (isTransparent) {
            return this.each(function (prf) {
                var t;
                ood.set(prf.properties, ["JSONData", "chart", "bgalpha"], isTransparent ? "0,0" : "");
                if (prf.renderId && prf._chartId && (t = FusionCharts(prf._chartId))) {
                    t.setTransparent(isTransparent);
                }
            });
        },
        getChartAttribute: function (key) {
            var prf = this.get(0);
            return ood.isStr(key) ? ood.get(prf.properties, ["JSONData", "chart", key]) : ood.get(prf.properties, ["JSONData", "chart"]);
        },
        setChartAttribute: function (key, value) {
            var h = {};
            if (ood.isStr(key)) {
                h[key] = value;
            } else h = key;

            return this.each(function (prf) {
                var t;
                if (prf.renderId && prf._chartId && (t = FusionCharts(prf._chartId))) {
                    t.setChartAttribute(h);
                    // refresh memory in ood from real
                    ood.set(prf.properties, ["JSONData", "chart"], t.getChartAttribute());
                } else {
                    // reset memory in ood only
                    var opt = ood.get(prf.properties, ["JSONData", "chart"]);
                    if (opt) ood.merge(opt, h, 'all');
                }
            });
        },
        getFCObject: function () {
            var prf = this.get(0);
            return prf.renderId && prf._chartId && FusionCharts(prf._chartId);
        },
        getSVGString: function () {
            var prf = this.get(0), o = prf.renderId && prf._chartId && FusionCharts(prf._chartId);
            return o ? o.getSVGString() : null;
        },

        updateCategories: function (data, index) {
            this.each(function (prf) {
                var JSONData = prf.properties.JSONData;
                data = ood.clone(data);
                JSONData.categories = data;
            });
            return this.refreshChart();
        },

        updateLine: function (data, index) {
            this.each(function (prf) {
                var JSONData = prf.properties.JSONData;
                data = ood.clone(data);
                if (ood.isArr(data) && ood.isArr(data[0])) {
                    JSONData.lineset = data;
                } else if ('lineset' in JSONData) {
                    ood.set(JSONData, ["lineset", index || 0, "data"], data);
                }
            });
            return this.refreshChart();
        },

        updateData: function (data, index) {
            this.each(function (prf) {
                var JSONData = prf.properties.JSONData;
                data = ood.clone(data);
                if (ood.isArr(data) && ood.isArr(data[0])) {
                    if ('dataset' in JSONData) {
                        JSONData.dataset = data;
                    } else {
                        JSONData.data = data[0];
                    }
                } else {
                    if ('dataset' in JSONData) {
                        ood.set(JSONData, ["dataset", index || 0, "data"], data);
                    } else {
                        JSONData.data = data;
                    }
                }
            });
            return this.refreshChart();
        },


        fillData: function (data, index, isLineset) {
            this.each(function (prf) {
                var JSONData = prf.properties.JSONData;
                data = ood.clone(data);
                if (ood.isArr(data) && ood.isArr(data[0])) {
                    if (isLineset) {
                        JSONData.lineset = data;
                    } else {
                        if ('dataset' in JSONData) {
                            JSONData.dataset = data;
                        } else {
                            JSONData.data = data[0];
                        }
                    }
                } else {
                    if (isLineset) {
                        if ('lineset' in JSONData) {
                            ood.set(JSONData, ["lineset", index || 0, "data"], data);
                        }
                    } else {
                        if ('dataset' in JSONData) {
                            ood.set(JSONData, ["dataset", index || 0, "data"], data);
                        } else {
                            JSONData.data = data;
                        }
                    }
                }
            });
            return this.refreshChart();
        },
        updateData: function (index, value) {
            return this.each(function (prf) {
                if (prf.renderId && prf._chartId && (t = FusionCharts(prf._chartId))) {
                    if (t.setData)
                        t.setData(index, value);
                }
            });
        },
        updateDataById: function (key, value) {
            return this.each(function (prf) {
                if (prf.renderId && prf._chartId && (t = FusionCharts(prf._chartId)))
                    if (t.setDataForId)
                        t.setDataForId(key, value);
            });
        },
        callFC: function (funName, params) {
            var fc;
            if ((fc = this.getFCObject()) && ood.isFun(fc[funName]))
                return fc[funName].apply(fc, params || []);
        },
        configure: function (options) {
            var prf = this.get(0), t;
            if (prf.renderId && prf._chartId && (t = FusionCharts(prf._chartId))) {
                t.configure(options);
            }
        },
        setTheme: function (theme) {
            if (typeof theme != "string" || !theme) theme = null;
            this.each(function (o) {
                if (theme != o.theme) {
                    if (theme === null)
                        delete o.theme;
                    else
                        o.theme = theme;
                }
            });
            return this.setChartAttribute("theme", theme);
        }
    },
    Static: {
        _objectProp: {JSONData: 1, configure: 1, plotData: 1, feedData: 1},
        Appearances: {
            KEY: {
                overflow: 'hidden',
                'background-color': 'var(--ood-bg)',
                'border': '1px solid var(--ood-border)'
            },
            BOX: {
                position: 'absolute',
                left: 0,
                top: 0,
                'z-index': 1,
                'background-color': 'var(--ood-bg-secondary)'
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
            BOX: {
                tagName: 'div'
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
            tabindex: null,
            expression: {
                ini: '',
                action: function () {
                }
            },
            defaultFocus: null,
            disableClickEffect: null,
            disableHoverEffect: null,
            disableTips: null,
            disabled: null,
            renderer: null,
            selectable: null,
            tips: null,
            width: {
                $spaceunit: 1,
                ini: '30em'
            },
            height: {
                $spaceunit: 1,
                ini: '25em'
            },
            chartCDN: "/plugins/fusioncharts/fusioncharts.js",
            chartType: {
                ini: "Column2D",
                //Single Series Charts
                listbox: ["Column2D", "Column3D", "Line", "Area2D", "Bar2D", "Bar3D", "Pie2D", "Pie3D", "Doughnut2D", "Doughnut3D", "Pareto2D", "Pareto3D",
                    //Multi-series
                    "MSColumn2D", "MSColumn3D", "MSLine", "MSBar2D", "MSBar3D", "MSArea", "Marimekko", "ZoomLine",
                    //Stacked
                    "StackedColumn3D", "StackedColumn2D", "StackedBar2D", "StackedBar3D", "StackedArea2D", "MSStackedColumn2D",
                    //Combination
                    "MSCombi3D", "MSCombi2D", "MSColumnLine3D", "StackedColumn2DLine", "StackedColumn3DLine", "MSCombiDY2D", "MSColumn3DLineDY", "StackedColumn3DLineDY", "MSStackedColumn2DLineDY",
                    //XYPlot
                    "Scatter", "Bubble",
                    //Scroll
                    "ScrollColumn2D", "ScrollLine2D", "ScrollArea2D", "ScrollStackedColumn2D", "ScrollCombi2D", "ScrollCombiDY2D",
                    // funnel
                    "Funnel",
                    // real time
                    "RealTimeLine", "RealTimeArea", "RealTimeColumn", "RealTimeLineDY", "RealTimeStackedArea", "RealTimeStackedColumn",
                    // Gauges
                    "HLinearGauge", "Cylinder", "HLED", "VLED", "Thermometer", "AngularGauge",
                    // others
                    "Pyramid ", "Radar"//,"MultiLevelPie"
                ],
                action: function () {
                    if (this.renderId) {
                        this.boxing().refreshChart();
                    }
                }
            },
            JSONData: {
                ini: {},
                get: function () {
                    var prf = this, prop = prf.properties, fc;
                    if (!ood.isEmpty(prop.JSONData))
                        return prop.JSONData;
                    else if (fc = prf.boxing().getFCObject())
                        return prf.box._cleanData(prf, fc.getJSONData());
                },
                set: function (data) {
                    var prf = this, prop = prf.properties;
                    if (ood.isStr(data)) data = ood.unserialize(data);
                    if (data) {
                        prop.XMLData = prop.XMLUrl = prop.JSONUrl = "";
                        prop.JSONData = ood.clone(data);

                        if (prf.renderId) {
                            prf.boxing().refreshChart('JSONData');
                        }
                    }
                }
            },
            XMLUrl: {
                ini: "",
                set: function (url) {
                    var prf = this, prop = prf.properties;

                    prop.XMLUrl = url;
                    prop.JSONUrl = prop.XMLData = "";
                    prop.JSONData = {};

                    if (prf.renderId) {
                        prf.boxing().refreshChart('XMLUrl');
                    }
                }
            },
            XMLData: {
                ini: "",
                get: function (force) {
                    var prf = this, prop = prf.properties, fc;
                    if (prop.XMLData)
                        return prop.XMLData;
                    else if (fc = prf.boxing().getFCObject())
                        return fc.getXMLData();
                },
                set: function (url) {
                    var prf = this, prop = prf.properties;

                    prop.XMLData = url;
                    prop.XMLUrl = prop.JSONUrl = "";
                    prop.JSONData = {};

                    if (prf.renderId) {
                        prf.boxing().refreshChart('XMLData');
                    }
                }
            },
            JSONUrl: {
                ini: "",
                set: function (url) {
                    var prf = this, prop = prf.properties;

                    prop.JSONUrl = url;
                    prop.XMLUrl = prop.XMLData = "";
                    prop.JSONData = {};

                    if (prf.renderId) {
                        prf.boxing().refreshChart('JSONUrl');
                    }
                }
            },
            plotData: {
                ini: {},
                get: function (data) {
                    var data = this.properties.JSONData;
                    return data.dataset || data.data || {};
                },
                set: function (data) {
                    var JSONData = this.properties.JSONData;
                    if (('dataset' in JSONData) || (ood.isArr(data) && ood.isArr(data[0])))
                        JSONData.dataset = ood.clone(data);
                    else
                        JSONData.data = ood.clone(data);

                    var bak = JSONData.chart.animation;
                    JSONData.chart.animation = '0';
                    this.boxing().refreshChart();
                    if (bak) JSONData.chart.animation = bak; else delete JSONData.chart.animation;
                    return this;
                }
            },
            feedData: {
                ini: "",
                set: function (data) {
                    var prf = this, t;
                    if (prf.renderId && prf._chartId && (t = FusionCharts(prf._chartId)) && t.feedData) {
                        if (ood.isFinite(data)) data = "value=" + data;
                        t.feedData(data || "");
                    }
                }
            }
        },
        _cleanData: function (prf, data) {
            var hoder = "Javascript:void(0)";
            if (data.dataset) {
                ood.arr.each(data.dataset, function (o, i) {
                    ood.arr.each(o.dataset, function (v, j) {
                        ood.arr.each(v.data, function (w, k) {
                            if (w.link == hoder) delete w.link;
                        });
                    });
                    ood.arr.each(o.data, function (v, j) {
                        if (v.link == hoder) delete v.link;
                    });
                });
            } else if (data.data) {
                ood.arr.each(data.data, function (o, i) {
                    if (o.link == hoder) delete o.link;
                    if (o.labelLink == hoder) delete o.labelLink;
                });
            }
            if (data.categories) {
                ood.arr.each(data.categories, function (o, i) {
                    ood.arr.each(o.category, function (v, j) {
                        if (v.link == hoder) delete v.link;
                    });
                });
            }
            return data;
        },
        _prepareFCData: function (prf, data) {
            var id = prf.$xid;
            data = ood.clone(data),
                hoder = "Javascript:void(0)";
            //show cursor as pointer
            if (data.dataset) {
                ood.arr.each(data.dataset, function (o, i) {
                    ood.arr.each(o.dataset, function (v, j) {
                        ood.arr.each(v.data, function (w, k) {
                            if (!w.link) w.link = hoder;
                        });
                    });
                    ood.arr.each(o.data, function (v, j) {
                        if (!v.link) v.link = hoder;
                    });
                });
            } else if (data.data) {
                ood.arr.each(data.data, function (o, i) {
                    if (!o.link) o.link = hoder;
                    if (!o.labelLink) o.labelLink = hoder;
                });
            }
            if (data.categories) {
                ood.arr.each(data.categories, function (o, i) {
                    ood.arr.each(o.category, function (v, j) {
                        if (!v.link) v.link = hoder;
                    });
                });
            }
            return data;
        },
        RenderTrigger: function () {
            var prf = this, prop = prf.properties;
            var fun = function () {
                if (!prf || !prf.box) return;

                // give chart dom id
                prf._chartId = "FC_" + prf.properties.chartType + "_" + prf.$xid;

                if (!ood.isEmpty(prf.properties.configure)) {
                    prf.boxing().setConfigure(prf.properties.configure, true);
                }
                if (prf.theme)
                    prf.boxing().setTheme(prf.theme);
                // render it
                prf.boxing().refreshChart();

                // set before destroy function
                (prf.$beforeDestroy = (prf.$beforeDestroy || {}))["unsubscribe"] = function () {
                    var t;
                    if (this._chartId && (t = FusionCharts(this._chartId))) {
                        t.removeEventListener("dataplotClick", prf._f1);
                        t.removeEventListener("dataLabelClick", prf._f2);
                        t.removeEventListener("annotationClick", prf._f3);
                        prf._f1 = prf._f2 = prf._f3 = null;
                        t.dispose();
                    }
                }
            };

            if (window.FusionCharts) fun();
            else {
                prf.boxing().busy(false, "Loading charts ...");
                ood.include("FusionCharts", prop.chartCDN, function () {
                    if (prf && prf.box) {
                        prf.boxing().free();
                        fun();
                    }
                }, null, false, {cache: true});
            }
        },


        getWidget: function () {
            dataConf = {
                FCPie: {
                    "chart": {
                        "caption": "Monthly Sales Summary",
                        "subcaption": "For the year 2006",
                        "xaxisname": "Month",
                        "yaxisname": "Sales",
                        "numberprefix": "$",
                        "useroundedges": "1",
                        "bgcolor": "FFFFFF,FFFFFF",
                        "showborder": "0",
                        "enablerotation": "0"
                    },
                    "data": [{"label": "January", "value": "17400"}, {"label": "February", "value": "19800"}, {
                        "label": "March",
                        "value": "21800"
                    }, {"label": "April", "value": "23800"}, {"label": "May", "value": "29600"}, {
                        "label": "June",
                        "value": "27600"
                    }]
                },
                FCsingle: {
                    "chart": {
                        "caption": "Monthly Sales Summary",
                        "subcaption": "For the year 2006",
                        "xaxisname": "Month",
                        "yaxisname": "Sales",
                        "numberprefix": "$",
                        "useroundedges": "1",
                        "bgcolor": "FFFFFF,FFFFFF",
                        "showborder": "0",
                        "rotatevalues": "1"
                    },
                    "data": [{"label": "January", "value": "17400"}, {
                        "vline": "1",
                        "color": "FF5904",
                        "thickness": "2"
                    }, {"label": "February", "value": "19800"}, {"label": "March", "value": "21800"}, {
                        "label": "April",
                        "value": "23800"
                    }, {"label": "May", "value": "29600"}, {"label": "June", "value": "27600"}],
                    "trendlines": [{"line": [{"startvalue": "22000", "color": "00cc00", "displayvalue": "Average"}]}]
                },
                FCmulti: {
                    "chart": {
                        "caption": "Business Results 2005 v 2006",
                        "xaxisname": "Month",
                        "yaxisname": "Revenue",
                        "showvalues": "0",
                        "numberprefix": "$",
                        "useroundedges": "1"
                    },
                    "categories": [{
                        "category": [{"label": "Jan"}, {"label": "Feb"}, {"label": "Mar"}, {"label": "Apr"}, {"label": "May"}, {"label": "Jun"}, {
                            "vline": "1",
                            "color": "FF5904",
                            "thickness": "2"
                        }, {"label": "July"}]
                    }],
                    "dataset": [{
                        "seriesname": "2006",
                        "data": [{"value": "27400"}, {"value": "29800"}, {"value": "25800"}, {"value": "26800"}, {"value": "29600"}, {"value": "32600"}, {"value": "31800"}]
                    }, {
                        "seriesname": "2005",
                        "data": [{"value": "10000"}, {"value": "11500"}, {"value": "12500"}, {"value": "15000"}, {"value": "11000"}, {"value": "9800"}, {"value": "11800"}]
                    }],
                    "trendlines": [{
                        "line": [{
                            "startvalue": "26000",
                            "color": "91C728",
                            "displayvalue": "Target",
                            "showontop": "1"
                        }]
                    }]
                },
                FCstack: {
                    "chart": {
                        "caption": "Business Results 2005 v 2006",
                        "xaxisname": "Month",
                        "yaxisname": "Revenue",
                        "showvalues": "0",
                        "numberprefix": "$"
                    },
                    "categories": [{"category": [{"label": "Jan"}, {"label": "Feb"}, {"label": "Mar"}, {"label": "Apr"}, {"label": "May"}, {"label": "Jun"}]}],
                    "dataset": [{
                        "seriesname": "2006",
                        "data": [{"value": "27400"}, {"value": "29800"}, {"value": "25800"}, {"value": "26800"}, {"value": "29600"}, {"value": "32600"}]
                    }, {
                        "seriesname": "2005",
                        "renderas": "Area",
                        "data": [{"value": "10000"}, {"value": "11500"}, {"value": "12500"}, {"value": "15000"}, {"value": "11000"}, {"value": "9800"}]
                    }, {
                        "seriesname": "2004",
                        "renderas": "Line",
                        "data": [{"value": "7000"}, {"value": "10500"}, {"value": "9500"}, {"value": "10000"}, {"value": "9000"}, {"value": "8800"}]
                    }],
                    "trendlines": [{
                        "line": [{
                            "startvalue": "22000",
                            "color": "91C728",
                            "displayvalue": "Target",
                            "showontop": "1"
                        }]
                    }]
                },
                FCstack2: {
                    "chart": {
                        "caption": "Annual Revenue",
                        "subcaption": "In Million $",
                        "xaxisname": "Year",
                        "pyaxisname": "Sales in M$",
                        "syaxisname": "Cost as % of Revenue",
                        "decimals": "0",
                        "numberprefix": "$",
                        "numbersuffix": "M",
                        "snumbersuffix": "%25"
                    },
                    "categories": [{"category": [{"label": "2001"}, {"label": "2002"}, {"label": "2003"}, {"label": "2004"}, {"label": "2005"}]}],
                    "dataset": [{
                        "dataset": [{
                            "seriesname": "Product A",
                            "color": "AFD8F8",
                            "showvalues": "0",
                            "data": [{"value": "30"}, {"value": "26"}, {"value": "29"}, {"value": "31"}, {"value": "34"}]
                        }, {
                            "seriesname": "Product B",
                            "color": "F6BD0F",
                            "showvalues": "0",
                            "data": [{"value": "21"}, {"value": "28"}, {"value": "39"}, {"value": "41"}, {"value": "24"}]
                        }]
                    }, {
                        "dataset": [{
                            "seriesname": "Service A",
                            "color": "8BBA00",
                            "showvalues": "0",
                            "data": [{"value": "27"}, {"value": "25"}, {"value": "28"}, {"value": "26"}, {"value": "10"}]
                        }, {
                            "seriesname": "Service B",
                            "color": "A66EDD",
                            "showvalues": "0",
                            "data": [{"value": "17"}, {"value": "15"}, {"value": "18"}, {"value": "16"}, {"value": "10"}]
                        }, {
                            "seriesname": "Service C",
                            "color": "F984A1",
                            "showvalues": "0",
                            "data": [{"value": "12"}, {"value": "17"}, {"value": "16"}, {"value": "15"}, {"value": "12"}]
                        }]
                    }]
                },
                FCcomb: {
                    "chart": {
                        "caption": "Business Results 2005 v 2006",
                        "xaxisname": "Month",
                        "yaxisname": "Revenue",
                        "showvalues": "0",
                        "numberprefix": "$"
                    },
                    "categories": [{"category": [{"label": "Jan"}, {"label": "Feb"}, {"label": "Mar"}, {"label": "Apr"}, {"label": "May"}, {"label": "Jun"}, {"label": "Jul"}, {"label": "Aug"}, {"label": "Sep"}, {"label": "Oct"}, {"label": "Nov"}, {"label": "Dec"}]}],
                    "dataset": [{
                        "seriesname": "2006",
                        "data": [{"value": "27400"}, {"value": "29800"}, {"value": "25800"}, {"value": "26800"}, {"value": "29600"}, {"value": "32600"}, {"value": "31800"}, {"value": "36700"}, {"value": "29700"}, {"value": "31900"}, {"value": "34800"}, {"value": "24800"}]
                    }, {
                        "seriesname": "2005",
                        "renderas": "Area",
                        "data": [{"value": "10000"}, {"value": "11500"}, {"value": "12500"}, {"value": "15000"}, {"value": "11000"}, {"value": "9800"}, {"value": "11800"}, {"value": "19700"}, {"value": "21700"}, {"value": "21900"}, {"value": "22900"}, {"value": "20800"}]
                    }, {
                        "seriesname": "2004",
                        "renderas": "Line",
                        "data": [{"value": "7000"}, {"value": "10500"}, {"value": "9500"}, {"value": "10000"}, {"value": "9000"}, {"value": "8800"}, {"value": "9800"}, {"value": "15700"}, {"value": "16700"}, {"value": "14900"}, {"value": "12900"}, {"value": "8800"}]
                    }],
                    "trendlines": [{
                        "line": [{
                            "startvalue": "22000",
                            "color": "91C728",
                            "displayvalue": "Target",
                            "showontop": "1"
                        }]
                    }]
                }
            },
                widgetConfig = [
                    {
                        id: 'ood.FusionChartsXT.iot',
                        key: 'ood.FusionChartsXT.iot',
                        caption: '工业仪器',
                        tip: "$RAD.widgets.esd.iot",
                        group: true,
                        imageClass: 'ri ri-lightbulb-line',
                        sub: [
                            {
                                id: 'ood.FusionChartsXT.9',
                                key: 'ood.FusionChartsXT.9',
                                caption: '仪表盘',
                                group: true,
                                initFold: false,
                                imageClass: 'ri ri-dashboard-line',
                                sub:
                                    [
                                        {
                                            id: 'ood.UI.FC_agg01',
                                            key: 'ood.UI.FusionChartsXT',
                                            caption: '$(RAD.widgets.fc.Angular) 1',
                                            imageClass: 'ri ri-dashboard-line',
                                            draggable: true,
                                            iniProp: {
                                                chartType: "AngularGauge",
                                                JSONData: {
                                                    "chart": {
                                                        "caption": "Customer Satisfaction Score",
                                                        "lowerlimit": "0",
                                                        "upperlimit": "100",
                                                        "lowerlimitdisplay": "Bad",
                                                        "upperlimitdisplay": "Good",
                                                        "palette": "1",
                                                        "numbersuffix": "%",
                                                        "tickvaluedistance": "10",
                                                        "showvalue": "0",
                                                        "gaugeinnerradius": "0",
                                                        "bgcolor": "FFFFFF",
                                                        "pivotfillcolor": "333333",
                                                        "pivotradius": "8",
                                                        "pivotfillmix": "333333, 333333",
                                                        "pivotfilltype": "radial",
                                                        "pivotfillratio": "0,100",
                                                        "showtickvalues": "1",
                                                        "showborder": "0"
                                                    },
                                                    "colorrange": {
                                                        "color": [{
                                                            "minvalue": "0",
                                                            "maxvalue": "45",
                                                            "code": "e44a00"
                                                        }, {
                                                            "minvalue": "45",
                                                            "maxvalue": "75",
                                                            "code": "f8bd19"
                                                        }, {"minvalue": "75", "maxvalue": "100", "code": "6baa01"}]
                                                    },
                                                    "dials": {
                                                        "dial": [{
                                                            "value": "92",
                                                            "rearextension": "15",
                                                            "radius": "100",
                                                            "bgcolor": "333333",
                                                            "bordercolor": "333333",
                                                            "basewidth": "8"
                                                        }]
                                                    }
                                                }
                                            }
                                        },
                                        {
                                            id: 'ood.UI.FC_agg02',
                                            key: 'ood.UI.FusionChartsXT',
                                            caption: '$(RAD.widgets.fc.Angular) 2',
                                            imageClass: 'ri ri-dashboard-line',
                                            draggable: true,
                                            iniProp: {
                                                chartType: "AngularGauge",
                                                JSONData: {
                                                    "chart": {
                                                        "manageresize": "1",
                                                        "origw": "415",
                                                        "origh": "415",
                                                        "managevalueoverlapping": "1",
                                                        "autoaligntickvalues": "1",
                                                        "tickvaluedistance": "1",
                                                        "bgcolor": "FFFFFF",
                                                        "upperlimit": "5000",
                                                        "lowerlimit": "0",
                                                        "numbersuffix": "/s",
                                                        "basefontcolor": "646F8F",
                                                        "majortmnumber": "11",
                                                        "majortmcolor": "646F8F",
                                                        "majortmheight": "9",
                                                        "minortmnumber": "5",
                                                        "minortmcolor": "646F8F",
                                                        "minortmheight": "3",
                                                        "showgaugeborder": "0",
                                                        "gaugeouterradius": "150",
                                                        "gaugeinnerradius": "135",
                                                        "gaugeoriginx": "210",
                                                        "gaugeoriginy": "210",
                                                        "gaugealpha": "50",
                                                        "placevaluesinside": "1",
                                                        "tooltipbgcolor": "F2F2FF",
                                                        "tooltipbordercolor": "6A6FA6",
                                                        "gaugefillmix": "",
                                                        "showshadow": "0",
                                                        "annrenderdelay": "0",
                                                        "pivotradius": "14",
                                                        "pivotfillmix": "{A1A0FF},{6A6FA6}",
                                                        "pivotbordercolor": "bebcb0",
                                                        "pivotfillratio": "70,30",
                                                        "showborder": "0",
                                                        "gaugestartangle": "230",
                                                        "gaugeendangle": "-50"
                                                    },
                                                    "colorrange": {
                                                        "color": [{
                                                            "minvalue": "0",
                                                            "maxvalue": "4999",
                                                            "code": "A1A0FF"
                                                        }, {"minvalue": "4999", "maxvalue": "5000", "code": "A1A0FF"}]
                                                    },
                                                    "dials": {
                                                        "dial": [{
                                                            "value": "2265",
                                                            "bgcolor": "6A6FA6,A1A0FF",
                                                            "borderalpha": "0",
                                                            "basewidth": "5",
                                                            "topwidth": "4"
                                                        }]
                                                    },
                                                    "annotations": {
                                                        "groups": [{
                                                            "x": "210",
                                                            "y": "210",
                                                            "showbelow": "1",
                                                            "items": [{
                                                                "type": "circle",
                                                                "x": "0",
                                                                "y": "0",
                                                                "radius": "200",
                                                                "fillcolor": "000000,2C6BB2, 135FAB",
                                                                "fillratio": "80,15, 5",
                                                                "bordercolor": "2C6BB2"
                                                            }, {
                                                                "type": "circle",
                                                                "x": "0",
                                                                "y": "0",
                                                                "radius": "180",
                                                                "fillcolor": "FFFFFF, D4D4D4",
                                                                "fillratio": "20,80",
                                                                "bordercolor": "2C6BB2"
                                                            }, {
                                                                "type": "arc",
                                                                "x": "0",
                                                                "y": "0",
                                                                "radius": "180",
                                                                "innerradius": "170",
                                                                "startangle": "-60",
                                                                "endangle": "240",
                                                                "fillcolor": "51884F",
                                                                "fillalpha": "50",
                                                                "bordercolor": "51884F"
                                                            }]
                                                        }]
                                                    }
                                                }
                                            }
                                        },
                                        {
                                            id: 'ood.UI.FC_agg03',
                                            key: 'ood.UI.FusionChartsXT',
                                            caption: '$(RAD.widgets.fc.Angular) 3',
                                            imageClass: 'ri ri-dashboard-line',
                                            draggable: true,
                                            iniProp: {
                                                chartType: "AngularGauge",
                                                JSONData: {
                                                    "chart": {
                                                        "manageresize": "1",
                                                        "origw": "340",
                                                        "origh": "340",
                                                        "bgcolor": "FFFFFF",
                                                        "upperlimit": "100",
                                                        "lowerlimit": "0",
                                                        "showlimits": "1",
                                                        "basefontcolor": "666666",
                                                        "majortmnumber": "11",
                                                        "majortmcolor": "666666",
                                                        "majortmheight": "8",
                                                        "minortmnumber": "5",
                                                        "minortmcolor": "666666",
                                                        "minortmheight": "3",
                                                        "pivotradius": "20",
                                                        "showgaugeborder": "0",
                                                        "gaugeouterradius": "100",
                                                        "gaugeinnerradius": "90",
                                                        "gaugeoriginx": "170",
                                                        "gaugeoriginy": "170",
                                                        "gaugestartangle": "250",
                                                        "gaugeendangle": "-70",
                                                        "placevaluesinside": "1",
                                                        "gaugefillmix": "",
                                                        "pivotfillmix": "{F0EFEA}, {BEBCB0}",
                                                        "pivotbordercolor": "BEBCB0",
                                                        "pivotfillratio": "80,20",
                                                        "showshadow": "0",
                                                        "showborder": "0"
                                                    },
                                                    "colorrange": {
                                                        "color": [{
                                                            "minvalue": "0",
                                                            "maxvalue": "80",
                                                            "code": "00FF00",
                                                            "alpha": "0"
                                                        }, {
                                                            "minvalue": "80",
                                                            "maxvalue": "100",
                                                            "code": "FF0000",
                                                            "alpha": "50"
                                                        }]
                                                    },
                                                    "dials": {
                                                        "dial": [{
                                                            "value": "65",
                                                            "bordercolor": "FFFFFF",
                                                            "bgcolor": "bebcb0, f0efea, bebcb0",
                                                            "borderalpha": "0",
                                                            "basewidth": "10",
                                                            "topwidth": "3"
                                                        }]
                                                    },
                                                    "annotations": {
                                                        "groups": [{
                                                            "x": "170",
                                                            "y": "170",
                                                            "items": [{
                                                                "type": "circle",
                                                                "x": "0",
                                                                "y": "0",
                                                                "radius": "150",
                                                                "bordercolor": "bebcb0",
                                                                "fillasgradient": "1",
                                                                "fillcolor": "f0efea, bebcb0",
                                                                "fillratio": "85,15"
                                                            }, {
                                                                "type": "circle",
                                                                "x": "0",
                                                                "y": "0",
                                                                "radius": "120",
                                                                "fillcolor": "bebcb0, f0efea",
                                                                "fillratio": "85,15"
                                                            }, {
                                                                "type": "circle",
                                                                "x": "0",
                                                                "color": "FFFFFF",
                                                                "y": "0",
                                                                "radius": "100",
                                                                "bordercolor": "f0efea"
                                                            }]
                                                        }]
                                                    }
                                                }
                                            }
                                        },
                                        {
                                            id: 'ood.UI.FC_agg04',
                                            key: 'ood.UI.FusionChartsXT',
                                            caption: '$(RAD.widgets.fc.Angular) 4',
                                            imageClass: 'ri ri-dashboard-line',
                                            draggable: true,
                                            iniProp: {
                                                chartType: "AngularGauge",
                                                JSONData: {
                                                    "chart": {
                                                        "manageresize": "1",
                                                        "origw": "340",
                                                        "origh": "340",
                                                        "bgcolor": "FFFFFF",
                                                        "upperlimit": "100",
                                                        "lowerlimit": "0",
                                                        "showlimits": "1",
                                                        "basefontcolor": "666666",
                                                        "majortmnumber": "11",
                                                        "majortmcolor": "666666",
                                                        "majortmheight": "8",
                                                        "minortmnumber": "5",
                                                        "minortmcolor": "666666",
                                                        "minortmheight": "3",
                                                        "pivotradius": "20",
                                                        "showgaugeborder": "0",
                                                        "gaugeouterradius": "100",
                                                        "gaugeinnerradius": "90",
                                                        "gaugeoriginx": "170",
                                                        "gaugeoriginy": "170",
                                                        "gaugestartangle": "250",
                                                        "gaugeendangle": "-70",
                                                        "placevaluesinside": "1",
                                                        "gaugefillmix": "",
                                                        "pivotfillmix": "{F0EFEA}, {BEBCB0}",
                                                        "pivotbordercolor": "BEBCB0",
                                                        "pivotfillratio": "80,20",
                                                        "showshadow": "0",
                                                        "showborder": "0"
                                                    },
                                                    "colorrange": {
                                                        "color": [{
                                                            "minvalue": "0",
                                                            "maxvalue": "80",
                                                            "code": "00FF00",
                                                            "alpha": "0"
                                                        }, {
                                                            "minvalue": "80",
                                                            "maxvalue": "100",
                                                            "code": "FF0000",
                                                            "alpha": "50"
                                                        }]
                                                    },
                                                    "dials": {
                                                        "dial": [{
                                                            "value": "65",
                                                            "bordercolor": "FFFFFF",
                                                            "bgcolor": "bebcb0, f0efea, bebcb0",
                                                            "borderalpha": "0",
                                                            "basewidth": "10",
                                                            "topwidth": "3"
                                                        }]
                                                    },
                                                    "annotations": {
                                                        "groups": [{
                                                            "x": "170",
                                                            "y": "170",
                                                            "items": [{
                                                                "type": "circle",
                                                                "x": "0",
                                                                "y": "0",
                                                                "radius": "150",
                                                                "bordercolor": "bebcb0",
                                                                "fillasgradient": "1",
                                                                "fillcolor": "f0efea, bebcb0",
                                                                "fillratio": "85,15"
                                                            }, {
                                                                "type": "circle",
                                                                "x": "0",
                                                                "y": "0",
                                                                "radius": "120",
                                                                "fillcolor": "bebcb0, f0efea",
                                                                "fillratio": "85,15"
                                                            }, {
                                                                "type": "circle",
                                                                "x": "0",
                                                                "color": "FFFFFF",
                                                                "y": "0",
                                                                "radius": "100",
                                                                "bordercolor": "f0efea"
                                                            }]
                                                        }]
                                                    }
                                                }
                                            }
                                        },
                                        {
                                            id: 'ood.UI.FC_agg05',
                                            key: 'ood.UI.FusionChartsXT',
                                            caption: '$(RAD.widgets.fc.Angular) 5',
                                            imageClass: 'ri ri-dashboard-line',
                                            draggable: true,
                                            iniProp: {
                                                chartType: "AngularGauge",
                                                JSONData: {
                                                    "chart": {
                                                        "manageresize": "1",
                                                        "origw": "330",
                                                        "origh": "300",
                                                        "bgcolor": "000000",
                                                        "bgalpha": "100",
                                                        "gaugestartangle": "235",
                                                        "gaugeendangle": "-55",
                                                        "lowerlimit": "0",
                                                        "upperlimit": "10",
                                                        "majortmnumber": "11",
                                                        "majortmthickness": "5",
                                                        "majortmcolor": "F48900",
                                                        "majortmheight": "15",
                                                        "minortmnumber": "4",
                                                        "minortmthickness": "2",
                                                        "minortmcolor": "FFFFFF",
                                                        "minortmheight": "13",
                                                        "placevaluesinside": "1",
                                                        "gaugeouterradius": "140",
                                                        "gaugeinnerradius": "85%",
                                                        "basefontcolor": "F48900",
                                                        "basefont": "Impact",
                                                        "basefontsize": "30",
                                                        "showshadow": "0",
                                                        "pivotradius": "20",
                                                        "pivotfillcolor": "000000,383836",
                                                        "pivotfilltype": "linear",
                                                        "pivotfillratio": "50,50",
                                                        "pivotfillangle": "240",
                                                        "annrenderdelay": "0",
                                                        "showborder": "0"
                                                    },
                                                    "dials": {
                                                        "dial": [{
                                                            "value": "5",
                                                            "color": "E70E00",
                                                            "bordercolor": "E70E00",
                                                            "basewidth": "25",
                                                            "topwidth": "1",
                                                            "radius": "85"
                                                        }]
                                                    },
                                                    "trendpoints": {
                                                        "point": [{
                                                            "displayvalue": " ",
                                                            "startvalue": "8",
                                                            "endvalue": "10",
                                                            "radius": "140",
                                                            "innerradius": "0",
                                                            "color": "F48900",
                                                            "alpha": "35",
                                                            "showborder": "0"
                                                        }]
                                                    },
                                                    "annotations": {
                                                        "groups": [{
                                                            "id": "Grp1",
                                                            "showbelow": "0",
                                                            "xscale": "200",
                                                            "yscale": "120",
                                                            "x": "$chartCenterX",
                                                            "y": "$chartCenterY",
                                                            "items": [{
                                                                "type": "circle",
                                                                "x": "0",
                                                                "y": "8",
                                                                "color": "FFFFFF",
                                                                "alpha": "15",
                                                                "radius": "7"
                                                            }]
                                                        }]
                                                    }
                                                }
                                            }
                                        },
                                        {
                                            id: 'ood.UI.FC_agg06',
                                            key: 'ood.UI.FusionChartsXT',
                                            caption: '$(RAD.widgets.fc.Angular) 6',
                                            imageClass: 'ri ri-dashboard-line',
                                            draggable: true,
                                            iniProp: {
                                                chartType: "AngularGauge",
                                                JSONData: {
                                                    "chart": {
                                                        "manageresize": "1",
                                                        "origw": "300",
                                                        "origh": "300",
                                                        "bgcolor": "FFFFFF",
                                                        "gaugestartangle": "225",
                                                        "gaugeendangle": "-45",
                                                        "bgalpha": "100",
                                                        "lowerlimit": "0",
                                                        "upperlimit": "180",
                                                        "majortmnumber": "8",
                                                        "majortmthickness": "3",
                                                        "majortmcolor": "FFFFFF",
                                                        "majortmheight": "7",
                                                        "minortmnumber": "0",
                                                        "placevaluesinside": "1",
                                                        "gaugeouterradius": "110",
                                                        "gaugeinnerradius": "100",
                                                        "showshadow": "0",
                                                        "pivotradius": "20",
                                                        "pivotfillcolor": "000000,383836",
                                                        "pivotfilltype": "linear",
                                                        "pivotfillratio": "50,50",
                                                        "pivotfillangle": "240",
                                                        "annrenderdelay": "0",
                                                        "gaugefillmix": "",
                                                        "showpivotborder": "1",
                                                        "pivotbordercolor": "999999",
                                                        "pivotborderthickness": "2",
                                                        "decimals": "0",
                                                        "gaugeoriginx": "150",
                                                        "gaugeoriginy": "150",
                                                        "basefontcolor": "FFFFFF"
                                                    },
                                                    "dials": {
                                                        "dial": [{
                                                            "value": "50",
                                                            "color": "FFFFFF,999999",
                                                            "alpha": "100",
                                                            "showborder": "0",
                                                            "basewidth": "3",
                                                            "topwidth": "3",
                                                            "radius": "100"
                                                        }]
                                                    },
                                                    "annotations": {
                                                        "groups": [{
                                                            "id": "Grp1",
                                                            "showbelow": "1",
                                                            "x": "150",
                                                            "y": "150",
                                                            "items": [{
                                                                "type": "circle",
                                                                "color": "1C1C1C,AAAAAA,1C1C1C",
                                                                "radius": "127",
                                                                "fillpattern": "linear"
                                                            }, {
                                                                "type": "circle",
                                                                "color": "9E9E9E,ECECEC",
                                                                "radius": "117",
                                                                "fillpattern": "linear",
                                                                "fillangle": "270"
                                                            }, {
                                                                "type": "circle",
                                                                "color": "000000,6C6C6C",
                                                                "radius": "115",
                                                                "fillpattern": "linear",
                                                                "fillangle": "270"
                                                            }]
                                                        }]
                                                    }
                                                }
                                            }
                                        },
                                        {
                                            id: 'ood.UI.FC_agg07',
                                            key: 'ood.UI.FusionChartsXT',
                                            caption: '$(RAD.widgets.fc.Angular) 7',
                                            imageClass: 'ri ri-dashboard-line',
                                            draggable: true,
                                            iniProp: {
                                                chartType: "AngularGauge",
                                                JSONData: {
                                                    "chart": {
                                                        "caption": "Revenue - 2013",
                                                        "manageresize": "1",
                                                        "origw": "400",
                                                        "origh": "250",
                                                        "managevalueoverlapping": "1",
                                                        "autoaligntickvalues": "1",
                                                        "bgcolor": "FFFFFF",
                                                        "fillangle": "45",
                                                        "upperlimit": "2500000",
                                                        "lowerlimit": "1600000",
                                                        "majortmnumber": "10",
                                                        "majortmheight": "8",
                                                        "showgaugeborder": "0",
                                                        "gaugeouterradius": "140",
                                                        "gaugeoriginx": "205",
                                                        "gaugeoriginy": "206",
                                                        "gaugeinnerradius": "2",
                                                        "formatnumberscale": "1",
                                                        "numberprefix": "$",
                                                        "decmials": "2",
                                                        "tickmarkdecimals": "1",
                                                        "pivotradius": "10",
                                                        "showpivotborder": "1",
                                                        "pivotbordercolor": "000000",
                                                        "pivotborderthickness": "10",
                                                        "pivotfillmix": "666666",
                                                        "tickvaluedistance": "10",
                                                        "valuebelowpivot": "1",
                                                        "showvalue": "1",
                                                        "showborder": "0"
                                                    },
                                                    "colorrange": {
                                                        "color": [{
                                                            "minvalue": "1600000",
                                                            "maxvalue": "1930000",
                                                            "code": "e44a00"
                                                        }, {
                                                            "minvalue": "1930000",
                                                            "maxvalue": "2170000",
                                                            "code": "f8bd19"
                                                        }, {
                                                            "minvalue": "2170000",
                                                            "maxvalue": "2500000",
                                                            "code": "6baa01"
                                                        }]
                                                    },
                                                    "dials": {
                                                        "dial": [{
                                                            "value": "2100000",
                                                            "borderalpha": "0",
                                                            "bgcolor": "000000",
                                                            "basewidth": "20",
                                                            "topwidth": "1",
                                                            "radius": "130"
                                                        }]
                                                    },
                                                    "annotations": {
                                                        "groups": [{
                                                            "x": "205",
                                                            "y": "207.5",
                                                            "items": [{
                                                                "type": "circle",
                                                                "x": "0",
                                                                "y": "2.5",
                                                                "radius": "150",
                                                                "startangle": "0",
                                                                "endangle": "180",
                                                                "fillpattern": "linear",
                                                                "fillasgradient": "1",
                                                                "fillcolor": "dddddd,666666",
                                                                "fillalpha": "100,100",
                                                                "fillratio": "50,50",
                                                                "fillangle": "0",
                                                                "showborder": "1",
                                                                "bordercolor": "444444",
                                                                "borderthickness": "2"
                                                            }, {
                                                                "type": "circle",
                                                                "x": "0",
                                                                "y": "0",
                                                                "radius": "145",
                                                                "startangle": "0",
                                                                "endangle": "180",
                                                                "fillpattern": "linear",
                                                                "fillasgradient": "1",
                                                                "fillcolor": "666666,ffffff",
                                                                "fillalpha": "100,100",
                                                                "fillratio": "50,50",
                                                                "fillangle": "0"
                                                            }]
                                                        }]
                                                    }
                                                }
                                            }
                                        },
                                        {
                                            id: 'ood.UI.FC_agg08',
                                            key: 'ood.UI.FusionChartsXT',
                                            caption: '$(RAD.widgets.fc.Angular) 8',
                                            imageClass: 'ri ri-dashboard-line',
                                            draggable: true,
                                            iniProp: {
                                                chartType: "AngularGauge",
                                                JSONData: {
                                                    "chart": {
                                                        "manageresize": "1",
                                                        "origw": "270",
                                                        "origh": "270",
                                                        "managevalueoverlapping": "1",
                                                        "autoaligntickvalues": "1",
                                                        "tickvaluedistance": "10",
                                                        "animation": "0",
                                                        "basefont": "Times New Roman",
                                                        "basefontsize": "18",
                                                        "basefontcolor": "FFFFFF",
                                                        "bgcolor": "333333",
                                                        "bgalpha": "100",
                                                        "gaugestartangle": "90",
                                                        "gaugeendangle": "-270",
                                                        "lowerlimit": "0",
                                                        "upperlimit": "12",
                                                        "lowerlimitdisplay": " ",
                                                        "upperlimitdisplay": "",
                                                        "majortmnumber": "12",
                                                        "majortmthickness": "3",
                                                        "majortmcolor": "FFFFFF",
                                                        "majortmheight": "7",
                                                        "minortmnumber": "4",
                                                        "minortmcolor": "FFFFFF",
                                                        "minortmheight": "4",
                                                        "placevaluesinside": "1",
                                                        "tickvaluestep": "3",
                                                        "gaugeouterradius": "95",
                                                        "gaugeinnerradius": "95",
                                                        "showshadow": "0",
                                                        "pivotfillcolor": "FFFFFF",
                                                        "pivotradius": "6",
                                                        "annrenderdelay": "0",
                                                        "showtooltip": "0",
                                                        "gaugeoriginx": "135",
                                                        "gaugeoriginy": "135"
                                                    },
                                                    "dials": {
                                                        "dial": [{
                                                            "id": "hrs",
                                                            "value": "7.5",
                                                            "color": "FFFFFF",
                                                            "basewidth": "3",
                                                            "topwidth": "1",
                                                            "radius": "50",
                                                            "rearextension": "12"
                                                        }, {
                                                            "id": "min",
                                                            "value": "4.3",
                                                            "color": "FFFFFF",
                                                            "basewidth": "3",
                                                            "topwidth": "1",
                                                            "radius": "70",
                                                            "rearextension": "12"
                                                        }, {
                                                            "id": "sec",
                                                            "value": "3",
                                                            "color": "FF0000",
                                                            "basewidth": "1",
                                                            "topwidth": "1",
                                                            "alpha": "100",
                                                            "radius": "86",
                                                            "rearextension": "20",
                                                            "borderalpha": "0"
                                                        }]
                                                    },
                                                    "annotations": {
                                                        "groups": [{
                                                            "id": "Grp1",
                                                            "showbelow": "1",
                                                            "x": "135",
                                                            "y": "135",
                                                            "items": [{
                                                                "type": "circle",
                                                                "color": "EBF0F4,85898C,484C4F,C5C6C8",
                                                                "fillratio": "30,30,30,10",
                                                                "fillangle": "270",
                                                                "radius": "120",
                                                                "fillpattern": "linear"
                                                            }, {
                                                                "type": "circle",
                                                                "color": "8E8E8E,83878A,E7E7E7",
                                                                "fillangle": "270",
                                                                "radius": "105",
                                                                "fillpattern": "linear"
                                                            }, {
                                                                "type": "circle",
                                                                "color": "07476D,19669E,186AA6,D2EAF6",
                                                                "fillratio": "5,45,40,10",
                                                                "fillangle": "270",
                                                                "radius": "103",
                                                                "fillpattern": "linear"
                                                            }, {
                                                                "type": "circle",
                                                                "color": "07476D,19669E,07476D",
                                                                "fillratio": "5,90,5",
                                                                "fillangle": "270",
                                                                "radius": "100",
                                                                "fillpattern": "linear"
                                                            }]
                                                        }, {
                                                            "id": "Grp2",
                                                            "showbelow": "1",
                                                            "x": "135",
                                                            "y": "135",
                                                            "items": [{
                                                                "type": "circle",
                                                                "radius": "12",
                                                                "color": "012A46"
                                                            }]
                                                        }]
                                                    }
                                                }
                                            }
                                        },
                                        {
                                            id: 'ood.UI.FC_agg09',
                                            key: 'ood.UI.FusionChartsXT',
                                            caption: '$(RAD.widgets.fc.Angular) 9',
                                            imageClass: 'ri ri-dashboard-line',
                                            draggable: true,
                                            iniProp: {
                                                chartType: "AngularGauge",
                                                JSONData: {
                                                    "chart": {
                                                        "manageresize": "1",
                                                        "origw": "400",
                                                        "origh": "250",
                                                        "managevalueoverlapping": "1",
                                                        "autoaligntickvalues": "1",
                                                        "bgcolor": "AEC0CA,FFFFFF",
                                                        "fillangle": "45",
                                                        "upperlimit": "2500000",
                                                        "lowerlimit": "1600000",
                                                        "majortmnumber": "10",
                                                        "majortmheight": "8",
                                                        "showgaugeborder": "0",
                                                        "gaugeouterradius": "140",
                                                        "gaugeoriginx": "205",
                                                        "gaugeoriginy": "206",
                                                        "gaugeinnerradius": "2",
                                                        "formatnumberscale": "1",
                                                        "numberprefix": "$",
                                                        "decmials": "2",
                                                        "tickmarkdecimals": "1",
                                                        "pivotradius": "17",
                                                        "showpivotborder": "1",
                                                        "pivotbordercolor": "000000",
                                                        "pivotborderthickness": "5",
                                                        "pivotfillmix": "FFFFFF,000000",
                                                        "tickvaluedistance": "10",
                                                        "showborder": "0"
                                                    },
                                                    "colorrange": {
                                                        "color": [{
                                                            "minvalue": "1600000",
                                                            "maxvalue": "1930000",
                                                            "code": "399E38"
                                                        }, {
                                                            "minvalue": "1930000",
                                                            "maxvalue": "2170000",
                                                            "code": "E48739"
                                                        }, {
                                                            "minvalue": "2170000",
                                                            "maxvalue": "2500000",
                                                            "code": "B41527"
                                                        }]
                                                    },
                                                    "dials": {
                                                        "dial": [{
                                                            "value": "2100000",
                                                            "borderalpha": "0",
                                                            "bgcolor": "000000",
                                                            "basewidth": "28",
                                                            "topwidth": "1",
                                                            "radius": "130"
                                                        }]
                                                    },
                                                    "annotations": {
                                                        "groups": [{
                                                            "x": "205",
                                                            "y": "207.5",
                                                            "items": [{
                                                                "type": "circle",
                                                                "x": "0",
                                                                "y": "2.5",
                                                                "radius": "150",
                                                                "startangle": "0",
                                                                "endangle": "180",
                                                                "fillpattern": "linear",
                                                                "fillasgradient": "1",
                                                                "fillcolor": "dddddd,666666",
                                                                "fillalpha": "100,100",
                                                                "fillratio": "50,50",
                                                                "fillangle": "0",
                                                                "showborder": "1",
                                                                "bordercolor": "444444",
                                                                "borderthickness": "2"
                                                            }, {
                                                                "type": "circle",
                                                                "x": "0",
                                                                "y": "0",
                                                                "radius": "145",
                                                                "startangle": "0",
                                                                "endangle": "180",
                                                                "fillpattern": "linear",
                                                                "fillasgradient": "1",
                                                                "fillcolor": "666666,ffffff",
                                                                "fillalpha": "100,100",
                                                                "fillratio": "50,50",
                                                                "fillangle": "0"
                                                            }]
                                                        }]
                                                    }
                                                }
                                            }
                                        },
                                        {
                                            id: 'ood.UI.FC_agg10',
                                            key: 'ood.UI.FusionChartsXT',
                                            caption: '$(RAD.widgets.fc.Angular) 10',
                                            imageClass: 'ri ri-dashboard-line',
                                            draggable: true,
                                            iniProp: {
                                                chartType: "AngularGauge",
                                                JSONData: {
                                                    "chart": {
                                                        "manageresize": "1",
                                                        "origw": "200",
                                                        "origh": "190",
                                                        "bgcolor": "FFFFFF",
                                                        "lowerlimit": "0",
                                                        "upperlimit": "55",
                                                        "majortmnumber": "7",
                                                        "showtickvalues": "0",
                                                        "majortmheight": "8",
                                                        "minortmnumber": "0",
                                                        "showtooltip": "0",
                                                        "majortmthickness": "3",
                                                        "gaugeouterradius": "130",
                                                        "gaugeoriginx": "100",
                                                        "gaugeoriginy": "160",
                                                        "gaugestartangle": "125",
                                                        "gaugeendangle": "55",
                                                        "placevaluesinside": "1",
                                                        "gaugeinnerradius": "115",
                                                        "annrenderdelay": "0",
                                                        "pivotfillmix": "{000000},{FFFFFF}",
                                                        "pivotfillratio": "50,50",
                                                        "showpivotborder": "1",
                                                        "pivotbordercolor": "444444",
                                                        "pivotborderthickness": "2",
                                                        "showshadow": "0",
                                                        "pivotradius": "18",
                                                        "pivotfilltype": "linear",
                                                        "showborder": "0"
                                                    },
                                                    "dials": {
                                                        "dial": [{
                                                            "value": "10",
                                                            "borderalpha": "0",
                                                            "bgcolor": "FF0000",
                                                            "basewidth": "6",
                                                            "topwidth": "6",
                                                            "radius": "120"
                                                        }]
                                                    },
                                                    "trendpoints": {
                                                        "point": [{
                                                            "startvalue": "0",
                                                            "displayvalue": "E",
                                                            "alpha": "0"
                                                        }, {"startvalue": "55", "displayvalue": "F", "alpha": "0"}]
                                                    },
                                                    "annotations": {
                                                        "groups": [{
                                                            "x": "100",
                                                            "y": "160",
                                                            "items": [{
                                                                "type": "arc",
                                                                "x": "0",
                                                                "y": "0",
                                                                "radius": "145",
                                                                "innerradius": "132",
                                                                "startangle": "53",
                                                                "endangle": "127",
                                                                "showborder": "1",
                                                                "bordercolor": "444444",
                                                                "borderthickness": "2"
                                                            }, {
                                                                "type": "arc",
                                                                "x": "0",
                                                                "y": "0",
                                                                "radius": "145",
                                                                "innerradius": "132",
                                                                "startangle": "53",
                                                                "endangle": "107",
                                                                "showborder": "1",
                                                                "color": "ffffff",
                                                                "bordercolor": "444444",
                                                                "borderthickness": "2"
                                                            }]
                                                        }, {
                                                            "x": "$chartCenterX",
                                                            "y": "160",
                                                            "showbelow": "1",
                                                            "scaleimages": "1",
                                                            "items": [{
                                                                "type": "image",
                                                                "x": "-12.5",
                                                                "y": "-100",
                                                                "url": "/demos/gallery/Resources/Fuel.gif"
                                                            }]
                                                        }]
                                                    }
                                                }
                                            }
                                        },
                                        {
                                            id: 'ood.UI.FC_agg11',
                                            key: 'ood.UI.FusionChartsXT',
                                            caption: '$(RAD.widgets.fc.Angular) 11',
                                            imageClass: 'ri ri-dashboard-line',
                                            draggable: true,
                                            iniProp: {
                                                chartType: "AngularGauge",
                                                JSONData: {
                                                    "chart": {
                                                        "manageresize": "1",
                                                        "origw": "330",
                                                        "origh": "170",
                                                        "basefontcolor": "FFFFFF",
                                                        "animation": "1",
                                                        "bgcolor": "000000",
                                                        "bgalpha": "100",
                                                        "lowerlimit": "0",
                                                        "upperlimit": "20",
                                                        "gaugestartangle": "180",
                                                        "gaugeendangle": "0",
                                                        "gaugeouterradius": "100",
                                                        "gaugeinnerradius": "90%",
                                                        "gaugeoriginx": "165",
                                                        "gaugeoriginy": "130",
                                                        "showtickvalues": "0",
                                                        "majortmnumber": "2",
                                                        "minortmnumber": "19",
                                                        "majortmcolor": "FFFFFF",
                                                        "minortmcolor": "EE0000",
                                                        "majortmheight": "15",
                                                        "majortmthickness": "2",
                                                        "minortmheight": "15",
                                                        "minortmthickness": "2",
                                                        "placevaluesinside": "1",
                                                        "pivotfillmix": "414340, 272727",
                                                        "pivotfillratio": "50,50",
                                                        "pivotborderthickness": "40",
                                                        "pivotbordercolor": "CCCCCC",
                                                        "pivotradius": "20",
                                                        "showshadow": "0",
                                                        "tooltipbgcolor": "000000"
                                                    },
                                                    "dials": {
                                                        "dial": [{
                                                            "radius": "120",
                                                            "basewidth": "12",
                                                            "topwidth": "1",
                                                            "color": "FF8F02,FFFFFF",
                                                            "fillratio": "90, 10",
                                                            "bordercolor": "FFFFFF",
                                                            "value": "7"
                                                        }]
                                                    },
                                                    "trendpoints": {
                                                        "point": [{
                                                            "startvalue": "3",
                                                            "color": "FFFFFF",
                                                            "alpha": "1",
                                                            "radius": "80",
                                                            "innerradius": "70",
                                                            "displayvalue": "E"
                                                        }, {
                                                            "startvalue": "17",
                                                            "color": "FFFFFF",
                                                            "alpha": "1",
                                                            "radius": "80",
                                                            "innerradius": "70",
                                                            "displayvalue": "F"
                                                        }]
                                                    },
                                                    "annotations": {
                                                        "groups": [{
                                                            "id": "Grp1",
                                                            "showbelow": "1",
                                                            "x": "165",
                                                            "y": "130",
                                                            "items": [{
                                                                "type": "arc",
                                                                "radius": "106",
                                                                "innerradius": "0",
                                                                "startangle": "0",
                                                                "endangle": "180",
                                                                "fillangle": "90",
                                                                "fillratio": "50,50",
                                                                "color": "606417, 000000",
                                                                "fillpattern": "linear",
                                                                "bordercolor": "FFFFFF",
                                                                "borderthickness": "2"
                                                            }, {
                                                                "type": "circle",
                                                                "radius": "21",
                                                                "fillalpha": "0",
                                                                "bordercolor": "FFFFFF",
                                                                "borderthickness": "2",
                                                                "borderalpha": "60"
                                                            }]
                                                        }]
                                                    }
                                                }
                                            }
                                        },
                                        {
                                            id: 'ood.UI.FC_agg12',
                                            key: 'ood.UI.FusionChartsXT',
                                            caption: '$(RAD.widgets.fc.Angular) 12',
                                            imageClass: 'ri ri-dashboard-line',
                                            imageClass: 'ri ri-dashboard-line',
                                            draggable: true,
                                            iniProp: {
                                                chartType: "AngularGauge",
                                                JSONData: {
                                                    "chart": {
                                                        "manageresize": "1",
                                                        "origw": "280",
                                                        "origh": "280",
                                                        "bgcolor": "FFFFFF",
                                                        "upperlimit": "180",
                                                        "lowerlimit": "0",
                                                        "majortmnumber": "7",
                                                        "majortmcolor": "AF9A03",
                                                        "majortmheight": "8",
                                                        "minortmnumber": "0",
                                                        "majortmthickness": "8",
                                                        "showgaugeborder": "0",
                                                        "gaugeouterradius": "100",
                                                        "gaugeoriginx": "140",
                                                        "gaugeoriginy": "140",
                                                        "gaugestartangle": "230",
                                                        "gaugeendangle": "-50",
                                                        "placevaluesinside": "1",
                                                        "gaugeinnerradius": "90",
                                                        "tickvaluedistance": "17",
                                                        "pivotradius": "12",
                                                        "pivotfillmix": "{AF9A03},{ffffff}",
                                                        "pivotbordercolor": "AF9A03",
                                                        "pivotborderthickness": "2",
                                                        "pivotfillratio": "50,50",
                                                        "pivotfilltype": "linear",
                                                        "showpivotborder": "1",
                                                        "showshadow": "0",
                                                        "showborder": "0"
                                                    },
                                                    "dials": {
                                                        "dial": [{
                                                            "value": "25",
                                                            "borderalpha": "0",
                                                            "bgcolor": "6A6FA6,AF9A03",
                                                            "basewidth": "4",
                                                            "topwidth": "4",
                                                            "radius": "93"
                                                        }]
                                                    },
                                                    "annotations": {
                                                        "groups": [{
                                                            "x": "140",
                                                            "y": "140",
                                                            "items": [{
                                                                "type": "circle",
                                                                "radius": "110",
                                                                "fillpattern": "linear",
                                                                "fillcolor": "eeeeee,ebce05,eeeeee",
                                                                "fillratio": "0,100,0",
                                                                "fillangle": "270",
                                                                "showborder": "1",
                                                                "bordercolor": "444444",
                                                                "borderthickness": "1"
                                                            }, {
                                                                "type": "circle",
                                                                "radius": "100",
                                                                "fillpattern": "linear",
                                                                "fillcolor": "ffffff,ebce05,eeeeee",
                                                                "fillalpha": "100,10,100",
                                                                "fillratio": "5,83,12",
                                                                "fillangle": "270"
                                                            }]
                                                        }]
                                                    }
                                                }
                                            }
                                        },
                                        {
                                            id: 'ood.UI.FC_agg13',
                                            key: 'ood.UI.FusionChartsXT',
                                            caption: '$(RAD.widgets.fc.Angular) 13',
                                            imageClass: 'ri ri-dashboard-line',
                                            imageClass: 'ri ri-dashboard-line',
                                            draggable: true,
                                            iniProp: {
                                                chartType: "AngularGauge",
                                                JSONData: {
                                                    "chart": {
                                                        "manageresize": "1",
                                                        "origw": "300",
                                                        "origh": "300",
                                                        "palette": "3",
                                                        "bgcolor": "333333, 453269",
                                                        "bgalpha": "100",
                                                        "lowerlimit": "0",
                                                        "upperlimit": "100",
                                                        "gaugestartangle": "240",
                                                        "gaugeendangle": "-60",
                                                        "gaugeouterradius": "120",
                                                        "gaugeinnerradius": "60%",
                                                        "gaugefillmix": "{light-10},{light-30},{light-20},{dark-5},{color},{light-30},{light-20},{dark-10}",
                                                        "gaugefillratio": "",
                                                        "basefontcolor": "FFFFFF",
                                                        "tooltipbgcolor": "333333",
                                                        "tooltipbordercolor": "333333",
                                                        "decimals": "1",
                                                        "gaugeoriginx": "150",
                                                        "gaugeoriginy": "150",
                                                        "showborder": "0"
                                                    },
                                                    "colorrange": {
                                                        "color": [{"minvalue": "0", "maxvalue": "30"}, {
                                                            "minvalue": "30",
                                                            "maxvalue": "50"
                                                        }, {"minvalue": "50", "maxvalue": "80"}, {
                                                            "minvalue": "80",
                                                            "maxvalue": "100"
                                                        }]
                                                    },
                                                    "dials": {
                                                        "dial": [{
                                                            "id": "Dial1",
                                                            "value": "60.2",
                                                            "basewidth": "6",
                                                            "topwidth": "1",
                                                            "editmode": "1",
                                                            "showvalue": "1",
                                                            "rearextension": "10",
                                                            "valuey": "270"
                                                        }, {
                                                            "id": "Dial2",
                                                            "value": "50.3",
                                                            "basewidth": "6",
                                                            "topwidth": "1",
                                                            "editmode": "1",
                                                            "rearextension": "10",
                                                            "showvalue": "1",
                                                            "valuey": "250"
                                                        }]
                                                    }
                                                }
                                            }
                                        },
                                        {
                                            id: 'ood.UI.FC_agg14',
                                            key: 'ood.UI.FusionChartsXT',
                                            caption: '$(RAD.widgets.fc.Angular) 14',
                                            imageClass: 'ri ri-dashboard-line',
                                            imageClass: 'ri ri-dashboard-line',
                                            draggable: true,
                                            iniProp: {
                                                chartType: "AngularGauge",
                                                JSONData: {
                                                    "chart": {
                                                        "manageresize": "1",
                                                        "origw": "320",
                                                        "origh": "320",
                                                        "tickvaluedistance": "-10",
                                                        "bgcolor": "FFFFFF",
                                                        "upperlimit": "240",
                                                        "lowerlimit": "0",
                                                        "basefontcolor": "FFFFFF",
                                                        "majortmnumber": "9",
                                                        "majortmcolor": "FFFFFF",
                                                        "majortmheight": "8",
                                                        "majortmthickness": "5",
                                                        "minortmnumber": "5",
                                                        "minortmcolor": "FFFFFF",
                                                        "minortmheight": "3",
                                                        "minortmthickness": "2",
                                                        "pivotradius": "10",
                                                        "pivotbgcolor": "000000",
                                                        "pivotbordercolor": "FFFFFF",
                                                        "pivotborderthickness": "2",
                                                        "tooltipbordercolor": "FFFFFF",
                                                        "tooltipbgcolor": "333333",
                                                        "gaugeouterradius": "135",
                                                        "gaugestartangle": "240",
                                                        "gaugeendangle": "-60",
                                                        "gaugealpha": "0",
                                                        "decimals": "0",
                                                        "showcolorrange": "0",
                                                        "placevaluesinside": "1",
                                                        "pivotfillmix": "",
                                                        "showpivotborder": "1",
                                                        "annrenderdelay": "0",
                                                        "gaugeoriginx": "160",
                                                        "gaugeoriginy": "160",
                                                        "showborder": "0"
                                                    },
                                                    "dials": {
                                                        "dial": [{
                                                            "value": "65",
                                                            "bgcolor": "000000",
                                                            "bordercolor": "FFFFFF",
                                                            "borderalpha": "100",
                                                            "basewidth": "4",
                                                            "topwidth": "4",
                                                            "borderthickness": "2",
                                                            "valuey": "260"
                                                        }]
                                                    },
                                                    "annotations": {
                                                        "groups": [{
                                                            "x": "160",
                                                            "y": "160",
                                                            "items": [{
                                                                "type": "circle",
                                                                "radius": "150",
                                                                "fillasgradient": "1",
                                                                "fillcolor": "4B4B4B,AAAAAA",
                                                                "fillalpha": "100,100",
                                                                "fillratio": "95,5"
                                                            }, {
                                                                "type": "circle",
                                                                "x": "0",
                                                                "y": "0",
                                                                "radius": "140",
                                                                "showborder": "1",
                                                                "bordercolor": "cccccc",
                                                                "fillasgradient": "1",
                                                                "fillcolor": "ffffff,000000",
                                                                "fillalpha": "50,100",
                                                                "fillratio": "1,99"
                                                            }]
                                                        }, {
                                                            "x": "160",
                                                            "y": "160",
                                                            "showbelow": "0",
                                                            "scaletext": "1",
                                                            "items": [{
                                                                "type": "text",
                                                                "y": "120",
                                                                "label": "KPH",
                                                                "fontcolor": "FFFFFF",
                                                                "fontsize": "14",
                                                                "bold": "1"
                                                            }]
                                                        }]
                                                    }
                                                }
                                            }
                                        },
                                        {
                                            id: 'ood.UI.FC_agg15',
                                            key: 'ood.UI.FusionChartsXT',
                                            caption: '$(RAD.widgets.fc.Angular) 15',
                                            imageClass: 'ri ri-dashboard-line',
                                            imageClass: 'ri ri-dashboard-line',
                                            draggable: true,
                                            iniProp: {
                                                chartType: "AngularGauge",
                                                JSONData: {
                                                    "chart": {
                                                        "manageresize": "1",
                                                        "origw": "270",
                                                        "origh": "270",
                                                        "upperlimit": "360",
                                                        "lowerlimit": "0",
                                                        "upperlimitdisplay": " ",
                                                        "lowerlimitdisplay": "Start",
                                                        "bgcolor": "FFFFFF",
                                                        "bgalpha": "0",
                                                        "showborder": "0",
                                                        "majortmnumber": "12",
                                                        "majortmheight": "9",
                                                        "minortmnumber": "5",
                                                        "minortmcolor": "000000",
                                                        "minortmheight": "3",
                                                        "majortmthickness": "2",
                                                        "gaugeinnerradius": "0",
                                                        "gaugeouterradius": "85",
                                                        "gaugestartangle": "180",
                                                        "gaugeendangle": "-180",
                                                        "charttopmargin": "0",
                                                        "chartleftmargin": "0",
                                                        "chartrightmargin": "0",
                                                        "chartbottommargin": "0",
                                                        "basefontcolor": "333333",
                                                        "decimals": "0",
                                                        "gaugeoriginx": "135",
                                                        "gaugeoriginy": "135",
                                                        "managevalueoverlapping": "1",
                                                        "autoaligntickvalues": "1",
                                                        "tickvaluedistance": "5"
                                                    },
                                                    "colorrange": {
                                                        "color": [{
                                                            "minvalue": "0",
                                                            "maxvalue": "120",
                                                            "code": "00B900",
                                                            "bordercolor": "00B900"
                                                        }, {
                                                            "minvalue": "120",
                                                            "maxvalue": "240",
                                                            "code": "FDC12E",
                                                            "bordercolor": "FDC12E"
                                                        }, {
                                                            "minvalue": "240",
                                                            "maxvalue": "360",
                                                            "code": "E95D0F",
                                                            "bordercolor": "E95D0F"
                                                        }]
                                                    },
                                                    "dials": {"dial": [{"value": "160", "radius": "85"}]},
                                                    "annotations": {
                                                        "groups": [{
                                                            "items": [{
                                                                "type": "rectangle",
                                                                "x": "$chartStartX+1",
                                                                "y": "$chartStartY+1",
                                                                "tox": "$chartEndX-1",
                                                                "toy": "$chartEndY-1",
                                                                "radius": "15",
                                                                "showborder": "1",
                                                                "fillcolor": "333333",
                                                                "bordercolor": "333333",
                                                                "borderthickness": "2"
                                                            }, {
                                                                "type": "rectangle",
                                                                "x": "$chartStartX+8",
                                                                "y": "$chartStartY+8",
                                                                "tox": "$chartEndX-8",
                                                                "toy": "$chartEndY-8",
                                                                "radius": "15",
                                                                "showborder": "1",
                                                                "fillcolor": "FFFFFF,009999,FFFFFF",
                                                                "fillangle": "45",
                                                                "fillalpha": "100,100,100",
                                                                "bordercolor": "333333"
                                                            }]
                                                        }]
                                                    }
                                                }
                                            }
                                        },
                                        {
                                            id: 'ood.UI.FC_agg16',
                                            key: 'ood.UI.FusionChartsXT',
                                            caption: '$(RAD.widgets.fc.Angular) 16',
                                            imageClass: 'ri ri-dashboard-line',
                                            imageClass: 'ri ri-dashboard-line',
                                            draggable: true,
                                            iniProp: {
                                                chartType: "AngularGauge",
                                                JSONData: {
                                                    "chart": {
                                                        "manageresize": "1",
                                                        "origw": "350",
                                                        "origh": "200",
                                                        "palette": "2",
                                                        "bgalpha": "0",
                                                        "bgcolor": "FFFFFF",
                                                        "lowerlimit": "0",
                                                        "upperlimit": "100",
                                                        "numbersuffix": "%",
                                                        "showborder": "0",
                                                        "basefontcolor": "FFFFDD",
                                                        "charttopmargin": "5",
                                                        "chartbottommargin": "5",
                                                        "tooltipbgcolor": "009999",
                                                        "gaugefillmix": "{dark-10},{light-70},{dark-10}",
                                                        "gaugefillratio": "3",
                                                        "pivotradius": "8",
                                                        "gaugeouterradius": "120",
                                                        "gaugeinnerradius": "70%",
                                                        "gaugeoriginx": "175",
                                                        "gaugeoriginy": "170",
                                                        "trendvaluedistance": "5",
                                                        "tickvaluedistance": "3",
                                                        "managevalueoverlapping": "1",
                                                        "autoaligntickvalues": "1"
                                                    },
                                                    "colorrange": {
                                                        "color": [{
                                                            "minvalue": "0",
                                                            "maxvalue": "45",
                                                            "code": "FF654F"
                                                        }, {
                                                            "minvalue": "45",
                                                            "maxvalue": "80",
                                                            "code": "F6BD0F"
                                                        }, {"minvalue": "80", "maxvalue": "100", "code": "8BBA00"}]
                                                    },
                                                    "dials": {
                                                        "dial": [{
                                                            "value": "72",
                                                            "rearextension": "10",
                                                            "basewidth": "10"
                                                        }]
                                                    },
                                                    "trendpoints": {
                                                        "point": [{
                                                            "startvalue": "62",
                                                            "displayvalue": "Average",
                                                            "usemarker": "1",
                                                            "markerradius": "8",
                                                            "dashed": "1",
                                                            "dashlen": "2",
                                                            "dashgap": "2"
                                                        }]
                                                    },
                                                    "annotations": {
                                                        "groups": [{
                                                            "id": "Grp1",
                                                            "showbelow": "1",
                                                            "showshadow": "1",
                                                            "items": [{
                                                                "type": "rectangle",
                                                                "x": "$chartStartX+5",
                                                                "y": "$chartStartY+5",
                                                                "tox": "$chartEndX-5",
                                                                "toy": "$chartEndY-5",
                                                                "radius": "10",
                                                                "fillcolor": "009999,333333",
                                                                "showborder": "0"
                                                            }]
                                                        }]
                                                    }
                                                }
                                            }
                                        },
                                        {
                                            id: 'ood.UI.FC_agg17',
                                            key: 'ood.UI.FusionChartsXT',
                                            caption: '$(RAD.widgets.fc.Angular) 17',
                                            imageClass: 'ri ri-dashboard-line',
                                            imageClass: 'ri ri-dashboard-line',
                                            draggable: true,
                                            iniProp: {
                                                chartType: "AngularGauge",
                                                JSONData: {
                                                    "chart": {
                                                        "manageresize": "1",
                                                        "origw": "250",
                                                        "origh": "250",
                                                        "palette": "4",
                                                        "lowerlimit": "0",
                                                        "upperlimit": "100",
                                                        "gaugestartangle": "220",
                                                        "gaugeendangle": "-40",
                                                        "numbersuffix": "%",
                                                        "bgcolor": "FFFFFF",
                                                        "showborder": "0",
                                                        "basefontcolor": "FFFFFF",
                                                        "gaugeouterradius": "80",
                                                        "gaugeinnerradius": "60",
                                                        "charttopmargin": "10",
                                                        "chartleftmargin": "5",
                                                        "tooltipbgcolor": "AEC0CA",
                                                        "tooltipbordercolor": "FFFFFF",
                                                        "pivotradius": "6",
                                                        "gaugeoriginx": "125",
                                                        "gaugeoriginy": "130",
                                                        "refreshinterval": "5"
                                                    },
                                                    "colorrange": {
                                                        "color": [{
                                                            "minvalue": "0",
                                                            "maxvalue": "99.99",
                                                            "code": "F6BD0F"
                                                        }, {
                                                            "minvalue": "99.99",
                                                            "maxvalue": "100",
                                                            "code": "F6BD0F",
                                                            "alpha": "0"
                                                        }]
                                                    },
                                                    "trendpoints": {
                                                        "point": [{
                                                            "startvalue": "70",
                                                            "endvalue": "100",
                                                            "color": "E10000",
                                                            "radius": "60",
                                                            "innerradius": "55",
                                                            "alpha": "70"
                                                        }]
                                                    },
                                                    "dials": {
                                                        "dial": [{
                                                            "value": "62",
                                                            "rearextension": "10",
                                                            "basewidth": "6"
                                                        }]
                                                    },
                                                    "annotations": {
                                                        "groups": [{
                                                            "id": "Grp1",
                                                            "showbelow": "1",
                                                            "items": [{
                                                                "type": "rectangle",
                                                                "x": "$chartStartX",
                                                                "y": "$chartStartY",
                                                                "tox": "$chartEndX",
                                                                "toy": "$chartEndY",
                                                                "radius": "10",
                                                                "fillcolor": "333333,555555,333333",
                                                                "fillangle": "90"
                                                            }, {
                                                                "type": "rectangle",
                                                                "x": "$chartStartX+5",
                                                                "y": "$chartStartY+5",
                                                                "tox": "$chartEndX-5",
                                                                "toy": "$chartEndY-5",
                                                                "radius": "10",
                                                                "fillcolor": "777777,C3D0D8,777777",
                                                                "fillangle": "90"
                                                            }, {
                                                                "type": "rectangle",
                                                                "x": "$chartStartX+10",
                                                                "y": "$chartStartY+10",
                                                                "tox": "$chartEndX-10",
                                                                "toy": "$chartEndY-10",
                                                                "radius": "10",
                                                                "fillcolor": "333333,ADB0B2,333333",
                                                                "fillangle": "180"
                                                            }]
                                                        }]
                                                    }
                                                }
                                            }
                                        }
                                    ]
                            },
                            {
                                id: 'ood.FusionChartsXT.10',
                                key: 'ood.FusionChartsXT.10',
                                caption: '标尺',
                                group: true,
                                imageClass: 'ri ri-arrow-left-right-line',
                                sub:
                                    [
                                        {
                                            id: 'ood.UI.FC_lgg01',
                                            key: 'ood.UI.FusionChartsXT',
                                            caption: '$(RAD.widgets.fc.Linear) 1',
                                            imageClass: 'ri ri-arrow-left-right-line',
                                            imageClass: 'ri ri-arrow-left-right-line',
                                            draggable: true,
                                            iniProp: {
                                                chartType: "HLinearGauge",
                                                JSONData: {
                                                    "chart": {
                                                        "caption": "Server CPU Utilization",
                                                        "subcaption": "food.hsm.com",
                                                        "captionFontColor": "#000000",
                                                        "subcaptionFontBold": "0",
                                                        "bgColor": "#ffffff",
                                                        "showBorder": "0",
                                                        "lowerLimit": "0",
                                                        "upperLimit": "100",
                                                        "numberSuffix": "%",
                                                        "valueAbovePointer": "0",
                                                        "showShadow": "0",
                                                        "gaugeFillMix": "{light}",
                                                        "valueBgColor": "#ffffff",
                                                        "valueBgAlpha": "60",
                                                        "valueFontColor": "#000000",
                                                        "pointerBgColor": "#ffffff",
                                                        "pointerBgAlpha": "50",
                                                        "baseFontColor": "#ffffff"
                                                    },
                                                    "colorRange": {
                                                        "color": [{
                                                            "minValue": "0",
                                                            "maxValue": "35",
                                                            "label": "Low",
                                                            "code": "#1aaf5d"
                                                        }, {
                                                            "minValue": "35",
                                                            "maxValue": "70",
                                                            "label": "Moderate",
                                                            "code": "#f2c500"
                                                        }, {
                                                            "minValue": "70",
                                                            "maxValue": "100",
                                                            "label": "High",
                                                            "code": "#c02d00"
                                                        }]
                                                    },
                                                    "pointers": {"pointer": [{"value": "72.5"}]}
                                                }
                                            }
                                        },
                                        {
                                            id: 'ood.UI.FC_lgg02',
                                            key: 'ood.UI.FusionChartsXT',
                                            caption: '$(RAD.widgets.fc.Linear) 2',
                                            imageClass: 'ri ri-arrow-left-right-line',
                                            imageClass: 'ri ri-arrow-left-right-line',
                                            draggable: true,
                                            iniProp: {
                                                chartType: "HLinearGauge",
                                                JSONData: {
                                                    "chart": {
                                                        "manageresize": "1",
                                                        "bgcolor": "FFFFFF",
                                                        "bgalpha": "0",
                                                        "showborder": "0",
                                                        "upperlimit": "100",
                                                        "lowerlimit": "0",
                                                        "gaugeroundradius": "5",
                                                        "chartbottommargin": "10",
                                                        "ticksbelowgauge": "0",
                                                        "showgaugelabels": "1",
                                                        "valueabovepointer": "0",
                                                        "pointerontop": "1",
                                                        "pointerradius": "9",
                                                        "numberprefix": "$"
                                                    },
                                                    "colorrange": {
                                                        "color": [{
                                                            "minvalue": "0",
                                                            "maxvalue": "35",
                                                            "label": "Low"
                                                        }, {
                                                            "minvalue": "35",
                                                            "maxvalue": "70",
                                                            "label": "Moderate"
                                                        }, {"minvalue": "70", "maxvalue": "100", "label": "High"}]
                                                    },
                                                    "pointers": {"pointer": [{"value": "27.5"}]}
                                                }
                                            }
                                        },
                                        {
                                            id: 'ood.UI.FC_lgg03',
                                            key: 'ood.UI.FusionChartsXT',
                                            caption: '$(RAD.widgets.fc.Linear) 3',
                                            imageClass: 'ri ri-arrow-left-right-line',
                                            imageClass: 'ri ri-arrow-left-right-line',
                                            draggable: true,
                                            iniProp: {
                                                chartType: "HLinearGauge",
                                                JSONData: {
                                                    "chart": {
                                                        "manageresize": "1",
                                                        "origw": "380",
                                                        "origh": "80",
                                                        "animation": "0",
                                                        "showshadow": "0",
                                                        "upperlimit": "9",
                                                        "lowerlimit": "0",
                                                        "charttopmargin": "5",
                                                        "chartbottommargin": "10",
                                                        "chartleftmargin": "30",
                                                        "chartrightmargin": "30",
                                                        "placeticksinside": "1",
                                                        "placevaluesinside": "1",
                                                        "pointerontop": "0",
                                                        "ticksbelowgauge": "0",
                                                        "valuepadding": "0",
                                                        "minortmheight": "4",
                                                        "majortmheight": "8",
                                                        "majortmcolor": "0B0D0F",
                                                        "majortmnumber": "10",
                                                        "majortmthickness": ".5",
                                                        "minortmnumber": "4",
                                                        "minortmthickness": ".5",
                                                        "pointerradius": "5",
                                                        "pointerbgcolor": "E00000",
                                                        "pointerbordercolor": "E00000",
                                                        "showgaugeborder": "0",
                                                        "basefontcolor": "FFFFFF",
                                                        "bgcolor": "004D69",
                                                        "bgalpha": "100"
                                                    },
                                                    "pointers": {"pointer": [{"value": "6.7"}]},
                                                    "annotations": {
                                                        "groups": [{
                                                            "id": "Grp1",
                                                            "showbelow": "1",
                                                            "x": "0",
                                                            "y": "0",
                                                            "items": [{
                                                                "type": "rectangle",
                                                                "x": "$gaugeStartX-17",
                                                                "y": "$gaugeStartY+2",
                                                                "tox": "$gaugeEndX+17",
                                                                "toy": "$chartEndY-8",
                                                                "radius": "8",
                                                                "color": "004D69"
                                                            }, {
                                                                "type": "rectangle",
                                                                "x": "$gaugeStartX-17",
                                                                "y": "$gaugeStartY+2",
                                                                "tox": "$gaugeEndX+17",
                                                                "toy": "$gaugeEndY+3",
                                                                "radius": "8",
                                                                "color": "09DBFE,32A6CF,0177A7",
                                                                "fillratio": "20,40,40",
                                                                "fillangle": "90"
                                                            }, {
                                                                "type": "rectangle",
                                                                "x": "$gaugeStartX-15",
                                                                "y": "$gaugeStartY+4",
                                                                "tox": "$gaugeEndX+15",
                                                                "toy": "$gaugeEndY+1",
                                                                "radius": "8",
                                                                "color": "09DBFE,32A6CF,C1DFEA",
                                                                "fillratio": "20,40,40",
                                                                "fillangle": "90"
                                                            }, {
                                                                "type": "rectangle",
                                                                "x": "$gaugeStartX-17",
                                                                "y": "$gaugeEndY+5",
                                                                "tox": "$gaugeEndX+17",
                                                                "toy": "$chartEndY-8",
                                                                "radius": "8",
                                                                "color": "055472,1D89AF",
                                                                "fillangle": "90"
                                                            }]
                                                        }, {
                                                            "id": "Grp2",
                                                            "showbelow": "1",
                                                            "x": "190",
                                                            "y": "$gaugeStartY+36",
                                                            "items": [{
                                                                "type": "text",
                                                                "label": "Richter Scale",
                                                                "color": "004D69",
                                                                "bold": "1",
                                                                "x": "0",
                                                                "y": "0"
                                                            }]
                                                        }]
                                                    }
                                                }
                                            }
                                        },
                                        {
                                            id: 'ood.UI.FC_lgg04',
                                            key: 'ood.UI.FusionChartsXT',
                                            caption: '$(RAD.widgets.fc.Linear) 4',
                                            imageClass: 'ri ri-arrow-left-right-line',
                                            imageClass: 'ri ri-arrow-left-right-line',
                                            draggable: true,
                                            iniProp: {
                                                chartType: "HLinearGauge",
                                                JSONData: {
                                                    "chart": {
                                                        "manageresize": "1",
                                                        "origw": "450",
                                                        "orih": "125",
                                                        "editmode": "1",
                                                        "bgcolor": "FFFFFF",
                                                        "bgalpha": "0",
                                                        "showborder": "0",
                                                        "upperlimit": "1000",
                                                        "lowerlimit": "0",
                                                        "numberprefix": "$",
                                                        "gaugeroundradius": "5",
                                                        "chartbottommargin": "30",
                                                        "ticksbelowgauge": "0",
                                                        "placeticksinside": "0",
                                                        "showgaugelabels": "0",
                                                        "pointerontop": "1",
                                                        "pointerradius": "14",
                                                        "chartleftmargin": "25",
                                                        "chartrightmargin": "30",
                                                        "majortmcolor": "868F9B",
                                                        "majortmheight": "10",
                                                        "majortmthickness": "2",
                                                        "pointerbgalpha": "0",
                                                        "pointerborderthickness": "2",
                                                        "majortmnumber": "0",
                                                        "minortmnumber": "0",
                                                        "showtooltip": "0",
                                                        "decimals": "0"
                                                    },
                                                    "colorrange": {
                                                        "color": [{
                                                            "minvalue": "0",
                                                            "maxvalue": "1000",
                                                            "code": "F6BD0F"
                                                        }]
                                                    },
                                                    "pointers": {"pointer": [{"value": "665"}]},
                                                    "trendpoints": {
                                                        "point": [{
                                                            "startvalue": "350",
                                                            "fontcolor": "FF4400",
                                                            "usemarker": "0",
                                                            "dashed": "1",
                                                            "dashlen": "1",
                                                            "dashgap": "3",
                                                            "markerradius": "5",
                                                            "color": "FF654F",
                                                            "alpha": "100",
                                                            "thickness": "2"
                                                        }, {
                                                            "startvalue": "800",
                                                            "fontcolor": "FF4400",
                                                            "usemarker": "0",
                                                            "dashed": "1",
                                                            "dashlen": "1",
                                                            "dashgap": "3",
                                                            "markerradius": "5",
                                                            "color": "8BBA00",
                                                            "alpha": "100",
                                                            "thickness": "2"
                                                        }]
                                                    },
                                                    "annotations": {
                                                        "groups": [{
                                                            "id": "Grp1",
                                                            "showbelow": "1",
                                                            "items": [{
                                                                "type": "rectangle",
                                                                "x": "$chartStartX+2",
                                                                "y": "$chartStartY+2",
                                                                "tox": "$chartEndX-5",
                                                                "toy": "$chartEndY-5",
                                                                "radius": "10",
                                                                "fillcolor": "D6E0F6",
                                                                "fillangle": "90",
                                                                "bordercolor": "868F9B",
                                                                "borderthickness": "2"
                                                            }]
                                                        }]
                                                    }
                                                }
                                            }
                                        },
                                        {
                                            id: 'ood.UI.FC_lgg05',
                                            key: 'ood.UI.FusionChartsXT',
                                            caption: '$(RAD.widgets.fc.Linear) 5',
                                            imageClass: 'ri ri-arrow-left-right-line',
                                            imageClass: 'ri ri-arrow-left-right-line',
                                            draggable: true,
                                            iniProp: {
                                                chartType: "HLinearGauge",
                                                JSONData: {
                                                    "chart": {
                                                        "manageresize": "1",
                                                        "origw": "450",
                                                        "orih": "120",
                                                        "bgcolor": "FFFFFF",
                                                        "bgalpha": "0",
                                                        "showborder": "0",
                                                        "upperlimit": "100",
                                                        "lowerlimit": "0",
                                                        "numbersuffix": "%",
                                                        "gaugeroundradius": "5",
                                                        "ticksbelowgauge": "1",
                                                        "placevaluesinside": "0",
                                                        "showgaugelabels": "0",
                                                        "valueabovepointer": "1",
                                                        "pointerontop": "1",
                                                        "pointerradius": "6",
                                                        "charttopmargin": "15",
                                                        "chartbottommargin": "15",
                                                        "chartleftmargin": "25",
                                                        "chartrightmargin": "30",
                                                        "majortmcolor": "666666"
                                                    },
                                                    "colorrange": {
                                                        "color": [{
                                                            "minvalue": "0",
                                                            "maxvalue": "100",
                                                            "code": "F6BD0F"
                                                        }]
                                                    },
                                                    "pointers": {"pointer": [{"value": "65"}]},
                                                    "trendpoints": {
                                                        "point": [{
                                                            "startvalue": "75",
                                                            "displayvalue": "Target",
                                                            "dashed": "1",
                                                            "dashlen": "1",
                                                            "dashgap": "3",
                                                            "color": "FFFFFF",
                                                            "thickness": "2"
                                                        }]
                                                    },
                                                    "annotations": {
                                                        "groups": [{
                                                            "id": "Grp1",
                                                            "showbelow": "1",
                                                            "items": [{
                                                                "type": "rectangle",
                                                                "x": "$chartStartX",
                                                                "y": "$chartStartY",
                                                                "tox": "$chartEndX",
                                                                "toy": "$chartEndY",
                                                                "radius": "10",
                                                                "fillcolor": "AEC0CA, 333333, AEC0CA",
                                                                "fillangle": "90"
                                                            }, {
                                                                "type": "rectangle",
                                                                "x": "$chartStartX+5",
                                                                "y": "$chartStartY+5",
                                                                "tox": "$chartEndX-5",
                                                                "toy": "$chartEndY-5",
                                                                "radius": "10",
                                                                "fillcolor": "333333, C3D0D8, 333333",
                                                                "fillangle": "90"
                                                            }, {
                                                                "type": "rectangle",
                                                                "x": "$chartStartX+10",
                                                                "y": "$chartStartY+10",
                                                                "tox": "$chartEndX-10",
                                                                "toy": "$chartEndY-10",
                                                                "radius": "10",
                                                                "fillcolor": "C4D5DC, A3A5A4",
                                                                "fillangle": "180"
                                                            }]
                                                        }]
                                                    }
                                                }
                                            }
                                        },
                                        {
                                            id: 'ood.UI.FC_lgg06',
                                            key: 'ood.UI.FusionChartsXT',
                                            caption: '$(RAD.widgets.fc.Linear) 6',
                                            imageClass: 'ri ri-arrow-left-right-line',
                                            draggable: true,
                                            iniProp: {
                                                chartType: "HLinearGauge",
                                                JSONData: {
                                                    "chart": {
                                                        "manageresize": "1",
                                                        "origw": "420",
                                                        "origh": "90",
                                                        "lowerlimit": "0",
                                                        "upperlimit": "100",
                                                        "numbersuffix": "%",
                                                        "showborder": "0",
                                                        "bgcolor": "FFFFFF",
                                                        "ticksbelowgauge": "1",
                                                        "valuepadding": "0",
                                                        "gaugefillmix": "",
                                                        "showgaugeborder": "0",
                                                        "pointerontop": "0",
                                                        "pointerradius": "5",
                                                        "pointerbordercolor": "000000",
                                                        "pointerbgcolor": "000000",
                                                        "annrenderdelay": "0",
                                                        "showshadow": "0",
                                                        "minortmnumber": "0",
                                                        "basefontcolor": "000000",
                                                        "animation": "0"
                                                    },
                                                    "colorrange": {
                                                        "color": [{
                                                            "minvalue": "0",
                                                            "maxvalue": "100",
                                                            "alpha": "0"
                                                        }]
                                                    },
                                                    "pointers": {"pointer": [{"value": "62"}]},
                                                    "annotations": {
                                                        "groups": [{
                                                            "id": "Grp1",
                                                            "showbelow": "0",
                                                            "x": "$chartCenterX",
                                                            "y": "-765",
                                                            "items": [{
                                                                "type": "circle",
                                                                "radius": "800",
                                                                "color": "FFFFFF"
                                                            }]
                                                        }, {
                                                            "id": "Grp2",
                                                            "showbelow": "1",
                                                            "items": [{
                                                                "type": "rectangle",
                                                                "x": "$gaugeStartX",
                                                                "y": "$gaugeStartY",
                                                                "tox": "$gaugeEndX",
                                                                "toy": "$gaugeEndY",
                                                                "fillcolor": "678000,FCEF27,E00000"
                                                            }]
                                                        }, {
                                                            "id": "Grp3",
                                                            "showbelow": "0",
                                                            "items": [{
                                                                "type": "text",
                                                                "x": "$gaugeStartX+25",
                                                                "y": "40",
                                                                "size": "10",
                                                                "color": "FFFFFF",
                                                                "bold": "1",
                                                                "label": "Good"
                                                            }, {
                                                                "type": "text",
                                                                "x": "$gaugeEndX-25",
                                                                "y": "40",
                                                                "size": "10",
                                                                "color": "FFFFFF",
                                                                "bold": "1",
                                                                "label": "Bad"
                                                            }]
                                                        }]
                                                    }
                                                }
                                            }
                                        },
                                        {
                                            id: 'ood.UI.FC_lgg07',
                                            key: 'ood.UI.FusionChartsXT',
                                            caption: '$(RAD.widgets.fc.Linear) 7',
                                            imageClass: 'ri ri-arrow-left-right-line',
                                            draggable: true,
                                            iniProp: {
                                                chartType: "HLinearGauge",
                                                JSONData: {
                                                    "chart": {
                                                        "manageresize": "1",
                                                        "bgcolor": "FFFFFF",
                                                        "bordercolor": "DCCEA1",
                                                        "charttopmargin": "0",
                                                        "chartbottommargin": "0",
                                                        "upperlimit": "100",
                                                        "lowerlimit": "0",
                                                        "ticksbelowgauge": "1",
                                                        "tickmarkdistance": "3",
                                                        "valuepadding": "-2",
                                                        "pointerradius": "5",
                                                        "majortmcolor": "000000",
                                                        "majortmnumber": "3",
                                                        "minortmnumber": "4",
                                                        "minortmheight": "4",
                                                        "majortmheight": "8",
                                                        "showshadow": "0",
                                                        "pointerbgcolor": "FFFFFF",
                                                        "pointerbordercolor": "000000",
                                                        "gaugeborderthickness": "3",
                                                        "basefontcolor": "000000",
                                                        "gaugefillmix": "{color},{FFFFFF}",
                                                        "gaugefillratio": "50,50",
                                                        "showborder": "0"
                                                    },
                                                    "colorrange": {
                                                        "color": [{
                                                            "minvalue": "0",
                                                            "maxvalue": "60",
                                                            "code": "B40001",
                                                            "bordercolor": "B40001",
                                                            "label": "Existing"
                                                        }, {
                                                            "minvalue": "60",
                                                            "maxvalue": "100",
                                                            "code": "5C8F0E",
                                                            "label": "Proposed"
                                                        }]
                                                    },
                                                    "pointers": {"pointer": [{"value": "60"}]}
                                                }
                                            }
                                        }
                                    ]
                            },
                            {
                                id: 'ood.FusionChartsXT.11',
                                key: 'ood.FusionChartsXT.11',
                                caption: '水平LED',
                                group: true,
                                imageClass: 'ri ri-arrow-left-right-line',
                                sub:
                                    [
                                        {
                                            id: 'ood.UI.FC_cgg01',
                                            key: 'ood.UI.FusionChartsXT',
                                            caption: '$(RAD.widgets.fc.Cylinder) 1',
                                            imageClass: 'ri ri-line-chart-line',
                                            draggable: true,
                                            iniProp: {
                                                chartType: "Cylinder",
                                                JSONData: {
                                                    "chart": {
                                                        "caption": "Fuel Meter",
                                                        "subcaption": "Diesel level in generator Bakersfield Central",
                                                        "subcaptionFontBold": "0",
                                                        "lowerLimit": "0",
                                                        "upperLimit": "120",
                                                        "lowerLimitDisplay": "Empty",
                                                        "upperLimitDisplay": "Full",
                                                        "numberSuffix": " ltrs",
                                                        "showValue": "0",
                                                        "showhovereffect": "1",
                                                        "bgCOlor": "#ffffff",
                                                        "borderAlpha": "0",
                                                        "cylFillColor": "#008ee4"
                                                    }, "value": "110"
                                                }
                                            }
                                        },
                                        {
                                            id: 'ood.UI.FC_cgg02',
                                            key: 'ood.UI.FusionChartsXT',
                                            caption: '$(RAD.widgets.fc.Cylinder) 2',
                                            imageClass: 'ri ri-line-chart-line',
                                            draggable: true,
                                            iniProp: {
                                                chartType: "Cylinder",
                                                JSONData: {
                                                    "chart": {
                                                        "manageresize": "1",
                                                        "bgcolor": "FFFFFF",
                                                        "bgalpha": "0",
                                                        "showborder": "0",
                                                        "lowerlimit": "0",
                                                        "upperlimit": "100",
                                                        "showtickmarks": "0",
                                                        "showtickvalues": "0",
                                                        "showlimits": "0",
                                                        "numbersuffix": "%",
                                                        "decmials": "0",
                                                        "cylfillcolor": "CC0000",
                                                        "basefontcolor": "CC0000",
                                                        "chartleftmargin": "15",
                                                        "chartrightmargin": "15",
                                                        "charttopmargin": "15"
                                                    },
                                                    "value": "44",
                                                    "annotations": {
                                                        "groups": [{
                                                            "showbelow": "1",
                                                            "items": [{
                                                                "type": "rectangle",
                                                                "x": "$chartStartX+1",
                                                                "y": "$chartStartY+1",
                                                                "tox": "$chartEndX-1",
                                                                "toy": "$chartEndY-1",
                                                                "color": "FFFFFF",
                                                                "alpha": "100",
                                                                "showborder": "0",
                                                                "bordercolor": "CC0000",
                                                                "borderthickness": "2",
                                                                "radius": "10"
                                                            }]
                                                        }]
                                                    }
                                                }
                                            }
                                        },
                                        {
                                            id: 'ood.UI.FC_cgg03',
                                            key: 'ood.UI.FusionChartsXT',
                                            caption: '$(RAD.widgets.fc.Cylinder) 3',
                                            imageClass: 'ri ri-line-chart-line',
                                            draggable: true,
                                            iniProp: {
                                                chartType: "Cylinder",
                                                JSONData: {
                                                    "chart": {
                                                        "manageresize": "1",
                                                        "upperlimit": "100",
                                                        "lowerlimit": "0",
                                                        "tickmarkgap": "5",
                                                        "numbersuffix": "%",
                                                        "showborder": "0"
                                                    }, "value": "32"
                                                }
                                            }
                                        },
                                        {
                                            id: 'ood.UI.FC_cgg04',
                                            key: 'ood.UI.FusionChartsXT',
                                            caption: '$(RAD.widgets.fc.Cylinder) 4',
                                            imageClass: 'ri ri-line-chart-line',
                                            draggable: true,
                                            iniProp: {
                                                chartType: "Cylinder",
                                                JSONData: {
                                                    "chart": {
                                                        "manageresize": "1",
                                                        "bgcolor": "F1F1FD",
                                                        "lowerlimit": "0",
                                                        "upperlimit": "5000",
                                                        "showtickmarks": "0",
                                                        "numbersuffix": " Ltrs",
                                                        "tickvaluedistance": "20",
                                                        "decmials": "0",
                                                        "tickmarkdecmials": "0",
                                                        "cylfillcolor": "996633",
                                                        "cylradius": "45",
                                                        "showvalue": "0",
                                                        "showborder": "0"
                                                    },
                                                    "value": "3650",
                                                    "annotations": {
                                                        "groups": [{
                                                            "x": "120",
                                                            "y": "60",
                                                            "scaletext": "1",
                                                            "items": [{
                                                                "type": "rectangle",
                                                                "x": "0",
                                                                "y": "0",
                                                                "tox": "160",
                                                                "toy": "60",
                                                                "radius": "0",
                                                                "fillcolor": "333333",
                                                                "fillalpha": "5"
                                                            }, {
                                                                "type": "line",
                                                                "x": "0",
                                                                "y": "0",
                                                                "toy": "60",
                                                                "color": "333333",
                                                                "thickness": "2"
                                                            }, {
                                                                "type": "line",
                                                                "x": "160",
                                                                "y": "0",
                                                                "toy": "60",
                                                                "color": "333333",
                                                                "thickness": "2"
                                                            }, {
                                                                "type": "line",
                                                                "x": "0",
                                                                "y": "0",
                                                                "tox": "5",
                                                                "color": "333333",
                                                                "thickness": "2"
                                                            }, {
                                                                "type": "line",
                                                                "x": "0",
                                                                "y": "60",
                                                                "tox": "5",
                                                                "color": "333333",
                                                                "thickness": "2"
                                                            }, {
                                                                "type": "line",
                                                                "x": "155",
                                                                "y": "0",
                                                                "tox": "160",
                                                                "color": "333333",
                                                                "thickness": "2"
                                                            }, {
                                                                "type": "line",
                                                                "x": "155",
                                                                "y": "60",
                                                                "tox": "160",
                                                                "color": "333333",
                                                                "thickness": "2"
                                                            }, {
                                                                "type": "text",
                                                                "label": "Fuel left in tanker",
                                                                "font": "Verdana",
                                                                "x": "25",
                                                                "y": "5",
                                                                "align": "left",
                                                                "valign": "bottom",
                                                                "fontcolor": "333333",
                                                                "fontsize": "10",
                                                                "isbold": "1"
                                                            }, {
                                                                "type": "text",
                                                                "label": "(expressed in ltrs)",
                                                                "font": "Verdana",
                                                                "x": "24",
                                                                "y": "20",
                                                                "align": "left",
                                                                "valign": "bottom",
                                                                "fontcolor": "333333",
                                                                "fontsize": "10"
                                                            }, {
                                                                "type": "text",
                                                                "label": "3650",
                                                                "font": "Verdana",
                                                                "x": "25",
                                                                "y": "35",
                                                                "align": "left",
                                                                "valign": "bottom",
                                                                "fontcolor": "333333",
                                                                "fontsize": "10",
                                                                "isbold": "1"
                                                            }]
                                                        }]
                                                    }
                                                }
                                            }
                                        }
                                    ]
                            },
                            {
                                id: 'ood.FusionChartsXT.12',
                                key: 'ood.FusionChartsXT.12',
                                caption: '$RAD.widgets.group.HLinearGauge',
                                group: true,
                                imageClass: 'ri ri-arrow-left-right-line',
                                sub:
                                    [
                                        {
                                            id: 'ood.UI.FC_ledgg01',
                                            key: 'ood.UI.FusionChartsXT',
                                            caption: '$(RAD.widgets.fc.HLED) 1',
                                            imageClass: 'ri ri-bar-chart-2-line',
                                            draggable: true,
                                            iniProp: {
                                                chartType: "HLED",
                                                JSONData: {
                                                    "chart": {
                                                        "manageresize": "1",
                                                        "chartbottommargin": "5",
                                                        "lowerlimit": "0",
                                                        "upperlimit": "100",
                                                        "lowerlimitdisplay": "Low",
                                                        "upperlimitdisplay": "High",
                                                        "numbersuffix": "%",
                                                        "showtickmarks": "1",
                                                        "tickvaluedistance": "0",
                                                        "majortmnumber": "5",
                                                        "majortmheight": "4",
                                                        "minortmnumber": "0",
                                                        "showtickvalues": "1",
                                                        "decimals": "0",
                                                        "ledgap": "1",
                                                        "ledsize": "1",
                                                        "ledboxbgcolor": "333333",
                                                        "ledbordercolor": "666666",
                                                        "borderthickness": "2",
                                                        "chartrightmargin": "20",
                                                        "showborder": "0"
                                                    },
                                                    "colorrange": {
                                                        "color": [{
                                                            "minvalue": "0",
                                                            "maxvalue": "30",
                                                            "code": "FF0000"
                                                        }, {
                                                            "minvalue": "30",
                                                            "maxvalue": "50",
                                                            "code": "FFFF00"
                                                        }, {"minvalue": "50", "maxvalue": "100", "code": "00FF00"}]
                                                    },
                                                    "value": "70"
                                                }
                                            }
                                        },
                                        {
                                            id: 'ood.UI.FC_ledgg02',
                                            key: 'ood.UI.FusionChartsXT',
                                            caption: '$(RAD.widgets.fc.HLED) 2',
                                            imageClass: 'ri ri-bar-chart-2-line',
                                            draggable: true,
                                            iniProp: {
                                                chartType: "HLED",
                                                JSONData: {
                                                    "chart": {
                                                        "manageresize": "1",
                                                        "lowerlimit": "0",
                                                        "upperlimit": "100",
                                                        "showtickmarks": "1",
                                                        "numbersuffix": "%",
                                                        "decimals": "0",
                                                        "ledgap": "0",
                                                        "ledsize": "2",
                                                        "ledboxbgcolor": "333333",
                                                        "ledbordercolor": "666666",
                                                        "borderthickness": "5",
                                                        "chartleftmargin": "25",
                                                        "chartrightmargin": "25",
                                                        "basefontcolor": "333333",
                                                        "ticksbelowgauge": "0",
                                                        "showborder": "0"
                                                    },
                                                    "colorrange": {
                                                        "color": [{
                                                            "minvalue": "0",
                                                            "maxvalue": "30",
                                                            "code": "00cccc"
                                                        }, {"minvalue": "50", "maxvalue": "100", "code": "0066ff"}]
                                                    },
                                                    "value": "70"
                                                }
                                            }
                                        },
                                        {
                                            id: 'ood.UI.FC_ledgg03',
                                            key: 'ood.UI.FusionChartsXT',
                                            caption: '$(RAD.widgets.fc.HLED) 3',
                                            imageClass: 'ri ri-bar-chart-2-line',
                                            draggable: true,
                                            iniProp: {
                                                chartType: "HLED",
                                                JSONData: {
                                                    "chart": {
                                                        "manageresize": "1",
                                                        "lowerlimit": "0",
                                                        "upperlimit": "120",
                                                        "lowerlimitdisplay": "Low",
                                                        "upperlimitdisplay": "High",
                                                        "palette": "4",
                                                        "numbersuffix": "dB",
                                                        "chartrightmargin": "20",
                                                        "chartleftmargin": "20",
                                                        "ledsize": "5",
                                                        "ledgap": "5",
                                                        "ticksbelowgauge": "0",
                                                        "showborder": "0"
                                                    },
                                                    "colorrange": {
                                                        "color": [{
                                                            "minvalue": "0",
                                                            "maxvalue": "60",
                                                            "code": "FF0000"
                                                        }, {
                                                            "minvalue": "60",
                                                            "maxvalue": "90",
                                                            "code": "FFFF00"
                                                        }, {"minvalue": "90", "maxvalue": "120", "code": "00FF00"}]
                                                    },
                                                    "value": "102"
                                                }
                                            }
                                        },
                                        {
                                            id: 'ood.UI.FC_ledgg04',
                                            key: 'ood.UI.FusionChartsXT',
                                            caption: '$(RAD.widgets.fc.HLED) 4',
                                            imageClass: 'ri ri-bar-chart-2-line',
                                            draggable: true,
                                            iniProp: {
                                                chartType: "HLED",
                                                JSONData: {
                                                    "chart": {
                                                        "manageresize": "1",
                                                        "palette": "3",
                                                        "lowerlimit": "0",
                                                        "upperlimit": "120",
                                                        "majortmcolor": "333333",
                                                        "majortmalpha": "100",
                                                        "majortmheight": "10",
                                                        "majortmthickness": "2",
                                                        "minortmcolor": "666666",
                                                        "minortmalpha": "100",
                                                        "minortmheight": "7",
                                                        "usesamefillcolor": "1",
                                                        "showborder": "0"
                                                    },
                                                    "colorrange": {
                                                        "color": [{
                                                            "minvalue": "0",
                                                            "maxvalue": "60",
                                                            "code": "FF0000"
                                                        }, {
                                                            "minvalue": "60",
                                                            "maxvalue": "90",
                                                            "code": "FFFF00"
                                                        }, {"minvalue": "90", "maxvalue": "120", "code": "00FF00"}]
                                                    },
                                                    "value": "52"
                                                }
                                            }
                                        },
                                        {
                                            id: 'ood.UI.FC_ledgg05',
                                            key: 'ood.UI.FusionChartsXT',
                                            caption: '$(RAD.widgets.fc.VLED) 1',
                                            imageClass: 'ri ri-pie-chart-line',
                                            draggable: true,
                                            iniProp: {
                                                chartType: "VLED",
                                                JSONData: {
                                                    "chart": {
                                                        "manageresize": "1",
                                                        "origw": "240",
                                                        "origh": "250",
                                                        "upperlimit": "100",
                                                        "lowerlimit": "0",
                                                        "numbersuffix": "%",
                                                        "majortmnumber": "11",
                                                        "majortmcolor": "646F8F",
                                                        "majortmheight": "9",
                                                        "minortmnumber": "2",
                                                        "minortmcolor": "646F8F",
                                                        "minortmheight": "3",
                                                        "majortmthickness": "1",
                                                        "decmials": "0",
                                                        "ledgap": "2",
                                                        "ledsize": "2",
                                                        "annrenderdelay": "1.7",
                                                        "showborder": "0"
                                                    },
                                                    "colorrange": {
                                                        "color": [{
                                                            "minvalue": "0",
                                                            "maxvalue": "20",
                                                            "code": "00dd00"
                                                        }]
                                                    },
                                                    "annotations": {
                                                        "groups": [{
                                                            "id": "GRP1",
                                                            "showbelow": "0",
                                                            "x": "$gaugeCenterX",
                                                            "constrainedscale": "0",
                                                            "items": [{
                                                                "type": "line",
                                                                "y": "$gaugeStartY+1",
                                                                "toy": "$gaugeEndY-1",
                                                                "color": "000000",
                                                                "linethickness": "3"
                                                            }]
                                                        }]
                                                    },
                                                    "value": "45"
                                                }
                                            }
                                        },
                                        {
                                            id: 'ood.UI.FC_ledgg06',
                                            key: 'ood.UI.FusionChartsXT',
                                            caption: '$(RAD.widgets.fc.VLED) 2',
                                            imageClass: 'ri ri-pie-chart-line',
                                            draggable: true,
                                            iniProp: {
                                                chartType: "VLED",
                                                JSONData: {
                                                    "chart": {
                                                        "manageresize": "1",
                                                        "upperlimit": "100",
                                                        "lowerlimit": "0",
                                                        "numbersuffix": "%",
                                                        "majortmnumber": "11",
                                                        "majortmcolor": "646F8F",
                                                        "majortmheight": "9",
                                                        "minortmnumber": "2",
                                                        "minortmcolor": "646F8F",
                                                        "minortmheight": "3",
                                                        "majortmthickness": "1",
                                                        "decimals": "0",
                                                        "ledgap": "0",
                                                        "ledsize": "1",
                                                        "ledborderthickness": "4",
                                                        "showborder": "0"
                                                    },
                                                    "colorrange": {
                                                        "color": [{
                                                            "minvalue": "0",
                                                            "maxvalue": "30",
                                                            "code": "cf0000"
                                                        }, {
                                                            "minvalue": "30",
                                                            "maxvalue": "60",
                                                            "code": "ffcc33"
                                                        }, {"minvalue": "60", "maxvalue": "100", "code": "99cc00"}]
                                                    },
                                                    "value": "95"
                                                }
                                            }
                                        }
                                    ]
                            },
                            {
                                id: 'ood.FusionChartsXT.13',
                                key: 'ood.FusionChartsXT.13',
                                caption: '温度计',
                                group: true,
                                imageClass: 'ri ri-lightbulb-line',
                                sub:
                                    [
                                        {
                                            id: 'ood.UI.FC_tmgg01',
                                            key: 'ood.UI.FusionChartsXT',
                                            caption: '$(RAD.widgets.fc.Thermometer) 1',
                                            imageClass: 'ri ri-thermometer-line',
                                            draggable: true,
                                            iniProp: {
                                                chartType: "Thermometer",
                                                JSONData: {
                                                    "chart": {
                                                        "caption": "Central cold storage",
                                                        "subcaption": "Bakersfield Central",
                                                        "subcaptionFontBold": "0",
                                                        "lowerLimit": "-20",
                                                        "upperLimit": "20",
                                                        "numberSuffix": "\u00B0C",
                                                        "bgColor": "#ffffff",
                                                        "showBorder": "0",
                                                        "thmFillColor": "#008ee4"
                                                    }, "value": "-10"
                                                }
                                            }
                                        },
                                        {
                                            id: 'ood.UI.FC_tmgg02',
                                            key: 'ood.UI.FusionChartsXT',
                                            caption: '$(RAD.widgets.fc.Thermometer) 2',
                                            imageClass: 'ri ri-thermometer-line',
                                            draggable: true,
                                            iniProp: {
                                                chartType: "Thermometer",
                                                JSONData: {
                                                    "chart": {
                                                        "manageresize": "1",
                                                        "palette": "3",
                                                        "bgcolor": "FFFFFF",
                                                        "bgalpha": "0",
                                                        "showborder": "0",
                                                        "lowerlimit": "0",
                                                        "upperlimit": "100",
                                                        "lowerlimitdisplay": "Low",
                                                        "upperlimitdisplay": "High",
                                                        "numbersuffix": "%",
                                                        "majortmheight": "4",
                                                        "minortmnumber": "5",
                                                        "usesamefillcolor": "0",
                                                        "showtickvalues": "1",
                                                        "decmials": "0",
                                                        "charttopmargin": "25",
                                                        "chartleftmargin": "20",
                                                        "chartbottommargin": "20",
                                                        "thmbulbradius": "20",
                                                        "gaugeoriginx": "40"
                                                    },
                                                    "value": "78.9",
                                                    "annotations": {
                                                        "groups": [{
                                                            "id": "Grp1",
                                                            "showbelow": "1",
                                                            "items": [{
                                                                "type": "rectangle",
                                                                "x": "$chartStartX+8",
                                                                "y": "$chartStartY",
                                                                "tox": "$chartEndX",
                                                                "toy": "$chartEndY",
                                                                "radius": "10",
                                                                "fillcolor": "AEC0CA, 333333, AEC0CA",
                                                                "fillangle": "90"
                                                            }, {
                                                                "type": "rectangle",
                                                                "x": "$chartStartX+13",
                                                                "y": "$chartStartY+5",
                                                                "tox": "$chartEndX-5",
                                                                "toy": "$chartEndY-5",
                                                                "radius": "10",
                                                                "fillcolor": "333333, C3D0D8, 333333",
                                                                "fillangle": "90"
                                                            }, {
                                                                "type": "rectangle",
                                                                "x": "$chartStartX+18",
                                                                "y": "$chartStartY+10",
                                                                "tox": "$chartEndX-10",
                                                                "toy": "$chartEndY-10",
                                                                "radius": "10",
                                                                "fillcolor": "C4D5DC, A3A5A4",
                                                                "fillangle": "180"
                                                            }]
                                                        }]
                                                    }
                                                }
                                            }
                                        },
                                        {
                                            id: 'ood.UI.FC_tmgg03',
                                            key: 'ood.UI.FusionChartsXT',
                                            caption: '$(RAD.widgets.fc.Thermometer) 3',
                                            imageClass: 'ri ri-thermometer-line',
                                            draggable: true,
                                            iniProp: {
                                                chartType: "Thermometer",
                                                JSONData: {
                                                    "chart": {
                                                        "manageresize": "1",
                                                        "lowerlimit": "-40",
                                                        "upperlimit": "120",
                                                        "majortmnumber": "11",
                                                        "majortmcolor": "A4D1FF",
                                                        "minortmcolor": "A4D1FF",
                                                        "majortmwidth": "4",
                                                        "minortmnumber": "3",
                                                        "majortmthickness": "1",
                                                        "basefontcolor": "A4D1FF",
                                                        "decmials": "0",
                                                        "tickmarkdecmials": "0",
                                                        "thmfillcolor": "FF5904",
                                                        "chartleftmargin": "30",
                                                        "charttopmargin": "40",
                                                        "chartbottommargin": "40",
                                                        "numbersuffix": "\u00B0",
                                                        "borderthickness": "2",
                                                        "thmbulbradius": "20",
                                                        "gaugeoriginx": "30",
                                                        "showborder": "0"
                                                    },
                                                    "value": "32",
                                                    "annotations": {
                                                        "groups": [{
                                                            "showbelow": "1",
                                                            "items": [{
                                                                "type": "rectangle",
                                                                "x": "$chartStartX",
                                                                "y": "$chartStartY",
                                                                "tox": "$chartEndX",
                                                                "toy": "$chartEndY",
                                                                "radius": "0",
                                                                "showborder": "0",
                                                                "borderthickness": "1",
                                                                "fillcolor": "666666,CCCCCC",
                                                                "fillalpha": "100",
                                                                "fillasgradient": "1",
                                                                "fillangle": "90",
                                                                "fillpattern": "linear"
                                                            }, {
                                                                "type": "rectangle",
                                                                "x": "$chartStartX+5",
                                                                "y": "$chartStartY+5",
                                                                "tox": "$chartEndX-5",
                                                                "toy": "$chartEndY-5",
                                                                "radius": "0",
                                                                "fillcolor": "CCCCCC,888888",
                                                                "fillalpha": "100,100",
                                                                "fillasgradient": "1",
                                                                "fillangle": "90",
                                                                "fillpattern": "linear"
                                                            }, {
                                                                "type": "rectangle",
                                                                "x": "$chartStartX+10",
                                                                "y": "$chartStartY+10",
                                                                "tox": "$chartEndX-10",
                                                                "toy": "$chartEndY-10",
                                                                "radius": "0",
                                                                "fillcolor": "004F9D"
                                                            }, {
                                                                "type": "text",
                                                                "x": "78",
                                                                "y": "403",
                                                                "label": "F",
                                                                "fontsize": "12",
                                                                "fontcolor": "A4D1FF"
                                                            }]
                                                        }]
                                                    }
                                                }
                                            }
                                        },
                                        {
                                            id: 'ood.UI.FC_tmgg04',
                                            key: 'ood.UI.FusionChartsXT',
                                            caption: '$(RAD.widgets.fc.Thermometer) 4',
                                            imageClass: 'ri ri-thermometer-line',
                                            draggable: true,
                                            iniProp: {
                                                chartType: "Thermometer",
                                                JSONData: {
                                                    "chart": {
                                                        "manageresize": "1",
                                                        "showborder": "0",
                                                        "bgcolor": "FFFFFF",
                                                        "bgalpha": "0",
                                                        "lowerlimit": "0",
                                                        "upperlimit": "100",
                                                        "majortmnumber": "11",
                                                        "majortmheight": "2",
                                                        "minortmnumber": "9",
                                                        "decmials": "0",
                                                        "thmfillcolor": "FF5904",
                                                        "chartleftmargin": "20",
                                                        "chartrightmargin": "20",
                                                        "charttopmargin": "40",
                                                        "chartbottommargin": "40",
                                                        "numbersuffix": "\u00B0",
                                                        "borderthickness": "2",
                                                        "thmbulbradius": "30",
                                                        "gaugeoriginx": "40"
                                                    },
                                                    "value": "32",
                                                    "annotations": {
                                                        "groups": [{
                                                            "showbelow": "1",
                                                            "items": [{
                                                                "type": "rectangle",
                                                                "x": "$chartStartX",
                                                                "y": "$chartStartY",
                                                                "tox": "$chartEndX",
                                                                "toy": "$chartEndY",
                                                                "radius": "15",
                                                                "showborder": "0",
                                                                "borderthickness": "2",
                                                                "fillcolor": "914800,000000",
                                                                "fillalpha": "100",
                                                                "fillasgradient": "1",
                                                                "fillangle": "45",
                                                                "fillpattern": "linear"
                                                            }, {
                                                                "type": "rectangle",
                                                                "x": "$chartStartX+10",
                                                                "y": "$chartStartY+10",
                                                                "tox": "$chartEndX-10",
                                                                "toy": "$chartEndY-10",
                                                                "radius": "10",
                                                                "showborder": "1",
                                                                "borderthickness": "2",
                                                                "fillcolor": "FFBC79,FFBA75",
                                                                "fillalpha": "50",
                                                                "fillangle": "45"
                                                            }, {
                                                                "type": "text",
                                                                "x": "77",
                                                                "y": "433",
                                                                "label": "C",
                                                                "fontsize": "12",
                                                                "fontcolor": "5D5D5D",
                                                                "bold": "1"
                                                            }]
                                                        }]
                                                    }
                                                }
                                            }
                                        }
                                    ]
                            },

                        ]
                    },
                    {
                        id: 'ood.UI.FusionChartsXT',
                        key: 'ood.UI.FusionChartsXT',
                        caption: '统计',
                        // itemStyle: "font-style: italic;",
                        group: true,
                        imageClass: 'ri ri-bar-chart-line',
                        sub: [
                            {
                                id: 'ood.FusionChartsXT.1',
                                key: 'ood.FusionChartsXT.1',
                                caption: '$RAD.widgets.group.Column',
                                group: true,
                                initFold: false,
                                imageClass: 'ri ri-bar-chart-line',
                                sub:
                                    [
                                        {
                                            id: 'ood.UI.FCs01',
                                            key: 'ood.UI.FusionChartsXT',
                                            caption: '$(RAD.widgets.fc.Column 2D)',
                                            imageClass: 'ri ri-bar-chart-2-line',
                                            draggable: true,
                                            iniProp: {
                                                chartType: "Column2D",
                                                dock: 'fill',
                                                JSONData: dataConf.FCsingle
                                            }
                                        },
                                        {
                                            id: 'ood.UI.FCs02',
                                            key: 'ood.UI.FusionChartsXT',
                                            caption: '$(RAD.widgets.fc.Column 3D)',
                                            imageClass: 'ri ri-bar-chart-2-line',
                                            draggable: true,
                                            iniProp: {
                                                chartType: "Column3D",
                                                dock: 'fill',
                                                JSONData: dataConf.FCsingle
                                            }
                                        },
                                        {
                                            id: 'ood.UI.FCs05',
                                            key: 'ood.UI.FusionChartsXT',
                                            caption: '$(RAD.widgets.fc.Bar 2D)',
                                            imageClass: 'ri ri-bar-chart-horizontal-line',
                                            draggable: true,
                                            iniProp: {
                                                chartType: "Bar2D",
                                                dock: 'fill',
                                                JSONData: dataConf.FCsingle
                                            }
                                        },
                                        {
                                            id: 'ood.UI.FCs051',
                                            key: 'ood.UI.FusionChartsXT',
                                            caption: '$(RAD.widgets.fc.Bar 3D)',
                                            imageClass: 'ri ri-bar-chart-horizontal-line',
                                            draggable: true,
                                            iniProp: {
                                                chartType: "Bar3D",
                                                dock: 'fill',
                                                JSONData: dataConf.FCsingle
                                            }
                                        },
                                        {
                                            id: 'ood.UI.FCs03',
                                            key: 'ood.UI.FusionChartsXT',
                                            caption: '$RAD.widgets.fc.Line',
                                            imageClass: 'ri ri-line-chart-line',
                                            draggable: true,
                                            iniProp: {
                                                chartType: "Line",
                                                dock: 'fill',
                                                JSONData: dataConf.FCsingle
                                            }
                                        },
                                        {
                                            id: 'ood.UI.FCs04',
                                            key: 'ood.UI.FusionChartsXT',
                                            caption: '$RAD.widgets.fc.Area2D',
                                            imageClass: 'ri ri-bar-chart-line',
                                            draggable: true,
                                            iniProp: {
                                                chartType: "Area2D",
                                                dock: 'fill',
                                                JSONData: dataConf.FCsingle
                                            }
                                        },
                                        {
                                            id: 'ood.UI.FCs06',
                                            key: 'ood.UI.FusionChartsXT',
                                            caption: '$(RAD.widgets.fc.Pie 2D)',
                                            imageClass: 'ri ri-pie-chart-line',
                                            draggable: true,
                                            iniProp: {
                                                chartType: "Pie2D",
                                                dock: 'fill',
                                                JSONData: dataConf.FCPie
                                            }
                                        },
                                        {
                                            id: 'ood.UI.FCs07',
                                            key: 'ood.UI.FusionChartsXT',
                                            caption: '$(RAD.widgets.fc.Pie 3D)',
                                            imageClass: 'ri ri-pie-chart-2-line',
                                            draggable: true,
                                            iniProp: {
                                                chartType: "Pie3D",
                                                dock: 'fill',
                                                JSONData: dataConf.FCPie
                                            }
                                        },
                                        {
                                            id: 'ood.UI.FCs08',
                                            key: 'ood.UI.FusionChartsXT',
                                            caption: '$(RAD.widgets.fc.Doughnut 2D)',
                                            imageClass: 'ri ri-donut-chart-line',
                                            draggable: true,
                                            iniProp: {
                                                chartType: "Doughnut2D",
                                                dock: 'fill',
                                                JSONData: dataConf.FCPie
                                            }
                                        },
                                        {
                                            id: 'ood.UI.FCs09',
                                            key: 'ood.UI.FusionChartsXT',
                                            caption: '$(RAD.widgets.fc.Doughnut 3D)',
                                            imageClass: 'ri ri-donut-chart-line',
                                            draggable: true,
                                            iniProp: {
                                                chartType: "Doughnut3D",
                                                dock: 'fill',
                                                JSONData: dataConf.FCPie
                                            }
                                        },
                                        {
                                            id: 'ood.UI.FCs10',
                                            key: 'ood.UI.FusionChartsXT',
                                            caption: '$(RAD.widgets.fc.Pareto 2D)',
                                            imageClass: 'ri ri-arrow-up-right-line',
                                            draggable: true,
                                            iniProp: {
                                                chartType: "Pareto2D",
                                                dock: 'fill',
                                                JSONData: dataConf.FCsingle
                                            }
                                        },
                                        {
                                            id: 'ood.UI.FCs11',
                                            key: 'ood.UI.FusionChartsXT',
                                            caption: '$(RAD.widgets.fc.Pareto 3D)',
                                            imageClass: 'ri ri-arrow-up-right-line',
                                            draggable: true,
                                            iniProp: {
                                                chartType: "Pareto3D",
                                                dock: 'fill',
                                                JSONData: dataConf.FCsingle
                                            }
                                        }
                                    ]
                            },
                            {
                                id: 'ood.FusionChartsXT.2',
                                key: 'ood.FusionChartsXT.2',
                                caption: '$RAD.widgets.group.Bar',
                                group: true,
                                imageClass: 'ri ri-bar-chart-line',
                                sub:
                                    [
                                        {
                                            id: 'ood.UI.FCm01',
                                            key: 'ood.UI.FusionChartsXT',
                                            caption: '$(RAD.widgets.fc.MSColumn 2D)',
                                            imageClass: 'ri ri-bar-chart-2-line',
                                            draggable: true,
                                            iniProp: {
                                                chartType: "MSColumn2D",
                                                dock: 'fill',
                                                JSONData: dataConf.FCmulti
                                            }
                                        },
                                        {
                                            id: 'ood.UI.FCm02',
                                            key: 'ood.UI.FusionChartsXT',
                                            caption: '$(RAD.widgets.fc.MSColumn 3D)',
                                            imageClass: 'ri ri-bar-chart-horizontal-line',
                                            draggable: true,
                                            iniProp: {
                                                chartType: "MSColumn3D",
                                                dock: 'fill',
                                                JSONData: dataConf.FCmulti
                                            }
                                        },
                                        {
                                            id: 'ood.UI.FCm04',
                                            key: 'ood.UI.FusionChartsXT',
                                            caption: '$(RAD.widgets.fc.MSBar 2D)',
                                            imageClass: 'ri ri-bar-chart-line',
                                            draggable: true,
                                            iniProp: {
                                                chartType: "MSBar2D",
                                                dock: 'fill',
                                                JSONData: dataConf.FCmulti
                                            }
                                        },
                                        {
                                            id: 'ood.UI.FCm05',
                                            key: 'ood.UI.FusionChartsXT',
                                            caption: '$(RAD.widgets.fc.MSBar 3D)',
                                            imageClass: 'ri ri-bar-chart-2-line',
                                            draggable: true,
                                            iniProp: {
                                                chartType: "MSBar3D",
                                                dock: 'fill',
                                                JSONData: dataConf.FCmulti
                                            }
                                        },
                                        {
                                            id: 'ood.UI.FCm03',
                                            key: 'ood.UI.FusionChartsXT',
                                            caption: '$(RAD.widgets.fc.MSLine)',
                                            imageClass: 'ri ri-line-chart-line',
                                            draggable: true,
                                            iniProp: {
                                                chartType: "MSLine",
                                                dock: 'fill',
                                                JSONData: dataConf.FCmulti
                                            }
                                        },
                                        {
                                            id: 'ood.UI.FCm06',
                                            key: 'ood.UI.FusionChartsXT',
                                            caption: '$(RAD.widgets.fc.MSArea)',
                                            imageClass: 'ri ri-bar-chart-line',
                                            draggable: true,
                                            iniProp: {
                                                chartType: "MSArea",
                                                dock: 'fill',
                                                JSONData: dataConf.FCmulti
                                            }
                                        },
                                        {
                                            id: 'ood.UI.FCm07',
                                            key: 'ood.UI.FusionChartsXT',
                                            caption: '$(RAD.widgets.fc.Marimekko)',
                                            imageClass: 'ri ri-bar-chart-2-line',
                                            draggable: true,
                                            iniProp: {
                                                chartType: "Marimekko",
                                                JSONData: dataConf.FCmulti
                                            }
                                        },
                                        {
                                            id: 'ood.UI.FCm08',
                                            key: 'ood.UI.FusionChartsXT',
                                            caption: '$(RAD.widgets.fc.Zoom Line)',
                                            imageClass: 'ri ri-line-chart-line',
                                            draggable: true,
                                            iniProp: {
                                                chartType: "ZoomLine",
                                                dock: 'fill',
                                                JSONData: {
                                                    "chart": {
                                                        "caption": "Unique Website Visitors",
                                                        "subcaption": "Last year",
                                                        "paletteColors": "#0075c2,#1aaf5d",
                                                        "captionFontSize": "14",
                                                        "subcaptionFontSize": "14",
                                                        "subcaptionFontBold": "0",
                                                        "showBorder": "0",
                                                        "bgColor": "#ffffff",
                                                        "baseFont": "Helvetica Neue,Arial",
                                                        "showCanvasBorder": "0",
                                                        "showShadow": "0",
                                                        "showAlternateHGridColor": "0",
                                                        "canvasBgColor": "#ffffff",
                                                        "yaxisname": "Unique Visitors",
                                                        "xaxisname": "Date",
                                                        "yaxisminValue": "800",
                                                        "yaxismaxValue": "1400",
                                                        "forceAxisLimits": "1",
                                                        "pixelsPerPoint": "0",
                                                        "pixelsPerLabel": "30",
                                                        "lineThickness": "1",
                                                        "compactdatamode": "1",
                                                        "dataseparator": "|",
                                                        "labelHeight": "30",
                                                        "scrollheight": "10",
                                                        "flatScrollBars": "1",
                                                        "scrollShowButtons": "0",
                                                        "scrollColor": "#cccccc",
                                                        "legendBgAlpha": "0",
                                                        "legendBorderAlpha": "0",
                                                        "legendShadow": "0",
                                                        "legendItemFontSize": "10",
                                                        "legendItemFontColor": "#666666"
                                                    },
                                                    "categories": [{"category": "Jan 01|Jan 02|Jan 03|Jan 04|Jan 05|Jan 06|Jan 07|Jan 08|Jan 09|Jan 10|Jan 11|Jan 12|Jan 13|Jan 14|Jan 15|Jan 16|Jan 17|Jan 18|Jan 19|Jan 20|Jan 21|Jan 22|Jan 23|Jan 24|Jan 25|Jan 26|Jan 27|Jan 28|Jan 29|Jan 30|Jan 31|Feb 01|Feb 02|Feb 03|Feb 04|Feb 05|Feb 06|Feb 07|Feb 08|Feb 09|Feb 10|Feb 11|Feb 12|Feb 13|Feb 14|Feb 15|Feb 16|Feb 17|Feb 18|Feb 19|Feb 20|Feb 21|Feb 22|Feb 23|Feb 24|Feb 25|Feb 26|Feb 27|Feb 28|Mar 01|Mar 02|Mar 03|Mar 04|Mar 05|Mar 06|Mar 07|Mar 08|Mar 09|Mar 10|Mar 11|Mar 12|Mar 13|Mar 14|Mar 15|Mar 16|Mar 17|Mar 18|Mar 19|Mar 20|Mar 21|Mar 22|Mar 23|Mar 24|Mar 25|Mar 26|Mar 27|Mar 28|Mar 29|Mar 30|Mar 31|Apr 01|Apr 02|Apr 03|Apr 04|Apr 05|Apr 06|Apr 07|Apr 08|Apr 09|Apr 10|Apr 11|Apr 12|Apr 13|Apr 14|Apr 15|Apr 16|Apr 17|Apr 18|Apr 19|Apr 20|Apr 21|Apr 22|Apr 23|Apr 24|Apr 25|Apr 26|Apr 27|Apr 28|Apr 29|Apr 30|May 01|May 02|May 03|May 04|May 05|May 06|May 07|May 08|May 09|May 10|May 11|May 12|May 13|May 14|May 15|May 16|May 17|May 18|May 19|May 20|May 21|May 22|May 23|May 24|May 25|May 26|May 27|May 28|May 29|May 30|May 31|Jun 01|Jun 02|Jun 03|Jun 04|Jun 05|Jun 06|Jun 07|Jun 08|Jun 09|Jun 10|Jun 11|Jun 12|Jun 13|Jun 14|Jun 15|Jun 16|Jun 17|Jun 18|Jun 19|Jun 20|Jun 21|Jun 22|Jun 23|Jun 24|Jun 25|Jun 26|Jun 27|Jun 28|Jun 29|Jun 30|Jul 01|Jul 02|Jul 03|Jul 04|Jul 05|Jul 06|Jul 07|Jul 08|Jul 09|Jul 10|Jul 11|Jul 12|Jul 13|Jul 14|Jul 15|Jul 16|Jul 17|Jul 18|Jul 19|Jul 20|Jul 21|Jul 22|Jul 23|Jul 24|Jul 25|Jul 26|Jul 27|Jul 28|Jul 29|Jul 30|Jul 31|Aug 01|Aug 02|Aug 03|Aug 04|Aug 05|Aug 06|Aug 07|Aug 08|Aug 09|Aug 10|Aug 11|Aug 12|Aug 13|Aug 14|Aug 15|Aug 16|Aug 17|Aug 18|Aug 19|Aug 20|Aug 21|Aug 22|Aug 23|Aug 24|Aug 25|Aug 26|Aug 27|Aug 28|Aug 29|Aug 30|Aug 31|Sep 01|Sep 02|Sep 03|Sep 04|Sep 05|Sep 06|Sep 07|Sep 08|Sep 09|Sep 10|Sep 11|Sep 12|Sep 13|Sep 14|Sep 15|Sep 16|Sep 17|Sep 18|Sep 19|Sep 20|Sep 21|Sep 22|Sep 23|Sep 24|Sep 25|Sep 26|Sep 27|Sep 28|Sep 29|Sep 30|Oct 01|Oct 02|Oct 03|Oct 04|Oct 05|Oct 06|Oct 07|Oct 08|Oct 09|Oct 10|Oct 11|Oct 12|Oct 13|Oct 14|Oct 15|Oct 16|Oct 17|Oct 18|Oct 19|Oct 20|Oct 21|Oct 22|Oct 23|Oct 24|Oct 25|Oct 26|Oct 27|Oct 28|Oct 29|Oct 30|Oct 31|Nov 01|Nov 02|Nov 03|Nov 04|Nov 05|Nov 06|Nov 07|Nov 08|Nov 09|Nov 10|Nov 11|Nov 12|Nov 13|Nov 14|Nov 15|Nov 16|Nov 17|Nov 18|Nov 19|Nov 20|Nov 21|Nov 22|Nov 23|Nov 24|Nov 25|Nov 26|Nov 27|Nov 28|Nov 29|Nov 30|Dec 01|Dec 02|Dec 03|Dec 04|Dec 05|Dec 06|Dec 07|Dec 08|Dec 09|Dec 10|Dec 11|Dec 12|Dec 13|Dec 14|Dec 15|Dec 16|Dec 17|Dec 18|Dec 19|Dec 20|Dec 21|Dec 22|Dec 23|Dec 24|Dec 25|Dec 26|Dec 27|Dec 28|Dec 29|Dec 30|Dec 31"}],
                                                    "dataset": [{
                                                        "seriesname": "harrysfoodmart.com",
                                                        "data": "978|976|955|981|992|964|973|949|985|962|977|955|988|959|985|965|991|985|966|989|960|944|976|980|940|941|945|952|973|946|951|983|942|964|937|942|963|971|969|967|934|935|956|974|930|936|935|973|979|990|994|992|994|984|991|986|963|985|1006|965|958|976|993|974|995|989|966|965|1011|995|1007|978|985|1012|997|985|1004|987|986|981|991|982|992|983|1018|994|976|989|1022|989|1002|983|1015|1006|1005|1003|1017|1014|995|1007|1001|1019|1012|1005|1027|1011|1013|1035|1010|1011|1011|1036|1041|1005|1005|997|1012|1032|1025|1020|998|1018|1000|1009|1005|1004|1042|1047|1021|1032|1019|1038|1050|1037|1019|1018|1035|1055|1028|1049|1013|1028|1023|1054|1041|1051|1069|1051|1072|1049|1054|1035|1072|1042|1048|1083|1054|1048|1065|1046|1055|1056|1085|1046|1048|1048|1068|1089|1074|1078|1046|1052|1082|1052|1067|1058|1051|1052|1082|1060|1076|1077|1059|1070|1082|1093|1100|1089|1079|1075|1087|1089|1088|1106|1107|1067|1076|1101|1094|1078|1097|1094|1083|1066|1079|1111|1100|1085|1091|1095|1081|1091|1077|1095|1107|1083|1116|1118|1101|1111|1096|1077|1086|1117|1087|1105|1107|1094|1112|1101|1084|1094|1125|1099|1108|1084|1099|1120|1122|1092|1120|1121|1094|1114|1099|1129|1095|1125|1127|1121|1129|1110|1097|1136|1110|1098|1131|1125|1144|1104|1117|1105|1105|1145|1102|1143|1115|1147|1149|1129|1108|1109|1130|1153|1121|1127|1133|1120|1155|1120|1147|1118|1117|1145|1152|1145|1130|1157|1135|1115|1156|1163|1131|1123|1137|1151|1160|1152|1166|1144|1137|1124|1151|1129|1133|1143|1139|1171|1135|1132|1174|1170|1163|1175|1152|1142|1160|1148|1173|1158|1160|1151|1142|1168|1153|1143|1157|1142|1172|1186|1176|1185|1175|1178|1184|1166|1148|1166|1186|1187|1180|1179|1161|1174|1155|1172|1173|1179|1149|1170|1175|1162|1151|1152|1163|1155|1197|1174|1199|1180|1160|1174|1159|1168|1160"
                                                    }, {
                                                        "seriesname": "harrysfashion.com",
                                                        "data": "1053|1057|1084|1082|1098|1055|1068|1067|1074|1056|1067|1078|1079|1084|1041|1052|1066|1080|1049|1051|1049|1044|1083|1053|1038|1077|1046|1067|1053|1033|1047|1055|1031|1074|1031|1041|1071|1057|1035|1070|1050|1069|1054|1049|1022|1044|1049|1058|1064|1088|1093|1103|1085|1072|1104|1106|1078|1061|1078|1105|1105|1062|1076|1074|1114|1069|1091|1086|1094|1072|1079|1088|1082|1075|1110|1120|1108|1102|1090|1088|1092|1102|1110|1111|1085|1113|1110|1116|1095|1105|1105|1122|1133|1132|1093|1097|1120|1105|1135|1106|1108|1135|1098|1136|1122|1113|1113|1145|1103|1127|1104|1126|1147|1120|1119|1120|1132|1107|1149|1147|1149|1141|1145|1152|1117|1144|1157|1134|1157|1120|1125|1153|1141|1132|1158|1134|1166|1167|1170|1163|1139|1171|1145|1156|1158|1154|1196|1196|1169|1174|1174|1195|1161|1201|1208|1188|1182|1188|1162|1174|1174|1211|1189|1211|1172|1211|1179|1199|1216|1184|1209|1181|1186|1174|1185|1220|1206|1190|1206|1201|1190|1209|1208|1189|1195|1188|1193|1206|1214|1205|1215|1200|1194|1210|1205|1236|1226|1208|1228|1235|1197|1197|1198|1224|1220|1210|1226|1244|1228|1241|1237|1201|1208|1238|1213|1222|1213|1249|1222|1221|1230|1223|1214|1251|1234|1220|1240|1213|1252|1224|1222|1234|1262|1255|1225|1226|1242|1240|1250|1265|1235|1228|1261|1221|1230|1235|1260|1273|1253|1268|1258|1233|1258|1248|1230|1270|1246|1242|1246|1253|1253|1257|1253|1266|1248|1247|1257|1245|1281|1271|1272|1248|1292|1251|1253|1257|1259|1288|1252|1297|1290|1268|1291|1258|1263|1254|1302|1279|1272|1271|1281|1261|1263|1289|1294|1272|1296|1264|1282|1268|1296|1280|1281|1277|1277|1292|1266|1310|1288|1312|1308|1300|1289|1273|1282|1300|1322|1301|1314|1296|1305|1305|1327|1323|1295|1314|1298|1312|1330|1293|1309|1286|1309|1332|1300|1295|1325|1322|1305|1323|1300|1308|1299|1324|1338|1313|1329|1331|1299|1329|1344|1335|1342|1307|1314|1326|1331|1328|1328|1311|1352|1328|1309|1311|1312"
                                                    }]
                                                }
                                            }
                                        }
                                    ]
                            },
                            {
                                id: 'ood.FusionChartsXT.3',
                                key: 'ood.FusionChartsXT.3',
                                caption: '$RAD.widgets.group.Stacked',
                                group: true,
                                imageClass: 'ri ri-bar-chart-line',
                                sub:
                                    [
                                        {
                                            id: 'ood.UI.FCst01',
                                            key: 'ood.UI.FusionChartsXT',
                                            caption: '$(RAD.widgets.fc.StackedColumn 2D)',
                                            imageClass: 'ri ri-bar-chart-2-line',
                                            draggable: true,
                                            iniProp: {
                                                chartType: "StackedColumn2D",
                                                dock: 'fill',
                                                JSONData: dataConf.FCstack
                                            }
                                        },
                                        {
                                            id: 'ood.UI.FCst02',
                                            key: 'ood.UI.FusionChartsXT',
                                            caption: '$(RAD.widgets.fc.StackedColumn 3D)',
                                            imageClass: 'ri ri-bar-chart-horizontal-line',
                                            draggable: true,
                                            iniProp: {
                                                chartType: "StackedColumn3D",
                                                dock: 'fill',
                                                JSONData: dataConf.FCstack
                                            }
                                        },
                                        {
                                            id: 'ood.UI.FCst03',
                                            key: 'ood.UI.FusionChartsXT',
                                            caption: '$(RAD.widgets.fc.StackedBar 2D)',
                                            imageClass: 'ri ri-bar-chart-2-line',
                                            draggable: true,
                                            iniProp: {
                                                chartType: "StackedBar2D",
                                                dock: 'fill',
                                                JSONData: dataConf.FCstack
                                            }
                                        },
                                        {
                                            id: 'ood.UI.FCst04',
                                            key: 'ood.UI.FusionChartsXT',
                                            caption: '$(RAD.widgets.fc.StackedBar 3D)',
                                            imageClass: 'ri ri-bar-chart-horizontal-line',
                                            draggable: true,
                                            iniProp: {
                                                chartType: "StackedBar3D",
                                                dock: 'fill',
                                                JSONData: dataConf.FCstack
                                            }
                                        },
                                        {
                                            id: 'ood.UI.FCst05',
                                            key: 'ood.UI.FusionChartsXT',
                                            caption: '$(RAD.widgets.fc.StackedArea 2D)',
                                            imageClass: 'ri ri-bar-chart-2-line',
                                            draggable: true,
                                            iniProp: {
                                                chartType: "StackedArea2D",
                                                dock: 'fill',
                                                JSONData: dataConf.FCstack
                                            }
                                        },
                                        {
                                            id: 'ood.UI.FCst06',
                                            key: 'ood.UI.FusionChartsXT',
                                            caption: '$(RAD.widgets.fc.MSStackedColumn 2D)',
                                            imageClass: 'ri ri-bar-chart-horizontal-line',
                                            draggable: true,
                                            iniProp: {
                                                chartType: "MSStackedColumn2D",
                                                dock: 'fill',
                                                JSONData: dataConf.FCstack2
                                            }
                                        }
                                    ]
                            },
                            {
                                id: 'ood.FusionChartsXT.4',
                                key: 'ood.FusionChartsXT.4',
                                caption: '$RAD.widgets.group.Combination',
                                group: true,
                                imageClass: 'ri ri-bar-chart-line',
                                sub:
                                    [
                                        {
                                            id: 'ood.UI.FCc01',
                                            key: 'ood.UI.FusionChartsXT',
                                            caption: '$(RAD.widgets.fc.MSCombi 2D)',
                                            imageClass: 'ri ri-line-chart-line',
                                            draggable: true,
                                            iniProp: {
                                                chartType: "MSCombi2D",
                                                dock: 'fill',
                                                JSONData: dataConf.FCcomb
                                            }
                                        },
                                        {
                                            id: 'ood.UI.FCc02',
                                            key: 'ood.UI.FusionChartsXT',
                                            caption: '$(RAD.widgets.fc.MSCombi 3D)',
                                            imageClass: 'ri ri-line-chart-line',
                                            draggable: true,
                                            iniProp: {
                                                chartType: "MSCombi3D",
                                                JSONData: dataConf.FCcomb
                                            }
                                        },
                                        {
                                            id: 'ood.UI.FCc03',
                                            key: 'ood.UI.FusionChartsXT',
                                            caption: '$(RAD.widgets.fc.MSColumnLine 3D)',
                                            imageClass: 'ri ri-line-chart-line',
                                            draggable: true,
                                            iniProp: {
                                                chartType: "MSColumnLine3D",
                                                dock: 'fill',
                                                JSONData: {
                                                    "chart": {
                                                        "caption": "Company Revenue",
                                                        "palette": "1",
                                                        "showvalues": "0",
                                                        "yaxisvaluespadding": "10",
                                                        "numberprefix": "$"
                                                    },
                                                    "categories": [{"category": [{"label": "Aug 05"}, {"label": "Sep 05"}, {"label": "Oct 05"}, {"label": "Nov 05"}, {"label": "Dec 05"}]}],
                                                    "dataset": [{
                                                        "seriesname": "Product A",
                                                        "data": [{"value": "36000"}, {"value": "34300"}, {"value": "30000"}, {"value": "27800"}, {"value": "25000"}]
                                                    }, {
                                                        "seriesname": "Product B",
                                                        "data": [{"value": "31000"}, {"value": "29300"}, {"value": "26000"}, {"value": "21000"}, {"value": "20500"}]
                                                    }, {
                                                        "seriesname": "Predicted",
                                                        "renderas": "Line",
                                                        "data": [{"value": "25000"}, {"value": "23000"}, {"value": "20000"}, {"value": "18000"}, {"value": "14500"}]
                                                    }, {
                                                        "seriesname": "2004 Avg.",
                                                        "renderas": "Line",
                                                        "data": [{"value": "17000"}, {"value": "15000"}, {"value": "16000"}, {"value": "11500"}, {"value": "10000"}]
                                                    }]
                                                }
                                            }
                                        },
                                        {
                                            id: 'ood.UI.FCc04',
                                            key: 'ood.UI.FusionChartsXT',
                                            caption: '$(RAD.widgets.fc.Stacked Column 2D Line)',
                                            imageClass: 'ri ri-line-chart-line',
                                            draggable: true,
                                            iniProp: {
                                                chartType: "StackedColumn2DLine",
                                                dock: 'fill',
                                                JSONData: {
                                                    "chart": {
                                                        "showvalues": "0",
                                                        "caption": "Cost Analysis",
                                                        "numberprefix": "$",
                                                        "xaxisname": "Quarters",
                                                        "yaxisname": "Cost",
                                                        "useroundedges": "1"
                                                    },
                                                    "categories": [{"category": [{"label": "Quarter 1"}, {"label": "Quarter 2"}, {"label": "Quarter 3"}, {"label": "Quarter 4"}]}],
                                                    "dataset": [{
                                                        "seriesname": "Fixed Cost",
                                                        "data": [{"value": "235000"}, {"value": "225100"}, {"value": "222000"}, {"value": "230500"}]
                                                    }, {
                                                        "seriesname": "Variable Cost",
                                                        "data": [{"value": "230000"}, {"value": "143000"}, {"value": "198000"}, {"value": "327600"}]
                                                    }, {
                                                        "seriesname": "Budgeted cost",
                                                        "renderas": "Line",
                                                        "data": [{"value": "455000"}, {"value": "334000"}, {"value": "426000"}, {"value": "403000"}]
                                                    }]
                                                }
                                            }
                                        },
                                        {
                                            id: 'ood.UI.FCc05',
                                            key: 'ood.UI.FusionChartsXT',
                                            caption: '$(RAD.widgets.fc.Stacked Column 3D Line)',
                                            imageClass: 'ri ri-line-chart-line',
                                            draggable: true,
                                            iniProp: {
                                                chartType: "StackedColumn3DLine",
                                                dock: 'fill',
                                                JSONData: {
                                                    "chart": {
                                                        "showvalues": "0",
                                                        "caption": "Cost Analysis",
                                                        "numberprefix": "$",
                                                        "xaxisname": "Quarters",
                                                        "yaxisname": "Cost",
                                                        "useroundedges": "1"
                                                    },
                                                    "categories": [{"category": [{"label": "Quarter 1"}, {"label": "Quarter 2"}, {"label": "Quarter 3"}, {"label": "Quarter 4"}]}],
                                                    "dataset": [{
                                                        "seriesname": "Fixed Cost",
                                                        "data": [{"value": "235000"}, {"value": "225100"}, {"value": "222000"}, {"value": "230500"}]
                                                    }, {
                                                        "seriesname": "Variable Cost",
                                                        "data": [{"value": "230000"}, {"value": "143000"}, {"value": "198000"}, {"value": "327600"}]
                                                    }, {
                                                        "seriesname": "Budgeted cost",
                                                        "renderas": "Line",
                                                        "data": [{"value": "455000"}, {"value": "334000"}, {"value": "426000"}, {"value": "403000"}]
                                                    }]
                                                }
                                            }
                                        },
                                        {
                                            id: 'ood.UI.FCc06',
                                            key: 'ood.UI.FusionChartsXT',
                                            caption: '$(RAD.widgets.fc.MSCombiDY 2D)',
                                            imageClass: 'ri ri-line-chart-line',
                                            draggable: true,
                                            iniProp: {
                                                chartType: "MSCombiDY2D",
                                                dock: 'fill',
                                                JSONData: {
                                                    "chart": {
                                                        "palette": "2",
                                                        "caption": "Sales by Product",
                                                        "subcaption": "March 2006",
                                                        "showvalues": "0",
                                                        "divlinedecimalprecision": "1",
                                                        "limitsdecimalprecision": "1",
                                                        "pyaxisname": "Amount",
                                                        "syaxisname": "Quantity",
                                                        "numberprefix": "$",
                                                        "formatnumberscale": "0"
                                                    },
                                                    "categories": [{"category": [{"label": "A"}, {"label": "B"}, {"label": "C"}, {"label": "D"}, {"label": "E"}, {"label": "F"}, {"label": "G"}, {"label": "H"}, {"label": "I"}, {"label": "J"}]}],
                                                    "dataset": [{
                                                        "seriesname": "Revenue",
                                                        "data": [{"value": "5854"}, {"value": "4171"}, {"value": "1375"}, {"value": "1875"}, {"value": "2246"}, {"value": "2696"}, {"value": "1287"}, {"value": "2140"}, {"value": "1603"}, {"value": "1628"}]
                                                    }, {
                                                        "seriesname": "Profit",
                                                        "renderas": "Area",
                                                        "parentyaxis": "P",
                                                        "data": [{"value": "3242"}, {"value": "3171"}, {"value": "700"}, {"value": "1287"}, {"value": "1856"}, {"value": "1126"}, {"value": "987"}, {"value": "1610"}, {"value": "903"}, {"value": "928"}]
                                                    }, {
                                                        "linethickness": "3",
                                                        "seriesname": "Quantity",
                                                        "parentyaxis": "S",
                                                        "data": [{"value": "174"}, {"value": "197"}, {"value": "155"}, {"value": "15"}, {"value": "66"}, {"value": "85"}, {"value": "37"}, {"value": "10"}, {"value": "44"}, {"value": "322"}]
                                                    }]
                                                }
                                            }
                                        },
                                        {
                                            id: 'ood.UI.FCc07',
                                            key: 'ood.UI.FusionChartsXT',
                                            caption: '$(RAD.widgets.fc.MSColumn 3D LineDY)',
                                            imageClass: 'ri ri-line-chart-line',
                                            draggable: true,
                                            iniProp: {
                                                chartType: "MSColumn3DLineDY",
                                                dock: 'fill',
                                                JSONData: {
                                                    "chart": {
                                                        "palette": "1",
                                                        "caption": "Sales",
                                                        "showlabels": "1",
                                                        "showvalues": "0",
                                                        "numberprefix": "$",
                                                        "syaxisvaluesdecimals": "2",
                                                        "connectnulldata": "0",
                                                        "pyaxisname": "Revenue",
                                                        "syaxisname": "Quantity",
                                                        "numdivlines": "4",
                                                        "formatnumberscale": "0"
                                                    },
                                                    "categories": [{"category": [{"label": "March"}, {"label": "April"}, {"label": "May"}, {"label": "June"}, {"label": "July"}]}],
                                                    "dataset": [{
                                                        "seriesname": "Product A",
                                                        "color": "AFD8F8",
                                                        "showvalues": "0",
                                                        "data": [{"value": "25601.34"}, {"value": "20148.82"}, {"value": "17372.76"}, {"value": "35407.15"}, {"value": "38105.68"}]
                                                    }, {
                                                        "seriesname": "Product B",
                                                        "color": "F6BD0F",
                                                        "showvalues": "0",
                                                        "data": [{"value": "57401.85"}, {"value": "41941.19"}, {"value": "45263.37"}, {"value": "117320.16"}, {
                                                            "value": "114845.27",
                                                            "dashed": "1"
                                                        }]
                                                    }, {
                                                        "seriesname": "Total Quantity",
                                                        "color": "8BBA00",
                                                        "showvalues": "0",
                                                        "parentyaxis": "S",
                                                        "data": [{"value": "45000"}, {"value": "44835"}, {"value": "42835"}, {"value": "77557"}, {"value": "92633"}]
                                                    }]
                                                }
                                            }
                                        },
                                        {
                                            id: 'ood.UI.FCc08',
                                            key: 'ood.UI.FusionChartsXT',
                                            caption: '$(RAD.widgets.fc.Stacked Column 3D LineDY)',
                                            imageClass: 'ri ri-line-chart-line',
                                            draggable: true,
                                            iniProp: {
                                                chartType: "StackedColumn3DLineDY",
                                                dock: 'fill',
                                                JSONData: {
                                                    "chart": {
                                                        "palette": "1",
                                                        "caption": "Sales",
                                                        "showlabels": "1",
                                                        "showvalues": "0",
                                                        "numberprefix": "$",
                                                        "syaxisvaluesdecimals": "2",
                                                        "connectnulldata": "0",
                                                        "pyaxisname": "Revenue",
                                                        "syaxisname": "Quantity",
                                                        "numdivlines": "4",
                                                        "formatnumberscale": "0",
                                                        "syncaxislimits": "1"
                                                    },
                                                    "categories": [{"category": [{"label": "March"}, {"label": "April"}, {"label": "May"}, {"label": "June"}, {"label": "July"}]}],
                                                    "dataset": [{
                                                        "seriesname": "Product A",
                                                        "color": "AFD8F8",
                                                        "showvalues": "0",
                                                        "data": [{"value": "25601.34"}, {"value": "20148.82"}, {"value": "17372.76"}, {"value": "35407.15"}, {"value": "38105.68"}]
                                                    }, {
                                                        "seriesname": "Product B",
                                                        "color": "F6BD0F",
                                                        "showvalues": "0",
                                                        "data": [{"value": "57401.85"}, {"value": "41941.19"}, {"value": "45263.37"}, {"value": "117320.16"}, {
                                                            "value": "114845.27",
                                                            "dashed": "1"
                                                        }]
                                                    }, {
                                                        "seriesname": "Total Quantity",
                                                        "color": "8BBA00",
                                                        "showvalues": "0",
                                                        "parentyaxis": "S",
                                                        "data": [{"value": "45000"}, {"value": "44835"}, {"value": "42835"}, {"value": "77557"}, {"value": "92633"}]
                                                    }]
                                                }
                                            }
                                        },
                                        {
                                            id: 'ood.UI.FCc09',
                                            key: 'ood.UI.FusionChartsXT',
                                            caption: '$(RAD.widgets.fc.MSStacked Column 2D LineDY)',
                                            imageClass: 'ri ri-line-chart-line',
                                            draggable: true,
                                            iniProp: {
                                                chartType: "MSStackedColumn2DLineDY",
                                                dock: 'fill',
                                                JSONData: {
                                                    "chart": {
                                                        "caption": "Annual Revenue",
                                                        "subcaption": "In Million $",
                                                        "xaxisname": "Year",
                                                        "pyaxisname": "Sales in M$",
                                                        "syaxisname": "Cost as % of Revenue",
                                                        "decimals": "0",
                                                        "numberprefix": "$",
                                                        "numbersuffix": "M",
                                                        "snumbersuffix": "%",
                                                        "setadaptivesymin": "1",
                                                        "showplotborder": "1",
                                                        "palette": "2"
                                                    },
                                                    "categories": [{
                                                        "font": "Arial",
                                                        "fontsize": "12",
                                                        "fontcolor": "000000",
                                                        "category": [{"label": "2001"}, {"label": "2002"}, {"label": "2003"}, {"label": "2004"}, {"label": "2005"}]
                                                    }],
                                                    "dataset": [{
                                                        "dataset": [{
                                                            "seriesname": "Product A",
                                                            "color": "AFD8F8",
                                                            "showvalues": "0",
                                                            "data": [{"value": "30"}, {"value": "26"}, {"value": "29"}, {"value": "31"}, {"value": "34"}]
                                                        }, {
                                                            "seriesname": "Product B",
                                                            "color": "F6BD0F",
                                                            "showvalues": "0",
                                                            "data": [{"value": "21"}, {"value": "28"}, {"value": "39"}, {"value": "41"}, {"value": "24"}]
                                                        }]
                                                    }, {
                                                        "dataset": [{
                                                            "seriesname": "Service A",
                                                            "color": "8BBA00",
                                                            "showvalues": "0",
                                                            "data": [{"value": "27"}, {"value": "25"}, {"value": "28"}, {"value": "26"}, {"value": "10"}]
                                                        }, {
                                                            "seriesname": "Service B",
                                                            "color": "A66EDD",
                                                            "showvalues": "0",
                                                            "data": [{"value": "17"}, {"value": "15"}, {"value": "18"}, {"value": "16"}, {"value": "10"}]
                                                        }, {
                                                            "seriesname": "Service C",
                                                            "color": "F984A1",
                                                            "showvalues": "0",
                                                            "data": [{"value": "12"}, {"value": "17"}, {"value": "16"}, {"value": "15"}, {"value": "12"}]
                                                        }]
                                                    }],
                                                    "lineset": [{
                                                        "seriesname": "Cost as % of Revenue",
                                                        "showvalues": "0",
                                                        "linethickness": "4",
                                                        "data": [{"value": "57"}, {"value": "68"}, {"value": "79"}, {"value": "73"}, {"value": "80"}]
                                                    }]
                                                }
                                            }
                                        }
                                    ]
                            },
                            {
                                id: 'ood.FusionChartsXT.5',
                                key: 'ood.FusionChartsXT.5',
                                caption: '$RAD.widgets.group.XYPlot',
                                group: true,
                                imageClass: 'ri ri-bar-chart-line',
                                sub:
                                    [
                                        {
                                            id: 'ood.UI.FCx01',
                                            key: 'ood.UI.FusionChartsXT',
                                            caption: '$RAD.widgets.fc.Scatter',
                                            imageClass: 'ri ri-bar-chart-horizontal-line',
                                            draggable: true,
                                            iniProp: {
                                                chartType: "Scatter",
                                                dock: 'fill',
                                                JSONData: {
                                                    "chart": {
                                                        "caption": "Income Expenditure Analysis",
                                                        "subcaption": "(sample survey done among buyers of LCD TV)",
                                                        "xaxisname": "Salary",
                                                        "yaxisname": "Amount spent on LCD TV",
                                                        "showregressionline": "1",
                                                        "xaxislabelmode": "auto"
                                                    },
                                                    "dataset": [{
                                                        "color": "000000",
                                                        "data": [{"x": "9200", "y": "1600"}, {
                                                            "x": "9900",
                                                            "y": "1800"
                                                        }, {"x": "9500", "y": "1510"}, {"x": "9700", "y": "1400"}, {
                                                            "x": "8100",
                                                            "y": "1500"
                                                        }, {"x": "8600", "y": "1300"}, {"x": "8300", "y": "1220"}, {
                                                            "x": "7800",
                                                            "y": "1300"
                                                        }, {"x": "7800", "y": "1220"}, {"x": "7000", "y": "1210"}, {
                                                            "x": "6000",
                                                            "y": "1140"
                                                        }, {"x": "6000", "y": "1000"}, {"x": "6200", "y": "950"}, {
                                                            "x": "5300",
                                                            "y": "940"
                                                        }, {"x": "4700", "y": "1000"}, {"x": "4800", "y": "947"}, {
                                                            "x": "4500",
                                                            "y": "850"
                                                        }, {"x": "4000", "y": "870"}, {"x": "3700", "y": "800"}, {
                                                            "x": "3100",
                                                            "y": "800"
                                                        }, {"x": "4500", "y": "600"}, {"x": "4000", "y": "660"}, {
                                                            "x": "3600",
                                                            "y": "500"
                                                        }, {"x": "3400", "y": "450"}, {"x": "3100", "y": "650"}, {
                                                            "x": "3100",
                                                            "y": "600"
                                                        }, {"x": "3100", "y": "540"}, {"x": "2800", "y": "460"}, {
                                                            "x": "2400",
                                                            "y": "650"
                                                        }, {"x": "2300", "y": "540"}, {"x": "3000", "y": "340"}, {
                                                            "x": "2000",
                                                            "y": "280"
                                                        }, {"x": "2200", "y": "340"}, {"x": "2000", "y": "180"}]
                                                    }]
                                                }
                                            }
                                        },
                                        {
                                            id: 'ood.UI.FCx02',
                                            key: 'ood.UI.FusionChartsXT',
                                            caption: '$RAD.widgets.fc.Bubble',
                                            imageClass: 'ri ri-circle-line',
                                            draggable: true,
                                            iniProp: {
                                                chartType: "Bubble",
                                                dock: 'fill',
                                                JSONData: {
                                                    "chart": {
                                                        "numberprefix": "$",
                                                        "is3d": "1",
                                                        "animation": "1",
                                                        "clipbubbles": "1",
                                                        "xaxismaxvalue": "100",
                                                        "showplotborder": "0",
                                                        "xaxisname": "x-axis",
                                                        "yaxisname": "y-axis",
                                                        "chartrightmargin": "30",
                                                        "drawquadrant": "1",
                                                        "quadrantlabeltl": "Top Left Quadrant",
                                                        "quadrantlabeltr": "Top Right Quadrant",
                                                        "quadrantlabelbr": "Bottom Right Quadrant",
                                                        "quadrantlabelbl": "Bottom Left Quadrant",
                                                        "quadrantlinethickness": "1",
                                                        "quadrantlinecolor": "000080",
                                                        "canvasbgalpha": "100",
                                                        "bgcolor": "C7C7C7",
                                                        "quadrantxval": "58",
                                                        "quadrantyval": "2.3"
                                                    },
                                                    "categories": [{
                                                        "category": [{"label": "0%", "x": "0"}, {
                                                            "label": "20%",
                                                            "x": "20",
                                                            "showverticalline": "1"
                                                        }, {"label": "40%", "x": "40", "showverticalline": "1"}, {
                                                            "label": "60%",
                                                            "x": "60",
                                                            "showverticalline": "1"
                                                        }, {"label": "80%", "x": "80", "showverticalline": "1"}, {
                                                            "label": "100%",
                                                            "x": "100",
                                                            "showverticalline": "1"
                                                        }]
                                                    }],
                                                    "dataset": [{
                                                        "showvalues": "0",
                                                        "data": [{
                                                            "x": "30",
                                                            "y": "1.3",
                                                            "z": "116",
                                                            "name": "Traders",
                                                            "color": "000080"
                                                        }, {
                                                            "x": "32",
                                                            "y": "3.5",
                                                            "z": "99",
                                                            "name": "Farmers",
                                                            "color": "ff0000"
                                                        }, {
                                                            "x": "10",
                                                            "y": "3.0",
                                                            "z": "49",
                                                            "name": "Farmers",
                                                            "color": "595959"
                                                        }, {
                                                            "x": "8",
                                                            "y": "1.9",
                                                            "z": "33",
                                                            "name": "Individuals",
                                                            "color": "55FFFF"
                                                        }, {
                                                            "x": "65",
                                                            "y": "1.4",
                                                            "z": "48",
                                                            "name": "Corporate Group C",
                                                            "color": "00BF55"
                                                        }, {
                                                            "x": "90",
                                                            "y": "1.0",
                                                            "z": "68",
                                                            "name": "Corporate Group C",
                                                            "color": "FFBF00"
                                                        }, {
                                                            "x": "68",
                                                            "y": "3.7",
                                                            "z": "70",
                                                            "name": "HNW Individuals",
                                                            "color": "FFFF00"
                                                        }, {
                                                            "x": "88",
                                                            "y": "3.0",
                                                            "z": "30",
                                                            "name": "HNW Individuals",
                                                            "color": "FF9FFF"
                                                        }]
                                                    }]
                                                }
                                            }
                                        }
                                    ]
                            },
                            {
                                id: 'ood.FusionChartsXT.6',
                                key: 'ood.FusionChartsXT.6',
                                caption: '$RAD.widgets.group.Scroll',
                                group: true,
                                imageClass: 'ri ri-arrow-up-down-line',
                                sub:
                                    [
                                        {
                                            id: 'ood.UI.FCsl01',
                                            key: 'ood.UI.FusionChartsXT',
                                            caption: '$(RAD.widgets.fc.ScrollColumn 2D)',
                                            imageClass: 'ri ri-bar-chart-2-line',
                                            draggable: true,
                                            iniProp: {
                                                chartType: "ScrollColumn2D",
                                                dock: 'fill',
                                                JSONData: {
                                                    "chart": {
                                                        "caption": "Business Results 2005 v 2006",
                                                        "xaxisname": "Month",
                                                        "yaxisname": "Revenue",
                                                        "showvalues": "0",
                                                        "numberprefix": "$",
                                                        "useroundedges": "1",
                                                        "legendborderalpha": "100",
                                                        "showborder": "0",
                                                        "bgcolor": "FFFFFF,FFFFFF"
                                                    },
                                                    "categories": [{"category": [{"label": "Jan"}, {"label": "Feb"}, {"label": "Mar"}, {"label": "Apr"}, {"label": "May"}, {"label": "Jun"}, {"label": "Jul"}, {"label": "Aug"}, {"label": "Sep"}, {"label": "Oct"}, {"label": "Nov"}, {"label": "Dec"}]}],
                                                    "dataset": [{
                                                        "seriesname": "2006",
                                                        "data": [{"value": "27400"}, {"value": "29800"}, {"value": "25800"}, {"value": "26800"}, {"value": "29600"}, {"value": "32600"}, {"value": "31800"}, {"value": "36700"}, {"value": "29700"}, {"value": "31900"}, {"value": "34800"}, {"value": "24800"}]
                                                    }, {
                                                        "seriesname": "2005",
                                                        "data": [{"value": "10000"}, {"value": "11500"}, {"value": "12500"}, {"value": "15000"}, {"value": "11000"}, {"value": "9800"}, {"value": "11800"}, {"value": "19700"}, {"value": "21700"}, {"value": "21900"}, {"value": "22900"}, {"value": "20800"}]
                                                    }]
                                                }
                                            }
                                        },
                                        {
                                            id: 'ood.UI.FCsl02',
                                            key: 'ood.UI.FusionChartsXT',
                                            caption: '$(RAD.widgets.fc.ScrollLine 2D)',
                                            imageClass: 'ri ri-bar-chart-2-line',
                                            draggable: true,
                                            iniProp: {
                                                chartType: "ScrollLine2D",
                                                dock: 'fill',
                                                JSONData: {
                                                    "chart": {
                                                        "caption": "Site hits per hour",
                                                        "subcaption": "In Thousands",
                                                        "numdivlines": "9",
                                                        "linethickness": "2",
                                                        "showvalues": "0",
                                                        "anchorradius": "3",
                                                        "anchorbgalpha": "50",
                                                        "numvdivlines": "24",
                                                        "showalternatevgridcolor": "1",
                                                        "alternatevgridalpha": "3",
                                                        "animation": "0"
                                                    },
                                                    "categories": [{"category": [{"label": "00:00"}, {"label": "01:00"}, {"label": "02:00"}, {"label": "03:00"}, {"label": "04:00"}, {"label": "05:00"}, {"label": "06:00"}, {"label": "07:00"}, {"label": "08:00"}, {"label": "09:00"}, {"label": "10:00"}, {"label": "11:00"}, {"label": "12:00"}, {"label": "13:00"}, {"label": "14:00"}, {"label": "15:00"}, {"label": "16:00"}, {"label": "17:00"}, {"label": "18:00"}, {"label": "19:00"}, {"label": "20:00"}, {"label": "21:00"}, {"label": "22:00"}, {"label": "23:00"}]}],
                                                    "dataset": [{
                                                        "seriesname": "Tue",
                                                        "color": "800080",
                                                        "anchorbordercolor": "800080",
                                                        "data": [{"value": "54"}, {"value": "165"}, {"value": "175"}, {"value": "190"}, {"value": "212"}, {"value": "241"}, {"value": "308"}, {"value": "401"}, {"value": "481"}, {"value": "851"}, {"value": "1250"}, {"value": "2415"}, {"value": "2886"}, {"value": "3252"}, {"value": "3673"}, {"value": "4026"}, {"value": "4470"}, {"value": "4813"}, {"value": "4961"}, {"value": "5086"}, {"value": "5284"}, {"value": "5391"}, {"value": "5657"}, {"value": "5847"}]
                                                    }, {
                                                        "seriesname": "Wed",
                                                        "color": "FF8040",
                                                        "anchorbordercolor": "FF8040",
                                                        "data": [{"value": "111"}, {"value": "120"}, {"value": "128"}, {"value": "140"}, {"value": "146"}, {"value": "157"}, {"value": "190"}, {"value": "250"}, {"value": "399"}, {"value": "691"}, {"value": "952"}, {"value": "1448"}, {"value": "1771"}, {"value": "2316"}, {"value": "2763"}, {"value": "3149"}, {"value": "3637"}, {"value": "4015"}, {"value": "4262"}, {"value": "4541"}, {"value": "4837"}, {"value": "5016"}, {"value": "5133"}, {"value": "5278"}]
                                                    }, {
                                                        "seriesname": "Thu",
                                                        "color": "FFFF00",
                                                        "anchorbordercolor": "FFFF00",
                                                        "data": [{"value": "115"}, {"value": "141"}, {"value": "175"}, {"value": "189"}, {"value": "208"}, {"value": "229"}, {"value": "252"}, {"value": "440"}, {"value": "608"}, {"value": "889"}, {"value": "1334"}, {"value": "1637"}, {"value": "2056"}, {"value": "2600"}, {"value": "3070"}, {"value": "3451"}, {"value": "3918"}, {"value": "4140"}, {"value": "4296"}, {"value": "4519"}, {"value": "4716"}, {"value": "4881"}, {"value": "5092"}, {"value": "5249"}]
                                                    }, {
                                                        "seriesname": "Fri",
                                                        "color": "FF0080",
                                                        "anchorbordercolor": "FF0080",
                                                        "data": [{"value": "98"}, {"value": "1112"}, {"value": "1192"}, {"value": "1219"}, {"value": "1264"}, {"value": "1282"}, {"value": "1365"}, {"value": "1433"}, {"value": "1559"}, {"value": "1823"}, {"value": "1867"}, {"value": "2198"}, {"value": "1112"}, {"value": "1192"}, {"value": "1219"}, {"value": "2264"}, {"value": "2282"}, {"value": "2365"}, {"value": "2433"}, {"value": "2559"}, {"value": "2823"}, {"value": "2867"}, {"value": "2867"}, {"value": "2867"}]
                                                    }]
                                                }
                                            }
                                        },
                                        {
                                            id: 'ood.UI.FCsl03',
                                            key: 'ood.UI.FusionChartsXT',
                                            caption: '$(RAD.widgets.fc.ScrollArea 2D)',
                                            imageClass: 'ri ri-bar-chart-line',
                                            draggable: true,
                                            iniProp: {
                                                chartType: "ScrollArea2D",
                                                dock: 'fill',
                                                JSONData: {
                                                    "chart": {
                                                        "caption": "Site hits per hour",
                                                        "subcaption": "In Thousands",
                                                        "numdivlines": "9",
                                                        "showvalues": "0",
                                                        "numvdivlines": "24",
                                                        "showalternatevgridcolor": "1",
                                                        "numvisibleplot": "12",
                                                        "plotgradientcolor": "",
                                                        "plotfillalpha": "30",
                                                        "palette": "4",
                                                        "scrollbtnpadding": "2",
                                                        "scrollheight": "26",
                                                        "scrollbtnwidth": "40",
                                                        "canvaspadding": "25"
                                                    },
                                                    "categories": [{"category": [{"label": "00:00"}, {"label": "01:00"}, {"label": "02:00"}, {"label": "03:00"}, {"label": "04:00"}, {"label": "05:00"}, {"label": "06:00"}, {"label": "07:00"}, {"label": "08:00"}, {"label": "09:00"}, {"label": "10:00"}, {"label": "11:00"}, {"label": "12:00"}, {"label": "13:00"}, {"label": "14:00"}, {"label": "15:00"}, {"label": "16:00"}, {"label": "17:00"}, {"label": "18:00"}, {"label": "19:00"}, {"label": "20:00"}, {"label": "21:00"}, {"label": "22:00"}, {"label": "23:00"}]}],
                                                    "dataset": [{
                                                        "seriesname": "Mon",
                                                        "color": "808080",
                                                        "anchorbordercolor": "808080",
                                                        "data": [{"value": "37"}, {"value": "45"}, {"value": "70"}, {"value": "79"}, {"value": "168"}, {"value": "337"}, {"value": "374"}, {"value": "431"}, {"value": "543"}, {"value": "784"}, {"value": "1117"}, {"value": "1415"}, {"value": "2077"}, {"value": "2510"}, {"value": "3025"}, {"value": "3383"}, {"value": "3711"}, {"value": "4016"}, {"value": "4355"}, {"value": "4751"}, {"value": "5154"}, {"value": "5475"}, {"value": "5696"}, {"value": "5801"}]
                                                    }, {
                                                        "seriesname": "Tue",
                                                        "color": "800080",
                                                        "anchorbordercolor": "800080",
                                                        "data": [{"value": "54"}, {"value": "165"}, {"value": "175"}, {"value": "190"}, {"value": "212"}, {"value": "241"}, {"value": "308"}, {"value": "401"}, {"value": "481"}, {"value": "851"}, {"value": "1250"}, {"value": "2415"}, {"value": "2886"}, {"value": "3252"}, {"value": "3673"}, {"value": "4026"}, {"value": "4470"}, {"value": "4813"}, {"value": "4961"}, {"value": "5086"}, {"value": "5284"}, {"value": "5391"}, {"value": "5657"}, {"value": "5847"}]
                                                    }, {
                                                        "seriesname": "Wed",
                                                        "color": "FF8040",
                                                        "anchorbordercolor": "FF8040",
                                                        "data": [{"value": "111"}, {"value": "120"}, {"value": "128"}, {"value": "140"}, {"value": "146"}, {"value": "157"}, {"value": "190"}, {"value": "250"}, {"value": "399"}, {"value": "691"}, {"value": "952"}, {"value": "1448"}, {"value": "1771"}, {"value": "2316"}, {"value": "2763"}, {"value": "3149"}, {"value": "3637"}, {"value": "4015"}, {"value": "4262"}, {"value": "4541"}, {"value": "4837"}, {"value": "5016"}, {"value": "5133"}, {"value": "5278"}]
                                                    }]
                                                }
                                            }
                                        },
                                        {
                                            id: 'ood.UI.FCsl04',
                                            key: 'ood.UI.FusionChartsXT',
                                            caption: '$(RAD.widgets.fc.ScrollStackedColumn 2D)',
                                            imageClass: 'ri ri-line-chart-line',
                                            draggable: true,
                                            iniProp: {
                                                chartType: "ScrollStackedColumn2D",
                                                dock: 'fill',
                                                JSONData: {
                                                    "chart": {
                                                        "palette": "3",
                                                        "caption": "Daily Sales",
                                                        "numberprefix": "$",
                                                        "xaxisname": "Day",
                                                        "useroundedges": "1",
                                                        "showvalues": "0",
                                                        "legendborderalpha": "0"
                                                    },
                                                    "categories": [{"category": [{"label": "1"}, {"label": "2"}, {"label": "3"}, {"label": "4"}, {"label": "5"}, {"label": "6"}, {"label": "7"}, {"label": "8"}, {"label": "9"}, {"label": "10"}, {"label": "11"}, {"label": "12"}, {"label": "13"}, {"label": "14"}, {"label": "15"}, {"label": "16"}, {"label": "17"}, {"label": "18"}, {"label": "19"}, {"label": "20"}, {"label": "21"}, {"label": "22"}, {"label": "23"}, {"label": "24"}, {"label": "25"}, {"label": "26"}, {"label": "27"}, {"label": "28"}, {"label": "29"}, {"label": "30"}, {"label": "31"}]}],
                                                    "dataset": [{
                                                        "seriesname": "Product A",
                                                        "data": [{"value": "57000"}, {"value": "58995"}, {"value": "61119"}, {"value": "63502"}, {"value": "57000"}, {"value": "58995"}, {"value": "61119"}, {"value": "63502"}, {"value": "65662"}, {"value": "68157"}, {"value": "69997"}, {"value": "72797"}, {"value": "75272"}, {"value": "77455"}, {"value": "79546"}, {"value": "80739"}, {"value": "82273"}, {"value": "84001"}, {"value": "85849"}, {"value": "85868"}, {"value": "87843"}, {"value": "90039"}, {"value": "61452"}, {"value": "64463"}, {"value": "65945"}, {"value": "68056"}, {"value": "70914"}, {"value": "73467"}, {"value": "76332"}, {"value": "78927"}, {"value": "81927"}]
                                                    }, {
                                                        "seriesname": "Product B",
                                                        "data": [{"value": "15500"}, {"value": "16361"}, {"value": "17212"}, {"value": "18443"}, {"value": "15500"}, {"value": "16361"}, {"value": "17212"}, {"value": "18443"}, {"value": "19270"}, {"value": "20782"}, {"value": "21128"}, {"value": "22606"}, {"value": "23915"}, {"value": "24982"}, {"value": "26436"}, {"value": "26810"}, {"value": "27421"}, {"value": "28162"}, {"value": "30000"}, {"value": "28965"}, {"value": "29767"}, {"value": "29559"}, {"value": "26150"}, {"value": "28349"}, {"value": "28676"}, {"value": "29221"}, {"value": "30681"}, {"value": "31665"}, {"value": "33109"}, {"value": "34062"}, {"value": "35849"}]
                                                    }, {
                                                        "seriesname": "Product C",
                                                        "data": [{"value": "38000"}, {"value": "39330"}, {"value": "40746"}, {"value": "42335"}, {"value": "38000"}, {"value": "39330"}, {"value": "40746"}, {"value": "42335"}, {"value": "43774"}, {"value": "45438"}, {"value": "46665"}, {"value": "48531"}, {"value": "50181"}, {"value": "51637"}, {"value": "53031"}, {"value": "53826"}, {"value": "54849"}, {"value": "56001"}, {"value": "57233"}, {"value": "57245"}, {"value": "58562"}, {"value": "60026"}, {"value": "40968"}, {"value": "42975"}, {"value": "43964"}, {}, {}, {}, {}, {}, {}]
                                                    }]
                                                }
                                            }
                                        },
                                        {
                                            id: 'ood.UI.FCsl05',
                                            key: 'ood.UI.FusionChartsXT',
                                            caption: '$(RAD.widgets.fc.ScrollCombi 2D)',
                                            imageClass: 'ri ri-bar-chart-2-line',
                                            draggable: true,
                                            iniProp: {
                                                chartType: "ScrollCombi2D",
                                                dock: 'fill',
                                                JSONData: {
                                                    "chart": {
                                                        "caption": "Hits per hour",
                                                        "linethickness": "2",
                                                        "showvalues": "0",
                                                        "areaovercolumns": "0",
                                                        "formatnumberscale": "0",
                                                        "useroundedges": "1",
                                                        "palette": "2",
                                                        "legendborderalpha": "0"
                                                    },
                                                    "categories": [{"category": [{"label": "00:00"}, {"label": "01:00"}, {"label": "02:00"}, {"label": "03:00"}, {"label": "04:00"}, {"label": "05:00"}, {"label": "06:00"}, {"label": "07:00"}, {"label": "08:00"}, {"label": "09:00"}, {"label": "10:00"}, {"label": "11:00"}, {"label": "12:00"}, {"label": "13:00"}, {"label": "14:00"}, {"label": "15:00"}, {"label": "16:00"}, {"label": "17:00"}, {"label": "18:00"}, {"label": "19:00"}, {"label": "20:00"}, {"label": "21:00"}, {"label": "22:00"}, {"label": "23:00"}]}],
                                                    "dataset": [{
                                                        "seriesname": "Expected",
                                                        "renderas": "Area",
                                                        "alpha": "60",
                                                        "showplotborder": "1",
                                                        "plotbordercolor": "0372ab",
                                                        "data": [{"value": "498"}, {"value": "512"}, {"value": "592"}, {"value": "619"}, {"value": "664"}, {"value": "782"}, {"value": "665"}, {"value": "833"}, {"value": "1259"}, {"value": "1623"}, {"value": "1867"}, {"value": "2198"}, {"value": "1112"}, {"value": "1192"}, {"value": "1219"}, {"value": "2264"}, {"value": "2282"}, {"value": "2365"}, {"value": "2433"}, {"value": "2559"}, {"value": "2823"}, {"value": "2867"}, {"value": "2867"}, {"value": "2867"}]
                                                    }, {
                                                        "seriesname": "Actuals - Monday",
                                                        "data": [{"value": "336"}, {"value": "371"}, {"value": "485"}, {"value": "592"}, {"value": "601"}, {"value": "716"}, {"value": "864"}, {"value": "880"}, {"value": "992"}, {"value": "1062"}, {"value": "1119"}, {"value": "1089"}, {"value": "1212"}, {"value": "904"}, {"value": "1215"}, {"value": "1358"}, {"value": "1482"}, {"value": "1666"}, {"value": "1811"}, {"value": "2051"}, {"value": "2138"}, {"value": "2209"}, {"value": "2247"}, {"value": "2301"}]
                                                    }, {
                                                        "seriesname": "Actuals - Tuesday",
                                                        "data": [{"value": "854"}, {"value": "965"}, {"value": "1175"}, {"value": "1190"}, {"value": "1212"}, {"value": "1241"}, {"value": "1308"}, {"value": "1401"}, {"value": "1481"}, {"value": "1851"}, {"value": "1250"}, {"value": "2415"}, {"value": "2886"}, {"value": "3252"}, {"value": "3673"}, {"value": "4026"}, {"value": "4470"}, {"value": "4813"}, {"value": "4961"}, {"value": "5086"}, {"value": "5284"}, {"value": "5391"}, {"value": "5657"}, {"value": "5847"}]
                                                    }, {
                                                        "seriesname": "Average",
                                                        "renderas": "Line",
                                                        "color": "0372AB",
                                                        "linethickness": "2",
                                                        "data": [{"value": "511"}, {"value": "520"}, {"value": "628"}, {"value": "640"}, {"value": "746"}, {"value": "757"}, {"value": "890"}, {"value": "1050"}, {"value": "1099"}, {"value": "1191"}, {"value": "1252"}, {"value": "1448"}, {"value": "1771"}, {"value": "2316"}, {"value": "2763"}, {"value": "3149"}, {"value": "3637"}, {"value": "4015"}, {"value": "4262"}, {"value": "4541"}, {"value": "4837"}, {"value": "5016"}, {"value": "5133"}, {"value": "5278"}]
                                                    }],
                                                    "trendlines": [{
                                                        "line": [{
                                                            "startvalue": "4500",
                                                            "color": "359A35",
                                                            "displayvalue": "Good",
                                                            "valueonright": "1"
                                                        }]
                                                    }]
                                                }
                                            }
                                        },
                                        {
                                            id: 'ood.UI.FCsl06',
                                            key: 'ood.UI.FusionChartsXT',
                                            caption: '$(RAD.widgets.fc.ScrollCombiDY 2D)',
                                            imageClass: 'ri ri-bar-chart-line',
                                            draggable: true,
                                            iniProp: {
                                                chartType: "ScrollCombiDY2D",
                                                dock: 'fill',
                                                JSONData: {
                                                    "chart": {
                                                        "palette": "2",
                                                        "caption": "Sales by Product",
                                                        "subcaption": "March 2006",
                                                        "showvalues": "0",
                                                        "divlinedecimalprecision": "1",
                                                        "limitsdecimalprecision": "1",
                                                        "pyaxisname": "Amount",
                                                        "syaxisname": "Quantity",
                                                        "numberprefix": "$",
                                                        "formatnumberscale": "0",
                                                        "numvisibleplot": "5"
                                                    },
                                                    "categories": [{"category": [{"label": "A"}, {"label": "B"}, {"label": "C"}, {"label": "D"}, {"label": "E"}, {"label": "F"}, {"label": "G"}, {"label": "H"}, {"label": "I"}, {"label": "J"}]}],
                                                    "dataset": [{
                                                        "seriesname": "Revenue",
                                                        "data": [{"value": "5854"}, {"value": "4171"}, {"value": "1375"}, {"value": "1875"}, {"value": "2246"}, {"value": "2696"}, {"value": "1287"}, {"value": "2140"}, {"value": "1603"}, {"value": "1628"}]
                                                    }, {
                                                        "seriesname": "Profit",
                                                        "renderas": "Area",
                                                        "parentyaxis": "P",
                                                        "data": [{"value": "3242"}, {"value": "3171"}, {"value": "700"}, {"value": "1287"}, {"value": "1856"}, {"value": "1126"}, {"value": "987"}, {"value": "1610"}, {"value": "903"}, {"value": "928"}]
                                                    }, {
                                                        "linethickness": "3",
                                                        "seriesname": "Quantity",
                                                        "parentyaxis": "S",
                                                        "data": [{"value": "174"}, {"value": "197"}, {"value": "155"}, {"value": "15"}, {"value": "66"}, {"value": "85"}, {"value": "37"}, {"value": "10"}, {"value": "44"}, {"value": "322"}]
                                                    }]
                                                }
                                            }
                                        }
                                    ]
                            },
                            {
                                id: 'ood.FusionChartsXT.7',
                                key: 'ood.FusionChartsXT.7',
                                caption: '$RAD.widgets.group.Funnel',
                                group: true,
                                imageClass: 'ri ri-filter-line',
                                sub:
                                    [
                                        {
                                            id: 'ood.UI.FCfu01',
                                            key: 'ood.UI.FusionChartsXT',
                                            caption: '$(RAD.widgets.fc.Funnel) 2D',
                                            imageClass: 'ri ri-filter-2-line',
                                            draggable: true,
                                            iniProp: {
                                                chartType: "Funnel",
                                                dock: 'fill',
                                                JSONData: {
                                                    "chart": {
                                                        "caption": "Sales distribution by Employee",
                                                        "subcaption": "Jan 07 - Jul 07",
                                                        "numberprefix": "$",
                                                        "is2d": "1",
                                                        "issliced": "1",
                                                        "showplotborder": "1",
                                                        "plotborderthickness": "1",
                                                        "plotbordercolor": "000000",
                                                        "streamlineddata": "0",
                                                        "showBorder": "0"
                                                    },
                                                    "data": [{"label": "Buchanan", "value": "50000"}, {
                                                        "label": "Callahan",
                                                        "value": "49000"
                                                    }, {"label": "Davolio", "value": "63000"}, {
                                                        "label": "Dodsworth",
                                                        "value": "41000"
                                                    }, {"label": "Fuller", "value": "74000"}, {
                                                        "label": "King",
                                                        "value": "49000"
                                                    }, {"label": "Leverling", "value": "77000"}]
                                                }
                                            }
                                        },
                                        {
                                            id: 'ood.UI.FCfu02',
                                            key: 'ood.UI.FusionChartsXT',
                                            caption: '$(RAD.widgets.fc.Funnel) 3D',
                                            imageClass: 'ri ri-filter-2-line',
                                            draggable: true,
                                            iniProp: {
                                                chartType: "Funnel",
                                                JSONData: {
                                                    "chart": {
                                                        "bgcolor": "FFFFFF",
                                                        "caption": "Conversion Funnel - 2013",
                                                        "decimals": "1",
                                                        "basefontsize": "11",
                                                        "issliced": "0",
                                                        "ishollow": "1",
                                                        "labeldistance": "8",
                                                        "showBorder": "0"
                                                    },
                                                    "data": [{"label": "Website Visits", "value": "385634"}, {
                                                        "label": "Downloads",
                                                        "value": "145631",
                                                        "color": "008ee4"
                                                    }, {
                                                        "label": "Interested to buy",
                                                        "value": "84564",
                                                        "color": "f8bd19"
                                                    }, {
                                                        "label": "Contract finalized",
                                                        "value": "50654",
                                                        "color": "6baa01"
                                                    }, {"label": "Purchased", "value": "25342", "color": "e44a00"}]
                                                }
                                            }
                                        }
                                    ]
                            },
                            {
                                id: 'ood.FusionChartsXT.7.1',
                                key: 'ood.FusionChartsXT.7.1',
                                caption: '$RAD.widgets.group.Pyramid',
                                group: true,
                                imageClass: 'ri ri-arrow-up-line',
                                sub:
                                    [
                                        {
                                            id: 'ood.UI.FCpyd01',
                                            key: 'ood.UI.FusionChartsXT',
                                            caption: '$(RAD.widgets.fc.Pyramid) 1',
                                            imageClass: 'ri ri-triangle-line',
                                            draggable: true,
                                            iniProp: {
                                                chartType: "Pyramid",
                                                JSONData: {
                                                    "chart": {
                                                        "bgcolor": "FFFFFF",
                                                        "caption": "Revenue distribution for 2013",
                                                        "basefontcolor": "333333",
                                                        "decimals": "0",
                                                        "numbersuffix": "M",
                                                        "numberprefix": "$",
                                                        "pyramidyscale": "40",
                                                        "chartbottommargin": "0",
                                                        "captionpadding": "0",
                                                        "showBorder": "0"
                                                    },
                                                    "data": [{"value": "17", "name": "Products", "color": "008ee4"}, {
                                                        "value": "21",
                                                        "name": "Services",
                                                        "color": "6baa01"
                                                    }, {"value": "20", "name": "Consultancy", "color": "f8bd19"}, {
                                                        "value": "5",
                                                        "name": "Others",
                                                        "color": "e44a00"
                                                    }]
                                                }
                                            }
                                        },
                                        {
                                            id: 'ood.UI.FCpyd02',
                                            key: 'ood.UI.FusionChartsXT',
                                            caption: '$(RAD.widgets.fc.Pyramid) 2',
                                            imageClass: 'ri ri-triangle-line',
                                            draggable: true,
                                            iniProp: {
                                                chartType: "Pyramid",
                                                dock: 'fill',
                                                JSONData: {
                                                    "chart": {
                                                        "bgcolor": "CCCCCC,FFFFFF",
                                                        "caption": "Population Distribution",
                                                        "basefontcolor": "333333",
                                                        "decimals": "0",
                                                        "numbersuffix": "%",
                                                        "pyramidyscale": "40",
                                                        "chartbottommargin": "0",
                                                        "captionpadding": "0",
                                                        "showBorder": "0"
                                                    },
                                                    "data": [{
                                                        "value": "17",
                                                        "name": " East Asia & Pacific",
                                                        "color": "FFCC33"
                                                    }, {
                                                        "value": "21",
                                                        "name": " Europe & Central Asia",
                                                        "color": "339900"
                                                    }, {
                                                        "value": "20",
                                                        "name": " Latin America & Carib.",
                                                        "color": "0066CC",
                                                        "issliced": "1"
                                                    }, {
                                                        "value": "14",
                                                        "name": " Middle East & North Africa",
                                                        "color": "D95000"
                                                    }, {"value": "16", "name": " South Asia", "color": "9B72CF"}, {
                                                        "value": "12",
                                                        "name": " Sub-Saharan Africa",
                                                        "color": "A10303"
                                                    }]
                                                }
                                            }
                                        },
                                        {
                                            id: 'ood.UI.FCpyd03',
                                            key: 'ood.UI.FusionChartsXT',
                                            caption: '$(RAD.widgets.fc.Pyramid) 3',
                                            imageClass: 'ri ri-triangle-line',
                                            draggable: true,
                                            iniProp: {
                                                chartType: "Pyramid",
                                                JSONData: {
                                                    "chart": {
                                                        "bgcolor": "FFFFFF",
                                                        "bgalpha": "100",
                                                        "caption": "Sales distribution by Employee",
                                                        "subcaption": "Jan 07 - Jul 07",
                                                        "numberprefix": "$",
                                                        "is2d": "1",
                                                        "issliced": "1",
                                                        "showplotborder": "1",
                                                        "plotborderthickness": "1",
                                                        "plotborderalpha": "100",
                                                        "plotbordercolor": "FFFFFF",
                                                        "enablesmartlabels": "1",
                                                        "showBorder": "0"
                                                    },
                                                    "data": [{"label": "Buchanan", "value": "50000"}, {
                                                        "label": "Callahan",
                                                        "value": "49000"
                                                    }, {"label": "Davolio", "value": "63000"}, {
                                                        "label": "Dodsworth",
                                                        "value": "41000"
                                                    }, {"label": "Fuller", "value": "74000"}, {
                                                        "label": "King",
                                                        "value": "49000"
                                                    }, {"label": "Leverling", "value": "77000"}, {
                                                        "label": "Peacock",
                                                        "value": "54000"
                                                    }, {"label": "Suyama", "value": "14000"}]
                                                }
                                            }
                                        },
                                        {
                                            id: 'ood.UI.FCpyd04',
                                            key: 'ood.UI.FusionChartsXT',
                                            caption: '$(RAD.widgets.fc.Pyramid) 4',
                                            imageClass: 'ri ri-triangle-line',
                                            draggable: true,
                                            iniProp: {
                                                chartType: "Pyramid",
                                                JSONData: {
                                                    "chart": {
                                                        "manageresize": "1",
                                                        "origw": "500",
                                                        "origh": "350",
                                                        "bgcolor": "FFFFFF",
                                                        "showborder": "0",
                                                        "showvalues": "0",
                                                        "showlabels": "0",
                                                        "issliced": "1",
                                                        "chartleftmargin": "10",
                                                        "chartrightmargin": "190",
                                                        "showtooltip": "1",
                                                        "annrenderdelay": "1.5"
                                                    },
                                                    "data": [{
                                                        "value": "10",
                                                        "label": "",
                                                        "color": "AFD8F8",
                                                        "tooltext": "Stocks(speculative){BR}Options(uncovered){BR}Margin Accounts{BR}Limited Partnerships"
                                                    }, {
                                                        "value": "16",
                                                        "label": "",
                                                        "color": "8BBA00",
                                                        "tooltext": "Corporate Bond Mutual Fund{BR}Stock Market Funds{BR}Blue Cip Stocks{BR}Investment Grade Bonds"
                                                    }, {
                                                        "value": "18",
                                                        "label": "",
                                                        "color": "A66EDD",
                                                        "tooltext": "Money Market, Government and{BR}Municipal Bond Mutual Funds{BR}Government Securities{BR}Unit Investment Trusts"
                                                    }, {
                                                        "value": "22",
                                                        "label": "",
                                                        "color": "F984A1",
                                                        "tooltext": "Certificates of deposits{BR}(CDs) (FDIC insured){BR}Bank Money Market{BR}Money Market Mutual Funds"
                                                    }],
                                                    "annotations": {
                                                        "groups": [{
                                                            "showbelow": "1",
                                                            "constrainedscale": "0",
                                                            "items": [{
                                                                "type": "rectangle",
                                                                "x": "$chartStartX+2",
                                                                "y": "$chartStartY+2",
                                                                "tox": "$chartEndX-2",
                                                                "toy": "$chartEndY-2",
                                                                "fillalpha": "0",
                                                                "radius": "15",
                                                                "showborder": "0",
                                                                "borderthickness": "2",
                                                                "color": "333333",
                                                                "borderalpha": "100"
                                                            }]
                                                        }, {
                                                            "showbelow": "0",
                                                            "x": "$canvasCenterX",
                                                            "constrainedscale": "0",
                                                            "items": [{
                                                                "type": "circle",
                                                                "y": "50",
                                                                "radius": "5",
                                                                "borderthickness": "1",
                                                                "color": "333333"
                                                            }, {
                                                                "type": "line",
                                                                "y": "50",
                                                                "tox": "55",
                                                                "borderthickness": "1",
                                                                "color": "333333"
                                                            }, {
                                                                "type": "line",
                                                                "x": "55",
                                                                "y": "25",
                                                                "toy": "75",
                                                                "color": "333333",
                                                                "borderthickness": "1"
                                                            }, {
                                                                "type": "circle",
                                                                "y": "110",
                                                                "radius": "5",
                                                                "color": "333333",
                                                                "borderthickness": "1"
                                                            }, {
                                                                "type": "line",
                                                                "y": "110",
                                                                "tox": "85",
                                                                "color": "333333",
                                                                "borderthickness": "1"
                                                            }, {
                                                                "type": "line",
                                                                "x": "85",
                                                                "y": "85",
                                                                "toy": "135",
                                                                "color": "333333",
                                                                "borderthickness": "1"
                                                            }, {
                                                                "type": "circle",
                                                                "y": "180",
                                                                "radius": "5",
                                                                "color": "333333",
                                                                "borderthickness": "1"
                                                            }, {
                                                                "type": "line",
                                                                "y": "180",
                                                                "tox": "105",
                                                                "color": "333333",
                                                                "borderthickness": "1"
                                                            }, {
                                                                "type": "line",
                                                                "x": "105",
                                                                "y": "155",
                                                                "toy": "205",
                                                                "color": "333333",
                                                                "borderthickness": "1"
                                                            }, {
                                                                "type": "circle",
                                                                "y": "280",
                                                                "radius": "5",
                                                                "color": "333333",
                                                                "borderthickness": "1"
                                                            }, {
                                                                "type": "line",
                                                                "y": "280",
                                                                "tox": "155",
                                                                "color": "333333",
                                                                "borderthickness": "1"
                                                            }, {
                                                                "type": "line",
                                                                "x": "155",
                                                                "y": "255",
                                                                "toy": "305",
                                                                "color": "333333",
                                                                "borderthickness": "1"
                                                            }, {
                                                                "type": "text",
                                                                "x": "60",
                                                                "y": "50",
                                                                "bold": "1",
                                                                "ishtml": "1",
                                                                "label": "Stocks(speculative){BR}Options(uncovered){BR}Margin Accounts{BR}Limited Partnerships",
                                                                "align": "left",
                                                                "color": "333333"
                                                            }, {
                                                                "type": "text",
                                                                "x": "90",
                                                                "y": "110",
                                                                "bold": "1",
                                                                "label": "Corporate Bond Mutual Fund{BR}Stock Market Funds{BR}Blue Cip Stocks{BR}Investment Grade Bonds",
                                                                "align": "left",
                                                                "color": "333333"
                                                            }, {
                                                                "type": "text",
                                                                "x": "110",
                                                                "y": "180",
                                                                "bold": "1",
                                                                "label": "Money Market, Government and{BR}Municipal Bond Mutual Funds{BR}Government Securities{BR}Unit Investment Trusts",
                                                                "align": "left",
                                                                "color": "333333"
                                                            }, {
                                                                "type": "text",
                                                                "x": "160",
                                                                "y": "280",
                                                                "bold": "1",
                                                                "label": "Certificates of deposits{BR}(CDs) (FDIC insured){BR}Bank Money Market{BR}Money Market Mutual Funds",
                                                                "align": "left",
                                                                "color": "333333"
                                                            }]
                                                        }]
                                                    },
                                                    "styles": {
                                                        "definition": [{
                                                            "name": "TTipFont",
                                                            "type": "font",
                                                            "ishtml": "1"
                                                        }],
                                                        "application": [{"toobject": "TOOLTIP", "styles": "TTipFont"}]
                                                    }
                                                }
                                            }
                                        }
                                    ]
                            },

                            {
                                id: 'ood.FusionChartsXT.7.2',
                                key: 'ood.FusionChartsXT.7.2',
                                caption: '$RAD.widgets.group.Radar',
                                group: true,
                                imageClass: 'ri ri-focus-line',
                                sub:
                                    [
                                        {
                                            id: 'ood.UI.FC_rad01',
                                            key: 'ood.UI.FusionChartsXT',
                                            caption: '$(RAD.widgets.fc.Radar) 1',
                                            imageClass: 'ri ri-radar-line',
                                            draggable: true,
                                            iniProp: {
                                                chartType: "Radar",
                                                dock: 'fill',
                                                JSONData: {
                                                    "chart": {
                                                        "caption": "Comparison of Enterprise Level Antivirus Software",
                                                        "canvasborderalpha": "0",
                                                        "radarborderalpha": "50",
                                                        "radarborderthickness": "1",
                                                        "radarfillcolor": "FFFFFF",
                                                        "showlabels": "1",
                                                        "drawanchors": "0",
                                                        "ymaxvalue": "10",
                                                        "showlimits": "0",
                                                        "bgcolor": "FFFFFF",
                                                        "legendborderalpha": "0",
                                                        "showborder": "0"
                                                    },
                                                    "categories": [{"category": [{"label": "Centralized Deployability"}, {"label": "Centralized Signature Updating"}, {"label": "Active Firewall"}, {"label": "Centralized Access & Firewall"}, {"label": "Centralized Network Health Monitoring"}, {"label": "Cost"}, {"label": "Technical Support"}]}],
                                                    "dataset": [{
                                                        "seriesname": "Kaspersky",
                                                        "color": "008ee4",
                                                        "alpha": "40",
                                                        "data": [{"value": "8"}, {"value": "9"}, {"value": "9"}, {"value": "8"}, {"value": "7"}, {"value": "9"}, {"value": "8"}]
                                                    }, {
                                                        "seriesname": "Norton",
                                                        "color": "6baa01",
                                                        "alpha": "40",
                                                        "data": [{"value": "7"}, {"value": "6"}, {"value": "6"}, {"value": "4"}, {"value": "7"}, {"value": "6"}, {"value": "5"}]
                                                    }]
                                                }
                                            }
                                        },
                                        {
                                            id: 'ood.UI.FC_rad02',
                                            key: 'ood.UI.FusionChartsXT',
                                            caption: '$(RAD.widgets.fc.Radar) 2',
                                            imageClass: 'ri ri-radar-line',
                                            draggable: true,
                                            iniProp: {
                                                chartType: "Radar",
                                                dock: 'fill',
                                                JSONData: {
                                                    "chart": {
                                                        "caption": "Variance Analysis",
                                                        "bgcolor": "FFFFFF",
                                                        "radarfillcolor": "FFFFFF",
                                                        "plotfillalpha": "5",
                                                        "plotborderthickness": "2",
                                                        "anchoralpha": "100",
                                                        "numberprefix": "$",
                                                        "numdivlines": "2",
                                                        "legendposition": "RIGHT",
                                                        "showborder": "0"
                                                    },
                                                    "categories": [{
                                                        "font": "Arial",
                                                        "fontsize": "11",
                                                        "category": [{"label": "Jan"}, {"label": "Feb"}, {"label": "Mar"}, {"label": "Apr"}, {"label": "May"}, {"label": "Jun"}, {"label": "Jul"}, {"label": "Aug"}, {"label": "Sep"}, {"label": "Oct"}, {"label": "Nov"}, {"label": "Dec"}]
                                                    }],
                                                    "dataset": [{
                                                        "seriesname": "Products",
                                                        "color": "CD6AC0",
                                                        "anchorsides": "6",
                                                        "anchorradius": "2",
                                                        "anchorbordercolor": "CD6AC0",
                                                        "anchorbgalpha": "0",
                                                        "data": [{"value": "1127654"}, {"value": "1226234"}, {"value": "1299456"}, {"value": "1311565"}, {"value": "1324454"}, {"value": "1357654"}, {"value": "1296234"}, {"value": "1359456"}, {"value": "1391565"}, {"value": "1414454"}, {"value": "1671565"}, {"value": "1134454"}]
                                                    }, {
                                                        "seriesname": "Services",
                                                        "color": "0099FF",
                                                        "anchorsides": "10",
                                                        "anchorbordercolor": "0099FF",
                                                        "anchorbgalpha": "0",
                                                        "anchorradius": "2",
                                                        "data": [{"value": "534241"}, {"value": "556728"}, {"value": "575619"}, {"value": "676713"}, {"value": "665520"}, {"value": "634241"}, {"value": "656728"}, {"value": "675619"}, {"value": "776713"}, {"value": "865520"}, {"value": "976713"}, {"value": "665520"}]
                                                    }]
                                                }
                                            }
                                        },
                                        {
                                            id: 'ood.UI.FC_rad03',
                                            key: 'ood.UI.FusionChartsXT',
                                            caption: '$(RAD.widgets.fc.Radar) 3',
                                            imageClass: 'ri ri-radar-line',
                                            draggable: true,
                                            iniProp: {
                                                chartType: "Radar",
                                                dock: 'fill',
                                                JSONData: {
                                                    "chart": {
                                                        "caption": "Radar Chart",
                                                        "anchoralpha": "0",
                                                        "showborder": "0"
                                                    },
                                                    "categories": [{"category": [{"label": "Index 1"}, {"label": "Index 2"}, {"label": "Index 3"}, {"label": "Index 4"}, {"label": "Index 5"}, {"label": "Index 6"}, {"label": "Index 7"}, {"label": "Index 8"}, {"label": "Index 9"}, {"label": "Index 10"}, {"label": "Index 11"}]}],
                                                    "dataset": [{
                                                        "seriesname": "Series 1",
                                                        "data": [{"value": "9"}, {"value": "9"}, {"value": "9"}, {"value": "7"}, {"value": "8"}, {"value": "8"}, {"value": "9"}, {"value": "9"}, {"value": "9"}, {"value": "7"}, {"value": "8"}]
                                                    }, {
                                                        "seriesname": "Series 2",
                                                        "data": [{"value": "5"}, {"value": "3"}, {"value": "2"}, {"value": "4"}, {"value": "5"}, {"value": "9"}, {"value": "5"}, {"value": "3"}, {"value": "2"}, {"value": "4"}, {"value": "5"}]
                                                    }]
                                                }
                                            }
                                        }
                                    ]
                            },
                            {
                                id: 'ood.FusionChartsXT.8',
                                key: 'ood.FusionChartsXT.8',
                                caption: '$(RAD.widgets.group.Real$-time)',
                                group: true,
                                imageClass: 'ri ri-time-line',
                                sub:
                                    [
                                        {
                                            id: 'ood.UI.FC_rtc01',
                                            key: 'ood.UI.FusionChartsXT',
                                            caption: '$(RAD.widgets.fc.Line)',
                                            imageClass: 'ri ri-line-chart-line',
                                            draggable: true,
                                            iniProp: {
                                                chartType: "RealTimeLine",
                                                dock: 'fill',
                                                JSONData: {
                                                    "chart": {
                                                        "caption": "Real-time stock price monitor",
                                                        "subCaption": "Harry's SuperMart",
                                                        "xAxisName": "Time",
                                                        "yAxisName": "Stock Price",
                                                        "numberPrefix": "$",
                                                        "refreshinterval": "5",
                                                        "yaxisminvalue": "35",
                                                        "yaxismaxvalue": "36",
                                                        "numdisplaysets": "10",
                                                        "labeldisplay": "rotate",
                                                        "showValues": "0",
                                                        "showRealTimeValue": "0",
                                                        "theme": "fint"
                                                    },
                                                    "categories": [{"category": [{"label": "Day Start"}]}],
                                                    "dataset": [{"data": [{"value": "35.27"}]}]
                                                }
                                            }
                                        },
                                        {
                                            id: 'ood.UI.FC_rtc02',
                                            key: 'ood.UI.FusionChartsXT',
                                            caption: '$(RAD.widgets.fc.Area)',
                                            imageClass: 'ri ri-bar-chart-line',
                                            draggable: true,
                                            iniProp: {
                                                chartType: "RealTimeArea",
                                                dock: 'fill',
                                                JSONData: {
                                                    "chart": {
                                                        "caption": "Real-time stock price monitor",
                                                        "subCaption": "Harry's SuperMart",
                                                        "xAxisName": "Time",
                                                        "yAxisName": "Stock Price",
                                                        "numberPrefix": "$",
                                                        "refreshinterval": "5",
                                                        "yaxisminvalue": "35",
                                                        "yaxismaxvalue": "36",
                                                        "numdisplaysets": "10",
                                                        "labeldisplay": "rotate",
                                                        "showValues": "0",
                                                        "showRealTimeValue": "0",
                                                        "paletteColors": "#0075c2,#1aaf5d",
                                                        "baseFontColor": "#333333",
                                                        "baseFont": "Helvetica Neue,Arial",
                                                        "captionFontSize": "14",
                                                        "subcaptionFontSize": "14",
                                                        "subcaptionFontBold": "0",
                                                        "showBorder": "0",
                                                        "bgColor": "#ffffff",
                                                        "showShadow": "0",
                                                        "canvasBgColor": "#ffffff",
                                                        "canvasBorderAlpha": "0",
                                                        "divlineAlpha": "100",
                                                        "divlineColor": "#999999",
                                                        "divlineThickness": "1",
                                                        "divLineDashed": "1",
                                                        "divLineDashLen": "1",
                                                        "divLineGapLen": "1",
                                                        "usePlotGradientColor": "0",
                                                        "showplotborder": "0",
                                                        "showXAxisLine": "1",
                                                        "xAxisLineThickness": "1",
                                                        "xAxisLineColor": "#999999",
                                                        "showAlternateHGridColor": "0"
                                                    },
                                                    "categories": [{"category": [{"label": "Day Start"}]}],
                                                    "dataset": [{"data": [{"value": "35.27"}]}]
                                                }
                                            }
                                        },
                                        {
                                            id: 'ood.UI.FC_rtc03',
                                            key: 'ood.UI.FusionChartsXT',
                                            caption: '$(RAD.widgets.fc.Column)',
                                            imageClass: 'ri ri-bar-chart-2-line',
                                            draggable: true,
                                            iniProp: {
                                                chartType: "RealTimeColumn",
                                                dock: 'fill',
                                                JSONData: {
                                                    "chart": {
                                                        "caption": "Online Transactions per 10 seconds",
                                                        "subCaption": "HarrysSupermart.com",
                                                        "showrealtimevalue": "1",
                                                        "yaxismaxvalue": "10",
                                                        "numdisplaysets": "10",
                                                        "labeldisplay": "rotate",
                                                        "slantLabels": "1",
                                                        "showLegend": "0",
                                                        "showValues": "0",
                                                        "numbersuffix": " Transactions",
                                                        "showlabels": "1",
                                                        "showRealTimeValue": "0",
                                                        "paletteColors": "#0075c2,#1aaf5d",
                                                        "baseFontColor": "#333333",
                                                        "baseFont": "Helvetica Neue,Arial",
                                                        "captionFontSize": "14",
                                                        "subcaptionFontSize": "14",
                                                        "subcaptionFontBold": "0",
                                                        "showBorder": "0",
                                                        "bgColor": "#ffffff",
                                                        "showShadow": "0",
                                                        "canvasBgColor": "#ffffff",
                                                        "canvasBorderAlpha": "0",
                                                        "divlineAlpha": "100",
                                                        "divlineColor": "#999999",
                                                        "divlineThickness": "1",
                                                        "divLineDashed": "1",
                                                        "divLineDashLen": "1",
                                                        "divLineGapLen": "1",
                                                        "usePlotGradientColor": "0",
                                                        "showplotborder": "0",
                                                        "valueFontColor": "#ffffff",
                                                        "placeValuesInside": "1",
                                                        "rotateValues": "1",
                                                        "showXAxisLine": "1",
                                                        "xAxisLineThickness": "1",
                                                        "xAxisLineColor": "#999999",
                                                        "showAlternateHGridColor": "0",
                                                        "legendBgAlpha": "0",
                                                        "legendBorderAlpha": "0",
                                                        "legendShadow": "0",
                                                        "legendItemFontSize": "10",
                                                        "legendItemFontColor": "#666666"
                                                    },
                                                    "categories": [{"category": [{"label": "Start"}]}],
                                                    "dataset": [{
                                                        "seriesname": "",
                                                        "alpha": "100",
                                                        "data": [{"value": "3"}]
                                                    }]
                                                }
                                            }
                                        },
                                        {
                                            id: 'ood.UI.FC_rtc04',
                                            key: 'ood.UI.FusionChartsXT',
                                            caption: '$(RAD.widgets.fc.Dual Y Line)',
                                            imageClass: 'ri ri-line-chart-line',
                                            draggable: true,
                                            iniProp: {
                                                chartType: "RealTimeLineDY",
                                                dock: 'fill',
                                                JSONData: {
                                                    "chart": {
                                                        "caption": "Stock Price Monitor",
                                                        "subCaption": "Harry's Supermart",
                                                        "captionFontSize": "14",
                                                        "subcaptionFontSize": "14",
                                                        "baseFontColor": "#333333",
                                                        "baseFont": "Helvetica Neue,Arial",
                                                        "subcaptionFontBold": "0",
                                                        "paletteColors": "#0075c2,#1aaf5d,#f2c500",
                                                        "bgColor": "#ffffff",
                                                        "canvasBgColor": "#ffffff",
                                                        "showBorder": "0",
                                                        "showShadow": "0",
                                                        "showCanvasBorder": "0",
                                                        "showRealTimeValue": "0",
                                                        "legendBorderAlpha": "0",
                                                        "legendShadow": "0",
                                                        "numberprefix": "$",
                                                        "setadaptiveymin": "1",
                                                        "setadaptivesymin": "1",
                                                        "xaxisname": "Time",
                                                        "labeldisplay": "Rotate",
                                                        "slantlabels": "1",
                                                        "pyaxisminvalue": "35",
                                                        "pyaxismaxvalue": "36",
                                                        "syaxisminvalue": "10000",
                                                        "syaxismaxvalue": "12000",
                                                        "divlineAlpha": "100",
                                                        "divlineColor": "#999999",
                                                        "showAlternateHGridColor": "0",
                                                        "divlineThickness": "1",
                                                        "divLineDashed": "1",
                                                        "divLineDashLen": "1",
                                                        "divLineGapLen": "1",
                                                        "numDisplaySets": "10"
                                                    },
                                                    "categories": [{"category": [{"label": "Day Start"}]}],
                                                    "dataset": [{
                                                        "seriesname": "HRYS Price",
                                                        "showvalues": "0",
                                                        "data": [{"value": "35.1"}]
                                                    }, {
                                                        "seriesname": "NYSE Index",
                                                        "showvalues": "0",
                                                        "parentyaxis": "S",
                                                        "data": [{"value": "10962.87"}]
                                                    }],
                                                    "trendlines": [{
                                                        "line": [{
                                                            "parentyaxis": "P",
                                                            "startvalue": "35.1",
                                                            "displayvalue": "Open",
                                                            "thickness": "1",
                                                            "color": "#0075c2",
                                                            "dashed": "1"
                                                        }, {
                                                            "parentyaxis": "S",
                                                            "startvalue": "10962.87",
                                                            "displayvalue": "Open",
                                                            "thickness": "1",
                                                            "color": "#1aaf5d",
                                                            "dashed": "1"
                                                        }]
                                                    }]
                                                }
                                            }
                                        },
                                        {
                                            id: 'ood.UI.FC_rtc05',
                                            key: 'ood.UI.FusionChartsXT',
                                            caption: '$(RAD.widgets.fc.Stacked Area)',
                                            imageClass: 'ri ri-bar-chart-2-line',
                                            draggable: true,
                                            iniProp: {
                                                chartType: "RealTimeStackedArea",
                                                dock: 'fill',
                                                JSONData: {
                                                    "chart": {
                                                        "caption": "Live Visitors on Site",
                                                        "subCaption": "Updated every 5 seconds",
                                                        "xAxisName": "Time",
                                                        "yAxisName": "No. of visitors",
                                                        "numberSuffix": "s",
                                                        "refreshinterval": "5",
                                                        "yaxisminvalue": "0",
                                                        "yaxismaxvalue": "60",
                                                        "numdisplaysets": "10",
                                                        "labeldisplay": "rotate",
                                                        "showValues": "0",
                                                        "showRealTimeValue": "0",
                                                        "paletteColors": "#0075c2,#1aaf5d",
                                                        "baseFontColor": "#333333",
                                                        "baseFont": "Helvetica Neue,Arial",
                                                        "captionFontSize": "14",
                                                        "subcaptionFontSize": "14",
                                                        "subcaptionFontBold": "0",
                                                        "showBorder": "0",
                                                        "bgColor": "#ffffff",
                                                        "showShadow": "0",
                                                        "usePlotGradientColor": "0",
                                                        "showPlotBorder": "0",
                                                        "canvasBgColor": "#ffffff",
                                                        "canvasBorderAlpha": "0",
                                                        "divlineAlpha": "100",
                                                        "divlineColor": "#999999",
                                                        "divlineThickness": "1",
                                                        "divLineDashed": "1",
                                                        "divLineDashLen": "1",
                                                        "divLineGapLen": "1",
                                                        "showXAxisLine": "1",
                                                        "xAxisLineThickness": "1",
                                                        "xAxisLineColor": "#999999",
                                                        "showAlternateHGridColor": "0",
                                                        "legendBgAlpha": "0",
                                                        "legendBorderAlpha": "0",
                                                        "legendShadow": "0",
                                                        "legendItemFontSize": "10",
                                                        "legendItemFontColor": "#666666"
                                                    },
                                                    "categories": [{"category": [{"label": "Day Start"}]}],
                                                    "dataset": [{
                                                        "seriesName": "clothing.hsm.com",
                                                        "data": [{"value": "12"}]
                                                    }, {"seriesName": "food.hsm.com", "data": [{"value": "20"}]}]
                                                }
                                            }
                                        },
                                        {
                                            id: 'ood.UI.FC_rtc06',
                                            key: 'ood.UI.FusionChartsXT',
                                            caption: '$(RAD.widgets.fc.Stacked Column)',
                                            imageClass: 'ri ri-bar-chart-2-line',
                                            draggable: true,
                                            iniProp: {
                                                chartType: "RealTimeStackedColumn",
                                                dock: 'fill',
                                                JSONData: {
                                                    "chart": {
                                                        "caption": "Live Visitors on Site",
                                                        "subCaption": "Every 5 seconds",
                                                        "xAxisName": "Time",
                                                        "yAxisName": "No. of visitors",
                                                        "refreshinterval": "5",
                                                        "numberSuffix": "s",
                                                        "yaxisminvalue": "0",
                                                        "yaxismaxvalue": "60",
                                                        "numdisplaysets": "10",
                                                        "labeldisplay": "rotate",
                                                        "showValues": "1",
                                                        "showRealTimeValue": "0",
                                                        "paletteColors": "#0075c2,#1aaf5d",
                                                        "baseFontColor": "#333333",
                                                        "baseFont": "Helvetica Neue,Arial",
                                                        "captionFontSize": "14",
                                                        "subcaptionFontSize": "14",
                                                        "subcaptionFontBold": "0",
                                                        "showBorder": "0",
                                                        "bgColor": "#ffffff",
                                                        "showShadow": "0",
                                                        "usePlotGradientColor": "0",
                                                        "showPlotBorder": "0",
                                                        "valueFontColor": "#ffffff",
                                                        "placeValuesInside": "1",
                                                        "canvasBgColor": "#ffffff",
                                                        "canvasBorderAlpha": "0",
                                                        "divlineAlpha": "100",
                                                        "divlineColor": "#999999",
                                                        "divlineThickness": "1",
                                                        "divLineDashed": "1",
                                                        "divLineDashLen": "1",
                                                        "divLineGapLen": "1",
                                                        "showXAxisLine": "1",
                                                        "xAxisLineThickness": "1",
                                                        "xAxisLineColor": "#999999",
                                                        "showAlternateHGridColor": "0",
                                                        "legendBgAlpha": "0",
                                                        "legendBorderAlpha": "0",
                                                        "legendShadow": "0",
                                                        "legendItemFontSize": "10",
                                                        "legendItemFontColor": "#666666"
                                                    },
                                                    "categories": [{"category": [{"label": "Day Start"}]}],
                                                    "dataset": [{
                                                        "seriesName": "clothing.hsm.com",
                                                        "data": [{"value": "12"}]
                                                    }, {"seriesName": "food.hsm.com", "data": [{"value": "20"}]}]
                                                }
                                            }
                                        }
                                    ]
                            }


                        ]
                    }
                ]

            return widgetConfig;
        },

        EventHandlers: {
            onFusionChartsEvent: function (profile, eventObject, argumentsObject) {
            },
            onDataClick: function (profile, argsMap, sourceData) {
            },
            onLabelClick: function (profile, argsMap) {
            },
            onAnnotationClick: function (profile, argsMap) {
            },
            onShowTips: null
        },
        _onresize: function (prf, width, height) {
            var size = prf.getSubNode('BOX').cssSize(),
                prop = prf.properties,
                // compare with px
                us = ood.$us(prf),
                adjustunit = function (v, emRate) {
                    return prf.$forceu(v, us > 0 ? 'em' : 'px', emRate)
                },
                root = prf.getRoot(),

                // caculate by px
                ww = width ? prf.$px(width) : width,
                hh = height ? prf.$px(height) : height,
                t;

            if ((width && !ood.compareNumber(size.width, ww, 6)) || (height && !ood.compareNumber(size.height, hh, 6))) {
                // reset here
                if (width) prop.width = adjustunit(ww);
                if (height) prop.height = adjustunit(hh);

                size = {
                    width: width ? prop.width : null,
                    height: height ? prop.height : null
                };
                prf.getSubNode('BOX').cssSize(size, true);
                if (prf.$inDesign || prop.cover) {
                    prf.getSubNode('COVER').cssSize(size, true);
                }
                if (prf.renderId && prf._chartId && (t = FusionCharts(prf._chartId))) {
                    // ensure by px
                    t.resizeTo(ww || void 0, hh || void 0);
                }
            }
        }
    }
});