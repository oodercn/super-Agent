{
  "alias": "CodeTempsNav",
  "children": [
    {
      "alias": "PAGECTX",
      "children": [
        {
          "alias": "projectName",
          "events": {},
          "key": "ood.UI.HiddenInput",
          "path": "dsm.temp.website.CodeTempsNav.PAGECTX.projectName",
          "properties": {
            "formField": true,
            "isPid": false,
            "name": "projectName",
            "pid": false,
            "tabindex": 0,
            "value": "DSMdsmVVVERSION0"
          }
        },
        {
          "alias": "dsmTempId",
          "events": {},
          "key": "ood.UI.HiddenInput",
          "path": "dsm.temp.website.CodeTempsNav.PAGECTX.dsmTempId",
          "properties": {
            "formField": true,
            "isPid": true,
            "name": "dsmTempId",
            "pid": true,
            "tabindex": 1
          }
        },
        {
          "alias": "projectVersionName",
          "events": {},
          "key": "ood.UI.HiddenInput",
          "path": "dsm.temp.website.CodeTempsNav.PAGECTX.projectVersionName",
          "properties": {
            "formField": true,
            "isPid": false,
            "name": "projectVersionName",
            "pid": false,
            "tabindex": 2,
            "value": "DSMdsmVVVERSION0"
          }
        },
        {
          "alias": "dsmTempType",
          "events": {},
          "key": "ood.UI.HiddenInput",
          "path": "dsm.temp.website.CodeTempsNav.PAGECTX.dsmTempType",
          "properties": {
            "formField": true,
            "isPid": true,
            "name": "dsmTempType",
            "pid": true,
            "tabindex": 3
          }
        },
        {
          "alias": "customDomainType",
          "events": {},
          "key": "ood.UI.HiddenInput",
          "path": "dsm.temp.website.CodeTempsNav.PAGECTX.customDomainType",
          "properties": {
            "formField": true,
            "isPid": true,
            "name": "customDomainType",
            "pid": true,
            "tabindex": 4
          }
        },
        {
          "alias": "dsmType",
          "events": {},
          "key": "ood.UI.HiddenInput",
          "path": "dsm.temp.website.CodeTempsNav.PAGECTX.dsmType",
          "properties": {
            "formField": true,
            "isPid": true,
            "name": "dsmType",
            "pid": true,
            "tabindex": 5
          }
        },
        {
          "alias": "repositoryType",
          "events": {},
          "key": "ood.UI.HiddenInput",
          "path": "dsm.temp.website.CodeTempsNav.PAGECTX.repositoryType",
          "properties": {
            "formField": true,
            "isPid": true,
            "name": "repositoryType",
            "pid": true,
            "tabindex": 6
          }
        },
        {
          "alias": "userSpace",
          "events": {},
          "key": "ood.UI.HiddenInput",
          "path": "dsm.temp.website.CodeTempsNav.PAGECTX.userSpace",
          "properties": {
            "formField": true,
            "isPid": true,
            "name": "userSpace",
            "pid": true,
            "tabindex": 7
          }
        },
        {
          "alias": "itemDomainType",
          "events": {},
          "key": "ood.UI.HiddenInput",
          "path": "dsm.temp.website.CodeTempsNav.PAGECTX.itemDomainType",
          "properties": {
            "formField": true,
            "isPid": true,
            "name": "itemDomainType",
            "pid": true,
            "tabindex": 8
          }
        },
        {
          "alias": "viewType",
          "events": {},
          "key": "ood.UI.HiddenInput",
          "path": "dsm.temp.website.CodeTempsNav.PAGECTX.viewType",
          "properties": {
            "formField": true,
            "isPid": true,
            "name": "viewType",
            "pid": true,
            "tabindex": 9
          }
        },
        {
          "alias": "aggregationType",
          "events": {},
          "key": "ood.UI.HiddenInput",
          "path": "dsm.temp.website.CodeTempsNav.PAGECTX.aggregationType",
          "properties": {
            "formField": true,
            "isPid": true,
            "name": "aggregationType",
            "pid": true,
            "tabindex": 10
          }
        },
        {
          "alias": "javaTempId",
          "events": {},
          "key": "ood.UI.HiddenInput",
          "path": "dsm.temp.website.CodeTempsNav.PAGECTX.javaTempId",
          "properties": {
            "formField": true,
            "isPid": true,
            "name": "javaTempId",
            "pid": true,
            "tabindex": 11
          }
        }
      ],
      "events": {},
      "key": "ood.UI.Block",
      "params": {},
      "path": "dsm.temp.website.CodeTempsNav.PAGECTX",
      "properties": {
        "borderType": "none",
        "tabindex": 0,
        "visibility": "hidden"
      }
    },
    {
      "alias": "CodeTempsNavMain",
      "children": [
        {
          "alias": "CodeTempsNavLAYOUT",
          "children": [
            {
              "alias": "WebSiteTreeViewPanel",
              "children": [
                {
                  "alias": "WebSiteTreeView",
                  "events": {
                    "onItemSelected": {
                      "actions": [
                        {
                          "args": [],
                          "conditions": [
                            {
                              "symbol": "non-empty",
                              "left": "{args[1].euClassName}",
                              "right": ""
                            }
                          ],
                          "desc": "清空",
                          "eventClass": "net.ooder.esd.annotation.event.TreeViewEventEnum",
                          "eventValue": "onItemSelected",
                          "method": "clearItems",
                          "target": "getCodeTempsNav",
                          "type": "control"
                        },
                        {
                          "args": [
                            "{args[1]}"
                          ],
                          "conditions": [
                            {
                              "symbol": "non-empty",
                              "left": "{args[1].euClassName}",
                              "right": ""
                            }
                          ],
                          "desc": "添加TAB页",
                          "eventClass": "net.ooder.esd.annotation.event.TreeViewEventEnum",
                          "eventValue": "onItemSelected",
                          "method": "insertItems2",
                          "target": "getCodeTempsNav",
                          "type": "control"
                        },
                        {
                          "args": [
                            "{args[1].id}"
                          ],
                          "conditions": [
                            {
                              "symbol": "non-empty",
                              "left": "{args[1].euClassName}",
                              "right": ""
                            }
                          ],
                          "desc": "添加点击事件",
                          "eventClass": "net.ooder.esd.annotation.event.TreeViewEventEnum",
                          "eventValue": "onItemSelected",
                          "method": "fireItemClickEvent",
                          "target": "getCodeTempsNav",
                          "type": "control"
                        }
                      ]
                    }
                  },
                  "key": "ood.UI.TreeView",
                  "path": "dsm.temp.website.CodeTempsNav.CodeTempsNavMain.CodeTempsNavLAYOUT.WebSiteTreeViewPanel.WebSiteTreeView",
                  "properties": {
                    "caption": "WebSiteTreeView",
                    "defaultFocus": false,
                    "disableClickEffect": false,
                    "disableHoverEffect": false,
                    "disableTips": false,
                    "disabled": false,
                    "dock": "none",
                    "formField": true,
                    "initFold": false,
                    "items": [
                      {
                        "bindClass": [
                          "java.lang.Void"
                        ],
                        "caption": "WebSiteTreeView",
                        "id": "codeTempsNav",
                        "imageClass": "ri-tools-line",
                        "initFold": false,
                        "pattern": "",
                        "sub": [
                          {
                            "bindClass": [
                              "net.ooder.dsm.website.temp.service.WebSiteDomainTreeService"
                            ],
                            "bindClassName": "net.ooder.dsm.website.temp.service.WebSiteDomainTreeService",
                            "caption": "JAVA模板（null)",
                            "closeBtn": false,
                            "dynDestory": true,
                            "dynLoad": true,
                            "entityClass": "net.ooder.esd.dsm.domain.enums.NavDomainType",
                            "groupName": "dsm_temp_website_AllCustomDomainTempGrid",
                            "id": "dsm_temp_website_AllCustomDomainTempGrid",
                            "imageClass": "ri-node-tree",
                            "pattern": "",
                            "tagVar": {
                              "name": "allCustomDomainTempGrid"
                            },
                            "title": "JAVA模板（null)"
                          },
                          {
                            "bindClass": [
                              "net.ooder.dsm.website.temp.service.WebSiteViewTreeService"
                            ],
                            "bindClassName": "net.ooder.dsm.website.temp.service.WebSiteViewTreeService",
                            "caption": "JAVA模板（null)",
                            "closeBtn": false,
                            "dynDestory": false,
                            "dynLoad": false,
                            "entityClass": "net.ooder.esd.annotation.ViewType",
                            "groupName": "dsm_temp_website_AllViewTempGrid",
                            "id": "dsm_temp_website_AllViewTempGrid",
                            "imageClass": "ri-node-tree",
                            "pattern": "",
                            "tagVar": {
                              "name": "allViewTempGrid"
                            },
                            "title": "JAVA模板（null)"
                          },
                          {
                            "bindClass": [
                              "net.ooder.dsm.website.temp.service.WebSiteAggregationService"
                            ],
                            "bindClassName": "net.ooder.dsm.website.temp.service.WebSiteAggregationService",
                            "caption": "JAVA模板（null)",
                            "closeBtn": false,
                            "dynDestory": false,
                            "dynLoad": false,
                            "entityClass": "net.ooder.annotation.AggregationType",
                            "groupName": "dsm_temp_website_WebSiteAggregation",
                            "id": "dsm_temp_website_WebSiteAggregation",
                            "imageClass": "ri-node-tree",
                            "pattern": "",
                            "tagVar": {
                              "name": "webSiteAggregation"
                            },
                            "title": "JAVA模板（null)"
                          },
                          {
                            "bindClass": [
                              "net.ooder.dsm.website.temp.service.WebSiteRepositoryAdminService"
                            ],
                            "bindClassName": "net.ooder.dsm.website.temp.service.WebSiteRepositoryAdminService",
                            "caption": "JAVA模板（null)",
                            "closeBtn": false,
                            "dynDestory": false,
                            "dynLoad": false,
                            "entityClass": "net.ooder.esd.dsm.enums.RepositoryType",
                            "groupName": "dsm_temp_website_WebSiteRepositoryAdmin",
                            "id": "dsm_temp_website_WebSiteRepositoryAdmin",
                            "imageClass": "ri-node-tree",
                            "pattern": "",
                            "tagVar": {
                              "name": "webSiteRepositoryAdmin"
                            },
                            "title": "JAVA模板（null)"
                          },
                          {
                            "bindClass": [
                              "net.ooder.dsm.website.temp.service.WebSiteDomainService"
                            ],
                            "bindClassName": "net.ooder.dsm.website.temp.service.WebSiteDomainService",
                            "caption": "JAVA模板（null)",
                            "closeBtn": false,
                            "dynDestory": false,
                            "dynLoad": false,
                            "entityClass": "net.ooder.esd.dsm.domain.enums.CustomDomainType",
                            "groupName": "dsm_temp_website_LoadChildCustomDomain",
                            "id": "dsm_temp_website_LoadChildCustomDomain",
                            "imageClass": "ri-node-tree",
                            "pattern": "",
                            "tagVar": {
                              "name": "loadChildCustomDomain"
                            },
                            "title": "JAVA模板（null)"
                          },
                          {
                            "bindClass": [
                              "net.ooder.dsm.website.temp.service.WebSiteAdminTempService"
                            ],
                            "bindClassName": "net.ooder.dsm.website.temp.service.WebSiteAdminTempService",
                            "caption": "模板分类",
                            "closeBtn": false,
                            "dynDestory": false,
                            "dynLoad": false,
                            "entityClass": "net.ooder.esd.dsm.enums.DSMType",
                            "groupName": "dsm_temp_website_LoadChildDSM",
                            "id": "dsm_temp_website_LoadChildDSM",
                            "imageClass": "ri-tools-line",
                            "pattern": "",
                            "tagVar": {
                              "name": "loadChildDSM"
                            },
                            "title": "模板分类"
                          },
                          {
                            "bindClass": [
                              "net.ooder.dsm.website.temp.service.WebSiteUserSpaceService"
                            ],
                            "bindClassName": "net.ooder.dsm.website.temp.service.WebSiteUserSpaceService",
                            "caption": "JAVA模板（null)",
                            "closeBtn": false,
                            "dynDestory": false,
                            "dynLoad": false,
                            "entityClass": "net.ooder.annotation.UserSpace",
                            "groupName": "dsm_temp_website_LoadUserSpaceType",
                            "id": "dsm_temp_website_LoadUserSpaceType",
                            "imageClass": "ri-node-tree",
                            "pattern": "",
                            "tagVar": {
                              "name": "loadUserSpaceType"
                            },
                            "title": "JAVA模板（null)"
                          },
                          {
                            "bindClass": [
                              "net.ooder.dsm.website.temp.tree.WebSiteTreeView"
                            ],
                            "bindClassName": "net.ooder.dsm.website.temp.tree.WebSiteTreeView",
                            "caption": "WebSiteTreeView",
                            "closeBtn": false,
                            "dynDestory": false,
                            "dynLoad": false,
                            "entityClass": "net.ooder.dsm.website.temp.tree.WebSiteTreeView",
                            "id": "null",
                            "imageClass": "ri-tools-line",
                            "pattern": "",
                            "tagVar": {
                              "name": "codeTempsNav"
                            },
                            "title": "WebSiteTreeView"
                          },
                          {
                            "bindClass": [
                              "net.ooder.dsm.website.temp.WebSiteJavaTempService"
                            ],
                            "bindClassName": "net.ooder.dsm.website.temp.WebSiteJavaTempService",
                            "caption": "loadUserSpaceType",
                            "id": "loadUserSpaceType",
                            "imageClass": "ri-tree-line",
                            "initFold": false,
                            "pattern": "",
                            "sub": [
                              {
                                "bindClass": [
                                  "net.ooder.dsm.website.temp.service.WebSiteDomainTreeService"
                                ],
                                "bindClassName": "net.ooder.dsm.website.temp.service.WebSiteDomainTreeService",
                                "caption": "loadUserSpaceType（null)",
                                "closeBtn": false,
                                "dynDestory": true,
                                "dynLoad": true,
                                "entityClass": "net.ooder.esd.dsm.domain.enums.NavDomainType",
                                "groupName": "dsm_temp_website_AllCustomDomainTempGrid",
                                "id": "loadUserSpaceType_dsm_temp_website_AllCustomDomainTempGrid",
                                "imageClass": "ri-node-tree",
                                "pattern": "",
                                "tagVar": {
                                  "name": "allCustomDomainTempGrid"
                                },
                                "title": "loadUserSpaceType（null)"
                              },
                              {
                                "bindClass": [
                                  "net.ooder.esd.dsm.temp.JavaTemp"
                                ],
                                "bindClassName": "net.ooder.esd.dsm.temp.JavaTemp",
                                "caption": "模版节点",
                                "closeBtn": false,
                                "dynDestory": false,
                                "dynLoad": false,
                                "entityClass": "net.ooder.esd.dsm.temp.JavaTemp",
                                "id": "loadUserSpaceType_null",
                                "imageClass": "ri-file-text-line",
                                "pattern": "",
                                "tagVar": {
                                  "name": "null"
                                },
                                "title": "模版节点"
                              },
                              {
                                "bindClass": [
                                  "net.ooder.dsm.website.temp.service.WebSiteAdminTempService"
                                ],
                                "bindClassName": "net.ooder.dsm.website.temp.service.WebSiteAdminTempService",
                                "caption": "模板分类",
                                "closeBtn": false,
                                "dynDestory": false,
                                "dynLoad": false,
                                "entityClass": "net.ooder.esd.dsm.enums.DSMType",
                                "groupName": "dsm_temp_website_LoadChildDSM",
                                "id": "loadUserSpaceType_dsm_temp_website_LoadChildDSM",
                                "imageClass": "ri-tools-line",
                                "pattern": "",
                                "tagVar": {
                                  "name": "loadChildDSM"
                                },
                                "title": "模板分类"
                              },
                              {
                                "bindClass": [
                                  "net.ooder.dsm.website.temp.service.WebSiteDomainService"
                                ],
                                "bindClassName": "net.ooder.dsm.website.temp.service.WebSiteDomainService",
                                "caption": "loadUserSpaceType（null)",
                                "closeBtn": false,
                                "dynDestory": false,
                                "dynLoad": false,
                                "entityClass": "net.ooder.esd.dsm.domain.enums.CustomDomainType",
                                "groupName": "dsm_temp_website_LoadChildCustomDomain",
                                "id": "loadUserSpaceType_dsm_temp_website_LoadChildCustomDomain",
                                "imageClass": "ri-node-tree",
                                "pattern": "",
                                "tagVar": {
                                  "name": "loadChildCustomDomain"
                                },
                                "title": "loadUserSpaceType（null)"
                              },
                              {
                                "bindClass": [
                                  "net.ooder.dsm.website.temp.service.WebSiteAggTempService"
                                ],
                                "bindClassName": "net.ooder.dsm.website.temp.service.WebSiteAggTempService",
                                "caption": "聚合类型",
                                "closeBtn": false,
                                "dynDestory": false,
                                "dynLoad": false,
                                "entityClass": "net.ooder.annotation.AggregationType",
                                "groupName": "dsm_temp_website_LoadAggTemps",
                                "id": "loadUserSpaceType_dsm_temp_website_LoadAggTemps",
                                "imageClass": "ri-node-tree",
                                "pattern": "",
                                "tagVar": {
                                  "name": "loadAggTemps"
                                },
                                "title": "聚合类型"
                              },
                              {
                                "bindClass": [
                                  "net.ooder.dsm.website.temp.service.WebSiteUserSpaceService"
                                ],
                                "bindClassName": "net.ooder.dsm.website.temp.service.WebSiteUserSpaceService",
                                "caption": "仓储分类",
                                "closeBtn": false,
                                "dynDestory": true,
                                "dynLoad": false,
                                "entityClass": "net.ooder.annotation.UserSpace",
                                "groupName": "dsm_temp_website_LoadUserSpaceType",
                                "id": "loadUserSpaceType_dsm_temp_website_LoadUserSpaceType",
                                "imageClass": "ri-node-tree",
                                "pattern": "",
                                "tagVar": {
                                  "name": "loadUserSpaceType"
                                },
                                "title": "仓储分类"
                              },
                              {
                                "bindClass": [
                                  "net.ooder.dsm.website.temp.service.WebSiteRepositoryTempService"
                                ],
                                "bindClassName": "net.ooder.dsm.website.temp.service.WebSiteRepositoryTempService",
                                "caption": "仓储类型",
                                "closeBtn": false,
                                "dynDestory": true,
                                "dynLoad": false,
                                "entityClass": "net.ooder.esd.dsm.enums.RepositoryType",
                                "groupName": "dsm_temp_website_LoadRepositoryTemps",
                                "id": "loadUserSpaceType_dsm_temp_website_LoadRepositoryTemps",
                                "imageClass": "ri-node-tree",
                                "pattern": "",
                                "tagVar": {
                                  "name": "loadRepositoryTemps"
                                },
                                "title": "仓储类型"
                              },
                              {
                                "bindClass": [
                                  "net.ooder.dsm.website.temp.service.WebSiteViewTempService"
                                ],
                                "bindClassName": "net.ooder.dsm.website.temp.service.WebSiteViewTempService",
                                "caption": "loadUserSpaceType（null)",
                                "closeBtn": false,
                                "dynDestory": false,
                                "dynLoad": false,
                                "entityClass": "net.ooder.esd.annotation.ViewType",
                                "groupName": "dsm_temp_website_LoadViewTemps",
                                "id": "loadUserSpaceType_dsm_temp_website_LoadViewTemps",
                                "imageClass": "ri-node-tree",
                                "pattern": "",
                                "tagVar": {
                                  "name": "loadViewTemps"
                                },
                                "title": "loadUserSpaceType（null)"
                              },
                              {
                                "bindClass": [
                                  "java.lang.Void"
                                ],
                                "caption": "loadChildDSM",
                                "id": "loadUserSpaceType_loadChildDSM",
                                "imageClass": "ri-tree-line",
                                "initFold": false,
                                "pattern": "",
                                "sub": [
                                  {
                                    "bindClass": [
                                      "net.ooder.dsm.website.temp.service.WebSiteDomainTreeService"
                                    ],
                                    "bindClassName": "net.ooder.dsm.website.temp.service.WebSiteDomainTreeService",
                                    "caption": "JAVA模板（null)",
                                    "closeBtn": false,
                                    "dynDestory": true,
                                    "dynLoad": true,
                                    "entityClass": "net.ooder.esd.dsm.domain.enums.OrgDomainType",
                                    "groupName": "dsm_temp_website_AllCustomDomainTempGrid",
                                    "id": "loadUserSpaceType_loadChildDSM_dsm_temp_website_AllCustomDomainTempGrid",
                                    "imageClass": "ri-node-tree",
                                    "pattern": "",
                                    "tagVar": {
                                      "name": "allCustomDomainTempGrid"
                                    },
                                    "title": "JAVA模板（null)"
                                  },
                                  {
                                    "bindClass": [
                                      "net.ooder.dsm.website.temp.service.WebSiteViewTreeService"
                                    ],
                                    "bindClassName": "net.ooder.dsm.website.temp.service.WebSiteViewTreeService",
                                    "caption": "JAVA模板（null)",
                                    "closeBtn": false,
                                    "dynDestory": false,
                                    "dynLoad": false,
                                    "entityClass": "net.ooder.esd.annotation.ViewType",
                                    "groupName": "dsm_temp_website_AllViewTempGrid",
                                    "id": "loadUserSpaceType_loadChildDSM_dsm_temp_website_AllViewTempGrid",
                                    "imageClass": "ri-node-tree",
                                    "pattern": "",
                                    "tagVar": {
                                      "name": "allViewTempGrid"
                                    },
                                    "title": "JAVA模板（null)"
                                  },
                                  {
                                    "bindClass": [
                                      "net.ooder.dsm.website.temp.service.WebSiteAggregationService"
                                    ],
                                    "bindClassName": "net.ooder.dsm.website.temp.service.WebSiteAggregationService",
                                    "caption": "JAVA模板（null)",
                                    "closeBtn": false,
                                    "dynDestory": false,
                                    "dynLoad": false,
                                    "entityClass": "net.ooder.annotation.AggregationType",
                                    "groupName": "dsm_temp_website_WebSiteAggregation",
                                    "id": "loadUserSpaceType_loadChildDSM_dsm_temp_website_WebSiteAggregation",
                                    "imageClass": "ri-node-tree",
                                    "pattern": "",
                                    "tagVar": {
                                      "name": "webSiteAggregation"
                                    },
                                    "title": "JAVA模板（null)"
                                  },
                                  {
                                    "bindClass": [
                                      "net.ooder.dsm.website.temp.service.WebSiteRepositoryAdminService"
                                    ],
                                    "bindClassName": "net.ooder.dsm.website.temp.service.WebSiteRepositoryAdminService",
                                    "caption": "JAVA模板（null)",
                                    "closeBtn": false,
                                    "dynDestory": false,
                                    "dynLoad": false,
                                    "entityClass": "net.ooder.esd.dsm.enums.RepositoryType",
                                    "groupName": "dsm_temp_website_WebSiteRepositoryAdmin",
                                    "id": "loadUserSpaceType_loadChildDSM_dsm_temp_website_WebSiteRepositoryAdmin",
                                    "imageClass": "ri-node-tree",
                                    "pattern": "",
                                    "tagVar": {
                                      "name": "webSiteRepositoryAdmin"
                                    },
                                    "title": "JAVA模板（null)"
                                  },
                                  {
                                    "bindClass": [
                                      "net.ooder.dsm.website.temp.service.WebSiteAdminTempService"
                                    ],
                                    "bindClassName": "net.ooder.dsm.website.temp.service.WebSiteAdminTempService",
                                    "caption": "模板分类",
                                    "closeBtn": false,
                                    "dynDestory": false,
                                    "dynLoad": false,
                                    "entityClass": "net.ooder.esd.dsm.enums.DSMType",
                                    "groupName": "dsm_temp_website_LoadChildDSM",
                                    "id": "loadUserSpaceType_loadChildDSM_dsm_temp_website_LoadChildDSM",
                                    "imageClass": "ri-tools-line",
                                    "pattern": "",
                                    "tagVar": {
                                      "name": "loadChildDSM"
                                    },
                                    "title": "模板分类"
                                  },
                                  {
                                    "bindClass": [
                                      "net.ooder.dsm.website.temp.service.WebSiteDomainService"
                                    ],
                                    "bindClassName": "net.ooder.dsm.website.temp.service.WebSiteDomainService",
                                    "caption": "JAVA模板（null)",
                                    "closeBtn": false,
                                    "dynDestory": false,
                                    "dynLoad": false,
                                    "entityClass": "net.ooder.esd.dsm.domain.enums.CustomDomainType",
                                    "groupName": "dsm_temp_website_LoadChildCustomDomain",
                                    "id": "loadUserSpaceType_loadChildDSM_dsm_temp_website_LoadChildCustomDomain",
                                    "imageClass": "ri-node-tree",
                                    "pattern": "",
                                    "tagVar": {
                                      "name": "loadChildCustomDomain"
                                    },
                                    "title": "JAVA模板（null)"
                                  },
                                  {
                                    "bindClass": [
                                      "net.ooder.dsm.website.temp.service.WebSiteUserSpaceService"
                                    ],
                                    "bindClassName": "net.ooder.dsm.website.temp.service.WebSiteUserSpaceService",
                                    "caption": "JAVA模板（null)",
                                    "closeBtn": false,
                                    "dynDestory": false,
                                    "dynLoad": false,
                                    "entityClass": "net.ooder.annotation.UserSpace",
                                    "groupName": "dsm_temp_website_LoadUserSpaceType",
                                    "id": "loadUserSpaceType_loadChildDSM_dsm_temp_website_LoadUserSpaceType",
                                    "imageClass": "ri-node-tree",
                                    "pattern": "",
                                    "tagVar": {
                                      "name": "loadUserSpaceType"
                                    },
                                    "title": "JAVA模板（null)"
                                  }
                                ],
                                "title": "loadChildDSM"
                              }
                            ],
                            "title": "loadUserSpaceType"
                          }
                        ],
                        "tabindex": 0,
                        "title": "WebSiteTreeView"
                      }
                    ],
                    "selMode": "single",
                    "singleOpen": false,
                    "tabindex": 0,
                    "visibility": "visible"
                  }
                }
              ],
              "events": {},
              "key": "ood.UI.Block",
              "path": "dsm.temp.website.CodeTempsNav.CodeTempsNavMain.CodeTempsNavLAYOUT.WebSiteTreeViewPanel",
              "properties": {
                "borderType": "none",
                "dock": "fill",
                "tabindex": 0
              },
              "target": "before"
            },
            {
              "alias": "getCodeTempsNav",
              "events": {
                "onItemSelected": {
                  "actions": [
                    {
                      "args": [
                        "{ood.showModule2()}",
                        null,
                        null,
                        "{args[1].euClassName}",
                        "getCodeTempsNav",
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
                      "eventValue": "onItemSelected",
                      "id": "net.ooder.esd.annotation.action.CustomLoadClassAction_tabShow_null_null",
                      "method": "showModule2",
                      "redirection": "other:callback:call",
                      "return": false,
                      "target": "url",
                      "type": "other"
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
                        "getCodeTempsNav",
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
                }
              },
              "key": "ood.UI.Tabs",
              "path": "dsm.temp.website.CodeTempsNav.CodeTempsNavMain.CodeTempsNavLAYOUT.getCodeTempsNav",
              "properties": {
                "activeLast": true,
                "autoReload": true,
                "autoSave": false,
                "barLocation": "top",
                "barSize": "2.5em",
                "borderType": "none",
                "caption": "JAVA模板",
                "id": "codeTempsNav",
                "imageClass": "ri-tools-line",
                "initFold": false,
                "items": [],
                "name": "CodeTempsNav",
                "noHandler": true,
                "selMode": "single",
                "singleOpen": true,
                "tabindex": 1,
                "tagCmdsAlign": "floatright",
                "tagVar": {}
              },
              "target": "main"
            }
          ],
          "events": {},
          "key": "ood.UI.Layout",
          "path": "dsm.temp.website.CodeTempsNav.CodeTempsNavMain.CodeTempsNavLAYOUT",
          "properties": {
            "borderType": "none",
            "items": [
              {
                "bindClass": [],
                "flexSize": false,
                "folded": false,
                "id": "before",
                "max": 1000,
                "min": 100,
                "overflow": "auto",
                "pos": "before",
                "size": 260,
                "tabindex": 0,
                "transparent": true
              },
              {
                "bindClass": [],
                "flexSize": true,
                "id": "main",
                "max": 300,
                "overflow": "auto",
                "pos": "main",
                "tabindex": 1,
                "transparent": true
              }
            ],
            "tabindex": 0,
            "transparent": true,
            "type": "horizontal"
          }
        }
      ],
      "events": {},
      "key": "ood.UI.Block",
      "path": "dsm.temp.website.CodeTempsNav.CodeTempsNavMain",
      "properties": {
        "borderType": "none",
        "dock": "fill",
        "panelBgClr": "transparent",
        "tabindex": 1
      }
    },
    {
      "alias": "RELOAD",
      "events": {},
      "key": "ood.APICaller",
      "path": "dsm.temp.website.CodeTempsNav.RELOAD",
      "properties": {
        "allform": false,
        "autoRun": true,
        "checkRequired": false,
        "checkValid": false,
        "desc": "JAVA模板",
        "id": "/dsm/temp/website/CodeTempsNav",
        "imageClass": "ri-tools-line",
        "isAllform": false,
        "methodName": "getCodeTempsNav",
        "name": "CodeTempsNav",
        "proxyType": "auto",
        "queryAsync": true,
        "queryMethod": "POST",
        "queryURL": "/dsm/temp/website/CodeTempsNav",
        "requestDataSource": [
          {
            "name": "PAGECTX",
            "path": "",
            "type": "FORM"
          },
          {
            "name": "WebSiteTreeViewPanel",
            "path": "",
            "type": "FORM"
          }
        ],
        "requestType": "FORM",
        "responseCallback": [],
        "responseDataTarget": [
          {
            "name": "WebSiteTreeView",
            "path": "data",
            "type": "TREEVIEW"
          },
          {
            "name": "PAGECTX",
            "path": "data",
            "type": "FORM"
          }
        ],
        "responseType": "JSON",
        "sourceClassName": "net.ooder.dsm.website.manager.WebSiteNav",
        "tabindex": 2,
        "tips": "JAVA模板"
      }
    }
  ],
  "className": "dsm.temp.website.CodeTempsNav",
  "customFunctions": {},
  "desc": "JAVA模板",
  "dio": false,
  "events": {},
  "formulas": [],
  "itemType": "TREEVIEW",
  "key": "ood.Module",
  "moduleType": "EUClass",
  "moduleVar": {},
  "moduleViewType": "NAVTREECONFIG",
  "path": "dsm.temp.website.CodeTempsNav",
  "properties": {
    "autoDestroy": true,
    "bindClass": [],
    "caption": "JAVA模板",
    "dock": "fill",
    "title": "JAVA模板"
  }
}