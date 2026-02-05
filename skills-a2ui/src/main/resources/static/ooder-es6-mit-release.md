# ooder - 现代化ES6模块版本正式发布（MIT许可证）

我们很高兴地宣布，ooder现代化ES6模块版本正式发布，采用MIT许可证，为现代Web应用提供了全面的UI组件库支持。

## 🎉 发布亮点

### 现代化架构设计

ooder ES6版本采用了"ES6模块优先"的设计理念，同时保持与传统`ood.Class`系统的完美兼容，让开发者可以渐进式地迁移现有项目。

- **ES6原生支持**：提供干净的导入语法，支持现代打包工具（Webpack、Rollup）的tree shaking
- **无缝迁移路径**：传统代码无需大规模修改即可与ES6模块共存
- **模块化设计**：核心功能拆分为独立模块，按需加载

### 丰富的组件生态

ooder包含60+精心设计的UI组件，覆盖了Web应用开发的各种场景：

- **基础组件**：Button、Input、Label、CheckBox、RadioBox等
- **布局组件**：Layout、Panel、Tabs、FoldingTabs、Stacks等
- **数据展示**：List、TreeGrid、TreeView、Gallery、FileUpload等
- **交互组件**：Dialog、MenuBar、ToolBar、PopMenu、ProgressBar等
- **表单组件**：FormLayout、DatePicker、TimePicker、ColorPicker等
- **多媒体组件**：Audio、Video、Camera、SVGPaper等

### 响应式设计与主题系统

- **移动优先**：所有组件均采用响应式设计，完美适配各种设备尺寸
- **主题支持**：提供丰富的主题系统，包括亮色、暗色、高对比度和紫色主题
- **CSS自定义属性**：基于CSS变量的主题定制，支持动态切换

## 🚀 快速开始

### ES6模块使用

```javascript
import { Button, Input, Dialog } from 'ooder';

// 创建按钮
const btn = new Button({
  text: '点击我',
  onClick: () => console.log('按钮被点击！')
});

// 创建输入框
const input = new Input({
  placeholder: '请输入文本...',
  value: ''
});

// 创建对话框
const dialog = new Dialog({
  title: '欢迎',
  content: '来自ooder的问候！',
  buttons: ['确定', '取消']
});
```

### 传统兼容模式

```javascript
// 传统代码仍然可以正常工作
const btn = new ood.UI.Button({
  text: '传统按钮'
});
```

## 📦 安装方式

### NPM
```bash
npm install ooder
```

### Yarn
```bash
yarn add ooder
```

### CDN
```html
<!-- 开发版本 -->
<script src="https://unpkg.com/ooder/dist/ood-compat.js"></script>

<!-- 生产版本 -->
<script src="https://unpkg.com/ooder/dist/ood-compat.min.js"></script>
```

## 🔧 构建与开发

### 开发服务器
```bash
npm run dev
# 访问 http://localhost:8080
```

### 生产构建
```bash
npm run build
# 输出到 dist/ 文件夹
```

### 代码质量检查
```bash
npm run lint              # ESLint检查
npm run lint:fix          # ESLint自动修复
npm run test              # 运行测试套件
npm run test:coverage     # 生成覆盖率报告
```

## 📚 完整的文档体系

ooder提供了全面的文档支持，帮助开发者快速上手和深入学习：

- **[API参考](./docs/api/)** - 完整的API文档
- **[迁移指南](./README-ES6-UPGRADE.md)** - 从传统版本迁移到ES6模块
- **[组件指南](./docs/guides/)** - 各组件的使用示例
- **[测试指南](./TEST-GUIDE.md)** - 组件测试方法
- **[AI辅助学习](./docs/ai-learning/)** - 使用AI学习框架

## 🤝 贡献指南

我们欢迎社区贡献！在开始之前，请阅读我们的[贡献指南](./CONTRIBUTING.md)。

### 贡献方式
- **Bug报告**：提交详细的问题复现步骤
- **功能请求**：建议新功能或改进
- **代码贡献**：提交bug修复或新功能的Pull Request
- **文档完善**：帮助改进文档、添加示例或翻译
- **测试**：编写测试用例，提高测试覆盖率

## 📄 MIT许可证

ooder采用MIT许可证，允许自由使用、修改和分发，无论是商业还是非商业项目。

```
MIT License

Copyright (c) 2026 ooder Contributors

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.
```

## 🚀 未来规划

### 短期目标
- 完成核心UI组件向ES6模块的迁移
- 提高测试覆盖率至90%以上
- 完善文档，添加更多示例
- 为所有组件提供TypeScript类型定义

### 长期愿景
- 全面TypeScript迁移
- Web Components兼容性
- 框架无关的组件架构
- 增强可访问性（a11y）功能

## 🙏 致谢

感谢所有为ooder项目做出贡献的开发者，以及现代Web标准的推动者，正是你们的努力让这个项目成为可能。

---

**需要帮助？** 查看我们的[故障排除指南](./docs/troubleshooting.md)或[提交issue](https://gitee.com/ooderCN/oodui-es6/issues)。

**发现Bug？** 请使用我们的[Bug报告模板](./.github/ISSUE_TEMPLATE/bug_report.md)进行报告。

**有功能请求？** 使用我们的[功能请求模板](./.github/ISSUE_TEMPLATE/feature_request.md)。

让我们一起构建更好的Web UI库！

---

**版本信息**：ooder v0.5.0 | 发布日期：2026-01-04 | 许可证：MIT