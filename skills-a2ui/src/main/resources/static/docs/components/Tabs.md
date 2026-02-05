# Tabs

Tabs标签页组件，提供多页面切换功能，支持单选、多选模式，可配置关闭按钮、选项按钮和弹出按钮，具有响应式布局和主题切换功能。

## 类名
`ood.UI.Tabs`

## 继承
`ood.UI`, `ood.absList`, `ood.absValue`

## 快速开始

```html
<!-- 引入组件 -->
<script type="text/javascript" src="ood/UI/Tabs.js"></script>

<!-- 创建标签页容器 -->
<div id="tabs-container"></div>

<script>
var tabs = ood.UI.Tabs({
    width: '40em',
    height: '30em',
    caption: '我的标签页',
    items: [
        {id: 'tab1', caption: '页面1', imageClass: 'ri-home-line'},
        {id: 'tab2', caption: '页面2'},
        {id: 'tab3', caption: '页面3', closeBtn: true, optBtn: 'ood-uicmd-opt', popBtn: true}
    ],
    value: 'tab1',
    autoFontColor: true,
    selMode: 'single',
    noPanel: false,
    noHandler: false
}).appendTo('#tabs-container');
</script>
```

## 属性

### 数据模型属性 (DataModel)

| 属性名 | 类型 | 默认值 | 描述 |
|--------|------|--------|------|
| `rotate` | Number | `null` | 旋转角度 |
| `iconColors` | Array | `null` | 图标颜色数组 |
| `itemColors` | Array | `null` | 项颜色数组 |
| `fontColors` | Array | `null` | 字体颜色数组 |
| `autoFontColor` | Boolean | `false` | 自动字体颜色，根据背景色自动调整字体颜色 |
| `autoIconColor` | Boolean | `true` | 自动图标颜色，根据背景色自动调整图标颜色 |
| `autoItemColor` | Boolean | `false` | 自动项颜色，根据背景色自动调整项颜色 |
| `expression` | String | `''` | 表达式 |
| `selectable` | Boolean | `true` | 是否可选择 |
| `dirtyMark` | Boolean | `false` | 脏标记 |
| `lazyAppend` | Boolean | `true` | 延迟追加，动态渲染子组件 |
| `isFormField` | Boolean | `false` | 是否为表单字段（隐藏属性） |
| `dock` | String | `'fill'` | 停靠方式：'fill', 'left', 'right', 'top', 'bottom' |
| `width` | String | `'18em'` | 组件宽度（带空间单位） |
| `height` | String | `'18em'` | 组件高度（带空间单位） |
| `position` | String | `'absolute'` | 定位方式 |
| `itemWidth` | Number | `0` | 项宽度，为0时自适应 |
| `itemAlign` | String | `''` | 项对齐方式：'', 'left', 'center', 'right' |
| `HAlign` | String | `'left'` | 水平对齐方式：'left', 'center', 'right' |
| `dropKeysPanel` | String | `''` | 面板拖放键 |
| `value` | String | `''` | 当前选中的标签页ID |
| `selMode` | String | `'single'` | 选择模式：'single'（单选）, 'multi'（多选） |
| `noPanel` | Boolean | `false` | 无面板模式，隐藏内容面板 |
| `noHandler` | Boolean | `false` | 无句柄模式，隐藏标签头区域 |
| `tagCmds` | Array | `[]` | 标签命令数组，用于在标签页上添加命令按钮 |

### 初始化属性 (iniProp)

| 属性名 | 类型 | 默认值 | 描述 |
|--------|------|--------|------|
| `items` | Array | 示例数组 | 标签页项数组，每个项包含id、caption、imageClass等属性 |
| `autoFontColor` | Boolean | `true` | 自动字体颜色 |
| `value` | String | `'a'` | 默认选中的标签页ID |
| `caption` | String | `'TABS'` | 组件标题 |

**items项属性：**
- `id` (String): 标签页唯一标识
- `caption` (String): 标签页显示标题
- `imageClass` (String): 图标CSS类，如'ri-image-line'
- `closeBtn` (Boolean): 是否显示关闭按钮
- `optBtn` (String): 选项按钮类，如'ood-uicmd-opt'
- `popBtn` (Boolean): 是否显示弹出按钮

## 方法

### `setTheme(theme)`
设置组件主题。

**参数：**
- `theme` (String): 主题名称，支持'light'（亮色）、'dark'（暗色）、'high-contrast'（高对比度）

**返回：**
- (Object): 组件实例，支持链式调用

**示例：**
```javascript
tabs.setTheme('dark');
```

### `getTheme()`
获取当前主题。

**返回：**
- (String): 当前主题名称

