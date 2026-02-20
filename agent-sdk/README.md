# Ooder Agent SDK

[![License: MIT](https://img.shields.io/badge/License-MIT-yellow.svg)](https://opensource.org/licenses/MIT)
[![Version](https://img.shields.io/badge/version-0.7.3-blue.svg)]()
[![Java](https://img.shields.io/badge/Java-8+-green.svg)](https://openjdk.org/)

## 概述

Ooder Agent SDK 是 Ooder Agent 平台的核心开发工具包，提供 Agent 生命周期管理、技能发现与安装、场景配置、网络通信等核心能力。

## 版本信息

| 版本 | 发布日期 | 说明 |
|------|----------|------|
| **0.7.3** | 2026-02-20 | 协议增强、云托管、GitHub/Gitee 发现 |
| 0.7.2 | 2026-02-17 | 能力中心、南北向协议完善 |
| 0.7.1 | 2026-02-15 | 场景驱动架构 |
| 0.6.6 | 2026-01-31 | 配置体系优化 |

## 0.7.3 新特性

### 协议体系

| 协议 | 说明 |
|------|------|
| DiscoveryProtocol | 9 种发现方法 |
| LoginProtocol | 本地认证，离线支持 |
| CollaborationProtocol | 场景组协作 |
| OfflineService | 离线服务 |
| EventBus | 事件总线 |
| CloudHostingProtocol | K8s 云托管 |

### 发现方法

```java
public enum DiscoveryMethod {
    UDP_BROADCAST,      // UDP 广播
    DHT_KADEMLIA,       // DHT 发现
    MDNS_DNS_SD,        // mDNS 服务发现
    SKILL_CENTER,       // 技能中心
    LOCAL_FS,           // 本地文件系统
    GITHUB,             // GitHub 仓库 (v0.7.3)
    GITEE,              // Gitee 仓库 (v0.7.3)
    GIT_REPOSITORY,     // 通用 Git 仓库 (v0.7.3)
    AUTO                // 自动检测
}
```

## 模块结构

```
agent-sdk/
├── src/main/java/net/ooder/sdk/
│   ├── OoderSDK.java              # SDK 入口
│   ├── api/                       # API 接口
│   │   ├── agent/                 # Agent 接口
│   │   ├── skill/                 # 技能接口
│   │   └── scene/                 # 场景接口
│   ├── common/                    # 公共模块
│   │   ├── annotation/            # 注解
│   │   ├── enums/                 # 枚举
│   │   └── exception/             # 异常
│   ├── infra/                     # 基础设施
│   │   ├── config/                # 配置
│   │   ├── lifecycle/             # 生命周期
│   │   └── event/                 # 事件
│   ├── southbound/                # 南向协议
│   │   └── protocol/              # 协议实现
│   └── northbound/                # 北向协议
│       └── protocol/              # 协议实现
└── pom.xml
```

## 快速开始

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

SDKConfiguration config = new SDKConfiguration();
config.setAgentId("my-agent-001");
config.setAgentName("My Agent");
config.setEndpoint("http://localhost:8080");

OoderSDK sdk = OoderSDK.builder()
    .configuration(config)
    .build();

sdk.initialize();
sdk.start();
```

### 创建 End Agent

```java
import net.ooder.sdk.api.agent.EndAgent;

EndAgent endAgent = sdk.createEndAgent();
endAgent.start();

// 获取场景管理器
SceneManager sceneManager = endAgent.getSceneManager();

// 加入场景
SceneJoinResult result = sceneManager.joinScene("auth-scene");
```

### 安装技能

```java
import net.ooder.sdk.api.skill.SkillPackageManager;
import net.ooder.sdk.common.enums.DiscoveryMethod;

SkillPackageManager packageManager = sdk.getSkillPackageManager();

InstallRequest request = new InstallRequest();
request.setSkillId("skill-org-feishu");
request.setVersion("0.7.3");
request.setDiscoveryMethod(DiscoveryMethod.GITHUB);

InstallResult result = packageManager.install(request).join();
```

## 核心 API

### OoderSDK

| 方法 | 说明 |
|------|------|
| `initialize()` | 初始化 SDK |
| `start()` | 启动 SDK |
| `stop()` | 停止 SDK |
| `createEndAgent()` | 创建 End Agent |
| `createRouteAgent()` | 创建 Route Agent |
| `getSkillPackageManager()` | 获取技能包管理器 |

### SkillPackageManager

| 方法 | 说明 |
|------|------|
| `install(request)` | 安装技能 |
| `uninstall(skillId)` | 卸载技能 |
| `discover(skillId, method)` | 发现技能 |
| `listInstalled()` | 列出已安装技能 |

### DiscoveryProtocol

| 方法 | 说明 |
|------|------|
| `discoverPeers()` | 发现节点 |
| `discoverMcp()` | 发现 MCP Agent |
| `startBroadcast()` | 开始广播 |
| `stopBroadcast()` | 停止广播 |

## 配置参考

```yaml
sdk:
  agentId: my-agent-001
  agentName: My Agent
  endpoint: http://localhost:8080
  udpPort: 9080
  
  discovery:
    udp:
      enabled: true
      multicastGroup: 224.0.0.1
      port: 54321
    github:
      enabled: true
      defaultOwner: ooderCN
      defaultRepo: skills
    gitee:
      enabled: true
      defaultOwner: ooderCN
      defaultRepo: skills
      
  skill:
    rootPath: ./data/skills
    
  healthCheck:
    heartbeatInterval: 5000
    timeout: 30000
```

## 相关项目

| 项目 | 说明 |
|------|------|
| [super-Agent](https://github.com/ooderCN/super-Agent) | 核心框架 |
| [ooder-Nexus](https://github.com/ooderCN/ooder-Nexus) | 分发枢纽 |
| [skills](https://github.com/ooderCN/skills) | 能力库 |
| [common](https://github.com/ooderCN/common) | 企业开发包 |

## 许可证

MIT License

---

**Made with ❤️ by Ooder Team**
