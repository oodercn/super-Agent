// for version 3.6
window.FCCONF = {
    gridAttr: {
        numVisiblePlot: {type: "spin", precision: 2, increment: 0.1, min: 2, max: 100},
        animate3D: {type: "checkbox"},
        animation: {type: "checkbox"},
        exeTime: {type: "spin", precision: 2, increment: 0.1, min: 0, max: 10},
        palette: {type: "spin", precision: 0, increment: 1, min: 1, max: 5},
        paletteThemeColor: {type: "color"},
        annRenderDelay: {type: "spin", precision: 0, increment: 1, min: 0, max: 5000},
        showPrintMenuItem: {type: "checkbox"},
        manageResize: {type: "checkbox"},
        paletteColors: {type: "input"},
        showRTMenuItem: {type: "checkbox"},
        showAboutMenuItem: {type: "checkbox"},
        refreshInstantly: {type: "checkbox"},
        aboutMenuItemLabel: {type: "input"},
        aboutMenuItemLink: {type: "input"},
        clipBubbles: {type: "checkbox"},
        negativeColor: {type: "color"},
        bubbleScale: {type: "checkbox"},
        connectNullData: {type: "checkbox"},
        areaOverColumns: {type: "checkbox"},
        showLabels: {type: "checkbox"},
        showLabelsAtCenter: {type: "checkbox"},
        maxLabelHeight: {type: "spin", precision: 0, increment: 1, min: 0},
        labelDisplay: {type: "listbox", editorListItems: ["", "AUTO", "WRAP", "STAGGER", "ROTATE", "NONE"]},
        useEllipsesWhenOverflow: {type: "checkbox"},
        rotateLabels: {type: "checkbox"},
        slantLabels: {type: "checkbox"},
        labelStep: {type: "spin", precision: 0, increment: 1, min: 1},
        xAxisLabelMode: {type: "listbox", editorListItems: ["", "AUTO", "CATEGORIES", "MIXED"]},
        maxLabelWidthPercent: {type: "spin", precision: 0, increment: 1, min: 1, max: 100},
        staggerLines: {type: "spin", precision: 0, increment: 1, min: 2},
        autoScale: {type: "checkbox"},
        origW: {type: "spin", precision: 0, increment: 1, min: 0},
        origH: {type: "spin", precision: 0, increment: 1, min: 0},
        valuePosition: {type: "listbox", editorListItems: ["", "ABOVE", "BELOW", "AUTO"]},
        rotateValues: {type: "checkbox"},
        placeValuesInside: {type: "checkbox"},
        showYAxisValues: {type: "checkbox"},
        showXAxisValues: {type: "checkbox"},
        showLimits: {type: "checkbox"},
        showVLimits: {type: "checkbox"},
        showDivLineValues: {type: "checkbox"},
        showVDivLineValues: {type: "checkbox"},
        showSecondaryLimits: {type: "checkbox"},
        showDivLineSecondaryValue: {type: "checkbox"},
        xAxisValuesStep: {type: "spin", precision: 0, increment: 1, min: 1},
        yAxisValuesStep: {type: "spin", precision: 0, increment: 1, min: 1},
        showShadow: {type: "checkbox"},
        adjustDiv: {type: "checkbox"},
        adjustVDiv: {type: "checkbox"},
        clickURL: {type: "input"},
        sYAxisMinValue: {type: "spin", precision: 0, increment: 1, min: 0},
        sYAxisMaxValue: {type: "spin", precision: 0, increment: 1, min: 0},
        setAdaptiveSYMin: {type: "checkbox"},
        setAdaptiveXMin: {type: "checkbox"},
        showRegressionLine: {type: "checkbox"},
        RegressionLineColor: {type: "color"},
        RegressionLineThickness: {type: "spin", precision: 0, increment: 1, min: 0, max: 5},
        RegressionLineAlpha: {type: "spin", precision: 2, increment: 1, min: 0, max: 100},
        showYOnX: {type: "checkbox"},
        maxColWidth: {type: "spin", precision: 0, increment: 1, min: 0},
        showSum: {type: "checkbox"},
        usePercentDistribution: {type: "checkbox"},
        showXAxisPercentValues: {type: "checkbox"},
        use3DLighting: {type: "checkbox"},
        defaultAnimation: {type: "checkbox"},
        xAxisMinValue: {type: "spin", precision: 0, increment: 1, min: 0},
        xAxisMaxValue: {type: "spin", precision: 0, increment: 1, min: 0},
        yAxisMinValue: {type: "spin", precision: 0, increment: 1, min: 0},
        yAxisMaxValue: {type: "spin", precision: 0, increment: 1, min: 0},
        stack100Percent: {type: "checkbox"},
        setAdaptiveYMin: {type: "checkbox"},
        useDataPlotColorForLabels: {type: "checkbox"},
        scrollToEnd: {type: "checkbox"},
        syncAxisLimits: {type: "checkbox"},
        compactDataMode: {type: "checkbox"},
        dataSeparator: {type: "input", maxlength: 1},
        axis: {type: "listbox", editorListItems: ["", "linear", "log"]},
        logBase: {type: "spin", precision: 0, increment: 1, min: 1},
        dynamicAxis: {type: "checkbox"},
        divIntervalHints: {type: "input"},
        allowPinMode: {type: "checkbox"},
        numVisibleLabels: {type: "spin", precision: 0, increment: 1, min: 0},
        anchorMinRenderDistance: {type: "spin", precision: 0, increment: 1, min: 0},
        showVDivLines: {type: "checkbox"},
        displayStartIndex: {type: "spin", precision: 0, increment: 1, min: 0},
        displayEndIndex: {type: "spin", precision: 0, increment: 1, min: 0},
        drawToolbarButtons: {type: "checkbox"},
        pixelsPerPoint: {type: "spin", precision: 0, increment: 1, min: 0},
        numMinorLogDivLines: {type: "spin", precision: 0, increment: 1, min: 0},
        xAxisTickColor: {type: "color"},
        xAxisTickAlpha: {type: "spin", precision: 2, increment: 1, min: 0, max: 100},
        xAxisTickThickness: {type: "spin", precision: 0, increment: 1, min: 0, max: 5},
        showZeroPies: {type: "checkbox"},
        showPercentValues: {type: "checkbox"},
        percentOfPrevious: {type: "checkbox"},
        showPercentInToolTip: {type: "checkbox"},
        labelSepChar: {type: "input", maxlength: 1},
        pYAxisMaxValue: {type: "spin", precision: 0, increment: 1, min: 0},
        pYAxisMinValue: {type: "spin", precision: 0, increment: 1, min: 0},
        showCumulativeLine: {type: "checkbox"},
        showLineValues: {type: "checkbox"},
        primaryAxisOnLeft: {type: "checkbox"},
        maxBarHeight: {type: "spin", precision: 0, increment: 1, min: 0},
        barDepth: {type: "spin", precision: 0, increment: 1, min: 0},
        use3DLineShift: {type: "checkbox"},
        streamlinedData: {type: "checkbox"},
        isHollow: {type: "checkbox"},
        useSameSlantAngle: {type: "checkbox"},
        funnelYScale: {type: "spin", precision: 0, increment: 1, min: 0, max: 40},
        pyramidYScale: {type: "spin", precision: 0, increment: 1, min: 0, max: 40},
        unescapeLinks: {type: "checkbox"},
        radarRadius: {type: "spin", precision: 0, increment: 1, min: 0},
        captionAlignment: {type: "listbox", editorListItems: ["", "left", "center", "right"]},
        captionOnTop: {type: "checkbox"},
        captionFontSize: {type: "spin", precision: 0, increment: 1, min: 0, max: 256},
        subCaptionFontSize: {type: "spin", precision: 0, increment: 1, min: 0, max: 256},
        captionFont: {type: "helpinput", editorListItems: CONF.designer_data_fontfamily},
        subCaptionFont: {type: "helpinput", editorListItems: CONF.designer_data_fontfamily},
        captionFontColor: {type: "color"},
        subCaptionFontColor: {type: "color"},
        captionFontBold: {type: "checkbox"},
        subCaptionFontBold: {type: "checkbox"},
        alignCaptionWithCanvas: {type: "checkbox"},
        captionHorizontalPadding: {type: "spin", precision: 0, increment: 1, min: 0},
        xAxisNameFontColor: {type: "color"},
        xAxisNameFontSize: {type: "spin", precision: 0, increment: 1, min: 0, max: 256},
        xAxisNameFontBold: {type: "checkbox"},
        xAxisNameFontItalic: {type: "checkbox"},
        xAxisNameBgColor: {type: "color"},
        xAxisNameBorderColor: {type: "color"},
        xAxisNameAlpha: {type: "spin", precision: 2, increment: 1, min: 0, max: 100},
        xAxisNameFontAlpha: {type: "spin", precision: 2, increment: 1, min: 0, max: 100},
        xAxisNameBgAlpha: {type: "spin", precision: 2, increment: 1, min: 0, max: 100},
        xAxisNameBorderAlpha: {type: "spin", precision: 2, increment: 1, min: 0, max: 100},
        xAxisNameBorderPadding: {type: "spin", precision: 0, increment: 1, min: 0},
        xAxisNameBorderRadius: {type: "spin", precision: 0, increment: 1, min: 0},
        xAxisNameBorderThickness: {type: "spin", precision: 0, increment: 1, min: 0, max: 5},
        xAxisNameBorderDashed: {type: "checkbox"},
        xAxisNameBorderDashLen: {type: "spin", precision: 0, increment: 1, min: 0},
        xAxisNameBorderDashGap: {type: "spin", precision: 0, increment: 1, min: 0},
        yAxisNameFont: {type: "helpinput", editorListItems: CONF.designer_data_fontfamily},
        yAxisNameFontColor: {type: "color"},
        yAxisNameFontSize: {type: "spin", precision: 0, increment: 1, min: 0, max: 256},
        yAxisNameFontBold: {type: "checkbox"},
        yAxisNameFontItalic: {type: "checkbox"},
        yAxisNameBgColor: {type: "color"},
        yAxisNameBorderColor: {type: "color"},
        yAxisNameAlpha: {type: "spin", precision: 2, increment: 1, min: 0, max: 100},
        yAxisNameFontAlpha: {type: "spin", precision: 2, increment: 1, min: 0, max: 100},
        yAxisNameBgAlpha: {type: "spin", precision: 2, increment: 1, min: 0, max: 100},
        yAxisNameBorderAlpha: {type: "spin", precision: 2, increment: 1, min: 0, max: 100},
        yAxisNameBorderPadding: {type: "spin", precision: 0, increment: 1, min: 0},
        yAxisNameBorderRadius: {type: "spin", precision: 0, increment: 1, min: 0},
        yAxisNameBorderThickness: {type: "spin", precision: 0, increment: 1, min: 0, max: 5},
        yAxisNameBorderDashed: {type: "checkbox"},
        yAxisNameBorderDashLen: {type: "spin", precision: 0, increment: 1, min: 0},
        yAxisNameBorderDashGap: {type: "spin", precision: 0, increment: 1, min: 0},
        pYAxisNameFont: {type: "helpinput", editorListItems: CONF.designer_data_fontfamily},
        pYAxisNameFontColor: {type: "color"},
        pYAxisNameFontSize: {type: "spin", precision: 0, increment: 1, min: 0, max: 256},
        pYAxisNameFontBold: {type: "checkbox"},
        pYAxisNameFontItalic: {type: "checkbox"},
        pYAxisNameBgColor: {type: "color"},
        pYAxisNameBorderColor: {type: "color"},
        pYAxisNameAlpha: {type: "spin", precision: 2, increment: 1, min: 0, max: 100},
        pYAxisNameFontAlpha: {type: "spin", precision: 2, increment: 1, min: 0, max: 100},
        pYAxisNameBgAlpha: {type: "spin", precision: 2, increment: 1, min: 0, max: 100},
        pYAxisNameBorderAlpha: {type: "spin", precision: 2, increment: 1, min: 0, max: 100},
        pYAxisNameBorderPadding: {type: "spin", precision: 0, increment: 1, min: 0},
        pYAxisNameBorderRadius: {type: "spin", precision: 0, increment: 1, min: 0},
        pYAxisNameBorderThickness: {type: "spin", precision: 0, increment: 1, min: 0, max: 5},
        pYAxisNameBorderDashed: {type: "checkbox"},
        pYAxisNameBorderDashLen: {type: "spin", precision: 0, increment: 1, min: 0},
        pYAxisNameBorderDashGap: {type: "spin", precision: 0, increment: 1, min: 0},
        sYAxisNameFont: {type: "helpinput", editorListItems: CONF.designer_data_fontfamily},
        sYAxisNameFontColor: {type: "color"},
        sYAxisNameFontSize: {type: "spin", precision: 0, increment: 1, min: 0, max: 256},
        sYAxisNameFontBold: {type: "checkbox"},
        sYAxisNameFontItalic: {type: "checkbox"},
        sYAxisNameBgColor: {type: "color"},
        sYAxisNameBorderColor: {type: "color"},
        sYAxisNameAlpha: {type: "spin", precision: 2, increment: 1, min: 0, max: 100},
        sYAxisNameFontAlpha: {type: "spin", precision: 2, increment: 1, min: 0, max: 100},
        sYAxisNameBgAlpha: {type: "spin", precision: 2, increment: 1, min: 0, max: 100},
        sYAxisNameBorderAlpha: {type: "spin", precision: 2, increment: 1, min: 0, max: 100},
        sYAxisNameBorderPadding: {type: "spin", precision: 0, increment: 1, min: 0},
        sYAxisNameBorderRadius: {type: "spin", precision: 0, increment: 1, min: 0},
        sYAxisNameBorderThickness: {type: "spin", precision: 0, increment: 1, min: 0, max: 5},
        sYAxisNameBorderDashed: {type: "checkbox"},
        sYAxisNameBorderDashLen: {type: "spin", precision: 0, increment: 1, min: 0},
        sYAxisNameBorderDashGap: {type: "spin", precision: 0, increment: 1, min: 0},
        color: {type: "color"},
        caption: {type: "input"},
        subCaption: {type: "input"},
        xAxisName: {type: "input"},
        yAxisName: {type: "input"},
        centerYaxisName: {type: "checkbox"},
        centerXaxisName: {type: "checkbox"},
        rotateYAxisName: {type: "checkbox"},
        yAxisNameWidth: {type: "spin", precision: 0, increment: 1, min: 0},
        rotateXAxisName: {type: "checkbox"},
        xAxisNameWidth: {type: "spin", precision: 0, increment: 1, min: 0},
        sYAxisName: {type: "input"},
        pYAxisName: {type: "input"},
        pYAxisNameWidth: {type: "spin", precision: 0, increment: 1, min: 0},
        sYAxisNameWidth: {type: "spin", precision: 0, increment: 1, min: 0},
        bgColor: {type: "color"},
        bgAlpha: {type: "spin", precision: 2, increment: 1, min: 0, max: 100},
        bgRatio: {type: "spin", precision: 0, increment: 1, min: 0, max: 100},
        bgAngle: {type: "spin", precision: 0, increment: 1, min: 0, max: 360},
        bgImage: {type: "input"},
        bgImageAlpha: {type: "spin", precision: 2, increment: 1, min: 0, max: 100},
        bgImageDisplayMode: {
            type: "listbox",
            editorListItems: ["", "stretch", "tile", "fit", "fill", "center", "none"]
        },
        bgImageVAlign: {type: "listbox", editorListItems: ["", "top", "middle", "bottom"]},
        bgImageHAlign: {type: "listbox", editorListItems: ["", "left", "middle", "right"]},
        bgImageScale: {type: "spin", precision: 0, increment: 1, min: 0, max: 300},
        xAxisLineColor: {type: "color"},
        canvasBgColor: {type: "color"},
        canvasBgAlpha: {type: "spin", precision: 2, increment: 1, min: 0, max: 100},
        canvasBgRatio: {type: "spin", precision: 0, increment: 1, min: 0, max: 100},
        canvasBgAngle: {type: "spin", precision: 0, increment: 1, min: 0, max: 360},
        canvasBorderColor: {type: "color"},
        canvasBorderThickness: {type: "spin", precision: 0, increment: 1, min: 0, max: 5},
        canvasBorderAlpha: {type: "spin", precision: 2, increment: 1, min: 0, max: 100},
        canvasBaseColor: {type: "color"},
        showCanvasBg: {type: "checkbox"},
        showCanvasBase: {type: "checkbox"},
        canvasBaseDepth: {type: "spin", precision: 0, increment: 1, min: 0, max: 100},
        canvasBgDepth: {type: "spin", precision: 0, increment: 1, min: 0, max: 100},
        showBorder: {type: "checkbox"},
        borderColor: {type: "color"},
        borderAlpha: {type: "spin", precision: 2, increment: 1, min: 0, max: 100},
        showVLineLabelBorder: {type: "checkbox"},
        yAxisLineColor: {type: "color"},
        logoURL: {type: "input"},
        logoPosition: {type: "listbox", editorListItems: ["", "TL", "TR", "BL", "BR", "CC"]},
        logoAlpha: {type: "spin", precision: 2, increment: 1, min: 0, max: 100},
        logoScale: {type: "spin", precision: 0, increment: 1, min: 0, max: 300},
        logoLink: {type: "input"},
        toolbarButtonColor: {type: "color"},
        toolbarButtonFontColor: {type: "color"},
        zoomPaneBorderColor: {type: "color"},
        zoomPaneBgColor: {type: "color"},
        zoomPaneBgAlpha: {type: "spin", precision: 2, increment: 1, min: 0, max: 100},
        pinLineThicknessDelta: {type: "spin", precision: 0, increment: 1, min: -1000, max: 1000},
        pinPaneBorderColor: {type: "color"},
        pinPaneBgColor: {type: "color"},
        pinPaneBgAlpha: {type: "spin", precision: 2, increment: 1, min: 0, max: 100},
        toolTipColor: {type: "color"},
        toolTipBarColor: {type: "color"},
        useCrossLine: {type: "checkbox"},
        mouseCursorColor: {type: "color"},
        btnResetChartTitle: {type: "input"},
        btnZoomOutTitle: {type: "input"},
        btnSwitchtoZoomModeTitle: {type: "input"},
        btnSwitchToPinModeTitle: {type: "input"},
        showToolBarButtonTooltext: {type: "checkbox"},
        btnResetChartTooltext: {type: "input"},
        btnSwitchToPinModeTooltext: {type: "input"},
        zoomOutMenuItemLabel: {type: "input"},
        resetChartMenuItemLabel: {type: "input"},
        zoomModeMenuItemLabel: {type: "input"},
        pinModeMenuItemLabel: {type: "input"},
        toolBarBtnTextVMargin: {type: "spin", precision: 0, increment: 1, min: 0},
        toolBarBtnTextHMargin: {type: "spin", precision: 0, increment: 1, min: 0},
        toolBarBtnHPadding: {type: "spin", precision: 0, increment: 1, min: 0},
        toolBarBtnVPadding: {type: "spin", precision: 0, increment: 1, min: 0},
        xAxisTickColor: {type: "color"},
        is2D: {type: "checkbox"},
        clustered: {type: "checkbox"},
        chartOrder: {type: "input"},
        chartOnTop: {type: "checkbox"},
        autoScaling: {type: "checkbox"},
        allowScaling: {type: "checkbox"},
        startAngX: {type: "spin", precision: 0, increment: 1, min: 0, max: 360},
        startAngY: {type: "spin", precision: 0, increment: 1, min: 0, max: 360},
        endAngX: {type: "spin", precision: 0, increment: 1, min: 0, max: 360},
        endAngY: {type: "spin", precision: 0, increment: 1, min: 0, max: 360},
        cameraAngX: {type: "spin", precision: 0, increment: 1, min: 0, max: 360},
        cameraAngY: {type: "spin", precision: 0, increment: 1, min: 0, max: 360},
        lightAngX: {type: "spin", precision: 0, increment: 1, min: 0, max: 360},
        lightAngY: {type: "spin", precision: 0, increment: 1, min: 0, max: 360},
        intensity: {type: "spin", precision: 0, increment: 1, min: 0, max: 10},
        dynamicShading: {type: "checkbox"},
        bright2D: {type: "checkbox"},
        allowRotation: {type: "checkbox"},
        constrainVerticalRotation: {type: "checkbox"},
        minVerticalRotAngle: {type: "spin", precision: 0, increment: 1, min: 0, max: 360},
        maxVerticalRotAngle: {type: "spin", precision: 0, increment: 1, min: 0, max: 360},
        constrainHorizontalRotation: {type: "checkbox"},
        minHorizontalRotAngle: {type: "spin", precision: 0, increment: 1, min: 0, max: 360},
        maxHorizontalRotAngle: {type: "spin", precision: 0, increment: 1, min: 0, max: 360},
        zDepth: {type: "spin", precision: 0, increment: 1, min: 0},
        zGapPlot: {type: "spin", precision: 0, increment: 1, min: 0},
        yzWallDepth: {type: "spin", precision: 0, increment: 1, min: 0},
        zxWallDepth: {type: "spin", precision: 0, increment: 1, min: 0},
        xyWallDepth: {type: "spin", precision: 0, increment: 1, min: 0},
        drawAnchors: {type: "checkbox"},
        anchorRadius: {type: "spin", precision: 0, increment: 1, min: 0},
        anchorBorderColor: {type: "color"},
        anchorBorderThickness: {type: "spin", precision: 0, increment: 1, min: 0},
        anchorBgColor: {type: "color"},
        anchorAlpha: {type: "spin", precision: 2, increment: 1, min: 0, max: 100},
        anchorBgAlpha: {type: "spin", precision: 2, increment: 1, min: 0, max: 100},
        anchorMinRenderDistance: {type: "spin", precision: 0, increment: 1, min: 0},
        scrollColor: {type: "color"},
        scrollHeight: {type: "spin", precision: 0, increment: 1, min: 0},
        scrollPadding: {type: "spin", precision: 0, increment: 1, min: 0},
        scrollBtnWidth: {type: "spin", precision: 0, increment: 1, min: 0},
        scrollBtnPadding: {type: "spin", precision: 0, increment: 1, min: 0},
        dataStreamURL: {type: "input"},
        refreshInterval: {type: "spin", precision: 0, increment: 1, min: 0},
        clearChartInterval: {type: "spin", precision: 0, increment: 1, min: 0},
        updateInterval: {type: "spin", precision: 0, increment: 1, min: 0},
        numDisplaySets: {type: "spin", precision: 0, increment: 1, min: 1},
        dataStamp: {type: "input"},
        showRealTimeValue: {type: "checkbox"},
        realTimeValueSep: {type: "input"},
        useMessageLog: {type: "checkbox"},
        messageLogWPercent: {type: "spin", precision: 0, increment: 1, min: 0, max: 100},
        messageLogHPercent: {type: "spin", precision: 0, increment: 1, min: 0, max: 100},
        messageLogShowTitle: {type: "checkbox"},
        messageLogTitle: {type: "input"},
        messageLogColor: {type: "color"},
        messageGoesToLog: {type: "checkbox"},
        messageGoesToJS: {type: "checkbox"},
        messageJSHandler: {type: "input"},
        messagePassAllToJS: {type: "checkbox"},
        numDivLines: {type: "spin", precision: 0, increment: 1, min: 1},
        numVDivLines: {type: "spin", precision: 0, increment: 1, min: 1},
        divLineEffect: {type: "listbox", editorListItems: ["", "EMBOSS", "BEVEL", "NONE"]},
        divLineColor: {type: "color"},
        vDivLineColor: {type: "color"},
        divLineThickness: {type: "spin", precision: 0, increment: 1, min: 0, max: 5},
        vDivLineThickness: {type: "spin", precision: 0, increment: 1, min: 0, max: 5},
        divLineAlpha: {type: "spin", precision: 2, increment: 1, min: 0, max: 100},
        vDivLineAlpha: {type: "spin", precision: 2, increment: 1, min: 0, max: 100},
        zeroPlaneMesh: {type: "checkbox"},
        divLineDashed: {type: "checkbox"},
        vDivLineDashed: {type: "checkbox"},
        divLineIsDashed: {type: "checkbox"},
        vDivLineIsDashed: {type: "checkbox"},
        divLineDashLen: {type: "spin", precision: 0, increment: 1, min: 0},
        vDivLineDashLen: {type: "spin", precision: 0, increment: 1, min: 0},
        divLineDashGap: {type: "spin", precision: 0, increment: 1, min: 0},
        vDivLineDashGap: {type: "spin", precision: 0, increment: 1, min: 0},
        zeroPlaneColor: {type: "color"},
        zeroPlaneThickness: {type: "spin", precision: 0, increment: 1, min: 0},
        zeroPlaneAlpha: {type: "spin", precision: 2, increment: 1, min: 0, max: 100},
        showVZeroPlane: {type: "checkbox"},
        vZeroPlaneColor: {type: "color"},
        vZeroPlaneThickness: {type: "spin", precision: 0, increment: 1, min: 0, max: 5},
        vZeroPlaneAlpha: {type: "spin", precision: 2, increment: 1, min: 0, max: 100},
        showPZeroPlaneValue: {type: "checkbox"},
        showSZeroPlaneValue: {type: "checkbox"},
        showZeroPlaneValue: {type: "checkbox"},
        showAlternateHGridColor: {type: "checkbox"},
        alternateHGridColor: {type: "color"},
        alternateHGridAlpha: {type: "spin", precision: 2, increment: 1, min: 0, max: 100},
        showAlternateVGridColor: {type: "checkbox"},
        alternateVGridColor: {type: "color"},
        alternateVGridAlpha: {type: "spin", precision: 2, increment: 1, min: 0, max: 100},
        showZeroPlane: {type: "checkbox"},
        zeroPlaneShowBorder: {type: "checkbox"},
        zeroPlaneBorderColor: {type: "color"},
        drawQuadrant: {type: "checkbox"},
        quadrantXVal: {type: "spin", precision: 0, increment: 1, min: 0},
        quadrantYVal: {type: "spin", precision: 0, increment: 1, min: 0},
        quadrantLineColor: {type: "color"},
        quadrantLineThickness: {type: "spin", precision: 0, increment: 1, min: 0},
        quadrantLineAlpha: {type: "spin", precision: 2, increment: 1, min: 0},
        quadrantLineDashed: {type: "checkbox"},
        quadrantLineDashLen: {type: "spin", precision: 0, increment: 1, min: 0},
        quadrantLineDashGap: {type: "spin", precision: 0, increment: 1, min: 0},
        quadrantLabelTL: {type: "input"},
        quadrantLabelTR: {type: "input"},
        quadrantLabelBL: {type: "input"},
        quadrantLabelBR: {type: "input"},
        quadrantLabelPadding: {type: "spin", precision: 0, increment: 1, min: -5000, max: 5000},
        showLegend: {type: "checkbox"},
        legendPosition: {type: "listbox", editorListItems: ["", "BOTTOM", "RIGHT"]},
        legendCaption: {type: "input"},
        legendItemFontBold: {type: "checkbox"},
        legendItemFont: {type: "helpinput", editorListItems: CONF.designer_data_fontfamily},
        legendItemFontSize: {type: "spin", precision: 0, increment: 1, min: 0, max: 256},
        legendItemFontColor: {type: "color"},
        legendItemHoverFontColor: {type: "color"},
        legendItemHiddenColor: {type: "color"},
        legendCaptionAlignment: {type: "listbox", editorListItems: ["", "left", "center", "right"]},
        legendCaptionBold: {type: "checkbox"},
        legendCaptionFont: {type: "helpinput", editorListItems: CONF.designer_data_fontfamily},
        legendCaptionFontSize: {type: "spin", precision: 0, increment: 1, min: 0, max: 256},
        legendCaptionFontColor: {type: "color"},
        legendIconScale: {type: "spin", precision: 0, increment: 1, min: 1, max: 5},
        legendBgColor: {type: "color"},
        legendBgAlpha: {type: "spin", precision: 2, increment: 1, min: 0, max: 100},
        legendBorderColor: {type: "color"},
        legendBorderThickness: {type: "spin", precision: 0, increment: 1, min: 0},
        legendBorderAlpha: {type: "spin", precision: 2, increment: 1, min: 0, max: 100},
        legendShadow: {type: "checkbox"},
        legendAllowDrag: {type: "checkbox"},
        legendScrollBgColor: {type: "color"},
        legendScrollBarColor: {type: "color"},
        legendScrollBtnColor: {type: "color"},
        reverseLegend: {type: "checkbox"},
        interactiveLegend: {type: "checkbox"},
        legendNumColumns: {type: "spin", precision: 0, increment: 1, min: 0},
        minimiseWrappingInLegend: {type: "checkbox"},
        radius3D: {type: "spin", precision: 0, increment: 1, min: 0, max: 100},
        slicingDistance: {type: "spin", precision: 0, increment: 1, min: 0},
        pieRadius: {type: "spin", precision: 0, increment: 1, min: 0},
        doughnutRadius: {type: "spin", precision: 0, increment: 1, min: 0},
        startingAngle: {type: "spin", precision: 0, increment: 1, min: 0, max: 360},
        enableRotation: {type: "checkbox"},
        pieInnerFaceAlpha: {type: "spin", precision: 2, increment: 1, min: 0, max: 100},
        pieOuterFaceAlpha: {type: "spin", precision: 2, increment: 1, min: 0, max: 100},
        pieYScale: {type: "spin", precision: 0, increment: 1, min: 30, max: 80},
        pieSliceDepth: {type: "spin", precision: 0, increment: 1, min: 0},
        enableSmartLabels: {type: "checkbox"},
        skipOverlapLabels: {type: "checkbox"},
        isSmartLineSlanted: {type: "checkbox"},
        smartLineColor: {type: "color"},
        smartLineThickness: {type: "spin", precision: 0, increment: 1, min: 0},
        smartLineAlpha: {type: "spin", precision: 2, increment: 1, min: 0, max: 100},
        labelDistance: {type: "spin", precision: 0, increment: 1, min: 0},
        smartLabelClearance: {type: "spin", precision: 0, increment: 1, min: 0},
        manageLabelOverflow: {type: "checkbox"},
        showToolTip: {type: "checkbox"},
        toolTipBgColor: {type: "color"},
        toolTipBorderColor: {type: "color"},
        toolTipSepChar: {type: "input", maxlength: 1},
        seriesNameInToolTip: {type: "checkbox"},
        showToolTipShadow: {type: "checkbox"},
        scaleRecursively: {type: "checkbox"},
        scaleSeparator: {type: "input", maxlength: 1},
        forceDecimals: {type: "checkbox"},
        forceYAxisValueDecimals: {type: "checkbox"},
        yAxisValueDecimals: {type: "spin", precision: 0, increment: 1, min: 0, max: 10},
        sYAxisValueDecimals: {type: "spin", precision: 0, increment: 1, min: 0, max: 10},
        canvasPadding: {type: "spin", precision: 0, increment: 1, min: 0, max: 100},
        captionPadding: {type: "spin", precision: 0, increment: 1, min: 0},
        yLabelGap: {type: "spin", precision: 0, increment: 1, min: 0},
        xLabelGap: {type: "spin", precision: 0, increment: 1, min: 0},
        xAxisNamePadding: {type: "spin", precision: 0, increment: 1, min: 0},
        yAxisNamePadding: {type: "spin", precision: 0, increment: 1, min: 0},
        yAxisValuesPadding: {type: "spin", precision: 0, increment: 1, min: 0},
        labelPadding: {type: "spin", precision: 0, increment: 1, min: 0},
        valuePadding: {type: "spin", precision: 0, increment: 1, min: 0},
        realTimeValuePadding: {type: "spin", precision: 0, increment: 1, min: 0},
        plotSpacePercent: {type: "spin", precision: 0, increment: 1, min: 0, max: 80},
        legendPadding: {type: "spin", precision: 0, increment: 1, min: 0},
        chartLeftMargin: {type: "spin", precision: 0, increment: 1, min: 0},
        chartRightMargin: {type: "spin", precision: 0, increment: 1, min: 0},
        chartTopMargin: {type: "spin", precision: 0, increment: 1, min: 0},
        chartBottomMargin: {type: "spin", precision: 0, increment: 1, min: 0},
        canvasLeftMargin: {type: "spin", precision: 0, increment: 1, min: 0},
        canvasRightMargin: {type: "spin", precision: 0, increment: 1, min: 0},
        canvasTopMargin: {type: "spin", precision: 0, increment: 1, min: 0},
        canvasBottomMargin: {type: "spin", precision: 0, increment: 1, min: 0},
        valueFont: {type: "helpinput", editorListItems: CONF.designer_data_fontfamily},
        valueFontColor: {type: "color"},
        valueFontSize: {type: "spin", precision: 0, increment: 1, min: 0, max: 256},
        valueFontBold: {type: "checkbox"},
        valueFontItalic: {type: "checkbox"},
        valueBgColor: {type: "color"},
        valueBorderColor: {type: "color"},
        valueAlpha: {type: "spin", precision: 2, increment: 1, min: 0, max: 100},
        valueFontAlpha: {type: "spin", precision: 2, increment: 1, min: 0, max: 100},
        valueBgAlpha: {type: "spin", precision: 2, increment: 1, min: 0, max: 100},
        valueBorderAlpha: {type: "spin", precision: 2, increment: 1, min: 0, max: 100},
        valueBorderThickness: {type: "spin", precision: 0, increment: 1, min: 0, max: 5},
        valueBorderRadius: {type: "spin", precision: 0, increment: 1, min: 0},
        valueBorderDashed: {type: "checkbox"},
        valueBorderDashGap: {type: "spin", precision: 0, increment: 1, min: 0},
        valueBorderDashLen: {type: "spin", precision: 0, increment: 1, min: 0},
        valueHoverAlpha: {type: "spin", precision: 2, increment: 1, min: 0, max: 100},
        valueFontHoverAlpha: {type: "spin", precision: 2, increment: 1, min: 0, max: 100},
        valueBgHoverAlpha: {type: "spin", precision: 2, increment: 1, min: 0, max: 100},
        valueBorderHoverAlpha: {type: "spin", precision: 2, increment: 1, min: 0, max: 100},
        showValuesOnHover: {type: "checkbox"},
        labelFont: {type: "helpinput", editorListItems: CONF.designer_data_fontfamily},
        labelFontColor: {type: "color"},
        labelFontSize: {type: "spin", precision: 0, increment: 1, min: 0, max: 256},
        labelFontBold: {type: "checkbox"},
        labelFontItalic: {type: "checkbox"},
        labelBgColor: {type: "color"},
        labelBorderColor: {type: "color"},
        labelAlpha: {type: "spin", precision: 2, increment: 1, min: 0, max: 100},
        labelBgAlpha: {type: "spin", precision: 2, increment: 1, min: 0, max: 100},
        labelBorderAlpha: {type: "spin", precision: 2, increment: 1, min: 0, max: 100},
        labelBorderPadding: {type: "spin", precision: 0, increment: 1, min: 0},
        labelBorderRadius: {type: "spin", precision: 0, increment: 1, min: 0},
        labelBorderThickness: {type: "spin", precision: 0, increment: 1, min: 0, max: 5},
        labelBorderDashed: {type: "checkbox"},
        labelBorderDashLen: {type: "spin", precision: 0, increment: 1, min: 0},
        labelBorderDashGap: {type: "spin", precision: 0, increment: 1, min: 0},
        labelLink: {type: "input"},
        showHoverEffect: {type: "checkbox"},
        plotHoverEffect: {type: "checkbox"},
        plotFillHoverColor: {type: "color"},
        plotFillHoverAlpha: {type: "spin", precision: 2, increment: 1, min: 0, max: 100},
        lineThickness: {type: "spin", precision: 0, increment: 1, min: 0, max: 5},
        useRoundEdges: {type: "checkbox"},
        overlapBars: {type: "checkbox"},
        showPlotBorder: {type: "checkbox"},
        plotBorderColor: {type: "color"},
        plotBorderThickness: {type: "spin", precision: 0, increment: 1, min: 0, max: 5},
        plotBorderAlpha: {type: "spin", precision: 2, increment: 1, min: 0, max: 100},
        plotBorderDashed: {type: "checkbox"},
        plotBorderDashLen: {type: "spin", precision: 0, increment: 1, min: 0},
        plotBorderDashGap: {type: "spin", precision: 0, increment: 1, min: 0},
        plotFillAngle: {type: "spin", precision: 0, increment: 1, min: 0, max: 360},
        plotFillRatio: {type: "spin", precision: 0, increment: 1, min: 0, max: 100},
        plotFillAlpha: {type: "spin", precision: 2, increment: 1, min: 0, max: 100},
        plotGradientColor: {type: "color"},
        plotFillColor: {type: "color"},
        overlapColumns: {type: "checkbox"},
        showRadarBorder: {type: "checkbox"},
        radarBorderColor: {type: "color"},
        radarBorderThickness: {type: "spin", precision: 0, increment: 1, min: 0, max: 5},
        radarBorderAlpha: {type: "spin", precision: 2, increment: 1, min: 0, max: 100},
        radarFillColor: {type: "color"},
        radarFillAlpha: {type: "spin", precision: 2, increment: 1, min: 0, max: 100},
        radarSpikeColor: {type: "color"},
        radarSpikeThickness: {type: "spin", precision: 0, increment: 1, min: 0, max: 5},
        radarSpikeAlpha: {type: "spin", precision: 2, increment: 1, min: 0, max: 100},
        formatNumber: {type: "checkbox"},
        yFormatNumber: {type: "checkbox"},
        xFormatNumber: {type: "checkbox"},
        formatNumberScale: {type: "checkbox"},
        yFormatNumberScale: {type: "checkbox"},
        xFormatNumberScale: {type: "checkbox"},
        defaultNumberScale: {type: "input"},
        yDefaultNumberScale: {type: "input"},
        xDefaultNumberScale: {type: "input"},
        numberScaleUnit: {type: "input"},
        yNumberScaleUnit: {type: "input"},
        xNumberScaleUnit: {type: "input"},
        NumberScaleUnit: {type: "input"},
        numberScaleValue: {type: "input"},
        yNumberScaleValue: {type: "input"},
        xNumberScaleValue: {type: "input"},
        sNumberScaleValue: {type: "input"},
        xScaleRecursively: {type: "checkbox"},
        maxScaleRecursion: {type: "spin", precision: 0, increment: 1, min: 0},
        xMaxScaleRecursion: {type: "spin", precision: 0, increment: 1, min: 0},
        numberPrefix: {type: "input", maxlength: 1},
        numberSuffix: {type: "input", maxlength: 1},
        yNumberPrefix: {type: "input", maxlength: 1},
        xNumberPrefix: {type: "input", maxlength: 1},
        yNumberSuffix: {type: "input", maxlength: 1},
        xNumberSuffix: {type: "input", maxlength: 1},
        decimalSeparator: {type: "input", maxlength: 1},
        thousandSeparator: {type: "input", maxlength: 1},
        thousandSeparatorPosition: {type: "spin", precision: 0, increment: 1, min: 0},
        inDecimalSeparator: {type: "input", maxlength: 1},
        inThousandSeparator: {type: "input", maxlength: 1},
        decimals: {type: "spin", precision: 0, increment: 1, min: 0, max: 10},
        forceYAxisValueDecimals: {type: "checkbox"},
        forceXAxisValueDecimals: {type: "checkbox"},
        forceYAxisDecimals: {type: "checkbox"},
        forcePYAxisDecimals: {type: "checkbox"},
        forceSYAxisDecimals: {type: "checkbox"},
        yAxisValueDecimals: {type: "spin", precision: 0, increment: 1, min: 0, max: 10},
        xAxisValueDecimals: {type: "spin", precision: 0, increment: 1, min: 0, max: 10},
        sFormatNumber: {type: "checkbox"},
        sFormatNumberScale: {type: "checkbox"},
        sDefaultNumberScale: {type: "input"},
        sNumberScaleUnit: {type: "input"},
        xScaleSeparator: {type: "input", maxlength: 1},
        sDecimals: {type: "spin", precision: 0, increment: 1, min: 0, max: 10},
        sForceDecimals: {type: "checkbox"},
        sYAxisValueDecimals: {type: "spin", precision: 0, increment: 1, min: 0, max: 10},
        sScaleRecursively: {type: "checkbox"},
        sMaxScaleRecursion: {type: "spin", precision: 0, increment: 1, min: 0},
        sScaleSeparator: {type: "input", maxlength: 1},
        sNumberPrefix: {type: "input", maxlength: 1},
        sNumberSuffix: {type: "input", maxlength: 1},
        forceSYAxisValueDecimals: {type: "checkbox"},
        baseFont: {type: "helpinput", editorListItems: CONF.designer_data_fontfamily},
        baseFontSize: {type: "spin", precision: 0, increment: 1, min: 0, max: 256},
        baseFontColor: {type: "color"},
        outCnvBaseFont: {type: "helpinput", editorListItems: CONF.designer_data_fontfamily},
        outCnvBaseFontSize: {type: "spin", precision: 0, increment: 1, min: 0, max: 256},
        outCnvBaseFontColor: {type: "color"},
        realTimeValueFont: {type: "helpinput", editorListItems: CONF.designer_data_fontfamily},
        realTimeValueFontSize: {type: "spin", precision: 0, increment: 1, min: 0, max: 256},
        realTimeValueFontColor: {type: "color"},
        minValue: {type: "spin", precision: 2},
        maxValue: {type: "spin", precision: 2},
        code: {type: "color"},
        setAdaptiveMin: {type: "checkbox"},
        upperLimit: {type: "spin", precision: 0, increment: 2},
        lowerLimit: {type: "spin", precision: 0, increment: 2},
        lowerLimitDisplay: {type: "checkbox"},
        upperLimitDisplay: {type: "checkbox"},
        showTickMarks: {type: "checkbox"},
        showTickValues: {type: "checkbox"},
        adjustTM: {type: "checkbox"},
        ticksOnRight: {type: "checkbox"},
        ticksBelowGauge: {type: "checkbox"},
        placeTicksInside: {type: "checkbox"},
        trendValueDistance: {type: "spin", precision: 0, increment: 1, min: 0},
        showGaugeLabels: {type: "checkbox"},
        gaugeRoundRadius: {type: "spin", precision: 0, increment: 1, min: 0},
        gaugeFillMix: {type: "input"},
        gaugeFillRatio: {type: "input"},
        majorTMNumber: {type: "spin", precision: 2},
        majorTMColor: {type: "color"},
        majorTMAlpha: {type: "spin", precision: 2, increment: 1, min: 0, max: 100},
        majorTMHeight: {type: "spin", precision: 2, increment: 1, min: 0},
        majorTMThickness: {type: "spin", precision: 0, increment: 1, min: 0},
        minorTMNumber: {type: "spin", precision: 2, increment: 1},
        minorTMColor: {type: "color"},
        minorTMAlpha: {type: "spin", precision: 2, increment: 1, min: 0, max: 100},
        minorTMHeight: {type: "spin", precision: 2, increment: 1, min: 0},
        minorTMThickness: {type: "spin", precision: 0, increment: 1, min: 0},
        tickMarkDistance: {type: "spin", precision: 0, increment: 1, min: 0},
        tickValueDistance: {type: "spin", precision: 0, increment: 1, min: 0},
        tickValueStep: {type: "spin", precision: 0, increment: 1, min: 0},
        tickValueDecimals: {type: "checkbox"},
        forceTickValueDecimals: {type: "checkbox"},
        gaugeFillColor: {type: "color"},
        showGaugeBorder: {type: "checkbox"},
        gaugeBorderColor: {type: "color"},
        gaugeBorderThickness: {type: "spin", precision: 0, increment: 1, min: 0},
        gaugeBorderAlpha: {type: "spin", precision: 2, increment: 1, min: 0, max: 100},
        ledSize: {type: "spin", precision: 0, increment: 1, min: 0},
        ledGap: {type: "spin", precision: 0, increment: 1, min: 0},
        useSameFillColor: {type: "color"},
        useSameFillBgColor: {type: "color"},
        cylOriginX: {type: "spin", precision: 0, increment: 1, min: 0},
        cylOriginY: {type: "spin", precision: 0, increment: 1, min: 0},
        cylRadius: {type: "spin", precision: 0, increment: 1, min: 0},
        cylHeight: {type: "spin", precision: 0, increment: 1, min: 0},
        cylYScale: {type: "spin", precision: 0, increment: 1, min: 0, max: 50},
        cylFillColor: {type: "color"},
        thmBulbRadius: {type: "spin", precision: 0, increment: 1, min: 0},
        thmHeight: {type: "spin", precision: 0, increment: 1, min: 0},
        gaugeFillAlpha: {type: "spin", precision: 2, increment: 1, min: 0, max: 100},
        pivotRadius: {type: "spin", precision: 0, increment: 1, min: 0},
        pivotFillColor: {type: "color"},
        pivotFillAlpha: {type: "spin", precision: 2, increment: 1, min: 0, max: 100},
        pivotFillAngle: {type: "spin", precision: 2, increment: 1, min: 0, max: 360},
        pivotFillType: {type: "listbox", editorListItems: ["", "linear", "radial"]},
        pivotFillMix: {type: "input"},
        pivotFillRatio: {type: "input"},
        showPivotBorder: {type: "checkbox"},
        pivotBorderThickness: {type: "spin", precision: 0, increment: 1, min: 0},
        pivotBorderColor: {type: "color"},
        pivotBorderAlpha: {type: "spin", precision: 2, increment: 1, min: 0, max: 100},
        editMode: {type: "checkbox"},
        pointerRadius: {type: "spin", precision: 0, increment: 1, min: 0},
        pointerBgColor: {type: "color"},
        pointerBgAlpha: {type: "spin", precision: 2, increment: 1, min: 0, max: 100},
        pointerSides: {type: "spin", precision: 0, increment: 1, min: 3, max: 15},
        pointerBorderThickness: {type: "spin", precision: 0, increment: 1, min: 0},
        pointerBorderColor: {type: "color"},
        pointerBorderAlpha: {type: "spin", precision: 2, increment: 1, min: 0, max: 100},
        valueAbovePointer: {type: "checkbox"},
        pointerOnTop: {type: "checkbox"},
        useMarker: {type: "checkbox"},
        markerColor: {type: "color"},
        markerBorderColor: {type: "color"},
        markerRadius: {type: "spin", precision: 0, increment: 1, min: 0},
        markerTooltext: {type: "input"},
        id: {type: "input"},
        radius: {type: "spin", precision: 0, increment: 1, min: 0},
        sides: {type: "spin", precision: 0, increment: 1, min: 3, max: 15},
        valueBelowPivot: {type: "checkbox"},
        autoAlignTickValues: {type: "checkbox"},
        gaugeStartAngle: {type: "spin", precision: 2, increment: 1, min: 0, max: 360},
        gaugeEndAngle: {type: "spin", precision: 2, increment: 1, min: 0, max: 360},
        gaugeOriginX: {type: "spin", precision: 0, increment: 1, min: 0},
        gaugeOriginY: {type: "spin", precision: 0, increment: 1, min: 0},
        gaugeOuterRadius: {type: "spin", precision: 0, increment: 1, min: 0},
        gaugeInnerRadius: {type: "spin", precision: 0, increment: 1, min: 0},
        valueInside: {type: "checkbox"},
        innerRadius: {type: "spin", precision: 0, increment: 1, min: 0},
        valueX: {type: "spin", precision: 0, increment: 1, min: 0},
        valueY: {type: "spin", precision: 0, increment: 1, min: 0},
        baseWidth: {type: "spin", precision: 0, increment: 1, min: 0},
        baseRadius: {type: "spin", precision: 0, increment: 1, min: 0},
        topWidth: {type: "spin", precision: 0, increment: 1, min: 0},
        rearExtension: {type: "spin", precision: 0, increment: 1, min: 0},
        thickness: {type: "spin", precision: 0, increment: 1, min: 0},
        alpha: {type: "spin", precision: 2, increment: 1, min: 0, max: 100},
        dashed: {type: "checkbox"},
        dashLen: {type: "spin", precision: 0, increment: 1, min: 0},
        dashGap: {type: "spin", precision: 0, increment: 1, min: 0},
        label: {type: "input"},
        showLabelBorder: {type: "checkbox"},
        linePosition: {type: "spin", precision: 2, increment: 0.1, min: 0, max: 1},
        labelPosition: {type: "spin", precision: 2, increment: 0.1, min: 0, max: 1},
        labelHAlign: {type: "listbox", editorListItems: ["", "left", "center", "right"]},
        labelVAlign: {type: "listbox", editorListItems: ["", "top", "middle", "bottom"]},
        displayValue: {type: "input"},
        isTrendZone: {type: "checkbox"},
        showOnTop: {type: "checkbox"},
        valueOnRight: {type: "checkbox"},
        parentYAxis: {type: "listbox", editorListItems: ["", "P", "S"]},
        startValue: {type: "spin", precision: 2, increment: 1, headerStyle: "font-weight:bold"},
        endValue: {type: "spin", precision: 2, increment: 1},
        startIndex: {type: "spin", precision: 0, increment: 1, min: 0, headerStyle: "font-weight:bold"},
        endIndex: {type: "spin", precision: 0, increment: 1, min: 0},
        displayAlways: {type: "checkbox"},
        displayWhenCount: {type: "spin", precision: 0, increment: 1, min: 0, headerStyle: "font-weight:bold"},
        valueOnTop: {type: "input"},
        toolText: {type: "input"},
        font: {type: "helpinput", editorListItems: CONF.designer_data_fontfamily},
        fontSize: {type: "spin", precision: 0, increment: 1, min: 0, max: 256},
        fontColor: {type: "color"},
        verticalLineColor: {type: "color"},
        verticalLineThickness: {type: "spin", precision: 0, increment: 1, min: 0, max: 5},
        verticalLineAlpha: {type: "spin", precision: 2, increment: 1, min: 0, max: 100},
        verticalLineDashed: {type: "checkbox"},
        verticalLineDashLen: {type: "spin", precision: 0, increment: 1, min: 0},
        verticalLineDashGap: {type: "spin", precision: 0, increment: 1, min: 0},
        showLabel: {type: "checkbox"},
        showVerticalLine: {type: "checkbox"},
        lineDashed: {type: "checkbox"},
        widthPercent: {type: "spin", precision: 0, increment: 1, min: 1, max: 100},
        labelBorderThickness: {type: "spin", precision: 0, increment: 1, min: 0},
        seriesName: {type: "input"},
        checkForAlerts: {type: "checkbox"},
        plotFillAlpha: {type: "input"},
        ratio: {type: "input"},
        showValues: {type: "checkbox"},
        anchorBorderAlpha: {type: "spin", precision: 2, increment: 1, min: 0, max: 100},
        drawLine: {type: "checkbox"},
        lineColor: {type: "color"},
        lineAlpha: {type: "spin", precision: 2, increment: 1, min: 0, max: 100},
        anchorSides: {type: "spin", precision: 0, increment: 1, min: 3, max: 20},
        lineDashGap: {type: "spin", precision: 0, increment: 1, min: 0},
        includeInLegend: {type: "checkbox"},
        renderAs: {type: "listbox", editorListItems: ["", "COLUMN", "AREA", "LINE"]},
        regressionLineColor: {type: "color"},
        regressionLineThickness: {type: "spin", precision: 0, increment: 1, min: 0},
        regressionLineAlpha: {type: "spin", precision: 2, increment: 1, min: 0, max: 100},
        lineDashLen: {type: "spin", precision: 0, increment: 1, min: 0},
        isSliced: {type: "checkbox"},
        x: {type: 'number', precision: 2, increment: 0.1},
        y: {type: 'number', precision: 2, increment: 0.1},
        z: {type: 'number', precision: 2, increment: 0.1},
        name: {type: 'input'},
        fontBold: {type: "checkbox"},
        fontItalic: {type: "checkbox"},
        borderPadding: {type: "spin", precision: 0, increment: 1, min: 0},
        borderRadius: {type: "spin", precision: 0, increment: 1, min: 0},
        borderThickness: {type: "spin", precision: 0, increment: 1, min: 0},
        borderDashed: {type: "checkbox"},
        borderDashLen: {type: "spin", precision: 0, increment: 1, min: 0},
        borderDashGap: {type: "spin", precision: 0, increment: 1, min: 0},
        link: {type: "input"},
        "value": {type: "number", precision: 2, increment: 0.1, headerStyle: "font-weight:bold"},
        showValue: {type: "checkbox"},
        constrainScale: {type: "checkbox"},
        scaleText: {type: "checkbox"},
        scaleImages: {type: "checkbox"},
        xShift: {type: "spin", precision: 0, increment: 1},
        yShift: {type: "spin", precision: 0, increment: 1},
        grpXShift: {type: "spin", precision: 0, increment: 1},
        grpYShift: {type: "spin", precision: 0, increment: 1},
        showBelow: {type: "checkbox"},
        'visible': {type: "checkbox"},
        textAlign: {type: "listbox", editorListItems: ["", 'left', 'right', 'center']},
        textVAlign: {type: "listbox", editorListItems: ["", 'top', 'bottom', 'middle']},
        rotateText: {type: "checkbox"},
        wrapText: {type: "checkbox"},

        fillPattern: {type: "listbox", editorListItems: ["", "linear", "radial"]},
        yRadius: {type: "spin", precision: 0, increment: 1, min: 0},
        startAngle: {type: "spin", precision: 2, increment: 1, min: 0, max: 360},
        endAngle: {type: "spin", precision: 2, increment: 1, min: 0, max: 360},
        innerRadius: {type: "spin", precision: 0, increment: 1, min: 0},
        url: {type: "input"},
        path: {type: "input"},
        align: {type: "listbox", editorListItems: ["", 'left', 'right', 'center']},
        vAlign: {type: "listbox", editorListItems: ["", 'top', 'bottom', 'middle']},
        bold: {type: "checkbox"},
        italic: {type: "checkbox"},
        leftMargin: {type: "spin", precision: 0, increment: 1, min: 0},
        wrapWidth: {type: "spin", precision: 0, increment: 1, min: 0},
        wrapHeight: {type: "spin", precision: 0, increment: 1, min: 0},
        wrap: {type: "checkbox"},
        annotations: {
            type: {type: 'label', disabled: true},
            x: {type: 'input'},
            y: {type: 'input'},
            toX: {type: 'input'},
            toY: {type: 'input'},
            fillColor: {type: "input"},
            fillRatio: {type: "input"},
            fillAlpha: {type: "input"},
            fillAngle: {type: "input"}
        }
    },
    attrCat: {
        Column2D: {
            chart: [
                {
                    id: "Chart", group: true, sub: [
                        {id: "Titles (Axis) Names", prop: ["caption", "subCaption", "xAxisName", "yAxisName"]},
                        {
                            id: "Functional Attributes",
                            prop: ["animation", "palette", "paletteColors", "showLabels", "maxLabelHeight", "labelDisplay", "useEllipsesWhenOverflow", "rotateLabels", "slantLabels", "labelStep", "staggerLines", "showValues", "rotateValues", "placeValuesInside", "showYAxisValues", "showLimits", "showDivLineValues", "yAxisValuesStep", "showShadow", "adjustDiv", "rotateYAxisName", "yAxisNameWidth", "clickURL", "yAxisMinValue", "yAxisMaxValue", "setAdaptiveYMin", "useDataPlotColorForLabels"]
                        },
                        {
                            id: "Chart Caption Cosmetics",
                            prop: ["captionAlignment", "captionOnTop", "captionFontSize", "subCaptionFontSize", "captionFont", "subCaptionFont", "captionFontColor", "subCaptionFontColor", "captionFontBold", "subCaptionFontBold", "alignCaptionWithCanvas", "captionHorizontalPadding"]
                        },
                        {
                            id: "X-axis Name Cosmetics",
                            prop: ["xAxisNameFontColor", "xAxisNameFontSize", "xAxisNameFontBold", "xAxisNameFontItalic", "xAxisNameBgColor", "xAxisNameBorderColor", "xAxisNameAlpha", "xAxisNameFontAlpha", "xAxisNameBgAlpha", "xAxisNameBorderAlpha", "xAxisNameBorderPadding", "xAxisNameBorderRadius", "xAxisNameBorderThickness", "xAxisNameBorderDashed", "xAxisNameBorderDashLen", "xAxisNameBorderDashGap"]
                        },
                        {
                            id: "Y-axis Name Cosmetics",
                            prop: ["yAxisNameFont", "yAxisNameFontColor", "yAxisNameFontSize", "yAxisNameFontBold", "yAxisNameFontItalic", "yAxisNameBgColor", "yAxisNameBorderColor", "yAxisNameAlpha", "yAxisNameFontAlpha", "yAxisNameBgAlpha", "yAxisNameBorderAlpha", "yAxisNameBorderPadding", "yAxisNameBorderRadius", "yAxisNameBorderThickness", "yAxisNameBorderDashed", "yAxisNameBorderDashLen", "yAxisNameBorderDashGap"]
                        },
                        {
                            id: "Charts Cosmetics",
                            prop: ["bgColor", "bgAlpha", "bgRatio", "bgAngle", "bgImage", "bgImageAlpha", "bgImageDisplayMode", "bgImageVAlign", "bgImageHAlign", "bgImageScale", "canvasBgColor", "canvasBgAlpha", "canvasBgRatio", "canvasBgAngle", "xAxisLineColor", "canvasBorderColor", "canvasBorderThickness", "canvasBorderAlpha", "showBorder", "borderColor", "borderThickness", "borderAlpha", "showVLineLabelBorder", "logoURL", "logoPosition", "logoAlpha", "logoScale", "logoLink"]
                        },
                        {
                            id: "Divisional Lines/Grids",
                            prop: ["numDivLines", "divLineColor", "divLineThickness", "divLineAlpha", "divLineIsDashed", "divLineDashLen", "divLineDashGap", "zeroPlaneColor", "zeroPlaneThickness", "zeroPlaneAlpha", "showZeroPlaneValue", "showAlternateHGridColor", "alternateHGridColor", "alternateHGridAlpha"]
                        },
                        {
                            id: "Tool-tip",
                            prop: ["showToolTip", "toolTipColor", "toolTipBgColor", "toolTipBorderColor", "toolTipSepChar", "showToolTipShadow"]
                        },
                        {
                            id: "Paddings and Margins",
                            prop: ["captionPadding", "xAxisNamePadding", "yAxisNamePadding", "yAxisValuesPadding", "labelPadding", "valuePadding", "plotSpacePercent", "chartLeftMargin", "chartRightMargin", "chartTopMargin", "chartBottomMargin", "canvasLeftMargin", "canvasRightMargin", "canvasTopMargin", "canvasBottomMargin"]
                        }
                    ]
                },
                {
                    id: "Data", group: true, sub: [
                        {
                            id: "Data Plot Cosmetics",
                            prop: ["useRoundEdges", "showPlotBorder", "plotBorderColor", "plotBorderThickness", "plotBorderAlpha", "plotBorderDashed", "plotBorderDashLen", "plotBorderDashGap", "plotFillAngle", "plotFillRatio", "plotFillAlpha", "plotGradientColor"]
                        },
                        {
                            id: "Data Value Cosmetics",
                            prop: ["valueFont", "valueFontColor", "valueFontSize", "valueFontBold", "valueFontItalic", "valueBgColor", "valueBorderColor", "valueAlpha", "valueFontAlpha", "valueBgAlpha", "valueBorderAlpha", "valueBorderThickness", "valueBorderRadius", "valueBorderDashed", "valueBorderDashGap", "valueBorderDashLen", "valueHoverAlpha", "valueFontHoverAlpha", "valueBgHoverAlpha", "valueBorderHoverAlpha", "showValuesOnHover"]
                        },
                        {
                            id: "Data Label Cosmetics",
                            prop: ["labelFont", "labelFontColor", "labelFontSize", "labelFontBold", "labelFontItalic", "labelBgColor", "labelBorderColor", "labelAlpha", "labelBgAlpha", "labelBorderAlpha", "labelBorderPadding", "labelBorderRadius", "labelBorderThickness", "labelBorderDashed", "labelBorderDashLen", "labelBorderDashGap", "labelLink"]
                        },
                        {
                            id: "Data Plot Hover Effects",
                            prop: ["showHoverEffect", "plotHoverEffect", "plotFillHoverColor", "plotFillHoverAlpha"]
                        }
                    ]
                },
                {
                    id: "Others", group: true, sub: [
                        {
                            id: "Number Formatting",
                            prop: ["formatNumber", "formatNumberScale", "defaultNumberScale", "numberScaleUnit", "numberScaleValue", "scaleRecursively", "maxScaleRecursion", "scaleSeparator", "numberPrefix", "numberSuffix", "decimalSeparator", "thousandSeparator", "thousandSeparatorPosition", "inDecimalSeparator", "inThousandSeparator", "decimals", "forceDecimals", "forceYAxisValueDecimals", "yAxisValueDecimals"]
                        },
                        {
                            id: "Font Properties",
                            prop: ["baseFont", "baseFontSize", "baseFontColor", "outCnvBaseFont", "outCnvBaseFontSize", "outCnvBaseFontColor"]
                        }
                    ]
                }
            ],
            "set": ["label", "value", "displayValue", "color", "link", "toolText", "showLabel", "showValue", "dashed", "alpha", "labelFont", "labelFontColor", "labelFontSize", "labelFontBold", "labelFontItalic", "labelBgColor", "labelBorderColor", "labelAlpha", "labelBgAlpha", "labelBorderAlpha", "labelBorderPadding", "labelBorderRadius", "labelBorderThickness", "labelBorderDashed", "labelBorderDashLen", "labelBorderDashGap", "labelLink"],
            "Vertical data separator lines": ["linePosition", "color", "thickness", "showLabelBorder", "label", "labelPosition", "alpha", "dashed", "dashLen", "dashGap", "labelHAlign", "labelVAlign"],
            "Trend Lines": ["startValue", "endValue", "displayValue", "color", "isTrendZone", "showOnTop", "thickness", "alpha", "dashed", "dashLen", "dashGap", "valueOnRight", "toolText"]
        },
        Column3D: {
            chart: [
                {
                    id: "Chart", group: true, sub: [
                        {
                            id: "Titles (Axis) Names",
                            prop: ["caption", "subCaption", "xAxisName", "yAxisName", "rotateYAxisName", "yAxisNameWidth", "centerYaxisName"]
                        },
                        {
                            id: "Functional Attributes",
                            prop: ["animation", "palette", "paletteColors", "showLabels", "maxLabelHeight", "labelDisplay", "useEllipsesWhenOverflow", "rotateLabels", "slantLabels", "labelStep", "staggerLines", "showValues", "rotateValues", "placeValuesInside", "showYAxisValues", "showLimits", "showDivLineValues", "yAxisValuesStep", "showShadow", "adjustDiv", "rotateYAxisName", "yAxisNameWidth", "clickURL", "yAxisMinValue", "yAxisMaxValue", "setAdaptiveYMin", "useDataPlotColorForLabels"]
                        },
                        {
                            id: "Chart Caption Cosmetics",
                            prop: ["captionAlignment", "captionOnTop", "captionFontSize", "subCaptionFontSize", "captionFont", "subCaptionFont", "captionFontColor", "subCaptionFontColor", "captionFontBold", "subCaptionFontBold", "alignCaptionWithCanvas", "captionHorizontalPadding"]
                        },
                        {
                            id: "X-axis Name Cosmetics",
                            prop: ["xAxisNameFontColor", "xAxisNameFontSize", "xAxisNameFontBold", "xAxisNameFontItalic", "xAxisNameBgColor", "xAxisNameBorderColor", "xAxisNameAlpha", "xAxisNameFontAlpha", "xAxisNameBgAlpha", "xAxisNameBorderAlpha", "xAxisNameBorderPadding", "xAxisNameBorderRadius", "xAxisNameBorderThickness", "xAxisNameBorderDashed", "xAxisNameBorderDashLen", "xAxisNameBorderDashGap"]
                        },
                        {
                            id: "Y-axis Name Cosmetics",
                            prop: ["yAxisNameFont", "yAxisNameFontColor", "yAxisNameFontSize", "yAxisNameFontBold", "yAxisNameFontItalic", "yAxisNameBgColor", "yAxisNameBorderColor", "yAxisNameAlpha", "yAxisNameFontAlpha", "yAxisNameBgAlpha", "yAxisNameBorderAlpha", "yAxisNameBorderPadding", "yAxisNameBorderRadius", "yAxisNameBorderThickness", "yAxisNameBorderDashed", "yAxisNameBorderDashLen", "yAxisNameBorderDashGap"]
                        },
                        {
                            id: "Charts Cosmetics",
                            prop: ["bgColor", "bgAlpha", "bgRatio", "bgAngle", "bgImage", "bgImageAlpha", "bgImageDisplayMode", "bgImageVAlign", "bgImageHAlign", "bgImageScale", "canvasBgColor", "canvasBgAlpha", "canvasBaseColor", "showCanvasBg", "xAxisLineColor", "showCanvasBase", "canvasBaseDepth", "canvasBgDepth", "showBorder", "borderColor", "borderThickness", "borderAlpha", "showVLineLabelBorder", "logoURL", "logoPosition", "logoAlpha", "logoScale", "logoLink"]
                        },
                        {
                            id: "Divisional Lines/Grids",
                            prop: ["numDivLines", "divLineColor", "divLineThickness", "divLineAlpha", "divLineIsDashed", "divLineDashLen", "divLineDashGap", "zeroPlaneColor", "zeroPlaneThickness", "zeroPlaneAlpha", "showZeroPlaneValue", "showAlternateHGridColor", "alternateHGridColor", "alternateHGridAlpha"]
                        },
                        {
                            id: "Tool-tip",
                            prop: ["showToolTip", "toolTipColor", "toolTipBgColor", "toolTipBorderColor", "toolTipSepChar", "showToolTipShadow"]
                        },
                        {
                            id: "Paddings and Margins",
                            prop: ["captionPadding", "xAxisNamePadding", "yAxisNamePadding", "yAxisValuesPadding", "labelPadding", "valuePadding", "plotSpacePercent", "chartLeftMargin", "chartRightMargin", "chartTopMargin", "chartBottomMargin", "canvasLeftMargin", "canvasRightMargin", "canvasTopMargin", "canvasBottomMargin"]
                        }
                    ]
                },
                {
                    id: "Data", group: true, sub: [
                        {
                            id: "Data Plot Cosmetics",
                            prop: ["showPlotBorder", "plotBorderColor", "plotBorderAlpha", "plotFillAlpha", "overlapColumns"]
                        },
                        {
                            id: "Data Value Cosmetics",
                            prop: ["valueFont", "valueFontColor", "valueFontSize", "valueFontBold", "valueFontItalic", "valueBgColor", "valueBorderColor", "valueAlpha", "valueFontAlpha", "valueBgAlpha", "valueBorderAlpha", "valueBorderThickness", "valueBorderRadius", "valueBorderDashed", "valueBorderDashGap", "valueBorderDashLen", "valueHoverAlpha", "valueFontHoverAlpha", "valueBgHoverAlpha", "valueBorderHoverAlpha", "showValuesOnHover"]
                        },
                        {
                            id: "Data Label Cosmetics",
                            prop: ["labelFont", "labelFontColor", "labelFontSize", "labelFontBold", "labelFontItalic", "labelBgColor", "labelBorderColor", "labelAlpha", "labelBgAlpha", "labelBorderAlpha", "labelBorderPadding", "labelBorderRadius", "labelBorderThickness", "labelBorderDashed", "labelBorderDashLen", "labelBorderDashGap", "labelLink"]
                        },
                        {
                            id: "Data Plot Hover Effects",
                            prop: ["showHoverEffect", "plotHoverEffect", "plotFillHoverColor", "plotFillHoverAlpha"]
                        }
                    ]
                },
                {
                    id: "Others", group: true, sub: [
                        {
                            id: "Number Formatting",
                            prop: ["formatNumber", "formatNumberScale", "defaultNumberScale", "numberScaleUnit", "numberScaleValue", "scaleRecursively", "maxScaleRecursion", "scaleSeparator", "numberPrefix", "numberSuffix", "decimalSeparator", "thousandSeparator", "thousandSeparatorPosition", "inDecimalSeparator", "inThousandSeparator", "decimals", "forceDecimals", "forceYAxisValueDecimals", "yAxisValueDecimals"]
                        },
                        {
                            id: "Font Properties",
                            prop: ["baseFont", "baseFontSize", "baseFontColor", "outCnvBaseFont", "outCnvBaseFontSize", "outCnvBaseFontColor"]
                        }
                    ]
                }
            ],
            "set": ["label", "value", "displayValue", "color", "link", "toolText", "showLabel", "showValue", "dashed", "alpha", "labelFont", "labelFontColor", "labelFontSize", "labelFontBold", "labelFontItalic", "labelBgColor", "labelBorderColor", "labelAlpha", "labelBgAlpha", "labelBorderAlpha", "labelBorderPadding", "labelBorderRadius", "labelBorderThickness", "labelBorderDashed", "labelBorderDashLen", "labelBorderDashGap", "labelLink"],
            "Vertical data separator lines": ["linePosition", "color", "thickness", "showLabelBorder", "label", "labelPosition", "alpha", "dashed", "dashLen", "dashGap", "labelHAlign", "labelVAlign"],
            "Trend Lines": ["startValue", "endValue", "displayValue", "color", "isTrendZone", "thickness", "alpha", "dashed", "dashLen", "dashGap", "valueOnRight", "toolText"]
        },
        Pie2D: {
            chart: [
                {
                    id: "Chart", group: true, sub: [
                        {id: "Titles (Axis) Names", prop: ["caption", "subCaption"]},
                        {
                            id: "Functional Attributes",
                            prop: ["animation", "palette", "paletteColors", "showZeroPies", "showPercentValues", "showPercentInToolTip", "showLabels", "showValues", "labelSepChar", "clickURL", "useDataPlotColorForLabels"]
                        },
                        {
                            id: "Chart Caption Cosmetics",
                            prop: ["captionAlignment", "captionOnTop", "captionFontSize", "subCaptionFontSize", "captionFont", "subCaptionFont", "captionFontColor", "subCaptionFontColor", "captionFontBold", "subCaptionFontBold", "alignCaptionWithCanvas", "captionHorizontalPadding"]
                        },
                        {
                            id: "Charts Cosmetics",
                            prop: ["showBorder", "borderColor", "borderThickness", "borderAlpha", "bgColor", "bgAlpha", "bgRatio", "bgAngle", "bgImage", "bgImageAlpha", "bgImageDisplayMode", "bgImageVAlign", "bgImageHAlign", "bgImageScale", "logoURL", "logoPosition", "logoAlpha", "logoScale", "logoLink"]
                        },
                        {
                            id: "Legend Properties",
                            prop: ["showLegend", "legendPosition", "legendCaption", "legendIconScale", "legendBgColor", "legendBgAlpha", "legendBorderColor", "legendBorderThickness", "legendBorderAlpha", "legendShadow", "legendAllowDrag", "legendScrollBgColor", "legendScrollBarColor", "legendScrollBtnColor", "reverseLegend", "interactiveLegend", "legendNumColumns", "minimiseWrappingInLegend"]
                        },
                        {
                            id: "Tool-tip",
                            prop: ["showToolTip", "toolTipColor", "toolTipBgColor", "toolTipBorderColor", "toolTipSepChar", "showToolTipShadow"]
                        },
                        {
                            id: "Paddings and Margins",
                            prop: ["captionPadding", "chartLeftMargin", "chartRightMargin", "chartTopMargin", "chartBottomMargin"]
                        }
                    ]
                },
                {
                    id: "Data", group: true, sub: [
                        {
                            id: "Pie / Doughnut Properties",
                            prop: ["radius3D", "slicingDistance", "pieRadius", "startingAngle", "enableRotation"]
                        },
                        {
                            id: "Data Label Cosmetics",
                            prop: ["labelFont", "labelFontColor", "labelFontSize", "labelFontBold", "labelFontItalic", "labelBgColor", "labelBorderColor", "labelAlpha", "labelBgAlpha", "labelBorderAlpha", "labelBorderPadding", "labelBorderRadius", "labelBorderThickness", "labelBorderDashed", "labelBorderDashLen", "labelBorderDashGap", "labelLink"]
                        },
                        {
                            id: "Smart Labels & Lines",
                            prop: ["enableSmartLabels", "skipOverlapLabels", "isSmartLineSlanted", "smartLineColor", "smartLineThickness", "smartLineAlpha", "labelDistance", "smartLabelClearance", "manageLabelOverflow", "useEllipsesWhenOverflow"]
                        },
                        {
                            id: "Data Plot Cosmetics",
                            prop: ["showPlotBorder", "plotBorderColor", "plotBorderThickness", "plotBorderAlpha", "plotFillAlpha", "showShadow", "use3DLighting"]
                        },
                        {
                            id: "Data Plot Hover Effects",
                            prop: ["showHoverEffect", "plotHoverEffect", "plotFillHoverColor", "plotFillHoverAlpha"]
                        }
                    ]
                },
                {
                    id: "Others", group: true, sub: [
                        {
                            id: "Number Formatting",
                            prop: ["formatNumber", "formatNumberScale", "defaultNumberScale", "numberScaleUnit", "numberScaleValue", "scaleRecursively", "maxScaleRecursion", "scaleSeparator", "numberPrefix", "numberSuffix", "decimalSeparator", "thousandSeparator", "thousandSeparatorPosition", "inDecimalSeparator", "inThousandSeparator", "decimals", "forceDecimals"]
                        },
                        {id: "Font Properties", prop: ["baseFont", "baseFontSize", "baseFontColor"]}
                    ]
                }
            ],
            "set": ["label", "value", "borderColor", "borderAlpha", "isSliced", "displayValue", "color", "link", "toolText", "dashed", "alpha", "showLabel", "showValue", "labelFont", "labelFontColor", "labelFontSize", "labelFontBold", "labelFontItalic", "labelBgColor", "labelBorderColor", "labelAlpha", "labelBgAlpha", "labelBorderAlpha", "labelBorderPadding", "labelBorderRadius", "labelBorderThickness", "labelBorderDashed", "labelBorderDashLen", "labelBorderDashGap", "labelLink"]
        },
        Pie3D: {
            chart: [
                {
                    id: "Chart", group: true, sub: [
                        {id: "Titles (Axis) Names", prop: ["caption", "subCaption"]},
                        {
                            id: "Functional Attributes",
                            prop: ["animation", "palette", "paletteColors", "showZeroPies", "showPercentValues", "showPercentInToolTip", "showLabels", "showValues", "labelSepChar", "clickURL", "useDataPlotColorForLabels"]
                        },
                        {
                            id: "Chart Caption Cosmetics",
                            prop: ["captionAlignment", "captionOnTop", "captionFontSize", "subCaptionFontSize", "captionFont", "subCaptionFont", "captionFontColor", "subCaptionFontColor", "captionFontBold", "subCaptionFontBold", "alignCaptionWithCanvas", "captionHorizontalPadding"]
                        },
                        {
                            id: "Charts Cosmetics",
                            prop: ["showBorder", "borderColor", "borderThickness", "borderAlpha", "bgColor", "bgAlpha", "bgRatio", "bgAngle", "bgImage", "bgImageAlpha", "bgImageDisplayMode", "bgImageVAlign", "bgImageHAlign", "bgImageScale", "logoURL", "logoPosition", "logoAlpha", "logoScale", "logoLink"]
                        },
                        {
                            id: "Legend Properties",
                            prop: ["showLegend", "legendPosition", "legendCaption", "legendIconScale", "legendBgColor", "legendBgAlpha", "legendBorderColor", "legendBorderThickness", "legendBorderAlpha", "legendShadow", "legendAllowDrag", "legendScrollBgColor", "legendScrollBarColor", "legendScrollBtnColor", "reverseLegend", "interactiveLegend", "legendNumColumns", "minimiseWrappingInLegend"]
                        },
                        {
                            id: "Tool-tip",
                            prop: ["showToolTip", "toolTipColor", "toolTipBgColor", "toolTipBorderColor", "toolTipSepChar", "showToolTipShadow"]
                        },
                        {
                            id: "Paddings and Margins",
                            prop: ["captionPadding", "chartLeftMargin", "chartRightMargin", "chartTopMargin", "chartBottomMargin"]
                        }
                    ]
                },
                {
                    id: "Data", group: true, sub: [
                        {
                            id: "Pie / Doughnut Properties",
                            prop: ["slicingDistance", "pieRadius", "startingAngle", "enableRotation", "pieInnerFaceAlpha", "pieOuterFaceAlpha", "pieYScale", "pieSliceDepth"]
                        },
                        {
                            id: "Data Label Cosmetics",
                            prop: ["labelFont", "labelFontColor", "labelFontSize", "labelFontBold", "labelFontItalic", "labelBgColor", "labelBorderColor", "labelAlpha", "labelBgAlpha", "labelBorderAlpha", "labelBorderPadding", "labelBorderRadius", "labelBorderThickness", "labelBorderDashed", "labelBorderDashLen", "labelBorderDashGap", "labelLink"]
                        },
                        {
                            id: "Smart Labels & Lines",
                            prop: ["enableSmartLabels", "skipOverlapLabels", "isSmartLineSlanted", "smartLineColor", "smartLineThickness", "smartLineAlpha", "labelDistance", "smartLabelClearance", "manageLabelOverflow", "useEllipsesWhenOverflow"]
                        },
                        {
                            id: "Data Plot Cosmetics",
                            prop: ["showPlotBorder", "plotBorderColor", "plotBorderThickness", "plotBorderAlpha", "plotFillAlpha", "use3DLighting"]
                        },
                        {
                            id: "Data Plot Hover Effects",
                            prop: ["showHoverEffect", "plotHoverEffect", "plotFillHoverColor", "plotFillHoverAlpha"]
                        }
                    ]
                },
                {
                    id: "Others", group: true, sub: [
                        {
                            id: "Number Formatting",
                            prop: ["formatNumber", "formatNumberScale", "defaultNumberScale", "numberScaleUnit", "numberScaleValue", "scaleRecursively", "maxScaleRecursion", "scaleSeparator", "numberPrefix", "numberSuffix", "decimalSeparator", "thousandSeparator", "thousandSeparatorPosition", "inDecimalSeparator", "inThousandSeparator", "decimals", "forceDecimals"]
                        },
                        {id: "Font Properties", prop: ["baseFont", "baseFontSize", "baseFontColor"]}
                    ]
                }
            ],
            "set": ["label", "value", "borderColor", "borderAlpha", "isSliced", "displayValue", "color", "link", "toolText", "dashed", "alpha", "showLabel", "showValue", "labelFont", "labelFontColor", "labelFontSize", "labelFontBold", "labelFontItalic", "labelBgColor", "labelBorderColor", "labelAlpha", "labelBgAlpha", "labelBorderAlpha", "labelBorderPadding", "labelBorderRadius", "labelBorderThickness", "labelBorderDashed", "labelBorderDashLen", "labelBorderDashGap", "labelLink"]
        },
        Line: {
            chart: [
                {
                    id: "Chart", group: true, sub: [
                        {id: "Titles (Axis) Names", prop: ["caption", "subCaption", "xAxisName", "yAxisName"]},
                        {
                            id: "Functional Attributes",
                            prop: ["animation", "palette", "paletteColors", "connectNullData", "showLabels", "maxLabelHeight", "labelDisplay", "useEllipsesWhenOverflow", "rotateLabels", "slantLabels", "labelStep", "staggerLines", "showValues", "valuePosition", "rotateValues", "showYAxisValues", "showLimits", "showDivLineValues", "yAxisValuesStep", "adjustDiv", "rotateYAxisName", "yAxisNameWidth", "clickURL", "yAxisMinValue", "yAxisMaxValue", "setAdaptiveYMin"]
                        },
                        {
                            id: "Chart Caption Cosmetics",
                            prop: ["captionAlignment", "captionOnTop", "captionFontSize", "subCaptionFontSize", "captionFont", "subCaptionFont", "captionFontColor", "subCaptionFontColor", "captionFontBold", "subCaptionFontBold", "alignCaptionWithCanvas", "captionHorizontalPadding"]
                        },
                        {
                            id: "X-axis Name Cosmetics",
                            prop: ["xAxisNameFontColor", "xAxisNameFontSize", "xAxisNameFontBold", "xAxisNameFontItalic", "xAxisNameBgColor", "xAxisNameBorderColor", "xAxisNameAlpha", "xAxisNameFontAlpha", "xAxisNameBgAlpha", "xAxisNameBorderAlpha", "xAxisNameBorderPadding", "xAxisNameBorderRadius", "xAxisNameBorderThickness", "xAxisNameBorderDashed", "xAxisNameBorderDashLen", "xAxisNameBorderDashGap"]
                        },
                        {
                            id: "Y-axis Name Cosmetics",
                            prop: ["yAxisNameFont", "yAxisNameFontColor", "yAxisNameFontSize", "yAxisNameFontBold", "yAxisNameFontItalic", "yAxisNameBgColor", "yAxisNameBorderColor", "yAxisNameAlpha", "yAxisNameFontAlpha", "yAxisNameBgAlpha", "yAxisNameBorderAlpha", "yAxisNameBorderPadding", "yAxisNameBorderRadius", "yAxisNameBorderThickness", "yAxisNameBorderDashed", "yAxisNameBorderDashLen", "yAxisNameBorderDashGap"]
                        },
                        {
                            id: "Charts Cosmetics",
                            prop: ["bgColor", "bgAlpha", "bgRatio", "bgAngle", "bgImage", "bgImageAlpha", "bgImageDisplayMode", "bgImageVAlign", "bgImageHAlign", "bgImageScale", "canvasBgColor", "canvasBgAlpha", "canvasBgRatio", "canvasBgAngle", "xAxisLineColor", "canvasBorderColor", "canvasBorderThickness", "canvasBorderAlpha", "showBorder", "borderColor", "borderThickness", "borderAlpha", "showVLineLabelBorder", "logoURL", "logoPosition", "logoAlpha", "logoScale", "logoLink"]
                        },
                        {
                            id: "Anchors",
                            prop: ["drawAnchors", "anchorSides", "anchorRadius", "anchorBorderColor", "anchorBorderThickness", "anchorBgColor", "anchorAlpha", "anchorBgAlpha"]
                        },
                        {
                            id: "Divisional Lines/Grids",
                            prop: ["numDivLines", "divLineColor", "divLineThickness", "divLineAlpha", "divLineIsDashed", "divLineDashLen", "divLineDashGap", "zeroPlaneColor", "zeroPlaneThickness", "zeroPlaneAlpha", "showZeroPlaneValue", "showAlternateHGridColor", "alternateHGridColor", "alternateHGridAlpha", "numVDivLines", "vDivLineColor", "vDivLineThickness", "vDivLineAlpha", "vDivLineIsDashed", "vDivLineDashLen", "vDivLineDashGap", "showAlternateVGridColor", "alternateVGridColor", "alternateVGridAlpha", "showZeroPlane"]
                        },
                        {
                            id: "Tool-tip",
                            prop: ["showToolTip", "toolTipColor", "toolTipBgColor", "toolTipBorderColor", "toolTipSepChar", "showToolTipShadow"]
                        },
                        {
                            id: "Paddings and Margins",
                            prop: ["captionPadding", "xAxisNamePadding", "yAxisNamePadding", "yAxisValuesPadding", "labelPadding", "valuePadding", "plotSpacePercent", "chartLeftMargin", "chartRightMargin", "chartTopMargin", "chartBottomMargin", "canvasLeftMargin", "canvasRightMargin", "canvasTopMargin", "canvasBottomMargin"]
                        }
                    ]
                },
                {
                    id: "Data", group: true, sub: [
                        {
                            id: "Data Plot Cosmetics",
                            prop: ["showShadow", "lineColor", "lineThickness", "lineAlpha", "lineDashed", "lineDashLen", "lineDashGap"]
                        },
                        {
                            id: "Data Value Cosmetics",
                            prop: ["valueFont", "valueFontColor", "valueFontSize", "valueFontBold", "valueFontItalic", "valueBgColor", "valueBorderColor", "valueAlpha", "valueFontAlpha", "valueBgAlpha", "valueBorderAlpha", "valueBorderThickness", "valueBorderRadius", "valueBorderDashed", "valueBorderDashGap", "valueBorderDashLen", "valueHoverAlpha", "valueFontHoverAlpha", "valueBgHoverAlpha", "valueBorderHoverAlpha", "showValuesOnHover"]
                        },
                        {
                            id: "Data Label Cosmetics",
                            prop: ["labelFont", "labelFontColor", "labelFontSize", "labelFontBold", "labelFontItalic", "labelBgColor", "labelBorderColor", "labelAlpha", "labelBgAlpha", "labelBorderAlpha", "labelBorderPadding", "labelBorderRadius", "labelBorderThickness", "labelBorderDashed", "labelBorderDashLen", "labelBorderDashGap", "labelLink"]
                        },
                        {
                            id: "Data Plot Hover Effects",
                            prop: ["showHoverEffect", "plotHoverEffect", "plotFillHoverColor", "plotFillHoverAlpha"]
                        }
                    ]
                },
                {
                    id: "Others", group: true, sub: [
                        {
                            id: "Number Formatting",
                            prop: ["formatNumber", "formatNumberScale", "defaultNumberScale", "numberScaleUnit", "numberScaleValue", "scaleRecursively", "maxScaleRecursion", "scaleSeparator", "numberPrefix", "numberSuffix", "decimalSeparator", "thousandSeparator", "thousandSeparatorPosition", "inDecimalSeparator", "inThousandSeparator", "decimals", "forceDecimals", "forceYAxisValueDecimals", "yAxisValueDecimals"]
                        },
                        {
                            id: "Font Properties",
                            prop: ["baseFont", "baseFontSize", "baseFontColor", "outCnvBaseFont", "outCnvBaseFontSize", "outCnvBaseFontColor"]
                        }
                    ]
                }
            ],
            "set": ["label", "value", "displayValue", "color", "link", "toolText", "showLabel", "showValue", "valuePosition", "dashed", "alpha", "anchorSides", "anchorRadius", "anchorBorderColor", "anchorBorderThickness", "anchorBgColor", "anchorAlpha", "anchorBgAlpha", "labelFont", "labelFontColor", "labelFontSize", "labelFontBold", "labelFontItalic", "labelBgColor", "labelBorderColor", "labelAlpha", "labelBgAlpha", "labelBorderAlpha", "labelBorderPadding", "labelBorderRadius", "labelBorderThickness", "labelBorderDashed", "labelBorderDashLen", "labelBorderDashGap", "labelLink"],
            "Vertical data separator lines": ["linePosition", "color", "thickness", "showLabelBorder", "label", "labelPosition", "alpha", "dashed", "dashLen", "dashGap", "labelHAlign", "labelVAlign"],
            "Trend Lines": ["startValue", "endValue", "displayValue", "color", "isTrendZone", "showOnTop", "thickness", "alpha", "dashed", "dashLen", "dashGap", "valueOnRight", "toolText"]
        },
        Bar2D: {
            chart: [
                {
                    id: "Chart", group: true, sub: [
                        {id: "Titles (Axis) Names", prop: ["caption", "subCaption", "xAxisName", "yAxisName"]},
                        {
                            id: "Functional Attributes",
                            prop: ["animation", "palette", "paletteColors", "showLabels", "labelStep", "maxLabelWidthPercent", "useEllipsesWhenOverflow", "showValues", "placeValuesInside", "showYAxisValues", "showLimits", "showDivLineValues", "yAxisValuesStep", "showShadow", "adjustDiv", "rotateXAxisName", "xAxisNameWidth", "clickURL", "yAxisMinValue", "yAxisMaxValue", "setAdaptiveYMin", "centerXaxisName", "useDataPlotColorForLabels"]
                        },
                        {
                            id: "Chart Caption Cosmetics",
                            prop: ["captionAlignment", "captionOnTop", "captionFontSize", "subCaptionFontSize", "captionFont", "subCaptionFont", "captionFontColor", "subCaptionFontColor", "captionFontBold", "subCaptionFontBold", "alignCaptionWithCanvas", "captionHorizontalPadding"]
                        },
                        {
                            id: "X-axis Name Cosmetics",
                            prop: ["xAxisNameFontColor", "xAxisNameFontSize", "xAxisNameFontBold", "xAxisNameFontItalic", "xAxisNameBgColor", "xAxisNameBorderColor", "xAxisNameAlpha", "xAxisNameFontAlpha", "xAxisNameBgAlpha", "xAxisNameBorderAlpha", "xAxisNameBorderPadding", "xAxisNameBorderRadius", "xAxisNameBorderThickness", "xAxisNameBorderDashed", "xAxisNameBorderDashLen", "xAxisNameBorderDashGap"]
                        },
                        {
                            id: "Y-axis Name Cosmetics",
                            prop: ["yAxisNameFont", "yAxisNameFontColor", "yAxisNameFontSize", "yAxisNameFontBold", "yAxisNameFontItalic", "yAxisNameBgColor", "yAxisNameBorderColor", "yAxisNameAlpha", "yAxisNameFontAlpha", "yAxisNameBgAlpha", "yAxisNameBorderAlpha", "yAxisNameBorderPadding", "yAxisNameBorderRadius", "yAxisNameBorderThickness", "yAxisNameBorderDashed", "yAxisNameBorderDashLen", "yAxisNameBorderDashGap"]
                        },
                        {
                            id: "Charts Cosmetics",
                            prop: ["showBorder", "borderColor", "borderThickness", "borderAlpha", "bgColor", "bgAlpha", "bgRatio", "bgAngle", "bgImage", "bgImageAlpha", "bgImageDisplayMode", "bgImageVAlign", "bgImageHAlign", "bgImageScale", "xAxisLineColor", "canvasBgColor", "canvasBgAlpha", "canvasBgRatio", "canvasBgAngle", "canvasBorderColor", "canvasBorderThickness", "canvasBorderAlpha", "showVLineLabelBorder", "logoURL", "logoPosition", "logoAlpha", "logoScale", "logoLink"]
                        },
                        {
                            id: "Divisional Lines/Grids",
                            prop: ["numDivLines", "divLineColor", "divLineThickness", "divLineAlpha", "divLineDashed", "divLineDashLen", "divLineDashGap", "zeroPlaneColor", "zeroPlaneThickness", "zeroPlaneAlpha", "showZeroPlaneValue", "showAlternateVGridColor", "alternateVGridColor", "alternateVGridAlpha"]
                        },
                        {
                            id: "Tool-tip",
                            prop: ["showToolTip", "toolTipColor", "toolTipBgColor", "toolTipBorderColor", "toolTipSepChar", "showToolTipShadow"]
                        },
                        {
                            id: "Paddings and Margins",
                            prop: ["captionPadding", "xAxisNamePadding", "yAxisNamePadding", "yAxisValuesPadding", "labelPadding", "valuePadding", "plotSpacePercent", "chartLeftMargin", "chartRightMargin", "chartTopMargin", "chartBottomMargin", "canvasLeftMargin", "canvasRightMargin", "canvasTopMargin", "canvasBottomMargin"]
                        },
                        {
                            id: "Data Plot Hover Effects",
                            prop: ["showHoverEffect", "plotHoverEffect", "plotFillHoverColor", "plotFillHoverAlpha"]
                        }
                    ]
                },
                {
                    id: "Data", group: true, sub: [
                        {
                            id: "Data Plot Cosmetics",
                            prop: ["useRoundEdges", "showPlotBorder", "plotBorderColor", "plotBorderThickness", "plotBorderAlpha", "plotBorderDashed", "plotBorderDashLen", "plotBorderDashGap", "plotFillAngle", "plotFillRatio", "plotFillAlpha", "plotGradientColor"]
                        },
                        {
                            id: "Data Value Cosmetics",
                            prop: ["valueFont", "valueFontColor", "valueFontSize", "valueFontBold", "valueFontItalic", "valueBgColor", "valueBorderColor", "valueAlpha", "valueFontAlpha", "valueBgAlpha", "valueBorderAlpha", "valueBorderThickness", "valueBorderRadius", "valueBorderDashed", "valueBorderDashGap", "valueBorderDashLen", "valueHoverAlpha", "valueFontHoverAlpha", "valueBgHoverAlpha", "valueBorderHoverAlpha", "showValuesOnHover"]
                        },
                        {
                            id: "Data Label Cosmetics",
                            prop: ["labelFont", "labelFontColor", "labelFontSize", "labelFontBold", "labelFontItalic", "labelBgColor", "labelBorderColor", "labelAlpha", "labelBgAlpha", "labelBorderAlpha", "labelBorderPadding", "labelBorderRadius", "labelBorderThickness", "labelBorderDashed", "labelBorderDashLen", "labelBorderDashGap", "labelLink"]
                        },
                        {
                            id: "Data Plot Hover Effects",
                            prop: ["showHoverEffect", "plotHoverEffect", "plotFillHoverColor", "plotFillHoverAlpha"]
                        }
                    ]
                },
                {
                    id: "Others", group: true, sub: [
                        {
                            id: "Number Formatting",
                            prop: ["formatNumber", "formatNumberScale", "defaultNumberScale", "numberScaleUnit", "numberScaleValue", "scaleRecursively", "maxScaleRecursion", "scaleSeparator", "numberPrefix", "numberSuffix", "decimalSeparator", "thousandSeparator", "thousandSeparatorPosition", "inDecimalSeparator", "inThousandSeparator", "decimals", "forceDecimals", "forceYAxisValueDecimals", "yAxisValueDecimals"]
                        },
                        {
                            id: "Font Properties",
                            prop: ["baseFont", "baseFontSize", "baseFontColor", "outCnvBaseFont", "outCnvBaseFontSize", "outCnvBaseFontColor"]
                        }
                    ]
                }
            ],
            "set": ["label", "value", "displayValue", "color", "link", "toolText", "showLabel", "showValue", "dashed", "alpha", "labelFont", "labelFontColor", "labelFontSize", "labelFontBold", "labelFontItalic", "labelBgColor", "labelBorderColor", "labelAlpha", "labelBgAlpha", "labelBorderAlpha", "labelBorderPadding", "labelBorderRadius", "labelBorderThickness", "labelBorderDashed", "labelBorderDashLen", "labelBorderDashGap", "labelLink"],
            "Vertical data separator lines": ["linePosition", "color", "thickness", "showLabelBorder", "label", "labelPosition", "alpha", "dashed", "dashLen", "dashGap", "labelHAlign", "labelVAlign"],
            "Trend Lines": ["startValue", "endValue", "displayValue", "color", "isTrendZone", "showOnTop", "thickness", "alpha", "dashed", "dashLen", "dashGap", "valueOnRight", "toolText"]
        },
        Bar3D: {
            chart: [
                {
                    id: "Chart", group: true, sub: [
                        {id: "Titles (Axis) Names", prop: ["caption", "subCaption", "xAxisName", "yAxisName"]},
                        {
                            id: "Functional Attributes",
                            prop: ["animation", "palette", "paletteColors", "showLabels", "labelDisplay", "useEllipsesWhenOverflow", "rotateLabels", "slantLabels", "labelStep", "staggerLines", "showValues", "rotateValues", "placeValuesInside", "showYAxisValues", "showLimits", "showDivLineValues", "yAxisValuesStep", "showShadow", "adjustDiv", "rotateYAxisName", "yAxisNameWidth", "clickURL", "maxColWidth", "use3DLighting", "yAxisMinValue", "yAxisMaxValue", "setAdaptiveYMin"]
                        },
                        {
                            id: "Chart Caption Cosmetics",
                            prop: ["captionAlignment", "captionOnTop", "captionFontSize", "subCaptionFontSize", "captionFont", "subCaptionFont", "captionFontColor", "subCaptionFontColor", "captionFontBold", "subCaptionFontBold", "alignCaptionWithCanvas", "captionHorizontalPadding"]
                        },
                        {
                            id: "X-axis Name Cosmetics",
                            prop: ["xAxisNameFontColor", "xAxisNameFontSize", "xAxisNameFontBold", "xAxisNameFontItalic", "xAxisNameBgColor", "xAxisNameBorderColor", "xAxisNameAlpha", "xAxisNameFontAlpha", "xAxisNameBgAlpha", "xAxisNameBorderAlpha", "xAxisNameBorderPadding", "xAxisNameBorderRadius", "xAxisNameBorderThickness", "xAxisNameBorderDashed", "xAxisNameBorderDashLen", "xAxisNameBorderDashGap"]
                        },
                        {
                            id: "Y-axis Name Cosmetics",
                            prop: ["yAxisNameFont", "yAxisNameFontColor", "yAxisNameFontSize", "yAxisNameFontBold", "yAxisNameFontItalic", "yAxisNameBgColor", "yAxisNameBorderColor", "yAxisNameAlpha", "yAxisNameFontAlpha", "yAxisNameBgAlpha", "yAxisNameBorderAlpha", "yAxisNameBorderPadding", "yAxisNameBorderRadius", "yAxisNameBorderThickness", "yAxisNameBorderDashed", "yAxisNameBorderDashLen", "yAxisNameBorderDashGap"]
                        },
                        {
                            id: "Charts Cosmetics",
                            prop: ["bgColor", "bgAlpha", "bgRatio", "bgAngle", "bgImage", "bgImageAlpha", "bgImageDisplayMode", "bgImageVAlign", "bgImageHAlign", "bgImageScale", "canvasBgColor", "canvasBgAlpha", "canvasBaseColor", "showCanvasBg", "xAxisLineColor", "showCanvasBase", "canvasBaseDepth", "canvasBgDepth", "showBorder", "borderColor", "borderThickness", "borderAlpha", "showVLineLabelBorder", "logoURL", "logoPosition", "logoAlpha", "logoScale", "logoLink"]
                        },
                        {
                            id: "Divisional Lines/Grids",
                            prop: ["numDivLines", "divLineColor", "divLineThickness", "divLineAlpha", "divLineDashed", "divLineDashLen", "divLineDashGap", "zeroPlaneColor", "zeroPlaneAlpha", "zeroPlaneShowBorder", "showZeroPlaneValue", "zeroPlaneBorderColor"]
                        },
                        {
                            id: "Tool-tip",
                            prop: ["showToolTip", "toolTipColor", "toolTipBgColor", "toolTipBorderColor", "toolTipSepChar", "showToolTipShadow"]
                        },
                        {
                            id: "Paddings and Margins",
                            prop: ["captionPadding", "xAxisNamePadding", "yAxisNamePadding", "yAxisValuesPadding", "labelPadding", "valuePadding", "plotSpacePercent", "chartLeftMargin", "chartRightMargin", "chartTopMargin", "chartBottomMargin", "canvasLeftMargin", "canvasRightMargin", "canvasTopMargin", "canvasBottomMargin"]
                        },
                        {
                            id: "Data Plot Hover Effects",
                            prop: ["showHoverEffect", "plotHoverEffect", "plotFillHoverColor", "plotFillHoverAlpha"]
                        }
                    ]
                },
                {
                    id: "Data", group: true, sub: [
                        {
                            id: "Data Plot Cosmetics",
                            prop: ["showPlotBorder", "plotBorderColor", "plotBorderAlpha", "plotFillAlpha"]
                        },
                        {
                            id: "Data Value Cosmetics",
                            prop: ["valueFont", "valueFontColor", "valueFontSize", "valueFontBold", "valueFontItalic", "valueBgColor", "valueBorderColor", "valueAlpha", "valueFontAlpha", "valueBgAlpha", "valueBorderAlpha", "valueBorderThickness", "valueBorderRadius", "valueBorderDashed", "valueBorderDashGap", "valueBorderDashLen", "valueHoverAlpha", "valueFontHoverAlpha", "valueBgHoverAlpha", "valueBorderHoverAlpha", "showValuesOnHover"]
                        },
                        {
                            id: "Data Label Cosmetics",
                            prop: ["labelFont", "labelFontColor", "labelFontSize", "labelFontBold", "labelFontItalic", "labelBgColor", "labelBorderColor", "labelAlpha", "labelBgAlpha", "labelBorderAlpha", "labelBorderPadding", "labelBorderRadius", "labelBorderThickness", "labelBorderDashed", "labelBorderDashLen", "labelBorderDashGap", "labelLink"]
                        },
                        {
                            id: "Data Plot Hover Effects",
                            prop: ["showHoverEffect", "plotHoverEffect", "plotFillHoverColor", "plotFillHoverAlpha"]
                        }
                    ]
                },
                {
                    id: "Others", group: true, sub: [
                        {
                            id: "Number Formatting",
                            prop: ["formatNumber", "formatNumberScale", "defaultNumberScale", "numberScaleUnit", "numberScaleValue", "scaleRecursively", "maxScaleRecursion", "scaleSeparator", "numberPrefix", "numberSuffix", "decimalSeparator", "thousandSeparator", "thousandSeparatorPosition", "inDecimalSeparator", "inThousandSeparator", "decimals", "forceDecimals", "forceYAxisValueDecimals", "yAxisValueDecimals"]
                        },
                        {
                            id: "Font Properties",
                            prop: ["baseFont", "baseFontSize", "baseFontColor", "outCnvBaseFont", "outCnvBaseFontSize", "outCnvBaseFontColor"]
                        }
                    ]
                }
            ],
            "set": ["label", "value", "displayValue", "color", "link", "toolText", "showLabel", "showValue", "dashed", "alpha", "labelFont", "labelFontColor", "labelFontSize", "labelFontBold", "labelFontItalic", "labelBgColor", "labelBorderColor", "labelAlpha", "labelBgAlpha", "labelBorderAlpha", "labelBorderPadding", "labelBorderRadius", "labelBorderThickness", "labelBorderDashed", "labelBorderDashLen", "labelBorderDashGap", "labelLink"],
            "Vertical data separator lines": ["linePosition", "color", "thickness", "showLabelBorder", "label", "labelPosition", "alpha", "dashed", "dashLen", "dashGap", "labelHAlign", "labelVAlign"],
            "Trend Lines": ["startValue", "endValue", "displayValue", "color", "isTrendZone", "showOnTop", "thickness", "alpha", "dashed", "dashLen", "dashGap", "valueOnRight", "toolText"]
        },
        Area2D: {
            chart: [
                {
                    id: "Chart", group: true, sub: [
                        {id: "Titles (Axis) Names", prop: ["caption", "subCaption", "xAxisName", "yAxisName"]},
                        {
                            id: "Functional Attributes",
                            prop: ["animation", "palette", "paletteColors", "connectNullData", "showLabels", "maxLabelHeight", "labelDisplay", "useEllipsesWhenOverflow", "rotateLabels", "slantLabels", "labelStep", "staggerLines", "showValues", "rotateValues", "showYAxisValues", "showLimits", "showDivLineValues", "yAxisValuesStep", "adjustDiv", "rotateYAxisName", "yAxisNameWidth", "clickURL", "yAxisMinValue", "yAxisMaxValue", "setAdaptiveYMin"]
                        },
                        {
                            id: "Chart Caption Cosmetics",
                            prop: ["captionAlignment", "captionOnTop", "captionFontSize", "subCaptionFontSize", "captionFont", "subCaptionFont", "captionFontColor", "subCaptionFontColor", "captionFontBold", "subCaptionFontBold", "alignCaptionWithCanvas", "captionHorizontalPadding"]
                        },
                        {
                            id: "X-axis Name Cosmetics",
                            prop: ["xAxisNameFontColor", "xAxisNameFontSize", "xAxisNameFontBold", "xAxisNameFontItalic", "xAxisNameBgColor", "xAxisNameBorderColor", "xAxisNameAlpha", "xAxisNameFontAlpha", "xAxisNameBgAlpha", "xAxisNameBorderAlpha", "xAxisNameBorderPadding", "xAxisNameBorderRadius", "xAxisNameBorderThickness", "xAxisNameBorderDashed", "xAxisNameBorderDashLen", "xAxisNameBorderDashGap"]
                        },
                        {
                            id: "Y-axis Name Cosmetics",
                            prop: ["yAxisNameFont", "yAxisNameFontColor", "yAxisNameFontSize", "yAxisNameFontBold", "yAxisNameFontItalic", "yAxisNameBgColor", "yAxisNameBorderColor", "yAxisNameAlpha", "yAxisNameFontAlpha", "yAxisNameBgAlpha", "yAxisNameBorderAlpha", "yAxisNameBorderPadding", "yAxisNameBorderRadius", "yAxisNameBorderThickness", "yAxisNameBorderDashed", "yAxisNameBorderDashLen", "yAxisNameBorderDashGap"]
                        },
                        {
                            id: "Charts Cosmetics",
                            prop: ["bgColor", "bgAlpha", "bgRatio", "bgAngle", "bgImage", "bgImageAlpha", "bgImageDisplayMode", "bgImageVAlign", "bgImageHAlign", "bgImageScale", "canvasBgColor", "canvasBgAlpha", "canvasBgRatio", "canvasBgAngle", "xAxisLineColor", "canvasBorderColor", "canvasBorderThickness", "canvasBorderAlpha", "showBorder", "borderColor", "borderThickness", "borderAlpha", "showVLineLabelBorder", "logoURL", "logoPosition", "logoAlpha", "logoScale", "logoLink"]
                        },
                        {
                            id: "Anchors",
                            prop: ["drawAnchors", "anchorSides", "anchorRadius", "anchorBorderColor", "anchorBorderThickness", "anchorBgColor", "anchorAlpha", "anchorBgAlpha"]
                        },
                        {
                            id: "Divisional Lines/Grids",
                            prop: ["numDivLines", "divLineColor", "divLineThickness", "divLineAlpha", "divLineDashed", "divLineDashLen", "divLineDashGap", "zeroPlaneColor", "zeroPlaneThickness", "zeroPlaneAlpha", "showZeroPlaneValue", "showAlternateHGridColor", "alternateHGridColor", "alternateHGridAlpha", "numVDivLines", "vDivLineColor", "vDivLineThickness", "vDivLineAlpha", "vDivLineDashed", "vDivLineDashLen", "vDivLineDashGap", "showAlternateVGridColor", "alternateVGridColor", "alternateVGridAlpha"]
                        },
                        {
                            id: "Tool-tip",
                            prop: ["showToolTip", "toolTipColor", "toolTipBgColor", "toolTipBorderColor", "toolTipSepChar", "showToolTipShadow"]
                        },
                        {
                            id: "Paddings and Margins",
                            prop: ["captionPadding", "xAxisNamePadding", "yAxisNamePadding", "yAxisValuesPadding", "labelPadding", "valuePadding", "plotSpacePercent", "chartLeftMargin", "chartRightMargin", "chartTopMargin", "chartBottomMargin", "canvasLeftMargin", "canvasRightMargin", "canvasTopMargin", "canvasBottomMargin"]
                        },
                        {
                            id: "Data Plot Hover Effects",
                            prop: ["showHoverEffect", "plotHoverEffect", "plotFillHoverColor", "plotFillHoverAlpha"]
                        }
                    ]
                },
                {
                    id: "Data", group: true, sub: [
                        {
                            id: "Data Plot Cosmetics",
                            prop: ["showPlotBorder", "plotBorderColor", "plotBorderThickness", "plotBorderAlpha", "plotBorderDashed", "plotBorderDashLen", "plotBorderDashGap", "plotFillAngle", "plotFillRatio", "plotFillAlpha", "plotGradientColor", "showShadow", "plotFillColor"]
                        },
                        {
                            id: "Data Value Cosmetics",
                            prop: ["valueFont", "valueFontColor", "valueFontSize", "valueFontBold", "valueFontItalic", "valueBgColor", "valueBorderColor", "valueAlpha", "valueFontAlpha", "valueBgAlpha", "valueBorderAlpha", "valueBorderThickness", "valueBorderRadius", "valueBorderDashed", "valueBorderDashGap", "valueBorderDashLen", "valueHoverAlpha", "valueFontHoverAlpha", "valueBgHoverAlpha", "valueBorderHoverAlpha", "showValuesOnHover"]
                        },
                        {
                            id: "Data Label Cosmetics",
                            prop: ["labelFont", "labelFontColor", "labelFontSize", "labelFontBold", "labelFontItalic", "labelBgColor", "labelBorderColor", "labelAlpha", "labelBgAlpha", "labelBorderAlpha", "labelBorderPadding", "labelBorderRadius", "labelBorderThickness", "labelBorderDashed", "labelBorderDashLen", "labelBorderDashGap", "labelLink"]
                        },
                        {
                            id: "Data Plot Hover Effects",
                            prop: ["showHoverEffect", "plotHoverEffect", "plotFillHoverColor", "plotFillHoverAlpha"]
                        }
                    ]
                },
                {
                    id: "Others", group: true, sub: [
                        {
                            id: "Number Formatting",
                            prop: ["formatNumber", "formatNumberScale", "defaultNumberScale", "numberScaleUnit", "numberScaleValue", "scaleRecursively", "maxScaleRecursion", "scaleSeparator", "numberPrefix", "numberSuffix", "decimalSeparator", "thousandSeparator", "thousandSeparatorPosition", "inDecimalSeparator", "inThousandSeparator", "decimals", "forceDecimals", "forceYAxisValueDecimals", "yAxisValueDecimals"]
                        },
                        {
                            id: "Font Properties",
                            prop: ["baseFont", "baseFontSize", "baseFontColor", "outCnvBaseFont", "outCnvBaseFontSize", "outCnvBaseFontColor"]
                        }
                    ]
                }
            ],
            "set": ["label", "value", "displayValue", "link", "toolText", "showLabel", "showValue", "anchorSides", "anchorRadius", "anchorBorderColor", "anchorBorderThickness", "anchorBgColor", "anchorAlpha", "anchorBgAlpha", "labelFont", "labelFont", "labelFontColor", "labelFontSize", "labelFontBold", "labelFontItalic", "labelBgColor", "labelBorderColor", "labelAlpha", "labelBgAlpha", "labelBorderAlpha", "labelBorderPadding", "labelBorderRadius", "labelBorderThickness", "labelBorderDashed", "labelBorderDashLen", "labelBorderDashGap", "labelLink"],
            "Vertical data separator lines": ["linePosition", "color", "thickness", "showLabelBorder", "label", "labelPosition", "alpha", "dashed", "dashLen", "dashGap", "labelHAlign", "labelVAlign"],
            "Trend Lines": ["startValue", "endValue", "displayValue", "color", "isTrendZone", "showOnTop", "thickness", "alpha", "dashed", "dashLen", "dashGap", "valueOnRight", "toolText"]
        },
        Doughnut2D: {
            chart: [
                {
                    id: "Chart", group: true, sub: [
                        {id: "Titles (Axis) Names", prop: ["caption", "subCaption"]},
                        {
                            id: "Functional Attributes",
                            prop: ["animation", "palette", "paletteColors", "showZeroPies", "showPercentValues", "showPercentInToolTip", "showLabels", "showValues", "labelSepChar", "clickURL", "useDataPlotColorForLabels"]
                        },
                        {
                            id: "Chart Caption Cosmetics",
                            prop: ["captionAlignment", "captionOnTop", "captionFontSize", "subCaptionFontSize", "captionFont", "subCaptionFont", "captionFontColor", "subCaptionFontColor", "captionFontBold", "subCaptionFontBold", "alignCaptionWithCanvas", "captionHorizontalPadding"]
                        },
                        {
                            id: "Charts Cosmetics",
                            prop: ["showBorder", "borderColor", "borderThickness", "borderAlpha", "bgColor", "bgAlpha", "bgRatio", "bgAngle", "bgImage", "bgImageAlpha", "bgImageDisplayMode", "bgImageVAlign", "bgImageHAlign", "bgImageScale", "showVLineLabelBorder", "logoURL", "logoPosition", "logoAlpha", "logoScale", "logoLink"]
                        },
                        {
                            id: "Legend Properties",
                            prop: ["showLegend", "legendPosition", "legendCaption", "legendIconScale", "legendBgColor", "legendBgAlpha", "legendBorderColor", "legendBorderThickness", "legendBorderAlpha", "legendShadow", "legendAllowDrag", "legendScrollBgColor", "legendScrollBarColor", "legendScrollBtnColor", "reverseLegend", "interactiveLegend", "legendNumColumns", "minimiseWrappingInLegend"]
                        },
                        {
                            id: "Tool-tip",
                            prop: ["showToolTip", "toolTipColor", "toolTipBgColor", "toolTipBorderColor", "toolTipSepChar", "showToolTipShadow"]
                        },
                        {
                            id: "Paddings and Margins",
                            prop: ["captionPadding", "chartLeftMargin", "chartRightMargin", "chartTopMargin", "chartBottomMargin"]
                        }
                    ]
                },
                {
                    id: "Data", group: true, sub: [
                        {
                            id: "Pie / Doughnut Properties",
                            prop: ["radius3D", "slicingDistance", "pieRadius", "doughnutRadius", "startingAngle", "enableRotation"]
                        },
                        {
                            id: "Data Label Cosmetics",
                            prop: ["labelFont", "labelFontColor", "labelFontSize", "labelFontBold", "labelFontItalic", "labelBgColor", "labelBorderColor", "labelAlpha", "labelBgAlpha", "labelBorderAlpha", "labelBorderPadding", "labelBorderRadius", "labelBorderThickness", "labelBorderDashed", "labelBorderDashLen", "labelBorderDashGap", "labelLink"]
                        },
                        {
                            id: "Smart Labels & Lines",
                            prop: ["enableSmartLabels", "skipOverlapLabels", "isSmartLineSlanted", "smartLineColor", "smartLineThickness", "smartLineAlpha", "labelDistance", "smartLabelClearance", "manageLabelOverflow", "useEllipsesWhenOverflow"]
                        },
                        {
                            id: "Data Plot Cosmetics",
                            prop: ["showPlotBorder", "plotBorderColor", "plotBorderThickness", "plotBorderAlpha", "plotFillAlpha", "showShadow", "use3DLighting"]
                        },
                        {
                            id: "Data Plot Hover Effects",
                            prop: ["showHoverEffect", "plotHoverEffect", "plotFillHoverColor", "plotFillHoverAlpha"]
                        }
                    ]
                },
                {
                    id: "Others", group: true, sub: [
                        {
                            id: "Number Formatting",
                            prop: ["formatNumber", "formatNumberScale", "defaultNumberScale", "numberScaleUnit", "numberScaleValue", "scaleRecursively", "maxScaleRecursion", "scaleSeparator", "numberPrefix", "numberSuffix", "decimalSeparator", "thousandSeparator", "thousandSeparatorPosition", "inDecimalSeparator", "inThousandSeparator", "decimals", "forceDecimals"]
                        },
                        {id: "Font Properties", prop: ["baseFont", "baseFontSize", "baseFontColor"]}
                    ]
                }
            ],
            "set": ["label", "value", "displayValue", "color", "link", "toolText", "dashed", "alpha", "borderColor", "borderAlpha", "isSliced", "showLabel", "showValue", "labelFont", "labelFontColor", "labelFontSize", "labelFontBold", "labelFontItalic", "labelBgColor", "labelBorderColor", "labelAlpha", "labelBgAlpha", "labelBorderAlpha", "labelBorderPadding", "labelBorderRadius", "labelBorderThickness", "labelBorderDashed", "labelBorderDashLen", "labelBorderDashGap", "labelLink"]
        },
        Doughnut3D: {
            chart: [
                {
                    id: "Chart", group: true, sub: [
                        {id: "Titles (Axis) Names", prop: ["caption", "subCaption"]},
                        {
                            id: "Functional Attributes",
                            prop: ["animation", "palette", "paletteColors", "showZeroPies", "showPercentValues", "showPercentInToolTip", "showLabels", "showValues", "labelSepChar", "clickURL", "useDataPlotColorForLabels"]
                        },
                        {
                            id: "Chart Caption Cosmetics",
                            prop: ["captionAlignment", "captionOnTop", "captionFontSize", "subCaptionFontSize", "captionFont", "subCaptionFont", "captionFontColor", "subCaptionFontColor", "captionFontBold", "subCaptionFontBold", "alignCaptionWithCanvas", "captionHorizontalPadding"]
                        },
                        {
                            id: "Charts Cosmetics",
                            prop: ["showBorder", "borderColor", "borderThickness", "borderAlpha", "bgColor", "bgAlpha", "bgRatio", "bgAngle", "bgImage", "bgImageAlpha", "bgImageDisplayMode", "bgImageVAlign", "bgImageHAlign", "bgImageScale", "showVLineLabelBorder", "logoURL", "logoPosition", "logoAlpha", "logoScale", "logoLink"]
                        },
                        {
                            id: "Legend Properties",
                            prop: ["showLegend", "legendPosition", "legendCaption", "legendIconScale", "legendBgColor", "legendBgAlpha", "legendBorderColor", "legendBorderThickness", "legendBorderAlpha", "legendShadow", "legendAllowDrag", "legendScrollBgColor", "legendScrollBarColor", "legendScrollBtnColor", "reverseLegend", "interactiveLegend", "legendNumColumns", "minimiseWrappingInLegend"]
                        },
                        {
                            id: "Tool-tip",
                            prop: ["showToolTip", "toolTipColor", "toolTipBgColor", "toolTipBorderColor", "toolTipSepChar", "showToolTipShadow"]
                        },
                        {
                            id: "Paddings and Margins",
                            prop: ["captionPadding", "chartLeftMargin", "chartRightMargin", "chartTopMargin", "chartBottomMargin"]
                        }
                    ]
                },
                {
                    id: "Data", group: true, sub: [
                        {
                            id: "Pie / Doughnut Properties",
                            prop: ["slicingDistance", "pieRadius", "doughnutRadius", "startingAngle", "enableRotation"]
                        },
                        {
                            id: "Data Label Cosmetics",
                            prop: ["labelFont", "labelFontColor", "labelFontSize", "labelFontBold", "labelFontItalic", "labelBgColor", "labelBorderColor", "labelAlpha", "labelBgAlpha", "labelBorderAlpha", "labelBorderPadding", "labelBorderRadius", "labelBorderThickness", "labelBorderDashed", "labelBorderDashLen", "labelBorderDashGap", "labelLink"]
                        },
                        {
                            id: "Smart Labels & Lines",
                            prop: ["enableSmartLabels", "skipOverlapLabels", "isSmartLineSlanted", "smartLineColor", "smartLineThickness", "smartLineAlpha", "labelDistance", "smartLabelClearance", "manageLabelOverflow", "useEllipsesWhenOverflow"]
                        },
                        {
                            id: "Data Plot Cosmetics",
                            prop: ["showPlotBorder", "plotBorderColor", "plotBorderThickness", "plotBorderAlpha", "plotFillAlpha", "use3DLighting"]
                        },
                        {
                            id: "Data Plot Hover Effects",
                            prop: ["showHoverEffect", "plotHoverEffect", "plotFillHoverColor", "plotFillHoverAlpha"]
                        }
                    ]
                },
                {
                    id: "Others", group: true, sub: [
                        {
                            id: "Number Formatting",
                            prop: ["formatNumber", "formatNumberScale", "defaultNumberScale", "numberScaleUnit", "numberScaleValue", "scaleRecursively", "maxScaleRecursion", "scaleSeparator", "numberPrefix", "numberSuffix", "decimalSeparator", "thousandSeparator", "thousandSeparatorPosition", "inDecimalSeparator", "inThousandSeparator", "decimals", "forceDecimals"]
                        },
                        {id: "Font Properties", prop: ["baseFont", "baseFontSize", "baseFontColor"]}
                    ]
                }
            ],
            "set": ["label", "value", "displayValue", "color", "link", "toolText", "dashed", "alpha", "borderColor", "borderAlpha", "isSliced", "showLabel", "showValue", "labelFont", "labelFontColor", "labelFontSize", "labelFontBold", "labelFontItalic", "labelBgColor", "labelBorderColor", "labelAlpha", "labelBgAlpha", "labelBorderAlpha", "labelBorderPadding", "labelBorderRadius", "labelBorderThickness", "labelBorderDashed", "labelBorderDashLen", "labelBorderDashGap", "labelLink"]
        },
        Pareto2D: {
            chart: [
                {
                    id: "Chart", group: true, sub: [
                        {
                            id: "Titles (Axis) Names",
                            prop: ["caption", "subCaption", "xAxisName", "sYAxisName", "pYAxisName"]
                        },
                        {
                            id: "Functional Attributes",
                            prop: ["animation", "palette", "paletteColors", "showLabels", "maxLabelHeight", "labelDisplay", "useEllipsesWhenOverflow", "rotateLabels", "slantLabels", "labelStep", "staggerLines", "showValues", "valuePosition", "rotateValues", "placeValuesInside", "showYAxisValues", "showLimits", "showDivLineValues", "showSecondaryLimits", "showDivLineSecondaryValue", "yAxisValuesStep", "showShadow", "adjustDiv", "rotateYAxisName", "yAxisNameWidth", "clickURL", "pYAxisMaxValue", "pYAxisMinValue", "setAdaptiveYMin", "pYAxisNameWidth", "sYAxisNameWidth", "showCumulativeLine", "showLineValues", "useDataPlotColorForLabels"]
                        },
                        {
                            id: "Chart Caption Cosmetics",
                            prop: ["captionAlignment", "captionOnTop", "captionFontSize", "subCaptionFontSize", "captionFont", "subCaptionFont", "captionFontColor", "subCaptionFontColor", "captionFontBold", "subCaptionFontBold", "alignCaptionWithCanvas", "captionHorizontalPadding"]
                        },
                        {
                            id: "X-axis Name Cosmetics",
                            prop: ["xAxisNameFontColor", "xAxisNameFontSize", "xAxisNameFontBold", "xAxisNameFontItalic", "xAxisNameBgColor", "xAxisNameBorderColor", "xAxisNameAlpha", "xAxisNameFontAlpha", "xAxisNameBgAlpha", "xAxisNameBorderAlpha", "xAxisNameBorderPadding", "xAxisNameBorderRadius", "xAxisNameBorderThickness", "xAxisNameBorderDashed", "xAxisNameBorderDashLen", "xAxisNameBorderDashGap"]
                        },
                        {
                            id: "Primary Y-axis Name Cosmetics",
                            prop: ["pYAxisNameFont", "pYAxisNameFontColor", "pYAxisNameFontSize", "pYAxisNameFontBold", "pYAxisNameFontItalic", "pYAxisNameBgColor", "pYAxisNameBorderColor", "pYAxisNameAlpha", "pYAxisNameFontAlpha", "pYAxisNameBgAlpha", "pYAxisNameBorderAlpha", "pYAxisNameBorderPadding", "pYAxisNameBorderRadius", "pYAxisNameBorderThickness", "pYAxisNameBorderDashed", "pYAxisNameBorderDashLen", "pYAxisNameBorderDashGap"]
                        },
                        {
                            id: "Charts Cosmetics",
                            prop: ["showBorder", "borderColor", "borderThickness", "borderAlpha", "bgColor", "bgAlpha", "bgRatio", "bgAngle", "bgImage", "bgImageAlpha", "bgImageDisplayMode", "bgImageVAlign", "bgImageHAlign", "bgImageScale", "xAxisLineColor", "canvasBgColor", "canvasBgAlpha", "canvasBgRatio", "canvasBgAngle", "canvasBorderColor", "canvasBorderThickness", "canvasBorderAlpha", "showVLineLabelBorder", "logoURL", "logoPosition", "logoAlpha", "logoScale", "logoLink"]
                        },
                        {
                            id: "Anchors",
                            prop: ["drawAnchors", "anchorSides", "anchorRadius", "anchorBorderColor", "anchorBorderThickness", "anchorBgColor", "anchorAlpha", "anchorBgAlpha"]
                        },
                        {
                            id: "Divisional Lines/Grids",
                            prop: ["numDivLines", "divLineColor", "divLineThickness", "divLineAlpha", "divLineDashed", "divLineDashLen", "divLineDashGap", "showAlternateVGridColor", "alternateVGridColor", "alternateVGridAlpha"]
                        },
                        {
                            id: "Tool-tip",
                            prop: ["showToolTip", "toolTipColor", "toolTipBgColor", "toolTipBorderColor", "toolTipSepChar", "showToolTipShadow"]
                        },
                        {
                            id: "Paddings and Margins",
                            prop: ["captionPadding", "xAxisNamePadding", "yAxisNamePadding", "yAxisValuesPadding", "labelPadding", "valuePadding", "plotSpacePercent", "chartLeftMargin", "chartRightMargin", "chartTopMargin", "chartBottomMargin", "canvasLeftMargin", "canvasRightMargin", "canvasTopMargin", "canvasBottomMargin"]
                        },
                        {
                            id: "Data Plot Hover Effects",
                            prop: ["showHoverEffect", "plotHoverEffect", "plotFillHoverColor", "plotFillHoverAlpha"]
                        }
                    ]
                },
                {
                    id: "Data", group: true, sub: [
                        {
                            id: "Data Plot Cosmetics",
                            prop: ["useRoundEdges", "showPlotBorder", "plotBorderColor", "plotBorderThickness", "plotBorderAlpha", "plotBorderDashed", "plotBorderDashLen", "plotBorderDashGap", "plotFillAngle", "plotFillRatio", "plotFillAlpha", "plotGradientColor", "lineColor", "lineThickness", "lineAlpha", "lineDashed", "lineDashLen", "lineDashGap"]
                        },
                        {
                            id: "Data Value Cosmetics",
                            prop: ["valueFont", "valueFontColor", "valueFontSize", "valueFontBold", "valueFontItalic", "valueBgColor", "valueBorderColor", "valueAlpha", "valueFontAlpha", "valueBgAlpha", "valueBorderAlpha", "valueBorderThickness", "valueBorderRadius", "valueBorderDashed", "valueBorderDashGap", "valueBorderDashLen", "valueHoverAlpha", "valueFontHoverAlpha", "valueBgHoverAlpha", "valueBorderHoverAlpha", "showValuesOnHover"]
                        },
                        {
                            id: "Data Label Cosmetics",
                            prop: ["labelFont", "labelFontColor", "labelFontSize", "labelFontBold", "labelFontItalic", "labelBgColor", "labelBorderColor", "labelAlpha", "labelBgAlpha", "labelBorderAlpha", "labelBorderPadding", "labelBorderRadius", "labelBorderThickness", "labelBorderDashed", "labelBorderDashLen", "labelBorderDashGap", "labelLink"]
                        },
                        {
                            id: "Data Plot Hover Effects",
                            prop: ["showHoverEffect", "plotHoverEffect", "plotFillHoverColor", "plotFillHoverAlpha"]
                        }
                    ]
                },
                {
                    id: "Others", group: true, sub: [
                        {
                            id: "Number Formatting",
                            prop: ["formatNumber", "formatNumberScale", "defaultNumberScale", "numberScaleUnit", "numberScaleValue", "scaleRecursively", "maxScaleRecursion", "scaleSeparator", "numberPrefix", "numberSuffix", "decimalSeparator", "thousandSeparator", "thousandSeparatorPosition", "inDecimalSeparator", "inThousandSeparator", "decimals", "forceDecimals", "forceYAxisValueDecimals", "yAxisValueDecimals"]
                        },
                        {
                            id: "Font Properties",
                            prop: ["baseFont", "baseFontSize", "baseFontColor", "outCnvBaseFont", "outCnvBaseFontSize", "outCnvBaseFontColor"]
                        }
                    ]
                }
            ],
            "set": ["label", "value", "displayValue", "color", "link", "toolText", "showLabel", "showValue", "dashed", "alpha", "labelFont", "labelFontColor", "labelFontSize", "labelFontBold", "labelFontItalic", "labelBgColor", "labelBorderColor", "labelAlpha", "labelBgAlpha", "labelBorderAlpha", "labelBorderPadding", "labelBorderRadius", "labelBorderThickness", "labelBorderDashed", "labelBorderDashLen", "labelBorderDashGap", "labelLink"],
            "Vertical data separator lines": ["color", "thickness", "alpha", "dashed", "dashLen", "dashGap", "label", "showLabelBorder", "linePosition", "labelPosition", "labelHAlign", "labelVAlign"],
            "Trend Lines": ["startValue", "endValue", "displayValue", "color", "isTrendZone", "showOnTop", "thickness", "alpha", "dashed", "dashLen", "dashGap", "valueOnRight", "toolText"]
        },
        Pareto3D: {
            chart: [
                {
                    id: "Chart", group: true, sub: [
                        {
                            id: "Titles (Axis) Names",
                            prop: ["caption", "subCaption", "xAxisName", "sYAxisName", "pYAxisName"]
                        },
                        {
                            id: "Functional Attributes",
                            prop: ["animation", "palette", "paletteColors", "showLabels", "maxLabelHeight", "labelDisplay", "useEllipsesWhenOverflow", "rotateLabels", "slantLabels", "labelStep", "staggerLines", "showValues", "valuePosition", "rotateValues", "placeValuesInside", "showYAxisValues", "showLimits", "showDivLineValues", "showSecondaryLimits", "showDivLineSecondaryValue", "yAxisValuesStep", "showShadow", "adjustDiv", "rotateYAxisName", "yAxisNameWidth", "clickURL", "maxColWidth", "use3DLighting", "pYAxisMaxValue", "pYAxisMinValue", "setAdaptiveYMin", "pYAxisNameWidth", "sYAxisNameWidth", "showCumulativeLine", "showLineValues", "primaryAxisOnLeft", "use3DLineShift", "useDataPlotColorForLabels"]
                        },
                        {
                            id: "Chart Caption Cosmetics",
                            prop: ["captionAlignment", "captionOnTop", "captionFontSize", "subCaptionFontSize", "captionFont", "subCaptionFont", "captionFontColor", "subCaptionFontColor", "captionFontBold", "subCaptionFontBold", "alignCaptionWithCanvas", "captionHorizontalPadding"]
                        },
                        {
                            id: "X-axis Name Cosmetics",
                            prop: ["xAxisNameFontColor", "xAxisNameFontSize", "xAxisNameFontBold", "xAxisNameFontItalic", "xAxisNameBgColor", "xAxisNameBorderColor", "xAxisNameAlpha", "xAxisNameFontAlpha", "xAxisNameBgAlpha", "xAxisNameBorderAlpha", "xAxisNameBorderPadding", "xAxisNameBorderRadius", "xAxisNameBorderThickness", "xAxisNameBorderDashed", "xAxisNameBorderDashLen", "xAxisNameBorderDashGap"]
                        },
                        {
                            id: "Primary Y-axis Name Cosmetics",
                            prop: ["pYAxisNameFont", "pYAxisNameFontColor", "pYAxisNameFontSize", "pYAxisNameFontBold", "pYAxisNameFontItalic", "pYAxisNameBgColor", "pYAxisNameBorderColor", "pYAxisNameAlpha", "pYAxisNameFontAlpha", "pYAxisNameBgAlpha", "pYAxisNameBorderAlpha", "pYAxisNameBorderPadding", "pYAxisNameBorderRadius", "pYAxisNameBorderThickness", "pYAxisNameBorderDashed", "pYAxisNameBorderDashLen", "pYAxisNameBorderDashGap"]
                        },
                        {
                            id: "Charts Cosmetics",
                            prop: ["bgColor", "bgAlpha", "bgRatio", "bgAngle", "bgImage", "bgImageAlpha", "bgImageDisplayMode", "bgImageVAlign", "bgImageHAlign", "bgImageScale", "canvasBgColor", "canvasBgAlpha", "canvasBaseColor", "showCanvasBg", "xAxisLineColor", "showCanvasBase", "canvasBaseDepth", "canvasBgDepth", "showBorder", "borderColor", "borderThickness", "borderAlpha", "showVLineLabelBorder", "logoURL", "logoPosition", "logoAlpha", "logoScale", "logoLink"]
                        },
                        {
                            id: "Anchors",
                            prop: ["drawAnchors", "anchorSides", "anchorRadius", "anchorBorderColor", "anchorBorderThickness", "anchorBgColor", "anchorAlpha", "anchorBgAlpha"]
                        },
                        {
                            id: "Divisional Lines/Grids",
                            prop: ["numDivLines", "divLineColor", "divLineThickness", "divLineAlpha", "divLineDashed", "divLineDashLen", "divLineDashGap", "zeroPlaneShowBorder", "zeroPlaneBorderColor"]
                        },
                        {
                            id: "Tool-tip",
                            prop: ["showToolTip", "toolTipColor", "toolTipBgColor", "toolTipBorderColor", "toolTipSepChar", "showToolTipShadow"]
                        },
                        {
                            id: "Paddings and Margins",
                            prop: ["captionPadding", "xAxisNamePadding", "yAxisNamePadding", "yAxisValuesPadding", "labelPadding", "valuePadding", "plotSpacePercent", "chartLeftMargin", "chartRightMargin", "chartTopMargin", "chartBottomMargin", "canvasLeftMargin", "canvasRightMargin", "canvasTopMargin", "canvasBottomMargin"]
                        },
                        {
                            id: "Data Plot Hover Effects",
                            prop: ["showHoverEffect", "plotHoverEffect", "plotFillHoverColor", "plotFillHoverAlpha"]
                        }
                    ]
                },
                {
                    id: "Data", group: true, sub: [
                        {
                            id: "Data Plot Cosmetics",
                            prop: ["showPlotBorder", "plotBorderColor", "plotBorderAlpha", "plotFillAlpha", "lineColor", "lineThickness", "lineAlpha", "lineDashed", "lineDashLen", "lineDashGap", "overlapColumns"]
                        },
                        {
                            id: "Data Value Cosmetics",
                            prop: ["valueFont", "valueFontColor", "valueFontSize", "valueFontBold", "valueFontItalic", "valueBgColor", "valueBorderColor", "valueAlpha", "valueFontAlpha", "valueBgAlpha", "valueBorderAlpha", "valueBorderThickness", "valueBorderRadius", "valueBorderDashed", "valueBorderDashGap", "valueBorderDashLen", "valueHoverAlpha", "valueFontHoverAlpha", "valueBgHoverAlpha", "valueBorderHoverAlpha", "showValuesOnHover"]
                        },
                        {
                            id: "Data Label Cosmetics",
                            prop: ["labelFont", "labelFontColor", "labelFontSize", "labelFontBold", "labelFontItalic", "labelBgColor", "labelBorderColor", "labelAlpha", "labelBgAlpha", "labelBorderAlpha", "labelBorderPadding", "labelBorderRadius", "labelBorderThickness", "labelBorderDashed", "labelBorderDashLen", "labelBorderDashGap", "labelLink"]
                        },
                        {
                            id: "Data Plot Hover Effects",
                            prop: ["showHoverEffect", "plotHoverEffect", "plotFillHoverColor", "plotFillHoverAlpha"]
                        }
                    ]
                },
                {
                    id: "Others", group: true, sub: [
                        {
                            id: "Number Formatting",
                            prop: ["formatNumber", "formatNumberScale", "defaultNumberScale", "numberScaleUnit", "numberScaleValue", "scaleRecursively", "maxScaleRecursion", "scaleSeparator", "numberPrefix", "numberSuffix", "decimalSeparator", "thousandSeparator", "thousandSeparatorPosition", "inDecimalSeparator", "inThousandSeparator", "decimals", "forceDecimals", "forceYAxisValueDecimals", "yAxisValueDecimals"]
                        },
                        {
                            id: "Font Properties",
                            prop: ["baseFont", "baseFontSize", "baseFontColor", "outCnvBaseFont", "outCnvBaseFontSize", "outCnvBaseFontColor"]
                        }
                    ]
                }
            ],
            "set": ["label", "value", "displayValue", "color", "link", "toolText", "showLabel", "showValue", "dashed", "alpha", "labelFont", "labelFontColor", "labelFontSize", "labelFontBold", "labelFontItalic", "labelBgColor", "labelBorderColor", "labelAlpha", "labelBgAlpha", "labelBorderAlpha", "labelBorderPadding", "labelBorderRadius", "labelBorderThickness", "labelBorderDashed", "labelBorderDashLen", "labelBorderDashGap", "labelLink"],
            "Vertical data separator lines": ["color", "thickness", "alpha", "dashed", "dashLen", "dashGap", "label", "showLabelBorder", "linePosition", "labelPosition", "labelHAlign", "labelVAlign"],
            "Trend Lines": ["startValue", "endValue", "displayValue", "color", "isTrendZone", "showOnTop", "thickness", "alpha", "dashed", "dashLen", "dashGap", "valueOnRight", "toolText"]
        },
        MSColumn2D: {
            chart: [
                {
                    id: "Chart", group: true, sub: [
                        {id: "Titles (Axis) Names", prop: ["caption", "subCaption", "xAxisName", "yAxisName"]},
                        {
                            id: "Functional Attributes",
                            prop: ["animation", "palette", "paletteColors", "showLabels", "maxLabelHeight", "labelDisplay", "useEllipsesWhenOverflow", "rotateLabels", "slantLabels", "labelStep", "staggerLines", "showValues", "rotateValues", "placeValuesInside", "showYAxisValues", "showLimits", "showDivLineValues", "yAxisValuesStep", "showShadow", "adjustDiv", "rotateYAxisName", "yAxisNameWidth", "clickURL", "yAxisMinValue", "yAxisMaxValue", "setAdaptiveYMin"]
                        },
                        {
                            id: "Chart Caption Cosmetics",
                            prop: ["captionAlignment", "captionOnTop", "captionFontSize", "subCaptionFontSize", "captionFont", "subCaptionFont", "captionFontColor", "subCaptionFontColor", "captionFontBold", "subCaptionFontBold", "alignCaptionWithCanvas", "captionHorizontalPadding"]
                        },
                        {
                            id: "X-axis Name Cosmetics",
                            prop: ["xAxisNameFontColor", "xAxisNameFontSize", "xAxisNameFontBold", "xAxisNameFontItalic", "xAxisNameBgColor", "xAxisNameBorderColor", "xAxisNameAlpha", "xAxisNameFontAlpha", "xAxisNameBgAlpha", "xAxisNameBorderAlpha", "xAxisNameBorderPadding", "xAxisNameBorderRadius", "xAxisNameBorderThickness", "xAxisNameBorderDashed", "xAxisNameBorderDashLen", "xAxisNameBorderDashGap"]
                        },
                        {
                            id: "Y-axis Name Cosmetics",
                            prop: ["yAxisNameFont", "yAxisNameFontColor", "yAxisNameFontSize", "yAxisNameFontBold", "yAxisNameFontItalic", "yAxisNameBgColor", "yAxisNameBorderColor", "yAxisNameAlpha", "yAxisNameFontAlpha", "yAxisNameBgAlpha", "yAxisNameBorderAlpha", "yAxisNameBorderPadding", "yAxisNameBorderRadius", "yAxisNameBorderThickness", "yAxisNameBorderDashed", "yAxisNameBorderDashLen", "yAxisNameBorderDashGap"]
                        },
                        {
                            id: "Charts Cosmetics",
                            prop: ["showBorder", "borderColor", "borderThickness", "borderAlpha", "bgColor", "bgAlpha", "bgRatio", "bgAngle", "bgImage", "bgImageAlpha", "bgImageDisplayMode", "bgImageVAlign", "bgImageHAlign", "bgImageScale", "xAxisLineColor", "canvasBgColor", "canvasBgAlpha", "canvasBgRatio", "canvasBgAngle", "canvasBorderColor", "canvasBorderThickness", "canvasBorderAlpha", "showVLineLabelBorder", "logoURL", "logoPosition", "logoAlpha", "logoScale", "logoLink"]
                        },
                        {
                            id: "Legend Properties",
                            prop: ["showLegend", "legendItemFontBold", "legendItemFont", "legendItemFontSize", "legendItemFontColor", "legendItemHoverFontColor", "legendCaptionAlignment", "legendCaptionBold", "legendCaptionFont", "legendCaptionFontSize", "legendIconScale", "legendItemHiddenColor", "legendPosition", "legendBgColor", "legendBgAlpha", "legendBorderColor", "legendBorderThickness", "legendBorderAlpha", "legendShadow", "legendAllowDrag", "legendScrollBgColor", "reverseLegend", "interactiveLegend", "legendNumColumns", "minimiseWrappingInLegend"]
                        },
                        {
                            id: "Divisional Lines/Grids",
                            prop: ["numDivLines", "divLineColor", "divLineThickness", "divLineAlpha", "divLineIsDashed", "divLineDashLen", "divLineDashGap", "zeroPlaneColor", "zeroPlaneThickness", "zeroPlaneAlpha", "showZeroPlaneValue", "showAlternateHGridColor", "alternateHGridColor", "alternateHGridAlpha"]
                        },
                        {
                            id: "Tool-tip",
                            prop: ["showToolTip", "toolTipColor", "toolTipBgColor", "toolTipBorderColor", "toolTipSepChar", "seriesNameInToolTip", "showToolTipShadow"]
                        },
                        {
                            id: "Paddings and Margins",
                            prop: ["captionPadding", "xAxisNamePadding", "yAxisNamePadding", "yAxisValuesPadding", "labelPadding", "valuePadding", "plotSpacePercent", "chartLeftMargin", "chartRightMargin", "chartTopMargin", "chartBottomMargin", "legendPadding", "canvasLeftMargin", "canvasRightMargin", "canvasTopMargin", "canvasBottomMargin"]
                        }
                    ]
                },
                {
                    id: "Data", group: true, sub: [
                        {
                            id: "Data Plot Cosmetics",
                            prop: ["useRoundEdges", "showPlotBorder", "plotBorderColor", "plotBorderThickness", "plotBorderAlpha", "plotBorderDashed", "plotBorderDashLen", "plotBorderDashGap", "plotFillAngle", "plotFillRatio", "plotFillAlpha", "plotGradientColor"]
                        },
                        {
                            id: "Data Value Cosmetics",
                            prop: ["valueFont", "valueFontColor", "valueFontSize", "valueFontBold", "valueFontItalic", "valueBgColor", "valueBorderColor", "valueAlpha", "valueFontAlpha", "valueBgAlpha", "valueBorderAlpha", "valueBorderThickness", "valueBorderRadius", "valueBorderDashed", "valueBorderDashGap", "valueBorderDashLen", "valueHoverAlpha", "valueFontHoverAlpha", "valueBgHoverAlpha", "valueBorderHoverAlpha", "showValuesOnHover"]
                        },
                        {
                            id: "Data Label Cosmetics",
                            prop: ["labelFont", "labelFontColor", "labelFontSize", "labelFontBold", "labelFontItalic", "labelBgColor", "labelBorderColor", "labelAlpha", "labelBgAlpha", "labelBorderAlpha", "labelBorderPadding", "labelBorderRadius", "labelBorderThickness", "labelBorderDashed", "labelBorderDashLen", "labelBorderDashGap", "labelLink"]
                        },
                        {
                            id: "Data Plot Hover Effects",
                            prop: ["showHoverEffect", "plotHoverEffect", "plotFillHoverColor", "plotFillHoverAlpha"]
                        }
                    ]
                },
                {
                    id: "Others", group: true, sub: [
                        {
                            id: "Number Formatting",
                            prop: ["formatNumber", "formatNumberScale", "defaultNumberScale", "numberScaleUnit", "numberScaleValue", "scaleRecursively", "maxScaleRecursion", "scaleSeparator", "numberPrefix", "numberSuffix", "decimalSeparator", "thousandSeparator", "thousandSeparatorPosition", "inDecimalSeparator", "inThousandSeparator", "decimals", "forceDecimals", "forceYAxisValueDecimals", "yAxisValueDecimals"]
                        },
                        {
                            id: "Font Properties",
                            prop: ["baseFont", "baseFontSize", "baseFontColor", "outCnvBaseFont", "outCnvBaseFontSize", "outCnvBaseFontColor"]
                        }
                    ]
                }
            ],
            "categories": ["font", "fontSize", "fontColor"],
            "category": ["label", "showLabel", "toolText", "font", "fontColor", "fontSize", "fontBold", "fontItalic", "bgColor", "borderColor", "alpha", "bgAlpha", "borderAlpha", "borderPadding", "borderRadius", "borderThickness", "borderDashed", "borderDashLen", "borderDashGap", "link"],
            "dataset": ["seriesName", "color", "alpha", "ratio", "showValues", "dashed", "includeInLegend"],
            "set": ["label", "value", "displayValue", "color", "link", "toolText", "showLabel", "showValue", "dashed", "alpha"],
            "Vertical data separator lines": ["linePosition", "color", "thickness", "showLabelBorder", "label", "labelPosition", "alpha", "dashed", "dashLen", "dashGap", "labelHAlign", "labelVAlign"],
            "Trend Lines": ["startValue", "endValue", "displayValue", "color", "isTrendZone", "showOnTop", "thickness", "alpha", "dashed", "dashLen", "dashGap", "valueOnRight", "toolText"]
        },
        MSColumn3D: {
            chart: [
                {
                    id: "Chart", group: true, sub: [
                        {id: "Titles (Axis) Names", prop: ["caption", "subCaption", "xAxisName", "yAxisName"]},
                        {
                            id: "Functional Attributes",
                            prop: ["animation", "palette", "paletteColors", "showLabels", "maxLabelHeight", "labelDisplay", "useEllipsesWhenOverflow", "rotateLabels", "slantLabels", "labelStep", "staggerLines", "showValues", "rotateValues", "placeValuesInside", "showYAxisValues", "showLimits", "showDivLineValues", "yAxisValuesStep", "showShadow", "adjustDiv", "rotateYAxisName", "yAxisNameWidth", "clickURL", "maxColWidth", "use3DLighting", "yAxisMinValue", "yAxisMaxValue", "setAdaptiveYMin"]
                        },
                        {
                            id: "Chart Caption Cosmetics",
                            prop: ["captionAlignment", "captionOnTop", "captionFontSize", "subCaptionFontSize", "captionFont", "subCaptionFont", "captionFontColor", "subCaptionFontColor", "captionFontBold", "subCaptionFontBold", "alignCaptionWithCanvas", "captionHorizontalPadding"]
                        },
                        {
                            id: "X-axis Name Cosmetics",
                            prop: ["xAxisNameFontColor", "xAxisNameFontSize", "xAxisNameFontBold", "xAxisNameFontItalic", "xAxisNameBgColor", "xAxisNameBorderColor", "xAxisNameAlpha", "xAxisNameFontAlpha", "xAxisNameBgAlpha", "xAxisNameBorderAlpha", "xAxisNameBorderPadding", "xAxisNameBorderRadius", "xAxisNameBorderThickness", "xAxisNameBorderDashed", "xAxisNameBorderDashLen", "xAxisNameBorderDashGap"]
                        },
                        {
                            id: "Y-axis Name Cosmetics",
                            prop: ["yAxisNameFont", "yAxisNameFontColor", "yAxisNameFontSize", "yAxisNameFontBold", "yAxisNameFontItalic", "yAxisNameBgColor", "yAxisNameBorderColor", "yAxisNameAlpha", "yAxisNameFontAlpha", "yAxisNameBgAlpha", "yAxisNameBorderAlpha", "yAxisNameBorderPadding", "yAxisNameBorderRadius", "yAxisNameBorderThickness", "yAxisNameBorderDashed", "yAxisNameBorderDashLen", "yAxisNameBorderDashGap"]
                        },
                        {
                            id: "Charts Cosmetics",
                            prop: ["showBorder", "borderColor", "borderThickness", "borderAlpha", "bgColor", "bgAlpha", "bgRatio", "bgAngle", "bgImage", "bgImageAlpha", "bgImageDisplayMode", "bgImageVAlign", "bgImageHAlign", "bgImageScale", "xAxisLineColor", "canvasBgColor", "canvasBgAlpha", "canvasBgRatio", "canvasBgAngle", "canvasBorderColor", "canvasBorderThickness", "canvasBorderAlpha", "showVLineLabelBorder", "logoURL", "logoPosition", "logoAlpha", "logoScale", "logoLink"]
                        },
                        {
                            id: "Legend Properties",
                            prop: ["showLegend", "legendItemFontBold", "legendItemFont", "legendItemFontSize", "legendItemFontColor", "legendItemHoverFontColor", "legendCaptionAlignment", "legendCaptionBold", "legendCaptionFont", "legendCaptionFontSize", "legendIconScale", "legendItemHiddenColor", "legendPosition", "legendBgColor", "legendBgAlpha", "legendBorderColor", "legendBorderThickness", "legendBorderAlpha", "legendShadow", "legendAllowDrag", "legendScrollBgColor", "reverseLegend", "interactiveLegend", "legendNumColumns", "minimiseWrappingInLegend"]
                        },
                        {
                            id: "Divisional Lines/Grids",
                            prop: ["numDivLines", "divLineColor", "divLineThickness", "divLineAlpha", "divLineDashed", "divLineDashLen", "divLineDashGap", "zeroPlaneColor", "zeroPlaneAlpha", "zeroPlaneShowBorder", "showZeroPlaneValue", "zeroPlaneBorderColor"]
                        },
                        {
                            id: "Tool-tip",
                            prop: ["showToolTip", "toolTipColor", "toolTipBgColor", "toolTipBorderColor", "toolTipSepChar", "seriesNameInToolTip", "showToolTipShadow"]
                        },
                        {
                            id: "Paddings and Margins",
                            prop: ["captionPadding", "xAxisNamePadding", "yAxisNamePadding", "yAxisValuesPadding", "labelPadding", "valuePadding", "plotSpacePercent", "chartLeftMargin", "chartRightMargin", "chartTopMargin", "chartBottomMargin", "legendPadding", "canvasLeftMargin", "canvasRightMargin", "canvasTopMargin", "canvasBottomMargin"]
                        }
                    ]
                },
                {
                    id: "Data", group: true, sub: [
                        {
                            id: "Data Plot Cosmetics",
                            prop: ["showPlotBorder", "plotBorderColor", "plotBorderAlpha", "plotFillAlpha", "overlapColumns"]
                        },
                        {
                            id: "Data Value Cosmetics",
                            prop: ["valueFont", "valueFontColor", "valueFontSize", "valueFontBold", "valueFontItalic", "valueBgColor", "valueBorderColor", "valueAlpha", "valueFontAlpha", "valueBgAlpha", "valueBorderAlpha", "valueBorderThickness", "valueBorderRadius", "valueBorderDashed", "valueBorderDashGap", "valueBorderDashLen", "valueHoverAlpha", "valueFontHoverAlpha", "valueBgHoverAlpha", "valueBorderHoverAlpha", "showValuesOnHover"]
                        },
                        {
                            id: "Data Label Cosmetics",
                            prop: ["labelFont", "labelFontColor", "labelFontSize", "labelFontBold", "labelFontItalic", "labelBgColor", "labelBorderColor", "labelAlpha", "labelBgAlpha", "labelBorderAlpha", "labelBorderPadding", "labelBorderRadius", "labelBorderThickness", "labelBorderDashed", "labelBorderDashLen", "labelBorderDashGap", "labelLink"]
                        },
                        {
                            id: "Data Plot Hover Effects",
                            prop: ["showHoverEffect", "plotHoverEffect", "plotFillHoverColor", "plotFillHoverAlpha"]
                        }
                    ]
                },
                {
                    id: "Others", group: true, sub: [
                        {
                            id: "Number Formatting",
                            prop: ["formatNumber", "formatNumberScale", "defaultNumberScale", "numberScaleUnit", "numberScaleValue", "scaleRecursively", "maxScaleRecursion", "scaleSeparator", "numberPrefix", "numberSuffix", "decimalSeparator", "thousandSeparator", "thousandSeparatorPosition", "inDecimalSeparator", "inThousandSeparator", "decimals", "forceDecimals", "forceYAxisValueDecimals", "yAxisValueDecimals"]
                        },
                        {
                            id: "Font Properties",
                            prop: ["baseFont", "baseFontSize", "baseFontColor", "outCnvBaseFont", "outCnvBaseFontSize", "outCnvBaseFontColor"]
                        }
                    ]
                }
            ],
            "categories": ["font", "fontSize", "fontColor"],
            "category": ["label", "showLabel", "toolText", "font", "fontColor", "fontSize", "fontBold", "fontItalic", "bgColor", "borderColor", "alpha", "bgAlpha", "borderAlpha", "borderPadding", "borderRadius", "borderThickness", "borderDashed", "borderDashLen", "borderDashGap", "link"],
            "dataset": ["seriesName", "color", "alpha", "ratio", "showValues", "dashed", "includeInLegend"],
            "set": ["label", "value", "displayValue", "color", "link", "toolText", "showLabel", "showValue", "alpha"],
            "Vertical data separator lines": ["linePosition", "color", "thickness", "showLabelBorder", "label", "labelPosition", "alpha", "dashed", "dashLen", "dashGap", "labelHAlign", "labelVAlign"],
            "Trend Lines": ["startValue", "endValue", "displayValue", "color", "isTrendZone", "thickness", "alpha", "dashed", "dashLen", "dashGap", "valueOnRight", "toolText"]
        },
        MSLine: {
            chart: [
                {
                    id: "Chart", group: true, sub: [
                        {id: "Titles (Axis) Names", prop: ["caption", "subCaption", "xAxisName", "yAxisName"]},
                        {
                            id: "Functional Attributes",
                            prop: ["animation", "palette", "paletteColors", "connectNullData", "showLabels", "maxLabelHeight", "labelDisplay", "useEllipsesWhenOverflow", "rotateLabels", "slantLabels", "labelStep", "staggerLines", "showValues", "valuePosition", "rotateValues", "showYAxisValues", "showLimits", "showDivLineValues", "yAxisValuesStep", "showShadow", "adjustDiv", "rotateYAxisName", "yAxisNameWidth", "clickURL", "yAxisMinValue", "yAxisMaxValue", "setAdaptiveYMin"]
                        },
                        {
                            id: "Chart Caption Cosmetics",
                            prop: ["captionAlignment", "captionOnTop", "captionFontSize", "subCaptionFontSize", "captionFont", "subCaptionFont", "captionFontColor", "subCaptionFontColor", "captionFontBold", "subCaptionFontBold", "alignCaptionWithCanvas", "captionHorizontalPadding"]
                        },
                        {
                            id: "X-axis Name Cosmetics",
                            prop: ["xAxisNameFontColor", "xAxisNameFontSize", "xAxisNameFontBold", "xAxisNameFontItalic", "xAxisNameBgColor", "xAxisNameBorderColor", "xAxisNameAlpha", "xAxisNameFontAlpha", "xAxisNameBgAlpha", "xAxisNameBorderAlpha", "xAxisNameBorderPadding", "xAxisNameBorderRadius", "xAxisNameBorderThickness", "xAxisNameBorderDashed", "xAxisNameBorderDashLen", "xAxisNameBorderDashGap"]
                        },
                        {
                            id: "Y-axis Name Cosmetics",
                            prop: ["yAxisNameFont", "yAxisNameFontColor", "yAxisNameFontSize", "yAxisNameFontBold", "yAxisNameFontItalic", "yAxisNameBgColor", "yAxisNameBorderColor", "yAxisNameAlpha", "yAxisNameFontAlpha", "yAxisNameBgAlpha", "yAxisNameBorderAlpha", "yAxisNameBorderPadding", "yAxisNameBorderRadius", "yAxisNameBorderThickness", "yAxisNameBorderDashed", "yAxisNameBorderDashLen", "yAxisNameBorderDashGap"]
                        },
                        {
                            id: "Charts Cosmetics",
                            prop: ["showBorder", "borderColor", "borderThickness", "borderAlpha", "bgColor", "bgAlpha", "bgRatio", "bgAngle", "bgImage", "bgImageAlpha", "bgImageDisplayMode", "bgImageVAlign", "bgImageHAlign", "bgImageScale", "xAxisLineColor", "canvasBgColor", "canvasBgAlpha", "canvasBgRatio", "canvasBgAngle", "canvasBorderColor", "canvasBorderThickness", "canvasBorderAlpha", "showVLineLabelBorder", "logoURL", "logoPosition", "logoAlpha", "logoScale", "logoLink"]
                        },
                        {
                            id: "Anchors",
                            prop: ["drawAnchors", "anchorSides", "anchorRadius", "anchorBorderColor", "anchorBorderThickness", "anchorBgColor", "anchorAlpha", "anchorBgAlpha"]
                        },
                        {
                            id: "Legend Properties",
                            prop: ["showLegend", "legendItemFontBold", "legendItemFont", "legendItemFontSize", "legendItemFontColor", "legendItemHoverFontColor", "legendCaptionAlignment", "legendCaptionBold", "legendCaptionFont", "legendCaptionFontSize", "legendIconScale", "legendItemHiddenColor", "legendPosition", "legendBgColor", "legendBgAlpha", "legendBorderColor", "legendBorderThickness", "legendBorderAlpha", "legendShadow", "legendAllowDrag", "legendScrollBgColor", "reverseLegend", "interactiveLegend", "legendNumColumns", "minimiseWrappingInLegend"]
                        },
                        {
                            id: "Divisional Lines/Grids",
                            prop: ["numVDivLines", "vDivLineColor", "vDivLineThickness", "vDivLineAlpha", "vDivLineDashed", "vDivLineDashLen", "vDivLineDashGap", "showAlternateVGridColor", "alternateVGridColor", "alternateVGridAlpha", "numDivLines", "divLineColor", "divLineThickness", "divLineAlpha", "divLineDashed", "divLineDashLen", "divLineDashGap", "zeroPlaneColor", "zeroPlaneThickness", "zeroPlaneAlpha", "showZeroPlaneValue", "showAlternateHGridColor", "alternateHGridColor", "alternateHGridAlpha", "showZeroPlane"]
                        },
                        {
                            id: "Tool-tip",
                            prop: ["showToolTip", "toolTipColor", "toolTipBgColor", "toolTipBorderColor", "toolTipSepChar", "seriesNameInToolTip", "showToolTipShadow"]
                        },
                        {
                            id: "Paddings and Margins",
                            prop: ["captionPadding", "xAxisNamePadding", "yAxisNamePadding", "yAxisValuesPadding", "labelPadding", "valuePadding", "plotSpacePercent", "chartLeftMargin", "chartRightMargin", "chartTopMargin", "chartBottomMargin", "canvasLeftMargin", "canvasRightMargin", "canvasTopMargin", "canvasBottomMargin"]
                        }
                    ]
                },
                {
                    id: "Data", group: true, sub: [
                        {
                            id: "Data Plot Cosmetics",
                            prop: ["lineColor", "lineThickness", "lineAlpha", "lineDashed", "lineDashLen", "lineDashGap"]
                        },
                        {
                            id: "Data Value Cosmetics",
                            prop: ["valueFont", "valueFontColor", "valueFontSize", "valueFontBold", "valueFontItalic", "valueBgColor", "valueBorderColor", "valueAlpha", "valueFontAlpha", "valueBgAlpha", "valueBorderAlpha", "valueBorderThickness", "valueBorderRadius", "valueBorderDashed", "valueBorderDashGap", "valueBorderDashLen", "valueHoverAlpha", "valueFontHoverAlpha", "valueBgHoverAlpha", "valueBorderHoverAlpha", "showValuesOnHover"]
                        },
                        {
                            id: "Data Label Cosmetics",
                            prop: ["labelFont", "labelFontColor", "labelFontSize", "labelFontBold", "labelFontItalic", "labelBgColor", "labelBorderColor", "labelAlpha", "labelBgAlpha", "labelBorderAlpha", "labelBorderPadding", "labelBorderRadius", "labelBorderThickness", "labelBorderDashed", "labelBorderDashLen", "labelBorderDashGap", "labelLink"]
                        },
                        {
                            id: "Data Plot Hover Effects",
                            prop: ["showHoverEffect", "plotHoverEffect", "plotFillHoverColor", "plotFillHoverAlpha"]
                        }
                    ]
                },
                {
                    id: "Others", group: true, sub: [
                        {
                            id: "Number Formatting",
                            prop: ["formatNumber", "formatNumberScale", "defaultNumberScale", "numberScaleUnit", "numberScaleValue", "scaleRecursively", "maxScaleRecursion", "scaleSeparator", "numberPrefix", "numberSuffix", "decimalSeparator", "thousandSeparator", "thousandSeparatorPosition", "inDecimalSeparator", "inThousandSeparator", "decimals", "forceDecimals", "forceYAxisValueDecimals", "yAxisValueDecimals"]
                        },
                        {
                            id: "Font Properties",
                            prop: ["baseFont", "baseFontSize", "baseFontColor", "outCnvBaseFont", "outCnvBaseFontSize", "outCnvBaseFontColor"]
                        }
                    ]
                }
            ],
            "categories": ["font", "fontSize", "fontColor"],
            "category": ["label", "showLabel", "toolText", "font", "fontColor", "fontSize", "fontBold", "fontItalic", "bgColor", "borderColor", "alpha", "bgAlpha", "borderAlpha", "borderPadding", "borderRadius", "borderThickness", "borderDashed", "borderDashLen", "borderDashGap", "link"],
            "dataset": ["includeInLegend", "seriesName", "color", "alpha", "showValues", "valuePosition", "dashed", "drawAnchors", "anchorSides", "anchorRadius", "anchorBorderColor", "anchorBorderThickness", "anchorBgColor", "anchorAlpha", "anchorBgAlpha", "lineThickness", "lineDashLen", "lineDashGap"],
            "set": ["value", "displayValue", "color", "link", "toolText", "showValue", "valuePosition", "alpha", "anchorSides", "anchorRadius", "anchorBorderColor", "anchorBorderThickness", "anchorBgColor", "anchorAlpha", "anchorBgAlpha", "dashed"],
            "Vertical data separator lines": ["linePosition", "color", "thickness", "showLabelBorder", "label", "labelPosition", "alpha", "dashed", "dashLen", "dashGap", "labelHAlign", "labelVAlign"],
            "Trend Lines": ["startValue", "endValue", "displayValue", "color", "isTrendZone", "showOnTop", "thickness", "alpha", "dashed", "dashLen", "dashGap", "valueOnRight", "toolText"]
        },
        ZoomLine: {
            chart: [
                {
                    id: "Chart", group: true, sub: [
                        {id: "Titles (Axis) Names", prop: ["caption", "subCaption", "xAxisName", "yAxisName"]},
                        {
                            id: "Functional Attributes",
                            prop: ["palette", "paletteColors", "connectNullData", "showLabels", "maxLabelHeight", "labelDisplay", "useEllipsesWhenOverflow", "rotateLabels", "showValues", "valuePosition", "rotateValues", "yAxisMaxValue", "yAxisMinValue", "yAxisNameWidth", "showShadow", "rotateYAxisName", "setAdaptiveYMin", "compactDataMode", "dataSeparator", "numMinorLogDivLines", "allowPinMode", "numVisibleLabels", "anchorMinRenderDistance", "displayStartIndex", "displayEndIndex", "pixelsPerPoint"]
                        },
                        {
                            id: "Chart Caption Cosmetics",
                            prop: ["captionAlignment", "captionOnTop", "captionFontSize", "subCaptionFontSize", "captionFont", "subCaptionFont", "captionFontColor", "subCaptionFontColor", "captionFontBold", "subCaptionFontBold", "alignCaptionWithCanvas", "captionHorizontalPadding"]
                        },
                        {
                            id: "X-axis Name Cosmetics",
                            prop: ["xAxisNameFontColor", "xAxisNameFontSize", "xAxisNameFontBold", "xAxisNameFontItalic", "xAxisNameBgColor", "xAxisNameBorderColor", "xAxisNameAlpha", "xAxisNameFontAlpha", "xAxisNameBgAlpha", "xAxisNameBorderAlpha", "xAxisNameBorderPadding", "xAxisNameBorderRadius", "xAxisNameBorderThickness", "xAxisNameBorderDashed", "xAxisNameBorderDashLen", "xAxisNameBorderDashGap"]
                        },
                        {
                            id: "Y-axis Name Cosmetics",
                            prop: ["yAxisNameFont", "yAxisNameFontColor", "yAxisNameFontSize", "yAxisNameFontBold", "yAxisNameFontItalic", "yAxisNameBgColor", "yAxisNameBorderColor", "yAxisNameAlpha", "yAxisNameFontAlpha", "yAxisNameBgAlpha", "yAxisNameBorderAlpha", "yAxisNameBorderPadding", "yAxisNameBorderRadius", "yAxisNameBorderThickness", "yAxisNameBorderDashed", "yAxisNameBorderDashLen", "yAxisNameBorderDashGap"]
                        },
                        {
                            id: "Charts Cosmetics",
                            prop: ["showBorder", "borderColor", "borderThickness", "borderAlpha", "bgColor", "bgAlpha", "bgRatio", "bgAngle", "bgImage", "bgImageAlpha", "bgImageDisplayMode", "bgImageVAlign", "bgImageHAlign", "bgImageScale", "xAxisLineColor", "canvasBgColor", "canvasBgAlpha", "canvasBgRatio", "canvasBgAngle", "canvasBorderColor", "canvasBorderThickness", "canvasBorderAlpha", "showVLineLabelBorder", "logoURL", "logoPosition", "logoAlpha", "logoScale", "logoLink", "paletteThemeColor", "toolbarButtonColor", "zoomPaneBorderColor", "zoomPaneBgColor", "zoomPaneBgAlpha", "pinLineThicknessDelta", "pinPaneBgColor", "pinPaneBgAlpha", "btnZoomOutTitle", "btnSwitchtoZoomModeTitle", "btnSwitchToPinModeTitle", "showToolBarButtonTooltext", "btnResetChartTooltext", "btnSwitchToPinModeTooltext", "resetChartMenuItemLabel", "zoomModeMenuItemLabel", "pinModeMenuItemLabel", "toolBarBtnTextHMargin", "toolBarBtnHPadding", "toolBarBtnVPadding"]
                        },
                        {id: "Scroll Properties", prop: ["scrollColor", "scrollHeight"]},
                        {
                            id: "Anchors",
                            prop: ["drawAnchors", "anchorRadius", "anchorBorderColor", "anchorBorderThickness", "anchorBgColor", "anchorBgAlpha", "anchorMinRenderDistance"]
                        },
                        {
                            id: "Legend Properties",
                            prop: ["showLegend", "legendItemFontBold", "legendItemFont", "legendItemFontSize", "legendItemFontColor", "legendPosition", "legendCaptionAlignment", "legendCaptionBold", "legendCaptionFont", "legendCaptionFontSize", "legendCaptionFontColor", "legendItemHiddenColor", "legendIconScale", "legendBgColor", "legendBgAlpha", "legendBorderColor", "legendBorderThickness", "legendBorderAlpha", "legendShadow", "legendAllowDrag", "legendScrollBgColor", "reverseLegend", "interactiveLegend", "legendNumColumns", "minimiseWrappingInLegend"]
                        },
                        {
                            id: "Divisional Lines/Grids",
                            prop: ["divLineColor", "divLineThickness", "divLineAlpha", "divLineDashed", "divLineDashLen", "divLineDashGap", "zeroPlaneColor", "zeroPlaneThickness", "zeroPlaneAlpha", "showZeroPlaneValue", "showAlternateHGridColor", "alternateHGridColor", "alternateHGridAlpha"]
                        },
                        {
                            id: "Tool-tip",
                            prop: ["showToolTip", "toolTipColor", "toolTipBgColor", "toolTipBorderColor", "toolTipSepChar", "useCrossLine"]
                        },
                        {
                            id: "Paddings and Margins",
                            prop: ["captionPadding", "xAxisNamePadding", "yAxisNamePadding", "yAxisValuesPadding", "labelPadding", "valuePadding", "plotSpacePercent", "chartLeftMargin", "chartRightMargin", "chartTopMargin", "chartBottomMargin", "canvasLeftMargin", "canvasRightMargin", "canvasTopMargin", "canvasBottomMargin"]
                        }
                    ]
                },
                {
                    id: "Data", group: true, sub: [
                        {id: "Data Plot Cosmetics", prop: ["lineColor", "lineThickness", "lineAlpha"]},
                        {
                            id: "Data Value Cosmetics",
                            prop: ["valueFont", "valueFontColor", "valueFontSize", "valueFontBold", "valueFontItalic", "valueBgColor", "valueBorderColor", "valueAlpha", "valueFontAlpha", "valueBgAlpha", "valueBorderAlpha", "valueBorderThickness", "valueBorderRadius", "valueBorderDashed", "valueBorderDashGap", "valueBorderDashLen", "valueHoverAlpha", "valueFontHoverAlpha", "valueBgHoverAlpha", "valueBorderHoverAlpha", "showValuesOnHover"]
                        },
                        {
                            id: "Data Label Cosmetics",
                            prop: ["labelFont", "labelFontColor", "labelFontSize", "labelFontBold", "labelFontItalic", "labelBgColor", "labelBorderColor", "labelAlpha", "labelBgAlpha", "labelBorderAlpha", "labelBorderPadding", "labelBorderRadius", "labelBorderThickness", "labelBorderDashed", "labelBorderDashLen", "labelBorderDashGap", "labelLink"]
                        },
                        {
                            id: "Data Plot Hover Effects",
                            prop: ["showHoverEffect", "plotHoverEffect", "plotFillHoverColor", "plotFillHoverAlpha"]
                        }
                    ]
                },
                {
                    id: "Others", group: true, sub: [
                        {
                            id: "Number Formatting",
                            prop: ["formatNumber", "formatNumberScale", "defaultNumberScale", "numberScaleUnit", "numberScaleValue", "scaleRecursively", "maxScaleRecursion", "scaleSeparator", "numberPrefix", "numberSuffix", "decimalSeparator", "thousandSeparator", "thousandSeparatorPosition", "inDecimalSeparator", "inThousandSeparator", "decimals", "forceDecimals", "forceYAxisValueDecimals", "yAxisValueDecimals"]
                        },
                        {
                            id: "Font Properties",
                            prop: ["baseFont", "baseFontSize", "baseFontColor", "outCnvBaseFont", "outCnvBaseFontSize", "outCnvBaseFontColor"]
                        }
                    ]
                }
            ],
            "categories": ["font", "fontSize", "fontColor"],
            "category": ["label", "showLabel", "toolText", "font", "fontColor", "fontSize", "fontBold", "fontItalic", "bgColor", "borderColor", "alpha", "bgAlpha", "borderAlpha", "borderPadding", "borderRadius", "borderThickness", "borderDashed", "borderDashLen", "borderDashGap", "link"],
            "dataset": ["seriesName", "color", "alpha", "showValues", "valuePosition", "drawAnchors", "anchorRadius", "lineThickness", "anchorBorderColor", "anchorBorderThickness", "anchorBgColor", "anchorBgAlpha", "includeInLegend", "dashed"],
            "set": ["value", "displayValue", "toolText"],
            "Vertical Trend Lines": ["startIndex", "endIndex", "displayValue", "displayAlways", "displayWhenCount", "color", "showOnTop", "thickness", "alpha", "valueOnTop"],
            "Trend Lines": ["valueOnRight", "color", "isTrendZone", "showOnTop", "thickness", "alpha", "dashed", "dashLen", "dashGap", "startValue", "endValue", "displayValue", "toolText"]
        },
        MSArea: {
            chart: [
                {
                    id: "Chart", group: true, sub: [
                        {id: "Titles (Axis) Names", prop: ["caption", "subCaption", "xAxisName", "yAxisName"]},
                        {
                            id: "Functional Attributes",
                            prop: ["animation", "palette", "paletteColors", "connectNullData", "showLabels", "maxLabelHeight", "labelDisplay", "useEllipsesWhenOverflow", "rotateLabels", "slantLabels", "labelStep", "staggerLines", "showValues", "valuePosition", "rotateValues", "showYAxisValues", "showLimits", "showDivLineValues", "yAxisValuesStep", "showShadow", "adjustDiv", "rotateYAxisName", "yAxisNameWidth", "clickURL", "yAxisMinValue", "yAxisMaxValue", "setAdaptiveYMin"]
                        },
                        {
                            id: "Chart Caption Cosmetics",
                            prop: ["captionAlignment", "captionOnTop", "captionFontSize", "subCaptionFontSize", "captionFont", "subCaptionFont", "captionFontColor", "subCaptionFontColor", "captionFontBold", "subCaptionFontBold", "alignCaptionWithCanvas", "captionHorizontalPadding"]
                        },
                        {
                            id: "X-axis Name Cosmetics",
                            prop: ["xAxisNameFontColor", "xAxisNameFontSize", "xAxisNameFontBold", "xAxisNameFontItalic", "xAxisNameBgColor", "xAxisNameBorderColor", "xAxisNameAlpha", "xAxisNameFontAlpha", "xAxisNameBgAlpha", "xAxisNameBorderAlpha", "xAxisNameBorderPadding", "xAxisNameBorderRadius", "xAxisNameBorderThickness", "xAxisNameBorderDashed", "xAxisNameBorderDashLen", "xAxisNameBorderDashGap"]
                        },
                        {
                            id: "Y-axis Name Cosmetics",
                            prop: ["yAxisNameFont", "yAxisNameFontColor", "yAxisNameFontSize", "yAxisNameFontBold", "yAxisNameFontItalic", "yAxisNameBgColor", "yAxisNameBorderColor", "yAxisNameAlpha", "yAxisNameFontAlpha", "yAxisNameBgAlpha", "yAxisNameBorderAlpha", "yAxisNameBorderPadding", "yAxisNameBorderRadius", "yAxisNameBorderThickness", "yAxisNameBorderDashed", "yAxisNameBorderDashLen", "yAxisNameBorderDashGap"]
                        },
                        {
                            id: "Charts Cosmetics",
                            prop: ["showBorder", "borderColor", "borderThickness", "borderAlpha", "bgColor", "bgAlpha", "bgRatio", "bgAngle", "bgImage", "bgImageAlpha", "bgImageDisplayMode", "bgImageVAlign", "bgImageHAlign", "bgImageScale", "xAxisLineColor", "canvasBgColor", "canvasBgAlpha", "canvasBgRatio", "canvasBgAngle", "canvasBorderColor", "canvasBorderThickness", "canvasBorderAlpha", "showVLineLabelBorder", "logoURL", "logoPosition", "logoAlpha", "logoScale", "logoLink"]
                        },
                        {
                            id: "Anchors",
                            prop: ["drawAnchors", "anchorSides", "anchorRadius", "anchorBorderColor", "anchorBorderThickness", "anchorBgColor", "anchorAlpha", "anchorBgAlpha"]
                        },
                        {
                            id: "Legend Properties",
                            prop: ["showLegend", "legendItemFontBold", "legendItemFont", "legendItemFontSize", "legendItemFontColor", "legendItemHoverFontColor", "legendCaptionAlignment", "legendCaptionBold", "legendCaptionFont", "legendCaptionFontSize", "legendIconScale", "legendItemHiddenColor", "legendPosition", "legendBgColor", "legendBgAlpha", "legendBorderColor", "legendBorderThickness", "legendBorderAlpha", "legendShadow", "legendAllowDrag", "legendScrollBgColor", "reverseLegend", "interactiveLegend", "legendNumColumns", "minimiseWrappingInLegend"]
                        },
                        {
                            id: "Divisional Lines/Grids",
                            prop: ["numDivLines", "divLineColor", "divLineThickness", "divLineAlpha", "divLineDashed", "divLineDashLen", "divLineDashGap", "zeroPlaneColor", "zeroPlaneThickness", "zeroPlaneAlpha", "showZeroPlaneValue", "showAlternateHGridColor", "alternateHGridColor", "alternateHGridAlpha", "numVDivLines", "vDivLineColor", "vDivLineThickness", "vDivLineAlpha", "vDivLineDashed", "vDivLineDashLen", "vDivLineDashGap", "showAlternateVGridColor", "alternateVGridColor", "alternateVGridAlpha"]
                        },
                        {
                            id: "Tool-tip",
                            prop: ["showToolTip", "toolTipColor", "toolTipBgColor", "toolTipBorderColor", "toolTipSepChar", "seriesNameInToolTip", "showToolTipShadow"]
                        },
                        {
                            id: "Paddings and Margins",
                            prop: ["captionPadding", "xAxisNamePadding", "yAxisNamePadding", "yAxisValuesPadding", "labelPadding", "valuePadding", "plotSpacePercent", "chartLeftMargin", "chartRightMargin", "chartTopMargin", "chartBottomMargin", "canvasLeftMargin", "canvasRightMargin", "canvasTopMargin", "canvasBottomMargin"]
                        }
                    ]
                },
                {
                    id: "Data", group: true, sub: [
                        {
                            id: "Data Plot Cosmetics",
                            prop: ["showPlotBorder", "plotBorderColor", "plotBorderThickness", "plotBorderAlpha", "plotBorderDashed", "plotBorderDashLen", "plotBorderDashGap", "plotFillAngle", "plotFillAlpha", "plotGradientColor", "plotFillColor"]
                        },
                        {
                            id: "Data Value Cosmetics",
                            prop: ["valueFont", "valueFontColor", "valueFontSize", "valueFontBold", "valueFontItalic", "valueBgColor", "valueBorderColor", "valueAlpha", "valueFontAlpha", "valueBgAlpha", "valueBorderAlpha", "valueBorderThickness", "valueBorderRadius", "valueBorderDashed", "valueBorderDashGap", "valueBorderDashLen", "valueHoverAlpha", "valueFontHoverAlpha", "valueBgHoverAlpha", "valueBorderHoverAlpha", "showValuesOnHover"]
                        },
                        {
                            id: "Data Label Cosmetics",
                            prop: ["labelFont", "labelFontColor", "labelFontSize", "labelFontBold", "labelFontItalic", "labelBgColor", "labelBorderColor", "labelAlpha", "labelBgAlpha", "labelBorderAlpha", "labelBorderPadding", "labelBorderRadius", "labelBorderThickness", "labelBorderDashed", "labelBorderDashLen", "labelBorderDashGap", "labelLink"]
                        },
                        {
                            id: "Data Plot Hover Effects",
                            prop: ["showHoverEffect", "plotHoverEffect", "plotFillHoverColor", "plotFillHoverAlpha"]
                        }
                    ]
                },
                {
                    id: "Others", group: true, sub: [
                        {
                            id: "Number Formatting",
                            prop: ["formatNumber", "formatNumberScale", "defaultNumberScale", "numberScaleUnit", "numberScaleValue", "scaleRecursively", "maxScaleRecursion", "scaleSeparator", "numberPrefix", "numberSuffix", "decimalSeparator", "thousandSeparator", "thousandSeparatorPosition", "inDecimalSeparator", "inThousandSeparator", "decimals", "forceDecimals", "forceYAxisValueDecimals", "yAxisValueDecimals"]
                        },
                        {
                            id: "Font Properties",
                            prop: ["baseFont", "baseFontSize", "baseFontColor", "outCnvBaseFont", "outCnvBaseFontSize", "outCnvBaseFontColor"]
                        }
                    ]
                }
            ],
            "categories": ["font", "fontSize", "fontColor"],
            "category": ["label", "showLabel", "toolText", "font", "fontColor", "fontSize", "fontBold", "fontItalic", "bgColor", "borderColor", "alpha", "bgAlpha", "borderAlpha", "borderPadding", "borderRadius", "borderThickness", "borderDashed", "borderDashLen", "borderDashGap", "link"],
            "dataset": ["seriesName", "color", "alpha", "showValues", "dashed", "includeInLegend", "drawAnchors", "anchorSides", "anchorRadius", "anchorBorderColor", "anchorBorderThickness", "anchorBgColor", "anchorAlpha", "anchorBgAlpha", "showPlotBorder", "plotBorderColor", "plotBorderThickness", "plotBorderAlpha"],
            "set": ["value", "displayValue", "link", "toolText", "showValue", "anchorSides", "anchorRadius", "anchorBorderColor", "anchorBorderThickness", "anchorBgColor", "anchorAlpha", "anchorBgAlpha"],
            "Vertical data separator lines": ["linePosition", "color", "thickness", "showLabelBorder", "label", "labelPosition", "alpha", "dashed", "dashLen", "dashGap", "labelHAlign", "labelVAlign"],
            "Trend Lines": ["startValue", "endValue", "displayValue", "color", "isTrendZone", "showOnTop", "thickness", "alpha", "dashed", "dashLen", "dashGap", "valueOnRight", "toolText"]
        },
        MSBar2D: {
            chart: [
                {
                    id: "Chart", group: true, sub: [
                        {id: "Titles (Axis) Names", prop: ["caption", "subCaption", "xAxisName", "yAxisName"]},
                        {
                            id: "Functional Attributes",
                            prop: ["animation", "palette", "paletteColors", "showLabels", "labelStep", "maxLabelWidthPercent", "useEllipsesWhenOverflow", "showValues", "placeValuesInside", "showYAxisValues", "showLimits", "showDivLineValues", "yAxisValuesStep", "showShadow", "adjustDiv", "clickURL", "rotateXAxisName", "xAxisNameWidth", "yAxisMinValue", "yAxisMaxValue", "setAdaptiveYMin", "centerXaxisName"]
                        },
                        {
                            id: "Chart Caption Cosmetics",
                            prop: ["captionAlignment", "captionOnTop", "captionFontSize", "subCaptionFontSize", "captionFont", "subCaptionFont", "captionFontColor", "subCaptionFontColor", "captionFontBold", "subCaptionFontBold", "alignCaptionWithCanvas", "captionHorizontalPadding"]
                        },
                        {
                            id: "X-axis Name Cosmetics",
                            prop: ["xAxisNameFontColor", "xAxisNameFontSize", "xAxisNameFontBold", "xAxisNameFontItalic", "xAxisNameBgColor", "xAxisNameBorderColor", "xAxisNameAlpha", "xAxisNameFontAlpha", "xAxisNameBgAlpha", "xAxisNameBorderAlpha", "xAxisNameBorderPadding", "xAxisNameBorderRadius", "xAxisNameBorderThickness", "xAxisNameBorderDashed", "xAxisNameBorderDashLen", "xAxisNameBorderDashGap"]
                        },
                        {
                            id: "Y-axis Name Cosmetics",
                            prop: ["yAxisNameFont", "yAxisNameFontColor", "yAxisNameFontSize", "yAxisNameFontBold", "yAxisNameFontItalic", "yAxisNameBgColor", "yAxisNameBorderColor", "yAxisNameAlpha", "yAxisNameFontAlpha", "yAxisNameBgAlpha", "yAxisNameBorderAlpha", "yAxisNameBorderPadding", "yAxisNameBorderRadius", "yAxisNameBorderThickness", "yAxisNameBorderDashed", "yAxisNameBorderDashLen", "yAxisNameBorderDashGap"]
                        },
                        {
                            id: "Charts Cosmetics",
                            prop: ["showBorder", "borderColor", "borderThickness", "borderAlpha", "bgColor", "bgAlpha", "bgRatio", "bgAngle", "bgImage", "bgImageAlpha", "bgImageDisplayMode", "bgImageVAlign", "bgImageHAlign", "bgImageScale", "xAxisLineColor", "canvasBgColor", "canvasBgAlpha", "canvasBgRatio", "canvasBgAngle", "canvasBorderColor", "canvasBorderThickness", "canvasBorderAlpha", "showVLineLabelBorder", "logoURL", "logoPosition", "logoAlpha", "logoScale", "logoLink"]
                        },
                        {
                            id: "Legend Properties",
                            prop: ["showLegend", "legendItemFontBold", "legendItemFont", "legendItemFontSize", "legendItemFontColor", "legendItemHoverFontColor", "legendCaptionAlignment", "legendCaptionBold", "legendCaptionFont", "legendCaptionFontSize", "legendIconScale", "legendItemHiddenColor", "legendPosition", "legendBgColor", "legendBgAlpha", "legendBorderColor", "legendBorderThickness", "legendBorderAlpha", "legendShadow", "legendAllowDrag", "legendScrollBgColor", "reverseLegend", "interactiveLegend", "legendNumColumns", "minimiseWrappingInLegend"]
                        },
                        {
                            id: "Divisional Lines/Grids",
                            prop: ["numDivLines", "divLineColor", "divLineThickness", "divLineAlpha", "divLineIsDashed", "divLineDashLen", "divLineDashGap", "zeroPlaneColor", "zeroPlaneThickness", "zeroPlaneAlpha", "showZeroPlaneValue", "showAlternateHGridColor", "alternateHGridColor", "alternateHGridAlpha"]
                        },
                        {
                            id: "Tool-tip",
                            prop: ["showToolTip", "toolTipColor", "toolTipBgColor", "toolTipBorderColor", "toolTipSepChar", "seriesNameInToolTip", "showToolTipShadow"]
                        },
                        {
                            id: "Paddings and Margins",
                            prop: ["captionPadding", "xAxisNamePadding", "yAxisNamePadding", "yAxisValuesPadding", "labelPadding", "valuePadding", "plotSpacePercent", "chartLeftMargin", "chartRightMargin", "chartTopMargin", "chartBottomMargin", "legendPadding", "canvasLeftMargin", "canvasRightMargin", "canvasTopMargin", "canvasBottomMargin"]
                        }
                    ]
                },
                {
                    id: "Data", group: true, sub: [
                        {
                            id: "Data Plot Cosmetics",
                            prop: ["useRoundEdges", "showPlotBorder", "plotBorderColor", "plotBorderThickness", "plotBorderAlpha", "plotBorderDashed", "plotBorderDashLen", "plotBorderDashGap", "plotFillAngle", "plotFillRatio", "plotFillAlpha", "plotGradientColor"]
                        },
                        {
                            id: "Data Value Cosmetics",
                            prop: ["valueFont", "valueFontColor", "valueFontSize", "valueFontBold", "valueFontItalic", "valueBgColor", "valueBorderColor", "valueAlpha", "valueFontAlpha", "valueBgAlpha", "valueBorderAlpha", "valueBorderThickness", "valueBorderRadius", "valueBorderDashed", "valueBorderDashGap", "valueBorderDashLen", "valueHoverAlpha", "valueFontHoverAlpha", "valueBgHoverAlpha", "valueBorderHoverAlpha", "showValuesOnHover"]
                        },
                        {
                            id: "Data Label Cosmetics",
                            prop: ["labelFont", "labelFontColor", "labelFontSize", "labelFontBold", "labelFontItalic", "labelBgColor", "labelBorderColor", "labelAlpha", "labelBgAlpha", "labelBorderAlpha", "labelBorderPadding", "labelBorderRadius", "labelBorderThickness", "labelBorderDashed", "labelBorderDashLen", "labelBorderDashGap", "labelLink"]
                        },
                        {
                            id: "Data Plot Hover Effects",
                            prop: ["showHoverEffect", "plotHoverEffect", "plotFillHoverColor", "plotFillHoverAlpha"]
                        }
                    ]
                },
                {
                    id: "Others", group: true, sub: [
                        {
                            id: "Number Formatting",
                            prop: ["formatNumber", "formatNumberScale", "defaultNumberScale", "numberScaleUnit", "numberScaleValue", "scaleRecursively", "maxScaleRecursion", "scaleSeparator", "numberPrefix", "numberSuffix", "decimalSeparator", "thousandSeparator", "thousandSeparatorPosition", "inDecimalSeparator", "inThousandSeparator", "decimals", "forceDecimals", "forceYAxisValueDecimals", "yAxisValueDecimals"]
                        },
                        {
                            id: "Font Properties",
                            prop: ["baseFont", "baseFontSize", "baseFontColor", "outCnvBaseFont", "outCnvBaseFontSize", "outCnvBaseFontColor"]
                        }
                    ]
                }
            ],
            "categories": ["font", "fontSize", "fontColor"],
            "category": ["label", "showLabel", "toolText", "font", "fontColor", "fontSize", "fontBold", "fontItalic", "bgColor", "borderColor", "alpha", "bgAlpha", "borderAlpha", "borderPadding", "borderRadius", "borderThickness", "borderDashed", "borderDashLen", "borderDashGap", "link"],
            "dataset": ["seriesName", "color", "alpha", "ratio", "showValues", "dashed", "includeInLegend"],
            "set": ["label", "value", "displayValue", "color", "link", "toolText", "showLabel", "showValue", "dashed", "alpha"],
            "Vertical data separator lines": ["linePosition", "color", "thickness", "showLabelBorder", "label", "labelPosition", "alpha", "dashed", "dashLen", "dashGap", "labelHAlign", "labelVAlign"],
            "Trend Lines": ["startValue", "endValue", "displayValue", "color", "isTrendZone", "showOnTop", "thickness", "alpha", "dashed", "dashLen", "dashGap", "valueOnRight", "toolText"]
        },
        MSBar3D: {
            chart: [
                {
                    id: "Chart", group: true, sub: [
                        {id: "Titles (Axis) Names", prop: ["caption", "subCaption", "xAxisName", "yAxisName"]},
                        {
                            id: "Functional Attributes",
                            prop: ["animation", "palette", "paletteColors", "showLabels", "labelStep", "maxLabelWidthPercent", "useEllipsesWhenOverflow", "showValues", "placeValuesInside", "showYAxisValues", "yAxisValuesStep", "showShadow", "adjustDiv", "rotateXAxisName", "xAxisNameWidth", "clickURL", "yAxisMaxValue", "yAxisMinValue", "maxBarHeight", "use3DLighting", "setAdaptiveYMin", "centerXaxisName"]
                        },
                        {
                            id: "Chart Caption Cosmetics",
                            prop: ["captionAlignment", "captionOnTop", "captionFontSize", "subCaptionFontSize", "captionFont", "subCaptionFont", "captionFontColor", "subCaptionFontColor", "captionFontBold", "subCaptionFontBold", "alignCaptionWithCanvas", "captionHorizontalPadding"]
                        },
                        {
                            id: "X-axis Name Cosmetics",
                            prop: ["xAxisNameFontColor", "xAxisNameFontSize", "xAxisNameFontBold", "xAxisNameFontItalic", "xAxisNameBgColor", "xAxisNameBorderColor", "xAxisNameAlpha", "xAxisNameFontAlpha", "xAxisNameBgAlpha", "xAxisNameBorderAlpha", "xAxisNameBorderPadding", "xAxisNameBorderRadius", "xAxisNameBorderThickness", "xAxisNameBorderDashed", "xAxisNameBorderDashLen", "xAxisNameBorderDashGap"]
                        },
                        {
                            id: "Y-axis Name Cosmetics",
                            prop: ["yAxisNameFont", "yAxisNameFontColor", "yAxisNameFontSize", "yAxisNameFontBold", "yAxisNameFontItalic", "yAxisNameBgColor", "yAxisNameBorderColor", "yAxisNameAlpha", "yAxisNameFontAlpha", "yAxisNameBgAlpha", "yAxisNameBorderAlpha", "yAxisNameBorderPadding", "yAxisNameBorderRadius", "yAxisNameBorderThickness", "yAxisNameBorderDashed", "yAxisNameBorderDashLen", "yAxisNameBorderDashGap"]
                        },
                        {
                            id: "Charts Cosmetics",
                            prop: ["showBorder", "borderColor", "borderThickness", "borderAlpha", "bgColor", "bgAlpha", "bgRatio", "bgAngle", "bgImage", "bgImageAlpha", "bgImageDisplayMode", "bgImageVAlign", "bgImageHAlign", "bgImageScale", "xAxisLineColor", "canvasBgColor", "canvasBgAlpha", "canvasBaseColor", "showCanvasBg", "showCanvasBase", "canvasBaseDepth", "canvasBgDepth", "showVLineLabelBorder", "logoURL", "logoPosition", "logoAlpha", "logoScale", "logoLink"]
                        },
                        {
                            id: "Legend Properties",
                            prop: ["showLegend", "legendItemFontBold", "legendItemFont", "legendItemFontSize", "legendItemFontColor", "legendItemHoverFontColor", "legendCaptionAlignment", "legendCaptionBold", "legendCaptionFont", "legendCaptionFontSize", "legendIconScale", "legendItemHiddenColor", "legendPosition", "legendBgColor", "legendBgAlpha", "legendBorderColor", "legendBorderThickness", "legendBorderAlpha", "legendShadow", "legendAllowDrag", "legendScrollBgColor", "reverseLegend", "interactiveLegend", "legendNumColumns", "minimiseWrappingInLegend"]
                        },
                        {
                            id: "Divisional Lines/Grids",
                            prop: ["numDivLines", "divLineColor", "divLineThickness", "divLineAlpha", "divLineDashed", "divLineDashLen", "divLineDashGap", "zeroPlaneColor", "zeroPlaneAlpha", "zeroPlaneShowBorder", "showZeroPlaneValue", "zeroPlaneBorderColor"]
                        },
                        {
                            id: "Tool-tip",
                            prop: ["showToolTip", "toolTipColor", "toolTipBgColor", "toolTipBorderColor", "toolTipSepChar", "seriesNameInToolTip", "showToolTipShadow"]
                        },
                        {
                            id: "Paddings and Margins",
                            prop: ["captionPadding", "xAxisNamePadding", "yAxisNamePadding", "yAxisValuesPadding", "labelPadding", "valuePadding", "plotSpacePercent", "chartLeftMargin", "chartRightMargin", "chartTopMargin", "chartBottomMargin", "legendPadding", "canvasLeftMargin", "canvasRightMargin", "canvasTopMargin", "canvasBottomMargin"]
                        }
                    ]
                },
                {
                    id: "Data", group: true, sub: [
                        {
                            id: "Data Plot Cosmetics",
                            prop: ["showPlotBorder", "plotBorderColor", "plotBorderAlpha", "plotFillAlpha", "overlapBars"]
                        },
                        {
                            id: "Data Value Cosmetics",
                            prop: ["valueFont", "valueFontColor", "valueFontSize", "valueFontBold", "valueFontItalic", "valueBgColor", "valueBorderColor", "valueAlpha", "valueFontAlpha", "valueBgAlpha", "valueBorderAlpha", "valueBorderThickness", "valueBorderRadius", "valueBorderDashed", "valueBorderDashGap", "valueBorderDashLen", "valueHoverAlpha", "valueFontHoverAlpha", "valueBgHoverAlpha", "valueBorderHoverAlpha", "showValuesOnHover"]
                        },
                        {
                            id: "Data Label Cosmetics",
                            prop: ["labelFont", "labelFontColor", "labelFontSize", "labelFontBold", "labelFontItalic", "labelBgColor", "labelBorderColor", "labelAlpha", "labelBgAlpha", "labelBorderAlpha", "labelBorderPadding", "labelBorderRadius", "labelBorderThickness", "labelBorderDashed", "labelBorderDashLen", "labelBorderDashGap", "labelLink"]
                        },
                        {
                            id: "Data Plot Hover Effects",
                            prop: ["showHoverEffect", "plotHoverEffect", "plotFillHoverColor", "plotFillHoverAlpha"]
                        }
                    ]
                },
                {
                    id: "Others", group: true, sub: [
                        {
                            id: "Number Formatting",
                            prop: ["formatNumber", "formatNumberScale", "defaultNumberScale", "numberScaleUnit", "numberScaleValue", "scaleRecursively", "maxScaleRecursion", "scaleSeparator", "numberPrefix", "numberSuffix", "decimalSeparator", "thousandSeparator", "thousandSeparatorPosition", "inDecimalSeparator", "inThousandSeparator", "decimals", "forceDecimals", "forceYAxisValueDecimals", "yAxisValueDecimals"]
                        },
                        {
                            id: "Font Properties",
                            prop: ["baseFont", "baseFontSize", "baseFontColor", "outCnvBaseFont", "outCnvBaseFontSize", "outCnvBaseFontColor"]
                        }
                    ]
                }
            ],
            "categories": ["font", "fontSize", "fontColor"],
            "category": ["label", "showLabel", "toolText", "font", "fontColor", "fontSize", "fontBold", "fontItalic", "bgColor", "borderColor", "alpha", "bgAlpha", "borderAlpha", "borderPadding", "borderRadius", "borderThickness", "borderDashed", "borderDashLen", "borderDashGap", "link"],
            "dataset": ["seriesName", "color", "alpha", "ratio", "showValues", "dashed", "includeInLegend"],
            "set": ["label", "value", "displayValue", "color", "link", "toolText", "showLabel", "showValue", "alpha"],
            "Vertical data separator lines": ["linePosition", "color", "thickness", "showLabelBorder", "label", "labelPosition", "alpha", "dashed", "dashLen", "dashGap", "labelHAlign", "labelVAlign"],
            "Trend Lines": ["startValue", "endValue", "displayValue", "color", "isTrendZone", "thickness", "alpha", "dashed", "dashLen", "dashGap", "valueOnRight", "toolText"]
        },
        Marimekko: {
            chart: [

                {
                    id: "Chart", group: true, sub: [
                        {id: "Titles (Axis) Names", prop: ["caption", "subCaption", "xAxisName", "yAxisName"]},
                        {
                            id: "Functional Attributes",
                            prop: ["animation", "palette", "paletteColors", "showLabels", "maxLabelHeight", "labelDisplay", "useEllipsesWhenOverflow", "rotateLabels", "slantLabels", "labelStep", "staggerLines", "showValues", "rotateValues", "showYAxisValues", "showLimits", "showDivLineValues", "yAxisValuesStep", "showShadow", "adjustDiv", "clickURL", "rotateYAxisName", "yAxisNameWidth", "yAxisMaxValue", "yAxisMinValue", "showSum", "usePercentDistribution", "showXAxisPercentValues"]
                        },
                        {
                            id: "Chart Caption Cosmetics",
                            prop: ["captionAlignment", "captionOnTop", "captionFontSize", "subCaptionFontSize", "captionFont", "subCaptionFont", "captionFontColor", "subCaptionFontColor", "captionFontBold", "subCaptionFontBold", "alignCaptionWithCanvas", "captionHorizontalPadding"]
                        },
                        {
                            id: "X-axis Name Cosmetics",
                            prop: ["xAxisNameFontColor", "xAxisNameFontSize", "xAxisNameFontBold", "xAxisNameFontItalic", "xAxisNameBgColor", "xAxisNameBorderColor", "xAxisNameAlpha", "xAxisNameFontAlpha", "xAxisNameBgAlpha", "xAxisNameBorderAlpha", "xAxisNameBorderPadding", "xAxisNameBorderRadius", "xAxisNameBorderThickness", "xAxisNameBorderDashed", "xAxisNameBorderDashLen", "xAxisNameBorderDashGap"]
                        },
                        {
                            id: "Y-axis Name Cosmetics",
                            prop: ["yAxisNameFont", "yAxisNameFontColor", "yAxisNameFontSize", "yAxisNameFontBold", "yAxisNameFontItalic", "yAxisNameBgColor", "yAxisNameBorderColor", "yAxisNameAlpha", "yAxisNameFontAlpha", "yAxisNameBgAlpha", "yAxisNameBorderAlpha", "yAxisNameBorderPadding", "yAxisNameBorderRadius", "yAxisNameBorderThickness", "yAxisNameBorderDashed", "yAxisNameBorderDashLen", "yAxisNameBorderDashGap"]
                        },
                        {
                            id: "Charts Cosmetics",
                            prop: ["showBorder", "borderColor", "borderThickness", "borderAlpha", "bgColor", "bgAlpha", "bgRatio", "bgAngle", "bgImage", "bgImageAlpha", "bgImageDisplayMode", "bgImageVAlign", "bgImageHAlign", "bgImageScale", "xAxisLineColor", "canvasBgColor", "canvasBgAlpha", "canvasBgRatio", "canvasBgAngle", "canvasBorderColor", "canvasBorderThickness", "canvasBorderAlpha", "showVLineLabelBorder", "logoURL", "logoPosition", "logoAlpha", "logoScale", "logoLink"]
                        },
                        {
                            id: "Legend Properties",
                            prop: ["showLegend", "legendItemFontBold", "legendItemFont", "legendItemFontSize", "legendItemFontColor", "legendPosition", "legendCaptionAlignment", "legendCaptionBold", "legendCaptionFont", "legendCaptionFontSize", "legendCaptionFontColor", "legendItemHiddenColor", "legendBgColor", "legendBgAlpha", "legendBorderColor", "legendBorderThickness", "legendBorderAlpha", "legendShadow", "legendAllowDrag", "legendScrollBgColor", "reverseLegend", "legendIconScale", "legendNumColumns", "minimiseWrappingInLegend"]
                        },
                        {
                            id: "Divisional Lines/Grids",
                            prop: ["numDivLines", "divLineColor", "divLineThickness", "divLineAlpha", "divLineIsDashed", "divLineDashLen", "divLineDashGap", "zeroPlaneColor", "zeroPlaneThickness", "showAlternateHGridColor", "alternateHGridColor", "alternateHGridAlpha"]
                        },
                        {
                            id: "Tool-tip",
                            prop: ["showToolTip", "toolTipColor", "toolTipBgColor", "toolTipBorderColor", "toolTipSepChar", "seriesNameInToolTip", "showToolTipShadow"]
                        },
                        {
                            id: "Paddings and Margins",
                            prop: ["captionPadding", "xAxisNamePadding", "yAxisNamePadding", "yAxisValuesPadding", "labelPadding", "valuePadding", "plotSpacePercent", "chartLeftMargin", "chartRightMargin", "chartTopMargin", "chartBottomMargin", "legendPadding", "canvasLeftMargin", "canvasRightMargin", "canvasTopMargin", "canvasBottomMargin"]
                        }
                    ]
                },
                {
                    id: "Data", group: true, sub: [
                        {
                            id: "Data Plot Cosmetics",
                            prop: ["showPlotBorder", "plotBorderColor", "plotBorderThickness", "plotBorderAlpha", "plotBorderDashed", "plotBorderDashLen", "plotBorderDashGap", "plotFillAngle", "plotFillRatio", "plotFillAlpha", "plotGradientColor"]
                        },
                        {
                            id: "Data Value Cosmetics",
                            prop: ["valueFont", "valueFontColor", "valueFontSize", "valueFontBold", "valueFontItalic", "valueBgColor", "valueBorderColor", "valueAlpha", "valueFontAlpha", "valueBgAlpha", "valueBorderAlpha", "valueBorderThickness", "valueBorderRadius", "valueBorderDashed", "valueBorderDashGap", "valueBorderDashLen", "valueHoverAlpha", "valueFontHoverAlpha", "valueBgHoverAlpha", "valueBorderHoverAlpha", "showValuesOnHover"]
                        },
                        {
                            id: "Data Label Cosmetics",
                            prop: ["labelFont", "labelFontColor", "labelFontSize", "labelFontBold", "labelFontItalic", "labelBgColor", "labelBorderColor", "labelAlpha", "labelBgAlpha", "labelBorderAlpha", "labelBorderPadding", "labelBorderRadius", "labelBorderThickness", "labelBorderDashed", "labelBorderDashLen", "labelBorderDashGap", "labelLink"]
                        },
                        {
                            id: "Data Plot Hover Effects",
                            prop: ["showHoverEffect", "plotHoverEffect", "plotFillHoverColor", "plotFillHoverAlpha"]
                        }
                    ]
                },
                {
                    id: "Others", group: true, sub: [
                        {
                            id: "Number Formatting",
                            prop: ["formatNumber", "formatNumberScale", "defaultNumberScale", "numberScaleUnit", "numberScaleValue", "scaleRecursively", "maxScaleRecursion", "scaleSeparator", "numberPrefix", "numberSuffix", "decimalSeparator", "thousandSeparator", "thousandSeparatorPosition", "inDecimalSeparator", "inThousandSeparator", "decimals", "forceDecimals", "forceYAxisValueDecimals", "yAxisValueDecimals"]
                        },
                        {
                            id: "Font Properties",
                            prop: ["baseFont", "baseFontSize", "baseFontColor", "outCnvBaseFont", "outCnvBaseFontSize", "outCnvBaseFontColor"]
                        }
                    ]
                }
            ],
            "categories": ["font", "fontSize", "fontColor"],
            "category": ["label", "showLabel", "toolText", "widthPercent", "font", "fontColor", "fontSize", "fontBold", "fontItalic", "bgColor", "borderColor", "alpha", "bgAlpha", "borderAlpha", "borderPadding", "borderRadius", "borderThickness", "borderDashed", "borderDashLen", "borderDashGap", "link"],
            "dataset": ["seriesName", "color", "alpha", "ratio", "showValues", "dashed", "includeInLegend"],
            "set": ["label", "value", "displayValue", "color", "link", "toolText", "showLabel", "showValue", "dashed", "alpha"],
            "Vertical data separator lines": ["linePosition", "color", "thickness", "showLabelBorder", "label", "labelPosition", "alpha", "dashed", "dashLen", "dashGap", "labelHAlign", "labelVAlign"],
            "Trend Lines": ["startValue", "endValue", "displayValue", "color", "isTrendZone", "showOnTop", "thickness", "alpha", "dashed", "dashLen", "dashGap", "valueOnRight", "toolText"]
        },
        StackedColumn2D: {
            chart: [
                {
                    id: "Chart", group: true, sub: [
                        {id: "Titles (Axis) Names", prop: ["caption", "subCaption", "xAxisName", "yAxisName"]},
                        {
                            id: "Functional Attributes",
                            prop: ["animation", "palette", "paletteColors", "showLabels", "maxLabelHeight", "labelDisplay", "useEllipsesWhenOverflow", "rotateLabels", "slantLabels", "labelStep", "staggerLines", "showValues", "rotateValues", "showYAxisValues", "showLimits", "showDivLineValues", "yAxisValuesStep", "showShadow", "adjustDiv", "rotateYAxisName", "yAxisNameWidth", "clickURL", "showSum", "yAxisMinValue", "yAxisMaxValue", "stack100Percent", "showPercentValues", "showPercentInToolTip"]
                        },
                        {
                            id: "Chart Caption Cosmetics",
                            prop: ["captionAlignment", "captionOnTop", "captionFontSize", "subCaptionFontSize", "captionFont", "subCaptionFont", "captionFontColor", "subCaptionFontColor", "captionFontBold", "subCaptionFontBold", "alignCaptionWithCanvas", "captionHorizontalPadding"]
                        },
                        {
                            id: "X-axis Name Cosmetics",
                            prop: ["xAxisNameFontColor", "xAxisNameFontSize", "xAxisNameFontBold", "xAxisNameFontItalic", "xAxisNameBgColor", "xAxisNameBorderColor", "xAxisNameAlpha", "xAxisNameFontAlpha", "xAxisNameBgAlpha", "xAxisNameBorderAlpha", "xAxisNameBorderPadding", "xAxisNameBorderRadius", "xAxisNameBorderThickness", "xAxisNameBorderDashed", "xAxisNameBorderDashLen", "xAxisNameBorderDashGap"]
                        },
                        {
                            id: "Y-axis Name Cosmetics",
                            prop: ["yAxisNameFont", "yAxisNameFontColor", "yAxisNameFontSize", "yAxisNameFontBold", "yAxisNameFontItalic", "yAxisNameBgColor", "yAxisNameBorderColor", "yAxisNameAlpha", "yAxisNameFontAlpha", "yAxisNameBgAlpha", "yAxisNameBorderAlpha", "yAxisNameBorderPadding", "yAxisNameBorderRadius", "yAxisNameBorderThickness", "yAxisNameBorderDashed", "yAxisNameBorderDashLen", "yAxisNameBorderDashGap"]
                        },
                        {
                            id: "Charts Cosmetics",
                            prop: ["showBorder", "borderColor", "borderThickness", "borderAlpha", "bgColor", "bgAlpha", "bgRatio", "bgAngle", "bgImage", "bgImageAlpha", "bgImageDisplayMode", "bgImageVAlign", "bgImageHAlign", "bgImageScale", "xAxisLineColor", "canvasBgColor", "canvasBgAlpha", "canvasBgRatio", "canvasBgAngle", "canvasBorderColor", "canvasBorderThickness", "canvasBorderAlpha", "showVLineLabelBorder", "logoURL", "logoPosition", "logoAlpha", "logoScale", "logoLink"]
                        },
                        {
                            id: "Legend Properties",
                            prop: ["showLegend", "legendItemFontBold", "legendItemFont", "legendItemFontSize", "legendItemFontColor", "legendItemHoverFontColor", "legendCaptionAlignment", "legendCaptionBold", "legendCaptionFont", "legendCaptionFontSize", "legendIconScale", "legendItemHiddenColor", "legendPosition", "legendBgColor", "legendBgAlpha", "legendBorderColor", "legendBorderThickness", "legendBorderAlpha", "legendShadow", "legendAllowDrag", "legendScrollBgColor", "reverseLegend", "interactiveLegend", "legendNumColumns", "minimiseWrappingInLegend"]
                        },
                        {
                            id: "Divisional Lines/Grids",
                            prop: ["numDivLines", "divLineColor", "divLineThickness", "divLineAlpha", "divLineIsDashed", "divLineDashLen", "divLineDashGap", "zeroPlaneColor", "zeroPlaneThickness", "zeroPlaneAlpha", "showZeroPlaneValue", "showAlternateHGridColor", "alternateHGridColor", "alternateHGridAlpha"]
                        },
                        {
                            id: "Tool-tip",
                            prop: ["showToolTip", "toolTipColor", "toolTipBgColor", "toolTipBorderColor", "toolTipSepChar", "seriesNameInToolTip", "showToolTipShadow"]
                        },
                        {
                            id: "Paddings and Margins",
                            prop: ["captionPadding", "xAxisNamePadding", "yAxisNamePadding", "yAxisValuesPadding", "labelPadding", "plotSpacePercent", "chartLeftMargin", "chartRightMargin", "chartTopMargin", "chartBottomMargin", "legendPadding", "canvasLeftMargin", "canvasRightMargin", "canvasTopMargin", "canvasBottomMargin"]
                        }
                    ]
                },
                {
                    id: "Data", group: true, sub: [
                        {
                            id: "Data Plot Cosmetics",
                            prop: ["useRoundEdges", "showPlotBorder", "plotBorderColor", "plotBorderThickness", "plotBorderAlpha", "plotBorderDashed", "plotBorderDashLen", "plotBorderDashGap", "plotFillAngle", "plotFillRatio", "plotFillAlpha", "plotGradientColor"]
                        },
                        {
                            id: "Data Value Cosmetics",
                            prop: ["valueFont", "valueFontColor", "valueFontSize", "valueFontBold", "valueFontItalic", "valueBgColor", "valueBorderColor", "valueAlpha", "valueFontAlpha", "valueBgAlpha", "valueBorderAlpha", "valueBorderThickness", "valueBorderRadius", "valueBorderDashed", "valueBorderDashGap", "valueBorderDashLen", "valueHoverAlpha", "valueFontHoverAlpha", "valueBgHoverAlpha", "valueBorderHoverAlpha", "showValuesOnHover"]
                        },
                        {
                            id: "Data Label Cosmetics",
                            prop: ["labelFont", "labelFontColor", "labelFontSize", "labelFontBold", "labelFontItalic", "labelBgColor", "labelBorderColor", "labelAlpha", "labelBgAlpha", "labelBorderAlpha", "labelBorderPadding", "labelBorderRadius", "labelBorderThickness", "labelBorderDashed", "labelBorderDashLen", "labelBorderDashGap", "labelLink"]
                        },
                        {
                            id: "Data Plot Hover Effects",
                            prop: ["showHoverEffect", "plotHoverEffect", "plotFillHoverColor", "plotFillHoverAlpha"]
                        }
                    ]
                },
                {
                    id: "Others", group: true, sub: [
                        {
                            id: "Number Formatting",
                            prop: ["formatNumber", "formatNumberScale", "defaultNumberScale", "numberScaleUnit", "numberScaleValue", "scaleRecursively", "maxScaleRecursion", "scaleSeparator", "numberPrefix", "numberSuffix", "decimalSeparator", "thousandSeparator", "thousandSeparatorPosition", "inDecimalSeparator", "inThousandSeparator", "decimals", "forceDecimals", "forceYAxisValueDecimals", "yAxisValueDecimals"]
                        },
                        {
                            id: "Font Properties",
                            prop: ["baseFont", "baseFontSize", "baseFontColor", "outCnvBaseFont", "outCnvBaseFontSize", "outCnvBaseFontColor"]
                        }
                    ]
                }
            ],
            "categories": ["font", "fontSize", "fontColor"],
            "category": ["label", "showLabel", "toolText", "font", "fontColor", "fontSize", "fontBold", "fontItalic", "bgColor", "borderColor", "alpha", "bgAlpha", "borderAlpha", "borderPadding", "borderRadius", "borderThickness", "borderDashed", "borderDashLen", "borderDashGap", "link"],
            "dataset": ["seriesName", "color", "alpha", "ratio", "showValues", "dashed", "includeInLegend"],
            "set": ["label", "value", "displayValue", "color", "link", "toolText", "showLabel", "showValue", "dashed", "alpha"],
            "Vertical data separator lines": ["linePosition", "color", "thickness", "showLabelBorder", "label", "labelPosition", "alpha", "dashed", "dashLen", "dashGap", "labelHAlign", "labelVAlign"],
            "Trend Lines": ["startValue", "endValue", "displayValue", "color", "isTrendZone", "showOnTop", "thickness", "alpha", "dashed", "dashLen", "dashGap", "valueOnRight", "toolText"]
        },
        StackedColumn3D: {
            chart: [
                {
                    id: "Chart", group: true, sub: [
                        {id: "Titles (Axis) Names", prop: ["caption", "subCaption", "xAxisName", "yAxisName"]},
                        {
                            id: "Functional Attributes",
                            prop: ["animation", "palette", "paletteColors", "showLabels", "maxLabelHeight", "labelDisplay", "useEllipsesWhenOverflow", "rotateLabels", "slantLabels", "labelStep", "staggerLines", "showValues", "rotateValues", "showYAxisValues", "showLimits", "showDivLineValues", "yAxisValuesStep", "showShadow", "adjustDiv", "rotateYAxisName", "yAxisNameWidth", "clickURL", "maxColWidth", "use3DLighting", "showSum", "yAxisMinValue", "yAxisMaxValue", "stack100Percent", "showPercentValues", "showPercentInToolTip"]
                        },
                        {
                            id: "Chart Caption Cosmetics",
                            prop: ["captionAlignment", "captionOnTop", "captionFontSize", "subCaptionFontSize", "captionFont", "subCaptionFont", "captionFontColor", "subCaptionFontColor", "captionFontBold", "subCaptionFontBold", "alignCaptionWithCanvas", "captionHorizontalPadding"]
                        },
                        {
                            id: "X-axis Name Cosmetics",
                            prop: ["xAxisNameFontColor", "xAxisNameFontSize", "xAxisNameFontBold", "xAxisNameFontItalic", "xAxisNameBgColor", "xAxisNameBorderColor", "xAxisNameAlpha", "xAxisNameFontAlpha", "xAxisNameBgAlpha", "xAxisNameBorderAlpha", "xAxisNameBorderPadding", "xAxisNameBorderRadius", "xAxisNameBorderThickness", "xAxisNameBorderDashed", "xAxisNameBorderDashLen", "xAxisNameBorderDashGap"]
                        },
                        {
                            id: "Y-axis Name Cosmetics",
                            prop: ["yAxisNameFont", "yAxisNameFontColor", "yAxisNameFontSize", "yAxisNameFontBold", "yAxisNameFontItalic", "yAxisNameBgColor", "yAxisNameBorderColor", "yAxisNameAlpha", "yAxisNameFontAlpha", "yAxisNameBgAlpha", "yAxisNameBorderAlpha", "yAxisNameBorderPadding", "yAxisNameBorderRadius", "yAxisNameBorderThickness", "yAxisNameBorderDashed", "yAxisNameBorderDashLen", "yAxisNameBorderDashGap"]
                        },
                        {
                            id: "Charts Cosmetics",
                            prop: ["showBorder", "borderColor", "borderThickness", "borderAlpha", "bgColor", "bgAlpha", "bgRatio", "bgAngle", "bgImage", "bgImageAlpha", "bgImageDisplayMode", "bgImageVAlign", "bgImageHAlign", "bgImageScale", "xAxisLineColor", "canvasBgColor", "canvasBgAlpha", "canvasBgRatio", "canvasBgAngle", "canvasBorderColor", "canvasBorderThickness", "canvasBorderAlpha", "showVLineLabelBorder", "logoURL", "logoPosition", "logoAlpha", "logoScale", "logoLink"]
                        },
                        {
                            id: "Legend Properties",
                            prop: ["showLegend", "legendItemFontBold", "legendItemFont", "legendItemFontSize", "legendItemFontColor", "legendItemHoverFontColor", "legendCaptionAlignment", "legendCaptionBold", "legendCaptionFont", "legendCaptionFontSize", "legendIconScale", "legendItemHiddenColor", "legendPosition", "legendBgColor", "legendBgAlpha", "legendBorderColor", "legendBorderThickness", "legendBorderAlpha", "legendShadow", "legendAllowDrag", "legendScrollBgColor", "reverseLegend", "interactiveLegend", "legendNumColumns", "minimiseWrappingInLegend"]
                        },
                        {
                            id: "Divisional Lines/Grids",
                            prop: ["numDivLines", "divLineColor", "divLineThickness", "divLineAlpha", "divLineDashed", "divLineDashLen", "divLineDashGap", "zeroPlaneColor", "zeroPlaneAlpha", "zeroPlaneShowBorder", "showZeroPlaneValue", "zeroPlaneBorderColor"]
                        },
                        {
                            id: "Tool-tip",
                            prop: ["showToolTip", "toolTipColor", "toolTipBgColor", "toolTipBorderColor", "toolTipSepChar", "seriesNameInToolTip", "showToolTipShadow"]
                        },
                        {
                            id: "Paddings and Margins",
                            prop: ["legendPadding", "captionPadding", "xAxisNamePadding", "yAxisNamePadding", "yAxisValuesPadding", "labelPadding", "plotSpacePercent", "chartLeftMargin", "chartRightMargin", "chartTopMargin", "chartBottomMargin", "canvasLeftMargin", "canvasRightMargin", "canvasTopMargin", "canvasBottomMargin"]
                        }
                    ]
                },
                {
                    id: "Data", group: true, sub: [
                        {
                            id: "Data Plot Cosmetics",
                            prop: ["showPlotBorder", "plotBorderColor", "plotBorderAlpha", "plotFillAlpha", "overlapColumns"]
                        },
                        {
                            id: "Data Value Cosmetics",
                            prop: ["valueFont", "valueFontColor", "valueFontSize", "valueFontBold", "valueFontItalic", "valueBgColor", "valueBorderColor", "valueAlpha", "valueFontAlpha", "valueBgAlpha", "valueBorderAlpha", "valueBorderThickness", "valueBorderRadius", "valueBorderDashed", "valueBorderDashGap", "valueBorderDashLen", "valueHoverAlpha", "valueFontHoverAlpha", "valueBgHoverAlpha", "valueBorderHoverAlpha", "showValuesOnHover"]
                        },
                        {
                            id: "Data Label Cosmetics",
                            prop: ["labelFont", "labelFontColor", "labelFontSize", "labelFontBold", "labelFontItalic", "labelBgColor", "labelBorderColor", "labelAlpha", "labelBgAlpha", "labelBorderAlpha", "labelBorderPadding", "labelBorderRadius", "labelBorderThickness", "labelBorderDashed", "labelBorderDashLen", "labelBorderDashGap", "labelLink"]
                        },
                        {
                            id: "Data Plot Hover Effects",
                            prop: ["showHoverEffect", "plotHoverEffect", "plotFillHoverColor", "plotFillHoverAlpha"]
                        }
                    ]
                },
                {
                    id: "Others", group: true, sub: [
                        {
                            id: "Number Formatting",
                            prop: ["formatNumber", "formatNumberScale", "defaultNumberScale", "numberScaleUnit", "numberScaleValue", "scaleRecursively", "maxScaleRecursion", "scaleSeparator", "numberPrefix", "numberSuffix", "decimalSeparator", "thousandSeparator", "thousandSeparatorPosition", "inDecimalSeparator", "inThousandSeparator", "decimals", "forceDecimals", "forceYAxisValueDecimals", "yAxisValueDecimals"]
                        },
                        {
                            id: "Font Properties",
                            prop: ["baseFont", "baseFontSize", "baseFontColor", "outCnvBaseFont", "outCnvBaseFontSize", "outCnvBaseFontColor"]
                        }
                    ]
                }
            ],
            "categories": ["font", "fontSize", "fontColor"],
            "category": ["label", "showLabel", "toolText", "font", "fontColor", "fontSize", "fontBold", "fontItalic", "bgColor", "borderColor", "alpha", "bgAlpha", "borderAlpha", "borderPadding", "borderRadius", "borderThickness", "borderDashed", "borderDashLen", "borderDashGap", "link"],
            "dataset": ["seriesName", "color", "alpha", "ratio", "showValues", "dashed", "includeInLegend"],
            "set": ["label", "value", "displayValue", "color", "link", "toolText", "showLabel", "showValue", "alpha"],
            "Vertical data separator lines": ["linePosition", "color", "thickness", "showLabelBorder", "label", "labelPosition", "alpha", "dashed", "dashLen", "dashGap", "labelHAlign", "labelVAlign"],
            "Trend Lines": ["startValue", "endValue", "displayValue", "color", "isTrendZone", "thickness", "alpha", "dashed", "dashLen", "dashGap", "valueOnRight", "toolText"]
        },
        StackedArea2D: {
            chart: [
                {
                    id: "Chart", group: true, sub: [
                        {id: "Titles (Axis) Names", prop: ["caption", "subCaption", "xAxisName", "yAxisName"]},
                        {
                            id: "Functional Attributes",
                            prop: ["numVDivLines", "vDivLineColor", "vDivLineThickness", "vDivLineAlpha", "vDivLineDashed", "vDivLineDashLen", "vDivLineDashGap", "showAlternateVGridColor", "alternateVGridColor", "alternateVGridAlpha", "numDivLines", "divLineColor", "divLineThickness", "divLineAlpha", "divLineDashed", "divLineDashLen", "divLineDashGap", "zeroPlaneColor", "zeroPlaneThickness", "zeroPlaneAlpha", "showZeroPlaneValue", "showAlternateHGridColor", "alternateHGridColor", "alternateHGridAlpha"]
                        },
                        {
                            id: "Chart Caption Cosmetics",
                            prop: ["captionAlignment", "captionOnTop", "captionFontSize", "subCaptionFontSize", "captionFont", "subCaptionFont", "captionFontColor", "subCaptionFontColor", "captionFontBold", "subCaptionFontBold", "alignCaptionWithCanvas", "captionHorizontalPadding"]
                        },
                        {
                            id: "X-axis Name Cosmetics",
                            prop: ["xAxisNameFontColor", "xAxisNameFontSize", "xAxisNameFontBold", "xAxisNameFontItalic", "xAxisNameBgColor", "xAxisNameBorderColor", "xAxisNameAlpha", "xAxisNameFontAlpha", "xAxisNameBgAlpha", "xAxisNameBorderAlpha", "xAxisNameBorderPadding", "xAxisNameBorderRadius", "xAxisNameBorderThickness", "xAxisNameBorderDashed", "xAxisNameBorderDashLen", "xAxisNameBorderDashGap"]
                        },
                        {
                            id: "Y-axis Name Cosmetics",
                            prop: ["yAxisNameFont", "yAxisNameFontColor", "yAxisNameFontSize", "yAxisNameFontBold", "yAxisNameFontItalic", "yAxisNameBgColor", "yAxisNameBorderColor", "yAxisNameAlpha", "yAxisNameFontAlpha", "yAxisNameBgAlpha", "yAxisNameBorderAlpha", "yAxisNameBorderPadding", "yAxisNameBorderRadius", "yAxisNameBorderThickness", "yAxisNameBorderDashed", "yAxisNameBorderDashLen", "yAxisNameBorderDashGap"]
                        },
                        {
                            id: "Charts Cosmetics",
                            prop: ["showBorder", "borderColor", "borderThickness", "borderAlpha", "bgColor", "bgAlpha", "bgRatio", "bgAngle", "bgImage", "bgImageAlpha", "bgImageDisplayMode", "bgImageVAlign", "bgImageHAlign", "bgImageScale", "xAxisLineColor", "canvasBgColor", "canvasBgAlpha", "canvasBgRatio", "canvasBgAngle", "canvasBorderColor", "canvasBorderThickness", "canvasBorderAlpha", "showVLineLabelBorder", "logoURL", "logoPosition", "logoAlpha", "logoScale", "logoLink"]
                        },
                        {
                            id: "Anchors",
                            prop: ["drawAnchors", "anchorSides", "anchorRadius", "anchorBorderColor", "anchorBorderThickness", "anchorBgColor", "anchorAlpha", "anchorBgAlpha"]
                        },
                        {
                            id: "Legend Properties",
                            prop: ["showLegend", "legendItemFontBold", "legendItemFont", "legendItemFontSize", "legendItemFontColor", "legendItemHoverFontColor", "legendCaptionAlignment", "legendCaptionBold", "legendCaptionFont", "legendCaptionFontSize", "legendIconScale", "legendItemHiddenColor", "legendPosition", "legendBgColor", "legendBgAlpha", "legendBorderColor", "legendBorderThickness", "legendBorderAlpha", "legendShadow", "legendAllowDrag", "legendScrollBgColor", "reverseLegend", "interactiveLegend", "legendNumColumns", "minimiseWrappingInLegend"]
                        },
                        {
                            id: "Divisional Lines/Grids",
                            prop: ["numDivLines", "divLineColor", "divLineThickness", "divLineAlpha", "divLineDashed", "divLineDashLen", "divLineDashGap", "zeroPlaneColor", "zeroPlaneThickness", "zeroPlaneAlpha", "showZeroPlaneValue", "showAlternateHGridColor", "alternateHGridColor", "alternateHGridAlpha", "numVDivLines", "vDivLineColor", "vDivLineThickness", "vDivLineAlpha", "vDivLineDashed", "vDivLineDashLen", "vDivLineDashGap", "showAlternateVGridColor", "alternateVGridColor", "alternateVGridAlpha"]
                        },
                        {
                            id: "Tool-tip",
                            prop: ["showToolTip", "toolTipColor", "toolTipBgColor", "toolTipBorderColor", "toolTipSepChar", "seriesNameInToolTip", "showToolTipShadow"]
                        },
                        {
                            id: "Paddings and Margins",
                            prop: ["captionPadding", "xAxisNamePadding", "yAxisNamePadding", "yAxisValuesPadding", "labelPadding", "valuePadding", "plotSpacePercent", "chartLeftMargin", "chartRightMargin", "chartTopMargin", "chartBottomMargin", "legendPadding", "canvasPadding", "canvasLeftMargin", "canvasRightMargin", "canvasTopMargin", "canvasBottomMargin"]
                        }
                    ]
                },
                {
                    id: "Data", group: true, sub: [
                        {
                            id: "Data Plot Cosmetics",
                            prop: ["showPlotBorder", "plotBorderColor", "plotBorderThickness", "plotBorderAlpha", "plotBorderDashed", "plotBorderDashLen", "plotBorderDashGap", "plotFillAngle", "plotFillAlpha", "plotGradientColor", "plotFillColor"]
                        },
                        {
                            id: "Data Value Cosmetics",
                            prop: ["valueFont", "valueFontColor", "valueFontSize", "valueFontBold", "valueFontItalic", "valueBgColor", "valueBorderColor", "valueAlpha", "valueFontAlpha", "valueBgAlpha", "valueBorderAlpha", "valueBorderThickness", "valueBorderRadius", "valueBorderDashed", "valueBorderDashGap", "valueBorderDashLen", "valueHoverAlpha", "valueFontHoverAlpha", "valueBgHoverAlpha", "valueBorderHoverAlpha", "showValuesOnHover"]
                        },
                        {
                            id: "Data Label Cosmetics",
                            prop: ["labelFont", "labelFontColor", "labelFontSize", "labelFontBold", "labelFontItalic", "labelBgColor", "labelBorderColor", "labelAlpha", "labelBgAlpha", "labelBorderAlpha", "labelBorderPadding", "labelBorderRadius", "labelBorderThickness", "labelBorderDashed", "labelBorderDashLen", "labelBorderDashGap", "labelLink"]
                        },
                        {
                            id: "Data Plot Hover Effects",
                            prop: ["showHoverEffect", "plotHoverEffect", "plotFillHoverColor", "plotFillHoverAlpha"]
                        }
                    ]
                },
                {
                    id: "Others", group: true, sub: [
                        {
                            id: "Number Formatting",
                            prop: ["formatNumber", "formatNumberScale", "defaultNumberScale", "numberScaleUnit", "numberScaleValue", "scaleRecursively", "maxScaleRecursion", "scaleSeparator", "numberPrefix", "numberSuffix", "decimalSeparator", "thousandSeparator", "thousandSeparatorPosition", "inDecimalSeparator", "inThousandSeparator", "decimals", "forceDecimals", "forceYAxisValueDecimals", "yAxisValueDecimals"]
                        },
                        {
                            id: "Font Properties",
                            prop: ["baseFont", "baseFontSize", "baseFontColor", "outCnvBaseFont", "outCnvBaseFontSize", "outCnvBaseFontColor"]
                        }
                    ]
                }
            ],
            "categories": ["font", "fontSize", "fontColor"],
            "category": ["label", "showLabel", "toolText", "font", "fontColor", "fontSize", "fontBold", "fontItalic", "bgColor", "borderColor", "alpha", "bgAlpha", "borderAlpha", "borderPadding", "borderRadius", "borderThickness", "borderDashed", "borderDashLen", "borderDashGap", "link"],
            "dataset": ["seriesName", "color", "alpha", "showValues", "dashed", "includeInLegend", "drawAnchors", "anchorSides", "anchorRadius", "anchorBorderColor", "anchorBorderThickness", "anchorBgColor", "anchorAlpha", "anchorBgAlpha", "showPlotBorder", "plotBorderColor", "plotBorderThickness", "plotBorderAlpha"],
            "set": ["value", "displayValue", "link", "toolText", "showValue", "anchorSides", "anchorRadius", "anchorBorderColor", "anchorBorderThickness", "anchorBgColor", "anchorAlpha", "anchorBgAlpha"],
            "Vertical data separator lines": ["linePosition", "color", "thickness", "showLabelBorder", "label", "labelPosition", "alpha", "dashed", "dashLen", "dashGap", "labelHAlign", "labelVAlign"],
            "Trend Lines": ["startValue", "endValue", "displayValue", "color", "isTrendZone", "showOnTop", "thickness", "alpha", "dashed", "dashLen", "dashGap", "valueOnRight", "toolText"]
        },
        StackedBar2D: {
            chart: [
                {
                    id: "Chart", group: true, sub: [
                        {id: "Titles (Axis) Names", prop: ["caption", "subCaption", "xAxisName", "yAxisName"]},
                        {
                            id: "Functional Attributes",
                            prop: ["animation", "palette", "paletteColors", "showLabels", "labelStep", "maxLabelWidthPercent", "useEllipsesWhenOverflow", "showValues", "showYAxisValues", "showLimits", "showDivLineValues", "yAxisValuesStep", "showShadow", "adjustDiv", "clickURL", "rotateXAxisName", "xAxisNameWidth", "showSum", "yAxisMinValue", "yAxisMaxValue", "stack100Percent", "showPercentValues", "showPercentInToolTip", "centerXaxisName"]
                        },
                        {
                            id: "Chart Caption Cosmetics",
                            prop: ["captionAlignment", "captionOnTop", "captionFontSize", "subCaptionFontSize", "captionFont", "subCaptionFont", "captionFontColor", "subCaptionFontColor", "captionFontBold", "subCaptionFontBold", "alignCaptionWithCanvas", "captionHorizontalPadding"]
                        },
                        {
                            id: "X-axis Name Cosmetics",
                            prop: ["xAxisNameFontColor", "xAxisNameFontSize", "xAxisNameFontBold", "xAxisNameFontItalic", "xAxisNameBgColor", "xAxisNameBorderColor", "xAxisNameAlpha", "xAxisNameFontAlpha", "xAxisNameBgAlpha", "xAxisNameBorderAlpha", "xAxisNameBorderPadding", "xAxisNameBorderRadius", "xAxisNameBorderThickness", "xAxisNameBorderDashed", "xAxisNameBorderDashLen", "xAxisNameBorderDashGap"]
                        },
                        {
                            id: "Y-axis Name Cosmetics",
                            prop: ["yAxisNameFont", "yAxisNameFontColor", "yAxisNameFontSize", "yAxisNameFontBold", "yAxisNameFontItalic", "yAxisNameBgColor", "yAxisNameBorderColor", "yAxisNameAlpha", "yAxisNameFontAlpha", "yAxisNameBgAlpha", "yAxisNameBorderAlpha", "yAxisNameBorderPadding", "yAxisNameBorderRadius", "yAxisNameBorderThickness", "yAxisNameBorderDashed", "yAxisNameBorderDashLen", "yAxisNameBorderDashGap"]
                        },
                        {
                            id: "Charts Cosmetics",
                            prop: ["showBorder", "borderColor", "borderThickness", "borderAlpha", "bgColor", "bgAlpha", "bgRatio", "bgAngle", "bgImage", "bgImageAlpha", "bgImageDisplayMode", "bgImageVAlign", "bgImageHAlign", "bgImageScale", "xAxisLineColor", "canvasBgColor", "canvasBgAlpha", "canvasBgRatio", "canvasBgAngle", "canvasBorderColor", "canvasBorderThickness", "canvasBorderAlpha", "showVLineLabelBorder", "logoURL", "logoPosition", "logoAlpha", "logoScale", "logoLink"]
                        },
                        {
                            id: "Legend Properties",
                            prop: ["showLegend", "legendItemFontBold", "legendItemFont", "legendItemFontSize", "legendItemFontColor", "legendItemHoverFontColor", "legendCaptionAlignment", "legendCaptionBold", "legendCaptionFont", "legendCaptionFontSize", "legendIconScale", "legendItemHiddenColor", "legendPosition", "legendBgColor", "legendBgAlpha", "legendBorderColor", "legendBorderThickness", "legendBorderAlpha", "legendShadow", "legendAllowDrag", "legendScrollBgColor", "reverseLegend", "interactiveLegend", "legendNumColumns", "minimiseWrappingInLegend"]
                        },
                        {
                            id: "Divisional Lines/Grids",
                            prop: ["numDivLines", "divLineColor", "divLineThickness", "divLineAlpha", "divLineDashed", "divLineDashLen", "divLineDashGap", "zeroPlaneColor", "zeroPlaneThickness", "zeroPlaneAlpha", "showZeroPlaneValue", "showAlternateVGridColor", "alternateVGridColor", "alternateVGridAlpha"]
                        },
                        {
                            id: "Tool-tip",
                            prop: ["showToolTip", "toolTipColor", "toolTipBgColor", "toolTipBorderColor", "toolTipSepChar", "seriesNameInToolTip", "showToolTipShadow"]
                        },
                        {
                            id: "Paddings and Margins",
                            prop: ["xAxisNamePadding", "yAxisNamePadding", "yAxisValuesPadding", "labelPadding", "plotSpacePercent", "chartLeftMargin", "chartRightMargin", "chartTopMargin", "chartBottomMargin", "legendPadding", "captionPadding", "canvasLeftMargin", "canvasRightMargin", "canvasTopMargin", "canvasBottomMargin"]
                        }
                    ]
                },
                {
                    id: "Data", group: true, sub: [
                        {
                            id: "Data Plot Cosmetics",
                            prop: ["useRoundEdges", "showPlotBorder", "plotBorderColor", "plotBorderThickness", "plotBorderAlpha", "plotBorderDashed", "plotBorderDashLen", "plotBorderDashGap", "plotFillAngle", "plotFillRatio", "plotFillAlpha", "plotGradientColor"]
                        },
                        {
                            id: "Data Value Cosmetics",
                            prop: ["valueFont", "valueFontColor", "valueFontSize", "valueFontBold", "valueFontItalic", "valueBgColor", "valueBorderColor", "valueAlpha", "valueFontAlpha", "valueBgAlpha", "valueBorderAlpha", "valueBorderThickness", "valueBorderRadius", "valueBorderDashed", "valueBorderDashGap", "valueBorderDashLen", "valueHoverAlpha", "valueFontHoverAlpha", "valueBgHoverAlpha", "valueBorderHoverAlpha", "showValuesOnHover"]
                        },
                        {
                            id: "Data Label Cosmetics",
                            prop: ["labelFont", "labelFontColor", "labelFontSize", "labelFontBold", "labelFontItalic", "labelBgColor", "labelBorderColor", "labelAlpha", "labelBgAlpha", "labelBorderAlpha", "labelBorderPadding", "labelBorderRadius", "labelBorderThickness", "labelBorderDashed", "labelBorderDashLen", "labelBorderDashGap", "labelLink"]
                        },
                        {
                            id: "Data Plot Hover Effects",
                            prop: ["showHoverEffect", "plotHoverEffect", "plotFillHoverColor", "plotFillHoverAlpha"]
                        }
                    ]
                },
                {
                    id: "Others", group: true, sub: [
                        {
                            id: "Number Formatting",
                            prop: ["formatNumber", "formatNumberScale", "defaultNumberScale", "numberScaleUnit", "numberScaleValue", "scaleRecursively", "maxScaleRecursion", "scaleSeparator", "numberPrefix", "numberSuffix", "decimalSeparator", "thousandSeparator", "thousandSeparatorPosition", "inDecimalSeparator", "inThousandSeparator", "decimals", "forceDecimals", "forceYAxisValueDecimals", "yAxisValueDecimals"]
                        },
                        {
                            id: "Font Properties",
                            prop: ["baseFont", "baseFontSize", "baseFontColor", "outCnvBaseFont", "outCnvBaseFontSize", "outCnvBaseFontColor"]
                        }
                    ]
                }
            ],
            "categories": ["font", "fontSize", "fontColor"],
            "category": ["label", "showLabel", "toolText", "font", "fontColor", "fontSize", "fontBold", "fontItalic", "bgColor", "borderColor", "alpha", "bgAlpha", "borderAlpha", "borderPadding", "borderRadius", "borderThickness", "borderDashed", "borderDashLen", "borderDashGap", "link"],
            "dataset": ["seriesName", "color", "alpha", "ratio", "showValues", "dashed", "includeInLegend"],
            "set": ["label", "value", "displayValue", "color", "link", "toolText", "showLabel", "showValue", "dashed", "alpha"],
            "Vertical data separator lines": ["linePosition", "color", "thickness", "showLabelBorder", "label", "labelPosition", "alpha", "dashed", "dashLen", "dashGap", "labelHAlign", "labelVAlign"],
            "Trend Lines": ["startValue", "endValue", "displayValue", "color", "isTrendZone", "showOnTop", "thickness", "alpha", "dashed", "dashLen", "dashGap", "valueOnRight", "toolText"]
        },
        StackedBar3D: {
            chart: [
                {
                    id: "Chart", group: true, sub: [
                        {id: "Titles (Axis) Names", prop: ["caption", "subCaption", "xAxisName", "yAxisName"]},
                        {
                            id: "Functional Attributes",
                            prop: ["animation", "palette", "paletteColors", "showLabels", "labelStep", "maxLabelWidthPercent", "useEllipsesWhenOverflow", "showValues", "showYAxisValues", "yAxisMinValue", "yAxisMaxValue", "maxBarHeight", "xAxisNameWidth", "clickURL", "yAxisValuesStep", "showShadow", "adjustDiv", "rotateXAxisName", "use3DLighting", "showSum", "stack100Percent", "showPercentValues", "showPercentInToolTip", "centerXaxisName"]
                        },
                        {
                            id: "Chart Caption Cosmetics",
                            prop: ["captionAlignment", "captionOnTop", "captionFontSize", "subCaptionFontSize", "captionFont", "subCaptionFont", "captionFontColor", "subCaptionFontColor", "captionFontBold", "subCaptionFontBold", "alignCaptionWithCanvas", "captionHorizontalPadding"]
                        },
                        {
                            id: "X-axis Name Cosmetics",
                            prop: ["xAxisNameFontColor", "xAxisNameFontSize", "xAxisNameFontBold", "xAxisNameFontItalic", "xAxisNameBgColor", "xAxisNameBorderColor", "xAxisNameAlpha", "xAxisNameFontAlpha", "xAxisNameBgAlpha", "xAxisNameBorderAlpha", "xAxisNameBorderPadding", "xAxisNameBorderRadius", "xAxisNameBorderThickness", "xAxisNameBorderDashed", "xAxisNameBorderDashLen", "xAxisNameBorderDashGap"]
                        },
                        {
                            id: "Y-axis Name Cosmetics",
                            prop: ["yAxisNameFont", "yAxisNameFontColor", "yAxisNameFontSize", "yAxisNameFontBold", "yAxisNameFontItalic", "yAxisNameBgColor", "yAxisNameBorderColor", "yAxisNameAlpha", "yAxisNameFontAlpha", "yAxisNameBgAlpha", "yAxisNameBorderAlpha", "yAxisNameBorderPadding", "yAxisNameBorderRadius", "yAxisNameBorderThickness", "yAxisNameBorderDashed", "yAxisNameBorderDashLen", "yAxisNameBorderDashGap"]
                        },
                        {
                            id: "Charts Cosmetics",
                            prop: ["showBorder", "borderColor", "borderThickness", "borderAlpha", "bgColor", "bgAlpha", "bgRatio", "bgAngle", "bgImage", "bgImageAlpha", "bgImageDisplayMode", "bgImageVAlign", "bgImageHAlign", "bgImageScale", "xAxisLineColor", "canvasBgColor", "canvasBgAlpha", "canvasBaseColor", "showCanvasBg", "showCanvasBase", "canvasBaseDepth", "canvasBgDepth", "showVLineLabelBorder", "logoURL", "logoPosition", "logoAlpha", "logoScale", "logoLink"]
                        },
                        {
                            id: "Legend Properties",
                            prop: ["showLegend", "legendItemFontBold", "legendItemFont", "legendItemFontSize", "legendItemFontColor", "legendItemHoverFontColor", "legendCaptionAlignment", "legendCaptionBold", "legendCaptionFont", "legendCaptionFontSize", "legendIconScale", "legendItemHiddenColor", "legendPosition", "legendBgColor", "legendBgAlpha", "legendBorderColor", "legendBorderThickness", "legendBorderAlpha", "legendShadow", "legendAllowDrag", "legendScrollBgColor", "reverseLegend", "interactiveLegend", "legendNumColumns", "minimiseWrappingInLegend"]
                        },
                        {
                            id: "Divisional Lines/Grids",
                            prop: ["numDivLines", "divLineColor", "divLineThickness", "divLineAlpha", "divLineDashed", "divLineDashLen", "divLineDashGap", "zeroPlaneColor", "zeroPlaneAlpha", "zeroPlaneShowBorder", "showZeroPlaneValue", "zeroPlaneBorderColor"]
                        },
                        {
                            id: "Tool-tip",
                            prop: ["showToolTip", "toolTipColor", "toolTipBgColor", "toolTipBorderColor", "toolTipSepChar", "seriesNameInToolTip", "showToolTipShadow"]
                        },
                        {
                            id: "Paddings and Margins",
                            prop: ["captionPadding", "xAxisNamePadding", "yAxisNamePadding", "yAxisValuesPadding", "labelPadding", "plotSpacePercent", "chartLeftMargin", "chartRightMargin", "chartTopMargin", "chartBottomMargin", "legendPadding", "canvasLeftMargin", "canvasRightMargin", "canvasTopMargin", "canvasBottomMargin"]
                        }
                    ]
                },
                {
                    id: "Data", group: true, sub: [
                        {
                            id: "Data Plot Cosmetics",
                            prop: ["showPlotBorder", "plotBorderColor", "plotBorderAlpha", "plotFillAlpha", "overlapBars"]
                        },
                        {
                            id: "Data Value Cosmetics",
                            prop: ["valueFont", "valueFontColor", "valueFontSize", "valueFontBold", "valueFontItalic", "valueBgColor", "valueBorderColor", "valueAlpha", "valueFontAlpha", "valueBgAlpha", "valueBorderAlpha", "valueBorderThickness", "valueBorderRadius", "valueBorderDashed", "valueBorderDashGap", "valueBorderDashLen", "valueHoverAlpha", "valueFontHoverAlpha", "valueBgHoverAlpha", "valueBorderHoverAlpha", "showValuesOnHover"]
                        },
                        {
                            id: "Data Label Cosmetics",
                            prop: ["labelFont", "labelFontColor", "labelFontSize", "labelFontBold", "labelFontItalic", "labelBgColor", "labelBorderColor", "labelAlpha", "labelBgAlpha", "labelBorderAlpha", "labelBorderPadding", "labelBorderRadius", "labelBorderThickness", "labelBorderDashed", "labelBorderDashLen", "labelBorderDashGap", "labelLink"]
                        },
                        {
                            id: "Data Plot Hover Effects",
                            prop: ["showHoverEffect", "plotHoverEffect", "plotFillHoverColor", "plotFillHoverAlpha"]
                        }
                    ]
                },
                {
                    id: "Others", group: true, sub: [
                        {
                            id: "Number Formatting",
                            prop: ["formatNumber", "formatNumberScale", "defaultNumberScale", "numberScaleUnit", "numberScaleValue", "scaleRecursively", "maxScaleRecursion", "scaleSeparator", "numberPrefix", "numberSuffix", "decimalSeparator", "thousandSeparator", "thousandSeparatorPosition", "inDecimalSeparator", "inThousandSeparator", "decimals", "forceDecimals", "forceYAxisValueDecimals", "yAxisValueDecimals"]
                        },
                        {
                            id: "Font Properties",
                            prop: ["baseFont", "baseFontSize", "baseFontColor", "outCnvBaseFont", "outCnvBaseFontSize", "outCnvBaseFontColor"]
                        }
                    ]
                }
            ],
            "categories": ["font", "fontSize", "fontColor"],
            "category": ["label", "showLabel", "toolText", "font", "fontColor", "fontSize", "fontBold", "fontItalic", "bgColor", "borderColor", "alpha", "bgAlpha", "borderAlpha", "borderPadding", "borderRadius", "borderThickness", "borderDashed", "borderDashLen", "borderDashGap", "link"],
            "dataset": ["seriesName", "color", "alpha", "ratio", "showValues", "dashed", "includeInLegend"],
            "set": ["label", "value", "displayValue", "color", "link", "toolText", "showLabel", "showValue", "alpha"],
            "Vertical data separator lines": ["linePosition", "color", "thickness", "showLabelBorder", "label", "labelPosition", "alpha", "dashed", "dashLen", "dashGap", "labelHAlign", "labelVAlign"],
            "Trend Lines": ["startValue", "endValue", "displayValue", "color", "isTrendZone", "thickness", "alpha", "dashed", "dashLen", "dashGap", "valueOnRight", "toolText"]
        },
        MSStackedColumn2D: {
            chart: [
                {
                    id: "Chart", group: true, sub: [
                        {id: "Titles (Axis) Names", prop: ["caption", "subCaption", "xAxisName", "yAxisName"]},
                        {
                            id: "Functional Attributes",
                            prop: ["animation", "palette", "paletteColors", "showLabels", "maxLabelHeight", "labelDisplay", "useEllipsesWhenOverflow", "rotateLabels", "slantLabels", "labelStep", "staggerLines", "showValues", "rotateValues", "showYAxisValues", "showLimits", "showDivLineValues", "yAxisValuesStep", "showShadow", "adjustDiv", "rotateYAxisName", "yAxisNameWidth", "clickURL", "showSum", "yAxisMinValue", "yAxisMaxValue", "stack100Percent", "showPercentValues", "showPercentInToolTip"]
                        },
                        {
                            id: "Chart Caption Cosmetics",
                            prop: ["captionAlignment", "captionOnTop", "captionFontSize", "subCaptionFontSize", "captionFont", "subCaptionFont", "captionFontColor", "subCaptionFontColor", "captionFontBold", "subCaptionFontBold", "alignCaptionWithCanvas", "captionHorizontalPadding"]
                        },
                        {
                            id: "X-axis Name Cosmetics",
                            prop: ["xAxisNameFontColor", "xAxisNameFontSize", "xAxisNameFontBold", "xAxisNameFontItalic", "xAxisNameBgColor", "xAxisNameBorderColor", "xAxisNameAlpha", "xAxisNameFontAlpha", "xAxisNameBgAlpha", "xAxisNameBorderAlpha", "xAxisNameBorderPadding", "xAxisNameBorderRadius", "xAxisNameBorderThickness", "xAxisNameBorderDashed", "xAxisNameBorderDashLen", "xAxisNameBorderDashGap"]
                        },
                        {
                            id: "Y-axis Name Cosmetics",
                            prop: ["yAxisNameFont", "yAxisNameFontColor", "yAxisNameFontSize", "yAxisNameFontBold", "yAxisNameFontItalic", "yAxisNameBgColor", "yAxisNameBorderColor", "yAxisNameAlpha", "yAxisNameFontAlpha", "yAxisNameBgAlpha", "yAxisNameBorderAlpha", "yAxisNameBorderPadding", "yAxisNameBorderRadius", "yAxisNameBorderThickness", "yAxisNameBorderDashed", "yAxisNameBorderDashLen", "yAxisNameBorderDashGap"]
                        },
                        {
                            id: "Charts Cosmetics",
                            prop: ["showBorder", "borderColor", "borderThickness", "borderAlpha", "bgColor", "bgAlpha", "bgRatio", "bgAngle", "bgImage", "bgImageAlpha", "bgImageDisplayMode", "bgImageVAlign", "bgImageHAlign", "bgImageScale", "xAxisLineColor", "canvasBgColor", "canvasBgAlpha", "canvasBgRatio", "canvasBgAngle", "canvasBorderColor", "canvasBorderThickness", "canvasBorderAlpha", "showVLineLabelBorder", "logoURL", "logoPosition", "logoAlpha", "logoScale", "logoLink"]
                        },
                        {
                            id: "Legend Properties",
                            prop: ["showLegend", "legendItemFontBold", "legendItemFont", "legendItemFontSize", "legendItemFontColor", "legendItemHoverFontColor", "legendCaptionAlignment", "legendCaptionBold", "legendCaptionFont", "legendCaptionFontSize", "legendIconScale", "legendItemHiddenColor", "legendPosition", "legendBgColor", "legendBgAlpha", "legendBorderColor", "legendBorderThickness", "legendBorderAlpha", "legendShadow", "legendAllowDrag", "legendScrollBgColor", "reverseLegend", "interactiveLegend", "legendNumColumns", "minimiseWrappingInLegend"]
                        },
                        {
                            id: "Divisional Lines/Grids",
                            prop: ["numDivLines", "divLineColor", "divLineThickness", "divLineAlpha", "divLineIsDashed", "divLineDashLen", "divLineDashGap", "zeroPlaneColor", "zeroPlaneThickness", "zeroPlaneAlpha", "showZeroPlaneValue", "showAlternateHGridColor", "alternateHGridColor", "alternateHGridAlpha"]
                        },
                        {
                            id: "Tool-tip",
                            prop: ["showToolTip", "toolTipColor", "toolTipBgColor", "toolTipBorderColor", "toolTipSepChar", "seriesNameInToolTip", "showToolTipShadow"]
                        },
                        {
                            id: "Paddings and Margins",
                            prop: ["captionPadding", "xAxisNamePadding", "yAxisNamePadding", "yAxisValuesPadding", "labelPadding", "plotSpacePercent", "chartLeftMargin", "chartRightMargin", "chartTopMargin", "chartBottomMargin", "legendPadding", "canvasLeftMargin", "canvasRightMargin", "canvasTopMargin", "canvasBottomMargin"]
                        }
                    ]
                },
                {
                    id: "Data", group: true, sub: [
                        {
                            id: "Data Plot Cosmetics",
                            prop: ["useRoundEdges", "showPlotBorder", "plotBorderColor", "plotBorderThickness", "plotBorderAlpha", "plotBorderDashed", "plotBorderDashLen", "plotBorderDashGap", "plotFillAngle", "plotFillRatio", "plotFillAlpha", "plotGradientColor"]
                        },
                        {
                            id: "Data Value Cosmetics",
                            prop: ["valueFont", "valueFontColor", "valueFontSize", "valueFontBold", "valueFontItalic", "valueBgColor", "valueBorderColor", "valueAlpha", "valueFontAlpha", "valueBgAlpha", "valueBorderAlpha", "valueBorderThickness", "valueBorderRadius", "valueBorderDashed", "valueBorderDashGap", "valueBorderDashLen", "valueHoverAlpha", "valueFontHoverAlpha", "valueBgHoverAlpha", "valueBorderHoverAlpha", "showValuesOnHover"]
                        },
                        {
                            id: "Data Label Cosmetics",
                            prop: ["labelFont", "labelFontColor", "labelFontSize", "labelFontBold", "labelFontItalic", "labelBgColor", "labelBorderColor", "labelAlpha", "labelBgAlpha", "labelBorderAlpha", "labelBorderPadding", "labelBorderRadius", "labelBorderThickness", "labelBorderDashed", "labelBorderDashLen", "labelBorderDashGap", "labelLink"]
                        },
                        {
                            id: "Data Plot Hover Effects",
                            prop: ["showHoverEffect", "plotHoverEffect", "plotFillHoverColor", "plotFillHoverAlpha"]
                        }
                    ]
                },
                {
                    id: "Others", group: true, sub: [
                        {
                            id: "Number Formatting",
                            prop: ["formatNumber", "formatNumberScale", "defaultNumberScale", "numberScaleUnit", "numberScaleValue", "scaleRecursively", "maxScaleRecursion", "scaleSeparator", "numberPrefix", "numberSuffix", "decimalSeparator", "thousandSeparator", "thousandSeparatorPosition", "inDecimalSeparator", "inThousandSeparator", "decimals", "forceDecimals", "forceYAxisValueDecimals", "yAxisValueDecimals"]
                        },
                        {
                            id: "Font Properties",
                            prop: ["baseFont", "baseFontSize", "baseFontColor", "outCnvBaseFont", "outCnvBaseFontSize", "outCnvBaseFontColor"]
                        }
                    ]
                }
            ],
            "categories": ["font", "fontSize", "fontColor"],
            "category": ["label", "showLabel", "toolText", "font", "fontColor", "fontSize", "fontBold", "fontItalic", "bgColor", "borderColor", "alpha", "bgAlpha", "borderAlpha", "borderPadding", "borderRadius", "borderThickness", "borderDashed", "borderDashLen", "borderDashGap", "link"],
            "dataset": ["seriesName", "color", "alpha", "ratio", "showValues", "dashed", "includeInLegend"],
            "set": ["label", "value", "displayValue", "color", "link", "toolText", "showLabel", "showValue", "dashed", "alpha"],
            "Vertical data separator lines": ["linePosition", "color", "thickness", "showLabelBorder", "label", "labelPosition", "alpha", "dashed", "dashLen", "dashGap", "labelHAlign", "labelVAlign"],
            "Trend Lines": ["startValue", "endValue", "displayValue", "color", "isTrendZone", "showOnTop", "thickness", "alpha", "dashed", "dashLen", "dashGap", "valueOnRight", "toolText"]
        },
        MSCombi2D: {
            chart: [
                {
                    id: "Chart", group: true, sub: [
                        {id: "Titles (Axis) Names", prop: ["caption", "subCaption", "xAxisName", "yAxisName"]},
                        {
                            id: "Functional Attributes",
                            prop: ["animation", "palette", "paletteColors", "connectNullData", "areaOverColumns", "showLabels", "maxLabelHeight", "labelDisplay", "useEllipsesWhenOverflow", "rotateLabels", "slantLabels", "labelStep", "staggerLines", "showValues", "valuePosition", "rotateValues", "placeValuesInside", "showYAxisValues", "showLimits", "showDivLineValues", "yAxisValuesStep", "showShadow", "adjustDiv", "rotateYAxisName", "yAxisNameWidth", "clickURL", "yAxisMinValue", "yAxisMaxValue", "setAdaptiveYMin"]
                        },
                        {
                            id: "Chart Caption Cosmetics",
                            prop: ["captionAlignment", "captionOnTop", "captionFontSize", "subCaptionFontSize", "captionFont", "subCaptionFont", "captionFontColor", "subCaptionFontColor", "captionFontBold", "subCaptionFontBold", "alignCaptionWithCanvas", "captionHorizontalPadding"]
                        },
                        {
                            id: "X-axis Name Cosmetics",
                            prop: ["xAxisNameFontColor", "xAxisNameFontSize", "xAxisNameFontBold", "xAxisNameFontItalic", "xAxisNameBgColor", "xAxisNameBorderColor", "xAxisNameAlpha", "xAxisNameFontAlpha", "xAxisNameBgAlpha", "xAxisNameBorderAlpha", "xAxisNameBorderPadding", "xAxisNameBorderRadius", "xAxisNameBorderThickness", "xAxisNameBorderDashed", "xAxisNameBorderDashLen", "xAxisNameBorderDashGap"]
                        },
                        {
                            id: "Y-axis Name Cosmetics",
                            prop: ["yAxisNameFont", "yAxisNameFontColor", "yAxisNameFontSize", "yAxisNameFontBold", "yAxisNameFontItalic", "yAxisNameBgColor", "yAxisNameBorderColor", "yAxisNameAlpha", "yAxisNameFontAlpha", "yAxisNameBgAlpha", "yAxisNameBorderAlpha", "yAxisNameBorderPadding", "yAxisNameBorderRadius", "yAxisNameBorderThickness", "yAxisNameBorderDashed", "yAxisNameBorderDashLen", "yAxisNameBorderDashGap"]
                        },
                        {
                            id: "Charts Cosmetics",
                            prop: ["showBorder", "borderColor", "borderThickness", "borderAlpha", "bgColor", "bgAlpha", "bgRatio", "bgAngle", "bgImage", "bgImageAlpha", "bgImageDisplayMode", "bgImageVAlign", "bgImageHAlign", "bgImageScale", "xAxisLineColor", "canvasBgColor", "canvasBgAlpha", "canvasBgRatio", "canvasBgAngle", "canvasBorderColor", "canvasBorderThickness", "canvasBorderAlpha", "showVLineLabelBorder", "logoURL", "logoPosition", "logoAlpha", "logoScale", "logoLink"]
                        },
                        {
                            id: "Anchors",
                            prop: ["drawAnchors", "anchorSides", "anchorRadius", "anchorBorderColor", "anchorBorderThickness", "anchorBgColor", "anchorAlpha", "anchorBgAlpha"]
                        },
                        {
                            id: "Legend Properties",
                            prop: ["showLegend", "legendItemFontBold", "legendItemFont", "legendItemFontSize", "legendItemFontColor", "legendPosition", "legendCaptionAlignment", "legendCaptionBold", "legendCaptionFont", "legendCaptionFontSize", "legendCaptionFontColor", "legendItemHiddenColor", "legendIconScale", "legendBgColor", "legendBgAlpha", "legendBorderColor", "legendBorderThickness", "legendBorderAlpha", "legendShadow", "legendAllowDrag", "legendScrollBgColor", "reverseLegend", "interactiveLegend", "legendNumColumns", "minimiseWrappingInLegend"]
                        },
                        {
                            id: "Divisional Lines/Grids",
                            prop: ["numDivLines", "divLineColor", "divLineThickness", "divLineAlpha", "divLineDashed", "divLineDashLen", "divLineDashGap", "zeroPlaneColor", "zeroPlaneThickness", "zeroPlaneAlpha", "showZeroPlaneValue", "showAlternateHGridColor", "alternateHGridColor", "alternateHGridAlpha"]
                        },
                        {
                            id: "Tool-tip",
                            prop: ["showToolTip", "toolTipColor", "toolTipBgColor", "toolTipBorderColor", "toolTipSepChar", "seriesNameInToolTip", "showToolTipShadow"]
                        },
                        {
                            id: "Paddings and Margins",
                            prop: ["captionPadding", "xAxisNamePadding", "yAxisNamePadding", "yAxisValuesPadding", "labelPadding", "valuePadding", "plotSpacePercent", "chartLeftMargin", "chartRightMargin", "chartTopMargin", "chartBottomMargin", "legendPadding", "canvasLeftMargin", "canvasRightMargin", "canvasTopMargin", "canvasBottomMargin"]
                        }
                    ]
                },
                {
                    id: "Data", group: true, sub: [
                        {
                            id: "Data Plot Cosmetics",
                            prop: ["useRoundEdges", "showPlotBorder", "plotBorderColor", "plotBorderThickness", "plotBorderAlpha", "plotBorderDashed", "plotBorderDashLen", "plotBorderDashGap", "plotFillAngle", "plotFillAlpha", "plotGradientColor", "lineColor", "lineThickness", "lineAlpha", "lineDashed", "lineDashLen", "lineDashGap"]
                        },
                        {
                            id: "Data Value Cosmetics",
                            prop: ["valueFont", "valueFontColor", "valueFontSize", "valueFontBold", "valueFontItalic", "valueBgColor", "valueBorderColor", "valueAlpha", "valueFontAlpha", "valueBgAlpha", "valueBorderAlpha", "valueBorderThickness", "valueBorderRadius", "valueBorderDashed", "valueBorderDashGap", "valueBorderDashLen", "valueHoverAlpha", "valueFontHoverAlpha", "valueBgHoverAlpha", "valueBorderHoverAlpha", "showValuesOnHover"]
                        },
                        {
                            id: "Data Label Cosmetics",
                            prop: ["labelFont", "labelFontColor", "labelFontSize", "labelFontBold", "labelFontItalic", "labelBgColor", "labelBorderColor", "labelAlpha", "labelBgAlpha", "labelBorderAlpha", "labelBorderPadding", "labelBorderRadius", "labelBorderThickness", "labelBorderDashed", "labelBorderDashLen", "labelBorderDashGap", "labelLink"]
                        },
                        {
                            id: "Data Plot Hover Effects",
                            prop: ["showHoverEffect", "plotHoverEffect", "plotFillHoverColor", "plotFillHoverAlpha"]
                        }
                    ]
                },
                {
                    id: "Others", group: true, sub: [
                        {
                            id: "Number Formatting",
                            prop: ["formatNumber", "formatNumberScale", "defaultNumberScale", "numberScaleUnit", "numberScaleValue", "scaleRecursively", "maxScaleRecursion", "scaleSeparator", "numberPrefix", "numberSuffix", "decimalSeparator", "thousandSeparator", "thousandSeparatorPosition", "inDecimalSeparator", "inThousandSeparator", "decimals", "forceDecimals", "forceYAxisValueDecimals", "yAxisValueDecimals"]
                        },
                        {
                            id: "Font Properties",
                            prop: ["baseFont", "baseFontSize", "baseFontColor", "outCnvBaseFont", "outCnvBaseFontSize", "outCnvBaseFontColor"]
                        }
                    ]
                }
            ],
            "categories": ["font", "fontSize", "fontColor"],
            "category": ["label", "showLabel", "toolText", "font", "fontColor", "fontSize", "fontBold", "fontItalic", "bgColor", "borderColor", "alpha", "bgAlpha", "borderAlpha", "borderPadding", "borderRadius", "borderThickness", "borderDashed", "borderDashLen", "borderDashGap", "link"],
            "dataset": ["renderAs", "parentYAxis", "showPlotBorder", "plotBorderColor", "plotBorderThickness", "plotBorderAlpha", "seriesName", "color", "alpha", "showValues", "valuePosition", "dashed", "includeInLegend", "drawAnchors", "anchorSides", "anchorRadius", "anchorBorderColor", "anchorBorderThickness", "anchorBgColor", "anchorAlpha", "lineThickness", "lineDashLen", "lineDashGap"],
            "set": ["value", "displayValue", "color", "link", "toolText", "showValue", "valuePosition", "dashed", "alpha", "anchorSides", "anchorRadius", "anchorBorderColor", "anchorBorderThickness", "anchorBgColor", "anchorAlpha", "anchorBgAlpha"],
            "Vertical data separator lines": ["linePosition", "color", "thickness", "showLabelBorder", "label", "labelPosition", "alpha", "dashed", "dashLen", "dashGap", "labelHAlign", "labelVAlign"],
            "Trend Lines": ["startValue", "endValue", "displayValue", "color", "isTrendZone", "showOnTop", "thickness", "alpha", "dashed", "dashLen", "dashGap", "valueOnRight", "toolText"]
        },
        MSCombi3D: {
            chart: [
                {
                    id: "Chart", group: true, sub: [
                        {id: "Titles (Axis) Names", prop: ["caption", "subCaption", "xAxisName", "yAxisName"]},
                        {
                            id: "Functional Attributes",
                            prop: ["animation", "animate3D", "exeTime", "palette", "paletteColors", "connectNullData", "showLabels", "maxLabelHeight", "showValues", "labelStep", "yAxisValuesStep", "showYAxisValues", "showLimits", "showDivLineValues", "adjustDiv", "clickURL", "yAxisMinValue", "yAxisMaxValue", "setAdaptiveYMin", "xAxisTickColor", "xAxisTickAlpha", "xAxisTickThickness"]
                        },
                        {
                            id: "3D Functional Attributes",
                            prop: ["is2D", "clustered", "chartOrder", "chartOnTop", "autoScaling", "allowScaling"]
                        },
                        {
                            id: "3D Camera Light & Rotation",
                            prop: ["startAngX", "startAngY", "endAngX", "endAngY", "cameraAngX", "cameraAngY", "lightAngX", "lightAngY", "intensity", "dynamicShading", "bright2D", "allowRotation", "constrainVerticalRotation", "minVerticalRotAngle", "maxVerticalRotAngle", "constrainHorizontalRotation", "minHorizontalRotAngle", "maxHorizontalRotAngle"]
                        },
                        {id: "3D Plot Cosmetics", prop: ["showPlotBorder", "zDepth", "zGapPlot"]},
                        {id: "3D Canvas Walls Attributes", prop: ["yzWallDepth", "zxWallDepth", "xyWallDepth"]},
                        {
                            id: "Chart Caption Cosmetics",
                            prop: ["captionAlignment", "captionOnTop", "captionFontSize", "subCaptionFontSize", "captionFont", "subCaptionFont", "captionFontColor", "subCaptionFontColor", "captionFontBold", "subCaptionFontBold", "alignCaptionWithCanvas", "captionHorizontalPadding"]
                        },
                        {
                            id: "X-axis Name Cosmetics",
                            prop: ["xAxisNameFontColor", "xAxisNameFontSize", "xAxisNameFontBold", "xAxisNameFontItalic", "xAxisNameBgColor", "xAxisNameBorderColor", "xAxisNameAlpha", "xAxisNameFontAlpha", "xAxisNameBgAlpha", "xAxisNameBorderAlpha", "xAxisNameBorderPadding", "xAxisNameBorderRadius", "xAxisNameBorderThickness", "xAxisNameBorderDashed", "xAxisNameBorderDashLen", "xAxisNameBorderDashGap"]
                        },
                        {
                            id: "Y-axis Name Cosmetics",
                            prop: ["yAxisNameFont", "yAxisNameFontColor", "yAxisNameFontSize", "yAxisNameFontBold", "yAxisNameFontItalic", "yAxisNameBgColor", "yAxisNameBorderColor", "yAxisNameAlpha", "yAxisNameFontAlpha", "yAxisNameBgAlpha", "yAxisNameBorderAlpha", "yAxisNameBorderPadding", "yAxisNameBorderRadius", "yAxisNameBorderThickness", "yAxisNameBorderDashed", "yAxisNameBorderDashLen", "yAxisNameBorderDashGap"]
                        },
                        {
                            id: "Charts Cosmetics",
                            prop: ["showBorder", "borderColor", "borderThickness", "borderAlpha", "bgColor", "bgAlpha", "bgRatio", "bgAngle", "bgImage", "bgImageAlpha", "bgImageDisplayMode", "bgImageVAlign", "bgImageHAlign", "bgImageScale", "xAxisLineColor", "canvasBgColor", "showVLineLabelBorder", "logoURL", "logoPosition", "logoAlpha", "logoScale", "logoLink", "xAxisTickColor", "xAxisTickAlpha", "xAxisTickThickness"]
                        },
                        {
                            id: "Legend Properties",
                            prop: ["showLegend", "legendItemFontBold", "legendItemFont", "legendItemFontSize", "legendItemFontColor", "legendItemHoverFontColor", "legendCaptionAlignment", "legendCaptionBold", "legendCaptionFont", "legendCaptionFontSize", "legendPosition", "legendItemHiddenColor", "legendCaption", "legendIconScale", "legendBgColor", "legendBgAlpha", "legendBorderColor", "legendBorderThickness", "legendBorderAlpha", "legendShadow", "legendAllowDrag", "legendScrollBgColor", "legendNumColumns", "minimiseWrappingInLegend"]
                        },
                        {
                            id: "Divisional Lines/Grids",
                            prop: ["numDivLines", "divLineEffect", "divLineColor", "divLineThickness", "divLineAlpha", "zeroPlaneMesh", "zeroPlaneColor", "zeroPlaneAlpha", "showZeroPlaneValue", "showAlternateHGridColor", "alternateHGridColor"]
                        },
                        {
                            id: "Tool-tip",
                            prop: ["showToolTip", "toolTipColor", "toolTipBgColor", "toolTipBorderColor", "toolTipSepChar", "seriesNameInToolTip", "showToolTipShadow"]
                        },
                        {
                            id: "Paddings and Margins",
                            prop: ["captionPadding", "xAxisNamePadding", "yAxisNamePadding", "yAxisValuesPadding", "labelPadding", "valuePadding", "plotSpacePercent", "chartLeftMargin", "chartRightMargin", "chartTopMargin", "chartBottomMargin", "legendPadding", "canvasLeftMargin", "canvasRightMargin", "canvasTopMargin", "canvasBottomMargin"]
                        }
                    ]
                },
                {
                    id: "Data", group: true, sub: [
                        {
                            id: "Data Plot Cosmetics",
                            prop: ["useRoundEdges", "showPlotBorder", "plotBorderColor", "plotBorderThickness", "plotBorderAlpha", "plotBorderDashed", "plotBorderDashLen", "plotBorderDashGap", "plotFillAngle", "plotFillRatio", "plotFillAlpha", "plotGradientColor"]
                        },
                        {
                            id: "Data Value Cosmetics",
                            prop: ["valueFont", "valueFontColor", "valueFontSize", "valueFontBold", "valueFontItalic", "valueBgColor", "valueBorderColor", "valueAlpha", "valueFontAlpha", "valueBgAlpha", "valueBorderAlpha", "valueBorderThickness", "valueBorderRadius", "valueBorderDashed", "valueBorderDashGap", "valueBorderDashLen", "valueHoverAlpha", "valueFontHoverAlpha", "valueBgHoverAlpha", "valueBorderHoverAlpha", "showValuesOnHover"]
                        },
                        {
                            id: "Data Label Cosmetics",
                            prop: ["labelFont", "labelFontColor", "labelFontSize", "labelFontBold", "labelFontItalic", "labelBgColor", "labelBorderColor", "labelAlpha", "labelBgAlpha", "labelBorderAlpha", "labelBorderPadding", "labelBorderRadius", "labelBorderThickness", "labelBorderDashed", "labelBorderDashLen", "labelBorderDashGap", "labelLink"]
                        },
                        {
                            id: "Data Plot Hover Effects",
                            prop: ["showHoverEffect", "plotHoverEffect", "plotFillHoverColor", "plotFillHoverAlpha"]
                        }
                    ]
                },
                {
                    id: "Others", group: true, sub: [
                        {
                            id: "Number Formatting",
                            prop: ["formatNumber", "formatNumberScale", "defaultNumberScale", "numberScaleUnit", "numberScaleValue", "scaleRecursively", "maxScaleRecursion", "scaleSeparator", "numberPrefix", "numberSuffix", "decimalSeparator", "thousandSeparator", "thousandSeparatorPosition", "inDecimalSeparator", "inThousandSeparator", "decimals", "forceDecimals", "forceYAxisValueDecimals", "yAxisValueDecimals"]
                        },
                        {
                            id: "Font Properties",
                            prop: ["baseFont", "baseFontSize", "baseFontColor", "outCnvBaseFont", "outCnvBaseFontSize", "outCnvBaseFontColor"]
                        }
                    ]
                }
            ],
            "categories": ["font", "fontSize", "fontColor"],
            "category": ["label", "showLabel", "toolText", "font", "fontColor", "fontSize", "fontBold", "fontItalic", "bgColor", "borderColor", "alpha", "bgAlpha", "borderAlpha", "borderPadding", "borderRadius", "borderThickness", "borderDashed", "borderDashLen", "borderDashGap", "link"],
            "dataset": ["renderAs", "seriesName", "color", "showValues", "includeInLegend"],
            "set": ["value", "displayValue", "link", "toolText"],
            "Vertical data separator lines": ["linePosition", "color", "thickness", "showLabelBorder", "label", "labelPosition", "alpha", "dashed", "dashLen", "dashGap", "labelHAlign", "labelVAlign"],
            "Trend Lines": ["startValue", "endValue", "displayValue", "color", "isTrendZone", "showOnTop", "thickness", "alpha", "dashed", "dashLen", "dashGap", "valueOnRight", "toolText"]
        },
        MSColumnLine3D: {
            chart: [
                {
                    id: "Chart", group: true, sub: [
                        {id: "Titles (Axis) Names", prop: ["caption", "subCaption", "xAxisName", "yAxisName"]},
                        {
                            id: "Functional Attributes",
                            prop: ["animation", "palette", "paletteColors", "connectNullData", "showLabels", "maxLabelHeight", "labelDisplay", "useEllipsesWhenOverflow", "rotateLabels", "slantLabels", "labelStep", "staggerLines", "showValues", "valuePosition", "rotateValues", "placeValuesInside", "showYAxisValues", "showLimits", "showDivLineValues", "yAxisValuesStep", "showShadow", "adjustDiv", "rotateYAxisName", "yAxisNameWidth", "clickURL", "maxColWidth", "use3DLighting", "yAxisMinValue", "yAxisMaxValue", "setAdaptiveYMin", "use3DLineShift"]
                        },
                        {
                            id: "Chart Caption Cosmetics",
                            prop: ["captionAlignment", "captionOnTop", "captionFontSize", "subCaptionFontSize", "captionFont", "subCaptionFont", "captionFontColor", "subCaptionFontColor", "captionFontBold", "subCaptionFontBold", "alignCaptionWithCanvas", "captionHorizontalPadding"]
                        },
                        {
                            id: "X-axis Name Cosmetics",
                            prop: ["xAxisNameFontColor", "xAxisNameFontSize", "xAxisNameFontBold", "xAxisNameFontItalic", "xAxisNameBgColor", "xAxisNameBorderColor", "xAxisNameAlpha", "xAxisNameFontAlpha", "xAxisNameBgAlpha", "xAxisNameBorderAlpha", "xAxisNameBorderPadding", "xAxisNameBorderRadius", "xAxisNameBorderThickness", "xAxisNameBorderDashed", "xAxisNameBorderDashLen", "xAxisNameBorderDashGap"]
                        },
                        {
                            id: "Y-axis Name Cosmetics",
                            prop: ["yAxisNameFont", "yAxisNameFontColor", "yAxisNameFontSize", "yAxisNameFontBold", "yAxisNameFontItalic", "yAxisNameBgColor", "yAxisNameBorderColor", "yAxisNameAlpha", "yAxisNameFontAlpha", "yAxisNameBgAlpha", "yAxisNameBorderAlpha", "yAxisNameBorderPadding", "yAxisNameBorderRadius", "yAxisNameBorderThickness", "yAxisNameBorderDashed", "yAxisNameBorderDashLen", "yAxisNameBorderDashGap"]
                        },
                        {
                            id: "Charts Cosmetics",
                            prop: ["showBorder", "borderColor", "borderThickness", "borderAlpha", "bgColor", "bgAlpha", "bgRatio", "bgAngle", "bgImage", "bgImageAlpha", "bgImageDisplayMode", "bgImageVAlign", "bgImageHAlign", "bgImageScale", "xAxisLineColor", "canvasBgColor", "canvasBgAlpha", "canvasBaseColor", "showCanvasBg", "showCanvasBase", "canvasBaseDepth", "canvasBgDepth", "showVLineLabelBorder", "logoURL", "logoPosition", "logoAlpha", "logoScale", "logoLink"]
                        },
                        {
                            id: "Anchors",
                            prop: ["drawAnchors", "anchorSides", "anchorRadius", "anchorBorderColor", "anchorBorderThickness", "anchorBgColor", "anchorAlpha", "anchorBgAlpha"]
                        },
                        {
                            id: "Legend Properties",
                            prop: ["showLegend", "legendItemFontBold", "legendItemFont", "legendItemFontSize", "legendItemFontColor", "legendItemHoverFontColor", "legendCaptionAlignment", "legendCaptionBold", "legendCaptionFont", "legendCaptionFontSize", "legendIconScale", "legendItemHiddenColor", "legendPosition", "legendBgColor", "legendBgAlpha", "legendBorderColor", "legendBorderThickness", "legendBorderAlpha", "legendShadow", "legendAllowDrag", "legendScrollBgColor", "reverseLegend", "interactiveLegend", "legendNumColumns", "minimiseWrappingInLegend"]
                        },
                        {
                            id: "Divisional Lines/Grids",
                            prop: ["numDivLines", "divLineColor", "divLineThickness", "divLineAlpha", "divLineDashed", "divLineDashLen", "divLineDashGap", "zeroPlaneColor", "zeroPlaneAlpha", "zeroPlaneShowBorder", "zeroPlaneBorderColor", "showZeroPlaneValue"]
                        },
                        {
                            id: "Tool-tip",
                            prop: ["showToolTip", "toolTipColor", "toolTipBgColor", "toolTipBorderColor", "toolTipSepChar", "seriesNameInToolTip", "showToolTipShadow"]
                        },
                        {
                            id: "Paddings and Margins",
                            prop: ["captionPadding", "xAxisNamePadding", "yAxisNamePadding", "yAxisValuesPadding", "labelPadding", "valuePadding", "plotSpacePercent", "chartLeftMargin", "chartRightMargin", "chartTopMargin", "chartBottomMargin", "legendPadding", "canvasLeftMargin", "canvasRightMargin", "canvasTopMargin", "canvasBottomMargin"]
                        }
                    ]
                },
                {
                    id: "Data", group: true, sub: [
                        {
                            id: "Data Plot Cosmetics",
                            prop: ["overlapColumns", "showPlotBorder", "plotBorderColor", "plotBorderAlpha", "plotFillAlpha", "lineColor", "lineThickness", "lineAlpha", "lineDashed", "lineDashLen", "lineDashGap"]
                        },
                        {
                            id: "Data Value Cosmetics",
                            prop: ["valueFont", "valueFontColor", "valueFontSize", "valueFontBold", "valueFontItalic", "valueBgColor", "valueBorderColor", "valueAlpha", "valueFontAlpha", "valueBgAlpha", "valueBorderAlpha", "valueBorderThickness", "valueBorderRadius", "valueBorderDashed", "valueBorderDashGap", "valueBorderDashLen", "valueHoverAlpha", "valueFontHoverAlpha", "valueBgHoverAlpha", "valueBorderHoverAlpha", "showValuesOnHover"]
                        },
                        {
                            id: "Data Label Cosmetics",
                            prop: ["labelFont", "labelFontColor", "labelFontSize", "labelFontBold", "labelFontItalic", "labelBgColor", "labelBorderColor", "labelAlpha", "labelBgAlpha", "labelBorderAlpha", "labelBorderPadding", "labelBorderRadius", "labelBorderThickness", "labelBorderDashed", "labelBorderDashLen", "labelBorderDashGap", "labelLink"]
                        },
                        {
                            id: "Data Plot Hover Effects",
                            prop: ["showHoverEffect", "plotHoverEffect", "plotFillHoverColor", "plotFillHoverAlpha"]
                        }
                    ]
                },
                {
                    id: "Others", group: true, sub: [
                        {
                            id: "Number Formatting",
                            prop: ["formatNumber", "formatNumberScale", "defaultNumberScale", "numberScaleUnit", "numberScaleValue", "scaleRecursively", "maxScaleRecursion", "scaleSeparator", "numberPrefix", "numberSuffix", "decimalSeparator", "thousandSeparator", "thousandSeparatorPosition", "inDecimalSeparator", "inThousandSeparator", "decimals", "forceDecimals", "forceYAxisValueDecimals", "yAxisValueDecimals"]
                        },
                        {
                            id: "Font Properties",
                            prop: ["baseFont", "baseFontSize", "baseFontColor", "outCnvBaseFont", "outCnvBaseFontSize", "outCnvBaseFontColor"]
                        }
                    ]
                }
            ],
            "categories": ["font", "fontSize", "fontColor"],
            "category": ["label", "showLabel", "toolText", "font", "fontColor", "fontSize", "fontBold", "fontItalic", "bgColor", "borderColor", "alpha", "bgAlpha", "borderAlpha", "borderPadding", "borderRadius", "borderThickness", "borderDashed", "borderDashLen", "borderDashGap", "link"],
            "dataset": ["renderAs", "parentYAxis", "seriesName", "color", "alpha", "showValues", "valuePosition", "includeInLegend", "drawAnchors", "anchorSides", "anchorRadius", "anchorBorderColor", "anchorBorderThickness", "anchorBgColor", "anchorAlpha", "anchorBgAlpha", "lineThickness", "dashed", "lineDashLen", "lineDashGap"],
            "set": ["value", "displayValue", "color", "link", "toolText", "showValue", "valuePosition", "alpha", "dashed", "anchorSides", "anchorRadius", "anchorBorderColor", "anchorBorderThickness", "anchorBgColor", "anchorAlpha", "anchorBgAlpha"],
            "Vertical data separator lines": ["color", "thickness", "alpha", "dashed", "dashLen", "dashGap", "label", "showLabelBorder", "linePosition", "labelPosition", "labelHAlign", "labelVAlign"],
            "Trend Lines": ["startValue", "endValue", "displayValue", "color", "isTrendZone", "thickness", "alpha", "dashed", "dashLen", "dashGap", "valueOnRight", "toolText"]
        },
        StackedColumn2DLine: {
            chart: [
                {
                    id: "Chart", group: true, sub: [
                        {id: "Titles (Axis) Names", prop: ["caption", "subCaption", "xAxisName", "yAxisName"]},
                        {
                            id: "Functional Attributes",
                            prop: ["animation", "palette", "paletteColors", "showLabels", "maxLabelHeight", "labelDisplay", "useEllipsesWhenOverflow", "rotateLabels", "slantLabels", "labelStep", "staggerLines", "connectNullData", "showValues", "valuePosition", "rotateValues", "showYAxisValues", "showLimits", "showDivLineValues", "yAxisValuesStep", "showShadow", "adjustDiv", "rotateYAxisName", "yAxisNameWidth", "clickURL", "showSum", "yAxisMinValue", "yAxisMaxValue"]
                        },
                        {
                            id: "Chart Caption Cosmetics",
                            prop: ["captionAlignment", "captionOnTop", "captionFontSize", "subCaptionFontSize", "captionFont", "subCaptionFont", "captionFontColor", "subCaptionFontColor", "captionFontBold", "subCaptionFontBold", "alignCaptionWithCanvas", "captionHorizontalPadding"]
                        },
                        {
                            id: "X-axis Name Cosmetics",
                            prop: ["xAxisNameFontColor", "xAxisNameFontSize", "xAxisNameFontBold", "xAxisNameFontItalic", "xAxisNameBgColor", "xAxisNameBorderColor", "xAxisNameAlpha", "xAxisNameFontAlpha", "xAxisNameBgAlpha", "xAxisNameBorderAlpha", "xAxisNameBorderPadding", "xAxisNameBorderRadius", "xAxisNameBorderThickness", "xAxisNameBorderDashed", "xAxisNameBorderDashLen", "xAxisNameBorderDashGap"]
                        },
                        {
                            id: "Y-axis Name Cosmetics",
                            prop: ["yAxisNameFont", "yAxisNameFontColor", "yAxisNameFontSize", "yAxisNameFontBold", "yAxisNameFontItalic", "yAxisNameBgColor", "yAxisNameBorderColor", "yAxisNameAlpha", "yAxisNameFontAlpha", "yAxisNameBgAlpha", "yAxisNameBorderAlpha", "yAxisNameBorderPadding", "yAxisNameBorderRadius", "yAxisNameBorderThickness", "yAxisNameBorderDashed", "yAxisNameBorderDashLen", "yAxisNameBorderDashGap"]
                        },
                        {
                            id: "Charts Cosmetics",
                            prop: ["showBorder", "borderColor", "borderThickness", "borderAlpha", "bgColor", "bgAlpha", "bgRatio", "bgAngle", "bgImage", "bgImageAlpha", "bgImageDisplayMode", "bgImageVAlign", "bgImageHAlign", "bgImageScale", "xAxisLineColor", "canvasBgColor", "canvasBgAlpha", "canvasBgRatio", "canvasBgAngle", "canvasBorderColor", "canvasBorderThickness", "canvasBorderAlpha", "showVLineLabelBorder", "logoURL", "logoPosition", "logoAlpha", "logoScale", "logoLink"]
                        },
                        {
                            id: "Anchors",
                            prop: ["drawAnchors", "anchorSides", "anchorRadius", "anchorBorderColor", "anchorBorderThickness", "anchorBgColor", "anchorAlpha", "anchorBgAlpha"]
                        },
                        {
                            id: "Legend Properties",
                            prop: ["showLegend", "legendItemFontBold", "legendItemFont", "legendItemFontSize", "legendItemFontColor", "legendItemHoverFontColor", "legendCaptionAlignment", "legendCaptionBold", "legendCaptionFont", "legendCaptionFontSize", "legendIconScale", "legendItemHiddenColor", "legendPosition", "legendBgColor", "legendBgAlpha", "legendBorderColor", "legendBorderThickness", "legendBorderAlpha", "legendShadow", "legendAllowDrag", "legendScrollBgColor", "reverseLegend", "interactiveLegend", "legendNumColumns", "minimiseWrappingInLegend"]
                        },
                        {
                            id: "Divisional Lines/Grids",
                            prop: ["numDivLines", "divLineColor", "divLineThickness", "divLineAlpha", "divLineDashed", "divLineDashLen", "divLineDashGap", "zeroPlaneColor", "zeroPlaneThickness", "zeroPlaneAlpha", "showZeroPlaneValue", "showAlternateHGridColor", "alternateHGridColor", "alternateHGridAlpha"]
                        },
                        {
                            id: "Tool-tip",
                            prop: ["showToolTip", "toolTipColor", "toolTipBgColor", "toolTipBorderColor", "toolTipSepChar", "seriesNameInToolTip", "showToolTipShadow"]
                        },
                        {
                            id: "Paddings and Margins",
                            prop: ["captionPadding", "xAxisNamePadding", "yAxisNamePadding", "yAxisValuesPadding", "labelPadding", "valuePadding", "plotSpacePercent", "chartLeftMargin", "chartRightMargin", "chartTopMargin", "chartBottomMargin", "legendPadding", "canvasLeftMargin", "canvasRightMargin", "canvasTopMargin", "canvasBottomMargin"]
                        }
                    ]
                },
                {
                    id: "Data", group: true, sub: [
                        {
                            id: "Data Plot Cosmetics",
                            prop: ["useRoundEdges", "showPlotBorder", "plotBorderColor", "plotBorderThickness", "plotBorderAlpha", "plotBorderDashed", "plotBorderDashLen", "plotBorderDashGap", "plotFillAngle", "plotFillRatio", "plotFillAlpha", "plotGradientColor"]
                        },
                        {
                            id: "Data Value Cosmetics",
                            prop: ["valueFont", "valueFontColor", "valueFontSize", "valueFontBold", "valueFontItalic", "valueBgColor", "valueBorderColor", "valueAlpha", "valueFontAlpha", "valueBgAlpha", "valueBorderAlpha", "valueBorderThickness", "valueBorderRadius", "valueBorderDashed", "valueBorderDashGap", "valueBorderDashLen", "valueHoverAlpha", "valueFontHoverAlpha", "valueBgHoverAlpha", "valueBorderHoverAlpha", "showValuesOnHover"]
                        },
                        {
                            id: "Data Label Cosmetics",
                            prop: ["labelFont", "labelFontColor", "labelFontSize", "labelFontBold", "labelFontItalic", "labelBgColor", "labelBorderColor", "labelAlpha", "labelBgAlpha", "labelBorderAlpha", "labelBorderPadding", "labelBorderRadius", "labelBorderThickness", "labelBorderDashed", "labelBorderDashLen", "labelBorderDashGap", "labelLink"]
                        },
                        {
                            id: "Data Plot Hover Effects",
                            prop: ["showHoverEffect", "plotHoverEffect", "plotFillHoverColor", "plotFillHoverAlpha"]
                        }
                    ]
                },
                {
                    id: "Others", group: true, sub: [
                        {
                            id: "Number Formatting",
                            prop: ["formatNumber", "formatNumberScale", "defaultNumberScale", "numberScaleUnit", "numberScaleValue", "scaleRecursively", "maxScaleRecursion", "scaleSeparator", "numberPrefix", "numberSuffix", "decimalSeparator", "thousandSeparator", "thousandSeparatorPosition", "inDecimalSeparator", "inThousandSeparator", "decimals", "forceDecimals", "forceYAxisValueDecimals", "yAxisValueDecimals"]
                        },
                        {
                            id: "Font Properties",
                            prop: ["baseFont", "baseFontSize", "baseFontColor", "outCnvBaseFont", "outCnvBaseFontSize", "outCnvBaseFontColor"]
                        }
                    ]
                }
            ],
            "categories": ["font", "fontSize", "fontColor"],
            "category": ["label", "showLabel", "toolText", "font", "fontColor", "fontSize", "fontBold", "fontItalic", "bgColor", "borderColor", "alpha", "bgAlpha", "borderAlpha", "borderPadding", "borderRadius", "borderThickness", "borderDashed", "borderDashLen", "borderDashGap", "link"],
            "dataset": ["renderAs", "seriesName", "color", "alpha", "ratio", "showValues", "valuePosition", "dashed", "includeInLegend"],
            "set": ["value", "displayValue", "color", "link", "toolText", "showValue", "valuePosition", "dashed", "alpha"],
            "Vertical data separator lines": ["color", "thickness", "alpha", "dashed", "dashLen", "dashGap", "label", "showLabelBorder", "linePosition", "labelPosition", "labelHAlign", "labelVAlign"],
            "Trend Lines": ["startValue", "endValue", "displayValue", "color", "isTrendZone", "showOnTop", "thickness", "alpha", "dashed", "dashLen", "dashGap", "valueOnRight", "toolText"]
        },
        StackedColumn3DLine: {
            chart: [
                {
                    id: "Chart", group: true, sub: [
                        {id: "Titles (Axis) Names", prop: ["caption", "subCaption", "xAxisName", "yAxisName"]},
                        {
                            id: "Functional Attributes",
                            prop: ["animation", "palette", "paletteColors", "showLabels", "maxLabelHeight", "labelDisplay", "useEllipsesWhenOverflow", "rotateLabels", "slantLabels", "labelStep", "staggerLines", "connectNullData", "showValues", "valuePosition", "rotateValues", "showYAxisValues", "showLimits", "showDivLineValues", "yAxisValuesStep", "showShadow", "adjustDiv", "rotateYAxisName", "yAxisNameWidth", "clickURL", "maxColWidth", "use3DLighting", "showSum", "yAxisMinValue", "yAxisMaxValue"]
                        },
                        {
                            id: "Chart Caption Cosmetics",
                            prop: ["captionAlignment", "captionOnTop", "captionFontSize", "subCaptionFontSize", "captionFont", "subCaptionFont", "captionFontColor", "subCaptionFontColor", "captionFontBold", "subCaptionFontBold", "alignCaptionWithCanvas", "captionHorizontalPadding"]
                        },
                        {
                            id: "X-axis Name Cosmetics",
                            prop: ["xAxisNameFontColor", "xAxisNameFontSize", "xAxisNameFontBold", "xAxisNameFontItalic", "xAxisNameBgColor", "xAxisNameBorderColor", "xAxisNameAlpha", "xAxisNameFontAlpha", "xAxisNameBgAlpha", "xAxisNameBorderAlpha", "xAxisNameBorderPadding", "xAxisNameBorderRadius", "xAxisNameBorderThickness", "xAxisNameBorderDashed", "xAxisNameBorderDashLen", "xAxisNameBorderDashGap"]
                        },
                        {
                            id: "Y-axis Name Cosmetics",
                            prop: ["yAxisNameFont", "yAxisNameFontColor", "yAxisNameFontSize", "yAxisNameFontBold", "yAxisNameFontItalic", "yAxisNameBgColor", "yAxisNameBorderColor", "yAxisNameAlpha", "yAxisNameFontAlpha", "yAxisNameBgAlpha", "yAxisNameBorderAlpha", "yAxisNameBorderPadding", "yAxisNameBorderRadius", "yAxisNameBorderThickness", "yAxisNameBorderDashed", "yAxisNameBorderDashLen", "yAxisNameBorderDashGap"]
                        },
                        {
                            id: "Charts Cosmetics",
                            prop: ["showBorder", "borderColor", "borderThickness", "borderAlpha", "bgColor", "bgAlpha", "bgRatio", "bgAngle", "bgImage", "bgImageAlpha", "bgImageDisplayMode", "bgImageVAlign", "bgImageHAlign", "bgImageScale", "xAxisLineColor", "canvasBgColor", "canvasBgAlpha", "canvasBaseColor", "showCanvasBg", "showCanvasBase", "canvasBaseDepth", "canvasBgDepth", "showVLineLabelBorder", "logoURL", "logoPosition", "logoAlpha", "logoScale", "logoLink"]
                        },
                        {
                            id: "Anchors",
                            prop: ["drawAnchors", "anchorSides", "anchorRadius", "anchorBorderColor", "anchorBorderThickness", "anchorBgColor", "anchorAlpha", "anchorBgAlpha"]
                        },
                        {
                            id: "Legend Properties",
                            prop: ["showLegend", "legendItemFontBold", "legendItemFont", "legendItemFontSize", "legendItemFontColor", "legendItemHoverFontColor", "legendCaptionAlignment", "legendCaptionBold", "legendCaptionFont", "legendCaptionFontSize", "legendIconScale", "legendItemHiddenColor", "legendPosition", "legendBgColor", "legendBgAlpha", "legendBorderColor", "legendBorderThickness", "legendBorderAlpha", "legendShadow", "legendAllowDrag", "legendScrollBgColor", "reverseLegend", "interactiveLegend", "legendNumColumns", "minimiseWrappingInLegend"]
                        },
                        {
                            id: "Divisional Lines/Grids",
                            prop: ["numDivLines", "divLineColor", "divLineThickness", "divLineAlpha", "divLineDashed", "divLineDashLen", "divLineDashGap", "zeroPlaneColor", "zeroPlaneAlpha", "showZeroPlaneValue", "zeroPlaneShowBorder", "zeroPlaneBorderColor"]
                        },
                        {
                            id: "Tool-tip",
                            prop: ["showToolTip", "toolTipColor", "toolTipBgColor", "toolTipBorderColor", "toolTipSepChar", "seriesNameInToolTip", "showToolTipShadow"]
                        },
                        {
                            id: "Paddings and Margins",
                            prop: ["captionPadding", "xAxisNamePadding", "yAxisNamePadding", "yAxisValuesPadding", "labelPadding", "valuePadding", "plotSpacePercent", "chartLeftMargin", "chartRightMargin", "chartTopMargin", "chartBottomMargin", "legendPadding", "canvasLeftMargin", "canvasRightMargin", "canvasTopMargin", "canvasBottomMargin"]
                        }
                    ]
                },
                {
                    id: "Data", group: true, sub: [
                        {
                            id: "Data Plot Cosmetics",
                            prop: ["overlapColumns", "showPlotBorder", "plotBorderColor", "plotBorderAlpha", "plotFillAlpha"]
                        },
                        {
                            id: "Data Value Cosmetics",
                            prop: ["valueFont", "valueFontColor", "valueFontSize", "valueFontBold", "valueFontItalic", "valueBgColor", "valueBorderColor", "valueAlpha", "valueFontAlpha", "valueBgAlpha", "valueBorderAlpha", "valueBorderThickness", "valueBorderRadius", "valueBorderDashed", "valueBorderDashGap", "valueBorderDashLen", "valueHoverAlpha", "valueFontHoverAlpha", "valueBgHoverAlpha", "valueBorderHoverAlpha", "showValuesOnHover"]
                        },
                        {
                            id: "Data Label Cosmetics",
                            prop: ["labelFont", "labelFontColor", "labelFontSize", "labelFontBold", "labelFontItalic", "labelBgColor", "labelBorderColor", "labelAlpha", "labelBgAlpha", "labelBorderAlpha", "labelBorderPadding", "labelBorderRadius", "labelBorderThickness", "labelBorderDashed", "labelBorderDashLen", "labelBorderDashGap", "labelLink"]
                        },
                        {
                            id: "Data Plot Hover Effects",
                            prop: ["showHoverEffect", "plotHoverEffect", "plotFillHoverColor", "plotFillHoverAlpha"]
                        }
                    ]
                },
                {
                    id: "Others", group: true, sub: [
                        {
                            id: "Number Formatting",
                            prop: ["formatNumber", "formatNumberScale", "defaultNumberScale", "numberScaleUnit", "numberScaleValue", "scaleRecursively", "maxScaleRecursion", "scaleSeparator", "numberPrefix", "numberSuffix", "decimalSeparator", "thousandSeparator", "thousandSeparatorPosition", "inDecimalSeparator", "inThousandSeparator", "decimals", "forceDecimals", "forceYAxisValueDecimals", "yAxisValueDecimals"]
                        },
                        {
                            id: "Font Properties",
                            prop: ["baseFont", "baseFontSize", "baseFontColor", "outCnvBaseFont", "outCnvBaseFontSize", "outCnvBaseFontColor"]
                        }
                    ]
                }
            ],
            "categories": ["font", "fontSize", "fontColor"],
            "category": ["label", "showLabel", "toolText", "font", "fontColor", "fontSize", "fontBold", "fontItalic", "bgColor", "borderColor", "alpha", "bgAlpha", "borderAlpha", "borderPadding", "borderRadius", "borderThickness", "borderDashed", "borderDashLen", "borderDashGap", "link"],
            "dataset": ["renderAs", "seriesName", "color", "alpha", "showValues", "valuePosition", "includeInLegend"],
            "set": ["value", "displayValue", "color", "link", "toolText", "showValue", "valuePosition", "alpha"],
            "Vertical data separator lines": ["color", "thickness", "alpha", "dashed", "dashLen", "dashGap", "label", "showLabelBorder", "linePosition", "labelPosition", "labelHAlign", "labelVAlign"],
            "Trend Lines": ["startValue", "endValue", "displayValue", "color", "isTrendZone", "thickness", "alpha", "dashed", "dashLen", "dashGap", "valueOnRight", "toolText"]
        },
        MSCombiDY2D: {
            chart: [
                {
                    id: "Chart", group: true, sub: [
                        {
                            id: "Titles (Axis) Names",
                            prop: ["caption", "subCaption", "xAxisName", "sYAxisName", "pYAxisName"]
                        },
                        {
                            id: "Functional Attributes",
                            prop: ["animation", "palette", "paletteColors", "connectNullData", "areaOverColumns", "showLabels", "maxLabelHeight", "labelDisplay", "useEllipsesWhenOverflow", "rotateLabels", "slantLabels", "labelStep", "staggerLines", "showValues", "valuePosition", "rotateValues", "placeValuesInside", "showYAxisValues", "showLimits", "showDivLineValues", "showSecondaryLimits", "showDivLineSecondaryValue", "yAxisValuesStep", "showShadow", "adjustDiv", "rotateYAxisName", "clickURL", "pYAxisMaxValue", "sYAxisMinValue", "sYAxisMaxValue", "pYAxisMinValue", "setAdaptiveYMin", "setAdaptiveSYMin", "syncAxisLimits", "pYAxisNameWidth", "sYAxisNameWidth"]
                        },
                        {
                            id: "Chart Caption Cosmetics",
                            prop: ["captionAlignment", "captionOnTop", "captionFontSize", "subCaptionFontSize", "captionFont", "subCaptionFont", "captionFontColor", "subCaptionFontColor", "captionFontBold", "subCaptionFontBold", "alignCaptionWithCanvas", "captionHorizontalPadding"]
                        },
                        {
                            id: "X-axis Name Cosmetics",
                            prop: ["xAxisNameFontColor", "xAxisNameFontSize", "xAxisNameFontBold", "xAxisNameFontItalic", "xAxisNameBgColor", "xAxisNameBorderColor", "xAxisNameAlpha", "xAxisNameFontAlpha", "xAxisNameBgAlpha", "xAxisNameBorderAlpha", "xAxisNameBorderPadding", "xAxisNameBorderRadius", "xAxisNameBorderThickness", "xAxisNameBorderDashed", "xAxisNameBorderDashLen", "xAxisNameBorderDashGap"]
                        },
                        {
                            id: "Y-axis Name Cosmetics",
                            prop: ["pYAxisNameFont", "pYAxisNameFontColor", "pYAxisNameFontSize", "pYAxisNameFontBold", "pYAxisNameFontItalic", "pYAxisNameBgColor", "pYAxisNameBorderColor", "pYAxisNameAlpha", "pYAxisNameFontAlpha", "pYAxisNameBgAlpha", "pYAxisNameBorderAlpha", "pYAxisNameBorderPadding", "pYAxisNameBorderRadius", "pYAxisNameBorderThickness", "pYAxisNameBorderDashed", "pYAxisNameBorderDashLen", "pYAxisNameBorderDashGap"]
                        },
                        {
                            id: "Charts Cosmetics",
                            prop: ["showBorder", "borderColor", "borderThickness", "borderAlpha", "bgColor", "bgAlpha", "bgRatio", "bgAngle", "bgImage", "bgImageAlpha", "bgImageDisplayMode", "bgImageVAlign", "bgImageHAlign", "bgImageScale", "xAxisLineColor", "canvasBgColor", "canvasBgAlpha", "canvasBgRatio", "canvasBgAngle", "canvasBorderColor", "canvasBorderThickness", "canvasBorderAlpha", "showVLineLabelBorder", "logoURL", "logoPosition", "logoAlpha", "logoScale", "logoLink"]
                        },
                        {
                            id: "Anchors",
                            prop: ["drawAnchors", "anchorSides", "anchorRadius", "anchorBorderColor", "anchorBorderThickness", "anchorBgColor", "anchorAlpha", "anchorBgAlpha"]
                        },
                        {
                            id: "Legend Properties",
                            prop: ["showLegend", "legendItemFontBold", "legendItemFont", "legendItemFontSize", "legendItemFontColor", "legendPosition", "legendCaptionAlignment", "legendCaptionBold", "legendCaptionFont", "legendCaptionFontSize", "legendCaptionFontColor", "legendItemHiddenColor", "legendIconScale", "legendBgColor", "legendBgAlpha", "legendBorderColor", "legendBorderThickness", "legendBorderAlpha", "legendShadow", "legendAllowDrag", "legendScrollBgColor", "reverseLegend", "interactiveLegend", "legendNumColumns", "minimiseWrappingInLegend"]
                        },
                        {
                            id: "Divisional Lines/Grids",
                            prop: ["numDivLines", "divLineColor", "divLineThickness", "divLineAlpha", "divLineDashed", "divLineDashLen", "divLineDashGap", "zeroPlaneColor", "zeroPlaneThickness", "zeroPlaneAlpha", "showPZeroPlaneValue", "showSZeroPlaneValue", "showAlternateHGridColor", "alternateHGridColor", "alternateHGridAlpha"]
                        },
                        {
                            id: "Tool-tip",
                            prop: ["showToolTip", "toolTipColor", "toolTipBgColor", "toolTipBorderColor", "toolTipSepChar", "seriesNameInToolTip", "showToolTipShadow"]
                        },
                        {
                            id: "Paddings and Margins",
                            prop: ["captionPadding", "xAxisNamePadding", "yAxisNamePadding", "yAxisValuesPadding", "labelPadding", "valuePadding", "plotSpacePercent", "chartLeftMargin", "chartRightMargin", "chartTopMargin", "chartBottomMargin", "legendPadding", "canvasLeftMargin", "canvasRightMargin", "canvasTopMargin", "canvasBottomMargin"]
                        }
                    ]
                },
                {
                    id: "Data", group: true, sub: [
                        {
                            id: "Data Plot Cosmetics",
                            prop: ["useRoundEdges", "showPlotBorder", "plotBorderColor", "plotBorderThickness", "plotBorderAlpha", "plotBorderDashed", "plotBorderDashLen", "plotBorderDashGap", "plotFillAngle", "plotFillAlpha", "plotGradientColor", "lineColor", "lineThickness", "lineAlpha", "lineDashed", "lineDashLen", "lineDashGap"]
                        },
                        {
                            id: "Data Value Cosmetics",
                            prop: ["valueFont", "valueFontColor", "valueFontSize", "valueFontBold", "valueFontItalic", "valueBgColor", "valueBorderColor", "valueAlpha", "valueFontAlpha", "valueBgAlpha", "valueBorderAlpha", "valueBorderThickness", "valueBorderRadius", "valueBorderDashed", "valueBorderDashGap", "valueBorderDashLen", "valueHoverAlpha", "valueFontHoverAlpha", "valueBgHoverAlpha", "valueBorderHoverAlpha", "showValuesOnHover"]
                        },
                        {
                            id: "Data Label Cosmetics",
                            prop: ["labelFont", "labelFontColor", "labelFontSize", "labelFontBold", "labelFontItalic", "labelBgColor", "labelBorderColor", "labelAlpha", "labelBgAlpha", "labelBorderAlpha", "labelBorderPadding", "labelBorderRadius", "labelBorderThickness", "labelBorderDashed", "labelBorderDashLen", "labelBorderDashGap", "labelLink"]
                        },
                        {
                            id: "Data Plot Hover Effects",
                            prop: ["showHoverEffect", "plotHoverEffect", "plotFillHoverColor", "plotFillHoverAlpha"]
                        }
                    ]
                },
                {
                    id: "Others", group: true, sub: [
                        {
                            id: "Number Formatting",
                            prop: ["formatNumber", "formatNumberScale", "defaultNumberScale", "numberScaleUnit", "numberScaleValue", "scaleRecursively", "sScaleRecursively", "maxScaleRecursion", "sMaxScaleRecursion", "scaleSeparator", "sScaleSeparator", "numberPrefix", "numberSuffix", "decimalSeparator", "thousandSeparator", "thousandSeparatorPosition", "inDecimalSeparator", "inThousandSeparator", "decimals", "forceDecimals", "forceYAxisValueDecimals", "forceSYAxisValueDecimals", "yAxisValueDecimals", "sFormatNumber", "sFormatNumberScale", "sDefaultNumberScale", "sNumberScaleUnit", "sNumberScaleValue", "sNumberPrefix", "sNumberSuffix", "sDecimals", "sYAxisValueDecimals"]
                        },
                        {
                            id: "Font Properties",
                            prop: ["baseFont", "baseFontSize", "baseFontColor", "outCnvBaseFont", "outCnvBaseFontSize", "outCnvBaseFontColor"]
                        }
                    ]
                }
            ],
            "categories": ["font", "fontSize", "fontColor"],
            "category": ["label", "showLabel", "toolText", "font", "fontColor", "fontSize", "fontBold", "fontItalic", "bgColor", "borderColor", "alpha", "bgAlpha", "borderAlpha", "borderPadding", "borderRadius", "borderThickness", "borderDashed", "borderDashLen", "borderDashGap", "link"],
            "dataset": ["renderAs", "parentYAxis", "showPlotBorder", "plotBorderColor", "plotBorderThickness", "plotBorderAlpha", "seriesName", "color", "alpha", "showValues", "valuePosition", "dashed", "includeInLegend", "drawAnchors", "anchorSides", "anchorRadius", "anchorBorderColor", "anchorBorderThickness", "anchorBgColor", "anchorAlpha", "lineThickness", "lineDashLen", "lineDashGap"],
            "set": ["anchorSides", "anchorRadius", "anchorBorderColor", "anchorBorderThickness", "anchorBgColor", "anchorAlpha", "anchorBgAlpha", "value", "displayValue", "color", "link", "toolText", "showValue", "valuePosition", "dashed", "alpha"],
            "Vertical data separator lines": ["linePosition", "color", "thickness", "showLabelBorder", "label", "labelPosition", "alpha", "dashed", "dashLen", "dashGap", "labelHAlign", "labelVAlign"],
            "Trend Lines": ["parentYAxis", "startValue", "endValue", "displayValue", "color", "isTrendZone", "showOnTop", "thickness", "alpha", "dashed", "dashLen", "dashGap", "toolText"]
        },
        MSColumn3DLineDY: {
            chart: [
                {
                    id: "Chart", group: true, sub: [
                        {
                            id: "Titles (Axis) Names",
                            prop: ["caption", "subCaption", "xAxisName", "pYAxisName", "sYAxisName"]
                        },
                        {
                            id: "Functional Attributes",
                            prop: ["animation", "palette", "paletteColors", "showLabels", "maxLabelHeight", "labelDisplay", "useEllipsesWhenOverflow", "rotateLabels", "slantLabels", "labelStep", "staggerLines", "connectNullData", "showValues", "valuePosition", "rotateValues", "showYAxisValues", "showLimits", "showDivLineValues", "yAxisValuesStep", "adjustDiv", "showSecondaryLimits", "showDivLineSecondaryValue", "clickURL", "maxColWidth", "use3DLighting", "showShadow", "pYAxisMaxValue", "pYAxisMinValue", "sYAxisMinValue", "sYAxisMaxValue", "setAdaptiveYMin", "setAdaptiveSYMin", "syncAxisLimits", "rotateYAxisName", "pYAxisNameWidth", "sYAxisNameWidth", "use3DLineShift", "primaryAxisOnLeft"]
                        },
                        {
                            id: "Chart Caption Cosmetics",
                            prop: ["captionAlignment", "captionOnTop", "captionFontSize", "subCaptionFontSize", "captionFont", "subCaptionFont", "captionFontColor", "subCaptionFontColor", "captionFontBold", "subCaptionFontBold", "alignCaptionWithCanvas", "captionHorizontalPadding"]
                        },
                        {
                            id: "X-axis Name Cosmetics",
                            prop: ["xAxisNameFontColor", "xAxisNameFontSize", "xAxisNameFontBold", "xAxisNameFontItalic", "xAxisNameBgColor", "xAxisNameBorderColor", "xAxisNameAlpha", "xAxisNameFontAlpha", "xAxisNameBgAlpha", "xAxisNameBorderAlpha", "xAxisNameBorderPadding", "xAxisNameBorderRadius", "xAxisNameBorderThickness", "xAxisNameBorderDashed", "xAxisNameBorderDashLen", "xAxisNameBorderDashGap"]
                        },
                        {
                            id: "Y-axis Name Cosmetics",
                            prop: ["pYAxisNameFont", "pYAxisNameFontColor", "pYAxisNameFontSize", "pYAxisNameFontBold", "pYAxisNameFontItalic", "pYAxisNameBgColor", "pYAxisNameBorderColor", "pYAxisNameAlpha", "pYAxisNameFontAlpha", "pYAxisNameBgAlpha", "pYAxisNameBorderAlpha", "pYAxisNameBorderPadding", "pYAxisNameBorderRadius", "pYAxisNameBorderThickness", "pYAxisNameBorderDashed", "pYAxisNameBorderDashLen", "pYAxisNameBorderDashGap"]
                        },
                        {
                            id: "Charts Cosmetics",
                            prop: ["showBorder", "borderColor", "borderThickness", "borderAlpha", "bgColor", "bgAlpha", "bgRatio", "bgAngle", "bgImage", "bgImageAlpha", "bgImageDisplayMode", "bgImageVAlign", "bgImageHAlign", "bgImageScale", "xAxisLineColor", "canvasBgColor", "canvasBgAlpha", "canvasBaseColor", "showCanvasBg", "showCanvasBase", "canvasBaseDepth", "canvasBgDepth", "showVLineLabelBorder", "logoURL", "logoPosition", "logoAlpha", "logoScale", "logoLink"]
                        },
                        {
                            id: "Anchors",
                            prop: ["drawAnchors", "anchorSides", "anchorRadius", "anchorBorderColor", "anchorBorderThickness", "anchorBgColor", "anchorAlpha", "anchorBgAlpha"]
                        },
                        {
                            id: "Legend Properties",
                            prop: ["showLegend", "legendItemFontBold", "legendItemFont", "legendItemFontSize", "legendItemFontColor", "legendPosition", "legendCaptionAlignment", "legendCaptionBold", "legendCaptionFont", "legendCaptionFontSize", "legendCaptionFontColor", "legendItemHiddenColor", "legendIconScale", "legendBgColor", "legendBgAlpha", "legendBorderColor", "legendBorderThickness", "legendBorderAlpha", "legendShadow", "legendAllowDrag", "legendScrollBgColor", "reverseLegend", "interactiveLegend", "legendNumColumns", "minimiseWrappingInLegend"]
                        },
                        {
                            id: "Divisional Lines/Grids",
                            prop: ["numDivLines", "divLineColor", "divLineThickness", "divLineAlpha", "divLineDashed", "divLineDashLen", "divLineDashGap", "zeroPlaneColor", "zeroPlaneAlpha", "showPZeroPlaneValue", "showSZeroPlaneValue", "zeroPlaneShowBorder", "zeroPlaneBorderColor"]
                        },
                        {
                            id: "Tool-tip",
                            prop: ["showToolTip", "toolTipColor", "toolTipBgColor", "toolTipBorderColor", "toolTipSepChar", "seriesNameInToolTip", "showToolTipShadow"]
                        },
                        {
                            id: "Paddings and Margins",
                            prop: ["captionPadding", "xAxisNamePadding", "yAxisNamePadding", "yAxisValuesPadding", "labelPadding", "valuePadding", "plotSpacePercent", "chartLeftMargin", "chartRightMargin", "chartTopMargin", "chartBottomMargin", "legendPadding", "canvasLeftMargin", "canvasRightMargin", "canvasTopMargin", "canvasBottomMargin"]
                        }
                    ]
                },
                {
                    id: "Data", group: true, sub: [
                        {
                            id: "Data Plot Cosmetics",
                            prop: ["overlapColumns", "showPlotBorder", "plotBorderColor", "plotBorderAlpha", "plotFillAlpha", "lineColor", "lineThickness", "lineAlpha", "lineDashed", "lineDashLen", "lineDashGap"]
                        },
                        {
                            id: "Data Value Cosmetics",
                            prop: ["valueFont", "valueFontColor", "valueFontSize", "valueFontBold", "valueFontItalic", "valueBgColor", "valueBorderColor", "valueAlpha", "valueFontAlpha", "valueBgAlpha", "valueBorderAlpha", "valueBorderThickness", "valueBorderRadius", "valueBorderDashed", "valueBorderDashGap", "valueBorderDashLen", "valueHoverAlpha", "valueFontHoverAlpha", "valueBgHoverAlpha", "valueBorderHoverAlpha", "showValuesOnHover"]
                        },
                        {
                            id: "Data Label Cosmetics",
                            prop: ["labelFont", "labelFontColor", "labelFontSize", "labelFontBold", "labelFontItalic", "labelBgColor", "labelBorderColor", "labelAlpha", "labelBgAlpha", "labelBorderAlpha", "labelBorderPadding", "labelBorderRadius", "labelBorderThickness", "labelBorderDashed", "labelBorderDashLen", "labelBorderDashGap", "labelLink"]
                        },
                        {
                            id: "Data Plot Hover Effects",
                            prop: ["showHoverEffect", "plotHoverEffect", "plotFillHoverColor", "plotFillHoverAlpha"]
                        }
                    ]
                },
                {
                    id: "Others", group: true, sub: [
                        {
                            id: "Number Formatting",
                            prop: ["formatNumber", "formatNumberScale", "defaultNumberScale", "numberScaleUnit", "numberScaleValue", "scaleRecursively", "sScaleRecursively", "maxScaleRecursion", "sMaxScaleRecursion", "scaleSeparator", "sScaleSeparator", "numberPrefix", "numberSuffix", "decimalSeparator", "thousandSeparator", "thousandSeparatorPosition", "inDecimalSeparator", "inThousandSeparator", "decimals", "forceDecimals", "forceYAxisValueDecimals", "forceSYAxisValueDecimals", "yAxisValueDecimals", "sFormatNumber", "sFormatNumberScale", "sDefaultNumberScale", "sNumberScaleUnit", "sNumberScaleValue", "sNumberPrefix", "sNumberSuffix", "sDecimals", "sYAxisValueDecimals"]
                        },
                        {
                            id: "Font Properties",
                            prop: ["baseFont", "baseFontSize", "baseFontColor", "outCnvBaseFont", "outCnvBaseFontSize", "outCnvBaseFontColor"]
                        }
                    ]
                }
            ],
            "categories": ["font", "fontSize", "fontColor"],
            "category": ["label", "showLabel", "toolText", "font", "fontColor", "fontSize", "fontBold", "fontItalic", "bgColor", "borderColor", "alpha", "bgAlpha", "borderAlpha", "borderPadding", "borderRadius", "borderThickness", "borderDashed", "borderDashLen", "borderDashGap", "link"],
            "dataset": ["parentYAxis", "seriesName", "color", "alpha", "showValues", "valuePosition", "includeInLegend", "drawAnchors", "anchorSides", "anchorRadius", "anchorBorderColor", "anchorBorderThickness", "anchorBgColor", "anchorAlpha", "anchorBgAlpha", "lineThickness", "dashed", "lineDashLen", "lineDashGap"],
            "set": ["dashed", "anchorSides", "anchorRadius", "anchorBorderColor", "anchorBorderThickness", "anchorBgColor", "anchorAlpha", "anchorBgAlpha", "value", "displayValue", "color", "link", "toolText", "showValue", "valuePosition", "alpha"],
            "Vertical data separator lines": ["color", "thickness", "alpha", "dashed", "dashLen", "dashGap", "label", "showLabelBorder", "linePosition", "labelPosition", "labelHAlign", "labelVAlign"],
            "Trend Lines": ["parentYAxis", "startValue", "endValue", "displayValue", "color", "isTrendZone", "thickness", "alpha", "dashed", "dashLen", "dashGap", "toolText"]
        },
        StackedColumn3DLineDY: {
            chart: [
                {
                    id: "Chart", group: true, sub: [
                        {
                            id: "Titles (Axis) Names",
                            prop: ["caption", "subCaption", "xAxisName", "pYAxisName", "sYAxisName"]
                        },
                        {
                            id: "Functional Attributes",
                            prop: ["animation", "palette", "paletteColors", "showLabels", "maxLabelHeight", "labelDisplay", "useEllipsesWhenOverflow", "rotateLabels", "slantLabels", "labelStep", "staggerLines", "connectNullData", "showValues", "valuePosition", "rotateValues", "showYAxisValues", "showLimits", "showDivLineValues", "yAxisValuesStep", "adjustDiv", "showSecondaryLimits", "showDivLineSecondaryValue", "clickURL", "maxColWidth", "use3DLighting", "showShadow", "pYAxisMinValue", "pYAxisMaxValue", "sYAxisMinValue", "sYAxisMaxValue", "setAdaptiveSYMin", "syncAxisLimits", "rotateYAxisName", "pYAxisNameWidth", "sYAxisNameWidth", "use3DLineShift", "primaryAxisOnLeft"]
                        },
                        {
                            id: "Chart Caption Cosmetics",
                            prop: ["captionAlignment", "captionOnTop", "captionFontSize", "subCaptionFontSize", "captionFont", "subCaptionFont", "captionFontColor", "subCaptionFontColor", "captionFontBold", "subCaptionFontBold", "alignCaptionWithCanvas", "captionHorizontalPadding"]
                        },
                        {
                            id: "X-axis Name Cosmetics",
                            prop: ["xAxisNameFontColor", "xAxisNameFontSize", "xAxisNameFontBold", "xAxisNameFontItalic", "xAxisNameBgColor", "xAxisNameBorderColor", "xAxisNameAlpha", "xAxisNameFontAlpha", "xAxisNameBgAlpha", "xAxisNameBorderAlpha", "xAxisNameBorderPadding", "xAxisNameBorderRadius", "xAxisNameBorderThickness", "xAxisNameBorderDashed", "xAxisNameBorderDashLen", "xAxisNameBorderDashGap"]
                        },
                        {
                            id: "Y-axis Name Cosmetics",
                            prop: ["pYAxisNameFont", "pYAxisNameFontColor", "pYAxisNameFontSize", "pYAxisNameFontBold", "pYAxisNameFontItalic", "pYAxisNameBgColor", "pYAxisNameBorderColor", "pYAxisNameAlpha", "pYAxisNameFontAlpha", "pYAxisNameBgAlpha", "pYAxisNameBorderAlpha", "pYAxisNameBorderPadding", "pYAxisNameBorderRadius", "pYAxisNameBorderThickness", "pYAxisNameBorderDashed", "pYAxisNameBorderDashLen", "pYAxisNameBorderDashGap"]
                        },
                        {
                            id: "Charts Cosmetics",
                            prop: ["showSum", "showBorder", "borderColor", "borderThickness", "borderAlpha", "bgColor", "bgAlpha", "bgRatio", "bgAngle", "bgImage", "bgImageAlpha", "bgImageDisplayMode", "bgImageVAlign", "bgImageHAlign", "xAxisLineColor", "bgImageScale", "canvasBgColor", "canvasBgAlpha", "canvasBaseColor", "showCanvasBg", "showCanvasBase", "canvasBaseDepth", "canvasBgDepth", "showVLineLabelBorder", "logoURL", "logoPosition", "logoAlpha", "logoScale", "logoLink"]
                        },
                        {
                            id: "Anchors",
                            prop: ["drawAnchors", "anchorSides", "anchorRadius", "anchorBorderColor", "anchorBorderThickness", "anchorBgColor", "anchorAlpha", "anchorBgAlpha"]
                        },
                        {
                            id: "Legend Properties",
                            prop: ["showLegend", "legendItemFontBold", "legendItemFont", "legendItemFontSize", "legendItemFontColor", "legendPosition", "legendCaptionAlignment", "legendCaptionBold", "legendCaptionFont", "legendCaptionFontSize", "legendCaptionFontColor", "legendItemHiddenColor", "legendIconScale", "legendBgColor", "legendBgAlpha", "legendBorderColor", "legendBorderThickness", "legendBorderAlpha", "legendShadow", "legendAllowDrag", "legendScrollBgColor", "reverseLegend", "interactiveLegend", "legendNumColumns", "minimiseWrappingInLegend"]
                        },
                        {
                            id: "Divisional Lines/Grids",
                            prop: ["numDivLines", "divLineColor", "divLineThickness", "divLineAlpha", "divLineDashed", "divLineDashLen", "divLineDashGap", "zeroPlaneColor", "zeroPlaneAlpha", "showPZeroPlaneValue", "showSZeroPlaneValue", "zeroPlaneShowBorder", "zeroPlaneBorderColor"]
                        },
                        {
                            id: "Tool-tip",
                            prop: ["showToolTip", "toolTipColor", "toolTipBgColor", "toolTipBorderColor", "toolTipSepChar", "seriesNameInToolTip", "showToolTipShadow"]
                        },
                        {
                            id: "Paddings and Margins",
                            prop: ["legendPadding", "captionPadding", "xAxisNamePadding", "yAxisNamePadding", "yAxisValuesPadding", "labelPadding", "valuePadding", "plotSpacePercent", "chartLeftMargin", "chartRightMargin", "chartTopMargin", "chartBottomMargin", "legendPadding", "canvasLeftMargin", "canvasRightMargin", "canvasTopMargin", "canvasBottomMargin"]
                        }
                    ]
                },
                {
                    id: "Data", group: true, sub: [
                        {
                            id: "Data Plot Cosmetics",
                            prop: ["overlapColumns", "showPlotBorder", "plotBorderColor", "plotBorderAlpha", "plotFillAlpha", "lineColor", "lineThickness", "lineAlpha", "lineDashed", "lineDashLen", "lineDashGap"]
                        },
                        {
                            id: "Data Value Cosmetics",
                            prop: ["valueFont", "valueFontColor", "valueFontSize", "valueFontBold", "valueFontItalic", "valueBgColor", "valueBorderColor", "valueAlpha", "valueFontAlpha", "valueBgAlpha", "valueBorderAlpha", "valueBorderThickness", "valueBorderRadius", "valueBorderDashed", "valueBorderDashGap", "valueBorderDashLen", "valueHoverAlpha", "valueFontHoverAlpha", "valueBgHoverAlpha", "valueBorderHoverAlpha", "showValuesOnHover"]
                        },
                        {
                            id: "Data Label Cosmetics",
                            prop: ["labelFont", "labelFontColor", "labelFontSize", "labelFontBold", "labelFontItalic", "labelBgColor", "labelBorderColor", "labelAlpha", "labelBgAlpha", "labelBorderAlpha", "labelBorderPadding", "labelBorderRadius", "labelBorderThickness", "labelBorderDashed", "labelBorderDashLen", "labelBorderDashGap", "labelLink"]
                        },
                        {
                            id: "Data Plot Hover Effects",
                            prop: ["showHoverEffect", "plotHoverEffect", "plotFillHoverColor", "plotFillHoverAlpha"]
                        }
                    ]
                },
                {
                    id: "Others", group: true, sub: [
                        {
                            id: "Number Formatting",
                            prop: ["formatNumber", "formatNumberScale", "defaultNumberScale", "numberScaleUnit", "numberScaleValue", "scaleRecursively", "sScaleRecursively", "maxScaleRecursion", "sMaxScaleRecursion", "scaleSeparator", "sScaleSeparator", "numberPrefix", "numberSuffix", "decimalSeparator", "thousandSeparator", "thousandSeparatorPosition", "inDecimalSeparator", "inThousandSeparator", "decimals", "forceDecimals", "forceYAxisValueDecimals", "forceSYAxisValueDecimals", "yAxisValueDecimals", "sFormatNumber", "sFormatNumberScale", "sDefaultNumberScale", "sNumberScaleUnit", "sNumberScaleValue", "sNumberPrefix", "sNumberSuffix", "sDecimals", "sYAxisValueDecimals"]
                        },
                        {
                            id: "Font Properties",
                            prop: ["baseFont", "baseFontSize", "baseFontColor", "outCnvBaseFont", "outCnvBaseFontSize", "outCnvBaseFontColor"]
                        }
                    ]
                }
            ],
            "categories": ["font", "fontSize", "fontColor"],
            "category": ["label", "showLabel", "toolText", "font", "fontColor", "fontSize", "fontBold", "fontItalic", "bgColor", "borderColor", "alpha", "bgAlpha", "borderAlpha", "borderPadding", "borderRadius", "borderThickness", "borderDashed", "borderDashLen", "borderDashGap", "link"],
            "dataset": ["parentYAxis", "seriesName", "color", "alpha", "showValues", "valuePosition", "includeInLegend", "drawAnchors", "anchorSides", "anchorRadius", "anchorBorderColor", "anchorBorderThickness", "anchorBgColor", "anchorAlpha", "anchorBgAlpha", "lineThickness", "dashed", "lineDashLen", "lineDashGap"],
            "set": ["dashed", "anchorSides", "anchorRadius", "anchorBorderColor", "anchorBorderThickness", "anchorBgColor", "anchorAlpha", "anchorBgAlpha", "value", "displayValue", "color", "link", "toolText", "showValue", "valuePosition", "alpha"],
            "Vertical data separator lines": ["color", "thickness", "alpha", "dashed", "dashLen", "dashGap", "label", "showLabelBorder", "linePosition", "labelPosition", "labelHAlign", "labelVAlign"],
            "Trend Lines": ["parentYAxis", "startValue", "endValue", "displayValue", "color", "isTrendZone", "thickness", "alpha", "dashed", "dashLen", "dashGap", "toolText"]
        },
        MSStackedColumn2DLineDY: {
            chart: [
                {
                    id: "Chart", group: true, sub: [
                        {
                            id: "Titles (Axis) Names",
                            prop: ["caption", "subCaption", "xAxisName", "pYAxisName", "sYAxisName"]
                        },
                        {
                            id: "Functional Attributes",
                            prop: ["animation", "palette", "paletteColors", "connectNullData", "showLabels", "maxLabelHeight", "labelDisplay", "useEllipsesWhenOverflow", "rotateLabels", "slantLabels", "labelStep", "staggerLines", "showValues", "valuePosition", "showYAxisValues", "showLimits", "showDivLineValues", "showSecondaryLimits", "showDivLineSecondaryValue", "showSum", "yAxisValuesStep", "showShadow", "adjustDiv", "rotateYAxisName", "rotateValues", "clickURL", "pYAxisMinValue", "pYAxisMaxValue", "sYAxisMinValue", "sYAxisMaxValue", "setAdaptiveSYMin", "syncAxisLimits", "pYAxisNameWidth", "sYAxisNameWidth"]
                        },
                        {
                            id: "Chart Caption Cosmetics",
                            prop: ["captionAlignment", "captionOnTop", "captionFontSize", "subCaptionFontSize", "captionFont", "subCaptionFont", "captionFontColor", "subCaptionFontColor", "captionFontBold", "subCaptionFontBold", "alignCaptionWithCanvas", "captionHorizontalPadding"]
                        },
                        {
                            id: "X-axis Name Cosmetics",
                            prop: ["xAxisNameFontColor", "xAxisNameFontSize", "xAxisNameFontBold", "xAxisNameFontItalic", "xAxisNameBgColor", "xAxisNameBorderColor", "xAxisNameAlpha", "xAxisNameFontAlpha", "xAxisNameBgAlpha", "xAxisNameBorderAlpha", "xAxisNameBorderPadding", "xAxisNameBorderRadius", "xAxisNameBorderThickness", "xAxisNameBorderDashed", "xAxisNameBorderDashLen", "xAxisNameBorderDashGap"]
                        },
                        {
                            id: "Y-axis Name Cosmetics",
                            prop: ["pYAxisNameFont", "pYAxisNameFontColor", "pYAxisNameFontSize", "pYAxisNameFontBold", "pYAxisNameFontItalic", "pYAxisNameBgColor", "pYAxisNameBorderColor", "pYAxisNameAlpha", "pYAxisNameFontAlpha", "pYAxisNameBgAlpha", "pYAxisNameBorderAlpha", "pYAxisNameBorderPadding", "pYAxisNameBorderRadius", "pYAxisNameBorderThickness", "pYAxisNameBorderDashed", "pYAxisNameBorderDashLen", "pYAxisNameBorderDashGap"]
                        },
                        {
                            id: "Charts Cosmetics",
                            prop: ["showBorder", "borderColor", "borderThickness", "borderAlpha", "bgColor", "bgAlpha", "bgRatio", "bgAngle", "bgImage", "bgImageAlpha", "bgImageDisplayMode", "bgImageVAlign", "bgImageHAlign", "bgImageScale", "xAxisLineColor", "canvasBgColor", "canvasBgAlpha", "canvasBgRatio", "canvasBgAngle", "canvasBorderColor", "canvasBorderThickness", "canvasBorderAlpha", "showVLineLabelBorder", "logoURL", "logoPosition", "logoAlpha", "logoScale", "logoLink"]
                        },
                        {
                            id: "Anchors",
                            prop: ["drawAnchors", "anchorSides", "anchorRadius", "anchorBorderColor", "anchorBorderThickness", "anchorBgColor", "anchorAlpha", "anchorBgAlpha"]
                        },
                        {
                            id: "Legend Properties",
                            prop: ["showLegend", "legendItemFontBold", "legendItemFont", "legendItemFontSize", "legendItemFontColor", "legendPosition", "legendCaptionAlignment", "legendCaptionBold", "legendCaptionFont", "legendCaptionFontSize", "legendCaptionFontColor", "legendItemHiddenColor", "legendIconScale", "legendBgColor", "legendBgAlpha", "legendBorderColor", "legendBorderThickness", "legendBorderAlpha", "legendShadow", "legendAllowDrag", "legendScrollBgColor", "reverseLegend", "interactiveLegend", "legendNumColumns", "minimiseWrappingInLegend"]
                        },
                        {
                            id: "Divisional Lines/Grids",
                            prop: ["numDivLines", "divLineColor", "divLineThickness", "divLineAlpha", "divLineDashed", "divLineDashLen", "divLineDashGap", "zeroPlaneColor", "zeroPlaneThickness", "zeroPlaneAlpha", "showPZeroPlaneValue", "showSZeroPlaneValue", "showAlternateHGridColor", "alternateHGridColor", "alternateHGridAlpha"]
                        },
                        {
                            id: "Tool-tip",
                            prop: ["showToolTip", "toolTipColor", "toolTipBgColor", "toolTipBorderColor", "toolTipSepChar", "seriesNameInToolTip", "showToolTipShadow"]
                        },
                        {
                            id: "Paddings and Margins",
                            prop: ["legendPadding", "captionPadding", "xAxisNamePadding", "yAxisNamePadding", "yAxisValuesPadding", "labelPadding", "valuePadding", "plotSpacePercent", "chartLeftMargin", "chartRightMargin", "chartTopMargin", "chartBottomMargin", "legendPadding", "canvasLeftMargin", "canvasRightMargin", "canvasTopMargin", "canvasBottomMargin"]
                        }
                    ]
                },
                {
                    id: "Data", group: true, sub: [
                        {
                            id: "Data Plot Cosmetics",
                            prop: ["useRoundEdges", "showPlotBorder", "plotBorderColor", "plotBorderThickness", "plotBorderAlpha", "plotBorderDashed", "plotBorderDashLen", "plotBorderDashGap", "plotFillAngle", "plotFillRatio", "plotFillAlpha", "plotGradientColor", "lineColor", "lineThickness", "lineAlpha", "lineDashed", "lineDashLen", "lineDashGap"]
                        },
                        {
                            id: "Data Value Cosmetics",
                            prop: ["valueFont", "valueFontColor", "valueFontSize", "valueFontBold", "valueFontItalic", "valueBgColor", "valueBorderColor", "valueAlpha", "valueFontAlpha", "valueBgAlpha", "valueBorderAlpha", "valueBorderThickness", "valueBorderRadius", "valueBorderDashed", "valueBorderDashGap", "valueBorderDashLen", "valueHoverAlpha", "valueFontHoverAlpha", "valueBgHoverAlpha", "valueBorderHoverAlpha", "showValuesOnHover"]
                        },
                        {
                            id: "Data Label Cosmetics",
                            prop: ["labelFont", "labelFontColor", "labelFontSize", "labelFontBold", "labelFontItalic", "labelBgColor", "labelBorderColor", "labelAlpha", "labelBgAlpha", "labelBorderAlpha", "labelBorderPadding", "labelBorderRadius", "labelBorderThickness", "labelBorderDashed", "labelBorderDashLen", "labelBorderDashGap", "labelLink"]
                        },
                        {
                            id: "Data Plot Hover Effects",
                            prop: ["showHoverEffect", "plotHoverEffect", "plotFillHoverColor", "plotFillHoverAlpha"]
                        }
                    ]
                },
                {
                    id: "Others", group: true, sub: [
                        {
                            id: "Number Formatting",
                            prop: ["formatNumber", "formatNumberScale", "defaultNumberScale", "numberScaleUnit", "numberScaleValue", "scaleRecursively", "sScaleRecursively", "maxScaleRecursion", "sMaxScaleRecursion", "scaleSeparator", "sScaleSeparator", "numberPrefix", "numberSuffix", "decimalSeparator", "thousandSeparator", "thousandSeparatorPosition", "inDecimalSeparator", "inThousandSeparator", "decimals", "forceDecimals", "forceYAxisValueDecimals", "forceSYAxisValueDecimals", "yAxisValueDecimals", "sFormatNumber", "sFormatNumberScale", "sDefaultNumberScale", "sNumberScaleUnit", "sNumberScaleValue", "sNumberPrefix", "sNumberSuffix", "sDecimals", "sYAxisValueDecimals"]
                        },
                        {
                            id: "Font Properties",
                            prop: ["baseFont", "baseFontSize", "baseFontColor", "outCnvBaseFont", "outCnvBaseFontSize", "outCnvBaseFontColor"]
                        }
                    ]
                }
            ],
            "categories": ["font", "fontSize", "fontColor"],
            "category": ["label", "showLabel", "toolText", "font", "fontColor", "fontSize", "fontBold", "fontItalic", "bgColor", "borderColor", "alpha", "bgAlpha", "borderAlpha", "borderPadding", "borderRadius", "borderThickness", "borderDashed", "borderDashLen", "borderDashGap", "link"],
            "dataset": ["includeInLegend", "seriesName", "color", "alpha", "ratio", "showValues", "valuePosition", "dashed"],
            "set": ["value", "displayValue", "color", "link", "toolText", "showValue", "valuePosition", "alpha", "anchorSides", "anchorRadius", "anchorBorderColor", "anchorBorderThickness", "anchorBgColor", "anchorAlpha", "anchorBgAlpha", "dashed"],
            "lineset": ["includeInLegend", "seriesName", "color", "alpha", "showValues", "valuePosition", "dashed", "lineDashLen", "lineDashGap", "lineThickness", "drawAnchors", "anchorSides", "anchorRadius", "anchorBorderColor", "anchorBorderThickness", "anchorBgColor", "anchorBgAlpha", "anchorAlpha"],
            "line": ["value", "displayValue", "color", "link", "toolText", "showValue", "valuePosition", "alpha", "anchorSides", "anchorRadius", "anchorBorderColor", "anchorBorderThickness", "anchorBgColor", "anchorAlpha", "anchorBgAlpha", "dashed"],
            "Vertical data separator lines": ["color", "thickness", "alpha", "dashed", "dashLen", "dashGap", "label", "showLabelBorder", "linePosition", "labelPosition", "labelHAlign", "labelVAlign"],
            "Trend Lines": ["parentYAxis", "startValue", "endValue", "displayValue", "color", "isTrendZone", "showOnTop", "thickness", "alpha", "dashed", "dashLen", "dashGap", "toolText"]
        },
        Scatter: {
            chart: [
                {
                    id: "Chart", group: true, sub: [
                        {id: "Titles (Axis) Names", prop: ["caption", "subCaption", "xAxisName", "yAxisName"]},
                        {
                            id: "Functional Attributes",
                            prop: ["animation", "palette", "paletteColors", "showLabels", "maxLabelHeight", "labelDisplay", "useEllipsesWhenOverflow", "rotateLabels", "slantLabels", "labelStep", "xAxisLabelMode", "staggerLines", "showValues", "showYAxisValues", "showXAxisValues", "rotateValues", "showLimits", "showVLimits", "showDivLineValues", "showVDivLineValues", "xAxisMinValue", "xAxisMaxValue", "yAxisMinValue", "yAxisMaxValue", "yAxisValuesStep", "xAxisValuesStep", "adjustDiv", "adjustVDiv", "rotateYAxisName", "yAxisNameWidth", "clickURL", "setAdaptiveYMin", "setAdaptiveXMin", "showRegressionLine", "showYOnX", "regressionLineColor", "regressionLineThickness", "regressionLineAlpha"]
                        },
                        {
                            id: "Chart Caption Cosmetics",
                            prop: ["captionAlignment", "captionOnTop", "captionFontSize", "subCaptionFontSize", "captionFont", "subCaptionFont", "captionFontColor", "subCaptionFontColor", "captionFontBold", "subCaptionFontBold", "alignCaptionWithCanvas", "captionHorizontalPadding"]
                        },
                        {
                            id: "X-axis Name Cosmetics",
                            prop: ["xAxisNameFontColor", "xAxisNameFontSize", "xAxisNameFontBold", "xAxisNameFontItalic", "xAxisNameBgColor", "xAxisNameBorderColor", "xAxisNameAlpha", "xAxisNameFontAlpha", "xAxisNameBgAlpha", "xAxisNameBorderAlpha", "xAxisNameBorderPadding", "xAxisNameBorderRadius", "xAxisNameBorderThickness", "xAxisNameBorderDashed", "xAxisNameBorderDashLen", "xAxisNameBorderDashGap"]
                        },
                        {
                            id: "Y-axis Name Cosmetics",
                            prop: ["yAxisNameFont", "yAxisNameFontColor", "yAxisNameFontSize", "yAxisNameFontBold", "yAxisNameFontItalic", "yAxisNameBgColor", "yAxisNameBorderColor", "yAxisNameAlpha", "yAxisNameFontAlpha", "yAxisNameBgAlpha", "yAxisNameBorderAlpha", "yAxisNameBorderPadding", "yAxisNameBorderRadius", "yAxisNameBorderThickness", "yAxisNameBorderDashed", "yAxisNameBorderDashLen", "yAxisNameBorderDashGap"]
                        },
                        {
                            id: "Charts Cosmetics",
                            prop: ["showBorder", "borderColor", "borderThickness", "borderAlpha", "bgColor", "bgAlpha", "bgRatio", "bgAngle", "bgImage", "bgImageAlpha", "bgImageDisplayMode", "bgImageVAlign", "bgImageHAlign", "bgImageScale", "xAxisLineColor", "canvasBgColor", "canvasBgAlpha", "canvasBgRatio", "canvasBgAngle", "canvasBorderColor", "canvasBorderThickness", "canvasBorderAlpha", "logoURL", "logoPosition", "logoAlpha", "logoScale", "logoLink"]
                        },
                        {
                            id: "Quadrants",
                            prop: ["drawQuadrant", "quadrantXVal", "quadrantYVal", "quadrantLineColor", "quadrantLineThickness", "quadrantLineAlpha", "quadrantLineDashed", "quadrantLineDashLen", "quadrantLineDashGap", "quadrantLabelTL", "quadrantLabelTR", "quadrantLabelBL", "quadrantLabelBR", "quadrantLabelPadding"]
                        },
                        {
                            id: "Anchors",
                            prop: ["drawAnchors", "anchorSides", "anchorRadius", "anchorBorderColor", "anchorBorderThickness", "anchorBgColor", "anchorAlpha", "anchorBgAlpha"]
                        },
                        {
                            id: "Legend Properties",
                            prop: ["showLegend", "legendItemFontBold", "legendItemFont", "legendItemFontSize", "legendItemFontColor", "legendPosition", "legendCaptionAlignment", "legendCaptionBold", "legendCaptionFont", "legendCaptionFontSize", "legendCaptionFontColor", "legendItemHiddenColor", "legendIconScale", "legendBgColor", "legendBgAlpha", "legendBorderColor", "legendBorderThickness", "legendBorderAlpha", "legendShadow", "legendAllowDrag", "legendScrollBgColor", "reverseLegend", "interactiveLegend", "legendNumColumns", "minimiseWrappingInLegend"]
                        },
                        {
                            id: "Divisional Lines/Grids",
                            prop: ["numDivLines", "divLineColor", "divLineThickness", "divLineAlpha", "divLineDashed", "divLineDashLen", "divLineDashGap", "showAlternateHGridColor", "alternateHGridColor", "alternateHGridAlpha", "numVDivLines", "vDivLineColor", "vDivLineThickness", "vDivLineAlpha", "vDivLineDashed", "vDivLineDashLen", "vDivLineDashGap", "showAlternateVGridColor", "alternateVGridColor", "alternateVGridAlpha", "showZeroPlane", "zeroPlaneColor", "zeroPlaneThickness", "zeroPlaneAlpha", "showZeroPlaneValue", "showVZeroPlane", "vZeroPlaneColor", "vZeroPlaneThickness", "vZeroPlaneAlpha"]
                        },
                        {
                            id: "Tool-tip",
                            prop: ["showToolTip", "toolTipColor", "toolTipBgColor", "toolTipBorderColor", "toolTipSepChar", "seriesNameInToolTip", "showToolTipShadow"]
                        },
                        {
                            id: "Paddings and Margins",
                            prop: ["captionPadding", "xAxisNamePadding", "yAxisNamePadding", "yAxisValuesPadding", "labelPadding", "chartLeftMargin", "chartRightMargin", "chartTopMargin", "chartBottomMargin", "valuePadding", "legendPadding", "canvasLeftMargin", "canvasRightMargin", "canvasTopMargin", "canvasBottomMargin"]
                        }
                    ]
                },
                {
                    id: "Data", group: true, sub: [
                        {
                            id: "Data Plot Cosmetics",
                            prop: ["lineThickness", "lineAlpha", "lineDashed", "lineDashLen", "lineDashGap"]
                        },
                        {
                            id: "Data Value Cosmetics",
                            prop: ["valueFont", "valueFontColor", "valueFontSize", "valueFontBold", "valueFontItalic", "valueBgColor", "valueBorderColor", "valueAlpha", "valueFontAlpha", "valueBgAlpha", "valueBorderAlpha", "valueBorderThickness", "valueBorderRadius", "valueBorderDashed", "valueBorderDashGap", "valueBorderDashLen", "valueHoverAlpha", "valueFontHoverAlpha", "valueBgHoverAlpha", "valueBorderHoverAlpha", "showValuesOnHover"]
                        },
                        {
                            id: "Data Plot Hover Effects",
                            prop: ["showHoverEffect", "plotHoverEffect", "plotFillHoverColor", "plotFillHoverAlpha"]
                        }
                    ]
                },
                {
                    id: "Others", group: true, sub: [
                        {
                            id: "Number Formatting",
                            prop: ["formatNumber", "yFormatNumber", "xFormatNumber", "formatNumberScale", "yFormatNumberScale", "xFormatNumberScale", "defaultNumberScale", "yDefaultNumberScale", "xDefaultNumberScale", "numberScaleUnit", "yNumberScaleUnit", "xNumberScaleUnit", "numberScaleValue", "scaleRecursively", "xScaleRecursively", "maxScaleRecursion", "xMaxScaleRecursion", "scaleSeparator", "xScaleSeparator", "yNumberScaleValue", "numberPrefix", "yNumberPrefix", "xNumberPrefix", "numberSuffix", "yNumberSuffix", "xNumberSuffix", "decimalSeparator", "thousandSeparator", "thousandSeparatorPosition", "inDecimalSeparator", "inThousandSeparator", "decimals", "forceDecimals", "forceYAxisValueDecimals", "forceXAxisValueDecimals", "yAxisValueDecimals", "xAxisValueDecimals"]
                        },
                        {
                            id: "Font Properties",
                            prop: ["baseFont", "baseFontSize", "baseFontColor", "outCnvBaseFont", "outCnvBaseFontSize", "outCnvBaseFontColor"]
                        }
                    ]
                }
            ],
            "categories": ["font", "fontSize", "fontColor", "verticalLineColor", "verticalLineThickness", "verticalLineAlpha", "verticalLineDashed", "verticalLineDashLen", "verticalLineDashGap"],
            "category": ["x", "label", "showLabel", "showVerticalLine", "lineDashed", "font", "fontColor", "fontSize", "fontBold", "fontItalic", "bgColor", "borderColor", "alpha", "bgAlpha", "borderAlpha", "borderPadding", "borderRadius", "borderThickness", "borderDashed", "borderDashLen", "borderDashGap", "link"],
            "dataset": ["seriesName", "color", "alpha", "showValues", "includeInLegend", "drawAnchors", "anchorSides", "anchorRadius", "anchorBorderColor", "anchorBorderThickness", "anchorBgColor", "anchorAlpha", "anchorBgAlpha", "drawLine", "lineColor", "lineAlpha", "lineThickness", "lineDashed", "lineDashLen", "lineDashGap", "showRegressionLine", "showYOnX", "regressionLineColor", "regressionLineThickness", "regressionLineAlpha"],
            "set": ["x", "y", "displayValue", "link", "toolText", "showValue"],
            "Vertical Trend Lines": ["startValue", "endValue", "displayValue", "color", "isTrendZone", "thickness", "alpha", "dashed", "dashLen", "dashGap", "toolText"],
            "Trend Lines": ["startValue", "endValue", "displayValue", "color", "isTrendZone", "showOnTop", "thickness", "alpha", "dashed", "dashLen", "dashGap", "valueOnRight"]
        },
        Bubble: {
            chart: [
                {
                    id: "Chart", group: true, sub: [
                        {id: "Titles (Axis) Names", prop: ["caption", "subCaption", "xAxisName", "yAxisName"]},
                        {
                            id: "Functional Attributes",
                            prop: ["animation", "palette", "paletteColors", "clipBubbles", "negativeColor", "showLabels", "maxLabelHeight", "labelDisplay", "useEllipsesWhenOverflow", "rotateLabels", "slantLabels", "labelStep", "xAxisLabelMode", "staggerLines", "showValues", "showYAxisValues", "showXAxisValues", "showLimits", "showVLimits", "showDivLineValues", "showVDivLineValues", "xAxisMinValue", "xAxisMaxValue", "yAxisMinValue", "yAxisMaxValue", "use3DLighting", "bubbleScale", "yAxisValuesStep", "xAxisValuesStep", "adjustDiv", "adjustVDiv", "rotateYAxisName", "yAxisNameWidth", "clickURL", "setAdaptiveYMin", "setAdaptiveXMin", "showRegressionLine", "showYOnX", "regressionLineColor", "regressionLineThickness", "regressionLineAlpha"]
                        },
                        {
                            id: "Chart Caption Cosmetics",
                            prop: ["captionAlignment", "captionOnTop", "captionFontSize", "subCaptionFontSize", "captionFont", "subCaptionFont", "captionFontColor", "subCaptionFontColor", "captionFontBold", "subCaptionFontBold", "alignCaptionWithCanvas", "captionHorizontalPadding"]
                        },
                        {
                            id: "X-axis Name Cosmetics",
                            prop: ["xAxisNameFontColor", "xAxisNameFontSize", "xAxisNameFontBold", "xAxisNameFontItalic", "xAxisNameBgColor", "xAxisNameBorderColor", "xAxisNameAlpha", "xAxisNameFontAlpha", "xAxisNameBgAlpha", "xAxisNameBorderAlpha", "xAxisNameBorderPadding", "xAxisNameBorderRadius", "xAxisNameBorderThickness", "xAxisNameBorderDashed", "xAxisNameBorderDashLen", "xAxisNameBorderDashGap"]
                        },
                        {
                            id: "Y-axis Name Cosmetics",
                            prop: ["yAxisNameFont", "yAxisNameFontColor", "yAxisNameFontSize", "yAxisNameFontBold", "yAxisNameFontItalic", "yAxisNameBgColor", "yAxisNameBorderColor", "yAxisNameAlpha", "yAxisNameFontAlpha", "yAxisNameBgAlpha", "yAxisNameBorderAlpha", "yAxisNameBorderPadding", "yAxisNameBorderRadius", "yAxisNameBorderThickness", "yAxisNameBorderDashed", "yAxisNameBorderDashLen", "yAxisNameBorderDashGap"]
                        },
                        {
                            id: "Charts Cosmetics",
                            prop: ["showBorder", "borderColor", "borderThickness", "borderAlpha", "bgColor", "bgAlpha", "bgRatio", "bgAngle", "bgImage", "bgImageAlpha", "bgImageDisplayMode", "bgImageVAlign", "bgImageHAlign", "bgImageScale", "xAxisLineColor", "canvasBgColor", "canvasBgAlpha", "canvasBgRatio", "canvasBgAngle", "canvasBorderColor", "canvasBorderThickness", "canvasBorderAlpha", "logoURL", "logoPosition", "logoAlpha", "logoScale", "logoLink"]
                        },
                        {
                            id: "Quadrants",
                            prop: ["drawQuadrant", "quadrantXVal", "quadrantYVal", "quadrantLineColor", "quadrantLineThickness", "quadrantLineAlpha", "quadrantLineDashed", "quadrantLineDashLen", "quadrantLineDashGap", "quadrantLabelTL", "quadrantLabelTR", "quadrantLabelBL", "quadrantLabelBR", "quadrantLabelPadding"]
                        },
                        {
                            id: "Legend Properties",
                            prop: ["showLegend", "legendItemFontBold", "legendItemFont", "legendItemFontSize", "legendItemFontColor", "legendPosition", "legendCaptionAlignment", "legendCaptionBold", "legendCaptionFont", "legendCaptionFontSize", "legendCaptionFontColor", "legendItemHiddenColor", "legendIconScale", "legendBgColor", "legendBgAlpha", "legendBorderColor", "legendBorderThickness", "legendBorderAlpha", "legendShadow", "legendAllowDrag", "legendScrollBgColor", "interactiveLegend", "legendNumColumns", "minimiseWrappingInLegend"]
                        },
                        {
                            id: "Divisional Lines/Grids",
                            prop: ["numDivLines", "divLineColor", "divLineThickness", "divLineAlpha", "divLineDashed", "divLineDashLen", "divLineDashGap", "showAlternateHGridColor", "alternateHGridColor", "alternateHGridAlpha", "numVDivLines", "vDivLineColor", "vDivLineThickness", "vDivLineAlpha", "vDivLineDashed", "vDivLineDashLen", "vDivLineDashGap", "showAlternateVGridColor", "alternateVGridColor", "alternateVGridAlpha", "showZeroPlane", "showZeroPlaneValue", "zeroPlaneColor", "zeroPlaneThickness", "zeroPlaneAlpha", "showVZeroPlane", "vZeroPlaneColor", "vZeroPlaneThickness", "vZeroPlaneAlpha"]
                        },
                        {
                            id: "Tool-tip",
                            prop: ["showToolTip", "toolTipColor", "toolTipBgColor", "toolTipBorderColor", "toolTipSepChar", "seriesNameInToolTip", "showToolTipShadow"]
                        },
                        {
                            id: "Paddings and Margins",
                            prop: ["captionPadding", "xAxisNamePadding", "yAxisNamePadding", "yAxisValuesPadding", "labelPadding", "chartLeftMargin", "chartRightMargin", "chartTopMargin", "chartBottomMargin", "legendPadding", "canvasLeftMargin", "canvasRightMargin", "canvasTopMargin", "canvasBottomMargin"]
                        }
                    ]
                },
                {
                    id: "Data", group: true, sub: [
                        {
                            id: "Data Plot Cosmetics",
                            prop: ["showPlotBorder", "plotBorderColor", "plotBorderThickness", "plotBorderAlpha", "plotFillAlpha"]
                        },
                        {
                            id: "Data Value Cosmetics",
                            prop: ["valueFont", "valueFontColor", "valueFontSize", "valueFontBold", "valueFontItalic", "valueBgColor", "valueBorderColor", "valueAlpha", "valueFontAlpha", "valueBgAlpha", "valueBorderAlpha", "valueBorderThickness", "valueBorderRadius", "valueBorderDashed", "valueBorderDashGap", "valueBorderDashLen", "valueHoverAlpha", "valueFontHoverAlpha", "valueBgHoverAlpha", "valueBorderHoverAlpha", "showValuesOnHover"]
                        },
                        {
                            id: "Data Plot Hover Effects",
                            prop: ["showHoverEffect", "plotHoverEffect", "plotFillHoverColor", "plotFillHoverAlpha"]
                        }
                    ]
                },
                {
                    id: "Others", group: true, sub: [
                        {
                            id: "Number Formatting",
                            prop: ["formatNumber", "yFormatNumber", "xFormatNumber", "formatNumberScale", "yFormatNumberScale", "xFormatNumberScale", "defaultNumberScale", "yDefaultNumberScale", "xDefaultNumberScale", "numberScaleUnit", "yNumberScaleUnit", "xNumberScaleUnit", "numberScaleValue", "scaleRecursively", "xScaleRecursively", "maxScaleRecursion", "xMaxScaleRecursion", "scaleSeparator", "xScaleSeparator", "yNumberScaleValue", "numberPrefix", "yNumberPrefix", "xNumberPrefix", "numberSuffix", "yNumberSuffix", "xNumberSuffix", "decimalSeparator", "thousandSeparator", "thousandSeparatorPosition", "inDecimalSeparator", "inThousandSeparator", "decimals", "forceDecimals", "forceYAxisValueDecimals", "forceXAxisValueDecimals", "yAxisValueDecimals", "xAxisValueDecimals"]
                        },
                        {
                            id: "Font Properties",
                            prop: ["baseFont", "baseFontSize", "baseFontColor", "outCnvBaseFont", "outCnvBaseFontSize", "outCnvBaseFontColor"]
                        }
                    ]
                }
            ],
            "categories": ["font", "fontSize", "fontColor", "verticalLineColor", "verticalLineThickness", "verticalLineAlpha", "verticalLineDashed", "verticalLineDashLen", "verticalLineDashGap"],
            "category": ["x", "label", "showLabel", "showVerticalLine", "lineDashed", "font", "fontColor", "fontSize", "fontBold", "fontItalic", "bgColor", "borderColor", "alpha", "bgAlpha", "borderAlpha", "borderPadding", "borderRadius", "borderThickness", "borderDashed", "borderDashLen", "borderDashGap", "link"],
            "dataset": ["seriesName", "color", "plotFillAlpha", "showValues", "includeInLegend", "showPlotBorder", "plotBorderColor", "plotBorderThickness", "plotBorderAlpha", "showRegressionLine", "showYOnX", "regressionLineColor", "regressionLineThickness", "regressionLineAlpha"],
            "set": ["x", "y", "z", "displayValue", "name", "color", "link", "toolText", "showValue", "alpha"],
            "Vertical Trend Lines": ["startValue", "endValue", "displayValue", "color", "isTrendZone", "thickness", "alpha", "dashed", "dashLen", "dashGap", "toolText"],
            "Trend Lines": ["startValue", "endValue", "displayValue", "color", "isTrendZone", "showOnTop", "thickness", "alpha", "dashed", "dashLen", "dashGap", "valueOnRight"]
        },
        ScrollColumn2D: {
            chart: [
                {
                    id: "Chart", group: true, sub: [
                        {
                            id: "Titles (Axis) Names",
                            prop: ["caption", "subCaption", "xAxisName", "yAxisName", "rotateYAxisName", "yAxisNameWidth", "centerYaxisName"]
                        },
                        {
                            id: "Functional Attributes",
                            prop: ["numVisiblePlot", "animation", "palette", "paletteColors", "showLabels", "maxLabelHeight", "labelDisplay", "useEllipsesWhenOverflow", "rotateLabels", "slantLabels", "labelStep", "staggerLines", "showValues", "rotateValues", "placeValuesInside", "showYAxisValues", "showLimits", "showDivLineValues", "yAxisValuesStep", "showShadow", "adjustDiv", "clickURL", "rotateYAxisName", "yAxisNameWidth", "yAxisMaxValue", "yAxisMinValue", "setAdaptiveYMin", "scrollToEnd"]
                        },
                        {
                            id: "Chart Caption Cosmetics",
                            prop: ["captionAlignment", "captionOnTop", "captionFontSize", "subCaptionFontSize", "captionFont", "subCaptionFont", "captionFontColor", "subCaptionFontColor", "captionFontBold", "subCaptionFontBold", "alignCaptionWithCanvas", "captionHorizontalPadding"]
                        },
                        {
                            id: "X-axis Name Cosmetics",
                            prop: ["xAxisNameFontColor", "xAxisNameFontSize", "xAxisNameFontBold", "xAxisNameFontItalic", "xAxisNameBgColor", "xAxisNameBorderColor", "xAxisNameAlpha", "xAxisNameFontAlpha", "xAxisNameBgAlpha", "xAxisNameBorderAlpha", "xAxisNameBorderPadding", "xAxisNameBorderRadius", "xAxisNameBorderThickness", "xAxisNameBorderDashed", "xAxisNameBorderDashLen", "xAxisNameBorderDashGap"]
                        },
                        {
                            id: "Y-axis Name Cosmetics",
                            prop: ["yAxisNameFont", "yAxisNameFontColor", "yAxisNameFontSize", "yAxisNameFontBold", "yAxisNameFontItalic", "yAxisNameBgColor", "yAxisNameBorderColor", "yAxisNameAlpha", "yAxisNameFontAlpha", "yAxisNameBgAlpha", "yAxisNameBorderAlpha", "yAxisNameBorderPadding", "yAxisNameBorderRadius", "yAxisNameBorderThickness", "yAxisNameBorderDashed", "yAxisNameBorderDashLen", "yAxisNameBorderDashGap"]
                        },
                        {
                            id: "Charts Cosmetics",
                            prop: ["showBorder", "borderColor", "borderThickness", "borderAlpha", "bgColor", "bgAlpha", "bgRatio", "bgAngle", "bgImage", "bgImageAlpha", "bgImageDisplayMode", "bgImageVAlign", "bgImageHAlign", "bgImageScale", "xAxisLineColor", "canvasBgColor", "canvasBgAlpha", "canvasBgRatio", "canvasBgAngle", "canvasBorderColor", "canvasBorderThickness", "canvasBorderAlpha", "showVLineLabelBorder", "logoURL", "logoPosition", "logoAlpha", "logoScale", "logoLink"]
                        },
                        {
                            id: "Scroll Bar Properties",
                            prop: ["scrollColor", "scrollHeight", "scrollPadding", "scrollBtnWidth", "scrollBtnPadding"]
                        },
                        {
                            id: "Divisional Lines/Grids",
                            prop: ["numDivLines", "divLineColor", "divLineThickness", "divLineAlpha", "divLineIsDashed", "divLineDashLen", "divLineDashGap", "zeroPlaneColor", "zeroPlaneThickness", "zeroPlaneAlpha", "showZeroPlaneValue", "showAlternateHGridColor", "alternateHGridColor", "alternateHGridAlpha"]
                        },
                        {
                            id: "Legend Properties",
                            prop: ["showLegend", "legendItemFontBold", "legendItemFont", "legendItemFontSize", "legendItemFontColor", "legendPosition", "legendCaptionAlignment", "legendCaptionBold", "legendCaptionFont", "legendCaptionFontSize", "legendCaptionFontColor", "legendItemHiddenColor", "legendIconScale", "legendBgColor", "legendBgAlpha", "legendBorderColor", "legendBorderThickness", "legendBorderAlpha", "legendShadow", "legendAllowDrag", "legendScrollBgColor", "reverseLegend", "interactiveLegend", "legendNumColumns", "minimiseWrappingInLegend"]
                        },
                        {
                            id: "Tool-tip",
                            prop: ["showToolTip", "toolTipBgColor", "toolTipBorderColor", "toolTipSepChar", "seriesNameInToolTip", "showToolTipShadow"]
                        },
                        {
                            id: "Paddings and Margins",
                            prop: ["captionPadding", "xAxisNamePadding", "yAxisNamePadding", "yAxisValuesPadding", "labelPadding", "valuePadding", "plotSpacePercent", "chartLeftMargin", "chartRightMargin", "chartTopMargin", "chartBottomMargin", "legendPadding", "canvasLeftMargin", "canvasRightMargin", "canvasTopMargin", "canvasBottomMargin"]
                        }
                    ]
                },
                {
                    id: "Data", group: true, sub: [
                        {
                            id: "Data Plot Cosmetics",
                            prop: ["useRoundEdges", "showPlotBorder", "plotBorderColor", "plotBorderThickness", "plotBorderAlpha", "plotBorderDashed", "plotBorderDashLen", "plotBorderDashGap", "plotFillAngle", "plotFillRatio", "plotFillAlpha", "plotGradientColor"]
                        },
                        {
                            id: "Data Value Cosmetics",
                            prop: ["valueFont", "valueFontColor", "valueFontSize", "valueFontBold", "valueFontItalic", "valueBgColor", "valueBorderColor", "valueAlpha", "valueFontAlpha", "valueBgAlpha", "valueBorderAlpha", "valueBorderThickness", "valueBorderRadius", "valueBorderDashed", "valueBorderDashGap", "valueBorderDashLen", "valueHoverAlpha", "valueFontHoverAlpha", "valueBgHoverAlpha", "valueBorderHoverAlpha", "showValuesOnHover"]
                        },
                        {
                            id: "Data Label Cosmetics",
                            prop: ["labelFont", "labelFontColor", "labelFontSize", "labelFontBold", "labelFontItalic", "labelBgColor", "labelBorderColor", "labelAlpha", "labelBgAlpha", "labelBorderAlpha", "labelBorderPadding", "labelBorderRadius", "labelBorderThickness", "labelBorderDashed", "labelBorderDashLen", "labelBorderDashGap", "labelLink"]
                        },
                        {
                            id: "Data Plot Hover Effects",
                            prop: ["showHoverEffect", "plotHoverEffect", "plotFillHoverColor", "plotFillHoverAlpha"]
                        }
                    ]
                },
                {
                    id: "Others", group: true, sub: [
                        {
                            id: "Number Formatting",
                            prop: ["formatNumber", "formatNumberScale", "defaultNumberScale", "numberScaleUnit", "numberScaleValue", "scaleRecursively", "maxScaleRecursion", "scaleSeparator", "numberPrefix", "numberSuffix", "decimalSeparator", "thousandSeparator", "thousandSeparatorPosition", "inDecimalSeparator", "inThousandSeparator", "decimals", "forceDecimals", "forceYAxisValueDecimals", "yAxisValueDecimals"]
                        },
                        {
                            id: "Font Properties",
                            prop: ["captionPadding", "xAxisNamePadding", "yAxisNamePadding", "yAxisValuesPadding", "labelPadding", "valuePadding", "plotSpacePercent", "chartLeftMargin", "chartRightMargin", "chartTopMargin", "chartBottomMargin", "legendPadding", "canvasLeftMargin", "canvasRightMargin", "canvasTopMargin", "canvasBottomMargin"]
                        }
                    ]
                }
            ],
            "categories": ["font", "fontSize", "fontColor"],
            "category": ["label", "showLabel", "toolText", "font", "fontColor", "fontSize", "fontBold", "fontItalic", "bgColor", "borderColor", "alpha", "bgAlpha", "borderAlpha", "borderPadding", "borderRadius", "borderThickness", "borderDashed", "borderDashLen", "borderDashGap", "link"],
            "dataset": ["seriesName", "color", "alpha", "ratio", "showValues", "dashed", "includeInLegend"],
            "set": ["alpha", "showValue", "dashed", "value", "displayValue", "color", "link", "toolText"],
            "Vertical data separator lines": ["color", "thickness", "alpha", "dashed", "dashLen", "dashGap", "label", "showLabelBorder", "linePosition", "labelPosition", "labelHAlign", "labelVAlign"],
            "Trend Lines": ["startValue", "endValue", "displayValue", "color", "dashGap", "valueOnRight", "isTrendZone", "showOnTop", "thickness", "alpha", "dashed", "dashLen", "toolText"]
        },
        ScrollLine2D: {
            chart: [
                {
                    id: "Chart", group: true, sub: [
                        {id: "Titles (Axis) Names", prop: ["caption", "subCaption", "xAxisName", "yAxisName"]},
                        {
                            id: "Functional Attributes",
                            prop: ["numVisiblePlot", "animation", "palette", "paletteColors", "connectNullData", "showLabels", "maxLabelHeight", "labelDisplay", "useEllipsesWhenOverflow", "rotateLabels", "slantLabels", "labelStep", "staggerLines", "showValues", "valuePosition", "rotateValues", "showYAxisValues", "showLimits", "showDivLineValues", "yAxisMaxValue", "yAxisMinValue", "yAxisNameWidth", "clickURL", "yAxisValuesStep", "showShadow", "adjustDiv", "rotateYAxisName", "setAdaptiveYMin", "scrollToEnd"]
                        },
                        {
                            id: "Chart Caption Cosmetics",
                            prop: ["captionAlignment", "captionOnTop", "captionFontSize", "subCaptionFontSize", "captionFont", "subCaptionFont", "captionFontColor", "subCaptionFontColor", "captionFontBold", "subCaptionFontBold", "alignCaptionWithCanvas", "captionHorizontalPadding"]
                        },
                        {
                            id: "X-axis Name Cosmetics",
                            prop: ["xAxisNameFontColor", "xAxisNameFontSize", "xAxisNameFontBold", "xAxisNameFontItalic", "xAxisNameBgColor", "xAxisNameBorderColor", "xAxisNameAlpha", "xAxisNameFontAlpha", "xAxisNameBgAlpha", "xAxisNameBorderAlpha", "xAxisNameBorderPadding", "xAxisNameBorderRadius", "xAxisNameBorderThickness", "xAxisNameBorderDashed", "xAxisNameBorderDashLen", "xAxisNameBorderDashGap"]
                        },
                        {
                            id: "Y-axis Name Cosmetics",
                            prop: ["yAxisNameFont", "yAxisNameFontColor", "yAxisNameFontSize", "yAxisNameFontBold", "yAxisNameFontItalic", "yAxisNameBgColor", "yAxisNameBorderColor", "yAxisNameAlpha", "yAxisNameFontAlpha", "yAxisNameBgAlpha", "yAxisNameBorderAlpha", "yAxisNameBorderPadding", "yAxisNameBorderRadius", "yAxisNameBorderThickness", "yAxisNameBorderDashed", "yAxisNameBorderDashLen", "yAxisNameBorderDashGap"]
                        },
                        {
                            id: "Charts Cosmetics",
                            prop: ["showBorder", "borderColor", "borderThickness", "borderAlpha", "bgColor", "bgAlpha", "bgRatio", "bgAngle", "bgImage", "bgImageAlpha", "bgImageDisplayMode", "bgImageVAlign", "bgImageHAlign", "bgImageScale", "xAxisLineColor", "canvasBgColor", "canvasBgAlpha", "canvasBgRatio", "canvasBgAngle", "canvasBorderColor", "canvasBorderThickness", "canvasBorderAlpha", "showVLineLabelBorder", "logoURL", "logoPosition", "logoAlpha", "logoScale", "logoLink"]
                        },
                        {
                            id: "Anchors",
                            prop: ["drawAnchors", "anchorSides", "anchorRadius", "anchorBorderColor", "anchorBorderThickness", "anchorBgColor", "anchorAlpha", "anchorBgAlpha"]
                        },
                        {
                            id: "Legend Properties",
                            prop: ["showLegend", "legendItemFontBold", "legendItemFont", "legendItemFontSize", "legendItemFontColor", "legendPosition", "legendCaptionAlignment", "legendCaptionBold", "legendCaptionFont", "legendCaptionFontSize", "legendCaptionFontColor", "legendItemHiddenColor", "legendIconScale", "legendBgColor", "legendBgAlpha", "legendBorderColor", "legendBorderThickness", "legendBorderAlpha", "legendShadow", "legendAllowDrag", "legendScrollBgColor", "reverseLegend", "interactiveLegend", "legendNumColumns", "minimiseWrappingInLegend"]
                        },
                        {
                            id: "Scroll Bar Properties",
                            prop: ["scrollColor", "scrollHeight", "scrollPadding", "scrollBtnWidth", "scrollBtnPadding"]
                        },
                        {
                            id: "Divisional Lines/Grids",
                            prop: ["showZeroPlane", "numVDivLines", "vDivLineColor", "vDivLineThickness", "vDivLineAlpha", "vDivLineDashed", "vDivLineDashLen", "vDivLineDashGap", "showAlternateVGridColor", "alternateVGridColor", "alternateVGridAlpha", "numDivLines", "divLineColor", "divLineThickness", "divLineAlpha", "divLineDashed", "divLineDashLen", "divLineDashGap", "zeroPlaneColor", "zeroPlaneThickness", "zeroPlaneAlpha", "showZeroPlaneValue", "showAlternateHGridColor", "alternateHGridColor", "alternateHGridAlpha"]
                        },
                        {
                            id: "Tool-tip",
                            prop: ["showToolTip", "toolTipColor", "toolTipBgColor", "toolTipBorderColor", "toolTipSepChar", "seriesNameInToolTip", "showToolTipShadow"]
                        },
                        {
                            id: "Paddings and Margins",
                            prop: ["captionPadding", "xAxisNamePadding", "yAxisNamePadding", "yAxisValuesPadding", "labelPadding", "valuePadding", "plotSpacePercent", "chartLeftMargin", "chartRightMargin", "chartTopMargin", "chartBottomMargin", "canvasLeftMargin", "canvasRightMargin", "canvasTopMargin", "canvasBottomMargin"]
                        }
                    ]
                },
                {
                    id: "Data", group: true, sub: [
                        {
                            id: "Data Plot Cosmetics",
                            prop: ["lineColor", "lineThickness", "lineAlpha", "lineDashed", "lineDashLen", "lineDashGap"]
                        },
                        {
                            id: "Data Value Cosmetics",
                            prop: ["valueFont", "valueFontColor", "valueFontSize", "valueFontBold", "valueFontItalic", "valueBgColor", "valueBorderColor", "valueAlpha", "valueFontAlpha", "valueBgAlpha", "valueBorderAlpha", "valueBorderThickness", "valueBorderRadius", "valueBorderDashed", "valueBorderDashGap", "valueBorderDashLen", "valueHoverAlpha", "valueFontHoverAlpha", "valueBgHoverAlpha", "valueBorderHoverAlpha", "showValuesOnHover"]
                        },
                        {
                            id: "Data Label Cosmetics",
                            prop: ["labelFont", "labelFontColor", "labelFontSize", "labelFontBold", "labelFontItalic", "labelBgColor", "labelBorderColor", "labelAlpha", "labelBgAlpha", "labelBorderAlpha", "labelBorderPadding", "labelBorderRadius", "labelBorderThickness", "labelBorderDashed", "labelBorderDashLen", "labelBorderDashGap", "labelLink"]
                        },
                        {
                            id: "Data Plot Hover Effects",
                            prop: ["showHoverEffect", "plotHoverEffect", "plotFillHoverColor", "plotFillHoverAlpha"]
                        }
                    ]
                },
                {
                    id: "Others", group: true, sub: [
                        {
                            id: "Number Formatting",
                            prop: ["formatNumber", "formatNumberScale", "defaultNumberScale", "numberScaleUnit", "numberScaleValue", "scaleRecursively", "maxScaleRecursion", "scaleSeparator", "numberPrefix", "numberSuffix", "decimalSeparator", "thousandSeparator", "thousandSeparatorPosition", "inDecimalSeparator", "inThousandSeparator", "decimals", "forceDecimals", "forceYAxisValueDecimals", "yAxisValueDecimals"]
                        },
                        {
                            id: "Font Properties",
                            prop: ["baseFont", "baseFontSize", "baseFontColor", "outCnvBaseFont", "outCnvBaseFontSize", "outCnvBaseFontColor"]
                        }
                    ]
                }
            ],
            "categories": ["font", "fontSize", "fontColor"],
            "category": ["label", "showLabel", "toolText", "font", "fontColor", "fontSize", "fontBold", "fontItalic", "bgColor", "borderColor", "alpha", "bgAlpha", "borderAlpha", "borderPadding", "borderRadius", "borderThickness", "borderDashed", "borderDashLen", "borderDashGap", "link"],
            "dataset": ["seriesName", "color", "alpha", "showValues", "valuePosition", "dashed", "includeInLegend", "drawAnchors", "anchorSides", "anchorRadius", "lineThickness", "lineDashLen", "lineDashGap", "anchorBorderColor", "anchorBorderThickness", "anchorBgColor", "anchorAlpha", "anchorBgAlpha"],
            "set": ["value", "displayValue", "color", "link", "toolText", "showValue", "valuePosition", "dashed", "alpha", "anchorBgAlpha", "anchorBgColor", "anchorAlpha", "anchorSides", "anchorRadius", "anchorBorderColor", "anchorBorderThickness"],
            "Vertical data separator lines": ["linePosition", "color", "thickness", "showLabelBorder", "label", "labelPosition", "alpha", "dashed", "dashLen", "dashGap", "labelHAlign", "labelVAlign"],
            "Trend Lines": ["startValue", "endValue", "displayValue", "color", "isTrendZone", "showOnTop", "thickness", "alpha", "dashed", "dashLen", "dashGap", "valueOnRight", "toolText"]
        },
        ScrollArea2D: {
            chart: [
                {
                    id: "Chart", group: true, sub: [
                        {id: "Titles (Axis) Names", prop: ["caption", "subCaption", "xAxisName", "yAxisName"]},
                        {
                            id: "Functional Attributes",
                            prop: ["numVisiblePlot", "animation", "palette", "paletteColors", "connectNullData", "showLabels", "maxLabelHeight", "labelDisplay", "useEllipsesWhenOverflow", "rotateLabels", "slantLabels", "labelStep", "staggerLines", "showValues", "rotateValues", "showYAxisValues", "showLimits", "showDivLineValues", "yAxisMaxValue", "yAxisMinValue", "yAxisNameWidth", "clickURL", "yAxisValuesStep", "showShadow", "adjustDiv", "rotateYAxisName", "setAdaptiveYMin", "scrollToEnd"]
                        },
                        {
                            id: "Chart Caption Cosmetics",
                            prop: ["captionAlignment", "captionOnTop", "captionFontSize", "subCaptionFontSize", "captionFont", "subCaptionFont", "captionFontColor", "subCaptionFontColor", "captionFontBold", "subCaptionFontBold", "alignCaptionWithCanvas", "captionHorizontalPadding"]
                        },
                        {
                            id: "X-axis Name Cosmetics",
                            prop: ["xAxisNameFontColor", "xAxisNameFontSize", "xAxisNameFontBold", "xAxisNameFontItalic", "xAxisNameBgColor", "xAxisNameBorderColor", "xAxisNameAlpha", "xAxisNameFontAlpha", "xAxisNameBgAlpha", "xAxisNameBorderAlpha", "xAxisNameBorderPadding", "xAxisNameBorderRadius", "xAxisNameBorderThickness", "xAxisNameBorderDashed", "xAxisNameBorderDashLen", "xAxisNameBorderDashGap"]
                        },
                        {
                            id: "Y-axis Name Cosmetics",
                            prop: ["yAxisNameFont", "yAxisNameFontColor", "yAxisNameFontSize", "yAxisNameFontBold", "yAxisNameFontItalic", "yAxisNameBgColor", "yAxisNameBorderColor", "yAxisNameAlpha", "yAxisNameFontAlpha", "yAxisNameBgAlpha", "yAxisNameBorderAlpha", "yAxisNameBorderPadding", "yAxisNameBorderRadius", "yAxisNameBorderThickness", "yAxisNameBorderDashed", "yAxisNameBorderDashLen", "yAxisNameBorderDashGap"]
                        },
                        {
                            id: "Charts Cosmetics",
                            prop: ["showBorder", "borderColor", "borderThickness", "borderAlpha", "bgColor", "bgAlpha", "bgRatio", "bgAngle", "bgImage", "bgImageAlpha", "bgImageDisplayMode", "bgImageVAlign", "bgImageHAlign", "bgImageScale", "xAxisLineColor", "canvasBgColor", "canvasBgAlpha", "canvasBgRatio", "canvasBgAngle", "canvasBorderColor", "canvasBorderThickness", "canvasBorderAlpha", "showVLineLabelBorder", "logoURL", "logoPosition", "logoAlpha", "logoScale", "logoLink"]
                        },
                        {
                            id: "Anchors",
                            prop: ["drawAnchors", "anchorSides", "anchorRadius", "anchorBorderColor", "anchorBorderThickness", "anchorBgColor", "anchorAlpha", "anchorBgAlpha"]
                        },
                        {
                            id: "Legend Properties",
                            prop: ["showLegend", "legendItemFontBold", "legendItemFont", "legendItemFontSize", "legendItemFontColor", "legendPosition", "legendCaptionAlignment", "legendCaptionBold", "legendCaptionFont", "legendCaptionFontSize", "legendCaptionFontColor", "legendItemHiddenColor", "legendIconScale", "legendBgColor", "legendBgAlpha", "legendBorderColor", "legendBorderThickness", "legendBorderAlpha", "legendShadow", "legendAllowDrag", "legendScrollBgColor", "reverseLegend", "interactiveLegend", "legendNumColumns", "minimiseWrappingInLegend"]
                        },
                        {
                            id: "Scroll Bar Properties",
                            prop: ["scrollColor", "scrollHeight", "scrollPadding", "scrollBtnWidth", "scrollBtnPadding"]
                        },
                        {
                            id: "Divisional Lines/Grids",
                            prop: ["numVDivLines", "vDivLineColor", "vDivLineThickness", "vDivLineAlpha", "vDivLineDashed", "vDivLineDashLen", "vDivLineDashGap", "showAlternateVGridColor", "alternateVGridColor", "alternateVGridAlpha", "numDivLines", "divLineColor", "divLineThickness", "divLineAlpha", "divLineDashed", "divLineDashLen", "divLineDashGap", "zeroPlaneColor", "zeroPlaneThickness", "zeroPlaneAlpha", "showZeroPlaneValue", "showAlternateHGridColor", "alternateHGridColor", "alternateHGridAlpha"]
                        },
                        {
                            id: "Tool-tip",
                            prop: ["showToolTip", "toolTipColor", "toolTipBgColor", "toolTipBorderColor", "toolTipSepChar", "seriesNameInToolTip", "showToolTipShadow"]
                        },
                        {
                            id: "Paddings and Margins",
                            prop: ["captionPadding", "xAxisNamePadding", "yAxisNamePadding", "yAxisValuesPadding", "labelPadding", "valuePadding", "plotSpacePercent", "chartLeftMargin", "chartRightMargin", "chartTopMargin", "chartBottomMargin", "canvasLeftMargin", "canvasRightMargin", "canvasTopMargin", "canvasBottomMargin"]
                        }
                    ]
                },
                {
                    id: "Data", group: true, sub: [
                        {
                            id: "Data Plot Cosmetics",
                            prop: ["showPlotBorder", "plotBorderColor", "plotBorderThickness", "plotBorderAlpha", "plotBorderDashed", "plotBorderDashLen", "plotBorderDashGap", "plotFillAngle", "plotFillAlpha", "plotGradientColor", "plotFillColor"]
                        },
                        {
                            id: "Data Value Cosmetics",
                            prop: ["valueFont", "valueFontColor", "valueFontSize", "valueFontBold", "valueFontItalic", "valueBgColor", "valueBorderColor", "valueAlpha", "valueFontAlpha", "valueBgAlpha", "valueBorderAlpha", "valueBorderThickness", "valueBorderRadius", "valueBorderDashed", "valueBorderDashGap", "valueBorderDashLen", "valueHoverAlpha", "valueFontHoverAlpha", "valueBgHoverAlpha", "valueBorderHoverAlpha", "showValuesOnHover"]
                        },
                        {
                            id: "Data Label Cosmetics",
                            prop: ["labelFont", "labelFontColor", "labelFontSize", "labelFontBold", "labelFontItalic", "labelBgColor", "labelBorderColor", "labelAlpha", "labelBgAlpha", "labelBorderAlpha", "labelBorderPadding", "labelBorderRadius", "labelBorderThickness", "labelBorderDashed", "labelBorderDashLen", "labelBorderDashGap", "labelLink"]
                        },
                        {
                            id: "Data Plot Hover Effects",
                            prop: ["showHoverEffect", "plotHoverEffect", "plotFillHoverColor", "plotFillHoverAlpha"]
                        }
                    ]
                },
                {
                    id: "Others", group: true, sub: [
                        {
                            id: "Number Formatting",
                            prop: ["formatNumber", "formatNumberScale", "defaultNumberScale", "numberScaleUnit", "numberScaleValue", "scaleRecursively", "maxScaleRecursion", "scaleSeparator", "numberPrefix", "numberSuffix", "decimalSeparator", "thousandSeparator", "thousandSeparatorPosition", "inDecimalSeparator", "inThousandSeparator", "decimals", "forceDecimals", "forceYAxisValueDecimals", "yAxisValueDecimals"]
                        },
                        {
                            id: "Font Properties",
                            prop: ["baseFont", "baseFontSize", "baseFontColor", "outCnvBaseFont", "outCnvBaseFontSize", "outCnvBaseFontColor"]
                        }
                    ]
                }
            ],
            "categories": ["font", "fontSize", "fontColor"],
            "category": ["label", "showLabel", "toolText", "font", "fontColor", "fontSize", "fontBold", "fontItalic", "bgColor", "borderColor", "alpha", "bgAlpha", "borderAlpha", "borderPadding", "borderRadius", "borderThickness", "borderDashed", "borderDashLen", "borderDashGap", "link"],
            "dataset": ["seriesName", "color", "alpha", "showValues", "dashed", "includeInLegend", "showPlotBorder", "plotBorderColor", "plotBorderThickness", "plotBorderAlpha", "drawAnchors", "anchorSides", "anchorRadius", "anchorBorderColor", "anchorBorderThickness", "anchorBgColor", "anchorAlpha", "anchorBgAlpha"],
            "set": ["value", "displayValue", "link", "toolText", "showValue", "anchorSides", "anchorRadius", "anchorBorderColor", "anchorBorderThickness", "anchorBgColor", "anchorAlpha", "anchorBgAlpha"],
            "Vertical data separator lines": ["linePosition", "color", "thickness", "showLabelBorder", "label", "labelPosition", "alpha", "dashed", "dashLen", "dashGap", "labelHAlign", "labelVAlign"],
            "Trend Lines": ["startValue", "endValue", "displayValue", "color", "isTrendZone", "showOnTop", "thickness", "alpha", "dashed", "dashLen", "dashGap", "valueOnRight", "toolText"]
        },
        ScrollStackedColumn2D: {
            chart: [
                {
                    id: "Chart", group: true, sub: [
                        {id: "Titles (Axis) Names", prop: ["caption", "subCaption", "xAxisName", "yAxisName"]},
                        {
                            id: "Functional Attributes",
                            prop: ["numVisiblePlot", "animation", "palette", "paletteColors", "showLabels", "maxLabelHeight", "labelDisplay", "useEllipsesWhenOverflow", "rotateLabels", "slantLabels", "labelStep", "staggerLines", "showValues", "rotateValues", "showYAxisValues", "showLimits", "showDivLineValues", "yAxisValuesStep", "showShadow", "yAxisMinValue", "yAxisMaxValue", "showSum", "adjustDiv", "rotateYAxisName", "yAxisNameWidth", "clickURL", "scrollToEnd", "stack100Percent", "showPercentValues", "showPercentInToolTip"]
                        },
                        {
                            id: "Chart Caption Cosmetics",
                            prop: ["captionAlignment", "captionOnTop", "captionFontSize", "subCaptionFontSize", "captionFont", "subCaptionFont", "captionFontColor", "subCaptionFontColor", "captionFontBold", "subCaptionFontBold", "alignCaptionWithCanvas", "captionHorizontalPadding"]
                        },
                        {
                            id: "X-axis Name Cosmetics",
                            prop: ["xAxisNameFontColor", "xAxisNameFontSize", "xAxisNameFontBold", "xAxisNameFontItalic", "xAxisNameBgColor", "xAxisNameBorderColor", "xAxisNameAlpha", "xAxisNameFontAlpha", "xAxisNameBgAlpha", "xAxisNameBorderAlpha", "xAxisNameBorderPadding", "xAxisNameBorderRadius", "xAxisNameBorderThickness", "xAxisNameBorderDashed", "xAxisNameBorderDashLen", "xAxisNameBorderDashGap"]
                        },
                        {
                            id: "Y-axis Name Cosmetics",
                            prop: ["yAxisNameFont", "yAxisNameFontColor", "yAxisNameFontSize", "yAxisNameFontBold", "yAxisNameFontItalic", "yAxisNameBgColor", "yAxisNameBorderColor", "yAxisNameAlpha", "yAxisNameFontAlpha", "yAxisNameBgAlpha", "yAxisNameBorderAlpha", "yAxisNameBorderPadding", "yAxisNameBorderRadius", "yAxisNameBorderThickness", "yAxisNameBorderDashed", "yAxisNameBorderDashLen", "yAxisNameBorderDashGap"]
                        },
                        {
                            id: "Charts Cosmetics",
                            prop: ["showBorder", "borderColor", "borderThickness", "borderAlpha", "bgColor", "bgAlpha", "bgRatio", "bgAngle", "bgImage", "bgImageAlpha", "bgImageDisplayMode", "bgImageVAlign", "bgImageHAlign", "bgImageScale", "xAxisLineColor", "canvasBgColor", "canvasBgAlpha", "canvasBgRatio", "canvasBgAngle", "canvasBorderColor", "canvasBorderThickness", "canvasBorderAlpha", "showVLineLabelBorder", "logoURL", "logoPosition", "logoAlpha", "logoScale", "logoLink"]
                        },
                        {
                            id: "Scroll Bar Properties",
                            prop: ["scrollColor", "scrollHeight", "scrollPadding", "scrollBtnWidth", "scrollBtnPadding"]
                        },
                        {
                            id: "Legend Properties",
                            prop: ["showLegend", "legendItemFontBold", "legendItemFont", "legendItemFontSize", "legendItemFontColor", "legendItemHoverFontColor", "legendCaptionAlignment", "legendCaptionBold", "legendCaptionFont", "legendCaptionFontSize", "legendIconScale", "legendItemHiddenColor", "legendPosition", "legendBgColor", "legendBgAlpha", "legendBorderColor", "legendBorderThickness", "legendBorderAlpha", "legendShadow", "legendAllowDrag", "legendScrollBgColor", "reverseLegend", "interactiveLegend", "legendNumColumns", "minimiseWrappingInLegend"]
                        },
                        {
                            id: "Anchors",
                            prop: ["anchorAlpha", "anchorBgAlpha", "drawAnchors", "anchorSides", "anchorRadius", "anchorBorderColor", "anchorBorderThickness", "anchorBgColor"]
                        },
                        {
                            id: "Divisional Lines/Grids",
                            prop: ["numDivLines", "divLineColor", "divLineThickness", "divLineAlpha", "divLineIsDashed", "divLineDashLen", "divLineDashGap", "zeroPlaneColor", "zeroPlaneThickness", "zeroPlaneAlpha", "showZeroPlaneValue", "showAlternateHGridColor", "alternateHGridColor", "alternateHGridAlpha"]
                        },
                        {
                            id: "Tool-tip",
                            prop: ["showToolTip", "toolTipColor", "toolTipBgColor", "toolTipBorderColor", "toolTipSepChar", "seriesNameInToolTip", "showToolTipShadow"]
                        },
                        {
                            id: "Paddings and Margins",
                            prop: ["captionPadding", "xAxisNamePadding", "yAxisNamePadding", "yAxisValuesPadding", "labelPadding", "valuePadding", "plotSpacePercent", "chartLeftMargin", "chartRightMargin", "chartTopMargin", "chartBottomMargin", "legendPadding", "canvasLeftMargin", "canvasRightMargin", "canvasTopMargin", "canvasBottomMargin"]
                        }
                    ]
                },
                {
                    id: "Data", group: true, sub: [
                        {
                            id: "Data Plot Cosmetics",
                            prop: ["useRoundEdges", "showPlotBorder", "plotBorderColor", "plotBorderThickness", "plotBorderAlpha", "plotBorderDashed", "plotBorderDashLen", "plotBorderDashGap", "plotFillAngle", "plotFillRatio", "plotFillAlpha", "plotGradientColor"]
                        },
                        {
                            id: "Data Value Cosmetics",
                            prop: ["valueFont", "valueFontColor", "valueFontSize", "valueFontBold", "valueFontItalic", "valueBgColor", "valueBorderColor", "valueAlpha", "valueFontAlpha", "valueBgAlpha", "valueBorderAlpha", "valueBorderThickness", "valueBorderRadius", "valueBorderDashed", "valueBorderDashGap", "valueBorderDashLen", "valueHoverAlpha", "valueFontHoverAlpha", "valueBgHoverAlpha", "valueBorderHoverAlpha", "showValuesOnHover"]
                        },
                        {
                            id: "Data Label Cosmetics",
                            prop: ["labelFont", "labelFontColor", "labelFontSize", "labelFontBold", "labelFontItalic", "labelBgColor", "labelBorderColor", "labelAlpha", "labelBgAlpha", "labelBorderAlpha", "labelBorderPadding", "labelBorderRadius", "labelBorderThickness", "labelBorderDashed", "labelBorderDashLen", "labelBorderDashGap", "labelLink"]
                        },
                        {
                            id: "Data Plot Hover Effects",
                            prop: ["showHoverEffect", "plotHoverEffect", "plotFillHoverColor", "plotFillHoverAlpha"]
                        }
                    ]
                },
                {
                    id: "Others", group: true, sub: [
                        {
                            id: "Number Formatting",
                            prop: ["formatNumber", "formatNumberScale", "defaultNumberScale", "numberScaleUnit", "numberScaleValue", "scaleRecursively", "maxScaleRecursion", "scaleSeparator", "numberPrefix", "numberSuffix", "decimalSeparator", "thousandSeparator", "thousandSeparatorPosition", "inDecimalSeparator", "inThousandSeparator", "decimals", "forceDecimals", "forceYAxisValueDecimals", "yAxisValueDecimals"]
                        },
                        {
                            id: "Font Properties",
                            prop: ["baseFont", "baseFontSize", "baseFontColor", "outCnvBaseFont", "outCnvBaseFontSize", "outCnvBaseFontColor"]
                        }
                    ]
                }
            ],
            "categories": ["font", "fontSize", "fontColor"],
            "category": ["label", "showLabel", "toolText", "font", "fontColor", "fontSize", "fontBold", "fontItalic", "bgColor", "borderColor", "alpha", "bgAlpha", "borderAlpha", "borderPadding", "borderRadius", "borderThickness", "borderDashed", "borderDashLen", "borderDashGap", "link"],
            "dataset": ["seriesName", "color", "alpha", "ratio", "showValues", "dashed", "includeInLegend"],
            "set": ["label", "value", "displayValue", "color", "link", "toolText", "showLabel", "showValue", "dashed", "alpha"],
            "Vertical data separator lines": ["linePosition", "color", "thickness", "showLabelBorder", "label", "labelPosition", "alpha", "dashed", "dashLen", "dashGap", "labelHAlign", "labelVAlign"],
            "Trend Lines": ["startValue", "endValue", "displayValue", "color", "isTrendZone", "showOnTop", "thickness", "alpha", "dashed", "dashLen", "dashGap", "valueOnRight", "toolText"]
        },
        ScrollCombi2D: {
            chart: [
                {
                    id: "Chart", group: true, sub: [
                        {id: "Titles (Axis) Names", prop: ["caption", "subCaption", "xAxisName", "yAxisName"]},
                        {
                            id: "Functional Attributes",
                            prop: ["numVisiblePlot", "animation", "palette", "paletteColors", "connectNullData", "areaOverColumns", "showLabels", "maxLabelHeight", "labelDisplay", "useEllipsesWhenOverflow", "rotateLabels", "slantLabels", "labelStep", "staggerLines", "showValues", "valuePosition", "rotateValues", "placeValuesInside", "showYAxisValues", "showLimits", "showDivLineValues", "yAxisValuesStep", "showShadow", "adjustDiv", "clickURL", "rotateYAxisName", "yAxisNameWidth", "yAxisMaxValue", "yAxisMinValue", "setAdaptiveYMin", "scrollToEnd"]
                        },
                        {
                            id: "Chart Caption Cosmetics",
                            prop: ["captionAlignment", "captionOnTop", "captionFontSize", "subCaptionFontSize", "captionFont", "subCaptionFont", "captionFontColor", "subCaptionFontColor", "captionFontBold", "subCaptionFontBold", "alignCaptionWithCanvas", "captionHorizontalPadding"]
                        },
                        {
                            id: "X-axis Name Cosmetics",
                            prop: ["xAxisNameFontColor", "xAxisNameFontSize", "xAxisNameFontBold", "xAxisNameFontItalic", "xAxisNameBgColor", "xAxisNameBorderColor", "xAxisNameAlpha", "xAxisNameFontAlpha", "xAxisNameBgAlpha", "xAxisNameBorderAlpha", "xAxisNameBorderPadding", "xAxisNameBorderRadius", "xAxisNameBorderThickness", "xAxisNameBorderDashed", "xAxisNameBorderDashLen", "xAxisNameBorderDashGap"]
                        },
                        {
                            id: "Y-axis Name Cosmetics",
                            prop: ["yAxisNameFont", "yAxisNameFontColor", "yAxisNameFontSize", "yAxisNameFontBold", "yAxisNameFontItalic", "yAxisNameBgColor", "yAxisNameBorderColor", "yAxisNameAlpha", "yAxisNameFontAlpha", "yAxisNameBgAlpha", "yAxisNameBorderAlpha", "yAxisNameBorderPadding", "yAxisNameBorderRadius", "yAxisNameBorderThickness", "yAxisNameBorderDashed", "yAxisNameBorderDashLen", "yAxisNameBorderDashGap"]
                        },
                        {
                            id: "Charts Cosmetics",
                            prop: ["showBorder", "borderColor", "borderThickness", "borderAlpha", "bgColor", "bgAlpha", "bgRatio", "bgAngle", "bgImage", "bgImageAlpha", "bgImageDisplayMode", "bgImageVAlign", "bgImageHAlign", "bgImageScale", "xAxisLineColor", "canvasBgColor", "canvasBgAlpha", "canvasBgRatio", "canvasBgAngle", "canvasBorderColor", "canvasBorderThickness", "canvasBorderAlpha", "showVLineLabelBorder", "logoURL", "logoPosition", "logoAlpha", "logoScale", "logoLink"]
                        },
                        {
                            id: "Scroll Bar Properties",
                            prop: ["scrollColor", "scrollHeight", "scrollPadding", "scrollBtnWidth", "scrollBtnPadding"]
                        },
                        {
                            id: "Anchors",
                            prop: ["drawAnchors", "anchorSides", "anchorRadius", "anchorBorderColor", "anchorBorderThickness", "anchorBgColor", "anchorAlpha", "anchorBgAlpha"]
                        },
                        {
                            id: "Legend Properties",
                            prop: ["showLegend", "legendItemFontBold", "legendItemFont", "legendItemFontSize", "legendItemFontColor", "legendItemHoverFontColor", "legendCaptionAlignment", "legendCaptionBold", "legendCaptionFont", "legendCaptionFontSize", "legendIconScale", "legendItemHiddenColor", "legendPosition", "legendBgColor", "legendBgAlpha", "legendBorderColor", "legendBorderThickness", "legendBorderAlpha", "legendShadow", "legendAllowDrag", "legendScrollBgColor", "reverseLegend", "interactiveLegend", "legendNumColumns", "minimiseWrappingInLegend"]
                        },
                        {
                            id: "Divisional Lines/Grids",
                            prop: ["numDivLines", "divLineColor", "divLineThickness", "divLineAlpha", "divLineDashed", "divLineDashLen", "divLineDashGap", "zeroPlaneColor", "zeroPlaneThickness", "zeroPlaneAlpha", "showZeroPlaneValue", "showAlternateHGridColor", "alternateHGridColor", "alternateHGridAlpha"]
                        },
                        {
                            id: "Tool-tip",
                            prop: ["showToolTip", "toolTipColor", "toolTipBgColor", "toolTipBorderColor", "toolTipSepChar", "seriesNameInToolTip", "showToolTipShadow"]
                        },
                        {
                            id: "Paddings and Margins",
                            prop: ["captionPadding", "xAxisNamePadding", "yAxisNamePadding", "yAxisValuesPadding", "labelPadding", "valuePadding", "plotSpacePercent", "chartLeftMargin", "chartRightMargin", "chartTopMargin", "chartBottomMargin", "legendPadding", "canvasLeftMargin", "canvasRightMargin", "canvasTopMargin", "canvasBottomMargin"]
                        }
                    ]
                },
                {
                    id: "Data", group: true, sub: [
                        {
                            id: "Data Plot Cosmetics",
                            prop: ["useRoundEdges", "showPlotBorder", "plotBorderColor", "plotBorderThickness", "plotBorderAlpha", "plotBorderDashed", "plotBorderDashLen", "plotBorderDashGap", "plotFillAngle", "plotFillAlpha", "plotGradientColor", "lineColor", "lineThickness", "lineAlpha", "lineDashed", "lineDashLen", "lineDashGap"]
                        },
                        {
                            id: "Data Value Cosmetics",
                            prop: ["valueFont", "valueFontColor", "valueFontSize", "valueFontBold", "valueFontItalic", "valueBgColor", "valueBorderColor", "valueAlpha", "valueFontAlpha", "valueBgAlpha", "valueBorderAlpha", "valueBorderThickness", "valueBorderRadius", "valueBorderDashed", "valueBorderDashGap", "valueBorderDashLen", "valueHoverAlpha", "valueFontHoverAlpha", "valueBgHoverAlpha", "valueBorderHoverAlpha", "showValuesOnHover"]
                        },
                        {
                            id: "Data Label Cosmetics",
                            prop: ["labelFont", "labelFontColor", "labelFontSize", "labelFontBold", "labelFontItalic", "labelBgColor", "labelBorderColor", "labelAlpha", "labelBgAlpha", "labelBorderAlpha", "labelBorderPadding", "labelBorderRadius", "labelBorderThickness", "labelBorderDashed", "labelBorderDashLen", "labelBorderDashGap", "labelLink"]
                        },
                        {
                            id: "Data Plot Hover Effects",
                            prop: ["showHoverEffect", "plotHoverEffect", "plotFillHoverColor", "plotFillHoverAlpha"]
                        }
                    ]
                },
                {
                    id: "Others", group: true, sub: [
                        {
                            id: "Number Formatting",
                            prop: ["formatNumber", "formatNumberScale", "defaultNumberScale", "numberScaleUnit", "numberScaleValue", "scaleRecursively", "maxScaleRecursion", "scaleSeparator", "numberPrefix", "numberSuffix", "decimalSeparator", "thousandSeparator", "thousandSeparatorPosition", "inDecimalSeparator", "inThousandSeparator", "decimals", "forceDecimals", "forceYAxisValueDecimals", "yAxisValueDecimals"]
                        },
                        {
                            id: "Font Properties",
                            prop: ["baseFont", "baseFontSize", "baseFontColor", "outCnvBaseFont", "outCnvBaseFontSize", "outCnvBaseFontColor"]
                        }
                    ]
                }
            ],
            "categories": ["font", "fontSize", "fontColor"],
            "category": ["label", "showLabel", "toolText", "font", "fontColor", "fontSize", "fontBold", "fontItalic", "bgColor", "borderColor", "alpha", "bgAlpha", "borderAlpha", "borderPadding", "borderRadius", "borderThickness", "borderDashed", "borderDashLen", "borderDashGap", "link"],
            "dataset": ["renderAs", "parentYAxis", "plotBorderThickness", "showPlotBorder", "seriesName", "color", "alpha", "showValues", "valuePosition", "dashed", "includeInLegend", "drawAnchors", "anchorSides", "anchorRadius", "anchorBorderColor", "anchorBorderThickness", "anchorBgColor", "anchorAlpha", "lineThickness", "plotBorderAlpha", "plotBorderColor", "lineDashLen", "lineDashGap"],
            "set": ["anchorSides", "anchorRadius", "anchorBorderColor", "anchorBorderThickness", "anchorBgAlpha", "anchorBgColor", "anchorAlpha", "value", "displayValue", "color", "link", "toolText", "showValue", "valuePosition", "dashed", "alpha"],
            "Vertical data separator lines": ["linePosition", "color", "thickness", "showLabelBorder", "label", "labelPosition", "alpha", "dashed", "dashLen", "dashGap", "labelHAlign", "labelVAlign"],
            "Trend Lines": ["startValue", "endValue", "displayValue", "color", "isTrendZone", "showOnTop", "thickness", "alpha", "dashed", "dashLen", "dashGap", "valueOnRight", "toolText"]
        },
        ScrollCombiDY2D: {
            chart: [
                {
                    id: "Chart", group: true, sub: [
                        {
                            id: "Titles (Axis) Names",
                            prop: ["caption", "subCaption", "xAxisName", "sYAxisName", "pYAxisName"]
                        },
                        {
                            id: "Functional Attributes",
                            prop: ["numVisiblePlot", "animation", "palette", "paletteColors", "connectNullData", "areaOverColumns", "showLabels", "maxLabelHeight", "labelDisplay", "useEllipsesWhenOverflow", "rotateLabels", "slantLabels", "labelStep", "staggerLines", "showValues", "valuePosition", "rotateValues", "placeValuesInside", "showYAxisValues", "showLimits", "showDivLineValues", "showSecondaryLimits", "showDivLineSecondaryValue", "clickURL", "adjustDiv", "rotateYAxisName", "yAxisValuesStep", "showShadow", "pYAxisMaxValue", "sYAxisMinValue", "sYAxisMaxValue", "pYAxisMinValue", "setAdaptiveYMin", "setAdaptiveSYMin", "syncAxisLimits", "pYAxisNameWidth", "sYAxisNameWidth", "scrollToEnd"]
                        },
                        {
                            id: "Chart Caption Cosmetics",
                            prop: ["captionAlignment", "captionOnTop", "captionFontSize", "subCaptionFontSize", "captionFont", "subCaptionFont", "captionFontColor", "subCaptionFontColor", "captionFontBold", "subCaptionFontBold", "alignCaptionWithCanvas", "captionHorizontalPadding"]
                        },
                        {
                            id: "X-axis Name Cosmetics",
                            prop: ["xAxisNameFontColor", "xAxisNameFontSize", "xAxisNameFontBold", "xAxisNameFontItalic", "xAxisNameBgColor", "xAxisNameBorderColor", "xAxisNameAlpha", "xAxisNameFontAlpha", "xAxisNameBgAlpha", "xAxisNameBorderAlpha", "xAxisNameBorderPadding", "xAxisNameBorderRadius", "xAxisNameBorderThickness", "xAxisNameBorderDashed", "xAxisNameBorderDashLen", "xAxisNameBorderDashGap"]
                        },
                        {
                            id: "Y-axis Name Cosmetics",
                            prop: ["pYAxisNameFont", "pYAxisNameFontColor", "pYAxisNameFontSize", "pYAxisNameFontBold", "pYAxisNameFontItalic", "pYAxisNameBgColor", "pYAxisNameBorderColor", "pYAxisNameAlpha", "pYAxisNameFontAlpha", "pYAxisNameBgAlpha", "pYAxisNameBorderAlpha", "pYAxisNameBorderPadding", "pYAxisNameBorderRadius", "pYAxisNameBorderThickness", "pYAxisNameBorderDashed", "pYAxisNameBorderDashLen", "pYAxisNameBorderDashGap"]
                        },
                        {
                            id: "Charts Cosmetics",
                            prop: ["showBorder", "borderColor", "borderThickness", "borderAlpha", "bgColor", "bgAlpha", "bgRatio", "bgAngle", "bgImage", "bgImageAlpha", "bgImageDisplayMode", "bgImageVAlign", "bgImageHAlign", "bgImageScale", "xAxisLineColor", "canvasBgColor", "canvasBgAlpha", "canvasBgRatio", "canvasBgAngle", "canvasBorderColor", "canvasBorderThickness", "canvasBorderAlpha", "showVLineLabelBorder", "logoURL", "logoPosition", "logoAlpha", "logoScale", "logoLink"]
                        },
                        {
                            id: "Scroll Bar Properties",
                            prop: ["scrollColor", "scrollHeight", "scrollPadding", "scrollBtnWidth", "scrollBtnPadding"]
                        },
                        {
                            id: "Anchors",
                            prop: ["drawAnchors", "anchorSides", "anchorRadius", "anchorBorderColor", "anchorBorderThickness", "anchorBgColor", "anchorAlpha", "anchorBgAlpha"]
                        },
                        {
                            id: "Legend Properties",
                            prop: ["showLegend", "legendItemFontBold", "legendItemFont", "legendItemFontSize", "legendItemFontColor", "legendPosition", "legendCaptionAlignment", "legendCaptionBold", "legendCaptionFont", "legendCaptionFontSize", "legendCaptionFontColor", "legendItemHiddenColor", "legendIconScale", "legendBgColor", "legendBgAlpha", "legendBorderColor", "legendBorderThickness", "legendBorderAlpha", "legendShadow", "legendAllowDrag", "legendScrollBgColor", "reverseLegend", "interactiveLegend", "legendNumColumns", "minimiseWrappingInLegend"]
                        },
                        {
                            id: "Divisional Lines/Grids",
                            prop: ["numDivLines", "divLineColor", "divLineThickness", "divLineAlpha", "divLineDashed", "divLineDashLen", "divLineDashGap", "zeroPlaneColor", "zeroPlaneThickness", "zeroPlaneAlpha", "showPZeroPlaneValue", "showSZeroPlaneValue", "showAlternateHGridColor", "alternateHGridColor", "alternateHGridAlpha"]
                        },
                        {
                            id: "Tool-tip",
                            prop: ["showToolTip", "toolTipColor", "toolTipBgColor", "toolTipBorderColor", "toolTipSepChar", "seriesNameInToolTip", "showToolTipShadow"]
                        },
                        {
                            id: "Paddings and Margins",
                            prop: ["captionPadding", "xAxisNamePadding", "yAxisNamePadding", "yAxisValuesPadding", "labelPadding", "valuePadding", "plotSpacePercent", "chartLeftMargin", "chartRightMargin", "chartTopMargin", "chartBottomMargin", "legendPadding", "canvasLeftMargin", "canvasRightMargin", "canvasTopMargin", "canvasBottomMargin"]
                        }
                    ]
                },
                {
                    id: "Data", group: true, sub: [
                        {
                            id: "Data Plot Cosmetics",
                            prop: ["useRoundEdges", "showPlotBorder", "plotBorderColor", "plotBorderThickness", "plotBorderAlpha", "plotBorderDashed", "plotBorderDashLen", "plotBorderDashGap", "plotFillAngle", "plotFillAlpha", "plotGradientColor", "lineColor", "lineThickness", "lineAlpha", "lineDashed", "lineDashLen", "lineDashGap"]
                        },
                        {
                            id: "Data Value Cosmetics",
                            prop: ["valueFont", "valueFontColor", "valueFontSize", "valueFontBold", "valueFontItalic", "valueBgColor", "valueBorderColor", "valueAlpha", "valueFontAlpha", "valueBgAlpha", "valueBorderAlpha", "valueBorderThickness", "valueBorderRadius", "valueBorderDashed", "valueBorderDashGap", "valueBorderDashLen", "valueHoverAlpha", "valueFontHoverAlpha", "valueBgHoverAlpha", "valueBorderHoverAlpha", "showValuesOnHover"]
                        },
                        {
                            id: "Data Label Cosmetics",
                            prop: ["labelFont", "labelFontColor", "labelFontSize", "labelFontBold", "labelFontItalic", "labelBgColor", "labelBorderColor", "labelAlpha", "labelBgAlpha", "labelBorderAlpha", "labelBorderPadding", "labelBorderRadius", "labelBorderThickness", "labelBorderDashed", "labelBorderDashLen", "labelBorderDashGap", "labelLink"]
                        },
                        {
                            id: "Data Plot Hover Effects",
                            prop: ["showHoverEffect", "plotHoverEffect", "plotFillHoverColor", "plotFillHoverAlpha"]
                        }
                    ]
                },
                {
                    id: "Others", group: true, sub: [
                        {
                            id: "Number Formatting",
                            prop: ["formatNumber", "formatNumberScale", "defaultNumberScale", "numberScaleUnit", "numberScaleValue", "scaleRecursively", "sScaleRecursively", "maxScaleRecursion", "sMaxScaleRecursion", "scaleSeparator", "sScaleSeparator", "numberPrefix", "numberSuffix", "decimalSeparator", "thousandSeparator", "thousandSeparatorPosition", "inDecimalSeparator", "inThousandSeparator", "decimals", "forceDecimals", "forceYAxisValueDecimals", "forceSYAxisValueDecimals", "yAxisValueDecimals", "sFormatNumber", "sFormatNumberScale", "sDefaultNumberScale", "sNumberScaleUnit", "sNumberScaleValue", "sNumberPrefix", "sNumberSuffix", "sDecimals", "sYAxisValueDecimals"]
                        },
                        {
                            id: "Font Properties",
                            prop: ["baseFont", "baseFontSize", "baseFontColor", "outCnvBaseFont", "outCnvBaseFontSize", "outCnvBaseFontColor"]
                        }
                    ]
                }
            ],
            "categories": ["font", "fontSize", "fontColor"],
            "category": ["label", "showLabel", "toolText", "font", "fontColor", "fontSize", "fontBold", "fontItalic", "bgColor", "borderColor", "alpha", "bgAlpha", "borderAlpha", "borderPadding", "borderRadius", "borderThickness", "borderDashed", "borderDashLen", "borderDashGap", "link"],
            "dataset": ["renderAs", "parentYAxis", "showPlotBorder", "plotBorderColor", "plotBorderThickness", "plotBorderAlpha", "seriesName", "color", "alpha", "showValues", "valuePosition", "dashed", "includeInLegend", "drawAnchors", "anchorSides", "anchorRadius", "anchorBorderColor", "anchorBorderThickness", "anchorBgColor", "anchorAlpha", "lineThickness", "lineDashLen", "lineDashGap"],
            "set": ["anchorSides", "alpha", "link", "toolText", "showValue", "valuePosition", "dashed", "anchorRadius", "anchorBorderColor", "anchorBorderThickness", "anchorBgColor", "anchorAlpha", "anchorBgAlpha", "value", "displayValue", "color"],
            "Vertical data separator lines": ["linePosition", "color", "thickness", "showLabelBorder", "label", "labelPosition", "alpha", "dashed", "dashLen", "dashGap", "labelHAlign", "labelVAlign"],
            "Trend Lines": ["parentYAxis", "startValue", "endValue", "displayValue", "color", "isTrendZone", "showOnTop", "thickness", "alpha", "dashed", "dashLen", "dashGap", "toolText"]
        },
        Funnel: {
            chart: [
                {
                    id: "Chart", group: true, sub: [
                        {id: "Titles (Axis) Names", prop: ["caption", "subCaption"]},
                        {
                            id: "Functional Attributes",
                            prop: ["animation", "clickURL", "palette", "paletteThemeColor", "annRenderDelay", "showPrintMenuItem", "manageResize", "useEllipsesWhenOverflow"]
                        },
                        {
                            id: "Funnel Functional Properties",
                            prop: ["streamlinedData", "is2D", "isSliced", "isHollow", "useSameSlantAngle", "funnelYScale"]
                        },
                        {
                            id: "Charts Cosmetics",
                            prop: ["bgColor", "bgAlpha", "bgRatio", "bgAngle", "showBorder", "borderColor", "borderThickness", "borderAlpha", "bgImage", "bgImageAlpha", "bgImageDisplayMode", "bgImageVAlign", "bgImageHAlign", "bgImageScale", "logoURL", "logoPosition", "logoAlpha", "logoScale", "logoLink"]
                        },
                        {
                            id: "Chart Caption Cosmetics",
                            prop: ["captionAlignment", "captionOnTop", "captionFontSize", "subCaptionFontSize", "captionFont", "subCaptionFont", "captionFontColor", "subCaptionFontColor", "captionFontBold", "subCaptionFontBold", "alignCaptionWithCanvas", "captionHorizontalPadding"]
                        },
                        {
                            id: "Legend Properties",
                            prop: ["showLegend", "legendPosition", "legendCaption", "legendItemFontBold", "legendItemFont", "legendItemFontSize", "legendItemFontColor", "legendItemHiddenColor", "legendCaptionAlignment", "legendCaptionBold", "legendCaptionFont", "legendCaptionFontSize", "legendCaptionFontColor", "legendIconScale", "legendBgColor", "legendBgAlpha", "legendBorderColor", "legendBorderThickness", "legendBorderAlpha", "legendShadow", "legendAllowDrag", "legendScrollBgColor", "legendScrollBarColor", "legendScrollBtnColor", "reverseLegend", "interactiveLegend", "legendNumColumns", "minimiseWrappingInLegend"]
                        },
                        {
                            id: "Tool-tip",
                            prop: ["showToolTip", "showPercentInToolTip", "toolTipBgColor", "toolTipColor", "toolTipBorderColor", "toolTipSepChar", "showToolTipShadow"]
                        },
                        {
                            id: "Paddings and Margins",
                            prop: ["captionPadding", "chartLeftMargin", "chartRightMargin", "chartTopMargin", "chartBottomMargin"]
                        }
                    ]
                },
                {
                    id: "Data", group: true, sub: [
                        {
                            id: "Data Plot Cosmetics",
                            prop: ["showPlotBorder", "plotBorderColor", "plotBorderThickness", "plotBorderAlpha", "plotFillAlpha"]
                        },
                        {
                            id: "Data Label Cosmetics",
                            prop: ["labelFont", "labelFontColor", "labelFontSize", "labelFontBold", "labelFontItalic", "labelBgColor", "labelBorderColor", "labelAlpha", "labelBgAlpha", "labelBorderAlpha", "labelBorderPadding", "labelBorderRadius", "labelBorderThickness", "labelBorderDashed", "labelBorderDashLen", "labelBorderDashGap", "labelLink"]
                        },
                        {
                            id: "Chart Labels and Values",
                            prop: ["showLabels", "showLabelsAtCenter", "labelDistance", "labelSepChar", "enableSmartLabels", "smartLineColor", "smartLineThickness", "smartLineAlpha", "showValues", "showPercentValues", "percentOfPrevious"]
                        }
                    ]
                },
                {
                    id: "Others", group: true, sub: [
                        {
                            id: "Number Formatting",
                            prop: ["formatNumber", "numberPrefix", "numberSuffix", "decimals", "forceDecimals", "formatNumberScale", "defaultNumberScale", "numberScaleUnit", "numberScaleValue", "scaleRecursively", "maxScaleRecursion", "scaleSeparator", "decimalSeparator", "thousandSeparator", "thousandSeparatorPosition", "inDecimalSeparator", "inThousandSeparator"]
                        },
                        {id: "Font Properties", prop: ["baseFont", "baseFontSize", "baseFontColor"]}
                    ]
                }
            ],
            "set": ["label", "value", "link", "toolText", "color", "alpha", "showValue", "borderColor", "borderAlpha", "isSliced", "labelFont", "labelFontColor", "labelFontSize", "labelFontBold", "labelFontItalic", "labelBgColor", "labelBorderColor", "labelAlpha", "labelBgAlpha", "labelBorderAlpha", "labelBorderPadding", "labelBorderRadius", "labelBorderThickness", "labelBorderDashed", "labelBorderDashLen", "labelBorderDashGap", "labelLink"]
        },
        Pyramid: {
            chart: [
                {
                    id: "Chart", group: true, sub: [
                        {id: "Titles (Axis) Names", prop: ["caption", "subCaption"]},
                        {
                            id: "Functional Attributes",
                            prop: ["animation", "clickURL", "palette", "paletteThemeColor", "annRenderDelay", "showPrintMenuItem", "manageResize", "useEllipsesWhenOverflow"]
                        },
                        {id: "Pyramid Functional Properties", prop: ["is2D", "isSliced", "pyramidYScale"]},
                        {
                            id: "Charts Cosmetics",
                            prop: ["bgColor", "bgAlpha", "bgRatio", "bgAngle", "showBorder", "borderColor", "borderThickness", "borderAlpha", "bgImage", "bgImageAlpha", "bgImageDisplayMode", "bgImageVAlign", "bgImageHAlign", "bgImageScale", "logoURL", "logoPosition", "logoAlpha", "logoScale", "logoLink"]
                        },
                        {
                            id: "Chart Caption Cosmetics",
                            prop: ["captionAlignment", "captionOnTop", "captionFontSize", "subCaptionFontSize", "captionFont", "subCaptionFont", "captionFontColor", "subCaptionFontColor", "captionFontBold", "subCaptionFontBold", "alignCaptionWithCanvas", "captionHorizontalPadding"]
                        },
                        {
                            id: "Legend Properties",
                            prop: ["showLegend", "legendPosition", "legendCaption", "legendItemFontBold", "legendItemFont", "legendItemFontSize", "legendItemFontColor", "legendItemHiddenColor", "legendCaptionAlignment", "legendCaptionBold", "legendCaptionFont", "legendCaptionFontSize", "legendCaptionFontColor", "legendIconScale", "legendBgColor", "legendBgAlpha", "legendBorderColor", "legendBorderThickness", "legendBorderAlpha", "legendShadow", "legendAllowDrag", "legendScrollBgColor", "legendScrollBarColor", "legendScrollBtnColor", "reverseLegend", "interactiveLegend", "legendNumColumns", "minimiseWrappingInLegend"]
                        },
                        {
                            id: "Tool-tip",
                            prop: ["showToolTip", "showPercentInToolTip", "toolTipBgColor", "toolTipColor", "toolTipBorderColor", "toolTipSepChar", "showToolTipShadow"]
                        },
                        {
                            id: "Paddings and Margins",
                            prop: ["captionPadding", "chartLeftMargin", "chartRightMargin", "chartTopMargin", "chartBottomMargin"]
                        }
                    ]
                },
                {
                    id: "Data", group: true, sub: [
                        {
                            id: "Data Plot Cosmetics",
                            prop: ["showPlotBorder", "plotBorderColor", "plotBorderThickness", "plotBorderAlpha", "plotFillAlpha"]
                        },
                        {
                            id: "Data Label Cosmetics",
                            prop: ["labelFont", "labelFontColor", "labelFontSize", "labelFontBold", "labelFontItalic", "labelBgColor", "labelBorderColor", "labelAlpha", "labelBgAlpha", "labelBorderAlpha", "labelBorderPadding", "labelBorderRadius", "labelBorderThickness", "labelBorderDashed", "labelBorderDashLen", "labelBorderDashGap", "labelLink"]
                        },
                        {
                            id: "Chart Labels and Values",
                            prop: ["showLabels", "showLabelsAtCenter", "labelDistance", "labelSepChar", "enableSmartLabels", "smartLineColor", "smartLineThickness", "smartLineAlpha", "showValues", "showPercentValues"]
                        }
                    ]
                },
                {
                    id: "Others", group: true, sub: [
                        {
                            id: "Number Formatting",
                            prop: ["formatNumber", "numberPrefix", "numberSuffix", "decimals", "forceDecimals", "formatNumberScale", "defaultNumberScale", "numberScaleUnit", "numberScaleValue", "scaleRecursively", "maxScaleRecursion", "scaleSeparator", "decimalSeparator", "thousandSeparator", "thousandSeparatorPosition", "inDecimalSeparator", "inThousandSeparator"]
                        },
                        {id: "Font Properties", prop: ["baseFont", "baseFontSize", "baseFontColor"]}
                    ]
                }
            ],
            "set": ["label", "value", "link", "toolText", "color", "alpha", "showValue", "borderColor", "borderAlpha", "isSliced", "labelFont", "labelFontColor", "labelFontSize", "labelFontBold", "labelFontItalic", "labelBgColor", "labelBorderColor", "labelAlpha", "labelBgAlpha", "labelBorderAlpha", "labelBorderPadding", "labelBorderRadius", "labelBorderThickness", "labelBorderDashed", "labelBorderDashLen", "labelBorderDashGap", "labelLink"]
        },
        Radar: {
            chart: [
                {
                    id: "Chart", group: true, sub: [
                        {id: "Titles (Axis) Names", prop: ["caption", "subCaption"]},
                        {
                            id: "Functional Attributes",
                            prop: ["animation", "palette", "paletteColors", "showLabels", "labelStep", "showValues", "showYAxisValues", "showLimits", "showDivLineValues", "yAxisValuesStep", "adjustDiv", "showPrintMenuItem", "unescapeLinks", "showZeroPlaneValue", "clickURL", "yAxisMinValue", "yAxisMaxValue", "radarRadius", "setAdaptiveYMin"]
                        },
                        {
                            id: "Chart Caption Cosmetics",
                            prop: ["captionAlignment", "captionOnTop", "captionFontSize", "subCaptionFontSize", "captionFont", "subCaptionFont", "captionFontColor", "subCaptionFontColor", "captionFontBold", "subCaptionFontBold", "alignCaptionWithCanvas", "captionHorizontalPadding"]
                        },
                        {
                            id: "Charts Cosmetics",
                            prop: ["showBorder", "borderColor", "borderThickness", "borderAlpha", "bgColor", "bgAlpha", "bgRatio", "bgAngle", "bgImage", "bgImageAlpha", "bgImageDisplayMode", "bgImageVAlign", "bgImageHAlign", "bgImageScale", "logoURL", "logoPosition", "logoAlpha", "logoScale", "logoLink"]
                        },
                        {
                            id: "Anchors",
                            prop: ["drawAnchors", "anchorSides", "anchorRadius", "anchorBorderColor", "anchorBorderThickness", "anchorBgColor", "anchorAlpha", "anchorBgAlpha"]
                        },
                        {
                            id: "Legend Properties",
                            prop: ["showLegend", "legendItemFontBold", "legendItemFont", "legendItemFontSize", "legendItemFontColor", "legendPosition", "legendCaptionAlignment", "legendCaptionBold", "legendCaptionFont", "legendCaptionFontSize", "legendCaptionFontColor", "legendItemHiddenColor", "legendIconScale", "legendBgColor", "legendBgAlpha", "legendBorderColor", "legendBorderThickness", "legendBorderAlpha", "legendShadow", "legendAllowDrag", "legendScrollBgColor", "reverseLegend", "interactiveLegend", "legendNumColumns"]
                        },
                        {
                            id: "Divisional Lines/Grids",
                            prop: ["numDivLines", "divLineColor", "divLineThickness", "divLineAlpha"]
                        },
                        {
                            id: "Tool-tip",
                            prop: ["showToolTip", "toolTipColor", "toolTipBgColor", "toolTipBorderColor", "toolTipSepChar", "seriesNameInToolTip", "showToolTipShadow"]
                        },
                        {
                            id: "Paddings and Margins",
                            prop: ["captionPadding", "labelPadding", "chartLeftMargin", "chartRightMargin", "chartTopMargin", "chartBottomMargin", "legendPadding"]
                        }
                    ]
                },
                {
                    id: "Data", group: true, sub: [
                        {
                            id: "Data Plot Cosmetics",
                            prop: ["showPlotBorder", "plotBorderColor", "plotBorderThickness", "plotBorderAlpha", "plotFillAlpha", "plotFillColor", "showRadarBorder", "radarBorderColor", "radarBorderThickness", "radarBorderAlpha", "radarFillColor", "radarFillAlpha", "radarSpikeColor", "radarSpikeThickness", "radarSpikeAlpha"]
                        },
                        {
                            id: "Data Value Cosmetics",
                            prop: ["valueFont", "valueFontColor", "valueFontSize", "valueFontBold", "valueFontItalic", "valueBgColor", "valueBorderColor", "valueAlpha", "valueFontAlpha", "valueBgAlpha", "valueBorderAlpha", "valueBorderThickness", "valueBorderRadius", "valueBorderDashed", "valueBorderDashGap", "valueBorderDashLen", "valueHoverAlpha", "valueFontHoverAlpha", "valueBgHoverAlpha", "valueBorderHoverAlpha", "showValuesOnHover"]
                        },
                        {
                            id: "Data Label Cosmetics",
                            prop: ["labelFont", "labelFontColor", "labelFontSize", "labelFontBold", "labelFontItalic", "labelBgColor", "labelBorderColor", "labelAlpha", "labelBgAlpha", "labelBorderAlpha", "labelBorderPadding", "labelBorderRadius", "labelBorderThickness", "labelBorderDashed", "labelBorderDashLen", "labelBorderDashGap", "labelLink"]
                        },
                        {
                            id: "Data Plot Hover Effects",
                            prop: ["showHoverEffect", "plotHoverEffect", "plotFillHoverColor", "plotFillHoverAlpha"]
                        }
                    ]
                },
                {
                    id: "Others", group: true, sub: [
                        {
                            id: "Number Formatting",
                            prop: ["formatNumber", "formatNumberScale", "defaultNumberScale", "numberScaleUnit", "numberScaleValue", "scaleRecursively", "maxScaleRecursion", "scaleSeparator", "numberPrefix", "numberSuffix", "decimalSeparator", "thousandSeparator", "thousandSeparatorPosition", "inDecimalSeparator", "inThousandSeparator", "decimals", "forceDecimals", "forceYAxisValueDecimals", "yAxisValueDecimals"]
                        },
                        {
                            id: "Font Properties",
                            prop: ["baseFont", "baseFontSize", "baseFontColor", "outCnvBaseFont", "outCnvBaseFontSize", "outCnvBaseFontColor"]
                        }
                    ]
                }
            ],
            "categories": ["font", "fontSize", "fontColor"],
            "category": ["label", "showLabel", "toolText", "font", "fontColor", "fontSize", "fontBold", "fontItalic", "bgColor", "borderColor", "alpha", "bgAlpha", "borderAlpha", "borderPadding", "borderRadius", "borderThickness", "borderDashed", "borderDashLen", "borderDashGap", "link"],
            "dataset": ["seriesName", "color", "alpha", "showValues", "includeInLegend", "showPlotBorder", "plotBorderColor", "plotBorderThickness", "plotBorderAlpha", "drawAnchors", "anchorSides", "anchorRadius", "anchorBorderColor", "anchorBorderThickness", "anchorBgColor", "anchorAlpha", "anchorBgAlpha"],
            "set": ["value", "color", "displayValue", "link", "toolText", "showValue", "alpha", "anchorSides", "anchorRadius", "anchorBorderColor", "anchorBorderThickness", "anchorBgColor", "anchorAlpha", "anchorBgAlpha"]
        },
        RealTimeLine: {
            chart: [
                {
                    id: "Chart", group: true, sub: [
                        {
                            id: "Titles (Axis) Names",
                            prop: ["caption", "subCaption", "xAxisName", "yAxisName", "rotateYAxisName", "yAxisNameWidth"]
                        },
                        {
                            id: "Functional Attributes",
                            prop: ["animation", "connectNullData", "clickURL", "palette", "paletteThemeColor", "paletteColors", "showRTMenuItem", "showPrintMenuItem", "refreshInstantly", "valuePosition", "manageResize", "useEllipsesWhenOverflow"]
                        },
                        {
                            id: "Chart Caption Cosmetics",
                            prop: ["captionAlignment", "captionOnTop", "captionFontSize", "subCaptionFontSize", "captionFont", "subCaptionFont", "captionFontColor", "subCaptionFontColor", "captionFontBold", "subCaptionFontBold", "alignCaptionWithCanvas", "captionHorizontalPadding"]
                        },
                        {
                            id: "X-axis Name Cosmetics",
                            prop: ["xAxisNameFontColor", "xAxisNameFontSize", "xAxisNameFontBold", "xAxisNameFontItalic", "xAxisNameBgColor", "xAxisNameBorderColor", "xAxisNameAlpha", "xAxisNameFontAlpha", "xAxisNameBgAlpha", "xAxisNameBorderAlpha", "xAxisNameBorderPadding", "xAxisNameBorderRadius", "xAxisNameBorderThickness", "xAxisNameBorderDashed", "xAxisNameBorderDashLen", "xAxisNameBorderDashGap"]
                        },
                        {
                            id: "Y-axis Name Cosmetics",
                            prop: ["yAxisNameFont", "yAxisNameFontColor", "yAxisNameFontSize", "yAxisNameFontBold", "yAxisNameFontItalic", "yAxisNameBgColor", "yAxisNameBorderColor", "yAxisNameAlpha", "yAxisNameFontAlpha", "yAxisNameBgAlpha", "yAxisNameBorderAlpha", "yAxisNameBorderPadding", "yAxisNameBorderRadius", "yAxisNameBorderThickness", "yAxisNameBorderDashed", "yAxisNameBorderDashLen", "yAxisNameBorderDashGap"]
                        },
                        {
                            id: "Chart Y-Axis",
                            prop: ["setAdaptiveYMin", "yAxisMinValue", "yAxisMaxValue", "showYAxisValues", "showLimits", "showDivLineValues", "yAxisValuesStep", "yAxisValueDecimals", "forceYAxisDecimals"]
                        },
                        {
                            id: "Anchors",
                            prop: ["drawAnchors", "anchorSides", "anchorRadius", "anchorBorderColor", "anchorBorderThickness", "anchorBgColor", "anchorAlpha", "anchorBgAlpha"]
                        },
                        {
                            id: "Charts Cosmetics",
                            prop: ["bgColor", "bgAlpha", "bgRatio", "bgAngle", "showBorder", "borderColor", "borderThickness", "borderAlpha", "bgImage", "bgImageAlpha", "bgImageDisplayMode", "bgImageVAlign", "bgImageHAlign", "bgImageScale", "xAxisLineColor", "logoURL", "logoPosition", "logoAlpha", "logoScale", "logoLink"]
                        },
                        {
                            id: "Canvas Cosmetics",
                            prop: ["canvasBgColor", "canvasBgAlpha", "canvasBgRatio", "canvasBgAngle", "canvasBorderColor", "canvasBorderThickness", "canvasBorderAlpha"]
                        },
                        {
                            id: "Divisional Lines/Grids",
                            prop: ["numDivLines", "divLineColor", "divLineThickness", "divLineAlpha", "divLineDashed", "divLineDashLen", "divLineDashGap", "showAlternateHGridColor", "alternateHGridColor", "alternateHGridAlpha", "numVDivLines", "vDivLineColor", "vDivLineThickness", "vDivLineAlpha", "vDivLineDashed", "vDivLineDashLen", "vDivLineDashGap", "showAlternateVGridColor", "alternateVGridColor", "alternateVGridAlpha"]
                        },
                        {
                            id: "Zero Plane Properties",
                            prop: ["showZeroPlane", "zeroPlaneColor", "zeroPlaneThickness", "zeroPlaneAlpha", "showZeroPlaneValue"]
                        },
                        {
                            id: "Legend Properties",
                            prop: ["showLegend", "legendItemFontBold", "legendItemFont", "legendItemFontSize", "legendItemFontColor", "legendPosition", "legendCaptionAlignment", "legendCaptionBold", "legendCaptionFont", "legendCaptionFontSize", "legendCaptionFontColor", "legendItemHiddenColor", "legendCaption", "legendBorderColor", "legendBorderThickness", "legendBorderAlpha", "legendBgColor", "legendBgAlpha", "legendShadow", "legendAllowDrag", "legendScrollBgColor", "reverseLegend", "legendIconScale", "legendNumColumns", "minimiseWrappingInLegend"]
                        },
                        {
                            id: "Tool-tip",
                            prop: ["showToolTip", "toolTipColor", "toolTipBgColor", "toolTipBorderColor", "toolTipSepChar", "seriesNameInToolTip", "showToolTipShadow"]
                        },
                        {
                            id: "Paddings and Margins",
                            prop: ["captionPadding", "canvasPadding", "xAxisNamePadding", "yAxisNamePadding", "yAxisValuesPadding", "labelPadding", "valuePadding", "realTimeValuePadding", "legendPadding", "chartLeftMargin", "chartRightMargin", "chartTopMargin", "chartBottomMargin", "canvasLeftMargin", "canvasRightMargin", "canvasTopMargin", "canvasBottomMargin"]
                        }
                    ]
                },
                {
                    id: "Data", group: true, sub: [
                        {
                            id: "Data Plot Cosmetics",
                            prop: ["showShadow", "lineColor", "lineThickness", "lineAlpha", "lineDashed", "lineDashLen", "lineDashGap"]
                        },
                        {
                            id: "Chart Labels and Values",
                            prop: ["showLabels", "maxLabelHeight", "labelDisplay", "slantLabels", "labelStep", "staggerLines", "showValues", "rotateValues"]
                        }
                    ]
                },
                {
                    id: "Others", group: true, sub: [
                        {
                            id: "Real-time Properties",
                            prop: ["dataStreamURL", "refreshInterval", "clearChartInterval", "updateInterval", "numDisplaySets", "dataStamp", "showRealTimeValue", "realTimeValueSep"]
                        },
                        {
                            id: "Number Formatting",
                            prop: ["formatNumber", "numberPrefix", "numberSuffix", "decimals", "forceDecimals", "formatNumberScale", "defaultNumberScale", "numberScaleUnit", "numberScaleValue", "scaleRecursively", "maxScaleRecursion", "scaleSeparator", "decimalSeparator", "thousandSeparator", "thousandSeparatorPosition", "inDecimalSeparator", "inThousandSeparator"]
                        },
                        {
                            id: "Font Properties",
                            prop: ["baseFont", "baseFontSize", "baseFontColor", "outCnvBaseFont", "outCnvBaseFontSize", "outCnvBaseFontColor", "realTimeValueFont", "realTimeValueFontSize", "realTimeValueFontColor"]
                        },
                        {
                            id: "Message Logger",
                            prop: ["useMessageLog", "messageLogWPercent", "messageLogHPercent", "messageLogShowTitle", "messageLogTitle", "messageLogColor", "messageGoesToLog", "messageGoesToJS", "messageJSHandler", "messagePassAllToJS"]
                        }
                    ]
                }
            ],
            "categories": ["font", "fontSize", "fontColor"],
            "category": ["label", "showLabel", "toolText", "font", "fontColor", "fontSize", "fontBold", "fontItalic", "bgColor", "borderColor", "alpha", "bgAlpha", "borderAlpha", "borderPadding", "borderRadius", "borderThickness", "borderDashed", "borderDashLen", "borderDashGap", "link"],
            "dataset": ["seriesName", "color", "alpha", "showValues", "includeInLegend", "checkForAlerts", "dashed", "lineDashLen", "lineDashGap", "lineThickness", "drawAnchors", "anchorSides", "anchorRadius", "anchorBorderColor", "anchorBorderThickness", "anchorBgColor", "anchorAlpha", "anchorBgAlpha", "valuePosition"],
            "set": ["value", "link", "toolText", "color", "alpha", "showValue", "dashed", "anchorSides", "anchorRadius", "anchorBorderColor", "anchorBorderThickness", "anchorBgColor", "anchorAlpha", "anchorBgAlpha", "valuePosition"],
            "Vertical data separator lines": ["label", "color", "thickness", "alpha", "dashed", "dashLen", "dashGap"],
            "Trend Lines": ["startValue", "endValue", "displayValue", "color", "thickness", "isTrendZone", "alpha", "showOnTop", "dashed", "dashLen", "dashGap", "valueOnRight"]
        },
        RealTimeLineDY: {
            chart: [
                {
                    id: "Chart", group: true, sub: [
                        {
                            id: "Titles (Axis) Names",
                            prop: ["caption", "subCaption", "xAxisName", "pYAxisName", "sYAxisName", "rotateYAxisName", "pYAxisNameWidth", "sYAxisNameWidth"]
                        },
                        {
                            id: "Functional Attributes",
                            prop: ["animation", "connectNullData", "clickURL", "palette", "paletteThemeColor", "paletteColors", "showRTMenuItem", "showPrintMenuItem", "refreshInstantly", "valuePosition", "manageResize", "useEllipsesWhenOverflow"]
                        },
                        {
                            id: "Chart Caption Cosmetics",
                            prop: ["captionAlignment", "captionOnTop", "captionFontSize", "subCaptionFontSize", "captionFont", "subCaptionFont", "captionFontColor", "subCaptionFontColor", "captionFontBold", "subCaptionFontBold", "alignCaptionWithCanvas", "captionHorizontalPadding"]
                        },
                        {
                            id: "X-axis Name Cosmetics",
                            prop: ["xAxisNameFontColor", "xAxisNameFontSize", "xAxisNameFontBold", "xAxisNameFontItalic", "xAxisNameBgColor", "xAxisNameBorderColor", "xAxisNameAlpha", "xAxisNameFontAlpha", "xAxisNameBgAlpha", "xAxisNameBorderAlpha", "xAxisNameBorderPadding", "xAxisNameBorderRadius", "xAxisNameBorderThickness", "xAxisNameBorderDashed", "xAxisNameBorderDashLen", "xAxisNameBorderDashGap"]
                        },
                        {
                            id: "Primary Y-axis Name Cosmetics",
                            prop: ["pYAxisNameFont", "pYAxisNameFontColor", "pYAxisNameFontSize", "pYAxisNameFontBold", "pYAxisNameFontItalic", "pYAxisNameBgColor", "pYAxisNameBorderColor", "pYAxisNameAlpha", "pYAxisNameFontAlpha", "pYAxisNameBgAlpha", "pYAxisNameBorderAlpha", "pYAxisNameBorderPadding", "pYAxisNameBorderRadius", "pYAxisNameBorderThickness", "pYAxisNameBorderDashed", "pYAxisNameBorderDashLen", "pYAxisNameBorderDashGap"]
                        },
                        {
                            id: "Secondary Y-axis Name Cosmetics",
                            prop: ["sYAxisNameFont", "sYAxisNameFontColor", "sYAxisNameFontSize", "sYAxisNameFontBold", "sYAxisNameFontItalic", "sYAxisNameBgColor", "sYAxisNameBorderColor", "sYAxisNameAlpha", "sYAxisNameFontAlpha", "sYAxisNameBgAlpha", "sYAxisNameBorderAlpha", "sYAxisNameBorderPadding", "sYAxisNameBorderRadius", "sYAxisNameBorderThickness", "sYAxisNameBorderDashed", "sYAxisNameBorderDashLen", "sYAxisNameBorderDashGap"]
                        },
                        {
                            id: "Chart Y-Axis",
                            prop: ["setAdaptiveYMin", "setAdaptiveSYMin", "pYAxisMinValue", "pYAxisMaxValue", "sYAxisMinValue", "sYAxisMaxValue", "showYAxisValues", "showLimits", "showDivLineValues", "showSecondaryLimits", "showDivLineSecondaryValue", "yAxisValuesStep", "yAxisValueDecimals", "sYAxisValueDecimals", "forceYAxisDecimals", "forceSYAxisDecimals"]
                        },
                        {
                            id: "Anchors",
                            prop: ["drawAnchors", "anchorSides", "anchorRadius", "anchorBorderColor", "anchorBorderThickness", "anchorBgColor", "anchorAlpha", "anchorBgAlpha"]
                        },
                        {
                            id: "Charts Cosmetics",
                            prop: ["bgColor", "bgAlpha", "bgRatio", "bgAngle", "showBorder", "borderColor", "borderThickness", "borderAlpha", "bgImage", "bgImageAlpha", "bgImageDisplayMode", "bgImageVAlign", "bgImageHAlign", "bgImageScale", "xAxisLineColor", "logoURL", "logoPosition", "logoAlpha", "logoScale", "logoLink"]
                        },
                        {
                            id: "Canvas Cosmetics",
                            prop: ["canvasBgColor", "canvasBgAlpha", "canvasBgRatio", "canvasBgAngle", "canvasBorderColor", "canvasBorderThickness", "canvasBorderAlpha"]
                        },
                        {
                            id: "Divisional Lines/Grids",
                            prop: ["numDivLines", "divLineColor", "divLineThickness", "divLineAlpha", "divLineDashed", "divLineDashLen", "divLineDashGap", "showAlternateHGridColor", "alternateHGridColor", "alternateHGridAlpha", "numVDivLines", "vDivLineColor", "vDivLineThickness", "vDivLineAlpha", "vDivLineDashed", "vDivLineDashLen", "vDivLineDashGap", "showAlternateVGridColor", "alternateVGridColor", "alternateVGridAlpha"]
                        },
                        {
                            id: "Zero Plane Properties",
                            prop: ["showZeroPlane", "zeroPlaneColor", "zeroPlaneThickness", "zeroPlaneAlpha", "showZeroPlaneValue"]
                        },
                        {
                            id: "Legend Properties",
                            prop: ["showLegend", "legendItemFontBold", "legendItemFont", "legendItemFontSize", "legendItemFontColor", "legendPosition", "legendCaptionAlignment", "legendCaptionBold", "legendCaptionFont", "legendCaptionFontSize", "legendCaptionFontColor", "legendItemHiddenColor", "legendCaption", "legendBorderColor", "legendBorderThickness", "legendBorderAlpha", "legendBgColor", "legendBgAlpha", "legendShadow", "legendAllowDrag", "legendScrollBgColor", "reverseLegend", "legendIconScale", "legendNumColumns", "minimiseWrappingInLegend"]
                        },
                        {
                            id: "Tool-tip",
                            prop: ["showToolTip", "toolTipColor", "toolTipBgColor", "toolTipBorderColor", "toolTipSepChar", "seriesNameInToolTip", "showToolTipShadow"]
                        },
                        {
                            id: "Paddings and Margins",
                            prop: ["captionPadding", "canvasPadding", "xAxisNamePadding", "yAxisNamePadding", "yAxisValuesPadding", "labelPadding", "valuePadding", "realTimeValuePadding", "legendPadding", "chartLeftMargin", "chartRightMargin", "chartTopMargin", "chartBottomMargin", "canvasLeftMargin", "canvasRightMargin", "canvasTopMargin", "canvasBottomMargin"]
                        }
                    ]
                },
                {
                    id: "Data", group: true, sub: [
                        {
                            id: "Data Plot Cosmetics",
                            prop: ["showShadow", "lineColor", "lineThickness", "lineAlpha", "lineDashed", "lineDashLen", "lineDashGap"]
                        },
                        {
                            id: "Chart Labels and Values",
                            prop: ["showLabels", "maxLabelHeight", "labelDisplay", "slantLabels", "labelStep", "staggerLines", "showValues", "rotateValues"]
                        }
                    ]
                },
                {
                    id: "Others", group: true, sub: [
                        {
                            id: "Real-time Properties",
                            prop: ["dataStreamURL", "refreshInterval", "clearChartInterval", "updateInterval", "numDisplaySets", "dataStamp", "showRealTimeValue", "realTimeValueSep"]
                        },
                        {
                            id: "Number Formatting",
                            prop: ["formatNumber", "numberPrefix", "numberSuffix", "decimals", "forceDecimals", "formatNumberScale", "defaultNumberScale", "numberScaleUnit", "numberScaleValue", "scaleRecursively", "maxScaleRecursion", "scaleSeparator", "decimalSeparator", "thousandSeparator", "thousandSeparatorPosition", "inDecimalSeparator", "inThousandSeparator"]
                        },
                        {
                            id: "Number Formatting (Secondary axis)",
                            prop: ["sFormatNumber", "sNumberPrefix", "sNumberSuffix", "sDecimals", "sForceDecimals", "sFormatNumberScale", "sDefaultNumberScale", "sNumberScaleUnit", "sNumberScaleValue", "sScaleRecursively", "sMaxScaleRecursion", "sScaleSeparator"]
                        },
                        {
                            id: "Font Properties",
                            prop: ["baseFont", "baseFontSize", "baseFontColor", "outCnvBaseFont", "outCnvBaseFontSize", "outCnvBaseFontColor", "realTimeValueFont", "realTimeValueFontSize", "realTimeValueFontColor"]
                        },
                        {
                            id: "Message Logger",
                            prop: ["useMessageLog", "messageLogWPercent", "messageLogHPercent", "messageLogShowTitle", "messageLogTitle", "messageLogColor", "messageGoesToLog", "messageGoesToJS", "messageJSHandler", "messagePassAllToJS"]
                        }
                    ]
                }
            ],
            "categories": ["font", "fontSize", "fontColor"],
            "category": ["label", "showLabel", "toolText", "font", "fontColor", "fontSize", "fontBold", "fontItalic", "bgColor", "borderColor", "alpha", "bgAlpha", "borderAlpha", "borderPadding", "borderRadius", "borderThickness", "borderDashed", "borderDashLen", "borderDashGap", "link"],
            "dataset": ["parentYAxis", "seriesName", "color", "alpha", "showValues", "includeInLegend", "checkForAlerts", "dashed", "lineDashLen", "lineDashGap", "lineThickness", "drawAnchors", "anchorSides", "anchorRadius", "anchorBorderColor", "anchorBorderThickness", "anchorBgColor", "anchorAlpha", "anchorBgAlpha", "valuePosition"],
            "set": ["value", "link", "toolText", "color", "alpha", "showValue", "dashed", "anchorSides", "anchorRadius", "anchorBorderColor", "anchorBorderThickness", "anchorBgColor", "anchorAlpha", "anchorBgAlpha", "valuePosition"],
            "Vertical data separator lines": ["label", "color", "thickness", "alpha", "dashed", "dashLen", "dashGap"],
            "Trend Lines": ["startValue", "endValue", "displayValue", "color", "thickness", "isTrendZone", "alpha", "showOnTop", "dashed", "dashLen", "dashGap", "valueOnRight"]
        },
        RealTimeArea: {
            chart: [
                {
                    id: "Chart", group: true, sub: [
                        {
                            id: "Titles (Axis) Names",
                            prop: ["caption", "subCaption", "xAxisName", "yAxisName", "rotateYAxisName", "yAxisNameWidth"]
                        },
                        {
                            id: "Functional Attributes",
                            prop: ["animation", "connectNullData", "clickURL", "palette", "paletteThemeColor", "paletteColors", "showRTMenuItem", "showPrintMenuItem", "refreshInstantly", "manageResize", "useEllipsesWhenOverflow"]
                        },
                        {
                            id: "Chart Caption Cosmetics",
                            prop: ["captionAlignment", "captionOnTop", "captionFontSize", "subCaptionFontSize", "captionFont", "subCaptionFont", "captionFontColor", "subCaptionFontColor", "captionFontBold", "subCaptionFontBold", "alignCaptionWithCanvas", "captionHorizontalPadding"]
                        },
                        {
                            id: "X-axis Name Cosmetics",
                            prop: ["xAxisNameFontColor", "xAxisNameFontSize", "xAxisNameFontBold", "xAxisNameFontItalic", "xAxisNameBgColor", "xAxisNameBorderColor", "xAxisNameAlpha", "xAxisNameFontAlpha", "xAxisNameBgAlpha", "xAxisNameBorderAlpha", "xAxisNameBorderPadding", "xAxisNameBorderRadius", "xAxisNameBorderThickness", "xAxisNameBorderDashed", "xAxisNameBorderDashLen", "xAxisNameBorderDashGap"]
                        },
                        {
                            id: "Y-axis Name Cosmetics",
                            prop: ["yAxisNameFont", "yAxisNameFontColor", "yAxisNameFontSize", "yAxisNameFontBold", "yAxisNameFontItalic", "yAxisNameBgColor", "yAxisNameBorderColor", "yAxisNameAlpha", "yAxisNameFontAlpha", "yAxisNameBgAlpha", "yAxisNameBorderAlpha", "yAxisNameBorderPadding", "yAxisNameBorderRadius", "yAxisNameBorderThickness", "yAxisNameBorderDashed", "yAxisNameBorderDashLen", "yAxisNameBorderDashGap"]
                        },
                        {
                            id: "Chart Y-Axis",
                            prop: ["setAdaptiveYMin", "yAxisMinValue", "yAxisMaxValue", "showYAxisValues", "showLimits", "showDivLineValues", "yAxisValuesStep", "yAxisValueDecimals", "forceYAxisDecimals"]
                        },
                        {
                            id: "Anchors",
                            prop: ["drawAnchors", "anchorSides", "anchorRadius", "anchorBorderColor", "anchorBorderThickness", "anchorBgColor", "anchorAlpha", "anchorBgAlpha"]
                        },
                        {
                            id: "Charts Cosmetics",
                            prop: ["bgColor", "bgAlpha", "bgRatio", "bgAngle", "showBorder", "borderColor", "borderThickness", "borderAlpha", "bgImage", "bgImageAlpha", "bgImageDisplayMode", "bgImageVAlign", "bgImageHAlign", "bgImageScale", "xAxisLineColor", "logoURL", "logoPosition", "logoAlpha", "logoScale", "logoLink"]
                        },
                        {
                            id: "Canvas Cosmetics",
                            prop: ["canvasBgColor", "canvasBgAlpha", "canvasBgRatio", "canvasBgAngle", "canvasBorderColor", "canvasBorderThickness", "canvasBorderAlpha"]
                        },
                        {
                            id: "Divisional Lines/Grids",
                            prop: ["numDivLines", "divLineColor", "divLineThickness", "divLineAlpha", "divLineDashed", "divLineDashLen", "divLineDashGap", "showAlternateHGridColor", "alternateHGridColor", "alternateHGridAlpha", "numVDivLines", "vDivLineColor", "vDivLineThickness", "vDivLineAlpha", "vDivLineDashed", "vDivLineDashLen", "vDivLineDashGap", "showAlternateVGridColor", "alternateVGridColor", "alternateVGridAlpha"]
                        },
                        {
                            id: "Zero Plane Properties",
                            prop: ["zeroPlaneColor", "zeroPlaneThickness", "zeroPlaneAlpha", "showZeroPlaneValue"]
                        },
                        {
                            id: "Legend Properties",
                            prop: ["showLegend", "legendItemFontBold", "legendItemFont", "legendItemFontSize", "legendItemFontColor", "legendPosition", "legendCaptionAlignment", "legendCaptionBold", "legendCaptionFont", "legendCaptionFontSize", "legendCaptionFontColor", "legendItemHiddenColor", "legendCaption", "legendBorderColor", "legendBorderThickness", "legendBorderAlpha", "legendBgColor", "legendBgAlpha", "legendShadow", "legendAllowDrag", "legendScrollBgColor", "reverseLegend", "legendIconScale", "legendNumColumns", "minimiseWrappingInLegend"]
                        },
                        {
                            id: "Tool-tip",
                            prop: ["showToolTip", "toolTipColor", "toolTipBgColor", "toolTipBorderColor", "toolTipSepChar", "seriesNameInToolTip", "showToolTipShadow"]
                        },
                        {
                            id: "Paddings and Margins",
                            prop: ["captionPadding", "canvasPadding", "xAxisNamePadding", "yAxisNamePadding", "yAxisValuesPadding", "labelPadding", "valuePadding", "realTimeValuePadding", "legendPadding", "chartLeftMargin", "chartRightMargin", "chartTopMargin", "chartBottomMargin", "canvasLeftMargin", "canvasRightMargin", "canvasTopMargin", "canvasBottomMargin"]
                        }
                    ]
                },
                {
                    id: "Data", group: true, sub: [
                        {
                            id: "Data Plot Cosmetics",
                            prop: ["showShadow", "showPlotBorder", "plotBorderColor", "plotBorderThickness", "plotBorderAlpha", "plotBorderDashed", "plotBorderDashLen", "plotBorderDashGap", "plotFillColor", "plotFillAlpha", "plotGradientColor", "plotFillAngle"]
                        },
                        {
                            id: "Chart Labels and Values",
                            prop: ["showLabels", "maxLabelHeight", "labelDisplay", "slantLabels", "labelStep", "staggerLines", "showValues", "rotateValues"]
                        }
                    ]
                },
                {
                    id: "Others", group: true, sub: [
                        {
                            id: "Real-time Properties",
                            prop: ["dataStreamURL", "refreshInterval", "clearChartInterval", "updateInterval", "numDisplaySets", "dataStamp", "showRealTimeValue", "realTimeValueSep"]
                        },
                        {
                            id: "Number Formatting",
                            prop: ["formatNumber", "numberPrefix", "numberSuffix", "decimals", "forceDecimals", "formatNumberScale", "defaultNumberScale", "numberScaleUnit", "numberScaleValue", "scaleRecursively", "maxScaleRecursion", "scaleSeparator", "decimalSeparator", "thousandSeparator", "thousandSeparatorPosition", "inDecimalSeparator", "inThousandSeparator"]
                        },
                        {
                            id: "Font Properties",
                            prop: ["baseFont", "baseFontSize", "baseFontColor", "outCnvBaseFont", "outCnvBaseFontSize", "outCnvBaseFontColor", "realTimeValueFont", "realTimeValueFontSize", "realTimeValueFontColor"]
                        },
                        {
                            id: "Message Logger",
                            prop: ["useMessageLog", "messageLogWPercent", "messageLogHPercent", "messageLogShowTitle", "messageLogTitle", "messageLogColor", "messageGoesToLog", "messageGoesToJS", "messageJSHandler", "messagePassAllToJS"]
                        }
                    ]
                }
            ],
            "categories": ["font", "fontSize", "fontColor"],
            "category": ["label", "showLabel", "toolText", "font", "fontColor", "fontSize", "fontBold", "fontItalic", "bgColor", "borderColor", "alpha", "bgAlpha", "borderAlpha", "borderPadding", "borderRadius", "borderThickness", "borderDashed", "borderDashLen", "borderDashGap", "link"],
            "dataset": ["seriesName", "color", "alpha", "showValues", "includeInLegend", "checkForAlerts", "dashed", "drawAnchors", "anchorSides", "anchorRadius", "anchorBorderColor", "anchorBorderThickness", "anchorBgColor", "anchorAlpha", "anchorBgAlpha", "showPlotBorder", "plotBorderColor", "plotBorderThickness", "plotBorderAlpha"],
            "set": ["value", "link", "toolText", "color", "alpha", "showValue", "dashed", "anchorSides", "anchorRadius", "anchorBorderColor", "anchorBorderThickness", "anchorBgColor", "anchorAlpha", "anchorBgAlpha"],
            "Vertical data separator lines": ["label", "color", "thickness", "alpha", "dashed", "dashLen", "dashGap"],
            "Trend Lines": ["startValue", "endValue", "displayValue", "color", "thickness", "isTrendZone", "alpha", "showOnTop", "dashed", "dashLen", "dashGap", "valueOnRight"]
        },
        RealTimeStackedArea: {
            chart: [
                {
                    id: "Chart", group: true, sub: [
                        {
                            id: "Titles (Axis) Names",
                            prop: ["caption", "subCaption", "xAxisName", "yAxisName", "rotateYAxisName", "yAxisNameWidth"]
                        },
                        {
                            id: "Functional Attributes",
                            prop: ["animation", "connectNullData", "clickURL", "palette", "paletteThemeColor", "paletteColors", "showRTMenuItem", "showPrintMenuItem", "refreshInstantly", "stack100Percent", "showPercentValues", "showPercentInToolTip", "manageResize", "useEllipsesWhenOverflow"]
                        },
                        {
                            id: "Chart Caption Cosmetics",
                            prop: ["captionAlignment", "captionOnTop", "captionFontSize", "subCaptionFontSize", "captionFont", "subCaptionFont", "captionFontColor", "subCaptionFontColor", "captionFontBold", "subCaptionFontBold", "alignCaptionWithCanvas", "captionHorizontalPadding"]
                        },
                        {
                            id: "X-axis Name Cosmetics",
                            prop: ["xAxisNameFontColor", "xAxisNameFontSize", "xAxisNameFontBold", "xAxisNameFontItalic", "xAxisNameBgColor", "xAxisNameBorderColor", "xAxisNameAlpha", "xAxisNameFontAlpha", "xAxisNameBgAlpha", "xAxisNameBorderAlpha", "xAxisNameBorderPadding", "xAxisNameBorderRadius", "xAxisNameBorderThickness", "xAxisNameBorderDashed", "xAxisNameBorderDashLen", "xAxisNameBorderDashGap"]
                        },
                        {
                            id: "Y-axis Name Cosmetics",
                            prop: ["yAxisNameFont", "yAxisNameFontColor", "yAxisNameFontSize", "yAxisNameFontBold", "yAxisNameFontItalic", "yAxisNameBgColor", "yAxisNameBorderColor", "yAxisNameAlpha", "yAxisNameFontAlpha", "yAxisNameBgAlpha", "yAxisNameBorderAlpha", "yAxisNameBorderPadding", "yAxisNameBorderRadius", "yAxisNameBorderThickness", "yAxisNameBorderDashed", "yAxisNameBorderDashLen", "yAxisNameBorderDashGap"]
                        },
                        {
                            id: "Chart Y-Axis",
                            prop: ["setAdaptiveYMin", "yAxisMinValue", "yAxisMaxValue", "showYAxisValues", "showLimits", "showDivLineValues", "yAxisValuesStep", "yAxisValueDecimals", "forceYAxisDecimals"]
                        },
                        {
                            id: "Anchors",
                            prop: ["drawAnchors", "anchorSides", "anchorRadius", "anchorBorderColor", "anchorBorderThickness", "anchorBgColor", "anchorAlpha", "anchorBgAlpha"]
                        },
                        {
                            id: "Charts Cosmetics",
                            prop: ["bgColor", "bgAlpha", "bgRatio", "bgAngle", "showBorder", "borderColor", "borderThickness", "borderAlpha", "bgImage", "bgImageAlpha", "bgImageDisplayMode", "bgImageVAlign", "bgImageHAlign", "bgImageScale", "xAxisLineColor", "logoURL", "logoPosition", "logoAlpha", "logoScale", "logoLink"]
                        },
                        {
                            id: "Canvas Cosmetics",
                            prop: ["canvasBgColor", "canvasBgAlpha", "canvasBgRatio", "canvasBgAngle", "canvasBorderColor", "canvasBorderThickness", "canvasBorderAlpha"]
                        },
                        {
                            id: "Divisional Lines/Grids",
                            prop: ["numDivLines", "divLineColor", "divLineThickness", "divLineAlpha", "divLineDashed", "divLineDashLen", "divLineDashGap", "showAlternateHGridColor", "alternateHGridColor", "alternateHGridAlpha", "numVDivLines", "vDivLineColor", "vDivLineThickness", "vDivLineAlpha", "vDivLineDashed", "vDivLineDashLen", "vDivLineDashGap", "showAlternateVGridColor", "alternateVGridColor", "alternateVGridAlpha"]
                        },
                        {
                            id: "Zero Plane Properties",
                            prop: ["zeroPlaneColor", "zeroPlaneThickness", "zeroPlaneAlpha", "showZeroPlaneValue"]
                        },
                        {
                            id: "Legend Properties",
                            prop: ["showLegend", "legendItemFontBold", "legendItemFont", "legendItemFontSize", "legendItemFontColor", "legendPosition", "legendCaptionAlignment", "legendCaptionBold", "legendCaptionFont", "legendCaptionFontSize", "legendCaptionFontColor", "legendItemHiddenColor", "legendCaption", "legendBorderColor", "legendBorderThickness", "legendBorderAlpha", "legendBgColor", "legendBgAlpha", "legendShadow", "legendAllowDrag", "legendScrollBgColor", "reverseLegend", "legendIconScale", "legendNumColumns", "minimiseWrappingInLegend"]
                        },
                        {
                            id: "Tool-tip",
                            prop: ["showToolTip", "toolTipColor", "toolTipBgColor", "toolTipBorderColor", "toolTipSepChar", "seriesNameInToolTip", "showToolTipShadow"]
                        },
                        {
                            id: "Paddings and Margins",
                            prop: ["captionPadding", "canvasPadding", "xAxisNamePadding", "yAxisNamePadding", "yAxisValuesPadding", "labelPadding", "valuePadding", "realTimeValuePadding", "legendPadding", "chartLeftMargin", "chartRightMargin", "chartTopMargin", "chartBottomMargin", "canvasLeftMargin", "canvasRightMargin", "canvasTopMargin", "canvasBottomMargin"]
                        }
                    ]
                },
                {
                    id: "Data", group: true, sub: [
                        {
                            id: "Data Plot Cosmetics",
                            prop: ["showShadow", "showPlotBorder", "plotBorderColor", "plotBorderThickness", "plotBorderAlpha", "plotBorderDashed", "plotBorderDashLen", "plotBorderDashGap", "plotFillColor", "plotFillAlpha", "plotGradientColor", "plotFillAngle"]
                        },
                        {
                            id: "Chart Labels and Values",
                            prop: ["showLabels", "maxLabelHeight", "labelDisplay", "slantLabels", "labelStep", "staggerLines", "showValues", "rotateValues"]
                        }
                    ]
                },
                {
                    id: "Others", group: true, sub: [
                        {
                            id: "Real-time Properties",
                            prop: ["dataStreamURL", "refreshInterval", "clearChartInterval", "updateInterval", "numDisplaySets", "dataStamp", "showRealTimeValue", "realTimeValueSep"]
                        },
                        {
                            id: "Number Formatting",
                            prop: ["formatNumber", "numberPrefix", "numberSuffix", "decimals", "forceDecimals", "formatNumberScale", "defaultNumberScale", "numberScaleUnit", "numberScaleValue", "scaleRecursively", "maxScaleRecursion", "scaleSeparator", "decimalSeparator", "thousandSeparator", "thousandSeparatorPosition", "inDecimalSeparator", "inThousandSeparator"]
                        },
                        {
                            id: "Font Properties",
                            prop: ["baseFont", "baseFontSize", "baseFontColor", "outCnvBaseFont", "outCnvBaseFontSize", "outCnvBaseFontColor", "realTimeValueFont", "realTimeValueFontSize", "realTimeValueFontColor"]
                        },
                        {
                            id: "Message Logger",
                            prop: ["useMessageLog", "messageLogWPercent", "messageLogHPercent", "messageLogShowTitle", "messageLogTitle", "messageLogColor", "messageGoesToLog", "messageGoesToJS", "messageJSHandler", "messagePassAllToJS"]
                        }
                    ]
                }
            ],
            "categories": ["font", "fontSize", "fontColor"],
            "category": ["label", "showLabel", "toolText", "font", "fontColor", "fontSize", "fontBold", "fontItalic", "bgColor", "borderColor", "alpha", "bgAlpha", "borderAlpha", "borderPadding", "borderRadius", "borderThickness", "borderDashed", "borderDashLen", "borderDashGap", "link"],
            "dataset": ["seriesName", "color", "alpha", "showValues", "includeInLegend", "checkForAlerts", "dashed", "drawAnchors", "anchorSides", "anchorRadius", "anchorBorderColor", "anchorBorderThickness", "anchorBgColor", "anchorAlpha", "anchorBgAlpha", "showPlotBorder", "plotBorderColor", "plotBorderThickness", "plotBorderAlpha"],
            "set": ["value", "link", "toolText", "color", "alpha", "showValue", "dashed", "anchorSides", "anchorRadius", "anchorBorderColor", "anchorBorderThickness", "anchorBgColor", "anchorAlpha", "anchorBgAlpha"],
            "Vertical data separator lines": ["label", "color", "thickness", "alpha", "dashed", "dashLen", "dashGap"],
            "Trend Lines": ["startValue", "endValue", "displayValue", "color", "thickness", "isTrendZone", "alpha", "showOnTop", "dashed", "dashLen", "dashGap", "valueOnRight"]
        },
        RealTimeColumn: {
            chart: [
                {
                    id: "Chart", group: true, sub: [
                        {
                            id: "Titles (Axis) Names",
                            prop: ["caption", "subCaption", "xAxisName", "yAxisName", "rotateYAxisName", "yAxisNameWidth"]
                        },
                        {
                            id: "Functional Attributes",
                            prop: ["animation", "clickURL", "palette", "paletteThemeColor", "paletteColors", "showRTMenuItem", "showPrintMenuItem", "useRoundEdges", "refreshInstantly", "manageResize", "useEllipsesWhenOverflow"]
                        },
                        {
                            id: "Chart Caption Cosmetics",
                            prop: ["captionAlignment", "captionOnTop", "captionFontSize", "subCaptionFontSize", "captionFont", "subCaptionFont", "captionFontColor", "subCaptionFontColor", "captionFontBold", "subCaptionFontBold", "alignCaptionWithCanvas", "captionHorizontalPadding"]
                        },
                        {
                            id: "X-axis Name Cosmetics",
                            prop: ["xAxisNameFontColor", "xAxisNameFontSize", "xAxisNameFontBold", "xAxisNameFontItalic", "xAxisNameBgColor", "xAxisNameBorderColor", "xAxisNameAlpha", "xAxisNameFontAlpha", "xAxisNameBgAlpha", "xAxisNameBorderAlpha", "xAxisNameBorderPadding", "xAxisNameBorderRadius", "xAxisNameBorderThickness", "xAxisNameBorderDashed", "xAxisNameBorderDashLen", "xAxisNameBorderDashGap"]
                        },
                        {
                            id: "Y-axis Name Cosmetics",
                            prop: ["yAxisNameFont", "yAxisNameFontColor", "yAxisNameFontSize", "yAxisNameFontBold", "yAxisNameFontItalic", "yAxisNameBgColor", "yAxisNameBorderColor", "yAxisNameAlpha", "yAxisNameFontAlpha", "yAxisNameBgAlpha", "yAxisNameBorderAlpha", "yAxisNameBorderPadding", "yAxisNameBorderRadius", "yAxisNameBorderThickness", "yAxisNameBorderDashed", "yAxisNameBorderDashLen", "yAxisNameBorderDashGap"]
                        },
                        {
                            id: "Chart Y-Axis",
                            prop: ["setAdaptiveYMin", "yAxisMinValue", "yAxisMaxValue", "showYAxisValues", "showLimits", "showDivLineValues", "yAxisValuesStep", "yAxisValueDecimals", "forceYAxisDecimals"]
                        },
                        {
                            id: "Charts Cosmetics",
                            prop: ["bgColor", "bgAlpha", "bgRatio", "bgAngle", "showBorder", "borderColor", "borderThickness", "borderAlpha", "bgImage", "bgImageAlpha", "bgImageDisplayMode", "bgImageVAlign", "bgImageHAlign", "bgImageScale", "xAxisLineColor", "logoURL", "logoPosition", "logoAlpha", "logoScale", "logoLink"]
                        },
                        {
                            id: "Canvas Cosmetics",
                            prop: ["canvasBgColor", "canvasBgAlpha", "canvasBgRatio", "canvasBgAngle", "canvasBorderColor", "canvasBorderThickness", "canvasBorderAlpha"]
                        },
                        {
                            id: "Divisional Lines/Grids",
                            prop: ["numDivLines", "divLineColor", "divLineThickness", "divLineAlpha", "divLineDashed", "divLineDashLen", "divLineDashGap", "showAlternateHGridColor", "alternateHGridColor", "alternateHGridAlpha", "numVDivLines", "vDivLineColor", "vDivLineThickness", "vDivLineAlpha", "vDivLineDashed", "vDivLineDashLen", "vDivLineDashGap", "showAlternateVGridColor", "alternateVGridColor", "alternateVGridAlpha"]
                        },
                        {
                            id: "Zero Plane Properties",
                            prop: ["zeroPlaneColor", "zeroPlaneThickness", "zeroPlaneAlpha", "showZeroPlaneValue"]
                        },
                        {
                            id: "Legend Properties",
                            prop: ["showLegend", "legendItemFontBold", "legendItemFont", "legendItemFontSize", "legendItemFontColor", "legendPosition", "legendCaptionAlignment", "legendCaptionBold", "legendCaptionFont", "legendCaptionFontSize", "legendCaptionFontColor", "legendItemHiddenColor", "legendCaption", "legendBorderColor", "legendBorderThickness", "legendBorderAlpha", "legendBgColor", "legendBgAlpha", "legendShadow", "legendAllowDrag", "legendScrollBgColor", "reverseLegend", "legendIconScale", "legendNumColumns", "minimiseWrappingInLegend"]
                        },
                        {
                            id: "Tool-tip",
                            prop: ["showToolTip", "toolTipColor", "toolTipBgColor", "toolTipBorderColor", "toolTipSepChar", "seriesNameInToolTip", "showToolTipShadow"]
                        },
                        {
                            id: "Paddings and Margins",
                            prop: ["captionPadding", "canvasPadding", "xAxisNamePadding", "yAxisNamePadding", "yAxisValuesPadding", "labelPadding", "valuePadding", "realTimeValuePadding", "legendPadding", "chartLeftMargin", "chartRightMargin", "chartTopMargin", "chartBottomMargin", "canvasLeftMargin", "canvasRightMargin", "canvasTopMargin", "canvasBottomMargin"]
                        }
                    ]
                },
                {
                    id: "Data", group: true, sub: [
                        {
                            id: "Data Plot Cosmetics",
                            prop: ["showShadow", "showPlotBorder", "plotBorderColor", "plotBorderThickness", "plotBorderAlpha", "plotBorderDashed", "plotBorderDashLen", "plotBorderDashGap", "plotFillAngle", "plotFillRatio", "plotFillAlpha", "plotGradientColor"]
                        },
                        {
                            id: "Chart Labels and Values",
                            prop: ["showLabels", "maxLabelHeight", "labelDisplay", "slantLabels", "labelStep", "staggerLines", "showValues", "placeValuesInside", "rotateValues"]
                        }
                    ]
                },
                {
                    id: "Others", group: true, sub: [
                        {
                            id: "Real-time Properties",
                            prop: ["dataStreamURL", "refreshInterval", "clearChartInterval", "updateInterval", "numDisplaySets", "dataStamp", "showRealTimeValue", "realTimeValueSep"]
                        },
                        {
                            id: "Number Formatting",
                            prop: ["formatNumber", "numberPrefix", "numberSuffix", "decimals", "forceDecimals", "formatNumberScale", "defaultNumberScale", "numberScaleUnit", "numberScaleValue", "scaleRecursively", "maxScaleRecursion", "scaleSeparator", "decimalSeparator", "thousandSeparator", "thousandSeparatorPosition", "inDecimalSeparator", "inThousandSeparator"]
                        },
                        {
                            id: "Font Properties",
                            prop: ["baseFont", "baseFontSize", "baseFontColor", "outCnvBaseFont", "outCnvBaseFontSize", "outCnvBaseFontColor", "realTimeValueFont", "realTimeValueFontSize", "realTimeValueFontColor"]
                        },
                        {
                            id: "Message Logger",
                            prop: ["useMessageLog", "messageLogWPercent", "messageLogHPercent", "messageLogShowTitle", "messageLogTitle", "messageLogColor", "messageGoesToLog", "messageGoesToJS", "messageJSHandler", "messagePassAllToJS"]
                        }
                    ]
                }
            ],
            "categories": ["font", "fontSize", "fontColor"],
            "category": ["label", "showLabel", "toolText", "font", "fontColor", "fontSize", "fontBold", "fontItalic", "bgColor", "borderColor", "alpha", "bgAlpha", "borderAlpha", "borderPadding", "borderRadius", "borderThickness", "borderDashed", "borderDashLen", "borderDashGap", "link"],
            "dataset": ["seriesName", "color", "alpha", "showValues", "includeInLegend", "checkForAlerts", "dashed"],
            "set": ["value", "link", "toolText", "color", "alpha", "showValue", "dashed"],
            "Vertical data separator lines": ["label", "color", "thickness", "alpha", "dashed", "dashLen", "dashGap"],
            "Trend Lines": ["startValue", "endValue", "displayValue", "color", "thickness", "isTrendZone", "alpha", "showOnTop", "dashed", "dashLen", "dashGap", "valueOnRight"]
        },
        RealTimeStackedColumn: {
            chart: [
                {
                    id: "Chart", group: true, sub: [
                        {
                            id: "Titles (Axis) Names",
                            prop: ["caption", "subCaption", "xAxisName", "yAxisName", "rotateYAxisName", "yAxisNameWidth"]
                        },
                        {
                            id: "Functional Attributes",
                            prop: ["animation", "clickURL", "palette", "paletteThemeColor", "paletteColors", "showRTMenuItem", "showPrintMenuItem", "useRoundEdges", "refreshInstantly", "stack100Percent", "showPercentValues", "showPercentInToolTip", "manageResize", "useEllipsesWhenOverflow"]
                        },
                        {
                            id: "Chart Caption Cosmetics",
                            prop: ["captionAlignment", "captionOnTop", "captionFontSize", "subCaptionFontSize", "captionFont", "subCaptionFont", "captionFontColor", "subCaptionFontColor", "captionFontBold", "subCaptionFontBold", "alignCaptionWithCanvas", "captionHorizontalPadding"]
                        },
                        {
                            id: "X-axis Name Cosmetics",
                            prop: ["xAxisNameFontColor", "xAxisNameFontSize", "xAxisNameFontBold", "xAxisNameFontItalic", "xAxisNameBgColor", "xAxisNameBorderColor", "xAxisNameAlpha", "xAxisNameFontAlpha", "xAxisNameBgAlpha", "xAxisNameBorderAlpha", "xAxisNameBorderPadding", "xAxisNameBorderRadius", "xAxisNameBorderThickness", "xAxisNameBorderDashed", "xAxisNameBorderDashLen", "xAxisNameBorderDashGap"]
                        },
                        {
                            id: "Y-axis Name Cosmetics",
                            prop: ["yAxisNameFont", "yAxisNameFontColor", "yAxisNameFontSize", "yAxisNameFontBold", "yAxisNameFontItalic", "yAxisNameBgColor", "yAxisNameBorderColor", "yAxisNameAlpha", "yAxisNameFontAlpha", "yAxisNameBgAlpha", "yAxisNameBorderAlpha", "yAxisNameBorderPadding", "yAxisNameBorderRadius", "yAxisNameBorderThickness", "yAxisNameBorderDashed", "yAxisNameBorderDashLen", "yAxisNameBorderDashGap"]
                        },
                        {
                            id: "Chart Y-Axis",
                            prop: ["setAdaptiveYMin", "yAxisMinValue", "yAxisMaxValue", "showYAxisValues", "showLimits", "showDivLineValues", "yAxisValuesStep", "yAxisValueDecimals", "forceYAxisDecimals"]
                        },
                        {
                            id: "Charts Cosmetics",
                            prop: ["bgColor", "bgAlpha", "bgRatio", "bgAngle", "showBorder", "borderColor", "borderThickness", "borderAlpha", "bgImage", "bgImageAlpha", "bgImageDisplayMode", "bgImageVAlign", "bgImageHAlign", "bgImageScale", "xAxisLineColor", "logoURL", "logoPosition", "logoAlpha", "logoScale", "logoLink"]
                        },
                        {
                            id: "Canvas Cosmetics",
                            prop: ["canvasBgColor", "canvasBgAlpha", "canvasBgRatio", "canvasBgAngle", "canvasBorderColor", "canvasBorderThickness", "canvasBorderAlpha"]
                        },
                        {
                            id: "Divisional Lines/Grids",
                            prop: ["numDivLines", "divLineColor", "divLineThickness", "divLineAlpha", "divLineDashed", "divLineDashLen", "divLineDashGap", "showAlternateHGridColor", "alternateHGridColor", "alternateHGridAlpha", "numVDivLines", "vDivLineColor", "vDivLineThickness", "vDivLineAlpha", "vDivLineDashed", "vDivLineDashLen", "vDivLineDashGap", "showAlternateVGridColor", "alternateVGridColor", "alternateVGridAlpha"]
                        },
                        {
                            id: "Zero Plane Properties",
                            prop: ["zeroPlaneColor", "zeroPlaneThickness", "zeroPlaneAlpha", "showZeroPlaneValue"]
                        },
                        {
                            id: "Legend Properties",
                            prop: ["showLegend", "legendItemFontBold", "legendItemFont", "legendItemFontSize", "legendItemFontColor", "legendPosition", "legendCaptionAlignment", "legendCaptionBold", "legendCaptionFont", "legendCaptionFontSize", "legendCaptionFontColor", "legendItemHiddenColor", "legendCaption", "legendBorderColor", "legendBorderThickness", "legendBorderAlpha", "legendBgColor", "legendBgAlpha", "legendShadow", "legendAllowDrag", "legendScrollBgColor", "reverseLegend", "legendIconScale", "legendNumColumns", "minimiseWrappingInLegend"]
                        },
                        {
                            id: "Tool-tip",
                            prop: ["showToolTip", "toolTipColor", "toolTipBgColor", "toolTipBorderColor", "toolTipSepChar", "seriesNameInToolTip", "showToolTipShadow"]
                        },
                        {
                            id: "Paddings and Margins",
                            prop: ["captionPadding", "canvasPadding", "xAxisNamePadding", "yAxisNamePadding", "yAxisValuesPadding", "labelPadding", "valuePadding", "realTimeValuePadding", "legendPadding", "chartLeftMargin", "chartRightMargin", "chartTopMargin", "chartBottomMargin", "canvasLeftMargin", "canvasRightMargin", "canvasTopMargin", "canvasBottomMargin"]
                        }
                    ]
                },
                {
                    id: "Data", group: true, sub: [
                        {
                            id: "Data Plot Cosmetics",
                            prop: ["showShadow", "showPlotBorder", "plotBorderColor", "plotBorderThickness", "plotBorderAlpha", "plotBorderDashed", "plotBorderDashLen", "plotBorderDashGap", "plotFillAngle", "plotFillRatio", "plotFillAlpha", "plotGradientColor"]
                        },
                        {
                            id: "Chart Labels and Values",
                            prop: ["showLabels", "maxLabelHeight", "labelDisplay", "slantLabels", "labelStep", "staggerLines", "showValues", "placeValuesInside", "rotateValues", "showSum"]
                        }
                    ]
                },
                {
                    id: "Others", group: true, sub: [
                        {
                            id: "Real-time Properties",
                            prop: ["dataStreamURL", "refreshInterval", "clearChartInterval", "updateInterval", "numDisplaySets", "dataStamp", "showRealTimeValue", "realTimeValueSep"]
                        },
                        {
                            id: "Number Formatting",
                            prop: ["formatNumber", "numberPrefix", "numberSuffix", "decimals", "forceDecimals", "formatNumberScale", "defaultNumberScale", "numberScaleUnit", "numberScaleValue", "scaleRecursively", "maxScaleRecursion", "scaleSeparator", "decimalSeparator", "thousandSeparator", "thousandSeparatorPosition", "inDecimalSeparator", "inThousandSeparator"]
                        },
                        {
                            id: "Font Properties",
                            prop: ["baseFont", "baseFontSize", "baseFontColor", "outCnvBaseFont", "outCnvBaseFontSize", "outCnvBaseFontColor", "realTimeValueFont", "realTimeValueFontSize", "realTimeValueFontColor"]
                        },
                        {
                            id: "Message Logger",
                            prop: ["useMessageLog", "messageLogWPercent", "messageLogHPercent", "messageLogShowTitle", "messageLogTitle", "messageLogColor", "messageGoesToLog", "messageGoesToJS", "messageJSHandler", "messagePassAllToJS"]
                        }
                    ]
                }
            ],
            "categories": ["font", "fontSize", "fontColor"],
            "category": ["label", "showLabel", "toolText", "font", "fontColor", "fontSize", "fontBold", "fontItalic", "bgColor", "borderColor", "alpha", "bgAlpha", "borderAlpha", "borderPadding", "borderRadius", "borderThickness", "borderDashed", "borderDashLen", "borderDashGap", "link"],
            "dataset": ["seriesName", "color", "alpha", "showValues", "includeInLegend", "checkForAlerts", "dashed"],
            "set": ["value", "link", "toolText", "color", "alpha", "showValue", "dashed"],
            "Vertical data separator lines": ["label", "color", "thickness", "alpha", "dashed", "dashLen", "dashGap"],
            "Trend Lines": ["startValue", "endValue", "displayValue", "color", "thickness", "isTrendZone", "alpha", "showOnTop", "dashed", "dashLen", "dashGap", "valueOnRight"]
        },

        // the following 5 have root value prop
        /*Bulb:{
            chart:[
                {id:"Chart", group:true, sub:[
                    {id:"Titles (Axis) Names", prop:["caption","subCaption"]},
                    {id:"Functional Attributes", prop:["animation","clickURL","palette","paletteThemeColor","annRenderDelay","autoScale","manageResize","origW","origH","showValue","useColorNameAsValue","placeValuesInside","showPrintMenuItem","refreshInstantly","useEllipsesWhenOverflow"]},
                    {id:"Chart Caption Cosmetics",prop:["captionAlignment","captionOnTop","captionFontSize","subCaptionFontSize","captionFont","subCaptionFont","captionFontColor","subCaptionFontColor","captionFontBold","subCaptionFontBold","alignCaptionWithCanvas","captionHorizontalPadding"]},
                    {id:"Gauge Scale (Color Range)",prop:["gaugeFillAlpha","gaugeOriginX","gaugeOriginY","gaugeRadius","showGaugeBorder","gaugeBorderColor","gaugeBorderThickness","gaugeBorderAlpha","is3D","upperLimit","lowerLimit"]},
                    {id:"Charts Cosmetics", prop:["bgColor","bgAlpha","bgRatio","bgAngle","showBorder","borderColor","borderThickness","borderAlpha","bgImage","bgImageAlpha","bgImageDisplayMode","bgImageVAlign","bgImageHAlign","bgImageScale","xAxisLineColor","logoURL","logoPosition","logoAlpha","logoScale","logoLink"]},
                    {id:"Tool-tip", prop:["showToolTip","toolTipColor","toolTipBgColor","toolTipBorderColor","showToolTipShadow"]},
                    {id:"Paddings and Margins", prop:["chartLeftMargin","chartRightMargin","chartTopMargin","chartBottomMargin","valuePadding"]}
                ]},
                {id:"Others", group:true, sub:[
                    {id:"Real-time Properties",prop:["dataStreamURL","refreshInterval","dataStamp","showRTMenuItem"]},
                    {id:"Number Formatting", prop:["formatNumber","numberPrefix","numberSuffix","decimals","forceDecimals","formatNumberScale","defaultNumberScale","numberScaleUnit","numberScaleValue","scaleRecursively","maxScaleRecursion","scaleSeparator","decimalSeparator","thousandSeparator","thousandSeparatorPosition","inDecimalSeparator","inThousandSeparator"]},
                    {id:"Font Properties", prop:["baseFont","baseFontSize","baseFontColor"]},
                    {id:"Message Logger",prop:["useMessageLog","messageLogWPercent","messageLogHPercent","messageLogShowTitle","messageLogTitle","messageLogColor","messageGoesToLog","messageGoesToJS","messageJSHandler","messagePassAllToJS"]}
                ]},
                {id:"Chart Value", root:1, prop:["value"]}
            ],
            //"colorrange" <=> Trend Lines/line
            "Color Range":["minValue","maxValue","code","label","alpha","borderColor","borderAlpha"]
        }, */
        HLED: {
            chart: [
                {
                    id: "Chart", group: true, sub: [
                        {id: "Titles (Axis) Names", prop: ["caption", "subCaption"]},
                        {
                            id: "Chart Caption Cosmetics",
                            prop: ["captionAlignment", "captionOnTop", "captionFontSize", "subCaptionFontSize", "captionFont", "subCaptionFont", "captionFontColor", "subCaptionFontColor", "captionFontBold", "subCaptionFontBold", "alignCaptionWithCanvas", "captionHorizontalPadding"]
                        },
                        {
                            id: "Functional Attributes",
                            prop: ["animation", "clickURL", "palette", "paletteThemeColor", "annRenderDelay", "showValue", "autoScale", "manageResize", "origW", "origH", "showShadow", "showPrintMenuItem", "refreshInstantly", "useEllipsesWhenOverflow"]
                        },
                        {
                            id: "Axis and Tick Mark Properties",
                            prop: ["setAdaptiveMin", "upperLimit", "lowerLimit", "lowerLimitDisplay", "upperLimitDisplay", "showTickMarks", "showTickValues", "showLimits", "adjustTM", "ticksBelowGauge", "majorTMNumber", "majorTMColor", "majorTMAlpha", "majorTMHeight", "majorTMThickness", "minorTMNumber", "minorTMColor", "minorTMAlpha", "minorTMHeight", "minorTMThickness", "tickMarkDistance", "tickValueDistance", "tickValueStep", "tickValueDecimals", "forceTickValueDecimals"]
                        },
                        {
                            id: "Gauge Scale (Color Range)",
                            prop: ["gaugeFillColor", "showGaugeBorder", "gaugeBorderColor", "gaugeBorderThickness", "gaugeBorderAlpha"]
                        },
                        {id: "LED Properties", prop: ["ledSize", "ledGap", "useSameFillColor", "useSameFillBgColor"]},
                        {
                            id: "Charts Cosmetics",
                            prop: ["bgColor", "bgAlpha", "bgRatio", "bgAngle", "showBorder", "borderColor", "borderThickness", "borderAlpha", "bgImage", "bgImageAlpha", "bgImageDisplayMode", "bgImageVAlign", "bgImageHAlign", "bgImageScale", "xAxisLineColor", "logoURL", "logoPosition", "logoAlpha", "logoScale", "logoLink"]
                        },
                        {
                            id: "Tool-tip",
                            prop: ["showToolTip", "toolTipColor", "toolTipBgColor", "toolTipBorderColor", "showToolTipShadow"]
                        },
                        {
                            id: "Paddings and Margins",
                            prop: ["chartLeftMargin", "chartRightMargin", "chartTopMargin", "chartBottomMargin", "valuePadding"]
                        }
                    ]
                },
                {
                    id: "Others", group: true, sub: [
                        {
                            id: "Real-time Properties",
                            prop: ["dataStreamURL", "refreshInterval", "dataStamp", "showRTMenuItem"]
                        },
                        {
                            id: "Number Formatting",
                            prop: ["formatNumber", "numberPrefix", "numberSuffix", "decimals", "forceDecimals", "formatNumberScale", "defaultNumberScale", "numberScaleUnit", "numberScaleValue", "scaleRecursively", "maxScaleRecursion", "scaleSeparator", "decimalSeparator", "thousandSeparator", "thousandSeparatorPosition", "inDecimalSeparator", "inThousandSeparator"]
                        },
                        {id: "Font Properties", prop: ["baseFont", "baseFontSize", "baseFontColor"]},
                        {
                            id: "Message Logger",
                            prop: ["useMessageLog", "messageLogWPercent", "messageLogHPercent", "messageLogShowTitle", "messageLogTitle", "messageLogColor", "messageGoesToLog", "messageGoesToJS", "messageJSHandler", "messagePassAllToJS"]
                        }
                    ]
                },
                {id: "Chart Value", root: 1, prop: ["value"]}
            ],
            //colorrange
            "Color Range": ["minValue", "maxValue", "code", "alpha", "borderColor", "borderAlpha"]
        },
        VLED: {
            chart: [
                {
                    id: "Chart", group: true, sub: [
                        {id: "Titles (Axis) Names", prop: ["caption", "subCaption"]},
                        {
                            id: "Chart Caption Cosmetics",
                            prop: ["captionAlignment", "captionOnTop", "captionFontSize", "subCaptionFontSize", "captionFont", "subCaptionFont", "captionFontColor", "subCaptionFontColor", "captionFontBold", "subCaptionFontBold", "alignCaptionWithCanvas", "captionHorizontalPadding"]
                        },
                        {
                            id: "Functional Attributes",
                            prop: ["animation", "clickURL", "palette", "paletteThemeColor", "annRenderDelay", "showValue", "autoScale", "manageResize", "origW", "origH", "showShadow", "showPrintMenuItem", "refreshInstantly", "useEllipsesWhenOverflow"]
                        },
                        {
                            id: "Axis and Tick Mark Properties",
                            prop: ["setAdaptiveMin", "upperLimit", "lowerLimit", "lowerLimitDisplay", "upperLimitDisplay", "showTickMarks", "showTickValues", "showLimits", "adjustTM", "ticksOnRight", "majorTMNumber", "majorTMColor", "majorTMAlpha", "majorTMHeight", "majorTMThickness", "minorTMNumber", "minorTMColor", "minorTMAlpha", "minorTMHeight", "minorTMThickness", "tickMarkDistance", "tickValueDistance", "tickValueStep", "tickValueDecimals", "forceTickValueDecimals"]
                        },
                        {
                            id: "Gauge Scale (Color Range)",
                            prop: ["gaugeFillColor", "showGaugeBorder", "gaugeBorderColor", "gaugeBorderThickness", "gaugeBorderAlpha"]
                        },
                        {id: "LED Properties", prop: ["ledSize", "ledGap", "useSameFillColor", "useSameFillBgColor"]},
                        {
                            id: "Charts Cosmetics",
                            prop: ["bgColor", "bgAlpha", "bgRatio", "bgAngle", "showBorder", "borderColor", "borderThickness", "borderAlpha", "bgImage", "bgImageAlpha", "bgImageDisplayMode", "bgImageVAlign", "bgImageHAlign", "bgImageScale", "xAxisLineColor", "logoURL", "logoPosition", "logoAlpha", "logoScale", "logoLink"]
                        },
                        {
                            id: "Tool-tip",
                            prop: ["showToolTip", "toolTipColor", "toolTipBgColor", "toolTipBorderColor", "showToolTipShadow"]
                        },
                        {
                            id: "Paddings and Margins",
                            prop: ["chartLeftMargin", "chartRightMargin", "chartTopMargin", "chartBottomMargin", "valuePadding"]
                        }
                    ]
                },
                {
                    id: "Others", group: true, sub: [
                        {
                            id: "Real-time Properties",
                            prop: ["dataStreamURL", "refreshInterval", "dataStamp", "showRTMenuItem"]
                        },
                        {
                            id: "Number Formatting",
                            prop: ["formatNumber", "numberPrefix", "numberSuffix", "decimals", "forceDecimals", "formatNumberScale", "defaultNumberScale", "numberScaleUnit", "numberScaleValue", "scaleRecursively", "maxScaleRecursion", "scaleSeparator", "decimalSeparator", "thousandSeparator", "thousandSeparatorPosition", "inDecimalSeparator", "inThousandSeparator"]
                        },
                        {id: "Font Properties", prop: ["baseFont", "baseFontSize", "baseFontColor"]},
                        {
                            id: "Message Logger",
                            prop: ["useMessageLog", "messageLogWPercent", "messageLogHPercent", "messageLogShowTitle", "messageLogTitle", "messageLogColor", "messageGoesToLog", "messageGoesToJS", "messageJSHandler", "messagePassAllToJS"]
                        }
                    ]
                },
                {id: "Chart Value", root: 1, prop: ["value"]}
            ],
            //colorrange   <=> Trend Lines/line
            "Color Range": ["minValue", "maxValue", "code", "alpha", "borderColor", "borderAlpha"]
        },
        Cylinder: {
            chart: [
                {
                    id: "Chart", group: true, sub: [
                        {id: "Titles (Axis) Names", prop: ["caption", "subCaption"]},
                        {
                            id: "Chart Caption Cosmetics",
                            prop: ["captionAlignment", "captionOnTop", "captionFontSize", "subCaptionFontSize", "captionFont", "subCaptionFont", "captionFontColor", "subCaptionFontColor", "captionFontBold", "subCaptionFontBold", "alignCaptionWithCanvas", "captionHorizontalPadding"]
                        },
                        {
                            id: "Functional Attributes",
                            prop: ["animation", "showValue", "clickURL", "palette", "paletteThemeColor", "annRenderDelay", "autoScale", "manageResize", "origW", "origH", "showPrintMenuItem", "refreshInstantly", "use3DLighting"]
                        },
                        {
                            id: "Axis and Tick Mark Properties",
                            prop: ["setAdaptiveMin", "upperLimit", "lowerLimit", "lowerLimitDisplay", "upperLimitDisplay", "showTickMarks", "showTickValues", "showLimits", "adjustTM", "ticksOnRight", "majorTMNumber", "majorTMColor", "majorTMAlpha", "majorTMHeight", "majorTMThickness", "minorTMNumber", "minorTMColor", "minorTMAlpha", "minorTMHeight", "minorTMThickness", "tickMarkDistance", "tickValueDistance", "tickValueStep", "tickValueDecimals", "forceTickValueDecimals"]
                        },
                        {
                            id: "Cylinder Properties",
                            prop: ["cylOriginX", "cylOriginY", "cylRadius", "cylHeight", "cylYScale", "cylFillColor"]
                        },
                        {
                            id: "Charts Cosmetics",
                            prop: ["bgColor", "bgAlpha", "bgRatio", "bgAngle", "showBorder", "borderColor", "borderThickness", "borderAlpha", "bgImage", "bgImageAlpha", "bgImageDisplayMode", "bgImageVAlign", "bgImageHAlign", "bgImageScale", "xAxisLineColor", "logoURL", "logoPosition", "logoAlpha", "logoScale", "logoLink"]
                        },
                        {
                            id: "Tool-tip",
                            prop: ["showToolTip", "toolTipColor", "toolTipBgColor", "toolTipBorderColor", "showToolTipShadow"]
                        },
                        {
                            id: "Paddings and Margins",
                            prop: ["chartLeftMargin", "chartRightMargin", "chartTopMargin", "chartBottomMargin", "valuePadding"]
                        }
                    ]
                },
                {
                    id: "Others", group: true, sub: [
                        {
                            id: "Real-time Properties",
                            prop: ["dataStreamURL", "refreshInterval", "dataStamp", "showRTMenuItem"]
                        },
                        {
                            id: "Number Formatting",
                            prop: ["formatNumber", "numberPrefix", "numberSuffix", "decimals", "forceDecimals", "formatNumberScale", "defaultNumberScale", "numberScaleUnit", "numberScaleValue", "scaleRecursively", "maxScaleRecursion", "scaleSeparator", "decimalSeparator", "thousandSeparator", "thousandSeparatorPosition", "inDecimalSeparator", "inThousandSeparator"]
                        },
                        {id: "Font Properties", prop: ["baseFont", "baseFontSize", "baseFontColor"]},
                        {
                            id: "Message Logger",
                            prop: ["useMessageLog", "messageLogWPercent", "messageLogHPercent", "messageLogShowTitle", "messageLogTitle", "messageLogColor", "messageGoesToLog", "messageGoesToJS", "messageJSHandler", "messagePassAllToJS"]
                        }
                    ]
                },
                {id: "Chart Value", root: 1, prop: ["value"]}
            ]
        },
        Thermometer: {
            chart: [
                {
                    id: "Chart", group: true, sub: [
                        {id: "Titles (Axis) Names", prop: ["caption", "subCaption"]},
                        {
                            id: "Chart Caption Cosmetics",
                            prop: ["captionAlignment", "captionOnTop", "captionFontSize", "subCaptionFontSize", "captionFont", "subCaptionFont", "captionFontColor", "subCaptionFontColor", "captionFontBold", "subCaptionFontBold", "alignCaptionWithCanvas", "captionHorizontalPadding"]
                        },
                        {
                            id: "Functional Attributes",
                            prop: ["animation", "clickURL", "palette", "paletteThemeColor", "annRenderDelay", "autoScale", "manageResize", "origW", "origH", "showValue", "showShadow", "use3DLighting", "showPrintMenuItem", "refreshInstantly", "useEllipsesWhenOverflow"]
                        },
                        {
                            id: "Axis and Tick Mark Properties",
                            prop: ["setAdaptiveMin", "upperLimit", "lowerLimit", "lowerLimitDisplay", "upperLimitDisplay", "showTickMarks", "showTickValues", "showLimits", "adjustTM", "ticksOnRight", "majorTMNumber", "majorTMColor", "majorTMAlpha", "majorTMHeight", "majorTMThickness", "minorTMNumber", "minorTMColor", "minorTMAlpha", "minorTMHeight", "minorTMThickness", "tickMarkDistance", "tickValueDistance", "tickValueStep", "tickValueDecimals", "forceTickValueDecimals"]
                        },
                        {
                            id: "Thermometer  Properties",
                            prop: ["thmBulbRadius", "thmHeight", "gaugeFillColor", "gaugeFillAlpha", "showGaugeBorder", "gaugeBorderColor", "gaugeBorderThickness", "gaugeBorderAlpha"]
                        },
                        {
                            id: "Charts Cosmetics",
                            prop: ["bgColor", "bgAlpha", "bgRatio", "bgAngle", "showBorder", "borderColor", "borderThickness", "borderAlpha", "bgImage", "bgImageAlpha", "bgImageDisplayMode", "bgImageVAlign", "bgImageHAlign", "bgImageScale", "xAxisLineColor", "logoURL", "logoPosition", "logoAlpha", "logoScale", "logoLink"]
                        },
                        {
                            id: "Tool-tip",
                            prop: ["showToolTip", "toolTipColor", "toolTipBgColor", "toolTipBorderColor", "showToolTipShadow"]
                        },
                        {
                            id: "Paddings and Margins",
                            prop: ["chartLeftMargin", "chartRightMargin", "chartTopMargin", "chartBottomMargin", "valuePadding"]
                        }
                    ]
                },
                {
                    id: "Others", group: true, sub: [
                        {
                            id: "Real-time Properties",
                            prop: ["dataStreamURL", "refreshInterval", "dataStamp", "showRTMenuItem"]
                        },
                        {
                            id: "Number Formatting",
                            prop: ["formatNumber", "numberPrefix", "numberSuffix", "decimals", "forceDecimals", "formatNumberScale", "defaultNumberScale", "numberScaleUnit", "numberScaleValue", "scaleRecursively", "maxScaleRecursion", "scaleSeparator", "decimalSeparator", "thousandSeparator", "thousandSeparatorPosition", "inDecimalSeparator", "inThousandSeparator"]
                        },
                        {id: "Font Properties", prop: ["baseFont", "baseFontSize", "baseFontColor"]},
                        {
                            id: "Message Logger",
                            prop: ["useMessageLog", "messageLogWPercent", "messageLogHPercent", "messageLogShowTitle", "messageLogTitle", "messageLogColor", "messageGoesToLog", "messageGoesToJS", "messageJSHandler", "messagePassAllToJS"]
                        }
                    ]
                },
                {id: "Chart Value", root: 1, prop: ["value"]}
            ]
        },

        // special setting
        HLinearGauge: {
            chart: [
                {
                    id: "Chart", group: true, sub: [
                        {id: "Titles (Axis) Names", prop: ["caption", "subCaption"]},
                        {
                            id: "Chart Caption Cosmetics",
                            prop: ["captionAlignment", "captionOnTop", "captionFontSize", "subCaptionFontSize", "captionFont", "subCaptionFont", "captionFontColor", "subCaptionFontColor", "captionFontBold", "subCaptionFontBold", "alignCaptionWithCanvas", "captionHorizontalPadding"]
                        },
                        {
                            id: "Functional Attributes",
                            prop: ["editMode", "animation", "clickURL", "palette", "paletteThemeColor", "annRenderDelay", "autoScale", "manageResize", "origW", "origH", "showValue", "valueAbovePointer", "pointerOnTop", "showShadow", "showPrintMenuItem", "refreshInstantly", "useEllipsesWhenOverflow"]
                        },
                        {
                            id: "Axis and Tick Mark Properties",
                            prop: ["setAdaptiveMin", "upperLimit", "lowerLimit", "lowerLimitDisplay", "upperLimitDisplay", "showTickMarks", "showTickValues", "showLimits", "adjustTM", "ticksBelowGauge", "placeTicksInside", "placeValuesInside", "majorTMNumber", "majorTMColor", "majorTMAlpha", "majorTMHeight", "majorTMThickness", "minorTMNumber", "minorTMColor", "minorTMAlpha", "minorTMHeight", "minorTMThickness", "tickMarkDistance", "tickValueDistance", "trendValueDistance", "tickValueStep", "tickValueDecimals", "forceTickValueDecimals"]
                        },
                        {
                            id: "Gauge Scale (Color Range)",
                            prop: ["showGaugeLabels", "gaugeRoundRadius", "gaugeFillMix", "gaugeFillRatio", "showGaugeBorder", "gaugeBorderColor", "gaugeBorderThickness", "gaugeBorderAlpha"]
                        },
                        {
                            id: "Pointer Properties",
                            prop: ["pointerRadius", "pointerBgColor", "pointerBgAlpha", "pointerSides", "pointerBorderThickness", "pointerBorderColor", "pointerBorderAlpha"]
                        },
                        {
                            id: "Charts Cosmetics",
                            prop: ["bgColor", "bgAlpha", "bgRatio", "bgAngle", "showBorder", "borderColor", "borderThickness", "borderAlpha", "bgImage", "bgImageAlpha", "bgImageDisplayMode", "bgImageVAlign", "bgImageHAlign", "bgImageScale", "xAxisLineColor", "logoURL", "logoPosition", "logoAlpha", "logoScale", "logoLink"]
                        },
                        {
                            id: "Tool-tip",
                            prop: ["showToolTip", "toolTipColor", "toolTipBgColor", "toolTipBorderColor", "showToolTipShadow"]
                        },
                        {
                            id: "Paddings and Margins",
                            prop: ["chartLeftMargin", "chartRightMargin", "chartTopMargin", "chartBottomMargin", "valuePadding"]
                        }
                    ]
                },
                {
                    id: "Others", group: true, sub: [
                        {
                            id: "Real-time Properties",
                            prop: ["dataStreamURL", "refreshInterval", "dataStamp", "showRTMenuItem"]
                        },
                        {
                            id: "Number Formatting",
                            prop: ["formatNumber", "numberPrefix", "numberSuffix", "decimals", "forceDecimals", "formatNumberScale", "defaultNumberScale", "numberScaleUnit", "numberScaleValue", "scaleRecursively", "maxScaleRecursion", "scaleSeparator", "decimalSeparator", "thousandSeparator", "thousandSeparatorPosition", "inDecimalSeparator", "inThousandSeparator"]
                        },
                        {id: "Font Properties", prop: ["baseFont", "baseFontSize", "baseFontColor"]},
                        {
                            id: "Message Logger",
                            prop: ["useMessageLog", "messageLogWPercent", "messageLogHPercent", "messageLogShowTitle", "messageLogTitle", "messageLogColor", "messageGoesToLog", "messageGoesToJS", "messageJSHandler", "messagePassAllToJS"]
                        }
                    ]
                }
            ],
            //colorrange  <=> Trend Lines/line
            "Color Range": ["minValue", "maxValue", "code", "label", "alpha", "borderColor", "borderAlpha"],
            //trendpoint  <=> Vertical Trend Lines/line
            "Trend Point": ["startValue", "endValue", "displayValue", "showOnTop", "color", "alpha", "dashed", "dashLen", "dashGap", "useMarker", "markerColor", "markerBorderColor", "markerRadius", "markerTooltext"],
            //pointers <=> data
            "pointer": ["id", "value", "showValue", "editMode", "borderColor", "borderThickness", "borderAlpha", "color", "bgAlpha", "radius", "sides", "link", "toolText"]
        },
        AngularGauge: {
            chart: [
                {
                    id: "Chart", group: true, sub: [
                        {id: "Titles (Axis) Names", prop: ["caption", "subCaption"]},
                        {
                            id: "Chart Caption Cosmetics",
                            prop: ["captionAlignment", "captionOnTop", "captionFontSize", "subCaptionFontSize", "captionFont", "subCaptionFont", "captionFontColor", "subCaptionFontColor", "captionFontBold", "subCaptionFontBold", "alignCaptionWithCanvas", "captionHorizontalPadding"]
                        },
                        {
                            id: "Functional Attributes",
                            prop: ["editMode", "animation", "clickURL", "palette", "paletteThemeColor", "annRenderDelay", "autoScale", "manageResize", "origW", "origH", "showValue", "valueBelowPivot", "showShadow", "showPrintMenuItem", "refreshInstantly", "useEllipsesWhenOverflow"]
                        },
                        {
                            id: "Axis and Tick Mark Properties",
                            prop: ["setAdaptiveMin", "upperLimit", "lowerLimit", "lowerLimitDisplay", "upperLimitDisplay", "showTickMarks", "showTickValues", "showLimits", "adjustTM", "placeTicksInside", "placeValuesInside", "majorTMNumber", "majorTMColor", "majorTMAlpha", "majorTMHeight", "majorTMThickness", "minorTMNumber", "minorTMColor", "minorTMAlpha", "minorTMHeight", "minorTMThickness", "tickValueDistance", "trendValueDistance", "tickValueStep", "tickValueDecimals", "forceTickValueDecimals", "autoAlignTickValues"]
                        },
                        {
                            id: "Gauge Scale (Color Range)",
                            prop: ["gaugeStartAngle", "gaugeEndAngle", "gaugeOriginX", "gaugeOriginY", "gaugeOuterRadius", "gaugeInnerRadius", "gaugeFillMix", "gaugeFillRatio", "showGaugeBorder", "gaugeBorderColor", "gaugeBorderThickness", "gaugeBorderAlpha"]
                        },
                        {
                            id: "Pivot Properties",
                            prop: ["pivotRadius", "pivotFillColor", "pivotFillAlpha", "pivotFillAngle", "pivotFillType", "pivotFillMix", "pivotFillRatio", "showPivotBorder", "pivotBorderThickness", "pivotBorderColor", "pivotBorderAlpha"]
                        },
                        {
                            id: "Charts Cosmetics",
                            prop: ["bgColor", "bgAlpha", "bgRatio", "bgAngle", "showBorder", "borderColor", "borderThickness", "borderAlpha", "bgImage", "bgImageAlpha", "bgImageDisplayMode", "bgImageVAlign", "bgImageHAlign", "bgImageScale", "xAxisLineColor", "logoURL", "logoPosition", "logoAlpha", "logoScale", "logoLink"]
                        },
                        {
                            id: "Tool-tip",
                            prop: ["showToolTip", "toolTipColor", "toolTipBgColor", "toolTipBorderColor", "showToolTipShadow"]
                        },
                        {
                            id: "Paddings and Margins",
                            prop: ["chartLeftMargin", "chartRightMargin", "chartTopMargin", "chartBottomMargin"]
                        }
                    ]
                },
                {
                    id: "Others", group: true, sub: [
                        {
                            id: "Real-time Properties",
                            prop: ["dataStreamURL", "refreshInterval", "dataStamp", "showRTMenuItem"]
                        },
                        {
                            id: "Number Formatting",
                            prop: ["formatNumber", "numberPrefix", "numberSuffix", "decimals", "forceDecimals", "formatNumberScale", "defaultNumberScale", "numberScaleUnit", "numberScaleValue", "scaleRecursively", "maxScaleRecursion", "scaleSeparator", "decimalSeparator", "thousandSeparator", "thousandSeparatorPosition", "inDecimalSeparator", "inThousandSeparator"]
                        },
                        {id: "Font Properties", prop: ["baseFont", "baseFontSize", "baseFontColor"]},
                        {
                            id: "Message Logger",
                            prop: ["useMessageLog", "messageLogWPercent", "messageLogHPercent", "messageLogShowTitle", "messageLogTitle", "messageLogColor", "messageGoesToLog", "messageGoesToJS", "messageJSHandler", "messagePassAllToJS"]
                        }
                    ]
                }
            ],
            //colorrange  <=> Trend Lines/line
            "Color Range": ["minValue", "maxValue", "code", "label", "alpha", "borderColor", "borderAlpha"],
    //trendpoint  <=> v Trend Lines/line
    "Trend Point": ["startValue", "endValue", "displayValue", "valueInside", "color", "alpha", "thickness", "showBorder", "borderColor", "radius", "innerRadius", "dashed", "dashLen", "dashGap", "useMarker", "markerColor", "markerBorderColor", "markerRadius", "markerTooltext"],
    //dials   <=> data
    "dial": ["id", "value", "showValue", "valueX", "valueY", "editMode", "borderColor", "borderThickness", "borderAlpha", "bgColor", "radius", "baseWidth", "baseRadius", "topWidth", "rearExtension", "link", "toolText"]
        }
    },
    annotations: {
        annotation: ["autoScale", "constrainScale", "scaleText", "scaleImages", "xShift", "yShift", "grpXShift", "grpYShift", "origW", "origH"],
        group: ["id", "x", "y", "showBelow", "autoScale", "constrainScale", "scaleText", "scaleImages", "xShift", "yShift", "grpXShift", "grpYShift", "link", "color", "alpha", "visible", "showShadow", "toolText", "font", "fontSize", "textAlign", "textVAlign", "rotateText", "wrapText"],
        // item
        line: ["type", "id", "x", "y", "toX", "toY", "fillColor", "fillAlpha", "fillRatio", "fillAngle", "fillPattern", "showBorder", "borderColor", "borderAlpha", "borderThickness", "dashed", "dashLen", "dashGap", "toolText", "link", "showShadow", "thickness"],
        circle: ["type", "id", "x", "y", "toX", "toY", "fillColor", "fillAlpha", "fillRatio", "fillAngle", "fillPattern", "showBorder", "borderColor", "borderAlpha", "borderThickness", "dashed", "dashLen", "dashGap", "toolText", "link", "showShadow", "radius", "yRadius", "startAngle", "endAngle"],
        arc: ["type", "id", "x", "y", "toX", "toY", "fillColor", "fillAlpha", "fillRatio", "fillAngle", "fillPattern", "showBorder", "borderColor", "borderAlpha", "borderThickness", "dashed", "dashLen", "dashGap", "toolText", "link", "showShadow", "radius", "innerRadius", "startAngle", "endAngle"],
        rectangle: ["type", "id", "x", "y", "toX", "toY", "fillColor", "fillAlpha", "fillRatio", "fillAngle", "fillPattern", "showBorder", "borderColor", "borderAlpha", "borderThickness", "dashed", "dashLen", "dashGap", "toolText", "link", "showShadow", "radius"],
        image: ["type", "id", "x", "y", "toX", "toY", "fillColor", "fillAlpha", "fillRatio", "fillAngle", "fillPattern", "showBorder", "borderColor", "borderAlpha", "borderThickness", "dashed", "dashLen", "dashGap", "toolText", "link", "showShadow", "url"],
        polygon: ["type", "id", "x", "y", "toX", "toY", "fillColor", "fillAlpha", "fillRatio", "fillAngle", "fillPattern", "showBorder", "borderColor", "borderAlpha", "borderThickness", "dashed", "dashLen", "dashGap", "toolText", "link", "showShadow", "sides", "radius", "startAngle"],
        path: ["type", "id", "x", "y", "toX", "toY", "fillColor", "fillAlpha", "fillRatio", "fillAngle", "fillPattern", "showBorder", "borderColor", "borderAlpha", "borderThickness", "dashed", "dashLen", "dashGap", "toolText", "link", "showShadow", "path"],
        text: ["type", "id", "x", "y", "toX", "toY", "fillColor", "fillAlpha", "fillRatio", "fillAngle", "fillPattern", "showBorder", "borderColor", "borderAlpha", "borderThickness", "dashed", "dashLen", "dashGap", "toolText", "link", "showShadow", "label", "align", "vAlign", "font", "fontSize", "fontColor", "bold", "italic", "leftMargin", "bgColor", "rotateText", "wrapWidth", "wrapHeight", "wrap"]
    }
};

/*
// To find no configuration items

ood.each(FCCONF.attrCat,function(o1,i1){
    ood.each(o1,function(oo1,key){
        if(key=='chart'){
            ood.arr.each(oo1,function(o2){
                ood.arr.each(o2.sub,function(o3){
                    ood.each(o3.prop,function(key2){
                        if(!(key2 in FCCONF.gridAttr) && !ood.get(FCCONF.gridAttr,[o3.id,key2])){
                            console.log(i1+"=>"+o2.id+"=>"+o3.id+"=>"+key2);
                        }
                    });
                });
            });
        }else{
            ood.each(oo1,function(key2){
                if(!(key2 in FCCONF.gridAttr) && !ood.get(FCCONF.gridAttr,[key,key2])){
                    console.log(i1+"=>"+key+"=>"+key2);
                }
            });
        }
    });
});
ood.each(FCCONF.annotations,function(o1,key){
    ood.arr.each(o1,function(key2){
        if(!(key2 in FCCONF.gridAttr) && !ood.get(FCCONF.gridAttr,["annotations",key2])){
            console.log("annotations=>"+key+"=>"+key2);
        }
    });
});
*/
