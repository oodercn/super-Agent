# TreeGrid组件专题

## 1. 组件概述

### 1.1 什么是TreeGrid

TreeGrid是Ooder框架中用于创建树形表格的核心组件，提供了强大的树形数据展示和管理能力。它结合了表格和树形结构的优点，能够清晰地展示具有层级关系的数据。

### 1.2 核心功能

- 支持树形数据展示和层级关系
- 提供灵活的列配置和定制能力
- 支持行选择、编辑和删除操作
- 支持分页、排序和过滤功能
- 支持响应式设计，适配不同屏幕尺寸
- 支持动态列生成，根据场景自动调整

## 2. 组件类型

### 2.1 基础TreeGrid

```javascript
ood.Class("ood.UI.TreeGrid", "ood.UI", {
    // 基础树形表格实现
});
```

### 2.2 移动端TreeGrid

```javascript
ood.Class("ood.UI.MTreeGrid", "ood.UI.TreeGrid", {
    // 移动端优化的树形表格
});
```

## 3. 使用场景

### 3.1 树形数据表格场景

**推荐组件**：`ood.UI.TreeGrid` 或 `ood.UI.MTreeGrid`

**适用情况**：
- 展示具有层级关系的表格数据
- 可展开/折叠的表格行
- 兼具树形结构和表格功能
- 复杂数据的综合展示
- 数据量大且具有层级关系的场景

**示例**：
```javascript
var treeGrid = ood.create("ood.UI.TreeGrid", {
    columns: [
        { name: "name", title: "名称", width: 200 },
        { name: "type", title: "类型", width: 100 },
        { name: "size", title: "大小", width: 100 },
        { name: "modified", title: "修改时间", width: 150 }
    ],
    data: treeGridData,
    onRowClick: function(row) {
        console.log("点击行:", row.name);
    }
});
```

### 3.2 产品分类场景

**推荐组件**：`ood.UI.TreeGrid`

**适用情况**：
- 展示产品分类和子分类
- 查看产品详细信息
- 管理产品数据

**示例**：
```javascript
var productTreeGrid = ood.create("ood.UI.TreeGrid", {
    columns: [
        { name: "categoryName", title: "分类名称", width: 200 },
        { name: "productName", title: "产品名称", width: 180 },
        { name: "price", title: "价格", width: 100, align: "right" },
        { name: "stock", title: "库存", width: 100, align: "right" },
        { name: "status", title: "状态", width: 100 }
    ],
    data: productData,
    expanded: true,
    showRowNumbers: true
});
```

### 3.3 学生课表场景

**推荐组件**：`ood.UI.TreeGrid`

**适用情况**：
- 展示学生课程信息
- 按学期/周分类课程
- 查看课程详细信息

**示例**：
```javascript
var scheduleTreeGrid = ood.create("ood.UI.TreeGrid", {
    columns: [
        { name: "studentId", title: "学生ID", hidden: true },
        { name: "name", title: "姓名", width: 120 },
        { name: "subject", title: "科目", width: 150 },
        { name: "teacher", title: "教师", width: 120 },
        { name: "time", title: "上课时间", width: 150 },
        { name: "location", title: "上课地点", width: 150 }
    ],
    data: scheduleData,
    rowHeight: "3em",
    editMode: "inline",
    selMode: "multibycheckbox"
});
```

## 4. 最佳实践

### 4.1 表格设计原则

1. **保持简洁**：只显示必要的列信息
2. **清晰的视觉层次**：使用树形结构清晰展示层级关系
3. **合理的列宽**：根据内容长度调整列宽
4. **适当的交互反馈**：提供行选择、编辑等操作的视觉反馈
5. **响应式设计**：适配不同屏幕尺寸

### 4.2 组件选择指南

| 使用场景 | 推荐组件 | 备选组件 |
|---------|---------|---------|
| 树形数据表格 | TreeGrid | MTreeGrid |
| 移动端树形表格 | MTreeGrid | TreeGrid |
| 简单数据表格 | Table | - |
| 复杂数据表格 | TreeGrid | Table + TreeView |

### 4.3 性能优化

1. **虚拟滚动**：处理大量数据时使用虚拟滚动
2. **懒加载**：对树形数据采用懒加载方式
3. **合理的分页**：根据数据量设置合适的分页大小
4. **优化列配置**：减少不必要的列渲染
5. **避免过度渲染**：合理使用组件的更新机制

## 5. 常见问题与解决方案

### 5.1 树形数据展示异常

**问题**：树形结构没有正确显示或无法展开/折叠

