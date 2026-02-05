{
  "Static": {
    "designViewConf": {
      "touchDevice": false
    },
    "viewStyles": {}
  },
  "alias": "CP19",
  "children": [
    {
      "alias": "CP19Main",
      "children": [
        {
          "alias": "treeview11",
          "events": {},
          "key": "ood.UI.TreeView",
          "path": "test.view.panel.cp19.Treeview11.Treeview11Main.treeview11",
          "properties": {
            "dock": "none",
            "position": "relative",
            "width": "48.48em"
          }
        },
        {
          "alias": "Treegrid8",
          "events": {},
          "key": "ood.UI.TreeGrid",
          "path": "test.view.panel.cp19.Treegrid8.Treegrid8Main.Treegrid8",
          "properties": {
            "header": [
              {
                "caption": "test",
                "hidden": false,
                "id": "a",
                "type": "input",
                "width": "8.03em"
              },
              {
                "caption": "test2",
                "hidden": false,
                "id": "b",
                "type": "input",
                "width": "8.03em"
              },
              {
                "caption": "test3",
                "hidden": false,
                "id": "c",
                "type": "input",
                "width": "8.03em"
              },
              {
                "caption": "test4",
                "flexSize": true,
                "hidden": false,
                "id": "d",
                "type": "input",
                "width": "8.03em"
              }
            ],
            "headerHeight": "3.0em",
            "name": "Treegrid8",
            "position": "relative",
            "rowHeight": "3.0em",
            "rowNumbered": true,
            "width": "48.48em"
          }
        }
      ],
      "events": {},
      "key": "ood.UI.Block",
      "path": "test.view.panel.CP19.CP19MainMain.CP19Main",
      "properties": {
        "borderType": "none",
        "conLayoutColumns": 2,
        "dock": "fill",
        "visibility": "visible"
      }
    },
    {
      "alias": "Reload",
      "events": {},
      "key": "ood.APICaller",
      "path": "test.view.panel.CP19.Reload",
      "properties": {
        "allform": false,
        "autoRun": false,
        "desc": "CP19Main",
        "id": "test/view/panel/CP19",
        "imageClass": "spafont spa-icon-c-webapi",
        "isAllform": false,
        "methodName": "getCP19",
        "name": "CP19Main",
        "proxyType": "auto",
        "queryAsync": true,
        "queryMethod": "POST",
        "queryURL": "test/view/panel/CP19",
        "requestDataSource": [
          {
            "name": "CP19Main",
            "path": "",
            "type": "form"
          },
          {
            "name": "pagectx",
            "path": "",
            "type": "form"
          }
        ],
        "requestType": "FORM",
        "responseCallback": [],
        "responseDataTarget": [
          {
            "name": "CP19Main",
            "path": "data",
            "type": "form"
          },
          {
            "name": "pagectx",
            "path": "data",
            "type": "form"
          }
        ],
        "responseType": "JSON",
        "sourceClassName": "net.ooder.test.view.panel.cp19.CP19API"
      }
    },
    {
      "alias": "pagectx",
      "children": [
        {
          "alias": "projectName",
          "events": {},
          "key": "ood.UI.HiddenInput",
          "path": "test.view.panel.CP19.pagectx.projectName",
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
          "path": "test.view.panel.CP19.pagectx.projectVersionName",
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
      "params": {},
      "path": "test.view.panel.CP19.pagectx",
      "properties": {
        "borderType": "none",
        "visibility": "hidden"
      }
    }
  ],
  "className": "test.view.panel.CP19",
  "customFunctions": {},
  "dio": false,
  "events": {},
  "formulas": [],
  "key": "ood.Module",
  "moduleType": "EUClass",
  "moduleVar": {},
  "moduleViewType": "BlockConfig",
  "path": "test.view.panel.CP19",
  "properties": {
    "autoDestroy": true,
    "bindClass": [],
    "caption": "CP19Main",
    "currComponentAlias": "CP19Main",
    "dsmProperties": {
      "domainId": "test_View",
      "moduleViewType": "BlockConfig",
      "projectName": "testVVVERSION0",
      "realPath": "test.view.panel.cp19.cp19main.pagectx.cp19main",
      "sourceClassName": "net.ooder.test.view.panel.cp19.CP19API",
      "sourceMethodName": "getCP19"
    },
    "name": "CP19Main",
    "panelType": "block",
    "src": "test/view/panel/CP19",
    "viewClass": "net.ooder.esd.custom.block.FullBlockComponent"
  },
  "reSetUrl": "",
  "saveUrl": "",
  "searchUrl": ""
}