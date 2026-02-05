{
  "Static": {
    "designViewConf": {
      "touchDevice": false
    },
    "viewStyles": {}
  },
  "alias": "CP21",
  "children": [
    {
      "alias": "pagectx",
      "children": [
        {
          "alias": "projectName",
          "events": {},
          "key": "ood.UI.HiddenInput",
          "path": "test.view.panel.CP21.pagectx.projectName",
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
          "path": "test.view.panel.CP21.pagectx.projectVersionName",
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
      "path": "test.view.panel.CP21.pagectx",
      "properties": {
        "borderType": "none",
        "visibility": "hidden"
      }
    },
    {
      "alias": "CP21Main",
      "children": [
        {
          "alias": "CP21",
          "events": {
            "onIniPanelView": {
              "actions": [
                {
                  "args": [
                    "{ood.showModule2()}",
                    null,
                    null,
                    "{args[1].euClassName}",
                    "CP21",
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
            },
            "onRender": {
              "actions": [
                {
                  "args": [
                    "a"
                  ],
                  "conditions": [],
                  "desc": "初始化",
                  "method": "fireItemClickEvent",
                  "target": "CP21",
                  "type": "control"
                }
              ]
            }
          },
          "key": "ood.UI.ButtonViews",
          "path": "test.view.panel.CP21.CP21Main.CP21",
          "properties": {
            "autoItemColor": true,
            "autoReload": false,
            "autoSave": false,
            "barLocation": "left",
            "barSize": "16.67em",
            "borderType": "none",
            "caption": "cP21",
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
            "name": "CP21",
            "sideBarStatus": "expand",
            "singleOpen": true,
            "tagVar": {},
            "value": "a"
          }
        }
      ],
      "events": {},
      "key": "ood.UI.Block",
      "path": "test.view.panel.CP21.CP21Main",
      "properties": {
        "borderType": "none",
        "dock": "fill",
        "left": "0.0em",
        "top": "0.0em",
        "visibility": "visible"
      }
    }
  ],
  "className": "test.view.panel.CP21",
  "customFunctions": {},
  "desc": "cP21",
  "dio": false,
  "events": {},
  "formulas": [],
  "itemType": "tabs",
  "key": "ood.Module",
  "moduleType": "EUClass",
  "moduleVar": {},
  "moduleViewType": "NavButtonViewsConfig",
  "path": "test.view.panel.CP21",
  "properties": {
    "autoDestroy": true,
    "bindClass": [],
    "caption": "",
    "dsmProperties": {
      "domainId": "test_View",
      "moduleViewType": "NavButtonViewsConfig",
      "projectName": "test",
      "realPath": "test.view.panel.cp21.cp21main.ood_ui_buttonviews36",
      "sourceClassName": "net.ooder.test.view.panel.cp21.api.CP21ServiceAPI",
      "sourceMethodName": "getCP21"
    },
    "dynLoad": false,
    "imageClass": "",
    "name": "CP21",
    "panelType": "block",
    "src": "/test/view/panel/CP21",
    "target": "",
    "viewClass": "net.ooder.esd.custom.nav.tab.buttonviews.FullNavButtonViewsComponent"
  }
}