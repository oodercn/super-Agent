# InfoBlock 组件

## 概述

InfoBlock 是一个信息块列表组件，用于显示图标、标题、描述和标志信息的网格布局。它继承自 List 组件，提供了现代化的信息展示功能，支持主题切换、响应式布局和可访问性增强。适用于仪表盘、设置面板、功能菜单等场景。

## 类名

- **完整类名**: `ood.UI.InfoBlock`
- **别名**: `ood.UI.IconList`（兼容性别名）
- **继承自**: `ood.UI.List`
- **模板结构**: 多层嵌套的 DOM 结构，包含 ITEM、ITEMFRAME、CAPTION、CONTENT、COMMENT、IMAGE、ICON、FLAG、EXTRA 等子节点

## 快速开始

```javascript
// 创建信息块列表
var infoBlock = ood.UI.InfoBlock({
    items: [
        {
            id: 'dashboard',
            caption: '仪表盘',
            comment: '查看统计数据和图表',
            iconFontCode: 'ri-dashboard-line',
            flagText: 'New'
        },
        {
            id: 'settings',
            caption: '设置',
            comment: '系统配置和偏好设置',
            iconFontCode: 'ri-settings-5-line'
        },
        {
            id: 'help',
            caption: '帮助',
            comment: '查看文档和支持',
            iconFontCode: 'ri-question-line'
        }
    ],
    columns: 3,
    responsive: true,
    theme: 'light'
}).appendTo('#container');

// 更新特定项目
infoBlock.updateItemData(infoBlock.get(0), {
    id: 'dashboard',
    caption: '控制面板',
    flagText: 'Updated'
});
```

## API 参考

### iniProp 属性

| 属性 | 类型 | 默认值 | 描述 |
|------|------|--------|------|
| `items` | Array | 包含2个示例项目的数组 | 信息块项目列表 |
| `dock` | String | `'fill'` | 布局停靠方式 |
| `selMode` | String | `'none'` | 选择模式 |
| `borderType` | String | `'none'` | 边框类型 |
| `columns` | Number | `1` | 列数 |

### DataModel 属性

#### 现代化功能属性
| 属性 | 类型 | 默认值 | 描述 |
|------|------|--------|------|
| `theme` | String | `'light'` | 主题模式：`'light'`, `'dark'`, `'highcontrast'` |
| `responsive` | Boolean | `true` | 是否启用响应式布局 |
| `responsiveBreakpoint` | String | `'md'` | 响应式断点：`'sm'`, `'md'`, `'lg'`, `'xl'` |
| `ariaLabel` | String | `''` | ARIA 标签，用于辅助技术 |
| `tabIndex` | String | `'0'` | Tab 键顺序 |

#### 布局与样式属性
| 属性 | 类型 | 默认值 | 描述 |
|------|------|--------|------|
| `bgimg` | String | `null` | 背景图片 URL |
| `iotStatus` | String | `null` | IoT 状态标识 |
| `tagCmds` | String | `null` | 标签命令 |
| `tagCmdsAlign` | String | `null` | 标签命令对齐方式 |
| `autoImgSize` | Boolean | `false` | 是否自动调整图片大小 |
| `autoItemSize` | Boolean | `true` | 是否自动调整项目大小 |
| `iconOnly` | Boolean | `false` | 是否仅显示图标 |
| `iconFontSize` | String | `''` | 图标字体大小 |
| `itemMargin` | Number | `6` | 项目外边距 |
| `itemPadding` | Number | `2` | 项目内边距 |
| `itemWidth` | Number | `32` | 项目宽度（支持空间单位） |
| `itemHeight` | Number | `32` | 项目高度（支持空间单位） |
| `imgWidth` | Number | `16` | 图片宽度 |
| `imgHeight` | Number | `16` | 图片高度 |
| `width` | String | `'16rem'` | 组件宽度 |
| `height` | String | `'16rem'` | 组件高度 |
| `columns` | Number | `0` | 列数（0表示自动） |
| `rows` | Number | `0` | 行数（0表示自动） |

#### 标志与装饰属性
| 属性 | 类型 | 默认值 | 描述 |
|------|------|--------|------|
| `flagText` | String | `null` | 标志文本 |
| `flagClass` | String | `null` | 标志 CSS 类名 |
| `flagStyle` | String | `null` | 标志样式 |

