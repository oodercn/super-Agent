{
  "alias": "GridMenuConfig",
  "children": [
    {
      "alias": "PAGECTX",
      "children": [
        {
          "alias": "currentClassName",
          "events": {},
          "key": "ood.UI.HiddenInput",
          "path": "dsm.view.config.grid.rowmenu.GridMenuConfig.PAGECTX.currentClassName",
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
          "path": "dsm.view.config.grid.rowmenu.GridMenuConfig.PAGECTX.xpath",
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
          "path": "dsm.view.config.grid.rowmenu.GridMenuConfig.PAGECTX.sourceMethodName",
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
          "path": "dsm.view.config.grid.rowmenu.GridMenuConfig.PAGECTX.sourceClassName",
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
          "path": "dsm.view.config.grid.rowmenu.GridMenuConfig.PAGECTX.projectName",
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
          "path": "dsm.view.config.grid.rowmenu.GridMenuConfig.PAGECTX.domainId",
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
          "path": "dsm.view.config.grid.rowmenu.GridMenuConfig.PAGECTX.projectVersionName",
          "properties": {
            "formField": true,
            "isPid": false,
            "name": "projectVersionName",
            "pid": false,
            "tabindex": 6,
            "value": "DSMdsmVVVERSION0"
          }
        },
        {
          "alias": "id",
          "events": {},
          "key": "ood.UI.HiddenInput",
          "path": "dsm.view.config.grid.rowmenu.GridMenuConfig.PAGECTX.id",
          "properties": {
            "formField": true,
            "isPid": true,
            "name": "id",
            "pid": true,
            "tabindex": 7
          }
        }
      ],
      "events": {},
      "key": "ood.UI.Block",
      "params": {},
      "path": "dsm.view.config.grid.rowmenu.GridMenuConfig.PAGECTX",
      "properties": {
        "borderType": "none",
        "tabindex": 0,
        "visibility": "hidden"
      }
    },
    {
      "alias": "GridMenuConfigMain",
      "children": [
        {
          "alias": "GridMenuConfig",
          "children": [
            {
              "alias": "tagCmdsAlign",
              "events": {},
              "key": "ood.UI.ComboInput",
              "path": "dsm.view.config.grid.rowmenu.GridMenuConfig.GridMenuConfigMain.GridMenuConfig.tagCmdsAlign",
              "properties": {
                "caption": "按钮位置",
                "defaultFocus": false,
                "desc": "按钮位置",
                "disableClickEffect": false,
                "disableHoverEffect": false,
                "disableTips": false,
                "disabled": false,
                "dock": "none",
                "dynLoad": true,
                "enumClass": "net.ooder.esd.annotation.ui.TagCmdsAlign",
                "items": [
                  {
                    "bindClass": [],
                    "caption": "left",
                    "id": "left",
                    "tabindex": 0,
                    "tagVar": {
                      "name": "left",
                      "clazz": "net.ooder.esd.annotation.ui.TagCmdsAlign"
                    },
                    "title": "left"
                  },
                  {
                    "bindClass": [],
                    "caption": "right",
                    "id": "right",
                    "tabindex": 1,
                    "tagVar": {
                      "name": "right",
                      "clazz": "net.ooder.esd.annotation.ui.TagCmdsAlign"
                    },
                    "title": "right"
                  },
                  {
                    "bindClass": [],
                    "caption": "floatright",
                    "id": "floatright",
                    "tabindex": 2,
                    "tagVar": {
                      "name": "floatright",
                      "clazz": "net.ooder.esd.annotation.ui.TagCmdsAlign"
                    },
                    "title": "floatright"
                  }
                ],
                "labelCaption": "按钮位置",
                "name": "tagCmdsAlign",
                "readonly": false,
                "tabindex": 0,
                "type": "combobox",
                "visibility": "visible"
              },
              "target": "B1"
            },
            {
              "alias": "buttonType",
              "events": {},
              "key": "ood.UI.ComboInput",
              "path": "dsm.view.config.grid.rowmenu.GridMenuConfig.GridMenuConfigMain.GridMenuConfig.buttonType",
              "properties": {
                "defaultFocus": false,
                "desc": "按钮类型",
                "disableClickEffect": false,
                "disableHoverEffect": false,
                "disableTips": false,
                "disabled": false,
                "dock": "none",
                "dynLoad": true,
                "enumClass": "net.ooder.esd.annotation.ui.CmdButtonType",
                "items": [
                  {
                    "bindClass": [],
                    "caption": "按钮",
                    "id": "button",
                    "imageClass": "ri-checkbox-blank-line",
                    "name": "按钮",
                    "tabindex": 0,
                    "tagVar": {
                      "name": "button",
                      "clazz": "net.ooder.esd.annotation.ui.CmdButtonType"
                    },
                    "title": "按钮",
                    "type": "button"
                  },
                  {
                    "bindClass": [],
                    "caption": "图片",
                    "id": "image",
                    "imageClass": "ri-image-line",
                    "name": "图片",
                    "tabindex": 1,
                    "tagVar": {
                      "name": "image",
                      "clazz": "net.ooder.esd.annotation.ui.CmdButtonType"
                    },
                    "title": "图片",
                    "type": "image"
                  },
                  {
                    "bindClass": [],
                    "caption": "文本",
                    "id": "text",
                    "imageClass": "ri-font-size",
                    "name": "文本",
                    "tabindex": 2,
                    "tagVar": {
                      "name": "text",
                      "clazz": "net.ooder.esd.annotation.ui.CmdButtonType"
                    },
                    "title": "文本",
                    "type": "text"
                  },
                  {
                    "bindClass": [],
                    "caption": "分隔符",
                    "id": "split",
                    "imageClass": "ri-separator",
                    "name": "分隔符",
                    "tabindex": 3,
                    "tagVar": {
                      "name": "split",
                      "clazz": "net.ooder.esd.annotation.ui.CmdButtonType"
                    },
                    "title": "分隔符",
                    "type": "split"
                  }
                ],
                "labelCaption": "按钮类型",
                "name": "buttonType",
                "readonly": false,
                "tabindex": 1,
                "type": "listbox",
                "visibility": "visible"
              },
              "target": "D1"
            },
            {
              "alias": "caption",
              "events": {},
              "key": "ood.UI.Input",
              "path": "dsm.view.config.grid.rowmenu.GridMenuConfig.GridMenuConfigMain.GridMenuConfig.caption",
              "properties": {
                "caption": "标题",
                "defaultFocus": false,
                "desc": "标题",
                "disableClickEffect": false,
                "disableHoverEffect": false,
                "disableTips": false,
                "disabled": false,
                "dock": "none",
                "events": {},
                "id": "caption",
                "imageClass": "",
                "items": [],
                "labelCaption": "标题",
                "labelPos": "left",
                "labelSize": "6.0em",
                "name": "caption",
                "readonly": false,
                "tabindex": 2,
                "tagVar": {},
                "visibility": "visible"
              },
              "target": "B2"
            },
            {
              "alias": "tips",
              "events": {},
              "key": "ood.UI.Input",
              "path": "dsm.view.config.grid.rowmenu.GridMenuConfig.GridMenuConfigMain.GridMenuConfig.tips",
              "properties": {
                "caption": "提示",
                "defaultFocus": false,
                "desc": "提示",
                "disableClickEffect": false,
                "disableHoverEffect": false,
                "disableTips": false,
                "disabled": false,
                "dock": "none",
                "events": {},
                "id": "tips",
                "imageClass": "",
                "items": [],
                "labelCaption": "提示",
                "labelPos": "left",
                "labelSize": "6.0em",
                "name": "tips",
                "readonly": false,
                "tabindex": 3,
                "tagVar": {},
                "visibility": "visible"
              },
              "target": "D2"
            },
            {
              "alias": "disabled",
              "events": {},
              "key": "ood.UI.CheckBox",
              "path": "dsm.view.config.grid.rowmenu.GridMenuConfig.GridMenuConfigMain.GridMenuConfig.disabled",
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
                "tabindex": 4,
                "visibility": "visible"
              },
              "target": "B3"
            },
            {
              "alias": "dynLoad",
              "events": {},
              "key": "ood.UI.CheckBox",
              "path": "dsm.view.config.grid.rowmenu.GridMenuConfig.GridMenuConfigMain.GridMenuConfig.dynLoad",
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
                "tabindex": 5,
                "visibility": "visible"
              },
              "target": "D3"
            },
            {
              "alias": "showCaption",
              "events": {},
              "key": "ood.UI.CheckBox",
              "path": "dsm.view.config.grid.rowmenu.GridMenuConfig.GridMenuConfigMain.GridMenuConfig.showCaption",
              "properties": {
                "caption": "",
                "defaultFocus": false,
                "desc": "是否显示标题",
                "disableClickEffect": false,
                "disableHoverEffect": false,
                "disableTips": false,
                "dock": "none",
                "events": {},
                "id": "showCaption",
                "name": "showCaption",
                "tabindex": 6,
                "visibility": "visible"
              },
              "target": "B4"
            },
            {
              "alias": "lazy",
              "events": {},
              "key": "ood.UI.CheckBox",
              "path": "dsm.view.config.grid.rowmenu.GridMenuConfig.GridMenuConfigMain.GridMenuConfig.lazy",
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
                "tabindex": 7,
                "visibility": "visible"
              },
              "target": "D4"
            },
            {
              "alias": "itemStyle",
              "events": {},
              "key": "ood.UI.Input",
              "path": "dsm.view.config.grid.rowmenu.GridMenuConfig.GridMenuConfigMain.GridMenuConfig.itemStyle",
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
                "tabindex": 8,
                "tagVar": {},
                "visibility": "visible"
              },
              "target": "B5"
            }
          ],
          "events": {},
          "key": "ood.UI.FormLayout",
          "path": "dsm.view.config.grid.rowmenu.GridMenuConfig.GridMenuConfigMain.GridMenuConfig",
          "properties": {
            "caption": "行菜单信息",
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
            "id": "gridMenuConfig",
            "imageClass": "ri-file-text-line",
            "layoutData": {
              "cells": {
                "A1": {
                  "cellName": "A1",
                  "style": {
                    "textAlign": "center"
                  },
                  "value": "按钮位置"
                },
                "C3": {
                  "cellName": "C3",
                  "style": {
                    "textAlign": "center"
                  },
                  "value": "动态加载"
                },
                "A2": {
                  "cellName": "A2",
                  "style": {
                    "textAlign": "center"
                  },
                  "value": "标题"
                },
                "C4": {
                  "cellName": "C4",
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
                  "value": "禁用"
                },
                "A4": {
                  "cellName": "A4",
                  "style": {
                    "textAlign": "center"
                  },
                  "value": "是否显示标题"
                },
                "A5": {
                  "cellName": "A5",
                  "style": {
                    "textAlign": "center"
                  },
                  "value": "菜单项Style"
                },
                "C1": {
                  "cellName": "C1",
                  "style": {
                    "textAlign": "center"
                  },
                  "value": "按钮类型"
                },
                "C2": {
                  "cellName": "C2",
                  "style": {
                    "textAlign": "center"
                  },
                  "value": "提示"
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
                  "row": 4,
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
                  "manualHeight": 50
                }
              },
              "rows": 5
            },
            "lineSpacing": 10,
            "mode": "write",
            "name": "GetGridMenuConfig",
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
      "path": "dsm.view.config.grid.rowmenu.GridMenuConfig.GridMenuConfigMain",
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
              "target": "dsm.view.config.grid.rowmenu.GridMenuConfig",
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
              "target": "dsm.view.config.grid.rowmenu.GridMenuConfig",
              "type": "page"
            }
          ]
        }
      },
      "key": "ood.APICaller",
      "path": "dsm.view.config.grid.rowmenu.GridMenuConfig.SAVE",
      "properties": {
        "allform": true,
        "autoRun": false,
        "checkRequired": false,
        "checkValid": false,
        "desc": "保存",
        "id": "/dsm/view/config/grid/rowmenu/saveRowClassMenu",
        "imageClass": "ri-code-box-line",
        "isAllform": true,
        "methodName": "saveRowClassMenu",
        "name": "saveRowClassMenu",
        "proxyType": "auto",
        "queryAsync": true,
        "queryMethod": "POST",
        "queryURL": "/dsm/view/config/grid/rowmenu/saveRowClassMenu",
        "requestDataSource": [
          {
            "name": "GridMenuConfigMain",
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
        "sourceClassName": "net.ooder.dsm.view.config.grid.row.rowcmd.menuclass.GridRowMenuService",
        "tabindex": 2,
        "tips": "保存"
      }
    },
    {
      "alias": "RELOAD",
      "events": {},
      "key": "ood.APICaller",
      "path": "dsm.view.config.grid.rowmenu.GridMenuConfig.RELOAD",
      "properties": {
        "allform": false,
        "autoRun": true,
        "checkRequired": false,
        "checkValid": false,
        "desc": "行菜单信息",
        "id": "/dsm/view/config/grid/rowmenu/GridMenuConfig",
        "imageClass": "ri-code-box-line",
        "isAllform": false,
        "methodName": "getGridMenuConfig",
        "name": "GridMenuConfig",
        "proxyType": "auto",
        "queryAsync": true,
        "queryMethod": "POST",
        "queryURL": "/dsm/view/config/grid/rowmenu/GridMenuConfig",
        "requestDataSource": [
          {
            "name": "GridMenuConfigMain",
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
            "name": "GridMenuConfigMain",
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
        "sourceClassName": "net.ooder.dsm.view.config.grid.row.rowcmd.GridRowMenuNav",
        "tabindex": 3,
        "tips": "行菜单信息"
      }
    }
  ],
  "className": "dsm.view.config.grid.rowmenu.GridMenuConfig",
  "customFunctions": {},
  "desc": "行菜单信息",
  "dio": false,
  "events": {},
  "formulas": [],
  "key": "ood.Module",
  "moduleType": "EUClass",
  "moduleVar": {},
  "moduleViewType": "FORMCONFIG",
  "path": "dsm.view.config.grid.rowmenu.GridMenuConfig",
  "properties": {
    "autoDestroy": true,
    "bindClass": [],
    "caption": "行菜单信息",
    "currComponentAlias": "GridMenuConfig",
    "dock": "fill",
    "title": "行菜单信息"
  },
  "saveUrl": "/dsm/view/config/grid/rowmenu/saveRowClassMenu"
}