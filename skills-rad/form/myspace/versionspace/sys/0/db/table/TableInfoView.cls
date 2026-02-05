{
  "Static": {
    "designViewConf": {
      "touchDevice": false
    },
    "viewStyles": {}
  },
  "alias": "TableInfoView",
  "children": [
    {
      "alias": "TableInfoView",
      "events": {},
      "key": "ood.UI.Block",
      "path": "db.table.TableInfoView.TableInfoView",
      "properties": {
        "dock": "fill",
        "panelBgClr": "transparent"
      }
    },
    {
      "alias": "PAGECTX",
      "children": [
        {
          "alias": "configKey",
          "events": {},
          "key": "ood.UI.HiddenInput",
          "path": "db.table.TableInfoView.PAGECTX.configKey",
          "properties": {
            "formField": true,
            "isPid": false,
            "name": "configKey",
            "pid": false
          }
        },
        {
          "alias": "tablename",
          "events": {},
          "key": "ood.UI.HiddenInput",
          "path": "db.table.TableInfoView.PAGECTX.tablename",
          "properties": {
            "formField": true,
            "isPid": true,
            "name": "tablename",
            "pid": true
          }
        },
        {
          "alias": "projectName",
          "events": {},
          "key": "ood.UI.HiddenInput",
          "path": "db.table.TableInfoView.PAGECTX.projectName",
          "properties": {
            "formField": true,
            "isPid": false,
            "name": "projectName",
            "pid": false,
            "value": "sysVVVERSION0"
          }
        },
        {
          "alias": "projectVersionName",
          "events": {},
          "key": "ood.UI.HiddenInput",
          "path": "db.table.TableInfoView.PAGECTX.projectVersionName",
          "properties": {
            "formField": true,
            "isPid": false,
            "name": "projectVersionName",
            "pid": false,
            "value": "sysVVVERSION0"
          }
        }
      ],
      "events": {},
      "key": "ood.UI.Block",
      "params": {},
      "path": "db.table.TableInfoView.PAGECTX",
      "properties": {
        "borderType": "none",
        "visibility": "hidden"
      }
    },
    {
      "alias": "TableInfoViewMain",
      "children": [
        {
          "alias": "TableInfoView",
          "children": [
            {
              "alias": "name",
              "events": {},
              "key": "ood.UI.Input",
              "path": "db.table.TableInfoView.TableInfoViewMain.TableInfoView.name",
              "properties": {
                "caption": "表名",
                "desc": "表名",
                "disabled": false,
                "events": {},
                "id": "name",
                "imageClass": "",
                "items": [],
                "labelCaption": "表名",
                "labelPos": "left",
                "labelSize": "6.0em",
                "name": "name",
                "readonly": false,
                "required": true,
                "tagVar": {}
              },
              "target": "B1"
            },
            {
              "alias": "cnname",
              "events": {},
              "key": "ood.UI.Input",
              "path": "db.table.TableInfoView.TableInfoViewMain.TableInfoView.cnname",
              "properties": {
                "caption": "注解",
                "desc": "注解",
                "disabled": false,
                "events": {},
                "id": "cnname",
                "imageClass": "",
                "items": [],
                "labelCaption": "注解",
                "labelPos": "left",
                "labelSize": "6.0em",
                "name": "cnname",
                "readonly": false,
                "required": true,
                "tagVar": {}
              },
              "target": "D1"
            },
            {
              "alias": "pkName",
              "events": {},
              "key": "ood.UI.Input",
              "path": "db.table.TableInfoView.TableInfoViewMain.TableInfoView.pkName",
              "properties": {
                "caption": "主键",
                "desc": "主键",
                "disabled": false,
                "events": {},
                "id": "pkName",
                "imageClass": "",
                "items": [],
                "labelCaption": "主键",
                "labelPos": "left",
                "labelSize": "6.0em",
                "name": "pkName",
                "readonly": true,
                "tagVar": {}
              },
              "target": "B2"
            },
            {
              "alias": "configKey",
              "events": {},
              "key": "ood.UI.Input",
              "path": "db.table.TableInfoView.TableInfoViewMain.TableInfoView.configKey",
              "properties": {
                "caption": "数据库标识",
                "desc": "数据库标识",
                "disabled": false,
                "events": {},
                "id": "configKey",
                "imageClass": "",
                "items": [],
                "labelCaption": "数据库标识",
                "labelPos": "left",
                "labelSize": "6.0em",
                "name": "configKey",
                "readonly": true,
                "tagVar": {}
              },
              "target": "D2"
            },
            {
              "alias": "url",
              "events": {},
              "key": "ood.UI.Input",
              "path": "db.table.TableInfoView.TableInfoViewMain.TableInfoView.url",
              "properties": {
                "caption": "连接串",
                "desc": "连接串",
                "disabled": false,
                "events": {},
                "id": "url",
                "imageClass": "",
                "items": [],
                "labelCaption": "连接串",
                "labelPos": "left",
                "labelSize": "6.0em",
                "name": "url",
                "readonly": true,
                "tagVar": {}
              },
              "target": "B3"
            }
          ],
          "events": {},
          "key": "ood.UI.FormLayout",
          "path": "db.table.TableInfoView.TableInfoViewMain.TableInfoView",
          "properties": {
            "caption": "库表信息",
            "defaultColWidth": 150,
            "defaultColumnSize": -1,
            "defaultRowHeight": 35,
            "defaultRowSize": -1,
            "dock": "fill",
            "floatHandler": false,
            "id": "tableInfoView",
            "imageClass": "ri-file-text-line",
            "layoutData": {
              "cells": {
                "A1": {
                  "cellName": "A1",
                  "style": {
                    "textAlign": "center"
                  },
                  "value": "表名"
                },
                "A2": {
                  "cellName": "A2",
                  "style": {
                    "textAlign": "center"
                  },
                  "value": "主键"
                },
                "A3": {
                  "cellName": "A3",
                  "style": {
                    "textAlign": "center"
                  },
                  "value": "连接串"
                },
                "C1": {
                  "cellName": "C1",
                  "style": {
                    "textAlign": "center"
                  },
                  "value": "注解"
                },
                "C2": {
                  "cellName": "C2",
                  "style": {
                    "textAlign": "center"
                  },
                  "value": "数据库标识"
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
                  "manualHeight": 35
                }
              },
              "rows": 3
            },
            "lineSpacing": 10,
            "mode": "write",
            "name": "GetTableInfoView",
            "solidGridlines": true,
            "stretchH": "all",
            "stretchHeight": "last",
            "tagVar": {}
          }
        }
      ],
      "events": {},
      "key": "ood.UI.Block",
      "path": "db.table.TableInfoView.TableInfoViewMain",
      "properties": {
        "borderType": "none",
        "dock": "fill"
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
              "target": "db.table.TableInfoView",
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
              "target": "db.table.TableInfoView",
              "type": "page"
            }
          ]
        }
      },
      "key": "ood.APICaller",
      "path": "db.table.TableInfoView.SAVE",
      "properties": {
        "allform": false,
        "autoRun": false,
        "checkRequired": false,
        "checkValid": false,
        "desc": "保存",
        "id": "/db/table/updateTable",
        "imageClass": "ri-code-box-line",
        "isAllform": false,
        "methodName": "updateTable",
        "name": "updateTable",
        "proxyType": "auto",
        "queryAsync": true,
        "queryMethod": "POST",
        "queryURL": "/db/table/updateTable",
        "requestDataSource": [
          {
            "name": "tableInfo",
            "path": "",
            "type": "FORM"
          },
          {
            "name": "tableInfo",
            "path": "",
            "type": "FORM"
          },
          {
            "name": "TableInfoView",
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
        "sourceClassName": "net.ooder.admin.db.table.TableService",
        "tips": "保存"
      }
    },
    {
      "alias": "RELOAD",
      "events": {},
      "key": "ood.APICaller",
      "path": "db.table.TableInfoView.RELOAD",
      "properties": {
        "allform": false,
        "autoRun": true,
        "checkRequired": false,
        "checkValid": false,
        "desc": "库表信息",
        "id": "/db/table/TableInfoView",
        "imageClass": "ri-code-box-line",
        "isAllform": false,
        "methodName": "getTableInfoView",
        "name": "TableInfoView",
        "proxyType": "auto",
        "queryAsync": true,
        "queryMethod": "POST",
        "queryURL": "/db/table/TableInfoView",
        "requestDataSource": [
          {
            "name": "TableInfoView",
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
            "name": "TableInfoView",
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
        "sourceClassName": "net.ooder.admin.db.table.TableNav",
        "tips": "库表信息"
      }
    }
  ],
  "className": "db.table.TableInfoView",
  "customFunctions": {},
  "desc": "库表信息",
  "dio": false,
  "events": {},
  "formulas": [],
  "key": "ood.Module",
  "moduleType": "EUClass",
  "moduleVar": {},
  "moduleViewType": "FORMCONFIG",
  "path": "db.table.TableInfoView",
  "properties": {
    "autoDestroy": true,
    "bindClass": [],
    "caption": "库表信息",
    "currComponentAlias": "TableInfoView",
    "dock": "fill",
    "title": "库表信息"
  },
  "saveUrl": "/db/table/saveTable"
}