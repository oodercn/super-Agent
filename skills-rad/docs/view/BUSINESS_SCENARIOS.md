# ooder-a2ui 业务场景示例

本文档展示了 ooder-a2ui 组件在实际业务场景中的应用，包括 OA、ERP、CRM 等系统的典型页面设计。通过这些示例，开发者可以了解如何将组件组合使用，构建完整的业务应用。

## 1. OA 系统 - 请假申请表单

### 1.1 场景描述
请假申请表单是 OA 系统中最常见的功能之一，用于员工提交请假申请。表单通常包含请假类型、请假时间、请假原因等字段。

### 1.2 组件使用

| 组件名称 | 用途 |
|----------|------|
| FormLayout | 表单布局 |
| Label | 字段标签 |
| Select | 请假类型选择 |
| DatePicker | 开始日期选择 |
| TimePicker | 开始时间选择 |
| DatePicker | 结束日期选择 |
| TimePicker | 结束时间选择 |
| TextArea | 请假原因输入 |
| CheckBox | 抄送人选择 |
| Button | 提交和取消按钮 |

### 1.3 实现示例

#### JSON 方式
```json
{
  "id": "leaveApplicationForm",
  "type": "Panel",
  "props": {
    "title": "请假申请表单",
    "padding": 20
  },
  "children": [
    {
      "type": "FormLayout",
      "props": {
        "columns": 2,
        "spacing": 20,
        "padding": 10
      },
      "children": [
        {
          "type": "Label",
          "props": {
            "text": "请假类型",
            "required": true,
            "width": 100
          }
        },
        {
          "type": "Select",
          "props": {
            "id": "leaveType",
            "width": 200,
            "options": [
              { "value": "annual", "label": "年假" },
              { "value": "sick", "label": "病假" },
              { "value": "personal", "label": "事假" },
              { "value": "marriage", "label": "婚假" },
              { "value": "funeral", "label": "丧假" }
            ]
          }
        },
        {
          "type": "Label",
          "props": {
            "text": "开始日期",
            "required": true,
            "width": 100
          }
        },
        {
          "type": "DatePicker",
          "props": {
            "id": "startDate",
            "width": 200
          }
        },
        {
          "type": "Label",
          "props": {
            "text": "开始时间",
            "required": true,
            "width": 100
          }
        },
        {
          "type": "TimePicker",
          "props": {
            "id": "startTime",
            "width": 200
          }
        },
        {
          "type": "Label",
          "props": {
            "text": "结束日期",
            "required": true,
            "width": 100
          }
        },
        {
          "type": "DatePicker",
          "props": {
            "id": "endDate",
            "width": 200
          }
        },
        {
          "type": "Label",
          "props": {
            "text": "结束时间",
            "required": true,
            "width": 100
          }
        },
        {
          "type": "TimePicker",
          "props": {
            "id": "endTime",
            "width": 200
          }
        },
        {
          "type": "Label",
          "props": {
            "text": "请假原因",
            "required": true,
            "width": 100
          }
        },
        {
          "type": "TextArea",
          "props": {
            "id": "reason",
            "width": 200,
            "height": 100,
            "placeholder": "请输入请假原因"
          }
        },
        {
          "type": "Label",
          "props": {
            "text": "抄送人",
            "width": 100
          }
        },
        {
          "type": "Stacks",
          "props": {
            "direction": "vertical",
            "spacing": 10,
            "width": 200
          },
          "children": [
            {
              "type": "CheckBox",
              "props": {
                "id": "ccManager",
                "text": "部门经理"
              }
            },
            {
              "type": "CheckBox",
              "props": {
                "id": "ccHr",
                "text": "人力资源"
              }
            },
            {
              "type": "CheckBox",
              "props": {
                "id": "ccColleague",
                "text": "同事"
              }
            }
          ]
        }
      ]
    },
    {
      "type": "Stacks",
      "props": {
        "direction": "horizontal",
        "justifyContent": "flex-end",
        "spacing": 10,
        "marginTop": 20
      },
      "children": [
        {
          "type": "Button",
          "props": {
            "text": "取消",
            "type": "default"
          }
        },
        {
          "type": "Button",
          "props": {
            "text": "提交申请",
            "type": "primary"
          }
        }
      ]
    }
  ]
}
```

#### JavaScript 方式
```javascript
const leaveApplicationForm = ood.create("Panel", {
  id: "leaveApplicationForm",
  title: "请假申请表单",
  padding: 20
});

// 创建表单布局
const formLayout = ood.create("FormLayout", {
  columns: 2,
  spacing: 20,
  padding: 10
});

// 请假类型
formLayout.addChild(ood.create("Label", {
  text: "请假类型",
  required: true,
  width: 100
}));

const leaveTypeSelect = ood.create("Select", {
  id: "leaveType",
  width: 200,
  options: [
    { value: "annual", label: "年假" },
    { value: "sick", label: "病假" },
    { value: "personal", label: "事假" },
    { value: "marriage", label: "婚假" },
    { value: "funeral", label: "丧假" }
  ]
});
formLayout.addChild(leaveTypeSelect);

// 开始日期和时间
formLayout.addChild(ood.create("Label", {
  text: "开始日期",
  required: true,
  width: 100
}));

const startDatePicker = ood.create("DatePicker", {
  id: "startDate",
  width: 200
});
formLayout.addChild(startDatePicker);

formLayout.addChild(ood.create("Label", {
  text: "开始时间",
  required: true,
  width: 100
}));

const startTimePicker = ood.create("TimePicker", {
  id: "startTime",
  width: 200
});
formLayout.addChild(startTimePicker);

// 结束日期和时间
formLayout.addChild(ood.create("Label", {
  text: "结束日期",
  required: true,
  width: 100
}));

const endDatePicker = ood.create("DatePicker", {
  id: "endDate",
  width: 200
});
formLayout.addChild(endDatePicker);

formLayout.addChild(ood.create("Label", {
  text: "结束时间",
  required: true,
  width: 100
}));

const endTimePicker = ood.create("TimePicker", {
  id: "endTime",
  width: 200
});
formLayout.addChild(endTimePicker);

// 请假原因
formLayout.addChild(ood.create("Label", {
  text: "请假原因",
  required: true,
  width: 100
}));

const reasonTextArea = ood.create("TextArea", {
  id: "reason",
  width: 200,
  height: 100,
  placeholder: "请输入请假原因"
});
formLayout.addChild(reasonTextArea);

// 抄送人
formLayout.addChild(ood.create("Label", {
  text: "抄送人",
  width: 100
}));

const ccStacks = ood.create("Stacks", {
  direction: "vertical",
  spacing: 10,
  width: 200
});

ccStacks.addChild(ood.create("CheckBox", {
  id: "ccManager",
  text: "部门经理"
}));

ccStacks.addChild(ood.create("CheckBox", {
  id: "ccHr",
  text: "人力资源"
}));

ccStacks.addChild(ood.create("CheckBox", {
  id: "ccColleague",
  text: "同事"
}));

formLayout.addChild(ccStacks);

// 按钮
const buttonStacks = ood.create("Stacks", {
  direction: "horizontal",
  justifyContent: "flex-end",
  spacing: 10,
  marginTop: 20
});

buttonStacks.addChild(ood.create("Button", {
  text: "取消",
  type: "default"
}));

buttonStacks.addChild(ood.create("Button", {
  text: "提交申请",
  type: "primary"
}));

// 组装表单
leaveApplicationForm.addChild(formLayout);
leaveApplicationForm.addChild(buttonStacks);

// 添加到页面
page.addChild(leaveApplicationForm);
```

