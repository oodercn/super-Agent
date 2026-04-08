# ooderNexus Enterprise - 企业版 AI Agent

<div align="center">

**面向企业级应用的 AI Agent 平台**

[![License](https://img.shields.io/badge/license-MIT-blue.svg)](LICENSE)
[![Java](https://img.shields.io/badge/Java-21-orange.svg)](https://openjdk.org/)
[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.4.4-brightgreen.svg)](https://spring.io/projects/spring-boot)
[![Version](https://img.shields.io/badge/version-3.0.2-green.svg)](https://gitee.com/ooderCN/super-Agent)

[在线文档](https://gitee.com/ooderCN/super-Agent) | [快速开始](#快速开始) | [企业特性](#企业特性)

</div>

---

## 📋 目录

- [项目简介](#项目简介)
- [版本信息](#版本信息)
- [核心特性](#核心特性)
- [企业特性](#企业特性)
- [快速开始](#快速开始)
- [功能模块](#功能模块)
- [架构设计](#架构设计)
- [开发指南](#开发指南)
- [开源协议](#开源协议)

---

## 项目简介

ooderNexus Enterprise 是面向企业级应用的 AI Agent 平台，提供多租户、组织架构、权限管理等企业级特性。基于 ooder-agent 核心框架，为企业提供安全、可控、可扩展的 AI 能力平台。

### 适用场景

- 🏢 企业办公自动化
- 👥 团队协作
- 📋 流程管理
- 🤖 企业AI助手

---

## 版本信息

| 组件 | 版本 |
|------|------|
| ooderNexus Enterprise | 3.0.2 |
| Spring Boot | 3.4.4 |
| Java | 21 |
| ooder SDK | 3.0.2 |

### 依赖组件

| 组件 | 版本 | 说明 |
|------|------|------|
| agent-sdk-core | 3.0.2 | Agent SDK 核心 |
| skill-common | 3.0.2 | 技能通用组件 |
| scene-engine | 3.0.2 | 场景引擎 |
| skill-hotplug-starter | 3.0.2 | 技能热插拔启动器 |
| skills-framework | 3.0.2 | 技能框架 |
| ooder-bpm-web | 3.0.2 | 工作流引擎 |
| skill-tenant | 3.0.2 | 租户管理 |
| skill-org | 3.0.2 | 组织架构 |

---

## 核心特性

### 🎯 场景引擎
- 场景定义与执行
- 场景组管理
- 场景生命周期控制

### 🔌 技能热插拔
- 动态技能加载
- 技能发现与安装
- 技能生命周期管理

### 🤖 LLM 集成
- 多模型支持 (DeepSeek, 百度千帆, 阿里百炼)
- 流式对话
- Function Calling

### 📊 工作流引擎
- BPMN 2.0 支持
- 流程编排
- 任务管理

---

## 企业特性

### 🏢 多租户支持
- 租户隔离
- 租户配置
- 租户数据安全

### 👥 组织架构
- 部门管理
- 层级结构
- 组织同步

### 🔐 权限管理
- 角色管理
- 权限控制
- 菜单权限

### 📈 监控告警
- 系统监控
- 日志管理
- 性能分析

---

## 快速开始

### 环境要求

- JDK 21+
- Maven 3.8+
- 8GB+ 内存
- 数据库 (MySQL/PostgreSQL 推荐)

### 编译运行

```bash
# 编译
mvn clean compile

# 运行 (端口: 8097)
mvn spring-boot:run

# 打包
mvn clean package -DskipTests
```

### Docker 部署

```bash
# 构建镜像
docker build -t ooder-nexus-enterprise:3.0.2 .

# 运行容器
docker run -d -p 8097:8097 ooder-nexus-enterprise:3.0.2
```

### 访问地址

- 控制台: http://localhost:8097/console/
- 登录页: http://localhost:8097/console/pages/login.html
- 工作台: http://localhost:8097/console/pages/workbench.html

---

## 功能模块

| 模块 | 说明 | 状态 |
|------|------|------|
| 首页 | 数据概览、快捷入口 | ✅ |
| 能力中心 | 能力发现、安装、配置 | ✅ |
| 场景管理 | 场景创建、激活、执行 | ✅ |
| 工作台 | 任务管理、待办事项 | ✅ |
| LLM配置 | 模型配置、对话管理 | ✅ |
| 组织管理 | 部门管理、成员管理 | ✅ |
| 租户管理 | 租户配置、租户隔离 | ✅ |
| 系统配置 | 数据库配置、系统设置 | ✅ |

### 角色权限

| 角色 | 权限 |
|------|------|
| admin | 系统管理、租户管理、组织管理、能力管理 |
| leader | 团队管理、场景管理、任务分配 |
| collaborator | 协作任务、场景执行 |
| user | 工作台、能力使用、个人设置 |

---

## 架构设计

```
ooder-nexus-enterprise/
├── src/main/java/net/ooder/enexus/
│   ├── EnexusApplication.java     # 启动类
│   ├── config/                    # 配置类
│   │   ├── AuthConfig.java        # 认证配置
│   │   ├── WebConfig.java         # Web配置
│   │   ├── SeSdkConfig.java       # SDK配置
│   │   └── SkillsFrameworkConfig.java
│   ├── controller/                # REST控制器
│   │   ├── AuthMenuController.java
│   │   ├── ConfigController.java
│   │   ├── LlmController.java
│   │   ├── SkillController.java
│   │   └── RoleManagementController.java
│   ├── dto/                       # 数据传输对象
│   │   ├── capability/
│   │   ├── config/
│   │   ├── llm/
│   │   ├── menu/
│   │   ├── role/
│   │   └── skill/
│   ├── llm/                       # LLM服务
│   │   ├── service/
│   │   ├── DeepSeekLlmProvider.java
│   │   ├── BaiduLlmProvider.java
│   │   └── AliyunBailianLlmProvider.java
│   ├── service/                   # 业务服务
│   │   └── MenuRoleConfigService.java
│   ├── skill/                     # 技能管理
│   │   ├── driver/
│   │   └── registry/
│   └── spi/                       # SPI扩展点
├── src/main/resources/
│   ├── application.yml            # 配置文件
│   ├── skill.yaml                 # 技能配置
│   └── static/console/            # 前端资源
│       ├── css/
│       ├── js/
│       ├── pages/
│       └── menu-config.json
└── pom.xml
```

---

## 开发指南

### 技术栈

- **后端**: Spring Boot 3.4.4, Java 21
- **前端**: 原生 JavaScript, CSS3
- **数据库**: SQLite / MySQL / PostgreSQL
- **AI**: DeepSeek, 百度千帆, 阿里百炼

### 本地开发

```bash
# 设置本地Maven仓库
mvn clean compile -o

# 调试模式运行
mvn spring-boot:run -Dspring-boot.run.jvmArguments="-Xdebug -Xrunjdwp:transport=dt_socket,server=y,suspend=n,address=5005"
```

### GraalVM Native Image

```bash
# 构建原生镜像
mvn -Pnative native:compile

# 运行原生镜像
./target/ooder-nexus-enterprise
```

---

## 开源协议

本项目采用 [MIT License](LICENSE) 开源协议。

---

## 相关链接

- 父项目: [super-Agent](https://gitee.com/ooderCN/super-Agent)
- 核心框架: [ooder-agent](../ooder-agent/README.md)
- 个人版: [ooder-nexus](../ooder-nexus/README.md)
- SDK: [ooder-sdk](https://gitee.com/ooderCN/ooder-sdk)

---

<div align="center">

**ooderNexus Enterprise 3.0.2** - 企业级 AI 平台

</div>
