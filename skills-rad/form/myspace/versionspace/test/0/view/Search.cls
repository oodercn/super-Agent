{
  "Static": {
    "designViewConf": {
      "touchDevice": false
    },
    "viewStyles": {}
  },
  "alias": "Search",
  "children": [
    {
      "alias": "SearchMain",
      "children": [
        {
          "alias": "Search",
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
                  "target": "demo.Search",
                  "type": "page"
                }
              ]
            },
            "onCmd": {
              "actions": [
                {
                  "args": [
                    "{page.getTestForm.setQueryData()}",
                    null,
                    null,
                    "{args[1].tagVar}",
                    ""
                  ],
                  "conditions": [
                    {
                      "symbol": "=",
                      "left": "{args[2]}",
                      "right": "getTestFormbutton"
                    }
                  ],
                  "desc": "创建 数据库表",
                  "method": "setQueryData",
                  "redirection": "other:callback:call",
                  "target": "getTestForm",
                  "type": "control"
                },
                {
                  "args": [
                    "{page.getTestForm.invoke()}"
                  ],
                  "conditions": [
                    {
                      "symbol": "=",
                      "left": "{args[2]}",
                      "right": "getTestFormbutton"
                    }
                  ],
                  "desc": "创建 数据库表",
                  "method": "invoke",
                  "redirection": "other:callback:call",
                  "return": false,
                  "target": "getTestForm",
                  "type": "control"
                }
              ]
            }
          },
          "key": "ood.UI.TreeGrid",
          "path": "test.view.Search.SearchMain.Search",
          "properties": {
            "desc": "Search",
            "editMode": "inline",
            "header": [
              {
                "caption": "personId",
                "hidden": true,
                "id": "personId",
                "tips": "personId",
                "title": "personId",
                "type": "input",
                "width": "8.0em"
              },
              {
                "caption": "姓名",
                "hidden": false,
                "id": "name",
                "tips": "姓名",
                "title": "姓名",
                "type": "input",
                "width": "150.0"
              },
              {
                "caption": "性別",
                "editorListItems": [
                  {
                    "bindClass": [],
                    "caption": "男",
                    "id": "men",
                    "name": "男",
                    "tagVar": {
                      "name": "men",
                      "clazz": "net.ooder.test.SexEnum"
                    },
                    "title": "男"
                  },
                  {
                    "bindClass": [],
                    "caption": "女",
                    "id": "women",
                    "name": "女",
                    "tagVar": {
                      "name": "women",
                      "clazz": "net.ooder.test.SexEnum"
                    },
                    "title": "女"
                  }
                ],
                "enumClass": "net.ooder.test.SexEnum",
                "hidden": false,
                "id": "sex",
                "tips": "性別",
                "title": "性別",
                "type": "input",
                "width": "150.0"
              },
              {
                "caption": "tagCmds",
                "hidden": true,
                "id": "tagCmds",
                "tips": "tagCmds",
                "title": "tagCmds",
                "type": "input",
                "width": "8.0em"
              },
              {
                "caption": "年龄",
                "flexSize": true,
                "hidden": false,
                "id": "age",
                "tips": "年龄",
                "title": "年龄",
                "type": "spin",
                "width": "150.0"
              }
            ],
            "headerHeight": "2.0em",
            "name": "Search",
            "rowHandler": true,
            "rowHandlerWidth": "5.0em",
            "rowHeight": "3.0em",
            "rowNumbered": true,
            "selMode": "multibycheckbox",
            "showHeader": true,
            "tagCmds": [
              {
                "buttonType": "text",
                "caption": "",
                "id": "getTestFormbutton",
                "imageClass": "fa-solid fa-code",
                "index": 0,
                "pos": "row",
                "tagCmdsAlign": "right",
                "tips": "创建 数据库表"
              }
            ],
            "uidColumn": "personId",
            "valueSeparator": ";"
          }
        },
        {
          "alias": "MENUBARMenu",
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
                  "target": "demo.TestForm",
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
          "key": "ood.UI.MenuBar",
          "path": "test.view.Search.SearchMain.MENUBARMenu",
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
                "id": "DELETE_button",
                "imageClass": "ri-times-line",
                "tagVar": {},
                "tips": "删除",
                "title": "删除",
                "type": "button"
              },
              {
                "bindClass": [],
                "caption": "添加",
                "expression": "true",
                "id": "ADD_button",
                "imageClass": "ri-plus-line",
                "tagVar": {},
                "tips": "添加",
                "title": "添加",
                "type": "button"
              },
              {
                "bindClass": [],
                "caption": "刷新",
                "expression": "true",
                "id": "RELOAD_button",
                "imageClass": "ri-sync-alt-line",
                "tagVar": {},
                "tips": "刷新",
                "title": "刷新",
                "type": "button"
              }
            ],
            "lazy": false,
            "name": "MENUBARMenu",
            "showCaption": true
          }
        }
      ],
      "events": {},
      "key": "ood.UI.Block",
      "path": "test.view.Search.SearchMain",
      "properties": {
        "borderType": "none",
        "dock": "fill",
        "name": "SearchMain"
      }
    },
    {
      "alias": "RELOAD",
      "events": {},
      "key": "ood.APICaller",
      "path": "test.view.Search.RELOAD",
      "properties": {
        "autoRun": true,
        "currClassName": "demo.Search",
        "desc": "Search",
        "id": "/demo/Search",
        "imageClass": "fa-solid fa-code",
        "methodName": "Search",
        "name": "Search",
        "proxyType": "auto",
        "queryMethod": "POST",
        "queryURL": "/demo/Search",
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
            "name": "PAGECTX",
            "path": "",
            "type": "FORM"
          }
        ],
        "requestType": "JSON",
        "responseCallback": [],
        "responseDataTarget": [
          {
            "name": "Search",
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
        "sourceClassName": "net.ooder.test.TestService",
        "tips": "Search"
      }
    },
    {
      "alias": "getTestForm",
      "events": {},
      "key": "ood.APICaller",
      "path": "test.view.Search.getTestForm",
      "properties": {
        "allform": false,
        "autoRun": false,
        "currClassName": "demo.Search",
        "desc": "创建 数据库表",
        "id": "/demo/CreateTable",
        "imageClass": "fa-solid fa-code",
        "isAllform": false,
        "methodName": "getTestForm",
        "name": "CreateTable",
        "proxyType": "auto",
        "queryAsync": true,
        "queryMethod": "POST",
        "queryURL": "/demo/CreateTable",
        "requestDataSource": [
          {
            "name": "PAGECTX",
            "path": "",
            "type": "FORM"
          }
        ],
        "requestType": "FORM",
        "responseCallback": [],
        "responseDataTarget": [],
        "responseType": "TEXT",
        "sourceClassName": "net.ooder.test.OtherButton",
        "tips": "getTestForm"
      }
    },
    {
      "alias": "PAGECTX",
      "children": [
        {
          "alias": "testView",
          "events": {},
          "key": "ood.UI.HiddenInput",
          "path": "test.view.Search.PAGECTX.testView",
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
          "path": "test.view.Search.PAGECTX.projectName",
          "properties": {
            "formField": true,
            "isPid": false,
            "name": "projectName",
            "pid": false,
            "value": "test"
          }
        },
        {
          "alias": "projectVersionName",
          "events": {},
          "key": "ood.UI.HiddenInput",
          "path": "test.view.Search.PAGECTX.projectVersionName",
          "properties": {
            "formField": true,
            "isPid": false,
            "name": "projectVersionName",
            "pid": false,
            "value": "test"
          }
        },
        {
          "alias": "personId",
          "events": {},
          "key": "ood.UI.HiddenInput",
          "path": "test.view.Search.PAGECTX.personId",
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
      "params": {
        "projectName": "test",
        "projectVersionName": "test"
      },
      "path": "test.view.Search.PAGECTX",
      "properties": {
        "borderType": "none",
        "name": "PAGECTX",
        "visibility": "hidden"
      }
    }
  ],
  "className": "test.view.Search",
  "customFunctions": {},
  "dio": false,
  "events": {},
  "formulas": [],
  "key": "ood.Module",
  "moduleType": "EUClass",
  "moduleVar": {},
  "moduleViewType": "GRIDCONFIG",
  "path": "test.view.Search",
  "properties": {
    "autoDestroy": true,
    "bindClass": [
      "net.ooder.test.TestService"
    ],
    "bindClassName": "net.ooder.test.TestService",
    "bindService": "net.ooder.test.TestService",
    "currComponentAlias": "Search",
    "dock": "fill",
    "dsmProperties": {
      "domainId": "TEST_VIEW",
      "moduleViewType": "GRIDCONFIG",
      "projectName": "test",
      "realPath": "demo.search",
      "sourceClassName": "net.ooder.test.TestService",
      "sourceMethodName": "Search"
    },
    "dynLoad": false,
    "panelType": "block",
    "src": "/demo/Search",
    "viewClass": "net.ooder.esd.custom.component.grid.FullGridComponent"
  }
}