### 1.4 最佳实践

- 使用 `FormLayout` 组件进行表单布局，保持字段对齐和间距一致
- 对于必填字段，添加 `required: true` 属性，显示必填标记
- 使用合适的组件类型，如日期选择使用 `DatePicker`，时间选择使用 `TimePicker`
- 合理组织表单字段顺序，从基本信息到详细信息
- 提供清晰的提交和取消按钮

## 2. ERP 系统 - 产品管理页面

### 2.1 场景描述
产品管理页面是 ERP 系统中的核心功能之一，用于管理产品信息，包括产品列表展示、搜索、筛选、新增、编辑和删除等操作。

### 2.2 组件使用

| 组件名称 | 用途 |
|----------|------|
| Layout | 页面布局 |
| Menu | 侧边导航 |
| Panel | 内容面板 |
| Stacks | 搜索区域布局 |
| TextInput | 产品名称搜索 |
| Select | 产品分类筛选 |
| Button | 搜索和新增按钮 |
| Table | 产品列表展示 |
| Pagination | 分页控件 |

### 2.3 实现示例

#### JSON 方式
```json
{
  "id": "productManagementPage",
  "type": "Layout",
  "props": {
    "id": "productLayout",
    "hasSider": true,
    "siderWidth": 200,
    "siderCollapsible": true
  },
  "children": [
    {
      "type": "Menu",
      "props": {
        "id": "sideMenu",
        "mode": "inline",
        "theme": "dark",
        "selectedKeys": ["products"],
        "items": [
          {
            "key": "dashboard",
            "label": "仪表盘",
            "icon": "dashboard"
          },
          {
            "key": "products",
            "label": "产品管理",
            "icon": "product"
          },
          {
            "key": "orders",
            "label": "订单管理",
            "icon": "order"
          },
          {
            "key": "inventory",
            "label": "库存管理",
            "icon": "inventory"
          },
          {
            "key": "suppliers",
            "label": "供应商管理",
            "icon": "supplier"
          }
        ]
      }
    },
    {
      "type": "Layout",
      "props": {
        "id": "contentLayout",
        "hasHeader": true,
        "headerHeight": 60
      },
      "children": [
        {
          "type": "Block",
          "props": {
            "id": "pageHeader",
            "style": {
              "backgroundColor": "#fff",
              "borderBottom": "1px solid #f0f0f0",
              "padding": "0 20px"
            }
          },
          "children": [
            {
              "type": "Label",
              "props": {
                "text": "产品管理",
                "style": {
                  "fontSize": 20,
                  "fontWeight": "bold",
                  "lineHeight": "60px"
                }
              }
            }
          ]
        },
        {
          "type": "Block",
          "props": {
            "id": "pageContent",
            "style": {
              "padding": "20px",
              "backgroundColor": "#f5f5f5"
            }
          },
          "children": [
            {
              "type": "Panel",
              "props": {
                "id": "productPanel",
                "title": "产品列表",
                "padding": 20
              },
              "children": [
                {
                  "type": "Stacks",
                  "props": {
                    "id": "searchArea",
                    "direction": "horizontal",
                    "justifyContent": "space-between",
                    "alignItems": "center",
                    "marginBottom": 20
                  },
                  "children": [
                    {
                      "type": "Stacks",
                      "props": {
                        "direction": "horizontal",
                        "spacing": 10,
                        "alignItems": "center"
                      },
                      "children": [
                        {
                          "type": "Label",
                          "props": {
                            "text": "产品名称:"
                          }
                        },
                        {
                          "type": "TextInput",
                          "props": {
                            "id": "productNameSearch",
                            "placeholder": "请输入产品名称",
                            "width": 200
                          }
                        },
                        {
                          "type": "Label",
                          "props": {
                            "text": "产品分类:"
                          }
                        },
                        {
                          "type": "Select",
                          "props": {
                            "id": "productCategoryFilter",
                            "placeholder": "请选择产品分类",
                            "width": 150,
                            "options": [
                              { "value": "electronics", "label": "电子产品" },
                              { "value": "clothing", "label": "服装" },
                              { "value": "home", "label": "家居" },
                              { "value": "sports", "label": "运动" }
                            ]
                          }
                        },
                        {
                          "type": "Button",
                          "props": {
                            "id": "searchButton",
                            "text": "搜索",
                            "type": "primary"
                          }
                        }
                      ]
                    },
                    {
                      "type": "Button",
                      "props": {
                        "id": "addProductButton",
                        "text": "新增产品",
                        "type": "primary",
                        "icon": "plus"
                      }
                    }
                  ]
                },
                {
                  "type": "Table",
                  "props": {
                    "id": "productTable",
                    "columns": [
                      { "title": "产品ID", "dataIndex": "id", "width": 80 },
                      { "title": "产品名称", "dataIndex": "name", "width": 200 },
                      { "title": "产品分类", "dataIndex": "category", "width": 120 },
                      { "title": "价格", "dataIndex": "price", "width": 100, "align": "right" },
                      { "title": "库存", "dataIndex": "stock", "width": 100, "align": "right" },
                      { "title": "状态", "dataIndex": "status", "width": 100 },
                      { "title": "操作", "dataIndex": "actions", "width": 150, "align": "center" }
                    ],
                    "dataSource": [
                      {
                        "id": "1",
                        "name": "智能手机",
                        "category": "电子产品",
                        "price": "5999",
                        "stock": "100",
                        "status": "在售"
                      },
                      {
                        "id": "2",
                        "name": "笔记本电脑",
                        "category": "电子产品",
                        "price": "8999",
                        "stock": "50",
                        "status": "在售"
                      },
                      {
                        "id": "3",
                        "name": "T恤",
                        "category": "服装",
                        "price": "99",
                        "stock": "200",
                        "status": "在售"
                      },
                      {
                        "id": "4",
                        "name": "运动鞋",
                        "category": "运动",
                        "price": "399",
                        "stock": "150",
                        "status": "在售"
                      },
                      {
                        "id": "5",
                        "name": "沙发",
                        "category": "家居",
                        "price": "2999",
                        "stock": "30",
                        "status": "在售"
                      }
                    ],
                    "rowKey": "id",
                    "pagination": {
                      "total": 100,
                      "pageSize": 10,
                      "current": 1,
                      "showSizeChanger": true,
                      "pageSizeOptions": ["10", "20", "50", "100"]
                    }
                  }
                }
              ]
            }
          ]
        }
      ]
    }
  ]
}
```

