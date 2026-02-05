# 工程架构概述

## 1. 项目背景

ooder-agent-rad 是从传统的 ooder-RAD 改造而来，原系统存在以下痛点：
- 纯 JS 实现，前端逻辑复杂，维护成本高
- 传统 Spring 架构，分层过多，调用链冗长
- 服务调用模式，各模块耦合度高，扩展困难
- 笨重的集群架构，部署复杂，资源利用率低

## 2. 改造目标

通过改造，新系统实现了以下目标：
- 采用 **ooderAgent 架构**：实现轻量化、高可用的微服务架构
- **全栈架构实现**：前后端一体化开发，提高开发效率
- **注解驱动开发**：简化配置，提高代码可读性和可维护性
- **插件化设计**：支持动态扩展，提高系统灵活性

## 3. 新架构核心设计

### 3.1 架构分层

ooder-agent-rad 采用分层架构设计，主要包含以下层次：

1. **表现层**：负责处理用户请求和响应，包括控制器和视图模板
2. **业务逻辑层**：实现核心业务逻辑，包括服务类和业务规则
3. **数据访问层**：负责数据持久化和访问，包括数据库操作和文件系统访问
4. **基础设施层**：提供通用功能支持，包括日志、配置、安全等

### 3.2 核心组件

| 组件 | 职责 | 实现类 |
|------|------|--------|
| 主控制器 | 应用入口，处理主视图请求 | [Main.java](../../src/main/java/net/ooder/editor/Main.java) |
| 主面板 | 应用的主框架面板，包含菜单栏、工具栏和工作区 | [FramePanel.java](../../src/main/java/net/ooder/editor/FramePanel.java) |
| OOD设计器 | 可视化设计器，支持拖拽式设计 | [OODDesignerForm.java](../../src/main/java/net/ooder/editor/OODDesignerForm.java) |
| 主状态工具栏 | 应用主状态工具栏 | [MainStatusToolBar.java](../../src/main/java/net/ooder/editor/MainStatusToolBar.java) |
| RAD主面板 | RAD主面板 | [RADMainPanel.java](../../src/main/java/net/ooder/editor/RADMainPanel.java) |
| 工具箱 | 提供组件库和工具集 | [ToolBox.java](../../src/main/java/net/ooder/editor/toolbox/ToolBox.java) |
| 文件服务 | 文件相关服务 | [OODFileService.java](../../src/main/java/net/ooder/editor/toolbox/file/service/OODFileService.java) |
| 模块服务 | 模块相关服务 | [OODModuleService.java](../../src/main/java/net/ooder/editor/toolbox/file/service/OODModuleService.java) |
| 页面服务 | 页面相关服务 | [OODPageService.java](../../src/main/java/net/ooder/editor/toolbox/file/service/OODPageService.java) |

### 3.3 技术栈

- **后端**：Java 1.8+, Spring Boot 2.7.0
- **前端**：JavaScript, HTML5, CSS3, SVG
- **框架**：ooder 全栈框架
- **架构**：ooderAgent 微服务架构
- **模板引擎**：FreeMarker 2.3.31
- **构建工具**：Maven 3.0+

## 4. 架构改造亮点

### 4.1 轻量化设计

告别笨重的集群架构，实现轻量化部署，降低资源消耗，提高部署效率。

### 4.2 全栈一体化开发

实现前后端一体化开发，提高开发效率，减少前后端协作成本。

### 4.3 注解驱动开发

采用简洁的注解配置替代繁琐的 XML 配置，提高代码可读性和可维护性。

### 4.4 插件化扩展

支持插件化动态扩展，便于功能扩展和定制化开发。

### 4.5 事件驱动通信

基于事件驱动的组件通信，降低组件间耦合度，提高系统灵活性。

## 5. 架构演进路线

### 5.1 第一阶段：架构分析与设计

- 分析原有系统架构和痛点
- 设计 ooderAgent 架构方案
- 定义核心组件和接口

### 5.2 第二阶段：核心组件实现

- 实现主框架和核心服务
- 开发注解驱动框架
- 实现视图系统

### 5.3 第三阶段：插件集成

- 集成 DSM、RIGHT 等现有插件
- 实现插件管理机制
- 优化插件通信

### 5.4 第四阶段：性能优化与测试

- 优化系统性能
- 完善测试用例
- 进行负载测试

### 5.5 第五阶段：部署与上线

- 实现轻量化部署方案
- 编写部署文档
- 上线运行

## 6. 未来规划

- 增强 AI 辅助设计能力
- 支持更多前端框架的代码生成
- 改进性能和用户体验
- 增加更多内置组件和模板
- 增强团队协作功能
- 支持更多云服务集成
- 提供移动端设计支持
- 增强安全性和稳定性
