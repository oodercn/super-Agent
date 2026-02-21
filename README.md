# Ooder SuperAgent - 企业级 AI 能力分发与自动化协作框架

[![License: MIT](https://img.shields.io/badge/License-MIT-yellow.svg)](https://opensource.org/licenses/MIT)
[![Version](https://img.shields.io/badge/version-0.7.3-blue.svg)](https://github.com/ooderCN/super-Agent)
[![Java](https://img.shields.io/badge/Java-8+-green.svg)](https://openjdk.org/)

## 项目概述

Ooder SuperAgent 是一套基于 MIT 协议的开源企业级 AI 能力分发与自动化协作框架，采用 P2P 网络架构，支持多 Agent 协作、Skill 模块化能力封装和场景化编排，旨在实现去中心化的分布式 AI 协同与自动化流程管理。

### 核心特性

- **P2P 分布式架构**：去中心化的节点发现和通信
- **三层 Agent 架构**：MCP Agent → Route Agent → End Agent
- **场景驱动设计**：基于场景的能力编排和协作
- **技能模块化**：可插拔的 Skill 能力封装
- **离线支持**：网络断开时的本地优先执行
- **云原生部署**：支持 Kubernetes 集群部署

## 版本信息

| 版本 | 发布日期 | 状态 | 说明 |
|------|----------|------|------|
| **0.7.3** | 2026-02-20 | 当前版本 | 协议增强、云托管、GitHub/Gitee 发现 |
| 0.7.2 | 2026-02-17 | 已发布 | 能力中心、南北向协议完善 |
| 0.7.1 | 2026-02-15 | 已发布 | 场景驱动架构 |
| 0.6.6 | 2026-01-31 | 已发布 | 配置体系优化 |

## 0.7.3 版本新特性

### 协议体系增强

| 协议 | 说明 |
|------|------|
| **DiscoveryProtocol** | 9 种发现方法 (UDP/DHT/mDNS/GitHub/Gitee/...) |
| **LoginProtocol** | 本地认证协议，支持离线认证 |
| **CollaborationProtocol** | 场景组协作协议，多 Agent 协同 |
| **OfflineService** | 离线服务，网络断开时运行 |
| **EventBus** | 事件总线，模块解耦 |
| **CloudHostingProtocol** | 云托管协议，K8s 部署支持 |

### 技能发现方法

```java
public enum DiscoveryMethod {
    UDP_BROADCAST,      // UDP 广播发现
    DHT_KADEMLIA,       // DHT 发现
    MDNS_DNS_SD,        // mDNS 服务发现
    SKILL_CENTER,       // 技能中心发现
    LOCAL_FS,           // 本地文件系统发现
    GITHUB,             // GitHub 仓库发现 (v0.7.3)
    GITEE,              // Gitee 仓库发现 (v0.7.3)
    GIT_REPOSITORY,     // 通用 Git 仓库发现 (v0.7.3)
    AUTO                // 自动检测
}
```

## 项目生态

```
┌─────────────────────────────────────────────────────────────────┐
│                    Ooder 开源生态                                │
├─────────────────────────────────────────────────────────────────┤
│                                                                 │
│  ┌─────────────────────────────────────────────────────────┐   │
│  │                    super-Agent (本仓库)                   │   │
│  │  ├── agent-skillcenter   技能中心                        │   │
│  │  ├── ooder-Nexus         分发枢纽 (个人版)               │   │
│  │  └── ooder-Nexus-Enterprise 分发枢纽 (企业版)            │   │
│  └─────────────────────────────────────────────────────────┘   │
│                              │                                   │
│              ┌───────────────┼───────────────┐                  │
│              │               │               │                  │
│              ▼               ▼               ▼                  │
│     ┌─────────────┐  ┌─────────────┐  ┌─────────────┐          │
│     │ ooder-sdk   │  │ ooder-skills│  │   common    │          │
│     │ (SDK核心)   │  │ (能力库)    │  │ (企业套包)  │          │
│     └─────────────┘  └─────────────┘  └─────────────┘          │
│                                                                 │
└─────────────────────────────────────────────────────────────────┘
```

## 核心架构

### 一核两翼三链架构

```
┌─────────────────────────────────────────────────────────────────┐
│                    Ooder Agent SDK 0.7.3 架构                    │
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

### 三层 Agent 架构

| Agent 类型 | 职责 | 说明 |
|-----------|------|------|
| **MCP Agent** | 主控节点 | AI 能力分发和管理，网络协调和安全认证 |
| **Route Agent** | 路由节点 | 命令路由和任务协调，动态拓扑和负载均衡 |
| **End Agent** | 终端节点 | 数据采集、处理和执行，本地优先执行 |

## 快速开始

### 环境要求

- JDK 1.8+
- Maven 3.6+
- Spring Boot 2.7.0

### 添加依赖

```xml
<dependency>
    <groupId>net.ooder</groupId>
    <artifactId>agent-sdk</artifactId>
    <version>0.7.3</version>
</dependency>
```

### 初始化 SDK

```java
import net.ooder.sdk.OoderSDK;
import net.ooder.sdk.infra.config.SDKConfiguration;

public class QuickStart {
    public static void main(String[] args) {
        SDKConfiguration config = new SDKConfiguration();
        config.setAgentId("my-agent-001");
        config.setAgentName("My Agent");
        config.setEndpoint("http://localhost:8080");
        
        OoderSDK sdk = OoderSDK.builder()
            .configuration(config)
            .build();
        
        sdk.initialize();
        sdk.start();
        
        System.out.println("SDK started successfully!");
    }
}
```

### 从 GitHub/Gitee 安装技能

```java
import net.ooder.sdk.api.skill.SkillPackageManager;
import net.ooder.sdk.common.enums.DiscoveryMethod;

SkillPackageManager packageManager = sdk.getSkillPackageManager();

InstallRequest request = new InstallRequest();
request.setSkillId("skill-org-feishu");
request.setVersion("0.7.3");
request.setDiscoveryMethod(DiscoveryMethod.GITHUB);

packageManager.install(request).join();
```

## 项目结构

```
super-Agent/
├── agent-skillcenter/            # 技能中心
│   ├── src/main/java/net/ooder/skillcenter/
│   │   ├── controller/           # 控制器
│   │   ├── service/              # 服务层
│   │   ├── market/               # 技能市场
│   │   └── p2p/                  # P2P 网络
│   └── pom.xml
│
├── ooder-Nexus/                  # 分发枢纽 (个人版)
│   ├── src/main/resources/
│   │   ├── scenes/               # 场景配置
│   │   ├── skills/               # 技能配置
│   │   └── static/               # 静态资源
│   └── pom.xml
│
├── ooder-Nexus-Enterprise/       # 分发枢纽 (企业版)
│   ├── src/main/resources/
│   │   ├── scenes/               # 场景配置
│   │   └── static/               # 静态资源
│   └── pom.xml
│
├── examples/                     # 示例代码
│   ├── end-agent/
│   ├── mcp-agent/
│   └── route-agent/
│
├── docs/                         # 文档
│   ├── overview/
│   └── skills/
│
├── protocol-release/             # 协议发布文档
│
└── README.md
```

> **注意**: agent-sdk 已迁移到独立仓库 [ooder-sdk](https://github.com/oodercn/ooder-sdk)

## 协议文档

### v0.7.3 协议体系

| 协议 | 文档 | 说明 |
|------|------|------|
| 主协议 | [protocol-main-v0.7.3.md](docs/protocols/protocol-main-v0.7.3.md) | 协议总览 |
| 发现协议 | [skill-discovery-protocol-v0.7.3.md](docs/protocols/skill-discovery-protocol-v0.7.3.md) | 技能发现 |
| 技能包协议 | [skill-package-protocol-v0.7.3.md](docs/protocols/skill-package-protocol-v0.7.3.md) | 技能包规范 |
| 云托管协议 | [cloud-hosting-protocol-v0.7.3.md](docs/protocols/cloud-hosting-protocol-v0.7.3.md) | K8s 托管 |

### 核心协议

| 协议类型 | 协议名称 | 说明 |
|---------|---------|------|
| **南向协议** | DiscoveryProtocol | Agent 发现、网络探测 |
| | LoginProtocol | 用户认证、会话管理 |
| | RoleProtocol | 角色决策、角色注册 |
| | CollaborationProtocol | 场景组协作、任务协同 |
| **北向协议** | DomainManagementProtocol | 域创建、成员管理 |
| | ObservationProtocol | 指标采集、日志查询 |

## 相关项目

| 项目 | 说明 | 地址 |
|------|------|------|
| **ooder-sdk** | Agent SDK 核心 | [github.com/oodercn/ooder-sdk](https://github.com/oodercn/ooder-sdk) |
| **ooder-skills** | 共享能力库 | [github.com/oodercn/ooder-skills](https://github.com/oodercn/ooder-skills) |
| **common** | 企业开发套包 | [github.com/oodercn/common](https://github.com/oodercn/common) |

## 文档资源

| 文档 | 说明 |
|------|------|
| [协议文档](protocol-release/) | 协议发布文档 |
| [开发指南](docs/overview/) | 开发概述 |
| [技能开发](docs/skills/) | 技能开发规范 |

## 贡献指南

欢迎贡献代码和提出问题！

1. Fork 仓库
2. 创建特性分支 (`git checkout -b feature/amazing-feature`)
3. 提交更改 (`git commit -m 'Add amazing feature'`)
4. 推送到分支 (`git push origin feature/amazing-feature`)
5. 创建 Pull Request

## 许可证

本项目采用 MIT 许可证 - 详见 [LICENSE](LICENSE) 文件

## 联系方式

- GitHub: [https://github.com/ooderCN/super-Agent](https://github.com/ooderCN/super-Agent)
- Gitee: [https://gitee.com/ooderCN/super-Agent](https://gitee.com/ooderCN/super-Agent)

---

**Made with ❤️ by Ooder Team**