#### JavaScript 方式
```javascript
const productManagementPage = ood.create("Layout", {
  id: "productLayout",
  hasSider: true,
  siderWidth: 200,
  siderCollapsible: true
});

// 创建侧边菜单
const sideMenu = ood.create("Menu", {
  id: "sideMenu",
  mode: "inline",
  theme: "dark",
  selectedKeys: ["products"],
  items: [
    {
      key: "dashboard",
      label: "仪表盘",
      icon: "dashboard"
    },
    {
      key: "products",
      label: "产品管理",
      icon: "product"
    },
    {
      key: "orders",
      label: "订单管理",
      icon: "order"
    },
    {
      key: "inventory",
      label: "库存管理",
      icon: "inventory"
    },
    {
      key: "suppliers",
      label: "供应商管理",
      icon: "supplier"
    }
  ]
});

// 创建内容布局
const contentLayout = ood.create("Layout", {
  id: "contentLayout",
  hasHeader: true,
  headerHeight: 60
});

// 创建页面头部
const pageHeader = ood.create("Block", {
  id: "pageHeader",
  style: {
    backgroundColor: "#fff",
    borderBottom: "1px solid #f0f0f0",
    padding: "0 20px"
  }
});

pageHeader.addChild(ood.create("Label", {
  text: "产品管理",
  style: {
    fontSize: 20,
    fontWeight: "bold",
    lineHeight: "60px"
  }
}));

// 创建页面内容
const pageContent = ood.create("Block", {
  id: "pageContent",
  style: {
    padding: "20px",
    backgroundColor: "#f5f5f5"
  }
});

// 创建产品面板
const productPanel = ood.create("Panel", {
  id: "productPanel",
  title: "产品列表",
  padding: 20
});

// 创建搜索区域
const searchArea = ood.create("Stacks", {
  id: "searchArea",
  direction: "horizontal",
  justifyContent: "space-between",
  alignItems: "center",
  marginBottom: 20
});

// 搜索条件
const searchConditions = ood.create("Stacks", {
  direction: "horizontal",
  spacing: 10,
  alignItems: "center"
});

searchConditions.addChild(ood.create("Label", {
  text: "产品名称:"
}));

searchConditions.addChild(ood.create("TextInput", {
  id: "productNameSearch",
  placeholder: "请输入产品名称",
  width: 200
}));

searchConditions.addChild(ood.create("Label", {
  text: "产品分类:"
}));

searchConditions.addChild(ood.create("Select", {
  id: "productCategoryFilter",
  placeholder: "请选择产品分类",
  width: 150,
  options: [
    { value: "electronics", label: "电子产品" },
    { value: "clothing", label: "服装" },
    { value: "home", label: "家居" },
    { value: "sports", label: "运动" }
  ]
}));

searchConditions.addChild(ood.create("Button", {
  id: "searchButton",
  text: "搜索",
  type: "primary"
}));

// 新增按钮
const addButton = ood.create("Button", {
  id: "addProductButton",
  text: "新增产品",
  type: "primary",
  icon: "plus"
});

searchArea.addChild(searchConditions);
searchArea.addChild(addButton);

// 创建产品表格
const productTable = ood.create("Table", {
  id: "productTable",
  columns: [
    { title: "产品ID", dataIndex: "id", width: 80 },
    { title: "产品名称", dataIndex: "name", width: 200 },
    { title: "产品分类", dataIndex: "category", width: 120 },
    { title: "价格", dataIndex: "price", width: 100, align: "right" },
    { title: "库存", dataIndex: "stock", width: 100, align: "right" },
    { title: "状态", dataIndex: "status", width: 100 },
    { title: "操作", dataIndex: "actions", width: 150, align: "center" }
  ],
  dataSource: [
    {
      id: "1",
      name: "智能手机",
      category: "电子产品",
      price: "5999",
      stock: "100",
      status: "在售"
    },
    {
      id: "2",
      name: "笔记本电脑",
      category: "电子产品",
      price: "8999",
      stock: "50",
      status: "在售"
    },
    {
      id: "3",
      name: "T恤",
      category: "服装",
      price: "99",
      stock: "200",
      status: "在售"
    },
    {
      id: "4",
      name: "运动鞋",
      category: "运动",
      price: "399",
      stock: "150",
      status: "在售"
    },
    {
      id: "5",
      name: "沙发",
      category: "家居",
      price: "2999",
      stock: "30",
      status: "在售"
    }
  ],
  rowKey: "id",
  pagination: {
    total: 100,
    pageSize: 10,
    current: 1,
    showSizeChanger: true,
    pageSizeOptions: ["10", "20", "50", "100"]
  }
});

// 组装面板
productPanel.addChild(searchArea);
productPanel.addChild(productTable);

// 组装页面内容
pageContent.addChild(productPanel);

// 组装内容布局
contentLayout.addChild(pageHeader);
contentLayout.addChild(pageContent);

// 组装页面
productManagementPage.addChild(sideMenu);
productManagementPage.addChild(contentLayout);

// 添加到页面
page.addChild(productManagementPage);
```

### 2.4 最佳实践

- 使用 `Layout` 组件构建页面整体结构，包括侧边导航和主内容区域
- 使用 `Menu` 组件实现侧边导航，支持折叠功能
- 使用 `Stacks` 组件灵活布局搜索区域
- 使用 `Table` 组件展示产品列表，支持分页
- 提供清晰的搜索和筛选功能
- 提供新增、编辑和删除等操作按钮

