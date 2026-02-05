{
  "Static": {
    "designViewConf": {
      "touchDevice": false
    },
    "viewStyles": {}
  },
  "alias": "CP22",
  "children": [
    {
      "alias": "pagectx",
      "children": [
        {
          "alias": "projectName",
          "events": {},
          "host": "pagectx",
          "key": "ood.UI.HiddenInput",
          "path": "test.view.panel.CP22.pagectx.projectName",
          "properties": {
            "isPid": false,
            "name": "projectName",
            "pid": false,
            "value": "test"
          }
        },
        {
          "alias": "projectVersionName",
          "events": {},
          "host": "pagectx",
          "key": "ood.UI.HiddenInput",
          "path": "test.view.panel.CP22.pagectx.projectVersionName",
          "properties": {
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
      "path": "test.view.panel.CP22.pagectx",
      "properties": {
        "borderType": "none",
        "visibility": "hidden"
      }
    },
    {
      "alias": "CP22Main",
      "children": [
        {
          "alias": "CP22",
          "events": {
            "onIniPanelView": {
              "actions": [
                {
                  "args": [
                    "{ood.showModule2()}",
                    null,
                    null,
                    "{args[1].euClassName}",
                    "CP22",
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
                  "target": "CP22",
                  "type": "control"
                }
              ]
            }
          },
          "host": "CP22Main",
          "key": "ood.UI.ButtonViews",
          "path": "test.view.panel.CP22.CP22Main.CP22",
          "properties": {
            "autoItemColor": true,
            "barLocation": "left",
            "barSize": "12em",
            "items": [
              {
                "bindClass": [],
                "caption": "page1",
                "closeBtn": false,
                "euClassName": "test.view.panel.cp22.buttonviews20.Treegrid54",
                "id": "a",
                "imageClass": "ood-icon-ood",
                "index": 0,
                "popBtn": false,
                "tabindex": 0,
                "tagVar": {}
              },
              {
                "bindClass": [],
                "caption": "page2",
                "closeBtn": false,
                "id": "b",
                "imageClass": "",
                "index": 0,
                "popBtn": false,
                "tabindex": 0,
                "tagVar": {}
              },
              {
                "bindClass": [],
                "caption": "page3",
                "closeBtn": false,
                "id": "c",
                "imageClass": "",
                "index": 0,
                "popBtn": false,
                "tabindex": 0,
                "tagVar": {}
              },
              {
                "bindClass": [],
                "caption": "page4",
                "closeBtn": true,
                "id": "d",
                "imageClass": "",
                "index": 0,
                "popBtn": true,
                "tabindex": 0,
                "tagVar": {}
              }
            ],
            "name": "CP22",
            "value": "a"
          }
        }
      ],
      "events": {},
      "host": "canvas",
      "key": "ood.UI.Block",
      "path": "test.view.panel.CP22.CP22Main",
      "properties": {
        "borderType": "none",
        "dock": "fill"
      }
    }
  ],
  "className": "test.view.panel.CP22",
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
  "path": "test.view.panel.CP22",
  "properties": {
    "autoDestroy": true,
    "bindClass": [
      "net.ooder.test.repository.panel.cp22.service.CP22ServiceImpl"
    ],
    "bindClassName": "net.ooder.test.repository.panel.cp22.service.CP22ServiceImpl",
    "bindService": "net.ooder.test.repository.panel.cp22.service.CP22ServiceImpl",
    "caption": "",
    "dock": "fill",
    "dsmProperties": {
      "domainId": "test_View",
      "moduleViewType": "NavButtonViewsConfig",
      "projectName": "test",
      "realPath": "test.view.panel.cp22",
      "sourceClassName": "net.ooder.test.view.panel.cp22.api.CP22ServiceAPI",
      "sourceMethodName": "getCP22"
    },
    "dynLoad": false,
    "imageClass": "",
    "name": "CP22",
    "panelType": "block",
    "src": "/test/view/panel/CP22",
    "target": "",
    "viewClass": "net.ooder.esd.custom.nav.tab.buttonviews.FullNavButtonViewsComponent"
  }
}