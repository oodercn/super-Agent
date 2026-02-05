{
  "alias": "APIMethodBaseView",
  "children": [
    {
      "alias": "PAGECTX",
      "children": [
        {
          "alias": "currentClassName",
          "events": {},
          "key": "ood.UI.HiddenInput",
          "path": "dsm.agg.api.config.APIMethodBaseView.PAGECTX.currentClassName",
          "properties": {
            "formField": true,
            "isPid": true,
            "name": "currentClassName",
            "pid": true,
            "tabindex": 0
          }
        },
        {
          "alias": "xpath",
          "events": {},
          "key": "ood.UI.HiddenInput",
          "path": "dsm.agg.api.config.APIMethodBaseView.PAGECTX.xpath",
          "properties": {
            "formField": true,
            "isPid": true,
            "name": "xpath",
            "pid": true,
            "tabindex": 1
          }
        },
        {
          "alias": "sourceMethodName",
          "events": {},
          "key": "ood.UI.HiddenInput",
          "path": "dsm.agg.api.config.APIMethodBaseView.PAGECTX.sourceMethodName",
          "properties": {
            "formField": true,
            "isPid": true,
            "name": "sourceMethodName",
            "pid": true,
            "tabindex": 2
          }
        },
        {
          "alias": "sourceClassName",
          "events": {},
          "key": "ood.UI.HiddenInput",
          "path": "dsm.agg.api.config.APIMethodBaseView.PAGECTX.sourceClassName",
          "properties": {
            "formField": true,
            "isPid": true,
            "name": "sourceClassName",
            "pid": true,
            "tabindex": 3
          }
        },
        {
          "alias": "projectName",
          "events": {},
          "key": "ood.UI.HiddenInput",
          "path": "dsm.agg.api.config.APIMethodBaseView.PAGECTX.projectName",
          "properties": {
            "formField": true,
            "isPid": false,
            "name": "projectName",
            "pid": false,
            "tabindex": 4,
            "value": "DSMdsmVVVERSION0"
          }
        },
        {
          "alias": "domainId",
          "events": {},
          "key": "ood.UI.HiddenInput",
          "path": "dsm.agg.api.config.APIMethodBaseView.PAGECTX.domainId",
          "properties": {
            "formField": true,
            "isPid": true,
            "name": "domainId",
            "pid": true,
            "tabindex": 5
          }
        },
        {
          "alias": "projectVersionName",
          "events": {},
          "key": "ood.UI.HiddenInput",
          "path": "dsm.agg.api.config.APIMethodBaseView.PAGECTX.projectVersionName",
          "properties": {
            "formField": true,
            "isPid": false,
            "name": "projectVersionName",
            "pid": false,
            "tabindex": 6,
            "value": "DSMdsmVVVERSION0"
          }
        }
      ],
      "events": {},
      "key": "ood.UI.Block",
      "params": {},
      "path": "dsm.agg.api.config.APIMethodBaseView.PAGECTX",
      "properties": {
        "borderType": "none",
        "tabindex": 0,
        "visibility": "hidden"
      }
    },
    {
      "alias": "APIMethodBaseViewMain",
      "children": [
        {
          "alias": "APIMethodBaseView",
          "children": [
            {
              "alias": "url",
              "events": {},
              "key": "ood.UI.Input",
              "path": "dsm.agg.api.config.APIMethodBaseView.APIMethodBaseViewMain.APIMethodBaseView.url",
              "properties": {
                "caption": "访问地址",
                "defaultFocus": false,
                "desc": "访问地址",
                "disableClickEffect": false,
                "disableHoverEffect": false,
                "disableTips": false,
                "disabled": false,
                "dock": "none",
                "events": {},
                "id": "url",
                "imageClass": "",
                "items": [],
                "labelCaption": "访问地址",
                "labelPos": "left",
                "labelSize": "6.0em",
                "name": "url",
                "readonly": false,
                "tabindex": 0,
                "tagVar": {},
                "visibility": "visible"
              },
              "target": "B1"
            },
            {
              "alias": "autoDisplay",
              "events": {},
              "key": "ood.UI.CheckBox",
              "path": "dsm.agg.api.config.APIMethodBaseView.APIMethodBaseViewMain.APIMethodBaseView.autoDisplay",
              "properties": {
                "caption": "",
                "defaultFocus": false,
                "desc": "自动显示",
                "disableClickEffect": false,
                "disableHoverEffect": false,
                "disableTips": false,
                "dock": "none",
                "events": {},
                "id": "autoDisplay",
                "name": "autoDisplay",
                "tabindex": 1,
                "visibility": "visible"
              },
              "target": "B2"
            },
            {
              "alias": "index",
              "events": {},
              "key": "ood.UI.ComboInput",
              "path": "dsm.agg.api.config.APIMethodBaseView.APIMethodBaseViewMain.APIMethodBaseView.index",
              "properties": {
                "caption": "显示顺序",
                "currencyTpl": "$ *",
                "decimalSeparator": ".",
                "defaultFocus": false,
                "desc": "显示顺序",
                "disableClickEffect": false,
                "disableHoverEffect": false,
                "disableTips": false,
                "disabled": false,
                "dock": "none",
                "forceFillZero": true,
                "increment": 1,
                "items": [],
                "labelCaption": "显示顺序",
                "name": "index",
                "precision": 0,
                "readonly": false,
                "tabindex": 2,
                "type": "spin",
                "visibility": "visible"
              },
              "target": "D2"
            },
            {
              "alias": "responseBody",
              "events": {},
              "key": "ood.UI.CheckBox",
              "path": "dsm.agg.api.config.APIMethodBaseView.APIMethodBaseViewMain.APIMethodBaseView.responseBody",
              "properties": {
                "caption": "",
                "defaultFocus": false,
                "desc": "JSON格式",
                "disableClickEffect": false,
                "disableHoverEffect": false,
                "disableTips": false,
                "dock": "none",
                "events": {},
                "id": "responseBody",
                "name": "responseBody",
                "tabindex": 3,
                "visibility": "visible"
              },
              "target": "B3"
            },
            {
              "alias": "queryAsync",
              "events": {},
              "key": "ood.UI.CheckBox",
              "path": "dsm.agg.api.config.APIMethodBaseView.APIMethodBaseViewMain.APIMethodBaseView.queryAsync",
              "properties": {
                "caption": "",
                "defaultFocus": false,
                "desc": "异步调用",
                "disableClickEffect": false,
                "disableHoverEffect": false,
                "disableTips": false,
                "dock": "none",
                "events": {},
                "id": "queryAsync",
                "name": "queryAsync",
                "tabindex": 4,
                "visibility": "visible"
              },
              "target": "D3"
            },
            {
              "alias": "autoRun",
              "events": {},
              "key": "ood.UI.CheckBox",
              "path": "dsm.agg.api.config.APIMethodBaseView.APIMethodBaseViewMain.APIMethodBaseView.autoRun",
              "properties": {
                "caption": "",
                "defaultFocus": false,
                "desc": "自动运行",
                "disableClickEffect": false,
                "disableHoverEffect": false,
                "disableTips": false,
                "dock": "none",
                "events": {},
                "id": "autoRun",
                "name": "autoRun",
                "tabindex": 5,
                "visibility": "visible"
              },
              "target": "B4"
            },
            {
              "alias": "allform",
              "events": {},
              "key": "ood.UI.CheckBox",
              "path": "dsm.agg.api.config.APIMethodBaseView.APIMethodBaseViewMain.APIMethodBaseView.allform",
              "properties": {
                "caption": "",
                "defaultFocus": false,
                "desc": "级联处理",
                "disableClickEffect": false,
                "disableHoverEffect": false,
                "disableTips": false,
                "dock": "none",
                "events": {},
                "id": "allform",
                "name": "allform",
                "tabindex": 6,
                "visibility": "visible"
              },
              "target": "D4"
            },
            {
              "alias": "method",
              "apis": [],
              "events": {},
              "id": "method",
              "key": "ood.UI.List",
              "path": "dsm.agg.api.config.APIMethodBaseView.APIMethodBaseViewMain.APIMethodBaseView.method",
              "properties": {
                "borderType": "flat",
                "defaultFocus": false,
                "disableClickEffect": false,
                "disableHoverEffect": false,
                "disableTips": false,
                "disabled": false,
                "dock": "none",
                "dynLoad": true,
                "enumClass": "java.lang.Enum",
                "events": {},
                "height": "15.0em",
                "id": "method",
                "itemRow": "cell",
                "items": [
                  {
                    "bindClass": [],
                    "caption": "GET",
                    "id": "GET",
                    "tabindex": 0,
                    "tagVar": {
                      "name": "GET",
                      "clazz": "org.springframework.web.bind.annotation.RequestMethod"
                    },
                    "title": "GET"
                  },
                  {
                    "bindClass": [],
                    "caption": "HEAD",
                    "id": "HEAD",
                    "tabindex": 1,
                    "tagVar": {
                      "name": "HEAD",
                      "clazz": "org.springframework.web.bind.annotation.RequestMethod"
                    },
                    "title": "HEAD"
                  },
                  {
                    "bindClass": [],
                    "caption": "POST",
                    "id": "POST",
                    "tabindex": 2,
                    "tagVar": {
                      "name": "POST",
                      "clazz": "org.springframework.web.bind.annotation.RequestMethod"
                    },
                    "title": "POST"
                  },
                  {
                    "bindClass": [],
                    "caption": "PUT",
                    "id": "PUT",
                    "tabindex": 3,
                    "tagVar": {
                      "name": "PUT",
                      "clazz": "org.springframework.web.bind.annotation.RequestMethod"
                    },
                    "title": "PUT"
                  },
                  {
                    "bindClass": [],
                    "caption": "PATCH",
                    "id": "PATCH",
                    "tabindex": 4,
                    "tagVar": {
                      "name": "PATCH",
                      "clazz": "org.springframework.web.bind.annotation.RequestMethod"
                    },
                    "title": "PATCH"
                  },
                  {
                    "bindClass": [],
                    "caption": "DELETE",
                    "id": "DELETE",
                    "tabindex": 5,
                    "tagVar": {
                      "name": "DELETE",
                      "clazz": "org.springframework.web.bind.annotation.RequestMethod"
                    },
                    "title": "DELETE"
                  },
                  {
                    "bindClass": [],
                    "caption": "OPTIONS",
                    "id": "OPTIONS",
                    "tabindex": 6,
                    "tagVar": {
                      "name": "OPTIONS",
                      "clazz": "org.springframework.web.bind.annotation.RequestMethod"
                    },
                    "title": "OPTIONS"
                  },
                  {
                    "bindClass": [],
                    "caption": "TRACE",
                    "id": "TRACE",
                    "tabindex": 7,
                    "tagVar": {
                      "name": "TRACE",
                      "clazz": "org.springframework.web.bind.annotation.RequestMethod"
                    },
                    "title": "TRACE"
                  }
                ],
                "name": "method",
                "selMode": "multibycheckbox",
                "tabindex": 7,
                "visibility": "visible",
                "width": "auto"
              },
              "target": "B5"
            }
          ],
          "events": {},
          "key": "ood.UI.FormLayout",
          "path": "dsm.agg.api.config.APIMethodBaseView.APIMethodBaseViewMain.APIMethodBaseView",
          "properties": {
            "caption": "API信息",
            "defaultColWidth": 150,
            "defaultColumnSize": -1,
            "defaultFocus": false,
            "defaultRowHeight": 35,
            "defaultRowSize": -1,
            "disableClickEffect": false,
            "disableHoverEffect": false,
            "disableTips": false,
            "disabled": false,
            "dock": "fill",
            "floatHandler": false,
            "id": "aPIMethodBaseView",
            "imageClass": "ri-code-line",
            "layoutData": {
              "cells": {
                "A1": {
                  "cellName": "A1",
                  "style": {
                    "textAlign": "center"
                  },
                  "value": "访问地址"
                },
                "C3": {
                  "cellName": "C3",
                  "style": {
                    "textAlign": "center"
                  },
                  "value": "异步调用"
                },
                "A2": {
                  "cellName": "A2",
                  "style": {
                    "textAlign": "center"
                  },
                  "value": "自动显示"
                },
                "C4": {
                  "cellName": "C4",
                  "style": {
                    "textAlign": "center"
                  },
                  "value": "级联处理"
                },
                "A3": {
                  "cellName": "A3",
                  "style": {
                    "textAlign": "center"
                  },
                  "value": "JSON格式"
                },
                "A4": {
                  "cellName": "A4",
                  "style": {
                    "textAlign": "center"
                  },
                  "value": "自动运行"
                },
                "A5": {
                  "cellName": "A5",
                  "style": {
                    "textAlign": "center"
                  },
                  "value": "HttpMethod"
                },
                "C2": {
                  "cellName": "C2",
                  "style": {
                    "textAlign": "center"
                  },
                  "value": "显示顺序"
                }
              },
              "colSetting": {
                "A": {
                  "manualWidth": 150
                },
                "B": {
                  "manualWidth": 150
                },
                "C": {
                  "manualWidth": 150
                },
                "D": {
                  "manualWidth": 150
                }
              },
              "cols": 4,
              "merged": [
                {
                  "col": 1,
                  "colspan": 3,
                  "removed": false,
                  "row": 0,
                  "rowspan": 1
                },
                {
                  "col": 1,
                  "colspan": 3,
                  "removed": false,
                  "row": 4,
                  "rowspan": 1
                }
              ],
              "rowSetting": {
                "1": {
                  "manualHeight": 30
                },
                "2": {
                  "manualHeight": 35
                },
                "3": {
                  "manualHeight": 35
                },
                "4": {
                  "manualHeight": 35
                },
                "5": {
                  "manualHeight": 60
                }
              },
              "rows": 5
            },
            "lineSpacing": 10,
            "mode": "write",
            "name": "GetAPIMethodBaseView",
            "solidGridlines": true,
            "stretchH": "all",
            "stretchHeight": "none",
            "tabindex": 0,
            "tagVar": {},
            "visibility": "visible"
          }
        }
      ],
      "events": {},
      "key": "ood.UI.Block",
      "path": "dsm.agg.api.config.APIMethodBaseView.APIMethodBaseViewMain",
      "properties": {
        "dock": "fill",
        "panelBgClr": "transparent",
        "tabindex": 1
      }
    },
    {
      "alias": "RELOAD",
      "events": {},
      "key": "ood.APICaller",
      "path": "dsm.agg.api.config.APIMethodBaseView.RELOAD",
      "properties": {
        "allform": false,
        "autoRun": true,
        "checkRequired": false,
        "checkValid": false,
        "desc": "API信息",
        "id": "/dsm/agg/api/config/APIMethodBaseView",
        "imageClass": "ri-code-line",
        "isAllform": false,
        "methodName": "getAPIMethodBaseView",
        "name": "APIMethodBaseView",
        "proxyType": "auto",
        "queryAsync": true,
        "queryMethod": "POST",
        "queryURL": "/dsm/agg/api/config/APIMethodBaseView",
        "requestDataSource": [
          {
            "name": "APIMethodBaseViewMain",
            "path": "",
            "type": "FORM"
          },
          {
            "name": "PAGECTX",
            "path": "",
            "type": "FORM"
          }
        ],
        "requestType": "FORM",
        "responseCallback": [],
        "responseDataTarget": [
          {
            "name": "APIMethodBaseViewMain",
            "path": "data",
            "type": "FORM"
          },
          {
            "name": "PAGECTX",
            "path": "data",
            "type": "FORM"
          }
        ],
        "responseType": "JSON",
        "sourceClassName": "net.ooder.dsm.aggregation.api.method.APIMethodInfo",
        "tabindex": 2,
        "tips": "API信息"
      }
    }
  ],
  "className": "dsm.agg.api.config.APIMethodBaseView",
  "customFunctions": {},
  "desc": "API信息",
  "dio": false,
  "events": {},
  "formulas": [],
  "key": "ood.Module",
  "moduleType": "EUClass",
  "moduleVar": {},
  "moduleViewType": "FORMCONFIG",
  "path": "dsm.agg.api.config.APIMethodBaseView",
  "properties": {
    "autoDestroy": true,
    "bindClass": [],
    "caption": "API信息",
    "currComponentAlias": "APIMethodBaseView",
    "dock": "fill",
    "title": "API信息"
  }
}