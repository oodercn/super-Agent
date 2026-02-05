# MCP Agent 功能介绍说明书

## 1. 系统概述

MCP (Multi-Channel Protocol) Agent 是一个功能强大的网络管理和设备控制平台，专为企业和家庭网络环境设计。它提供了全面的网络监控、设备管理、配置管理和安全监控等功能，帮助用户实现网络设备的集中管理和智能化控制。

### 1.1 系统架构

MCP Agent 采用分层架构设计，包括：
- **API 层**：提供 RESTful API 接口，支持外部系统集成
- **控制器层**：实现核心业务逻辑，处理各种管理任务
- **服务层**：提供底层服务支持，如网络监控、设备控制等
- **存储层**：管理配置数据、设备信息和系统状态

### 1.2 技术特性

- **Java 8 兼容**：支持在 Java 8 环境中运行
- **Spring Boot 框架**：基于 Spring Boot 构建，提供良好的扩展性
- **RESTful API**：提供标准化的 RESTful 接口
- **实时监控**：支持网络和设备的实时状态监控
- **智能控制**：支持设备的远程控制和自动化管理
- **安全管理**：提供网络安全状态监控和告警

## 2. 核心功能模块

### 2.1 配置管理

**模块说明**：负责管理 MCP Agent 的各种配置参数，包括系统配置、路由配置和终端配置。

**主要功能**：
- 获取和更新 MCP 配置（版本、端口、心跳间隔等）
- 获取和更新路由配置（最大路由数、路由超时等）
- 获取和更新终端配置（最大终端数、终端超时等）
- 重置配置为默认值
- 查看配置变更历史
- 获取配置统计信息

**API 接口**：
- `GET /api/config/mcp` - 获取 MCP 配置
- `PUT /api/config/mcp` - 更新 MCP 配置
- `GET /api/config/route` - 获取路由配置
- `PUT /api/config/route` - 更新路由配置
- `GET /api/config/end` - 获取终端配置
- `PUT /api/config/end` - 更新终端配置
- `GET /api/config/all` - 获取所有配置
- `POST /api/config/reset/{configType}` - 重置配置为默认值
- `GET /api/config/history` - 获取配置变更历史
- `GET /api/config/stats` - 获取配置统计信息

### 2.2 健康检查

**模块说明**：提供系统健康状态监控和服务检查功能，确保系统正常运行。

**主要功能**：
- 获取健康检查概览（系统状态、网络状态、服务状态）
- 获取系统状态（CPU 使用率、内存使用率、磁盘使用率等）
- 获取网络状态（网络连接数、网络延迟、带宽使用等）
- 获取服务状态（各服务运行情况）
- 检查指定服务状态
- 检查所有服务状态
- 获取健康检查历史记录
- 获取告警信息
- 清除所有告警

**API 接口**：
- `GET /api/health/overview` - 获取健康检查概览
- `GET /api/health/system/status` - 获取系统状态
- `GET /api/health/network/status` - 获取网络状态
- `GET /api/health/service/status` - 获取服务状态
- `GET /api/health/service/check/{serviceType}` - 检查指定服务状态
- `POST /api/health/service/check-all` - 检查所有服务状态
- `GET /api/health/history` - 获取健康检查历史
- `GET /api/health/alerts` - 获取告警信息
- `POST /api/health/alerts/clear` - 清除所有告警

### 2.3 设备管理

**模块说明**：管理智能设备的状态和控制，支持设备的远程操作和监控。

**主要功能**：
- 获取设备列表（支持按状态、类型、位置过滤）
- 获取设备详细信息
- 控制设备（开关、设置属性、重置等）
- 获取设备操作日志
- 获取设备类型列表及统计信息

**API 接口**：
- `GET /api/devices/list` - 获取设备列表
- `GET /api/devices/detail/{deviceId}` - 获取设备详情
- `POST /api/devices/control/{deviceId}` - 控制设备
- `GET /api/devices/logs` - 获取设备操作日志
- `GET /api/devices/types` - 获取设备类型列表

### 2.4 网络监控

**模块说明**：监控网络状态、带宽使用和网络接口状态，提供网络性能分析。

**主要功能**：
- 获取网络状态（整体状态、互联网连接、局域网状态、WiFi状态）
- 获取带宽监控数据（上传/下载速度、延迟、丢包率）
- 获取网络接口状态（接口列表、带宽使用、错误率）
- 刷新网络状态
- 获取网络监控摘要

