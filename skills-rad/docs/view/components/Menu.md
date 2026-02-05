# Menu 组件

## 1. 组件概述
Menu 组件是 ooder-a2ui 前端框架中的一个导航菜单组件，用于构建应用程序的导航系统，支持水平和垂直两种方向，以及多级嵌套菜单。

### 1.1 核心功能
- 支持水平和垂直两种布局方向
- 支持多级嵌套菜单
- 支持折叠/展开功能
- 支持选中状态和高亮显示
- 支持自定义图标和样式
- 支持快捷键导航
- 支持主题定制
- 支持响应式设计

### 1.2 适用场景
- 应用程序的主导航菜单
- 侧边栏导航
- 顶部导航栏
- 下拉菜单
- 上下文菜单
- 多级分类菜单

## 2. 创建方法

### 2.1 JSON 方式创建
```json
{
  "id": "menu1",
  "type": "Menu",
  "props": {
    "mode": "vertical",
    "selectedKeys": ["home"],
    "defaultOpenKeys": ["submenu1"],
    "style": {
      "width": 200
    }
  },
  "children": [
    {
      "id": "menuItem1",
      "type": "MenuItem",
      "props": {
        "key": "home",
        "text": "首页",
        "icon": "home"
      }
    },
    {
      "id": "submenu1",
      "type": "SubMenu",
      "props": {
        "key": "submenu1",
        "text": "菜单组1",
        "icon": "appstore"
      },
      "children": [
        {
          "id": "menuItem2",
          "type": "MenuItem",
          "props": {
            "key": "item1",
            "text": "子菜单1"
          }
        },
        {
          "id": "menuItem3",
          "type": "MenuItem",
          "props": {
            "key": "item2",
            "text": "子菜单2"
          }
        }
      ]
    }
  ]
}
```

### 2.2 JavaScript 方式创建
```javascript
const menu = ood.create("Menu", {
  id: "menu1",
  mode: "horizontal",
  selectedKeys: ["home"],
  defaultOpenKeys: [],
  style: {
    backgroundColor: "#1890ff",
    color: "#ffffff"
  }
});

// 创建菜单项
const homeItem = ood.create("MenuItem", {
  key: "home",
  text: "首页",
  icon: "home"
});

const aboutItem = ood.create("MenuItem", {
  key: "about",
  text: "关于我们"
});

// 创建子菜单
const productSubMenu = ood.create("SubMenu", {
  key: "products",
  text: "产品中心",
  icon: "appstore"
});

// 创建子菜单的菜单项
const product1Item = ood.create("MenuItem", {
  key: "product1",
  text: "产品1"
});

const product2Item = ood.create("MenuItem", {
  key: "product2",
  text: "产品2"
});

// 添加子菜单项到子菜单
productSubMenu.addChild(product1Item);
productSubMenu.addChild(product2Item);

// 添加菜单项到菜单
menu.addChild(homeItem);
menu.addChild(aboutItem);
menu.addChild(productSubMenu);

// 添加到页面
page.addChild(menu);

// 监听菜单选择事件
menu.on("onSelect", (selectedKey, item) => {
  console.log("选中的菜单项:", selectedKey);
  console.log("菜单项信息:", item);
});

// 监听子菜单展开/折叠事件
menu.on("onOpenChange", (openKeys) => {
  console.log("展开的子菜单:", openKeys);
});
```

## 3. 属性列表

| 属性名 | 类型 | 默认值 | 说明 |
|--------|------|--------|------|
| **mode** | string | "vertical" | 菜单模式："vertical"（垂直）或 "horizontal"（水平） |
| **selectedKeys** | array | [] | 当前选中的菜单项 key 数组 |
| **defaultSelectedKeys** | array | [] | 默认选中的菜单项 key 数组 |
| **openKeys** | array | [] | 当前展开的子菜单 key 数组 |
| **defaultOpenKeys** | array | [] | 默认展开的子菜单 key 数组 |
| **theme** | string | "light" | 主题："light" 或 "dark" |
| **style** | object | {} | 菜单样式 |
| **inlineIndent** | number | 24 | 垂直菜单的子菜单缩进距离 |
| **triggerSubMenuAction** | string | "hover" | 子菜单触发方式："hover" 或 "click" |
| **overflowedIndicator** | string/object | "more" | 水平菜单溢出时的指示符 |
| **className** | string | "" | 自定义CSS类名 |

## 4. MenuItem 属性

| 属性名 | 类型 | 默认值 | 说明 |
|--------|------|--------|------|
| **key** | string | - | 菜单项唯一标识 |
| **text** | string | - | 菜单项文本 |
| **icon** | string | - | 菜单项图标 |
| **disabled** | boolean | false | 是否禁用 |
| **href** | string | - | 菜单项链接地址 |
| **target** | string | - | 链接打开方式 |
| **style** | object | {} | 菜单项样式 |
| **className** | string | - | 自定义CSS类名 |

## 5. SubMenu 属性

