# Ooder Agent SDK

## 1. 概述

Ooder Agent SDK 是一个功能强大的Agent开发工具包，采用"一核两翼三链"架构设计，提供了完整的Agent生命周期管理能力。本SDK支持Java 8及以上版本。

### 1.1 版本信息

| 版本 | 发布日期 | 状态 |
|------|----------|------|
| **0.7.2** | 2026-02-17 | 当前版本 |
| 0.7.1 | 2026-02-15 | 已发布 |
| 0.6.6 | 2026-01-31 | 已发布 |

### 1.2 核心特性

| 特性 | 描述 |
|------|------|
| **南北向分层架构** | 核心抽象层、南向服务层、北向服务层三层分离 |
| **能力中心** | 能力规范、分发、管理、监测、协同五大服务 |
| **用户-组织-域模型** | 支持多租户、多组织、多域的复杂业务场景 |
| **增强场景组** | 自组网、LLM介入、离线运行、多点分支 |
| **三链引擎** | SkillsFlow、数据中心、数据工具链飞轮 |

## 2. 架构设计

### 2.1 一核两翼三链架构

```
┌─────────────────────────────────────────────────────────────────────────────────┐
│                           Ooder Agent SDK 0.7.2 架构                              │
├─────────────────────────────────────────────────────────────────────────────────┤
│                                                                                 │
│  ┌─────────────────────────────────────────────────────────────────────────┐   │
│  │                          应用层（Application Layer）                       │   │
│  │  ┌───────────┐  ┌───────────┐  ┌───────────┐  ┌───────────┐            │   │
│  │  │ 用户应用   │  │ 组织管理   │  │ 技能市场   │  │ 协作平台   │            │   │
│  │  └───────────┘  └───────────┘  └───────────┘  └───────────┘            │   │
│  └─────────────────────────────────────────────────────────────────────────┘   │
│                                        ↕                                        │
│  ┌─────────────────────────────────────────────────────────────────────────┐   │
│  │                        三链引擎层（Three Chains Layer）                     │   │
│  │  ┌─────────────────┐  ┌─────────────────┐  ┌─────────────────┐          │   │
│  │  │   SkillsFlow    │  │    数据中心      │  │ 数据工具链飞轮   │          │   │
│  │  │   (技能链)       │  │    (数据链)      │  │   (工具链)       │          │   │
│  │  └─────────────────┘  └─────────────────┘  └─────────────────┘          │   │
│  └─────────────────────────────────────────────────────────────────────────┘   │
│                                        ↕                                        │
│  ┌─────────────────────────────────────────────────────────────────────────┐   │
│  │                        能力中心层（Capability Center Layer）               │   │
│  │  ┌──────────┐ ┌──────────┐ ┌──────────┐ ┌──────────┐ ┌──────────┐      │   │
│  │  │ 能力规范  │ │ 能力分发  │ │ 能力管理  │ │ 能力监测  │ │ 能力协同  │      │   │
│  │  └──────────┘ └──────────┘ └──────────┘ └──────────┘ └──────────┘      │   │
│  └─────────────────────────────────────────────────────────────────────────┘   │
│                                        ↕                                        │
│  ┌─────────────────────────────────────────────────────────────────────────┐   │
│  │                        南北向服务层（Northbound/Southbound Layer）         │   │
│  │  ┌─────────────────────────────┐  ┌─────────────────────────────┐       │   │
│  │  │       北向服务层             │  │       南向服务层             │       │   │
│  │  │  - 域管理协议  - 立体观测    │  │  - 发现协议    - 角色协议    │       │   │
│  │  │  - P2P网络    - 域级安全     │  │  - 登录协议    - 协作协议    │       │   │
│  │  └─────────────────────────────┘  └─────────────────────────────┘       │   │
│  └─────────────────────────────────────────────────────────────────────────┘   │
│                                        ↕                                        │
│  ┌─────────────────────────────────────────────────────────────────────────┐   │
│  │                        核心抽象层（Core Abstraction Layer）                │   │
│  │  ┌─────────────────┐  ┌─────────────────┐  ┌─────────────────┐          │   │
│  │  │ 核心网络抽象    │  │ 核心安全抽象    │  │ 核心协作抽象    │          │   │
│  │  └─────────────────┘  └─────────────────┘  └─────────────────┘          │   │
│  └─────────────────────────────────────────────────────────────────────────┘   │
│                                                                                 │
└─────────────────────────────────────────────────────────────────────────────────┘
```

## 3. 核心模块

### 3.1 能力中心

能力中心是SDK的核心服务层，提供五大服务：

| 服务 | 接口 | 描述 |
|------|------|------|
| **能力规范服务** | `CapabilitySpecService` | 能力定义、验证、版本管理 |
| **能力分发服务** | `CapabilityDistService` | 能力分发、部署、状态追踪 |
| **能力管理服务** | `CapabilityMgtService` | 能力注册、启用/禁用、版本回滚 |
| **能力监测服务** | `CapabilityMonService` | 日志采集、指标监控、告警管理 |
| **能力协同服务** | `CapabilityCoopService` | 编排、场景组、链式调用 |

### 3.2 南向协议

南向协议提供Agent内部通信能力：