**API 接口**：
- `GET /api/network/monitor/status` - 获取网络状态
- `GET /api/network/monitor/bandwidth` - 获取带宽监控数据
- `GET /api/network/monitor/interfaces` - 获取网络接口状态
- `POST /api/network/monitor/refresh` - 刷新网络状态
- `GET /api/network/monitor/summary` - 获取网络监控摘要

### 2.5 命令管理

**模块说明**：管理命令的执行、队列和统计，支持命令的发送和监控。

**主要功能**：
- 发送命令到设备或系统
- 管理命令队列（添加、移除、查看）
- 获取命令执行统计（执行历史、类型统计、状态统计）
- 获取命令执行趋势
- 获取命令类型分析
- 获取命令执行效率分析
- 获取命令执行失败分析

**API 接口**：
- `POST /api/command/send` - 发送命令
- `GET /api/command/queue` - 获取命令队列
- `POST /api/command/queue/add` - 添加命令到队列
- `POST /api/command/queue/remove` - 从队列移除命令
- `GET /api/command/stats/overview` - 获取命令执行统计
- `GET /api/command/stats/trend` - 获取命令执行趋势
- `GET /api/command/stats/type-analysis` - 获取命令类型分析
- `GET /api/command/stats/efficiency` - 获取命令执行效率分析
- `GET /api/command/stats/failure-analysis` - 获取命令执行失败分析

### 2.6 MCP Agent 管理

**模块说明**：管理 MCP Agent 实例的生命周期和状态，支持多实例管理。

**主要功能**：
- 获取所有 MCP Agent 实例
- 获取单个 MCP Agent 实例详情
- 创建 MCP Agent 实例
- 更新 MCP Agent 实例
- 删除 MCP Agent 实例
- 启动/停止/重启 MCP Agent 实例
- 检查 MCP Agent 实例状态

**API 接口**：
- `GET /api/mcp/management/agents` - 获取所有 MCP Agent 实例
- `GET /api/mcp/management/agents/{agentId}` - 获取单个 MCP Agent 实例详情
- `POST /api/mcp/management/agents` - 创建 MCP Agent 实例
- `PUT /api/mcp/management/agents/{agentId}` - 更新 MCP Agent 实例
- `DELETE /api/mcp/management/agents/{agentId}` - 删除 MCP Agent 实例
- `POST /api/mcp/management/agents/{agentId}/start` - 启动 MCP Agent 实例
- `POST /api/mcp/management/agents/{agentId}/stop` - 停止 MCP Agent 实例
- `POST /api/mcp/management/agents/{agentId}/restart` - 重启 MCP Agent 实例
- `POST /api/mcp/management/agents/{agentId}/check` - 检查 MCP Agent 实例状态

### 2.7 终端管理

**模块说明**：管理终端设备的发现、注册和状态监控。

**主要功能**：
- 获取终端列表（支持按状态、类型、路由代理过滤）
- 获取终端详细信息
- 添加终端
- 删除终端
- 更新终端状态
- 发现终端
- 获取终端发现状态
- 获取终端统计信息

**API 接口**：
- `GET /api/network/endagent/list` - 获取终端列表
- `GET /api/network/endagent/detail/{agentId}` - 获取终端详情
- `POST /api/network/endagent/add` - 添加终端
- `DELETE /api/network/endagent/delete/{agentId}` - 删除终端
- `PUT /api/network/endagent/update/{agentId}` - 更新终端状态
- `POST /api/network/endagent/discover` - 发现终端
- `GET /api/network/endagent/discover/status` - 获取终端发现状态
- `GET /api/network/endagent/stats` - 获取终端统计信息

### 2.8 路由管理

**模块说明**：管理网络路由的配置和状态，支持路由的发现和监控。

**主要功能**：
- 获取路由列表
- 获取路由详细信息
- 添加路由
- 删除路由
- 更新路由状态
- 发现路由
- 获取路由统计信息

**API 接口**：
- `GET /api/network/route/list` - 获取路由列表
- `GET /api/network/route/detail/{routeId}` - 获取路由详情
- `POST /api/network/route/add` - 添加路由
- `DELETE /api/network/route/delete/{routeId}` - 删除路由
- `PUT /api/network/route/update/{routeId}` - 更新路由状态
- `POST /api/network/route/discover` - 发现路由
- `GET /api/network/route/stats` - 获取路由统计信息

### 2.9 安全管理

