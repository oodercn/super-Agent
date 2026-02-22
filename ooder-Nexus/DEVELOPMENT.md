# 开发设计文档

## 1. 项目概述

### 1.1 前言

OoderAgent V0.6.6 作为标志性版本，正式补齐了基础网络管理与Skills技能执行的核心能力，搭建起P2P AI通信的核心骨架，为后续各类AI应用落地奠定了坚实基础。但不可忽视的是，底层协议的枯燥晦涩、纯命令行的操作模式，如同两道门槛，将多数热爱AI的开发者、爱好者挡在门外，让核心能力难以被快速感知和应用——这也是Nexus基于该版本开发的核心初衷，用可视化页面打破门槛，让技术能力更易触达。

这个版本是一个开发者示例工程，包含了一些常见场景中的示例管理界面，用户可以使用SkillsCenter 的 AIBridge 功能将家里或者公司的路由器、NAS设备、智能家居网关等设备安装上相应的，ooderAgent 就可以使用，Nexus 开发包将，这些设备的能力实现可视化，同时将其编入到SkillFlow中，扩展AI的能力。另外这个版本还提供了，关于场景、场景组、以及能力管理等相关的基础的示例管理程序。开发者可以根据自身的业务需求构建属于自己的SuperAgent 分发程序。

### 1.2 项目目的

Nexus 是一个基于 ooderAgent 架构的 P2P AI 能力分发枢纽产品，采用 MIT 开源协议。本项目旨在打造一个去中心化的 AI 能力共享和管理平台，支持个人、小微企业及部门内部部署，实现 AI 能力的安全、高效分发。

当前版本是在 ooderAgent 核心 0.6.6 开发的预览版程序，0.6.6 以后会独立建立分支。部分程序未经严格测试，正式发布版本前仅供研究学习 ooderAgent 框架。相关应用推荐配合 ooder-skillsCenter 使用。

### 1.3 SDK 0.6.6 与示例程序说明

SDK 0.6.6 实现了完整的 ooderAgent 协议，为 Nexus 提供了稳定的底层通信能力。Nexus 在 SDK 基础的通讯和能力管理上，针对常见的路由器安装、智能设备安装提供了网络和路由管理示例页面，同时针对家庭网关的特殊设备也提供了相应的开发示例。

开发者可以通过 skillsCenter 下载相应的 skills 插件，安装在自己的路由器或网关上，就可以有针对性地进行开发，使其支持跨生态的 AI 分发服务。

### 1.4 核心价值

- **去中心化架构**：采用 P2P 自组网技术，无需中心服务器
- **AI 能力共享**：实现技能的发布、分享和执行
- **本地存储管理**：提供安全可靠的文件存储和版本控制
- **多平台支持**：兼容 PC、NAS、智能设备等多种硬件平台
- **灵活扩展**：通过技能体系实现功能的灵活扩展

### 1.5 项目定位

Nexus 定位为个人和小微企业的 AI 能力分发解决方案，专注于以下场景：
- **个人开发者**：快速构建和分享 AI 能力
- **小微企业**：低成本部署内部 AI 服务
- **部门团队**：实现团队内部 AI 能力的共享和管理
- **智能设备生态**：为智能设备提供 AI 能力支持

## 2. 技术架构

### 2.1 系统架构

Nexus 采用分层架构设计，包含以下核心层：

| 层次 | 组件 | 职责 |
|------|------|------|
| 基础层 | ooderAgent | 提供 P2P 网络通信、服务发现、命令处理等基础功能 |
| 应用层 | Nexus | 基于 ooderAgent 构建的 AI 能力分发枢纽，提供高级功能 |
| 技能层 | Skills | AI 能力的封装和执行单元 |
| 存储层 | VFS | 虚拟文件系统，提供统一的存储接口 |

### 2.2 技术栈

| 类别 | 技术/框架 | 版本 |
|------|-----------|------|
| 后端 | Java | 8+ |
| 后端框架 | Spring Boot | 2.7.0 |
| P2P 网络 | ooderAgent SDK | 0.6.6 |
| 前端 | HTML5/CSS3/JavaScript | ES6+ |
| 存储 | 本地文件系统 | - |
| 构建工具 | Maven | 3.6+ |

