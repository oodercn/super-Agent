# ooder SuperAgent - 企业级AI能力分发与自动化协作框架

## 项目概述

ooder SuperAgent 是一套基于 MIT 协议的开源企业级 AI 能力分发与自动化协作框架，它通过创新的 Agent 架构和 SKILL 管理机制，为企业提供了从简单任务到复杂流程的全场景自动化解决方案。

本项目作为 ooderAI 开源生态的核心组件，实现了基于场景化协作架构的数据流转系统，展示了 SuperAgent 平台的核心设计理念和技术实现。系统采用 P2P 网络架构和无状态技能分发机制，通过标准化协议实现服务发现、数据流转和协同工作。

最新更新 (v0.6.6 Config)：
- **完整的Spring Boot配置体系**：基于@ConfigurationProperties的统一配置管理，支持热配置
- **增强的UDP监控管理机制**：实时指标收集、EWMA延迟估算、吞吐量和错误率计算
- **重构的网络结构**：从单例模式重构为Spring组件，智能端口分配策略
- **新增异步处理能力**：基于CompletableFuture的异步执行，支持带返回值和无返回值的任务
- **消除硬编码值**：所有网络参数可配置，Agent端口可通过配置文件管理
- **类型安全配置**：使用枚举类型和类型安全的配置管理，提高代码可靠性
- **智能端口分配**：基于网络环境的智能端口选择，避免端口冲突
- **网络环境感知**：根据不同的网络环境自动调整配置参数

## 项目生态

ooder SuperAgent Nexus 版本包含以下核心组件：

1. **核心引擎**：
   - agent-sdk：Agent SDK 实现，包含完整的 EndAgent、RouteAgent、McpAgent 接口体系，支持 P2P 网络和无状态技能分发
   - agent-skillcenter：技能中心实现，支持技能注册、发现、执行和共享，实现本地优先执行

2. **技能生态**：
   - trae-solo-standard：标准 Skills 实现，专为 trae-solo IDE 设计
   - skills-a2ui：A2UI 图转代码服务，支持响应式设计

3. **工具链**：
   - ES6 模块系统：现代化 JavaScript 开发体验
   - VFS 工具：虚拟文件系统管理，支持文件同步和监控
   - A2UI 工具：AI 辅助 UI 生成，支持多主题和组件库

4. **测试环境**：
   - test-scenarios：场景化测试目录，包含服务发现、代理层次、网络环境、性能测试和可靠性测试等核心场景

5. **协议体系**：
   - protocol-release：完整的协议文档体系，包含 AI Bridge 协议、A2A 协议和 P2P 协议

## 核心设计理念

