# MTabs

移动标签页组件，专为移动设备优化的标签页界面，支持顶部、底部、左侧、右侧四种位置，提供折叠/展开功能、主题切换和响应式设计，适用于移动端导航和内容切换场景。

## 类名
`ood.UI.MTabs`

## 继承
`ood.UI.Tabs`

## 快速开始

```html
<!-- 引入组件 -->
<script type="text/javascript" src="ood/UI/MTabs.js"></script>

<!-- 创建移动标签页 -->
<div id="mtabs-container"></div>

<script>
var mTabs = ood.UI.MTabs({
    width: '100%',
    height: '500px',
    theme: 'light',
    responsive: true,
    barLocation: 'left',
    barSize: '3em',
    sideBarStatus: 'expand',
    borderType: 'flat',
    items: [
        {
            id: 'home',
            caption: '首页',
            imageClass: 'ri-home-line'
        },
        {
            id: 'profile',
            caption: '个人中心',
            imageClass: 'ri-user-line'
        },
        {
            id: 'settings',
            caption: '设置',
            imageClass: 'ri-settings-line'
        },
        {
            id: 'help',
            caption: '帮助',
            imageClass: 'ri-question-line'
        }
    ],
    value: 'home'
}).appendTo('#mtabs-container');

// 切换标签页
mTabs.value('profile');
</script>
```

## 属性

### 初始化属性 (iniProp)

| 属性名 | 类型 | 默认值 | 描述 |
|--------|------|--------|------|
| `barLocation` | String | `'left'` | 标签栏位置：'top', 'bottom', 'left', 'right' |
| `barSize` | String | `'12em'` | 标签栏大小（展开状态） |
| `sideBarStatus` | String | `'fold'` | 侧边栏状态：'expand'（展开）, 'fold'（折叠） |
| `items` | Array | 预定义4个示例项 | 标签页项目数组 |
| `value` | String | `'a'` | 当前选中的标签页ID |

### 数据模型属性 (DataModel)

| 属性名 | 类型 | 默认值 | 描述 |
|--------|------|--------|------|
| `HAlign` | String | `null` | 水平对齐方式（继承自Tabs） |
| `barLocation` | String | `'top'` | 标签栏位置：'top', 'bottom', 'left', 'right'，切换时自动调整布局 |
| `barHAlign` | String | `'left'` | 标签栏水平对齐：'left', 'center', 'right' |
| `barVAlign` | String | `'top'` | 标签栏垂直对齐：'top', 'bottom' |
| `barSize` | String | `'2.5em'` | 标签栏大小（带空间单位） |
| `borderType` | String | `'none'` | 边框类型：'none', 'flat', 'inset', 'outset' |
| `noFoldBar` | Boolean | `false` | 是否禁用折叠栏 |
| `sideBarStatus` | String | `'expand'` | 侧边栏状态：'expand'（展开）, 'fold'（折叠） |
| `sideBarSize` | String | `'3em'` | 折叠状态下的侧边栏大小 |

### 继承自Tabs的属性
- `items`: 标签页项目数组（可覆盖）
- `value`: 当前选中的标签页ID
- `theme`: 主题设置
- `responsive`: 响应式设计开关
- `tabindex`: Tab键顺序
- `disabled`: 禁用状态
- `selectable`: 是否可选择

## 方法

### `setBarLocation(location, silent)`
设置标签栏位置。

**参数：**
- `location` (String): 位置值：'top', 'bottom', 'left', 'right'
- `silent` (Boolean, 可选): 是否静默更新（不触发action）

**返回：**
- (Object): 组件实例，支持链式调用。

### `setBarHAlign(align, silent)`
设置标签栏水平对齐方式。

**参数：**
- `align` (String): 对齐值：'left', 'center', 'right'
- `silent` (Boolean, 可选): 是否静默更新

**返回：**
- (Object): 组件实例，支持链式调用。

### `setBarVAlign(align, silent)`
设置标签栏垂直对齐方式。

**参数：**
- `align` (String): 对齐值：'top', 'bottom'
- `silent` (Boolean, 可选): 是否静默更新

**返回：**
- (Object): 组件实例，支持链式调用。

### `setBorderType(type, silent)`
设置边框类型。

**参数：**
- `type` (String): 边框类型：'none', 'flat', 'inset', 'outset'
- `silent` (Boolean, 可选): 是否静默更新

**返回：**
- (Object): 组件实例，支持链式调用。

### `setSideBarStatus(status, silent)`
设置侧边栏状态（展开/折叠）。

**参数：**
- `status` (String): 状态值：'expand', 'fold'
- `silent` (Boolean, 可选): 是否静默更新

**返回：**
- (Object): 组件实例，支持链式调用。

### `adjustSize()`
调整组件尺寸，适应容器变化。

**返回：**
- (Object): 组件实例，支持链式调用。

### `getContainer(subId)`
获取指定子ID的容器节点。

**参数：**
- `subId` (String): 子项目ID

**返回：**
- (Object): DOM节点或组件实例。

