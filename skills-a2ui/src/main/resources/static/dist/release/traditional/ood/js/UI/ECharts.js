ood.Class("ood.UI.ECharts", "ood.UI", {
    Initialize: function () {
        var dataModals = {};
        for (var i = 0; i < 9; i++) {
            dataModals["dataValue" + (i + 1)] = {
                ini: "",
                dynamic: true,
                get: (function (index) {
                    return function (v) {
                        var echarts = this.boxing().getECharts();
                        if (!echarts) return "";
                        var option = echarts.getOption();
                        if (!option.series) return "[x]";
                        var arr = [], hasData;
                        for (var i = 0, l = option.series.length; i < l; i++) {
                            var data = option.series[i].data;
                            if (data) {
                                if (index < data.length) {
                                    data = data && data[index];
                                    arr.push(ood.isSet(data) ? ood.isHash(data) ? data.value : data : "");
                                    hasData = 1;
                                } else {
                                    arr.push("");
                                }
                            } else {
                                return "";
                            }
                        }
                        return hasData ? arr.join(":") : "";
                    }
                })(i),
                set: (function (index) {
                    return function (v, force) {
                        var echarts = this.boxing().getECharts();
                        if (!echarts) return;
                        var option = echarts.getOption(), updated;
                        if (!option.series) return;

                        var updater = this.properties.optionUpdater, s, d;
                        if (!updater.series) {
                            updater.series = option.series;
                        }
                        var arr = (v + "").split(":");
                        for (var i = 0, l = arr.length; i < l; i++) {
                            if (i >= updater.series.length)
                                updater.series[i] = {
                                    type: updater.series[0] && updater.series[0].type || 'line',
                                    data: []
                                };
                            s = updater.series[i];
                            if (arr[i] !== "") {
                                if (!s.data) s.data = [];
                                d = s.data;
                                v = parseFloat(arr[i]) || 0;
                                if (force || v !== (ood.isHash(d[index]) ? d[index].value : d[index])) {
                                    if (ood.isHash(d[index])) d[index].value = v;
                                    else d[index] = v;
                                    updated = 1;
                                }
                            }
                        }
                        if (updated)
                            this.boxing().setOptionUpdater(updater, true);
                    }
                })(i)
            }
            dataModals["realTimeData" + (i + 1)] = {
                ini: "",
                dynamic: true,
                get: (function (index) {
                    return function (v) {
                        var echarts = this.boxing().getECharts();
                        if (!echarts) return "";
                        var option = echarts.getOption();
                        if (!option.series || !option.xAxis) return "[x]";
                        var values = option.series[index] && option.series[index].data;
                        var dataI = values && values[values.length - 1];
                        return ood.isSet(dataI) ? ood.isHash(dataI) ? dataI.value : dataI : "";
                    }
                })(i),
                set: (function (index) {
                    return function (v) {
                        v = parseFloat(v) || 0;
                        var echarts = this.boxing().getECharts();
                        if (!echarts) return;
                        var option = echarts.getOption();
                        if (!option.series || !option.xAxis) return;

                        var updater = this.properties.optionUpdater, s, d, columnSize = this.properties.realTimePoints;
                        if (!updater.xAxis) {
                            updater.xAxis = option.xAxis;
                        }
                        if (!updater.series) {
                            updater.series = option.series;
                        }
                        var time = ood.Date.format(new Date, this.properties.xAxisDateFormatter);
                        if ((s = updater.xAxis[index]) && (d = s.data)) {
                            d.push(time);
                            if (d.length >= columnSize) d.shift();
                            while (d.length < columnSize) d.unshift(0);
                        }
                        if (index >= updater.series.length) {
                            for (var m = updater.series.length - 1; m <= index; m++) updater.series[m] = {
                                type: updater.series[0] && updater.series[0].type || 'line',
                                data: []
                            };
                        }

                        s = updater.series[index];
                        if (!s.data) s.data = [];
                        d = s.data;

                        d.push(v);
                        if (d.length >= columnSize) d.shift();
                        while (d.length < columnSize) d.unshift(0);

                        //console.log(index, v, d, updater);
                        this.boxing().setOptionUpdater(updater, true);
                    }
                })(i)
            };
        }

        ood.UI.ECharts.setDataModel(dataModals);
    },
    Instance: {

        _reBindProp: function (prf, hash, key) {
            var ins = prf.boxing(), fn, nhash = {};
            if (key == "optionUpdater" && ood.isHash(hash)) {
                for (var i in hash)
                    nhash[i] = ood.isFun(hash[i]) ? hash[i](prf) : ood.adjustVar(hash[i]);
                if (ood.isFun(ins[fn = 'set' + ood.str.initial(key)])) ins[fn](nhash, true);
                return false;
            }
        },
        getECharts: function () {
            return this.get(0) && this.get(0).$echarts;
        },
        optionAdapter: function (option) {
            return option;
        },
        echarts_call: function (funName, params) {
            var echarts = this.getECharts();
            if (echarts && echarts[funName]) {
                return echarts[funName].apply(echarts, params || []);
            }
        },
        echarts_dispatchAction: function (payload) {
            return this.echarts_call("dispatchAction", [payload]);
        },
        echarts_showLoading: function (type, opts) {
            return this.echarts_call("showLoading", [type, opts]);
        },
        echarts_hideLoading: function () {
            return this.echarts_call("hideLoading");
        },
        echarts_getOption: function () {
            return this.echarts_call("getOption", []);
        },
        echarts_setOption: function (option) {
            return this.echarts_call("setOption", [option, notMerge, lazyUpdate, silent]);
        },
        echarts_getDataURL: function (opts) {
            return this.echarts_call("getDataURL", [opts]);
        },
        echarts_getConnectedDataURL: function (opts) {
            return this.echarts_call("getConnectedDataURL", [opts]);
        },
        echarts_appendData: function (opts) {
            return this.echarts_call("appendData", [opts]);
        },
        echarts_clear: function () {
            return this.echarts_call("clear");
        },
        echarts_isDisposed: function () {
            return !this.get(0) || !this.get(0).$echarts || this.echarts_call("isDisposed");
        }
    },
    Static: {
        _objectProp: {chartOption: 1, optionUpdater: 1, dataset: 1},
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
            autoTips: null,
            width: {
                $spaceunit: 1,
                ini: '30em'
            },
            height: {
                $spaceunit: 1,
                ini: '25em'
            },

            chartCDN: "/plugins/echarts/echarts.min.js",
            chartCDNGL: "/plugins/echarts/echarts-gl.min.js",
            chartTheme: {
                ini: "",
                action: function (v) {
                    this.boxing().refresh();
                }
            },
            chartRenderer: {
                ini: "canvas",
                listbox: ['', 'canvas', 'svg'],
                action: function (v) {
                    this.boxing().refresh();
                }
            },
            chartDevicePixelRatio: {
                ini: window.devicePixelRatio,
                action: function (v) {
                    this.boxing().refresh();
                }
            },
            chartResizeSilent: false,
            chartOption: {
                ini: {},
                action: function (v) {
                    var prf = this, ins = prf.boxing(), prop = prf.properties;
                    if (!ins.echarts_isDisposed() && prf.$echarts) {
                        var option = ood.isFun(v.getOption) ? v.getOption() : v,
                            binder = prop.optionUpdater;
                        for (var i in binder) {
                            ood.set(option, i.split('.'), binder[i]);
                        }
                        if ('dataset' in option && !ood.isEmpty(option.dataset)) {
                            var t = option.dataset;
                            var dimensions = (t.dimensions || (t.source && t.source[0]) || [0]).length;
                            if (option.xAxis && ood.isHash(option.xAxis)) option.xAxis = [option.xAxis];
                            if (option.yAxis && ood.isHash(option.yAxis)) option.yAxis = [option.yAxis];

                            // clear/init series data
                            // first column is category
                            for (var i = 0, l = dimensions - 1; i < l; i++) {
                                var type = option.series[0] && option.series[0].type || 'line'
                                if (!option.series[i]) option.series[i] = {type: type};
                                else delete option.series[i].data;
                            }

                            // clear xAxis data
                            if (t = option.xAxis) for (var i = 0, l = t.length; i < l; i++) delete t[i].data;
                            // clear yAxis data
                            if (t = option.yAxis) for (var i = 0, l = t.length; i < l; i++) delete t[i].data;

                            // clear the char inner data, if exists
                            var opt = prf.$echarts.getOption();
                            if (opt) {
                                if (t = opt.series) for (var i = 0, l = t.length; i < l; i++) delete t[i].data;
                                if (t = opt.xAxis) for (var i = 0, l = t.length; i < l; i++) delete t[i].data;
                                if (t = opt.yAxis) for (var i = 0, l = t.length; i < l; i++) delete t[i].data;
                                // only reset option
                                try {
                                    prf.$echarts.setOption(opt, true, true, true);
                                } catch (e) {
                                    ood.log(e)
                                }
                            }
                        }
                        if ((v = prop.tagVar.optionAdapter) && ood.isFun(v)) option = v.call(ins, option, prf);
                        if ((v = ins.optionAdapter) && ood.isFun(v)) option = v.call(ins, option, prf);
                        if (!(ins.beforeSetOption && false === ins.beforeSetOption(prf, option))) {
                            try {
                                prf.$echarts.setOption(option);
                            } catch (e) {
                                ood.log(e)
                            }
                        }
                    }
                }
            },
            optionUpdater: {
                ini: {},
                action: function (v) {
                    var prf = this;
                    ood.resetRun("echart-data-setting:" + prf.$xid, function () {
                        if (prf && prf.box) prf.boxing().setChartOption(prf.properties.chartOption, true);
                    });
                }
            },
            dataset: {
                ini: {},
                get: function () {
                    var v = this.properties.optionUpdater.dataset;
                    return v || {};
                },
                set: function (v, force) {
                    var prop = this.properties, f = prop.xAxisDateFormatter, o = prop.optionUpdater;
                    if (v === o.dataset && !force) return;
                    if (!v || !ood.isHash(v)) {
                        delete o.dataset;
                    } else {
                        o.dataset = v;
                        if (f)
                            ood.each(o.dataset.source, function (h, i) {
                                if (ood.isNumb(h[0])) {
                                    h[0] = ood.Date.format(h[0], f)
                                }
                            });
                    }
                    return this.boxing().setOptionUpdater(o, true);
                }
            },
            xAxisDateFormatter: "hh:mm:ss",
            realTimePoints: 5
        },
        RenderTrigger: function () {
            var prf = this, prop = prf.properties;
            var fun = function () {
                if (!prf || !prf.box) return;
                var opts = {
                    width: prf.$px(prop.width),
                    height: prf.$px(prop.height)
                };
                if (prop.chartRenderer != "canvas") opts.chartRenderer = prop.chartRenderer;
                if (prop.chartDevicePixelRatio != window.devicePixelRatio) opts.chartDevicePixelRatio = prop.chartDevicePixelRatio;

                var chart = echarts.init(prf.getSubNode("BOX").get(0), prop.chartTheme, opts);
                prf.$echarts = chart;
                prf.boxing().setChartOption(prop.chartOption, true);
                var evts1 = "click,dblclick,mousedown,mouseup,mouseover,mouseout,globalout,contextmenu".split(",");
                if (prf.onMouseEvent)
                    ood.arr.each(evts1, function (name) {
                        chart.on(name, function (params) {
                            if (prf && prf.onMouseEvent) prf.onMouseEvent(prf, name, params);
                        });
                    });

                var evts2 = "legendselectchanged,legendunselected,legendscroll,datazoom,datarangeselected,timelinechanged,timelineplaychanged,restore,dataviewchanged,magictypechanged,geoselectchanged,geoselected,geounselected,pieselectchanged,pieselected,pieunselected,mapselectchanged,mapselected,mapunselected,axisareaselected,focusnodeadjacency,unfocusnodeadjacency,brush,brushselected,rendered,finished".split(",");
                if (prf.onChartEvent)
                    ood.arr.each(evts2, function (name) {
                        chart.on(name, function (params) {
                            if (prf && prf.onChartEvent) prf.onChartEvent(prf, name, params);
                        });
                    });

                // set before destroy function
                (prf.$beforeDestroy = (prf.$beforeDestroy || {}))["destroyechart"] = function () {
                    var t = this.$echarts;
                    if (t) {
                        ood.arr.each(evts1, function (name) {
                            t.off(name);
                        });
                        ood.arr.each(evts2, function (name) {
                            t.off(name);
                        });
                        t.dispose();
                        delete this.$echarts;
                    }
                }
            };
            if (window.echarts) fun();
            else {
                prf.boxing().busy(false, "Loading charts ...");
                var gl = prop.chartCDNGL;
                ood.include("echarts", prop.chartCDN, function () {
                    if (gl) ood.include("", gl, function () {
                        if (prf && prf.box) {
                            prf.boxing().free();
                            fun();
                        }
                    }, null, false, {cache: true});
                }, null, false, {cache: true});
            }
        },

        getWidget: function () {
            widgetConfig = [
                {
                    id: 'ood.UI.ECharts',
                    key: 'ood.UI.ECharts',
                    caption: 'ECharts',
                    group: true,
                    imageClass: 'ri ri-line-chart-line',
                    sub: [
                        {
                            id: 'ood.ECharts.1',
                            key: 'ood.ECharts.1',
                            caption: '$RAD.widgets.fc.Line',
                            group: true,
                            initFold: false,
                            imageClass: 'ri ri-line-chart-line',
                            sub:
                                [
                                    {
                                        id: 'ood.UI.ECs11',
                                        key: 'ood.UI.ECharts',
                                        caption: '$(RAD.widgets.ec.Basic Line)',
                                        draggable: true,
                                        iniProp: {
                                            chartOption: {
                                                xAxis: {
                                                    type: 'category',
                                                    data: ['Mon', 'Tue', 'Wed', 'Thu', 'Fri', 'Sat', 'Sun']
                                                },
                                                yAxis: {
                                                    type: 'value'
                                                },
                                                series: [{
                                                    data: [820, 932, 901, 934, 1290, 1330, 1320],
                                                    type: 'line',
                                                    smooth: true
                                                }]
                                            }
                                        }
                                    },
                                    {
                                        id: 'ood.UI.ECs12',
                                        key: 'ood.UI.ECharts',
                                        caption: '$(RAD.widgets.ec.Basic area)',
                                        draggable: true,
                                        iniProp: {
                                            chartOption: {
                                                xAxis: {
                                                    type: 'category',
                                                    boundaryGap: false,
                                                    data: ['Mon', 'Tue', 'Wed', 'Thu', 'Fri', 'Sat', 'Sun']
                                                },
                                                yAxis: {
                                                    type: 'value'
                                                },
                                                series: [{
                                                    data: [820, 932, 901, 934, 1290, 1330, 1320],
                                                    type: 'line',
                                                    areaStyle: {}
                                                }]
                                            }
                                        }
                                    }
                                ]
                        },
                        {
                            id: 'ood.ECharts.2',
                            key: 'ood.ECharts.2',
                            caption: '$(RAD.widgets.fc.Column 2D)',
                            group: true,
                            imageClass: 'ri ri-bar-chart-line',
                            sub:
                                [
                                    {
                                        id: 'ood.UI.ECs21',
                                        key: 'ood.UI.ECharts',
                                        caption: '$RAD.widgets.ec.Basic Bar',
                                        draggable: true,
                                        iniProp: {
                                            chartOption: {
                                                xAxis: {
                                                    type: 'category',
                                                    data: ['Mon', 'Tue', 'Wed', 'Thu', 'Fri', 'Sat', 'Sun']
                                                },
                                                yAxis: {
                                                    type: 'value'
                                                },
                                                series: [{
                                                    data: [120, 200, 150, 80, 70, 110, 130],
                                                    type: 'bar'
                                                }]
                                            }
                                        }
                                    },
                                    {
                                        id: 'ood.UI.ECs22',
                                        key: 'ood.UI.ECharts',
                                        caption: '$(RAD.widgets.ec.Stacked Bar)',
                                        draggable: true,
                                        iniProp: {
                                            chartOption: {
                                                tooltip: {
                                                    trigger: 'axis',
                                                    axisPointer: {
                                                        type: 'shadow'
                                                    }
                                                },
                                                legend: {
                                                    data: ['Direct', 'Email', 'Alliance', 'Video', 'Search']
                                                },
                                                grid: {
                                                    left: '3%',
                                                    right: '4%',
                                                    bottom: '3%',
                                                    containLabel: true
                                                },
                                                xAxis: {
                                                    type: 'value'
                                                },
                                                yAxis: {
                                                    type: 'category',
                                                    data: ['Mon', 'Tue', 'Wed', 'Thu', 'Fri', 'Sat', 'Sun']
                                                },
                                                series: [
                                                    {
                                                        name: 'Direct',
                                                        type: 'bar',
                                                        stack: 'sum',
                                                        label: {
                                                            normal: {
                                                                show: true,
                                                                position: 'insideRight'
                                                            }
                                                        },
                                                        data: [320, 302, 301, 334, 390, 330, 320]
                                                    },
                                                    {
                                                        name: 'Email',
                                                        type: 'bar',
                                                        stack: 'sum',
                                                        label: {
                                                            normal: {
                                                                show: true,
                                                                position: 'insideRight'
                                                            }
                                                        },
                                                        data: [120, 132, 101, 134, 90, 230, 210]
                                                    },
                                                    {
                                                        name: 'Alliance',
                                                        type: 'bar',
                                                        stack: 'sum',
                                                        label: {
                                                            normal: {
                                                                show: true,
                                                                position: 'insideRight'
                                                            }
                                                        },
                                                        data: [220, 182, 191, 234, 290, 330, 310]
                                                    },
                                                    {
                                                        name: 'Video',
                                                        type: 'bar',
                                                        stack: 'sum',
                                                        label: {
                                                            normal: {
                                                                show: true,
                                                                position: 'insideRight'
                                                            }
                                                        },
                                                        data: [150, 212, 201, 154, 190, 330, 410]
                                                    },
                                                    {
                                                        name: 'Search',
                                                        type: 'bar',
                                                        stack: 'sum',
                                                        label: {
                                                            normal: {
                                                                show: true,
                                                                position: 'insideRight'
                                                            }
                                                        },
                                                        data: [820, 832, 901, 934, 1290, 1330, 1320]
                                                    }
                                                ]
                                            }
                                        }
                                    }
                                ]
                        },
                        {
                            id: 'ood.ECharts.3',
                            key: 'ood.ECharts.3',
                            caption: '饼图',
                            group: true,
                            imageClass: 'ri ri-bar-chart-line',
                            sub:
                                [
                                    {
                                        id: 'ood.UI.ECs31',
                                        key: 'ood.UI.ECharts',
                                        caption: '$(RAD.widgets.ec.Basic Pie)',
                                        draggable: true,
                                        iniProp: {
                                            chartOption: {
                                                title: {
                                                    text: 'Visitor Statistics',
                                                    subtext: 'Fake data',
                                                    x: 'center'
                                                },
                                                tooltip: {
                                                    trigger: 'item',
                                                    formatter: "{a} <br/>{b} : {c} ({d}%)"
                                                },
                                                legend: {
                                                    orient: 'vertical',
                                                    left: 'left',
                                                    data: ['Direct', 'Email', 'Alliance', 'Video', 'Search']
                                                },
                                                series: [
                                                    {
                                                        name: 'Visitor Statistics',
                                                        type: 'pie',
                                                        radius: '55%',
                                                        center: ['50%', '60%'],
                                                        data: [
                                                            {value: 335, name: 'Direct'},
                                                            {value: 310, name: 'Email'},
                                                            {value: 234, name: 'Alliance'},
                                                            {value: 135, name: 'Video'},
                                                            {value: 1548, name: 'Search'}
                                                        ],
                                                        itemStyle: {
                                                            emphasis: {
                                                                shadowBlur: 10,
                                                                shadowOffsetX: 0,
                                                                shadowColor: 'rgba(0, 0, 0, 0.5)'
                                                            }
                                                        }
                                                    }
                                                ]
                                            }
                                        }
                                    },
                                    {
                                        id: 'ood.UI.ECs32',
                                        key: 'ood.UI.ECharts',
                                        caption: '$(RAD.widgets.ec.Customized Pie)',
                                        draggable: true,
                                        iniProp: {
                                            chartOption: {
                                                backgroundColor: '#2c343c',

                                                title: {
                                                    text: 'Customized Pie',
                                                    left: 'center',
                                                    top: 20,
                                                    textStyle: {
                                                        color: '#ccc'
                                                    }
                                                },

                                                tooltip: {
                                                    trigger: 'item',
                                                    formatter: "{a} <br/>{b} : {c} ({d}%)"
                                                },

                                                visualMap: {
                                                    show: false,
                                                    min: 80,
                                                    max: 600,
                                                    inRange: {
                                                        colorLightness: [0, 1]
                                                    }
                                                },
                                                series: [
                                                    {
                                                        name: 'Pie',
                                                        type: 'pie',
                                                        radius: '55%',
                                                        center: ['50%', '50%'],
                                                        data: [
                                                            {value: 335, name: 'Serie 1'},
                                                            {value: 310, name: 'Serie 2'},
                                                            {value: 274, name: 'Serie 3'},
                                                            {value: 235, name: 'Serie 4'},
                                                            {value: 400, name: 'Serie 5'}
                                                        ].sort(function (a, b) {
                                                            return a.value - b.value;
                                                        }),
                                                        roseType: 'radius',
                                                        label: {
                                                            normal: {
                                                                textStyle: {
                                                                    color: 'rgba(255, 255, 255, 0.3)'
                                                                }
                                                            }
                                                        },
                                                        labelLine: {
                                                            normal: {
                                                                lineStyle: {
                                                                    color: 'rgba(255, 255, 255, 0.3)'
                                                                },
                                                                smooth: 0.2,
                                                                length: 10,
                                                                length2: 20
                                                            }
                                                        },
                                                        itemStyle: {
                                                            normal: {
                                                                color: '#c23531',
                                                                shadowBlur: 200,
                                                                shadowColor: 'rgba(0, 0, 0, 0.5)'
                                                            }
                                                        },

                                                        animationType: 'scale',
                                                        animationEasing: 'elasticOut',
                                                        animationDelay: function (idx) {
                                                            return Math.random() * 200;
                                                        }
                                                    }
                                                ]
                                            }
                                        }
                                    }
                                ]
                        },
                        {
                            id: 'ood.ECharts.4',
                            key: 'ood.ECharts.4',
                            caption: '点阵图',
                            group: true,
                            imageClass: 'ri ri-pie-chart-line',
                            sub: [{
                                id: 'ood.UI.ECs41',
                                key: 'ood.UI.ECharts',
                                caption: '$(RAD.widgets.ec.Basic Scatter)',
                                draggable: true,
                                iniProp: {
                                    chartOption: {
                                        xAxis: {},
                                        yAxis: {},
                                        series: [{
                                            symbolSize: 20,
                                            data: [[10.0, 8.04], [8.0, 6.95], [13.0, 7.58], [9.0, 8.81], [11.0, 8.33], [14.0, 9.96], [6.0, 7.24], [4.0, 4.26], [12.0, 10.84], [7.0, 4.82], [5.0, 5.68]],
                                            type: 'scatter'
                                        }]
                                    }
                                }
                            }]
                        },
                        {
                            id: 'ood.ECharts.5',
                            key: 'ood.ECharts.5',
                            caption: '3D视图',
                            group: true,
                            imageClass: 'ri ri-bar-chart-line',
                            sub:
                                [
                                    {
                                        id: 'ood.UI.ECs51',
                                        key: 'ood.UI.ECharts',
                                        caption: '$(RAD.widgets.ec.Basic Radar)',
                                        draggable: true,
                                        iniProp: {
                                            chartOption: {
                                                title: {
                                                    text: 'Basic Radar'
                                                },
                                                tooltip: {},
                                                legend: {
                                                    data: ['Allocated Budget', 'Actual Spending']
                                                },
                                                radar: {
                                                    // shape: 'circle',
                                                    name: {
                                                        textStyle: {
                                                            color: '#fff',
                                                            backgroundColor: '#999',
                                                            borderRadius: 3,
                                                            padding: [3, 5]
                                                        }
                                                    },
                                                    indicator: [
                                                        {name: 'sales', max: 6500},
                                                        {name: 'Administration', max: 16000},
                                                        {name: 'Information Techology', max: 30000},
                                                        {name: 'Customer Support', max: 38000},
                                                        {name: 'Development', max: 52000},
                                                        {name: 'Marketing', max: 25000}
                                                    ]
                                                },
                                                series: [{
                                                    name: 'Budget vs spending',
                                                    type: 'radar',
                                                    // areaStyle: {normal: {}},
                                                    data: [
                                                        {
                                                            value: [4300, 10000, 28000, 35000, 50000, 19000],
                                                            name: 'Allocated Budget'
                                                        },
                                                        {
                                                            value: [5000, 14000, 28000, 31000, 42000, 21000],
                                                            name: 'Actual Spending'
                                                        }
                                                    ]
                                                }]
                                            }
                                        }
                                    },

                                    {
                                        id: 'ood.UI.ECs61',
                                        key: 'ood.UI.ECharts',
                                        caption: '$(RAD.widgets.ec.Basic Funnel)',
                                        draggable: true,
                                        iniProp: {
                                            chartOption: {
                                                title: {
                                                    text: 'Funnel',
                                                    subtext: 'Fake Data'
                                                },
                                                tooltip: {
                                                    trigger: 'item',
                                                    formatter: "{a} <br/>{b} : {c}%"
                                                },
                                                toolbox: {
                                                    feature: {
                                                        dataView: {readOnly: false},
                                                        restore: {},
                                                        saveAsImage: {}
                                                    }
                                                },
                                                legend: {
                                                    data: ['Page View', 'Click', 'Visitor', 'Consulting', 'Order']
                                                },
                                                calculable: true,
                                                series: [
                                                    {
                                                        name: 'Funnel',
                                                        type: 'funnel',
                                                        left: '10%',
                                                        top: 60,
                                                        //x2: 80,
                                                        bottom: 60,
                                                        width: '80%',
                                                        // height: {totalHeight} - y - y2,
                                                        min: 0,
                                                        max: 100,
                                                        minSize: '0%',
                                                        maxSize: '100%',
                                                        sort: 'descending',
                                                        gap: 2,
                                                        label: {
                                                            normal: {
                                                                show: true,
                                                                position: 'inside'
                                                            },
                                                            emphasis: {
                                                                textStyle: {
                                                                    fontSize: 20
                                                                }
                                                            }
                                                        },
                                                        labelLine: {
                                                            normal: {
                                                                length: 10,
                                                                lineStyle: {
                                                                    width: 1,
                                                                    type: 'solid'
                                                                }
                                                            }
                                                        },
                                                        itemStyle: {
                                                            normal: {
                                                                borderColor: '#fff',
                                                                borderWidth: 1
                                                            }
                                                        },
                                                        data: [
                                                            {value: 60, name: 'Visitor'},
                                                            {value: 40, name: 'Consulting'},
                                                            {value: 20, name: 'Order'},
                                                            {value: 80, name: 'Click'},
                                                            {value: 100, name: 'Page view'}
                                                        ]
                                                    }
                                                ]
                                            }
                                        }
                                    },

                                    {
                                        id: 'ood.UI.ECs71',
                                        key: 'ood.UI.ECharts',
                                        caption: '$(RAD.widgets.ec.Basic Gauge)',
                                        draggable: true,
                                        iniProp: {
                                            chartOption: {
                                                tooltip: {
                                                    formatter: "{a} <br/>{b} : {c}%"
                                                },
                                                toolbox: {
                                                    feature: {
                                                        restore: {},
                                                        saveAsImage: {}
                                                    }
                                                },
                                                series: [
                                                    {
                                                        name: 'Index',
                                                        type: 'gauge',
                                                        detail: {formatter: '50%'},
                                                        data: [{value: 50, name: 'Rate'}]
                                                    }
                                                ]
                                            }
                                        }
                                    },

                                    {
                                        id: 'ood.UI.ECs82',
                                        key: 'ood.UI.ECharts',
                                        caption: '$(RAD.widgets.ec.3D Stacked Bar)',
                                        draggable: true,
                                        iniProp: {
                                            chartOption: {
                                                color: ['#7cb5ec', '#434348', '#90ed7d', '#f7a35c', '#8085e9', '#f15c80', '#e4d354', '#8085e8', '#8d4653', '#91e8e1'],
                                                title: {
                                                    text: '3D Stacked Bars',
                                                    x: 'center'
                                                },
                                                tooltip: {},
                                                xAxis3D: {
                                                    type: 'category',
                                                    data: ['Apple', 'Orange', 'Pear', 'Grapes', 'Banana']
                                                },
                                                yAxis3D: {
                                                    type: 'category',
                                                    data: ['']
                                                },
                                                zAxis3D: {
                                                    type: 'value'
                                                },
                                                grid3D: {
                                                    boxWidth: 200,
                                                    boxDepth: 20,
                                                    axisPointer: {
                                                        show: false
                                                    },
                                                    light: {
                                                        main: {
                                                            intensity: 1.2
                                                        },
                                                        ambient: {
                                                            intensity: 0.3
                                                        }
                                                    },
                                                    viewControl: {
                                                        alpha: 10,
                                                        beta: 20,
                                                        minAlpha: 10,
                                                        maxAlpha: 10,
                                                        minBeta: 20,
                                                        maxBeta: 20
                                                    }
                                                },
                                                series: [{
                                                    type: 'bar3D',
                                                    barSize: 15,
                                                    data: [
                                                        [0, 0, 5],
                                                        [1, 0, 3],
                                                        [2, 0, 4],
                                                        [3, 0, 7],
                                                        [4, 0, 2]
                                                    ],
                                                    stack: 'stack',
                                                    shading: 'lambert',
                                                    emphasis: {
                                                        label: {
                                                            show: false
                                                        }
                                                    }
                                                }, {
                                                    type: 'bar3D',
                                                    barSize: 15,
                                                    data: [
                                                        [0, 0, 3],
                                                        [1, 0, 4],
                                                        [2, 0, 4],
                                                        [3, 0, 2],
                                                        [4, 0, 5]
                                                    ],
                                                    stack: 'stack',
                                                    shading: 'lambert',
                                                    emphasis: {
                                                        label: {
                                                            show: false
                                                        }
                                                    }
                                                }]
                                            }
                                        }
                                    },
                                    {
                                        id: 'ood.UI.ECs82',
                                        key: 'ood.UI.ECharts',
                                        caption: '$(RAD.widgets.ec.Simple Surface)',
                                        draggable: true,
                                        iniProp: {
                                            chartOption: {
                                                tooltip: {},
                                                backgroundColor: '#fff',
                                                visualMap: {
                                                    show: false,
                                                    dimension: 2,
                                                    min: -1,
                                                    max: 1,
                                                    inRange: {
                                                        color: ['#313695', '#4575b4', '#74add1', '#abd9e9', '#e0f3f8', '#ffffbf', '#fee090', '#fdae61', '#f46d43', '#d73027', '#a50026']
                                                    }
                                                },
                                                xAxis3D: {
                                                    type: 'value'
                                                },
                                                yAxis3D: {
                                                    type: 'value'
                                                },
                                                zAxis3D: {
                                                    type: 'value'
                                                },
                                                grid3D: {
                                                    viewControl: {
                                                        // projection: 'orthographic'
                                                    }
                                                },
                                                series: [{
                                                    type: 'surface',
                                                    wireframe: {
                                                        // show: false
                                                    },
                                                    equation: {
                                                        x: {
                                                            step: 0.05
                                                        },
                                                        y: {
                                                            step: 0.05
                                                        },
                                                        z: function (x, y) {
                                                            if (Math.abs(x) < 0.1 && Math.abs(y) < 0.1) {
                                                                return '-';
                                                            }
                                                            return Math.sin(x * Math.PI) * Math.sin(y * Math.PI);
                                                        }
                                                    }
                                                }]
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
            onMouseEvent: function (profile, eventName, eventParams) {
            },
            onChartEvent: function (profile, eventName, eventParams) {
            },
            beforeSetOption: function (prf, option) {
            },
            onShowTips: null
        },
        _beforeSerialized: function (profile) {
            var o = ood.UI._beforeSerialized.call(this, profile),
                op = o.properties;
            for (var i = 0; i < 9; i++) {
                delete op["realTimeData" + (i + 1)];
                delete op["dataValue" + (i + 1)];
            }
            delete op.optionUpdater;
            return o;
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
                if (prf.renderId && (t = prf.$echarts)) {
                    // ensure by px
                    t.resize({width: ww, height: hh, silent: prop.chartResizeSilent});
                }
            }
        }
    }
});