基于 [ooderAI Agent 管理平台设计](https://cloud.tencent.com/developer/article/2619515)，SuperAgent 采用以下核心设计理念：

- **MCPAgent 分发网络**：企业/第三方系统可以将自己的 MCPAgent 接入 AIServer，将 AI 能力通过 SuperAgent 分发；个人用户拥有专属 MCPAgent，整合个人 AI 工具
- **AI 能力标准化**：定义统一的 AI 能力接口规范，支持 1:n Capability 关系，实现 AI 能力的模块化设计
- **跨域自动化编排**：通过 Skillflow 可视化工具，实现跨企业/个人 MCPAgent 的工作流编排
- **安全可靠的设计**：基于 RBAC 的权限管理，支持多种部署方式，确保系统安全可靠

## 核心协议体系

ooder SuperAgent 围绕两大核心协议构建完整的协作生态：

### 1. AIbridge 协议

AIbridge 协议定义了 MCPAgent 与 AIServer 之间的通信规范，采用 TCP/HTTP 协议确保通信的可靠性和安全性。该协议包含：

- **命令参数规范**：标准化的命令格式和参数定义
- **协议完整性说明**：确保数据传输的完整性和一致性
- **安全机制增强**：基于 HMAC-SHA256 的认证机制，确保通信安全
- **技术实现细节**：详细的协议实现指南和最佳实践

### 2. A2A 协议（Scene/Group 机制）

A2A 协议定义了 Agent 之间的协作规范，通过 Scene 和 Group 机制实现多 Agent 协同工作：

- **Scene（场景）**：协作上下文环境，定义了协作的业务场景和规则
- **Group（组）**：协作团队，由多个 Agent 组成，共同完成特定任务
- **自主加入机制**：Agent 可以通过 UDP 广播自动发现并加入相关场景
- **命令协同机制**：基于场景的命令分发和执行协同

## 最新技术更新

### VFS (Virtual File System) 更新

- **增强的文件同步机制**：优化了 VFS 与本地文件系统的同步流程，提高了文件传输速度和可靠性
- **完整的文件管理功能**：支持文件上传、下载、删除、重命名等操作
- **文件监控**：实时监控文件变化，自动触发同步
- **错误处理**：改进了错误处理机制，提供更详细的错误信息

### SDK 更新

- **ES6 模块支持**：集成了完整的 ES6 模块系统，支持现代 JavaScript 语法
- **Tree Shaking 优化**：针对现代打包工具（Webpack、Rollup）进行了优化，减少了打包体积
- **TypeScript 类型定义**：提供了完整的 TypeScript 类型定义，提升开发体验
- **向后兼容性**：保持与遗留 `ood.Class` 系统的完全兼容，支持渐进式迁移
- **构建系统优化**：使用 Webpack 4 构建，提供开发和生产版本
- **SDK Agent 接口设计实现**：完整的 EndAgent、RouteAgent、McpAgent 接口体系，支持角色分离和功能继承
- **模块化架构**：接口与实现分离，便于扩展和测试
- **异步执行机制**：使用 CompletableFuture 实现高效异步操作
- **安全加密传输**：内置消息加密机制，保护数据安全
- **Skill 集成框架**：提供统一的 Skill 集成接口，支持无缝集成自定义技能

### A2UI (AI to UI) 更新

- **增强的图转代码能力**：优化了 AI 模型，提高了代码生成的准确性和质量
- **多格式支持**：支持 HTML、JavaScript、JSON 等多种输出格式
- **多主题支持**：内置 Light、Dark、Purple 三种主题，可根据需求自定义
- **组件库扩展**：增加了更多 UI 组件，包括 Button、Input、Dialog、TreeGrid、Tabs 等 60+ 组件
- **响应式设计**：生成的代码支持响应式布局，适配不同设备尺寸

### SkillCenter (技能中心) 更新

- **核心功能**：支持技能注册、发现、执行和共享
- **P2P 网络管理**：自动节点发现、心跳检测、技能共享机制
- **应用场景**：个人AI中心、分布式技能执行、跨设备技能共享、技能开发与测试等
- **技能管理**：支持技能分类、版本管理、权限控制
- **执行引擎**：本地优先执行，远程技能按需调用
- **IDE 集成**：支持技能的快速开发、测试和部署
- **安全机制**：内置消息加密和权限管理

### trae-solo 集成与增强

- **标准 Skills 实现**：完全按照 Skills 标准规范实现，支持分级加载机制
- **Web API 调用**：通过 HTTP API 调用服务，减少 Token 消耗
- **无缝集成**：专为 trae-solo IDE 优化，提供友好的用户体验
- **功能丰富**：支持基本实用功能（问候、计算、时间获取等）和 A2UI 图转代码功能
- **完整协作流程**：实现了 SKILL A、B、C 三个核心服务的完整协作流程
- **性能优化**：减少了初始化时间、Token 消耗和内存占用
- **多格式支持**：支持 HTML、JavaScript、JSON 等多种输出格式
- **多主题支持**：支持 Light、Dark、Purple 三种主题

### mcpAgent 增强

- **Agent SDK v0.6.3 全面升级**：提供完整的南向协议实现框架
- **核心功能增强**：增强了 Agent 生命周期管理、命令发送与接收、网络链路管理等
- **高级功能支持**：提供命令队列与超时处理、网络拓扑管理、LLM 交互管理
- **框架集成**：完善了与 Spring Boot 等框架的集成支持
- **性能优化**：提供了详细的性能优化建议和最佳实践

## 测试代码设计范围

Nexus 版本测试代码实现了 SuperAgent 核心功能的验证，覆盖以下关键场景：

1. **服务发现测试**：验证 P2P 网络中的服务发现机制，包括节点自动发现、技能注册与发现、UDP 网络通信、心跳机制
2. **代理层次测试**：测试 MCP Agent → Route Agent → End Agent 三层架构的协作，包括命令传递和转发、状态同步
3. **网络环境测试**：验证系统在本地网络、局域网和内网环境下的适应性
4. **性能测试**：评估系统在不同并发负载下的性能表现
5. **可靠性测试**：测试系统在节点故障和网络异常情况下的恢复能力

系统通过场景化测试验证完整的 P2P 协作流程，每个场景包含完整的测试环境和文档，确保测试覆盖度和可重复性。

## 系统架构

ooder SuperAgent 企业总体结构由六大核心组件构成：

1. **Auth 认证中心**：提供统一的身份认证和授权服务
2. **DataServer 数据中心**：统一管理企业数据
3. **Skill 能力管理中心**：统一管理 SKILL 的注册、分类、发现和调用
4. **LLM 调度中心**：统一管理 LLM 资源，实现 LLM 的高效调度和管控
5. **Skillflow 调度中心**：实现跨域自动化编排
6. **Agent 协作层**：实现 Agent 之间的协同工作

### P2P 分布式架构

在 P2P 分布式场景中，系统采用三层 Agent 架构：

1. **End Agent**：终端节点，直接与数据源或目标系统交互，负责数据采集、处理和执行
   - 支持无状态技能分发，实现技能按需获取
   - 采用本地优先执行原则，保护用户隐私

2. **Route Agent**：路由节点，负责命令路由和任务协调，处理 End Agent 之间的通信
   - 实现动态拓扑管理和负载均衡
   - 支持自组织网络，自动处理节点加入和离开

3. **MCP Agent**：主控节点，提供 AI 能力分发和管理，负责网络协调和安全认证
   - 支持去中心化身份管理
   - 实现技能市场和共享机制

本次测试代码实现了 Agent 协作层的核心功能，展示了 Scene 和 Group 机制的实际应用，以及 P2P 网络中的节点发现、通信和协同工作。

## 快速开始

### 环境要求

- JDK 1.8+
- Maven 3.6+
- Spring Boot 2.7.0

### 快速运行测试用例

#### 1. 编译项目

```bash
# 编译所有模块
mvn clean compile
```

#### 2. 构建测试环境

```bash
# 构建所有模块
mvn clean package -DskipTests
```

#### 3. 启动测试服务

启动核心测试服务：

```bash
# 启动技能A服务（信息检索）
cd skills-a
mvn spring-boot:run

# 启动技能B服务（数据提交）
cd skills-b
mvn spring-boot:run

# 启动技能C服务（场景管理）
cd skills-c
mvn spring-boot:run
```

#### 4. 执行测试

```bash
# 使用 curl 测试服务发现流程
curl -X POST http://localhost:8081/api/v1/discovery -H "Content-Type: application/json" -d '{"sceneId":"TEST_SCENE_001"}'

# 测试技能调用
curl -X POST http://localhost:8081/api/v1/skill/invoke -H "Content-Type: application/json" -d '{"skillName":"information-retrieval-skill","params":{"query":"test"}}'
```

#### 5. 查看测试结果

检查各服务的日志，确认：
- 服务注册到P2P网络，技能注册成功
- 服务发现机制正常工作
- 技能调用流程完整
- UDP通信正常，心跳机制运行中
- 代理协作流程顺畅

### Nexus 版本特有测试结果

- **P2P网络测试**：节点自动发现成功，动态拓扑构建完成，负载均衡正常
- **无状态技能分发**：技能按需获取成功，无状态封装验证通过
- **去中心化身份管理**：身份验证成功，权限控制有效
- **本地优先执行**：本地技能执行优先，隐私保护验证通过
- **自组织网络**：节点自动加入和离开，网络拓扑自动调整

## 项目结构

```
super-agent/
├── agent-sdk/                    # Agent SDK 实现
│   ├── src/                      # 源代码
│   ├── lib/                      # 依赖库
│   └── pom.xml                   # Maven 配置
├── agent-rad/                    # Agent RAD 实现
│   ├── src/                      # 源代码
│   └── pom.xml                   # Maven 配置
├── skill-a/                      # 技能A实现（信息检索）
│   ├── src/                      # 源代码
│   └── pom.xml                   # Maven 配置
├── skill-b/                      # 技能B实现（数据提交）
│   ├── src/                      # 源代码
│   └── pom.xml                   # Maven 配置
├── skill-c/                      # 技能C实现（场景管理）
│   ├── src/                      # 源代码
│   └── pom.xml                   # Maven 配置
├── test-scenarios/               # 测试场景目录
│   ├── service-discovery/        # 服务发现场景
│   │   ├── end-agent-1/          # 终端代理 1
│   │   ├── end-agent-2/          # 终端代理 2
│   │   ├── route-agent/          # 路由代理
│   │   ├── TEST_DOCUMENT.md      # 测试文档
│   │   └── TEST_REPORT.md        # 测试报告
│   ├── agent-hierarchy/          # 代理层次结构场景
│   │   ├── mcp-agent/            # MCP 代理
│   │   ├── route-agent/          # 路由代理
│   │   ├── end-agent/            # 终端代理
│   │   └── TEST_DOCUMENT.md      # 测试文档
│   ├── network-environment/      # 网络环境场景
│   │   ├── local-network/        # 本地网络环境
│   │   │   └── TEST_DOCUMENT.md  # 测试文档
│   │   ├── lan-network/          # 局域网环境
│   │   │   └── TEST_DOCUMENT.md  # 测试文档
│   │   └── intranet-network/     # 企业内网环境
│   │       └── TEST_DOCUMENT.md  # 测试文档
│   ├── performance-test/         # 性能测试场景
│   │   └── TEST_DOCUMENT.md      # 测试文档
│   └── reliability-test/         # 可靠性测试场景
│       └── TEST_DOCUMENT.md      # 测试文档
├── trae-solo-standard/           # 标准 Skills 实现，专为 trae-solo IDE 设计
│   ├── skills/                   # 技能目录
│   │   ├── trae-solo/            # 主技能
│   │   └── a2ui/                 # A2UI 技能
│   └── README.md                 # 说明文档
├── skills-a2ui/                  # A2UI 图转代码服务
│   ├── src/                      # 源代码
│   ├── docs/                     # 文档
│   └── pom.xml                   # Maven 配置
├── protocol-release/             # 协议发布目录
│   ├── v0.6.3/                   # 0.6.3 版本协议文档
│   │   ├── agent/                # Agent 协议文档
│   │   ├── ai-bridge/            # AI Bridge 协议文档
│   │   ├── general/              # 通用文档
│   │   ├── guide/                # 指南文档
│   │   ├── main/                 # 主协议文档
│   │   ├── sdk/                  # SDK 文档
│   │   ├── skill/                # Skill 协议文档
│   │   ├── skill-implementation/ # Skill 实现文档
│   │   └── skill-spec/           # Skill 规范文档
│   └── v0.6.5/                   # 0.6.5 Nexus 版本协议文档
│       ├── agent/                # Agent 协议文档
│       ├── ai-bridge/            # AI Bridge 协议文档
│       ├── general/              # 通用文档
│       ├── guide/                # 指南文档
│       ├── main/                 # 主协议文档
│       ├── p2p/                  # P2P 网络协议文档
│       ├── sdk/                  # SDK 文档
│       ├── skill/                # Skill 协议文档
│       ├── skill-implementation/ # Skill 实现文档
│       └── skill-spec/           # Skill 规范文档
├── docs/                         # 文档目录
│   ├── architecture/             # 架构文档
│   ├── communication/            # 通信文档
│   ├── components/               # 组件文档
│   ├── deployment/               # 部署文档
│   ├── faq/                      # 常见问题
│   ├── overview/                 # 概览文档
│   ├── reference/                # 参考文档
│   ├── security/                 # 安全文档
│   └── skills/                   # 技能文档
├── TEST_CASES.md                 # 测试用例文档
├── TEST_SCENARIOS_5W_ANALYSIS.md # 测试场景5W分析
├── TEST_ENVIRONMENT_AND_RESULTS.md # 测试环境与结果汇总
├── P2P_TEST_ENVIRONMENT_PLAN.md  # P2P测试环境构建计划
├── command-system-list.md        # 命令系统列表
├── ooderAgent-SDD实践解析.md      # SDD实践解析
├── ooderai-agent-quickstart-guide.md # 快速上手指南
├── SDK-集成说明.md                # SDK集成说明
└── README.md                      # 项目说明文档
```

## 测试环境与文档

### 测试用例文档

项目提供了详细的测试用例文档 `TEST_CASES.md`，包含了23个测试用例，覆盖了以下测试场景：

1. **Service Discovery**：验证P2P网络中的服务发现机制
2. **Agent Hierarchy**：测试三层架构的协作能力
3. **Network Environment**：验证系统在不同网络环境下的适应性
4. **Performance Test**：评估系统在不同负载下的性能表现
5. **Reliability Test**：测试系统在故障情况下的恢复能力
6. **综合测试**：验证整个系统的端到端功能

每个测试用例都包含详细的测试步骤、预期结果和实际结果字段，便于测试执行和结果记录。

### 场景化测试目录结构

Nexus 版本采用场景化测试目录结构，每个场景包含完整的测试环境和文档，确保测试的可重复性和可扩展性：

```
super-agent/
├── test-scenarios/                    # 测试场景目录
│   ├── service-discovery/             # 服务发现场景
│   │   ├── end-agent-1/               # 终端代理 1
│   │   ├── end-agent-2/               # 终端代理 2
│   │   ├── route-agent/               # 路由代理
│   │   ├── TEST_DOCUMENT.md           # 测试文档
│   │   └── TEST_REPORT.md             # 测试报告
│   ├── agent-hierarchy/               # 代理层次结构场景
│   │   ├── mcp-agent/                 # MCP 代理
│   │   ├── route-agent/               # 路由代理
│   │   ├── end-agent/                 # 终端代理
│   │   └── TEST_DOCUMENT.md           # 测试文档
│   ├── network-environment/           # 网络环境场景
│   │   ├── local-network/             # 本地网络环境
│   │   │   └── TEST_DOCUMENT.md       # 测试文档
│   │   ├── lan-network/               # 局域网环境
│   │   │   └── TEST_DOCUMENT.md       # 测试文档
│   │   └── intranet-network/          # 企业内网环境
│   │       └── TEST_DOCUMENT.md       # 测试文档
│   ├── performance-test/              # 性能测试场景
│   │   └── TEST_DOCUMENT.md           # 测试文档
│   └── reliability-test/              # 可靠性测试场景
│       └── TEST_DOCUMENT.md           # 测试文档
├── TEST_SCENARIOS_5W_ANALYSIS.md      # 测试场景5W分析
├── TEST_ENVIRONMENT_AND_RESULTS.md    # 测试环境与结果汇总
├── P2P_TEST_ENVIRONMENT_PLAN.md       # P2P测试环境构建计划
```

### 核心测试场景（Nexus 版本）

| 测试场景 | 描述 | 执行方式 | 测试重点 |
|---------|------|----------|----------|
| **Service Discovery** | 验证 P2P 网络中的服务发现机制，包括节点自动发现、技能注册与发现、UDP 网络通信、心跳机制 | 启动终端代理和路由代理，观察服务注册和发现过程 | 节点自动发现、技能注册、UDP 通信 |
| **Agent Hierarchy** | 测试 MCP → Route → End 三层架构的协作，包括命令传递和转发、状态同步 | 按照层次结构启动代理，执行命令传递测试 | 命令传递、状态同步、层次化服务发现 |
| **Network Environment** | 测试系统在不同网络环境下的适应性，包括本地网络、局域网和内网环境 | 在不同网络环境下执行服务发现和数据流转测试 | 网络环境适配、服务发现效率 |
| **Performance Test** | 评估系统在不同并发负载下的性能表现 | 使用性能测试工具执行不同负载下的测试 | 响应时间、吞吐量、资源占用 |
| **Reliability Test** | 测试系统在节点故障和网络异常情况下的恢复能力 | 模拟节点故障和网络异常，测试系统的恢复能力 | 故障恢复、网络弹性、容错能力 |

### 测试报告与分析

- **TEST_ENVIRONMENT_AND_RESULTS.md**：测试环境与结果汇总报告，包含所有测试场景的执行结果
- **TEST_SCENARIOS_5W_ANALYSIS.md**：测试场景5W分析说明，详细分析每个测试场景的目的、方法和结果
- **test-scenarios/service-discovery/TEST_REPORT.md**：服务发现场景测试报告，记录详细的测试过程和结果
- **P2P_TEST_ENVIRONMENT_PLAN.md**：P2P测试环境构建计划，包括网络配置和测试策略
- **rpa-skills-test-summary.md**：完整的测试总结报告，汇总所有测试场景的结果和发现的问题
- **test-vs-protocol-differences.md**：测试与协议差异对比，分析实际实现与协议规范的差异

## AI-IDE 一键构建

### VS Code 配置

在项目根目录创建 `.vscode/launch.json` 文件，配置 Nexus 版本的测试环境：

```json
{
  "version": "0.2.0",
  "configurations": [
    {
      "type": "java",
      "name": "End Agent 1",
      "request": "launch",
      "mainClass": "net.ooder.examples.endagent.EndAgentApplication",
      "projectName": "end-agent-1"
    },
    {
      "type": "java",
      "name": "End Agent 2",
      "request": "launch",
      "mainClass": "net.ooder.examples.endagent.EndAgentApplication",
      "projectName": "end-agent-2"
    },
    {
      "type": "java",
      "name": "Route Agent",
      "request": "launch",
      "mainClass": "net.ooder.examples.routeagent.RouteAgentApplication",
      "projectName": "route-agent"
    },
    {
      "type": "java",
      "name": "MCP Agent",
      "request": "launch",
      "mainClass": "net.ooder.examples.mcpagent.McpAgentApplication",
      "projectName": "mcp-agent"
    }
  ]
}
```

### IntelliJ IDEA 配置

1. 打开项目根目录
2. 配置四个 Spring Boot 运行配置（End Agent 1、End Agent 2、Route Agent、MCP Agent）
3. 设置正确的主类和工作目录
4. 使用一键运行功能启动测试环境

## 协议文档

### v0.6.5 Nexus 协议文档

ooder SuperAgent v0.6.5 Nexus 提供了完整的协议文档体系，详细说明请参考 `protocol-release/v0.6.5` 目录：

- **main/protocol-main.md**：Ooder AI Bridge 协议主文档
- **ai-bridge/ai-bridge-protocol.md**：AI Bridge 协议分册
- **agent/agent-protocol.md**：Agent 协议分册
- **skill/skill-protocol.md**：Skill 协议分册
- **skill/skill-vfs-protocol.md**：Skill VFS 专用协议分册
- **sdk/SDK-集成说明.md**：SDK 集成说明
- **sdk/SDK-Usage-Guide.md**：SDK 使用指南
- **guide/ooderai-agent-quickstart-guide.md**：ooderAI Agent 快速上手指南
- **general/protocol-overview.md**：协议概述
- **general/protocol-statement.md**：协议声明
- **p2p/p2p-protocol.md**：P2P 网络协议分册

### 核心协议体系

v0.6.5 Nexus 版本围绕三大核心协议构建完整的协作生态：

1. **北向协议（AI Bridge 协议）**：以 mcpAgent 为中心，向云服务（包括公云、私有云、混合云）通信的协议统称
2. **南向协议**：mcpAgent 向下与其他组件通信的协议统称
3. **P2P 协议**：SuperAgent 节点间通信和技能共享的协议

### UDP 通信规范

系统采用 UDP 广播实现服务发现，详情请参考协议文档中的相关部分。

## 开发指南

### 代码结构

Nexus 版本的 Agent 服务采用标准的 Spring Boot 项目结构：

```
agent-x/
├── src/
│   ├── main/java/net/ooder/examples/agentx/
│   │   ├── config/     # 配置类
│   │   ├── controller/ # 控制器
│   │   ├── model/      # 数据模型
│   │   ├── service/    # 服务层
│   │   └── AgentApplication.java # 主类
│   └── resources/      # 配置文件
└── pom.xml             # Maven 配置
```

### 模块化包结构

Agent SDK 采用模块化包结构，便于扩展和维护：

```
agent-sdk/
├── agent/           # Agent 相关接口和实现
│   ├── end/         # EndAgent 实现
│   ├── route/       # RouteAgent 实现
│   └── mcp/         # McpAgent 实现
├── command/         # 命令相关组件
│   ├── service/     # 命令服务
│   └── packet/      # 命令数据包
├── network/         # 网络相关组件
│   ├── discovery/   # 网络发现
│   ├── packet/      # UDP 数据包
│   └── port/        # 端口管理
├── llm/             # LLM 抽象层
│   ├── api/         # 外部 API 实现
│   ├── skill/       # 本地技能
│   └── flow/        # 技能流程
├── system/          # 系统级组件
│   ├── config/      # 配置管理
│   ├── util/        # 工具类
│   └── factory/     # 工厂类
└── example/         # 示例代码
```

### Nexus 测试环境

SuperAgent V0.6.5 Nexus 版本提供了完整的 Nexus 测试环境支持，详细配置请参考 `docs/deployment/superagent-nexus-test-environment-guide.md` 文件：

- **Nexus 仓库配置**：搭建本地 Nexus 仓库，实现依赖的统一管理
- **项目配置**：更新 Maven 配置，使用 Nexus 仓库解析依赖
- **依赖部署**：将 agent-sdk、skill-vfs 等模块部署到 Nexus
- **测试验证**：验证依赖解析和版本一致性

### 部署流程

1. **搭建 Nexus 环境**：按照指南安装和配置 Nexus Repository Manager
2. **配置 Maven**：更新 settings.xml 文件，配置 Nexus 仓库访问
3. **部署依赖**：将项目模块部署到 Nexus 仓库
4. **验证环境**：运行构建测试，确认依赖正确解析
5. **开始开发**：基于 Nexus 测试环境进行开发和测试

### 错误码体系

系统采用 1000-5999 范围的错误码：

| 错误码 | 描述 |
|-------|------|
| 1001 | Scene 不存在 |
| 1002 | Scene 已存在 |
| 1003 | Group 不存在 |
| 1004 | Group 已存在 |
| 1005 | Agent 不存在 |
| 1006 | Agent 已在 Scene 中 |
| 1007 | Agent 已在 Group 中 |

## 联系方式

- GitHub 项目地址：`https://github.com/oodercn/super-Agent`

## 更新说明

### 最新更新（2026-02-04 - v0.6.6 Config）

Config 版本是 ooder SuperAgent 的配置增强版本，实现了从硬编码配置到智能配置体系的重大升级，核心更新包括：

- **完整的 Spring Boot 配置体系**：
  - 基于 `@ConfigurationProperties` 的统一配置管理
  - 支持热配置，无需重启应用即可生效
  - 类型安全的配置管理，使用枚举类型提高代码可靠性
  - 分层配置结构，根据功能模块划分配置类

- **增强的 UDP 监控管理机制**：
  - 实时指标收集，包括延迟、吞吐量、错误率等
  - EWMA 延迟估算算法，提供更准确的网络延迟评估
  - 端口级监控，支持对每个端口的独立监控
  - 自动清理过期指标，避免内存泄漏

- **重构的网络结构**：
  - 从单例模式重构为 Spring 组件，利用依赖注入提高代码可测试性
  - 智能端口分配策略，基于网络环境自动选择合适的端口
  - 网络环境感知，根据不同的网络环境自动调整配置参数
  - 端口冲突检测和处理，提高系统可靠性

- **新增异步处理能力**：
  - 基于 `CompletableFuture` 的异步执行服务
  - 支持带返回值和无返回值的异步任务
  - 内置超时控制机制，避免任务无限等待
  - 优雅的错误处理，简化异步代码的编写

- **消除硬编码值**：
  - 所有网络参数可通过配置文件管理
  - Agent 端口可通过配置文件灵活设置
  - 提供合理的默认值，简化配置过程
  - 配置分层管理，提高配置的可维护性

- **测试配置优化**：
  - 修复了 `TestConfiguration.java` 文件中的编译错误
  - 确保测试用例能够正确加载配置
  - 提供完整的测试配置支持

- **构建和部署优化**：
  - 成功编译并生成了 agent-sdk-0.6.6.jar 文件
  - 更新了所有关联项目的 pom.xml 文件，确保版本一致性
  - 优化了依赖管理，确保所有项目能够正确解析依赖

### 历史更新（2026-01-29 - v0.6.5 Nexus）

Nexus 版本是 ooder SuperAgent 的重大更新，实现了从技术原型到成熟产品的飞跃，核心更新包括：

- **P2P 网络架构**：
  - 构建了完整的 P2P 生态系统，支持设备间技能和数据共享
  - 实现了节点自动发现、动态拓扑和负载均衡
  - 支持自组织网络，自动处理节点加入和离开

- **无状态技能分发**：
  - 实现了技能的无状态封装和按需获取
  - 支持去中心化的技能分发机制，无单点故障
  - 优化了技能缓存和版本管理，提高执行效率

- **三层 Agent 架构**：
  - MCP Agent：主控节点，提供 AI 能力分发和管理
  - Route Agent：路由节点，负责命令路由和任务协调
  - End Agent：终端节点，直接与数据源或目标系统交互

- **本地优先执行**：
  - 采用本地优先执行原则，保护用户隐私
  - 支持离线使用，确保在无网络环境下的基本功能

- **去中心化身份管理**：
  - 用户拥有完全自主的身份管理能力
  - 可以控制自己的数据和能力的共享范围

- **场景化测试**：
  - 构建了场景化测试目录结构，每个场景包含完整的测试环境和文档
  - 实现了 Service Discovery、Agent Hierarchy、Network Environment、Performance Test 和 Reliability Test 等核心测试场景
  - 生成了详细的测试报告和 5W 分析文档
  - 创建了完整的测试用例文档 `TEST_CASES.md`，包含23个测试用例

- **技术创新**：
  - 链路表复制和 CPA 构建，确保网络中的链路信息和控制平面协议一致
  - End Agent 无状态化，提高系统稳定性
  - Route Agent 自动升级，确保网络的弹性和可靠性
  - MCP Agent 监听频道，提高网络的可管理性和安全性

- **依赖管理优化**：
  - 统一了项目依赖管理，避免交叉依赖
  - 调整了 base pom 为本地文件依赖，避免本地 Maven 库依赖
  - 使用 SDK 的 lib 目录作为公共本地 jar 依赖
  - SDK 编译后自动复制 jar 到 lib 目录

- **编译和部署优化**：
  - 修复了所有编译错误，确保项目能够成功编译
  - 优化了部署流程，提高了部署效率
  - 确保了所有模块都能够使用本地文件依赖成功编译

### 后续规划

- **v0.7.0 版本**：添加更多功能和接口，提高系统可扩展性
- **v1.0.0 版本**：发布稳定版本，提供完整的文档和示例
- **技能市场**：完善技能共享和交易机制
- **多语言支持**：增强国际化能力，支持更多语言
- **云服务集成**：与主流云服务平台集成，扩展应用场景
- **测试优化**：修复测试错误，提高测试覆盖率和稳定性
- **性能优化**：进一步优化系统性能，提高并发处理能力
- **安全性增强**：加强系统安全性，提供更全面的安全防护