## 3. SDK 6.5 集成

### 3.1 SDK 概述

ooderAgent SDK 6.5 是 Nexus 项目的核心依赖，提供了 P2P 网络通信、服务发现、命令处理等基础功能。SDK 6.5 版本引入了多项新特性和改进，为 Nexus 提供了更强大的底层支持。

### 3.2 SDK 架构

ooderAgent SDK 6.5 采用模块化架构，包含以下核心模块：

| 模块 | 职责 | 主要类 |
|------|------|--------|
| 网络模块 | P2P 网络通信 | NetworkModule |
| 服务模块 | 服务发现和调用 | ServiceModule |
| 命令模块 | 命令处理 | CommandModule |
| 安全模块 | 安全机制 | SecurityModule |
| 配置模块 | 系统配置 | ConfigModule |
| 监控模块 | 系统监控 | MonitorModule |

### 3.3 SDK 核心功能

#### 3.3.1 P2P 网络通信
- **自动发现**：通过 UDP 广播自动发现网络中的其他节点
- **节点管理**：管理网络节点的连接和状态
- **数据传输**：支持点对点数据传输
- **网络拓扑**：构建和维护网络拓扑结构
- **NAT 穿透**：支持复杂网络环境下的节点发现

#### 3.3.2 服务发现
- **服务注册**：注册本地服务供其他节点发现
- **服务发现**：发现网络中的其他服务
- **服务调用**：远程调用其他节点的服务
- **服务监控**：监控服务状态和健康状况

#### 3.3.3 命令处理
- **命令解析**：解析和处理网络命令
- **命令执行**：执行本地和远程命令
- **命令响应**：返回命令执行结果
- **命令队列**：管理命令执行队列

#### 3.3.4 安全机制
- **通信加密**：支持 AES-256 加密通信
- **身份验证**：基于 KEY 的身份验证
- **权限控制**：细粒度的权限控制
- **安全审计**：记录安全相关操作

### 3.4 SDK 集成方式

#### 3.4.1 Maven 依赖

```xml
<dependency>
    <groupId>net.ooder</groupId>
    <artifactId>agent-sdk</artifactId>
    <version>0.6.6</version>
</dependency>
```

#### 3.4.2 初始化配置

```java
// 初始化 ooderAgent
AgentConfig config = new AgentConfig();
config.setId("nexus-001");
config.setName("Nexus");
config.setType("nexus");
config.setDescription("P2P AI Capability Distribution Hub");

// 网络配置
NetworkConfig networkConfig = new NetworkConfig();
networkConfig.setMode("p2p");
networkConfig.setPort(9876);
networkConfig.setDiscoveryInterval(30000);
networkConfig.setMaxNodes(100);
config.setNetworkConfig(networkConfig);

// 安全配置
SecurityConfig securityConfig = new SecurityConfig();
securityConfig.setEnabled(true);
securityConfig.setEncryption("AES-256");
securityConfig.setAuthenticationEnabled(true);
config.setSecurityConfig(securityConfig);

// 监控配置
MonitorConfig monitorConfig = new MonitorConfig();
monitorConfig.setEnabled(true);
monitorConfig.setMetricsInterval(60000);
config.setMonitorConfig(monitorConfig);

// 初始化 Agent
Agent agent = AgentFactory.createAgent(config);
agent.start();
```

### 3.5 SDK 6.5 新特性

- **性能优化**：改进了网络通信和服务发现性能，减少了网络延迟
- **稳定性增强**：提高了系统稳定性和容错能力，增加了自动重连机制
- **API 简化**：简化了 SDK API，提高了易用性，减少了样板代码
- **安全增强**：增强了安全机制，提高了系统安全性，增加了安全审计功能
- **多平台支持**：扩展了对更多平台的支持，包括 ARM 架构设备
- **监控增强**：增加了系统监控能力，提供了更丰富的 metrics
- **配置管理**：改进了配置管理机制，支持动态配置更新

### 3.6 SDK 使用最佳实践

#### 3.6.1 初始化最佳实践
- **延迟初始化**：在应用启动时延迟初始化 SDK，避免阻塞启动过程
- **配置验证**：在初始化前验证配置参数的有效性
- **异常处理**：妥善处理初始化过程中可能出现的异常

