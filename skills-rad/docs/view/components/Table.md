# Table 组件

## 1. 组件概述
Table 组件是 ooder-a2ui 前端框架中的一个强大的数据展示组件，用于以表格形式展示结构化数据，支持排序、筛选、分页、编辑等丰富功能。

### 1.1 核心功能
- 支持基本表格数据展示
- 支持列排序（单列和多列）
- 支持列筛选
- 支持分页功能
- 支持行选择（单选和多选）
- 支持行编辑
- 支持固定表头和表尾
- 支持固定列
- 支持自定义列模板
- 支持树形结构数据
- 支持虚拟滚动（大数据量）
- 支持响应式布局
- 支持主题定制

### 1.2 适用场景
- 数据管理系统的列表展示
- 报表数据的表格呈现
- 订单管理的明细列表
- 用户管理的用户列表
- 数据分析的结果展示
- 配置项的表格编辑

## 2. 创建方法

### 2.1 JSON 方式创建
```json
{
  "id": "table1",
  "type": "Table",
  "props": {
    "columns": [
      { "title": "ID", "dataIndex": "id", "key": "id", "width": 80 },
      { "title": "姓名", "dataIndex": "name", "key": "name", "width": 120 },
      { "title": "年龄", "dataIndex": "age", "key": "age", "width": 80 },
      { "title": "性别", "dataIndex": "gender", "key": "gender", "width": 80 },
      { "title": "邮箱", "dataIndex": "email", "key": "email" }
    ],
    "dataSource": [
      { "id": 1, "name": "张三", "age": 25, "gender": "男", "email": "zhangsan@example.com" },
      { "id": 2, "name": "李四", "age": 30, "gender": "女", "email": "lisi@example.com" },
      { "id": 3, "name": "王五", "age": 28, "gender": "男", "email": "wangwu@example.com" }
    ],
    "pagination": {
      "current": 1,
      "pageSize": 10,
      "total": 3
    },
    "bordered": true,
    "rowKey": "id"
  }
}
```

### 2.2 JavaScript 方式创建
```javascript
const table = ood.create("Table", {
  id: "table1",
  columns: [
    {
      title: "ID",
      dataIndex: "id",
      key: "id",
      width: 80,
      sorter: (a, b) => a.id - b.id
    },
    {
      title: "姓名",
      dataIndex: "name",
      key: "name",
      width: 120
    },
    {
      title: "年龄",
      dataIndex: "age",
      key: "age",
      width: 80,
      sorter: (a, b) => a.age - b.age
    },
    {
      title: "操作",
      key: "action",
      width: 150,
      render: (text, record) => (
        <div>
          <Button size="small" onClick={() => handleEdit(record)}>编辑</Button>
          <Button size="small" type="danger" onClick={() => handleDelete(record)}>删除</Button>
        </div>
      )
    }
  ],
  dataSource: [],
  pagination: {
    current: 1,
    pageSize: 10,
    total: 0
  },
  rowSelection: {
    type: "checkbox",
    onChange: (selectedRowKeys, selectedRows) => {
      console.log("选中的行:", selectedRows);
    }
  },
  bordered: true,
  rowKey: "id",
  loading: false,
  width: "100%",
  height: 500
});

// 添加到页面
page.addChild(table);

// 加载数据
loadData().then(data => {
  table.setDataSource(data.list);
  table.setPagination({
    current: data.pageNum,
    pageSize: data.pageSize,
    total: data.total
  });
  table.setLoading(false);
});

// 监听分页变化
table.on("onChange", (pagination, filters, sorter) => {
  console.log("分页变化:", pagination);
  console.log("筛选条件:", filters);
  console.log("排序条件:", sorter);
  // 重新加载数据
  loadData(pagination, filters, sorter);
});
```

## 3. 属性列表

| 属性名 | 类型 | 默认值 | 说明 |
|--------|------|--------|------|
| **columns** | array | [] | 列配置数组 |
| **dataSource** | array | [] | 数据源数组 |
| **rowKey** | string/function | "key" | 行数据的主键 |
| **pagination** | boolean/object | false | 分页配置，false 表示不显示分页 |
| **rowSelection** | object | null | 行选择配置 |
| **bordered** | boolean | false | 是否显示边框 |
| **loading** | boolean | false | 是否显示加载状态 |
| **scroll** | object | {} | 滚动配置，可配置 x 和 y |
| **size** | string | "medium" | 表格尺寸："small"、"medium"、"large" |
| **footer** | function | null | 表格页脚渲染函数 |
| **title** | function | null | 表格标题渲染函数 |
| **expandable** | object | null | 可展开行配置 |
| **treeData** | boolean | false | 是否为树形数据 |
| **treeDefaultExpandAll** | boolean | false | 是否默认展开所有树形节点 |
| **treeDefaultExpandedKeys** | array | [] | 默认展开的树形节点键 |
| **width** | number/string | "100%" | 表格宽度 |
| **height** | number/string | null | 表格高度，设置后启用虚拟滚动 |
| **className** | string | "" | 自定义CSS类名 |

