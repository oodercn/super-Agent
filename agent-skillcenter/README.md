# Agent SkillCenter

[![License: MIT](https://img.shields.io/badge/License-MIT-yellow.svg)](https://opensource.org/licenses/MIT)
[![Java 8](https://img.shields.io/badge/Java-8-blue.svg)]()
[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-2.7.0-green.svg)]()
[![Version](https://img.shields.io/badge/version-2.2-blue.svg)]()

基于 Spring Boot 的技能中心管理系统，支持技能的创建、管理、市场分发和 P2P 网络共享。

## 功能特性

### 核心功能

- **技能管理**: 创建、编辑、删除、执行技能
- **技能市场**: 浏览、搜索、下载、评分技能
- **P2P 网络**: 局域网内技能共享与发现
- **用户管理**: 多用户支持与权限控制
- **群组管理**: 技能分组与共享
- **执行管理**: 技能执行历史与状态监控
- **存储管理**: 技能存储、备份与恢复
- **系统管理**: 系统配置、健康检查、日志管理

### v2.2 新特性

- **SDK 适配层**: 完整的 SDK 适配器模式
- **云托管支持**: Kubernetes 集群部署
- **多语言运行时**: Java/Node/Python 执行器
- **DTO 分层架构**: 完整的请求/响应/查询 DTO
- **资源管理**: ResourceAllocator 资源分配
- **编排服务**: OrchestrationService 编排能力

## 环境要求

- Java 8 或更高版本
- Maven 3.6+

## 快速开始

```bash
# 克隆项目
git clone https://github.com/oodercn/super-Agent.git
cd super-Agent/agent-skillcenter

# 构建项目
mvn clean package -DskipTests

# 运行项目
java -jar target/agent-skillcenter-2.2.jar
```

### 访问应用

- 控制台: http://localhost:8081/skillcenter/console/index.html
- 默认端口: 8081

## 项目结构

```
agent-skillcenter/
├── src/main/java/net/ooder/
│   ├── nexus/skillcenter/           # 控制器和 DTO 层
│   │   ├── controller/              # REST API 控制器
│   │   ├── dto/                     # 数据传输对象
│   │   └── config/                  # 配置类
│   └── skillcenter/                 # 核心服务层
│       ├── sdk/                     # SDK 适配器
│       ├── service/                 # 业务逻辑层
│       ├── market/                  # 技能市场
│       ├── p2p/                     # P2P 网络通信
│       ├── storage/                 # 存储管理
│       ├── southbound/              # 南向服务
│       └── execution/               # 执行引擎
├── src/main/resources/
│   ├── static/console/              # 前端控制台
│   └── application.yml              # 应用配置
├── pom.xml
└── README.md
```

## 配置说明

### 应用配置 (application.yml)

```yaml
server:
  port: 8081
  servlet:
    context-path: /skillcenter

skillcenter:
  sdk:
    mode: mock  # mock 或 real
    agent-id: ${AGENT_ID:SuperAgent-Default}
    agent-name: ${AGENT_NAME:SuperAgent}
```

### 数据存储

技能数据存储在项目目录下的 `skillcenter/storage/` 文件夹中：

| 文件 | 说明 |
|------|------|
| skill_listings.json | 技能列表 |
| skill_ratings.json | 技能评分 |
| skill_reviews.json | 技能评价 |
| groups.json | 群组信息 |
| executions.json | 执行记录 |

## API 文档

API 基础路径: `/skillcenter/api`

### 主要接口

| 接口 | 路径 | 说明 |
|------|------|------|
| 技能列表 | GET /api/skills | 获取所有技能 |
| 技能详情 | GET /api/skills/{id} | 获取技能详情 |
| 创建技能 | POST /api/skills | 创建新技能 |
| 更新技能 | PUT /api/skills/{id} | 更新技能 |
| 删除技能 | DELETE /api/skills/{id} | 删除技能 |
| 执行技能 | POST /api/skills/{id}/execute | 执行技能 |
| 市场技能 | GET /api/market/skills | 获取市场技能 |
| 搜索技能 | GET /api/market/skills/search | 搜索技能 |

## 版本历史

| 版本 | 说明 |
|------|------|
| 2.2 | SDK 适配层、云托管支持、多语言运行时 |
| 2.1 | DTO 分层架构、资源管理、编排服务 |
| 2.0 | 核心功能重构 |
| 1.0 | 初始版本 |

## 相关项目

| 项目 | 说明 | 地址 |
|------|------|------|
| super-Agent | 核心框架 | [GitHub](https://github.com/oodercn/super-Agent) / [Gitee](https://gitee.com/ooderCN/super-Agent) |
| ooder-sdk | SDK 核心 | [GitHub](https://github.com/oodercn/ooder-sdk) / [Gitee](https://gitee.com/ooderCN/ooder-sdk) |

## 许可证

本项目采用 [MIT 许可证](LICENSE) 开源。

## 贡献指南

欢迎提交 Issue 和 Pull Request 来改进本项目。

## 联系方式

- GitHub: https://github.com/oodercn/super-Agent
- Gitee: https://gitee.com/ooderCN/super-Agent

---

**Made with ❤️ by Ooder Team**
