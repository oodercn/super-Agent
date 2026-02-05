{
  "alias": "RowMenuConfig",
  "children": [
    {
      "alias": "PAGECTX",
      "children": [
        {
          "alias": "currentClassName",
          "events": {},
          "key": "ood.UI.HiddenInput",
          "path": "dsm.view.config.tree.rowmenu.RowMenuConfig.PAGECTX.currentClassName",
          "properties": {
            "formField": true,
            "isPid": true,
            "name": "currentClassName",
            "pid": true,
            "tabindex": 0
          }
        },
        {
          "alias": "childViewId",
          "events": {},
          "key": "ood.UI.HiddenInput",
          "path": "dsm.view.config.tree.rowmenu.RowMenuConfig.PAGECTX.childViewId",
          "properties": {
            "formField": true,
            "isPid": true,
            "name": "childViewId",
            "pid": true,
            "tabindex": 1
          }
        },
        {
          "alias": "xpath",
          "events": {},
          "key": "ood.UI.HiddenInput",
          "path": "dsm.view.config.tree.rowmenu.RowMenuConfig.PAGECTX.xpath",
          "properties": {
            "formField": true,
            "isPid": true,
            "name": "xpath",
            "pid": true,
            "tabindex": 2
          }
        },
        {
          "alias": "sourceMethodName",
          "events": {},
          "key": "ood.UI.HiddenInput",
          "path": "dsm.view.config.tree.rowmenu.RowMenuConfig.PAGECTX.sourceMethodName",
          "properties": {
            "formField": true,
            "isPid": false,
            "name": "sourceMethodName",
            "pid": false,
            "tabindex": 3
          }
        },
        {
          "alias": "sourceClassName",
          "events": {},
          "key": "ood.UI.HiddenInput",
          "path": "dsm.view.config.tree.rowmenu.RowMenuConfig.PAGECTX.sourceClassName",
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
          "path": "dsm.view.config.tree.rowmenu.RowMenuConfig.PAGECTX.methodName",
          "properties": {
            "formField": true,
            "isPid": true,
            "name": "methodName",
            "pid": true,
            "tabindex": 5
          }
        },
        {
          "alias": "projectName",
          "events": {},
          "key": "ood.UI.HiddenInput",
          "path": "dsm.view.config.tree.rowmenu.RowMenuConfig.PAGECTX.projectName",
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
          "path": "dsm.view.config.tree.rowmenu.RowMenuConfig.PAGECTX.domainId",
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
          "path": "dsm.view.config.tree.rowmenu.RowMenuConfig.PAGECTX.projectVersionName",
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
          "alias": "soruceMethodName",
          "events": {},
          "key": "ood.UI.HiddenInput",
          "path": "dsm.view.config.tree.rowmenu.RowMenuConfig.PAGECTX.soruceMethodName",
          "properties": {
            "formField": true,
            "isPid": true,
            "name": "soruceMethodName",
            "pid": true,
            "tabindex": 9
          }
        },
        {
          "alias": "id",
          "events": {},
          "key": "ood.UI.HiddenInput",
          "path": "dsm.view.config.tree.rowmenu.RowMenuConfig.PAGECTX.id",
          "properties": {
            "formField": true,
            "isPid": true,
            "name": "id",
            "pid": true,
            "tabindex": 10
          }
        }
      ],
      "events": {},
      "key": "ood.UI.Block",
      "params": {},
      "path": "dsm.view.config.tree.rowmenu.RowMenuConfig.PAGECTX",
      "properties": {
        "borderType": "none",
        "tabindex": 0,
        "visibility": "hidden"
      }
    },
    {
      "alias": "RowMenuConfigMain",
      "children": [
        {
          "alias": "RowMenuConfig",
          "children": [
            {
              "alias": "tagCmdsAlign",
              "events": {},
              "key": "ood.UI.ComboInput",
              "path": "dsm.view.config.tree.rowmenu.RowMenuConfig.RowMenuConfigMain.RowMenuConfig.tagCmdsAlign",
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
              "path": "dsm.view.config.tree.rowmenu.RowMenuConfig.RowMenuConfigMain.RowMenuConfig.buttonType",
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
              "path": "dsm.view.config.tree.rowmenu.RowMenuConfig.RowMenuConfigMain.RowMenuConfig.caption",
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
              "path": "dsm.view.config.tree.rowmenu.RowMenuConfig.RowMenuConfigMain.RowMenuConfig.tips",
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
              "path": "dsm.view.config.tree.rowmenu.RowMenuConfig.RowMenuConfigMain.RowMenuConfig.disabled",
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
              "path": "dsm.view.config.tree.rowmenu.RowMenuConfig.RowMenuConfigMain.RowMenuConfig.dynLoad",
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
              "path": "dsm.view.config.tree.rowmenu.RowMenuConfig.RowMenuConfigMain.RowMenuConfig.showCaption",
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
              "path": "dsm.view.config.tree.rowmenu.RowMenuConfig.RowMenuConfigMain.RowMenuConfig.lazy",
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
              "path": "dsm.view.config.tree.rowmenu.RowMenuConfig.RowMenuConfigMain.RowMenuConfig.itemStyle",
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
          "path": "dsm.view.config.tree.rowmenu.RowMenuConfig.RowMenuConfigMain.RowMenuConfig",
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
            "id": "rowMenuConfig",
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
            "name": "GetRowMenuConfig",
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
      "path": "dsm.view.config.tree.rowmenu.RowMenuConfig.RowMenuConfigMain",
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
              "target": "dsm.view.config.tree.rowmenu.RowMenuConfig",
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
              "target": "dsm.view.config.tree.rowmenu.RowMenuConfig",
              "type": "page"
            }
          ]
        }
      },
      "key": "ood.APICaller",
      "path": "dsm.view.config.tree.rowmenu.RowMenuConfig.SAVE",
      "properties": {
        "allform": true,
        "autoRun": false,
        "checkRequired": false,
        "checkValid": false,
        "desc": "保存",
        "id": "/dsm/view/config/tree/rowmenu/saveRowMenu",
        "imageClass": "ri-code-box-line",
        "isAllform": true,
        "methodName": "saveRowMenu",
        "name": "saveRowMenu",
        "proxyType": "auto",
        "queryAsync": true,
        "queryMethod": "POST",
        "queryURL": "/dsm/view/config/tree/rowmenu/saveRowMenu",
        "requestDataSource": [
          {
            "name": "RowMenuConfigMain",
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
        "sourceClassName": "net.ooder.dsm.view.config.tree.rowcmd.menuclass.TreeRowMenuService",
        "tabindex": 2,
        "tips": "保存"
      }
    },
    {
      "alias": "RELOAD",
      "events": {},
      "key": "ood.APICaller",
      "path": "dsm.view.config.tree.rowmenu.RowMenuConfig.RELOAD",
      "properties": {
        "allform": false,
        "autoRun": true,
        "checkRequired": false,
        "checkValid": false,
        "desc": "基础信息",
        "id": "/dsm/view/config/tree/rowmenu/RowMenuConfig",
        "imageClass": "ri-code-box-line",
        "isAllform": false,
        "methodName": "getRowMenuConfig",
        "name": "RowMenuConfig",
        "proxyType": "auto",
        "queryAsync": true,
        "queryMethod": "POST",
        "queryURL": "/dsm/view/config/tree/rowmenu/RowMenuConfig",
        "requestDataSource": [
          {
            "name": "RowMenuConfigMain",
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
            "name": "RowMenuConfigMain",
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
        "sourceClassName": "net.ooder.dsm.view.config.tree.rowcmd.TreeRowMenuNav",
        "tabindex": 3,
        "tips": "基础信息"
      }
    }
  ],
  "className": "dsm.view.config.tree.rowmenu.RowMenuConfig",
  "customFunctions": {},
  "desc": "基础信息",
  "dio": false,
  "events": {},
  "formulas": [],
  "key": "ood.Module",
  "moduleType": "EUClass",
  "moduleVar": {},
  "moduleViewType": "FORMCONFIG",
  "path": "dsm.view.config.tree.rowmenu.RowMenuConfig",
  "properties": {
    "autoDestroy": true,
    "bindClass": [],
    "caption": "基础信息",
    "currComponentAlias": "RowMenuConfig",
    "dock": "fill",
    "title": "基础信息"
  },
  "saveUrl": "/dsm/view/config/tree/rowmenu/saveRowMenu"
}