## 3. CRM 系统 - 客户信息管理

### 3.1 场景描述
客户信息管理是 CRM 系统中的核心功能之一，用于管理客户信息，包括客户基本信息、联系记录、销售机会等。

### 3.2 组件使用

| 组件名称 | 用途 |
|----------|------|
| Tab | 标签页切换 |
| Card | 客户基本信息卡片 |
| FormLayout | 表单布局 |
| Label | 字段标签 |
| TextInput | 文本输入 |
| Select | 下拉选择 |
| TextArea | 多行文本输入 |
| Table | 联系记录列表 |
| Button | 操作按钮 |

### 3.3 实现示例

#### JSON 方式
```json
{
  "id": "customerManagementPage",
  "type": "Panel",
  "props": {
    "title": "客户信息管理",
    "padding": 20
  },
  "children": [
    {
      "type": "Tab",
      "props": {
        "id": "customerTabs",
        "activeKey": "basicInfo",
        "type": "card"
      },
      "children": [
        {
          "type": "TabPane",
          "props": {
            "key": "basicInfo",
            "tab": "基本信息"
          },
          "children": [
            {
              "type": "Card",
              "props": {
                "id": "basicInfoCard",
                "title": "客户基本信息",
                "padding": 20,
                "marginBottom": 20
              },
              "children": [
                {
                  "type": "FormLayout",
                  "props": {
                    "columns": 2,
                    "spacing": 20,
                    "padding": 10
                  },
                  "children": [
                    {
                      "type": "Label",
                      "props": {
                        "text": "客户名称",
                        "required": true,
                        "width": 100
                      }
                    },
                    {
                      "type": "TextInput",
                      "props": {
                        "id": "customerName",
                        "width": 300,
                        "value": "ABC有限公司"
                      }
                    },
                    {
                      "type": "Label",
                      "props": {
                        "text": "客户类型",
                        "width": 100
                      }
                    },
                    {
                      "type": "Select",
                      "props": {
                        "id": "customerType",
                        "width": 300,
                        "value": "enterprise",
                        "options": [
                          { "value": "enterprise", "label": "企业客户" },
                          { "value": "individual", "label": "个人客户" }
                        ]
                      }
                    },
                    {
                      "type": "Label",
                      "props": {
                        "text": "联系人",
                        "required": true,
                        "width": 100
                      }
                    },
                    {
                      "type": "TextInput",
                      "props": {
                        "id": "contactPerson",
                        "width": 300,
                        "value": "张三"
                      }
                    },
                    {
                      "type": "Label",
                      "props": {
                        "text": "联系电话",
                        "required": true,
                        "width": 100
                      }
                    },
                    {
                      "type": "TextInput",
                      "props": {
                        "id": "contactPhone",
                        "width": 300,
                        "value": "13800138000"
                      }
                    },
                    {
                      "type": "Label",
                      "props": {
                        "text": "电子邮箱",
                        "width": 100
                      }
                    },
                    {
                      "type": "TextInput",
                      "props": {
                        "id": "email",
                        "width": 300,
                        "value": "zhangsan@abc.com"
                      }
                    },
                    {
                      "type": "Label",
                      "props": {
                        "text": "地址",
                        "width": 100
                      }
                    },
                    {
                      "type": "TextArea",
                      "props": {
                        "id": "address",
                        "width": 300,
                        "height": 80,
                        "value": "北京市朝阳区建国路88号"
                      }
                    }
                  ]
                }
              ]
            }
          ]
        },
        {
          "type": "TabPane",
          "props": {
            "key": "contactRecords",
            "tab": "联系记录"
          },
          "children": [
            {
              "type": "Stacks",
              "props": {
                "id": "contactRecordsHeader",
                "direction": "horizontal",
                "justifyContent": "flex-end",
                "marginBottom": 20
              },
              "children": [
                {
                  "type": "Button",
                  "props": {
                    "id": "addContactRecord",
                    "text": "新增联系记录",
                    "type": "primary"
                  }
                }
              ]
            },
            {
              "type": "Table",
              "props": {
                "id": "contactRecordsTable",
                "columns": [
                  { title: "日期", dataIndex: "date", width: 120 },
                  { title: "联系人", dataIndex: "contact", width: 100 },
                  { title: "联系方式", dataIndex: "method", width: 100 },
                  { title: "内容摘要", dataIndex: "summary", width: 300 },
                  { title: "跟进人", dataIndex: "follower", width: 100 },
                  { title: "操作", dataIndex: "actions", width: 100, "align": "center" }
                ],
                "dataSource": [
                  {
                    "id": "1",
                    "date": "2025-01-15",
                    "contact": "张三",
                    "method": "电话",
                    "summary": "讨论产品需求",
                    "follower": "李四"
                  },
                  {
                    "id": "2",
                    "date": "2025-01-10",
                    "contact": "张三",
                    "method": "邮件",
                    "summary": "发送产品报价",
                    "follower": "李四"
                  },
                  {
                    "id": "3",
                    "date": "2025-01-05",
                    "contact": "张三",
                    "method": "会议",
                    "summary": "初次拜访",
                    "follower": "李四"
                  }
                ],
                "rowKey": "id"
              }
            }
          ]
        },
        {
          "type": "TabPane",
          "props": {
            "key": "salesOpportunities",
            "tab": "销售机会"
          },
          "children": [
            {
              "type": "Stacks",
              "props": {
                "id": "salesOpportunitiesHeader",
                "direction": "horizontal",
                "justifyContent": "flex-end",
                "marginBottom": 20
              },
              "children": [
                {
                  "type": "Button",
                  "props": {
                    "id": "addSalesOpportunity",
                    "text": "新增销售机会",
                    "type": "primary"
                  }
                }
              ]
            },
            {
              "type": "Table",
              "props": {
                "id": "salesOpportunitiesTable",
                "columns": [
                  { title: "机会名称", dataIndex: "name", width: 200 },
                  { title: "产品", dataIndex: "product", width: 150 },
                  { title: "预计金额", dataIndex: "amount", width: 120, "align": "right" },
                  { title: "阶段", dataIndex: "stage", width: 100 },
                  { title: "负责人", dataIndex: "owner", width: 100 },
                  { title: "预计成交日期", dataIndex: "expectedDate", width: 120 },
                  { title: "操作", dataIndex: "actions", width: 100, "align": "center" }
                ],
                "dataSource": [
                  {
                    "id": "1",
                    "name": "ABC有限公司产品采购",
                    "product": "企业管理系统",
                    "amount": "500000",
                    "stage": "谈判中",
                    "owner": "李四",
                    "expectedDate": "2025-03-31"
                  },
                  {
                    "id": "2",
                    "name": "ABC有限公司服务升级",
                    "product": "技术支持服务",
                    "amount": "100000",
                    "stage": "已报价",
                    "owner": "李四",
                    "expectedDate": "2025-02-28"
                  }
                ],
                "rowKey": "id"
              }
            }
          ]
        }
      ]
    }
  ]
}
```