| 协议 | 接口 | 描述 |
|------|------|------|
| **发现协议** | `DiscoveryProtocol` | Agent发现、网络探测 |
| **角色协议** | `RoleProtocol` | 角色决策、角色注册 |
| **登录协议** | `LoginProtocol` | 用户认证、会话管理 |
| **协作协议** | `CollaborationProtocol` | 场景组协作、任务协同 |

### 3.3 北向协议

北向协议提供外部系统交互能力：

| 协议 | 接口 | 描述 |
|------|------|------|
| **域管理协议** | `DomainManagementProtocol` | 域创建、成员管理、策略配置 |
| **立体观测协议** | `ObservationProtocol` | 指标采集、日志查询、追踪分析 |

### 3.4 Nexus服务

Nexus服务提供核心连接管理：

| 服务 | 接口 | 描述 |
|------|------|------|
| **连接服务** | `NexusService` | 网络连接、状态管理 |
| **离线服务** | `OfflineService` | 离线模式、数据同步 |
| **私有资源服务** | `PrivateResourceService` | 技能安装、存储管理、分享链接 |

## 4. 快速开始

### 4.1 添加依赖

```xml
<dependency>
    <groupId>net.ooder</groupId>
    <artifactId>agent-sdk</artifactId>
    <version>0.7.2</version>
</dependency>
```

### 4.2 初始化SDK

```java
import net.ooder.sdk.capability.CapabilityCenter;
import net.ooder.sdk.capability.impl.CapabilityCenterImpl;
import net.ooder.sdk.nexus.NexusService;
import net.ooder.sdk.nexus.NexusServiceFactory;

public class SDKExample {
    public static void main(String[] args) {
        CapabilityCenter capabilityCenter = new CapabilityCenterImpl();
        capabilityCenter.initialize();
        
        NexusService nexusService = NexusServiceFactory.create();
        NexusConfig config = new NexusConfig();
        config.setNodeId("node-001");
        config.setNodeName("Test Node");
        
        nexusService.start(config).join();
        
        System.out.println("SDK initialized successfully!");
    }
}
```

### 4.3 使用能力中心

```java
import net.ooder.sdk.capability.*;
import net.ooder.sdk.capability.model.*;

public class CapabilityExample {
    public static void main(String[] args) {
        CapabilityCenter capabilityCenter = new CapabilityCenterImpl();
        capabilityCenter.initialize();
        
        CapabilitySpecService specService = capabilityCenter.getSpecService();
        
        SpecDefinition definition = new SpecDefinition();
        definition.setName("MyCapability");
        definition.setType(CapabilityType.SKILL);
        definition.setVersion("1.0.0");
        
        CapabilitySpec spec = specService.registerSpec(definition).join();
        System.out.println("Capability registered: " + spec.getSpecId());
    }
}
```

## 5. 配置参考

### 5.1 核心配置

```properties
ooder.sdk.node.id=node-001
ooder.sdk.node.name=MyNode
ooder.sdk.network.broadcast-address=255.255.255.255
ooder.sdk.network.default-port=9001
```

### 5.2 安全配置

```properties
ooder.sdk.security.jwt-secret=your-secret-key
ooder.sdk.security.jwt-expiration=86400000
ooder.sdk.security.encryption-enabled=true
```

### 5.3 监控配置

```properties
ooder.sdk.monitoring.enabled=true
ooder.sdk.monitoring.interval=30000
ooder.sdk.monitoring.retention-days=7
```

## 6. 文档资源

| 文档 | 路径 | 描述 |
|------|------|------|
| **总体架构手册** | `docs/architecture/OVERALL_ARCHITECTURE.md` | 一核两翼三链架构设计 |
| **南北向架构** | `docs/architecture/NORTHBOUND_SOUTHBOUND_ARCHITECTURE.md` | 南北向分层设计 |
| **核心抽象层** | `docs/architecture/CORE_ABSTRACTION_LAYER.md` | 核心接口抽象设计 |
| **能力中心规范** | `docs/manuals/CAPABILITY_CENTER_SPECIFICATION.md` | 能力中心详细设计 |
| **南向服务手册** | `docs/manuals/SOUTHBOUND_SERVICE_MANUAL.md` | 南向服务使用指南 |
| **北向服务手册** | `docs/manuals/NORTHBOUND_SERVICE_MANUAL.md` | 北向服务使用指南 |
| **升级指南** | `docs/manuals/SDK_0.7.2_UPGRADE_GUIDE.md` | 版本升级指南 |

## 7. 版本历史

| 版本 | 日期 | 变更内容 |
|------|------|----------|
| **0.7.2** | 2026-02-17 | 数据类重构到model包，修复编译问题，完善测试覆盖 |
| 0.7.1 | 2026-02-15 | 能力中心五大服务实现，南北向协议完善 |
| 0.6.6 | 2026-01-31 | 初始版本，核心模块实现 |

## 8. 贡献指南

欢迎贡献代码和提出问题。请遵循以下步骤：

1. Fork 仓库
2. 创建特性分支
3. 提交更改
4. 推送到分支
5. 创建拉取请求

## 9. 许可证

本SDK使用Apache 2.0许可证。
