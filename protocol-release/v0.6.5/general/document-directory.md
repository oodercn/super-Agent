# Ooder AI Bridge 协议文档目录

## 1. 文档概述

本目录列出了 Ooder AI Bridge 协议 v0.6.3 版本的所有文档，按功能模块和分册进行分类，方便用户查找和使用。

## 2. 文档结构

```
protocol-release/v0.6.3/
├── general/              # 通用文档
│   ├── protocol-overview.md        # 协议概论
│   ├── protocol-statement.md       # 协议声明
│   └── document-directory.md       # 文档目录
├── main/                 # 协议主文档
│   └── protocol-main.md            # 协议主文档
├── ai-bridge/            # AI Bridge 协议分册
│   └── ai-bridge-protocol.md       # AI Bridge 协议
├── skill/                # Skill 协议分册
│   ├── skill-protocol.md           # Skill 协议
│   └── skill-vfs-protocol.md       # Skill VFS 专用协议
├── agent/                # Agent 协议分册
│   └── agent-protocol.md           # Agent 协议
├── skill-spec/           # Skill 需求规格说明
│   ├── skill-requirements.md       # Skill 功能需求
│   ├── skill-interface.md          # Skill 接口定义
│   └── skill-data-model.md         # Skill 数据模型
├── skill-implementation/ # Skill 协议栈参考实现
│   ├── implementation-guide.md     # 实现指南
│   ├── reference-code.md           # 参考代码
│   └── deployment-guide.md         # 部署指南
├── sdk/                  # SDK 文档
│   ├── SDK-集成说明.md             # SDK 集成说明
│   └── SDK-Usage-Guide.md          # SDK 使用指南
└── guide/                # 指南文档
    └── ooderai-agent-quickstart-guide.md # ooderAI Agent 快速上手指南
```

## 3. 分册文档

### 3.1 通用分册

| 文档名称 | 文档路径 | 主要内容 |
|----------|----------|----------|
| 协议概论 | general/protocol-overview.md | 协议背景、目标、架构、术语定义等 |
| 协议声明 | general/protocol-statement.md | 版权声明、使用条款、免责声明等 |
| 文档目录 | general/document-directory.md | 文档结构和导航 |

### 3.2 协议主文档
| 文档名称 | 文档路径 | 主要内容 |
|----------|----------|----------|
| 协议主文档 | main/protocol-main.md | 合并的协议主文档，包含核心概念和架构 |

### 3.3 AI Bridge 协议分册

| 文档名称 | 文档路径 | 主要内容 |
|----------|----------|----------|
| AI Bridge 协议 | ai-bridge/ai-bridge-protocol.md | AI Bridge 协议的详细规范和实现指南 |

### 3.4 Skill 协议分册

| 文档名称 | 文档路径 | 主要内容 |
|----------|----------|----------|
| Skill 协议 | skill/skill-protocol.md | Skill 协议的详细规范和实现指南 |

### 3.5 Agent 协议分册

| 文档名称 | 文档路径 | 主要内容 |
|----------|----------|----------|
| Agent 协议 | agent/agent-protocol.md | Agent 协议的详细规范和实现指南 |

### 3.6 Skill 需求规格说明分册
| 文档名称 | 文档路径 | 主要内容 |
|----------|----------|----------|
| Skill 功能需求 | skill-spec/skill-requirements.md | Skill 的功能需求、非功能需求等 |
| Skill 接口定义 | skill-spec/skill-interface.md | Skill API 接口、参数定义等 |
| Skill 数据模型 | skill-spec/skill-data-model.md | Skill 的数据结构、关系模型等 |

### 3.7 Skill 协议栈参考实现分册
| 文档名称 | 文档路径 | 主要内容 |
|----------|----------|----------|
| 实现指南 | skill-implementation/implementation-guide.md | 实现步骤、技术选型等 |
| 参考代码 | skill-implementation/reference-code.md | 示例代码、最佳实践等 |
| 部署指南 | skill-implementation/deployment-guide.md | 部署步骤、配置说明等 |

