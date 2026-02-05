{
  "alias": "AggMenuConfigView",
  "children": [
    {
      "alias": "PAGECTX",
      "children": [
        {
          "alias": "currentClassName",
          "events": {},
          "key": "ood.UI.HiddenInput",
          "path": "dsm.agg.menu.config.AggMenuConfigView.PAGECTX.currentClassName",
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
          "path": "dsm.agg.menu.config.AggMenuConfigView.PAGECTX.xpath",
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
          "path": "dsm.agg.menu.config.AggMenuConfigView.PAGECTX.sourceClassName",
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
          "path": "dsm.agg.menu.config.AggMenuConfigView.PAGECTX.projectName",
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
          "path": "dsm.agg.menu.config.AggMenuConfigView.PAGECTX.domainId",
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
          "path": "dsm.agg.menu.config.AggMenuConfigView.PAGECTX.projectVersionName",
          "properties": {
            "formField": true,
            "isPid": false,
            "name": "projectVersionName",
            "pid": false,
            "tabindex": 5,
            "value": "DSMdsmVVVERSION0"
          }
        },
        {
          "alias": "parentId",
          "events": {},
          "key": "ood.UI.HiddenInput",
          "path": "dsm.agg.menu.config.AggMenuConfigView.PAGECTX.parentId",
          "properties": {
            "formField": true,
            "isPid": true,
            "name": "parentId",
            "pid": true,
            "tabindex": 6
          }
        },
        {
          "alias": "id",
          "events": {},
          "key": "ood.UI.HiddenInput",
          "path": "dsm.agg.menu.config.AggMenuConfigView.PAGECTX.id",
          "properties": {
            "formField": true,
            "isPid": true,
            "name": "id",
            "pid": true,
            "tabindex": 7
          }
        },
        {
          "alias": "index",
          "events": {},
          "key": "ood.UI.HiddenInput",
          "path": "dsm.agg.menu.config.AggMenuConfigView.PAGECTX.index",
          "properties": {
            "formField": true,
            "isPid": false,
            "name": "index",
            "pid": false,
            "tabindex": 8
          }
        }
      ],
      "events": {},
      "key": "ood.UI.Block",
      "params": {},
      "path": "dsm.agg.menu.config.AggMenuConfigView.PAGECTX",
      "properties": {
        "borderType": "none",
        "tabindex": 0,
        "visibility": "hidden"
      }
    },
    {
      "alias": "AggMenuConfigViewMain",
      "children": [
        {
          "alias": "AggMenuConfigView",
          "children": [
            {
              "alias": "menuType",
              "apis": [],
              "events": {},
              "id": "menuType",
              "key": "ood.UI.List",
              "path": "dsm.agg.menu.config.AggMenuConfigView.AggMenuConfigViewMain.AggMenuConfigView.menuType",
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
                "id": "menuType",
                "items": [
                  {
                    "bindClass": [],
                    "caption": "工具栏",
                    "id": "TOOLBAR",
                    "imageClass": "ri-tools-line",
                    "name": "工具栏",
                    "tabindex": 0,
                    "tagVar": {
                      "name": "TOOLBAR",
                      "clazz": "net.ooder.esd.annotation.menu.CustomMenuType"
                    },
                    "title": "工具栏"
                  },
                  {
                    "bindClass": [],
                    "caption": "弹出菜单",
                    "id": "LISTMENU",
                    "imageClass": "ri-tools-line",
                    "name": "弹出菜单",
                    "tabindex": 1,
                    "tagVar": {
                      "name": "LISTMENU",
                      "clazz": "net.ooder.esd.annotation.menu.CustomMenuType"
                    },
                    "title": "弹出菜单"
                  },
                  {
                    "bindClass": [],
                    "caption": "下标栏",
                    "id": "BOTTOMBAR",
                    "imageClass": "ri-checkbox-blank-line",
                    "name": "下标栏",
                    "tabindex": 2,
                    "tagVar": {
                      "name": "BOTTOMBAR",
                      "clazz": "net.ooder.esd.annotation.menu.CustomMenuType"
                    },
                    "title": "下标栏"
                  },
                  {
                    "bindClass": [],
                    "caption": "菜单栏",
                    "id": "MENUBAR",
                    "imageClass": "ri-menu-line",
                    "name": "菜单栏",
                    "tabindex": 3,
                    "tagVar": {
                      "name": "MENUBAR",
                      "clazz": "net.ooder.esd.annotation.menu.CustomMenuType"
                    },
                    "title": "菜单栏"
                  },
                  {
                    "bindClass": [],
                    "caption": "行控制按钮",
                    "id": "ROWCMD",
                    "imageClass": "ri-checkbox-blank-line",
                    "name": "行控制按钮",
                    "tabindex": 4,
                    "tagVar": {
                      "name": "ROWCMD",
                      "clazz": "net.ooder.esd.annotation.menu.CustomMenuType"
                    },
                    "title": "行控制按钮"
                  },
                  {
                    "bindClass": [],
                    "caption": "右键菜单",
                    "id": "CONTEXTMENU",
                    "imageClass": "ri-menu-line",
                    "name": "右键菜单",
                    "tabindex": 5,
                    "tagVar": {
                      "name": "CONTEXTMENU",
                      "clazz": "net.ooder.esd.annotation.menu.CustomMenuType"
                    },
                    "title": "右键菜单"
                  },
                  {
                    "bindClass": [],
                    "caption": "子菜单",
                    "id": "SUB",
                    "imageClass": "ri-menu-line",
                    "name": "子菜单",
                    "tabindex": 6,
                    "tagVar": {
                      "name": "SUB",
                      "clazz": "net.ooder.esd.annotation.menu.CustomMenuType"
                    },
                    "title": "子菜单"
                  },
                  {
                    "bindClass": [],
                    "caption": "流程控制栏",
                    "id": "BPM",
                    "imageClass": "ri-node-tree",
                    "name": "流程控制栏",
                    "tabindex": 7,
                    "tagVar": {
                      "name": "BPM",
                      "clazz": "net.ooder.esd.annotation.menu.CustomMenuType"
                    },
                    "title": "流程控制栏"
                  },
                  {
                    "bindClass": [],
                    "caption": "流程控制栏",
                    "id": "BPMBOTTOM",
                    "imageClass": "ri-node-tree",
                    "name": "流程控制栏",
                    "tabindex": 8,
                    "tagVar": {
                      "name": "BPMBOTTOM",
                      "clazz": "net.ooder.esd.annotation.menu.CustomMenuType"
                    },
                    "title": "流程控制栏"
                  },
                  {
                    "bindClass": [],
                    "caption": "RAD通用组件",
                    "id": "COMPONENT",
                    "imageClass": "ri-menu-line",
                    "name": "RAD通用组件",
                    "tabindex": 9,
                    "tagVar": {
                      "name": "COMPONENT",
                      "clazz": "net.ooder.esd.annotation.menu.CustomMenuType"
                    },
                    "title": "RAD通用组件"
                  },
                  {
                    "bindClass": [],
                    "caption": "RAD顶部插件",
                    "id": "PLUGINS",
                    "imageClass": "ri-menu-line",
                    "name": "RAD顶部插件",
                    "tabindex": 10,
                    "tagVar": {
                      "name": "PLUGINS",
                      "clazz": "net.ooder.esd.annotation.menu.CustomMenuType"
                    },
                    "title": "RAD顶部插件"
                  },
                  {
                    "bindClass": [],
                    "caption": "RAD工具栏菜单",
                    "id": "TOP",
                    "imageClass": "ri-menu-line",
                    "name": "RAD工具栏菜单",
                    "tabindex": 11,
                    "tagVar": {
                      "name": "TOP",
                      "clazz": "net.ooder.esd.annotation.menu.CustomMenuType"
                    },
                    "title": "RAD工具栏菜单"
                  }
                ],
                "name": "menuType",
                "selMode": "single",
                "tabindex": 0,
                "visibility": "visible",
                "width": "auto"
              },
              "target": "B1"
            },
            {
              "alias": "caption",
              "events": {},
              "key": "ood.UI.Input",
              "path": "dsm.agg.menu.config.AggMenuConfigView.AggMenuConfigViewMain.AggMenuConfigView.caption",
              "properties": {
                "caption": "显示名称",
                "defaultFocus": false,
                "desc": "显示名称",
                "disableClickEffect": false,
                "disableHoverEffect": false,
                "disableTips": false,
                "disabled": false,
                "dock": "none",
                "events": {},
                "id": "caption",
                "imageClass": "",
                "items": [],
                "labelCaption": "显示名称",
                "labelPos": "left",
                "labelSize": "6.0em",
                "name": "caption",
                "readonly": false,
                "tabindex": 1,
                "tagVar": {},
                "visibility": "visible"
              },
              "target": "B2"
            },
            {
              "alias": "imageClass",
              "events": {},
              "key": "ood.UI.ComboInput",
              "path": "dsm.agg.menu.config.AggMenuConfigView.AggMenuConfigViewMain.AggMenuConfigView.imageClass",
              "properties": {
                "caption": "图标",
                "defaultFocus": false,
                "desc": "图标",
                "disableClickEffect": false,
                "disableHoverEffect": false,
                "disableTips": false,
                "disabled": false,
                "dock": "none",
                "id": "imageClass",
                "items": [],
                "labelCaption": "图标",
                "name": "imageClass",
                "readonly": false,
                "tabindex": 2,
                "type": "input",
                "visibility": "visible"
              },
              "target": "F2"
            },
            {
              "alias": "hAlign",
              "events": {},
              "key": "ood.UI.ComboInput",
              "path": "dsm.agg.menu.config.AggMenuConfigView.AggMenuConfigViewMain.AggMenuConfigView.hAlign",
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
                "tabindex": 3,
                "type": "combobox",
                "visibility": "visible"
              },
              "target": "B3"
            },
            {
              "alias": "vAlign",
              "events": {},
              "key": "ood.UI.ComboInput",
              "path": "dsm.agg.menu.config.AggMenuConfigView.AggMenuConfigViewMain.AggMenuConfigView.vAlign",
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
                "tabindex": 4,
                "type": "combobox",
                "visibility": "visible"
              },
              "target": "F3"
            },
            {
              "alias": "showCaption",
              "events": {},
              "key": "ood.UI.CheckBox",
              "path": "dsm.agg.menu.config.AggMenuConfigView.AggMenuConfigViewMain.AggMenuConfigView.showCaption",
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
                "tabindex": 5,
                "visibility": "visible"
              },
              "target": "B4"
            },
            {
              "alias": "handler",
              "events": {},
              "key": "ood.UI.CheckBox",
              "path": "dsm.agg.menu.config.AggMenuConfigView.AggMenuConfigViewMain.AggMenuConfigView.handler",
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
                "tabindex": 6,
                "visibility": "visible"
              },
              "target": "D4"
            },
            {
              "alias": "dynLoad",
              "events": {},
              "key": "ood.UI.CheckBox",
              "path": "dsm.agg.menu.config.AggMenuConfigView.AggMenuConfigViewMain.AggMenuConfigView.dynLoad",
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
                "tabindex": 7,
                "visibility": "visible"
              },
              "target": "F4"
            },
            {
              "alias": "lazy",
              "events": {},
              "key": "ood.UI.CheckBox",
              "path": "dsm.agg.menu.config.AggMenuConfigView.AggMenuConfigViewMain.AggMenuConfigView.lazy",
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
                "tabindex": 8,
                "visibility": "visible"
              },
              "target": "H4"
            },
            {
              "alias": "serviceClass",
              "events": {},
              "key": "ood.UI.Input",
              "path": "dsm.agg.menu.config.AggMenuConfigView.AggMenuConfigViewMain.AggMenuConfigView.serviceClass",
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
                "tabindex": 9,
                "tagVar": {},
                "visibility": "visible"
              },
              "target": "B5"
            }
          ],
          "events": {},
          "key": "ood.UI.FormLayout",
          "path": "dsm.agg.menu.config.AggMenuConfigView.AggMenuConfigViewMain.AggMenuConfigView",
          "properties": {
            "caption": "菜单配置",
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
            "id": "aggMenuConfigView",
            "imageClass": "ri-file-text-line",
            "layoutData": {
              "cells": {
                "A1": {
                  "cellName": "A1",
                  "style": {
                    "textAlign": "center"
                  },
                  "value": "菜单类型"
                },
                "A2": {
                  "cellName": "A2",
                  "style": {
                    "textAlign": "center"
                  },
                  "value": "显示名称"
                },
                "C4": {
                  "cellName": "C4",
                  "style": {
                    "textAlign": "center"
                  },
                  "value": "行头手柄"
                },
                "A3": {
                  "cellName": "A3",
                  "style": {
                    "textAlign": "center"
                  },
                  "value": "垂直对齐"
                },
                "A4": {
                  "cellName": "A4",
                  "style": {
                    "textAlign": "center"
                  },
                  "value": "显示标题"
                },
                "A5": {
                  "cellName": "A5",
                  "style": {
                    "textAlign": "center"
                  },
                  "value": "实现类"
                },
                "E2": {
                  "cellName": "E2",
                  "style": {
                    "textAlign": "center"
                  },
                  "value": "图标"
                },
                "G4": {
                  "cellName": "G4",
                  "style": {
                    "textAlign": "center"
                  },
                  "value": "延迟加载"
                },
                "E3": {
                  "cellName": "E3",
                  "style": {
                    "textAlign": "center"
                  },
                  "value": "横向对齐"
                },
                "E4": {
                  "cellName": "E4",
                  "style": {
                    "textAlign": "center"
                  },
                  "value": "动态加载"
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
                },
                "E": {
                  "manualWidth": 150
                },
                "F": {
                  "manualWidth": 150
                },
                "G": {
                  "manualWidth": 150
                },
                "H": {
                  "manualWidth": 150
                }
              },
              "cols": 8,
              "merged": [
                {
                  "col": 1,
                  "colspan": 7,
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
                  "col": 5,
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
                  "col": 5,
                  "colspan": 3,
                  "removed": false,
                  "row": 2,
                  "rowspan": 1
                },
                {
                  "col": 1,
                  "colspan": 7,
                  "removed": false,
                  "row": 4,
                  "rowspan": 1
                }
              ],
              "rowSetting": {
                "1": {
                  "manualHeight": 100
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
                }
              },
              "rows": 5
            },
            "lineSpacing": 10,
            "mode": "write",
            "name": "GetAggMenuConfigView",
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
                        "{page.RESET.invoke()}"
                      ],
                      "conditions": [
                        {
                          "symbol": "=",
                          "left": "{args[1].id}",
                          "right": "RESET_button"
                        }
                      ],
                      "desc": "重置",
                      "enumClassName": "net.ooder.esd.annotation.action.CustomFormAction",
                      "enumValue": "RESET",
                      "eventClass": "net.ooder.esd.annotation.event.FieldEventEnum",
                      "eventValue": "onClick",
                      "id": "net.ooder.esd.annotation.action.CustomFormAction_RESET_null_null",
                      "method": "invoke",
                      "redirection": "other:callback:call",
                      "return": false,
                      "target": "RESET",
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
              "path": "dsm.agg.menu.config.AggMenuConfigView.AggMenuConfigViewMain.SAVEBottomBlock.SAVEBottom",
              "properties": {
                "borderType": "none",
                "dock": "fill",
                "itemType": "button",
                "itemWidth": "auto",
                "items": [
                  {
                    "caption": "重置",
                    "iconColor": "#E6945C",
                    "id": "RESET_button",
                    "imageClass": "ri-refresh-line",
                    "index": 0,
                    "itemType": "button",
                    "tabindex": 0,
                    "tagVar": {
                      "name": "RESET",
                      "clazz": "net.ooder.esd.annotation.menu.CustomFormMenu"
                    },
                    "tips": "重置",
                    "title": "重置"
                  },
                  {
                    "caption": "保存",
                    "iconColor": "#195ead",
                    "id": "SAVE_button",
                    "imageClass": "ri-save-line",
                    "index": 0,
                    "itemType": "button",
                    "tabindex": 1,
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
                "tabindex": 0,
                "width": "auto"
              }
            }
          ],
          "events": {},
          "key": "ood.UI.Block",
          "path": "dsm.agg.menu.config.AggMenuConfigView.AggMenuConfigViewMain.SAVEBottomBlock",
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
      "events": {},
      "key": "ood.UI.Block",
      "path": "dsm.agg.menu.config.AggMenuConfigView.AggMenuConfigViewMain",
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
      "path": "dsm.agg.menu.config.AggMenuConfigView.SAVE",
      "properties": {
        "allform": true,
        "autoRun": false,
        "checkRequired": false,
        "checkValid": false,
        "desc": "保存",
        "id": "/dsm/agg/menu/config/method/group/saveWebMenu",
        "imageClass": "ri-code-box-line",
        "isAllform": true,
        "methodName": "saveAggMenu",
        "name": "saveWebMenu",
        "proxyType": "auto",
        "queryAsync": true,
        "queryMethod": "POST",
        "queryURL": "/dsm/agg/menu/config/method/group/saveWebMenu",
        "requestDataSource": [
          {
            "name": "menuBarBean",
            "path": "",
            "type": "FORM"
          },
          {
            "name": "currentClassName",
            "path": "",
            "type": "FORM"
          },
          {
            "name": "xpath",
            "path": "",
            "type": "FORM"
          },
          {
            "name": "menuBarBean",
            "path": "",
            "type": "FORM"
          },
          {
            "name": "currentClassName",
            "path": "",
            "type": "FORM"
          },
          {
            "name": "xpath",
            "path": "",
            "type": "FORM"
          },
          {
            "name": "AggMenuConfigViewMain",
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
        "sourceClassName": "net.ooder.dsm.aggregation.config.menu.tree.AggMenuConfigNavService",
        "tabindex": 2,
        "tips": "保存"
      }
    },
    {
      "alias": "RELOAD",
      "events": {},
      "key": "ood.APICaller",
      "path": "dsm.agg.menu.config.AggMenuConfigView.RELOAD",
      "properties": {
        "allform": false,
        "autoRun": true,
        "checkRequired": false,
        "checkValid": false,
        "desc": "菜单配置",
        "id": "/dsm/agg/menu/config/AggMenuConfigView",
        "imageClass": "ri-code-box-line",
        "isAllform": false,
        "methodName": "getAggMenuConfigView",
        "name": "AggMenuConfigView",
        "proxyType": "auto",
        "queryAsync": true,
        "queryMethod": "POST",
        "queryURL": "/dsm/agg/menu/config/AggMenuConfigView",
        "requestDataSource": [
          {
            "name": "AggMenuConfigViewMain",
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
            "name": "AggMenuConfigViewMain",
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
        "tips": "菜单配置"
      }
    }
  ],
  "className": "dsm.agg.menu.config.AggMenuConfigView",
  "customFunctions": {},
  "desc": "菜单配置",
  "dio": false,
  "events": {},
  "formulas": [],
  "key": "ood.Module",
  "moduleType": "EUClass",
  "moduleVar": {},
  "moduleViewType": "FORMCONFIG",
  "path": "dsm.agg.menu.config.AggMenuConfigView",
  "properties": {
    "autoDestroy": true,
    "bindClass": [],
    "caption": "菜单配置",
    "currComponentAlias": "AggMenuConfigView",
    "dock": "fill",
    "title": "菜单配置"
  },
  "saveUrl": "/dsm/agg/menu/config/method/group/saveWebMenu"
}