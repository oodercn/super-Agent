# Ooder 框架组件开发 Skills-Solo 使用说明

## 1. Skills-Solo 概述

Skills-Solo 是按照 trae-solo 规范组织的 Ooder 框架组件开发技能包，包含了核心组件开发所需的技能文档、生成工具和详细组件文档。它旨在帮助开发者快速学习和使用 Ooder 框架的核心组件，提高开发效率。

## 2. 目录结构

```
ooder-skills/
├── docs/                # 组件详细文档目录
│   ├── FormLayout.md    # FormLayout 组件专题文档
│   └── TreeGrid.md      # TreeGrid 组件专题文档
├── tools/               # 组件生成工具目录
│   ├── generateForm.js  # 表单组件生成工具（基于 RAD 设计）
│   └── generateList.js  # 列表组件生成工具（基于 RAD 设计）
├── README.md            # 插件包说明文档
├── SKILL.md             # 技能文档（带目录）
└── SKILLS_SOLO.md       # Skills-Solo 使用说明
```

## 3. 核心功能

### 3.1 组件开发技能文档

**文件**：`SKILL.md`

**功能**：提供完整的 Ooder 框架组件开发技能指南，包含：
- 详细的文档目录，支持快速导航
- 核心组件介绍（FormLayout 和 TreeGrid）
- 组件生成工具使用方法
- 运行环境和使用步骤

**使用方法**：直接打开阅读，或使用 Markdown 阅读器查看。文档包含二级目录，可快速跳转到感兴趣的章节。

### 3.2 组件生成工具

#### 3.2.1 表单组件生成工具

**文件**：`tools/generateForm.js`

**功能**：基于 RAD 设计的表单组件生成工具，生成符合 Ooder 框架规范的表单组件代码。

**参考模板**：JDSEasy RAD Studio 生成的 Form.js

**使用方法**：

```bash
# 基本用法
node tools/generateForm.js "类名：TestForm，标题：测试表单" TestForm.js

# 自定义别名
node tools/generateForm.js "类名：TestForm，标题：测试表单，别名：TestForm" TestForm.js
```

#### 3.2.2 列表组件生成工具

**文件**：`tools/generateList.js`

**功能**：基于 RAD 设计的列表组件生成工具，生成符合 Ooder 框架规范的列表组件代码，支持动态列生成。

**参考模板**：JDSEasy RAD Studio 生成的 Grid.js

**使用方法**：

```bash
# 基本用法
node tools/generateList.js "类名：TestList，标题：测试列表" TestList.js

# 自定义别名和服务
node tools/generateList.js "类名：TestList，标题：测试列表，别名：Search，服务：net.ooder.test.TestService" TestList.js

# 自定义列
node tools/generateList.js "类名：StudentSchedule，标题：学生课表，别名：Schedule，服务：net.ooder.test.CourseService，包含哪些列：学生ID,姓名，科目" TestStudentSchedule.js
```

### 3.3 组件详细文档

#### 3.3.1 FormLayout 组件文档

**文件**：`docs/FormLayout.md`

**功能**：提供 FormLayout 组件的详细使用指南，包含：
- 组件概述和核心功能
- 组件类型（基础 FormLayout 和移动端 MFormLayout）
- 多种使用场景和示例
- 最佳实践和性能优化建议
- 常见问题与解决方案

#### 3.3.2 TreeGrid 组件文档

**文件**：`docs/TreeGrid.md`

**功能**：提供 TreeGrid 组件的详细使用指南，包含：
- 组件概述和核心功能
- 组件类型（基础 TreeGrid 和移动端 MTreeGrid）
- 多种使用场景和示例
- 最佳实践和性能优化建议
- 常见问题与解决方案

## 4. 安装和使用

### 4.1 安装

1. 将 `ooder-skills` 目录复制到您的 Ooder 项目中
2. 确保您的项目中已经包含了 Ooder 框架
3. 确保运行环境为 agent-rad

### 4.2 使用步骤

#### 4.2.1 学习技能文档

