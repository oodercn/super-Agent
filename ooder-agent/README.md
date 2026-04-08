# ooderAgent - AI Agent 核心框架

<div align="center">

**面向企业级应用的 AI Agent 操作系统核心**

[![License](https://img.shields.io/badge/license-MIT-blue.svg)](LICENSE)
[![Java](https://img.shields.io/badge/Java-21-orange.svg)](https://openjdk.org/)
[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.4.4-brightgreen.svg)](https://spring.io/projects/spring-boot)
[![Version](https://img.shields.io/badge/version-3.0.2-green.svg)](https://gitee.com/ooderCN/super-Agent)

[在线文档](https://gitee.com/ooderCN/super-Agent) | [快速开始](#快速开始) | [架构设计](#架构设计)

</div>

---

## 📋 目录

- [项目简介](#项目简介)
- [版本信息](#版本信息)
- [核心特性](#核心特性)
- [快速开始](#快速开始)
- [架构设计](#架构设计)
- [功能模块](#功能模块)
- [API接口](#api接口)
- [开发指南](#开发指南)
- [开源协议](#开源协议)

---

## 项目简介

### 什么是 ooderAgent？

ooderAgent 是一个面向企业级应用的 AI Agent 核心框架，采用模块化设计，内置核心驱动，支持通过 Gitee 发现机制动态安装和扩展应用。它为企业提供了一个灵活、可扩展、易于管理的 AI Agent 运行平台。

### 核心价值

- **🚀 快速部署**: 最小化设计，开箱即用，5 分钟完成部署
- **🔌 灵活扩展**: 模块化架构，按需安装，动态扩展
- **🔐 安全可控**: MIT 开源协议，企业级安全，完全自主可控
- **📊 统一管理**: 集中式管理，可视化监控，简化运维
- **🤖 AI 驱动**: 内置 LLM 支持，智能决策，自动化执行

---

## 版本信息

| 组件 | 版本 |
|------|------|
| ooderAgent | 3.0.2 |
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

---

## 核心特性

### 🎯 场景引擎 (Scene Engine)
- 场景定义与执行
- 场景组管理
- 场景生命周期控制
- 场景知识绑定

### 🔌 技能热插拔 (Skill Hotplug)
- 动态技能加载
- 技能发现与安装
- 技能生命周期管理
- 技能包版本控制

### 🤖 LLM 集成
- 多模型支持 (DeepSeek, 百度千帆, 阿里百炼)
- 流式对话
- Function Calling
- 上下文管理

### 📊 工作流引擎
- BPMN 2.0 支持
- 流程编排
- 任务管理
- 流程监控

### 📚 知识库
- 知识存储
- 向量检索
- 知识绑定

---

## 快速开始

### 环境要求

- JDK 21+
- Maven 3.8+
- 8GB+ 内存

### 编译运行

```bash
# 编译
mvn clean compile

# 运行 (端口: 8099)
mvn spring-boot:run

# 打包
mvn clean package -DskipTests
```

### Docker 部署

```bash
# 构建镜像
docker build -t ooder-agent:3.0.2 .

# 运行容器
docker run -d -p 8099:8099 ooder-agent:3.0.2
```

### 访问地址

- 控制台: http://localhost:8099/console/
- 登录页: http://localhost:8099/console/pages/login.html
- 工作台: http://localhost:8099/console/pages/workbench.html

---

## 架构设计

```
ooder-agent/
├── src/main/java/net/ooder/agent/
│   ├── AgentApplication.java      # 启动类
│   ├── config/                    # 配置类
│   │   ├── AuthConfig.java        # 认证配置
│   │   ├── WebConfig.java         # Web配置
│   │   ├── SeSdkConfig.java       # SDK配置
│   │   └── SkillsFrameworkConfig.java
│   ├── controller/                # REST控制器
│   │   ├── AuthMenuController.java
│   │   ├── ConfigController.java
│   │   ├── LlmController.java
│   │   └── SkillController.java
│   ├── dto/                       # 数据传输对象
│   │   ├── capability/
│   │   ├── config/
│   │   ├── llm/
│   │   ├── menu/
│   │   └── skill/
│   ├── llm/                       # LLM服务
│   │   ├── service/
│   │   ├── DeepSeekLlmProvider.java
│   │   ├── BaiduLlmProvider.java
│   │   └── AliyunBailianLlmProvider.java
│   ├── service/                   # 业务服务
│   │   └── MenuRoleConfigService.java
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

## 功能模块

| 模块 | 说明 | 状态 |
|------|------|------|
| 认证授权 | 用户登录、角色管理、权限控制 | ✅ |
| 能力中心 | 能力发现、安装、配置 | ✅ |
| 场景管理 | 场景创建、激活、执行 | ✅ |
| LLM对话 | 多模型对话、流式响应 | ✅ |
| 工作台 | 任务管理、待办事项 | ✅ |
| 系统配置 | LLM配置、数据库配置 | ✅ |

### 角色权限

| 角色 | 权限 |
|------|------|
| admin | 系统管理、能力管理、配置管理 |
| user | 工作台、能力使用、个人设置 |
| installer | 技能安装、系统配置 |

---

## API接口

### 认证接口

| 方法 | 路径 | 说明 |
|------|------|------|
| POST | /api/auth/login | 用户登录 |
| POST | /api/auth/logout | 用户登出 |
| GET | /api/auth/menu | 获取用户菜单 |

### 能力接口

| 方法 | 路径 | 说明 |
|------|------|------|
| GET | /api/capability/list | 获取能力列表 |
| GET | /api/capability/{id} | 获取能力详情 |
| POST | /api/capability/install | 安装能力 |

### LLM接口

| 方法 | 路径 | 说明 |
|------|------|------|
| GET | /api/llm/providers | 获取LLM提供商列表 |
| GET | /api/llm/models | 获取模型列表 |
| POST | /api/llm/chat | 发起对话 |

---

## 开发指南

### 技术栈

- **后端**: Spring Boot 3.4.4, Java 21
- **前端**: 原生 JavaScript, CSS3
- **数据库**: SQLite (可扩展)
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
./target/ooder-agent
```

---

## 开源协议

本项目采用 [MIT License](LICENSE) 开源协议。

---

## 相关链接

- 父项目: [super-Agent](https://gitee.com/ooderCN/super-Agent)
- SDK: [ooder-sdk](https://gitee.com/ooderCN/ooder-sdk)
- 技能库: [ooder-skills](https://gitee.com/ooderCN/ooder-skills)

---

<div align="center">

**ooderAgent 3.0.2** - 让 AI Agent 更简单

</div>
