{
  "Static": {
    "designViewConf": {
      "touchDevice": false
    },
    "viewStyles": {}
  },
  "alias": "SearchTestForm",
  "children": [
    {
      "alias": "PAGECTX",
      "children": [
        {
          "alias": "testView",
          "events": {},
          "key": "ood.UI.HiddenInput",
          "path": "demo.SearchTestForm.PAGECTX.testView",
          "properties": {
            "formField": true,
            "isPid": false,
            "name": "testView",
            "pid": false
          }
        },
        {
          "alias": "projectName",
          "events": {},
          "key": "ood.UI.HiddenInput",
          "path": "demo.SearchTestForm.PAGECTX.projectName",
          "properties": {
            "formField": true,
            "isPid": false,
            "name": "projectName",
            "pid": false,
            "value": "testVVVERSION0"
          }
        },
        {
          "alias": "projectVersionName",
          "events": {},
          "key": "ood.UI.HiddenInput",
          "path": "demo.SearchTestForm.PAGECTX.projectVersionName",
          "properties": {
            "formField": true,
            "isPid": false,
            "name": "projectVersionName",
            "pid": false,
            "value": "testVVVERSION0"
          }
        },
        {
          "alias": "personId",
          "events": {},
          "key": "ood.UI.HiddenInput",
          "path": "demo.SearchTestForm.PAGECTX.personId",
          "properties": {
            "formField": true,
            "isPid": true,
            "name": "personId",
            "pid": true
          }
        }
      ],
      "events": {},
      "key": "ood.UI.Block",
      "params": {},
      "path": "demo.SearchTestForm.PAGECTX",
      "properties": {
        "borderType": "none",
        "visibility": "hidden"
      }
    },
    {
      "alias": "SearchTestFormMain",
      "children": [
        {
          "alias": "SAVEBottomBlock",
          "children": [
            {
              "alias": "SAVEBottom",
              "apis": [],
              "events": {
                "onClick": {
                  "actions": [
                    {
                      "args": [
                        "{page.SEARCH.invoke()}"
                      ],
                      "conditions": [
                        {
                          "symbol": "=",
                          "left": "{args[1].id}",
                          "right": "SEARCH_button"
                        }
                      ],
                      "desc": "查找",
                      "enumClassName": "net.ooder.esd.annotation.action.CustomFormAction",
                      "enumValue": "SEARCH",
                      "eventClass": "net.ooder.esd.annotation.event.FieldEventEnum",
                      "eventValue": "onClick",
                      "id": "net.ooder.esd.annotation.action.CustomFormAction_SEARCH_null_null",
                      "method": "invoke",
                      "redirection": "other:callback:call",
                      "return": false,
                      "target": "SEARCH",
                      "type": "control"
                    },
                    {
                      "args": [
                        "{page.SAVE.invoke()}"
                      ],
                      "conditions": [
                        {
                          "symbol": "=",
                          "left": "{args[1].id}",
                          "right": "SAVE_button"
                        }
                      ],
                      "desc": "保存",
                      "enumClassName": "net.ooder.esd.annotation.action.CustomFormAction",
                      "enumValue": "SAVE",
                      "eventClass": "net.ooder.esd.annotation.event.FieldEventEnum",
                      "eventValue": "onClick",
                      "id": "net.ooder.esd.annotation.action.CustomFormAction_SAVE_null_null",
                      "method": "invoke",
                      "redirection": "other:callback:call",
                      "return": true,
                      "target": "SAVE",
                      "type": "control"
                    }
                  ]
                }
              },
              "key": "ood.UI.StatusButtons",
              "menuType": "BOTTOMBAR",
              "path": "demo.SearchTestForm.SearchTestFormMain.SAVEBottomBlock.SAVEBottom",
              "properties": {
                "borderType": "none",
                "dock": "fill",
                "itemType": "button",
                "itemWidth": "auto",
                "items": [
                  {
                    "caption": "查找",
                    "iconColor": "#87CEEB",
                    "id": "SEARCH_button",
                    "imageClass": "ri-search-line",
                    "index": 0,
                    "itemType": "button",
                    "tagVar": {
                      "name": "SEARCH",
                      "clazz": "net.ooder.esd.annotation.menu.CustomFormMenu"
                    },
                    "tips": "查找",
                    "title": "查找"
                  },
                  {
                    "caption": "保存",
                    "iconColor": "#195ead",
                    "id": "SAVE_button",
                    "imageClass": "ri-save-line",
                    "index": 0,
                    "itemType": "button",
                    "tagVar": {
                      "name": "SAVE",
                      "clazz": "net.ooder.esd.annotation.menu.CustomFormMenu"
                    },
                    "tips": "保存",
                    "title": "保存"
                  }
                ],
                "menuType": "BOTTOMBAR",
                "position": "static",
                "showCaption": true,
                "width": "auto"
              }
            }
          ],
          "events": {},
          "key": "ood.UI.Block",
          "path": "demo.SearchTestForm.SearchTestFormMain.SAVEBottomBlock",
          "properties": {
            "borderType": "none",
            "comboType": "STATUSBUTTONS",
            "dock": "bottom",
            "height": "3.0em"
          }
        },
        {
          "alias": "SearchTestFormForm",
          "children": [
            {
              "alias": "name",
              "events": {},
              "key": "ood.UI.Input",
              "path": "demo.SearchTestForm.SearchTestFormMain.SearchTestFormForm.name",
              "properties": {
                "caption": "姓名",
                "desc": "姓名",
                "disabled": false,
                "events": {},
                "expression": "this.age+this.name",
                "id": "name",
                "imageClass": "",
                "labelCaption": "姓名",
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
              "alias": "sex",
              "events": {},
              "key": "ood.UI.ComboInput",
              "path": "demo.SearchTestForm.SearchTestFormMain.SearchTestFormForm.sex",
              "properties": {
                "caption": "性別",
                "desc": "性別",
                "disabled": false,
                "enumClass": "net.ooder.test.SexEnum",
                "id": "sex",
                "labelCaption": "性別",
                "name": "sex",
                "readonly": false,
                "type": "input",
                "visibility": "visible"
              },
              "target": "B2"
            },
            {
              "alias": "tagCmds",
              "events": {},
              "key": "ood.UI.Input",
              "path": "demo.SearchTestForm.SearchTestFormMain.SearchTestFormForm.tagCmds",
              "properties": {
                "caption": "tagCmds",
                "desc": "tagCmds",
                "disabled": false,
                "events": {},
                "id": "tagCmds",
                "labelCaption": "tagCmds",
                "labelPos": "left",
                "labelSize": "6.0em",
                "name": "tagCmds",
                "readonly": false,
                "tagVar": {}
              },
              "target": "B3"
            },
            {
              "alias": "age",
              "events": {},
              "key": "ood.UI.ComboInput",
              "path": "demo.SearchTestForm.SearchTestFormMain.SearchTestFormForm.age",
              "properties": {
                "caption": "年龄",
                "currencyTpl": "$ *",
                "decimalSeparator": ".",
                "desc": "年龄",
                "disabled": false,
                "expression": "this.age+this.name",
                "forceFillZero": true,
                "increment": 1,
                "labelCaption": "年龄",
                "name": "age",
                "precision": 0,
                "readonly": false,
                "type": "spin",
                "visibility": "visible"
              },
              "target": "B4"
            }
          ],
          "events": {},
          "key": "ood.UI.FormLayout",
          "path": "demo.SearchTestForm.SearchTestFormMain.SearchTestFormForm",
          "properties": {
            "caption": "SerchTestForm",
            "defaultColWidth": 150,
            "defaultColumnSize": -1,
            "defaultRowHeight": 35,
            "defaultRowSize": -1,
            "dock": "fill",
            "floatHandler": false,
            "id": "SerchTestForm",
            "imageClass": "fa-solid fa-file-alt",
            "layoutData": {
              "cells": {
                "A1": {
                  "cellName": "A1",
                  "style": {
                    "textAlign": "center"
                  },
                  "value": "姓名"
                },
                "A2": {
                  "cellName": "A2",
                  "style": {
                    "textAlign": "center"
                  },
                  "value": "性別"
                },
                "A3": {
                  "cellName": "A3",
                  "style": {
                    "textAlign": "center"
                  },
                  "value": "tagCmds"
                },
                "A4": {
                  "cellName": "A4",
                  "style": {
                    "textAlign": "center"
                  },
                  "value": "年龄"
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
              "cols": 2,
              "rowSetting": {
                "1": {
                  "manualHeight": 35
                },
                "2": {
                  "manualHeight": 35
                },
                "3": {
                  "manualHeight": 35
                },
                "4": {
                  "manualHeight": 35
                }
              },
              "rows": 4
            },
            "lineSpacing": 10,
            "mode": "write",
            "name": "SerchTestForm",
            "solidGridlines": true,
            "stretchH": "all",
            "stretchHeight": "none",
            "tagVar": {},
            "visibility": "visible"
          }
        }
      ],
      "events": {},
      "key": "ood.UI.Block",
      "path": "demo.SearchTestForm.SearchTestFormMain",
      "properties": {
        "borderType": "none",
        "dock": "fill",
        "imageClass": "fa-solid fa-file-alt",
        "visibility": "visible"
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
              "target": "demo.SearchTestForm",
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
              "target": "demo.SearchTestForm",
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
              "id": "saveForm_0",
              "method": "destroy",
              "redirection": "page::",
              "return": true,
              "target": "demo.SearchTestForm",
              "type": "page"
            }
          ]
        }
      },
      "key": "ood.APICaller",
      "path": "demo.SearchTestForm.SAVE",
      "properties": {
        "allform": false,
        "autoRun": false,
        "desc": "保存",
        "id": "/demo/SaveForm",
        "imageClass": "fa-solid fa-code",
        "isAllform": false,
        "methodName": "saveForm",
        "name": "SaveForm",
        "proxyType": "auto",
        "queryAsync": true,
        "queryMethod": "POST",
        "queryURL": "/demo/SaveForm",
        "requestDataSource": [
          {
            "name": "testView",
            "path": "",
            "type": "FORM"
          },
          {
            "name": "testView",
            "path": "",
            "type": "FORM"
          },
          {
            "name": "SearchTestFormMain",
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
        "sourceClassName": "net.ooder.test.TestService",
        "tips": "saveForm"
      }
    },
    {
      "alias": "RELOAD",
      "events": {},
      "key": "ood.APICaller",
      "path": "demo.SearchTestForm.RELOAD",
      "properties": {
        "desc": "SerchTestForm",
        "id": "/demo/SearchTestForm",
        "imageClass": "fa-solid fa-code",
        "methodName": "SerchTestForm",
        "name": "SearchTestForm",
        "proxyType": "auto",
        "queryMethod": "POST",
        "queryURL": "/demo/SearchTestForm",
        "requestDataSource": [
          {
            "name": "testView",
            "path": "",
            "type": "FORM"
          },
          {
            "name": "testView",
            "path": "",
            "type": "FORM"
          },
          {
            "name": "SearchTestFormMain",
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
        "responseDataTarget": [
          {
            "name": "SearchTestFormMain",
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
        "sourceClassName": "net.ooder.test.TestService",
        "tips": "SerchTestForm"
      }
    }
  ],
  "className": "demo.SearchTestForm",
  "customFunctions": {},
  "desc": "SerchTestForm",
  "dio": false,
  "events": {},
  "formulas": [],
  "key": "ood.Module",
  "moduleType": "EUClass",
  "moduleVar": {},
  "moduleViewType": "FORMCONFIG",
  "path": "demo.SearchTestForm",
  "properties": {
    "autoDestroy": true,
    "bindClass": [
      "net.ooder.test.TestService"
    ],
    "bindClassName": "net.ooder.test.TestService",
    "bindService": "net.ooder.test.TestService",
    "currComponentAlias": "SearchTestFormForm",
    "dsmProperties": {
      "domainId": "TEST_VIEW",
      "moduleViewType": "FORMCONFIG",
      "projectName": "test",
      "realPath": "demo.searchtestform.searchtestformform",
      "sourceClassName": "net.ooder.test.TestService",
      "sourceMethodName": "SerchTestForm"
    },
    "dynLoad": false,
    "name": "SerchTestForm",
    "panelType": "block",
    "src": "/demo/SearchTestForm",
    "viewClass": "net.ooder.esd.custom.component.form.FullClassFormComponent"
  },
  "saveUrl": "/demo/SaveForm"
}