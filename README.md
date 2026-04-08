# Ooder SuperAgent 3.0.2 - 企业级 AI Agent 核心框架

<div align="center">

[![License: MIT](https://img.shields.io/badge/License-MIT-yellow.svg)](https://opensource.org/licenses/MIT)
[![Version](https://img.shields.io/badge/version-3.0.2-blue.svg)](https://github.com/ooderCN/super-Agent)
[![Java](https://img.shields.io/badge/Java-21-orange.svg)](https://openjdk.org/)
[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.4.4-brightgreen.svg)](https://spring.io/projects/spring-boot)

**面向企业级应用的 AI Agent 操作系统**

[在线文档](https://gitee.com/ooderCN/super-Agent) | [快速开始](#快速开始) | [架构设计](#架构设计) | [升级报告](ooder-agent/UPGRADE_REPORT.md)

</div>

---

## 📋 目录

- [项目简介](#项目简介)
- [核心特性](#核心特性)
- [版本信息](#版本信息)
- [快速开始](#快速开始)
- [架构设计](#架构设计)
- [功能模块](#功能模块)
- [技术栈](#技术栈)
- [项目生态](#项目生态)
- [开发指南](#开发指南)
- [开源协议](#开源协议)

---

## 项目简介

### 什么是 Ooder SuperAgent？

Ooder SuperAgent 是一个面向企业级应用的 AI Agent 核心框架，采用模块化设计，内置核心驱动，支持通过 Gitee/GitHub 发现机制动态安装和扩展应用。它为企业提供了一个灵活、可扩展、易于管理的 AI Agent 运行平台。

### 核心价值

- **🚀 快速部署**: 最小化设计，开箱即用，5 分钟完成部署
- **🔌 灵活扩展**: 模块化架构，按需安装，动态扩展
- **🔐 安全可控**: MIT 开源协议，企业级安全，完全自主可控
- **📊 统一管理**: 集中式管理，可视化监控，简化运维
- **🤖 AI 驱动**: 内置 LLM 支持，智能决策，自动化执行
- **🌐 分布式架构**: P2P 网络，多 Agent 协作，去中心化

---

## 核心特性

### 🎯 Agent 管理系统
- Agent 会话管理
- Agent 心跳监控
- Agent 消息服务
- Agent 故障转移
- Agent 拓扑管理

### 🔌 Capability 能力中心
- 能力激活服务
- 能力连接器 (Http/WebSocket/Internal)
- 能力安装服务
- 能力调用服务
- 能力注册表
- 密钥管理服务
- 能力状态管理
- 依赖健康检查

### 🎭 Scene 场景引擎
- 场景定义与执行
- 场景组管理
- 场景生命周期控制
- 场景模板服务

### 🤖 LLM 多模型集成
- DeepSeek 支持
- 百度千帆支持
- 阿里百炼支持
- 流式对话
- Function Calling
- 提示词索引服务
- 多级上下文管理

### 📊 Knowledge 知识库
- 知识存储和检索
- 向量嵌入服务
- 场景知识绑定
- 文档导入 (URL/文本)

### 🔄 Workflow 工作流引擎
- BPMN 2.0 支持
- 流程编排
- 任务管理
- 待办事项

### 🌐 WebSocket 实时通信
- 执行 WebSocket 处理器
- 实时消息推送
- 会话管理

### 📈 监控与审计
- 操作日志审计
- LLM 调用统计
- 能力使用统计
- 健康检查端点

---

## 版本信息

### 当前版本: 3.0.2

| 组件 | 版本 | 说明 |
|------|------|------|
| **Ooder SuperAgent** | 3.0.2 | 当前版本 |
| **Ooder SDK** | 3.0.2 | SDK 核心 |
| **Spring Boot** | 3.4.4 | 基础框架 |
| **Java** | 21 | 运行环境 |
| **SpringDoc** | 2.8.6 | API 文档 |

### 版本历史

| 版本 | 发布日期 | 状态 | 说明 |
|------|----------|------|------|
| **3.0.2** | 2026-04-08 | 当前版本 | 从 MVP (apexos) 同步完整功能 |
| 3.0.1 | 2026-04-07 | 已发布 | 基础 Agent 框架 |
| 3.0.0 | 2026-03-23 | 已发布 | 项目初始化 |

### 3.0.2 版本新特性

#### 🎉 从 MVP (apexos) 同步的完整功能

- ✅ **Agent 管理模块** (27 文件) - 完整的 Agent 生命周期管理
- ✅ **Capability 能力管理模块** (75 文件) - 能力注册、激活、调用、监控
- ✅ **Config 配置管理模块** (20+ 文件) - 配置加载、继承、存储
- ✅ **Controller 控制器层** (30+ 文件) - 完整的 REST API
- ✅ **DTO 数据传输对象** (212+ 文件) - 完整的数据模型
- ✅ **Service 服务层** (32 文件) - 核心业务逻辑
- ✅ **其他核心模块** - Engine, Event, Knowledge, LLM, Notification, Snapshot, SPI, Template, Tool, WebSocket

详见: [UPGRADE_REPORT.md](ooder-agent/UPGRADE_REPORT.md)

---

## 快速开始

### 环境要求

- JDK 21+
- Maven 3.8+
- 8GB+ 内存

### 编译运行

```bash
# 克隆仓库
git clone https://gitee.com/ooderCN/super-Agent.git
cd super-Agent

# 编译
mvn clean compile

# 运行
cd ooder-agent
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

### 访问应用

- **主页**: http://localhost:8099
- **API 文档**: http://localhost:8099/swagger-ui.html
- **健康检查**: http://localhost:8099/actuator/health

---

## 架构设计

### 一核两翼三链架构

```
┌─────────────────────────────────────────────────────────────────┐
│                    Ooder Agent 3.0.2 架构                        │
├─────────────────────────────────────────────────────────────────┤
│                                                                 │
│  应用层                                                          │
│  ┌───────────┐  ┌───────────┐  ┌───────────┐  ┌───────────┐   │
│  │ 用户应用   │  │ 组织管理   │  │ 技能市场   │  │ 协作平台   │   │
│  └───────────┘  └───────────┘  └───────────┘  └───────────┘   │
│                                                                 │
│  三链引擎层                                                      │
│  ┌─────────────────┐  ┌─────────────────┐  ┌─────────────────┐ │
│  │   SkillsFlow    │  │    数据中心      │  │ 数据工具链飞轮   │ │
│  └─────────────────┘  └─────────────────┘  └─────────────────┘ │
│                                                                 │
│  能力中心层                                                      │
│  ┌──────────┐ ┌──────────┐ ┌──────────┐ ┌──────────┐ ┌──────────┐│
│  │ 能力规范  │ │ 能力分发  │ │ 能力管理  │ │ 能力监测  │ │ 能力协同  ││
│  └──────────┘ └──────────┘ └──────────┘ └──────────┘ └──────────┘│
│                                                                 │
│  南北向服务层                                                    │
│  ┌─────────────────────────────┐  ┌─────────────────────────────┐│
│  │       北向服务层             │  │       南向服务层             ││
│  │  域管理协议  立体观测        │  │  发现协议    角色协议        ││
│  │  P2P网络    域级安全         │  │  登录协议    协作协议        ││
│  └─────────────────────────────┘  └─────────────────────────────┘│
│                                                                 │
│  核心抽象层                                                      │
│  ┌─────────────────┐  ┌─────────────────┐  ┌─────────────────┐ │
│  │ 核心网络抽象    │  │ 核心安全抽象    │  │ 核心协作抽象    │ │
│  └─────────────────┘  └─────────────────┘  └─────────────────┘ │
│                                                                 │
└─────────────────────────────────────────────────────────────────┘
```

### 项目结构

```
ooder-agent/
├── src/main/java/net/ooder/agent/
│   ├── AgentApplication.java           # 主应用
│   ├── config/                          # 配置类
│   ├── controller/                      # REST 控制器
│   ├── dto/                             # 数据传输对象
│   ├── llm/                             # LLM 服务
│   ├── service/                         # 业务服务
│   └── skill/                           # 技能管理
│       └── scene/                       # 场景引擎
│           ├── agent/                   # Agent 管理
│           ├── capability/              # 能力管理
│           ├── config/                  # 配置管理
│           ├── controller/              # 场景控制器
│           ├── dto/                     # 场景 DTO
│           ├── engine/                  # 场景引擎
│           ├── event/                   # 事件处理
│           ├── knowledge/               # 知识库
│           ├── llm/                     # LLM 集成
│           ├── notification/            # 通知服务
│           ├── service/                 # 场景服务
│           ├── snapshot/                # 快照管理
│           ├── spi/                     # SPI 接口
│           ├── template/                # 模板管理
│           ├── tool/                    # 工具集成
│           └── websocket/               # WebSocket
├── src/main/resources/
│   ├── application.yml                  # 配置文件
│   └── static/console/                  # 前端资源
└── pom.xml
```

---

## 功能模块

### 核心模块

| 模块 | 说明 | 文件数 |
|------|------|--------|
| **Agent 管理** | Agent 会话、心跳、消息、故障转移 | 27 |
| **Capability 能力** | 能力激活、连接、安装、调用、注册 | 75 |
| **Config 配置** | 配置加载、继承、存储、初始化 | 20+ |
| **Controller 控制器** | REST API 控制器 | 30+ |
| **DTO 数据对象** | 数据传输对象 | 212+ |
| **Service 服务** | 核心业务服务 | 32 |
| **LLM 集成** | 多模型支持、提示词、上下文 | 27 |
| **Knowledge 知识库** | 知识存储、嵌入、绑定 | 2 |
| **Event 事件** | 事件监听、发布 | 7 |
| **Engine 引擎** | 上下文信任、脚本执行 | 4 |
| **Notification 通知** | 场景通知服务 | 1 |
| **Snapshot 快照** | 版本快照管理 | 4 |
| **WebSocket** | 实时通信 | 2 |

### 功能统计

- **Java 文件**: 400+
- **配置文件**: 5
- **前端文件**: 100+
- **代码行数**: 60,000+

---

## 技术栈

### 后端技术栈

| 技术 | 版本 | 说明 |
|------|------|------|
| **Java** | 21 | LTS 版本，虚拟线程支持 |
| **Spring Boot** | 3.4.4 | 基础框架 |
| **Ooder SDK** | 3.0.2 | Agent SDK 核心 |
| **SpringDoc** | 2.8.6 | OpenAPI 文档 |
| **SQLite** | 3.49.1 | 嵌入式数据库 |
| **MVEL** | 2.5.0 | 表达式引擎 |
| **FastJSON2** | 2.0.57 | JSON 处理 |
| **Lombok** | 1.18.30 | 代码简化 |

### 前端技术栈

| 技术 | 说明 |
|------|------|
| **JavaScript** | ES6+ 原生实现 |
| **CSS3** | 组件化样式 |
| **RemixIcon** | 图标库 |
| **模块化** | 组件化设计 |

### AI 模型支持

| 模型 | 提供商 | 说明 |
|------|--------|------|
| **DeepSeek** | DeepSeek | 深度求索 |
| **千帆** | 百度 | 百度智能云 |
| **百炼** | 阿里 | 阿里云 |

---

## 项目生态

```
┌─────────────────────────────────────────────────────────────────┐
│                    Ooder 开源生态                                │
├─────────────────────────────────────────────────────────────────┤
│                                                                 │
│  ┌─────────────────────────────────────────────────────────┐   │
│  │                    super-Agent (本仓库)                   │   │
│  │  ├── ooder-agent           Agent 核心 (3.0.2)            │   │
│  │  ├── ooder-nexus           分发枢纽 (个人版)             │   │
│  │  └── ooder-nexus-enterprise 分发枢纽 (企业版)            │   │
│  └─────────────────────────────────────────────────────────┘   │
│                              │                                   │
│              ┌───────────────┼───────────────┐                  │
│              │               │               │                  │
│              ▼               ▼               ▼                  │
│     ┌─────────────┐  ┌─────────────┐  ┌─────────────┐          │
│     │ ooder-sdk   │  │ ooder-skills│  │   common    │          │
│     │ (SDK核心)   │  │ (能力库)    │  │ (企业套包)  │          │
│     │  3.0.2      │  │  mvp/apexos │  │             │          │
│     └─────────────┘  └─────────────┘  └─────────────┘          │
│                                                                 │
└─────────────────────────────────────────────────────────────────┘
```

### 相关项目

| 项目 | 说明 | 地址 |
|------|------|------|
| **ooder-sdk** | Agent SDK 核心 (3.0.2) | [github.com/oodercn/ooder-sdk](https://github.com/oodercn/ooder-sdk) |
| **ooder-skills** | 共享能力库 (MVP/ApexOS) | [github.com/oodercn/ooder-skills](https://github.com/oodercn/ooder-skills) |
| **common** | 企业开发套包 | [github.com/oodercn/common](https://github.com/oodercn/common) |

---

## 开发指南

### 添加依赖

```xml
<dependency>
    <groupId>net.ooder</groupId>
    <artifactId>agent-sdk-core</artifactId>
    <version>3.0.2</version>
</dependency>
```

### 配置示例

```yaml
server:
  port: 8099

ooder:
  scene:
    auto-init: true
    default-group-id: sg-default
  discovery:
    use-se-sdk: true
    use-index-first: true
  llm:
    provider: deepseek
    model: deepseek-chat
```

### 从 Gitee/GitHub 安装技能

```java
import net.ooder.sdk.api.skill.SkillPackageManager;
import net.ooder.sdk.common.enums.DiscoveryMethod;

SkillPackageManager packageManager = sdk.getSkillPackageManager();

InstallRequest request = new InstallRequest();
request.setSkillId("skill-org-feishu");
request.setVersion("3.0.2");
request.setDiscoveryMethod(DiscoveryMethod.GITEE);

packageManager.install(request).join();
```

---

## 文档资源

| 文档 | 说明 |
|------|------|
| [升级报告](ooder-agent/UPGRADE_REPORT.md) | 3.0.2 升级详情 |
| [变更日志](ooder-agent/CHANGELOG.md) | 版本变更记录 |
| [协议文档](protocol-release/) | 协议发布文档 |
| [开发指南](docs/overview/) | 开发概述 |
| [技能开发](docs/skills/) | 技能开发规范 |

---

## 贡献指南

欢迎贡献代码和提出问题！

1. Fork 仓库
2. 创建特性分支 (`git checkout -b feature/amazing-feature`)
3. 提交更改 (`git commit -m 'Add amazing feature'`)
4. 推送到分支 (`git push origin feature/amazing-feature`)
5. 创建 Pull Request

---

## 开源协议

本项目采用 [MIT License](LICENSE) 开源协议。

---

## 联系方式

- **GitHub**: [https://github.com/ooderCN/super-Agent](https://github.com/ooderCN/super-Agent)
- **Gitee**: [https://gitee.com/ooderCN/super-Agent](https://gitee.com/ooderCN/super-Agent)

---

<div align="center">

**Ooder SuperAgent 3.0.2** - 让 AI Agent 更简单

**Made with ❤️ by Ooder Team**

</div>