#### JavaScript 方式
```javascript
const customerManagementPage = ood.create("Panel", {
  id: "customerManagementPage",
  title: "客户信息管理",
  padding: 20
});

// 创建标签页
const customerTabs = ood.create("Tab", {
  id: "customerTabs",
  activeKey: "basicInfo",
  type: "card"
});

// 基本信息标签页
const basicInfoTab = ood.create("TabPane", {
  key: "basicInfo",
  tab: "基本信息"
});

// 基本信息卡片
const basicInfoCard = ood.create("Card", {
  id: "basicInfoCard",
  title: "客户基本信息",
  padding: 20,
  marginBottom: 20
});

// 基本信息表单
const basicInfoForm = ood.create("FormLayout", {
  columns: 2,
  spacing: 20,
  padding: 10
});

// 客户名称
basicInfoForm.addChild(ood.create("Label", {
  text: "客户名称",
  required: true,
  width: 100
}));

basicInfoForm.addChild(ood.create("TextInput", {
  id: "customerName",
  width: 300,
  value: "ABC有限公司"
}));

// 客户类型
basicInfoForm.addChild(ood.create("Label", {
  text: "客户类型",
  width: 100
}));

basicInfoForm.addChild(ood.create("Select", {
  id: "customerType",
  width: 300,
  value: "enterprise",
  options: [
    { value: "enterprise", label: "企业客户" },
    { value: "individual", label: "个人客户" }
  ]
}));

// 联系人
basicInfoForm.addChild(ood.create("Label", {
  text: "联系人",
  required: true,
  width: 100
}));

basicInfoForm.addChild(ood.create("TextInput", {
  id: "contactPerson",
  width: 300,
  value: "张三"
}));

// 联系电话
basicInfoForm.addChild(ood.create("Label", {
  text: "联系电话",
  required: true,
  width: 100
}));

basicInfoForm.addChild(ood.create("TextInput", {
  id: "contactPhone",
  width: 300,
  value: "13800138000"
}));

// 电子邮箱
basicInfoForm.addChild(ood.create("Label", {
  text: "电子邮箱",
  width: 100
}));

basicInfoForm.addChild(ood.create("TextInput", {
  id: "email",
  width: 300,
  value: "zhangsan@abc.com"
}));

// 地址
basicInfoForm.addChild(ood.create("Label", {
  text: "地址",
  width: 100
}));

basicInfoForm.addChild(ood.create("TextArea", {
  id: "address",
  width: 300,
  height: 80,
  value: "北京市朝阳区建国路88号"
}));

// 组装基本信息卡片
basicInfoCard.addChild(basicInfoForm);
basicInfoTab.addChild(basicInfoCard);

// 联系记录标签页
const contactRecordsTab = ood.create("TabPane", {
  key: "contactRecords",
  tab: "联系记录"
});

// 联系记录头部
const contactRecordsHeader = ood.create("Stacks", {
  id: "contactRecordsHeader",
  direction: "horizontal",
  justifyContent: "flex-end",
  marginBottom: 20
});

contactRecordsHeader.addChild(ood.create("Button", {
  id: "addContactRecord",
  text: "新增联系记录",
  type: "primary"
}));

// 联系记录表格
const contactRecordsTable = ood.create("Table", {
  id: "contactRecordsTable",
  columns: [
    { title: "日期", dataIndex: "date", width: 120 },
    { title: "联系人", dataIndex: "contact", width: 100 },
    { title: "联系方式", dataIndex: "method", width: 100 },
    { title: "内容摘要", dataIndex: "summary", width: 300 },
    { title: "跟进人", dataIndex: "follower", width: 100 },
    { title: "操作", dataIndex: "actions", width: 100, "align": "center" }
  ],
  dataSource: [
    {
      id: "1",
      date: "2025-01-15",
      contact: "张三",
      method: "电话",
      summary: "讨论产品需求",
      follower: "李四"
    },
    {
      id: "2",
      date: "2025-01-10",
      contact: "张三",
      method: "邮件",
      summary: "发送产品报价",
      follower: "李四"
    },
    {
      id: "3",
      date: "2025-01-05",
      contact: "张三",
      method: "会议",
      summary: "初次拜访",
      follower: "李四"
    }
  ],
  rowKey: "id"
});

// 组装联系记录标签页
contactRecordsTab.addChild(contactRecordsHeader);
contactRecordsTab.addChild(contactRecordsTable);

// 销售机会标签页
const salesOpportunitiesTab = ood.create("TabPane", {
  key: "salesOpportunities",
  tab: "销售机会"
});

// 销售机会头部
const salesOpportunitiesHeader = ood.create("Stacks", {
  id: "salesOpportunitiesHeader",
  direction: "horizontal",
  justifyContent: "flex-end",
  marginBottom: 20
});

salesOpportunitiesHeader.addChild(ood.create("Button", {
  id: "addSalesOpportunity",
  text: "新增销售机会",
  type: "primary"
}));

// 销售机会表格
const salesOpportunitiesTable = ood.create("Table", {
  id: "salesOpportunitiesTable",
  columns: [
    { title: "机会名称", dataIndex: "name", width: 200 },
    { title: "产品", dataIndex: "product", width: 150 },
    { title: "预计金额", dataIndex: "amount", width: 120, "align": "right" },
    { title: "阶段", dataIndex: "stage", width: 100 },
    { title: "负责人", dataIndex: "owner", width: 100 },
    { title: "预计成交日期", dataIndex: "expectedDate", width: 120 },
    { title: "操作", dataIndex: "actions", width: 100, "align": "center" }
  ],
  dataSource: [
    {
      id: "1",
      name: "ABC有限公司产品采购",
      product: "企业管理系统",
      amount: "500000",
      stage: "谈判中",
      owner: "李四",
      expectedDate: "2025-03-31"
    },
    {
      id: "2",
      name: "ABC有限公司服务升级",
      product: "技术支持服务",
      amount: "100000",
      stage: "已报价",
      owner: "李四",
      expectedDate: "2025-02-28"
    }
  ],
  rowKey: "id"
});

// 组装销售机会标签页
salesOpportunitiesTab.addChild(salesOpportunitiesHeader);
salesOpportunitiesTab.addChild(salesOpportunitiesTable);

// 组装标签页
customerTabs.addChild(basicInfoTab);
customerTabs.addChild(contactRecordsTab);
customerTabs.addChild(salesOpportunitiesTab);

// 组装页面
customerManagementPage.addChild(customerTabs);

// 添加到页面
page.addChild(customerManagementPage);
```