1. 打开 `SKILL.md` 文件，查看详细的组件开发技能指南
2. 使用文档目录快速跳转到感兴趣的章节
3. 了解核心组件的特性和使用方法

#### 4.2.2 使用组件生成工具

1. 打开命令行工具
2. 进入项目根目录
3. 运行相应的生成脚本：
   - 生成表单组件：`node ooder-skills/tools/generateForm.js <描述> <输出文件>`
   - 生成列表组件：`node ooder-skills/tools/generateList.js <描述> <输出文件>`
4. 根据提示输入自然语言描述
5. 生成组件代码文件
6. 根据实际需求修改代码

#### 4.2.3 参考组件详细文档

1. 打开 `docs/FormLayout.md` 或 `docs/TreeGrid.md`
2. 学习组件的详细使用方法和最佳实践
3. 参考示例代码实现自己的组件
4. 查看常见问题解决方案，解决开发中遇到的问题

## 5. 使用示例

### 5.1 生成表单组件示例

**命令**：
```bash
node ooder-skills/tools/generateForm.js "类名：LoginForm，标题：登录表单，别名：Login" LoginForm.js
```

**输出**：生成 `LoginForm.js` 文件，包含完整的表单组件结构、APICaller、FormLayout、底部按钮栏和上下文区块。

### 5.2 生成列表组件示例

**命令**：
```bash
node ooder-skills/tools/generateList.js "类名：StudentSchedule，标题：学生课表，别名：Schedule，服务：net.ooder.test.CourseService，包含哪些列：学生ID,姓名，科目，教师，上课时间，上课地点" TestStudentSchedule.js
```

**输出**：生成 `TestStudentSchedule.js` 文件，包含完整的列表组件结构、APICaller、TreeGrid、菜单栏和上下文区块，以及用户指定的所有列。

## 6. 最佳实践

1. **先学习技能文档**：在使用生成工具前，建议先阅读 `SKILL.md` 了解核心组件的基本概念和使用方法

2. **合理使用生成工具**：生成工具可以快速生成组件骨架，但生成的代码可能需要根据实际需求进行调整

3. **参考组件详细文档**：在开发复杂组件时，建议参考 `docs/` 目录下的组件专题文档，了解最佳实践和常见问题解决方案

4. **遵循组件开发规范**：确保生成的组件符合 Ooder 框架的开发规范，便于维护和扩展

5. **测试生成的组件**：生成组件后，建议进行测试，确保组件功能正常

6. **定期更新技能包**：定期更新 `ooder-skills` 目录，获取最新的技能文档和工具

## 7. 技术支持

- **技能文档**：`SKILL.md` - 提供完整的组件开发技能指南
- **组件文档**：`docs/` 目录 - 提供详细的组件使用指南
- **生成工具**：`tools/` 目录 - 提供组件生成工具
- **示例代码**：生成的组件文件可作为参考示例
- **框架资源**：请参考 Ooder 框架官方文档

## 8. 更新日志

### v2.0.0 (2026-01-26)
- 按照 trae-solo 规范重新组织目录结构
- 更新 `SKILL.md`，添加详细的二级文档目录
- 优化组件生成工具，支持动态列生成
- 添加组件详细文档（FormLayout.md 和 TreeGrid.md）
- 更新运行环境为 agent-rad
- 添加 Skills-Solo 使用说明

## 9. 许可证

本插件包使用 MIT 许可证，详情请查看项目根目录下的 LICENSE 文件。

## 10. 联系方式

如有问题或建议，请通过以下方式联系：

- 项目地址：https://github.com/oodercn/super-Agent
- 文档地址：https://docs.ooder.net
- 社区论坛：https://forum.ooder.net

## 11. 结语

Skills-Solo 提供了完整的 Ooder 框架组件开发技能支持，包括技能文档、生成工具和组件详细文档。通过使用 Skills-Solo，开发者可以快速学习和使用 Ooder 框架的核心组件，提高开发效率，构建高质量的 Ooder 应用。

祝您使用愉快！