### `toggleDarkMode()`
切换暗黑模式。

**返回：**
- (Object): 组件实例

### `adjustLayout(profile)`
调整响应式布局。

**参数：**
- `profile` (Object): 可选，UI配置对象

**返回：**
- (Object): 组件实例

### `getPanel(subId)`
获取指定标签页的内容面板。

**参数：**
- `subId` (String): 标签页ID

**返回：**
- (Object): 面板DOM对象

### `addPanel(paras, children, item)`
添加新的标签页。

**参数：**
- `paras` (Object): 标签页参数，包含id、caption、closeBtn等属性
- `children` (Array): 子组件数组
- `item` (Object): 参考项，用于插入位置

**返回：**
- (Object): 组件实例

**示例：**
```javascript
tabs.addPanel(
    {id: 'newTab', caption: '新标签页', closeBtn: true},
    [childComponent]
);
```

### `removePanel(domId)`
移除指定标签页。

**参数：**
- `domId` (String): 标签页DOM ID

**返回：**
- (Object): 组件实例

### `getPanelPara(domId)`
获取标签页参数。

**参数：**
- `domId` (String): 标签页DOM ID

**返回：**
- (Object): 标签页参数对象

### `getAllFormValues(isAll)`
获取所有表单值。

**参数：**
- `isAll` (Boolean): 是否获取所有值

**返回：**
- (Object): 表单值对象

### `getPanelChildren(domId)`
获取标签页的子组件。

**参数：**
- `domId` (String): 标签页DOM ID

**返回：**
- (Array): 子组件数组

### `resetPanelView(subId, removeChildren, destroyChildren)`
重置标签页视图。

**参数：**
- `subId` (String|Boolean): 标签页ID或true表示所有标签页
- `removeChildren` (Boolean): 是否移除子组件，默认true
- `destroyChildren` (Boolean): 是否销毁子组件，默认true

**返回：**
- (Object): 组件实例

### `iniPanelView(subId)`
初始化标签页视图。

**参数：**
- `subId` (String): 可选，指定标签页ID，不指定则初始化所有标签页

**返回：**
- (Object): 组件实例

### `fireItemClickEvent(subId)`
触发标签页点击事件。

**参数：**
- `subId` (String): 标签页ID

**返回：**
- (Object): 组件实例

### `removeItems(arr, purgeNow)`
移除标签页项。

**参数：**
- `arr` (Array|String): 要移除的标签页ID数组或字符串（用分隔符分隔）
- `purgeNow` (Boolean): 是否立即清除

**返回：**
- (Object): 组件实例

### `clearItems(purgeNow)`
清除所有标签页项。

**参数：**
- `purgeNow` (Boolean): 是否立即清除

**返回：**
- (Object): 组件实例

### `markItemCaption(subId, mark, force, tag, cls)`
标记标签页标题（如脏标记）。

**参数：**
- `subId` (String): 标签页ID
- `mark` (Boolean): 是否标记
- `force` (Boolean): 是否强制标记
- `tag` (String|Function): 标记文本或函数
- `cls` (String): CSS类名

**返回：**
- (Object): 组件实例

**示例：**
```javascript
// 添加星号标记
tabs.markItemCaption('tab1', true);
// 移除标记
tabs.markItemCaption('tab1', false);
```

### `getCurPanel()`
获取当前选中的面板。

**返回：**
- (Object): 当前面板对象或null

### `autoSave()`
自动保存当前模块。

### `getActiveModule()`
获取当前活动的模块。

**返回：**
- (Object): 模块对象或null

## 事件

### `onCmd(profile, item, cmdkey, e, src)`
命令按钮点击事件。

### `onIniPanelView(profile, item)`
面板视图初始化事件。

### `beforePagePop(profile, item, options, e, src)`
页面弹出前事件。

### `beforePageClose(profile, item, src)`
页面关闭前事件。

### `afterPageClose(profile, item)`
页面关闭后事件。

### `onShowOptions(profile, item, e, src)`
显示选项事件。

### `onItemSelected(profile, item, e, src, type)`
标签页选中事件。

### `onCaptionActive(profile, item, e, src)`
标题激活事件。

### `onClickPanel(profile, item, e, src)`
面板点击事件。

## CSS 变量 (Appearances)

