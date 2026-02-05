# Ooder AI Bridge 协议主文档 - v0.6.2
## 1. 协议概述

### 1.1 协议背景

随着人工智能技术的快速发展，多智能体系统之间的协同与通信变得日益重要。Ooder AI Bridge 协议旨在建立一个标准化的通信框架，实现不同智能体、技能服务和资源系统之间的无缝交互与协作。
### 1.2 协议目标

- **标准化通信**：定义统一的消息格式和通信接口，消除系统间的互操作障碍
- **安全可靠**：提供完善的认证、授权和加密机制，确保通信安全
- **灵活扩展**：支持动态发现和注册，适应不断增长的服务和资源
- **高效协同**：优化通信协议，减少延迟，提高多智能体协同效率

### 1.3 应用场景

- 智能体间的任务协作与资源共享
- 技能服务的动态发现与调用
- 跨平台资源的统一管理与访问
- 多模态数据的交换与处理
- 实时监控与远程控制
## 2. 协议架构

### 2.1 分层架构

```
┌───────────────────────应用层        │────▶│ 协议层        │────▶│ 传输层        ┐
└───────────────────────                └───────────────────────                └───────────────────────
        └────────────────────────────────────────────────────────────────────────────────
                                安全层
```

### 2.2 核心组件

- **消息格式**：定义统一的请求/响应结构
- **命令系统**：标准化的操作指令集
- **安全机制**：认证、授权、加密和安全审计
- **错误处理**：统一的错误码和异常处理机制
- **扩展机制**：支持自定义命令和参数
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

- Skill 需求规范- Skill-Capability 关系
- Skill 接口定义
- Skill 数据模型

### 3.4 Agent 分册

- MCP Agent 规范
- Route Agent 规范
- End Agent 规范
- Agent 通信机制

## 4. 术语定义

| 术语 | 解释 |
|------|------|
| Ooder | 智能体系统的统一品牌名称 |
| Skill | 提供特定功能的服务单元 |
| Capability | Skill提供的具体能力 |
| Space | 资源组织的基本单元 |
| Zone | Space内的子区域，用于更细粒度的资源管理 |
| Endpoint | 服务的网络访问点 |
| Endpoints | 服务的多个网络访问点列表 |
| AI Bridge | 智能体间通信的桥梁协议，属于北上协议 |
| MCP Agent | 主控智能体，负责资源管理和调度 |
| Route Agent | 路由智能体，负责消息路由和转发 |
| End Agent | 终端智能体，负责与外部设备和系统交互 |
| 北上协议 | 以mcpAgent为中心，向云服务（包括公云、私有云、混合云）通信的协议统称 |
| 南下协议 | mcpAgent向下与其他组件通信的协议统称 |
| Agent协议 | 南下协议的具体实现，用于mcpAgent、routeAgent、endAgent及skills之间的通信 |

## 5. 版本说明

### 5.1 当前版本

- 版本号：v0\.6\.2
- 发布日期：2026-01-18
- 主要更新：  - 术语统一（SuperAgent → Ooder）  - Endpoint 升级为 Endpoints（单个 → 列表）  - Place/Area 升级为 Space/Zone
  - 完善 Skill-Capability 关系模型
  - 增强安全机制

### 5.2 版本历史

| 版本号 | 发布日期 | 主要变化 |
|--------|----------|----------|
| v0.51  | 2025-12-15 | 初始版本发布 |
| v0.52  | 2026-01-18 | 术语统一与功能增强 |

## 6. 协议规范

### 6.1 消息格式

```json
{
  "version": "0\.6\.2",
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
    "trace_id": "trace-456"
  }
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

## 7. 适用范围

本协议适用于以下场景：

- 企业级智能体系统
- 跨平台技能服务集成- 资源管理与监控系统- 多智能体协同应用

## 9. 版权声明

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

---

**Ooder Technology Co., Ltd.**
