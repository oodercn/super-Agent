# 组件概述

A2UI (ood.js) 提供丰富的企业级UI组件库，包含50+个组件，涵盖基础组件、布局组件、数据组件、图表组件和移动端组件。

## 📋 组件分类

### 基础组件
用于构建用户界面的基本元素。

| 组件 | 描述 | 主要特性 |
|------|------|---------|
| **Input** | 输入框组件 | 支持多种类型（文本、密码、邮箱等）、验证、格式化 |
| **ButtonLayout** | 按钮组件 | 多种样式类型、图标支持、点击事件 |
| **CheckBox** | 复选框组件 | 选中/未选中状态、标签支持、分组功能 |
| **RadioBox** | 单选按钮组件 | 单选组、值绑定、事件处理 |
| **Label** | 标签组件 | 文本显示、HTML支持、样式配置 |
| **ProgressBar** | 进度条组件 | 百分比显示、动画效果、自定义颜色 |
| **Slider** | 滑块组件 | 范围选择、值绑定、事件回调 |

### 布局组件
用于组织和管理界面布局。

| 组件 | 描述 | 主要特性 |
|------|------|---------|
| **Layout** | 布局容器 | 多区域布局、嵌套支持、响应式设计 |
| **Panel** | 面板组件 | 标题栏、内容区域、边框样式 |
| **Group** | 分组组件 | 折叠/展开、标题显示、边框样式 |
| **Tabs** | 标签页组件 | 多标签切换、动态添加/删除、事件处理 |
| **Stacks** | 堆栈组件 | 多视图切换、动画效果、历史记录 |
| **PageBar** | 分页组件 | 页码导航、数据绑定、自定义样式 |
| **Dialog** | 对话框组件 | 模态/非模态、拖拽、大小调整 |

### 数据组件
用于展示和操作数据。

| 组件 | 描述 | 主要特性 |
|------|------|---------|
| **List** | 列表组件 | 数据绑定、虚拟滚动、搜索过滤 |
| **Gallery** | 画廊组件 | 图片展示、缩略图、幻灯片模式 |
| **TreeView** | 树形视图组件 | 层次数据、展开/折叠、复选框 |
| **TreeGrid** | 树形表格组件 | 表格+树形、排序、过滤、编辑 |
| **FormLayout** | 表单布局组件 | 多列布局、验证、数据收集 |
| **MFormLayout** | 移动端表单布局 | 响应式设计、触摸优化 |

### 图表组件
用于数据可视化。

| 组件 | 描述 | 许可证 |
|------|------|--------|
| **ECharts** | ECharts图表组件 | Apache License 2.0 |
| **FusionChartsXT** | FusionCharts图表组件 | **商业许可证** |
| **SVGPaper** | SVG绘图组件 | MIT License |

### 移动端组件
专门为移动设备优化的组件。

| 组件 | 描述 | 主要特性 |
|------|------|---------|
| **Mobile.Button** | 移动端按钮 | 触摸反馈、多种样式、图标支持 |
| **Mobile.Input** | 移动端输入框 | 虚拟键盘优化、验证、格式化 |
| **Mobile.List** | 移动端列表 | 下拉刷新、上拉加载、滑动操作 |
| **Mobile.NavBar** | 导航栏组件 | 标题、操作按钮、状态栏适配 |
| **Mobile.TabBar** | 标签栏组件 | 底部导航、徽章显示、图标切换 |
| **Mobile.Drawer** | 抽屉组件 | 侧滑菜单、手势控制、动画效果 |

## 🎯 组件使用模式

### 1. 组件创建
所有组件都通过构造函数创建：

```javascript
var component = new ood.UI.ComponentName({
    // 配置选项
    property1: value1,
    property2: value2,
    // 事件回调
    onEventName: function(profile, event, ...args) {
        // 事件处理逻辑
    }
});
```

### 2. 组件配置
每个组件都支持丰富的配置选项：

```javascript
var button = new ood.UI.ButtonLayout({
    // 基本属性
    text: '保存',
    type: 'primary',
    size: 'large',
    
    // 样式配置
    style: {
        width: '120px',
        height: '40px',
        margin: '10px'
    },
    
    // 图标配置
    icon: 'ri-save-line',
    iconPosition: 'left',
    
    // 状态配置
    disabled: false,
    loading: false,
    
    // 事件回调
    onClick: function(profile, event) {
        console.log('按钮被点击', profile, event);
    }
});
```

### 3. 组件生命周期

#### 初始化阶段
```javascript
var component = new ood.UI.ComponentName(config);
// 组件已创建但未渲染
```