#### 3.6.2 网络使用最佳实践
- **节点管理**：定期清理无效节点，保持网络健康
- **带宽管理**：合理控制数据传输速率，避免网络拥塞
- **重试机制**：实现合理的网络操作重试机制

#### 3.6.3 安全使用最佳实践
- **密钥管理**：安全存储和管理身份验证密钥
- **权限控制**：严格控制服务访问权限
- **加密配置**：始终启用通信加密

#### 3.6.4 性能优化最佳实践
- **连接池管理**：合理配置连接池大小
- **缓存机制**：使用缓存减少网络操作
- **异步操作**：对于耗时操作使用异步 API

### 3.7 SDK 6.5 API 参考

#### 3.7.1 核心 API

##### 3.7.1.1 Agent 类

```java
// 创建 Agent 实例
Agent agent = AgentFactory.createAgent(config);

// 启动 Agent
agent.start();

// 停止 Agent
agent.stop();

// 获取模块实例
NetworkModule networkModule = agent.getNetworkModule();
ServiceModule serviceModule = agent.getServiceModule();
CommandModule commandModule = agent.getCommandModule();
SecurityModule securityModule = agent.getSecurityModule();
ConfigModule configModule = agent.getConfigModule();
MonitorModule monitorModule = agent.getMonitorModule();
```

##### 3.7.1.2 网络模块 API

```java
// 发现网络节点
List<NodeInfo> nodes = networkModule.discoverNodes();

// 获取节点信息
NodeInfo nodeInfo = networkModule.getNodeInfo(nodeId);

// 发送消息
boolean success = networkModule.sendMessage(nodeId, message);

// 加入网络
boolean joined = networkModule.joinNetwork();

// 离开网络
boolean left = networkModule.leaveNetwork();
```

##### 3.7.1.3 服务模块 API

```java
// 注册服务
boolean registered = serviceModule.registerService(serviceId, service);

// 发现服务
ServiceInfo serviceInfo = serviceModule.discoverService(serviceId);

// 调用服务
Object result = serviceModule.callService(serviceId, method, params);

// 取消注册服务
boolean unregistered = serviceModule.unregisterService(serviceId);
```

##### 3.7.1.4 命令模块 API

```java
// 发送命令
CommandResponse response = commandModule.sendCommand(nodeId, command);

// 注册命令处理器
boolean registered = commandModule.registerCommandHandler(commandType, handler);

// 取消注册命令处理器
boolean unregistered = commandModule.unregisterCommandHandler(commandType);

// 执行本地命令
CommandResponse response = commandModule.executeLocalCommand(command);
```

##### 3.7.1.5 安全模块 API

```java
// 生成密钥对
KeyPair keyPair = securityModule.generateKeyPair();

// 验证身份
boolean authenticated = securityModule.authenticate(nodeId, token);

// 检查权限
boolean authorized = securityModule.checkPermission(serviceId, action);

// 加密数据
byte[] encrypted = securityModule.encrypt(data, key);

// 解密数据
byte[] decrypted = securityModule.decrypt(encrypted, key);
```

##### 3.7.1.6 监控模块 API

```java
// 获取系统指标
SystemMetrics metrics = monitorModule.getSystemMetrics();

// 获取网络指标
NetworkMetrics networkMetrics = monitorModule.getNetworkMetrics();

// 注册指标监听器
monitorModule.registerMetricsListener(listener);

// 取消注册指标监听器
monitorModule.unregisterMetricsListener(listener);
```

### 3.8 SDK 6.5 版本兼容性

#### 3.8.1 向后兼容性

ooderAgent SDK 6.5 保持了与之前版本的向后兼容性，主要变更包括：

- **API 变更**：部分 API 方法签名发生变化，但保持了功能兼容性
- **配置变更**：新增了一些配置项，旧配置项仍然有效
- **行为变更**：部分默认行为发生变化，以提高系统性能和稳定性

#### 3.8.2 迁移指南

从旧版本 SDK 迁移到 6.6 版本的主要步骤：

1. **更新依赖**：将 Maven 依赖版本更新为 0.6.6
2. **检查配置**：更新配置参数，添加新的配置项
3. **API 适配**：适配变更的 API 方法签名
4. **测试验证**：在测试环境中验证迁移后的功能

