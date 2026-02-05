{
  "alias": "WebSiteInfo",
  "children": [
    {
      "alias": "PAGECTX",
      "children": [
        {
          "alias": "projectName",
          "events": {},
          "key": "ood.UI.HiddenInput",
          "path": "dsm.temp.website.WebSiteInfo.PAGECTX.projectName",
          "properties": {
            "formField": true,
            "isPid": false,
            "name": "projectName",
            "pid": false,
            "tabindex": 0,
            "value": "DSMdsmVVVERSION0"
          }
        },
        {
          "alias": "dsmTempId",
          "events": {},
          "key": "ood.UI.HiddenInput",
          "path": "dsm.temp.website.WebSiteInfo.PAGECTX.dsmTempId",
          "properties": {
            "formField": true,
            "isPid": true,
            "name": "dsmTempId",
            "pid": true,
            "tabindex": 1
          }
        },
        {
          "alias": "projectVersionName",
          "events": {},
          "key": "ood.UI.HiddenInput",
          "path": "dsm.temp.website.WebSiteInfo.PAGECTX.projectVersionName",
          "properties": {
            "formField": true,
            "isPid": false,
            "name": "projectVersionName",
            "pid": false,
            "tabindex": 2,
            "value": "DSMdsmVVVERSION0"
          }
        },
        {
          "alias": "thumbnailType",
          "events": {},
          "key": "ood.UI.HiddenInput",
          "path": "dsm.temp.website.WebSiteInfo.PAGECTX.thumbnailType",
          "properties": {
            "formField": true,
            "isPid": true,
            "name": "thumbnailType",
            "pid": true,
            "tabindex": 3
          }
        }
      ],
      "events": {},
      "key": "ood.UI.Block",
      "params": {},
      "path": "dsm.temp.website.WebSiteInfo.PAGECTX",
      "properties": {
        "borderType": "none",
        "tabindex": 0,
        "visibility": "hidden"
      }
    },
    {
      "alias": "WebSiteInfoMain",
      "children": [
        {
          "alias": "WebSiteInfo",
          "children": [
            {
              "alias": "name",
              "events": {},
              "key": "ood.UI.Input",
              "path": "dsm.temp.website.WebSiteInfo.WebSiteInfoMain.WebSiteInfo.name",
              "properties": {
                "caption": "模板名称",
                "defaultFocus": false,
                "desc": "模板名称",
                "disableClickEffect": false,
                "disableHoverEffect": false,
                "disableTips": false,
                "disabled": false,
                "dock": "none",
                "events": {},
                "id": "name",
                "imageClass": "",
                "items": [],
                "labelCaption": "模板名称",
                "labelPos": "left",
                "labelSize": "6.0em",
                "name": "name",
                "readonly": false,
                "required": true,
                "tabindex": 0,
                "tagVar": {},
                "visibility": "visible"
              },
              "target": "B1"
            },
            {
              "alias": "space",
              "events": {},
              "key": "ood.UI.Input",
              "path": "dsm.temp.website.WebSiteInfo.WebSiteInfoMain.WebSiteInfo.space",
              "properties": {
                "caption": "命名空间",
                "defaultFocus": false,
                "desc": "命名空间",
                "disableClickEffect": false,
                "disableHoverEffect": false,
                "disableTips": false,
                "disabled": false,
                "dock": "none",
                "events": {},
                "id": "space",
                "imageClass": "",
                "items": [],
                "labelCaption": "命名空间",
                "labelPos": "left",
                "labelSize": "6.0em",
                "name": "space",
                "readonly": false,
                "required": true,
                "tabindex": 1,
                "tagVar": {},
                "visibility": "visible"
              },
              "target": "D1"
            },
            {
              "alias": "type",
              "events": {},
              "key": "ood.UI.ComboInput",
              "path": "dsm.temp.website.WebSiteInfo.WebSiteInfoMain.WebSiteInfo.type",
              "properties": {
                "defaultFocus": false,
                "desc": "业务类型",
                "disableClickEffect": false,
                "disableHoverEffect": false,
                "disableTips": false,
                "disabled": false,
                "dock": "none",
                "dynLoad": true,
                "enumClass": "net.ooder.esd.dsm.enums.DSMTempType",
                "items": [
                  {
                    "bindClass": [],
                    "caption": "通用数据库模板",
                    "id": "dao",
                    "imageClass": "ri-database-line",
                    "name": "通用数据库模板",
                    "pattern": "",
                    "tabindex": 0,
                    "tagVar": {
                      "name": "dao",
                      "clazz": "net.ooder.esd.dsm.enums.DSMTempType"
                    },
                    "title": "通用数据库模板"
                  },
                  {
                    "bindClass": [],
                    "caption": "通用视图模板",
                    "id": "custom",
                    "imageClass": "ri-stack-line",
                    "name": "通用视图模板",
                    "pattern": "",
                    "tabindex": 1,
                    "tagVar": {
                      "name": "custom",
                      "clazz": "net.ooder.esd.dsm.enums.DSMTempType"
                    },
                    "title": "通用视图模板"
                  },
                  {
                    "bindClass": [],
                    "caption": "业务流程模板",
                    "id": "process",
                    "imageClass": "ri-node-tree",
                    "name": "业务流程模板",
                    "pattern": "",
                    "tabindex": 2,
                    "tagVar": {
                      "name": "process",
                      "clazz": "net.ooder.esd.dsm.enums.DSMTempType"
                    },
                    "title": "业务流程模板"
                  },
                  {
                    "bindClass": [],
                    "caption": "统计分析模板",
                    "id": "statistics",
                    "imageClass": "ri-line-chart-line",
                    "name": "统计分析模板",
                    "pattern": "",
                    "tabindex": 3,
                    "tagVar": {
                      "name": "statistics",
                      "clazz": "net.ooder.esd.dsm.enums.DSMTempType"
                    },
                    "title": "统计分析模板"
                  },
                  {
                    "bindClass": [],
                    "caption": "门户网站模板",
                    "id": "cms",
                    "imageClass": "ri-earth-line",
                    "name": "门户网站模板",
                    "pattern": "",
                    "tabindex": 4,
                    "tagVar": {
                      "name": "cms",
                      "clazz": "net.ooder.esd.dsm.enums.DSMTempType"
                    },
                    "title": "门户网站模板"
                  },
                  {
                    "bindClass": [],
                    "caption": "移动应用模板",
                    "id": "mobile",
                    "imageClass": "ri-smartphone-line",
                    "name": "移动应用模板",
                    "pattern": "",
                    "tabindex": 5,
                    "tagVar": {
                      "name": "mobile",
                      "clazz": "net.ooder.esd.dsm.enums.DSMTempType"
                    },
                    "title": "移动应用模板"
                  }
                ],
                "labelCaption": "业务类型",
                "name": "type",
                "readonly": false,
                "tabindex": 2,
                "type": "listbox",
                "visibility": "visible"
              },
              "target": "B2"
            },
            {
              "alias": "desc",
              "events": {},
              "key": "ood.UI.RichEditor",
              "path": "dsm.temp.website.WebSiteInfo.WebSiteInfoMain.WebSiteInfo.desc",
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
                "tabindex": 3,
                "textType": "text",
                "visibility": "visible"
              },
              "target": "B3"
            },
            {
              "alias": "image",
              "events": {},
              "key": "ood.UI.Image",
              "path": "dsm.temp.website.WebSiteInfo.WebSiteInfoMain.WebSiteInfo.image",
              "properties": {
                "defaultFocus": false,
                "disableClickEffect": false,
                "disableHoverEffect": false,
                "disableTips": false,
                "disabled": false,
                "dock": "none",
                "events": {},
                "id": "image",
                "name": "image",
                "tabindex": 4,
                "visibility": "visible"
              },
              "target": "B4"
            },
            {
              "alias": "thumbnailFile",
              "events": {
                "uploadcomplete": {
                  "actions": [
                    {
                      "args": [],
                      "conditions": [],
                      "desc": "刷新页面",
                      "enumClassName": "net.ooder.esd.annotation.action.CustomPageAction",
                      "enumValue": "RELOAD",
                      "eventClass": "net.ooder.esd.annotation.event.FileUploadEventEnum",
                      "eventValue": "uploadcomplete",
                      "id": "net.ooder.esd.annotation.action.CustomPageAction_RELOAD_null_null",
                      "method": "initData",
                      "redirection": "page::",
                      "return": true,
                      "target": "dsm.temp.website.WebSiteInfo",
                      "type": "page"
                    }
                  ]
                }
              },
              "key": "ood.UI.FileUpload",
              "path": "dsm.temp.website.WebSiteInfo.WebSiteInfoMain.WebSiteInfo.thumbnailFile",
              "properties": {
                "defaultFocus": false,
                "desc": "上传略缩图",
                "disableClickEffect": false,
                "disableHoverEffect": false,
                "disableTips": false,
                "disabled": false,
                "dock": "none",
                "events": {},
                "height": "30.0em",
                "id": "thumbnailFile",
                "name": "thumbnailFile",
                "src": "/plugins/fileupload/uploadgrid.html",
                "tabindex": 5,
                "uploadUrl": "/custom/dsm/AttachUPLoad?thumbnailType=dsmTemp",
                "visibility": "visible",
                "width": "30.0em"
              },
              "target": "D4"
            }
          ],
          "events": {},
          "key": "ood.UI.FormLayout",
          "path": "dsm.temp.website.WebSiteInfo.WebSiteInfoMain.WebSiteInfo",
          "properties": {
            "caption": "站点模版信息",
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
            "id": "webSiteInfo",
            "imageClass": "ri-file-text-line",
            "layoutData": {
              "cells": {
                "A1": {
                  "cellName": "A1",
                  "style": {
                    "textAlign": "center"
                  },
                  "value": "模板名称"
                },
                "A2": {
                  "cellName": "A2",
                  "style": {
                    "textAlign": "center"
                  },
                  "value": "业务类型"
                },
                "C4": {
                  "cellName": "C4",
                  "style": {
                    "textAlign": "center"
                  },
                  "value": "上传略缩图"
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
                  "value": "图片"
                },
                "C1": {
                  "cellName": "C1",
                  "style": {
                    "textAlign": "center"
                  },
                  "value": "命名空间"
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
                  "row": 1,
                  "rowspan": 1
                },
                {
                  "col": 1,
                  "colspan": 3,
                  "removed": false,
                  "row": 2,
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
            "name": "GetWebSiteInfo",
            "solidGridlines": true,
            "stretchH": "all",
            "stretchHeight": "last",
            "tabindex": 0,
            "tagVar": {},
            "visibility": "visible"
          }
        }
      ],
      "events": {},
      "key": "ood.UI.Block",
      "path": "dsm.temp.website.WebSiteInfo.WebSiteInfoMain",
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
              "target": "dsm.temp.website.WebSiteInfo",
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
              "target": "dsm.temp.website.WebSiteInfo",
              "type": "page"
            }
          ]
        }
      },
      "key": "ood.APICaller",
      "path": "dsm.temp.website.WebSiteInfo.SAVE",
      "properties": {
        "allform": false,
        "autoRun": false,
        "checkRequired": false,
        "checkValid": false,
        "desc": "保存",
        "id": "/dsm/temp/website/updateWebSite",
        "imageClass": "ri-code-box-line",
        "isAllform": false,
        "methodName": "updateWebSite",
        "name": "updateWebSite",
        "proxyType": "auto",
        "queryAsync": true,
        "queryMethod": "POST",
        "queryURL": "/dsm/temp/website/updateWebSite",
        "requestDataSource": [
          {
            "name": "tempInfo",
            "path": "",
            "type": "FORM"
          },
          {
            "name": "tempInfo",
            "path": "",
            "type": "FORM"
          },
          {
            "name": "WebSiteInfoMain",
            "path": "",
            "type": "FORM"
          },
          {
            "name": "PAGECTX",
            "path": "",
            "type": "FORM"
          }
        ],
        "requestType": "JSON",
        "responseCallback": [],
        "responseDataTarget": [],
        "responseType": "JSON",
        "sourceClassName": "net.ooder.dsm.website.manager.WebSiteService",
        "tabindex": 2,
        "tips": "保存"
      }
    },
    {
      "alias": "RELOAD",
      "events": {},
      "key": "ood.APICaller",
      "path": "dsm.temp.website.WebSiteInfo.RELOAD",
      "properties": {
        "allform": false,
        "autoRun": true,
        "checkRequired": false,
        "checkValid": false,
        "desc": "站点模版信息",
        "id": "/dsm/temp/website/WebSiteInfo",
        "imageClass": "ri-code-box-line",
        "isAllform": false,
        "methodName": "getWebSiteInfo",
        "name": "WebSiteInfo",
        "proxyType": "auto",
        "queryAsync": true,
        "queryMethod": "POST",
        "queryURL": "/dsm/temp/website/WebSiteInfo",
        "requestDataSource": [
          {
            "name": "WebSiteInfoMain",
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
            "name": "WebSiteInfoMain",
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
        "sourceClassName": "net.ooder.dsm.website.manager.WebSiteNav",
        "tabindex": 3,
        "tips": "站点模版信息"
      }
    }
  ],
  "className": "dsm.temp.website.WebSiteInfo",
  "customFunctions": {},
  "desc": "站点模版信息",
  "dio": false,
  "events": {},
  "formulas": [],
  "key": "ood.Module",
  "moduleType": "EUClass",
  "moduleVar": {},
  "moduleViewType": "FORMCONFIG",
  "path": "dsm.temp.website.WebSiteInfo",
  "properties": {
    "autoDestroy": true,
    "bindClass": [],
    "caption": "站点模版信息",
    "currComponentAlias": "WebSiteInfo",
    "dock": "fill",
    "title": "站点模版信息"
  },
  "saveUrl": "/dsm/temp/website/updateWebSite"
}