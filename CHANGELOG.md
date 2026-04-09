# Changelog

All notable changes to this project will be documented in this file.

## [3.0.2] - 2026-04-09

### Added
- 升级到 Java 21
- 升级到 Spring Boot 3.4.4
- 升级 ooder SDK 到 3.0.2
- 新增 GraalVM Native Image 支持
- 新增 LLM 多模型支持 (DeepSeek, 百度千帆, 阿里百炼)
- 从 MVP (apexos) 工程同步完整功能模块到 ooder-agent/ooder-nexus
- 从 os 工程同步完整功能模块到 ooder-nexus-enterprise
- 新增 Agent 会话管理和心跳监控功能
- 新增 Capability 能力管理系统
- 新增完整的 DTO 数据传输对象体系
- 新增 Knowledge 知识库管理功能
- 新增 LLM 配置和监控功能
- 新增 Scene 场景管理功能
- 新增 WebSocket 实时通信支持
- 新增 SpringDoc OpenAPI 文档支持

### Changed
- 重构项目结构，模块化设计
  - ooder-agent: 核心框架 (端口 8099)
  - ooder-nexus: 个人版 (端口 8098)
  - ooder-nexus-enterprise: 企业版 (端口 8097)
- 优化前端页面，统一UI风格
- 更新文档到 3.0.2 版本
- 统一包名规范 (net.ooder.agent/nexus/enexus)
- 修复登录 API 路径 (/auth -> /mvp-auth)
- 修复菜单配置中不存在的页面引用

### Fixed
- 修复编码问题 (BOM 字符)
- 修复依赖冲突
- 修复包名不一致问题
- 修复 Maven 缓存失败记录

### Removed
- 移除历史版本协议文档
- 移除过时的依赖
- 移除损坏的 Java 源文件

## [3.0.1] - 2026-04-07

### Added
- 初始版本发布
- 基础 Agent 框架
- 场景引擎集成
- 技能热插拔支持

## [3.0.0] - 2026-03-23

### Added
- 项目初始化
- 基础项目结构
