{
  "alias": "ConfigBaseInfo",
  "children": [
    {
      "alias": "PAGECTX",
      "children": [
        {
          "alias": "currentClassName",
          "events": {},
          "key": "ood.UI.HiddenInput",
          "path": "dsm.agg.menu.config.ConfigBaseInfo.PAGECTX.currentClassName",
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
          "path": "dsm.agg.menu.config.ConfigBaseInfo.PAGECTX.xpath",
          "properties": {
            "formField": true,
            "isPid": true,
            "name": "xpath",
            "pid": true,
            "tabindex": 1
          }
        },
        {
          "alias": "sourceClassName",
          "events": {},
          "key": "ood.UI.HiddenInput",
          "path": "dsm.agg.menu.config.ConfigBaseInfo.PAGECTX.sourceClassName",
          "properties": {
            "formField": true,
            "isPid": false,
            "name": "sourceClassName",
            "pid": false,
            "tabindex": 2
          }
        },
        {
          "alias": "projectName",
          "events": {},
          "key": "ood.UI.HiddenInput",
          "path": "dsm.agg.menu.config.ConfigBaseInfo.PAGECTX.projectName",
          "properties": {
            "formField": true,
            "isPid": false,
            "name": "projectName",
            "pid": false,
            "tabindex": 3,
            "value": "DSMdsmVVVERSION0"
          }
        },
        {
          "alias": "domainId",
          "events": {},
          "key": "ood.UI.HiddenInput",
          "path": "dsm.agg.menu.config.ConfigBaseInfo.PAGECTX.domainId",
          "properties": {
            "formField": true,
            "isPid": true,
            "name": "domainId",
            "pid": true,
            "tabindex": 4
          }
        },
        {
          "alias": "projectVersionName",
          "events": {},
          "key": "ood.UI.HiddenInput",
          "path": "dsm.agg.menu.config.ConfigBaseInfo.PAGECTX.projectVersionName",
          "properties": {
            "formField": true,
            "isPid": false,
            "name": "projectVersionName",
            "pid": false,
            "tabindex": 5,
            "value": "DSMdsmVVVERSION0"
          }
        }
      ],
      "events": {},
      "key": "ood.UI.Block",
      "params": {},
      "path": "dsm.agg.menu.config.ConfigBaseInfo.PAGECTX",
      "properties": {
        "borderType": "none",
        "tabindex": 0,
        "visibility": "hidden"
      }
    },
    {
      "alias": "ConfigBaseInfoMain",
      "children": [
        {
          "alias": "ConfigBaseInfo",
          "children": [
            {
              "alias": "url",
              "events": {},
              "key": "ood.UI.Input",
              "path": "dsm.agg.menu.config.ConfigBaseInfo.ConfigBaseInfoMain.ConfigBaseInfo.url",
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
              "alias": "name",
              "events": {},
              "key": "ood.UI.Input",
              "path": "dsm.agg.menu.config.ConfigBaseInfo.ConfigBaseInfoMain.ConfigBaseInfo.name",
              "properties": {
                "caption": "名称",
                "defaultFocus": false,
                "desc": "名称",
                "disableClickEffect": false,
                "disableHoverEffect": false,
                "disableTips": false,
                "disabled": false,
                "dock": "none",
                "events": {},
                "id": "name",
                "imageClass": "",
                "items": [],
                "labelCaption": "名称",
                "labelPos": "left",
                "labelSize": "6.0em",
                "name": "name",
                "readonly": false,
                "tabindex": 1,
                "tagVar": {},
                "visibility": "visible"
              },
              "target": "B2"
            },
            {
              "alias": "desc",
              "events": {},
              "key": "ood.UI.RichEditor",
              "path": "dsm.agg.menu.config.ConfigBaseInfo.ConfigBaseInfoMain.ConfigBaseInfo.desc",
              "properties": {
                "cmdList": "none",
                "defaultFocus": false,
                "desc": "描述",
                "disableClickEffect": false,
                "disableHoverEffect": false,
                "disableTips": false,
                "dock": "none",
                "events": {},
                "id": "desc",
                "name": "desc",
                "tabindex": 2,
                "textType": "text",
                "visibility": "visible"
              },
              "target": "B3"
            },
            {
              "alias": "sourceClassName",
              "events": {},
              "key": "ood.UI.Input",
              "path": "dsm.agg.menu.config.ConfigBaseInfo.ConfigBaseInfoMain.ConfigBaseInfo.sourceClassName",
              "properties": {
                "caption": "类名",
                "defaultFocus": false,
                "desc": "类名",
                "disableClickEffect": false,
                "disableHoverEffect": false,
                "disableTips": false,
                "disabled": false,
                "dock": "none",
                "events": {},
                "id": "sourceClassName",
                "imageClass": "",
                "items": [],
                "labelCaption": "类名",
                "labelPos": "left",
                "labelSize": "6.0em",
                "name": "sourceClassName",
                "readonly": false,
                "tabindex": 3,
                "tagVar": {},
                "visibility": "visible"
              },
              "target": "B4"
            }
          ],
          "events": {},
          "key": "ood.UI.FormLayout",
          "path": "dsm.agg.menu.config.ConfigBaseInfo.ConfigBaseInfoMain.ConfigBaseInfo",
          "properties": {
            "caption": "WEB配置",
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
            "id": "configBaseInfo",
            "imageClass": "ri-file-text-line",
            "layoutData": {
              "cells": {
                "A1": {
                  "cellName": "A1",
                  "style": {
                    "textAlign": "center"
                  },
                  "value": "访问地址"
                },
                "A2": {
                  "cellName": "A2",
                  "style": {
                    "textAlign": "center"
                  },
                  "value": "名称"
                },
                "A3": {
                  "cellName": "A3",
                  "style": {
                    "textAlign": "center"
                  },
                  "value": "描述"
                },
                "A4": {
                  "cellName": "A4",
                  "style": {
                    "textAlign": "center"
                  },
                  "value": "类名"
                }
              },
              "colSetting": {
                "A": {
                  "manualWidth": 150
                },
                "B": {
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
                  "row": 1,
                  "rowspan": 1
                },
                {
                  "col": 1,
                  "colspan": 3,
                  "removed": false,
                  "row": 2,
                  "rowspan": 1
                },
                {
                  "col": 1,
                  "colspan": 3,
                  "removed": false,
                  "row": 3,
                  "rowspan": 1
                }
              ],
              "rowSetting": {
                "1": {
                  "manualHeight": 35
                },
                "2": {
                  "manualHeight": 35
                },
                "3": {
                  "manualHeight": 50
                },
                "4": {
                  "manualHeight": 35
                }
              },
              "rows": 4
            },
            "lineSpacing": 10,
            "mode": "write",
            "name": "GetConfigBaseInfo",
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
      "path": "dsm.agg.menu.config.ConfigBaseInfo.ConfigBaseInfoMain",
      "properties": {
        "dock": "fill",
        "panelBgClr": "transparent",
        "tabindex": 1
      }
    },
    {
      "alias": "SAVE",
      "events": {
        "afterInvoke": {
          "actions": [
            {
              "args": [],
              "conditions": [],
              "desc": "刷新父级页面",
              "enumClassName": "net.ooder.esd.annotation.action.CustomPageAction",
              "enumValue": "RELOADPARENT",
              "eventClass": "net.ooder.esd.annotation.event.CustomCallBack",
              "eventValue": "RELOADPARENT",
              "id": "net.ooder.esd.annotation.action.CustomPageAction_RELOADPARENT_null_null",
              "method": "reloadParent",
              "redirection": "page::",
              "return": true,
              "target": "dsm.agg.menu.config.ConfigBaseInfo",
              "type": "page"
            },
            {
              "args": [],
              "conditions": [],
              "desc": "关闭页面",
              "enumClassName": "net.ooder.esd.annotation.action.CustomPageAction",
              "enumValue": "CLOSE",
              "eventClass": "net.ooder.esd.annotation.event.CustomCallBack",
              "eventValue": "CLOSE",
              "id": "net.ooder.esd.annotation.action.CustomPageAction_CLOSE_null_null",
              "method": "destroy",
              "redirection": "page::",
              "return": true,
              "target": "dsm.agg.menu.config.ConfigBaseInfo",
              "type": "page"
            }
          ]
        }
      },
      "key": "ood.APICaller",
      "path": "dsm.agg.menu.config.ConfigBaseInfo.SAVE",
      "properties": {
        "allform": true,
        "autoRun": false,
        "checkRequired": false,
        "checkValid": false,
        "desc": "保存",
        "id": "/dsm/agg/menu/config/saveAggMenu",
        "imageClass": "ri-code-box-line",
        "isAllform": true,
        "methodName": "saveAggMenu",
        "name": "saveAggMenu",
        "proxyType": "auto",
        "queryAsync": true,
        "queryMethod": "POST",
        "queryURL": "/dsm/agg/menu/config/saveAggMenu",
        "requestDataSource": [
          {
            "name": "ConfigBaseInfoMain",
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
        "responseDataTarget": [],
        "responseType": "JSON",
        "sourceClassName": "net.ooder.dsm.aggregation.config.menu.AggMenuService",
        "tabindex": 2,
        "tips": "保存"
      }
    },
    {
      "alias": "RELOAD",
      "events": {},
      "key": "ood.APICaller",
      "path": "dsm.agg.menu.config.ConfigBaseInfo.RELOAD",
      "properties": {
        "allform": false,
        "autoRun": true,
        "checkRequired": false,
        "checkValid": false,
        "desc": "WEB配置",
        "id": "/dsm/agg/menu/config/ConfigBaseInfo",
        "imageClass": "ri-code-box-line",
        "isAllform": false,
        "methodName": "getConfigBaseInfo",
        "name": "ConfigBaseInfo",
        "proxyType": "auto",
        "queryAsync": true,
        "queryMethod": "POST",
        "queryURL": "/dsm/agg/menu/config/ConfigBaseInfo",
        "requestDataSource": [
          {
            "name": "ConfigBaseInfoMain",
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
            "name": "ConfigBaseInfoMain",
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
        "sourceClassName": "net.ooder.dsm.aggregation.config.menu.AggMenuNav",
        "tabindex": 3,
        "tips": "WEB配置"
      }
    }
  ],
  "className": "dsm.agg.menu.config.ConfigBaseInfo",
  "customFunctions": {},
  "desc": "WEB配置",
  "dio": false,
  "events": {},
  "formulas": [],
  "key": "ood.Module",
  "moduleType": "EUClass",
  "moduleVar": {},
  "moduleViewType": "FORMCONFIG",
  "path": "dsm.agg.menu.config.ConfigBaseInfo",
  "properties": {
    "autoDestroy": true,
    "bindClass": [],
    "caption": "WEB配置",
    "currComponentAlias": "ConfigBaseInfo",
    "dock": "fill",
    "title": "WEB配置"
  },
  "saveUrl": "/dsm/agg/menu/config/saveAggMenu"
}