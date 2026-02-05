{
  "alias": "Chart",
  "children": [
    {
      "alias": "RELOAD",
      "events": {},
      "host": "this",
      "key": "ood.APICaller",
      "path": "demo.Chart.RELOAD",
      "properties": {
        "autoRun": true,
        "desc": "图表",
        "name": "RELOAD",
        "proxyType": "auto",
        "queryMethod": "POST",
        "queryURL": "/demo/Chart",
        "requestDataSource": [
          {
            "name": "ChartMain",
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
            "name": "ChartMain",
            "path": "data",
            "type": "FORM"
          },
          {
            "name": "chart",
            "path": "data",
            "type": "FCHART"
          }
        ],
        "responseType": "JSON",
        "tabindex": 0,
        "tips": "图表"
      }
    },
    {
      "alias": "getChart",
      "events": {},
      "host": "this",
      "key": "ood.APICaller",
      "path": "demo.Chart.getChart",
      "properties": {
        "autoRun": true,
        "desc": "图表",
        "name": "getChart",
        "proxyType": "auto",
        "queryMethod": "POST",
        "queryURL": "/demo/Chart",
        "requestDataSource": [],
        "requestType": "FORM",
        "responseCallback": [],
        "responseDataTarget": [],
        "responseType": "JSON",
        "tabindex": 1,
        "tips": "图表"
      }
    },
    {
      "alias": "SerchTestForm",
      "events": {},
      "host": "this",
      "key": "ood.APICaller",
      "path": "demo.Chart.SerchTestForm",
      "properties": {
        "desc": "SerchTestForm",
        "name": "SerchTestForm",
        "proxyType": "auto",
        "queryMethod": "POST",
        "queryURL": "/demo/SearchTestForm",
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
          }
        ],
        "requestType": "JSON",
        "responseCallback": [],
        "responseDataTarget": [],
        "responseType": "JSON",
        "tabindex": 2,
        "tips": "SerchTestForm"
      }
    },
    {
      "alias": "loadTestList",
      "events": {},
      "host": "this",
      "key": "ood.APICaller",
      "path": "demo.Chart.loadTestList",
      "properties": {
        "desc": "列表",
        "name": "loadTestList",
        "proxyType": "auto",
        "queryMethod": "POST",
        "queryURL": "/demo/TestList",
        "requestDataSource": [],
        "requestType": "FORM",
        "responseCallback": [],
        "responseDataTarget": [],
        "responseType": "JSON",
        "tabindex": 3,
        "tips": "列表"
      }
    },
    {
      "alias": "saveForm",
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
              "target": "demo.Chart",
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
              "target": "demo.Chart",
              "type": "page"
            }
          ]
        }
      },
      "host": "this",
      "key": "ood.APICaller",
      "path": "demo.Chart.saveForm",
      "properties": {
        "desc": "保存",
        "name": "saveForm",
        "proxyType": "auto",
        "queryMethod": "POST",
        "queryURL": "/demo/SaveForm",
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
          }
        ],
        "requestType": "JSON",
        "responseCallback": [],
        "responseDataTarget": [],
        "responseType": "JSON",
        "tabindex": 4,
        "tips": "保存"
      }
    },
    {
      "alias": "getTestForm",
      "events": {},
      "host": "this",
      "key": "ood.APICaller",
      "path": "demo.Chart.getTestForm",
      "properties": {
        "autoRun": true,
        "desc": "添加",
        "name": "getTestForm",
        "proxyType": "auto",
        "queryMethod": "POST",
        "queryURL": "/demo/TestForm",
        "requestDataSource": [
          {
            "name": "testView",
            "path": "",
            "type": "FORM"
          },
          {
            "name": "projectName",
            "path": "",
            "type": "FORM"
          },
          {
            "name": "testView",
            "path": "",
            "type": "FORM"
          },
          {
            "name": "projectName",
            "path": "",
            "type": "FORM"
          }
        ],
        "requestType": "JSON",
        "responseCallback": [],
        "responseDataTarget": [
          {
            "name": "PAGECTX",
            "path": "data",
            "type": "EXPRESSION"
          }
        ],
        "responseType": "JSON",
        "tabindex": 5,
        "tips": "添加"
      }
    },
    {
      "alias": "Search",
      "events": {},
      "host": "this",
      "key": "ood.APICaller",
      "path": "demo.Chart.Search",
      "properties": {
        "desc": "Search",
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
          }
        ],
        "requestType": "JSON",
        "responseCallback": [],
        "responseDataTarget": [],
        "responseType": "JSON",
        "tabindex": 6,
        "tips": "Search"
      }
    },
    {
      "alias": "ChartMain",
      "children": [
        {
          "alias": "chart",
          "events": {
            "onDataClick": {
              "actions": [
                {
                  "args": [
                    "{page.ONDATACLICK.invoke()}"
                  ],
                  "conditions": [],
                  "desc": "点击数据项",
                  "enumClassName": "net.ooder.esd.annotation.action.CustomFieldAction",
                  "enumValue": "ONDATACLICK",
                  "eventClass": "net.ooder.esd.annotation.event.FieldEventEnum",
                  "eventValue": "onDataClick",
                  "id": "net.ooder.esd.annotation.action.CustomFieldAction_ONDATACLICK_null_null",
                  "method": "invoke",
                  "redirection": "other:callback:call",
                  "return": false,
                  "target": "ONDATACLICK",
                  "type": "control"
                }
              ]
            }
          },
          "host": "ChartMain",
          "key": "ood.UI.FusionChartsXT",
          "path": "demo.Chart.ChartMain.chart",
          "properties": {
            "jSONData": {
              "categories": [
                {
                  "category": []
                }
              ],
              "chart": {
                "bgcolor": "FFFFFF,FFFFFF",
                "caption": "测试视图",
                "xaxisname": "X轴名称",
                "yaxisname": "Y轴"
              },
              "data": [
                {
                  "id": "6dbe845a-b3c5-47ad-a48f-6cfabccd6250",
                  "label": "星期一",
                  "tagVar": {
                    "test": "test"
                  },
                  "tips": "6dbe845a-b3c5-47ad-a48f-6cfabccd6250",
                  "value": "3000"
                },
                {
                  "id": "1943aed3-e3f4-4238-95a4-7ca011f79936",
                  "label": "星期二",
                  "tagVar": {
                    "test": "test"
                  },
                  "tips": "1943aed3-e3f4-4238-95a4-7ca011f79936",
                  "value": "300"
                },
                {
                  "id": "0ad6d479-50d5-4261-813b-7d6b0e1bc3cc",
                  "label": "星期三",
                  "tagVar": {
                    "test": "test"
                  },
                  "tips": "0ad6d479-50d5-4261-813b-7d6b0e1bc3cc",
                  "value": "400"
                },
                {
                  "id": "be4a163b-c68d-4b25-9bf3-417cfb938101",
                  "label": "星期四",
                  "tagVar": {
                    "test": "test"
                  },
                  "tips": "be4a163b-c68d-4b25-9bf3-417cfb938101",
                  "value": "800"
                },
                {
                  "id": "c146d330-2948-4ecb-8387-badaafa2d38d",
                  "label": "星期五",
                  "tagVar": {
                    "test": "test"
                  },
                  "tips": "c146d330-2948-4ecb-8387-badaafa2d38d",
                  "value": "1000"
                },
                {
                  "id": "d72ab490-bd63-4d0a-8f3e-a115ed82ad77",
                  "label": "星期六",
                  "tagVar": {
                    "test": "test"
                  },
                  "tips": "d72ab490-bd63-4d0a-8f3e-a115ed82ad77",
                  "value": "1500"
                },
                {
                  "id": "a255db5a-cca4-4673-82c4-3b9d9b1d4ca1",
                  "label": "星期日",
                  "tagVar": {
                    "test": "test"
                  },
                  "tips": "a255db5a-cca4-4673-82c4-3b9d9b1d4ca1",
                  "value": "2500"
                }
              ],
              "trendlines": [
                {
                  "line": []
                }
              ]
            },
            "name": "Chart",
            "tabindex": 0,
            "visibility": "visible"
          }
        }
      ],
      "events": {},
      "host": "canvas",
      "key": "ood.UI.Block",
      "path": "demo.Chart.ChartMain",
      "properties": {
        "borderType": "none",
        "dock": "fill",
        "name": "ChartMain",
        "panelBgClr": "transparent",
        "tabindex": 7,
        "visibility": "visible"
      }
    },
    {
      "alias": "getTestList",
      "events": {},
      "host": "this",
      "key": "ood.APICaller",
      "path": "demo.Chart.getTestList",
      "properties": {
        "desc": "testList",
        "name": "getTestList",
        "proxyType": "auto",
        "queryMethod": "POST",
        "queryURL": "/demo/DynTestList",
        "requestDataSource": [],
        "requestType": "FORM",
        "responseCallback": [],
        "responseDataTarget": [],
        "responseType": "JSON",
        "tabindex": 8,
        "tips": "testList"
      }
    },
    {
      "alias": "PAGECTX",
      "children": [
        {
          "alias": "projectName",
          "events": {},
          "host": "PAGECTX",
          "key": "ood.UI.HiddenInput",
          "path": "demo.Chart.PAGECTX.projectName",
          "properties": {
            "isPid": false,
            "name": "projectName",
            "pid": false,
            "tabindex": 0,
            "value": "test"
          }
        },
        {
          "alias": "projectVersionName",
          "events": {},
          "host": "PAGECTX",
          "key": "ood.UI.HiddenInput",
          "path": "demo.Chart.PAGECTX.projectVersionName",
          "properties": {
            "isPid": false,
            "name": "projectVersionName",
            "pid": false,
            "tabindex": 1,
            "value": "test"
          }
        }
      ],
      "events": {},
      "key": "ood.UI.Block",
      "params": {},
      "path": "demo.Chart.PAGECTX",
      "properties": {
        "borderType": "none",
        "tabindex": 9,
        "visibility": "hidden"
      }
    }
  ],
  "className": "demo.Chart",
  "customAppend": "function(parent, subId, left, top){ return false}",
  "customFunctions": {},
  "dio": false,
  "events": {},
  "formulas": [],
  "key": "ood.Module",
  "moduleType": "EUClass",
  "moduleVar": {
    "ViewMenuBar": "{}"
  },
  "moduleViewType": "CHARTCONFIG",
  "path": "demo.Chart",
  "properties": {
    "autoDestroy": true,
    "bindClass": [],
    "caption": "图表",
    "currComponentAlias": "chart",
    "dock": "fill",
    "dsmProperties": {
      "domainId": "TEST_VIEW",
      "moduleViewType": "CHARTCONFIG",
      "projectName": "test",
      "realPath": "demo.chart",
      "sourceClassName": "net.ooder.test.TestService",
      "sourceMethodName": "getChart"
    },
    "title": "图表"
  }
}