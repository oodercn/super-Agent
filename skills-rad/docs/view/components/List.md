# List 组件

## 1. 组件概述

List 组件是 ooder-a2ui 框架中的数据展示组件，用于以列表形式展示结构化数据。它支持多种列表布局、交互方式和数据绑定，是构建各种列表界面的基础组件。

### 1.1 核心功能

- 多种列表布局（垂直列表、水平列表、网格列表）
- 灵活的数据绑定和渲染
- 支持列表项点击、滑动等交互
- 支持分组列表和索引
- 支持无限滚动和分页加载
- 支持自定义列表项模板
- 支持空状态处理
- 响应式设计

### 1.2 应用场景

- 数据列表展示
- 菜单列表
- 商品列表
- 联系人列表
- 消息列表
- 任务列表
- 分类列表

## 2. 创建方式

### 2.1 JSON 方式

```json
{
  "key": "ood.UI.List",
  "properties": {
    "data": [
      {"id": 1, "title": "列表项 1", "subtitle": "副标题 1"},
      {"id": 2, "title": "列表项 2", "subtitle": "副标题 2"},
      {"id": 3, "title": "列表项 3", "subtitle": "副标题 3"}
    ],
    "layout": "vertical",
    "itemTemplate": "ood.UI.ListItem",
    "onItemClick": "onListItemClick"
  }
}
```

### 2.2 JavaScript 方式

```javascript
// 创建 List 组件
var list = ood.create("ood.UI.List")
    .setProperties({
        data: [
            {id: 1, title: "列表项 1", subtitle: "副标题 1"},
            {id: 2, title: "列表项 2", subtitle: "副标题 2"}
        ],
        layout: "vertical",
        onItemClick: function(profile, item, index) {
            console.log("点击了列表项:", item, "索引:", index);
        }
    });
```

## 3. 属性配置

### 3.1 基础属性

| 属性名 | 类型 | 默认值 | 说明 |
|--------|------|--------|------|
| `key` | String | "ood.UI.List" | 组件类型 |
| `layout` | String | "vertical" | 列表布局：vertical, horizontal, grid |
| `direction` | String | "ltr" | 列表方向：ltr, rtl |
| `scrollable` | Boolean | true | 是否支持滚动 |
| `showScrollbar` | Boolean | true | 是否显示滚动条 |
| `emptyText` | String | "暂无数据" | 空数据时显示的文本 |
| `loading` | Boolean | false | 是否显示加载状态 |
| `className` | String | "" | 自定义类名 |

### 3.2 数据属性

| 属性名 | 类型 | 默认值 | 说明 |
|--------|------|--------|------|
| `data` | Array/Object | [] | 列表数据，可以是数组或数据源对象 |
| `itemTemplate` | String/Function | "ood.UI.ListItem" | 列表项模板组件或渲染函数 |
| `groupBy` | String/Function | null | 分组字段或分组函数 |
| `sortBy` | String/Function | null | 排序字段或排序函数 |
| `filter` | Function | null | 数据过滤函数 |

### 3.3 样式属性

| 属性名 | 类型 | 默认值 | 说明 |
|--------|------|--------|------|
| `width` | String/Number | "100%" | 列表宽度 |
| `height` | String/Number | "auto" | 列表高度 |
| `itemHeight` | Number | 50 | 列表项高度（用于虚拟滚动） |
| `gap` | Number | 0 | 列表项间距 |
| `padding` | Number/String | 0 | 列表内边距 |
| `background` | String | "#ffffff" | 列表背景色 |
| `border` | String | "none" | 列表边框 |
| `borderRadius` | Number | 0 | 列表圆角 |

### 3.4 交互属性

| 属性名 | 类型 | 默认值 | 说明 |
|--------|------|--------|------|
| `selectable` | Boolean | false | 是否支持选择 |
| `multiple` | Boolean | false | 是否支持多选 |
| `selectedKeys` | Array | [] | 选中项的键值数组 |
| `disabledKeys` | Array | [] | 禁用项的键值数组 |
| `swipeable` | Boolean | false | 是否支持滑动操作 |
| `pullRefresh` | Boolean | false | 是否支持下拉刷新 |
| `loadMore` | Boolean | false | 是否支持上拉加载更多 |

## 4. 方法列表

