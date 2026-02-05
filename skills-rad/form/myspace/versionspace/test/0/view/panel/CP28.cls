{
  "Static": {
    "designViewConf": {
      "touchDevice": false
    },
    "viewStyles": {}
  },
  "alias": "CP28",
  "children": [
    {
      "alias": "pagectx",
      "children": [
        {
          "alias": "projectName",
          "events": {},
          "key": "ood.UI.HiddenInput",
          "path": "test.view.panel.CP28.pagectx.projectName",
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
          "path": "test.view.panel.CP28.pagectx.projectVersionName",
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
      "path": "test.view.panel.CP28.pagectx",
      "properties": {
        "borderType": "none",
        "visibility": "hidden"
      }
    },
    {
      "alias": "CP28Main",
      "children": [
        {
          "alias": "CP28",
          "events": {
            "onRender": {
              "actions": [
                {
                  "args": [
                    "a"
                  ],
                  "conditions": [],
                  "desc": "初始化",
                  "method": "fireItemClickEvent",
                  "target": "CP28",
                  "type": "control"
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
                    "CP28",
                    "{args[1].id}",
                    "{args[1].tagVar}",
                    "{page.getData()}",
                    null,
                    "{page}"
                  ],
                  "conditions": [],
                  "desc": "切换窗口",
                  "enumClassName": "net.ooder.esd.custom.action.CustomLoadClassAction",
                  "enumValue": "tabShow",
                  "eventClass": "net.ooder.esd.tool.ui.enums.event.enums.TabsEventEnum",
                  "eventValue": "onItemSelected",
                  "id": "url_other_showModule2",
                  "method": "showModule2",
                  "redirection": "other:callback:call",
                  "return": true,
                  "target": "url",
                  "type": "other"
                }
              ]
            }
          },
          "key": "ood.UI.ButtonViews",
          "path": "test.view.panel.CP28.CP28Main.CP28",
          "properties": {
            "autoItemColor": true,
            "autoReload": false,
            "autoSave": false,
            "barLocation": "left",
            "barSize": "16.67em",
            "borderType": "none",
            "caption": "cP28",
            "enumClass": "net.ooder.esd.custom.nav.tab.data.TabItemEnums",
            "fontColors": [],
            "iconColors": [],
            "id": "TreeView_null",
            "imageClass": "spafont spa-icon-c-buttonviews",
            "itemColors": [],
            "items": [
              {
                "bindClass": [],
                "caption": "page1",
                "euClassName": "test.view.panel.cp28.Treegrid12",
                "id": "a",
                "imageClass": "ood-icon-ood",
                "index": 0,
                "tabindex": 0,
                "tagVar": {}
              },
              {
                "bindClass": [],
                "caption": "page2",
                "id": "b",
                "index": 0,
                "tabindex": 0,
                "tagVar": {}
              },
              {
                "bindClass": [],
                "caption": "page3",
                "id": "c",
                "index": 0,
                "tabindex": 0,
                "tagVar": {}
              },
              {
                "bindClass": [],
                "caption": "page4",
                "closeBtn": true,
                "id": "d",
                "index": 0,
                "popBtn": true,
                "tabindex": 0,
                "tagVar": {}
              }
            ],
            "name": "CP28",
            "sideBarStatus": "expand",
            "singleOpen": true,
            "tagVar": {},
            "value": "a"
          }
        }
      ],
      "events": {},
      "key": "ood.UI.Block",
      "path": "test.view.panel.CP28.CP28Main",
      "properties": {
        "borderType": "none",
        "dock": "fill",
        "left": "0.0em",
        "top": "0.0em"
      }
    }
  ],
  "className": "test.view.panel.CP28",
  "customFunctions": {},
  "desc": "cP28",
  "dio": false,
  "events": {},
  "formulas": [],
  "itemType": "tabs",
  "key": "ood.Module",
  "moduleType": "EUClass",
  "moduleVar": {},
  "moduleViewType": "NavButtonViewsConfig",
  "path": "test.view.panel.CP28",
  "properties": {
    "autoDestroy": true,
    "bindClass": [],
    "caption": "",
    "dsmProperties": {
      "domainId": "test_View",
      "moduleViewType": "NavButtonViewsConfig",
      "projectName": "test",
      "realPath": "test.view.panel.cp28.ood_ui_buttonviews14",
      "sourceClassName": "net.ooder.test.view.panel.cp28.api.CP28ServiceAPI",
      "sourceMethodName": "getCP28"
    },
    "dynLoad": false,
    "imageClass": "",
    "name": "CP28",
    "panelType": "block",
    "src": "/test/view/panel/CP28",
    "target": "",
    "viewClass": "net.ooder.esd.custom.nav.tab.buttonviews.FullNavButtonViewsComponent"
  }
}