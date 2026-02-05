# ooder - 现代化ES6模块版本

一个全面的UI组件库，支持完整的ES6模块，专为现代Web应用构建，同时保持与遗留代码库的向后兼容性。

## 🚀 特性

### 现代化架构
- **ES6模块优先**: 原生ES6模块支持，提供清晰的导入语法
- **渐进式迁移**: 从遗留的`ood.Class`系统无缝过渡
- **支持Tree Shaking**: 针对现代打包工具（Webpack、Rollup）进行了优化

### 丰富的组件生态
- **60+ UI组件**: 包括Button、Input、Dialog、TreeGrid、Tabs等
- **响应式设计**: 移动优先，完全响应式的组件
- **主题系统**: 广泛的主题支持，使用CSS自定义属性

### 优秀的开发体验
- **TypeScript就绪**: 提供完整的类型定义
- **全面的文档**: 详细的API参考和使用指南
- **内置测试**: 完整的测试套件，确保质量

## 📦 安装

### NPM
```bash
npm install ooder
```

### Yarn
```bash
yarn add ooder
```

### CDN（全局使用）
```html
<!-- 开发版本 -->
<script src="https://unpkg.com/ooder/dist/ood-compat.js"></script>

<!-- 生产版本 -->
<script src="https://unpkg.com/ooder/dist/ood-compat.min.js"></script>
```

## 🎯 快速开始

### ES6模块使用
```javascript
import { Button, Input, Dialog } from 'ooder';

// 创建按钮
const btn = new Button({
  text: '点击我',
  onClick: () => console.log('按钮被点击!')
});

// 创建输入框
const input = new Input({
  placeholder: '请输入文本...',
  value: ''
});

// 创建对话框
const dialog = new Dialog({
  title: '欢迎',
  content: '来自ooder的问候!',
  buttons: ['确定', '取消']
});
```

### 遗留兼容模式
```javascript
// 仍然可以与遗留代码一起工作
const btn = new ood.UI.Button({
  text: '遗留按钮'
});
```

## 📁 项目结构

```
ooder/
├── ood/                          # 源代码
│   ├── es6/                      # ES6兼容层
│   │   ├── index.js             # 主入口文件
│   │   ├── shim.js              # 全局对象桥接
│   │   └── registry.js          # 模块注册表
│   ├── modules/                  # ES6模块（迁移目标）
│   │   └── Cookies.js           # 迁移示例
│   └── js/                       # 遗留代码（未修改）
│       ├── UI.js                # UI核心
│       └── 36+ modules          # 完整功能
├── dist/                         # 构建输出
│   ├── ood-es6.js               # ES6开发版本
│   └── ood-es6.min.js           # ES6生产版本
├── docs/                         # 文档
├── examples/                     # 使用示例
├── tests/                        # 测试文件
└── package.json                  # 项目配置
```

## 🔧 构建与开发

### 开发服务器
```bash
npm run dev
# 打开 http://localhost:8080
```

### 生产构建
```bash
npm run build
# 输出到 dist/ 文件夹
```

### 代码质量
```bash
npm run lint              # ESLint检查
npm run lint:fix          # ESLint自动修复
npm run test              # 运行测试套件
npm run test:coverage     # 生成覆盖率报告
```

## 📚 文档

### 可用文档
- **[API参考](./docs/api/)** - 完整的API文档
- **[迁移指南](./README-ES6-UPGRADE.md)** - 从遗留版本迁移到ES6模块
- **[组件指南](./docs/guides/)** - 每个组件的使用示例
- **[测试指南](./TEST-GUIDE.md)** - 如何测试组件
- **[AI辅助学习](./docs/ai-learning/)** - 使用AI学习框架

### 快速链接
- [入门指南](./docs/guides/getting-started.md)
- [组件示例](./examples/)
- [迁移示例](./ood/modules/Cookies.js)
- [构建配置](./webpack.config.js)

## 🤝 贡献

我们欢迎贡献！在开始之前，请阅读我们的[贡献指南](./CONTRIBUTING.md)。

### 贡献方式
- **Bug报告**: 提交详细的问题复现步骤
- **功能请求**: 建议新功能或改进
- **代码贡献**: 提交bug修复或新功能的Pull Request
- **文档**: 帮助改进文档，添加示例或翻译
- **测试**: 编写测试，提高测试覆盖率

### 开发工作流
1. Fork仓库
2. 创建特性分支 (`git checkout -b feature/awesome-feature`)
3. 提交更改 (`git commit -m '添加awesome特性'`)
4. 推送到分支 (`git push origin feature/awesome-feature`)
5. 打开Pull Request

## 🧪 测试

### 测试结构
```
tests/
├── unit/                    # 单元测试
├── integration/            # 集成测试
├── e2e/                    # 端到端测试
└── fixtures/               # 测试数据
```

### 运行测试
```bash
npm test                    # 运行所有测试
npm run test:unit          # 仅运行单元测试
npm run test:integration   # 仅运行集成测试
npm run test:e2e           # 仅运行端到端测试
npm run test:coverage      # 生成覆盖率报告
```

## 📄 许可证

本项目采用MIT许可证 - 查看[LICENSE](./LICENSE)文件了解详情。

## 🙏 致谢

- 原始OOD框架开发者
- 所有帮助改进这个库的贡献者
- 使这次迁移成为可能的现代Web标准

## 🚀 未来规划

### 短期目标
- 完成核心UI组件向ES6模块的迁移
- 将测试覆盖率提高到90%以上
- 改进文档，添加更多示例
- 为所有组件添加TypeScript类型定义

### 长期愿景
- 完全TypeScript迁移
- Web Components兼容性
- 框架无关的组件架构
- 增强可访问性（a11y）功能

---

**需要帮助？** 查看我们的[故障排除指南](./docs/troubleshooting.md)或[提交issue](https://gitee.com/ooderCN/oodui-es6/issues)。

**发现Bug？** 请使用我们的[issue模板](./.github/ISSUE_TEMPLATE/bug_report.md)进行报告。

**有功能请求？** 使用我们的[功能请求模板](./.github/ISSUE_TEMPLATE/feature_request.md)。