### `refresh()`
刷新组件，重新渲染标签页。

**返回：**
- (Object): 组件实例，支持链式调用。

## CSS 变量 (Appearances)

| 类名 | 描述 |
|------|------|
| `LIST` | 标签栏容器样式 |
| `LIST-attop`, `LIST-atbottom`, `LIST-atleft`, `LIST-atright` | 标签栏位置样式 |
| `LISTBG` | 标签栏背景样式 |
| `MENU` | 菜单按钮样式 |
| `MENU2` | 折叠/展开按钮样式 |
| `MENUICON2` | 折叠/展开图标样式 |
| `ITEMS` | 标签页项目容器样式 |
| `ITEMS-left`, `ITEMS-center`, `ITEMS-right` | 标签页水平对齐样式 |
| `ITEM` | 单个标签页样式 |
| `ITEMI` | 标签页图标样式 |
| `ITEMC` | 标签页内容样式 |
| `HANDLE` | 拖拽手柄样式 |

## 示例

### 移动端导航标签栏

```html
<!DOCTYPE html>
<html>
<head>
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <script type="text/javascript" src="ood/ood.js"></script>
    <script type="text/javascript" src="ood/UI/MTabs.js"></script>
    <link rel="stylesheet" type="text/css" href="css/default.css">
    <style>
        body { margin: 0; padding: 0; font-family: -apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, sans-serif; }
        .app-container { display: flex; height: 100vh; overflow: hidden; }
        .content-area { flex: 1; padding: 20px; overflow-y: auto; }
        .tab-content { display: none; }
        .tab-content.active { display: block; }
    </style>
</head>
<body>
    <div class="app-container">
        <div id="mobile-tabs"></div>
        <div class="content-area" id="main-content">
            <div id="home-content" class="tab-content active">
                <h2>首页</h2>
                <p>欢迎使用移动应用！这里是首页内容。</p>
            </div>
            <div id="profile-content" class="tab-content">
                <h2>个人中心</h2>
                <p>这里是您的个人信息和设置。</p>
            </div>
            <div id="settings-content" class="tab-content">
                <h2>设置</h2>
                <p>应用设置选项。</p>
            </div>
            <div id="help-content" class="tab-content">
                <h2>帮助</h2>
                <p>使用指南和常见问题。</p>
            </div>
        </div>
    </div>
    
    <script>
    var mobileTabs = ood.UI.MTabs({
        width: '80px',
        height: '100%',
        theme: 'dark',
        responsive: true,
        barLocation: 'left',
        barSize: '80px',
        sideBarStatus: 'expand',
        borderType: 'flat',
        items: [
            {
                id: 'home',
                caption: '首页',
                imageClass: 'ri-home-line',
                tooltip: '返回首页'
            },
            {
                id: 'profile',
                caption: '我的',
                imageClass: 'ri-user-line',
                tooltip: '个人中心'
            },
            {
                id: 'settings',
                caption: '设置',
                imageClass: 'ri-settings-line',
                tooltip: '应用设置'
            },
            {
                id: 'help',
                caption: '帮助',
                imageClass: 'ri-question-line',
                tooltip: '使用帮助'
            }
        ],
        value: 'home'
    }).appendTo('#mobile-tabs');
    
    // 标签页切换事件
    mobileTabs.on('change', function(profile, oldValue, newValue) {
        // 隐藏所有内容
        document.querySelectorAll('.tab-content').forEach(function(el) {
            el.classList.remove('active');
        });
        // 显示对应内容
        var contentId = newValue + '-content';
        var contentEl = document.getElementById(contentId);
        if (contentEl) {
            contentEl.classList.add('active');
        }
    });
    </script>
</body>
</html>
```

### 可折叠侧边栏

```html
<div id="sidebar-container"></div>

<script>
var sidebar = ood.UI.MTabs({
    width: '300px',
    height: '600px',
    theme: 'light',
    responsive: true,
    barLocation: 'left',
    barSize: '250px',
    sideBarStatus: 'expand',
    sideBarSize: '60px',
    borderType: 'inset',
    noFoldBar: false,
    items: [
        {
            id: 'dashboard',
            caption: '仪表板',
            imageClass: 'ri-dashboard-line',
            content: '<h3>仪表板</h3><p>这里是数据概览</p>'
        },
        {
            id: 'reports',
            caption: '报表',
            imageClass: 'ri-file-chart-line',
            content: '<h3>报表中心</h3><p>各类统计报表</p>'
        },
        {
            id: 'analytics',
            caption: '分析',
            imageClass: 'ri-line-chart-line',
            content: '<h3>数据分析</h3><p>深度数据挖掘</p>'
        },
        {
            id: 'team',
            caption: '团队',
            imageClass: 'ri-team-line',
            content: '<h3>团队管理</h3><p>成员协作平台</p>'
        }
    ],
    value: 'dashboard'
}).appendTo('#sidebar-container');

// 自动折叠/展开
function toggleSidebar() {
    var currentStatus = sidebar.properties.sideBarStatus;
    var newStatus = currentStatus === 'expand' ? 'fold' : 'expand';
    sidebar.setSideBarStatus(newStatus);
}

// 响应屏幕尺寸变化
window.addEventListener('resize', function() {
    if (window.innerWidth < 768) {
        sidebar.setSideBarStatus('fold');
    } else {
        sidebar.setSideBarStatus('expand');
    }
});
</script>
```