## 4. 列配置属性

| 属性名 | 类型 | 默认值 | 说明 |
|--------|------|--------|------|
| **title** | string/function | - | 列标题 |
| **dataIndex** | string | - | 列数据对应的字段名 |
| **key** | string | - | 列的唯一标识 |
| **width** | number/string | - | 列宽度 |
| **fixed** | string/boolean | false | 是否固定列，"left" 或 "right" |
| **sorter** | boolean/function | false | 是否可排序，或自定义排序函数 |
| **filters** | array | - | 筛选器配置 |
| **filteredValue** | array | - | 筛选值 |
| **filterMultiple** | boolean | true | 是否支持多选筛选 |
| **render** | function | - | 自定义列渲染函数 |
| **editable** | boolean | false | 是否可编辑 |
| **align** | string | "left" | 对齐方式："left"、"center"、"right" |
| **className** | string | - | 自定义列CSS类名 |

## 5. 方法列表

| 方法名 | 签名 | 说明 |
|--------|------|------|
| **setColumns** | `setColumns(columns)` | 设置表格列配置 |
| **setDataSource** | `setDataSource(dataSource)` | 设置数据源 |
| **getData** | `getData()` | 获取当前显示的数据 |
| **setPagination** | `setPagination(pagination)` | 设置分页配置 |
| **getPagination** | `getPagination()` | 获取当前分页配置 |
| **setLoading** | `setLoading(loading)` | 设置加载状态 |
| **getSelectedRows** | `getSelectedRows()` | 获取选中的行数据 |
| **getSelectedRowKeys** | `getSelectedRowKeys()` | 获取选中的行键 |
| **clearSelection** | `clearSelection()` | 清除选择 |
| **toggleRowSelection** | `toggleRowSelection(record, selected)` | 切换行选择状态 |
| **expandRow** | `expandRow(rowKey, expanded)` | 展开或折叠行 |
| **scrollTo** | `scrollTo(options)` | 滚动到指定位置 |
| **reload** | `reload()` | 重新加载数据 |

## 6. 事件处理

| 事件名 | 说明 | 回调参数 |
|--------|------|----------|
| **onChange** | 分页、排序、筛选变化时触发 | `(pagination, filters, sorter)` |
| **onRowClick** | 行被点击时触发 | `(record, index, event)` |
| **onRowDoubleClick** | 行被双击时触发 | `(record, index, event)` |
| **onHeaderCellClick** | 表头单元格被点击时触发 | `(column, event)` |
| **onCellClick** | 单元格被点击时触发 | `(record, column, event)` |
| **onSelectionChange** | 选择变化时触发 | `(selectedRowKeys, selectedRows)` |
| **onExpand** | 展开/折叠行时触发 | `(expanded, record)` |
| **onEdit** | 编辑行时触发 | `(record, column, value)` |
| **onFilter** | 筛选时触发 | `(value, record)` |
| **onSort** | 排序时触发 | `(sorter, filters)` |

## 7. 示例代码

### 7.1 基本表格
```json
{
  "id": "basicTable",
  "type": "Table",
  "props": {
    "columns": [
      { "title": "ID", "dataIndex": "id", "key": "id", "width": 80 },
      { "title": "姓名", "dataIndex": "name", "key": "name", "width": 120 },
      { "title": "年龄", "dataIndex": "age", "key": "age", "width": 80 },
      { "title": "性别", "dataIndex": "gender", "key": "gender", "width": 80 },
      { "title": "邮箱", "dataIndex": "email", "key": "email" }
    ],
    "dataSource": [
      { "id": 1, "name": "张三", "age": 25, "gender": "男", "email": "zhangsan@example.com" },
      { "id": 2, "name": "李四", "age": 30, "gender": "女", "email": "lisi@example.com" },
      { "id": 3, "name": "王五", "age": 28, "gender": "男", "email": "wangwu@example.com" }
    ],
    "rowKey": "id",
    "bordered": true,
    "width": "100%"
  }
}
```