### 3.9 SDK 6.5 故障排查

#### 3.9.1 常见问题

| 问题 | 可能原因 | 解决方案 |
|------|---------|----------|
| 初始化失败 | 配置错误、端口被占用 | 检查配置参数，确保端口未被占用 |
| 节点无法发现 | 网络隔离、防火墙阻止 | 检查网络连接，开放 P2P 端口 |
| 服务调用失败 | 网络中断、服务不可用 | 检查网络连接，验证服务状态 |
| 性能下降 | 节点过多、网络拥塞 | 清理无效节点，优化网络配置 |

#### 3.9.2 日志分析

SDK 6.5 提供了详细的日志记录，可通过以下方式分析日志：

- **日志级别**：可配置不同的日志级别，如 DEBUG、INFO、WARN、ERROR
- **日志分类**：按模块分类记录日志，便于定位问题
- **日志格式**：标准化的日志格式，包含时间戳、模块、级别、消息等信息

#### 3.9.3 诊断工具

SDK 6.5 内置了诊断工具，可用于排查问题：

```java
// 运行诊断
DiagnosticReport report = agent.runDiagnostic();

// 检查网络状态
NetworkDiagnosticResult networkResult = report.getNetworkResult();

// 检查服务状态
ServiceDiagnosticResult serviceResult = report.getServiceResult();

// 检查安全状态
SecurityDiagnosticResult securityResult = report.getSecurityResult();
```

## 4. 核心功能模块

### 4.1 个人中心

#### 4.1.1 功能概述

个人中心模块提供用户个人相关的功能，包括仪表盘、技能管理、执行历史、分享管理、群组管理等。

#### 4.1.2 技术实现

- **控制器**：`PersonalController` 处理个人中心相关的 API 请求
- **服务层**：提供个人数据管理和技能管理等服务
- **数据存储**：使用本地存储管理个人数据和技能信息

### 4.2 P2P 网络

#### 4.2.1 功能概述

P2P 网络模块负责网络节点的发现、管理和通信，实现去中心化的网络架构。

#### 4.2.2 技术实现

- **控制器**：`P2PNetworkController` 处理网络相关的 API 请求
- **服务层**：`P2PService` 实现网络节点管理和通信
- **底层依赖**：基于 ooderAgent SDK 6.5 实现

### 4.3 技能管理

#### 4.3.1 功能概述

技能管理模块负责技能的发布、分享、执行和管理，是 Nexus 的核心功能之一。

#### 4.3.2 技术实现

- **控制器**：`SkillBridgeController` 处理技能相关的 API 请求
- **服务层**：`SkillManager` 管理技能的生命周期
- **执行引擎**：负责技能的执行和结果处理

### 4.4 存储管理

#### 4.4.1 功能概述

存储管理模块提供文件的存储、管理、版本控制和分享功能。

#### 4.4.2 技术实现

- **控制器**：`StorageController` 处理存储相关的 API 请求
- **服务层**：实现文件管理和版本控制
- **底层存储**：基于本地文件系统，提供虚拟文件系统接口

### 4.5 系统监控

#### 4.5.1 功能概述

系统监控模块负责监控系统状态、网络状态和服务状态，提供健康检查和告警功能。

#### 4.5.2 技术实现

- **控制器**：`SystemMonitorController` 处理监控相关的 API 请求
- **服务层**：实现系统指标收集和告警管理
- **监控指标**：CPU、内存、磁盘、网络等系统指标

## 5. 开发环境搭建

### 5.1 环境要求

| 组件 | 版本/要求 |
|------|-----------|
| JDK | 8 或更高版本 |
| Maven | 3.6 或更高版本 |
| Git | 2.0 或更高版本 |
| 操作系统 | Windows/Linux/macOS |

### 5.2 开发流程

1. **克隆代码仓库**
   ```bash
   git clone https://gitee.com/ooderCN/ooder-nexus.git
   cd ooder-nexus
   ```

2. **编译项目**
   ```bash
   mvn clean package
   ```

3. **运行开发服务器**
   ```bash
   mvn spring-boot:run
   ```

4. **访问开发环境**
   打开浏览器，访问：http://localhost:8091/console/index.html