### 底部导航栏

```html
<div id="bottom-nav-container"></div>

<script>
var bottomNav = ood.UI.MTabs({
    width: '100%',
    height: '70px',
    theme: 'light',
    responsive: true,
    barLocation: 'bottom',
    barSize: '70px',
    sideBarStatus: 'expand',
    borderType: 'flat',
    barHAlign: 'center',
    items: [
        {
            id: 'home',
            caption: '首页',
            imageClass: 'ri-home-3-line',
            compact: true
        },
        {
            id: 'search',
            caption: '搜索',
            imageClass: 'ri-search-line',
            compact: true
        },
        {
            id: 'cart',
            caption: '购物车',
            imageClass: 'ri-shopping-cart-line',
            compact: true,
            badge: '3'
        },
        {
            id: 'account',
            caption: '我的',
            imageClass: 'ri-account-circle-line',
            compact: true
        }
    ],
    value: 'home'
}).appendTo('#bottom-nav-container');

// 添加徽章更新
function updateCartBadge(count) {
    var cartItem = bottomNav.items().find(function(item) {
        return item.id === 'cart';
    });
    if (cartItem) {
        cartItem.badge = count > 0 ? count.toString() : '';
        bottomNav.refresh();
    }
}

// 示例：更新购物车数量
updateCartBadge(5);
</script>
```

### 响应式标签页组

```html
<div class="responsive-tabs">
    <div id="responsive-tabs-container"></div>
    <div id="tabs-content-area"></div>
</div>

<script>
var responsiveTabs = ood.UI.MTabs({
    width: '100%',
    height: 'auto',
    theme: 'light',
    responsive: true,
    barLocation: 'top',
    barSize: '50px',
    sideBarStatus: 'expand',
    borderType: 'flat',
    items: [
        {
            id: 'overview',
            caption: '概览',
            imageClass: 'ri-eye-line',
            content: '<div class="tab-panel"><h3>系统概览</h3><p>当前系统状态和关键指标</p></div>'
        },
        {
            id: 'performance',
            caption: '性能',
            imageClass: 'ri-speed-line',
            content: '<div class="tab-panel"><h3>性能监控</h3><p>系统性能数据和趋势分析</p></div>'
        },
        {
            id: 'security',
            caption: '安全',
            imageClass: 'ri-shield-check-line',
            content: '<div class="tab-panel"><h3>安全中心</h3><p>安全状态和防护措施</p></div>'
        },
        {
            id: 'logs',
            caption: '日志',
            imageClass: 'ri-file-list-line',
            content: '<div class="tab-panel"><h3>系统日志</h3><p>操作记录和审计日志</p></div>'
        }
    ],
    value: 'overview'
}).appendTo('#responsive-tabs-container');

// 动态响应布局
function adjustLayout() {
    var containerWidth = document.querySelector('.responsive-tabs').offsetWidth;
    
    if (containerWidth < 480) {
        // 小屏幕：底部导航
        responsiveTabs.setBarLocation('bottom');
        responsiveTabs.setBarSize('60px');
        responsiveTabs.properties.barHAlign = 'center';
    } else if (containerWidth < 768) {
        // 中等屏幕：左侧导航
        responsiveTabs.setBarLocation('left');
        responsiveTabs.setBarSize('200px');
    } else {
        // 大屏幕：顶部导航
        responsiveTabs.setBarLocation('top');
        responsiveTabs.setBarSize('50px');
    }
    
    responsiveTabs.refresh();
}

// 初始调整
adjustLayout();
// 窗口大小变化时调整
window.addEventListener('resize', adjustLayout);
</script>
```

## 注意事项

1. **移动端优化**：组件专门针对移动设备进行优化，支持触摸操作和响应式布局。

2. **四种位置**：支持'top'（顶部）、'bottom'（底部）、'left'（左侧）、'right'（右侧）四种标签栏位置。

3. **折叠功能**：侧边栏支持'expand'（展开）和'fold'（折叠）两种状态，节省屏幕空间。

4. **响应式设计**：组件自动适应不同屏幕尺寸，在小屏幕设备上优化布局。

5. **主题支持**：支持light和dark主题，可自定义CSS变量调整外观。

6. **性能考虑**：大量标签页项目（超过20个）可能会影响性能，建议合理设计导航结构。

7. **浏览器兼容**：支持现代浏览器和移动端浏览器，部分高级功能在老版本浏览器中可能受限。

8. **可访问性**：组件内置ARIA属性支持，提升屏幕阅读器兼容性。

9. **动态更新**：支持通过JavaScript API动态添加、删除、更新标签页项目。

10. **事件系统**：提供完整的change、select、deselect等事件，可实现丰富交互。