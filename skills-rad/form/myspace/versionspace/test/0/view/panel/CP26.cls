{
  "alias": "CP26",
  "children": [
    {
      "alias": "PAGECTX",
      "children": [
        {
          "alias": "projectName",
          "events": {},
          "key": "ood.UI.HiddenInput",
          "path": "view.panel.CP26.PAGECTX.projectName",
          "properties": {
            "formField": true,
            "isPid": false,
            "name": "projectName",
            "pid": false,
            "tabindex": 0,
            "value": "testVVVERSION0"
          }
        },
        {
          "alias": "projectVersionName",
          "events": {},
          "key": "ood.UI.HiddenInput",
          "path": "view.panel.CP26.PAGECTX.projectVersionName",
          "properties": {
            "formField": true,
            "isPid": false,
            "name": "projectVersionName",
            "pid": false,
            "tabindex": 1,
            "value": "testVVVERSION0"
          }
        }
      ],
      "events": {},
      "key": "ood.UI.Block",
      "params": {},
      "path": "view.panel.CP26.PAGECTX",
      "properties": {
        "borderType": "none",
        "tabindex": 0,
        "visibility": "hidden"
      }
    },
    {
      "alias": "CP26Main",
      "children": [
        {
          "alias": "BUTTONVIEWS1",
          "events": {
            "onIniPanelView": {
              "actions": [
                {
                  "args": [
                    "{ood.showModule2()}",
                    null,
                    null,
                    "{args[1].euClassName}",
                    "BUTTONVIEWS1",
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
                  "type": "control"
                }
              ]
            }
          },
          "key": "ood.UI.ButtonViews",
          "path": "view.panel.CP26.CP26Main.BUTTONVIEWS1",
          "properties": {
            "autoItemColor": true,
            "autoReload": false,
            "autoSave": false,
            "barLocation": "left",
            "barSize": "16.67em",
            "caption": "ood_ui_buttonviews9",
            "id": "TREEVIEW_ood_ui_buttonviews9",
            "imageClass": "ri-grid-line",
            "items": [
              {
                "bindClass": [
                  null
                ],
                "caption": "page1",
                "euClassName": "view.panel.cp26.Treegrid8",
                "id": "a",
                "imageClass": "ood-icon-ood",
                "index": 0,
                "tabindex": 0,
                "tagVar": {},
                "title": "page1"
              },
              {
                "bindClass": [],
                "caption": "page2",
                "id": "b",
                "index": 0,
                "tabindex": 0,
                "tagVar": {},
                "title": "page2"
              },
              {
                "bindClass": [],
                "caption": "page3",
                "id": "c",
                "index": 0,
                "tabindex": 0,
                "tagVar": {},
                "title": "page3"
              },
              {
                "bindClass": [],
                "caption": "page4",
                "closeBtn": true,
                "id": "d",
                "index": 0,
                "popBtn": true,
                "tabindex": 0,
                "tagVar": {},
                "title": "page4"
              }
            ],
            "name": "ood_ui_buttonviews9",
            "noFoldBar": false,
            "sideBarStatus": "expand",
            "singleOpen": true,
            "tabindex": 0,
            "tagVar": {},
            "value": "a"
          }
        }
      ],
      "events": {},
      "key": "ood.UI.Block",
      "path": "view.panel.CP26.CP26Main",
      "properties": {
        "borderType": "none",
        "defaultFocus": false,
        "disableClickEffect": false,
        "disableHoverEffect": false,
        "disableTips": false,
        "disabled": false,
        "dock": "fill",
        "panelBgClr": "transparent",
        "tabindex": 1,
        "visibility": "visible"
      }
    }
  ],
  "className": "view.panel.CP26",
  "customFunctions": {},
  "desc": "cP26",
  "dio": false,
  "events": {},
  "formulas": [],
  "itemType": "TABS",
  "key": "ood.Module",
  "moduleType": "EUClass",
  "moduleVar": {},
  "moduleViewType": "NAVBUTTONVIEWSCONFIG",
  "path": "view.panel.CP26",
  "properties": {
    "autoDestroy": true,
    "bindClass": [],
    "caption": "cP26",
    "currComponentAlias": "BUTTONVIEWS1",
    "dock": "fill",
    "title": "cP26"
  }
}