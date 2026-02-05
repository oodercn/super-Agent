# Image 组件

## 概述

Image 是一个功能丰富的图片显示组件，支持单张图片、图片集合、响应式布局、主题切换和可访问性增强。它提供了现代化的图片展示功能，包括懒加载、错误处理、交互效果等，适用于相册、产品展示、媒体库等多种场景。

## 类名

- **完整类名**: `ood.UI.Image`
- **继承自**: `ood.UI`
- **模板标签**: `img`

## 快速开始

```javascript
// 创建单张图片
var image = ood.UI.Image({
    src: 'images/photo.jpg',
    alt: '美丽风景',
    width: 400,
    height: 300,
    theme: 'dark'
}).appendTo('#container');

// 创建图片集合（相册）
var gallery = ood.UI.Image({
    items: [
        { id: '1', image: 'images/photo1.jpg', alt: '风景1', tips: '点击查看大图' },
        { id: '2', image: 'images/photo2.jpg', alt: '风景2', tips: '点击查看大图' },
        { id: '3', image: 'images/photo3.jpg', alt: '风景3', tips: '点击查看大图' }
    ],
    activeItem: '1',
    responsive: true
}).appendTo('#gallery');

// 切换图片
gallery.setActiveItem('2');
```

## API 参考

### iniProp 属性

| 属性 | 类型 | 默认值 | 描述 |
|------|------|--------|------|
| `src` | String | `"ri-image-line"` | 图标字体资源，用于默认图片 |
| `IconFontSize` | String | `'3em'` | 图标字体大小 |

### DataModel 属性

#### 现代化功能属性
| 属性 | 类型 | 默认值 | 描述 |
|------|------|--------|------|
| `theme` | String | `'light'` | 主题模式：`'light'`, `'dark'`, `'highcontrast'` |
| `responsive` | Boolean | `true` | 是否启用响应式布局 |
| `responsiveBreakpoint` | String | `'md'` | 响应式断点：`'sm'`, `'md'`, `'lg'`, `'xl'` |
| `ariaLabel` | String | `''` | ARIA 标签，用于辅助技术 |
| `tabIndex` | String | `'0'` | Tab 键顺序 |

#### 图片显示属性
| 属性 | 类型 | 默认值 | 描述 |
|------|------|--------|------|
| `src` | String | `ood.ini.img_bg` | 图片源地址 |
| `alt` | String | `""` | 图片替代文本 |
| `width` | String | `'auto'` | 图片宽度，支持 px、em、百分比 |
| `height` | String | `'auto'` | 图片高度，支持 px、em、百分比 |
| `maxWidth` | Number | `800` | 图片最大宽度限制 |
| `maxHeight` | Number | `600` | 图片最大高度限制 |
| `cursor` | String | `'auto'` | 鼠标指针样式，支持多种预设值 |

#### 图片集合属性
| 属性 | 类型 | 默认值 | 描述 |
|------|------|--------|------|
| `items` | Array | `[]` | 图片集合数组，每个元素包含 `id`, `image`, `alt`, `tips` |
| `activeItem` | String | `""` | 当前激活的图片 ID |

#### 通用属性
| 属性 | 类型 | 默认值 | 描述 |
|------|------|--------|------|
| `expression` | String | `''` | 值表达式，支持动态计算 |

### 实例方法

#### 图片控制
| 方法 | 参数 | 返回 | 描述 |
|------|------|------|------|
| `fireClickEvent()` | 无 | `this` | 触发点击事件 |
| `getRate()` | 无 | Number | 获取图片缩放比例 |
| `getUIValue()` | 无 | String | 获取当前图片源地址 |
| `setUIValue(src)` | `src`: String | 无 | 设置图片源地址 |
| `getValue()` | 无 | String | 获取当前图片源地址 |
| `setValue(src)` | `src`: String | 无 | 设置图片源地址 |

#### 主题与样式
| 方法 | 参数 | 返回 | 描述 |
|------|------|------|------|
| `setTheme(theme)` | `theme`: String | `this` | 设置主题：`'light'`, `'dark'`, `'highcontrast'` |
| `getTheme()` | 无 | String | 获取当前主题 |
| `toggleDarkMode()` | 无 | `this` | 切换暗黑模式 |

#### 响应式布局
| 方法 | 参数 | 返回 | 描述 |
|------|------|------|------|
| `setResponsiveBreakpoint(breakpoint)` | `breakpoint`: String | `this` | 设置响应式断点 |
| `adjustLayout()` | 无 | `this` | 根据屏幕尺寸调整布局 |

#### 可访问性增强
| 方法 | 参数 | 返回 | 描述 |
|------|------|------|------|
| `enhanceAccessibility()` | 无 | `this` | 增强可访问性支持 |
| `enableKeyboardNavigation()` | 无 | `this` | 启用键盘导航 |
| `setScreenReaderLabel(label)` | `label`: String | `this` | 设置屏幕阅读器标签 |

#### 图片集合管理
| 方法 | 参数 | 返回 | 描述 |
|------|------|------|------|
| `setActiveItem(itemId)` | `itemId`: String | 无 | 设置激活的图片项 |

### 静态方法

#### `_adjust(profile, width, height)`
调整图片尺寸，内部使用。

