# Group

分组容器组件，提供可折叠/展开的容器区域，支持主题切换、响应式设计和可访问性增强。常用于组织相关UI元素，创建可折叠面板、设置区域、导航分组等场景。

## 类名
`ood.UI.Group`

## 继承
`ood.UI.Panel`

## 快速开始

```html
<!-- 引入组件 -->
<script type="text/javascript" src="ood/UI/Group.js"></script>

<!-- 创建分组容器 -->
<div id="group-container"></div>

<script>
var group = ood.UI.Group({
    caption: '设置面板',
    toggleBtn: true,
    width: '400px',
    height: '300px',
    theme: 'light',
    responsive: true
}).appendTo('#group-container');

// 在分组容器中添加内容
group.append('<p>这是分组容器的内容区域。</p>');
</script>
```

## 属性

### 初始化属性 (iniProp)

| 属性名 | 类型 | 默认值 | 描述 |
|--------|------|--------|------|
| `caption` | String | `'组容器'` | 分组标题 |
| `toggleBtn` | Boolean | `false` | 是否显示折叠/展开按钮 |
| `width` | String | `'18em'` | 组件宽度 |

### 数据模型属性 (DataModel)

| 属性名 | 类型 | 默认值 | 描述 |
|--------|------|--------|------|
| `expression` | Object | `{ini: ''}` | 表达式 |
| `euClassName` | String | `''` | 额外CSS类名 |
| `dock` | String | `'none'` | 停靠方式 |
| `noFrame` | Boolean | `null` | 是否无边框 |
| `borderType` | String | `null` | 边框类型 |
| `toggleBtn` | Object | `{ini: true}` | 折叠/展开按钮 |
| `transitionDuration` | Object | `{ini: 'normal'}` | 过渡动画时长：'fast', 'normal', 'slow' |
| `responsiveBreakpoint` | Object | `{ini: 'md'}` | 响应式断点：'sm', 'md', 'lg', 'xl' |
| `ariaLabel` | Object | `{ini: ''}` | ARIA标签 |
| `tabIndex` | Object | `{ini: 0}` | Tab索引 |

## 方法

### `setTheme(profile, theme)`
设置主题。

**参数：**
- `profile` (Object): 组件profile对象
- `theme` (String): 主题名称：'light', 'dark', 'highcontrast'

### `toggleTheme(profile)`
循环切换主题（light → dark → highcontrast → light）。

**参数：**
- `profile` (Object): 组件profile对象

**返回：**
- (String): 切换后的主题名称

### `enableKeyboardNavigation(profile)`
启用键盘导航支持，为标题栏添加Tab索引和Enter/Space键响应。

**参数：**
- `profile` (Object): 组件profile对象

### `setScreenReaderLabel(profile, label)`
设置屏幕阅读器标签，增强可访问性。

**参数：**
- `profile` (Object): 组件profile对象
- `label` (String): ARIA标签文本

### `updateResponsibleLayout(profile, breakpoint)`
更新响应式布局，根据断点调整显示效果。

**参数：**
- `profile` (Object): 组件profile对象
- `breakpoint` (String): 断点名称：'sm', 'md', 'lg', 'xl'

### `_prepareData(profile)` (内部方法)
准备渲染数据，设置样式和类名。

### `_toggle(profile, value, ignoreEvent)` (内部方法)
切换折叠/展开状态。

**参数：**
- `profile` (Object): 组件profile对象
- `value` (Boolean): 是否展开
- `ignoreEvent` (Boolean): 是否忽略事件触发

### `_onresize(profile, width, height)` (内部方法)
处理尺寸变化，调整布局。

## 事件

组件支持以下事件：

### 触摸事件
- `touchstart` - 触摸开始
- `touchmove` - 触摸移动
- `touchend` - 触摸结束
- `touchcancel` - 触摸取消

### 滑动手势
- `swipe` - 滑动（任意方向）
- `swipeleft` - 向左滑动
- `swiperight` - 向右滑动
- `swipeup` - 向上滑动
- `swipedown` - 向下滑动

### 按压手势
- `press` - 长按开始
- `pressup` - 长按结束

### 自定义事件
- `beforeExpand` - 展开前触发
- `afterExpand` - 展开后触发
- `beforeFold` - 折叠前触发
- `afterFold` - 折叠后触发

## 示例

### 基本分组容器

```html
<!DOCTYPE html>
<html>
<head>
    <script type="text/javascript" src="ood/ood.js"></script>
    <script type="text/javascript" src="ood/UI/Group.js"></script>
    <link rel="stylesheet" type="text/css" href="css/default.css">
</head>
<body>
    <div id="my-group"></div>
    
    <script>
    // 创建分组容器
    var group = ood.UI.Group({
        caption: '用户信息',
        toggleBtn: true,
        width: '500px',
        height: '350px',
        theme: 'light',
        transitionDuration: 'normal'
    }).appendTo('#my-group');
    
    // 添加内容
    group.append(`
        <div class="user-profile">
            <h3>张三</h3>
            <p>邮箱: zhangsan@example.com</p>
            <p>电话: 13800138000</p>
            <p>地址: 北京市朝阳区</p>
        </div>
    `);
    
    // 监听折叠/展开事件
    group.on('toggle', function(isExpanded) {
        console.log('分组容器', isExpanded ? '展开' : '折叠');
    });
    </script>
</body>
</html>
```

### 响应式分组容器