### 3.8 Skill VFS 专用协议分册
| 文档名称 | 文档路径 | 主要内容 |
|----------|----------|----------|
| Skill VFS 专用协议 | skill/skill-vfs-protocol.md | Skill VFS 专用协议规范 |

### 3.9 SDK 文档分册
| 文档名称 | 文档路径 | 主要内容 |
|----------|----------|----------|
| SDK 集成说明 | sdk/SDK-集成说明.md | SDK 集成配置和使用指南 |
| SDK 使用指南 | sdk/SDK-Usage-Guide.md | SDK 详细使用说明和示例 |

### 3.10 指南文档分册
| 文档名称 | 文档路径 | 主要内容 |
|----------|----------|----------|
| ooderAI Agent 快速上手指南 | guide/ooderai-agent-quickstart-guide.md | ooderAI Agent 快速上手和使用指南 |

## 4. 文档更新记录

| 更新日期 | 更新内容 | 更新人 |
|----------|----------|--------|
| 2026-01-18 | 初始版本发布 | 技术文档团队 |
| 2026-01-23 | 集成A2UI Skills，添加南向协议与LLM分册 | 技术文档团队 |
| 2026-01-23 | 统一术语，将"南下"改为"南向"，"北上"改为"北向" | 技术文档团队 |
| 2026-01-23 | 优化docs目录结构，增加架构、组件、部署等分类 | 技术文档团队 |
| 2026-01-23 | 添加SDK文档和指南文档 | 技术文档团队 |

## 5. 快速导航
### 5.1 按主题查找
#### 5.1.1 协议基础
- 协议背景与目标：[协议概论](general/protocol-overview.md#2-协议目标)
- 协议架构：[协议概论](general/protocol-overview.md#4-协议架构)
- 术语定义：[协议概论](general/protocol-overview.md#5-术语定义)

#### 5.1.2 Skill 开发
- Skill 功能需求：[Skill 功能需求](skill-spec/skill-requirements.md)
- Skill 接口定义：[Skill 接口定义](skill-spec/skill-interface.md)
- Skill 实现指南：[实现指南](skill-implementation/implementation-guide.md)

#### 5.1.3 安全与合规
- 安全机制：[协议概论](general/protocol-overview.md#42-核心组件)
- 版权声明：[协议声明](general/protocol-statement.md#1-版权声明)
- 免责声明：[协议声明](general/protocol-statement.md#4-免责声明)

#### 5.1.4 南向协议与LLM
- 南向协议规范：[协议主文档](main/protocol-main.md#4-南向协议规范)
- LLM 分册：[AI Bridge 协议](ai-bridge/ai-bridge-protocol.md#17-llm-协议分册)

#### 5.1.5 SDK 开发
- SDK 集成说明：[SDK 集成说明](sdk/SDK-集成说明.md)
- SDK 使用指南：[SDK 使用指南](sdk/SDK-Usage-Guide.md)

#### 5.1.6 快速上手
- ooderAI Agent 快速上手指南：[ooderAI Agent 快速上手指南](guide/ooderai-agent-quickstart-guide.md)

### 5.2 按文档类型查找
#### 5.2.1 需求文档
- Skill 功能需求：[Skill 功能需求](skill-spec/skill-requirements.md)

#### 5.2.2 接口文档
- Skill 接口定义：[Skill 接口定义](skill-spec/skill-interface.md)

#### 5.2.3 实现文档
- 实现指南：[实现指南](skill-implementation/implementation-guide.md)
- 参考代码：[参考代码](skill-implementation/reference-code.md)

#### 5.2.4 部署文档
- 部署指南：[部署指南](skill-implementation/deployment-guide.md)

## 8. 版本信息

- **当前版本**：v0.6.3
- **发布日期**：2026-01-23
- **文档状态**：正式发布
