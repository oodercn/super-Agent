{
  "Static": {
    "designViewConf": {
      "touchDevice": false
    },
    "viewStyles": {}
  },
  "alias": "T6",
  "children": [
    {
      "alias": "T6Main",
      "children": [
        {
          "alias": "T6",
          "events": {},
          "key": "ood.UI.TreeGrid",
          "path": "view.grid.T6.T6Main.T6",
          "properties": {
            "editMode": "inline",
            "header": [
              {
                "caption": "ID",
                "hidden": false,
                "id": "id",
                "tips": "ID",
                "title": "ID",
                "type": "input",
                "width": "5.0em"
              },
              {
                "caption": "名称",
                "hidden": false,
                "id": "name",
                "tips": "名称",
                "title": "名称",
                "type": "input",
                "width": "10.0em"
              },
              {
                "caption": "状态",
                "hidden": false,
                "id": "status",
                "tips": "状态",
                "title": "状态",
                "type": "input",
                "width": "6.67em"
              },
              {
                "caption": "创建时间",
                "flexSize": true,
                "hidden": false,
                "id": "createTime",
                "tips": "创建时间",
                "title": "创建时间",
                "type": "input",
                "width": "11.67em"
              }
            ],
            "headerHeight": "3.0em",
            "items": [],
            "left": "0.0em",
            "name": "T6",
            "rowHeight": "3.0em",
            "rowNumbered": true,
            "top": "0.0em"
          }
        },
        {
          "alias": "T6_PAGEBARDiv",
          "children": [
            {
              "alias": "T6_PAGEBAR",
              "data": 20,
              "events": {},
              "key": "ood.UI.PageBar",
              "path": "view.grid.T6.T6Main.T6_PAGEBARDiv.T6_PAGEBAR",
              "properties": {
                "caption": "分页",
                "disabled": false,
                "name": "T6_PAGEBAR",
                "nextMark": "",
                "pageCount": 20,
                "prevMark": "",
                "showMoreBtns": true,
                "tips": "分页",
                "title": "分页",
                "value": ""
              }
            }
          ],
          "events": {},
          "key": "ood.UI.Div",
          "path": "view.grid.T6.T6Main.T6_PAGEBARDiv",
          "properties": {
            "dock": "bottom",
            "height": "2.5em",
            "name": "T6_PAGEBARDiv"
          }
        }
      ],
      "events": {},
      "key": "ood.UI.Block",
      "path": "view.grid.T6.T6Main",
      "properties": {
        "borderType": "none",
        "dock": "fill",
        "left": "0.0em",
        "name": "T6Main",
        "top": "0.0em",
        "visibility": "visible"
      }
    },
    {
      "alias": "RELOAD",
      "events": {},
      "key": "ood.APICaller",
      "path": "view.grid.T6.RELOAD",
      "properties": {
        "allform": false,
        "autoRun": true,
        "checkRequired": false,
        "checkValid": false,
        "currClassName": "view.grid.T6",
        "desc": "t6",
        "id": "view/grid/T6",
        "imageClass": "fa-solid fa-code",
        "isAllform": false,
        "methodName": "getT6",
        "name": "Treegrid13",
        "proxyType": "auto",
        "queryAsync": true,
        "queryMethod": "POST",
        "queryURL": "view/grid/T6",
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
            "name": "T6",
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
        "sourceClassName": "net.ooder.test.view.view.grid.T6API",
        "tips": "t6"
      }
    },
    {
      "alias": "PAGECTX",
      "children": [
        {
          "alias": "projectName",
          "events": {},
          "key": "ood.UI.HiddenInput",
          "path": "view.grid.T6.PAGECTX.projectName",
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
          "path": "view.grid.T6.PAGECTX.projectVersionName",
          "properties": {
            "formField": true,
            "isPid": false,
            "name": "projectVersionName",
            "pid": false,
            "value": "test"
          }
        }
      ],
      "events": {},
      "key": "ood.UI.Block",
      "params": {
        "projectName": "test",
        "projectVersionName": "test"
      },
      "path": "view.grid.T6.PAGECTX",
      "properties": {
        "borderType": "none",
        "name": "PAGECTX",
        "visibility": "hidden"
      }
    }
  ],
  "className": "view.grid.T6",
  "customFunctions": {},
  "dio": false,
  "events": {},
  "formulas": [],
  "key": "ood.Module",
  "moduleType": "EUClass",
  "moduleVar": {},
  "moduleViewType": "GRIDCONFIG",
  "path": "view.grid.T6",
  "properties": {
    "autoDestroy": true,
    "bindClass": [
      null
    ],
    "bindClassName": "net.ooder.test.view.view.grid.T6API",
    "currComponentAlias": "T6",
    "dock": "fill",
    "dsmProperties": {
      "domainId": "TEST_VIEW",
      "moduleViewType": "GRIDCONFIG",
      "projectName": "test",
      "realPath": "view.grid.t6.t6main.t6",
      "sourceClassName": "net.ooder.test.view.view.grid.T6API",
      "sourceMethodName": "getT6"
    },
    "dynLoad": false,
    "name": "T6",
    "panelType": "block",
    "viewClass": "net.ooder.esd.custom.component.grid.FullGridComponent"
  }
}