### 7.2 带分页的表格
```javascript
const paginationTable = ood.create("Table", {
  id: "paginationTable",
  columns: [
    { "title": "ID", "dataIndex": "id", "key": "id", "width": 80 },
    { "title": "姓名", "dataIndex": "name", "key": "name", "width": 120 },
    { "title": "部门", "dataIndex": "department", "key": "department", "width": 150 },
    { "title": "职位", "dataIndex": "position", "key": "position", "width": 150 },
    { "title": "入职日期", "dataIndex": "hireDate", "key": "hireDate", "width": 120 }
  ],
  dataSource: [],
  rowKey: "id",
  pagination: {
    current: 1,
    pageSize: 10,
    total: 0,
    showSizeChanger: true,
    pageSizeOptions: ["10", "20", "50", "100"],
    showTotal: (total, range) => `第 ${range[0]}-${range[1]} 条，共 ${total} 条`
  },
  bordered: true,
  loading: true,
  width: "100%",
  height: 600
});

// 添加到页面
page.addChild(paginationTable);

// 监听分页变化
paginationTable.on("onChange", (pagination) => {
  console.log("当前页码:", pagination.current);
  console.log("每页条数:", pagination.pageSize);
  // 重新加载数据
  loadUserData(pagination.current, pagination.pageSize);
});
```

### 7.3 带选择功能的表格
```json
{
  "id": "selectionTable",
  "type": "Table",
  "props": {
    "columns": [
      { "title": "ID", "dataIndex": "id", "key": "id", "width": 80 },
      { "title": "商品名称", "dataIndex": "name", "key": "name", "width": 200 },
      { "title": "价格", "dataIndex": "price", "key": "price", "width": 100, "align": "right" },
      { "title": "库存", "dataIndex": "stock", "key": "stock", "width": 100, "align": "right" },
      { "title": "状态", "dataIndex": "status", "key": "status", "width": 100 }
    ],
    "dataSource": [
      { "id": 1, "name": "商品1", "price": 100, "stock": 1000, "status": "在售" },
      { "id": 2, "name": "商品2", "price": 200, "stock": 500, "status": "在售" },
      { "id": 3, "name": "商品3", "price": 300, "stock": 0, "status": "缺货" }
    ],
    "rowKey": "id",
    "rowSelection": {
      "type": "checkbox",
      "onChange": "(selectedRowKeys, selectedRows) => console.log('选中的商品:', selectedRows)"
    },
    "bordered": true,
    "width": "100%"
  }
}
```

### 7.4 带排序和筛选的表格
```javascript
const sortFilterTable = ood.create("Table", {
  id: "sortFilterTable",
  columns: [
    {
      title: "ID",
      dataIndex: "id",
      key: "id",
      width: 80,
      sorter: (a, b) => a.id - b.id
    },
    {
      title: "姓名",
      dataIndex: "name",
      key: "name",
      width: 120
    },
    {
      title: "年龄",
      dataIndex: "age",
      key: "age",
      width: 80,
      sorter: (a, b) => a.age - b.age,
      filters: [
        { text: "18-25", value: "18-25" },
        { text: "26-35", value: "26-35" },
        { text: "36-45", value: "36-45" },
        { text: "45+", value: "45+" }
      ],
      onFilter: (value, record) => {
        if (value === "18-25") return record.age >= 18 && record.age <= 25;
        if (value === "26-35") return record.age >= 26 && record.age <= 35;
        if (value === "36-45") return record.age >= 36 && record.age <= 45;
        if (value === "45+") return record.age > 45;
        return true;
      }
    },
    {
      title: "性别",
      dataIndex: "gender",
      key: "gender",
      width: 80,
      filters: [
        { text: "男", value: "男" },
        { text: "女", value: "女" }
      ],
      onFilter: (value, record) => record.gender === value
    },
    {
      title: "部门",
      dataIndex: "department",
      key: "department",
      width: 150,
      filters: [
        { text: "技术部", value: "技术部" },
        { text: "市场部", value: "市场部" },
        { text: "财务部", value: "财务部" },
        { text: "人力资源部", value: "人力资源部" }
      ],
      onFilter: (value, record) => record.department === value
    }
  ],
  dataSource: userData,
  rowKey: "id",
  bordered: true,
  width: "100%"
});

// 添加到页面
page.addChild(sortFilterTable);

// 监听排序和筛选变化
sortFilterTable.on("onChange", (pagination, filters, sorter) => {
  console.log("筛选条件:", filters);
  console.log("排序条件:", sorter);
});
```