**模块说明**：提供网络安全状态监控和安全事件管理，确保网络环境的安全。

**主要功能**：
- 获取安全状态（整体安全评分、威胁检测、安全事件）
- 获取安全事件列表
- 获取安全策略
- 更新安全策略
- 获取安全统计信息

**API 接口**：
- `GET /api/security/status` - 获取安全状态
- `GET /api/security/events` - 获取安全事件列表
- `GET /api/security/policies` - 获取安全策略
- `PUT /api/security/policies` - 更新安全策略
- `GET /api/security/stats` - 获取安全统计信息

### 2.10 系统管理

**模块说明**：管理系统配置和状态，提供系统级别的监控和控制。

**主要功能**：
- 获取系统配置（基础配置、网络配置、服务配置等）
- 更新系统配置
- 重置系统配置
- 获取系统配置状态
- 获取系统状态（CPU、内存、磁盘、网络等）
- 获取服务状态

**API 接口**：
- `GET /api/system/config/all` - 获取所有系统配置
- `GET /api/system/config/{configType}` - 获取指定类型的系统配置
- `PUT /api/system/config/{configType}` - 更新指定类型的系统配置
- `POST /api/system/config/reset/{configType}` - 重置指定类型的系统配置
- `GET /api/system/config/status` - 获取系统配置状态
- `GET /api/system/status` - 获取系统状态
- `GET /api/service/status` - 获取服务状态

### 2.11 日志管理

**模块说明**：管理系统日志和操作日志，提供日志的查询和分析功能。

**主要功能**：
- 获取系统日志
- 获取操作日志
- 清理日志
- 导出日志
- 生成日志上下文

**API 接口**：
- `GET /api/log/system` - 获取系统日志
- `GET /api/log/operation` - 获取操作日志
- `POST /api/log/clear` - 清理日志
- `GET /api/log/export` - 导出日志
- `GET /api/log/context` - 生成日志上下文

### 2.12 网络设备管理

**模块说明**：管理网络设备的状态和配置，支持网络设备的监控和控制。

**主要功能**：
- 获取网络设备列表
- 获取网络设备详细信息
- 控制网络设备
- 获取网络设备统计信息

**API 接口**：
- `GET /api/network/device/list` - 获取网络设备列表
- `GET /api/network/device/detail/{deviceId}` - 获取网络设备详情
- `POST /api/network/device/control/{deviceId}` - 控制网络设备
- `GET /api/network/device/stats` - 获取网络设备统计信息

### 2.13 网络配置管理

**模块说明**：管理网络配置参数，支持网络设置的调整和优化。

**主要功能**：
- 获取网络配置
- 更新网络配置
- 重置网络配置
- 获取网络配置统计信息

**API 接口**：
- `GET /api/network/config` - 获取网络配置
- `PUT /api/network/config` - 更新网络配置
- `POST /api/network/config/reset` - 重置网络配置
- `GET /api/network/config/stats` - 获取网络配置统计信息

### 2.14 网络链接管理

**模块说明**：管理网络链接的状态和配置，支持链接的监控和管理。

**主要功能**：
- 获取网络链接列表
- 获取网络链接详细信息
- 添加网络链接
- 删除网络链接
- 更新网络链接状态
- 获取网络链接统计信息

**API 接口**：
- `GET /api/network/link/list` - 获取网络链接列表
- `GET /api/network/link/detail/{linkId}` - 获取网络链接详情
- `POST /api/network/link/add` - 添加网络链接
- `DELETE /api/network/link/delete/{linkId}` - 删除网络链接
- `PUT /api/network/link/update/{linkId}` - 更新网络链接状态
- `GET /api/network/link/stats` - 获取网络链接统计信息

### 2.15 场景管理

**模块说明**：管理自动化场景，支持基于条件触发的设备控制和操作。

**主要功能**：
- 获取场景列表
- 获取场景详细信息
- 创建场景
- 删除场景
- 更新场景
- 执行场景
- 获取场景统计信息

**API 接口**：
- `GET /api/scenario/list` - 获取场景列表
- `GET /api/scenario/detail/{scenarioId}` - 获取场景详情
- `POST /api/scenario/create` - 创建场景
- `DELETE /api/scenario/delete/{scenarioId}` - 删除场景
- `PUT /api/scenario/update/{scenarioId}` - 更新场景
- `POST /api/scenario/execute/{scenarioId}` - 执行场景
- `GET /api/scenario/stats` - 获取场景统计信息

