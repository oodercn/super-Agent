# Super Agent Examples

本目录包含 Ooder Super Agent 的示例项目，展示了不同类型 Agent 的实现方式。

## 示例项目列表

### 1. MCP Agent (Master Control Plane Agent)
**目录**: `mcp-agent`

**功能说明**:
- 主控平面 Agent，负责管理和协调 Route Agent 和 End Agent
- 实现了北上协议和南下协议的核心逻辑
- 支持场景管理、技能调用和 VFS 集成

**主要特性**:
- 节点发现和注册
- 心跳机制和状态管理
- 命令转发和路由
- 场景创建和管理

**技术栈**:
- Spring Boot 2.7.0
- Agent SDK 0.7.3
- Scene Engine 0.7.3
- Spring Data JPA + H2 Database

### 2. Route Agent
**目录**: `route-agent`

**功能说明**:
- 路由 Agent，负责在 MCP Agent 和 End Agent 之间转发请求
- 提供负载均衡和连接管理功能
- 支持加密传输和证书验证

**主要特性**:
- 请求转发和路由
- 负载均衡
- 连接管理
- 安全传输

**技术栈**:
- Spring Boot 2.7.0
- Spring Cloud Gateway
- Agent SDK 0.7.3
- Scene Engine 0.7.3

#### 2.1 RPC Skill Example
**目录**: `route-agent/rpc-skill-example`

**功能说明**:
- Route Agent 的技能实现示例
- 展示了如何开发和集成自定义技能
- 包含证书 SN 验证和授权管理

### 3. End Agent
**目录**: `end-agent`

**功能说明**:
- 终端 Agent，负责执行具体的技能和任务
- 提供本地能力和资源的管理
- 支持与外部系统的集成

**主要特性**:
- 技能发现和调用
- 资源管理
- 场景加入和离开
- 智能体注册和注销
- VFS 同步和恢复

**技术栈**:
- Spring Boot 2.7.0
- FastJSON
- JWT Authentication

## 快速开始

### 前提条件
- JDK 1.8+
- Maven 3.6+
- Git

### 构建和运行

#### 1. 构建所有示例
```bash
# 在项目根目录执行
mvn clean package
```

#### 2. 运行 MCP Agent
```bash
cd examples/mcp-agent
java -jar target/mcp-agent-1.0.0-SNAPSHOT.jar
```

#### 3. 运行 Route Agent
```bash
cd examples/route-agent
java -jar target/route-agent-1.0.0-SNAPSHOT.jar
```

#### 4. 运行 End Agent
```bash
cd examples/end-agent
java -jar target/end-agent-1.0.0-SNAPSHOT.jar
```

## 配置说明

### 端口配置
- MCP Agent: 默认端口 9010 (UDP), 8080 (HTTP)
- Route Agent: 默认端口 9002 (UDP), 8081 (HTTP)
- End Agent: 默认端口 8082 (HTTP)

### 核心配置文件
- `application.yml` - Spring Boot 应用配置
- `agent.properties` - Agent 相关配置

## 协议版本

所有示例项目基于 Super Agent v0.7.3 协议实现，包含以下核心协议:

- **北上协议**: End Agent → Route Agent → MCP Agent
- **南下协议**: MCP Agent → Route Agent → End Agent
- **技能调用协议**: 跨 Agent 的技能调用
- **场景管理协议**: 场景的创建、加入和管理

## 开发指南

### 如何添加新技能
1. 在对应 Agent 项目中创建技能实现类
2. 注册到技能管理器
3. 配置技能元数据
4. 实现技能调用逻辑

### 如何扩展协议
1. 参考 `protocol-release/v0.7.3` 目录下的协议文档
2. 在对应模块中实现协议扩展
3. 更新相关配置和依赖

## 故障排查

### 常见问题

1. **连接失败**
   - 检查网络连接和防火墙设置
   - 验证 Agent 端口是否正确配置

2. **注册失败**
   - 检查 MCP Agent 是否运行
   - 验证 Agent ID 和配置是否正确

3. **技能调用失败**
   - 检查技能是否正确注册
   - 验证参数格式是否符合协议要求

4. **场景创建失败**
   - 检查场景权限配置
   - 验证场景参数是否完整

### 日志位置
- Spring Boot 日志: `logs/` 目录
- Agent 日志: `agent-logs/` 目录

## 版本历史

- **v0.7.3** (2026-02) - 基于 Super Agent v0.7.3 协议的完整示例
- **v0.7.0** (2026-01) - 初始版本，基于 v0.7.0 协议

## 联系我们

- 官方网站: https://gitee.com/ooderCN
- 文档中心: https://gitee.com/ooderCN/super-Agent/wiki
- 问题反馈: https://gitee.com/ooderCN/super-Agent/issues

---

**注意**: 本示例仅供学习和参考，生产环境部署请根据实际需求进行配置和优化。