### 3.4 最佳实践

- 使用 `Tab` 组件实现不同信息模块的切换
- 使用 `Card` 组件组织客户基本信息
- 使用 `FormLayout` 组件布局表单字段
- 使用 `Table` 组件展示联系记录和销售机会
- 提供清晰的操作按钮

## 4. 通用场景 - 数据统计仪表板

### 4.1 场景描述
数据统计仪表板是各种系统中常见的功能，用于展示关键业务指标和数据趋势，帮助用户快速了解业务状况。

### 4.2 组件使用

| 组件名称 | 用途 |
|----------|------|
| Layout | 页面布局 |
| Panel | 面板容器 |
| Stacks | 卡片布局 |
| Card | 数据卡片 |
| Label | 文本标签 |
| Table | 数据列表 |
| Button | 操作按钮 |

### 4.3 实现示例

#### JSON 方式
```json
{
  "id": "dashboardPage",
  "type": "Layout",
  "props": {
    "id": "dashboardLayout",
    "hasHeader": true,
    "headerHeight": 60
  },
  "children": [
    {
      "type": "Block",
      "props": {
        "id": "dashboardHeader",
        "style": {
          "backgroundColor": "#fff",
          "borderBottom": "1px solid #f0f0f0",
          "padding": "0 20px"
        }
      },
      "children": [
        {
          "type": "Label",
          "props": {
            "text": "数据仪表板",
            "style": {
              "fontSize": 20,
              "fontWeight": "bold",
              "lineHeight": "60px"
            }
          }
        }
      ]
    },
    {
      "type": "Block",
      "props": {
        "id": "dashboardContent",
        "style": {
          "padding": "20px",
          "backgroundColor": "#f5f5f5"
        }
      },
      "children": [
        {
          "type": "Stacks",
          "props": {
            "id": "metricsCards",
            "direction": "horizontal",
            "spacing": 20,
            "marginBottom": 20
          },
          "children": [
            {
              "type": "Card",
              "props": {
                "id": "metricCard1",
                "padding": 20,
                "style": {
                  "width": 250,
                  "backgroundColor": "#e6f7ff",
                  "borderLeft": "4px solid #1890ff"
                }
              },
              "children": [
                {
                  "type": "Label",
                  "props": {
                    "text": "总销售额",
                    "style": {
                      "fontSize": 14,
                      "color": "#666"
                    }
                  }
                },
                {
                  "type": "Label",
                  "props": {
                    "text": "¥ 1,234,567",
                    "style": {
                      "fontSize": 24,
                      "fontWeight": "bold",
                      "marginTop": 10,
                      "color": "#000"
                    }
                  }
                },
                {
                  "type": "Stacks",
                  "props": {
                    "id": "growth1",
                    "direction": "horizontal",
                    "alignItems": "center",
                    "marginTop": 10
                  },
                  "children": [
                    {
                      "type": "Label",
                      "props": {
                        "text": "+12.5%",
                        "style": {
                          "fontSize": 12,
                          "color": "#52c41a"
                        }
                      }
                    },
                    {
                      "type": "Label",
                      "props": {
                        "text": "较上月",
                        "style": {
                          "fontSize": 12,
                          "color": "#999",
                          "marginLeft": 5
                        }
                      }
                    }
                  ]
                }
              ]
            },
            {
              "type": "Card",
              "props": {
                "id": "metricCard2",
                "padding": 20,
                "style": {
                  "width": 250,
                  "backgroundColor": "#f6ffed",
                  "borderLeft": "4px solid #52c41a"
                }
              },
              "children": [
                {
                  "type": "Label",
                  "props": {
                    "text": "订单数量",
                    "style": {
                      "fontSize": 14,
                      "color": "#666"
                    }
                  }
                },
                {
                  "type": "Label",
                  "props": {
                    "text": "8,923",
                    "style": {
                      "fontSize": 24,
                      "fontWeight": "bold",
                      "marginTop": 10,
                      "color": "#000"
                    }
                  }
                },
                {
                  "type": "Stacks",
                  "props": {
                    "id": "growth2",
                    "direction": "horizontal",
                    "alignItems": "center",
                    "marginTop": 10
                  },
                  "children": [
                    {
                      "type": "Label",
                      "props": {
                        "text": "+8.3%",
                        "style": {
                          "fontSize": 12,
                          "color": "#52c41a"
                        }
                      }
                    },
                    {
                      "type": "Label",
                      "props": {
                        "text": "较上月",
                        "style": {
                          "fontSize": 12,
                          "color": "#999",
                          "marginLeft": 5
                        }
                      }
                    }
                  ]
                }
              ]
            },
            {
              "type": "Card",
              "props": {
                "id": "metricCard3",
                "padding": 20,
                "style": {
                  "width": 250,
                  "backgroundColor": "#fff7e6",
                  "borderLeft": "4px solid #faad14"
                }
              },
              "children": [
                {
                  "type": "Label",
                  "props": {
                    "text": "活跃用户",
                    "style": {
                      "fontSize": 14,
                      "color": "#666"
                    }
                  }
                },
                {
                  "type": "Label",
                  "props": {
                    "text": "12,567",
                    "style": {
                      "fontSize": 24,
                      "fontWeight": "bold",
                      "marginTop": 10,
                      "color": "#000"
                    }
                  }
                },
                {
                  "type": "Stacks",
                  "props": {
                    "id": "growth3",
                    "direction": "horizontal",
                    "alignItems": "center",
                    "marginTop": 10
                  },
                  "children": [
                    {
                      "type": "Label",
                      "props": {
                        "text": "+5.2%",
                        "style": {
                          "fontSize": 12,
                          "color": "#52c41a"
                        }
                      }
                    },
                    {
                      "type": "Label",
                      "props": {
                        "text": "较上月",
                        "style": {
                          "fontSize": 12,
                          "color": "#999",
                          "marginLeft": 5
                        }
                      }
                    }
                  ]
                }
              ]
            },
            {
              "type": "Card",
              "props": {
                "id": "metricCard4",
                "padding": 20,
                "style": {
                  "width": 250,
                  "backgroundColor": "#fff1f0",
                  "borderLeft": "4px solid #f5222d"
                }
              },
              "children": [
                {
                  "type": "Label",
                  "props": {
                    "text": "退货率",
                    "style": {
                      "fontSize": 14,
                      "color": "#666"
                    }
                  }
                },
                {
                  "type": "Label",
                  "props": {
                    "text": "2.3%",
                    "style": {
                      "fontSize": 24,
                      "fontWeight": "bold",
                      "marginTop": 10,
                      "color": "#000"
                    }
                  }
                },
                {
                  "type": "Stacks",
                  "props": {
                    "id": "growth4",
                    "direction": "horizontal",
                    "alignItems": "center",
                    "marginTop": 10
                  },
                  "children": [
                    {
                      "type": "Label",
                      "props": {
                        "text": "-0.5%",
                        "style": {
                          "fontSize": 12,
                          "color": "#52c41a"
                        }
                      }
                    },
                    {
                      "type": "Label",
                      "props": {
                        "text": "较上月",
                        "style": {
                          "fontSize": 12,
                          "color": "#999",
                          "marginLeft": 5
                        }
                      }
                    }
                  ]
                }
              ]
            }
          ]
        },
        {
          "type": "Stacks",
          "props": {
            "id": "bottomContent",
            "direction": "horizontal",
            "spacing": 20
          },
          "children": [
            {
              "type": "Panel",
              "props": {
                "id": "recentOrdersPanel",
                "title": "最近订单",
                "padding": 20,
                "style": {
                  "flex": 1
                }
              },
              "children": [
                {
                  "type": "Table",
                  "props": {
                    "id": "recentOrdersTable",
                    "columns": [
                      { title: "订单号", dataIndex: "orderNo", width: 150 },
                      { title: "客户名称", dataIndex: "customerName", width: 200 },
                      { title: "金额", dataIndex: "amount", width: 120, "align": "right" },
                      { title: "状态", dataIndex: "status", width: 100 },
                      { title: "下单时间", dataIndex: "orderTime", width: 150 }
                    ],
                    "dataSource": [
                      {
                        "id": "1",
                        "orderNo": "ORD20250115001",
                        "customerName": "ABC有限公司",
                        "amount": "12,345",
                        "status": "已付款",
                        "orderTime": "2025-01-15 14:30:00"
                      },
                      {
                        "id": "2",
                        "orderNo": "ORD20250115002",
                        "customerName": "XYZ科技",
                        "amount": "23,456",
                        "status": "已发货",
                        "orderTime": "2025-01-15 13:20:00"
                      },
                      {
                        "id": "3",
                        "orderNo": "ORD20250115003",
                        "customerName": "DEF贸易",
                        "amount": "34,567",
                        "status": "待付款",
                        "orderTime": "2025-01-15 12:10:00"
                      }
                    ],
                    "rowKey": "id"
                  }
                }
              ]
            },
            {
              "type": "Panel",
              "props": {
                "id": "topProductsPanel",
                "title": "热销产品",
                "padding": 20,
                "style": {
                  "flex": 1
                }
              },
              "children": [
                {
                  "type": "Table",
                  "props": {
                    "id": "topProductsTable",
                    "columns": [
                      { title: "排名", dataIndex: "rank", width: 80, "align": "center" },
                      { title: "产品名称", dataIndex: "productName", width: 200 },
                      { title: "销售额", dataIndex: "sales", width: 120, "align": "right" },
                      { title: "销量", dataIndex: "quantity", width: 100, "align": "right" },
                      { title: "操作", dataIndex: "actions", width: 100, "align": "center" }
                    ],
                    "dataSource": [
                      {
                        "id": "1",
                        "rank": "1",
                        "productName": "智能手机",
                        "sales": "599,900",
                        "quantity": "100"
                      },
                      {
                        "id": "2",
                        "rank": "2",
                        "productName": "笔记本电脑",
                        "sales": "449,950",
                        "quantity": "50"
                      },
                      {
                        "id": "3",
                        "rank": "3",
                        "productName": "平板电脑",
                        "sales": "299,700",
                        "quantity": "60"
                      }
                    ],
                    "rowKey": "id"
                  }
                }
              ]
            }
          ]
        }
      ]
    }
  ]
}
```

