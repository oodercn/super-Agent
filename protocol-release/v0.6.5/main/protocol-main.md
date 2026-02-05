# Ooder AI Bridge 协议主文档 - v0.6.3

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

### 3.5 南向协议与LLM分册

- 南向协议规范
- LLM 分册
- VFS 增强
- A2UI Skills 集成

## 4. 南向协议规范

### 4.1 协议概述

南向协议是 ooderAgent 向底层服务或组件发送指令的统一协议，旨在实现 ooderAgent 内部组件（routeAgent、endAgent）之间的高效协同工作，以及与外部服务的标准化交互。

#### 4.1.1 设计目标

- **统一标准**：定义统一的指令格式和交互规范
- **灵活扩展**：支持动态添加和移除服务组件
- **可靠性**：实现错误处理和重试机制
- **异步处理**：支持命令的异步执行和结果查询
- **版本兼容**：确保不同版本之间的兼容性

### 4.2 协议格式

#### 4.2.1 基本格式

南向协议采用 JSON 格式，确保数据结构的一致性和可解析性：

```json
{
  "protocol_version": "0.6.3",
  "command_id": "uuid",
  "timestamp": "2026-01-23T12:00:00Z",
  "source": {
    "component": "string",
    "id": "string"
  },
  "destination": {
    "component": "string",
    "id": "string"
  },
  "operation": "string",
  "payload": {
    // 操作参数
  },
  "metadata": {
    "priority": "high|medium|low",
    "timeout": "number",
    "retry_count": "number"
  }
}
```

#### 4.2.2 字段说明

| 字段 | 类型 | 必选 | 说明 |
|------|------|------|------|
| protocol_version | string | 是 | 协议版本，固定为 "0.6.3" |
| command_id | string | 是 | 命令唯一标识，UUID 格式 |
| timestamp | string | 是 | 命令发送时间，ISO 8601 格式 |
| source | object | 是 | 命令发送方信息 |
| source.component | string | 是 | 发送方组件类型（mcpAgent/routeAgent/endAgent） |
| source.id | string | 是 | 发送方组件 ID |
| destination | object | 是 | 命令接收方信息 |
| destination.component | string | 是 | 接收方组件类型 |
| destination.id | string | 是 | 接收方组件 ID |
| operation | string | 是 | 操作类型 |
| payload | object | 是 | 操作参数 |
| metadata | object | 否 | 元数据信息 |
| metadata.priority | string | 否 | 优先级，默认为 "medium" |
| metadata.timeout | number | 否 | 超时时间（秒），默认为 30 |
| metadata.retry_count | number | 否 | 重试次数，默认为 3 |

### 4.3 0.6.2-0.6.3 补充内容

#### 4.3.1 协议增强

- **统一请求格式**：所有组件间的请求均采用南向协议 v0.6.3 格式
- **响应格式标准化**：统一响应格式，确保一致性
- **版本协商机制**：支持组件间的版本协商

#### 4.3.2 LLM 集成

- **集成 LLM 分册**：将 LLM 相关功能作为南向协议的独立分册
- **LLM 查询优化**：增强 LLM 查询的性能和可靠性
- **Token 管理**：优化 Token 使用，减少消耗

#### 4.3.3 VFS 增强

- **VFS 事务约束**：确保数据的完整性和一致性
- **全局地址列表**：建立和维护 VFS 地址的全局索引
- **故障恢复机制**：当 endAgent 离线时，通过命令 ID 直接获取结果

#### 4.3.4 A2UI Skills 集成

- **A2UI Skills 支持**：集成 A2UI 技能，提供图生代码功能
- **四分离设计原则**：支持属性、样式、事件、行为的分离设计

### 4.4 操作类型

| 操作类型 | 功能描述 | 目标组件 | 参数 |
|----------|----------|----------|------|
| execute-llm-query | 执行 LLM 查询 | mcpAgent | `{"skill_id": "string", "query": "string", "parameters": "object"}` |
| get-llm-status | 获取 LLM 服务状态 | mcpAgent | N/A |
| list-llm-models | 列出可用 LLM 模型 | mcpAgent | N/A |
| execute-llm-advanced-query | 执行高级 LLM 查询 | mcpAgent | `{"skill_id": "string", "query": "string", "parameters": "object", "options": "object"}` |

### 4.5 错误处理

| 错误码 | 错误描述 | 处理策略 |
|--------|----------|----------|
| 1000 | 参数错误 | 直接返回错误 |
| 1001 | 认证失败 | 直接返回错误 |
| 1002 | 权限不足 | 直接返回错误 |
| 1003 | 资源不存在 | 直接返回错误 |
| 1004 | 请求超时 | 指数退避重试 |
| 1005 | 网络错误 | 指数退避重试 |
| 1006 | 服务繁忙 | 指数退避重试 |
| 1007 | 内部错误 | 指数退避重试 |
| 1008 | 数据格式错误 | 直接返回错误 |
| 1009 | LLM 服务不可用 | 指数退避重试 |

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

## 5. 版本说明

### 5.1 当前版本

- 版本号：v0.6.3
- 发布日期：2026-01-23
- 主要更新：
  - 集成A2UI Skills，提供图生代码功能
  - 统一南向协议格式，集成LLM分册
  - 增强VFS支持，添加事务约束和全局地址列表
  - 完善命令体系，添加A2UI相关命令
  - 优化Token使用，减少消耗

### 5.2 版本历史

| 版本号 | 发布日期 | 主要变化 |
|--------|----------|----------|
| v0.51  | 2025-12-15 | 初始版本发布 |
| v0.6.2 | 2026-01-18 | 术语统一与功能增强 |
| v0.6.3 | 2026-01-23 | 集成A2UI Skills，统一南向协议格式，增强VFS支持 |

## 6. 协议规范

### 6.1 消息格式

```json
{
  "version": "0.6.3",
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
- 跨平台技能服务集成
- 资源管理与监控系统
- 多智能体协同应用
- A2UI 图生代码场景
- LLM 集成与优化场景

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