## 6. 技能开发指南

### 6.1 技能结构

技能是 Nexus 中 AI 能力的封装单元，具有以下结构：

```
my-skill/
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── com/
│   │   │       └── example/
│   │   │           └── skill/
│   │   │               ├── MySkill.java      # 技能实现
│   │   │               └── MySkillInfo.java   # 技能信息
│   │   └── resources/
│   │       └── skill.json                    # 技能描述文件
│   └── test/
│       └── java/
│           └── com/
│               └── example/
│                   └── skill/
│                       └── MySkillTest.java  # 技能测试
└── pom.xml                                  # Maven 配置文件
```

### 6.2 技能接口

所有技能必须实现 `Skill` 接口：

```java
public interface Skill {
    // 获取技能信息
    SkillInfo getInfo();
    
    // 执行技能
    SkillResult execute(SkillContext context, SkillParam param);
    
    // 验证参数
    boolean validateParam(SkillParam param);
}
```

### 6.3 技能部署

1. **编译打包**
   ```bash
   mvn clean package
   ```

2. **发布技能**
   通过 Nexus 控制台或 API 发布技能：
   ```bash
   POST /api/personal/skills
   Content-Type: multipart/form-data
   
   # 表单数据
   - skillFile: <skill.jar>
   - skillJson: <skill.json>
   ```

## 7. SDK 6.5 开发指南

### 7.1 SDK 架构

ooderAgent SDK 6.5 采用模块化架构，包含以下核心模块：

| 模块 | 职责 | 主要类 |
|------|------|--------|
| 网络模块 | P2P 网络通信 | NetworkModule |
| 服务模块 | 服务发现和调用 | ServiceModule |
| 命令模块 | 命令处理 | CommandModule |
| 安全模块 | 安全机制 | SecurityModule |
| 配置模块 | 系统配置 | ConfigModule |

### 7.2 核心 API

#### 7.2.1 网络 API

```java
// 发现网络节点
List<NodeInfo> nodes = agent.getNetworkModule().discoverNodes();

// 加入网络
boolean joined = agent.getNetworkModule().joinNetwork();

// 发送消息
boolean sent = agent.getNetworkModule().sendMessage(nodeId, message);
```

#### 7.2.2 服务 API

```java
// 注册服务
agent.getServiceModule().registerService(serviceId, service);

// 发现服务
ServiceInfo service = agent.getServiceModule().discoverService(serviceId);

// 调用服务
Object result = agent.getServiceModule().callService(serviceId, method, params);
```

#### 7.2.3 命令 API

```java
// 发送命令
CommandResponse response = agent.getCommandModule().sendCommand(nodeId, command);

// 注册命令处理器
agent.getCommandModule().registerCommandHandler(commandType, handler);
```

### 7.3 最佳实践

- **错误处理**：妥善处理 SDK 可能抛出的异常
- **资源管理**：及时释放不再使用的资源
- **异步操作**：对于耗时操作，使用异步 API
- **配置优化**：根据实际需求优化 SDK 配置
- **监控日志**：添加适当的日志记录，便于问题排查

## 8. 测试策略

### 8.1 测试层次

| 测试类型 | 目标 | 工具 |
|---------|------|------|
| 单元测试 | 验证单个组件的功能 | JUnit 5 |
| 集成测试 | 验证组件间的交互 | Spring Test |
| 功能测试 | 验证系统功能的正确性 | 手动测试 |
| 性能测试 | 评估系统性能 | JMH |
| 兼容性测试 | 验证系统在不同环境下的兼容性 | 多环境测试 |

### 8.2 测试用例

测试用例覆盖以下核心功能：

- **P2P 网络**：节点发现、网络连接、消息传输
- **技能管理**：技能发布、执行、分享
- **存储管理**：文件上传、下载、版本控制
- **系统监控**：健康检查、指标监控、告警管理

## 9. 部署与运维

### 9.1 部署方式

#### 9.1.1 本地部署

1. **从源码编译**
   ```bash
   git clone https://gitee.com/ooderCN/ooder-nexus.git
   cd ooder-nexus
   mvn clean package
   java -jar target/independent-nexus-0.6.6.jar
   ```