| 类名 | 描述 |
|------|------|
| `KEY` | 组件容器样式 |
| `LIST` | 标签头列表区域样式 |
| `LISTBG` | 列表背景样式 |
| `MENU` | 菜单按钮样式（响应式折叠时显示） |
| `MENU2` | 二级菜单样式 |
| `MENUCAPTION` | 菜单标题样式 |
| `ITEMS` | 标签项容器样式 |
| `ITEM` | 单个标签项样式 |
| `ITEM-checked` | 选中状态的标签项样式 |
| `ITEM-checked:hover` | 选中状态标签项悬停样式 |
| `ITEMI` | 标签项内部容器样式 |
| `ITEMC` | 标签项内容容器样式 |
| `HANDLE` | 标签项句柄样式 |
| `ITEM-checked HANDLE` | 选中状态标签项句柄样式 |
| `PANEL` | 内容面板样式 |
| `CAPTION` | 标题样式 |
| `CMDS` | 命令按钮容器样式 |
| `LTAGCMDS, RTAGCMDS` | 左右标签命令容器样式 |
| `CMD` | 命令按钮样式 |

## 示例

### 基本标签页

```html
<div id="basic-tabs"></div>

<script>
var basicTabs = ood.UI.Tabs({
    width: '500px',
    height: '400px',
    items: [
        {id: 'home', caption: '首页', imageClass: 'ri-home-line'},
        {id: 'profile', caption: '个人资料', imageClass: 'ri-user-line'},
        {id: 'settings', caption: '设置', imageClass: 'ri-settings-line', closeBtn: true}
    ],
    value: 'home'
}).appendTo('#basic-tabs');

// 添加内容到标签页
basicTabs.append(ood('<div>首页内容</div>'), 'home');
basicTabs.append(ood('<div>个人资料内容</div>'), 'profile');
basicTabs.append(ood('<div>设置内容</div>'), 'settings');
</script>
```

### 动态添加标签页

```html
<div id="dynamic-tabs"></div>
<button onclick="addNewTab()">添加标签页</button>

<script>
var dynamicTabs = ood.UI.Tabs({
    width: '600px',
    height: '450px',
    items: [
        {id: 'tab1', caption: '标签1'}
    ],
    value: 'tab1'
}).appendTo('#dynamic-tabs');

function addNewTab() {
    var tabId = 'tab' + (dynamicTabs.getItems().length + 1);
    dynamicTabs.addPanel(
        {id: tabId, caption: '新标签' + (dynamicTabs.getItems().length + 1), closeBtn: true},
        [ood('<div>这是新标签页的内容</div>')]
    );
    dynamicTabs.fireItemClickEvent(tabId);
}
</script>
```

### 多选模式

```html
<div id="multi-tabs"></div>

<script>
var multiTabs = ood.UI.Tabs({
    width: '550px',
    height: '420px',
    items: [
        {id: 'm1', caption: '标签A'},
        {id: 'm2', caption: '标签B'},
        {id: 'm3', caption: '标签C'},
        {id: 'm4', caption: '标签D'}
    ],
    selMode: 'multi',
    noPanel: true, // 多选模式通常不需要内容面板
    value: 'm1,m3' // 初始选中多个
}).appendTo('#multi-tabs');

// 获取选中的标签页
multiTabs.on('change', function() {
    console.log('当前选中:', multiTabs.getUIValue());
});
</script>
```

### 响应式主题切换

```html
<div id="theme-tabs"></div>
<button onclick="toggleTheme()">切换主题</button>

<script>
var themeTabs = ood.UI.Tabs({
    width: '100%',
    height: '500px',
    items: [
        {id: 'page1', caption: '页面1'},
        {id: 'page2', caption: '页面2'},
        {id: 'page3', caption: '页面3'}
    ],
    value: 'page1',
    autoFontColor: true,
    autoIconColor: true
}).appendTo('#theme-tabs');

// 初始设置亮色主题
themeTabs.setTheme('light');

function toggleTheme() {
    themeTabs.toggleDarkMode();
}
</script>
```

## 注意事项

1. **响应式布局**：组件支持响应式设计，在窄屏下会自动切换到图标模式或菜单模式。

2. **主题系统**：支持亮色、暗色、高对比度主题，可通过`setTheme()`方法切换。

3. **选择模式**：
   - `single`（默认）：单选模式，每次只能选择一个标签页
   - `multi`：多选模式，可同时选择多个标签页，值用分隔符连接

4. **延迟渲染**：默认启用`lazyAppend`，标签页内容在首次激活时才会渲染，提高初始加载性能。

5. **动态操作**：支持通过`addPanel()`、`removePanel()`等方法动态添加、删除标签页。

6. **事件处理**：提供完整的事件系统，可监听标签页选择、关闭、弹出等操作。

7. **CSS定制**：通过Appearances定义的CSS类可自定义组件样式。

8. **无障碍支持**：支持键盘导航，可通过Tab键在标签页间切换。

9. **浏览器兼容性**：支持现代浏览器，部分高级功能需要CSS3支持。