#### 通用属性
| 属性 | 类型 | 默认值 | 描述 |
|------|------|--------|------|
| `expression` | String | `''` | 值表达式，支持动态计算 |

### 实例方法

#### 数据管理
| 方法 | 参数 | 返回 | 描述 |
|------|------|------|------|
| `updateItemData(profile, item)` | `profile`: Object, `item`: Object | 无 | 更新特定项目的数据并刷新显示 |
| `_afterInsertItems(profile)` | `profile`: Object | 无 | 插入项目后的处理逻辑（内部使用） |

#### 主题与样式
| 方法 | 参数 | 返回 | 描述 |
|------|------|------|------|
| `setTheme(theme)` | `theme`: String | `this` | 设置主题：`'light'`, `'dark'`, `'highcontrast'` |
| `getTheme()` | 无 | String | 获取当前主题 |
| `toggleDarkMode()` | 无 | `this` | 切换暗黑模式 |

#### 响应式布局
| 方法 | 参数 | 返回 | 描述 |
|------|------|------|------|
| `adjustLayout()` | 无 | `this` | 根据屏幕尺寸调整布局 |

#### 可访问性增强
| 方法 | 参数 | 返回 | 描述 |
|------|------|------|------|
| `enhanceAccessibility()` | 无 | `this` | 增强可访问性支持 |

#### 初始化方法
| 方法 | 参数 | 返回 | 描述 |
|------|------|------|------|
| `InfoBlockTrigger()` | 无 | 无 | 初始化现代化功能（内部使用） |

### 静态方法

#### `_prepareData(profile)`
准备渲染数据，内部使用。

**参数**:
- `profile` (Object): 配置对象

**返回**: Object - 处理后的数据对象

#### `_prepareItem(profile, item, oitem, pid, index, len)`
准备单个项目的数据，内部使用。

**参数**:
- `profile` (Object): 配置对象
- `item` (Object): 项目数据
- `oitem` (Object): 原始项目数据
- `pid` (String): 父ID
- `index` (Number): 索引
- `len` (Number): 长度

**返回**: 无

### 事件

InfoBlock 支持以下事件：

#### 交互事件
| 事件 | 参数 | 描述 |
|------|------|------|
| `onCmd` | 无 | 命令执行时触发 |
| `onFlagClick` | `profile`, `item`, `e`, `src` | 点击标志时触发 |

#### 触摸事件
| 事件 | 参数 | 描述 |
|------|------|------|
| `touchstart` | `profile`, `item`, `e`, `src` | 触摸开始时触发 |
| `touchmove` | `profile`, `item`, `e`, `src` | 触摸移动时触发 |
| `touchend` | `profile`, `item`, `e`, `src` | 触摸结束时触发 |
| `touchcancel` | `profile`, `item`, `e`, `src` | 触摸取消时触发 |

#### 滑动手势
| 事件 | 参数 | 描述 |
|------|------|------|
| `swipe` | `profile`, `item`, `e`, `src` | 滑动时触发 |
| `swipeleft` | `profile`, `item`, `e`, `src` | 向左滑动时触发 |
| `swiperight` | `profile`, `item`, `e`, `src` | 向右滑动时触发 |
| `swipeup` | `profile`, `item`, `e`, `src` | 向上滑动时触发 |
| `swipedown` | `profile`, `item`, `e`, `src` | 向下滑动时触发 |

#### 按压事件
| 事件 | 参数 | 描述 |
|------|------|------|
| `press` | `profile`, `item`, `e`, `src` | 按压时触发 |
| `pressup` | `profile`, `item`, `e`, `src` | 按压释放时触发 |

## 使用示例

### 示例 1：仪表盘功能菜单
```javascript
var dashboardMenu = ood.UI.InfoBlock({
    items: [
        {
            id: 'analytics',
            caption: '数据分析',
            comment: '查看详细统计报告',
            iconFontCode: 'ri-bar-chart-line',
            iconFontSize: '2.5em',
            itemWidth: 120,
            itemHeight: 120
        },
        {
            id: 'reports',
            caption: '报表',
            comment: '生成和导出报表',
            iconFontCode: 'ri-file-text-line',
            iconFontSize: '2.5em',
            flagText: 'Pro'
        },
        {
            id: 'notifications',
            caption: '通知',
            comment: '系统消息和提醒',
            iconFontCode: 'ri-notification-3-line',
            iconFontSize: '2.5em',
            flagClass: 'ri-star-line'
        }
    ],
    columns: 4,
    responsiveBreakpoint: 'lg',
    theme: 'dark',
    ariaLabel: '仪表板功能菜单'
}).appendTo('#dashboard');

// 启用键盘导航
dashboardMenu.enableKeyboardNavigation();
```

