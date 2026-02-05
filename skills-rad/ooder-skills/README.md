# Ooder框架组件开发技能插件包

## 1. 插件包概述

这个插件包包含了 Ooder 框架组件开发所需的技能文档、工具和示例，按照 trae-solo 规范组织，方便开发者学习和使用。

## 2. 目录结构

```
ooder-skills/
├── docs/                # 文档目录
├── examples/            # 示例代码目录
│   └── TestButtonClass.js  # 示例按钮组件
├── tools/               # 工具脚本目录
│   ├── naturalToOoder.js   # 自然语言转换工具
│   └── fixComponents.js    # 组件修复工具
├── README.md            # 插件包说明文档
└── SKILL.md             # 技能文档
```

## 3. 技能文档

**SKILL.md** 是 Ooder 框架组件开发的技能指南，包含以下主要内容：

- **核心技能**：框架基础、组件开发规范、组件验证和修复
- **基础组件技能**：输入类、选择类、按钮类、显示类组件
- **布局组件技能**：基础布局、高级布局、表单布局组件
- **数据组件技能**：列表数据、树形数据、选择器组件
- **图表组件技能**：ECharts、FusionChartsXT、SVG绘图组件
- **交互组件技能**：导航、弹窗、媒体组件
- **移动端组件技能**：移动端基础组件
- **工具和自动化技能**：自然语言转换工具、组件修复工具、组件缓存和验证
- **进阶技能**：组件性能优化、自定义组件开发、主题系统定制
- **最佳实践和问题排查**：组件开发最佳实践、常见问题排查
- **资源和持续学习**：框架资源、进阶学习方向

## 4. 工具和脚本

### 4.1 自然语言转换工具

**工具名称**：`./tools/naturalToOoder.js`

**功能描述**：将自然语言描述转换为符合 Ooder 框架规范的 class.js 文件

**使用方法**：

```bash
node ./tools/naturalToOoder.js "类名：TestClass，标题：测试组件，按钮：主要按钮，按钮：次要按钮" ./examples/TestClass.js
```

**输入格式**：`类名：XXX，标题：XXX，按钮：XXX`

**输出结果**：生成符合 Ooder 框架规范的 class.js 文件

### 4.2 组件修复工具

**工具名称**：`./tools/fixComponents.js`

**功能描述**：修复组件文件中的不支持属性和错误事件处理

**使用方法**：

```bash
# 修复单个文件
node ./tools/fixComponents.js ./examples/ButtonExample.js

# 修复目录下所有文件
node ./tools/fixComponents.js ./examples/
```

**修复内容**：
- 移除不支持的属性
- 修复事件处理方法（如将 setOnClick 转换为 onClick）
- 添加正确的定位属性
- 修复组件类型错误

## 5. 示例代码

**示例目录**：`./examples/`

**示例文件**：
- `TestButtonClass.js`：按钮组件示例，展示了 Ooder 框架组件的基本结构和使用方法

## 6. 安装和使用

### 6.1 安装

1. 将 `ooder-skills` 目录复制到您的项目中
2. 确保您的项目中已经包含了 Ooder 框架

### 6.2 使用

1. 学习技能文档：阅读 `SKILL.md` 了解 Ooder 框架组件开发的技能点
2. 使用工具：
   - 使用自然语言转换工具快速生成组件代码
   - 使用组件修复工具修复现有组件代码
3. 参考示例：查看 `examples/` 目录下的示例代码，了解组件的使用方法
4. 开发实践：根据技能文档和示例，开发符合规范的 Ooder 框架组件

## 7. 技术支持

- **框架文档**：`../oodui-es6/docs/`
- **组件缓存**：`../form/myspace/versionspace/test/0/view/BasicComponents/ComponentCache.js`
- **示例代码**：`./examples/`

## 8. 更新日志

### v1.0.0 (2026-01-25)
- 初始版本
- 包含技能文档、工具和示例
- 按照 trae-solo 规范组织

## 9. 许可证

本插件包使用 MIT 许可证，详情请查看项目根目录下的 LICENSE 文件。
