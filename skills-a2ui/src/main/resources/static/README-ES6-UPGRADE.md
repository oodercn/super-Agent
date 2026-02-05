# 从遗留OOD迁移到ES6模块

本指南将帮助您将现有的OOD应用程序迁移到使用现代ES6模块系统。

## 📋 目录

- [介绍](#介绍)
- [迁移策略](#迁移策略)
- [分步迁移](#分步迁移)
- [常见模式](#常见模式)
- [迁移示例](#迁移示例)
- [故障排除](#故障排除)

## 📖 介绍

ooder ES6模块版本提供了一种现代的方式来使用OOD组件，同时保持与遗留代码的向后兼容性。这允许您逐步迁移应用程序。

### 迁移的主要好处

- **更好的性能**：ES6模块支持tree shaking，减少捆绑大小
- **现代开发**：更简洁的语法和更好的工具支持
- **提高可维护性**：更好的代码组织和封装
- **面向未来**：与现代Web标准对齐

## 🛠️ 迁移策略

我们建议采用**渐进式迁移**方法：

1. **从新代码开始**：对所有新组件和功能使用ES6模块
2. **迁移关键组件**：逐步迁移您最常用的组件
3. **更新依赖**：确保所有依赖项支持ES6模块
4. **彻底测试**：在每个迁移步骤后运行测试
5. **完成迁移**：当所有组件都迁移完成后，移除遗留代码

## 🔍 分步迁移

### 1. 更新构建系统

如果您使用Webpack或Rollup等构建工具，请更新您的配置以支持ES6模块：

```javascript
// webpack.config.js示例
module.exports = {
  mode: 'development',
  entry: './src/index.js',
  output: {
    filename: 'bundle.js',
    path: path.resolve(__dirname, 'dist'),
  },
  module: {
    rules: [
      {
        test: /\.js$/,
        exclude: /node_modules/,
        use: {
          loader: 'babel-loader',
          options: {
            presets: ['@babel/preset-env'],
            plugins: ['@babel/plugin-proposal-class-properties']
          }
        }
      }
    ]
  }
};
```

### 2. 配置HTML

更新您的HTML以使用ES6模块：

```html
<!-- 遗留方法 -->
<script src="ood/js/ood.js"></script>
<script src="your-app.js"></script>

<!-- 现代ES6方法 -->
<script type="module" src="your-app.js"></script>
```

### 3. 迁移简单组件

让我们将一个简单的组件从遗留OOD迁移到ES6模块：

#### 遗留组件

```javascript
// 遗留组件定义
ood.Class({
  $name: 'MyLegacyComponent',
  $extends: ood.UI.Component,
  
  init: function(options) {
    this.$super(options);
    this.text = options.text || 'Default Text';
  },
  
  render: function() {
    return '<div>' + this.text + '</div>';
  }
});
```

#### ES6模块组件

```javascript
// ES6模块组件
import { register } from '../es6/shim.js';
import { ood } from '../es6/shim.js';

@register('MyComponent')
export class MyComponent {
  constructor(options) {
    this.text = options.text || 'Default Text';
  }
  
  render() {
    return `<div>${this.text}</div>`;
  }
}
```

### 4. 使用迁移后的组件

```javascript
// 遗留用法
const legacyComponent = new ood.MyLegacyComponent({ text: 'Hello' });

// ES6模块用法
import { MyComponent } from './MyComponent.js';
const component = new MyComponent({ text: 'Hello' });
```

## 🔤 常见模式

### 导出组件

```javascript
// 命名导出（推荐）
export class MyComponent {
  // ...
}

// 默认导出
export default class MyComponent {
  // ...
}
```

### 导入组件

```javascript
// 导入命名导出
import { MyComponent } from './MyComponent.js';

// 导入默认导出
import MyComponent from './MyComponent.js';

// 导入多个组件
import { Component1, Component2 } from './components.js';
```

### 全局注册组件

```javascript
import { register } from '../es6/shim.js';

@register('GlobalComponent')
export class GlobalComponent {
  // ...
}

// 可以这样使用：
// import { GlobalComponent } from './GlobalComponent.js';
// 或
// const component = new ood.GlobalComponent();
```

## 📊 迁移示例

### 迁移Cookies模块

查看[迁移后的Cookies模块](./ood/modules/Cookies.js)以获取真实世界的示例。

### 迁移UI组件

对于UI组件，您可以使用`adaptES6Class`辅助函数：

```javascript
import { adaptES6Class } from '../es6/module-bridge.js';

class MyUIComponent {
  // ...
}

// 使其与遗留OOD UI系统兼容
adaptES6Class(MyUIComponent, ood.UI.Component);
```

## ❓ 故障排除

### 常见问题

1. **模块未找到错误**
   - 检查导入路径
   - 确保文件扩展名正确
   - 验证您的构建系统配置

2. **遗留代码冲突**
   - 使用兼容层
   - 避免在同一文件中混合遗留代码和现代代码
   - 在每个迁移步骤后彻底测试

3. **性能问题**
   - 确保启用了tree shaking
   - 优化您的构建配置
   - 对大型应用程序使用代码分割

### 调试提示

- 使用浏览器开发者工具调试模块加载
- 检查控制台中的导入/导出错误
- 使用源映射获得更好的调试体验
- 经常运行测试

## 🚀 下一步

- [阅读API参考](./docs/api/)
- [查看组件指南](./docs/guides/)
- [了解AI辅助学习](./docs/ai-learning/)

## 🤝 获取帮助

- [提交issue](https://gitee.com/ooderCN/oodui-es6/issues)
- [阅读文档](./docs/)
- [加入社区](#)（即将推出）

---

**迁移愉快！** 🎉