## 8. 最佳实践

### 8.1 列设计
- 合理设置列宽，避免内容过长或过短
- 对于重要的列，考虑固定显示
- 对于可排序的列，添加排序图标
- 对于需要筛选的列，添加筛选器
- 合理使用 align 属性，对齐数据

### 8.2 数据处理
- 对于大数据量（>1000条），建议使用分页或虚拟滚动
- 合理设置 rowKey，确保唯一性
- 对于复杂数据，使用 render 函数自定义渲染
- 考虑数据的更新频率，合理使用 reload 方法

### 8.3 性能优化
- 对于大量数据，启用虚拟滚动
- 避免在 render 函数中执行复杂计算
- 合理使用 loading 状态，提升用户体验
- 考虑使用 memo 或其他优化手段，减少不必要的重渲染

### 8.4 交互设计
- 提供清晰的行选择反馈
- 对于可编辑的表格，提供明确的编辑状态
- 对于展开行，提供清晰的展开/折叠图标
- 提供合理的分页配置，允许用户调整每页条数

### 8.5 可访问性
- 确保表格支持键盘导航
- 提供清晰的ARIA属性
- 支持屏幕阅读器
- 提供足够的颜色对比度
- 为重要操作提供明确的视觉反馈

## 9. 常见问题与解决方案

### 9.1 表格数据不显示
**问题**：设置了 dataSource，但表格不显示数据
**解决方案**：检查 columns 配置是否正确，确保 dataIndex 与数据源字段匹配，且 rowKey 配置正确

### 9.2 排序功能不生效
**问题**：设置了 sorter: true，但排序功能不生效
**解决方案**：确保 columns 中配置了正确的 sorter 属性，或提供自定义排序函数

### 9.3 分页功能不生效
**问题**：设置了 pagination，但分页功能不生效
**解决方案**：确保 pagination 配置正确，且监听了 onChange 事件，在事件中重新加载数据

### 9.4 行选择功能不生效
**问题**：设置了 rowSelection，但无法选择行
**解决方案**：确保 rowKey 配置正确，且 rowSelection 类型配置正确（checkbox 或 radio）

### 9.5 表格性能问题
**问题**：表格数据量大时，渲染卡顿
**解决方案**：启用虚拟滚动，或使用分页加载，或优化 render 函数

## 10. 浏览器兼容性

| 浏览器 | 支持版本 | 注意事项 |
|--------|----------|----------|
| Chrome | 60+ | 完全支持 |
| Firefox | 55+ | 完全支持 |
| Safari | 12+ | 完全支持 |
| Edge | 79+ | 完全支持 |
| IE11 | 不支持 | 建议使用其他表格方案 |

## 11. 相关组件

- **List**：列表组件，用于简单的数据展示
- **Card**：卡片组件，用于展示结构化数据
- **Pagination**：分页组件，用于分页控制
- **Button**：按钮组件，用于表格操作
- **Input**：输入组件，用于表格筛选

## 12. 升级与更新历史

| 版本 | 更新内容 | 日期 |
|------|----------|------|
| 1.0.0 | 初始版本，支持基本表格功能 | 2025-01-15 |
| 1.1.0 | 新增排序和筛选功能 | 2025-03-20 |
| 1.2.0 | 新增分页功能 | 2025-05-10 |
| 1.3.0 | 新增行选择和行编辑功能 | 2025-07-05 |
| 1.4.0 | 新增虚拟滚动支持 | 2025-09-15 |
| 1.5.0 | 新增树形结构数据支持 | 2025-11-20 |

## 13. 扩展阅读

- [ooder-a2ui 数据展示组件最佳实践](FRONTEND_COMPONENTS.md#数据展示组件最佳实践)
- [虚拟滚动技术原理](https://developer.mozilla.org/zh-CN/docs/Web/Performance/Virtual_scrolling)
- [响应式设计在表格中的应用](https://web.dev/responsive-design/)

## 14. 贡献与反馈

如有任何问题或建议，请通过以下方式反馈：
- 提交Issue：[GitHub Issues](https://github.com/ooder/ooder-pro/issues)
- 邮件反馈：support@ooder.com
- 社区论坛：[ooder社区](https://community.ooder.com)

---

**最后更新时间**：2026-01-25  
**文档版本**：1.0  
**作者**：ooder-a2ui 开发团队