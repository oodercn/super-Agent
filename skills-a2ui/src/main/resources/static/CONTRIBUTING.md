# 为ooder做贡献

感谢您对为ooder做贡献感兴趣！本文档提供了为项目做贡献的指南和说明。

## 📋 目录
- [行为准则](#行为准则)
- [开始贡献](#开始贡献)
- [开发工作流](#开发工作流)
- [代码标准](#代码标准)
- [提交指南](#提交指南)
- [Pull Request流程](#pull-request流程)
- [测试](#测试)
- [文档](#文档)
- [社区](#社区)

## 📜 行为准则

请阅读并遵守我们的[行为准则](./CODE_OF_CONDUCT.md)。我们致力于为所有贡献者提供一个欢迎和包容的环境。

## 🚀 开始贡献

### 先决条件
- Node.js 14+ 和 npm/yarn
- 基本了解ES6模块
- 熟悉OOD框架（有帮助但不是必需）

### 设置开发环境
1. Fork仓库
2. 克隆您的fork：
   ```bash
   git clone https://gitee.com/ooderCN/oodui-es6.git
   cd oodui-es6
   ```
3. 安装依赖：
   ```bash
   npm install
   ```
4. 启动开发服务器：
   ```bash
   npm run dev
   ```
5. 在浏览器中打开 http://localhost:8080

## 🔧 开发工作流

### 分支策略
- `main`：稳定的、生产就绪的代码
- `develop`：用于功能开发的分支
- `feature/*`：新功能或增强
- `bugfix/*`：bug修复
- `docs/*`：文档更新

### 创建功能
1. 从`develop`创建一个功能分支：
   ```bash
   git checkout develop
   git pull origin develop
   git checkout -b feature/your-feature-name
   ```
2. 进行您的更改
3. 编写或更新测试
4. 更新文档
5. 运行测试并确保它们通过
6. 按照提交指南提交您的更改
7. 推送到您的fork
8. 创建一个Pull Request

## 📝 代码标准

### ES6模块迁移标准
将遗留代码迁移到ES6模块时：

1. **文件位置**：将迁移后的模块放在`ood/modules/`中
2. **命名约定**：类名使用PascalCase
3. **导出风格**：对主要功能使用命名导出
4. **装饰器使用**：应用`@register()`装饰器进行全局注册
5. **导入路径**：在`ood/`目录内使用相对导入

### 示例迁移模板
```javascript
import { register } from '../es6/shim.js';
import { LegacyDependency } from '../js/LegacyModule.js';

/**
 * 模块描述
 * @class MyModule
 */
@register('ood.MyModule')
export class MyModule {
  /**
   * 静态方法示例
   * @static
   */
  static staticMethod() {
    // 实现
  }

  /**
   * 实例方法示例
   */
  instanceMethod() {
    // 实现，可以使用LegacyDependency
  }
}
```

### 代码质量规则
- 使用提供的配置运行ESLint
- 生产代码中不使用console.log（仅调试模式）
- 优先使用const而不是let，避免使用var
- 使用有意义的变量和函数名称
- 为公共API添加JSDoc注释
- 保持函数小而专注（少于50行）

## 💾 提交指南

### 提交信息格式
```
type(scope): subject

body

footer
```

### 类型
- `feat`：新功能
- `fix`：bug修复
- `docs`：文档更改
- `style`：代码格式化，缺失的分号等
- `refactor`：既不修复bug也不添加功能的代码更改
- `perf`：性能改进
- `test`：添加或修复测试
- `chore`：对构建过程或辅助工具的更改

### 示例
```
feat(Button): 添加加载状态支持

- 为Button组件添加loading属性
- 实现旋转动画
- 使用示例更新文档

Closes #123
```

```
fix(Dialog): 修复关闭按钮定位

- 调整关闭按钮的CSS
- 添加响应式处理
- 更新测试用例

Resolves #456
```

## 🔍 Pull Request流程

### PR检查清单
提交PR之前，请确保：

1. ✅ 代码符合项目标准
2. ✅ 测试通过 (`npm test`)
3. ✅ ESLint通过 (`npm run lint`)
4. ✅ 文档已更新
5. ✅ 提交信息遵循指南
6. ✅ 与基础分支无冲突

### PR描述模板
```markdown
## 描述
对更改的简要描述

## 相关问题
修复 # (问题编号)

## 更改类型
- [ ] Bug修复（非破坏性更改）
- [ ] 新功能（非破坏性更改）
- [ ] 破坏性更改（修复或功能会导致现有功能无法按预期工作）
- [ ] 文档更新

## 测试
- [ ] 添加/更新了单元测试
- [ ] 集成测试通过
- [ ] 端到端测试通过

## 截图（如果适用）

## 附加说明
任何附加信息
```

### 审查流程
1. PR会自动进行测试
2. 维护者会审查代码并提供反馈
3. 解决审查评论
4. 一旦获得批准，PR将被合并
5. 更改将自动部署（如果适用）

## 🧪 测试

### 测试类别
1. **单元测试**：测试单个函数/方法
2. **集成测试**：测试模块交互
3. **端到端测试**：测试完整用户工作流
4. **兼容性测试**：确保与遗留代码兼容

### 编写测试
```javascript
// Button组件的单元测试示例
import { Button } from '../../src/modules/Button.js';

describe('Button', () => {
  test('应该创建带有文本的按钮', () => {
    const button = new Button({ text: '点击我' });
    expect(button.getText()).toBe('点击我');
  });

  test('应该处理点击事件', () => {
    const mockClick = jest.fn();
    const button = new Button({ onClick: mockClick });
    button.click();
    expect(mockClick).toHaveBeenCalled();
  });
});
```

### 运行测试
```bash
npm test                    # 运行所有测试
npm run test:unit          # 仅运行单元测试
npm run test:integration   # 运行集成测试
npm run test:e2e           # 运行端到端测试
npm run test:coverage      # 覆盖率报告
```

## 📚 文档

### 文档标准
- 所有公共API必须有JSDoc注释
- 示例应该实用且可测试
- 保持文档与代码更改同步
- 使用清晰、简洁的语言

### 添加文档
1. 更新JSDoc注释中的API参考
2. 在`docs/examples/`中添加示例
3. 更新`docs/guides/`中的相关指南
4. 确保交叉引用正确

## 👥 社区

### 获取帮助
- 首先查看[文档](./docs/)
- 搜索现有问题和讨论
- 加入我们的社区聊天（如果可用）
- 在讨论中提问

### 报告问题
报告问题时，请包含：
1. 对问题的清晰描述
2. 复现步骤
3. 预期行为与实际行为
4. 环境详细信息（浏览器、操作系统、Node版本）
5. 相关代码片段

### 功能请求
对于功能请求，请提供：
1. 对功能的详细描述
2. 用例和好处
3. 建议的实现方法（如果已知）
4. 潜在挑战

## 📊 贡献认可

### 贡献者列表
所有贡献者都列在[AUTHORS](./AUTHORS)文件中。如果您是新贡献者，请确保将自己添加到列表中。

### 贡献者徽章
- 首次贡献者：🎉
- 重要贡献：🚀
- 文档英雄：📚
- Bug猎手：🐛

## 🛠️ 开发工具

### 有用的脚本
```bash
# 构建脚本
npm run build              # 生产构建
npm run build:dev          # 开发构建
npm run build:watch       # 监听模式

# 代码质量
npm run lint              # ESLint检查
npm run lint:fix          # 自动修复lint问题

# 迁移工具
npm run migrate:help      # 显示迁移帮助
npm run migrate:cookies   # 迁移Cookies.js
npm run migrate:all       # 迁移所有模块
```

### 编辑器配置
推荐的VS Code扩展：
- ESLint
- Prettier
- JSDoc注释
- Code Spell Checker

## 📈 贡献指标

我们跟踪以下领域的贡献：
- 代码贡献（代码行数，功能）
- Bug修复和问题解决
- 文档改进
- 测试覆盖率改进
- 社区支持和指导

## 🎉 感谢！

感谢您考虑为ooder做出贡献。您的贡献帮助使这个项目变得更好！

---

*本文档是一个动态文档。如果您有改进建议，请提交PR或打开issue。*