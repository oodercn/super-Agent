{
  "alias": "AggConfigTree",
  "children": [
    {
      "alias": "PAGECTX",
      "children": [
        {
          "alias": "fieldname",
          "events": {},
          "key": "ood.UI.HiddenInput",
          "path": "dsm.view.config.grid.cell.contextmenu.AggConfigTree.PAGECTX.fieldname",
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
          "path": "dsm.view.config.grid.cell.contextmenu.AggConfigTree.PAGECTX.currentClassName",
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
          "path": "dsm.view.config.grid.cell.contextmenu.AggConfigTree.PAGECTX.xpath",
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
          "path": "dsm.view.config.grid.cell.contextmenu.AggConfigTree.PAGECTX.sourceMethodName",
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
          "path": "dsm.view.config.grid.cell.contextmenu.AggConfigTree.PAGECTX.sourceClassName",
          "properties": {
            "formField": true,
            "isPid": true,
            "name": "sourceClassName",
            "pid": true,
            "tabindex": 4
          }
        },
        {
          "alias": "id",
          "events": {},
          "key": "ood.UI.HiddenInput",
          "path": "dsm.view.config.grid.cell.contextmenu.AggConfigTree.PAGECTX.id",
          "properties": {
            "formField": true,
            "isPid": false,
            "name": "id",
            "pid": false,
            "tabindex": 5
          }
        },
        {
          "alias": "projectName",
          "events": {},
          "key": "ood.UI.HiddenInput",
          "path": "dsm.view.config.grid.cell.contextmenu.AggConfigTree.PAGECTX.projectName",
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
          "path": "dsm.view.config.grid.cell.contextmenu.AggConfigTree.PAGECTX.domainId",
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
          "path": "dsm.view.config.grid.cell.contextmenu.AggConfigTree.PAGECTX.projectVersionName",
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
          "alias": "viewClassName",
          "events": {},
          "key": "ood.UI.HiddenInput",
          "path": "dsm.view.config.grid.cell.contextmenu.AggConfigTree.PAGECTX.viewClassName",
          "properties": {
            "formField": true,
            "isPid": true,
            "name": "viewClassName",
            "pid": true,
            "tabindex": 9
          }
        },
        {
          "alias": "menuType",
          "events": {},
          "key": "ood.UI.HiddenInput",
          "path": "dsm.view.config.grid.cell.contextmenu.AggConfigTree.PAGECTX.menuType",
          "properties": {
            "formField": true,
            "isPid": true,
            "name": "menuType",
            "pid": true,
            "tabindex": 10
          }
        }
      ],
      "events": {},
      "key": "ood.UI.Block",
      "params": {},
      "path": "dsm.view.config.grid.cell.contextmenu.AggConfigTree.PAGECTX",
      "properties": {
        "borderType": "none",
        "tabindex": 0,
        "visibility": "hidden"
      }
    },
    {
      "alias": "AggConfigTreeMain",
      "children": [
        {
          "alias": "AggConfigTreeLAYOUT",
          "children": [
            {
              "alias": "AggMenuConfigTreePanel",
              "children": [
                {
                  "alias": "AggMenuConfigTree",
                  "events": {
                    "onGetContent": {
                      "actions": [
                        {
                          "args": [
                            "{page.dsm_agg_entity_config_LoadNav.setQueryData()}",
                            null,
                            null,
                            "{args[1].tagVar}",
                            ""
                          ],
                          "conditions": [
                            {
                              "symbol": "=",
                              "left": "{args[1].groupName}",
                              "right": "dsm_agg_entity_config_LoadNav"
                            }
                          ],
                          "desc": "设置扩展参数",
                          "eventClass": "net.ooder.esd.annotation.event.TreeViewEventEnum",
                          "eventValue": "onGetContent",
                          "method": "setQueryData",
                          "redirection": "other:callback:call",
                          "return": true,
                          "target": "dsm_agg_entity_config_LoadNav",
                          "type": "control"
                        },
                        {
                          "args": [
                            "{page.dsm_agg_entity_config_LoadNav.invoke()}",
                            "temp",
                            null,
                            "{args[2]}"
                          ],
                          "conditions": [
                            {
                              "symbol": "=",
                              "left": "{args[1].groupName}",
                              "right": "dsm_agg_entity_config_LoadNav"
                            }
                          ],
                          "desc": "子节点装载",
                          "eventClass": "net.ooder.esd.annotation.event.TreeViewEventEnum",
                          "eventValue": "onGetContent",
                          "method": "invoke",
                          "redirection": "other:callback:call",
                          "return": true,
                          "target": "dsm_agg_entity_config_LoadNav",
                          "type": "control"
                        },
                        {
                          "args": [
                            "{page.AggMenuMainNavItem_MethodConfig_dsm_agg_menu_config_LoadAllMethod.setQueryData()}",
                            null,
                            null,
                            "{args[1].tagVar}",
                            ""
                          ],
                          "conditions": [
                            {
                              "symbol": "=",
                              "left": "{args[1].groupName}",
                              "right": "AggMenuMainNavItem_MethodConfig_dsm_agg_menu_config_LoadAllMethod"
                            }
                          ],
                          "desc": "设置扩展参数",
                          "eventClass": "net.ooder.esd.annotation.event.TreeViewEventEnum",
                          "eventValue": "onGetContent",
                          "method": "setQueryData",
                          "redirection": "other:callback:call",
                          "return": true,
                          "target": "AggMenuMainNavItem_MethodConfig_dsm_agg_menu_config_LoadAllMethod",
                          "type": "control"
                        },
                        {
                          "args": [
                            "{page.AggMenuMainNavItem_MethodConfig_dsm_agg_menu_config_LoadAllMethod.invoke()}",
                            "temp",
                            null,
                            "{args[2]}"
                          ],
                          "conditions": [
                            {
                              "symbol": "=",
                              "left": "{args[1].groupName}",
                              "right": "AggMenuMainNavItem_MethodConfig_dsm_agg_menu_config_LoadAllMethod"
                            }
                          ],
                          "desc": "子节点装载",
                          "eventClass": "net.ooder.esd.annotation.event.TreeViewEventEnum",
                          "eventValue": "onGetContent",
                          "method": "invoke",
                          "redirection": "other:callback:call",
                          "return": true,
                          "target": "AggMenuMainNavItem_MethodConfig_dsm_agg_menu_config_LoadAllMethod",
                          "type": "control"
                        },
                        {
                          "args": [
                            "{page.dsm_agg_entity_LoadChildView.setQueryData()}",
                            null,
                            null,
                            "{args[1].tagVar}",
                            ""
                          ],
                          "conditions": [
                            {
                              "symbol": "=",
                              "left": "{args[1].groupName}",
                              "right": "dsm_agg_entity_LoadChildView"
                            }
                          ],
                          "desc": "设置扩展参数",
                          "eventClass": "net.ooder.esd.annotation.event.TreeViewEventEnum",
                          "eventValue": "onGetContent",
                          "method": "setQueryData",
                          "redirection": "other:callback:call",
                          "return": true,
                          "target": "dsm_agg_entity_LoadChildView",
                          "type": "control"
                        },
                        {
                          "args": [
                            "{page.dsm_agg_entity_LoadChildView.invoke()}",
                            "temp",
                            null,
                            "{args[2]}"
                          ],
                          "conditions": [
                            {
                              "symbol": "=",
                              "left": "{args[1].groupName}",
                              "right": "dsm_agg_entity_LoadChildView"
                            }
                          ],
                          "desc": "子节点装载",
                          "eventClass": "net.ooder.esd.annotation.event.TreeViewEventEnum",
                          "eventValue": "onGetContent",
                          "method": "invoke",
                          "redirection": "other:callback:call",
                          "return": true,
                          "target": "dsm_agg_entity_LoadChildView",
                          "type": "control"
                        },
                        {
                          "args": [
                            "{page.AggMenuNavItem_CustomMethodConfig_dsm_agg_menu_config_method_LoadMenuMethod.setQueryData()}",
                            null,
                            null,
                            "{args[1].tagVar}",
                            ""
                          ],
                          "conditions": [
                            {
                              "symbol": "=",
                              "left": "{args[1].groupName}",
                              "right": "AggMenuNavItem_CustomMethodConfig_dsm_agg_menu_config_method_LoadMenuMethod"
                            }
                          ],
                          "desc": "设置扩展参数",
                          "eventClass": "net.ooder.esd.annotation.event.TreeViewEventEnum",
                          "eventValue": "onGetContent",
                          "method": "setQueryData",
                          "redirection": "other:callback:call",
                          "return": true,
                          "target": "AggMenuNavItem_CustomMethodConfig_dsm_agg_menu_config_method_LoadMenuMethod",
                          "type": "control"
                        },
                        {
                          "args": [
                            "{page.AggMenuNavItem_CustomMethodConfig_dsm_agg_menu_config_method_LoadMenuMethod.invoke()}",
                            "temp",
                            null,
                            "{args[2]}"
                          ],
                          "conditions": [
                            {
                              "symbol": "=",
                              "left": "{args[1].groupName}",
                              "right": "AggMenuNavItem_CustomMethodConfig_dsm_agg_menu_config_method_LoadMenuMethod"
                            }
                          ],
                          "desc": "子节点装载",
                          "eventClass": "net.ooder.esd.annotation.event.TreeViewEventEnum",
                          "eventValue": "onGetContent",
                          "method": "invoke",
                          "redirection": "other:callback:call",
                          "return": true,
                          "target": "AggMenuNavItem_CustomMethodConfig_dsm_agg_menu_config_method_LoadMenuMethod",
                          "type": "control"
                        },
                        {
                          "args": [
                            "{page.AggEntityMethodNavItem_ViewConfig_dsm_agg_entity_config_LoadNav.setQueryData()}",
                            null,
                            null,
                            "{args[1].tagVar}",
                            ""
                          ],
                          "conditions": [
                            {
                              "symbol": "=",
                              "left": "{args[1].groupName}",
                              "right": "AggEntityMethodNavItem_ViewConfig_dsm_agg_entity_config_LoadNav"
                            }
                          ],
                          "desc": "设置扩展参数",
                          "eventClass": "net.ooder.esd.annotation.event.TreeViewEventEnum",
                          "eventValue": "onGetContent",
                          "method": "setQueryData",
                          "redirection": "other:callback:call",
                          "return": true,
                          "target": "AggEntityMethodNavItem_ViewConfig_dsm_agg_entity_config_LoadNav",
                          "type": "control"
                        },
                        {
                          "args": [
                            "{page.AggEntityMethodNavItem_ViewConfig_dsm_agg_entity_config_LoadNav.invoke()}",
                            "temp",
                            null,
                            "{args[2]}"
                          ],
                          "conditions": [
                            {
                              "symbol": "=",
                              "left": "{args[1].groupName}",
                              "right": "AggEntityMethodNavItem_ViewConfig_dsm_agg_entity_config_LoadNav"
                            }
                          ],
                          "desc": "子节点装载",
                          "eventClass": "net.ooder.esd.annotation.event.TreeViewEventEnum",
                          "eventValue": "onGetContent",
                          "method": "invoke",
                          "redirection": "other:callback:call",
                          "return": true,
                          "target": "AggEntityMethodNavItem_ViewConfig_dsm_agg_entity_config_LoadNav",
                          "type": "control"
                        },
                        {
                          "args": [
                            "{page.AggEntityNavItem_ViewMethodConfig_dsm_agg_entity_config_view_LoadView.setQueryData()}",
                            null,
                            null,
                            "{args[1].tagVar}",
                            ""
                          ],
                          "conditions": [
                            {
                              "symbol": "=",
                              "left": "{args[1].groupName}",
                              "right": "AggEntityNavItem_ViewMethodConfig_dsm_agg_entity_config_view_LoadView"
                            }
                          ],
                          "desc": "设置扩展参数",
                          "eventClass": "net.ooder.esd.annotation.event.TreeViewEventEnum",
                          "eventValue": "onGetContent",
                          "method": "setQueryData",
                          "redirection": "other:callback:call",
                          "return": true,
                          "target": "AggEntityNavItem_ViewMethodConfig_dsm_agg_entity_config_view_LoadView",
                          "type": "control"
                        },
                        {
                          "args": [
                            "{page.AggEntityNavItem_ViewMethodConfig_dsm_agg_entity_config_view_LoadView.invoke()}",
                            "temp",
                            null,
                            "{args[2]}"
                          ],
                          "conditions": [
                            {
                              "symbol": "=",
                              "left": "{args[1].groupName}",
                              "right": "AggEntityNavItem_ViewMethodConfig_dsm_agg_entity_config_view_LoadView"
                            }
                          ],
                          "desc": "子节点装载",
                          "eventClass": "net.ooder.esd.annotation.event.TreeViewEventEnum",
                          "eventValue": "onGetContent",
                          "method": "invoke",
                          "redirection": "other:callback:call",
                          "return": true,
                          "target": "AggEntityNavItem_ViewMethodConfig_dsm_agg_entity_config_view_LoadView",
                          "type": "control"
                        },
                        {
                          "args": [
                            "{page.AggEntityNavItem_CustomMethodConfig_dsm_agg_entity_config_method_LoadMethod.setQueryData()}",
                            null,
                            null,
                            "{args[1].tagVar}",
                            ""
                          ],
                          "conditions": [
                            {
                              "symbol": "=",
                              "left": "{args[1].groupName}",
                              "right": "AggEntityNavItem_CustomMethodConfig_dsm_agg_entity_config_method_LoadMethod"
                            }
                          ],
                          "desc": "设置扩展参数",
                          "eventClass": "net.ooder.esd.annotation.event.TreeViewEventEnum",
                          "eventValue": "onGetContent",
                          "method": "setQueryData",
                          "redirection": "other:callback:call",
                          "return": true,
                          "target": "AggEntityNavItem_CustomMethodConfig_dsm_agg_entity_config_method_LoadMethod",
                          "type": "control"
                        },
                        {
                          "args": [
                            "{page.AggEntityNavItem_CustomMethodConfig_dsm_agg_entity_config_method_LoadMethod.invoke()}",
                            "temp",
                            null,
                            "{args[2]}"
                          ],
                          "conditions": [
                            {
                              "symbol": "=",
                              "left": "{args[1].groupName}",
                              "right": "AggEntityNavItem_CustomMethodConfig_dsm_agg_entity_config_method_LoadMethod"
                            }
                          ],
                          "desc": "子节点装载",
                          "eventClass": "net.ooder.esd.annotation.event.TreeViewEventEnum",
                          "eventValue": "onGetContent",
                          "method": "invoke",
                          "redirection": "other:callback:call",
                          "return": true,
                          "target": "AggEntityNavItem_CustomMethodConfig_dsm_agg_entity_config_method_LoadMethod",
                          "type": "control"
                        },
                        {
                          "args": [
                            "{page.AggEntityMethodNavItem_ModuleConfig_dsm_agg_module_config_LoadModuleNav.setQueryData()}",
                            null,
                            null,
                            "{args[1].tagVar}",
                            ""
                          ],
                          "conditions": [
                            {
                              "symbol": "=",
                              "left": "{args[1].groupName}",
                              "right": "AggEntityMethodNavItem_ModuleConfig_dsm_agg_module_config_LoadModuleNav"
                            }
                          ],
                          "desc": "设置扩展参数",
                          "eventClass": "net.ooder.esd.annotation.event.TreeViewEventEnum",
                          "eventValue": "onGetContent",
                          "method": "setQueryData",
                          "redirection": "other:callback:call",
                          "return": true,
                          "target": "AggEntityMethodNavItem_ModuleConfig_dsm_agg_module_config_LoadModuleNav",
                          "type": "control"
                        },
                        {
                          "args": [
                            "{page.AggEntityMethodNavItem_ModuleConfig_dsm_agg_module_config_LoadModuleNav.invoke()}",
                            "temp",
                            null,
                            "{args[2]}"
                          ],
                          "conditions": [
                            {
                              "symbol": "=",
                              "left": "{args[1].groupName}",
                              "right": "AggEntityMethodNavItem_ModuleConfig_dsm_agg_module_config_LoadModuleNav"
                            }
                          ],
                          "desc": "子节点装载",
                          "eventClass": "net.ooder.esd.annotation.event.TreeViewEventEnum",
                          "eventValue": "onGetContent",
                          "method": "invoke",
                          "redirection": "other:callback:call",
                          "return": true,
                          "target": "AggEntityMethodNavItem_ModuleConfig_dsm_agg_module_config_LoadModuleNav",
                          "type": "control"
                        },
                        {
                          "args": [
                            "{page.AggEntityNavItem_FieldsConfig_dsm_agg_field_LoadFieldList.setQueryData()}",
                            null,
                            null,
                            "{args[1].tagVar}",
                            ""
                          ],
                          "conditions": [
                            {
                              "symbol": "=",
                              "left": "{args[1].groupName}",
                              "right": "AggEntityNavItem_FieldsConfig_dsm_agg_field_LoadFieldList"
                            }
                          ],
                          "desc": "设置扩展参数",
                          "eventClass": "net.ooder.esd.annotation.event.TreeViewEventEnum",
                          "eventValue": "onGetContent",
                          "method": "setQueryData",
                          "redirection": "other:callback:call",
                          "return": true,
                          "target": "AggEntityNavItem_FieldsConfig_dsm_agg_field_LoadFieldList",
                          "type": "control"
                        },
                        {
                          "args": [
                            "{page.AggEntityNavItem_FieldsConfig_dsm_agg_field_LoadFieldList.invoke()}",
                            "temp",
                            null,
                            "{args[2]}"
                          ],
                          "conditions": [
                            {
                              "symbol": "=",
                              "left": "{args[1].groupName}",
                              "right": "AggEntityNavItem_FieldsConfig_dsm_agg_field_LoadFieldList"
                            }
                          ],
                          "desc": "子节点装载",
                          "eventClass": "net.ooder.esd.annotation.event.TreeViewEventEnum",
                          "eventValue": "onGetContent",
                          "method": "invoke",
                          "redirection": "other:callback:call",
                          "return": true,
                          "target": "AggEntityNavItem_FieldsConfig_dsm_agg_field_LoadFieldList",
                          "type": "control"
                        },
                        {
                          "args": [
                            "{page.AggEntityMethodNavItem_DesignerConfig_dsm_agg_module_config_LoadWinItem.setQueryData()}",
                            null,
                            null,
                            "{args[1].tagVar}",
                            ""
                          ],
                          "conditions": [
                            {
                              "symbol": "=",
                              "left": "{args[1].groupName}",
                              "right": "AggEntityMethodNavItem_DesignerConfig_dsm_agg_module_config_LoadWinItem"
                            }
                          ],
                          "desc": "设置扩展参数",
                          "eventClass": "net.ooder.esd.annotation.event.TreeViewEventEnum",
                          "eventValue": "onGetContent",
                          "method": "setQueryData",
                          "redirection": "other:callback:call",
                          "return": true,
                          "target": "AggEntityMethodNavItem_DesignerConfig_dsm_agg_module_config_LoadWinItem",
                          "type": "control"
                        },
                        {
                          "args": [
                            "{page.AggEntityMethodNavItem_DesignerConfig_dsm_agg_module_config_LoadWinItem.invoke()}",
                            "temp",
                            null,
                            "{args[2]}"
                          ],
                          "conditions": [
                            {
                              "symbol": "=",
                              "left": "{args[1].groupName}",
                              "right": "AggEntityMethodNavItem_DesignerConfig_dsm_agg_module_config_LoadWinItem"
                            }
                          ],
                          "desc": "子节点装载",
                          "eventClass": "net.ooder.esd.annotation.event.TreeViewEventEnum",
                          "eventValue": "onGetContent",
                          "method": "invoke",
                          "redirection": "other:callback:call",
                          "return": true,
                          "target": "AggEntityMethodNavItem_DesignerConfig_dsm_agg_module_config_LoadWinItem",
                          "type": "control"
                        }
                      ]
                    },
                    "onItemSelected": {
                      "actions": [
                        {
                          "args": [],
                          "conditions": [
                            {
                              "symbol": "non-empty",
                              "left": "{args[1].euClassName}",
                              "right": ""
                            }
                          ],
                          "desc": "清空",
                          "eventClass": "net.ooder.esd.annotation.event.TreeViewEventEnum",
                          "eventValue": "onItemSelected",
                          "method": "clearItems",
                          "target": "getAggConfigTree",
                          "type": "control"
                        },
                        {
                          "args": [
                            "{args[1]}"
                          ],
                          "conditions": [
                            {
                              "symbol": "non-empty",
                              "left": "{args[1].euClassName}",
                              "right": ""
                            }
                          ],
                          "desc": "添加TAB页",
                          "eventClass": "net.ooder.esd.annotation.event.TreeViewEventEnum",
                          "eventValue": "onItemSelected",
                          "method": "insertItems2",
                          "target": "getAggConfigTree",
                          "type": "control"
                        },
                        {
                          "args": [
                            "{args[1].id}"
                          ],
                          "conditions": [
                            {
                              "symbol": "non-empty",
                              "left": "{args[1].euClassName}",
                              "right": ""
                            }
                          ],
                          "desc": "添加点击事件",
                          "eventClass": "net.ooder.esd.annotation.event.TreeViewEventEnum",
                          "eventValue": "onItemSelected",
                          "method": "fireItemClickEvent",
                          "target": "getAggConfigTree",
                          "type": "control"
                        }
                      ]
                    }
                  },
                  "key": "ood.UI.TreeView",
                  "path": "dsm.view.config.grid.cell.contextmenu.AggConfigTree.AggConfigTreeMain.AggConfigTreeLAYOUT.AggMenuConfigTreePanel.AggMenuConfigTree",
                  "properties": {
                    "caption": "AggMenuConfigTree",
                    "defaultFocus": false,
                    "disableClickEffect": false,
                    "disableHoverEffect": false,
                    "disableTips": false,
                    "disabled": false,
                    "dock": "none",
                    "dynDestory": true,
                    "formField": true,
                    "initFold": false,
                    "items": [
                      {
                        "bindClass": [
                          "java.lang.Void"
                        ],
                        "caption": "AggMenuConfigTree",
                        "id": "aggConfigTree",
                        "imageClass": "ri-settings-3-line",
                        "initFold": false,
                        "sub": [
                          {
                            "bindClass": [
                              "net.ooder.dsm.aggregation.config.entity.tree.EntityMethodService"
                            ],
                            "bindClassName": "net.ooder.dsm.aggregation.config.entity.tree.EntityMethodService",
                            "caption": "菜单配置（null)",
                            "closeBtn": false,
                            "dynDestory": true,
                            "dynLoad": true,
                            "entityClass": "net.ooder.dsm.aggregation.config.menu.tree.MenuItemViewBean",
                            "groupName": "dsm_agg_entity_config_LoadNav",
                            "id": "dsm_agg_entity_config_LoadNav",
                            "imageClass": "ri-node-tree",
                            "initFold": true,
                            "tagVar": {
                              "name": "loadNav"
                            },
                            "title": "菜单配置（null)"
                          },
                          {
                            "bindClass": [
                              "net.ooder.dsm.aggregation.config.menu.tree.AggMenuWebService"
                            ],
                            "bindClassName": "net.ooder.dsm.aggregation.config.menu.tree.AggMenuWebService",
                            "caption": "Web配置",
                            "closeBtn": false,
                            "dynDestory": true,
                            "dynLoad": true,
                            "entityClass": "net.ooder.dsm.aggregation.config.menu.tree.AggMenuNavItem",
                            "groupName": "AggMenuNavItem_WebMethodConfig_dsm_agg_menu_config_method_ConfigBaseInfo",
                            "id": "WebMethodConfig",
                            "imageClass": "spafont spa-icon-c-webapi",
                            "initFold": false,
                            "tagVar": {
                              "name": "WebMethodConfig"
                            },
                            "title": "Web配置"
                          },
                          {
                            "bindClass": [
                              "net.ooder.dsm.aggregation.config.entity.AggEntityInfoService"
                            ],
                            "bindClassName": "net.ooder.dsm.aggregation.config.entity.AggEntityInfoService",
                            "caption": "菜单配置（null)",
                            "closeBtn": false,
                            "dynDestory": true,
                            "dynLoad": true,
                            "entityClass": "net.ooder.esd.bean.CustomViewBean",
                            "groupName": "dsm_view_entity_AggEntityInfo",
                            "id": "dsm_view_entity_AggEntityInfo",
                            "imageClass": "ri-node-tree",
                            "initFold": true,
                            "tagVar": {
                              "name": "aggEntityInfo"
                            },
                            "title": "菜单配置（null)"
                          },
                          {
                            "bindClass": [
                              "net.ooder.dsm.aggregation.config.menu.tree.AggMenuMethodsNavService"
                            ],
                            "bindClassName": "net.ooder.dsm.aggregation.config.menu.tree.AggMenuMethodsNavService",
                            "caption": "菜单项配置",
                            "closeBtn": false,
                            "dynDestory": true,
                            "dynLoad": true,
                            "entityClass": "net.ooder.dsm.aggregation.config.menu.tree.AggMenuMainNavItem",
                            "groupName": "AggMenuMainNavItem_MethodConfig_dsm_agg_menu_config_LoadAllMethod",
                            "id": "MethodConfig",
                            "imageClass": "ri-settings-line",
                            "initFold": false,
                            "tagVar": {
                              "name": "MethodConfig"
                            },
                            "title": "菜单项配置"
                          },
                          {
                            "bindClass": [
                              "net.ooder.dsm.aggregation.config.entity.tree.AggEntityTreeNavService"
                            ],
                            "bindClassName": "net.ooder.dsm.aggregation.config.entity.tree.AggEntityTreeNavService",
                            "caption": "菜单配置（null)",
                            "closeBtn": false,
                            "dynDestory": false,
                            "dynLoad": false,
                            "entityClass": "net.ooder.esd.bean.CustomAPICallBean",
                            "groupName": "dsm_agg_entity_config_nav_LoadEntity",
                            "id": "dsm_agg_entity_config_nav_LoadEntity",
                            "imageClass": "ri-node-tree",
                            "tagVar": {
                              "name": "loadEntity"
                            },
                            "title": "菜单配置（null)"
                          },
                          {
                            "bindClass": [
                              "net.ooder.dsm.aggregation.config.menu.tree.AggMenuConfigNavService"
                            ],
                            "bindClassName": "net.ooder.dsm.aggregation.config.menu.tree.AggMenuConfigNavService",
                            "caption": "菜单配置",
                            "closeBtn": false,
                            "dynDestory": true,
                            "dynLoad": true,
                            "entityClass": "net.ooder.dsm.aggregation.config.menu.tree.AggMenuNavItem",
                            "groupName": "AggMenuNavItem_MenuMethodConfig_dsm_agg_menu_config_method_group_AggMenuConfigView",
                            "id": "MenuMethodConfig",
                            "imageClass": "spafont spa-icon-project",
                            "initFold": false,
                            "tagVar": {
                              "name": "MenuMethodConfig"
                            },
                            "title": "菜单配置"
                          },
                          {
                            "bindClass": [
                              "net.ooder.dsm.aggregation.config.entity.tree.AggEntityViewService"
                            ],
                            "bindClassName": "net.ooder.dsm.aggregation.config.entity.tree.AggEntityViewService",
                            "caption": "菜单配置（null)",
                            "closeBtn": false,
                            "dynDestory": true,
                            "dynLoad": true,
                            "entityClass": "net.ooder.dsm.aggregation.config.menu.tree.MenuItemBean",
                            "groupName": "dsm_agg_entity_LoadChildView",
                            "id": "dsm_agg_entity_LoadChildView",
                            "imageClass": "ri-node-tree",
                            "initFold": true,
                            "tagVar": {
                              "name": "loadChildView"
                            },
                            "title": "菜单配置（null)"
                          },
                          {
                            "bindClass": [
                              "net.ooder.dsm.aggregation.config.menu.tree.AggMenuMethodNavService"
                            ],
                            "bindClassName": "net.ooder.dsm.aggregation.config.menu.tree.AggMenuMethodNavService",
                            "caption": "方法调用",
                            "closeBtn": false,
                            "dynDestory": true,
                            "dynLoad": true,
                            "entityClass": "net.ooder.dsm.aggregation.config.menu.tree.AggMenuNavItem",
                            "groupName": "AggMenuNavItem_CustomMethodConfig_dsm_agg_menu_config_method_LoadMenuMethod",
                            "id": "CustomMethodConfig",
                            "imageClass": "ri-settings-line",
                            "initFold": true,
                            "tagVar": {
                              "name": "CustomMethodConfig"
                            },
                            "title": "方法调用"
                          },
                          {
                            "bindClass": [
                              "net.ooder.dsm.aggregation.config.menu.tree.AggMenuConfigTree"
                            ],
                            "bindClassName": "net.ooder.dsm.aggregation.config.menu.tree.AggMenuConfigTree",
                            "caption": "AggMenuConfigTree",
                            "closeBtn": false,
                            "dynDestory": false,
                            "dynLoad": false,
                            "entityClass": "net.ooder.dsm.aggregation.config.menu.tree.AggMenuConfigTree",
                            "id": "null",
                            "imageClass": "ri-settings-3-line",
                            "tagVar": {
                              "name": "aggConfigTree"
                            },
                            "title": "AggMenuConfigTree"
                          },
                          {
                            "bindClass": [
                              "java.lang.Void"
                            ],
                            "caption": "loadNav",
                            "groupName": "dsm_agg_entity_config_LoadNav",
                            "id": "loadNav",
                            "imageClass": "ri-tree-line",
                            "initFold": false,
                            "sub": [
                              {
                                "bindClass": [
                                  "net.ooder.dsm.aggregation.config.entity.tree.field.AggEntityFieldService"
                                ],
                                "bindClassName": "net.ooder.dsm.aggregation.config.entity.tree.field.AggEntityFieldService",
                                "caption": "loadNav（null)",
                                "closeBtn": false,
                                "dynDestory": false,
                                "dynLoad": false,
                                "entityClass": "net.ooder.esd.dsm.aggregation.FieldAggConfig",
                                "groupName": "dsm_agg_field_FieldInfo",
                                "id": "loadNav_dsm_agg_field_FieldInfo",
                                "imageClass": "ri-node-tree",
                                "tagVar": {
                                  "name": "fieldInfo"
                                },
                                "title": "loadNav（null)"
                              },
                              {
                                "bindClass": [
                                  "net.ooder.dsm.aggregation.config.entity.ref.AggRefService"
                                ],
                                "bindClassName": "net.ooder.dsm.aggregation.config.entity.ref.AggRefService",
                                "caption": "loadNav（null)",
                                "closeBtn": false,
                                "dynDestory": true,
                                "dynLoad": false,
                                "entityClass": "net.ooder.esd.dsm.aggregation.ref.AggEntityRef",
                                "groupName": "dsm_agg_entity_config_ref_RefInfo",
                                "id": "loadNav_dsm_agg_entity_config_ref_RefInfo",
                                "imageClass": "ri-node-tree",
                                "initFold": true,
                                "tagVar": {
                                  "name": "refInfo"
                                },
                                "title": "loadNav（null)"
                              },
                              {
                                "bindClass": [
                                  "net.ooder.dsm.aggregation.config.entity.AggEntityInfoService"
                                ],
                                "bindClassName": "net.ooder.dsm.aggregation.config.entity.AggEntityInfoService",
                                "caption": "loadNav（null)",
                                "closeBtn": false,
                                "dynDestory": true,
                                "dynLoad": true,
                                "entityClass": "net.ooder.esd.bean.CustomViewBean",
                                "groupName": "dsm_view_entity_AggEntityInfo",
                                "id": "loadNav_dsm_view_entity_AggEntityInfo",
                                "imageClass": "ri-node-tree",
                                "initFold": true,
                                "tagVar": {
                                  "name": "aggEntityInfo"
                                },
                                "title": "loadNav（null)"
                              },
                              {
                                "bindClass": [
                                  "net.ooder.dsm.aggregation.config.entity.tree.EntityMethodService"
                                ],
                                "bindClassName": "net.ooder.dsm.aggregation.config.entity.tree.EntityMethodService",
                                "caption": "loadNav（null)",
                                "closeBtn": false,
                                "dynDestory": false,
                                "dynLoad": false,
                                "entityClass": "net.ooder.esd.bean.MethodConfig",
                                "groupName": "dsm_agg_entity_config_LoadNav",
                                "id": "loadNav_dsm_agg_entity_config_LoadNav",
                                "imageClass": "ri-node-tree",
                                "initFold": true,
                                "tagVar": {
                                  "name": "loadNav"
                                },
                                "title": "loadNav（null)"
                              },
                              {
                                "bindClass": [
                                  "net.ooder.dsm.aggregation.config.entity.tree.EntityMethodService"
                                ],
                                "bindClassName": "net.ooder.dsm.aggregation.config.entity.tree.EntityMethodService",
                                "caption": "控制调用",
                                "closeBtn": false,
                                "dynDestory": false,
                                "dynLoad": true,
                                "entityClass": "net.ooder.dsm.aggregation.config.entity.tree.AggEntityMethodNavItem",
                                "groupName": "AggEntityMethodNavItem_ViewConfig_dsm_agg_entity_config_LoadNav",
                                "id": "loadNav_ViewConfig",
                                "imageClass": "spafont spa-icon-c-webapi",
                                "initFold": true,
                                "tagVar": {
                                  "name": "ViewConfig"
                                },
                                "title": "控制调用"
                              },
                              {
                                "bindClass": [
                                  "net.ooder.dsm.aggregation.config.entity.AggEntityInfoService",
                                  "net.ooder.dsm.aggregation.config.entity.tree.AggEntityViewNavService"
                                ],
                                "bindClassName": "net.ooder.dsm.aggregation.config.entity.AggEntityInfoService",
                                "caption": "方法调用",
                                "closeBtn": false,
                                "dynDestory": true,
                                "dynLoad": true,
                                "entityClass": "net.ooder.dsm.aggregation.config.entity.tree.AggEntityNavItem",
                                "groupName": "AggEntityNavItem_ViewMethodConfig_dsm_agg_entity_config_view_LoadView",
                                "id": "loadNav_ViewMethodConfig",
                                "imageClass": "spafont spa-icon-project",
                                "initFold": false,
                                "tagVar": {
                                  "name": "ViewMethodConfig"
                                },
                                "title": "方法调用"
                              },
                              {
                                "bindClass": [
                                  "net.ooder.dsm.aggregation.module.event.ModuleEventService"
                                ],
                                "bindClassName": "net.ooder.dsm.aggregation.module.event.ModuleEventService",
                                "caption": "模块事件",
                                "closeBtn": false,
                                "dynDestory": false,
                                "dynLoad": false,
                                "entityClass": "net.ooder.dsm.aggregation.module.ModuleEventNavItem",
                                "groupName": "ModuleEventNavItem_ModuleEventView_dsm_agg_entity_config_module_ModuleEvents",
                                "id": "loadNav_ModuleEventView",
                                "imageClass": "spafont spa-icon-event",
                                "initFold": false,
                                "tagVar": {
                                  "name": "ModuleEventView"
                                },
                                "title": "模块事件"
                              },
                              {
                                "bindClass": [
                                  "net.ooder.dsm.aggregation.api.method.service.MethodBaseService"
                                ],
                                "bindClassName": "net.ooder.dsm.aggregation.api.method.service.MethodBaseService",
                                "caption": "接口配置",
                                "closeBtn": false,
                                "dynDestory": false,
                                "dynLoad": false,
                                "entityClass": "net.ooder.dsm.aggregation.api.method.AggAPINavItem",
                                "groupName": "AggAPINavItem_APIConfig_dsm_agg_api_config_APIBaseMethodInfo",
                                "id": "loadNav_APIConfig",
                                "imageClass": "spafont spa-icon-c-webapi",
                                "initFold": true,
                                "tagVar": {
                                  "name": "APIConfig"
                                },
                                "title": "接口配置"
                              },
                              {
                                "bindClass": [
                                  "net.ooder.dsm.aggregation.config.entity.AggEntityInfoService",
                                  "net.ooder.dsm.aggregation.config.entity.tree.AggEntityMethodNavService"
                                ],
                                "bindClassName": "net.ooder.dsm.aggregation.config.entity.AggEntityInfoService",
                                "caption": "领域事件",
                                "closeBtn": false,
                                "dynDestory": true,
                                "dynLoad": true,
                                "entityClass": "net.ooder.dsm.aggregation.config.entity.tree.AggEntityNavItem",
                                "groupName": "AggEntityNavItem_CustomMethodConfig_dsm_agg_entity_config_method_LoadMethod",
                                "id": "loadNav_CustomMethodConfig",
                                "imageClass": "ri-settings-line",
                                "initFold": true,
                                "tagVar": {
                                  "name": "CustomMethodConfig"
                                },
                                "title": "领域事件"
                              },
                              {
                                "bindClass": [
                                  "net.ooder.dsm.aggregation.module.ModuleConfigService"
                                ],
                                "bindClassName": "net.ooder.dsm.aggregation.module.ModuleConfigService",
                                "caption": "动作事件",
                                "closeBtn": false,
                                "dynDestory": false,
                                "dynLoad": true,
                                "entityClass": "net.ooder.dsm.aggregation.config.entity.tree.AggEntityMethodNavItem",
                                "groupName": "AggEntityMethodNavItem_ModuleConfig_dsm_agg_module_config_LoadModuleNav",
                                "id": "loadNav_ModuleConfig",
                                "imageClass": "ri-play-circle-line",
                                "initFold": true,
                                "tagVar": {
                                  "name": "ModuleConfig"
                                },
                                "title": "动作事件"
                              },
                              {
                                "bindClass": [
                                  "net.ooder.dsm.aggregation.config.entity.tree.AggEntityViewService"
                                ],
                                "bindClassName": "net.ooder.dsm.aggregation.config.entity.tree.AggEntityViewService",
                                "caption": "loadNav（null)",
                                "closeBtn": false,
                                "dynDestory": true,
                                "dynLoad": true,
                                "entityClass": "net.ooder.esd.bean.CustomViewBean",
                                "groupName": "dsm_agg_entity_LoadChildView",
                                "id": "loadNav_dsm_agg_entity_LoadChildView",
                                "imageClass": "ri-node-tree",
                                "initFold": true,
                                "tagVar": {
                                  "name": "loadChildView"
                                },
                                "title": "loadNav（null)"
                              },
                              {
                                "bindClass": [
                                  "net.ooder.dsm.aggregation.config.entity.tree.field.AggEntityFieldsService"
                                ],
                                "bindClassName": "net.ooder.dsm.aggregation.config.entity.tree.field.AggEntityFieldsService",
                                "caption": "条目信息",
                                "closeBtn": false,
                                "dynDestory": true,
                                "dynLoad": true,
                                "entityClass": "net.ooder.dsm.aggregation.config.entity.tree.AggEntityNavItem",
                                "groupName": "AggEntityNavItem_FieldsConfig_dsm_agg_field_LoadFieldList",
                                "id": "loadNav_FieldsConfig",
                                "imageClass": "spafont spa-icon-c-comboinput",
                                "initFold": true,
                                "tagVar": {
                                  "name": "FieldsConfig"
                                },
                                "title": "条目信息"
                              },
                              {
                                "bindClass": [
                                  "net.ooder.dsm.aggregation.module.panel.ModuleWinService",
                                  "net.ooder.dsm.aggregation.module.ModuleDeisignerService"
                                ],
                                "bindClassName": "net.ooder.dsm.aggregation.module.panel.ModuleWinService",
                                "caption": "窗体画布",
                                "closeBtn": false,
                                "dynDestory": false,
                                "dynLoad": true,
                                "entityClass": "net.ooder.dsm.aggregation.config.entity.tree.AggEntityMethodNavItem",
                                "groupName": "AggEntityMethodNavItem_DesignerConfig_dsm_agg_module_config_LoadWinItem",
                                "id": "loadNav_DesignerConfig",
                                "imageClass": "ri-paint-brush-line",
                                "initFold": true,
                                "tagVar": {
                                  "name": "DesignerConfig"
                                },
                                "title": "窗体画布"
                              },
                              {
                                "bindClass": [
                                  "net.ooder.dsm.aggregation.module.action.ModuleActionService"
                                ],
                                "bindClassName": "net.ooder.dsm.aggregation.module.action.ModuleActionService",
                                "caption": "常用动作",
                                "closeBtn": false,
                                "dynDestory": false,
                                "dynLoad": false,
                                "entityClass": "net.ooder.dsm.aggregation.module.ModuleEventNavItem",
                                "groupName": "ModuleEventNavItem_ModuleActionView_dsm_agg_module_config_actions_ModuleActions",
                                "id": "loadNav_ModuleActionView",
                                "imageClass": "spafont spa-icon-action",
                                "initFold": false,
                                "tagVar": {
                                  "name": "ModuleActionView"
                                },
                                "title": "常用动作"
                              },
                              {
                                "bindClass": [
                                  "net.ooder.dsm.aggregation.api.method.service.MethodEventService"
                                ],
                                "bindClassName": "net.ooder.dsm.aggregation.api.method.service.MethodEventService",
                                "caption": "监听事件",
                                "closeBtn": false,
                                "dynDestory": false,
                                "dynLoad": false,
                                "entityClass": "net.ooder.dsm.aggregation.api.method.AggAPINavItem",
                                "groupName": "AggAPINavItem_EventConfig_dsm_agg_api_config_events_CustomEvents",
                                "id": "loadNav_EventConfig",
                                "imageClass": "spafont spa-icon-event",
                                "initFold": false,
                                "tagVar": {
                                  "name": "EventConfig"
                                },
                                "title": "监听事件"
                              },
                              {
                                "bindClass": [
                                  "net.ooder.dsm.aggregation.api.method.service.ViewEventService"
                                ],
                                "bindClassName": "net.ooder.dsm.aggregation.api.method.service.ViewEventService",
                                "caption": "视图绑定事件",
                                "closeBtn": false,
                                "dynDestory": false,
                                "dynLoad": false,
                                "entityClass": "net.ooder.dsm.aggregation.api.method.AggAPINavItem",
                                "groupName": "AggAPINavItem_BindEventConfig_dsm_agg_api_config_events_view_ViewEvents",
                                "id": "loadNav_BindEventConfig",
                                "imageClass": "spafont spa-icon-c-databinder",
                                "initFold": false,
                                "tagVar": {
                                  "name": "BindEventConfig"
                                },
                                "title": "视图绑定事件"
                              },
                              {
                                "bindClass": [
                                  "net.ooder.dsm.aggregation.api.method.service.APIBindMenuService"
                                ],
                                "bindClassName": "net.ooder.dsm.aggregation.api.method.service.APIBindMenuService",
                                "caption": "关联按钮",
                                "closeBtn": false,
                                "dynDestory": false,
                                "dynLoad": false,
                                "entityClass": "net.ooder.dsm.aggregation.api.method.AggAPINavItem",
                                "groupName": "AggAPINavItem_BindConfig_dsm_agg_api_config_bindmenu_APIBindMenus",
                                "id": "loadNav_BindConfig",
                                "imageClass": "spafont spa-icon-values",
                                "initFold": false,
                                "tagVar": {
                                  "name": "BindConfig"
                                },
                                "title": "关联按钮"
                              },
                              {
                                "bindClass": [
                                  "net.ooder.dsm.aggregation.api.method.service.APIBindActionService"
                                ],
                                "bindClassName": "net.ooder.dsm.aggregation.api.method.service.APIBindActionService",
                                "caption": "绑定动作",
                                "closeBtn": false,
                                "dynDestory": false,
                                "dynLoad": false,
                                "entityClass": "net.ooder.dsm.aggregation.api.method.AggAPINavItem",
                                "groupName": "AggAPINavItem_BindActionConfig_dsm_agg_api_config_bindactions_BindActions",
                                "id": "loadNav_BindActionConfig",
                                "imageClass": "spafont spa-icon-values",
                                "initFold": false,
                                "tagVar": {
                                  "name": "BindActionConfig"
                                },
                                "title": "绑定动作"
                              },
                              {
                                "bindClass": [
                                  "net.ooder.dsm.aggregation.config.entity.info.AggregationInfoService"
                                ],
                                "bindClassName": "net.ooder.dsm.aggregation.config.entity.info.AggregationInfoService",
                                "caption": "聚合配置",
                                "closeBtn": false,
                                "dynDestory": false,
                                "dynLoad": true,
                                "entityClass": "net.ooder.dsm.aggregation.config.entity.tree.AggEntityMethodNavItem",
                                "groupName": "AggEntityMethodNavItem_AggInfo_dsm_manager_agg_entity_AggInfo",
                                "id": "loadNav_AggInfo",
                                "imageClass": "spafont spa-icon-rendermode",
                                "initFold": true,
                                "tagVar": {
                                  "name": "AggInfo"
                                },
                                "title": "聚合配置"
                              },
                              {
                                "bindClass": [
                                  "net.ooder.dsm.aggregation.api.method.service.CustomActionService"
                                ],
                                "bindClassName": "net.ooder.dsm.aggregation.api.method.service.CustomActionService",
                                "caption": "回调动作",
                                "closeBtn": false,
                                "dynDestory": false,
                                "dynLoad": false,
                                "entityClass": "net.ooder.dsm.aggregation.api.method.AggAPINavItem",
                                "groupName": "AggAPINavItem_CallBackActionConfig_dsm_agg_api_config_customactions_CustomActions",
                                "id": "loadNav_CallBackActionConfig",
                                "imageClass": "spafont spa-icon-action",
                                "initFold": false,
                                "tagVar": {
                                  "name": "CallBackActionConfig"
                                },
                                "title": "回调动作"
                              }
                            ],
                            "title": "loadNav"
                          }
                        ],
                        "tabindex": 0,
                        "title": "AggMenuConfigTree"
                      }
                    ],
                    "selMode": "single",
                    "singleOpen": false,
                    "tabindex": 0,
                    "visibility": "visible"
                  }
                }
              ],
              "events": {},
              "key": "ood.UI.Block",
              "path": "dsm.view.config.grid.cell.contextmenu.AggConfigTree.AggConfigTreeMain.AggConfigTreeLAYOUT.AggMenuConfigTreePanel",
              "properties": {
                "borderType": "none",
                "dock": "fill",
                "tabindex": 0
              },
              "target": "before"
            },
            {
              "alias": "getAggConfigTree",
              "events": {
                "onItemSelected": {
                  "actions": [
                    {
                      "args": [
                        "{ood.showModule2()}",
                        null,
                        null,
                        "{args[1].euClassName}",
                        "getAggConfigTree",
                        "{args[1].id}",
                        "{args[1].tagVar}",
                        "{page.getData()}",
                        "{page}",
                        "{true}"
                      ],
                      "conditions": [],
                      "desc": "切换窗口",
                      "enumClassName": "net.ooder.esd.annotation.action.CustomLoadClassAction",
                      "enumValue": "tabShow",
                      "eventClass": "net.ooder.esd.annotation.event.TabsEventEnum",
                      "eventValue": "onItemSelected",
                      "id": "net.ooder.esd.annotation.action.CustomLoadClassAction_tabShow_null_null",
                      "method": "showModule2",
                      "redirection": "other:callback:call",
                      "return": false,
                      "target": "url",
                      "type": "other"
                    }
                  ]
                },
                "onIniPanelView": {
                  "actions": [
                    {
                      "args": [
                        "{ood.showModule2()}",
                        null,
                        null,
                        "{args[1].euClassName}",
                        "getAggConfigTree",
                        "{args[1].id}",
                        "{args[1].tagVar}",
                        "{page.getData()}",
                        "{page}",
                        "{true}"
                      ],
                      "conditions": [],
                      "desc": "切换窗口",
                      "enumClassName": "net.ooder.esd.annotation.action.CustomLoadClassAction",
                      "enumValue": "tabShow",
                      "eventClass": "net.ooder.esd.annotation.event.TabsEventEnum",
                      "eventValue": "onIniPanelView",
                      "id": "net.ooder.esd.annotation.action.CustomLoadClassAction_tabShow_null_null",
                      "method": "showModule2",
                      "redirection": "other:callback:call",
                      "return": false,
                      "target": "url",
                      "type": "other"
                    }
                  ]
                }
              },
              "key": "ood.UI.Tabs",
              "path": "dsm.view.config.grid.cell.contextmenu.AggConfigTree.AggConfigTreeMain.AggConfigTreeLAYOUT.getAggConfigTree",
              "properties": {
                "activeLast": true,
                "autoReload": true,
                "autoSave": false,
                "barLocation": "top",
                "barSize": "2.5em",
                "borderType": "none",
                "caption": "菜单配置",
                "id": "aggConfigTree",
                "imageClass": "ri-settings-3-line",
                "initFold": false,
                "items": [],
                "name": "AggConfigTree",
                "noHandler": true,
                "selMode": "single",
                "singleOpen": true,
                "tabindex": 1,
                "tagCmdsAlign": "floatright",
                "tagVar": {}
              },
              "target": "main"
            }
          ],
          "events": {},
          "key": "ood.UI.Layout",
          "path": "dsm.view.config.grid.cell.contextmenu.AggConfigTree.AggConfigTreeMain.AggConfigTreeLAYOUT",
          "properties": {
            "borderType": "none",
            "items": [
              {
                "bindClass": [],
                "flexSize": false,
                "folded": false,
                "id": "before",
                "max": 1000,
                "min": 100,
                "overflow": "auto",
                "pos": "before",
                "size": 260,
                "tabindex": 0,
                "transparent": true
              },
              {
                "bindClass": [],
                "flexSize": true,
                "id": "main",
                "max": 300,
                "overflow": "auto",
                "pos": "main",
                "tabindex": 1,
                "transparent": true
              }
            ],
            "tabindex": 0,
            "transparent": true,
            "type": "horizontal"
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
              "target": "dsm.view.config.grid.cell.contextmenu.AggConfigTree",
              "type": "page"
            }
          ]
        }
      },
      "key": "ood.UI.Dialog",
      "path": "dsm.view.config.grid.cell.contextmenu.AggConfigTree.AggConfigTreeMain",
      "properties": {
        "caption": "菜单配置",
        "defaultFocus": false,
        "disableClickEffect": false,
        "disableHoverEffect": false,
        "disableTips": false,
        "disabled": false,
        "dock": "none",
        "height": "680.0",
        "imageClass": "ri-settings-3-line",
        "left": "220.0",
        "showEffects": "Classic",
        "tabindex": 1,
        "top": "100.0",
        "visibility": "visible",
        "width": "900.0"
      }
    },
    {
      "alias": "RELOAD",
      "events": {},
      "key": "ood.APICaller",
      "path": "dsm.view.config.grid.cell.contextmenu.AggConfigTree.RELOAD",
      "properties": {
        "allform": false,
        "autoRun": true,
        "checkRequired": false,
        "checkValid": false,
        "desc": "菜单配置",
        "id": "/dsm/view/config/grid/cell/contextmenu/AggConfigTree",
        "imageClass": "ri-settings-3-line",
        "isAllform": false,
        "methodName": "getAggConfigTree",
        "name": "AggConfigTree",
        "proxyType": "auto",
        "queryAsync": true,
        "queryMethod": "POST",
        "queryURL": "/dsm/view/config/grid/cell/contextmenu/AggConfigTree",
        "requestDataSource": [
          {
            "name": "PAGECTX",
            "path": "",
            "type": "FORM"
          },
          {
            "name": "AggMenuConfigTreePanel",
            "path": "",
            "type": "FORM"
          }
        ],
        "requestType": "FORM",
        "responseCallback": [],
        "responseDataTarget": [
          {
            "name": "AggMenuConfigTree",
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
        "tips": "菜单配置"
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
              "target": "dsm.view.config.grid.cell.contextmenu.AggConfigTree",
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
              "target": "dsm.view.config.grid.cell.contextmenu.AggConfigTree",
              "type": "page"
            }
          ]
        }
      },
      "key": "ood.APICaller",
      "path": "dsm.view.config.grid.cell.contextmenu.AggConfigTree.SAVE",
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
            "name": "AggMenuConfigTree",
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
    },
    {
      "alias": "dsm_agg_entity_config_LoadNav",
      "events": {},
      "key": "ood.APICaller",
      "path": "dsm.view.config.grid.cell.contextmenu.AggConfigTree.dsm_agg_entity_config_LoadNav",
      "properties": {
        "allform": false,
        "autoRun": false,
        "checkRequired": false,
        "checkValid": false,
        "desc": "loadNav",
        "id": "/dsm/agg/entity/config/loadNav",
        "imageClass": "ri-code-box-line",
        "isAllform": false,
        "methodName": "loadNav",
        "name": "loadNav",
        "proxyType": "auto",
        "queryAsync": true,
        "queryMethod": "POST",
        "queryURL": "/dsm/agg/entity/config/loadNav",
        "requestDataSource": [
          {
            "name": "PAGECTX",
            "path": "",
            "type": "FORM"
          },
          {
            "name": "AggMenuConfigTree",
            "path": "",
            "type": "FORM"
          }
        ],
        "requestType": "FORM",
        "responseCallback": [],
        "responseDataTarget": [],
        "responseType": "JSON",
        "sourceClassName": "net.ooder.dsm.aggregation.config.entity.tree.EntityMethodService",
        "tabindex": 4,
        "tips": "loadNav"
      }
    },
    {
      "alias": "AggMenuMainNavItem_MethodConfig_dsm_agg_menu_config_LoadAllMethod",
      "events": {},
      "key": "ood.APICaller",
      "path": "dsm.view.config.grid.cell.contextmenu.AggConfigTree.AggMenuMainNavItem_MethodConfig_dsm_agg_menu_config_LoadAllMethod",
      "properties": {
        "allform": false,
        "autoRun": false,
        "checkRequired": false,
        "checkValid": false,
        "desc": "loadAllMethod",
        "id": "/dsm/agg/menu/config/loadAllMethod",
        "imageClass": "ri-code-box-line",
        "isAllform": false,
        "methodName": "loadAllMethod",
        "name": "loadAllMethod",
        "proxyType": "auto",
        "queryAsync": true,
        "queryMethod": "POST",
        "queryURL": "/dsm/agg/menu/config/loadAllMethod",
        "requestDataSource": [
          {
            "name": "PAGECTX",
            "path": "",
            "type": "FORM"
          },
          {
            "name": "AggMenuConfigTree",
            "path": "",
            "type": "FORM"
          }
        ],
        "requestType": "FORM",
        "responseCallback": [],
        "responseDataTarget": [],
        "responseType": "JSON",
        "sourceClassName": "net.ooder.dsm.aggregation.config.menu.tree.AggMenuMethodsNavService",
        "tabindex": 5,
        "tips": "loadAllMethod"
      }
    },
    {
      "alias": "AggMenuNavItem_CustomMethodConfig_dsm_agg_menu_config_method_LoadMenuMethod",
      "events": {},
      "key": "ood.APICaller",
      "path": "dsm.view.config.grid.cell.contextmenu.AggConfigTree.AggMenuNavItem_CustomMethodConfig_dsm_agg_menu_config_method_LoadMenuMethod",
      "properties": {
        "allform": false,
        "autoRun": false,
        "checkRequired": false,
        "checkValid": false,
        "desc": "loadMenuMethod",
        "id": "/dsm/agg/menu/config/method/loadMenuMethod",
        "imageClass": "ri-code-box-line",
        "isAllform": false,
        "methodName": "loadMenuMethod",
        "name": "loadMenuMethod",
        "proxyType": "auto",
        "queryAsync": true,
        "queryMethod": "POST",
        "queryURL": "/dsm/agg/menu/config/method/loadMenuMethod",
        "requestDataSource": [
          {
            "name": "PAGECTX",
            "path": "",
            "type": "FORM"
          },
          {
            "name": "AggMenuConfigTree",
            "path": "",
            "type": "FORM"
          }
        ],
        "requestType": "FORM",
        "responseCallback": [],
        "responseDataTarget": [],
        "responseType": "JSON",
        "sourceClassName": "net.ooder.dsm.aggregation.config.menu.tree.AggMenuMethodNavService",
        "tabindex": 7,
        "tips": "loadMenuMethod"
      }
    },
    {
      "alias": "AggEntityMethodNavItem_ViewConfig_dsm_agg_entity_config_LoadNav",
      "events": {},
      "key": "ood.APICaller",
      "path": "dsm.view.config.grid.cell.contextmenu.AggConfigTree.AggEntityMethodNavItem_ViewConfig_dsm_agg_entity_config_LoadNav",
      "properties": {
        "allform": false,
        "autoRun": false,
        "checkRequired": false,
        "checkValid": false,
        "desc": "loadNav",
        "id": "/dsm/agg/entity/config/loadNav",
        "imageClass": "ri-code-box-line",
        "isAllform": false,
        "methodName": "loadNav",
        "name": "loadNav",
        "proxyType": "auto",
        "queryAsync": true,
        "queryMethod": "POST",
        "queryURL": "/dsm/agg/entity/config/loadNav",
        "requestDataSource": [
          {
            "name": "PAGECTX",
            "path": "",
            "type": "FORM"
          },
          {
            "name": "AggMenuConfigTree",
            "path": "",
            "type": "FORM"
          }
        ],
        "requestType": "FORM",
        "responseCallback": [],
        "responseDataTarget": [],
        "responseType": "JSON",
        "sourceClassName": "net.ooder.dsm.aggregation.config.entity.tree.EntityMethodService",
        "tabindex": 8,
        "tips": "loadNav"
      }
    },
    {
      "alias": "AggEntityNavItem_ViewMethodConfig_dsm_agg_entity_config_view_LoadView",
      "events": {},
      "key": "ood.APICaller",
      "path": "dsm.view.config.grid.cell.contextmenu.AggConfigTree.AggEntityNavItem_ViewMethodConfig_dsm_agg_entity_config_view_LoadView",
      "properties": {
        "allform": false,
        "autoRun": false,
        "checkRequired": false,
        "checkValid": false,
        "desc": "loadView",
        "id": "/dsm/agg/entity/config/view/loadView",
        "imageClass": "ri-code-box-line",
        "isAllform": false,
        "methodName": "loadView",
        "name": "loadView",
        "proxyType": "auto",
        "queryAsync": true,
        "queryMethod": "POST",
        "queryURL": "/dsm/agg/entity/config/view/loadView",
        "requestDataSource": [
          {
            "name": "PAGECTX",
            "path": "",
            "type": "FORM"
          },
          {
            "name": "AggMenuConfigTree",
            "path": "",
            "type": "FORM"
          }
        ],
        "requestType": "FORM",
        "responseCallback": [],
        "responseDataTarget": [],
        "responseType": "JSON",
        "sourceClassName": "net.ooder.dsm.aggregation.config.entity.tree.AggEntityViewNavService",
        "tabindex": 9,
        "tips": "loadView"
      }
    },
    {
      "alias": "AggEntityNavItem_CustomMethodConfig_dsm_agg_entity_config_method_LoadMethod",
      "events": {},
      "key": "ood.APICaller",
      "path": "dsm.view.config.grid.cell.contextmenu.AggConfigTree.AggEntityNavItem_CustomMethodConfig_dsm_agg_entity_config_method_LoadMethod",
      "properties": {
        "allform": false,
        "autoRun": false,
        "checkRequired": false,
        "checkValid": false,
        "desc": "loadMethod",
        "id": "/dsm/agg/entity/config/method/loadMethod",
        "imageClass": "ri-code-box-line",
        "isAllform": false,
        "methodName": "loadMethod",
        "name": "loadMethod",
        "proxyType": "auto",
        "queryAsync": true,
        "queryMethod": "POST",
        "queryURL": "/dsm/agg/entity/config/method/loadMethod",
        "requestDataSource": [
          {
            "name": "PAGECTX",
            "path": "",
            "type": "FORM"
          },
          {
            "name": "AggMenuConfigTree",
            "path": "",
            "type": "FORM"
          }
        ],
        "requestType": "FORM",
        "responseCallback": [],
        "responseDataTarget": [],
        "responseType": "JSON",
        "sourceClassName": "net.ooder.dsm.aggregation.config.entity.tree.AggEntityMethodNavService",
        "tabindex": 10,
        "tips": "loadMethod"
      }
    },
    {
      "alias": "AggEntityMethodNavItem_ModuleConfig_dsm_agg_module_config_LoadModuleNav",
      "events": {},
      "key": "ood.APICaller",
      "path": "dsm.view.config.grid.cell.contextmenu.AggConfigTree.AggEntityMethodNavItem_ModuleConfig_dsm_agg_module_config_LoadModuleNav",
      "properties": {
        "allform": false,
        "autoRun": false,
        "checkRequired": false,
        "checkValid": false,
        "desc": "loadWinNav",
        "id": "/dsm/agg/module/config/loadModuleNav",
        "imageClass": "ri-code-box-line",
        "isAllform": false,
        "methodName": "loadWinNav",
        "name": "loadModuleNav",
        "proxyType": "auto",
        "queryAsync": true,
        "queryMethod": "POST",
        "queryURL": "/dsm/agg/module/config/loadModuleNav",
        "requestDataSource": [
          {
            "name": "PAGECTX",
            "path": "",
            "type": "FORM"
          },
          {
            "name": "AggMenuConfigTree",
            "path": "",
            "type": "FORM"
          }
        ],
        "requestType": "FORM",
        "responseCallback": [],
        "responseDataTarget": [],
        "responseType": "JSON",
        "sourceClassName": "net.ooder.dsm.aggregation.module.ModuleConfigService",
        "tabindex": 11,
        "tips": "loadWinNav"
      }
    },
    {
      "alias": "dsm_agg_entity_LoadChildView",
      "events": {},
      "key": "ood.APICaller",
      "path": "dsm.view.config.grid.cell.contextmenu.AggConfigTree.dsm_agg_entity_LoadChildView",
      "properties": {
        "allform": false,
        "autoRun": false,
        "checkRequired": false,
        "checkValid": false,
        "desc": "loadChildView",
        "id": "/dsm/agg/entity/loadChildView",
        "imageClass": "ri-code-box-line",
        "isAllform": false,
        "methodName": "loadChildView",
        "name": "loadChildView",
        "proxyType": "auto",
        "queryAsync": true,
        "queryMethod": "POST",
        "queryURL": "/dsm/agg/entity/loadChildView",
        "requestDataSource": [
          {
            "name": "PAGECTX",
            "path": "",
            "type": "FORM"
          },
          {
            "name": "AggMenuConfigTree",
            "path": "",
            "type": "FORM"
          }
        ],
        "requestType": "FORM",
        "responseCallback": [],
        "responseDataTarget": [],
        "responseType": "JSON",
        "sourceClassName": "net.ooder.dsm.aggregation.config.entity.tree.AggEntityViewService",
        "tabindex": 11,
        "tips": "loadChildView"
      }
    },
    {
      "alias": "AggEntityNavItem_FieldsConfig_dsm_agg_field_LoadFieldList",
      "events": {},
      "key": "ood.APICaller",
      "path": "dsm.view.config.grid.cell.contextmenu.AggConfigTree.AggEntityNavItem_FieldsConfig_dsm_agg_field_LoadFieldList",
      "properties": {
        "allform": false,
        "autoRun": false,
        "checkRequired": false,
        "checkValid": false,
        "desc": "loadFieldList",
        "id": "/dsm/agg/field/loadFieldList",
        "imageClass": "ri-code-box-line",
        "isAllform": false,
        "methodName": "loadFieldList",
        "name": "loadFieldList",
        "proxyType": "auto",
        "queryAsync": true,
        "queryMethod": "POST",
        "queryURL": "/dsm/agg/field/loadFieldList",
        "requestDataSource": [
          {
            "name": "PAGECTX",
            "path": "",
            "type": "FORM"
          },
          {
            "name": "AggMenuConfigTree",
            "path": "",
            "type": "FORM"
          }
        ],
        "requestType": "FORM",
        "responseCallback": [],
        "responseDataTarget": [],
        "responseType": "JSON",
        "sourceClassName": "net.ooder.dsm.aggregation.config.entity.tree.field.AggEntityFieldsService",
        "tabindex": 12,
        "tips": "loadFieldList"
      }
    },
    {
      "alias": "AggEntityMethodNavItem_DesignerConfig_dsm_agg_module_config_LoadWinItem",
      "events": {},
      "key": "ood.APICaller",
      "path": "dsm.view.config.grid.cell.contextmenu.AggConfigTree.AggEntityMethodNavItem_DesignerConfig_dsm_agg_module_config_LoadWinItem",
      "properties": {
        "allform": false,
        "autoRun": false,
        "checkRequired": false,
        "checkValid": false,
        "desc": "loadWinItem",
        "id": "/dsm/agg/module/config/loadWinItem",
        "imageClass": "ri-code-box-line",
        "isAllform": false,
        "methodName": "loadWinItem",
        "name": "loadWinItem",
        "proxyType": "auto",
        "queryAsync": true,
        "queryMethod": "POST",
        "queryURL": "/dsm/agg/module/config/loadWinItem",
        "requestDataSource": [
          {
            "name": "PAGECTX",
            "path": "",
            "type": "FORM"
          },
          {
            "name": "AggMenuConfigTree",
            "path": "",
            "type": "FORM"
          }
        ],
        "requestType": "FORM",
        "responseCallback": [],
        "responseDataTarget": [],
        "responseType": "JSON",
        "sourceClassName": "net.ooder.dsm.aggregation.module.panel.ModuleWinService",
        "tabindex": 13,
        "tips": "loadWinItem"
      }
    }
  ],
  "className": "dsm.view.config.grid.cell.contextmenu.AggConfigTree",
  "customFunctions": {},
  "desc": "菜单配置",
  "dio": true,
  "events": {},
  "formulas": [],
  "itemType": "TREEVIEW",
  "key": "ood.Module",
  "moduleType": "EUClass",
  "moduleVar": {},
  "moduleViewType": "NAVTREECONFIG",
  "path": "dsm.view.config.grid.cell.contextmenu.AggConfigTree",
  "properties": {
    "autoDestroy": true,
    "bindClass": [],
    "caption": "菜单配置",
    "dock": "fill",
    "title": "菜单配置"
  },
  "saveUrl": "/dsm/view/config/grid/cell/contextmenu/saveCellMenuClass"
}