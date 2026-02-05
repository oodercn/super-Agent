{
  "alias": "FieldMenuConfig",
  "children": [
    {
      "alias": "PAGECTX",
      "children": [
        {
          "alias": "fieldname",
          "events": {},
          "key": "ood.UI.HiddenInput",
          "path": "dsm.view.config.form.field.contextmenu.FieldMenuConfig.PAGECTX.fieldname",
          "properties": {
            "formField": true,
            "isPid": true,
            "name": "fieldname",
            "pid": true,
            "tabindex": 0
          }
        },
        {
          "alias": "currentClassName",
          "events": {},
          "key": "ood.UI.HiddenInput",
          "path": "dsm.view.config.form.field.contextmenu.FieldMenuConfig.PAGECTX.currentClassName",
          "properties": {
            "formField": true,
            "isPid": true,
            "name": "currentClassName",
            "pid": true,
            "tabindex": 1
          }
        },
        {
          "alias": "xpath",
          "events": {},
          "key": "ood.UI.HiddenInput",
          "path": "dsm.view.config.form.field.contextmenu.FieldMenuConfig.PAGECTX.xpath",
          "properties": {
            "formField": true,
            "isPid": false,
            "name": "xpath",
            "pid": false,
            "tabindex": 2
          }
        },
        {
          "alias": "sourceMethodName",
          "events": {},
          "key": "ood.UI.HiddenInput",
          "path": "dsm.view.config.form.field.contextmenu.FieldMenuConfig.PAGECTX.sourceMethodName",
          "properties": {
            "formField": true,
            "isPid": true,
            "name": "sourceMethodName",
            "pid": true,
            "tabindex": 3
          }
        },
        {
          "alias": "sourceClassName",
          "events": {},
          "key": "ood.UI.HiddenInput",
          "path": "dsm.view.config.form.field.contextmenu.FieldMenuConfig.PAGECTX.sourceClassName",
          "properties": {
            "formField": true,
            "isPid": true,
            "name": "sourceClassName",
            "pid": true,
            "tabindex": 4
          }
        },
        {
          "alias": "methodName",
          "events": {},
          "key": "ood.UI.HiddenInput",
          "path": "dsm.view.config.form.field.contextmenu.FieldMenuConfig.PAGECTX.methodName",
          "properties": {
            "formField": true,
            "isPid": false,
            "name": "methodName",
            "pid": false,
            "tabindex": 5
          }
        },
        {
          "alias": "projectName",
          "events": {},
          "key": "ood.UI.HiddenInput",
          "path": "dsm.view.config.form.field.contextmenu.FieldMenuConfig.PAGECTX.projectName",
          "properties": {
            "formField": true,
            "isPid": false,
            "name": "projectName",
            "pid": false,
            "tabindex": 6,
            "value": "DSMdsmVVVERSION0"
          }
        },
        {
          "alias": "domainId",
          "events": {},
          "key": "ood.UI.HiddenInput",
          "path": "dsm.view.config.form.field.contextmenu.FieldMenuConfig.PAGECTX.domainId",
          "properties": {
            "formField": true,
            "isPid": true,
            "name": "domainId",
            "pid": true,
            "tabindex": 7
          }
        },
        {
          "alias": "projectVersionName",
          "events": {},
          "key": "ood.UI.HiddenInput",
          "path": "dsm.view.config.form.field.contextmenu.FieldMenuConfig.PAGECTX.projectVersionName",
          "properties": {
            "formField": true,
            "isPid": false,
            "name": "projectVersionName",
            "pid": false,
            "tabindex": 8,
            "value": "DSMdsmVVVERSION0"
          }
        },
        {
          "alias": "path",
          "events": {},
          "key": "ood.UI.HiddenInput",
          "path": "dsm.view.config.form.field.contextmenu.FieldMenuConfig.PAGECTX.path",
          "properties": {
            "formField": true,
            "isPid": true,
            "name": "path",
            "pid": true,
            "tabindex": 9
          }
        },
        {
          "alias": "id",
          "events": {},
          "key": "ood.UI.HiddenInput",
          "path": "dsm.view.config.form.field.contextmenu.FieldMenuConfig.PAGECTX.id",
          "properties": {
            "formField": true,
            "isPid": true,
            "name": "id",
            "pid": true,
            "tabindex": 10
          }
        },
        {
          "alias": "parentID",
          "events": {},
          "key": "ood.UI.HiddenInput",
          "path": "dsm.view.config.form.field.contextmenu.FieldMenuConfig.PAGECTX.parentID",
          "properties": {
            "formField": true,
            "isPid": false,
            "name": "parentID",
            "pid": false,
            "tabindex": 11
          }
        }
      ],
      "events": {},
      "key": "ood.UI.Block",
      "params": {},
      "path": "dsm.view.config.form.field.contextmenu.FieldMenuConfig.PAGECTX",
      "properties": {
        "borderType": "none",
        "tabindex": 0,
        "visibility": "hidden"
      }
    },
    {
      "alias": "FieldMenuConfigMain",
      "children": [
        {
          "alias": "FieldMenuConfig",
          "children": [
            {
              "alias": "handler",
              "events": {},
              "key": "ood.UI.CheckBox",
              "path": "dsm.view.config.form.field.contextmenu.FieldMenuConfig.FieldMenuConfigMain.FieldMenuConfig.handler",
              "properties": {
                "caption": "",
                "defaultFocus": false,
                "desc": "行头手柄",
                "disableClickEffect": false,
                "disableHoverEffect": false,
                "disableTips": false,
                "dock": "none",
                "events": {},
                "id": "handler",
                "name": "handler",
                "tabindex": 0,
                "visibility": "visible"
              },
              "target": "B1"
            },
            {
              "alias": "dynLoad",
              "events": {},
              "key": "ood.UI.CheckBox",
              "path": "dsm.view.config.form.field.contextmenu.FieldMenuConfig.FieldMenuConfigMain.FieldMenuConfig.dynLoad",
              "properties": {
                "caption": "",
                "defaultFocus": false,
                "desc": "动态加载",
                "disableClickEffect": false,
                "disableHoverEffect": false,
                "disableTips": false,
                "dock": "none",
                "events": {},
                "id": "dynLoad",
                "name": "dynLoad",
                "tabindex": 1,
                "visibility": "visible"
              },
              "target": "D1"
            },
            {
              "alias": "lazy",
              "events": {},
              "key": "ood.UI.CheckBox",
              "path": "dsm.view.config.form.field.contextmenu.FieldMenuConfig.FieldMenuConfigMain.FieldMenuConfig.lazy",
              "properties": {
                "caption": "",
                "defaultFocus": false,
                "desc": "延迟加载",
                "disableClickEffect": false,
                "disableHoverEffect": false,
                "disableTips": false,
                "dock": "none",
                "events": {},
                "id": "lazy",
                "name": "lazy",
                "tabindex": 2,
                "visibility": "visible"
              },
              "target": "B2"
            },
            {
              "alias": "hideAfterClick",
              "events": {},
              "key": "ood.UI.CheckBox",
              "path": "dsm.view.config.form.field.contextmenu.FieldMenuConfig.FieldMenuConfigMain.FieldMenuConfig.hideAfterClick",
              "properties": {
                "caption": "",
                "defaultFocus": false,
                "desc": "点击隐藏",
                "disableClickEffect": false,
                "disableHoverEffect": false,
                "disableTips": false,
                "dock": "none",
                "events": {},
                "id": "hideAfterClick",
                "name": "hideAfterClick",
                "tabindex": 3,
                "visibility": "visible"
              },
              "target": "D2"
            },
            {
              "alias": "autoHide",
              "events": {},
              "key": "ood.UI.CheckBox",
              "path": "dsm.view.config.form.field.contextmenu.FieldMenuConfig.FieldMenuConfigMain.FieldMenuConfig.autoHide",
              "properties": {
                "caption": "",
                "defaultFocus": false,
                "desc": "自动隐藏",
                "disableClickEffect": false,
                "disableHoverEffect": false,
                "disableTips": false,
                "dock": "none",
                "events": {},
                "id": "autoHide",
                "name": "autoHide",
                "tabindex": 4,
                "visibility": "visible"
              },
              "target": "B3"
            },
            {
              "alias": "formField",
              "events": {},
              "key": "ood.UI.CheckBox",
              "path": "dsm.view.config.form.field.contextmenu.FieldMenuConfig.FieldMenuConfigMain.FieldMenuConfig.formField",
              "properties": {
                "caption": "",
                "defaultFocus": false,
                "desc": "是否数据域",
                "disableClickEffect": false,
                "disableHoverEffect": false,
                "disableTips": false,
                "dock": "none",
                "events": {},
                "id": "formField",
                "name": "formField",
                "tabindex": 5,
                "visibility": "visible"
              },
              "target": "D3"
            },
            {
              "alias": "serviceClass",
              "events": {},
              "key": "ood.UI.Input",
              "path": "dsm.view.config.form.field.contextmenu.FieldMenuConfig.FieldMenuConfigMain.FieldMenuConfig.serviceClass",
              "properties": {
                "caption": "实现类",
                "defaultFocus": false,
                "desc": "实现类",
                "disableClickEffect": false,
                "disableHoverEffect": false,
                "disableTips": false,
                "disabled": false,
                "dock": "none",
                "events": {},
                "id": "serviceClass",
                "imageClass": "",
                "items": [],
                "labelCaption": "实现类",
                "labelPos": "left",
                "labelSize": "6.0em",
                "name": "serviceClass",
                "readonly": false,
                "tabindex": 6,
                "tagVar": {},
                "visibility": "visible"
              },
              "target": "B4"
            },
            {
              "alias": "listKey",
              "events": {},
              "key": "ood.UI.Input",
              "path": "dsm.view.config.form.field.contextmenu.FieldMenuConfig.FieldMenuConfigMain.FieldMenuConfig.listKey",
              "properties": {
                "caption": "拖动支持KEY",
                "defaultFocus": false,
                "desc": "拖动支持KEY",
                "disableClickEffect": false,
                "disableHoverEffect": false,
                "disableTips": false,
                "disabled": false,
                "dock": "none",
                "events": {},
                "id": "listKey",
                "imageClass": "",
                "items": [],
                "labelCaption": "拖动支持KEY",
                "labelPos": "left",
                "labelSize": "6.0em",
                "name": "listKey",
                "readonly": false,
                "tabindex": 7,
                "tagVar": {},
                "visibility": "visible"
              },
              "target": "B5"
            },
            {
              "alias": "iconFontSize",
              "events": {},
              "key": "ood.UI.Input",
              "path": "dsm.view.config.form.field.contextmenu.FieldMenuConfig.FieldMenuConfigMain.FieldMenuConfig.iconFontSize",
              "properties": {
                "caption": "字体大小",
                "defaultFocus": false,
                "desc": "字体大小",
                "disableClickEffect": false,
                "disableHoverEffect": false,
                "disableTips": false,
                "disabled": false,
                "dock": "none",
                "events": {},
                "id": "iconFontSize",
                "imageClass": "",
                "items": [],
                "labelCaption": "字体大小",
                "labelPos": "left",
                "labelSize": "6.0em",
                "name": "iconFontSize",
                "readonly": false,
                "tabindex": 8,
                "tagVar": {},
                "visibility": "visible"
              },
              "target": "D5"
            },
            {
              "alias": "itemClass",
              "events": {},
              "key": "ood.UI.Input",
              "path": "dsm.view.config.form.field.contextmenu.FieldMenuConfig.FieldMenuConfigMain.FieldMenuConfig.itemClass",
              "properties": {
                "caption": "菜单项Class",
                "defaultFocus": false,
                "desc": "菜单项Class",
                "disableClickEffect": false,
                "disableHoverEffect": false,
                "disableTips": false,
                "disabled": false,
                "dock": "none",
                "events": {},
                "id": "itemClass",
                "imageClass": "",
                "items": [],
                "labelCaption": "菜单项Class",
                "labelPos": "left",
                "labelSize": "6.0em",
                "name": "itemClass",
                "readonly": false,
                "tabindex": 9,
                "tagVar": {},
                "visibility": "visible"
              },
              "target": "B6"
            },
            {
              "alias": "itemStyle",
              "events": {},
              "key": "ood.UI.Input",
              "path": "dsm.view.config.form.field.contextmenu.FieldMenuConfig.FieldMenuConfigMain.FieldMenuConfig.itemStyle",
              "properties": {
                "caption": "菜单项Style",
                "defaultFocus": false,
                "desc": "菜单项Style",
                "disableClickEffect": false,
                "disableHoverEffect": false,
                "disableTips": false,
                "disabled": false,
                "dock": "none",
                "events": {},
                "id": "itemStyle",
                "imageClass": "",
                "items": [],
                "labelCaption": "菜单项Style",
                "labelPos": "left",
                "labelSize": "6.0em",
                "name": "itemStyle",
                "readonly": false,
                "tabindex": 10,
                "tagVar": {},
                "visibility": "visible"
              },
              "target": "B7"
            },
            {
              "alias": "bindService",
              "events": {},
              "key": "ood.UI.Input",
              "path": "dsm.view.config.form.field.contextmenu.FieldMenuConfig.FieldMenuConfigMain.FieldMenuConfig.bindService",
              "properties": {
                "caption": "绑定服务",
                "defaultFocus": false,
                "desc": "绑定服务",
                "disableClickEffect": false,
                "disableHoverEffect": false,
                "disableTips": false,
                "disabled": false,
                "dock": "none",
                "events": {},
                "id": "bindService",
                "imageClass": "",
                "items": [],
                "labelCaption": "绑定服务",
                "labelPos": "left",
                "labelSize": "6.0em",
                "name": "bindService",
                "readonly": false,
                "tabindex": 11,
                "tagVar": {},
                "visibility": "visible"
              },
              "target": "B8"
            }
          ],
          "events": {},
          "key": "ood.UI.FormLayout",
          "path": "dsm.view.config.form.field.contextmenu.FieldMenuConfig.FieldMenuConfigMain.FieldMenuConfig",
          "properties": {
            "caption": "右键菜单信息",
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
            "id": "fieldMenuConfig",
            "imageClass": "ri-file-text-line",
            "layoutData": {
              "cells": {
                "A1": {
                  "cellName": "A1",
                  "style": {
                    "textAlign": "center"
                  },
                  "value": "行头手柄"
                },
                "C3": {
                  "cellName": "C3",
                  "style": {
                    "textAlign": "center"
                  },
                  "value": "是否数据域"
                },
                "A2": {
                  "cellName": "A2",
                  "style": {
                    "textAlign": "center"
                  },
                  "value": "延迟加载"
                },
                "A3": {
                  "cellName": "A3",
                  "style": {
                    "textAlign": "center"
                  },
                  "value": "自动隐藏"
                },
                "C5": {
                  "cellName": "C5",
                  "style": {
                    "textAlign": "center"
                  },
                  "value": "字体大小"
                },
                "A4": {
                  "cellName": "A4",
                  "style": {
                    "textAlign": "center"
                  },
                  "value": "实现类"
                },
                "A5": {
                  "cellName": "A5",
                  "style": {
                    "textAlign": "center"
                  },
                  "value": "拖动支持KEY"
                },
                "A6": {
                  "cellName": "A6",
                  "style": {
                    "textAlign": "center"
                  },
                  "value": "菜单项Class"
                },
                "A7": {
                  "cellName": "A7",
                  "style": {
                    "textAlign": "center"
                  },
                  "value": "菜单项Style"
                },
                "A8": {
                  "cellName": "A8",
                  "style": {
                    "textAlign": "center"
                  },
                  "value": "绑定服务"
                },
                "C1": {
                  "cellName": "C1",
                  "style": {
                    "textAlign": "center"
                  },
                  "value": "动态加载"
                },
                "C2": {
                  "cellName": "C2",
                  "style": {
                    "textAlign": "center"
                  },
                  "value": "点击隐藏"
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
                  "row": 3,
                  "rowspan": 1
                },
                {
                  "col": 1,
                  "colspan": 3,
                  "removed": false,
                  "row": 5,
                  "rowspan": 1
                },
                {
                  "col": 1,
                  "colspan": 3,
                  "removed": false,
                  "row": 6,
                  "rowspan": 1
                },
                {
                  "col": 1,
                  "colspan": 3,
                  "removed": false,
                  "row": 7,
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
                },
                "4": {
                  "manualHeight": 35
                },
                "5": {
                  "manualHeight": 35
                },
                "6": {
                  "manualHeight": 35
                },
                "7": {
                  "manualHeight": 50
                },
                "8": {
                  "manualHeight": 35
                }
              },
              "rows": 8
            },
            "lineSpacing": 10,
            "mode": "write",
            "name": "GetFieldMenuConfig",
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
      "path": "dsm.view.config.form.field.contextmenu.FieldMenuConfig.FieldMenuConfigMain",
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
              "target": "dsm.view.config.form.field.contextmenu.FieldMenuConfig",
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
              "target": "dsm.view.config.form.field.contextmenu.FieldMenuConfig",
              "type": "page"
            }
          ]
        }
      },
      "key": "ood.APICaller",
      "path": "dsm.view.config.form.field.contextmenu.FieldMenuConfig.SAVE",
      "properties": {
        "allform": true,
        "autoRun": false,
        "checkRequired": false,
        "checkValid": false,
        "desc": "保存",
        "id": "/dsm/view/config/form/field/contextmenu/addFieldContextMenu",
        "imageClass": "ri-code-box-line",
        "isAllform": true,
        "methodName": "addFieldContextMenu",
        "name": "addFieldContextMenu",
        "proxyType": "auto",
        "queryAsync": true,
        "queryMethod": "POST",
        "queryURL": "/dsm/view/config/form/field/contextmenu/addFieldContextMenu",
        "requestDataSource": [
          {
            "name": "FieldMenuConfigMain",
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
        "sourceClassName": "net.ooder.dsm.view.config.form.field.contextmenu.menuclass.ContextFieldMenuService",
        "tabindex": 2,
        "tips": "保存"
      }
    },
    {
      "alias": "RELOAD",
      "events": {},
      "key": "ood.APICaller",
      "path": "dsm.view.config.form.field.contextmenu.FieldMenuConfig.RELOAD",
      "properties": {
        "allform": false,
        "autoRun": true,
        "checkRequired": false,
        "checkValid": false,
        "desc": "右键菜单信息",
        "id": "/dsm/view/config/form/field/contextmenu/FieldMenuConfig",
        "imageClass": "ri-code-box-line",
        "isAllform": false,
        "methodName": "getFieldMenuConfig",
        "name": "FieldMenuConfig",
        "proxyType": "auto",
        "queryAsync": true,
        "queryMethod": "POST",
        "queryURL": "/dsm/view/config/form/field/contextmenu/FieldMenuConfig",
        "requestDataSource": [
          {
            "name": "FieldMenuConfigMain",
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
            "name": "FieldMenuConfigMain",
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
        "sourceClassName": "net.ooder.dsm.view.config.form.field.contextmenu.ContextFieldMenuNav",
        "tabindex": 3,
        "tips": "右键菜单信息"
      }
    }
  ],
  "className": "dsm.view.config.form.field.contextmenu.FieldMenuConfig",
  "customFunctions": {},
  "desc": "右键菜单信息",
  "dio": false,
  "events": {},
  "formulas": [],
  "key": "ood.Module",
  "moduleType": "EUClass",
  "moduleVar": {},
  "moduleViewType": "FORMCONFIG",
  "path": "dsm.view.config.form.field.contextmenu.FieldMenuConfig",
  "properties": {
    "autoDestroy": true,
    "bindClass": [],
    "caption": "右键菜单信息",
    "currComponentAlias": "FieldMenuConfig",
    "dock": "fill",
    "title": "右键菜单信息"
  },
  "saveUrl": "/dsm/view/config/form/field/contextmenu/addFieldContextMenu"
}