| 属性名 | 类型 | 默认值 | 说明 |
|--------|------|--------|------|
| **key** | string | - | 子菜单唯一标识 |
| **text** | string | - | 子菜单文本 |
| **icon** | string | - | 子菜单图标 |
| **disabled** | boolean | false | 是否禁用 |
| **style** | object | {} | 子菜单样式 |
| **className** | string | - | 自定义CSS类名 |

## 6. 方法列表

| 方法名 | 签名 | 说明 |
|--------|------|------|
| **setSelectedKeys** | `setSelectedKeys(keys)` | 设置当前选中的菜单项 |
| **getSelectedKeys** | `getSelectedKeys()` | 获取当前选中的菜单项 |
| **setOpenKeys** | `setOpenKeys(keys)` | 设置当前展开的子菜单 |
| **getOpenKeys** | `getOpenKeys()` | 获取当前展开的子菜单 |
| **openSubMenu** | `openSubMenu(key)` | 展开指定子菜单 |
| **closeSubMenu** | `closeSubMenu(key)` | 关闭指定子菜单 |
| **toggleSubMenu** | `toggleSubMenu(key)` | 切换子菜单展开/折叠状态 |
| **addItem** | `addItem(item)` | 添加菜单项 |
| **removeItem** | `removeItem(key)` | 移除指定菜单项 |
| **updateItem** | `updateItem(key, props)` | 更新指定菜单项 |

## 7. 事件处理

| 事件名 | 说明 | 回调参数 |
|--------|------|----------|
| **onSelect** | 选择菜单项时触发 | `(selectedKey, item)` |
| **onOpenChange** | 子菜单展开/折叠时触发 | `openKeys` |
| **onClick** | 点击菜单项时触发 | `(key, item, event)` |
| **onMouseEnter** | 鼠标进入菜单项时触发 | `(key, item, event)` |
| **onMouseLeave** | 鼠标离开菜单项时触发 | `(key, item, event)` |

## 8. 示例代码

### 8.1 垂直菜单
```json
{
  "id": "verticalMenu",
  "type": "Menu",
  "props": {
    "mode": "vertical",
    "selectedKeys": ["item1"],
    "defaultOpenKeys": ["submenu1"],
    "style": {
      "width": 200,
      "borderRight": "1px solid #e8e8e8"
    }
  },
  "children": [
    {
      "type": "MenuItem",
      "props": {
        "key": "item1",
        "text": "菜单项1",
        "icon": "file"
      }
    },
    {
      "type": "SubMenu",
      "props": {
        "key": "submenu1",
        "text": "子菜单1",
        "icon": "folder"
      },
      "children": [
        {
          "type": "MenuItem",
          "props": {
            "key": "subitem1",
            "text": "子菜单项1"
          }
        },
        {
          "type": "MenuItem",
          "props": {
            "key": "subitem2",
            "text": "子菜单项2"
          }
        }
      ]
    },
    {
      "type": "MenuItem",
      "props": {
        "key": "item2",
        "text": "菜单项2",
        "icon": "setting"
      }
    }
  ]
}
```

### 8.2 水平菜单
```javascript
const horizontalMenu = ood.create("Menu", {
  id: "horizontalMenu",
  mode: "horizontal",
  selectedKeys: ["home"],
  theme: "dark",
  style: {
    backgroundColor: "#001529",
    color: "#ffffff"
  }
});

// 创建菜单项
const homeItem = ood.create("MenuItem", {
  key: "home",
  text: "首页",
  icon: "home"
});

const aboutItem = ood.create("MenuItem", {
  key: "about",
  text: "关于我们"
});

const servicesItem = ood.create("MenuItem", {
  key: "services",
  text: "服务"
});

const contactItem = ood.create("MenuItem", {
  key: "contact",
  text: "联系我们"
});

// 添加菜单项
horizontalMenu.addChild(homeItem);
horizontalMenu.addChild(aboutItem);
horizontalMenu.addChild(servicesItem);
horizontalMenu.addChild(contactItem);

// 添加到页面顶部
page.addChild(horizontalMenu);
```

### 8.3 带图标的菜单
```json
{
  "id": "iconMenu",
  "type": "Menu",
  "props": {
    "mode": "vertical",
    "selectedKeys": ["dashboard"],
    "style": {
      "width": 64,
      "backgroundColor": "#001529",
      "color": "#ffffff"
    }
  },
  "children": [
    {
      "type": "MenuItem",
      "props": {
        "key": "dashboard",
        "text": "仪表盘",
        "icon": "dashboard"
      }
    },
    {
      "type": "MenuItem",
      "props": {
        "key": "user",
        "text": "用户管理",
        "icon": "user"
      }
    },
    {
      "type": "MenuItem",
      "props": {
        "key": "setting",
        "text": "设置",
        "icon": "setting"
      }
    }
  ]
}
```

