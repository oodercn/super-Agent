{
  "alias": "TestForm",
  "children": [
    {
      "alias": "PAGECTX",
      "children": [
        {
          "alias": "testView",
          "events": {},
          "key": "ood.UI.HiddenInput",
          "path": "demo.TestForm.PAGECTX.testView",
          "properties": {
            "formField": true,
            "isPid": false,
            "name": "testView",
            "pid": false,
            "tabindex": 0
          }
        },
        {
          "alias": "projectName",
          "events": {},
          "key": "ood.UI.HiddenInput",
          "path": "demo.TestForm.PAGECTX.projectName",
          "properties": {
            "formField": true,
            "isPid": false,
            "name": "projectName",
            "pid": false,
            "tabindex": 1,
            "value": "testVVVERSION0"
          }
        },
        {
          "alias": "projectVersionName",
          "events": {},
          "key": "ood.UI.HiddenInput",
          "path": "demo.TestForm.PAGECTX.projectVersionName",
          "properties": {
            "formField": true,
            "isPid": false,
            "name": "projectVersionName",
            "pid": false,
            "tabindex": 2,
            "value": "testVVVERSION0"
          }
        },
        {
          "alias": "personId",
          "events": {},
          "key": "ood.UI.HiddenInput",
          "path": "demo.TestForm.PAGECTX.personId",
          "properties": {
            "formField": true,
            "isPid": true,
            "name": "personId",
            "pid": true,
            "tabindex": 3
          }
        }
      ],
      "events": {},
      "key": "ood.UI.Block",
      "params": {},
      "path": "demo.TestForm.PAGECTX",
      "properties": {
        "borderType": "none",
        "tabindex": 0,
        "visibility": "hidden"
      }
    },
    {
      "alias": "CustomFieldService_onChange",
      "events": {},
      "key": "ood.APICaller",
      "path": "demo.TestForm.CustomFieldService_onChange",
      "properties": {
        "allform": false,
        "autoRun": false,
        "checkRequired": false,
        "checkValid": false,
        "desc": "onChange",
        "id": "/form/custom/field/onChange.dyn",
        "imageClass": "ri-code-box-line",
        "isAllform": false,
        "methodName": "onChange",
        "name": "onChange.dyn",
        "proxyType": "auto",
        "queryAsync": true,
        "queryMethod": "POST",
        "queryURL": "/form/custom/field/onChange.dyn",
        "requestDataSource": [
          {
            "name": "TestForm",
            "path": "",
            "type": "FORM"
          },
          {
            "name": "TestFormMain",
            "path": "",
            "type": "FORM"
          }
        ],
        "requestType": "FORM",
        "responseCallback": [],
        "responseDataTarget": [
          {
            "name": "PAGECTX",
            "path": "data",
            "type": "EXPRESSION"
          }
        ],
        "responseType": "JSON",
        "sourceClassName": "net.ooder.esd.custom.service.CustomFieldService",
        "tabindex": 1,
        "tips": "onChange"
      }
    },
    {
      "alias": "TestFormMain",
      "children": [
        {
          "alias": "TestForm",
          "children": [
            {
              "alias": "name",
              "events": {
                "onChange": {
                  "actions": [
                    {
                      "args": [
                        "{page.CustomFieldService_onChange.setQueryData()}",
                        null,
                        null,
                        "name",
                        "fieldName"
                      ],
                      "conditions": [],
                      "desc": "设定参数",
                      "method": "setQueryData",
                      "redirection": "other:callback:call",
                      "return": true,
                      "target": "CustomFieldService_onChange",
                      "type": "control"
                    },
                    {
                      "args": [
                        "{page.CustomFieldService_onChange.invoke()}"
                      ],
                      "conditions": [],
                      "desc": "调用onChange",
                      "eventClass": "net.ooder.esd.annotation.event.FieldEventEnum",
                      "eventValue": "onChange",
                      "method": "invoke",
                      "redirection": "other:callback:call",
                      "return": false,
                      "target": "CustomFieldService_onChange",
                      "type": "control"
                    }
                  ]
                }
              },
              "key": "ood.UI.Input",
              "path": "demo.TestForm.TestFormMain.TestForm.name",
              "properties": {
                "caption": "姓名",
                "defaultFocus": false,
                "desc": "姓名",
                "disableClickEffect": false,
                "disableHoverEffect": false,
                "disableTips": false,
                "disabled": false,
                "dock": "none",
                "events": {},
                "expression": "age+name",
                "id": "name",
                "imageClass": "",
                "items": [],
                "labelCaption": "姓名",
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
              "alias": "sex",
              "events": {},
              "key": "ood.UI.ComboInput",
              "path": "demo.TestForm.TestFormMain.TestForm.sex",
              "properties": {
                "caption": "性別",
                "defaultFocus": false,
                "desc": "性別",
                "disableClickEffect": false,
                "disableHoverEffect": false,
                "disableTips": false,
                "disabled": false,
                "dock": "none",
                "enumClass": "net.ooder.test.SexEnum",
                "id": "sex",
                "items": [],
                "labelCaption": "性別",
                "name": "sex",
                "readonly": false,
                "tabindex": 1,
                "type": "input",
                "visibility": "visible"
              },
              "target": "B2"
            },
            {
              "alias": "tagCmds",
              "events": {},
              "key": "ood.UI.ComboInput",
              "path": "demo.TestForm.TestFormMain.TestForm.tagCmds",
              "properties": {
                "caption": "tagCmds",
                "defaultFocus": false,
                "disableClickEffect": false,
                "disableHoverEffect": false,
                "disableTips": false,
                "disabled": false,
                "dock": "none",
                "id": "tagCmds",
                "items": [],
                "labelCaption": "tagCmds",
                "name": "tagCmds",
                "readonly": false,
                "tabindex": 2,
                "type": "input",
                "visibility": "visible"
              },
              "target": "B3"
            },
            {
              "alias": "age",
              "events": {
                "onChange": {
                  "actions": [
                    {
                      "args": [
                        "{page.CustomFieldService_onChange.setQueryData()}",
                        null,
                        null,
                        "age",
                        "fieldName"
                      ],
                      "conditions": [],
                      "desc": "设定参数",
                      "method": "setQueryData",
                      "redirection": "other:callback:call",
                      "return": true,
                      "target": "CustomFieldService_onChange",
                      "type": "control"
                    },
                    {
                      "args": [
                        "{page.CustomFieldService_onChange.invoke()}"
                      ],
                      "conditions": [],
                      "desc": "调用onChange",
                      "eventClass": "net.ooder.esd.annotation.event.FieldEventEnum",
                      "eventValue": "onChange",
                      "method": "invoke",
                      "redirection": "other:callback:call",
                      "return": false,
                      "target": "CustomFieldService_onChange",
                      "type": "control"
                    }
                  ]
                }
              },
              "key": "ood.UI.ComboInput",
              "path": "demo.TestForm.TestFormMain.TestForm.age",
              "properties": {
                "caption": "年龄",
                "defaultFocus": false,
                "desc": "年龄",
                "disableClickEffect": false,
                "disableHoverEffect": false,
                "disableTips": false,
                "disabled": false,
                "dock": "none",
                "id": "age",
                "items": [],
                "labelCaption": "年龄",
                "name": "age",
                "readonly": false,
                "tabindex": 3,
                "type": "input",
                "visibility": "visible"
              },
              "target": "B4"
            }
          ],
          "events": {},
          "key": "ood.UI.FormLayout",
          "path": "demo.TestForm.TestFormMain.TestForm",
          "properties": {
            "caption": "添加",
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
            "id": "testForm",
            "imageClass": "ri-file-text-line",
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
            "name": "GetTestForm",
            "solidGridlines": true,
            "stretchH": "all",
            "stretchHeight": "none",
            "tabindex": 0,
            "tagVar": {},
            "visibility": "visible"
          }
        },
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
                    },
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
                    }
                  ]
                }
              },
              "key": "ood.UI.StatusButtons",
              "menuType": "BOTTOMBAR",
              "path": "demo.TestForm.TestFormMain.SAVEBottomBlock.SAVEBottom",
              "properties": {
                "borderType": "none",
                "dock": "fill",
                "itemType": "button",
                "itemWidth": "auto",
                "items": [
                  {
                    "caption": "保存",
                    "iconColor": "#195ead",
                    "id": "SAVE_button",
                    "imageClass": "ri-save-line",
                    "index": 0,
                    "itemType": "button",
                    "tabindex": 0,
                    "tagVar": {
                      "name": "SAVE",
                      "clazz": "net.ooder.esd.annotation.menu.CustomFormMenu"
                    },
                    "tips": "保存",
                    "title": "保存"
                  },
                  {
                    "caption": "查找",
                    "iconColor": "#87CEEB",
                    "id": "SEARCH_button",
                    "imageClass": "ri-search-line",
                    "index": 0,
                    "itemType": "button",
                    "tabindex": 1,
                    "tagVar": {
                      "name": "SEARCH",
                      "clazz": "net.ooder.esd.annotation.menu.CustomFormMenu"
                    },
                    "tips": "查找",
                    "title": "查找"
                  }
                ],
                "menuType": "BOTTOMBAR",
                "position": "static",
                "showCaption": true,
                "tabindex": 0,
                "width": "auto"
              }
            }
          ],
          "events": {},
          "key": "ood.UI.Block",
          "path": "demo.TestForm.TestFormMain.SAVEBottomBlock",
          "properties": {
            "borderType": "none",
            "comboType": "STATUSBUTTONS",
            "dock": "bottom",
            "height": "3.0em",
            "overflow": "hidden",
            "tabindex": 1
          }
        }
      ],
      "events": {
        "afterDestroy": {
          "actions": [
            {
              "args": [],
              "conditions": [],
              "desc": "关闭页面",
              "enumClassName": "net.ooder.esd.annotation.action.CustomPageAction",
              "enumValue": "CLOSE",
              "eventClass": "net.ooder.esd.annotation.event.DialogEventEnum",
              "eventValue": "afterDestroy",
              "id": "net.ooder.esd.annotation.action.CustomPageAction_CLOSE_null_null",
              "method": "destroy",
              "redirection": "page::",
              "return": true,
              "target": "demo.TestForm",
              "type": "page"
            }
          ]
        }
      },
      "key": "ood.UI.Dialog",
      "path": "demo.TestForm.TestFormMain",
      "properties": {
        "defaultFocus": false,
        "disableClickEffect": false,
        "disableHoverEffect": false,
        "disableTips": false,
        "disabled": false,
        "dock": "none",
        "height": "35.0em",
        "left": "220.0",
        "showEffects": "Classic",
        "tabindex": 2,
        "top": "100.0",
        "visibility": "visible",
        "width": "40.0em"
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
              "target": "demo.TestForm",
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
              "target": "demo.TestForm",
              "type": "page"
            }
          ]
        }
      },
      "key": "ood.APICaller",
      "path": "demo.TestForm.SAVE",
      "properties": {
        "allform": false,
        "autoRun": false,
        "checkRequired": false,
        "checkValid": false,
        "desc": "保存",
        "id": "/demo/SaveForm",
        "imageClass": "ri-code-box-line",
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
            "name": "TestFormMain",
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
        "tabindex": 3,
        "tips": "保存"
      }
    },
    {
      "alias": "RELOAD",
      "events": {},
      "key": "ood.APICaller",
      "path": "demo.TestForm.RELOAD",
      "properties": {
        "allform": false,
        "autoRun": true,
        "checkRequired": false,
        "checkValid": false,
        "desc": "添加",
        "id": "/demo/TestForm",
        "imageClass": "ri-code-box-line",
        "isAllform": false,
        "methodName": "getTestForm",
        "name": "TestForm",
        "proxyType": "auto",
        "queryAsync": true,
        "queryMethod": "POST",
        "queryURL": "/demo/TestForm",
        "requestDataSource": [
          {
            "name": "testView",
            "path": "",
            "type": "FORM"
          },
          {
            "name": "projectName",
            "path": "",
            "type": "FORM"
          },
          {
            "name": "testView",
            "path": "",
            "type": "FORM"
          },
          {
            "name": "projectName",
            "path": "",
            "type": "FORM"
          },
          {
            "name": "TestFormMain",
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
            "name": "PAGECTX",
            "path": "data",
            "type": "EXPRESSION"
          },
          {
            "name": "TestFormMain",
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
        "tabindex": 4,
        "tips": "添加"
      }
    }
  ],
  "className": "demo.TestForm",
  "customFunctions": {},
  "desc": "添加",
  "dio": true,
  "events": {},
  "formulas": [],
  "key": "ood.Module",
  "moduleType": "EUClass",
  "moduleVar": {},
  "moduleViewType": "FORMCONFIG",
  "path": "demo.TestForm",
  "properties": {
    "autoDestroy": true,
    "bindClass": [],
    "caption": "添加",
    "currComponentAlias": "TestForm",
    "dock": "fill",
    "title": "添加"
  },
  "saveUrl": "/demo/SaveForm"
}