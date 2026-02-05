# ooder - 发布版本使用指南

本发布版本包含两种构建方式，满足不同项目的需求：

## 1. ES6 模块版本 (现代项目推荐)

### 文件位置
- `es6-modules/` - ES6 模块系统核心文件
- `resources/` - 共享资源文件（CSS、图标、主题等）

### 使用方法

#### 浏览器直接使用
```html
<!-- 使用打包版本 -->
<script src="es6-modules/ood-es6-bundle.js"></script>
<script>
  // ood 对象已挂载到 window
  const dialog = new ood.UI.Dialog({
    title: '提示',
    content: 'Hello, ES6!'
  });
  dialog.show();
</script>
```

#### ES6 模块导入
```javascript
// 现代构建工具（Webpack、Vite、Rollup等）
import { shim, registry, bridge } from './es6-modules/index.js';

// 初始化模块系统
shim.init();
bridge.wrapLegacyClasses();

// 使用模块
const Dialog = ood.UI.Dialog;
```

### 核心文件说明
- `index.js` - ES6 模块主入口
- `shim.js` - 全局对象兼容层
- `module-bridge.js` - 传统-ES6 模块桥接
- `registry.js` - 模块注册表
- `ood-es6-bundle.js` - 完整打包版本

## 2. 传统 JavaScript 版本 (兼容旧项目)

### 文件位置
- `traditional/ood/js/` - 核心 JavaScript 文件
- `traditional/ood/css/` - 样式文件
- `traditional/ood/appearance/` - 主题文件
- `traditional/ood/iconfont/` - 图标字体
- `traditional/ood/Locale/` - 国际化资源

### 使用方法

#### 基本引入
```html
<!-- 引入核心库 -->
<script src="traditional/ood/js/ood.js"></script>
<!-- 引入UI库 -->
<script src="traditional/ood/js/UI.js"></script>
<!-- 引入样式 -->
<link rel="stylesheet" href="traditional/ood/css/default.css">
```

#### 完整功能引入
```html
<!-- 核心库 + UI库 + 扩展模块 -->
<script src="traditional/ood/js/ood-all.js"></script>
<!-- 或使用调试版本 -->
<script src="traditional/ood/js/ood-debug.js"></script>
```

#### 组件使用示例
```javascript
// 创建对话框
const dialog = new ood.UI.Dialog({
  title: '传统版本',
  content: '使用传统JavaScript API',
  width: 400,
  height: 300
});

// 显示对话框
dialog.show();
```

## 3. 共享资源

`resources/` 目录包含两种版本共享的资源文件：

### CSS 样式
- `resources/css/` - 所有CSS文件
- 支持响应式设计和多主题

### 图标字体
- `resources/iconfont/` - 图标字体文件
- 包含丰富的UI图标

### 主题外观
- `resources/appearance/` - 主题系统
  - `light/` - 浅色主题
  - `dark/` - 深色主题
  - `high-contrast/` - 高对比度主题
  - `purple/` - 紫色主题

### 国际化
- `resources/Locale/` - 语言包
  - `en.js` - 英语
  - `cn.js` - 中文

### 图片资源
- `resources/img/` - 图片文件

## 4. 示例页面

`examples/` 目录包含三种示例：

1. `index.html` - 基础示例
2. `index-es6.html` - ES6 模块示例
3. `index-optimized.html` - 优化示例

## 5. 版本选择建议

### 新项目
推荐使用 ES6 模块版本，支持 Tree Shaking，代码分割，现代模块系统。

### 旧项目升级
- 如果项目已经是 ES6 架构，使用 ES6 模块版本
- 如果项目是传统架构，使用传统版本或逐步迁移

### 混合架构
ES6 模块版本包含兼容层，支持与传统代码混合使用。

## 6. 快速开始

### ES6 版本
```bash
# 现代构建工具中直接导入
npm install ooder
# 或直接使用发布文件
```

### 传统版本
```html
<!DOCTYPE html>
<html>
<head>
  <link rel="stylesheet" href="traditional/ood/css/default.css">
</head>
<body>
  <script src="traditional/ood/js/ood.js"></script>
  <script src="traditional/ood/js/UI.js"></script>
  <script>
    // 代码在这里
  </script>
</body>
</html>
```

## 7. 技术支持

- 详细文档: README.md
- 使用指南: USAGE.md
- 变更日志: CHANGELOG.md
- 许可证: LICENSE (MIT)
- 贡献指南: CONTRIBUTING.md
- 行为准则: CODE_OF_CONDUCT.md
- 作者: AUTHORS

## 8. 构建说明

### 传统构建
```bash
# Windows
build\build_ood_release.bat

# 输出到 traditional/ 目录
```

### ES6 构建
```bash
# 使用 Webpack
npm run release:build

# 输出到 es6-modules/ 目录
```

## 9. 兼容性

- ES6 版本：现代浏览器 (Chrome 61+, Firefox 60+, Safari 10.1+, Edge 79+)
- 传统版本：IE 9+, 所有现代浏览器

---

**注意**: 本发布版本已移除所有开发相关文件，仅包含生产代码和必要文档。如需开发工具和完整项目结构，请访问项目仓库。