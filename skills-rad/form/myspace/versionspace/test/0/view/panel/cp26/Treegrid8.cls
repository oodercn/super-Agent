{
  "alias": "Treegrid8",
  "children": [
    {
      "alias": "PAGECTX",
      "children": [
        {
          "alias": "projectName",
          "events": {},
          "key": "ood.UI.HiddenInput",
          "path": "view.panel.cp26.Treegrid8.PAGECTX.projectName",
          "properties": {
            "formField": true,
            "isPid": false,
            "name": "projectName",
            "pid": false,
            "value": "testVVVERSION0"
          }
        },
        {
          "alias": "projectVersionName",
          "events": {},
          "key": "ood.UI.HiddenInput",
          "path": "view.panel.cp26.Treegrid8.PAGECTX.projectVersionName",
          "properties": {
            "formField": true,
            "isPid": false,
            "name": "projectVersionName",
            "pid": false,
            "value": "testVVVERSION0"
          }
        }
      ],
      "events": {},
      "key": "ood.UI.Block",
      "params": {},
      "path": "view.panel.cp26.Treegrid8.PAGECTX",
      "properties": {
        "borderType": "none",
        "visibility": "hidden"
      }
    },
    {
      "alias": "Treegrid8Main",
      "children": [
        {
          "alias": "Treegrid8",
          "events": {},
          "key": "ood.UI.TreeGrid",
          "path": "view.panel.cp26.Treegrid8.Treegrid8Main.Treegrid8",
          "properties": {
            "header": [
              {
                "caption": "col1",
                "hidden": false,
                "id": "A",
                "tips": "col1",
                "title": "col1",
                "type": "input",
                "width": "8.0em"
              },
              {
                "caption": "col2",
                "hidden": false,
                "id": "B",
                "tips": "col2",
                "title": "col2",
                "type": "input",
                "width": "8.0em"
              },
              {
                "caption": "col3",
                "hidden": false,
                "id": "C",
                "tips": "col3",
                "title": "col3",
                "type": "input",
                "width": "8.0em"
              },
              {
                "caption": "col4",
                "flexSize": true,
                "hidden": false,
                "id": "D",
                "tips": "col4",
                "title": "col4",
                "type": "input",
                "width": "8.0em"
              }
            ],
            "headerHeight": "3.0em",
            "items": [],
            "left": "0.0em",
            "name": "Treegrid8",
            "rowHeight": "3.0em",
            "rowNumbered": true,
            "selMode": "multibycheckbox",
            "top": "0.0em",
            "visibility": "visible"
          }
        }
      ],
      "events": {},
      "key": "ood.UI.Block",
      "path": "view.panel.cp26.Treegrid8.Treegrid8Main",
      "properties": {
        "borderType": "none",
        "dock": "fill",
        "left": "0.0em",
        "top": "0.0em",
        "visibility": "visible"
      }
    },
    {
      "alias": "RELOAD",
      "events": {},
      "key": "ood.APICaller",
      "path": "view.panel.cp26.Treegrid8.RELOAD",
      "properties": {
        "allform": false,
        "autoRun": true,
        "checkRequired": false,
        "checkValid": false,
        "desc": "treegrid8",
        "id": "view/panel/cp26/Treegrid8",
        "imageClass": "ri-code-box-line",
        "isAllform": false,
        "methodName": "getTreegrid8",
        "name": "Treegrid8",
        "proxyType": "auto",
        "queryAsync": true,
        "queryMethod": "POST",
        "queryURL": "view/panel/cp26/Treegrid8",
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
            "name": "Treegrid8",
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
        "sourceClassName": "net.ooder.test.view.view.panel.cp26.Treegrid8API",
        "tips": "treegrid8"
      }
    }
  ],
  "className": "view.panel.cp26.Treegrid8",
  "customFunctions": {},
  "desc": "treegrid8",
  "dio": false,
  "events": {},
  "formulas": [],
  "key": "ood.Module",
  "moduleType": "EUClass",
  "moduleVar": {},
  "moduleViewType": "GRIDCONFIG",
  "path": "view.panel.cp26.Treegrid8",
  "properties": {
    "autoDestroy": true,
    "bindClass": [],
    "caption": "treegrid8",
    "currComponentAlias": "Treegrid8",
    "dock": "fill",
    "title": "treegrid8"
  }
}