### 8.4 多级嵌套菜单
```javascript
const nestedMenu = ood.create("Menu", {
  id: "nestedMenu",
  mode: "vertical",
  selectedKeys: ["level3"],
  defaultOpenKeys: ["level1", "level2"],
  style: {
    width: 250,
    borderRight: "1px solid #e8e8e8"
  }
});

// 创建三级嵌套菜单
const level1Menu = ood.create("SubMenu", {
  key: "level1",
  text: "一级菜单",
  icon: "appstore"
});

const level2Menu = ood.create("SubMenu", {
  key: "level2",
  text: "二级菜单"
});

const level3Menu = ood.create("MenuItem", {
  key: "level3",
  text: "三级菜单项"
});

// 组装菜单
level2Menu.addChild(level3Menu);
level1Menu.addChild(level2Menu);
nestedMenu.addChild(level1Menu);

// 添加到页面
page.addChild(nestedMenu);
```

## 9. 最佳实践

### 9.1 菜单设计
- 保持菜单结构的简洁性，避免过深的嵌套层级
- 对于重要的导航项，放在菜单的前面位置
- 合理使用图标，增强菜单的视觉识别性
- 对于长文本菜单项，考虑使用省略号或缩短文本

### 9.2 交互设计
- 提供清晰的选中状态反馈
- 合理设置子菜单的触发方式
- 对于水平菜单，考虑添加溢出处理
- 支持键盘导航，提升可访问性

### 9.3 性能优化
- 避免过多的菜单项，影响渲染性能
- 对于动态菜单，考虑使用懒加载
- 合理使用 selectedKeys 和 openKeys，避免不必要的重渲染
- 对于复杂菜单，考虑使用虚拟滚动技术

### 9.4 响应式设计
- 对于水平菜单，在小屏幕上考虑转为垂直菜单
- 合理设置菜单宽度，适应不同屏幕尺寸
- 考虑添加折叠/展开功能，适应移动端

### 9.5 可访问性
- 确保菜单支持键盘导航
- 提供清晰的 ARIA 属性
- 支持屏幕阅读器
- 提供足够的颜色对比度
- 为菜单项添加适当的焦点样式

## 10. 常见问题与解决方案

### 10.1 菜单项选中状态不更新
**问题**：点击菜单项后，选中状态不更新
**解决方案**：确保正确配置了 key 属性，且监听了 onSelect 事件，在事件中更新 selectedKeys

### 10.2 子菜单不展开
**问题**：鼠标悬停或点击子菜单时，子菜单不展开
**解决方案**：检查子菜单是否正确配置了 key 属性，且 triggerSubMenuAction 配置正确

### 10.3 水平菜单溢出
**问题**：水平菜单的菜单项过多，溢出容器
**解决方案**：使用 overflowedIndicator 属性，或在小屏幕上转为垂直菜单

### 10.4 菜单样式自定义困难
**问题**：难以自定义菜单的样式
**解决方案**：使用 className 属性添加自定义 CSS 类，或使用 style 属性直接设置样式

### 10.5 嵌套菜单性能问题
**问题**：嵌套层级过深，导致菜单渲染性能下降
**解决方案**：简化菜单结构，减少嵌套层级，或使用懒加载

## 11. 浏览器兼容性

| 浏览器 | 支持版本 | 注意事项 |
|--------|----------|----------|
| Chrome | 60+ | 完全支持 |
| Firefox | 55+ | 完全支持 |
| Safari | 12+ | 完全支持 |
| Edge | 79+ | 完全支持 |
| IE11 | 不支持 | 建议使用其他菜单方案 |

## 12. 相关组件

- **Tab**：标签页组件，用于页面内导航
- **Breadcrumb**：面包屑组件，用于显示当前位置
- **Dropdown**：下拉菜单组件
- **Icon**：图标组件，用于菜单图标
- **Layout**：布局组件，用于菜单容器

## 13. 升级与更新历史

| 版本 | 更新内容 | 日期 |
|------|----------|------|
| 1.0.0 | 初始版本，支持基本菜单功能 | 2025-01-15 |
| 1.1.0 | 新增水平菜单和嵌套菜单支持 | 2025-03-20 |
| 1.2.0 | 新增主题切换功能 | 2025-05-10 |
| 1.3.0 | 增强键盘导航支持 | 2025-07-05 |
| 1.4.0 | 优化响应式设计 | 2025-09-15 |
| 1.5.0 | 支持虚拟滚动 | 2025-11-20 |

## 14. 扩展阅读

- [CSS 导航菜单设计指南](https://developer.mozilla.org/zh-CN/docs/Web/CSS/CSS_Flexible_Box_Layout/Basic_Concepts_of_Flexbox)
- [响应式导航设计最佳实践](https://web.dev/responsive-navigation-patterns/)
- [ooder-a2ui 组件库介绍](FRONTEND_COMPONENTS.md)

## 15. 贡献与反馈

如有任何问题或建议，请通过以下方式反馈：
- 提交Issue：[GitHub Issues](https://github.com/ooder/ooder-pro/issues)
- 邮件反馈：support@ooder.com
- 社区论坛：[ooder社区](https://community.ooder.com)

---

**最后更新时间**：2026-01-25  
**文档版本**：1.0  
**作者**：ooder-a2ui 开发团队