2. **直接运行**
   从项目发布页面下载最新的可执行 JAR 文件，直接运行：
   ```bash
   java -jar independent-nexus-0.6.6.jar
   ```

#### 9.1.2 容器部署

1. **构建 Docker 镜像**
   ```bash
   docker build -t ooder-nexus:latest .
   ```

2. **运行容器**
   ```bash
   docker run -d -p 8091:8091 --name ooder-nexus ooder-nexus:latest
   ```

### 9.2 配置管理

主要配置文件为 `application.yml`，位于 `src/main/resources/` 目录。

#### 9.2.1 核心配置项

```yaml
server:
  port: 8091

spring:
  application:
    name: ooder-nexus

ooder:
  agent:
    id: "nexus-001"
    name: "ooderNexus"
    type: "nexus"
    description: "P2P AI Capability Distribution Hub"
  network:
    mode: "p2p"
    discovery:
      enabled: true
      interval: 30000
    security:
      enabled: true
      encryption: "AES-256"
  p2p:
    port: 9876
    host: 0.0.0.0
    timeout: 30000
    retry: 3
    max-nodes: 100
  skill:
    base-package: "net.ooder.nexus.skill"
    auto-discovery: true
  storage:
    type: "local"
    local:
      root-path: "./data/storage"
      cache-path: "./data/cache"
      temp-path: "./data/temp"
    version:
      enabled: true
      max-versions: 10
    hash:
      algorithm: "MD5"
    cleanup:
      enabled: true
      interval: 86400
```

### 9.3 监控与维护

#### 9.3.1 日志管理

| 日志文件 | 用途 | 路径 |
|---------|------|------|
| 系统日志 | 系统运行状态 | logs/system.log |
| 错误日志 | 错误信息 | logs/error.log |
| 审计日志 | 操作审计 | logs/audit.log |

#### 9.3.2 常见问题

| 问题 | 可能原因 | 解决方案 |
|------|---------|----------|
| 节点无法发现 | 网络隔离、防火墙阻止 | 检查网络连接，开放 P2P 端口 |
| 技能执行失败 | 参数错误、依赖缺失 | 检查执行参数，确保依赖已安装 |
| 存储问题 | 磁盘空间不足、权限错误 | 清理磁盘空间，检查文件权限 |
| 服务启动失败 | 端口被占用、配置错误 | 检查端口占用，验证配置文件 |

## 10. 版本管理

### 10.1 版本策略

Nexus 采用语义化版本控制，版本号格式为 `major.minor.patch`：

- **Major**：不兼容的 API 变更
- **Minor**：向后兼容的功能添加
- **Patch**：向后兼容的 bug 修复

### 10.2 版本历史

| 版本 | 发布日期 | 主要特性 |
|------|---------|----------|
| v1.0.0 | 2025-11 | 初始版本，基本功能实现 |

### 10.3 SDK 版本对应关系

| Nexus 版本 | ooderAgent SDK 版本 | 兼容性 |
|-----------|-------------------|--------|
| v1.0.0 | 0.6.6 | 完全兼容 |

## 11. 开发规范

### 11.1 代码规范

- **命名规范**：使用清晰、描述性的命名
- **代码风格**：遵循 Java 代码风格指南
- **注释规范**：添加详细的代码注释
- **异常处理**：妥善处理异常情况

### 11.2 文档规范

- **API 文档**：使用 Javadoc 注释
- **架构文档**：维护系统架构图和组件关系
- **使用文档**：提供详细的使用指南
- **变更文档**：记录版本变更和兼容性信息

### 11.3 版本控制

- **分支管理**：采用 Git 分支管理策略
- **提交规范**：遵循提交信息规范
- **代码审查**：实施代码审查流程
- **持续集成**：配置持续集成环境

## 12. 结论

Nexus 项目通过采用 P2P 自组网技术和模块化设计，实现了一个去中心化的 AI 能力分发枢纽。基于 ooderAgent SDK 6.5 的强大功能，Nexus 提供了安全、高效的 AI 能力共享和管理平台。

通过本开发设计文档，开发者可以了解 Nexus 的技术架构、核心功能和开发流程，快速上手并参与到项目开发中。未来，Nexus 将继续演进，提供更多高级功能和更好的用户体验。