### 2.16 工具管理

**模块说明**：管理系统工具和实用程序，提供各种网络和系统工具的访问。

**主要功能**：
- 获取工具列表
- 执行工具
- 获取工具执行结果
- 管理工具配置

**API 接口**：
- `GET /api/tool/list` - 获取工具列表
- `POST /api/tool/execute/{toolId}` - 执行工具
- `GET /api/tool/result/{executionId}` - 获取工具执行结果
- `PUT /api/tool/config/{toolId}` - 更新工具配置

## 3. API 接口规范

### 3.1 通用响应格式

所有 API 接口返回统一的 JSON 格式响应：

```json
{
  "status": "success",
  "message": "操作成功的消息",
  "data": { /* 响应数据 */ },
  "code": 200,
  "timestamp": 1620000000000
}
```

错误响应格式：

```json
{
  "status": "error",
  "message": "错误消息",
  "code": 500,
  "timestamp": 1620000000000
}
```

### 3.2 HTTP 方法

- `GET` - 获取资源
- `POST` - 创建资源或执行操作
- `PUT` - 更新资源
- `DELETE` - 删除资源

### 3.3 认证与授权

API 接口支持基于令牌的认证机制，通过 HTTP 头部 `Authorization` 字段传递认证令牌。

### 3.4 速率限制

API 接口实施速率限制，防止滥用和过度请求。默认限制为每秒 100 个请求。

## 4. 系统要求

### 4.1 硬件要求

- CPU: 2 核或以上
- 内存: 4GB 或以上
- 磁盘: 20GB 可用空间或以上
- 网络: 千兆以太网或以上

### 4.2 软件要求

- Java: JDK 8 或以上
- 操作系统: Windows Server 2016+, Linux (Ubuntu 18.04+, CentOS 7+)
- 数据库: 可选，支持 MySQL 5.7+, PostgreSQL 10+
- Web 服务器: 内置 Tomcat

## 5. 部署与安装

### 5.1 安装步骤

1. 下载 MCP Agent 安装包
2. 解压安装包到目标目录
3. 配置系统环境变量（JAVA_HOME）
4. 运行启动脚本：`./start.sh` (Linux) 或 `start.bat` (Windows)
5. 访问 Web 控制台：`http://localhost:8080`

### 5.2 配置文件

主要配置文件位于 `conf/` 目录：
- `application.properties` - 应用基本配置
- `system-config.properties` - 系统配置
- `logback.xml` - 日志配置

## 6. 安全注意事项

1. **认证安全**：确保使用强密码并定期更换
2. **网络安全**：限制 API 访问 IP，使用 HTTPS
3. **权限控制**：实施最小权限原则
4. **日志审计**：定期检查系统日志，监控异常操作
5. **漏洞修复**：及时更新系统和依赖库

## 7. 故障排除

### 7.1 常见问题

| 问题 | 可能原因 | 解决方案 |
|------|----------|----------|
| API 访问失败 | 网络连接问题 | 检查网络连接和防火墙设置 |
| 设备控制失败 | 设备离线或无响应 | 检查设备连接状态和电源 |
| 系统性能下降 | 资源不足 | 增加系统资源或优化配置 |
| 日志过大 | 日志级别设置过高 | 调整日志级别为 INFO 或 WARN |

### 7.2 诊断工具

- `GET /api/health/overview` - 检查系统健康状态
- `GET /api/log/system` - 查看系统日志
- `POST /api/service/check-all` - 检查所有服务状态

## 8. 总结

MCP Agent 是一个功能全面、易于使用的网络管理和设备控制平台，提供了丰富的 API 接口和管理功能。它不仅支持企业级网络环境的管理需求，也适用于家庭网络的智能化控制。通过 MCP Agent，用户可以实现网络设备的集中管理、实时监控和智能化控制，提高网络管理效率和安全性。

### 8.1 核心优势

- **全面的功能**：涵盖网络管理、设备控制、安全监控等多个领域
- **易于集成**：提供标准化的 RESTful API 接口
- **高度可扩展**：模块化设计，支持功能扩展和定制
- **实时监控**：提供实时的网络和设备状态监控
- **智能化控制**：支持设备的自动化控制和场景管理
- **安全可靠**：内置安全监控和告警机制

MCP Agent 为用户提供了一个统一的平台，简化了网络管理和设备控制的复杂性，是现代网络环境中不可或缺的管理工具。