#### JavaScript 方式
```javascript
const dashboardPage = ood.create("Layout", {
  id: "dashboardLayout",
  hasHeader: true,
  headerHeight: 60
});

// 页面头部
const dashboardHeader = ood.create("Block", {
  id: "dashboardHeader",
  style: {
    backgroundColor: "#fff",
    borderBottom: "1px solid #f0f0f0",
    padding: "0 20px"
  }
});

dashboardHeader.addChild(ood.create("Label", {
  text: "数据仪表板",
  style: {
    fontSize: 20,
    fontWeight: "bold",
    lineHeight: "60px"
  }
}));

// 页面内容
const dashboardContent = ood.create("Block", {
  id: "dashboardContent",
  style: {
    padding: "20px",
    backgroundColor: "#f5f5f5"
  }
});

// 指标卡片布局
const metricsCards = ood.create("Stacks", {
  id: "metricsCards",
  direction: "horizontal",
  spacing: 20,
  marginBottom: 20
});

// 创建指标卡片的函数
function createMetricCard(title, value, growth, color, borderColor) {
  const card = ood.create("Card", {
    padding: 20,
    style: {
      width: 250,
      backgroundColor: color,
      borderLeft: `4px solid ${borderColor}`
    }
  });

  card.addChild(ood.create("Label", {
    text: title,
    style: {
      fontSize: 14,
      color: "#666"
    }
  }));

  card.addChild(ood.create("Label", {
    text: value,
    style: {
      fontSize: 24,
      fontWeight: "bold",
      marginTop: 10,
      color: "#000"
    }
  }));

  const growthStack = ood.create("Stacks", {
    direction: "horizontal",
    alignItems: "center",
    marginTop: 10
  });

  growthStack.addChild(ood.create("Label", {
    text: growth,
    style: {
      fontSize: 12,
      color: "#52c41a"
    }
  }));

  growthStack.addChild(ood.create("Label", {
    text: "较上月",
    style: {
      fontSize: 12,
      color: "#999",
      marginLeft: 5
    }
  }));

  card.addChild(growthStack);

  return card;
}

// 添加指标卡片
metricsCards.addChild(createMetricCard(
  "总销售额", "¥ 1,234,567", "+12.5%", 
  "#e6f7ff", "#1890ff"
));

metricsCards.addChild(createMetricCard(
  "订单数量", "8,923", "+8.3%", 
  "#f6ffed", "#52c41a"
));

metricsCards.addChild(createMetricCard(
  "活跃用户", "12,567", "+5.2%", 
  "#fff7e6", "#faad14"
));

metricsCards.addChild(createMetricCard(
  "退货率", "2.3%", "-0.5%", 
  "#fff1f0", "#f5222d"
));

// 底部内容布局
const bottomContent = ood.create("Stacks", {
  id: "bottomContent",
  direction: "horizontal",
  spacing: 20
});

// 最近订单面板
const recentOrdersPanel = ood.create("Panel", {
  id: "recentOrdersPanel",
  title: "最近订单",
  padding: 20,
  style: {
    flex: 1
  }
});

// 最近订单表格
const recentOrdersTable = ood.create("Table", {
  id: "recentOrdersTable",
  columns: [
    { title: "订单号", dataIndex: "orderNo", width: 150 },
    { title: "客户名称", dataIndex: "customerName", width: 200 },
    { title: "金额", dataIndex: "amount", width: 120, align: "right" },
    { title: "状态", dataIndex: "status", width: 100 },
    { title: "下单时间", dataIndex: "orderTime", width: 150 }
  ],
  dataSource: [
    {
      id: "1",
      orderNo: "ORD20250115001",
      customerName: "ABC有限公司",
      amount: "12,345",
      status: "已付款",
      orderTime: "2025-01-15 14:30:00"
    },
    {
      id: "2",
      orderNo: "ORD20250115002",
      customerName: "XYZ科技",
      amount: "23,456",
      status: "已发货",
      orderTime: "2025-01-15 13:20:00"
    },
    {
      id: "3",
      orderNo: "ORD20250115003",
      customerName: "DEF贸易",
      amount: "34,567",
      status: "待付款",
      orderTime: "2025-01-15 12:10:00"
    }
  ],
  rowKey: "id"
});

// 组装最近订单面板
recentOrdersPanel.addChild(recentOrdersTable);

// 热销产品面板
const topProductsPanel = ood.create("Panel", {
  id: "topProductsPanel",
  title: "热销产品",
  padding: 20,
  style: {
    flex: 1
  }
});

// 热销产品表格
const topProductsTable = ood.create("Table", {
  id: "topProductsTable",
  columns: [
    { title: "排名", dataIndex: "rank", width: 80, align: "center" },
    { title: "产品名称", dataIndex: "productName", width: 200 },
    { title: "销售额", dataIndex: "sales", width: 120, align: "right" },
    { title: "销量", dataIndex: "quantity", width: 100, align: "right" },
    { title: "操作", dataIndex: "actions", width: 100, align: "center" }
  ],
  dataSource: [
    {
      id: "1",
      rank: "1",
      productName: "智能手机",
      sales: "599,900",
      quantity: "100"
    },
    {
      id: "2",
      rank: "2",
      productName: "笔记本电脑",
      sales: "449,950",
      quantity: "50"
    },
    {
      id: "3",
      rank: "3",
      productName: "平板电脑",
      sales: "299,700",
      quantity: "60"
    }
  ],
  rowKey: "id"
});

// 组装热销产品面板
topProductsPanel.addChild(topProductsTable);

// 组装底部内容
bottomContent.addChild(recentOrdersPanel);
bottomContent.addChild(topProductsPanel);

// 组装页面内容
dashboardContent.addChild(metricsCards);
dashboardContent.addChild(bottomContent);

// 组装页面
dashboardPage.addChild(dashboardHeader);
dashboardPage.addChild(dashboardContent);

// 添加到页面
page.addChild(dashboardPage);
```

