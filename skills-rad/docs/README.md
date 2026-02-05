# ooder-agent-rad 工程文档

## 概述

ooder-agent-rad 是基于 ooder 全栈框架实现的一个完整的 RAD（快速应用开发）工具组合。它采用现代化的 ooderAgent 架构，实现了前后端一体化开发，支持注解驱动设计和插件化扩展，为开发者提供高效、灵活的应用开发体验。

## 项目背景

ooder-agent-rad 是从传统的 ooder-RAD 改造而来，原系统存在以下痛点：
- 纯 JS 实现，前端逻辑复杂，维护成本高
- 传统 Spring 架构，分层过多，调用链冗长
- 服务调用模式，各模块耦合度高，扩展困难
- 笨重的集群架构，部署复杂，资源利用率低

通过改造，新系统采用了 ooderAgent 全栈架构，实现了轻量化、高可用的微服务架构，支持注解驱动开发和插件化设计，显著提高了开发效率和系统灵活性。

## 文档结构

本文档按照技术文档结构化规范对 ooder-agent-rad 的技术文档进行分类和组织，便于知识体系的构建和维护。

### 1. 架构与核心概念
- [工程架构概述](architecture/ARCHITECTURE_OVERVIEW.md)
- [ooderAgent 架构设计](architecture/OODER_AGENT_ARCHITECTURE.md)
- [核心概念详解](architecture/CORE_CONCEPTS.md)
- [架构改造对比](architecture/ARCHITECTURE_COMPARISON.md)

### 2. 注解驱动开发
- [注解体系总览](annotation/ANNOTATION_OVERVIEW.md)
- [视图注解详解](annotation/VIEW_ANNOTATION.md)
- [服务注解详解](annotation/SERVICE_ANNOTATION.md)
- [组件注解详解](annotation/COMPONENT_ANNOTATION.md)
- [注解最佳实践](annotation/ANNOTATION_BEST_PRACTICES.md)

### 3. 视图系统
- [视图设计规范](view/VIEW_DESIGN_SPEC.md)
- [主视图架构](view/MAIN_VIEW_ARCHITECTURE.md)
- [OODDesigner 集成](view/OOD_DESIGNER_INTEGRATION.md)
- [导航菜单设计](view/NAVIGATION_MENU.md)

### 4. 服务架构
- [服务设计规范](service/SERVICE_DESIGN_SPEC.md)
- [核心服务详解](service/CORE_SERVICES.md)
- [VFS-WORKFLOW 桥接](service/VFS_WORKFLOW_BRIDGE.md)

### 5. 插件系统
- [插件开发指南](plugin/PLUGIN_DEVELOPMENT_GUIDE.md)
- [现有插件介绍](plugin/EXISTING_PLUGINS.md)
- [插件集成示例](plugin/PLUGIN_INTEGRATION_EXAMPLE.md)

### 6. 开发指南
- [快速入门](dev/QUICK_START.md)
- [开发环境搭建](dev/DEVELOPMENT_ENV.md)
- [代码规范](dev/CODE_SPEC.md)
- [测试指南](dev/TEST_GUIDE.md)

### 7. 部署与运维
- [部署指南](deploy/DEPLOYMENT_GUIDE.md)

## 技术栈

- **后端**：Java 1.8+, Spring Boot 2.7.0
- **前端**：JavaScript, HTML5, CSS3, SVG
- **框架**：ooder 全栈框架
- **架构**：ooderAgent 微服务架构
- **模板引擎**：FreeMarker 2.3.31
- **构建工具**：Maven 3.0+

## 核心特性

- **全栈架构**：前后端一体化开发，提高开发效率
- **注解驱动**：简化配置，提高代码可读性和可维护性
- **插件化设计**：支持动态扩展，提高系统灵活性
- **事件驱动**：基于事件的组件通信，降低耦合度
- **轻量化部署**：告别笨重的集群架构，实现轻量化部署
- **高可用设计**：基于 ooderAgent 架构，实现高可用设计

## 学习路径

1. **入门阶段**：阅读工程架构概述、核心概念详解、快速入门
2. **开发阶段**：阅读注解体系总览、视图设计规范、服务设计规范
3. **高级阶段**：阅读ooderAgent 架构设计、插件开发指南、注解最佳实践
4. **运维阶段**：阅读部署指南

## 许可证

本项目采用 GNU General Public License v3.0 开源协议。