**解决方案**：
1. 检查数据格式是否符合要求，包含id、parentId等必要字段
2. 确保设置了正确的uidColumn和parentIdColumn
3. 检查数据是否正确加载

### 5.2 表格性能问题

**问题**：包含大量数据的表格加载和响应缓慢

**解决方案**：
1. 启用虚拟滚动
2. 采用懒加载方式加载数据
3. 优化列配置，减少不必要的列
4. 合理设置分页大小

### 5.3 动态列生成问题

**问题**：动态生成的列没有正确显示

**解决方案**：
1. 确保列配置格式正确
2. 检查setColumns方法是否正确调用
3. 确保列数据类型与组件类型匹配

## 6. 示例代码

### 6.1 基础树形表格

```javascript
var basicTreeGrid = ood.create("ood.UI.TreeGrid")
    .setHost(host, "basicTreeGrid")
    .setName("basicTreeGrid")
    .setTabindex(0)
    .setSelMode("multibycheckbox")
    .setEditMode("inline")
    .setRowNumbered(true)
    .setRowHeight("3em")
    .setColumns([
        {
            "caption": "ID",
            "hidden": true,
            "id": "id",
            "tips": "ID",
            "title": "ID",
            "type": "input",
            "width": "8em"
        },
        {
            "caption": "名称",
            "hidden": false,
            "id": "name",
            "tips": "名称",
            "title": "名称",
            "type": "input",
            "width": "18em"
        },
        {
            "caption": "类型",
            "hidden": false,
            "id": "type",
            "tips": "类型",
            "title": "类型",
            "type": "input",
            "width": "12em"
        },
        {
            "caption": "状态",
            "hidden": false,
            "id": "status",
            "tips": "状态",
            "title": "状态",
            "type": "input",
            "width": "10em"
        }
    ])
    .setUidColumn("id")
    .setTagCmds([
        {
            "buttonType": "text",
            "caption": "",
            "id": "editbutton",
            "imageClass": "fa-solid fa-edit",
            "index": 0,
            "pos": "row",
            "tagCmdsAlign": "right",
            "tips": "编辑"
        }
    ])
    .setValue("");
```

### 6.2 学生课表树形表格

```javascript
var scheduleTreeGrid = ood.create("ood.UI.TreeGrid")
    .setHost(host, "scheduleTreeGrid")
    .setName("scheduleTreeGrid")
    .setDesc("学生课表")
    .setTabindex(0)
    .setSelMode("multibycheckbox")
    .setEditMode("inline")
    .setRowNumbered(true)
    .setRowHeight("3em")
    .setColumns([
        {
            "caption": "学生ID",
            "hidden": true,
            "id": "studentId",
            "tips": "学生ID",
            "title": "学生ID",
            "type": "input",
            "width": "8em"
        },
        {
            "caption": "姓名",
            "hidden": false,
            "id": "name",
            "tips": "姓名",
            "title": "姓名",
            "type": "input",
            "width": "12em"
        },
        {
            "caption": "科目",
            "hidden": false,
            "id": "subject",
            "tips": "科目",
            "title": "科目",
            "type": "input",
            "width": "15em"
        },
        {
            "caption": "教师",
            "hidden": false,
            "id": "teacher",
            "tips": "教师",
            "title": "教师",
            "type": "input",
            "width": "12em"
        },
        {
            "caption": "上课时间",
            "hidden": false,
            "id": "courseTime",
            "tips": "上课时间",
            "title": "上课时间",
            "type": "input",
            "width": "15em"
        },
        {
            "caption": "上课地点",
            "hidden": false,
            "id": "location",
            "tips": "上课地点",
            "title": "上课地点",
            "type": "input",
            "width": "15em"
        }
    ])
    .setUidColumn("studentId")
    .setTagCmds([
        {
            "buttonType": "text",
            "caption": "",
            "id": "editbutton",
            "imageClass": "fa-solid fa-edit",
            "index": 0,
            "pos": "row",
            "tagCmdsAlign": "right",
            "tips": "编辑"
        }
    ])
    .setValue("");
```

## 7. 总结

TreeGrid组件是Ooder框架中用于创建树形表格的核心组件，提供了强大的树形数据展示和管理能力。它结合了表格和树形结构的优点，能够清晰地展示具有层级关系的数据。

在选择树形表格组件时，应根据具体需求和使用场景进行选择：
- **普通树形表格**：优先使用`ood.UI.TreeGrid`
- **移动端树形表格**：使用`ood.UI.MTreeGrid`
- **简单数据表格**：可使用`ood.UI.Table`

通过合理选择和使用树形表格组件，可以创建出高效、易用的数据展示界面，提升用户体验和开发效率。