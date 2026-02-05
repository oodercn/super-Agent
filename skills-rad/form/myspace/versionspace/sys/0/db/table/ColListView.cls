{
  "Static": {
    "designViewConf": {
      "touchDevice": false
    },
    "viewStyles": {}
  },
  "alias": "ColListView",
  "children": [
    {
      "alias": "ColListView",
      "events": {},
      "key": "ood.UI.Block",
      "path": "db.table.ColListView.ColListView",
      "properties": {
        "borderType": "none",
        "dock": "fill",
        "panelBgClr": "transparent"
      }
    },
    {
      "alias": "PAGECTX",
      "children": [
        {
          "alias": "tablename",
          "events": {},
          "key": "ood.UI.HiddenInput",
          "path": "db.table.ColListView.PAGECTX.tablename",
          "properties": {
            "formField": true,
            "isPid": true,
            "name": "tablename",
            "pid": true
          }
        },
        {
          "alias": "projectName",
          "events": {},
          "key": "ood.UI.HiddenInput",
          "path": "db.table.ColListView.PAGECTX.projectName",
          "properties": {
            "formField": true,
            "isPid": false,
            "name": "projectName",
            "pid": false,
            "value": "sysVVVERSION0"
          }
        },
        {
          "alias": "projectVersionName",
          "events": {},
          "key": "ood.UI.HiddenInput",
          "path": "db.table.ColListView.PAGECTX.projectVersionName",
          "properties": {
            "formField": true,
            "isPid": false,
            "name": "projectVersionName",
            "pid": false,
            "value": "sysVVVERSION0"
          }
        },
        {
          "alias": "name",
          "events": {},
          "key": "ood.UI.HiddenInput",
          "path": "db.table.ColListView.PAGECTX.name",
          "properties": {
            "formField": true,
            "isPid": true,
            "name": "name",
            "pid": true
          }
        },
        {
          "alias": "projectId",
          "events": {},
          "key": "ood.UI.HiddenInput",
          "path": "db.table.ColListView.PAGECTX.projectId",
          "properties": {
            "formField": true,
            "isPid": true,
            "name": "projectId",
            "pid": true
          }
        },
        {
          "alias": "dataType",
          "events": {},
          "key": "ood.UI.HiddenInput",
          "path": "db.table.ColListView.PAGECTX.dataType",
          "properties": {
            "formField": true,
            "isPid": false,
            "name": "dataType",
            "pid": false
          }
        },
        {
          "alias": "url",
          "events": {},
          "key": "ood.UI.HiddenInput",
          "path": "db.table.ColListView.PAGECTX.url",
          "properties": {
            "formField": true,
            "isPid": false,
            "name": "url",
            "pid": false
          }
        },
        {
          "alias": "configKey",
          "events": {},
          "key": "ood.UI.HiddenInput",
          "path": "db.table.ColListView.PAGECTX.configKey",
          "properties": {
            "formField": true,
            "isPid": true,
            "name": "configKey",
            "pid": true
          }
        }
      ],
      "events": {},
      "key": "ood.UI.Block",
      "params": {},
      "path": "db.table.ColListView.PAGECTX",
      "properties": {
        "borderType": "none",
        "visibility": "hidden"
      }
    },
    {
      "alias": "ColListViewMain",
      "children": [
        {
          "alias": "Cols",
          "events": {
            "onCmd": {
              "actions": [
                {
                  "args": [
                    "{page.del.setQueryData()}",
                    null,
                    null,
                    "{args[1].tagVar}",
                    ""
                  ],
                  "conditions": [
                    {
                      "symbol": "=",
                      "left": "{args[2]}",
                      "right": "delbutton"
                    }
                  ],
                  "desc": "删除",
                  "eventClass": "net.ooder.esd.annotation.event.GridEventEnum",
                  "eventValue": "onCmd",
                  "method": "setQueryData",
                  "redirection": "other:callback:call",
                  "target": "del",
                  "type": "control"
                },
                {
                  "args": [
                    "{page.del.invoke()}"
                  ],
                  "conditions": [
                    {
                      "symbol": "=",
                      "left": "{args[2]}",
                      "right": "delbutton"
                    }
                  ],
                  "desc": "删除",
                  "eventClass": "net.ooder.esd.annotation.event.GridEventEnum",
                  "eventValue": "onCmd",
                  "method": "invoke",
                  "redirection": "other:callback:call",
                  "return": false,
                  "target": "del",
                  "type": "control"
                },
                {
                  "args": [
                    "{page.moveDown.setQueryData()}",
                    null,
                    null,
                    "{args[1].tagVar}",
                    ""
                  ],
                  "conditions": [
                    {
                      "symbol": "=",
                      "left": "{args[2]}",
                      "right": "moveDownbutton"
                    }
                  ],
                  "desc": "向下",
                  "eventClass": "net.ooder.esd.annotation.event.GridEventEnum",
                  "eventValue": "onCmd",
                  "method": "setQueryData",
                  "redirection": "other:callback:call",
                  "target": "moveDown",
                  "type": "control"
                },
                {
                  "args": [
                    "{page.moveDown.invoke()}"
                  ],
                  "conditions": [
                    {
                      "symbol": "=",
                      "left": "{args[2]}",
                      "right": "moveDownbutton"
                    }
                  ],
                  "desc": "向下",
                  "eventClass": "net.ooder.esd.annotation.event.GridEventEnum",
                  "eventValue": "onCmd",
                  "method": "invoke",
                  "redirection": "other:callback:call",
                  "return": false,
                  "target": "moveDown",
                  "type": "control"
                },
                {
                  "args": [
                    "{page.moveUP.setQueryData()}",
                    null,
                    null,
                    "{args[1].tagVar}",
                    ""
                  ],
                  "conditions": [
                    {
                      "symbol": "=",
                      "left": "{args[2]}",
                      "right": "moveUPbutton"
                    }
                  ],
                  "desc": "向上",
                  "eventClass": "net.ooder.esd.annotation.event.GridEventEnum",
                  "eventValue": "onCmd",
                  "method": "setQueryData",
                  "redirection": "other:callback:call",
                  "target": "moveUP",
                  "type": "control"
                },
                {
                  "args": [
                    "{page.moveUP.invoke()}"
                  ],
                  "conditions": [
                    {
                      "symbol": "=",
                      "left": "{args[2]}",
                      "right": "moveUPbutton"
                    }
                  ],
                  "desc": "向上",
                  "eventClass": "net.ooder.esd.annotation.event.GridEventEnum",
                  "eventValue": "onCmd",
                  "method": "invoke",
                  "redirection": "other:callback:call",
                  "return": false,
                  "target": "moveUP",
                  "type": "control"
                }
              ]
            }
          },
          "key": "ood.UI.TreeGrid",
          "path": "db.table.ColListView.ColListViewMain.Cols",
          "properties": {
            "desc": "getCols",
            "editMode": "inline",
            "header": [
              {
                "caption": "字段名",
                "hidden": false,
                "id": "name",
                "tips": "字段名",
                "title": "字段名",
                "type": "input"
              },
              {
                "caption": "类型",
                "editorListItems": [
                  {
                    "bindClass": [],
                    "caption": "字符串",
                    "id": "VARCHAR",
                    "name": "字符串",
                    "pattern": "",
                    "tagVar": {
                      "name": "VARCHAR",
                      "clazz": "net.ooder.annotation.ColType"
                    },
                    "title": "字符串"
                  },
                  {
                    "bindClass": [],
                    "caption": "日期",
                    "id": "DATETIME",
                    "name": "日期",
                    "pattern": "",
                    "tagVar": {
                      "name": "DATETIME",
                      "clazz": "net.ooder.annotation.ColType"
                    },
                    "title": "日期"
                  },
                  {
                    "bindClass": [],
                    "caption": "大文本",
                    "id": "TEXT",
                    "name": "大文本",
                    "pattern": "",
                    "tagVar": {
                      "name": "TEXT",
                      "clazz": "net.ooder.annotation.ColType"
                    },
                    "title": "大文本"
                  },
                  {
                    "bindClass": [],
                    "caption": "整数",
                    "id": "INTEGER",
                    "name": "整数",
                    "pattern": "",
                    "tagVar": {
                      "name": "INTEGER",
                      "clazz": "net.ooder.annotation.ColType"
                    },
                    "title": "整数"
                  },
                  {
                    "bindClass": [],
                    "caption": "数字",
                    "id": "FLOAT",
                    "name": "数字",
                    "pattern": "",
                    "tagVar": {
                      "name": "FLOAT",
                      "clazz": "net.ooder.annotation.ColType"
                    },
                    "title": "数字"
                  },
                  {
                    "bindClass": [],
                    "caption": "BOOLEAN",
                    "id": "BOOLEAN",
                    "name": "BOOLEAN",
                    "pattern": "",
                    "tagVar": {
                      "name": "BOOLEAN",
                      "clazz": "net.ooder.annotation.ColType"
                    },
                    "title": "BOOLEAN"
                  },
                  {
                    "bindClass": [],
                    "caption": "字符串",
                    "id": "VARCHAR2",
                    "name": "字符串",
                    "pattern": "",
                    "tagVar": {
                      "name": "VARCHAR2",
                      "clazz": "net.ooder.annotation.ColType"
                    },
                    "title": "字符串"
                  },
                  {
                    "bindClass": [],
                    "caption": "二进制",
                    "id": "BLOB",
                    "name": "二进制",
                    "pattern": "",
                    "tagVar": {
                      "name": "BLOB",
                      "clazz": "net.ooder.annotation.ColType"
                    },
                    "title": "二进制"
                  },
                  {
                    "bindClass": [],
                    "caption": "CHAR",
                    "id": "CHAR",
                    "name": "CHAR",
                    "pattern": "",
                    "tagVar": {
                      "name": "CHAR",
                      "clazz": "net.ooder.annotation.ColType"
                    },
                    "title": "CHAR"
                  },
                  {
                    "bindClass": [],
                    "caption": "TINYINT",
                    "id": "TINYINT",
                    "name": "TINYINT",
                    "pattern": "",
                    "tagVar": {
                      "name": "TINYINT",
                      "clazz": "net.ooder.annotation.ColType"
                    },
                    "title": "TINYINT"
                  },
                  {
                    "bindClass": [],
                    "caption": "SMALLINT",
                    "id": "SMALLINT",
                    "name": "SMALLINT",
                    "pattern": "",
                    "tagVar": {
                      "name": "SMALLINT",
                      "clazz": "net.ooder.annotation.ColType"
                    },
                    "title": "SMALLINT"
                  },
                  {
                    "bindClass": [],
                    "caption": "枚举",
                    "id": "ENUM",
                    "name": "枚举",
                    "pattern": "",
                    "tagVar": {
                      "name": "ENUM",
                      "clazz": "net.ooder.annotation.ColType"
                    },
                    "title": "枚举"
                  },
                  {
                    "bindClass": [],
                    "caption": "MEDIUMINT",
                    "id": "MEDIUMINT",
                    "name": "MEDIUMINT",
                    "pattern": "",
                    "tagVar": {
                      "name": "MEDIUMINT",
                      "clazz": "net.ooder.annotation.ColType"
                    },
                    "title": "MEDIUMINT"
                  },
                  {
                    "bindClass": [],
                    "caption": "BIT",
                    "id": "BIT",
                    "name": "BIT",
                    "pattern": "",
                    "tagVar": {
                      "name": "BIT",
                      "clazz": "net.ooder.annotation.ColType"
                    },
                    "title": "BIT"
                  },
                  {
                    "bindClass": [],
                    "caption": "BIGINT",
                    "id": "BIGINT",
                    "name": "BIGINT",
                    "pattern": "",
                    "tagVar": {
                      "name": "BIGINT",
                      "clazz": "net.ooder.annotation.ColType"
                    },
                    "title": "BIGINT"
                  },
                  {
                    "bindClass": [],
                    "caption": "DOUBLE",
                    "id": "DOUBLE",
                    "name": "DOUBLE",
                    "pattern": "",
                    "tagVar": {
                      "name": "DOUBLE",
                      "clazz": "net.ooder.annotation.ColType"
                    },
                    "title": "DOUBLE"
                  },
                  {
                    "bindClass": [],
                    "caption": "可变长度",
                    "id": "DECIMAL",
                    "name": "可变长度",
                    "pattern": "",
                    "tagVar": {
                      "name": "DECIMAL",
                      "clazz": "net.ooder.annotation.ColType"
                    },
                    "title": "可变长度"
                  },
                  {
                    "bindClass": [],
                    "caption": "集合",
                    "id": "SET",
                    "name": "集合",
                    "pattern": "",
                    "tagVar": {
                      "name": "SET",
                      "clazz": "net.ooder.annotation.ColType"
                    },
                    "title": "集合"
                  },
                  {
                    "bindClass": [],
                    "caption": "JSON",
                    "id": "JSON",
                    "name": "JSON",
                    "pattern": "",
                    "tagVar": {
                      "name": "JSON",
                      "clazz": "net.ooder.annotation.ColType"
                    },
                    "title": "JSON"
                  },
                  {
                    "bindClass": [],
                    "caption": "几何类型",
                    "id": "Geometry",
                    "name": "几何类型",
                    "pattern": "",
                    "tagVar": {
                      "name": "Geometry",
                      "clazz": "net.ooder.annotation.ColType"
                    },
                    "title": "几何类型"
                  },
                  {
                    "bindClass": [],
                    "caption": "ID",
                    "id": "ID",
                    "name": "ID",
                    "pattern": "",
                    "tagVar": {
                      "name": "ID",
                      "clazz": "net.ooder.annotation.ColType"
                    },
                    "title": "ID"
                  },
                  {
                    "bindClass": [],
                    "caption": "整形",
                    "id": "INT",
                    "name": "整形",
                    "pattern": "",
                    "tagVar": {
                      "name": "INT",
                      "clazz": "net.ooder.annotation.ColType"
                    },
                    "title": "整形"
                  },
                  {
                    "bindClass": [],
                    "caption": "DATE",
                    "id": "DATE",
                    "name": "DATE",
                    "pattern": "",
                    "tagVar": {
                      "name": "DATE",
                      "clazz": "net.ooder.annotation.ColType"
                    },
                    "title": "DATE"
                  },
                  {
                    "bindClass": [],
                    "caption": "TIME",
                    "id": "TIME",
                    "name": "TIME",
                    "pattern": "",
                    "tagVar": {
                      "name": "TIME",
                      "clazz": "net.ooder.annotation.ColType"
                    },
                    "title": "TIME"
                  },
                  {
                    "bindClass": [],
                    "caption": "TIMESTAMP",
                    "id": "TIMESTAMP",
                    "name": "TIMESTAMP",
                    "pattern": "",
                    "tagVar": {
                      "name": "TIMESTAMP",
                      "clazz": "net.ooder.annotation.ColType"
                    },
                    "title": "TIMESTAMP"
                  },
                  {
                    "bindClass": [],
                    "caption": "YEAR",
                    "id": "YEAR",
                    "name": "YEAR",
                    "pattern": "",
                    "tagVar": {
                      "name": "YEAR",
                      "clazz": "net.ooder.annotation.ColType"
                    },
                    "title": "YEAR"
                  }
                ],
                "enumClass": "net.ooder.annotation.ColType",
                "hidden": false,
                "id": "colType",
                "tips": "类型",
                "title": "类型",
                "type": "listbox"
              },
              {
                "caption": "长度",
                "hidden": false,
                "id": "length",
                "tips": "长度",
                "title": "长度",
                "type": "number",
                "width": "6.0em"
              },
              {
                "caption": "数字精度",
                "hidden": false,
                "id": "fractions",
                "tips": "数字精度",
                "title": "数字精度",
                "type": "number",
                "width": "6.0em"
              },
              {
                "caption": "是否主键",
                "hidden": false,
                "id": "pk",
                "tips": "是否主键",
                "title": "是否主键",
                "type": "checkbox"
              },
              {
                "caption": "是否可为空",
                "hidden": false,
                "id": "canNull",
                "tips": "是否可为空",
                "title": "是否可为空",
                "type": "checkbox"
              },
              {
                "caption": "注解",
                "flexSize": true,
                "hidden": false,
                "id": "cnname",
                "tips": "注解",
                "title": "注解",
                "type": "input",
                "width": "150.0"
              },
              {
                "caption": "工程名",
                "hidden": true,
                "id": "projectId",
                "tips": "工程名",
                "title": "工程名",
                "type": "input"
              },
              {
                "caption": "表名",
                "hidden": true,
                "id": "tablename",
                "tips": "表名",
                "title": "表名",
                "type": "input"
              },
              {
                "caption": "数据类型",
                "hidden": true,
                "id": "dataType",
                "tips": "数据类型",
                "title": "数据类型",
                "type": "number",
                "width": "6.0em"
              },
              {
                "caption": "连接串",
                "hidden": true,
                "id": "url",
                "tips": "连接串",
                "title": "连接串",
                "type": "input"
              },
              {
                "caption": "数据库标识",
                "hidden": true,
                "id": "configKey",
                "tips": "数据库标识",
                "title": "数据库标识",
                "type": "input"
              }
            ],
            "headerHeight": "2.0em",
            "items": [],
            "name": "Cols",
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
                "id": "delbutton",
                "imageClass": "ri-delete-bin-fill",
                "index": 0,
                "pos": "row",
                "tagCmdsAlign": "right",
                "tips": "删除"
              },
              {
                "buttonType": "text",
                "caption": "",
                "id": "moveDownbutton",
                "imageClass": "ri-arrow-down-s-line",
                "index": 0,
                "pos": "row",
                "tagCmdsAlign": "right",
                "tips": "向下"
              },
              {
                "buttonType": "text",
                "caption": "",
                "id": "moveUPbutton",
                "imageClass": "ri-arrow-up-s-line",
                "index": 0,
                "pos": "row",
                "tagCmdsAlign": "right",
                "tips": "向上"
              }
            ],
            "uidColumn": "name",
            "valueSeparator": ";"
          }
        }
      ],
      "events": {},
      "key": "ood.UI.Block",
      "path": "db.table.ColListView.ColListViewMain",
      "properties": {
        "borderType": "none",
        "dock": "fill"
      }
    },
    {
      "alias": "RELOAD",
      "events": {},
      "key": "ood.APICaller",
      "path": "db.table.ColListView.RELOAD",
      "properties": {
        "allform": false,
        "autoRun": true,
        "checkRequired": false,
        "checkValid": false,
        "desc": "字段信息",
        "id": "/db/table/ColListView",
        "imageClass": "spafont spa-icon-c-comboinput",
        "isAllform": false,
        "methodName": "getCols",
        "name": "ColListView",
        "proxyType": "auto",
        "queryAsync": true,
        "queryMethod": "POST",
        "queryURL": "/db/table/ColListView",
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
            "name": "Cols",
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
        "sourceClassName": "net.ooder.admin.db.table.TableNav",
        "tips": "字段信息"
      }
    },
    {
      "alias": "del",
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
      "path": "db.table.ColListView.del",
      "properties": {
        "allform": false,
        "autoRun": false,
        "checkRequired": false,
        "checkValid": false,
        "desc": "删除",
        "id": "/db/action/del",
        "imageClass": "ri-delete-bin-fill",
        "isAllform": false,
        "methodName": "del",
        "name": "del",
        "proxyType": "auto",
        "queryAsync": true,
        "queryMethod": "POST",
        "queryURL": "/db/action/del",
        "requestDataSource": [
          {
            "name": "Cols",
            "path": "",
            "type": "TREEGRIDROW"
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
        "sourceClassName": "net.ooder.admin.db.action.DBColAction",
        "tips": "删除"
      }
    },
    {
      "alias": "moveDown",
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
      "path": "db.table.ColListView.moveDown",
      "properties": {
        "allform": false,
        "autoRun": false,
        "checkRequired": false,
        "checkValid": false,
        "desc": "向下",
        "id": "/db/action/moveDown",
        "imageClass": "ri-arrow-down-s-line",
        "isAllform": false,
        "methodName": "moveDown",
        "name": "moveDown",
        "proxyType": "auto",
        "queryAsync": true,
        "queryMethod": "POST",
        "queryURL": "/db/action/moveDown",
        "requestDataSource": [
          {
            "name": "Cols",
            "path": "",
            "type": "TREEGRIDROW"
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
        "sourceClassName": "net.ooder.admin.db.action.DBColAction",
        "tips": "向下"
      }
    },
    {
      "alias": "moveUP",
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
      "path": "db.table.ColListView.moveUP",
      "properties": {
        "allform": false,
        "autoRun": false,
        "checkRequired": false,
        "checkValid": false,
        "desc": "向上",
        "id": "/db/action/moveUP",
        "imageClass": "ri-arrow-up-s-line",
        "isAllform": false,
        "methodName": "moveUP",
        "name": "moveUP",
        "proxyType": "auto",
        "queryAsync": true,
        "queryMethod": "POST",
        "queryURL": "/db/action/moveUP",
        "requestDataSource": [
          {
            "name": "Cols",
            "path": "",
            "type": "TREEGRIDROW"
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
        "sourceClassName": "net.ooder.admin.db.action.DBColAction",
        "tips": "向上"
      }
    }
  ],
  "className": "db.table.ColListView",
  "customFunctions": {},
  "desc": "字段信息",
  "dio": false,
  "events": {},
  "formulas": [],
  "key": "ood.Module",
  "moduleType": "EUClass",
  "moduleVar": {},
  "moduleViewType": "GRIDCONFIG",
  "path": "db.table.ColListView",
  "properties": {
    "autoDestroy": true,
    "bindClass": [],
    "caption": "字段信息",
    "currComponentAlias": "Cols",
    "dock": "fill",
    "title": "字段信息"
  }
}