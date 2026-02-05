# 测试指南

本指南解释了如何为ooder项目编写和运行测试。

## 📋 目录

- [介绍](#介绍)
- [测试结构](#测试结构)
- [编写测试](#编写测试)
- [运行测试](#运行测试)
- [测试覆盖率](#测试覆盖率)
- [最佳实践](#最佳实践)
- [故障排除](#故障排除)

## 📖 介绍

ooder使用Jest进行测试，Jest提供了一种简单而强大的方式来为您的组件和模块编写测试。

### 为什么测试很重要

- **质量保证**：确保您的代码按预期工作
- **防止回归**：在bug进入生产环境之前捕获它们
- **文档**：测试作为代码应该如何工作的文档
- **重构安全**：允许您自信地重构代码
- **协作**：帮助团队成员理解您的代码如何工作

## 📁 测试结构

测试目录结构遵循以下模式：

```
tests/
├── unit/                    # 单元测试（单个组件/函数）
├── integration/            # 集成测试（组件交互）
├── e2e/                    # 端到端测试（完整用户流程）
└── fixtures/               # 测试数据和模拟对象
```

## ✍️ 编写测试

### 单元测试

单元测试专注于隔离测试单个组件或函数。

#### 单元测试示例

```javascript
import { Button } from '../ood/js/UI/Button.js';

describe('Button组件', () => {
  test('应该创建一个带有文本的按钮', () => {
    const btn = new Button({ text: '点击我' });
    expect(btn.text).toBe('点击我');
  });

  test('应该处理点击事件', () => {
    const mockClick = jest.fn();
    const btn = new Button({ onClick: mockClick });
    btn.click();
    expect(mockClick).toHaveBeenCalled();
  });

  test('应该支持禁用状态', () => {
    const btn = new Button({ disabled: true });
    expect(btn.disabled).toBe(true);
  });
});
```

### 集成测试

集成测试验证多个组件一起工作是否正确。

#### 集成测试示例

```javascript
import { Dialog } from '../ood/js/UI/Dialog.js';
import { Button } from '../ood/js/UI/Button.js';

describe('Dialog集成', () => {
  test('点击按钮时应该显示对话框', () => {
    // 创建按钮和对话框
    const dialog = new Dialog({ 
      title: '测试', 
      content: '对话框内容',
      visible: false
    });
    
    const btn = new Button({ 
      text: '打开对话框',
      onClick: () => dialog.show()
    });
    
    // 模拟按钮点击
    btn.click();
    
    // 验证对话框可见
    expect(dialog.visible).toBe(true);
  });
});
```

### 模拟依赖

使用Jest的模拟功能来模拟依赖项：

```javascript
// 模拟依赖
jest.mock('../ood/js/UI/Button.js', () => ({
  Button: jest.fn().mockImplementation((options) => ({
    text: options.text,
    click: jest.fn(),
    disabled: options.disabled || false
  }))
}));

describe('模拟的Button', () => {
  test('应该创建一个模拟按钮', () => {
    const { Button } = require('../ood/js/UI/Button.js');
    const btn = new Button({ text: '模拟' });
    expect(btn.text).toBe('模拟');
  });
});
```

## 🏃 运行测试

### 测试脚本

使用以下npm脚本来运行测试：

```bash
# 运行所有测试
npm test

# 仅运行单元测试
npm run test:unit

# 仅运行集成测试
npm run test:integration

# 仅运行端到端测试
npm run test:e2e

# 在监听模式下运行测试
npm run test:watch

# 生成覆盖率报告
npm run test:coverage
```

### 运行特定测试

```bash
# 运行匹配模式的测试
npm test -- -t "Button"

# 运行特定文件中的测试
npm test -- tests/unit/Button.test.js
```

## 📊 测试覆盖率

### 生成覆盖率报告

```bash
npm run test:coverage
```

这将在`coverage/`目录中生成覆盖率报告，显示：

- 被测试覆盖的代码百分比
- 覆盖的行、函数和分支
- 覆盖率低的文件

### 覆盖率要求

我们的目标是：
- **>90%** 的整体覆盖率
- **>80%** 的每个模块覆盖率
- **100%** 的关键组件覆盖率

## 🌟 最佳实践

### 测试编写指南

1. **一次测试一件事**：每个测试应该专注于单个功能
2. **使用描述性名称**：测试名称应该清楚地描述它们测试的内容
3. **保持测试简洁**：测试应该简洁易懂
4. **测试边界情况**：测试空值、null、undefined和极端值
5. **模拟外部依赖**：将测试与外部服务隔离
6. **设置和清理**：使用`beforeEach()`和`afterEach()`进行通用设置/清理
7. **避免魔术数字**：使用命名常量而不是硬编码值
8. **测试成功和失败情况**：测试预期行为和错误情况

### 良好测试结构示例

```javascript
describe('ComponentName', () => {
  let component;
  
  beforeEach(() => {
    // 设置通用测试环境
    component = new ComponentName({ option: 'value' });
  });
  
  afterEach(() => {
    // 每个测试后清理
    component.destroy();
  });
  
  describe('methodName', () => {
    test('调用时应该执行某些操作', () => {
      // 测试实现
    });
    
    test('应该处理边界情况', () => {
      // 测试边界情况
    });
  });
});
```

## ❓ 故障排除

### 常见问题

1. **测试失败，显示"Module Not Found"**
   - 检查导入路径
   - 确保使用正确的模块语法
   - 验证文件存在

2. **测试超时**
   - 检查代码中是否有无限循环
   - 如有必要，增加测试超时时间
   - 验证异步操作是否正确处理

3. **模拟函数不工作**
   - 确保模拟正确设置
   - 检查模拟实现
   - 验证函数实际上被调用

4. **覆盖率报告显示低覆盖率**
   - 在报告中识别未覆盖的行
   - 为缺失的功能添加测试
   - 检查是否有一些代码被有意排除

## 🚀 下一步

- [阅读Jest文档](https://jestjs.io/docs/getting-started)
- [了解Jest中的模拟](https://jestjs.io/docs/mocking)
- [探索示例测试](tests/)

---

**测试愉快！** 🧪