# ooderNexus - 个人版 AI Agent

<div align="center">

**面向个人用户的 AI Agent 平台**

[![License](https://img.shields.io/badge/license-MIT-blue.svg)](LICENSE)
[![Java](https://img.shields.io/badge/Java-21-orange.svg)](https://openjdk.org/)
[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.4.4-brightgreen.svg)](https://spring.io/projects/spring-boot)
[![Version](https://img.shields.io/badge/version-3.0.2-green.svg)](https://gitee.com/ooderCN/super-Agent)

[在线文档](https://gitee.com/ooderCN/super-Agent) | [快速开始](#快速开始)

</div>

---

## 📋 目录

- [项目简介](#项目简介)
- [版本信息](#版本信息)
- [核心特性](#核心特性)
- [快速开始](#快速开始)
- [功能模块](#功能模块)
- [开发指南](#开发指南)
- [开源协议](#开源协议)

---

## 项目简介

ooderNexus 是面向个人用户的 AI Agent 平台，提供轻量级部署和完整的 Agent 功能。基于 ooder-agent 核心框架，专注于个人使用场景，简化配置，快速上手。

### 适用场景

- 🏠 个人助手
- 📝 知识管理
- 🤖 AI对话
- 📊 任务管理

---

## 版本信息

| 组件 | 版本 |
|------|------|
| ooderNexus | 3.0.2 |
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

---

## 核心特性

### 🚀 轻量部署
- 单机部署，5分钟启动
- 最小化依赖
- SQLite 内置数据库

### 🤖 LLM 集成
- 多模型支持 (DeepSeek, 百度千帆, 阿里百炼)
- 流式对话
- 上下文记忆

### 📊 场景管理
- 场景创建、激活、执行
- 场景模板
- 场景历史

### 🔌 技能扩展
- 动态安装技能包
- 技能市场
- 技能配置

---

## 快速开始

### 环境要求

- JDK 21+
- Maven 3.8+
- 4GB+ 内存

### 编译运行

```bash
# 编译
mvn clean compile

# 运行 (端口: 8098)
mvn spring-boot:run

# 打包
mvn clean package -DskipTests
```

### Docker 部署

```bash
# 构建镜像
docker build -t ooder-nexus:3.0.2 .

# 运行容器
docker run -d -p 8098:8098 ooder-nexus:3.0.2
```

### 访问地址

- 控制台: http://localhost:8098/console/
- 登录页: http://localhost:8098/console/pages/login.html
- 工作台: http://localhost:8098/console/pages/workbench.html

---

## 功能模块

| 模块 | 说明 | 状态 |
|------|------|------|
| 首页 | 数据概览、快捷入口 | ✅ |
| 能力中心 | 能力发现、安装、使用 | ✅ |
| 工作台 | 任务管理、待办事项 | ✅ |
| LLM配置 | 模型配置、对话管理 | ✅ |
| 系统配置 | 数据库配置、系统设置 | ✅ |

### 角色权限

| 角色 | 权限 |
|------|------|
| user | 所有功能 |

---

## 开发指南

### 项目结构

```
ooder-nexus/
├── src/main/java/net/ooder/nexus/
│   └── NexusApplication.java      # 启动类
├── src/main/resources/
│   ├── application.yml            # 配置文件
│   └── static/console/            # 前端资源
│       ├── css/
│       ├── js/
│       ├── pages/
│       └── menu-config.json
└── pom.xml
```

### 技术栈

- **后端**: Spring Boot 3.4.4, Java 21
- **前端**: 原生 JavaScript, CSS3
- **数据库**: SQLite
- **AI**: DeepSeek, 百度千帆, 阿里百炼

---

## 开源协议

本项目采用 [MIT License](LICENSE) 开源协议。

---

## 相关链接

- 父项目: [super-Agent](https://gitee.com/ooderCN/super-Agent)
- 核心框架: [ooder-agent](../ooder-agent/README.md)
- 企业版: [ooder-nexus-enterprise](../ooder-nexus-enterprise/README.md)
- SDK: [ooder-sdk](https://gitee.com/ooderCN/ooder-sdk)

---

<div align="center">

**ooderNexus 3.0.2** - 个人 AI 助手

</div>
