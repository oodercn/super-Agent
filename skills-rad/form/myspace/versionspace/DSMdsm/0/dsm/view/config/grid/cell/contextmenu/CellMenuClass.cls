{
  "alias": "CellMenuClass",
  "children": [
    {
      "alias": "PAGECTX",
      "children": [
        {
          "alias": "fieldname",
          "events": {},
          "key": "ood.UI.HiddenInput",
          "path": "dsm.view.config.grid.cell.contextmenu.CellMenuClass.PAGECTX.fieldname",
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
          "path": "dsm.view.config.grid.cell.contextmenu.CellMenuClass.PAGECTX.currentClassName",
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
          "path": "dsm.view.config.grid.cell.contextmenu.CellMenuClass.PAGECTX.xpath",
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
          "path": "dsm.view.config.grid.cell.contextmenu.CellMenuClass.PAGECTX.sourceMethodName",
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
          "path": "dsm.view.config.grid.cell.contextmenu.CellMenuClass.PAGECTX.sourceClassName",
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
          "path": "dsm.view.config.grid.cell.contextmenu.CellMenuClass.PAGECTX.projectName",
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
          "alias": "domainId",
          "events": {},
          "key": "ood.UI.HiddenInput",
          "path": "dsm.view.config.grid.cell.contextmenu.CellMenuClass.PAGECTX.domainId",
          "properties": {
            "formField": true,
            "isPid": true,
            "name": "domainId",
            "pid": true,
            "tabindex": 6
          }
        },
        {
          "alias": "projectVersionName",
          "events": {},
          "key": "ood.UI.HiddenInput",
          "path": "dsm.view.config.grid.cell.contextmenu.CellMenuClass.PAGECTX.projectVersionName",
          "properties": {
            "formField": true,
            "isPid": false,
            "name": "projectVersionName",
            "pid": false,
            "tabindex": 7,
            "value": "DSMdsmVVVERSION0"
          }
        },
        {
          "alias": "menuClass",
          "events": {},
          "key": "ood.UI.HiddenInput",
          "path": "dsm.view.config.grid.cell.contextmenu.CellMenuClass.PAGECTX.menuClass",
          "properties": {
            "formField": true,
            "isPid": true,
            "name": "menuClass",
            "pid": true,
            "tabindex": 8
          }
        },
        {
          "alias": "desc",
          "events": {},
          "key": "ood.UI.HiddenInput",
          "path": "dsm.view.config.grid.cell.contextmenu.CellMenuClass.PAGECTX.desc",
          "properties": {
            "formField": true,
            "isPid": false,
            "name": "desc",
            "pid": false,
            "tabindex": 9
          }
        }
      ],
      "events": {},
      "key": "ood.UI.Block",
      "params": {},
      "path": "dsm.view.config.grid.cell.contextmenu.CellMenuClass.PAGECTX",
      "properties": {
        "borderType": "none",
        "tabindex": 0,
        "visibility": "hidden"
      }
    },
    {
      "alias": "CellMenuClassMain",
      "children": [
        {
          "alias": "CellMenuClass",
          "events": {
            "onDblclickRow": {
              "actions": [
                {
                  "args": [
                    "{page.show2()}",
                    "null",
                    null,
                    null,
                    null,
                    null,
                    "{args[1]}",
                    "{page}",
                    "{true}"
                  ],
                  "conditions": [],
                  "desc": "编辑",
                  "enumClassName": "net.ooder.esd.annotation.action.CustomPageAction",
                  "enumValue": "EDITOR",
                  "eventClass": "net.ooder.esd.annotation.event.GridEventEnum",
                  "eventValue": "onDblclickRow",
                  "id": "net.ooder.esd.annotation.action.CustomPageAction_EDITOR_null_null",
                  "method": "show2",
                  "redirection": "page::",
                  "return": true,
                  "target": "dsm.view.config.form.field.contextmenu.AggConfigTree",
                  "type": "page"
                }
              ]
            }
          },
          "key": "ood.UI.TreeGrid",
          "path": "dsm.view.config.grid.cell.contextmenu.CellMenuClass.CellMenuClassMain.CellMenuClass",
          "properties": {
            "defaultFocus": false,
            "desc": "getCellMenuClass",
            "disableClickEffect": false,
            "disableHoverEffect": false,
            "disableTips": false,
            "disabled": false,
            "header": [
              {
                "caption": "fieldname",
                "hidden": true,
                "id": "fieldname",
                "tips": "fieldname",
                "title": "fieldname",
                "type": "input"
              },
              {
                "caption": "currentClassName",
                "hidden": true,
                "id": "currentClassName",
                "tips": "currentClassName",
                "title": "currentClassName",
                "type": "input"
              },
              {
                "caption": "xpath",
                "hidden": true,
                "id": "xpath",
                "tips": "xpath",
                "title": "xpath",
                "type": "input"
              },
              {
                "caption": "sourceMethodName",
                "hidden": true,
                "id": "sourceMethodName",
                "tips": "sourceMethodName",
                "title": "sourceMethodName",
                "type": "input"
              },
              {
                "caption": "sourceClassName",
                "hidden": true,
                "id": "sourceClassName",
                "tips": "sourceClassName",
                "title": "sourceClassName",
                "type": "input"
              },
              {
                "caption": "domainId",
                "hidden": true,
                "id": "domainId",
                "tips": "domainId",
                "title": "domainId",
                "type": "input"
              },
              {
                "caption": "menuClass",
                "hidden": true,
                "id": "menuClass",
                "tips": "menuClass",
                "title": "menuClass",
                "type": "input"
              },
              {
                "caption": "名称",
                "hidden": false,
                "id": "name",
                "tips": "名称",
                "title": "名称",
                "type": "input",
                "width": "150.0"
              },
              {
                "caption": "包名",
                "flexSize": true,
                "hidden": false,
                "id": "packageName",
                "tips": "包名",
                "title": "包名",
                "type": "input",
                "width": "150.0"
              },
              {
                "caption": "描述",
                "hidden": true,
                "id": "desc",
                "tips": "描述",
                "title": "描述",
                "type": "input"
              }
            ],
            "headerHeight": "2.0em",
            "items": [],
            "name": "CellMenuClass",
            "rowHandler": true,
            "rowHandlerWidth": "5.0em",
            "rowHeight": "3.0em",
            "rowNumbered": true,
            "selMode": "multibycheckbox",
            "showHeader": true,
            "tabindex": 0,
            "uidColumn": "menuClass",
            "valueSeparator": ";",
            "visibility": "visible"
          }
        },
        {
          "alias": "CellMenuClass_PAGEBARDiv",
          "children": [
            {
              "alias": "CellMenuClass_PAGEBAR",
              "data": 20,
              "events": {},
              "key": "ood.UI.PageBar",
              "path": "dsm.view.config.grid.cell.contextmenu.CellMenuClass.CellMenuClassMain.CellMenuClass_PAGEBARDiv.CellMenuClass_PAGEBAR",
              "properties": {
                "caption": "分页",
                "pageCount": 20,
                "showMoreBtns": true,
                "tabindex": 0,
                "tips": "分页",
                "title": "分页"
              }
            }
          ],
          "events": {},
          "key": "ood.UI.Div",
          "path": "dsm.view.config.grid.cell.contextmenu.CellMenuClass.CellMenuClassMain.CellMenuClass_PAGEBARDiv",
          "properties": {
            "dock": "bottom",
            "height": "2.5em",
            "tabindex": 1
          }
        },
        {
          "alias": "MENUBARMenu",
          "apis": [],
          "events": {
            "onMenuBtnClick": {
              "actions": [
                {
                  "args": [
                    "{page.DELETE.invoke()}"
                  ],
                  "conditions": [
                    {
                      "symbol": "=",
                      "left": "{args[1].id}",
                      "right": "DELETE_button"
                    }
                  ],
                  "desc": "刪除",
                  "enumClassName": "net.ooder.esd.annotation.action.CustomGridAction",
                  "enumValue": "DELETE",
                  "eventClass": "net.ooder.esd.annotation.event.MenuEventEnum",
                  "eventValue": "onMenuBtnClick",
                  "id": "net.ooder.esd.annotation.action.CustomGridAction_DELETE_null_null",
                  "method": "invoke",
                  "redirection": "other:callback:call",
                  "return": false,
                  "target": "DELETE",
                  "type": "control"
                },
                {
                  "args": [
                    null,
                    null,
                    null,
                    null,
                    null,
                    null,
                    "{page.getData()}",
                    "{page}",
                    "{true}"
                  ],
                  "conditions": [
                    {
                      "symbol": "=",
                      "left": "{args[1].id}",
                      "right": "ADD_button"
                    }
                  ],
                  "desc": "新增",
                  "enumClassName": "net.ooder.esd.annotation.action.CustomPageAction",
                  "enumValue": "ADD",
                  "eventClass": "net.ooder.esd.annotation.event.MenuEventEnum",
                  "eventValue": "onMenuBtnClick",
                  "id": "net.ooder.esd.annotation.action.CustomPageAction_ADD_null_null",
                  "method": "show2",
                  "redirection": "page::",
                  "return": true,
                  "target": "dsm.view.config.form.field.contextmenu.FieldContextMenuTree",
                  "type": "page"
                },
                {
                  "args": [
                    "{page.RELOAD.invoke()}"
                  ],
                  "conditions": [
                    {
                      "symbol": "=",
                      "left": "{args[1].id}",
                      "right": "RELOAD_button"
                    }
                  ],
                  "desc": "重新装载",
                  "enumClassName": "net.ooder.esd.annotation.action.CustomGridAction",
                  "enumValue": "RELOAD",
                  "eventClass": "net.ooder.esd.annotation.event.MenuEventEnum",
                  "eventValue": "onMenuBtnClick",
                  "id": "net.ooder.esd.annotation.action.CustomGridAction_RELOAD_null_null",
                  "method": "invoke",
                  "redirection": "other:callback:call",
                  "return": true,
                  "target": "RELOAD",
                  "type": "control"
                }
              ]
            }
          },
          "id": "MENUBARMenu",
          "index": 100,
          "key": "ood.UI.MenuBar",
          "menuType": "MENUBAR",
          "path": "dsm.view.config.grid.cell.contextmenu.CellMenuClass.CellMenuClassMain.MENUBARMenu",
          "properties": {
            "autoFontColor": false,
            "autoIconColor": true,
            "autoItemColor": false,
            "dock": "top",
            "hAlign": "left",
            "index": 100,
            "items": [
              {
                "bindClass": [],
                "caption": "删除",
                "expression": "true",
                "iconColor": "#e04d7f",
                "id": "DELETE_button",
                "imageClass": "ri-close-line",
                "tabindex": 0,
                "tagVar": {},
                "tips": "删除",
                "title": "删除",
                "type": "button"
              },
              {
                "bindClass": [],
                "caption": "添加",
                "expression": "true",
                "iconColor": "#46C37B",
                "id": "ADD_button",
                "imageClass": "ri-add-line",
                "tabindex": 1,
                "tagVar": {},
                "tips": "添加",
                "title": "添加",
                "type": "button"
              },
              {
                "bindClass": [],
                "caption": "刷新",
                "expression": "true",
                "iconColor": "#195ead",
                "id": "RELOAD_button",
                "imageClass": "ri-refresh-line",
                "tabindex": 2,
                "tagVar": {},
                "tips": "刷新",
                "title": "刷新",
                "type": "button"
              }
            ],
            "lazy": false,
            "showCaption": true,
            "tabindex": 2
          },
          "showCaption": true
        }
      ],
      "events": {},
      "key": "ood.UI.Block",
      "path": "dsm.view.config.grid.cell.contextmenu.CellMenuClass.CellMenuClassMain",
      "properties": {
        "borderType": "none",
        "dock": "fill",
        "panelBgClr": "transparent",
        "tabindex": 1
      }
    },
    {
      "alias": "DELETE",
      "events": {
        "afterInvoke": {
          "actions": [
            {
              "args": [
                "{page.RELOAD.invoke()}"
              ],
              "conditions": [],
              "desc": "重新装载",
              "enumClassName": "net.ooder.esd.annotation.action.CustomGridAction",
              "enumValue": "RELOAD",
              "eventClass": "net.ooder.esd.annotation.event.CustomCallBack",
              "eventValue": "RELOAD",
              "id": "net.ooder.esd.annotation.action.CustomGridAction_RELOAD_null_null",
              "method": "invoke",
              "redirection": "other:callback:call",
              "return": true,
              "target": "RELOAD",
              "type": "control"
            }
          ]
        }
      },
      "key": "ood.APICaller",
      "path": "dsm.view.config.grid.cell.contextmenu.CellMenuClass.DELETE",
      "properties": {
        "allform": false,
        "autoRun": false,
        "checkRequired": false,
        "checkValid": false,
        "desc": "删除",
        "id": "/dsm/view/config/grid/cell/contextmenu/delContextMenu",
        "imageClass": "ri-code-box-line",
        "isAllform": false,
        "methodName": "delContextMenu",
        "name": "delContextMenu",
        "proxyType": "auto",
        "queryAsync": true,
        "queryMethod": "POST",
        "queryURL": "/dsm/view/config/grid/cell/contextmenu/delContextMenu",
        "requestDataSource": [
          {
            "name": "CellMenuClass",
            "path": "",
            "type": "TREEGRID"
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
        "tabindex": 2,
        "tips": "删除"
      }
    },
    {
      "alias": "RELOAD",
      "events": {},
      "key": "ood.APICaller",
      "path": "dsm.view.config.grid.cell.contextmenu.CellMenuClass.RELOAD",
      "properties": {
        "autoRun": true,
        "desc": "单元格菜单选择",
        "id": "/dsm/view/config/grid/cell/contextmenu/CellMenuClass",
        "imageClass": "ri-git-branch-line",
        "methodName": "getCellMenuClass",
        "name": "CellMenuClass",
        "proxyType": "auto",
        "queryMethod": "POST",
        "queryURL": "/dsm/view/config/grid/cell/contextmenu/CellMenuClass",
        "requestDataSource": [
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
            "name": "CellMenuClass",
            "path": "data",
            "type": "TREEGRID"
          },
          {
            "name": "PAGECTX",
            "path": "data",
            "type": "FORM"
          }
        ],
        "responseType": "JSON",
        "sourceClassName": "net.ooder.dsm.view.config.grid.cell.contextmenu.ContextCellMenuNav",
        "tabindex": 3,
        "tips": "单元格菜单选择"
      }
    }
  ],
  "className": "dsm.view.config.grid.cell.contextmenu.CellMenuClass",
  "customFunctions": {},
  "desc": "单元格菜单选择",
  "dio": false,
  "events": {},
  "formulas": [],
  "key": "ood.Module",
  "moduleType": "EUClass",
  "moduleVar": {},
  "moduleViewType": "GRIDCONFIG",
  "path": "dsm.view.config.grid.cell.contextmenu.CellMenuClass",
  "properties": {
    "autoDestroy": true,
    "bindClass": [],
    "caption": "单元格菜单选择",
    "currComponentAlias": "CellMenuClass",
    "dock": "fill",
    "title": "单元格菜单选择"
  }
}