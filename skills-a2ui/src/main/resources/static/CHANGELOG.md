# 更新日志

本文件记录了该项目的所有显著变更。

格式基于 [Keep a Changelog](https://keepachangelog.com/zh-CN/1.0.0/)，
本项目遵循 [语义化版本控制](https://semver.org/spec/v2.0.0.html)。

## [未发布]

### 添加
- 完整的开源文档套件
- 带有适当版权归属的MIT许可证文件
- AI辅助学习框架文档
- 全面的API文档
- 贡献指南和行为准则
- 作者文件以感谢贡献者
- 结构化文档目录（docs/api、docs/guides、docs/ai-learning）

### 变更
- 更新package.json，添加完整元数据（作者、仓库、关键词）
- 优化代码结构，提高可维护性
- 增强README.md，添加全面的项目信息

### 修复
- 代码质量问题（console.log语句、linting警告）
- 文档漏洞和缺失信息

## [0.5.0] - 2026-01-04

### 添加
- ooder的初始版本，支持ES6模块
- ES6兼容层（shim.js、registry.js、index.js）
- 从遗留版本到ES6模块的迁移工具
- 用于开发和生产构建的Webpack配置
- 全面的测试套件
- 基本文档结构

### 功能
- 完整的ES6模块支持，支持tree shaking
- 与遗留`ood.Class`系统的向后兼容性
- 60+ UI组件（Button、Input、Dialog、TreeGrid、Tabs等）
- 响应式设计和广泛的主题支持
- TypeScript类型定义

---

## 版本控制指南

本项目遵循 [语义化版本控制](https://semver.org/)：

- **MAJOR** 版本：不兼容的API变更
- **MINOR** 版本：向后兼容的功能添加
- **PATCH** 版本：向后兼容的bug修复

## 发布流程

1. 更新`package.json`中的版本
2. 使用发布说明更新`CHANGELOG.md`
3. 为发布创建git标签
4. 构建并测试发布版本
5. 发布到npm注册表（如适用）