```html
<div id="responsive-group"></div>

<script>
var responsiveGroup = ood.UI.Group({
    caption: '响应式面板',
    toggleBtn: true,
    width: '100%',
    height: 'auto',
    theme: 'light',
    responsive: true,
    responsiveBreakpoint: 'md',
    transitionDuration: 'fast'
}).appendTo('#responsive-group');

// 添加复杂内容
responsiveGroup.append(`
    <div class="responsive-content">
        <h4>自适应内容</h4>
        <p>此分组容器会根据屏幕尺寸自动调整布局。</p>
        <div class="stats">
            <div class="stat-item">
                <span class="value">128</span>
                <span class="label">项目数</span>
            </div>
            <div class="stat-item">
                <span class="value">64</span>
                <span class="label">用户数</span>
            </div>
            <div class="stat-item">
                <span class="value">32</span>
                <span class="label">在线数</span>
            </div>
        </div>
    </div>
`);

// 根据窗口大小更新布局
window.addEventListener('resize', function() {
    var width = window.innerWidth;
    var breakpoint = width < 576 ? 'sm' : 
                     width < 768 ? 'md' : 
                     width < 992 ? 'lg' : 'xl';
    responsiveGroup.updateResponsiveLayout(breakpoint);
});
</script>
```

### 可访问性增强分组

```html
<div id="accessible-group"></div>

<script>
var accessibleGroup = ood.UI.Group({
    caption: '无障碍面板',
    toggleBtn: true,
    width: '600px',
    height: '400px',
    theme: 'light',
    ariaLabel: '用户设置面板，包含个人资料和偏好设置',
    tabIndex: 0
}).appendTo('#accessible-group');

// 设置屏幕阅读器标签
accessibleGroup.setScreenReaderLabel('用户设置面板，包含个人资料和偏好设置');

// 启用键盘导航
accessibleGroup.enableKeyboardNavigation();

// 添加可访问内容
accessibleGroup.append(`
    <div role="region" aria-label="用户个人资料">
        <h5 id="profile-heading">个人资料</h5>
        <div aria-labelledby="profile-heading">
            <p>姓名: 李四</p>
            <p>角色: 管理员</p>
            <p>最后登录: 2025-01-05</p>
        </div>
    </div>
    <div role="region" aria-label="偏好设置">
        <h5 id="prefs-heading">偏好设置</h5>
        <div aria-labelledby="prefs-heading">
            <p>语言: 简体中文</p>
            <p>时区: 中国标准时间 (UTC+8)</p>
            <p>主题: 浅色</p>
        </div>
    </div>
`);

// 切换主题
accessibleGroup.toggleTheme();
</script>
```

### 多分组嵌套

```html
<div id="nested-groups"></div>

<script>
var nestedGroups = ood.UI.Group({
    caption: '主分组',
    toggleBtn: true,
    width: '800px',
    height: '600px',
    theme: 'dark'
}).appendTo('#nested-groups');

// 添加嵌套分组
nestedGroups.append(`
    <div id="subgroup1"></div>
    <div id="subgroup2"></div>
`);

// 创建子分组1
var subgroup1 = ood.UI.Group({
    caption: '系统设置',
    toggleBtn: true,
    width: '100%',
    height: '200px',
    theme: 'dark'
}).appendTo('#subgroup1');

subgroup1.append(`
    <ul>
        <li>网络设置</li>
        <li>安全设置</li>
        <li>通知设置</li>
    </ul>
`);

// 创建子分组2
var subgroup2 = ood.UI.Group({
    caption: '用户管理',
    toggleBtn: true,
    width: '100%',
    height: '250px',
    theme: 'dark'
}).appendTo('#subgroup2');

subgroup2.append(`
    <table>
        <thead>
            <tr><th>用户名</th><th>角色</th><th>状态</th></tr>
        </thead>
        <tbody>
            <tr><td>张三</td><td>管理员</td><td>在线</td></tr>
            <tr><td>李四</td><td>编辑</td><td>离线</td></tr>
        </tbody>
    </table>
`);
</script>
```

## 注意事项

1. **折叠/展开功能**：
   - 设置 `toggleBtn: true` 显示折叠/展开按钮
   - 点击标题栏或按钮可切换状态
   - 支持平滑过渡动画，可通过 `transitionDuration` 调整速度

2. **主题系统**：
   - 支持亮色（light）、暗色（dark）、高对比度（highcontrast）主题
   - 使用CSS变量和类名实现主题切换
   - 可与OOD框架全局主题系统集成

3. **响应式设计**：
   - 支持断点配置：sm（<576px）、md（<768px）、lg（<992px）、xl（≥992px）
   - 自动根据屏幕尺寸调整布局和样式
   - 可通过 `updateResponsiveLayout` 方法动态更新

4. **可访问性**：
   - 自动添加ARIA角色和属性
   - 支持键盘导航（Tab、Enter、Space）
   - 屏幕阅读器友好，可设置详细标签
   - 遵循WCAG 2.1 AA标准

5. **内容管理**：
   - 使用 `append` 方法添加内容
   - 支持HTML字符串和OOD组件对象
   - 内容区域会自动适配容器尺寸

6. **尺寸控制**：
   - 支持固定尺寸（px、em）和自适应（auto、%）
   - 折叠时自动调整高度
   - 可通过CSS自定义样式

7. **性能优化**：
   - 使用CSS过渡实现动画，性能优于JavaScript
   - 支持懒加载内容，提高初始渲染速度
   - 优化DOM操作，减少重排重绘