| 方法名 | 说明 | 参数 | 返回值 |
|--------|------|------|--------|
| `setData(data)` | 设置列表数据 | data: 列表数据 | 当前实例 |
| `getData()` | 获取列表数据 | 无 | 列表数据 |
| `addItem(item, index)` | 添加列表项 | item: 列表项数据<br>index: 插入位置 | 当前实例 |
| `removeItem(index)` | 移除列表项 | index: 列表项索引 | 当前实例 |
| `updateItem(index, item)` | 更新列表项 | index: 列表项索引<br>item: 更新后的数据 | 当前实例 |
| `getSelectedItems()` | 获取选中项 | 无 | 选中项数组 |
| `setSelectedKeys(keys)` | 设置选中项 | keys: 选中项键值数组 | 当前实例 |
| `refresh()` | 刷新列表 | 无 | 当前实例 |
| `scrollTo(index)` | 滚动到指定索引 | index: 列表项索引 | 当前实例 |
| `scrollToTop()` | 滚动到顶部 | 无 | 当前实例 |
| `scrollToBottom()` | 滚动到底部 | 无 | 当前实例 |

## 5. 事件处理

| 事件名 | 说明 | 回调参数 |
|--------|------|----------|
| `onItemClick` | 列表项点击事件 | profile: 组件配置<br>item: 点击的列表项数据<br>index: 列表项索引 |
| `onItemLongClick` | 列表项长按事件 | profile: 组件配置<br>item: 长按的列表项数据<br>index: 列表项索引 |
| `onItemSwipe` | 列表项滑动事件 | profile: 组件配置<br>item: 滑动的列表项数据<br>direction: 滑动方向 |
| `onSelectChange` | 选择状态变化事件 | profile: 组件配置<br>selectedKeys: 选中项键值数组<br>selectedItems: 选中项数据数组 |
| `onPullRefresh` | 下拉刷新事件 | profile: 组件配置 |
| `onLoadMore` | 上拉加载更多事件 | profile: 组件配置 |
| `onScroll` | 滚动事件 | profile: 组件配置<br>scrollTop: 滚动距离<br>scrollHeight: 总高度<br>clientHeight: 可视高度 |

## 6. 列表项类型

### 6.1 基础列表项

```json
{
  "key": "ood.UI.ListItem",
  "properties": {
    "title": "列表项标题",
    "subtitle": "列表项副标题",
    "icon": "ri ri-home-line",
    "rightIcon": "ri ri-arrow-right-line"
  }
}
```

### 6.2 带图列表项

```json
{
  "key": "ood.UI.ListItem",
  "properties": {
    "title": "带图列表项",
    "image": {
      "src": "https://example.com/image.jpg",
      "width": 60,
      "height": 60,
      "radius": 8
    },
    "subtitle": "带图列表项副标题",
    "rightText": "右侧文本"
  }
}
```

### 6.3 操作列表项

```json
{
  "key": "ood.UI.ListItem",
  "properties": {
    "title": "带操作项的列表项",
    "subtitle": "可进行多项操作",
    "actions": [
      {"icon": "ri ri-edit-line", "onClick": "onEdit"},
      {"icon": "ri ri-delete-line", "onClick": "onDelete"}
    ]
  }
}
```

## 7. 使用示例

### 7.1 基础列表

```javascript
// 创建基础列表
var basicList = ood.create("ood.UI.List")
    .setProperties({
        data: [
            {id: 1, title: "列表项 1", subtitle: "这是第一个列表项的副标题"},
            {id: 2, title: "列表项 2", subtitle: "这是第二个列表项的副标题"},
            {id: 3, title: "列表项 3", subtitle: "这是第三个列表项的副标题"}
        ],
        onItemClick: function(profile, item, index) {
            ood.Mobile.Toast.success(`点击了第 ${index + 1} 项: ${item.title}`);
        }
    });
```

### 7.2 带图标的列表

```javascript
// 创建带图标的列表
var iconList = ood.create("ood.UI.List")
    .setProperties({
        data: [
            {id: 1, title: "首页", icon: "ri ri-home-line"},
            {id: 2, title: "消息", icon: "ri ri-message-line"},
            {id: 3, title: "联系人", icon: "ri ri-contacts-line"},
            {id: 4, title: "设置", icon: "ri ri-settings-line"}
        ],
        itemTemplate: function(item) {
            return ood.create("ood.UI.ListItem")
                .setProperties({
                    title: item.title,
                    icon: item.icon,
                    rightIcon: "ri ri-arrow-right-line"
                });
        }
    });
```

### 7.3 带操作项的列表

