{
  "alias": "BottomMenuConfig",
  "children": [
    {
      "alias": "PAGECTX",
      "children": [
        {
          "alias": "currentClassName",
          "events": {},
          "key": "ood.UI.HiddenInput",
          "path": "dsm.view.config.menu.bottom.BottomMenuConfig.PAGECTX.currentClassName",
          "properties": {
            "formField": true,
            "isPid": true,
            "name": "currentClassName",
            "pid": true,
            "tabindex": 0
          }
        },
        {
          "alias": "xpath",
          "events": {},
          "key": "ood.UI.HiddenInput",
          "path": "dsm.view.config.menu.bottom.BottomMenuConfig.PAGECTX.xpath",
          "properties": {
            "formField": true,
            "isPid": true,
            "name": "xpath",
            "pid": true,
            "tabindex": 1
          }
        },
        {
          "alias": "sourceMethodName",
          "events": {},
          "key": "ood.UI.HiddenInput",
          "path": "dsm.view.config.menu.bottom.BottomMenuConfig.PAGECTX.sourceMethodName",
          "properties": {
            "formField": true,
            "isPid": true,
            "name": "sourceMethodName",
            "pid": true,
            "tabindex": 2
          }
        },
        {
          "alias": "sourceClassName",
          "events": {},
          "key": "ood.UI.HiddenInput",
          "path": "dsm.view.config.menu.bottom.BottomMenuConfig.PAGECTX.sourceClassName",
          "properties": {
            "formField": true,
            "isPid": true,
            "name": "sourceClassName",
            "pid": true,
            "tabindex": 3
          }
        },
        {
          "alias": "projectName",
          "events": {},
          "key": "ood.UI.HiddenInput",
          "path": "dsm.view.config.menu.bottom.BottomMenuConfig.PAGECTX.projectName",
          "properties": {
            "formField": true,
            "isPid": false,
            "name": "projectName",
            "pid": false,
            "tabindex": 4,
            "value": "DSMdsmVVVERSION0"
          }
        },
        {
          "alias": "domainId",
          "events": {},
          "key": "ood.UI.HiddenInput",
          "path": "dsm.view.config.menu.bottom.BottomMenuConfig.PAGECTX.domainId",
          "properties": {
            "formField": true,
            "isPid": true,
            "name": "domainId",
            "pid": true,
            "tabindex": 5
          }
        },
        {
          "alias": "projectVersionName",
          "events": {},
          "key": "ood.UI.HiddenInput",
          "path": "dsm.view.config.menu.bottom.BottomMenuConfig.PAGECTX.projectVersionName",
          "properties": {
            "formField": true,
            "isPid": false,
            "name": "projectVersionName",
            "pid": false,
            "tabindex": 6,
            "value": "DSMdsmVVVERSION0"
          }
        },
        {
          "alias": "id",
          "events": {},
          "key": "ood.UI.HiddenInput",
          "path": "dsm.view.config.menu.bottom.BottomMenuConfig.PAGECTX.id",
          "properties": {
            "formField": true,
            "isPid": true,
            "name": "id",
            "pid": true,
            "tabindex": 7
          }
        }
      ],
      "events": {},
      "key": "ood.UI.Block",
      "params": {},
      "path": "dsm.view.config.menu.bottom.BottomMenuConfig.PAGECTX",
      "properties": {
        "borderType": "none",
        "tabindex": 0,
        "visibility": "hidden"
      }
    },
    {
      "alias": "BottomMenuConfigMain",
      "children": [
        {
          "alias": "BottomMenuConfig",
          "children": [
            {
              "alias": "barDock",
              "events": {},
              "key": "ood.UI.ComboInput",
              "path": "dsm.view.config.menu.bottom.BottomMenuConfig.BottomMenuConfigMain.BottomMenuConfig.barDock",
              "properties": {
                "defaultFocus": false,
                "desc": "容器位置",
                "disableClickEffect": false,
                "disableHoverEffect": false,
                "disableTips": false,
                "disabled": false,
                "dock": "none",
                "dynLoad": true,
                "enumClass": "net.ooder.esd.annotation.ui.Dock",
                "items": [
                  {
                    "bindClass": [],
                    "caption": "默认",
                    "id": "none",
                    "name": "默认",
                    "tabindex": 0,
                    "tagVar": {
                      "name": "none",
                      "clazz": "net.ooder.esd.annotation.ui.Dock"
                    },
                    "title": "默认",
                    "type": "none"
                  },
                  {
                    "bindClass": [],
                    "caption": "顶部对齐",
                    "id": "top",
                    "name": "顶部对齐",
                    "tabindex": 1,
                    "tagVar": {
                      "name": "top",
                      "clazz": "net.ooder.esd.annotation.ui.Dock"
                    },
                    "title": "顶部对齐"
                  },
                  {
                    "bindClass": [],
                    "caption": "底部对齐",
                    "id": "bottom",
                    "name": "底部对齐",
                    "tabindex": 2,
                    "tagVar": {
                      "name": "bottom",
                      "clazz": "net.ooder.esd.annotation.ui.Dock"
                    },
                    "title": "底部对齐"
                  },
                  {
                    "bindClass": [],
                    "caption": "左对齐",
                    "id": "left",
                    "name": "左对齐",
                    "tabindex": 3,
                    "tagVar": {
                      "name": "left",
                      "clazz": "net.ooder.esd.annotation.ui.Dock"
                    },
                    "title": "左对齐"
                  },
                  {
                    "bindClass": [],
                    "caption": "右对齐",
                    "id": "right",
                    "name": "右对齐",
                    "tabindex": 4,
                    "tagVar": {
                      "name": "right",
                      "clazz": "net.ooder.esd.annotation.ui.Dock"
                    },
                    "title": "右对齐"
                  },
                  {
                    "bindClass": [],
                    "caption": "居中对齐",
                    "id": "center",
                    "name": "居中对齐",
                    "tabindex": 5,
                    "tagVar": {
                      "name": "center",
                      "clazz": "net.ooder.esd.annotation.ui.Dock"
                    },
                    "title": "居中对齐"
                  },
                  {
                    "bindClass": [],
                    "caption": "垂直居中",
                    "id": "middle",
                    "name": "垂直居中",
                    "tabindex": 6,
                    "tagVar": {
                      "name": "middle",
                      "clazz": "net.ooder.esd.annotation.ui.Dock"
                    },
                    "title": "垂直居中"
                  },
                  {
                    "bindClass": [],
                    "caption": "自适应排列",
                    "id": "origin",
                    "name": "自适应排列",
                    "tabindex": 7,
                    "tagVar": {
                      "name": "origin",
                      "clazz": "net.ooder.esd.annotation.ui.Dock"
                    },
                    "title": "自适应排列"
                  },
                  {
                    "bindClass": [],
                    "caption": "宽度自适应",
                    "id": "width",
                    "name": "宽度自适应",
                    "tabindex": 8,
                    "tagVar": {
                      "name": "width",
                      "clazz": "net.ooder.esd.annotation.ui.Dock"
                    },
                    "title": "宽度自适应"
                  },
                  {
                    "bindClass": [],
                    "caption": "高度自适应",
                    "id": "height",
                    "name": "高度自适应",
                    "tabindex": 9,
                    "tagVar": {
                      "name": "height",
                      "clazz": "net.ooder.esd.annotation.ui.Dock"
                    },
                    "title": "高度自适应"
                  },
                  {
                    "bindClass": [],
                    "caption": "自动填充",
                    "id": "fill",
                    "name": "自动填充",
                    "tabindex": 10,
                    "tagVar": {
                      "name": "fill",
                      "clazz": "net.ooder.esd.annotation.ui.Dock"
                    },
                    "title": "自动填充"
                  },
                  {
                    "bindClass": [],
                    "caption": "覆盖",
                    "id": "cover",
                    "name": "覆盖",
                    "tabindex": 11,
                    "tagVar": {
                      "name": "cover",
                      "clazz": "net.ooder.esd.annotation.ui.Dock"
                    },
                    "title": "覆盖"
                  }
                ],
                "labelCaption": "容器位置",
                "name": "barDock",
                "readonly": false,
                "tabindex": 0,
                "type": "listbox",
                "visibility": "visible"
              },
              "target": "B1"
            },
            {
              "alias": "height",
              "events": {},
              "key": "ood.UI.Input",
              "path": "dsm.view.config.menu.bottom.BottomMenuConfig.BottomMenuConfigMain.BottomMenuConfig.height",
              "properties": {
                "caption": "容器高度",
                "defaultFocus": false,
                "desc": "容器高度",
                "disableClickEffect": false,
                "disableHoverEffect": false,
                "disableTips": false,
                "disabled": false,
                "dock": "none",
                "events": {},
                "id": "height",
                "imageClass": "",
                "items": [],
                "labelCaption": "容器高度",
                "labelPos": "left",
                "labelSize": "6.0em",
                "name": "height",
                "readonly": false,
                "tabindex": 1,
                "tagVar": {},
                "visibility": "visible"
              },
              "target": "D1"
            },
            {
              "alias": "itemAlign",
              "events": {},
              "key": "ood.UI.ComboInput",
              "path": "dsm.view.config.menu.bottom.BottomMenuConfig.BottomMenuConfigMain.BottomMenuConfig.itemAlign",
              "properties": {
                "caption": "按钮对齐方式",
                "defaultFocus": false,
                "desc": "按钮对齐方式",
                "disableClickEffect": false,
                "disableHoverEffect": false,
                "disableTips": false,
                "disabled": false,
                "dock": "none",
                "dynLoad": true,
                "enumClass": "net.ooder.esd.annotation.ui.AlignType",
                "items": [
                  {
                    "bindClass": [],
                    "caption": "center",
                    "id": "center",
                    "tabindex": 0,
                    "tagVar": {
                      "name": "center",
                      "clazz": "net.ooder.esd.annotation.ui.AlignType"
                    },
                    "title": "center"
                  },
                  {
                    "bindClass": [],
                    "caption": "left",
                    "id": "left",
                    "tabindex": 1,
                    "tagVar": {
                      "name": "left",
                      "clazz": "net.ooder.esd.annotation.ui.AlignType"
                    },
                    "title": "left"
                  },
                  {
                    "bindClass": [],
                    "caption": "right",
                    "id": "right",
                    "tabindex": 2,
                    "tagVar": {
                      "name": "right",
                      "clazz": "net.ooder.esd.annotation.ui.AlignType"
                    },
                    "title": "right"
                  }
                ],
                "labelCaption": "按钮对齐方式",
                "name": "itemAlign",
                "readonly": false,
                "tabindex": 2,
                "type": "combobox",
                "visibility": "visible"
              },
              "target": "B2"
            },
            {
              "alias": "barBorderType",
              "events": {},
              "key": "ood.UI.ComboInput",
              "path": "dsm.view.config.menu.bottom.BottomMenuConfig.BottomMenuConfigMain.BottomMenuConfig.barBorderType",
              "properties": {
                "caption": "容器边框",
                "defaultFocus": false,
                "desc": "容器边框",
                "disableClickEffect": false,
                "disableHoverEffect": false,
                "disableTips": false,
                "disabled": false,
                "dock": "none",
                "dynLoad": true,
                "enumClass": "net.ooder.esd.annotation.ui.BorderType",
                "items": [
                  {
                    "bindClass": [],
                    "caption": "none",
                    "id": "none",
                    "tabindex": 0,
                    "tagVar": {
                      "name": "none",
                      "clazz": "net.ooder.esd.annotation.ui.BorderType"
                    },
                    "title": "none"
                  },
                  {
                    "bindClass": [],
                    "caption": "flat",
                    "id": "flat",
                    "tabindex": 1,
                    "tagVar": {
                      "name": "flat",
                      "clazz": "net.ooder.esd.annotation.ui.BorderType"
                    },
                    "title": "flat"
                  },
                  {
                    "bindClass": [],
                    "caption": "inset",
                    "id": "inset",
                    "tabindex": 2,
                    "tagVar": {
                      "name": "inset",
                      "clazz": "net.ooder.esd.annotation.ui.BorderType"
                    },
                    "title": "inset"
                  },
                  {
                    "bindClass": [],
                    "caption": "outset",
                    "id": "outset",
                    "tabindex": 3,
                    "tagVar": {
                      "name": "outset",
                      "clazz": "net.ooder.esd.annotation.ui.BorderType"
                    },
                    "title": "outset"
                  }
                ],
                "labelCaption": "容器边框",
                "name": "barBorderType",
                "readonly": false,
                "tabindex": 3,
                "type": "combobox",
                "visibility": "visible"
              },
              "target": "D2"
            },
            {
              "alias": "itemType",
              "events": {},
              "key": "ood.UI.ComboInput",
              "path": "dsm.view.config.menu.bottom.BottomMenuConfig.BottomMenuConfigMain.BottomMenuConfig.itemType",
              "properties": {
                "caption": "按钮类型",
                "defaultFocus": false,
                "desc": "按钮类型",
                "disableClickEffect": false,
                "disableHoverEffect": false,
                "disableTips": false,
                "disabled": false,
                "dock": "none",
                "dynLoad": true,
                "enumClass": "net.ooder.esd.annotation.ui.StatusItemType",
                "items": [
                  {
                    "bindClass": [],
                    "caption": "text",
                    "id": "text",
                    "tabindex": 0,
                    "tagVar": {
                      "name": "text",
                      "clazz": "net.ooder.esd.annotation.ui.StatusItemType"
                    },
                    "title": "text"
                  },
                  {
                    "bindClass": [],
                    "caption": "button",
                    "id": "button",
                    "tabindex": 1,
                    "tagVar": {
                      "name": "button",
                      "clazz": "net.ooder.esd.annotation.ui.StatusItemType"
                    },
                    "title": "button"
                  },
                  {
                    "bindClass": [],
                    "caption": "dropButton",
                    "id": "dropButton",
                    "tabindex": 2,
                    "tagVar": {
                      "name": "dropButton",
                      "clazz": "net.ooder.esd.annotation.ui.StatusItemType"
                    },
                    "title": "dropButton"
                  }
                ],
                "labelCaption": "按钮类型",
                "name": "itemType",
                "readonly": false,
                "tabindex": 4,
                "type": "combobox",
                "visibility": "visible"
              },
              "target": "B3"
            },
            {
              "alias": "dock",
              "events": {},
              "key": "ood.UI.ComboInput",
              "path": "dsm.view.config.menu.bottom.BottomMenuConfig.BottomMenuConfigMain.BottomMenuConfig.dock",
              "properties": {
                "defaultFocus": false,
                "desc": "按钮位置",
                "disableClickEffect": false,
                "disableHoverEffect": false,
                "disableTips": false,
                "disabled": false,
                "dock": "none",
                "dynLoad": true,
                "enumClass": "net.ooder.esd.annotation.ui.Dock",
                "items": [
                  {
                    "bindClass": [],
                    "caption": "默认",
                    "id": "none",
                    "name": "默认",
                    "tabindex": 0,
                    "tagVar": {
                      "name": "none",
                      "clazz": "net.ooder.esd.annotation.ui.Dock"
                    },
                    "title": "默认",
                    "type": "none"
                  },
                  {
                    "bindClass": [],
                    "caption": "顶部对齐",
                    "id": "top",
                    "name": "顶部对齐",
                    "tabindex": 1,
                    "tagVar": {
                      "name": "top",
                      "clazz": "net.ooder.esd.annotation.ui.Dock"
                    },
                    "title": "顶部对齐"
                  },
                  {
                    "bindClass": [],
                    "caption": "底部对齐",
                    "id": "bottom",
                    "name": "底部对齐",
                    "tabindex": 2,
                    "tagVar": {
                      "name": "bottom",
                      "clazz": "net.ooder.esd.annotation.ui.Dock"
                    },
                    "title": "底部对齐"
                  },
                  {
                    "bindClass": [],
                    "caption": "左对齐",
                    "id": "left",
                    "name": "左对齐",
                    "tabindex": 3,
                    "tagVar": {
                      "name": "left",
                      "clazz": "net.ooder.esd.annotation.ui.Dock"
                    },
                    "title": "左对齐"
                  },
                  {
                    "bindClass": [],
                    "caption": "右对齐",
                    "id": "right",
                    "name": "右对齐",
                    "tabindex": 4,
                    "tagVar": {
                      "name": "right",
                      "clazz": "net.ooder.esd.annotation.ui.Dock"
                    },
                    "title": "右对齐"
                  },
                  {
                    "bindClass": [],
                    "caption": "居中对齐",
                    "id": "center",
                    "name": "居中对齐",
                    "tabindex": 5,
                    "tagVar": {
                      "name": "center",
                      "clazz": "net.ooder.esd.annotation.ui.Dock"
                    },
                    "title": "居中对齐"
                  },
                  {
                    "bindClass": [],
                    "caption": "垂直居中",
                    "id": "middle",
                    "name": "垂直居中",
                    "tabindex": 6,
                    "tagVar": {
                      "name": "middle",
                      "clazz": "net.ooder.esd.annotation.ui.Dock"
                    },
                    "title": "垂直居中"
                  },
                  {
                    "bindClass": [],
                    "caption": "自适应排列",
                    "id": "origin",
                    "name": "自适应排列",
                    "tabindex": 7,
                    "tagVar": {
                      "name": "origin",
                      "clazz": "net.ooder.esd.annotation.ui.Dock"
                    },
                    "title": "自适应排列"
                  },
                  {
                    "bindClass": [],
                    "caption": "宽度自适应",
                    "id": "width",
                    "name": "宽度自适应",
                    "tabindex": 8,
                    "tagVar": {
                      "name": "width",
                      "clazz": "net.ooder.esd.annotation.ui.Dock"
                    },
                    "title": "宽度自适应"
                  },
                  {
                    "bindClass": [],
                    "caption": "高度自适应",
                    "id": "height",
                    "name": "高度自适应",
                    "tabindex": 9,
                    "tagVar": {
                      "name": "height",
                      "clazz": "net.ooder.esd.annotation.ui.Dock"
                    },
                    "title": "高度自适应"
                  },
                  {
                    "bindClass": [],
                    "caption": "自动填充",
                    "id": "fill",
                    "name": "自动填充",
                    "tabindex": 10,
                    "tagVar": {
                      "name": "fill",
                      "clazz": "net.ooder.esd.annotation.ui.Dock"
                    },
                    "title": "自动填充"
                  },
                  {
                    "bindClass": [],
                    "caption": "覆盖",
                    "id": "cover",
                    "name": "覆盖",
                    "tabindex": 11,
                    "tagVar": {
                      "name": "cover",
                      "clazz": "net.ooder.esd.annotation.ui.Dock"
                    },
                    "title": "覆盖"
                  }
                ],
                "labelCaption": "按钮位置",
                "name": "dock",
                "readonly": false,
                "tabindex": 5,
                "type": "listbox",
                "visibility": "visible"
              },
              "target": "D3"
            },
            {
              "alias": "tagCmdsAlign",
              "events": {},
              "key": "ood.UI.ComboInput",
              "path": "dsm.view.config.menu.bottom.BottomMenuConfig.BottomMenuConfigMain.BottomMenuConfig.tagCmdsAlign",
              "properties": {
                "caption": "按钮对齐",
                "defaultFocus": false,
                "desc": "按钮对齐",
                "disableClickEffect": false,
                "disableHoverEffect": false,
                "disableTips": false,
                "disabled": false,
                "dock": "none",
                "dynLoad": true,
                "enumClass": "net.ooder.esd.annotation.ui.TagCmdsAlign",
                "items": [
                  {
                    "bindClass": [],
                    "caption": "left",
                    "id": "left",
                    "tabindex": 0,
                    "tagVar": {
                      "name": "left",
                      "clazz": "net.ooder.esd.annotation.ui.TagCmdsAlign"
                    },
                    "title": "left"
                  },
                  {
                    "bindClass": [],
                    "caption": "right",
                    "id": "right",
                    "tabindex": 1,
                    "tagVar": {
                      "name": "right",
                      "clazz": "net.ooder.esd.annotation.ui.TagCmdsAlign"
                    },
                    "title": "right"
                  },
                  {
                    "bindClass": [],
                    "caption": "floatright",
                    "id": "floatright",
                    "tabindex": 2,
                    "tagVar": {
                      "name": "floatright",
                      "clazz": "net.ooder.esd.annotation.ui.TagCmdsAlign"
                    },
                    "title": "floatright"
                  }
                ],
                "labelCaption": "按钮对齐",
                "name": "tagCmdsAlign",
                "readonly": false,
                "tabindex": 6,
                "type": "combobox",
                "visibility": "visible"
              },
              "target": "B4"
            },
            {
              "alias": "borderType",
              "events": {},
              "key": "ood.UI.ComboInput",
              "path": "dsm.view.config.menu.bottom.BottomMenuConfig.BottomMenuConfigMain.BottomMenuConfig.borderType",
              "properties": {
                "caption": "按钮边框",
                "defaultFocus": false,
                "desc": "按钮边框",
                "disableClickEffect": false,
                "disableHoverEffect": false,
                "disableTips": false,
                "disabled": false,
                "dock": "none",
                "dynLoad": true,
                "enumClass": "net.ooder.esd.annotation.ui.BorderType",
                "items": [
                  {
                    "bindClass": [],
                    "caption": "none",
                    "id": "none",
                    "tabindex": 0,
                    "tagVar": {
                      "name": "none",
                      "clazz": "net.ooder.esd.annotation.ui.BorderType"
                    },
                    "title": "none"
                  },
                  {
                    "bindClass": [],
                    "caption": "flat",
                    "id": "flat",
                    "tabindex": 1,
                    "tagVar": {
                      "name": "flat",
                      "clazz": "net.ooder.esd.annotation.ui.BorderType"
                    },
                    "title": "flat"
                  },
                  {
                    "bindClass": [],
                    "caption": "inset",
                    "id": "inset",
                    "tabindex": 2,
                    "tagVar": {
                      "name": "inset",
                      "clazz": "net.ooder.esd.annotation.ui.BorderType"
                    },
                    "title": "inset"
                  },
                  {
                    "bindClass": [],
                    "caption": "outset",
                    "id": "outset",
                    "tabindex": 3,
                    "tagVar": {
                      "name": "outset",
                      "clazz": "net.ooder.esd.annotation.ui.BorderType"
                    },
                    "title": "outset"
                  }
                ],
                "labelCaption": "按钮边框",
                "name": "borderType",
                "readonly": false,
                "tabindex": 7,
                "type": "combobox",
                "visibility": "visible"
              },
              "target": "D4"
            },
            {
              "alias": "connected",
              "events": {},
              "key": "ood.UI.CheckBox",
              "path": "dsm.view.config.menu.bottom.BottomMenuConfig.BottomMenuConfigMain.BottomMenuConfig.connected",
              "properties": {
                "caption": "",
                "defaultFocus": false,
                "desc": "自动对齐",
                "disableClickEffect": false,
                "disableHoverEffect": false,
                "disableTips": false,
                "dock": "none",
                "events": {},
                "id": "connected",
                "name": "connected",
                "tabindex": 8,
                "visibility": "visible"
              },
              "target": "B5"
            },
            {
              "alias": "itemPadding",
              "events": {},
              "key": "ood.UI.Input",
              "path": "dsm.view.config.menu.bottom.BottomMenuConfig.BottomMenuConfigMain.BottomMenuConfig.itemPadding",
              "properties": {
                "caption": "按钮间距",
                "defaultFocus": false,
                "desc": "按钮间距",
                "disableClickEffect": false,
                "disableHoverEffect": false,
                "disableTips": false,
                "disabled": false,
                "dock": "none",
                "events": {},
                "id": "itemPadding",
                "imageClass": "",
                "items": [],
                "labelCaption": "按钮间距",
                "labelPos": "left",
                "labelSize": "6.0em",
                "name": "itemPadding",
                "readonly": false,
                "tabindex": 9,
                "tagVar": {},
                "visibility": "visible"
              },
              "target": "D5"
            },
            {
              "alias": "itemMargin",
              "events": {},
              "key": "ood.UI.Input",
              "path": "dsm.view.config.menu.bottom.BottomMenuConfig.BottomMenuConfigMain.BottomMenuConfig.itemMargin",
              "properties": {
                "caption": "按钮边距",
                "defaultFocus": false,
                "desc": "按钮边距",
                "disableClickEffect": false,
                "disableHoverEffect": false,
                "disableTips": false,
                "disabled": false,
                "dock": "none",
                "events": {},
                "id": "itemMargin",
                "imageClass": "",
                "items": [],
                "labelCaption": "按钮边距",
                "labelPos": "left",
                "labelSize": "6.0em",
                "name": "itemMargin",
                "readonly": false,
                "tabindex": 10,
                "tagVar": {},
                "visibility": "visible"
              },
              "target": "B6"
            },
            {
              "alias": "itemWidth",
              "events": {},
              "key": "ood.UI.Input",
              "path": "dsm.view.config.menu.bottom.BottomMenuConfig.BottomMenuConfigMain.BottomMenuConfig.itemWidth",
              "properties": {
                "caption": "按钮宽度",
                "defaultFocus": false,
                "desc": "按钮宽度",
                "disableClickEffect": false,
                "disableHoverEffect": false,
                "disableTips": false,
                "disabled": false,
                "dock": "none",
                "events": {},
                "id": "itemWidth",
                "imageClass": "",
                "items": [],
                "labelCaption": "按钮宽度",
                "labelPos": "left",
                "labelSize": "6.0em",
                "name": "itemWidth",
                "readonly": false,
                "tabindex": 11,
                "tagVar": {},
                "visibility": "visible"
              },
              "target": "D6"
            },
            {
              "alias": "itemHeight",
              "events": {},
              "key": "ood.UI.Input",
              "path": "dsm.view.config.menu.bottom.BottomMenuConfig.BottomMenuConfigMain.BottomMenuConfig.itemHeight",
              "properties": {
                "caption": "按钮高度",
                "defaultFocus": false,
                "desc": "按钮高度",
                "disableClickEffect": false,
                "disableHoverEffect": false,
                "disableTips": false,
                "disabled": false,
                "dock": "none",
                "events": {},
                "id": "itemHeight",
                "imageClass": "",
                "items": [],
                "labelCaption": "按钮高度",
                "labelPos": "left",
                "labelSize": "6.0em",
                "name": "itemHeight",
                "readonly": false,
                "tabindex": 12,
                "tagVar": {},
                "visibility": "visible"
              },
              "target": "B7"
            }
          ],
          "events": {},
          "key": "ood.UI.FormLayout",
          "path": "dsm.view.config.menu.bottom.BottomMenuConfig.BottomMenuConfigMain.BottomMenuConfig",
          "properties": {
            "caption": "基础信息",
            "defaultColWidth": 150,
            "defaultColumnSize": -1,
            "defaultFocus": false,
            "defaultRowHeight": 35,
            "defaultRowSize": -1,
            "disableClickEffect": false,
            "disableHoverEffect": false,
            "disableTips": false,
            "disabled": false,
            "dock": "fill",
            "floatHandler": false,
            "id": "bottomMenuConfig",
            "imageClass": "ri-file-text-line",
            "layoutData": {
              "cells": {
                "C1": {
                  "cellName": "C1",
                  "style": {
                    "textAlign": "center"
                  },
                  "value": "容器高度"
                },
                "C2": {
                  "cellName": "C2",
                  "style": {
                    "textAlign": "center"
                  },
                  "value": "容器边框"
                },
                "A1": {
                  "cellName": "A1",
                  "style": {
                    "textAlign": "center"
                  },
                  "value": "容器位置"
                },
                "C3": {
                  "cellName": "C3",
                  "style": {
                    "textAlign": "center"
                  },
                  "value": "按钮位置"
                },
                "A2": {
                  "cellName": "A2",
                  "style": {
                    "textAlign": "center"
                  },
                  "value": "按钮对齐方式"
                },
                "C4": {
                  "cellName": "C4",
                  "style": {
                    "textAlign": "center"
                  },
                  "value": "按钮边框"
                },
                "A3": {
                  "cellName": "A3",
                  "style": {
                    "textAlign": "center"
                  },
                  "value": "按钮类型"
                },
                "C5": {
                  "cellName": "C5",
                  "style": {
                    "textAlign": "center"
                  },
                  "value": "按钮间距"
                },
                "A4": {
                  "cellName": "A4",
                  "style": {
                    "textAlign": "center"
                  },
                  "value": "按钮对齐"
                },
                "C6": {
                  "cellName": "C6",
                  "style": {
                    "textAlign": "center"
                  },
                  "value": "按钮宽度"
                },
                "A5": {
                  "cellName": "A5",
                  "style": {
                    "textAlign": "center"
                  },
                  "value": "自动对齐"
                },
                "A6": {
                  "cellName": "A6",
                  "style": {
                    "textAlign": "center"
                  },
                  "value": "按钮边距"
                },
                "A7": {
                  "cellName": "A7",
                  "style": {
                    "textAlign": "center"
                  },
                  "value": "按钮高度"
                }
              },
              "colSetting": {
                "A": {
                  "manualWidth": 150
                },
                "B": {
                  "manualWidth": 150
                },
                "C": {
                  "manualWidth": 150
                },
                "D": {
                  "manualWidth": 150
                }
              },
              "cols": 4,
              "rowSetting": {
                "1": {
                  "manualHeight": 35
                },
                "2": {
                  "manualHeight": 35
                },
                "3": {
                  "manualHeight": 35
                },
                "4": {
                  "manualHeight": 35
                },
                "5": {
                  "manualHeight": 35
                },
                "6": {
                  "manualHeight": 35
                },
                "7": {
                  "manualHeight": 35
                }
              },
              "rows": 7
            },
            "lineSpacing": 10,
            "mode": "write",
            "name": "GetBottomMenuConfig",
            "solidGridlines": true,
            "stretchH": "all",
            "stretchHeight": "none",
            "tabindex": 0,
            "tagVar": {},
            "visibility": "visible"
          }
        }
      ],
      "events": {},
      "key": "ood.UI.Block",
      "path": "dsm.view.config.menu.bottom.BottomMenuConfig.BottomMenuConfigMain",
      "properties": {
        "dock": "fill",
        "panelBgClr": "transparent",
        "tabindex": 1
      }
    },
    {
      "alias": "RELOAD",
      "events": {},
      "key": "ood.APICaller",
      "path": "dsm.view.config.menu.bottom.BottomMenuConfig.RELOAD",
      "properties": {
        "allform": false,
        "autoRun": true,
        "checkRequired": false,
        "checkValid": false,
        "desc": "基础信息",
        "id": "/dsm/view/config/menu/bottom/BottomMenuConfig",
        "imageClass": "ri-code-box-line",
        "isAllform": false,
        "methodName": "getBottomMenuConfig",
        "name": "BottomMenuConfig",
        "proxyType": "auto",
        "queryAsync": true,
        "queryMethod": "POST",
        "queryURL": "/dsm/view/config/menu/bottom/BottomMenuConfig",
        "requestDataSource": [
          {
            "name": "BottomMenuConfigMain",
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
            "name": "BottomMenuConfigMain",
            "path": "data",
            "type": "FORM"
          },
          {
            "name": "PAGECTX",
            "path": "data",
            "type": "FORM"
          }
        ],
        "responseType": "JSON",
        "sourceClassName": "net.ooder.dsm.view.config.menu.bottommenu.BottomBarNav",
        "tabindex": 2,
        "tips": "基础信息"
      }
    }
  ],
  "className": "dsm.view.config.menu.bottom.BottomMenuConfig",
  "customFunctions": {},
  "desc": "基础信息",
  "dio": false,
  "events": {},
  "formulas": [],
  "key": "ood.Module",
  "moduleType": "EUClass",
  "moduleVar": {},
  "moduleViewType": "FORMCONFIG",
  "path": "dsm.view.config.menu.bottom.BottomMenuConfig",
  "properties": {
    "autoDestroy": true,
    "bindClass": [],
    "caption": "基础信息",
    "currComponentAlias": "BottomMenuConfig",
    "dock": "fill",
    "title": "基础信息"
  }
}