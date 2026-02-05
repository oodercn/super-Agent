{
  "alias": "ToolBarConfig",
  "children": [
    {
      "alias": "PAGECTX",
      "children": [
        {
          "alias": "currentClassName",
          "events": {},
          "key": "ood.UI.HiddenInput",
          "path": "dsm.view.config.menu.toolbar.ToolBarConfig.PAGECTX.currentClassName",
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
          "path": "dsm.view.config.menu.toolbar.ToolBarConfig.PAGECTX.xpath",
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
          "path": "dsm.view.config.menu.toolbar.ToolBarConfig.PAGECTX.sourceMethodName",
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
          "path": "dsm.view.config.menu.toolbar.ToolBarConfig.PAGECTX.sourceClassName",
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
          "path": "dsm.view.config.menu.toolbar.ToolBarConfig.PAGECTX.projectName",
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
          "path": "dsm.view.config.menu.toolbar.ToolBarConfig.PAGECTX.domainId",
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
          "path": "dsm.view.config.menu.toolbar.ToolBarConfig.PAGECTX.projectVersionName",
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
      "path": "dsm.view.config.menu.toolbar.ToolBarConfig.PAGECTX",
      "properties": {
        "borderType": "none",
        "tabindex": 0,
        "visibility": "hidden"
      }
    },
    {
      "alias": "ToolBarConfigMain",
      "children": [
        {
          "alias": "ToolBarConfig",
          "children": [
            {
              "alias": "groupId",
              "events": {},
              "key": "ood.UI.Input",
              "path": "dsm.view.config.menu.toolbar.ToolBarConfig.ToolBarConfigMain.ToolBarConfig.groupId",
              "properties": {
                "caption": "组ID",
                "defaultFocus": false,
                "desc": "组ID",
                "disableClickEffect": false,
                "disableHoverEffect": false,
                "disableTips": false,
                "disabled": false,
                "dock": "none",
                "events": {},
                "id": "groupId",
                "imageClass": "",
                "items": [],
                "labelCaption": "组ID",
                "labelPos": "left",
                "labelSize": "6.0em",
                "name": "groupId",
                "readonly": false,
                "tabindex": 0,
                "tagVar": {},
                "visibility": "visible"
              },
              "target": "B1"
            },
            {
              "alias": "iconFontSize",
              "events": {},
              "key": "ood.UI.ComboInput",
              "path": "dsm.view.config.menu.toolbar.ToolBarConfig.ToolBarConfigMain.ToolBarConfig.iconFontSize",
              "properties": {
                "caption": "字体大小",
                "defaultFocus": false,
                "desc": "字体大小",
                "disableClickEffect": false,
                "disableHoverEffect": false,
                "disableTips": false,
                "disabled": false,
                "dock": "none",
                "id": "iconFontSize",
                "items": [],
                "labelCaption": "字体大小",
                "name": "iconFontSize",
                "readonly": false,
                "tabindex": 1,
                "type": "input",
                "visibility": "visible"
              },
              "target": "B2"
            },
            {
              "alias": "hAlign",
              "events": {},
              "key": "ood.UI.ComboInput",
              "path": "dsm.view.config.menu.toolbar.ToolBarConfig.ToolBarConfigMain.ToolBarConfig.hAlign",
              "properties": {
                "caption": "垂直对齐",
                "defaultFocus": false,
                "desc": "垂直对齐",
                "disableClickEffect": false,
                "disableHoverEffect": false,
                "disableTips": false,
                "disabled": false,
                "dock": "none",
                "dynLoad": true,
                "enumClass": "net.ooder.esd.annotation.ui.HAlignType",
                "items": [
                  {
                    "bindClass": [],
                    "caption": "center",
                    "id": "center",
                    "tabindex": 0,
                    "tagVar": {
                      "name": "center",
                      "clazz": "net.ooder.esd.annotation.ui.HAlignType"
                    },
                    "title": "center"
                  },
                  {
                    "bindClass": [],
                    "caption": "left",
                    "id": "left",
                    "tabindex": 1,
                    "tagVar": {
                      "name": "left",
                      "clazz": "net.ooder.esd.annotation.ui.HAlignType"
                    },
                    "title": "left"
                  },
                  {
                    "bindClass": [],
                    "caption": "right",
                    "id": "right",
                    "tabindex": 2,
                    "tagVar": {
                      "name": "right",
                      "clazz": "net.ooder.esd.annotation.ui.HAlignType"
                    },
                    "title": "right"
                  }
                ],
                "labelCaption": "垂直对齐",
                "name": "hAlign",
                "readonly": false,
                "tabindex": 2,
                "type": "combobox",
                "visibility": "visible"
              },
              "target": "D2"
            },
            {
              "alias": "vAlign",
              "events": {},
              "key": "ood.UI.ComboInput",
              "path": "dsm.view.config.menu.toolbar.ToolBarConfig.ToolBarConfigMain.ToolBarConfig.vAlign",
              "properties": {
                "caption": "横向对齐",
                "defaultFocus": false,
                "desc": "横向对齐",
                "disableClickEffect": false,
                "disableHoverEffect": false,
                "disableTips": false,
                "disabled": false,
                "dock": "none",
                "dynLoad": true,
                "enumClass": "net.ooder.esd.annotation.ui.VAlignType",
                "items": [
                  {
                    "bindClass": [],
                    "caption": "bottom",
                    "id": "bottom",
                    "tabindex": 0,
                    "tagVar": {
                      "name": "bottom",
                      "clazz": "net.ooder.esd.annotation.ui.VAlignType"
                    },
                    "title": "bottom"
                  },
                  {
                    "bindClass": [],
                    "caption": "middle",
                    "id": "middle",
                    "tabindex": 1,
                    "tagVar": {
                      "name": "middle",
                      "clazz": "net.ooder.esd.annotation.ui.VAlignType"
                    },
                    "title": "middle"
                  },
                  {
                    "bindClass": [],
                    "caption": "top",
                    "id": "top",
                    "tabindex": 2,
                    "tagVar": {
                      "name": "top",
                      "clazz": "net.ooder.esd.annotation.ui.VAlignType"
                    },
                    "title": "top"
                  }
                ],
                "labelCaption": "横向对齐",
                "name": "vAlign",
                "readonly": false,
                "tabindex": 3,
                "type": "combobox",
                "visibility": "visible"
              },
              "target": "B3"
            },
            {
              "alias": "showCaption",
              "events": {},
              "key": "ood.UI.CheckBox",
              "path": "dsm.view.config.menu.toolbar.ToolBarConfig.ToolBarConfigMain.ToolBarConfig.showCaption",
              "properties": {
                "caption": "",
                "defaultFocus": false,
                "desc": "显示标题",
                "disableClickEffect": false,
                "disableHoverEffect": false,
                "disableTips": false,
                "dock": "none",
                "events": {},
                "id": "showCaption",
                "name": "showCaption",
                "tabindex": 4,
                "visibility": "visible"
              },
              "target": "D3"
            },
            {
              "alias": "handler",
              "events": {},
              "key": "ood.UI.CheckBox",
              "path": "dsm.view.config.menu.toolbar.ToolBarConfig.ToolBarConfigMain.ToolBarConfig.handler",
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
                "tabindex": 5,
                "visibility": "visible"
              },
              "target": "B4"
            },
            {
              "alias": "dynLoad",
              "events": {},
              "key": "ood.UI.CheckBox",
              "path": "dsm.view.config.menu.toolbar.ToolBarConfig.ToolBarConfigMain.ToolBarConfig.dynLoad",
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
                "tabindex": 6,
                "visibility": "visible"
              },
              "target": "D4"
            },
            {
              "alias": "disabled",
              "events": {},
              "key": "ood.UI.CheckBox",
              "path": "dsm.view.config.menu.toolbar.ToolBarConfig.ToolBarConfigMain.ToolBarConfig.disabled",
              "properties": {
                "caption": "",
                "defaultFocus": false,
                "desc": "禁用",
                "disableClickEffect": false,
                "disableHoverEffect": false,
                "disableTips": false,
                "dock": "none",
                "events": {},
                "id": "disabled",
                "name": "disabled",
                "tabindex": 7,
                "visibility": "visible"
              },
              "target": "B5"
            },
            {
              "alias": "formField",
              "events": {},
              "key": "ood.UI.CheckBox",
              "path": "dsm.view.config.menu.toolbar.ToolBarConfig.ToolBarConfigMain.ToolBarConfig.formField",
              "properties": {
                "caption": "",
                "defaultFocus": false,
                "desc": "表单字段",
                "disableClickEffect": false,
                "disableHoverEffect": false,
                "disableTips": false,
                "dock": "none",
                "events": {},
                "id": "formField",
                "name": "formField",
                "tabindex": 8,
                "visibility": "visible"
              },
              "target": "D5"
            },
            {
              "alias": "lazy",
              "events": {},
              "key": "ood.UI.CheckBox",
              "path": "dsm.view.config.menu.toolbar.ToolBarConfig.ToolBarConfigMain.ToolBarConfig.lazy",
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
                "tabindex": 9,
                "visibility": "visible"
              },
              "target": "B6"
            },
            {
              "alias": "bindService",
              "events": {},
              "key": "ood.UI.Input",
              "path": "dsm.view.config.menu.toolbar.ToolBarConfig.ToolBarConfigMain.ToolBarConfig.bindService",
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
                "id": "bindService",
                "imageClass": "",
                "items": [],
                "labelCaption": "实现类",
                "labelPos": "left",
                "labelSize": "6.0em",
                "name": "bindService",
                "readonly": false,
                "tabindex": 10,
                "tagVar": {},
                "visibility": "visible"
              },
              "target": "B7"
            }
          ],
          "events": {},
          "key": "ood.UI.FormLayout",
          "path": "dsm.view.config.menu.toolbar.ToolBarConfig.ToolBarConfigMain.ToolBarConfig",
          "properties": {
            "caption": "基础信息",
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
            "id": "toolBarConfig",
            "imageClass": "ri-file-text-line",
            "layoutData": {
              "cells": {
                "A1": {
                  "cellName": "A1",
                  "style": {
                    "textAlign": "center"
                  },
                  "value": "组ID"
                },
                "C3": {
                  "cellName": "C3",
                  "style": {
                    "textAlign": "center"
                  },
                  "value": "显示标题"
                },
                "A2": {
                  "cellName": "A2",
                  "style": {
                    "textAlign": "center"
                  },
                  "value": "字体大小"
                },
                "C4": {
                  "cellName": "C4",
                  "style": {
                    "textAlign": "center"
                  },
                  "value": "动态加载"
                },
                "A3": {
                  "cellName": "A3",
                  "style": {
                    "textAlign": "center"
                  },
                  "value": "横向对齐"
                },
                "C5": {
                  "cellName": "C5",
                  "style": {
                    "textAlign": "center"
                  },
                  "value": "表单字段"
                },
                "A4": {
                  "cellName": "A4",
                  "style": {
                    "textAlign": "center"
                  },
                  "value": "行头手柄"
                },
                "A5": {
                  "cellName": "A5",
                  "style": {
                    "textAlign": "center"
                  },
                  "value": "禁用"
                },
                "A6": {
                  "cellName": "A6",
                  "style": {
                    "textAlign": "center"
                  },
                  "value": "延迟加载"
                },
                "A7": {
                  "cellName": "A7",
                  "style": {
                    "textAlign": "center"
                  },
                  "value": "实现类"
                },
                "C2": {
                  "cellName": "C2",
                  "style": {
                    "textAlign": "center"
                  },
                  "value": "垂直对齐"
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
                  "row": 6,
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
                  "manualHeight": 35
                }
              },
              "rows": 7
            },
            "lineSpacing": 10,
            "mode": "write",
            "name": "GetToolBarConfig",
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
      "path": "dsm.view.config.menu.toolbar.ToolBarConfig.ToolBarConfigMain",
      "properties": {
        "dock": "fill",
        "panelBgClr": "transparent",
        "tabindex": 1
      }
    },
    {
      "alias": "SAVE",
      "events": {},
      "key": "ood.APICaller",
      "path": "dsm.view.config.menu.toolbar.ToolBarConfig.SAVE",
      "properties": {
        "allform": false,
        "autoRun": false,
        "checkRequired": false,
        "checkValid": false,
        "desc": "保存",
        "id": "/dsm/view/config/menu/toolbar/saveToolMenu",
        "imageClass": "ri-code-box-line",
        "isAllform": false,
        "methodName": "saveToolMenu",
        "name": "saveToolMenu",
        "proxyType": "auto",
        "queryAsync": true,
        "queryMethod": "POST",
        "queryURL": "/dsm/view/config/menu/toolbar/saveToolMenu",
        "requestDataSource": [
          {
            "name": "menuConfigView",
            "path": "",
            "type": "FORM"
          },
          {
            "name": "xpath",
            "path": "",
            "type": "FORM"
          },
          {
            "name": "sourceMethodName",
            "path": "",
            "type": "FORM"
          },
          {
            "name": "domainId",
            "path": "",
            "type": "FORM"
          },
          {
            "name": "projectName",
            "path": "",
            "type": "FORM"
          },
          {
            "name": "currentClassName",
            "path": "",
            "type": "FORM"
          },
          {
            "name": "menuConfigView",
            "path": "",
            "type": "FORM"
          },
          {
            "name": "xpath",
            "path": "",
            "type": "FORM"
          },
          {
            "name": "sourceMethodName",
            "path": "",
            "type": "FORM"
          },
          {
            "name": "domainId",
            "path": "",
            "type": "FORM"
          },
          {
            "name": "projectName",
            "path": "",
            "type": "FORM"
          },
          {
            "name": "currentClassName",
            "path": "",
            "type": "FORM"
          },
          {
            "name": "ToolBarConfigMain",
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
        "sourceClassName": "net.ooder.dsm.view.config.menu.toolbar.ToolBarConfigService",
        "tabindex": 2,
        "tips": "保存"
      }
    },
    {
      "alias": "RELOAD",
      "events": {},
      "key": "ood.APICaller",
      "path": "dsm.view.config.menu.toolbar.ToolBarConfig.RELOAD",
      "properties": {
        "allform": false,
        "autoRun": true,
        "checkRequired": false,
        "checkValid": false,
        "desc": "基础信息",
        "id": "/dsm/view/config/menu/toolbar/ToolBarConfig",
        "imageClass": "ri-code-box-line",
        "isAllform": false,
        "methodName": "getToolBarConfig",
        "name": "ToolBarConfig",
        "proxyType": "auto",
        "queryAsync": true,
        "queryMethod": "POST",
        "queryURL": "/dsm/view/config/menu/toolbar/ToolBarConfig",
        "requestDataSource": [
          {
            "name": "ToolBarConfigMain",
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
            "name": "ToolBarConfigMain",
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
        "sourceClassName": "net.ooder.dsm.view.config.menu.toolbar.ToolBarNav",
        "tabindex": 3,
        "tips": "基础信息"
      }
    }
  ],
  "className": "dsm.view.config.menu.toolbar.ToolBarConfig",
  "customFunctions": {},
  "desc": "基础信息",
  "dio": false,
  "events": {},
  "formulas": [],
  "key": "ood.Module",
  "moduleType": "EUClass",
  "moduleVar": {},
  "moduleViewType": "FORMCONFIG",
  "path": "dsm.view.config.menu.toolbar.ToolBarConfig",
  "properties": {
    "autoDestroy": true,
    "bindClass": [],
    "caption": "基础信息",
    "currComponentAlias": "ToolBarConfig",
    "dock": "fill",
    "title": "基础信息"
  },
  "saveUrl": "/dsm/view/config/menu/toolbar/saveToolMenu"
}