# Ooder AI Bridge 协议主文档 - v0.7.0

## 1. 协议概述

### 1.1 协议背景

随着人工智能技术的快速发展，多智能体系统之间的协同与通信变得日益重要。Ooder AI Bridge 协议旨在建立一个标准化的通信框架，实现不同智能体、技能服务和资源系统之间的无缝交互与协作。

v0.7.0 版本是 Ooder Agent 协议的重大升级，主题为"广域网内 P2P 安全认证设计"，旨在解决 SuperAgent 在广域网环境下的安全通信和认证问题，实现跨网络、跨设备的安全可靠协作。

### 1.2 协议目标

- **标准化通信**：定义统一的消息格式和通信接口，消除系统间的互操作障碍
- **安全可靠**：提供完善的认证、授权和加密机制，确保通信安全
- **广域网支持**：实现 SuperAgent 在广域网环境下的安全通信和认证
- **灵活扩展**：支持动态发现和注册，适应不断增长的服务和资源
- **高效协同**：优化通信协议，减少延迟，提高多智能体协同效率

### 1.3 应用场景

- 广域网内的 SuperAgent 节点通信
- 跨网络、跨设备的技能共享和调用
- 企业级多智能体系统的分布式部署
- 个人设备间的安全数据同步和协同
- 跨组织的 SuperAgent 网络协作

## 2. 协议架构

### 2.1 分层架构

```
┌───────────────────────
│        应用层        │
└───────────────────────
        │
┌───────────────────────
│        协议层        │
└───────────────────────
        │
┌───────────────────────
│        传输层        │
└───────────────────────
        │
┌───────────────────────
│        安全层        │
└───────────────────────
```

### 2.2 核心组件

- **消息格式**：定义统一的请求/响应结构
- **命令系统**：标准化的操作指令集
- **安全机制**：认证、授权、加密和安全审计
- **错误处理**：统一的错误码和异常处理机制
- **扩展机制**：支持自定义命令和参数
- **广域网适配**：针对广域网环境的协议优化

## 3. 文档结构

本协议文档采用分册结构，包括以下部分：

### 3.1 主文档（本文件）

- 协议概述
- 架构设计
- 术语定义
- 版本说明

### 3.2 AI Bridge 分册

- 通信协议
- 消息格式
- 命令系统
- 安全机制

### 3.3 Skill 分册

- Skill 需求规范
- Skill-Capability 关系
- Skill 接口定义
- Skill 数据模型

### 3.4 Agent 分册

- MCP Agent 规范
- Route Agent 规范
- End Agent 规范
- Agent 通信机制

### 3.5 P2P 协议分册

- 广域网 P2P 网络架构
- 安全认证机制
- 节点发现与路由
- 数据传输与加密

### 3.6 南向协议与LLM分册

- 南向协议规范
- LLM 分册
- VFS 增强
- A2UI Skills 集成

## 4. 版本说明

### 4.1 当前版本

- 版本号：v0.7.0
- 发布日期：2026-02-11
- 主要更新：
  - 广域网内 P2P 安全认证设计
  - 增强的安全机制，支持跨网络认证
  - 优化的节点发现和路由机制
  - 完善的错误处理和重试策略
  - 支持 Maven Central 依赖管理

### 4.2 版本历史

| 版本号 | 发布日期 | 主要变化 |
|--------|----------|----------|
| v0.51  | 2025-12-15 | 初始版本发布 |
| v0.6.2 | 2026-01-18 | 术语统一与功能增强 |
| v0.6.3 | 2026-01-23 | 集成A2UI Skills，统一南向协议格式 |
| v0.6.5 | 2026-01-29 | P2P 网络架构，无状态技能分发 |
| v0.6.6 | 2026-02-04 | Maven Central 依赖管理，配置体系优化 |
| v0.7.0 | 2026-02-11 | 广域网内 P2P 安全认证设计 |

## 5. 术语定义

| 术语 | 解释 |
|------|------|
| Ooder | 智能体系统的统一品牌名称 |
| Skill | 提供特定功能的服务单元 |
| Capability | Skill提供的具体能力 |
| Space | 资源组织的基本单元 |
| Zone | Space内的子区域，用于更细粒度的资源管理 |
| Endpoint | 服务的网络访问点 |
| Endpoints | 服务的多个网络访问点列表 |
| AI Bridge | 智能体间通信的桥梁协议，属于北向协议 |
| MCP Agent | 主控智能体，负责资源管理和调度 |
| Route Agent | 路由智能体，负责消息路由和转发 |
| End Agent | 终端智能体，负责与外部设备和系统交互 |
| 北向协议 | 以mcpAgent为中心，向云服务（包括公云、私有云、混合云）通信的协议统称 |
| 南向协议 | mcpAgent向下与其他组件通信的协议统称 |
| Agent协议 | 南向协议的具体实现，用于mcpAgent、routeAgent、endAgent及skills之间的通信协议 |
| A2UI | AI to UI，从图片生成UI代码的技术 |
| VFS | Virtual File System，虚拟文件系统 |
| LLM | Large Language Model，大语言模型 |
| P2P | 点对点（Peer-to-Peer），一种去中心化的网络架构 |
| 广域网 | 覆盖范围较大的网络，如互联网 |
| 局域网 | 覆盖范围较小的网络，如家庭或办公室网络 |

## 6. 协议规范

### 6.1 消息格式

```json
{
  "version": "0.7.0",
  "id": "uuid-1234-5678-90ab-cdef",
  "timestamp": 1737000000000,
  "type": "request",
  "command": "skill.discover",
  "params": {
    "space_id": "space-123",
    "capability": "weather"
  },
  "metadata": {
    "sender_id": "agent-123",
    "trace_id": "trace-456",
    "security_level": "high"
  },
  "signature": "digital_signature"
}
```

### 6.2 错误处理

| 错误码 | 描述 | HTTP状态码 |
|--------|------|------------|
| 1001 | 参数错误 | 400 |
| 1002 | 认证失败 | 401 |
| 1003 | 权限不足 | 403 |
| 1004 | 资源不存在 | 404 |
| 1005 | 内部错误 | 500 |
| 1006 | 服务不可用 | 503 |
| 2001 | 网络错误 | 502 |
| 2002 | 安全验证失败 | 401 |
| 2003 | 广域网连接失败 | 504 |
| 2004 | 节点认证失败 | 401 |

## 7. 适用范围

本协议适用于以下场景：

- 企业级智能体系统
- 跨平台技能服务集成
- 资源管理与监控系统
- 多智能体协同应用
- A2UI 图生代码场景
- LLM 集成与优化场景
- 广域网 P2P 安全通信场景

## 8. 版权声明

MIT License

Copyright (c) 2026 Ooder Technology Co., Ltd.

Permission is hereby granted, free of charge, to any person obtaining a copy
of this document and associated documentation files, to deal
in the document without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the document, and to permit persons to whom the document is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the document.

THE DOCUMENT IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE DOCUMENT OR THE USE OR OTHER DEALINGS IN THE
DOCUMENT.
