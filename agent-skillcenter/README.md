# Agent SkillCenter

[![License: MIT](https://img.shields.io/badge/License-MIT-yellow.svg)](https://opensource.org/licenses/MIT)
[![Java 8](https://img.shields.io/badge/Java-8-blue.svg)]()
[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-2.7.0-green.svg)]()
[![Version](https://img.shields.io/badge/version-3.0-blue.svg)]()

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

### v3.0 新特性

- **SDK 适配层**: 完整的 SDK 适配器模式
- **云托管支持**: Kubernetes 集群部署
- **多语言运行时**: Java/Node/Python 执行器
- **DTO 分层架构**: 完整的请求/响应/查询 DTO
- **资源管理**: ResourceAllocator 资源分配
- **编排服务**: OrchestrationService 编排能力
- **场景管理**: SceneManager 场景管理
- **个人 AI 中心**: PersonalAICenter 个人 AI 配置
- **网络安全**: NetworkManager 网络管理与安全

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
java -jar target/agent-skillcenter-3.0.jar
```

### 访问应用

- 控制台: http://localhost:8083/skillcenter/console/index.html
- 默认端口: 8083

## 项目结构

```
agent-skillcenter/
├── src/main/java/net/ooder/
│   ├── nexus/skillcenter/           # 控制器和 DTO 层
│   │   ├── controller/              # REST API 控制器
│   │   ├── dto/                     # 数据传输对象
│   │   ├── exception/               # 异常处理
│   │   └── model/                   # 模型类
│   └── skillcenter/                 # 核心服务层
│       ├── sdk/                     # SDK 适配器
│       ├── service/                 # 业务逻辑层
│       ├── market/                  # 技能市场
│       ├── p2p/                     # P2P 网络通信
│       ├── storage/                 # 存储管理
│       ├── execution/               # 执行引擎
│       ├── scene/                   # 场景管理
│       ├── personalai/              # 个人 AI 中心
│       ├── resources/               # 资源管理
│       ├── runtime/                 # 运行时执行器
│       └── monitoring/              # 监控服务
├── src/main/resources/
│   ├── static/console/              # 前端控制台
│   ├── application.yml              # 应用配置
│   └── application-sdk.yml          # SDK 配置
├── pom.xml
└── README.md
```

## 配置说明

### 应用配置 (application.yml)

```yaml
server:
  port: 8083
  servlet:
    context-path: /skillcenter

skillcenter:
  # 技能执行超时时间（毫秒）
  execution-timeout: 30000
  # 最大技能并发数
  max-concurrent-executions: 100
  # SDK配置
  sdk:
    mode: sdk
    enable-logging: true
```

### 技能发现配置

```yaml
skill:
  discovery:
    # GitHub发现配置
    github:
      enabled: true
      api-url: https://api.github.com
      token: ${GITHUB_TOKEN:}
      default-owner: ${GITHUB_OWNER:}
      skills-path: skills
    # Gitee发现配置
    gitee:
      enabled: true
      api-url: https://gitee.com/api/v5
      token: ${GITEE_TOKEN:}
      default-owner: ${GITEE_OWNER:}
      skills-path: skills
    # 缓存TTL（毫秒）
    cache-ttl: 3600000
```

## API 文档

API 基础路径: `/skillcenter/api`

### 主要接口

| 接口 | 路径 | 方法 | 说明 |
|------|------|------|------|
| 技能列表 | /api/skills/list | POST | 获取所有技能 |
| 技能详情 | /api/skills/get | POST | 获取技能详情 |
| 创建技能 | /api/skills/add | POST | 创建新技能 |
| 更新技能 | /api/skills/update | POST | 更新技能 |
| 删除技能 | /api/skills/delete | POST | 删除技能 |
| 执行技能 | /api/skills/{skillId}/execute | POST | 执行技能 |
| 审核技能 | /api/skills/{skillId}/approve | POST | 审核技能 |
| 拒绝技能 | /api/skills/{skillId}/reject | POST | 拒绝技能 |
| 仪表盘统计 | /api/dashboard | POST | 获取系统概览统计数据 |
| 执行统计 | /api/dashboard/execution-stats | POST | 获取技能执行统计数据 |
| 市场统计 | /api/dashboard/market-stats | POST | 获取市场活跃度统计数据 |
| 系统统计 | /api/dashboard/system-stats | POST | 获取系统资源使用统计数据 |

## 版本历史

| 版本 | 说明 |
|------|------|
| 3.0 | 场景管理、个人 AI 中心、网络安全 |
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