#### 渲染阶段
```javascript
component.render();
// 组件已渲染到DOM
```

#### 更新阶段
```javascript
component.update(config);
// 更新组件配置
```

#### 销毁阶段
```javascript
component.destroy();
// 组件已从DOM移除
```

## 🔌 组件事件系统

### 内置事件
每个组件都提供标准事件：

| 事件 | 描述 | 参数 |
|------|------|------|
| `onClick` | 点击事件 | `(profile, event)` |
| `onChange` | 值改变事件 | `(profile, event, value)` |
| `onFocus` | 获取焦点事件 | `(profile, event)` |
| `onBlur` | 失去焦点事件 | `(profile, event)` |
| `onKeyDown` | 键盘按下事件 | `(profile, event)` |

### 自定义事件
可以触发和监听自定义事件：

```javascript
// 触发事件
component.trigger('customEvent', { data: 'example' });

// 监听事件
component.on('customEvent', function(data) {
    console.log('收到自定义事件:', data);
});

// 移除事件监听
component.off('customEvent', handler);
```

## 🎨 组件样式系统

### CSS类名约定
组件使用BEM命名约定：

```css
.ood-ui-component {}           /* 块 */
.ood-ui-component__element {}   /* 元素 */
.ood-ui-component--modifier {}  /* 修饰符 */
```

### 主题支持
组件支持CSS变量主题：

```css
:root {
    --primary-color: #007AFF;
    --secondary-color: #5856D6;
    --border-radius: 4px;
}
```

### 响应式设计
组件内置响应式支持：

```javascript
var component = new ood.UI.ComponentName({
    responsive: {
        breakpoints: {
            mobile: { maxWidth: 768 },
            tablet: { minWidth: 769, maxWidth: 1024 },
            desktop: { minWidth: 1025 }
        }
    }
});
```

## 📱 移动端组件特性

### 触摸交互
- 触摸反馈效果
- 长按手势支持
- 滑动操作
- 触觉反馈

### 响应式设计
- 屏幕尺寸自适应
- 安全区域适配
- 横竖屏切换

### 性能优化
- 虚拟滚动
- 防抖节流
- 懒加载
- 动画优化

## 🔧 组件开发指南

### 创建新组件
1. 继承合适的基类
2. 实现核心方法
3. 定义模板和样式
4. 添加事件处理

### 组件继承结构
```
ood.UI (基础UI类)
├── ood.UI.BasicComponent (基础组件)
├── ood.UI.LayoutComponent (布局组件)
├── ood.UI.DataComponent (数据组件)
└── ood.Mobile.Base (移动端基础类)
```

### 组件开发规范
1. **命名规范**: 类名使用帕斯卡命名法
2. **方法规范**: 公共方法使用驼峰命名法
3. **事件规范**: 事件名使用 `on` 前缀
4. **配置规范**: 配置对象使用JSON格式

## 📚 组件示例

### 基础示例
```javascript
// 创建表单
var form = new ood.UI.FormLayout({
    title: '用户注册',
    fields: [
        {
            type: 'input',
            label: '用户名',
            name: 'username',
            required: true
        },
        {
            type: 'password',
            label: '密码',
            name: 'password',
            required: true
        }
    ],
    onSubmit: function(data) {
        console.log('表单数据:', data);
    }
});
```

### 数据绑定示例
```javascript
// 创建列表
var list = new ood.UI.List({
    data: [
        { id: 1, name: '项目1', status: 'active' },
        { id: 2, name: '项目2', status: 'completed' }
    ],
    template: function(item) {
        return '<div>' + item.name + ' - ' + item.status + '</div>';
    },
    onItemClick: function(item, index) {
        console.log('点击项目:', item);
    }
});
```

## 🔗 相关资源

- [组件API参考](./api-reference.md)
- [主题系统指南](./themes.md)
- [移动端组件文档](./mobile-components.md)
- [示例代码](../examples/)

## 🆘 组件问题排查

### 常见问题
1. **组件不显示**: 检查容器元素和 `render()` 调用
2. **样式异常**: 验证CSS引用和类名
3. **事件不触发**: 确认事件绑定时机和名称
4. **性能问题**: 启用虚拟滚动和懒加载

### 调试技巧
```javascript
// 调试组件状态
console.log('组件状态:', component.getState());

// 检查DOM元素
console.log('组件DOM:', component.getRootNode());

// 验证配置
console.log('组件配置:', component.getConfig());
```

---

这份文档提供了A2UI组件库的全面概述。有关每个组件的详细用法，请参考具体的组件文档。