**参数**:
- `profile` (Object): 配置对象
- `width` (Number): 原始宽度
- `height` (Number): 原始高度

**返回**: Array - 调整后的 [宽度, 高度]

### 事件

Image 组件支持以下事件：

#### 图片加载事件
| 事件 | 参数 | 描述 |
|------|------|------|
| `beforeLoad` | `profile` | 图片加载前触发 |
| `afterLoad` | `profile`, `path`, `width`, `height` | 图片加载完成后触发 |

#### 交互事件
| 事件 | 参数 | 描述 |
|------|------|------|
| `onClick` | `profile`, `e`, `src` | 点击图片时触发 |
| `onDblclick` | `profile`, `e`, `src` | 双击图片时触发 |
| `onError` | `profile` | 图片加载错误时触发 |

#### 主题事件
| 事件 | 参数 | 描述 |
|------|------|------|
| `onThemeChange` | `theme` | 主题变化时触发（可通过 profile.onThemeChange 回调） |

## 使用示例

### 示例 1：基本图片显示
```javascript
var profileImage = ood.UI.Image({
    src: 'https://example.com/avatar.jpg',
    alt: '用户头像',
    width: 150,
    height: 150,
    className: 'profile-avatar',
    responsive: true
}).appendTo('#profile');

// 响应式断点配置
profileImage.setResponsiveBreakpoint('lg');
```

### 示例 2：图片相册
```javascript
var photoAlbum = ood.UI.Image({
    items: [
        {
            id: 'vacation1',
            image: 'photos/vacation/day1.jpg',
            alt: '假期第一天',
            tips: '海滩日落'
        },
        {
            id: 'vacation2', 
            image: 'photos/vacation/day2.jpg',
            alt: '假期第二天',
            tips: '登山探险'
        },
        {
            id: 'vacation3',
            image: 'photos/vacation/day3.jpg',
            alt: '假期第三天',
            tips: '城市观光'
        }
    ],
    activeItem: 'vacation1',
    responsive: true,
    theme: 'dark',
    cursor: 'pointer'
}).appendTo('#album');

// 绑定键盘导航
photoAlbum.enableKeyboardNavigation();

// 切换图片
document.getElementById('next-btn').addEventListener('click', function() {
    var currentId = photoAlbum.get(0).properties.activeItem;
    var items = photoAlbum.get(0).properties.items;
    var currentIndex = items.findIndex(item => item.id === currentId);
    var nextIndex = (currentIndex + 1) % items.length;
    photoAlbum.setActiveItem(items[nextIndex].id);
});
```

### 示例 3：增强可访问性
```javascript
var accessibleImage = ood.UI.Image({
    src: 'data/chart.png',
    alt: '2023年销售趋势图',
    ariaLabel: '交互式销售图表，可使用键盘导航',
    tabIndex: '0',
    responsiveBreakpoint: 'md'
}).appendTo('#report');

// 增强可访问性功能
accessibleImage.enhanceAccessibility();
accessibleImage.setScreenReaderLabel('销售趋势图表，显示季度数据变化');
```

### 示例 4：动态主题切换
```javascript
var themableImage = ood.UI.Image({
    src: 'ui/background.jpg',
    responsive: true
}).appendTo('#background');

// 根据系统主题自动切换
if (window.matchMedia && window.matchMedia('(prefers-color-scheme: dark)').matches) {
    themableImage.setTheme('dark');
}

// 添加主题切换按钮
document.getElementById('theme-toggle').addEventListener('click', function() {
    themableImage.toggleDarkMode();
});
```

## 注意事项

1. **图片加载性能**: 对于大量图片或大尺寸图片，建议使用懒加载技术。Image 组件内置了异步加载机制，但应合理设置 `maxWidth` 和 `maxHeight` 以避免内存问题。

2. **响应式设计**: 启用 `responsive: true` 后，组件会根据屏幕尺寸自动调整布局。可以自定义 `responsiveBreakpoint` 来适配不同设备。

3. **可访问性**: 组件提供了完整的 ARIA 支持和键盘导航功能。始终设置 `alt` 属性为图片提供有意义的描述，对于装饰性图片可使用空字符串 `alt=""`。

4. **图片集合**: 使用 `items` 和 `activeItem` 管理图片集合时，确保每个项目有唯一的 `id`。切换激活项会自动更新 `src`、`alt` 和 `tips`。

5. **主题系统**: 主题变化会通过 CSS 类应用样式。确保样式表中定义了对应的主题类（如 `.image-dark`、`.image-highcontrast`）。

6. **错误处理**: 图片加载失败时会触发 `onError` 事件。建议提供备用图片或适当的用户提示。

7. **浏览器兼容性**: 组件已针对主流浏览器优化，包括 IE8+。PNG 图片在旧版 IE 中会自动修复透明度问题。

8. **移动端适配**: 在小屏幕设备上，组件会自动添加 `.image-mobile` 和 `.image-tiny` 类，可以通过 CSS 进一步定制移动端样式。

9. **交互反馈**: 设置 `cursor: 'pointer'` 可以为可点击图片提供视觉反馈。禁用状态下会自动移除交互效果。

10. **数据绑定**: Image 组件完全支持 A2UI 的数据绑定系统，可以与 `DataBinder` 集成实现数据驱动的图片更新。