```javascript
// 创建带操作项的列表
var actionList = ood.create("ood.UI.List")
    .setProperties({
        data: [
            {id: 1, title: "任务 1", status: "pending"},
            {id: 2, title: "任务 2", status: "completed"},
            {id: 3, title: "任务 3", status: "pending"}
        ],
        itemTemplate: function(item) {
            return ood.create("ood.UI.ListItem")
                .setProperties({
                    title: item.title,
                    subtitle: item.status === "completed" ? "已完成" : "待完成",
                    rightText: item.status === "completed" ? "已完成" : "待完成",
                    actions: [
                        {
                            icon: "ri ri-check-line",
                            onClick: function() {
                                ood.Mobile.Toast.success(`标记任务 ${item.id} 为完成`);
                            }
                        },
                        {
                            icon: "ri ri-delete-line",
                            onClick: function() {
                                ood.Mobile.Toast.success(`删除任务 ${item.id}`);
                            }
                        }
                    ]
                });
        }
    });
```

### 7.4 分组列表

```javascript
// 创建分组列表
var groupList = ood.create("ood.UI.List")
    .setProperties({
        data: [
            {id: 1, title: "苹果", category: "水果"},
            {id: 2, title: "香蕉", category: "水果"},
            {id: 3, title: "胡萝卜", category: "蔬菜"},
            {id: 4, title: "西红柿", category: "蔬菜"}
        ],
        groupBy: "category",
        onItemClick: function(profile, item) {
            ood.Mobile.Toast.success(`选择了 ${item.category}: ${item.title}`);
        }
    });
```

### 7.5 无限滚动列表

```javascript
// 创建无限滚动列表
var infiniteList = ood.create("ood.UI.List")
    .setProperties({
        data: [],
        loadMore: true,
        emptyText: "暂无数据",
        onLoadMore: function(profile) {
            // 模拟加载更多数据
            setTimeout(function() {
                var currentData = profile.boxing().getData();
                var newData = [];
                var startIndex = currentData.length + 1;
                
                for (var i = 0; i < 10; i++) {
                    newData.push({
                        id: startIndex + i,
                        title: `列表项 ${startIndex + i}`,
                        subtitle: `动态加载的列表项 ${startIndex + i}`
                    });
                }
                
                // 添加新数据
                profile.boxing().setData(currentData.concat(newData));
            }, 1000);
        }
    });
```

## 8. 最佳实践

### 8.1 列表设计原则

1. **保持简洁**：列表项内容应简洁明了，避免过多信息
2. **一致的视觉层次**：使用清晰的标题、副标题和辅助信息层次
3. **适当的间距**：保持合适的列表项间距，提高可读性
4. **明确的交互反馈**：提供清晰的点击、触摸反馈
5. **考虑空状态**：设计友好的空状态提示
6. **优化长列表性能**：对于大量数据，使用虚拟滚动或分页加载

### 8.2 性能优化

1. **使用虚拟滚动**：对于大量数据，启用虚拟滚动减少DOM节点
2. **合理设置itemHeight**：准确设置itemHeight以提高虚拟滚动性能
3. **避免复杂的列表项模板**：简化列表项结构，减少渲染开销
4. **使用数据分页**：对于大数据集，采用分页加载策略
5. **图片懒加载**：列表中的图片使用懒加载

### 8.3 交互设计

1. **提供多种交互方式**：根据场景支持点击、长按、滑动等交互
2. **清晰的操作入口**：操作按钮应清晰可见，易于点击
3. **合理的操作反馈**：操作后提供明确的反馈信息
4. **支持键盘导航**：确保键盘用户可以正常操作列表
5. **考虑可访问性**：添加适当的ARIA属性

## 9. 浏览器兼容性

| 浏览器 | 支持版本 | 备注 |
|--------|----------|------|
| Chrome | 最新版 | 完全支持 |
| Firefox | 最新版 | 完全支持 |
| Safari | 最新版 | 完全支持 |
| Edge | 最新版 | 完全支持 |
| IE | 11+ | 部分功能可能受限 |

## 10. 注意事项

1. **数据格式一致性**：确保列表数据格式一致，便于渲染
2. **键值唯一性**：每个列表项应具有唯一的键值，便于组件跟踪和更新
3. **避免过度嵌套**：列表项模板应避免过度嵌套，影响性能
4. **合理使用事件**：避免在列表项中绑定过多事件，考虑事件委托
5. **测试不同数据量**：测试列表在不同数据量下的性能表现
6. **考虑移动端体验**：在移动端，确保列表项大小适合触摸操作

## 11. 扩展阅读

- [Table 组件文档](Table.md)
- [Card 组件文档](Card.md)
- [数据绑定最佳实践](../BEST_PRACTICES_DATA_BINDING.md)
- [性能优化指南](../PERFORMANCE_OPTIMIZATION.md)

List 组件是构建各种列表界面的基础组件，通过合理配置和扩展，可以满足不同场景下的列表展示需求。结合数据绑定和事件处理，可以创建出交互丰富、性能优良的列表界面。