### 示例 2：设置选项面板
```javascript
var settingsPanel = ood.UI.InfoBlock({
    items: [
        {
            id: 'account',
            caption: '账户设置',
            comment: '修改个人信息和密码',
            image: 'icons/account.png',
            autoImgSize: true
        },
        {
            id: 'privacy',
            caption: '隐私设置',
            comment: '管理隐私权限和数据',
            image: 'icons/privacy.png',
            flagText: '重要'
        },
        {
            id: 'notifications',
            caption: '通知设置',
            comment: '配置推送和提醒',
            image: 'icons/bell.png'
        },
        {
            id: 'appearance',
            caption: '外观设置',
            comment: '调整主题和显示',
            iconFontCode: 'ri-palette-line',
            flagClass: 'ri-sun-line'
        }
    ],
    columns: 2,
    responsive: true,
    itemPadding: '12px',
    itemMargin: '8px',
    borderType: 'round'
}).appendTo('#settings');

// 主题切换功能
document.getElementById('theme-switch').addEventListener('change', function(e) {
    settingsPanel.setTheme(e.target.checked ? 'dark' : 'light');
});
```

### 示例 3：产品功能展示
```javascript
var productFeatures = ood.UI.InfoBlock({
    items: [
        {
            id: 'security',
            caption: '高级安全',
            comment: '端到端加密和多因素认证',
            iconFontCode: 'ri-shield-check-line',
            iconFontSize: '3em',
            bgimg: 'gradients/blue.jpg'
        },
        {
            id: 'performance',
            caption: '卓越性能',
            comment: '高速处理和低延迟响应',
            iconFontCode: 'ri-rocket-line',
            iconFontSize: '3em',
            bgimg: 'gradients/purple.jpg'
        },
        {
            id: 'integration',
            caption: '无缝集成',
            comment: '支持第三方服务和 API',
            iconFontCode: 'ri-plug-line',
            iconFontSize: '3em',
            bgimg: 'gradients/green.jpg'
        },
        {
            id: 'support',
            caption: '24/7 支持',
            comment: '专业团队随时提供帮助',
            iconFontCode: 'ri-customer-service-2-line',
            iconFontSize: '3em',
            bgimg: 'gradients/orange.jpg'
        }
    ],
    columns: 2,
    rows: 2,
    width: '100%',
    height: '400px',
    autoItemSize: false,
    itemWidth: '50%',
    itemHeight: '50%'
}).appendTo('#features');
```

## 注意事项

1. **项目数据结构**: 每个项目应包含 `id`、`caption`、`comment` 等字段。支持 `iconFontCode`（图标字体）、`image`（图片URL）或 `icon`（图标URL）三种显示方式。

2. **响应式布局**: 启用响应式后，组件会根据屏幕尺寸自动调整列数和布局。小屏幕设备会添加 `.infoblock-mobile` 和 `.infoblock-tiny` 类。

3. **可访问性**: 组件自动为容器添加 `role="grid"`，为每个项目添加 `role="gridcell"`。确保为重要项目提供有意义的 `aria-label`。

4. **主题系统**: 主题变化通过 CSS 类应用。确保样式表中定义了 `.infoblock-dark`、`.infoblock-highcontrast` 等主题类。

5. **图片加载**: 支持自动图片大小调整（`autoImgSize: true`）。图片加载失败时会显示错误状态，成功加载后隐藏加载指示器。

6. **标志功能**: 通过 `flagText`、`flagClass` 和 `flagStyle` 自定义项目标志。点击标志会触发 `onFlagClick` 事件。

7. **触摸支持**: 完整支持触摸事件和滑动手势，适用于移动端应用。

8. **性能优化**: 对于大量项目，建议合理设置 `columns` 和 `rows`，避免过度渲染。考虑使用虚拟滚动对于超长列表。

9. **兼容性**: 组件已针对主流浏览器优化，包括旧版 IE。图标字体使用 Remix Icon 系统。

10. **数据绑定**: 完全支持 A2UI 数据绑定系统，可以与 `DataBinder` 集成实现动态数据更新。