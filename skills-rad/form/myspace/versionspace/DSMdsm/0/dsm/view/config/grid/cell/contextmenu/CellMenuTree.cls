{
  "alias": "CellMenuTree",
  "children": [
    {
      "alias": "PAGECTX",
      "children": [
        {
          "alias": "fieldname",
          "events": {},
          "key": "ood.UI.HiddenInput",
          "path": "dsm.view.config.grid.cell.contextmenu.CellMenuTree.PAGECTX.fieldname",
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
          "path": "dsm.view.config.grid.cell.contextmenu.CellMenuTree.PAGECTX.currentClassName",
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
          "path": "dsm.view.config.grid.cell.contextmenu.CellMenuTree.PAGECTX.xpath",
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
          "path": "dsm.view.config.grid.cell.contextmenu.CellMenuTree.PAGECTX.sourceMethodName",
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
          "path": "dsm.view.config.grid.cell.contextmenu.CellMenuTree.PAGECTX.sourceClassName",
          "properties": {
            "formField": true,
            "isPid": true,
            "name": "sourceClassName",
            "pid": true,
            "tabindex": 4
          }
        },
        {
          "alias": "projectName",
          "events": {},
          "key": "ood.UI.HiddenInput",
          "path": "dsm.view.config.grid.cell.contextmenu.CellMenuTree.PAGECTX.projectName",
          "properties": {
            "formField": true,
            "isPid": false,
            "name": "projectName",
            "pid": false,
            "tabindex": 5,
            "value": "DSMdsmVVVERSION0"
          }
        },
        {
          "alias": "projectVersionName",
          "events": {},
          "key": "ood.UI.HiddenInput",
          "path": "dsm.view.config.grid.cell.contextmenu.CellMenuTree.PAGECTX.projectVersionName",
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
          "alias": "menuType",
          "events": {},
          "key": "ood.UI.HiddenInput",
          "path": "dsm.view.config.grid.cell.contextmenu.CellMenuTree.PAGECTX.menuType",
          "properties": {
            "formField": true,
            "isPid": true,
            "name": "menuType",
            "pid": true,
            "tabindex": 7
          }
        },
        {
          "alias": "domainId",
          "events": {},
          "key": "ood.UI.HiddenInput",
          "path": "dsm.view.config.grid.cell.contextmenu.CellMenuTree.PAGECTX.domainId",
          "properties": {
            "formField": true,
            "isPid": true,
            "name": "domainId",
            "pid": true,
            "tabindex": 8
          }
        },
        {
          "alias": "euClassName",
          "events": {},
          "key": "ood.UI.HiddenInput",
          "path": "dsm.view.config.grid.cell.contextmenu.CellMenuTree.PAGECTX.euClassName",
          "properties": {
            "formField": true,
            "isPid": true,
            "name": "euClassName",
            "pid": true,
            "tabindex": 9
          }
        },
        {
          "alias": "fieldName",
          "events": {},
          "key": "ood.UI.HiddenInput",
          "path": "dsm.view.config.grid.cell.contextmenu.CellMenuTree.PAGECTX.fieldName",
          "properties": {
            "formField": true,
            "isPid": false,
            "name": "fieldName",
            "pid": false,
            "tabindex": 10
          }
        },
        {
          "alias": "fieldCaption",
          "events": {},
          "key": "ood.UI.HiddenInput",
          "path": "dsm.view.config.grid.cell.contextmenu.CellMenuTree.PAGECTX.fieldCaption",
          "properties": {
            "formField": true,
            "isPid": false,
            "name": "fieldCaption",
            "pid": false,
            "tabindex": 11
          }
        }
      ],
      "events": {},
      "key": "ood.UI.Block",
      "params": {},
      "path": "dsm.view.config.grid.cell.contextmenu.CellMenuTree.PAGECTX",
      "properties": {
        "borderType": "none",
        "tabindex": 0,
        "visibility": "hidden"
      }
    },
    {
      "alias": "CellMenuTreeMain",
      "children": [
        {
          "alias": "CellMenuTree",
          "events": {},
          "key": "ood.UI.TreeView",
          "path": "dsm.view.config.grid.cell.contextmenu.CellMenuTree.CellMenuTreeMain.CellMenuTree",
          "properties": {
            "caption": "AllCellContextMenuTree",
            "defaultFocus": false,
            "disableClickEffect": false,
            "disableHoverEffect": false,
            "disableTips": false,
            "disabled": false,
            "dock": "none",
            "formField": true,
            "initFold": false,
            "items": [
              {
                "bindClass": [
                  "java.lang.Void"
                ],
                "caption": "AllCellContextMenuTree",
                "id": "cellMenuTree",
                "imageClass": "ri-tree-line",
                "initFold": false,
                "sub": [
                  {
                    "bindClass": [
                      "net.ooder.esd.custom.ESDClass"
                    ],
                    "bindClassName": "net.ooder.esd.custom.ESDClass",
                    "caption": "导入菜单动作（ESDClass)",
                    "closeBtn": false,
                    "dynDestory": false,
                    "dynLoad": false,
                    "entityClass": "net.ooder.esd.custom.ESDClass",
                    "id": "null",
                    "imageClass": "ri-grid-line",
                    "tagVar": {
                      "name": "null"
                    },
                    "title": "导入菜单动作（ESDClass)"
                  },
                  {
                    "bindClass": [
                      "net.ooder.dsm.aggregation.config.menu.pop.AggMenuTreeService"
                    ],
                    "bindClassName": "net.ooder.dsm.aggregation.config.menu.pop.AggMenuTreeService",
                    "caption": "导入菜单动作（null)",
                    "closeBtn": false,
                    "dynDestory": true,
                    "dynLoad": false,
                    "entityClass": "net.ooder.esd.annotation.menu.CustomMenuType",
                    "groupName": "dsm_agg_menu_config_LoadChildMenu",
                    "id": "dsm_agg_menu_config_LoadChildMenu",
                    "imageClass": "ri-node-tree",
                    "tagVar": {
                      "name": "loadChildMenu"
                    },
                    "title": "导入菜单动作（null)"
                  },
                  {
                    "bindClass": [
                      "java.lang.Void"
                    ],
                    "caption": "loadChildMenu",
                    "groupName": "dsm_agg_menu_config_LoadChildMenu",
                    "id": "loadChildMenu",
                    "imageClass": "ri-tree-line",
                    "initFold": false,
                    "sub": [
                      {
                        "bindClass": [
                          "net.ooder.esd.custom.ESDClass"
                        ],
                        "bindClassName": "net.ooder.esd.custom.ESDClass",
                        "caption": "loadChildMenu（ESDClass)",
                        "closeBtn": false,
                        "dynDestory": false,
                        "dynLoad": false,
                        "entityClass": "net.ooder.esd.custom.ESDClass",
                        "id": "loadChildMenu_null",
                        "imageClass": "ri-table-line",
                        "tagVar": {
                          "name": "null"
                        },
                        "title": "loadChildMenu（ESDClass)"
                      },
                      {
                        "bindClass": [
                          "net.ooder.dsm.aggregation.config.menu.pop.AggMenuTreeService"
                        ],
                        "bindClassName": "net.ooder.dsm.aggregation.config.menu.pop.AggMenuTreeService",
                        "caption": "loadChildMenu（null)",
                        "closeBtn": false,
                        "dynDestory": true,
                        "dynLoad": false,
                        "entityClass": "net.ooder.esd.annotation.menu.CustomMenuType",
                        "groupName": "dsm_agg_menu_config_LoadChildMenu",
                        "id": "loadChildMenu_dsm_agg_menu_config_LoadChildMenu",
                        "imageClass": "ri-node-tree",
                        "tagVar": {
                          "name": "loadChildMenu"
                        },
                        "title": "loadChildMenu（null)"
                      }
                    ],
                    "title": "loadChildMenu"
                  }
                ],
                "tabindex": 0,
                "title": "AllCellContextMenuTree"
              }
            ],
            "selMode": "multibycheckbox",
            "singleOpen": false,
            "tabindex": 0,
            "visibility": "visible"
          }
        },
        {
          "alias": "StatusBottomBlock",
          "children": [
            {
              "alias": "StatusBottom",
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
                      "enumClassName": "net.ooder.esd.annotation.action.CustomTreeAction",
                      "enumValue": "SAVE",
                      "eventClass": "net.ooder.esd.annotation.event.FieldEventEnum",
                      "eventValue": "onClick",
                      "id": "net.ooder.esd.annotation.action.CustomTreeAction_SAVE_null_null",
                      "method": "invoke",
                      "redirection": "other:callback:call",
                      "return": true,
                      "target": "SAVE",
                      "type": "control"
                    },
                    {
                      "args": [],
                      "conditions": [
                        {
                          "symbol": "=",
                          "left": "{args[1].id}",
                          "right": "CLOSE_button"
                        }
                      ],
                      "desc": "关闭页面",
                      "enumClassName": "net.ooder.esd.annotation.action.CustomPageAction",
                      "enumValue": "CLOSE",
                      "eventClass": "net.ooder.esd.annotation.event.FieldEventEnum",
                      "eventValue": "onClick",
                      "id": "net.ooder.esd.annotation.action.CustomPageAction_CLOSE_null_null",
                      "method": "destroy",
                      "redirection": "page::",
                      "return": true,
                      "target": "dsm.view.config.grid.cell.contextmenu.CellMenuTree",
                      "type": "page"
                    }
                  ]
                }
              },
              "key": "ood.UI.StatusButtons",
              "menuType": "BOTTOMBAR",
              "path": "dsm.view.config.grid.cell.contextmenu.CellMenuTree.CellMenuTreeMain.StatusBottomBlock.StatusBottom",
              "properties": {
                "borderType": "none",
                "dock": "fill",
                "itemType": "button",
                "itemWidth": "auto",
                "items": [
                  {
                    "caption": "确定",
                    "expression": "true",
                    "iconColor": "#195ead",
                    "id": "SAVE_button",
                    "imageClass": "ri-save-line",
                    "index": 0,
                    "itemType": "button",
                    "tabindex": 0,
                    "tagVar": {
                      "name": "SAVE",
                      "clazz": "net.ooder.esd.annotation.menu.TreeMenu"
                    },
                    "tips": "确定",
                    "title": "确定"
                  },
                  {
                    "caption": "关闭",
                    "expression": "true",
                    "iconColor": "#1f8d9b",
                    "id": "CLOSE_button",
                    "imageClass": "ri-close-line",
                    "index": 0,
                    "itemType": "button",
                    "tabindex": 1,
                    "tagVar": {
                      "name": "CLOSE",
                      "clazz": "net.ooder.esd.annotation.menu.TreeMenu"
                    },
                    "tips": "关闭",
                    "title": "关闭"
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
          "path": "dsm.view.config.grid.cell.contextmenu.CellMenuTree.CellMenuTreeMain.StatusBottomBlock",
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
              "target": "dsm.view.config.grid.cell.contextmenu.CellMenuTree",
              "type": "page"
            }
          ]
        }
      },
      "key": "ood.UI.Dialog",
      "path": "dsm.view.config.grid.cell.contextmenu.CellMenuTree.CellMenuTreeMain",
      "properties": {
        "caption": "导入菜单动作",
        "defaultFocus": false,
        "disableClickEffect": false,
        "disableHoverEffect": false,
        "disableTips": false,
        "disabled": false,
        "dock": "none",
        "height": "36.0em",
        "left": "220.0",
        "showEffects": "Classic",
        "tabindex": 1,
        "top": "100.0",
        "visibility": "visible",
        "width": "400.0"
      }
    },
    {
      "alias": "RELOAD",
      "events": {},
      "key": "ood.APICaller",
      "path": "dsm.view.config.grid.cell.contextmenu.CellMenuTree.RELOAD",
      "properties": {
        "allform": true,
        "autoRun": true,
        "checkRequired": false,
        "checkValid": false,
        "desc": "导入菜单动作",
        "id": "/dsm/view/config/grid/cell/contextmenu/CellMenuTree",
        "imageClass": "ri-code-box-line",
        "isAllform": true,
        "methodName": "getCellMenuTree",
        "name": "CellMenuTree",
        "proxyType": "auto",
        "queryAsync": true,
        "queryMethod": "POST",
        "queryURL": "/dsm/view/config/grid/cell/contextmenu/CellMenuTree",
        "requestDataSource": [
          {
            "name": "PAGECTX",
            "path": "",
            "type": "FORM"
          },
          {
            "name": "CellMenuTreeMain",
            "path": "",
            "type": "FORM"
          }
        ],
        "requestType": "FORM",
        "responseCallback": [],
        "responseDataTarget": [
          {
            "name": "CellMenuTree",
            "path": "data",
            "type": "TREEVIEW"
          },
          {
            "name": "PAGECTX",
            "path": "data",
            "type": "FORM"
          }
        ],
        "responseType": "JSON",
        "sourceClassName": "net.ooder.dsm.view.config.grid.cell.contextmenu.menuclass.ContextCellMenuService",
        "tabindex": 2,
        "tips": "导入菜单动作"
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
              "target": "dsm.view.config.grid.cell.contextmenu.CellMenuTree",
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
              "target": "dsm.view.config.grid.cell.contextmenu.CellMenuTree",
              "type": "page"
            }
          ]
        }
      },
      "key": "ood.APICaller",
      "path": "dsm.view.config.grid.cell.contextmenu.CellMenuTree.SAVE",
      "properties": {
        "allform": true,
        "autoRun": false,
        "checkRequired": false,
        "checkValid": false,
        "desc": "保存",
        "id": "/dsm/view/config/grid/cell/contextmenu/saveCellMenuClass",
        "imageClass": "ri-code-box-line",
        "isAllform": true,
        "methodName": "saveCellMenuClass",
        "name": "saveCellMenuClass",
        "proxyType": "auto",
        "queryAsync": true,
        "queryMethod": "POST",
        "queryURL": "/dsm/view/config/grid/cell/contextmenu/saveCellMenuClass",
        "requestDataSource": [
          {
            "name": "CellMenuTree",
            "path": "",
            "type": "FORM"
          },
          {
            "name": "CellMenuTreeMain",
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
        "sourceClassName": "net.ooder.dsm.view.config.grid.cell.contextmenu.menuclass.ContextCellMenuService",
        "tabindex": 3,
        "tips": "保存"
      }
    }
  ],
  "className": "dsm.view.config.grid.cell.contextmenu.CellMenuTree",
  "customFunctions": {},
  "desc": "导入菜单动作",
  "dio": true,
  "editorPath": "/dsm/view/config/grid/cell/contextmenu/AggConfigTree",
  "events": {
    "onDestroy": {
      "actions": [
        {
          "args": [
            "{page.getParentModule().setData()}",
            null,
            null,
            "{page.CellMenuTree.getCallBackValue()}"
          ],
          "conditions": [
            {
              "symbol": "!=",
              "left": "{page.CellMenuTreegetCaptionValue()}",
              "right": "{}"
            }
          ],
          "desc": "填充返回值",
          "eventClass": "net.ooder.esd.annotation.event.ModuleEventEnum",
          "eventValue": "onDestroy",
          "method": "call",
          "target": "callback",
          "type": "other"
        }
      ]
    }
  },
  "formulas": [],
  "itemType": "TREEVIEW",
  "key": "ood.Module",
  "moduleType": "EUClass",
  "moduleVar": {},
  "moduleViewType": "TREECONFIG",
  "path": "dsm.view.config.grid.cell.contextmenu.CellMenuTree",
  "properties": {
    "autoDestroy": true,
    "bindClass": [],
    "caption": "导入菜单动作",
    "currComponentAlias": "CellMenuTree",
    "dock": "fill",
    "title": "导入菜单动作"
  },
  "saveUrl": "/dsm/view/config/grid/cell/contextmenu/saveCellMenuClass"
}