### 4.4 最佳实践

- 使用 `Layout` 组件构建页面整体结构
- 使用 `Card` 组件展示关键指标，提供直观的数据展示
- 使用 `Stacks` 组件灵活布局各个模块
- 使用 `Table` 组件展示详细数据列表
- 合理使用颜色和样式，突出重要信息
- 保持页面简洁，避免信息过载
- 提供清晰的数据趋势和对比

## 5. 组件组合使用建议

### 5.1 表单组合

- 使用 `FormLayout` + `Label` + `Input` 组件构建完整表单
- 对于复杂表单，考虑使用 `Panel` 或 `Tabs` 组件进行分组
- 使用 `Button` 组件作为表单提交和重置按钮

### 5.2 数据展示组合

- 使用 `Card` + `Label` 组件展示关键指标
- 使用 `Table` 组件展示详细数据
- 使用 `Pagination` 组件进行分页控制
- 使用 `Button` 组件提供数据操作功能

### 5.3 页面布局组合

- 使用 `Layout` 组件构建页面整体结构
- 使用 `Menu` 组件实现导航
- 使用 `Panel` 组件作为内容容器
- 使用 `Stacks` 组件灵活布局各个模块

### 5.4 交互组件组合

- 使用 `Button` + `Dialog` 组件实现模态交互
- 使用 `Select` + `TextInput` 组件实现复杂输入
- 使用 `Tab` 组件实现内容切换
- 使用 `Tooltip` 组件提供额外信息

## 6. 开发建议

### 6.1 组件选择原则

- 根据功能需求选择合适的组件
- 优先使用框架提供的组件，避免重复造轮子
- 考虑组件的性能和可维护性
- 考虑组件的可访问性和兼容性

### 6.2 代码组织建议

- 按照功能模块组织代码
- 合理使用组件化设计，提高代码复用性
- 保持代码的简洁性和可读性
- 提供清晰的注释和文档

### 6.3 性能优化建议

- 避免不必要的组件渲染
- 合理使用懒加载和虚拟滚动
- 优化组件的 props 和状态传递
- 避免在组件中进行复杂计算

### 6.4 可访问性建议

- 支持键盘导航
- 提供适当的 ARIA 属性
- 支持屏幕阅读器
- 提供足够的颜色对比度

---

**最后更新时间**：2026-01-25  
**文档版本**：1.0  
**作者**：ooder-a2ui 开发团队